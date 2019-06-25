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
public class EntryListingReportLineModel
{

	public static final String FIELD_LAST_UPDATED_DTS = "lastUpdatedDts";

	private String componentId;
	private String name;
	private String shortDescription;
	private String entryType;
	private Date lastSubmitDts;
	private Date lastUpdatedDts;
	private Date lastVendorUpdateApprovedDate;
	private String evaluationStatus;
	private String viewLink;

	public EntryListingReportLineModel()
	{
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getShortDescription()
	{
		return shortDescription;
	}

	public void setShortDescription(String shortDescription)
	{
		this.shortDescription = shortDescription;
	}

	public String getEntryType()
	{
		return entryType;
	}

	public void setEntryType(String entryType)
	{
		this.entryType = entryType;
	}

	public Date getLastSubmitDts() {
		return this.lastSubmitDts;
	}

	public void setLastSubmitDts(Date lastSubmitDts) {
		this.lastSubmitDts = lastSubmitDts;
	}

	public Date getLastUpdatedDts()
	{
		return lastUpdatedDts;
	}

	public void setLastUpdatedDts(Date lastUpdatedDts)
	{
		this.lastUpdatedDts = lastUpdatedDts;
	}

	public Date getlastVendorUpdateApprovedDate()
	{
		return lastVendorUpdateApprovedDate;
	}

	public void setlastVendorUpdateApprovedDate(Date lastVendorUpdateApprovedDate)
	{
		this.lastVendorUpdateApprovedDate = lastVendorUpdateApprovedDate;
	}

	public String getEvaluationStatus()
	{
		return evaluationStatus;
	}

	public void setEvaluationStatus(String evaluationStatus)
	{
		this.evaluationStatus = evaluationStatus;
	}

	public String getViewLink()
	{
		return viewLink;
	}

	public void setViewLink(String viewLink)
	{
		this.viewLink = viewLink;
	}

}
