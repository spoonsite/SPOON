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
	private Set<String> externalGroups = new HashSet<>();

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
			if (role.getPermissions() != null) {
				for (SecurityRolePermission securityPermission : role.getPermissions()) {
					uniquePermissions.add(securityPermission.getPermission());
				}
			}
		}
		return uniquePermissions;
	}

	public boolean allowUnspecifiedDataSources()
	{
		boolean allow = false;

		for (SecurityRole role : roles) {
			if (role.getAllowUnspecifiedDataSource()) {
				allow = true;
				break;
			}
		}

		return allow;
	}

	public boolean allowUnspecifiedDataSensitivty()
	{
		boolean allow = false;

		for (SecurityRole role : roles) {
			if (role.getAllowUnspecifiedDataSensitivity()) {
				allow = true;
				break;
			}
		}

		return allow;
	}

	public Set<String> dataSources()
	{
		Set<String> uniqueSources = new HashSet<>();
		for (SecurityRole role : roles) {
			if (role.getDataSecurity() != null) {
				for (SecurityRoleData securityRoleData : role.getDataSecurity()) {
					if (StringUtils.isNotBlank(securityRoleData.getDataSource())) {
						uniqueSources.add(securityRoleData.getDataSource());
					}
				}
			}
		}
		return uniqueSources;
	}

	public Set<String> dataSensitivity()
	{
		Set<String> uniqueSensitivity = new HashSet<>();
		for (SecurityRole role : roles) {
			if (role.getDataSecurity() != null) {
				for (SecurityRoleData securityRoleData : role.getDataSecurity()) {
					if (StringUtils.isNotBlank(securityRoleData.getDataSensitivity())) {
						uniqueSensitivity.add(securityRoleData.getDataSensitivity());
					}
				}
			}
		}
		return uniqueSensitivity;
	}

	public String userLandingPage()
	{
		String landingPage = "/";
		if (userProfile != null) {
			if (StringUtils.isNotBlank(userProfile.getLandingPage())) {
				landingPage = userProfile.getLandingPage();
			} else {
				if (!roles.isEmpty()) {
					//highest priority should be top
					roles.sort((role1, role2) -> {

						if (role1.getLandingPagePriority() == null && role2.getLandingPagePriority() == null) {
							return 0;
						} else if (role1.getLandingPagePriority() != null && role2.getLandingPagePriority() == null) {
							return -1;
						} else if (role1.getLandingPagePriority() == null && role2.getLandingPagePriority() != null) {
							return 1;
						} else if (role1.getLandingPagePriority() != null && role2.getLandingPagePriority() != null) {
							return role2.getLandingPagePriority().compareTo(role1.getLandingPagePriority());
						}

						return 0;
					});
					if (StringUtils.isNotBlank(roles.get(0).getLandingPage())) {
						landingPage = roles.get(0).getLandingPage();
					}
				}
			}
		}
		return landingPage;
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

	public Set<String> getExternalGroups()
	{
		return externalGroups;
	}

	public void setExternalGroups(Set<String> externalGroups)
	{
		this.externalGroups = externalGroups;
	}

}
