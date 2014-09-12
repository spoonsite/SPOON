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

import edu.usu.sdl.openstorefront.storage.model.ComponentMedia;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.Validate;

/**
 *
 * @author dshurtleff
 */
public class ImageAction
		extends BaseAction
{

	@Validate(required = true)
	private String mediaId;

	@DefaultHandler
	public Resolution sendMedia()
	{
		ComponentMedia componentMedia = service.getPersistenceService().findById(ComponentMedia.class, mediaId);
		String contentType = "image/png";
		if (componentMedia != null) {
			contentType = componentMedia.getMimeType();
		}
		return new StreamingResolution(projectId)
		{

			@Override
			protected void stream(HttpServletResponse response) throws Exception
			{
				super.stream(response); //To change body of generated methods, choose Tools | Templates.
			}

		};
	}

	public String getMediaId()
	{
		return mediaId;
	}

	public void setMediaId(String mediaId)
	{
		this.mediaId = mediaId;
	}

}
