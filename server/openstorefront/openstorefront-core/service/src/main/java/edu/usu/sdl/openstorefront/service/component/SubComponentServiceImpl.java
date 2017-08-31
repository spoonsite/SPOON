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
package edu.usu.sdl.openstorefront.service.component;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.BaseComponent;
import edu.usu.sdl.openstorefront.core.entity.ChangeLog;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.ComponentEvaluationSection;
import edu.usu.sdl.openstorefront.core.entity.ComponentExternalDependency;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.ComponentMetadata;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.entity.ComponentRelationship;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewCon;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewConPk;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewPro;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewProPk;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.Contact;
import edu.usu.sdl.openstorefront.core.entity.LoggableModel;
import edu.usu.sdl.openstorefront.core.entity.ReviewCon;
import edu.usu.sdl.openstorefront.core.entity.ReviewPro;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.core.model.BulkComponentAttributeChange;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.view.AttributeCodeSave;
import edu.usu.sdl.openstorefront.core.view.ComponentReviewProCon;
import edu.usu.sdl.openstorefront.core.view.ComponentReviewView;
import edu.usu.sdl.openstorefront.core.view.NewAttributeCode;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.ComponentServiceImpl;
import edu.usu.sdl.openstorefront.validation.CleanKeySanitizer;
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
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
import org.apache.commons.lang3.StringUtils;

/**
 * Handles sub-entities of components
 *
 * @author dshurtleff
 */
