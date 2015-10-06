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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Holds user group information")
public class UserGroup
		extends StandardEntity<UserGroup>
{

	@PK(generated = true)
	private String userGroupId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	@FK(UserProfile.class)
	private String username;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String role;

	public UserGroup()
	{
	}

	public String getRole()
	{
		return role;
	}

	public void setRole(String role)
	{
		this.role = role;
	}

	public String getUserGroupId()
	{
		return userGroupId;
	}

	public void setUserGroupId(String userGroupId)
	{
		this.userGroupId = userGroupId;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

}
