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

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import java.util.List;
import javax.persistence.OneToMany;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Holds the security role information")
public class SecurityRole
		extends StandardEntity<SecurityRole>
{

	private String roleName;

	private String description;

	private String landingPage;

	@OneToMany(orphanRemoval = true)
	private List<SecurityRolePermission> permissions;

	@OneToMany(orphanRemoval = true)
	private List<SecurityRoleData> dataSecurity;

	private Boolean allowUnspecifiedDataSource;

	private Boolean allowUnspecifiedDataSensitivity;

	public SecurityRole()
	{
	}

	public String getRoleName()
	{
		return roleName;
	}

	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getLandingPage()
	{
		return landingPage;
	}

	public void setLandingPage(String landingPage)
	{
		this.landingPage = landingPage;
	}

	public List<SecurityRolePermission> getPermissions()
	{
		return permissions;
	}

	public void setPermissions(List<SecurityRolePermission> permissions)
	{
		this.permissions = permissions;
	}

	public List<SecurityRoleData> getDataSecurity()
	{
		return dataSecurity;
	}

	public void setDataSecurity(List<SecurityRoleData> dataSecurity)
	{
		this.dataSecurity = dataSecurity;
	}

	public Boolean getAllowUnspecifiedDataSource()
	{
		return allowUnspecifiedDataSource;
	}

	public void setAllowUnspecifiedDataSource(Boolean allowUnspecifiedDataSource)
	{
		this.allowUnspecifiedDataSource = allowUnspecifiedDataSource;
	}

	public Boolean getAllowUnspecifiedDataSensitivity()
	{
		return allowUnspecifiedDataSensitivity;
	}

	public void setAllowUnspecifiedDataSensitivity(Boolean allowUnspecifiedDataSensitivity)
	{
		this.allowUnspecifiedDataSensitivity = allowUnspecifiedDataSensitivity;
	}

}
