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
package edu.usu.sdl.openstorefront.web.rest.model;

import edu.usu.sdl.openstorefront.storage.model.ComponentMedia;
import edu.usu.sdl.openstorefront.storage.model.MediaType;
import edu.usu.sdl.openstorefront.util.TranslateUtil;

/**
 *
 * @author dshurtleff
 */
public class ComponentMediaView
{
	private String link;
	private String contentType;
	private String caption;

	public ComponentMediaView()
	{
	}
	
	public static ComponentMediaView toView(ComponentMedia media)
	{
		ComponentMediaView mediaView = new ComponentMediaView();
		mediaView.setLink(media.getLink());
		mediaView.setContentType(TranslateUtil.translate(MediaType.class, media.getMimeType()));
		mediaView.setCaption(media.getCaption());
		return mediaView;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public String getCaption()
	{
		return caption;
	}

	public void setCaption(String caption)
	{
		this.caption = caption;
	}

}
