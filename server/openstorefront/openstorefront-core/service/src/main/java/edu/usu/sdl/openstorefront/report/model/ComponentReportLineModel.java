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
public class ComponentReportLineModel
{

	private String name;
	private String organization;
	private Date lastActivityDts;
	private String approvalStatus;
	private Date approvalDts;
	private String approvalUser;
	private String activeStatus;
	private Date createDts;
	private String createUser;
	private Date lastViewed;
	private long views;
	private long resourcesClicked;
	private long activeReviews;
	private long tags;
	private long activeQuestions;
	private long activeQuestionResponses;

	public ComponentReportLineModel()
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

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public Date getLastActivityDts()
	{
		return lastActivityDts;
	}

	public void setLastActivityDts(Date lastActivityDts)
	{
		this.lastActivityDts = lastActivityDts;
	}

	public String getApprovalStatus()
	{
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus)
	{
		this.approvalStatus = approvalStatus;
	}

	public Date getApprovalDts()
	{
		return approvalDts;
	}

	public void setApprovalDts(Date approvalDts)
	{
		this.approvalDts = approvalDts;
	}

	public String getApprovalUser()
	{
		return approvalUser;
	}

	public void setApprovalUser(String approvalUser)
	{
		this.approvalUser = approvalUser;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public Date getCreateDts()
	{
		return createDts;
	}

	public void setCreateDts(Date createDts)
	{
		this.createDts = createDts;
	}

	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	public Date getLastViewed()
	{
		return lastViewed;
	}

	public void setLastViewed(Date lastViewed)
	{
		this.lastViewed = lastViewed;
	}

	public long getViews()
	{
		return views;
	}

	public void setViews(long views)
	{
		this.views = views;
	}

	public long getResourcesClicked()
	{
		return resourcesClicked;
	}

	public void setResourcesClicked(long resourcesClicked)
	{
		this.resourcesClicked = resourcesClicked;
	}

	public long getActiveReviews()
	{
		return activeReviews;
	}

	public void setActiveReviews(long activeReviews)
	{
		this.activeReviews = activeReviews;
	}

	public long getTags()
	{
		return tags;
	}

	public void setTags(long tags)
	{
		this.tags = tags;
	}

	public long getActiveQuestions()
	{
		return activeQuestions;
	}

	public void setActiveQuestions(long activeQuestions)
	{
		this.activeQuestions = activeQuestions;
	}

	public long getActiveQuestionResponses()
	{
		return activeQuestionResponses;
	}

	public void setActiveQuestionResponses(long activeQuestionResponses)
	{
		this.activeQuestionResponses = activeQuestionResponses;
	}

}