public class SubComponentServiceImpl
		extends BaseComponentServiceImpl
{

	private static final Logger LOG = Logger.getLogger(SubComponentServiceImpl.class.getName());

	public SubComponentServiceImpl(ComponentServiceImpl componentService)
	{
		super(componentService);
	}

	public <T extends BaseComponent> List<T> getBaseComponent(Class<T> subComponentClass, String componentId)
	{
		return getBaseComponent(subComponentClass, componentId, BaseComponent.ACTIVE_STATUS);
	}

	public <T extends BaseComponent> List<T> getBaseComponent(Class<T> subComponentClass, String componentId, String activeStatus)
	{
		try {
			T baseComponentExample = subComponentClass.newInstance();
			baseComponentExample.setComponentId(componentId);
			baseComponentExample.setActiveStatus(activeStatus);
			List<T> data = persistenceService.queryByExample(new QueryByExample(baseComponentExample));
			data = FilterEngine.filter(data);
			return data;
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
	}

	public <T extends BaseComponent> T deactivateBaseComponent(Class<T> subComponentClass, Object pk)
	{
		return deactivateBaseComponent(subComponentClass, pk, true, null);
	}

	public <T extends BaseComponent> T deactivateBaseComponent(Class<T> subComponentClass, Object pk, boolean updateComponentActivity, String updateUser)
	{
		T found = persistenceService.findById(subComponentClass, pk);
		if (found != null) {

			if (found instanceof LoggableModel) {
				componentService.getChangeLogService().logStatusChange(found, T.INACTIVE_STATUS);
			}

			found.setActiveStatus(T.INACTIVE_STATUS);
			found.setUpdateDts(TimeUtil.currentDate());
			if (StringUtils.isBlank(updateUser)) {
				updateUser = SecurityUtil.getCurrentUserName();
			}
			found.setUpdateUser(updateUser);
			persistenceService.persist(found);

			if (updateComponentActivity) {
				updateComponentLastActivity(found.getComponentId());
			}
		}
		return found;
	}

	public <T extends BaseComponent> T activateBaseComponent(Class<T> subComponentClass, Object pk)
	{
		T found = persistenceService.findById(subComponentClass, pk);
		if (found != null) {
			if (found instanceof LoggableModel) {
				componentService.getChangeLogService().logStatusChange(found, T.ACTIVE_STATUS);
			}

			found.setActiveStatus(T.ACTIVE_STATUS);
			found.populateBaseUpdateFields();
			persistenceService.persist(found);

			updateComponentLastActivity(found.getComponentId());
		}
		return found;
	}

	public <T extends BaseComponent> void deleteBaseComponent(Class<T> subComponentClass, Object pk)
	{
		deleteBaseComponent(subComponentClass, pk, true);
	}

	public <T extends BaseComponent> void deleteBaseComponent(Class<T> subComponentClass, Object pk, boolean updateComponentActivity)
	{
		T found = persistenceService.findById(subComponentClass, pk);
		if (found != null) {
			String componentId = found.getComponentId();
			if (found instanceof ComponentResource) {
				removeLocalResource((ComponentResource) found);
			}
			if (found instanceof ComponentMedia) {
				removeLocalMedia((ComponentMedia) found);
			}

			if (found instanceof LoggableModel) {
				componentService.getChangeLogService().removeEntityChange(subComponentClass, found);
			}

			persistenceService.delete(found);

			if (updateComponentActivity) {
				updateComponentLastActivity(componentId);
			}
		}
	}

	void removeLocalResource(ComponentResource componentResource)
	{
		//Note: this can't be rolled back
		Path path = componentResource.pathToResource();
		if (path != null) {
			if (path.toFile().exists()) {
				if (path.toFile().delete() == false) {
					LOG.log(Level.WARNING, MessageFormat.format("Unable to delete local component resource. Path: {0}", path.toString()));
				}
			}
		}
	}

	void removeLocalMedia(ComponentMedia componentMedia)
	{
		//Note: this can't be rolled back
		Path path = componentMedia.pathToMedia();
		if (path != null) {
			if (path.toFile().exists()) {
				if (path.toFile().delete() == false) {
					LOG.log(Level.WARNING, MessageFormat.format("Unable to delete local component media. Path: {0}", path.toString()));
				}
			}
		}
	}

	public <T extends BaseComponent> void deleteAllBaseComponent(Class<T> subComponentClass, String componentId)
	{
		deleteAllBaseComponent(subComponentClass, componentId, true);
	}

	public <T extends BaseComponent> void deleteAllBaseComponent(Class<T> subComponentClass, String componentId, boolean updateComponentActivity)
	{
		try {
			T example = subComponentClass.newInstance();
			example.setComponentId(componentId);

			if (subComponentClass.getName().equals(ComponentResource.class.getName())) {
				List<T> resources = persistenceService.queryByExample(example);
				resources.forEach(resource
						-> {
					removeLocalResource((ComponentResource) resource);
				});
			}
			if (subComponentClass.getName().equals(ComponentMedia.class.getName())) {
				List<T> media = persistenceService.queryByExample(example);
				media.forEach(mediaItem
						-> {
					removeLocalMedia((ComponentMedia) mediaItem);
				});
			}
			if (example instanceof LoggableModel) {
				componentService.getChangeLogService().removedAllEntityChange(example);
			}

			persistenceService.deleteByExample(example);

			if (updateComponentActivity) {
				updateComponentLastActivity(componentId);
			}
		} catch (InstantiationException | IllegalAccessException ex) {
			Logger.getLogger(ComponentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public List<ComponentAttribute> getAttributesByComponentId(String componentId)
	{
		ComponentAttribute example = new ComponentAttribute();
		ComponentAttributePk pk = new ComponentAttributePk();
		pk.setComponentId(componentId);
		example.setComponentAttributePk(pk);
		example.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
		return persistenceService.queryByExample(new QueryByExample(example));
	}

	public void saveComponentAttribute(ComponentAttribute attribute, boolean updateLastActivity)
	{
		saveComponentAttribute(attribute, updateLastActivity, false);
	}

	public void saveComponentAttribute(ComponentAttribute attribute, boolean updateLastActivity, boolean skipMissingAttribute)
	{
		Objects.requireNonNull(attribute, "Requires Component Attrubute");
		Objects.requireNonNull(attribute.getComponentAttributePk(), "Requires Component Attrubute PK");
		Objects.requireNonNull(attribute.getComponentAttributePk().getAttributeType(), "Requires Component Attrubute PK Attribute Type");
		Objects.requireNonNull(attribute.getComponentAttributePk().getAttributeCode(), "Requires Component Attrubute PK Attribute Code");
		Objects.requireNonNull(attribute.getComponentAttributePk().getComponentId(), "Requires Component Attrubute PK Component Id");

		ValidationResult validationResult = checkComponentAttribute(attribute);

		//Need to Santize the user code to match the attribute code after the code has been converted.
		CleanKeySanitizer sanitizer = new CleanKeySanitizer();
		String sanitizedCode = (String) sanitizer.santize(attribute.getComponentAttributePk().getAttributeCode());
		attribute.getComponentAttributePk().setAttributeCode(sanitizedCode);

		if (validationResult.valid()) {
			AttributeType type = componentService.getAttributeService().findType(attribute.getComponentAttributePk().getAttributeType());
			if (type.getAllowMultipleFlg() == false) {
				ComponentAttribute example = new ComponentAttribute();
				example.setComponentAttributePk(new ComponentAttributePk());
				example.getComponentAttributePk().setAttributeType(attribute.getComponentAttributePk().getAttributeType());
				example.getComponentAttributePk().setComponentId(attribute.getComponentAttributePk().getComponentId());
				persistenceService.deleteByExample(example);
			}

			ComponentAttribute oldAttribute = persistenceService.findById(ComponentAttribute.class, attribute.getComponentAttributePk());
			if (oldAttribute != null) {
				componentService.getChangeLogService().logFieldChange(oldAttribute, ChangeLog.PK_FIELD, oldAttribute.getComponentAttributePk().pkValue(), attribute.getComponentAttributePk().pkValue());

				oldAttribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
				oldAttribute.updateFields(attribute);
				persistenceService.persist(oldAttribute);
			} else {
				attribute.populateBaseCreateFields();
				persistenceService.persist(attribute);

				componentService.getChangeLogService().addEntityChange(attribute);
			}
			if (updateLastActivity) {
				updateComponentLastActivity(attribute.getComponentAttributePk().getComponentId());
			}

		} else {
			if (skipMissingAttribute) {
				LOG.log(Level.WARNING, MessageFormat.format("Unable to save attribute. {0}", validationResult.toString()));
			} else {
				throw new OpenStorefrontRuntimeException(validationResult.toString(), "Check data passed in.");
			}
		}
	}

	public void saveComponentContact(ComponentContact contact)
	{
		saveComponentContact(contact, true);
	}

	public void saveComponentContact(ComponentContact contact, boolean updateLastActivity)
	{
		Contact contactFull = componentService.getContactService().saveContact(contact.toContact());
		contact.setContactId(contactFull.getContactId());

		ComponentContact oldContact = persistenceService.findById(ComponentContact.class, contact.getComponentContactId());

		if (oldContact != null) {
			oldContact.updateFields(contact);
			persistenceService.persist(oldContact);
		} else {
			contact.setComponentContactId(persistenceService.generateId());
			contact.populateBaseCreateFields();
			persistenceService.persist(contact);

			componentService.getChangeLogService().addEntityChange(contact);
		}
		componentService.getOrganizationService().addOrganization(contact.getOrganization());

		if (updateLastActivity) {
			updateComponentLastActivity(contact.getComponentId());
		}
	}

	public void saveComponentDependency(ComponentExternalDependency dependency)
	{
		saveComponentDependency(dependency, true);
	}

	public void saveComponentDependency(ComponentExternalDependency dependency, boolean updateLastActivity)
	{
		ComponentExternalDependency oldDependency = persistenceService.findById(ComponentExternalDependency.class, dependency.getDependencyId());
		if (oldDependency != null) {
			oldDependency.updateFields(dependency);
			persistenceService.persist(oldDependency);
		} else {
			dependency.setDependencyId(persistenceService.generateId());
			dependency.populateBaseCreateFields();
			persistenceService.persist(dependency);

			componentService.getChangeLogService().addEntityChange(dependency);
		}
		if (updateLastActivity) {
			updateComponentLastActivity(dependency.getComponentId());
		}
	}

	public void saveComponentEvaluationSection(ComponentEvaluationSection section)
	{
		saveComponentEvaluationSection(section, true);
	}

	public void saveComponentEvaluationSection(List<ComponentEvaluationSection> sections)
	{
		sections.forEach(section
				-> {
			saveComponentEvaluationSection(section, false);
		});
		if (!sections.isEmpty()) {
			updateComponentLastActivity(sections.get(0).getComponentId());
		}
	}

	public void saveComponentEvaluationSection(ComponentEvaluationSection section, boolean updateLastActivity)
	{
		ComponentEvaluationSection oldSection = persistenceService.findById(ComponentEvaluationSection.class, section.getComponentEvaluationSectionPk());
		if (oldSection != null) {
			oldSection.updateFields(section);
			persistenceService.persist(oldSection);
		} else {
			section.populateBaseCreateFields();
			persistenceService.persist(section);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(section.getComponentId());
		}
	}

	public ComponentMedia saveComponentMedia(ComponentMedia media)
	{
		return saveComponentMedia(media, true);
	}

	ComponentMedia saveComponentMedia(ComponentMedia media, boolean updateLastActivity)
	{
		ComponentMedia newMedia;

		ComponentMedia oldMedia = persistenceService.findById(ComponentMedia.class, media.getComponentMediaId());
		if (oldMedia != null) {
			if (StringUtils.isNotBlank(media.getLink())) {
				removeLocalMedia(oldMedia);
			}
			oldMedia.updateFields(media);
			newMedia = persistenceService.persist(oldMedia);
		} else {
			if (StringUtils.isBlank(media.getComponentMediaId())) {
				media.setComponentMediaId(persistenceService.generateId());
			}

			//On a merge there may be a pre-existing file that needs to be rename
			if (StringUtils.isNotBlank(media.getFileName())) {
				if (media.getFileName().equals(media.getComponentMediaId()) == false) {
					Path oldPath = media.pathToMedia();
					if (oldPath != null && oldPath.toFile().exists()) {
						try {
							Files.move(oldPath, oldPath.resolveSibling(media.getComponentMediaId()));
						} catch (IOException ioe) {
							throw new OpenStorefrontRuntimeException("Failed to rename media; trying to re-point record.", "Download media; delete original media record and then re-upload.", ioe);
						}
						media.setFileName(media.getComponentMediaId());
					}
				}
			}
			media.populateBaseCreateFields();
			newMedia = persistenceService.persist(media);
			componentService.getChangeLogService().addEntityChange(media);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(media.getComponentId());
		}
		return newMedia;
	}

	public void saveComponentMetadata(ComponentMetadata metadata)
	{
		saveComponentMetadata(metadata, true);
	}

	void saveComponentMetadata(ComponentMetadata metadata, boolean updateLastActivity)
	{
		ComponentMetadata oldMetadata = persistenceService.findById(ComponentMetadata.class, metadata.getMetadataId());
		if (oldMetadata != null) {
			oldMetadata.updateFields(metadata);
			persistenceService.persist(oldMetadata);
		} else {
			metadata.setMetadataId(persistenceService.generateId());
			metadata.populateBaseCreateFields();
			persistenceService.persist(metadata);
			componentService.getChangeLogService().addEntityChange(metadata);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(metadata.getComponentId());
		}
	}

	public ComponentRelationship saveComponentRelationship(ComponentRelationship componentRelationship)
	{
		return saveComponentRelationship(componentRelationship, true);
	}

	ComponentRelationship saveComponentRelationship(ComponentRelationship componentRelationship, boolean updateLastActivity)
	{
		ComponentRelationship newRelationship;

		ComponentRelationship componentRelationshipExisting = persistenceService.findById(ComponentRelationship.class, componentRelationship.getComponentRelationshipId());

		if (componentRelationshipExisting == null) {
			//handle duplicates
			ComponentRelationship relationshipCheck = new ComponentRelationship();
			relationshipCheck.setRelationshipType(componentRelationship.getRelationshipType());
			relationshipCheck.setComponentId(componentRelationship.getComponentId());
			relationshipCheck.setRelatedComponentId(componentRelationship.getRelatedComponentId());

			QueryByExample queryByExample = new QueryByExample(relationshipCheck);
			queryByExample.setReturnNonProxied(false);

			componentRelationshipExisting = persistenceService.queryOneByExample(queryByExample);
		}

		if (componentRelationshipExisting != null) {
			componentRelationshipExisting.updateFields(componentRelationship);
			newRelationship = persistenceService.persist(componentRelationshipExisting);
		} else {
			componentRelationship.setComponentRelationshipId(persistenceService.generateId());
			componentRelationship.populateBaseCreateFields();
			newRelationship = persistenceService.persist(componentRelationship);

			componentService.getChangeLogService().addEntityChange(componentRelationship);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(componentRelationship.getComponentId());
		}
		return newRelationship;
	}

	public void saveComponentQuestion(ComponentQuestion question)
	{
		saveComponentQuestion(question, true);
	}

	void saveComponentQuestion(ComponentQuestion question, boolean updateLastActivity)
	{
		ComponentQuestion oldQuestion = persistenceService.findById(ComponentQuestion.class, question.getQuestionId());
		if (oldQuestion != null) {
			oldQuestion.updateFields(question);
			persistenceService.persist(oldQuestion);
			question = oldQuestion;
		} else {
			question.setQuestionId(persistenceService.generateId());
			question.populateBaseCreateFields();
			persistenceService.persist(question);

			componentService.getChangeLogService().addEntityChange(question);
		}
		handleUserDataAlert(question);
		componentService.getOrganizationService().addOrganization(question.getOrganization());

		if (updateLastActivity) {
			updateComponentLastActivity(question.getComponentId());
		}
	}

	public void saveComponentQuestionResponse(ComponentQuestionResponse response)
	{
		saveComponentQuestionResponse(response, true);
	}

	public void saveComponentQuestionResponse(ComponentQuestionResponse response, boolean updateLastActivity)
	{
		ComponentQuestionResponse oldResponse = persistenceService.findById(ComponentQuestionResponse.class, response.getResponseId());
		if (oldResponse != null) {
			oldResponse.updateFields(response);
			persistenceService.persist(oldResponse);
			response = oldResponse;
		} else {
			response.setResponseId(persistenceService.generateId());
			response.populateBaseCreateFields();
			persistenceService.persist(response);

			componentService.getChangeLogService().addEntityChange(response);
		}
		handleUserDataAlert(response);
		componentService.getOrganizationService().addOrganization(response.getOrganization());

		if (updateLastActivity) {
			updateComponentLastActivity(response.getComponentId());
		}
	}

	public ComponentResource saveComponentResource(ComponentResource resource)
	{
		return saveComponentResource(resource, true);
	}

	ComponentResource saveComponentResource(ComponentResource resource, boolean updateLastActivity)
	{
		ComponentResource oldResource = persistenceService.findById(ComponentResource.class, resource.getResourceId());
		if (oldResource != null) {

			if (StringUtils.isNotBlank(resource.getLink())) {
				removeLocalResource(oldResource);
			}
			oldResource.updateFields(resource);

			persistenceService.persist(oldResource);
			resource = oldResource;
		} else {
			if (StringUtils.isBlank(resource.getResourceId())) {
				resource.setResourceId(persistenceService.generateId());
			}

			//On a merge there may be a pre-existing file that needs to be rename
			if (StringUtils.isNotBlank(resource.getFileName())) {
				if (resource.getFileName().equals(resource.getResourceId()) == false) {
					Path oldPath = resource.pathToResource();
					if (oldPath != null && oldPath.toFile().exists()) {
						try {
							Files.move(oldPath, oldPath.resolveSibling(resource.getResourceId()));
						} catch (IOException ioe) {
							throw new OpenStorefrontRuntimeException("Failed to rename resource; trying to re-point record.", "Download resource; delete original media resource and then re-upload.", ioe);
						}
						resource.setFileName(resource.getResourceId());
					}
				}
			}

			resource.populateBaseCreateFields();
			persistenceService.persist(resource);
			componentService.getChangeLogService().addEntityChange(resource);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(resource.getComponentId());
		}
		return resource;
	}

	public void saveComponentReview(ComponentReview review)
	{
		saveComponentReview(review, true);
	}

	void saveComponentReview(ComponentReview review, boolean updateLastActivity)
	{
		ComponentReview oldReview = persistenceService.findById(ComponentReview.class, review.getComponentReviewId());
		if (oldReview != null) {
			oldReview.updateFields(review);
			persistenceService.persist(oldReview);
			review = oldReview;
		} else {
			review.setComponentReviewId(persistenceService.generateId());
			review.populateBaseCreateFields();
			persistenceService.persist(review);
			componentService.getChangeLogService().addEntityChange(review);
		}
		handleUserDataAlert(review);
		componentService.getOrganizationService().addOrganization(review.getOrganization());

		if (updateLastActivity) {
			updateComponentLastActivity(review.getComponentId());
		}
	}

	public void saveComponentReviewCon(ComponentReviewCon con)
	{
		saveComponentReviewCon(con, true);
	}

	void saveComponentReviewCon(ComponentReviewCon con, boolean updateLastActivity)
	{
		ComponentReviewCon oldCon = persistenceService.findById(ComponentReviewCon.class, con.getComponentReviewConPk());
		if (oldCon != null) {
			oldCon.updateFields(con);
			persistenceService.persist(oldCon);
		} else {
			con.populateBaseCreateFields();
			persistenceService.persist(con);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(con.getComponentId());
		}
	}

	public void saveComponentReviewPro(ComponentReviewPro pro)
	{
		saveComponentReviewPro(pro, true);
	}

	void saveComponentReviewPro(ComponentReviewPro pro, boolean updateLastActivity)
	{
		ComponentReviewPro oldPro = persistenceService.findById(ComponentReviewPro.class, pro.getComponentReviewProPk());
		if (oldPro != null) {
			oldPro.setActiveStatus(pro.getActiveStatus());
			oldPro.setUpdateDts(TimeUtil.currentDate());
			oldPro.setUpdateUser(pro.getUpdateUser());
			persistenceService.persist(oldPro);
		} else {
			pro.setActiveStatus(ComponentReviewPro.ACTIVE_STATUS);
			pro.setCreateDts(TimeUtil.currentDate());
			pro.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(pro);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(pro.getComponentId());
		}
	}

	public void saveComponentTag(ComponentTag tag)
	{
		componentService.getComponentServicePrivate().doSaveComponentTag(tag, true);
	}

	public void doSaveComponentTag(ComponentTag tag, boolean updateLastActivity)
	{
		Objects.requireNonNull(tag);
		Objects.requireNonNull(tag.getComponentId(), "Must set the component Id");
		Objects.requireNonNull(tag.getText());

		ComponentTag oldTag = persistenceService.findById(ComponentTag.class, tag.getTagId());
		if (oldTag == null) {
			ComponentTag componentTagExample = new ComponentTag();
			componentTagExample.setComponentId(tag.getComponentId());
			List<ComponentTag> componentTags = componentTagExample.findByExampleProxy();

			for (ComponentTag tagExisting : componentTags) {

				if (tagExisting.getText().toLowerCase().equals(tag.getText().toLowerCase())) {
					oldTag = tagExisting;
				}
			}
		}

		if (oldTag != null) {
			oldTag.updateFields(tag);
			persistenceService.persist(oldTag);
			tag = oldTag;
		} else {
			tag.setTagId(persistenceService.generateId());
			tag.populateBaseCreateFields();
			persistenceService.persist(tag);
			componentService.getChangeLogService().addEntityChange(tag);
		}
		handleUserDataAlert(tag);

		if (updateLastActivity) {
			updateComponentLastActivity(tag.getComponentId());
		}
	}

	public ValidationResult checkComponentAttribute(ComponentAttribute attribute)
	{
		ValidationResult validationResult = new ValidationResult();

		AttributeCodePk pk = new AttributeCodePk();
		pk.setAttributeCode(attribute.getComponentAttributePk().getAttributeCode());
		pk.setAttributeType(attribute.getComponentAttributePk().getAttributeType());

		AttributeType attributeType = componentService.getAttributeService().findType(attribute.getComponentAttributePk().getAttributeType());
		if (attributeType == null) {
			RuleResult ruleResult = new RuleResult();
			ruleResult.setEntityClassName(ComponentAttributePk.class.getSimpleName());
			ruleResult.setFieldName(AttributeType.FIELD_ATTRIBUTE_TYPE);
			ruleResult.setMessage("Attribute type not found");
			validationResult.getRuleResults().add(ruleResult);
		} else {
			AttributeCode attributeCode = componentService.getAttributeService().findCodeForType(pk);
			if (attributeCode == null) {
				if (Convert.toBoolean(attributeType.getAllowUserGeneratedCodes())) {
					AttributeCodeSave attributeCodeSave = new AttributeCodeSave();
					NewAttributeCode newAttributeCode = new NewAttributeCode();
					newAttributeCode.setAttributeType(attribute.getComponentAttributePk().getAttributeType());
					newAttributeCode.setAttributeCodeLabel(attribute.getComponentAttributePk().getAttributeCode());
					attributeCodeSave.getUserAttributes().add(newAttributeCode);
					componentService.getAttributeService().saveUserCodes(attributeCodeSave);
				} else {
					RuleResult ruleResult = new RuleResult();
					ruleResult.setEntityClassName(ComponentAttributePk.class.getSimpleName());
					ruleResult.setFieldName(AttributeCodePk.FIELD_ATTRIBUTE_CODE);
					ruleResult.setMessage("Attribute code not found");
					validationResult.getRuleResults().add(ruleResult);
				}
			}
		}

		return validationResult;
	}

	public List<ComponentTag> getTagCloud()
	{
		String query = "select * from ComponentTag where activeStatus='A' GROUP BY text";
		List<ComponentTag> tags = persistenceService.query(query, null);
		tags = FilterEngine.filter(tags, true);
		return tags;
	}

	public List<ComponentMetadata> getMetadata()
	{
		String query = "SELECT * FROM ComponentMetadata LET $component = (SELECT name, activeStatus, approvalState FROM Component WHERE componentId = $parent.current.componentId LIMIT 1) WHERE activeStatus = 'A' AND $component[0].activeStatus = 'A' AND $component[0].approvalState = 'A' ORDER BY label ASC, componentId ASC";
		return persistenceService.query(query, null);
	}

	public List<ComponentReviewView> getReviewByUser(String username)
	{
		ComponentReview example = new ComponentReview();
		example.setActiveStatus(ComponentReview.ACTIVE_STATUS);
		example.setCreateUser(username);
		List<ComponentReview> tempReviews = persistenceService.queryByExample(new QueryByExample(example));

		ComponentReview pendingReviewExample = new ComponentReview();
		pendingReviewExample.setCreateUser(username);
		pendingReviewExample.setActiveStatus(ComponentReview.PENDING_STATUS);

		List<ComponentReview> pendingComponentReviews = persistenceService.queryByExample(pendingReviewExample);
		tempReviews.addAll(pendingComponentReviews);

		List<ComponentReviewView> reviews = new ArrayList();
		tempReviews.forEach(review
				-> {
			ComponentReviewPro tempPro = new ComponentReviewPro();
			ComponentReviewProPk tempProPk = new ComponentReviewProPk();
			ComponentReviewCon tempCon = new ComponentReviewCon();
			ComponentReviewConPk tempConPk = new ComponentReviewConPk();

			tempProPk.setComponentReviewId(review.getComponentReviewId());
			tempConPk.setComponentReviewId(review.getComponentReviewId());

			tempPro.setComponentReviewProPk(tempProPk);
			tempCon.setComponentReviewConPk(tempConPk);

			ComponentReviewView tempView = ComponentReviewView.toView(review);

			tempView.setPros(ComponentReviewProCon.toViewListPro(persistenceService.queryByExample(new QueryByExample(tempPro))));
			tempView.setCons(ComponentReviewProCon.toViewListCon(persistenceService.queryByExample(new QueryByExample(tempCon))));

			reviews.add(tempView);
		});

		reviews.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_DESCENDING, ComponentReviewView.UPDATE_DATE_FIELD));

		//filter out unapproved components
		for (int i = reviews.size() - 1; i >= 0; i--) {
			ComponentReviewView reviewView = reviews.get(i);
			if (core.checkComponentApproval(reviewView.getComponentId()) == false) {
				reviews.remove(i);
			}
		}

		return reviews;
	}

	public ComponentMedia saveMediaFile(ComponentMedia media, InputStream fileInput)
	{
		return saveMediaFile(media, fileInput, true);
	}

	public ComponentMedia saveMediaFile(ComponentMedia media, InputStream fileInput, boolean updateLastActivity)
	{
		Objects.requireNonNull(media);
		Objects.requireNonNull(fileInput);

		if (StringUtils.isBlank(media.getComponentMediaId())) {
			media = saveComponentMedia(media, updateLastActivity);
		}
		media.setFileName(media.getComponentMediaId());
		try (InputStream in = fileInput) {
			Files.copy(in, media.pathToMedia(), StandardCopyOption.REPLACE_EXISTING);
			media.setUpdateUser(SecurityUtil.getCurrentUserName());
			media = saveComponentMedia(media, updateLastActivity);
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to store media file.", "Contact System Admin.  Check file permissions and disk space ", ex);
		}
		return media;
	}

	public void saveResourceFile(ComponentResource resource, InputStream fileInput)
	{
		Objects.requireNonNull(resource);
		Objects.requireNonNull(fileInput);

		if (StringUtils.isBlank(resource.getResourceId())) {
			resource.setResourceId(persistenceService.generateId());
		}
		resource.setFileName(resource.getResourceId());
		try (InputStream in = fileInput) {
			Files.copy(in, resource.pathToResource(), StandardCopyOption.REPLACE_EXISTING);
			resource.setUpdateUser(SecurityUtil.getCurrentUserName());
			saveComponentResource(resource);
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to store resource file.", "Contact System Admin.  Check file permissions and disk space ", ex);
		}
	}

	public ValidationResult saveDetailReview(ComponentReview review, List<ComponentReviewPro> pros, List<ComponentReviewCon> cons)
	{
		ValidationResult validationResult = new ValidationResult();

		ValidationModel validationModel = new ValidationModel(review);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult reviewResults = ValidationUtil.validate(validationModel);
		validationResult.merge(reviewResults);

		for (ComponentReviewPro reviewPro : pros) {
			ReviewPro proCode = componentService.getLookupService().getLookupEnity(ReviewPro.class, reviewPro.getComponentReviewProPk().getReviewPro());
			if (proCode == null) {
				proCode = componentService.getLookupService().getLookupEnityByDesc(ReviewPro.class, reviewPro.getComponentReviewProPk().getReviewPro());
				if (proCode == null) {
					reviewPro.getComponentReviewProPk().setReviewPro(null);
				} else {
					reviewPro.getComponentReviewProPk().setReviewPro(proCode.getCode());
				}
			} else {
				reviewPro.getComponentReviewProPk().setReviewPro(proCode.getCode());
			}
			validationModel = new ValidationModel(reviewPro);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult proResults = ValidationUtil.validate(validationModel);
			validationResult.merge(proResults);
		}

		for (ComponentReviewCon reviewCon : cons) {
			ReviewCon conCode = componentService.getLookupService().getLookupEnity(ReviewCon.class, reviewCon.getComponentReviewConPk().getReviewCon());
			if (conCode == null) {
				conCode = componentService.getLookupService().getLookupEnityByDesc(ReviewCon.class, reviewCon.getComponentReviewConPk().getReviewCon());
				if (conCode == null) {
					reviewCon.getComponentReviewConPk().setReviewCon(null);
				} else {
					reviewCon.getComponentReviewConPk().setReviewCon(conCode.getCode());
				}
			} else {
				reviewCon.getComponentReviewConPk().setReviewCon(conCode.getCode());
			}
			validationModel = new ValidationModel(reviewCon);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult conResults = ValidationUtil.validate(validationModel);
			validationResult.merge(conResults);
		}

		if (validationResult.valid()) {
			if (PropertiesManager.getValue(PropertiesManager.KEY_USER_REVIEW_AUTO_APPROVE, "true").toLowerCase().equals("true")) {
				review.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			} else {
				review.setActiveStatus(ComponentReview.PENDING_STATUS);
			}
			review.setCreateUser(SecurityUtil.getCurrentUserName());
			review.setUpdateUser(SecurityUtil.getCurrentUserName());
			saveComponentReview(review, false);

			//delete existing pros
			ComponentReviewPro componentReviewProExample = new ComponentReviewPro();
			componentReviewProExample.setComponentId(review.getComponentId());
			componentReviewProExample.setComponentReviewProPk(new ComponentReviewProPk());
			componentReviewProExample.getComponentReviewProPk().setComponentReviewId(review.getComponentReviewId());
			persistenceService.deleteByExample(componentReviewProExample);

			for (ComponentReviewPro reviewPro : pros) {
				reviewPro.setComponentId(review.getComponentId());
				reviewPro.getComponentReviewProPk().setComponentReviewId(review.getComponentReviewId());
				reviewPro.setCreateUser(SecurityUtil.getCurrentUserName());
				reviewPro.setUpdateUser(SecurityUtil.getCurrentUserName());
				reviewPro.setActiveStatus(ComponentReviewPro.ACTIVE_STATUS);
				saveComponentReviewPro(reviewPro, false);
			}

			//delete existing cons
			ComponentReviewCon componentReviewConExample = new ComponentReviewCon();
			componentReviewConExample.setComponentId(review.getComponentId());
			componentReviewConExample.setComponentReviewConPk(new ComponentReviewConPk());
			componentReviewConExample.getComponentReviewConPk().setComponentReviewId(review.getComponentReviewId());
			persistenceService.deleteByExample(componentReviewConExample);

			for (ComponentReviewCon reviewCon : cons) {
				reviewCon.setComponentId(review.getComponentId());
				reviewCon.getComponentReviewConPk().setComponentReviewId(review.getComponentReviewId());
				reviewCon.setCreateUser(SecurityUtil.getCurrentUserName());
				reviewCon.setUpdateUser(SecurityUtil.getCurrentUserName());
				reviewCon.setActiveStatus(ComponentReviewPro.ACTIVE_STATUS);
				saveComponentReviewCon(reviewCon, false);
			}

			updateComponentLastActivity(review.getComponentId());
		}

		return validationResult;
	}

	public void bulkComponentAttributeChange(BulkComponentAttributeChange bulkComponentAttributeChange)
	{
		Set<String> componentIdSet = new HashSet<>();

		for (ComponentAttribute componentAttribute : bulkComponentAttributeChange.getAttributes()) {

			componentAttribute.populateBaseUpdateFields();
			switch (bulkComponentAttributeChange.getOpertionType()) {
				case ACTIVATE:
					componentIdSet.add(componentAttribute.getComponentId());
					componentAttribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
					persistenceService.persist(componentAttribute);

					componentService.getChangeLogService().logStatusChange(componentAttribute, ComponentAttribute.ACTIVE_STATUS);
					break;
				case INACTIVE:
					componentIdSet.add(componentAttribute.getComponentId());
					componentAttribute.setActiveStatus(ComponentAttribute.INACTIVE_STATUS);
					persistenceService.persist(componentAttribute);

					componentService.getChangeLogService().logStatusChange(componentAttribute, ComponentAttribute.INACTIVE_STATUS);
					break;
				case DELETE:
					persistenceService.delete(componentAttribute);

					componentService.getChangeLogService().removeEntityChange(ComponentAttribute.class, componentAttribute);
					break;
				default:
					throw new UnsupportedOperationException("Add support for other type of operations");
			}
		}
		componentIdSet.forEach(componentId
				-> {
			updateComponentLastActivity(componentId);
		});
	}

	public ComponentReview setReviewPending(String ComponentReviewId)
	{
		ComponentReview found = persistenceService.findById(ComponentReview.class, ComponentReviewId);
		if (found != null) {
			if (found instanceof LoggableModel) {
				componentService.getChangeLogService().logStatusChange(found, ComponentReview.PENDING_STATUS);
			}

			found.setActiveStatus(ComponentReview.PENDING_STATUS);
			found.populateBaseUpdateFields();
			persistenceService.persist(found);

			updateComponentLastActivity(found.getComponentId());
		}
		return found;
	}

	public ComponentQuestion setQuestionPending(String ComponentQuestionId)
	{
		ComponentQuestion found = persistenceService.findById(ComponentQuestion.class, ComponentQuestionId);
		if (found != null) {
			if (found instanceof LoggableModel) {
				componentService.getChangeLogService().logStatusChange(found, ComponentQuestion.PENDING_STATUS);
			}

			found.setActiveStatus(ComponentQuestion.PENDING_STATUS);
			found.populateBaseUpdateFields();
			persistenceService.persist(found);

			updateComponentLastActivity(found.getComponentId());
		}
		return found;
	}

	public ComponentQuestionResponse setQuestionResponsePending(String ComponentQuestionResponseId)
	{
		ComponentQuestionResponse found = persistenceService.findById(ComponentQuestionResponse.class, ComponentQuestionResponseId);
		if (found != null) {
			if (found instanceof LoggableModel) {
				componentService.getChangeLogService().logStatusChange(found, ComponentQuestionResponse.PENDING_STATUS);
			}

			found.setActiveStatus(ComponentQuestionResponse.PENDING_STATUS);
			found.populateBaseUpdateFields();
			persistenceService.persist(found);

			updateComponentLastActivity(found.getComponentId());
		}
		return found;
	}

}
