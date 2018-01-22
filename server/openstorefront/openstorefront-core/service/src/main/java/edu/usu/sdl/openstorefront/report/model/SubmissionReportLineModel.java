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

import java.util.Date;

/**
 *
 * @author dshurtleff
 */
public class SubmissionReportLineModel
{

	private String name;
	private Date createDts;
	private Date updateDts;
	private Date submittedDts;
	private String submitterName;
	private String submitterEmail;
	private String submitterPhone;
	private String submitterOrganization;
	private String loggedInUser;
	private String currentAprovalStatus;
	private String activeStatus;

	public SubmissionReportLineModel()
	{
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Date getCreateDts()
	{
		return createDts;
	}

	public void setCreateDts(Date createDts)
	{
		this.createDts = createDts;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public Date getSubmittedDts()
	{
		return submittedDts;
	}

	public void setSubmittedDts(Date submittedDts)
	{
		this.submittedDts = submittedDts;
	}

	public String getSubmitterName()
	{
		return submitterName;
	}

	public void setSubmitterName(String submitterName)
	{
		this.submitterName = submitterName;
	}

	public String getSubmitterEmail()
	{
		return submitterEmail;
	}

	public void setSubmitterEmail(String submitterEmail)
	{
		this.submitterEmail = submitterEmail;
	}

	public String getLoggedInUser()
	{
		return loggedInUser;
	}

	public void setLoggedInUser(String loggedInUser)
	{
		this.loggedInUser = loggedInUser;
	}

	public String getCurrentAprovalStatus()
	{
		return currentAprovalStatus;
	}

	public void setCurrentAprovalStatus(String currentAprovalStatus)
	{
		this.currentAprovalStatus = currentAprovalStatus;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public String getSubmitterOrganization()
	{
		return submitterOrganization;
	}

	public void setSubmitterOrganization(String submitterOrganization)
	{
		this.submitterOrganization = submitterOrganization;
	}

	public String getSubmitterPhone()
	{
		return submitterPhone;
	}

	public void setSubmitterPhone(String submitterPhone)
	{
		this.submitterPhone = submitterPhone;
	}

}
