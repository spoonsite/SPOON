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
public class EntryStatusDetailModel
{
	private String name;
	private String entryType;
	private String description;
	private String createUser;
	private Date createDts;	
	private Date lastVendorUpdateApproveDate;
	private Date lastSystemUpdDate;
	private String createUserEmail;
	private String createUserOrganization;
	private String status;
	private boolean published;
	private boolean userSubmitted;
	private Date submissionDate;

	public EntryStatusDetailModel()
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

	public String getEntryType()
	{
		return entryType;
	}

	public void setEntryType(String entryType)
	{
		this.entryType = entryType;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	public Date getCreateDts()
	{
		return createDts;
	}

	public void setCreateDts(Date createDts)
	{
		this.createDts = createDts;
	}

	public Date getLastVendorUpdateApproveDate() {
		return this.lastVendorUpdateApproveDate;
	}

	public void setLastVendorUpdateApproveDate(Date lastVendorUpdateApproveDate) {
		this.lastVendorUpdateApproveDate = lastVendorUpdateApproveDate;
	}

	public Date getLastSystemUpdDate() {
		return this.lastSystemUpdDate;
	}

	public void setLastSystemUpdDate(Date lastSystemUpdDate) {
		this.lastSystemUpdDate = lastSystemUpdDate;
	}

	public String getCreateUserEmail()
	{
		return createUserEmail;
	}

	public void setCreateUserEmail(String createUserEmail)
	{
		this.createUserEmail = createUserEmail;
	}

	public String getCreateUserOrganization()
	{
		return createUserOrganization;
	}

	public void setCreateUserOrganization(String createUserOrganization)
	{
		this.createUserOrganization = createUserOrganization;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public boolean getPublished()
	{
		return published;
	}

	public void setPublished(boolean published)
	{
		this.published = published;
	}

	public boolean getUserSubmitted()
	{
		return userSubmitted;
	}

	public void setUserSubmitted(boolean userSubmitted)
	{
		this.userSubmitted = userSubmitted;
	}

	public Date getSubmissionDate()
	{
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate)
	{
		this.submissionDate = submissionDate;
	}
	
}
