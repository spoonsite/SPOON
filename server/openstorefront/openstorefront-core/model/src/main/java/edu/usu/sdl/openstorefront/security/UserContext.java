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
package edu.usu.sdl.openstorefront.security;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.entity.SecurityRole;
import edu.usu.sdl.openstorefront.core.entity.SecurityRoleData;
import edu.usu.sdl.openstorefront.core.entity.SecurityRolePermission;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * Holds user info for a session
 *
 * @author dshurtleff
 */
public class UserContext
		implements Serializable
{

	private UserProfile userProfile;
	private List<SecurityRole> roles = new ArrayList<>();
	private boolean admin;

	public UserContext()
	{
	}

	@Override
	public String toString()
	{
		if (userProfile != null) {
			return userProfile.getUsername();
		}
		return OpenStorefrontConstant.ANONYMOUS_USER;
	}

	public Set<String> roles()
	{
		Set<String> uniqueRoles = new HashSet<>();
		for (SecurityRole role : roles) {
			uniqueRoles.add(role.getRoleName());
		}
		return uniqueRoles;
	}

	public Set<String> permissions()
	{
		Set<String> uniquePermissions = new HashSet<>();
		for (SecurityRole role : roles) {
			for (SecurityRolePermission securityPermission : role.getPermissions()) {
				uniquePermissions.add(securityPermission.getPermission());
			}
		}
		return uniquePermissions;
	}

	public Set<String> dataSources()
	{
		Set<String> uniqueSources = new HashSet<>();
		for (SecurityRole role : roles) {
			for (SecurityRoleData securityRoleData : role.getDataSecurity()) {
				if (StringUtils.isNotBlank(securityRoleData.getDataSource())) {
					uniqueSources.add(securityRoleData.getDataSource());
				}
			}
		}
		return uniqueSources;
	}

	public Set<String> dataSensitivity()
	{
		Set<String> uniqueSensitivity = new HashSet<>();
		for (SecurityRole role : roles) {
			for (SecurityRoleData securityRoleData : role.getDataSecurity()) {
				if (StringUtils.isNotBlank(securityRoleData.getDataSensitivity())) {
					uniqueSensitivity.add(securityRoleData.getDataSensitivity());
				}
			}
		}
		return uniqueSensitivity;
	}

	public UserProfile getUserProfile()
	{
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile)
	{
		this.userProfile = userProfile;
	}

	public boolean isAdmin()
	{
		return admin;
	}

	public void setAdmin(boolean admin)
	{
		this.admin = admin;
	}

	public List<SecurityRole> getRoles()
	{
		return roles;
	}

	public void setRoles(List<SecurityRole> roles)
	{
		this.roles = roles;
	}

}
