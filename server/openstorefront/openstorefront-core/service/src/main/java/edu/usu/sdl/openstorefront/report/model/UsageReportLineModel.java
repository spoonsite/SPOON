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
package edu.usu.sdl.openstorefront.report.model;

/**
 *
 * @author dshurtleff
 */
public class UsageReportLineModel
{

	private String username;
	private String userGUID;
	private String userOrganization;
	private String userType;
	private String userEmail;
	private long numberOfLogins;
	private long componentViews;
	private long resourcesClicked;

	public UsageReportLineModel()
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

	public String getUserGUID()
	{
		return userGUID;
	}

	public void setUserGUID(String userGUID)
	{
		this.userGUID = userGUID;
	}

	public String getUserOrganization()
	{
		return userOrganization;
	}

	public void setUserOrganization(String userOrganization)
	{
		this.userOrganization = userOrganization;
	}

	public String getUserType()
	{
		return userType;
	}

	public void setUserType(String userType)
	{
		this.userType = userType;
	}

	public String getUserEmail()
	{
		return userEmail;
	}

	public void setUserEmail(String userEmail)
	{
		this.userEmail = userEmail;
	}

	public long getNumberOfLogins()
	{
		return numberOfLogins;
	}

	public void setNumberOfLogins(long numberOfLogins)
	{
		this.numberOfLogins = numberOfLogins;
	}

	public long getComponentViews()
	{
		return componentViews;
	}

	public void setComponentViews(long componentViews)
	{
		this.componentViews = componentViews;
	}

	public long getResourcesClicked()
	{
		return resourcesClicked;
	}

	public void setResourcesClicked(long resourcesClicked)
	{
		this.resourcesClicked = resourcesClicked;
	}

}
