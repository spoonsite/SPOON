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
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
public class AttributeType
		extends BaseEntity
{

	@PK
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	private String attributeType;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_DESCRIPTION)
	private String description;

	@NotNull
	private Boolean visibleFlg;

	@NotNull
	private Boolean requiredFlg;

	@NotNull
	private Boolean architectureFlg;

	@NotNull
	private Boolean importantFlg;

	@NotNull
	private Boolean allowMutlipleFlg;

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

	public AttributeType()
	{
	}

	public String getAttributeType()
	{
		return attributeType;
	}

	public void setAttributeType(String attributeType)
	{
		this.attributeType = attributeType;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Boolean getVisibleFlg()
	{
		return visibleFlg;
	}

	public void setVisibleFlg(Boolean visibleFlg)
	{
		this.visibleFlg = visibleFlg;
	}

	public Boolean getRequiredFlg()
	{
		return requiredFlg;
	}

	public void setRequiredFlg(Boolean requiredFlg)
	{
		this.requiredFlg = requiredFlg;
	}

	public Boolean getArchitectureFlg()
	{
		return architectureFlg;
	}

	public void setArchitectureFlg(Boolean architectureFlg)
	{
		this.architectureFlg = architectureFlg;
	}

	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	public Date getCreateDts()
	{
		return createDts;
	}

	public void setCreateDts(Date createDts)
	{
		this.createDts = createDts;
	}

	public String getUpdateUser()
	{
		return updateUser;
	}

	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public Boolean getImportantFlg()
	{
		return importantFlg;
	}

	public void setImportantFlg(Boolean importantFlg)
	{
		this.importantFlg = importantFlg;
	}

	public Boolean getAllowMutlipleFlg()
	{
		return allowMutlipleFlg;
	}

	public void setAllowMutlipleFlg(Boolean allowMutlipleFlg)
	{
		this.allowMutlipleFlg = allowMutlipleFlg;
	}

}
