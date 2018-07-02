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

import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.MediaFile;
import edu.usu.sdl.openstorefront.core.entity.ResourceType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ComponentResourceView
		extends StandardEntityView
{

	private String resourceId;
	private String resourceType;
	private String resourceTypeDesc;
	private String description;
	private String link;
	private String localResourceName;
	private String originalFileName;
	private String mimeType;
	private String actualLink;
	private Boolean restricted;
	private Date updateDts;
	private String activeStatus;
	private String originalLink;
	private Boolean privateFlag;
	private String componentId;
	private MediaFile file;

	private static final String LOCAL_RESOURCE_URL = "Resource.action?LoadResource&resourceId=";
	private static final String ACTUAL_RESOURCE_URL = "Resource.action?Redirect&resourceId=";

	public static List<ComponentResourceView> toViewList(List<ComponentResource> resources)
	{
		List<ComponentResourceView> viewList = new ArrayList<>();
		resources.forEach(resource -> {
			viewList.add(toView(resource));
		});
		return viewList;
	}

	public static ComponentResourceView toView(ComponentResource componentResource)
	{
		ComponentResourceView componentResourceView = new ComponentResourceView();
		componentResourceView.setResourceId(componentResource.getResourceId());
		componentResourceView.setDescription(componentResource.getDescription());
		componentResourceView.setResourceType(componentResource.getResourceType());
		componentResourceView.setResourceTypeDesc(TranslateUtil.translate(ResourceType.class, componentResource.getResourceType()));
		componentResourceView.setRestricted(componentResource.getRestricted());
		componentResourceView.setPrivateFlag(componentResource.getPrivateFlag());
		componentResourceView.setUpdateDts(componentResource.getUpdateDts());
		componentResourceView.setActiveStatus(componentResource.getActiveStatus());
		componentResourceView.setComponentId(componentResource.getComponentId());
		componentResourceView.setFile(componentResource.getFile());

		String link = componentResource.getLink();
		link = StringProcessor.stripHtml(link);
		componentResourceView.setOriginalLink(link);
		if (componentResource.getFile() != null) {
			componentResourceView.setLocalResourceName(componentResource.getFile().getFileName());
			componentResourceView.setMimeType(componentResource.getFile().getMimeType());
			componentResourceView.setOriginalFileName(componentResource.getFile().getOriginalName());
			if (StringUtils.isNotBlank(componentResource.getFile().getFileName())) {
				link = LOCAL_RESOURCE_URL + componentResource.getFile().getMediaFileId();
			}
		}
		componentResourceView.setLink(link);
		componentResourceView.setActualLink(ACTUAL_RESOURCE_URL + componentResource.getResourceId());

		componentResourceView.toStandardView(componentResource);

		return componentResourceView;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getResourceType()
	{
		return resourceType;
	}

	public void setResourceType(String resourceType)
	{
		this.resourceType = resourceType;
	}

	public Boolean getRestricted()
	{
		return restricted;
	}

	public void setRestricted(Boolean restricted)
	{
		this.restricted = restricted;
	}

	public String getResourceTypeDesc()
	{
		return resourceTypeDesc;
	}

	public void setResourceTypeDesc(String resourceTypeDesc)
	{
		this.resourceTypeDesc = resourceTypeDesc;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public String getActualLink()
	{
		return actualLink;
	}

	public void setActualLink(String actualLink)
	{
		this.actualLink = actualLink;
	}

	public String getResourceId()
	{
		return resourceId;
	}

	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}

	public String getLocalResourceName()
	{
		return localResourceName;
	}

	public void setLocalResourceName(String localResourceName)
	{
		this.localResourceName = localResourceName;
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

	public String getOriginalFileName()
	{
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName)
	{
		this.originalFileName = originalFileName;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public Boolean getPrivateFlag()
	{
		return privateFlag;
	}

	public void setPrivateFlag(Boolean privateFlag)
	{
		this.privateFlag = privateFlag;
	}

	public MediaFile getFile()
	{
		return file;
	}

	public void setFile(MediaFile file)
	{
		this.file = file;
	}

}
