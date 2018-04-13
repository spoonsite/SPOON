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
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.api.SubmissionFormService;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.MediaFile;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormResource;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormTemplate;
import edu.usu.sdl.openstorefront.core.entity.SubmissionTemplateStatus;
import edu.usu.sdl.openstorefront.core.entity.UserSubmission;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionField;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionMedia;
import edu.usu.sdl.openstorefront.core.model.ComponentFormSet;
import edu.usu.sdl.openstorefront.core.util.MediaFileType;
import edu.usu.sdl.openstorefront.service.mapping.MappingController;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	@Override
	public SubmissionFormTemplate saveSubmissionFormTemplate(SubmissionFormTemplate template)
	{
		Objects.requireNonNull(template);

		//Find type to verify against pick one
		List<ComponentType> componentType = getComponentService().getAllComponentTypes();
		if (componentType.isEmpty()) {
			throw new OpenStorefrontRuntimeException("At least one component type needs to be defined and active", "Add Component Type");
		}

		ValidationResult validationResult = validateTemplate(template, componentType.get(0).getComponentType());
		if (validationResult.valid()) {
			template.setTemplateStatus(SubmissionTemplateStatus.VALID);
		} else {
			template.setTemplateStatus(SubmissionTemplateStatus.INCOMPLETE);
		}

		SubmissionFormTemplate existing = persistenceService.findById(SubmissionFormTemplate.class, template.getSubmissionTemplateId());
		if (existing != null) {
			existing.updateFields(template);
			template = persistenceService.persist(existing);
		} else {
			template.setSubmissionTemplateId(persistenceService.generateId());
			template.populateBaseCreateFields();
			template = persistenceService.persist(template);
		}
		template = persistenceService.unwrapProxyObject(template);
		return template;
	}

	@Override
	public void deleteSubmissionFormTemplate(String templateId)
	{
		SubmissionFormTemplate existing = persistenceService.findById(SubmissionFormTemplate.class, templateId);
		if (existing != null) {

			SubmissionFormResource resourceExample = new SubmissionFormResource();
			resourceExample.setTemplateId(templateId);

			List<SubmissionFormResource> resources = resourceExample.findByExample();
			resources.forEach(resource -> {
				deleteSubmissionFormResource(resource.getResourceId());
			});

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
	public SubmissionFormResource saveSubmissionFormResource(SubmissionFormResource resource, InputStream in)
	{
		Objects.requireNonNull(resource);
		Objects.requireNonNull(resource.getFile());
		Objects.requireNonNull(in);

		SubmissionFormResource savedResource = resource.save();
		try (InputStream fileInput = in) {
			MediaFile mediaFile = savedResource.getFile();
			mediaFile.setFileName(persistenceService.generateId() + OpenStorefrontConstant.getFileExtensionForMime(mediaFile.getMimeType()));
			mediaFile.setFileType(MediaFileType.RESOURCE);
			Path path = Paths.get(MediaFileType.RESOURCE.getPath(), mediaFile.getFileName());
			Files.copy(fileInput, path, StandardCopyOption.REPLACE_EXISTING);

			persistenceService.persist(savedResource);
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to store media file.", "Contact System Admin.  Check file permissions and disk space ", ex);
		}

		savedResource = persistenceService.unwrapProxyObject(savedResource);
		return savedResource;

	}

	@Override
	public void deleteSubmissionFormResource(String resourceId)
	{
		SubmissionFormResource resource = persistenceService.findById(SubmissionFormResource.class, resourceId);

		if (resource.getFile() != null) {
			Path path = resource.getFile().path();
			if (path != null && path.toFile().exists()) {
				try {
					Files.delete(path);
				} catch (IOException ex) {
					LOG.log(Level.WARNING, MessageFormat.format("Unable to delete local media. Path: {0}", path));
					LOG.log(Level.FINE, null, ex);
				}
			}

			persistenceService.delete(resource);
		}
	}

	public void setMappingController(MappingController mappingController)
	{
		this.mappingController = mappingController;
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
			existing = persistenceService.persist(userSubmission);
		}
		existing = persistenceService.unwrapProxyObject(existing);
		return existing;
	}

	@Override
	public ComponentFormSet verifySubmission(UserSubmission userSubmission)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void submitUserSubmissionForApproval(UserSubmission userSubmission)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public UserSubmission editComponentForSubmission(String submissionTemplateId, String componentId)
	{
		Objects.isNull(componentId);
		ComponentFormSet componentFormSet = new ComponentFormSet();

		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void submitChangeRequestForApproval(UserSubmission userSubmission)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
		UserSubmission existing = persistenceService.findById(UserSubmission.class, userSubmissionId);
		if (existing != null) {

			if (existing.getFields() != null) {
				for (UserSubmissionField field : existing.getFields()) {
					handleMediaDelete(field);
				}
			}

			persistenceService.delete(existing);
		}
	}

	private void handleMediaDelete(UserSubmissionField field)
	{
		if (field.getMedia() != null) {
			for (UserSubmissionMedia media : field.getMedia()) {
				if (media.getFile() != null) {
					deleteSubmissionMedia(media.getFile());
				}
			}
		}
	}

	private void deleteSubmissionMedia(MediaFile mediaFile)
	{
		Path path = mediaFile.path();
		if (path != null
				&& path.toFile().exists()
				&& path.toFile().delete() == false) {
			LOG.log(Level.WARNING, MessageFormat.format("Unable to delete local media. Path: {0}", path.toString()));
		}
	}

}
