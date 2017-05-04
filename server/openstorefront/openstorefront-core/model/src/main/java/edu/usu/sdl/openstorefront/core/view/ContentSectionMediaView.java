/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionMedia;
import edu.usu.sdl.openstorefront.core.entity.MediaType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class ContentSectionMediaView
	extends ContentSectionMedia
{
	private String mediaTypeDescription;
	private String link;

	public ContentSectionMediaView()
	{
	}

	public static ContentSectionMediaView toView(ContentSectionMedia contentSectionMedia)
	{
		ContentSectionMediaView view = new ContentSectionMediaView();
		try {
			BeanUtils.copyProperties(view, contentSectionMedia);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		view.setMediaTypeDescription(TranslateUtil.translate(MediaType.class, contentSectionMedia.getMediaTypeCode()));
		view.setLink("Media.action?SectionMedia&mediaId=" + contentSectionMedia.getContentSectionMediaId());		
		
		return view;
	}
	
	public static List<ContentSectionMediaView> toView(List<ContentSectionMedia> mediaRecords)
	{
		List<ContentSectionMediaView> views = new ArrayList<>();
		
		mediaRecords.forEach((media) -> {
			views.add(toView(media));
		});
		return views;
	}	
	
	public String getMediaTypeDescription()
	{
		return mediaTypeDescription;
	}

	public void setMediaTypeDescription(String mediaTypeDescription)
	{
		this.mediaTypeDescription = mediaTypeDescription;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

}
