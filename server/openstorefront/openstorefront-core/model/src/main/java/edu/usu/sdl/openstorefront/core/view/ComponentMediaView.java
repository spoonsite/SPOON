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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.MediaType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ComponentMediaView
		extends StandardEntityView
{

	private String componentMediaId;
	private String link;
	private String mediaTypeCode;
	private String contentType;
	private String mimeType;
	private String caption;
	private Date updateDts;
	private String activeStatus;
	private String fileName;
	private String originalFileName;
	private String originalLink;
	private Boolean hideInDisplay;
	private Boolean usedInline;
	private Boolean iconFlag;

	public static ComponentMediaView toView(ComponentMedia media)
	{
		ComponentMediaView mediaView = new ComponentMediaView();
		mediaView.setComponentMediaId(media.getComponentMediaId());
		mediaView.setActiveStatus(media.getActiveStatus());
		mediaView.setOriginalLink(media.getLink());
		mediaView.setMediaTypeCode(media.getMediaTypeCode());
		mediaView.setContentType(TranslateUtil.translate(MediaType.class, media.getMediaTypeCode()));
		mediaView.setCaption(media.getCaption());
		mediaView.setUpdateDts(media.getUpdateDts());
		mediaView.setHideInDisplay(media.getHideInDisplay());
		mediaView.setUsedInline(media.getUsedInline());
		mediaView.setIconFlag(media.getIconFlag());
		mediaView.setLink(media.getLink());
		if (media.getFile() != null) {
			mediaView.setFileName(media.getFile().getFileName());
			mediaView.setOriginalFileName(media.getFile().getOriginalName());
			mediaView.setMimeType(media.getFile().getMimeType());
			if (StringUtils.isNotBlank(media.getFile().getFileName())) {
				mediaView.setLink("Media.action?LoadMedia&mediaId=" + media.getFile().getMediaFileId());
			}
		}
		mediaView.toStandardView(media);

		return mediaView;
	}

	public static List<ComponentMediaView> toViewList(List<ComponentMedia> mediaList)
	{
		List<ComponentMediaView> componentMediaViews = new ArrayList<>();
		mediaList.forEach(media -> {
			componentMediaViews.add(toView(media));
		});
		return componentMediaViews;
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

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public String getMimeType()
	{
		return mimeType;
	}

	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public String getOriginalLink()
	{
		return originalLink;
	}

	public void setOriginalLink(String originalLink)
	{
		this.originalLink = originalLink;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getOriginalFileName()
	{
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName)
	{
		this.originalFileName = originalFileName;
	}

	public String getComponentMediaId()
	{
		return componentMediaId;
	}

	public void setComponentMediaId(String componentMediaId)
	{
		this.componentMediaId = componentMediaId;
	}

	public String getMediaTypeCode()
	{
		return mediaTypeCode;
	}

	public void setMediaTypeCode(String mediaTypeCode)
	{
		this.mediaTypeCode = mediaTypeCode;
	}

	public Boolean getHideInDisplay()
	{
		return hideInDisplay;
	}

	public void setHideInDisplay(Boolean hideInDisplay)
	{
		this.hideInDisplay = hideInDisplay;
	}

	public Boolean getUsedInline()
	{
		return usedInline;
	}

	public void setUsedInline(Boolean usedInline)
	{
		this.usedInline = usedInline;
	}

	public Boolean getIconFlag()
	{
		return iconFlag;
	}

	public void setIconFlag(Boolean iconFlag)
	{
		this.iconFlag = iconFlag;
	}

}
