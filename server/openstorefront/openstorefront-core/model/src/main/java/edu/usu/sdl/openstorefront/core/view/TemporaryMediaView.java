/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.entity.TemporaryMedia;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TemporaryMediaView
		extends StandardEntityView
{

	private String name;
	private String mimeType;
	private String orignalFileName;
	private String originalSourceURL;

	private String mediaLink;
	private Date updateDts;
	private String updateUser;

	public static TemporaryMediaView toView(TemporaryMedia temporaryMedia)
	{
		TemporaryMediaView temporaryMediaView = new TemporaryMediaView();
		temporaryMediaView.setName(temporaryMedia.getName());
		temporaryMediaView.setOrignalFileName(temporaryMedia.getOriginalFileName());
		temporaryMediaView.setOriginalSourceURL(temporaryMedia.getOriginalSourceURL());
		temporaryMediaView.setMimeType(temporaryMedia.getMimeType());
		temporaryMediaView.setUpdateDts(temporaryMedia.getUpdateDts());
		temporaryMediaView.setUpdateUser(temporaryMedia.getUpdateUser());
		temporaryMediaView.setMediaLink("Media.action?TemporaryMedia&name=" + temporaryMedia.getName());
		temporaryMediaView.toStandardView(temporaryMedia);
		return temporaryMediaView;
	}

	public static List<TemporaryMediaView> toViewList(List<TemporaryMedia> temporaryMedia)
	{
		List<TemporaryMediaView> views = new ArrayList<>();
		temporaryMedia.forEach(media -> {
			views.add(toView(media));
		});
		return views;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getMimeType()
	{
		return mimeType;
	}

	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}

	public String getOrignalFileName()
	{
		return orignalFileName;
	}

	public void setOrignalFileName(String orignalFileName)
	{
		this.orignalFileName = orignalFileName;
	}

	public String getMediaLink()
	{
		return mediaLink;
	}

	public void setMediaLink(String mediaLink)
	{
		this.mediaLink = mediaLink;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public String getUpdateUser()
	{
		return updateUser;
	}

	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}

	public String getOriginalSourceURL()
	{
		return originalSourceURL;
	}

	public void setOriginalSourceURL(String originalSourceURL)
	{
		this.originalSourceURL = originalSourceURL;
	}

}
