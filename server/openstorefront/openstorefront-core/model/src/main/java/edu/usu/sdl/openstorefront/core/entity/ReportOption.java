/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embeddable;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * Part of Report/Scheduled Report
 *
 * @author dshurtleff
 */
@APIDescription("Holds report options")
@Embeddable
public class ReportOption
		implements Serializable
{

	@ConsumeField
	private Date startDts;

	@ConsumeField
	private Date endDts;
	
	@ConsumeField
	private boolean displayContacts;
	
	@ConsumeField
	private boolean displayDependencies;
	
	@ConsumeField
	private boolean displayDescription;
	
	@ConsumeField
	private boolean displayDetails;
	
	@ConsumeField
	private boolean displayEvalSummary;
	
	@ConsumeField
	private boolean displayEvalVersions;
	
	@ConsumeField
	private boolean displayMetaData;
	
	@ConsumeField
	private boolean displayOrgData;
	
	@ConsumeField
	private boolean displayQA;
	
	@ConsumeField
	private boolean displayRelationships;
	
	@ConsumeField
	private boolean displayReportReviews;
	
	@ConsumeField
	private boolean displayResources;
	
	@ConsumeField
	private boolean displayTags;
	
	@ConsumeField
	private boolean displayVitals;

	@Min(1)
	@Max(30)
	@ConsumeField
	private Integer previousDays;

	@Min(1)
	@Max(300)
	@ConsumeField
	private Integer maxWaitSeconds;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	private String category;
	
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	private String assignedUser;
	
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	private String assignedGroup;

	@Version
	private String storageVersion;

	public ReportOption()
	{
	}

	public Date getStartDts()
	{
		return startDts;
	}

	public void setStartDts(Date startDts)
	{
		this.startDts = startDts;
	}

	public Date getEndDts()
	{
		return endDts;
	}

	public void setEndDts(Date endDts)
	{
		this.endDts = endDts;
	}

	public Integer getPreviousDays()
	{
		return previousDays;
	}

	public void setPreviousDays(Integer previousDays)
	{
		this.previousDays = previousDays;
	}

	public String getStorageVersion()
	{
		return storageVersion;
	}

	public void setStorageVersion(String storageVersion)
	{
		this.storageVersion = storageVersion;
	}

	public Integer getMaxWaitSeconds()
	{
		return maxWaitSeconds;
	}

	public void setMaxWaitSeconds(Integer maxWaitSeconds)
	{
		this.maxWaitSeconds = maxWaitSeconds;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public String getAssignedUser()
	{
		return assignedUser;
	}

	public void setAssignedUser(String assignedUser)
	{
		this.assignedUser = assignedUser;
	}

	public String getAssignedGroup()
	{
		return assignedGroup;
	}

	public void setAssignedGroup(String assignedGroup)
	{
		this.assignedGroup = assignedGroup;
	}
	
	public boolean getDisplayContacts()
	{
		return displayContacts;
	}

	public void setDisplayContacts(boolean displayContacts)
	{
		this.displayContacts = displayContacts;
	}

	public boolean getDisplayDependencies()
	{
		return displayDependencies;
	}

	public void setDisplayDependencies(boolean displayDependencies)
	{
		this.displayDependencies = displayDependencies;
	}

	public boolean getDisplayDescription()
	{
		return displayDescription;
	}

	public void setDisplayDescription(boolean displayDescription)
	{
		this.displayDescription = displayDescription;
	}

	public boolean getDisplayDetails()
	{
		return displayDetails;
	}

	public void setDisplayDetails(boolean displayDetails)
	{
		this.displayDetails = displayDetails;
	}

	public boolean getDisplayEvalSummary()
	{
		return displayEvalSummary;
	}

	public void setDisplayEvalSummary(boolean displayEvalSummary)
	{
		this.displayEvalSummary = displayEvalSummary;
	}

	public boolean getDisplayEvalVersions()
	{
		return displayEvalVersions;
	}

	public void setDisplayEvalVersions(boolean displayEvalVersions)
	{
		this.displayEvalVersions = displayEvalVersions;
	}

	public boolean getDisplayMetaData()
	{
		return displayMetaData;
	}

	public void setDisplayMetaData(boolean displayMetaData)
	{
		this.displayMetaData = displayMetaData;
	}

	public boolean getDisplayOrgData()
	{
		return displayOrgData;
	}

	public void setDisplayOrgData(boolean displayOrgData)
	{
		this.displayOrgData = displayOrgData;
	}

	public boolean getDisplayQA()
	{
		return displayQA;
	}

	public void setDisplayQA(boolean displayQA)
	{
		this.displayQA = displayQA;
	}

	public boolean getDisplayRelationships()
	{
		return displayRelationships;
	}

	public void setDisplayRelationships(boolean displayRelationships)
	{
		this.displayRelationships = displayRelationships;
	}

	public boolean getDisplayReportReviews()
	{
		return displayReportReviews;
	}

	public void setDisplayReportReviews(boolean displayReportReviews)
	{
		this.displayReportReviews = displayReportReviews;
	}

	public boolean getDisplayResources()
	{
		return displayResources;
	}

	public void setDisplayResources(boolean displayResources)
	{
		this.displayResources = displayResources;
	}

	public boolean getDisplayTags()
	{
		return displayTags;
	}

	public void setDisplayTags(boolean displayTags)
	{
		this.displayTags = displayTags;
	}

	public boolean getDisplayVitals()
	{
		return displayVitals;
	}

	public void setDisplayVitals(boolean displayVitals)
	{
		this.displayVitals = displayVitals;
	}
}
