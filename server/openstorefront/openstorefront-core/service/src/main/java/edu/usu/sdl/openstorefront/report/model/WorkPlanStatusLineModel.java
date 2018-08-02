/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
public class WorkPlanStatusLineModel
{

	private String linkName;
	private String linkType;
	private String workPlanName;
	private String currentStepName;
	private String currentAssignee;
	private String currentAssignedGroup;
	private String currentSubStatus;
	private Date lastUpdateChangeDts;
	private long daysSincesLastUpdate;
	private long numberOfComments;
	private Date lastCommentUpdate;
	private String workPlanLinkId;

	public WorkPlanStatusLineModel()
	{
	}

	public String getLinkName()
	{
		return linkName;
	}

	public void setLinkName(String linkName)
	{
		this.linkName = linkName;
	}

	public String getLinkType()
	{
		return linkType;
	}

	public void setLinkType(String linkType)
	{
		this.linkType = linkType;
	}

	public String getWorkPlanName()
	{
		return workPlanName;
	}

	public void setWorkPlanName(String workPlanName)
	{
		this.workPlanName = workPlanName;
	}

	public String getCurrentStepName()
	{
		return currentStepName;
	}

	public void setCurrentStepName(String currentStepName)
	{
		this.currentStepName = currentStepName;
	}

	public String getCurrentAssignee()
	{
		return currentAssignee;
	}

	public void setCurrentAssignee(String currentAssignee)
	{
		this.currentAssignee = currentAssignee;
	}

	public String getCurrentAssignedGroup()
	{
		return currentAssignedGroup;
	}

	public void setCurrentAssignedGroup(String currentAssignedGroup)
	{
		this.currentAssignedGroup = currentAssignedGroup;
	}

	public String getCurrentSubStatus()
	{
		return currentSubStatus;
	}

	public void setCurrentSubStatus(String currentSubStatus)
	{
		this.currentSubStatus = currentSubStatus;
	}

	public Date getLastUpdateChangeDts()
	{
		return lastUpdateChangeDts;
	}

	public void setLastUpdateChangeDts(Date lastUpdateChangeDts)
	{
		this.lastUpdateChangeDts = lastUpdateChangeDts;
	}

	public long getDaysSincesLastUpdate()
	{
		return daysSincesLastUpdate;
	}

	public void setDaysSincesLastUpdate(long daysSincesLastUpdate)
	{
		this.daysSincesLastUpdate = daysSincesLastUpdate;
	}

	public long getNumberOfComments()
	{
		return numberOfComments;
	}

	public void setNumberOfComments(long numberOfComments)
	{
		this.numberOfComments = numberOfComments;
	}

	public String getWorkPlanLinkId()
	{
		return workPlanLinkId;
	}

	public void setWorkPlanLinkId(String workPlanLinkId)
	{
		this.workPlanLinkId = workPlanLinkId;
	}

	public Date getLastCommentUpdate()
	{
		return lastCommentUpdate;
	}

	public void setLastCommentUpdate(Date lastCommentUpdate)
	{
		this.lastCommentUpdate = lastCommentUpdate;
	}

}
