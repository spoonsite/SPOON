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
import edu.usu.sdl.openstorefront.service.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.storage.model.ApprovalStatus;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentMedia;
import edu.usu.sdl.openstorefront.storage.model.GeneralMedia;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
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
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

/**
 * Use to transmit media
 *
 * @author dshurtleff
 */
public class MediaAction
		extends BaseAction
{

	public static final String MISSING_IMAGE = "/image/close-red.png";
	public static final long MISSING_MEDIA_IMAGE_SIZE = 10898;

	private static final Logger log = Logger.getLogger(MediaAction.class.getName());

	@Validate(required = true, on = "LoadMedia")
	private String mediaId;

	@ValidateNestedProperties({
		@Validate(required = true, field = "mediaTypeCode", on = "UploadMedia"),
		@Validate(required = true, field = "componentId", on = "UploadMedia")
	})
	private ComponentMedia componentMedia;

	@Validate(required = true, on = "UploadMedia")
	private FileBean file;

	@Validate(required = true, on = "GeneralMedia")
	private String name;

	@ValidateNestedProperties({
		@Validate(required = true, field = "name", on = "UploadGeneralMedia")
	})
	private GeneralMedia generalMedia;

	@DefaultHandler
	public Resolution audioTestPage()
	{
		//return new ForwardResolution("/WEB-INF/securepages/test/audioTest.jsp");
		return new ErrorResolution(HttpServletResponse.SC_FORBIDDEN, "Access denied");
	}

	@HandlesEvent("LoadMedia")
	public Resolution sendMedia() throws FileNotFoundException
	{
		componentMedia = service.getPersistenceService().findById(ComponentMedia.class, mediaId);
		if (componentMedia == null) {
			throw new OpenStorefrontRuntimeException("Media not Found", "Check media Id");
		}

		InputStream in;
		long length;
		Path path = componentMedia.pathToMedia();
		if (path != null && path.toFile().exists()) {
			in = new FileInputStream(path.toFile());
			length = path.toFile().length();
		} else {
			Component component = service.getPersistenceService().findById(Component.class, componentMedia.getComponentId());
			log.log(Level.WARNING, MessageFormat.format("Media not on disk: {0} Check media record: {1} on component {2} ({3}) ", new Object[]{componentMedia.pathToMedia(), mediaId, component.getName(), component.getComponentId()}));
			in = new FileSystemManager().getClass().getResourceAsStream(MISSING_IMAGE);
			length = MISSING_MEDIA_IMAGE_SIZE;
		}
		return new RangeResolutionBuilder()
				.setContentType(componentMedia.getMimeType())
				.setInputStream(in)
				.setTotalLength(length)
				.setRequest(getContext().getRequest())
				.setFilename(componentMedia.getOriginalName())
				.createRangeResolution();
	}

	@HandlesEvent("UploadMedia")
	public Resolution uploadMedia()
	{
		Resolution resolution = null;
		Map<String, String> errors = new HashMap<>();

		if (componentMedia != null) {
			Component component = service.getPersistenceService().findById(Component.class, componentMedia.getComponentId());
			if (component != null) {
				boolean allow = false;
				if (SecurityUtil.isAdminUser()) {
					allow = true;
					log.log(Level.INFO, SecurityUtil.adminAuditLogMessage(getContext().getRequest()));
				} else if (SecurityUtil.isCurrentUserTheOwner(component)) {
					if (ApprovalStatus.APPROVED.equals(component.getApprovalState()) == false) {
						allow = true;
					}
				}
				if (allow) {
					componentMedia.setActiveStatus(ComponentMedia.ACTIVE_STATUS);
					componentMedia.setUpdateUser(SecurityUtil.getCurrentUserName());
					componentMedia.setCreateUser(SecurityUtil.getCurrentUserName());
					componentMedia.setOriginalName(file.getFileName());
					componentMedia.setMimeType(file.getContentType());

					ValidationModel validationModel = new ValidationModel(componentMedia);
					validationModel.setConsumeFieldsOnly(true);
					ValidationResult validationResult = ValidationUtil.validate(validationModel);
					if (validationResult.valid()) {
						try {
							service.getComponentService().saveMediaFile(componentMedia, file.getInputStream());

							if (SecurityUtil.isAdminUser() == false) {
								if (ApprovalStatus.PENDING.equals(component.getApprovalState())) {
									service.getComponentService().checkComponentCancelStatus(componentMedia.getComponentId(), ApprovalStatus.NOT_SUBMITTED);
								}
							}
						} catch (IOException ex) {
							throw new OpenStorefrontRuntimeException("Unable to able to save media.", "Contact System Admin. Check disk space and permissions.", ex);
						} finally {
							try {
								file.delete();
							} catch (IOException ex) {
								log.log(Level.WARNING, "Unable to remove temp upload file.", ex);
							}
						}
					} else {
						errors.put("file", validationResult.toHtmlString());
					}
				} else {
					resolution = new ErrorResolution(HttpServletResponse.SC_FORBIDDEN, "Access denied");
				}
			} else {
				errors.put("componentMedia", "Missing component; check Component Id");
			}
		} else {
			errors.put("componentMedia", "Missing component media information");
		}
		if (resolution == null) {
			resolution = streamUploadResponse(errors);
		}
		return resolution;
	}

	@HandlesEvent("GeneralMedia")
	public Resolution generalMedia() throws FileNotFoundException
	{
		GeneralMedia generalMediaExample = new GeneralMedia();
		generalMediaExample.setName(name);
		generalMedia = service.getPersistenceService().queryOneByExample(GeneralMedia.class, generalMediaExample);
		if (generalMedia == null) {
			log.log(Level.FINE, MessageFormat.format("General Media with name: {0} is not found.", name));
			return new StreamingResolution("image/png")
			{

				@Override
				protected void stream(HttpServletResponse response) throws Exception
				{
					try (InputStream in = new FileSystemManager().getClass().getResourceAsStream(MISSING_IMAGE)) {
						FileSystemManager.copy(in, response.getOutputStream());
					}
				}

			}.setFilename("MediaNotFound.png");
		}

		InputStream in;
		long length;
		Path path = generalMedia.pathToMedia();
		if (path != null && path.toFile().exists()) {
			in = new FileInputStream(path.toFile());
			length = path.toFile().length();
		} else {
			log.log(Level.WARNING, MessageFormat.format("Media not on disk: {0} Check general media record: {1} ", new Object[]{generalMedia.pathToMedia(), generalMedia.getName()}));
			in = new FileSystemManager().getClass().getResourceAsStream(MISSING_IMAGE);
			length = MISSING_MEDIA_IMAGE_SIZE;
		}

		return new RangeResolutionBuilder()
				.setContentType(generalMedia.getMimeType())
				.setInputStream(in)
				.setTotalLength(length)
				.setRequest(getContext().getRequest())
				.setFilename(generalMedia.getOriginalFileName())
				.createRangeResolution();
	}

	@HandlesEvent("UploadGeneralMedia")
	public Resolution uploadGeneralMedia()
	{
		Map<String, String> errors = new HashMap<>();
		if (SecurityUtil.isAdminUser()) {
			log.log(Level.INFO, SecurityUtil.adminAuditLogMessage(getContext().getRequest()));
			if (generalMedia != null) {
				generalMedia.setActiveStatus(ComponentMedia.ACTIVE_STATUS);
				generalMedia.setUpdateUser(SecurityUtil.getCurrentUserName());
				generalMedia.setCreateUser(SecurityUtil.getCurrentUserName());
				generalMedia.setOriginalFileName(file.getFileName());
				generalMedia.setMimeType(file.getContentType());

				ValidationModel validationModel = new ValidationModel(generalMedia);
				validationModel.setConsumeFieldsOnly(true);
				ValidationResult validationResult = ValidationUtil.validate(validationModel);
				if (validationResult.valid()) {
					try {
						service.getSystemService().saveGeneralMedia(generalMedia, file.getInputStream());
					} catch (IOException ex) {
						throw new OpenStorefrontRuntimeException("Unable to able to save media.", "Contact System Admin. Check disk space and permissions.", ex);
					} finally {
						try {
							file.delete();
						} catch (IOException ex) {
							log.log(Level.WARNING, "Unable to remove temp upload file.", ex);
						}
					}
				} else {
					errors.put("file", validationResult.toHtmlString());
				}
			} else {
				errors.put("generalMedia", "Missing general media information");
			}
			return streamUploadResponse(errors);
		}
		return new ErrorResolution(HttpServletResponse.SC_FORBIDDEN, "Access denied");
	}

	public String getMediaId()
	{
		return mediaId;
	}

	public void setMediaId(String mediaId)
	{
		this.mediaId = mediaId;
	}

	public ComponentMedia getComponentMedia()
	{
		return componentMedia;
	}

	public void setComponentMedia(ComponentMedia componentMedia)
	{
		this.componentMedia = componentMedia;
	}

	public FileBean getFile()
	{
		return file;
	}

	public void setFile(FileBean file)
	{
		this.file = file;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public GeneralMedia getGeneralMedia()
	{
		return generalMedia;
	}

	public void setGeneralMedia(GeneralMedia generalMedia)
	{
		this.generalMedia = generalMedia;
	}

}
