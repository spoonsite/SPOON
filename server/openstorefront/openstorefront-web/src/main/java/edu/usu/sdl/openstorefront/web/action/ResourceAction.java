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

import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentResource;
import edu.usu.sdl.openstorefront.storage.model.ComponentTracking;
import edu.usu.sdl.openstorefront.storage.model.TrackEventCode;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import edu.usu.sdl.openstorefront.util.StringProcessor;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

/**
 * Handles Resources Interaction
 *
 * @author dshurtleff
 */
public class ResourceAction
		extends BaseAction
{

	private static final Logger log = Logger.getLogger(ResourceAction.class.getName());

	@Validate(required = true, on = {"LoadResource", "Redirect"})
	private String resourceId;

	@ValidateNestedProperties({
		@Validate(required = true, field = "resourceType", on = "UploadResource")
	})
	private ComponentResource componentResource;

	@Validate(required = true, on = "UploadResource")
	private FileBean file;

	@HandlesEvent("LoadResource")
	public Resolution loadResource()
	{
		componentResource = service.getPersistenceService().findById(ComponentResource.class, resourceId);
		if (componentResource == null) {
			throw new OpenStorefrontRuntimeException("Resource not Found", "Check resource Id: " + resourceId);
		}

		return new StreamingResolution(componentResource.getMimeType())
		{

			@Override
			protected void stream(HttpServletResponse response) throws Exception
			{
				Path path = componentResource.pathToResource();
				if (path != null && path.toFile().exists()) {
					Files.copy(path, response.getOutputStream());
				} else {
					Component component = service.getPersistenceService().findById(Component.class, componentResource.getComponentId());
					String message = MessageFormat.format("Resource not on disk: {0} Check resource record: {1} on component {2} ({3}) ", new Object[]{componentResource.pathToResource(), resourceId, component.getName(), component.getComponentId()});
					throw new OpenStorefrontRuntimeException(message);
				}
			}

		};
	}

	@HandlesEvent("UploadResource")
	public Resolution uploadResource()
	{
		Map<String, String> errors = new HashMap<>();
		if (SecurityUtil.isAdminUser()) {
			log.log(Level.INFO, SecurityUtil.adminAuditLogMessage(getContext().getRequest()));
			if (componentResource != null) {
				componentResource.setActiveStatus(ComponentResource.ACTIVE_STATUS);
				componentResource.setUpdateUser(SecurityUtil.getCurrentUserName());
				componentResource.setCreateUser(SecurityUtil.getCurrentUserName());
				componentResource.setOriginalName(file.getFileName());
				componentResource.setMimeType(file.getContentType());

				ValidationModel validationModel = new ValidationModel(componentResource);
				validationModel.setConsumeFieldsOnly(true);
				ValidationResult validationResult = ValidationUtil.validate(validationModel);
				if (validationResult.valid()) {
					try {
						service.getComponentService().saveResourceFile(componentResource, file.getInputStream());
					} catch (IOException ex) {
						throw new OpenStorefrontRuntimeException("Unable to able to save resource.", "Contact System Admin. Check disk space and permissions.", ex);
					}
				} else {
					errors.put("file", validationResult.toHtmlString());
				}
			}
			return streamUploadResponse(errors);
		}
		return new ErrorResolution(HttpServletResponse.SC_FORBIDDEN, "Access denyed");
	}

	@HandlesEvent("Redirect")
	public Resolution redirect()
	{
		componentResource = service.getPersistenceService().findById(ComponentResource.class, resourceId);
		if (componentResource == null) {
			throw new OpenStorefrontRuntimeException("Resource not Found", "Check resource Id: " + resourceId);
		}

		ComponentTracking componentTracking = new ComponentTracking();
		componentTracking.setClientIp(SecurityUtil.getClientIp(getContext().getRequest()));
		componentTracking.setComponentId(componentResource.getComponentId());
		String link = StringProcessor.stripHtml(componentResource.getLink());
		if (componentResource.getFileName() != null) {
			componentTracking.setResourceLink(componentResource.pathToResource().toString());
		} else {
			componentTracking.setResourceLink(link);
		}
		componentTracking.setTrackEventTypeCode(TrackEventCode.EXTERNAL_LINK_CLICK);
		componentTracking.setEventDts(TimeUtil.currentDate());
		service.getComponentService().saveComponentTracking(componentTracking);

		if (componentResource.getFileName() != null) {
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

}
