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
public class EntryOrgDetailModel
{

	private String name;
	private Date lastSubmitDts;
	private Date lastActivityDts;
	private Date approvedDts;
	private String approvalState;

	public EntryOrgDetailModel()
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

	public Date getLastSubmitDts() {
		return this.lastSubmitDts;
	}

	public void setLastSubmitDts(Date lastSubmitDts) {
		this.lastSubmitDts = lastSubmitDts;
	}

	public Date getLastActivityDts()
	{
		return lastActivityDts;
	}

	public void setLastActivityDts(Date lastActivityDts)
	{
		this.lastActivityDts = lastActivityDts;
	}

	public Date getApprovedDts() {
		return this.approvedDts;
	}

	public void setApprovedDts(Date approvedDts) {
		this.approvedDts = approvedDts;
	}

	public String getApprovalState()
	{
		return approvalState;
	}

	public void setApprovalState(String approvalState)
	{
		this.approvalState = approvalState;
	}

}
