/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.entity.GeneralMedia;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class GeneralMediaView
		extends StandardEntityView
{

	private String name;
	private String mimeType;
	private String originalFileName;
	private String mediaLink;
	private Date updateDts;
	private String updateUser;

	public GeneralMediaView()
	{
	}

	public static GeneralMediaView toView(GeneralMedia generalMedia)
	{
		GeneralMediaView generalMediaView = new GeneralMediaView();
		generalMediaView.setName(generalMedia.getName());
		generalMediaView.setOriginalFileName(generalMedia.getOriginalFileName());
		generalMediaView.setMimeType(generalMedia.getMimeType());
		generalMediaView.setUpdateDts(generalMedia.getUpdateDts());
		generalMediaView.setUpdateUser(generalMedia.getUpdateUser());
		generalMediaView.setMediaLink("Media.action?GeneralMedia&name=" + generalMedia.getName());
		generalMediaView.toStandardView(generalMedia);
		return generalMediaView;
	}

	public static List<GeneralMediaView> toViewList(List<GeneralMedia> generalMedia)
	{
		List<GeneralMediaView> views = new ArrayList<>();
		generalMedia.forEach(media -> {
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

	public String getOriginalFileName()
	{
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName)
	{
		this.originalFileName = originalFileName;
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

}
