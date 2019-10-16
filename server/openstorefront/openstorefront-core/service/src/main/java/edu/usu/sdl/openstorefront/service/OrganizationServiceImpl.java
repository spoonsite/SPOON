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

import edu.usu.sdl.openstorefront.common.exception.AttachedReferencesException;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.api.OrganizationService;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOptionBuilder;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
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
import edu.usu.sdl.openstorefront.core.entity.SecurityPolicy;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.model.OrgReference;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

	private static final Logger LOG = Logger.getLogger(OrganizationServiceImpl.class.getName());
	private static final int MAX_REFERENCE_NAME_DETAIL = 80;

	@Override
	public void addOrganization(String organizationName)
	{
		if (StringUtils.isNotBlank(organizationName)) {
			Organization organizationExisting = getPersistenceService().findById(Organization.class, Organization.toKey(organizationName));
			if (organizationExisting == null) {
				Organization organizationNew = new Organization();
				organizationNew.setName(organizationName);
				saveOrganization(organizationNew);
			}
		}
	}

	@Override
	public Organization saveOrganization(Organization organization)
	{
		Objects.requireNonNull(organization, "Organization is required");
		Objects.requireNonNull(organization.getName(), "Organization name is required");

		Organization savedOrganization;

		Organization organizationExisting = getPersistenceService().findById(Organization.class, organization.getOrganizationId());
		if (organizationExisting == null) {
			organizationExisting = getPersistenceService().findById(Organization.class, organization.nameToKey());
		}

		if (organizationExisting != null) {

			if (organizationExisting.getName().equals(organization.getName()) == false) {
				//update associated data
				updateOrganizationOnEntity(new Component(), organizationExisting.getName(), organization);
				updateOrganizationOnEntity(new ComponentContact(), organizationExisting.getName(), organization);
				updateOrganizationOnEntity(new Contact(), organizationExisting.getName(), organization);
				int usersChanged = updateOrganizationOnEntity(new UserProfile(), organizationExisting.getName(), organization);

				if (usersChanged > 0) {
					SecurityPolicy securityPolicy = getSecurityService().getSecurityPolicy();
					if (Convert.toBoolean(securityPolicy.getDisableUserInfoEdit()) == false) {
						LOG.log(Level.WARNING, "Users are being managed externally. So they may change or recreate groups if they don't match after the user sync.  User were moved in this application only.");
					}
				}
				updateOrganizationOnEntity(new ComponentReview(), organizationExisting.getName(), organization);
				updateOrganizationOnEntity(new ComponentQuestion(), organizationExisting.getName(), organization);
				updateOrganizationOnEntity(new ComponentQuestionResponse(), organizationExisting.getName(), organization);
				clearOrganizationCaches();
			}
			savedOrganization = getRepoFactory().getOrganizationRepo().handleOrganizationUpdate(getPersistenceService(), organizationExisting, organization);
		} else {
			organization.setOrganizationId(organization.nameToKey());
			organization.populateBaseCreateFields();
			savedOrganization = getPersistenceService().persist(organization);
		}

		return savedOrganization;
	}

	@Override
	public void saveOrganizationLogo(Organization organization, InputStream fileInput)
	{
		Objects.requireNonNull(organization);
		Objects.requireNonNull(organization.getOrganizationId());
		Objects.requireNonNull(organization.getName());
		Objects.requireNonNull(organization.getLogoOriginalFileName());
		Objects.requireNonNull(organization.getLogoMimeType());
		Objects.requireNonNull(fileInput);

		Organization savedOrganization = getPersistenceService().findById(Organization.class, organization.getOrganizationId());
		savedOrganization.setLogoMimeType(organization.getLogoMimeType());
		savedOrganization.setLogoOriginalFileName(organization.getLogoOriginalFileName());
		savedOrganization.setLogoFileName(organization.nameToKey());
		savedOrganization.populateBaseUpdateFields();
		getPersistenceService().persist(savedOrganization);

		try (InputStream in = fileInput) {
			Files.copy(in, savedOrganization.pathToLogo(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to store organization logo.", "Contact System Admin.  Check file permissions and disk space ", ex);
		}
	}

	private void clearOrganizationCaches()
	{
		OSFCacheManager.getContactCache().removeAll();
		OSFCacheManager.getComponentCache().removeAll();
		OSFCacheManager.getSearchCache().removeAll();
	}

	@Override
	public void extractOrganizations()
	{
		extractOrg(UserProfile.class);
		extractOrg(Component.class);
		extractOrg(ComponentContact.class);
		extractOrg(Contact.class);
		extractOrg(ComponentReview.class);
		extractOrg(ComponentQuestion.class);
		extractOrg(ComponentQuestionResponse.class);
	}

	private void extractOrg(Class organizationClass)
	{
		try {
			LOG.log(Level.FINE, MessageFormat.format("Extracting Orgs from {0}", organizationClass.getSimpleName()));

			StandardEntity standardEntityExample = (StandardEntity) organizationClass.newInstance();
			QueryByExample<StandardEntity> queryByExample = new QueryByExample<>(standardEntityExample);
			queryByExample.setDistinctField(OrganizationModel.FIELD_ORGANIZATION);

			List<StandardEntity> orgModels = getPersistenceService().queryByExample(queryByExample);
			for (StandardEntity entity : orgModels) {
				addOrganization(((OrganizationModel) entity).getOrganization());
			}
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new OpenStorefrontRuntimeException("Unable to create an Organization Model (sub)-class", "Check class passed: " + organizationClass.getName(), ex);
		}
	}

	@Override
	public void mergeOrganizations(String targetOrganizationId, String toMergeOrganizationId)
	{
		Objects.requireNonNull(targetOrganizationId);
		Objects.requireNonNull(toMergeOrganizationId);

		Organization organizationTarget = getPersistenceService().findById(Organization.class, targetOrganizationId);
		Organization organizationMerge = getPersistenceService().findById(Organization.class, toMergeOrganizationId);

		if (organizationTarget != null) {
			if (organizationMerge != null) {

				//Note: this is internal transformation so no need to update indexes or alert users
				updateOrganizationOnEntity(new Component(), organizationMerge.getName(), organizationTarget);
				updateOrganizationOnEntity(new ComponentContact(), organizationMerge.getName(), organizationTarget);
				updateOrganizationOnEntity(new Contact(), organizationMerge.getName(), organizationTarget);
				int usersChanged = updateOrganizationOnEntity(new UserProfile(), organizationMerge.getName(), organizationTarget);

				if (usersChanged > 0) {
					SecurityPolicy securityPolicy = getSecurityService().getSecurityPolicy();
					if (Convert.toBoolean(securityPolicy.getDisableUserInfoEdit()) == false) {
						LOG.log(Level.WARNING, "Users are being managed externally. So they may change or recreate groups if they don't match after the user sync.  Users were moved in this application only.");
					}
				}

				updateOrganizationOnEntity(new ComponentReview(), organizationMerge.getName(), organizationTarget);
				updateOrganizationOnEntity(new ComponentQuestion(), organizationMerge.getName(), organizationTarget);
				updateOrganizationOnEntity(new ComponentQuestionResponse(), organizationMerge.getName(), organizationTarget);
				clearOrganizationCaches();

				getPersistenceService().delete(organizationMerge);

			} else {
				throw new OpenStorefrontRuntimeException("Organization to Merge does not exist.", "Check data and refresh to make sure merge organization still exists");
			}
		} else {
			throw new OpenStorefrontRuntimeException("Target Organization does not exist.", "Check data and refresh to make sure target still exists");
		}
	}

	private <T extends BaseEntity> int updateOrganizationOnEntity(T entityExample, String existingOrgName, Organization organizationTarget)
	{
		int changedRecords = 0;
		if (entityExample instanceof OrganizationModel) {
			LOG.log(Level.FINE, MessageFormat.format("Updating organizations on {0}", entityExample.getClass().getSimpleName()));
			((OrganizationModel) entityExample).setOrganization(existingOrgName);

			@SuppressWarnings("unchecked")
			List<T> entities = entityExample.findByExampleProxy();
			for (T entity : entities) {
				((OrganizationModel) entity).setOrganization(organizationTarget.getName());
				((StandardEntity) entity).populateBaseUpdateFields();
				getPersistenceService().persist(entity);
				changedRecords++;
			}
			LOG.log(Level.FINE, MessageFormat.format("Updated: ", entities.size()));
		} else {
			throw new OpenStorefrontRuntimeException("Entity does not implement Organization", "Programming error");
		}
		return changedRecords;
	}

	@Override
	public List<OrgReference> findReferences(String organization, boolean onlyActive, boolean onlyApprovedComponents)
	{
		List<OrgReference> references = new ArrayList<>();

		if (StringUtils.isNotBlank(organization)) {
			Organization organizationEntity = getPersistenceService().findById(Organization.class, organization);
			if (organizationEntity == null) {
				organizationEntity = new Organization();
				organizationEntity.setName(organization);
				organizationEntity = organizationEntity.find();
			}

			if (organizationEntity != null) {
				organization = organizationEntity.getName();
			} else {
				LOG.log(Level.WARNING, MessageFormat.format("Unable to find organization with key or label:  {0}", organization));
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
		references.addAll(findOrgReferences(new ComponentContact(), organization, (ComponentContact entity) -> {
			OrgReference reference = new OrgReference();
			reference.setActiveStatus(entity.getActiveStatus());
			reference.setComponentId(entity.getComponentId());
			reference.setComponentName(getComponentService().getComponentName(entity.getComponentId()));
			reference.setComponentApproveStatus(getComponentService().getComponentApprovalStatus(entity.getComponentId()));
			reference.setReferenceId(entity.getContactId());
			reference.setReferenceName(String.join(" ",
					StringUtils.defaultString(StringProcessor.enclose(entity.getSecurityMarkingType())),
					StringUtils.defaultString(TranslateUtil.translate(ContactType.class, entity.getContactType())),
					StringUtils.defaultString(entity.getFirstName()),
					StringUtils.defaultString(entity.getLastName()),
					StringUtils.defaultString(entity.getEmail())
			));
			return reference;
		}));

		List<OrgReference> globalContacts = findOrgReferences(new Contact(), organization, (Contact entity) -> {
			OrgReference reference = new OrgReference();
			reference.setActiveStatus(entity.getActiveStatus());
			reference.setReferenceId(entity.getContactId());
			reference.setReferenceName(String.join(" ",
					StringUtils.defaultString(StringProcessor.enclose(entity.getSecurityMarkingType())),
					StringUtils.defaultString(entity.getFirstName()),
					StringUtils.defaultString(entity.getLastName()),
					StringUtils.defaultString(entity.getEmail())
			));
			return reference;
		});

		//filter out duplications (Contacts vs Component Contacts ...keep CompoenentContact)
		Set<String> existingReferenceId = new HashSet<>();
		for (OrgReference orgReference : references) {
			existingReferenceId.add(orgReference.getReferenceId());
		}
		for (OrgReference orgReference : globalContacts) {
			if (existingReferenceId.contains(orgReference.getReferenceId()) == false) {
				references.add(orgReference);
			}
		}

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
			//Case in-senstive
			entity.setOrganization(organization.toLowerCase());
			QueryByExample<BaseEntity> queryByExample = new QueryByExample<>((BaseEntity) entity);
			queryByExample.getFieldOptions().put(OrganizationModel.FIELD_ORGANIZATION, new GenerateStatementOptionBuilder().setMethod(GenerateStatementOption.METHOD_LOWER_CASE).build());

			entities = getPersistenceService().queryByExample(queryByExample);
		} else {
			entities = getRepoFactory().getOrganizationRepo().findReferencesNoOrg(entity);
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
			Organization organizationFound = getPersistenceService().findById(Organization.class, organizationId);
			if (organizationFound != null) {
				deleteOrganizationLogo(organizationId);
				getPersistenceService().delete(organizationFound);
			}
		} else {
			throw new AttachedReferencesException();
		}
	}

	@Override
	public void deleteOrganizationLogo(String organizationId)
	{
		Organization organizationFound = getPersistenceService().findById(Organization.class, organizationId);
		if (organizationFound != null) {
			Path path = organizationFound.pathToLogo();
			if (path != null) {
				if (path.toFile().exists()) {
					if (!path.toFile().delete()) {
						LOG.log(Level.WARNING, MessageFormat.format("Unable to delete logo. File might be in use. Path: {0}", path.toString()));
					}
				}
			}
			organizationFound.setLogoFileName(null);
			organizationFound.setLogoMimeType(null);
			organizationFound.setLogoOriginalFileName(null);
			organizationFound.populateBaseUpdateFields();
			getPersistenceService().persist(organizationFound);
		} else {
			LOG.log(Level.WARNING, MessageFormat.format("Unable to find organization. Check Id: ", organizationId));
		}
	}

}
