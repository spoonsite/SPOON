/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.action;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.NetworkUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.entity.MediaFile;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.TrackEventCode;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.action.resolution.RangeResolutionBuilder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.apache.commons.lang3.StringUtils;

/**
 * Handles Resources Interaction
 *
 * @author dshurtleff
 */
public class ResourceAction
		extends BaseAction
{

	private static final Logger LOG = Logger.getLogger(ResourceAction.class.getName());

	@Validate(required = true, on = {"LoadResource", "Redirect", "LoadSubmissionFormResource"})
	private String resourceId;

	@ValidateNestedProperties({
		@Validate(required = true, field = "resourceType", on = "UploadResource")
		,
		@Validate(required = true, field = "componentId", on = "UploadResource")
	})
	private ComponentResource componentResource;
	private MediaFile mediaFile;

	@Validate(required = true, on = "UploadResource")
	private FileBean file;

	private static final String ACCESS_DENIED = "Access denied";

	@DefaultHandler
	public Resolution defaultPage()
	{
		return new ErrorResolution(HttpServletResponse.SC_FORBIDDEN, ACCESS_DENIED);
	}

	@HandlesEvent("LoadResource")
	public Resolution loadResource() throws FileNotFoundException
	{
		mediaFile = service.getPersistenceService().findById(MediaFile.class, resourceId);
		if (mediaFile == null) {
			componentResource = service.getPersistenceService().findById(ComponentResource.class, resourceId);
			componentResource = filterEngine.filter(componentResource, true);
			if (componentResource == null || componentResource.getFile() == null) {
				throw resourceNotFoundException(resourceId);
			}
			mediaFile = componentResource.getFile();
		}

		InputStream in;
		long length;
		Path path = mediaFile.path();
		if (path != null && path.toFile().exists()) {
			in = new FileInputStream(path.toFile());
			length = path.toFile().length();
		} else if (componentResource != null) {
			Component component = service.getPersistenceService().findById(Component.class, componentResource.getComponentId());
			String message = MessageFormat.format("Resource not on disk: {0} Check resource record: {1} on component {2} ({3}) ", componentResource.pathToResource(), resourceId, component.getName(), component.getComponentId());
			throw new OpenStorefrontRuntimeException(message);
		} else {
			String message = MessageFormat.format("Resource not on disk. Check Media File record: {0} ", resourceId);
			throw new OpenStorefrontRuntimeException(message);
		}

		return new RangeResolutionBuilder()
				.setContentType(mediaFile.getMimeType())
				.setInputStream(in)
				.setTotalLength(length)
				.setRequest(getContext().getRequest())
				.setFilename(mediaFile.getOriginalName())
				.createRangeResolution();

	}

	private OpenStorefrontRuntimeException resourceNotFoundException(String resourceId)
	{
		return new OpenStorefrontRuntimeException("Resource not Found", "Check resource Id: " + resourceId);
	}

	@ValidationMethod(on = {"UploadResource"})
	public void uploadHook(ValidationErrors errors)
	{
		checkUploadSizeValidation(errors, file, "file");
	}

	@HandlesEvent("UploadResource")
	public Resolution uploadResource()
	{
		Resolution resolution = null;
		Map<String, String> errors = new HashMap<>();

		if (componentResource != null) {
			Component component = service.getPersistenceService().findById(Component.class, componentResource.getComponentId());
			if (component != null) {
				boolean allow = false;
				if (SecurityUtil.hasPermission(SecurityPermission.ADMIN_ENTRY_RESOURCE_MANAGEMENT)) {
					allow = true;
					LOG.log(Level.INFO, () -> SecurityUtil.adminAuditLogMessage(getContext().getRequest()));
				} else if (SecurityUtil.hasPermission(SecurityPermission.USER_EVALUATIONS_UPDATE)) {
					if (ApprovalStatus.APPROVED.equals(component.getApprovalState()) == false) {
						allow = true;
					}
				} else if (SecurityUtil.isCurrentUserTheOwner(component)) {
					if (ApprovalStatus.APPROVED.equals(component.getApprovalState()) == false) {
						allow = true;
					}
				}
				if (allow) {
					if (!doesFileExceedLimit(file)) {

						componentResource.setActiveStatus(ComponentResource.ACTIVE_STATUS);
						componentResource.setUpdateUser(SecurityUtil.getCurrentUserName());
						componentResource.setCreateUser(SecurityUtil.getCurrentUserName());

						ValidationModel validationModel = new ValidationModel(componentResource);
						validationModel.setConsumeFieldsOnly(true);
						ValidationResult validationResult = ValidationUtil.validate(validationModel);
						if (validationResult.valid()) {
							try {
								service.getComponentService().saveResourceFile(componentResource, file.getInputStream(), file.getContentType(), StringProcessor.getJustFileName(file.getFileName()));

								if (SecurityUtil.isEntryAdminUser() == false) {
									if (ApprovalStatus.PENDING.equals(component.getApprovalState())) {
										service.getComponentService().checkComponentCancelStatus(componentResource.getComponentId(), ApprovalStatus.NOT_SUBMITTED);
									}
								}
							} catch (IOException ex) {
								throw new OpenStorefrontRuntimeException("Unable to able to save resource.", "Contact System Admin. Check disk space and permissions.", ex);
							} finally {
								deleteUploadFile(file);
							}
						} else {
							errors.put("file", validationResult.toHtmlString());
						}
					}
				} else {
					resolution = new ErrorResolution(HttpServletResponse.SC_FORBIDDEN, ACCESS_DENIED);
				}
			} else {
				errors.put("componentResource", "Missing component; check Component Id");
			}
		} else {
			errors.put("componentResource", "Missing component resource information");
		}
		if (resolution == null) {
			resolution = streamUploadResponse(errors);
		}
		return resolution;
	}

	@HandlesEvent("Redirect")
	public Resolution redirect() throws FileNotFoundException
	{
		componentResource = service.getPersistenceService().findById(ComponentResource.class, resourceId);
		componentResource = filterEngine.filter(componentResource, true);
		if (componentResource == null) {
			throw resourceNotFoundException(resourceId);
		}

		ComponentTracking componentTracking = new ComponentTracking();
		componentTracking.setClientIp(NetworkUtil.getClientIp(getContext().getRequest()));
		componentTracking.setComponentId(componentResource.getComponentId());
		Component component = service.getPersistenceService().findById(Component.class, componentResource.getComponentId());
		if (component != null) {
			componentTracking.setComponentType(component.getComponentType());
		} else {
			LOG.log(Level.WARNING, MessageFormat.format("Unable to find Component for the resource.  Component Id: {0}.  Check Data.", componentResource.getComponentId()));
		}
		String link = StringProcessor.stripHtml(componentResource.getLink());
		if (componentResource.getFile() != null && StringUtils.isNotBlank(componentResource.getFile().getFileName())) {
			componentTracking.setResourceLink(componentResource.pathToResource().toString());
		} else {
			componentTracking.setResourceLink(link);
		}
		componentTracking.setResourceType(componentResource.getResourceType());
		componentTracking.setRestrictedResouce(componentResource.getRestricted());
		componentTracking.setTrackEventTypeCode(TrackEventCode.EXTERNAL_LINK_CLICK);
		componentTracking.setEventDts(TimeUtil.currentDate());
		service.getComponentService().saveComponentTracking(componentTracking);

		if (componentResource.getFile() != null && StringUtils.isNotBlank(componentResource.getFile().getFileName())) {
			return loadResource();
		} else {
			return new RedirectResolution(link, false);
		}
	}

	public String getResourceId()
	{
		return resourceId;
	}

	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}

	public ComponentResource getComponentResource()
	{
		return componentResource;
	}

	public void setComponentResource(ComponentResource componentResource)
	{
		this.componentResource = componentResource;
	}

	public FileBean getFile()
	{
		return file;
	}

	public void setFile(FileBean file)
	{
		this.file = file;
	}

	public MediaFile getMediaFile()
	{
		return mediaFile;
	}

	public void setMediaFile(MediaFile mediaFile)
	{
		this.mediaFile = mediaFile;
	}

}
