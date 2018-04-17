/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.core.annotation.PK;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Stores user dashboard")
public class UserDashboard
		extends StandardEntity<UserDashboard>
{

	public static final String DEFAULT_NAME = "DEFAULT";

	@PK(generated = true)
	@NotNull
	private String dashboardId;

	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	@NotNull
	private String username;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_80)
	private String name;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_16K)
	@APIDescription("UI can use this field to store state.")
	private String dashboardState;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public UserDashboard()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		UserDashboard userDashboard = (UserDashboard) entity;
		this.setName(userDashboard.getName());
		this.setDashboardState(userDashboard.getDashboardState());

	}

	public String getDashboardId()
	{
		return dashboardId;
	}

	public void setDashboardId(String dashboardId)
	{
		this.dashboardId = dashboardId;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDashboardState()
	{
		return dashboardState;
	}

	public void setDashboardState(String dashboardState)
	{
		this.dashboardState = dashboardState;
	}

}
