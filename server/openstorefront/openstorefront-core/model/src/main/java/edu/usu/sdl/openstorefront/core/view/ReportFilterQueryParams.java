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
package edu.usu.sdl.openstorefront.core.view;

import javax.ws.rs.QueryParam;

/**
 *
 * @author dshurtleff
 */
public class ReportFilterQueryParams
		extends FilterQueryParams
{

	@QueryParam("showAllUsers")
	private boolean showAllUsers;

	@QueryParam("showScheduledOnly")
	private boolean showScheduledOnly;

	@QueryParam("reportType")
	private String reportType;

	public boolean getShowAllUsers()
	{
		return showAllUsers;
	}

	public void setShowAllUsers(boolean showAllUsers)
	{
		this.showAllUsers = showAllUsers;
	}

	public String getReportType()
	{
		return reportType;
	}

	public void setReportType(String reportType)
	{
		this.reportType = reportType;
	}

	public boolean getShowScheduledOnly()
	{
		return showScheduledOnly;
	}

	public void setShowScheduledOnly(Boolean showScheduledOnly)
	{
		this.showScheduledOnly = showScheduledOnly;
	}

}
