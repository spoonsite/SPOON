/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.usu.sdl.openstorefront.service;

import com.orientechnologies.orient.core.record.impl.ODocument;
import edu.usu.sdl.openstorefront.common.exception.AttachedReferencesException;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.api.OrganizationService;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.Contact;
import edu.usu.sdl.openstorefront.core.entity.ContactType;
import edu.usu.sdl.openstorefront.core.entity.Organization;
import edu.usu.sdl.openstorefront.core.entity.OrganizationModel;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.model.OrgReference;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * Handles Organizations
 *
 * @author dshurtleff
 */
public class OrganizationServiceImpl
		extends ServiceProxy
		implements OrganizationService
{

	private static final Logger log = Logger.getLogger(OrganizationServiceImpl.class.getName());
	private static final int MAX_REFERENCE_NAME_DETAIL = 80;

	@Override
	public void addOrganization(String organizationName)
	{
		if (StringUtils.isNotBlank(organizationName)) {
			Organization organizationExisting = persistenceService.findById(Organization.class, Organization.toKey(organizationName));
			if (organizationExisting == null) {
				Organization organizationNew = new Organization();
				organizationNew.setName(organizationName);
				saveOrganization(organizationNew);
			}
		}
	}

	@Override
	public void saveOrganization(Organization organization)
	{
		Objects.requireNonNull(organization, "Organization is required");
		Objects.requireNonNull(organization.getName(), "Organization name is required");

		Organization organizationExisting = persistenceService.findById(Organization.class, organization.getOrganizationId());
		if (organizationExisting == null) {
			organizationExisting = persistenceService.findById(Organization.class, organization.nameToKey());
		}

		if (organizationExisting != null) {

			if (organizationExisting.getName().equals(organization.getName()) == false) {
				//update associated data
				updateOrganizationOnEntity(new Component(), organizationExisting.getName(), organization);
				updateOrganizationOnEntity(new ComponentContact(), organizationExisting.getName(), organization);
				updateOrganizationOnEntity(new UserProfile(), organizationExisting.getName(), organization);
				updateOrganizationOnEntity(new ComponentReview(), organizationExisting.getName(), organization);
				updateOrganizationOnEntity(new ComponentQuestion(), organizationExisting.getName(), organization);
				updateOrganizationOnEntity(new ComponentQuestionResponse(), organizationExisting.getName(), organization);
			}

			organizationExisting.updateFields(organization);
			persistenceService.persist(organizationExisting);
		} else {
			organization.setOrganizationId(organization.nameToKey());
			organization.populateBaseCreateFields();
			persistenceService.persist(organization);
		}

	}

	@Override
	public void extractOrganizations()
	{
		extractOrg(UserProfile.class);
		extractOrg(Component.class);
		extractOrg(ComponentContact.class);
		extractOrg(ComponentReview.class);
		extractOrg(ComponentQuestion.class);
		extractOrg(ComponentQuestionResponse.class);
	}

	private void extractOrg(Class organizationClass)
	{
		log.log(Level.FINE, MessageFormat.format("Extracting Orgs from {0}", organizationClass.getSimpleName()));
		List<ODocument> documents = persistenceService.query("Select DISTINCT(organization) as organization from " + organizationClass.getSimpleName(), new HashMap<>());
		log.log(Level.FINE, MessageFormat.format("Found: {0}", documents.size()));
		documents.forEach(document -> {
			String org = document.field("organization");
			addOrganization(org);
		});
	}

	@Override
	public void mergeOrganizations(String targetOrganizationId, String toMergeOrganizationId)
	{
		Objects.requireNonNull(targetOrganizationId);
		Objects.requireNonNull(toMergeOrganizationId);

		Organization organizationTarget = persistenceService.findById(Organization.class, targetOrganizationId);
		Organization organizationMerge = persistenceService.findById(Organization.class, toMergeOrganizationId);

		if (organizationTarget != null) {
			if (organizationMerge != null) {

				//Note: this is internal transformaion so no need to update indexes or alert users
				updateOrganizationOnEntity(new Component(), organizationMerge.getName(), organizationTarget);
				updateOrganizationOnEntity(new ComponentContact(), organizationMerge.getName(), organizationTarget);
				updateOrganizationOnEntity(new UserProfile(), organizationMerge.getName(), organizationTarget);
				updateOrganizationOnEntity(new ComponentReview(), organizationMerge.getName(), organizationTarget);
				updateOrganizationOnEntity(new ComponentQuestion(), organizationMerge.getName(), organizationTarget);
				updateOrganizationOnEntity(new ComponentQuestionResponse(), organizationMerge.getName(), organizationTarget);

				persistenceService.delete(organizationMerge);

			} else {
				throw new OpenStorefrontRuntimeException("Organization to Merge does not exist.", "Check data and refresh to make sure merge organization still exists");
			}
		} else {
			throw new OpenStorefrontRuntimeException("Target Organization does not exist.", "Check data and refresh to make sure target still exists");
		}
	}

	private <T extends BaseEntity> void updateOrganizationOnEntity(T entityExample, String existingOrgName, Organization organizationTarget)
	{
		if (entityExample instanceof OrganizationModel) {
			log.log(Level.FINE, MessageFormat.format("Updating organizations on {0}", entityExample.getClass().getSimpleName()));
			((OrganizationModel) entityExample).setOrganization(existingOrgName);

			List<T> entities = entityExample.findByExampleProxy();
			entities.forEach(entity -> {
				((OrganizationModel) entity).setOrganization(organizationTarget.getName());
				((StandardEntity) entity).populateBaseUpdateFields();
				persistenceService.persist(entity);
			});
			log.log(Level.FINE, MessageFormat.format("Updated: ", entities.size()));
		} else {
			throw new OpenStorefrontRuntimeException("Entity does not implement Organization", "Programming error");
		}
	}

	@Override
	public List<OrgReference> findReferences(String organization, boolean onlyActive, boolean onlyApprovedComponents)
	{
		List<OrgReference> references = new ArrayList<>();

		if (StringUtils.isNotBlank(organization)) {
			Organization organizationEntity = persistenceService.findById(Organization.class, organization);
			if (organizationEntity == null) {
				organizationEntity = new Organization();
				organizationEntity.setName(organization);
				organizationEntity = organizationEntity.find();
			}

			if (organizationEntity != null) {
				organization = organizationEntity.getName();
			} else {
				log.log(Level.WARNING, MessageFormat.format("Unable to find organization with key or label:  {0}", organization));
				return references;
			}
		}

		references.addAll(findOrgReferences(new Component(), organization, (Component entity) -> {
			OrgReference reference = new OrgReference();
			reference.setActiveStatus(entity.getActiveStatus());
			reference.setComponentApproveStatus(entity.getApprovalState());
			reference.setReferenceId(entity.getComponentId());
			reference.setReferenceName(String.join(" ",
					StringUtils.defaultString(StringProcessor.enclose(entity.getSecurityMarkingType())),
					getComponentService().getComponentName(entity.getComponentId())
			));
			return reference;
		}));
		references.addAll(findOrgReferences(new Contact(), organization, (Contact entity) -> {
			OrgReference reference = new OrgReference();
			ComponentContact componentContact = new ComponentContact();
			componentContact.setContactId(entity.getContactId());
			componentContact = (ComponentContact) componentContact.find();
			
			reference.setActiveStatus(entity.getActiveStatus());
			reference.setComponentId(componentContact.getComponentId());
			reference.setComponentName(getComponentService().getComponentName(componentContact.getComponentId()));
			reference.setComponentApproveStatus(getComponentService().getComponentApprovalStatus(componentContact.getComponentId()));
			reference.setReferenceId(entity.getContactId());
			reference.setReferenceName(String.join(" ",
					StringUtils.defaultString(StringProcessor.enclose(entity.getSecurityMarkingType())),
					StringUtils.defaultString(TranslateUtil.translate(ContactType.class, componentContact.getContactType())),
					StringUtils.defaultString(entity.getFirstName()),
					StringUtils.defaultString(entity.getLastName()),
					StringUtils.defaultString(entity.getEmail())
			));
			return reference;
		}));

		references.addAll(findOrgReferences(new ComponentReview(), organization, (ComponentReview entity) -> {
			OrgReference reference = new OrgReference();
			reference.setActiveStatus(entity.getActiveStatus());
			reference.setComponentId(entity.getComponentId());
			reference.setComponentName(getComponentService().getComponentName(entity.getComponentId()));
			reference.setComponentApproveStatus(getComponentService().getComponentApprovalStatus(entity.getComponentId()));
			reference.setReferenceId(entity.getComponentReviewId());
			reference.setReferenceName(String.join("-",
					StringUtils.defaultString(entity.getCreateUser()),
					StringUtils.defaultString(StringProcessor.enclose(entity.getSecurityMarkingType())),
					StringUtils.defaultString(StringProcessor.ellipseString(entity.getTitle(), MAX_REFERENCE_NAME_DETAIL))
			));
			return reference;
		}));
		references.addAll(findOrgReferences(new ComponentQuestion(), organization, (ComponentQuestion entity) -> {
			OrgReference reference = new OrgReference();
			reference.setActiveStatus(entity.getActiveStatus());
			reference.setComponentId(entity.getComponentId());
			reference.setComponentName(getComponentService().getComponentName(entity.getComponentId()));
			reference.setComponentApproveStatus(getComponentService().getComponentApprovalStatus(entity.getComponentId()));
			reference.setReferenceId(entity.getQuestionId());
			reference.setReferenceName(String.join("-",
					StringUtils.defaultString(entity.getCreateUser()),
					String.join(" ",
							StringUtils.defaultString(StringProcessor.enclose(entity.getSecurityMarkingType())),
							StringUtils.defaultString(StringProcessor.ellipseString(entity.getQuestion(), MAX_REFERENCE_NAME_DETAIL)))
			));
			return reference;
		}));
		references.addAll(findOrgReferences(new ComponentQuestionResponse(), organization, (ComponentQuestionResponse entity) -> {
			OrgReference reference = new OrgReference();
			reference.setActiveStatus(entity.getActiveStatus());
			reference.setComponentId(entity.getComponentId());
			reference.setComponentName(getComponentService().getComponentName(entity.getComponentId()));
			reference.setComponentApproveStatus(getComponentService().getComponentApprovalStatus(entity.getComponentId()));
			reference.setReferenceId(entity.getResponseId());
			reference.setReferenceName(String.join("-",
					StringUtils.defaultString(entity.getCreateUser()),
					String.join(" ",
							StringUtils.defaultString(StringProcessor.enclose(entity.getSecurityMarkingType())),
							StringUtils.defaultString(StringProcessor.ellipseString(entity.getResponse(), MAX_REFERENCE_NAME_DETAIL)))
			));
			return reference;
		}));
		references.addAll(findOrgReferences(new UserProfile(), organization, (UserProfile entity) -> {
			OrgReference reference = new OrgReference();
			reference.setActiveStatus(entity.getActiveStatus());
			reference.setReferenceId(entity.getUsername());
			reference.setReferenceName(String.join(" ",
					StringUtils.defaultString(entity.getFirstName()),
					StringUtils.defaultString(entity.getLastName()),
					StringUtils.defaultString(entity.getEmail())
			));
			return reference;
		}));

		if (onlyActive) {
			references = references.stream().filter(r -> StandardEntity.ACTIVE_STATUS.equals(r.getActiveStatus())).collect(Collectors.toList());
		}

		if (onlyApprovedComponents) {
			references = references.stream().filter(r -> r.getComponentApproveStatus() == null || ApprovalStatus.APPROVED.equals(r.getComponentApproveStatus()))
					.collect(Collectors.toList());
		}

		return references;
	}

	private <T extends OrganizationModel> List<OrgReference> findOrgReferences(T entity, String organization, OrgReference.ReferenceTranformer<T> tranformer)
	{
		List<OrgReference> references = new ArrayList<>();
		List<T> entities;
		if (StringUtils.isNotBlank(organization)) {
			entity.setOrganization(organization);
			entities = ((BaseEntity) entity).findByExample();

		} else {
			//Search for records with no org
			String query = "select from " + entity.getClass().getSimpleName() + " where organization is null ";
			entities = persistenceService.query(query, new HashMap<>());
		}
		entities.forEach(entityFound -> {
			OrgReference reference = tranformer.transform(entityFound);
			reference.setReferenceType(entity.getClass().getSimpleName());
			references.add(reference);
		});

		return references;
	}

	@Override
	public void removeOrganization(String organizationId) throws AttachedReferencesException
	{
		List<OrgReference> references = findReferences(organizationId, false, false);
		if (references.isEmpty()) {
			Organization organizationFound = persistenceService.findById(Organization.class, organizationId);
			if (organizationFound != null) {
				persistenceService.delete(organizationFound);
			}
		} else {
			throw new AttachedReferencesException();
		}
	}

}
