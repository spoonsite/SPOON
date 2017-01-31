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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Security account; used in the builtin security realm.")
public class UserSecurity
	extends StandardEntity<UserSecurity>		
{
	@PK
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	private String username;
	
	@NotNull	
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_80)
	@APIDescription("Only Applicatble when using internal security; This is hashed")	
	private transient String password;	
	
	@NotNull
	private transient String userSalt;
	
	@NotNull
	@Min(0)
	@APIDescription("This will reset upon successful login")
	private Integer failLoginAttempts;

	public UserSecurity()
	{
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getUserSalt()
	{
		return userSalt;
	}

	public void setUserSalt(String userSalt)
	{
		this.userSalt = userSalt;
	}

	public Integer getFailLoginAttempts()
	{
		return failLoginAttempts;
	}

	public void setFailLoginAttempts(Integer failLoginAttempts)
	{
		this.failLoginAttempts = failLoginAttempts;
	}
	
}
