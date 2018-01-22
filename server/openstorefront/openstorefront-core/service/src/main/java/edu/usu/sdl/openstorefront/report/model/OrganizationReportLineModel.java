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
public class OrganizationReportLineModel
{

	private String organization;
	private long users;
	private long reviews;
	private long questions;
	private long questionResponse;
	private long componentViews;
	private long componentResourcesClick;
	private long logins;

	public OrganizationReportLineModel()
	{
	}

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public long getUsers()
	{
		return users;
	}

	public void setUsers(long users)
	{
		this.users = users;
	}

	public long getReviews()
	{
		return reviews;
	}

	public void setReviews(long reviews)
	{
		this.reviews = reviews;
	}

	public long getQuestions()
	{
		return questions;
	}

	public void setQuestions(long questions)
	{
		this.questions = questions;
	}

	public long getQuestionResponse()
	{
		return questionResponse;
	}

	public void setQuestionResponse(long questionResponse)
	{
		this.questionResponse = questionResponse;
	}

	public long getComponentViews()
	{
		return componentViews;
	}

	public void setComponentViews(long componentViews)
	{
		this.componentViews = componentViews;
	}

	public long getComponentResourcesClick()
	{
		return componentResourcesClick;
	}

	public void setComponentResourcesClick(long componentResourcesClick)
	{
		this.componentResourcesClick = componentResourcesClick;
	}

	public long getLogins()
	{
		return logins;
	}

	public void setLogins(long logins)
	{
		this.logins = logins;
	}

}
