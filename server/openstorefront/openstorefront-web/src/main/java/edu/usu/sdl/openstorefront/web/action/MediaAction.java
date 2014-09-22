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
import edu.usu.sdl.openstorefront.storage.model.ComponentMedia;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
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

	@Validate(required = true, on = "LoadMedia")
	private String mediaId;

	@ValidateNestedProperties({
		@Validate(required = true, field = "mediaTypeCode", on = "UploadMedia")
	})
	private ComponentMedia componentMedia;

	@Validate(required = true, on = "UploadMedia")
	private FileBean file;

	@HandlesEvent("LoadMedia")
	public Resolution sendMedia()
	{
		componentMedia = service.getPersistenceService().findById(ComponentMedia.class, mediaId);
		if (componentMedia == null) {
			throw new OpenStorefrontRuntimeException("Media not Found", "Check media Id");
		}

		return new StreamingResolution(componentMedia.getMimeType())
		{

			@Override
			protected void stream(HttpServletResponse response) throws Exception
			{
				Path path = componentMedia.pathToMedia();
				if (path != null) {
					Files.copy(path, response.getOutputStream());
				} else {
					throw new OpenStorefrontRuntimeException("Media not on disk", "Check media record: " + mediaId);
				}
			}

		};
	}

	@HandlesEvent("UploadMedia")
	public Resolution uploadMedia()
	{
		Map<String, String> errors = new HashMap<>();
		if (componentMedia != null) {
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
				} catch (IOException ex) {
					throw new OpenStorefrontRuntimeException("Unable to able to save media.", "Contact System Admin. Check disk space and permissions.", ex);
				}
			} else {
				errors.put("file", validationResult.toHtmlString());
			}
		}
		return streamUploadResponse(errors);
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

}
