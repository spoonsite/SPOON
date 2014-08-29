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

import edu.usu.sdl.openstorefront.doc.ConsumeField;
import edu.usu.sdl.openstorefront.doc.ValidValueType;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.PK;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
public class Component
		extends BaseComponent
{

	@PK
	@NotNull
	private String componentId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_COMPONENT_NAME)
	@ConsumeField
	private String name;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_COMPONENT_DESCRIPTION)
	@ConsumeField
	private String description;

	@ConsumeField
	private String parentComponentId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GUID)
	@ConsumeField
	private String guid;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_ORGANIZATION)
	@ConsumeField
	private String organization;

	@ConsumeField
	private Date releaseDate;

	@NotNull
	@ConsumeField
	private Date postDate;

	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@ConsumeField
	private String version;

	@NotNull
	@ValidValueType(
			{
				"A", "P"
			})
	@ConsumeField
	private String approvalState;

	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	private String approvedUser;
	private Date approvedDts;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	private String createUser;

	@NotNull
	private Date createDts;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	private String updateUser;

	@NotNull
	private Date updateDts;

	@NotNull
	private Date lastActivityDts;

	public Component()
	{
	}
	
	@Override
	public void setPrimaryKey(String itemId, String itemCode, String componentId)
	{
		componentId = itemId;
	}
	
	@Override
	public void setPrimaryKey(Object pk)
	{
		componentId = (String)pk;
	}
	
	@Override
	public Object getPrimaryKey()
	{
		return componentId;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
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

	public String getParentComponentId()
	{
		return parentComponentId;
	}

	public void setParentComponentId(String parentComponentId)
	{
		this.parentComponentId = parentComponentId;
	}

	public String getGuid()
	{
		return guid;
	}

	public void setGuid(String guid)
	{
		this.guid = guid;
	}

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getApprovalState()
	{
		return approvalState;
	}

	public void setApprovalState(String approvalState)
	{
		this.approvalState = approvalState;
	}

	public String getApprovedUser()
	{
		return approvedUser;
	}

	public void setApprovedUser(String approvedUser)
	{
		this.approvedUser = approvedUser;
	}

	public Date getApprovedDts()
	{
		return approvedDts;
	}

	public void setApprovedDts(Date approvedDts)
	{
		this.approvedDts = approvedDts;
	}

	@Override
	public String getCreateUser()
	{
		return createUser;
	}

	/**
	 *
	 * @param createUser
	 */
	@Override
	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Date getCreateDts()
	{
		return createDts;
	}

	/**
	 *
	 * @param createDts
	 */
	@Override
	public void setCreateDts(Date createDts)
	{
		this.createDts = createDts;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String getUpdateUser()
	{
		return updateUser;
	}

	/**
	 *
	 * @param updateUser
	 */
	@Override
	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Date getUpdateDts()
	{
		return updateDts;
	}

	/**
	 *
	 * @param updateDts
	 */
	@Override
	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public Date getLastActivityDts()
	{
		return lastActivityDts;
	}

	public void setLastActivityDts(Date lastActivityDts)
	{
		this.lastActivityDts = lastActivityDts;
	}

	public Date getReleaseDate()
	{
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate)
	{
		this.releaseDate = releaseDate;
	}

	public Date getPostDate()
	{
		return postDate;
	}

	public void setPostDate(Date postDate)
	{
		this.postDate = postDate;
	}

}
