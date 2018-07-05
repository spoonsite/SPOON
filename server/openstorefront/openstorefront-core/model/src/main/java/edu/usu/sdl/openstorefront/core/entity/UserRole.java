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
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Holds link from user to role")
public class UserRole
		extends StandardEntity<UserRole>
{

	@PK(generated = true)
	@NotNull
	private String userRoleId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	@FK(UserProfile.class)
	@ConsumeField
	private String username;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@FK(SecurityRole.class)
	@ConsumeField
	private String role;
	
	private Boolean keep;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public UserRole()
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

	public String getUserRoleId()
	{
		return userRoleId;
	}

	public void setUserRoleId(String userRoleId)
	{
		this.userRoleId = userRoleId;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public Boolean getKeep()
	{
		return keep;
	}

	public void setKeep(Boolean keep)
	{
		this.keep = keep;
	}
}
