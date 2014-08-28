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
package edu.usu.sdl.openstorefront.storage.model;

import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.PK;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
public class ComponentResource
		extends BaseComponent
{

	@PK
	@NotNull
	private String resourceId;

	private String resourceFileId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	private String resourceType;

	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_URL)
	private String link;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String name;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_DESCRIPTION)
	private String description;
	private Boolean restricted;

	public static final String LOCAL_RESOURCE_URL = "Resource.action?LoadResource&resourceId=";

	public ComponentResource()
	{
	}
	
	@Override
	public void setPrimaryKey(String itemId, String itemCode, String componentId)
	{
		resourceId = itemId;
	}
	
	@Override
	public Object getPrimaryKey()
	{
		return resourceId;
	}

	public String getResourceId()
	{
		return resourceId;
	}

	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}

	public String getResourceFileId()
	{
		return resourceFileId;
	}

	public void setResourceFileId(String resourceFileId)
	{
		this.resourceFileId = resourceFileId;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
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

}
