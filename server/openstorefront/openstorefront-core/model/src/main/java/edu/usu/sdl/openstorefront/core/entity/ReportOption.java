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

}
