/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.api.SubmissionFormService;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentRelationship;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.entity.MediaFile;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormField;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormSection;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormTemplate;
import edu.usu.sdl.openstorefront.core.entity.SubmissionTemplateStatus;
import edu.usu.sdl.openstorefront.core.entity.UserSubmission;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionField;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionMedia;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.ComponentDeleteOptions;
import edu.usu.sdl.openstorefront.core.model.ComponentFormSet;
import edu.usu.sdl.openstorefront.core.model.EditSubmissionOptions;
import edu.usu.sdl.openstorefront.core.model.UserSubmissionAll;
import edu.usu.sdl.openstorefront.core.model.VerifySubmissionTemplateResult;
import edu.usu.sdl.openstorefront.core.util.MediaFileType;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.mapping.MappingController;
import edu.usu.sdl.openstorefront.service.mapping.MappingException;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * Handles Submission Forms and Templates
 *
 * @author dshurtleff
 */
public class SubmissionFormServiceImpl
		extends ServiceProxy
		implements SubmissionFormService
{

	private static final Logger LOG = Logger.getLogger(SubmissionFormServiceImpl.class.getName());

	private MappingController mappingController = new MappingController();

	public SubmissionFormServiceImpl()
	{
	}

	public SubmissionFormServiceImpl(PersistenceService persistenceService)
	{
		super(persistenceService);
	}

	public void setMappingController(MappingController mappingController)
	{
		this.mappingController = mappingController;
	}

	@Override
	public SubmissionFormTemplate saveSubmissionFormTemplate(SubmissionFormTemplate template)
	{
		Objects.requireNonNull(template);

		if (Convert.toBoolean(template.getDefaultTemplate())) {
			template.setEntryType(null);
		}

		String componentTypeInUse = template.getEntryType();
		if (StringUtils.isBlank(componentTypeInUse)) {
			//Find type to verify against pick one
			List<ComponentType> componentType = getComponentService().getAllComponentTypes();
			if (componentType.isEmpty()) {
				throw new OpenStorefrontRuntimeException("At least one component type needs to be defined and active", "Add Component Type");
			}
			componentTypeInUse = componentType.get(0).getComponentType();
		}

		ValidationResult validationResult = validateTemplate(template, componentTypeInUse);
		if (validationResult.valid()) {
			template.setTemplateStatus(SubmissionTemplateStatus.PENDING_VERIFICATION);
			template.setTemplateStatusDetail(null);
		} else {
			template.setTemplateStatus(SubmissionTemplateStatus.INCOMPLETE);
			template.setTemplateStatusDetail(validationResult.toHtmlString());
			LOG.log(Level.FINE, () -> "Unable to validated template (Incomplete): " + validationResult.toString());
		}
		template.setActiveStatus(SubmissionFormTemplate.INACTIVE_STATUS);

		SubmissionFormTemplate existing = persistenceService.findById(SubmissionFormTemplate.class, template.getSubmissionTemplateId());
		if (existing != null) {
			existing.updateFields(template);
			template = persistenceService.persist(existing);
		} else {
			template.setSubmissionTemplateId(persistenceService.generateId());
			template.populateBaseCreateFields();
			template.updateSectionLinks();
			template = persistenceService.persist(template);
		}
		template = persistenceService.unwrapProxyObject(template);
		return template;
	}

	@Override
	public void saveSubmissionTemplateAsDefault(SubmissionFormTemplate template)
	{
		template.setDefaultTemplate(Boolean.TRUE);
		template.setTemplateStatus(SubmissionTemplateStatus.VERIFIED);

		SubmissionFormTemplate existing = persistenceService.findById(SubmissionFormTemplate.class, template.getSubmissionTemplateId());
		if (existing != null) {
			existing.updateFields(template);
			persistenceService.persist(existing);
		} else {
			template.setSubmissionTemplateId(persistenceService.generateId());
			template.populateBaseCreateFields();
			template.updateSectionLinks();
			persistenceService.persist(template);
		}
	}

	@Override
	public void deleteSubmissionFormTemplate(String templateId)
	{
		SubmissionFormTemplate existing = persistenceService.findById(SubmissionFormTemplate.class, templateId);
		if (existing != null) {
			persistenceService.delete(existing);
		}
	}

	@Override
	public ValidationResult validateTemplate(SubmissionFormTemplate template, String componentType)
	{
		Objects.requireNonNull(template);
		return mappingController.verifyTemplate(template, componentType);
	}

	@Override
	public List<UserSubmission> getUserSubmissions(String ownerUsername)
	{
		UserSubmission userSubmissionExample = new UserSubmission();
		userSubmissionExample.setActiveStatus(UserSubmission.ACTIVE_STATUS);
		userSubmissionExample.setOwnerUsername(ownerUsername);
		return userSubmissionExample.findByExample();
	}

	@Override
	public UserSubmission saveUserSubmission(UserSubmission userSubmission)
	{
		UserSubmission existing = persistenceService.findById(UserSubmission.class, userSubmission.getUserSubmissionId());
		if (existing != null) {
			existing.updateFields(userSubmission);
			existing = persistenceService.persist(existing);
		} else {
			userSubmission.setUserSubmissionId(persistenceService.generateId());
			userSubmission.populateBaseCreateFields();
			if (userSubmission.getOwnerUsername() == null) {
				userSubmission.setOwnerUsername(SecurityUtil.getCurrentUserName());
			}
			if (userSubmission.getFields() == null) {
				userSubmission.setFields(new ArrayList<>());
			}
			populateComponentNameField(userSubmission);

			for (UserSubmissionField field : userSubmission.getFields()) {
				field.setFieldId(persistenceService.generateId());
			}

			existing = persistenceService.persist(userSubmission);
		}
		existing = persistenceService.unwrapProxyObject(existing);
		return existing;
	}

	private void populateComponentNameField(UserSubmission userSubmission)
	{
		SubmissionFormTemplate template = persistenceService.findById(SubmissionFormTemplate.class, userSubmission.getTemplateId());
		if (template != null) {
			//prepopulate
			SubmissionFormField nameField = null;
			for (SubmissionFormSection section : template.getSections()) {
				for (SubmissionFormField field : section.getFields()) {
					if (Component.FIELD_NAME.equals(field.getFieldName())) {
						nameField = field;
					}
				}
			}
			if (nameField != null) {
				boolean addField = true;
				for (UserSubmissionField field : userSubmission.getFields()) {
					if (nameField.getFieldId().equals(field.getTemplateFieldId())) {
						field.setRawValue(userSubmission.getSubmissionName());
						addField = false;
					}
				}
				if (addField) {
					UserSubmissionField field = new UserSubmissionField();
					field.setRawValue(userSubmission.getSubmissionName());
					field.setTemplateFieldId(nameField.getFieldId());
					userSubmission.getFields().add(field);
				}
			}

		} else {
			throw new OpenStorefrontRuntimeException("Unable to find Form Template.", "Refresh and try again.");
		}
	}

	@Override
	public UserSubmissionMedia saveSubmissionFormMedia(String userSubmissionId, String templateFieldId, MediaFile mediaFile, InputStream in)
	{
		Objects.requireNonNull(userSubmissionId);
		Objects.requireNonNull(templateFieldId);
		Objects.requireNonNull(in);

		//check for existing and remove old
		UserSubmissionMedia userSubmissionMedia;
		try (InputStream fileInput = in) {
			userSubmissionMedia = new UserSubmissionMedia();
			userSubmissionMedia.setTemplateFieldId(templateFieldId);
			userSubmissionMedia.setUserSubmissionId(userSubmissionId);
			userSubmissionMedia.setSubmissionMediaId(persistenceService.generateId());

			mediaFile.setMediaFileId(persistenceService.generateId());
			mediaFile.setFileName(persistenceService.generateId() + OpenStorefrontConstant.getFileExtensionForMime(mediaFile.getMimeType()));
			mediaFile.setFileType(MediaFileType.MEDIA);
			Path path = Paths.get(MediaFileType.MEDIA.getPath(), mediaFile.getFileName());
			Files.copy(fileInput, path, StandardCopyOption.REPLACE_EXISTING);

			userSubmissionMedia.setFile(mediaFile);
			userSubmissionMedia.populateBaseCreateFields();
			userSubmissionMedia = persistenceService.persist(userSubmissionMedia);
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to store media file.", "Contact System Admin.  Check file permissions and disk space ", ex);
		}
		return userSubmissionMedia;
	}

	@Override
	public VerifySubmissionTemplateResult verifySubmission(UserSubmission userSubmission)
	{
		Objects.requireNonNull(userSubmission);

		VerifySubmissionTemplateResult verifySubmissionTemplateResult = new VerifySubmissionTemplateResult();

		SubmissionFormTemplate formTemplate = persistenceService.findById(SubmissionFormTemplate.class, userSubmission.getTemplateId());
		if (formTemplate != null) {
			try {
				ComponentFormSet componentFormSet = mappingController.mapUserSubmissionToEntry(formTemplate, userSubmission);
				componentFormSet.getPrimary().getComponent().setApprovalState(ApprovalStatus.PENDING);
				ValidationResult validationResult = componentFormSet.validate(true);

				verifySubmissionTemplateResult.setComponentFormSet(componentFormSet);
				verifySubmissionTemplateResult.setValidationResult(validationResult);

				if (validationResult.valid()) {
					formTemplate.setTemplateStatus(SubmissionTemplateStatus.VERIFIED);
					formTemplate.populateBaseUpdateFields();
					persistenceService.persist(formTemplate);

					deleteUserSubmission(userSubmission.getUserSubmissionId());
				}

			} catch (MappingException ex) {
				LOG.log(Level.WARNING, "Failed to mapped user submisson");
				if (LOG.isLoggable(Level.FINE)) {
					LOG.log(Level.FINE, null, ex);
				}
			}

		} else {
			throw missingFormTemplateException(userSubmission.getTemplateId());
		}
		return verifySubmissionTemplateResult;
	}

	private OpenStorefrontRuntimeException missingFormTemplateException(String templateId)
	{
		return new OpenStorefrontRuntimeException("Unable to find form template. Template Id: " + templateId, "Check Data");
	}

	@Override
	public ValidationResult submitUserSubmissionForApproval(UserSubmission userSubmission)
	{
		Objects.requireNonNull(userSubmission);

		ValidationResult validationResult = new ValidationResult();

		SubmissionFormTemplate formTemplate = persistenceService.findById(SubmissionFormTemplate.class, userSubmission.getTemplateId());
		if (formTemplate != null) {
			try {
				ComponentFormSet componentFormSet = mappingController.mapUserSubmissionToEntry(formTemplate, userSubmission);

				componentFormSet.getPrimary().getComponent().setApprovalState(ApprovalStatus.PENDING);

				validationResult.merge(componentFormSet.getPrimary().validate());
				for (ComponentAll componentAll : componentFormSet.getChildren()) {
					validationResult.merge(componentAll.validate());
				}

				if (validationResult.valid()) {
					FileHistoryOption options = new FileHistoryOption();
					options.setUploadTags(Boolean.TRUE);

					ComponentAll saveComponent = getComponentService().saveFullComponent(componentFormSet.getPrimary(), options);
					getComponentService().submitComponentSubmission(saveComponent.getComponent().getComponentId());
					for (ComponentAll componentAll : componentFormSet.getChildren()) {
						ComponentAll childComponent = getComponentService().saveFullComponent(componentAll, options);
						getComponentService().submitComponentSubmission(childComponent.getComponent().getComponentId());
					}
					internalDeleteUserSubmission(userSubmission.getUserSubmissionId(), false);
				}

			} catch (MappingException ex) {
				Logger.getLogger(SubmissionFormServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
			}

		} else {
			throw missingFormTemplateException(userSubmission.getTemplateId());
		}

		return validationResult;
	}

	@Override
	public UserSubmission editComponentForSubmission(String componentId, EditSubmissionOptions options)
	{
		Objects.requireNonNull(componentId);
		Objects.requireNonNull(options);

		UserSubmission userSubmission;

		ComponentFormSet componentFormSet = new ComponentFormSet();
		ComponentAll componentAll = getComponentService().getFullComponent(componentId);
		componentFormSet.setPrimary(componentAll);

		ComponentType componentType = new ComponentType();
		componentType.setComponentType(componentAll.getComponent().getComponentType());
		componentType = componentType.find();

		SubmissionFormTemplate formTemplate;
		if (componentType != null) {
			formTemplate = findTemplateForComponentType(componentType.getComponentType());
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find component Type", "Check Data");
		}

		for (ComponentRelationship relationship : componentAll.getRelationships()) {
			componentFormSet.getChildren().add(getComponentService().getFullComponent(relationship.getRelatedComponentId()));
		}

		UserSubmissionAll userSubmissionAll;
		try {
			userSubmissionAll = mappingController.mapEntriesToUserSubmission(formTemplate, componentFormSet);
		} catch (MappingException ex) {
			throw new OpenStorefrontRuntimeException("Unable to map entry to submission.", "Check error ticket/logs", ex);
		}

		userSubmissionAll.getUserSubmission().setSubmissionName(componentAll.getComponent().getName());
		if (options.isForChangeRequest()) {
			if (options.isEditChangeRequest()) {
				Component component = new Component();
				component.setComponentId(componentId);
				component = component.find();

				userSubmissionAll.getUserSubmission().setOriginalComponentId(component.getPendingChangeId());
			} else {
				userSubmissionAll.getUserSubmission().setOriginalComponentId(componentId);
			}
		} else {
			userSubmissionAll.getUserSubmission().setOriginalComponentId(null);
		}

		userSubmission = saveUserSubmission(userSubmissionAll.getUserSubmission());
		for (UserSubmissionMedia media : userSubmissionAll.getMedia()) {
			media.setSubmissionMediaId(persistenceService.generateId());
			media.setUserSubmissionId(userSubmission.getUserSubmissionId());
			media.populateBaseCreateFields();
			persistenceService.persist(media);
		}

		if (options.removeComponent()) {
			ComponentDeleteOptions deleteOptions = new ComponentDeleteOptions();
			deleteOptions.setKeepMediaFiles(true);
			getComponentServicePrivate().cascadeDeleteOfComponent(componentId, deleteOptions);
		}

		return userSubmission;
	}

	@Override
	public ValidationResult submitChangeRequestForApproval(UserSubmission userSubmission)
	{
		Objects.requireNonNull(userSubmission);
		Objects.requireNonNull(userSubmission.getOriginalComponentId());

		ValidationResult validationResult = new ValidationResult();

		SubmissionFormTemplate formTemplate = persistenceService.findById(SubmissionFormTemplate.class, userSubmission.getTemplateId());
		if (formTemplate != null) {
			try {
				ComponentFormSet componentFormSet = mappingController.mapUserSubmissionToEntry(formTemplate, userSubmission);

				componentFormSet.getPrimary().getComponent().setApprovalState(ApprovalStatus.PENDING);
				componentFormSet.getPrimary().getComponent().setComponentId(persistenceService.generateId());
				componentFormSet.getPrimary().getComponent().setPendingChangeId(userSubmission.getOriginalComponentId());

				validationResult.merge(componentFormSet.getPrimary().validate());
				for (ComponentAll componentAll : componentFormSet.getChildren()) {
					validationResult.merge(componentAll.validate());
				}

				if (validationResult.valid()) {
					FileHistoryOption options = new FileHistoryOption();
					options.setUploadTags(Boolean.TRUE);

					ComponentAll savedComponentAll = getComponentService().saveFullComponent(componentFormSet.getPrimary(), options);
					for (ComponentAll componentAll : componentFormSet.getChildren()) {
						getComponentService().saveFullComponent(componentAll, options);
					}
					getComponentService().submitChangeRequest(savedComponentAll.getComponent().getComponentId());

					internalDeleteUserSubmission(userSubmission.getUserSubmissionId(), false);
				}

			} catch (MappingException ex) {
				Logger.getLogger(SubmissionFormServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
			}

		} else {
			throw missingFormTemplateException(userSubmission.getTemplateId());
		}

		return validationResult;
	}

	@Override
	public void reassignUserSubmission(String userSubmissionId, String newOwnerUsername)
	{
		Objects.requireNonNull(userSubmissionId);

		UserSubmission existing = persistenceService.findById(UserSubmission.class, userSubmissionId);
		if (existing != null) {
			existing.setOwnerUsername(newOwnerUsername);
			existing.populateBaseUpdateFields();
			persistenceService.persist(existing);
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find user submission. Id: " + userSubmissionId, "Check Id and refresh");
		}
	}

	@Override
	public void deleteUserSubmission(String userSubmissionId)
	{
		internalDeleteUserSubmission(userSubmissionId, true);
	}

	private void internalDeleteUserSubmission(String userSubmissionId, boolean deleteActualMedia)
	{
		UserSubmission existing = persistenceService.findById(UserSubmission.class, userSubmissionId);
		if (existing != null) {

			UserSubmissionMedia userSubmissionMedia = new UserSubmissionMedia();
			userSubmissionMedia.setUserSubmissionId(userSubmissionId);
			List<UserSubmissionMedia> userSubmissionMediaRecords = userSubmissionMedia.findByExampleProxy();

			for (UserSubmissionMedia media : userSubmissionMediaRecords) {
				if (deleteActualMedia) {
					handleMediaDelete(media);
				}
				persistenceService.delete(media);
			}

			persistenceService.delete(existing);
		}
	}

	private void handleMediaDelete(UserSubmissionMedia media)
	{
		if (media != null && media.getFile() != null) {
			deleteSubmissionMedia(media.getFile());
		}
	}

	private void deleteSubmissionMedia(MediaFile mediaFile)
	{
		Path path = mediaFile.path();
		try {
			if (path != null
					&& Files.deleteIfExists(path)) {
				LOG.log(Level.WARNING, () -> MessageFormat.format("Unable to delete local media...unable to find it. Path: {0}", path));
			}
		} catch (IOException ex) {
			LOG.log(Level.WARNING, ex, () -> MessageFormat.format("Unable to delete local media...check permissions or it may be in use. Path: {0}", path));
		}
	}

	@Override
	public void deleteUserSubmissionMedia(String submissionMediaId)
	{
		UserSubmissionMedia existing = persistenceService.findById(UserSubmissionMedia.class, submissionMediaId);
		if (existing != null) {

			handleMediaDelete(existing);
			persistenceService.delete(existing);
		}

	}

	@Override
	public void toggleActiveStatus(String submissionTemplateId, String newStatus)
	{
		Objects.requireNonNull(submissionTemplateId);
		Objects.requireNonNull(submissionTemplateId);

		SubmissionFormTemplate targetSubmissionFormTemplate = persistenceService.findById(SubmissionFormTemplate.class, submissionTemplateId);

		// if activating a template, inactivate other templates that have same entry type
		if (SubmissionFormTemplate.ACTIVE_STATUS.equals(newStatus) && targetSubmissionFormTemplate.getEntryType() != null) {
			SubmissionFormTemplate affectedTemplateExample = new SubmissionFormTemplate();
			affectedTemplateExample.setEntryType(targetSubmissionFormTemplate.getEntryType());
			affectedTemplateExample.setActiveStatus(SubmissionFormTemplate.ACTIVE_STATUS);

			List<SubmissionFormTemplate> affectedTemplates = affectedTemplateExample.findByExampleProxy();
			affectedTemplates.forEach(template -> {
				if (!template.getSubmissionTemplateId().equals(submissionTemplateId)) {
					template.setActiveStatus(SubmissionFormTemplate.INACTIVE_STATUS);
					persistenceService.persist(template);
				}
			});
		}

		targetSubmissionFormTemplate.setActiveStatus(newStatus);
		targetSubmissionFormTemplate.populateBaseUpdateFields();
		persistenceService.persist(targetSubmissionFormTemplate);
	}

	@Override
	public SubmissionFormTemplate findTemplateForComponentType(String componentType)
	{
		Objects.requireNonNull(componentType);

		SubmissionFormTemplate submissionFormTemplate = new SubmissionFormTemplate();
		submissionFormTemplate.setEntryType(componentType);
		submissionFormTemplate.setActiveStatus(SubmissionFormTemplate.ACTIVE_STATUS);
		submissionFormTemplate.setTemplateStatus(SubmissionTemplateStatus.VERIFIED);
		submissionFormTemplate = submissionFormTemplate.find();

		if (submissionFormTemplate == null) {
			//default
			submissionFormTemplate = new SubmissionFormTemplate();
			submissionFormTemplate.setDefaultTemplate(Boolean.TRUE);
			submissionFormTemplate = submissionFormTemplate.find();
		}

		return submissionFormTemplate;
	}

}
