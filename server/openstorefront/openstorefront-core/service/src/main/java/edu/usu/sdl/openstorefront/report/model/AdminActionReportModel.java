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

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.FeedbackTicket;
import edu.usu.sdl.openstorefront.core.entity.UserSecurity;
import edu.usu.sdl.openstorefront.core.view.ComponentQuestionResponseView;
import edu.usu.sdl.openstorefront.core.view.ComponentQuestionView;
import edu.usu.sdl.openstorefront.core.view.ComponentReviewView;
import edu.usu.sdl.openstorefront.core.view.EvaluationView;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class AdminActionReportModel
		extends BaseReportModel
{

	private List<Component> pendingAdminEntries = new ArrayList<>();
	private List<Component> pendingUserEntries = new ArrayList<>();
	private List<Component> changeRequests = new ArrayList<>();
	private List<ComponentReviewView> pendingReviews = new ArrayList<>();
	private List<ComponentQuestionView> pendingQuestions = new ArrayList<>();
	private List<ComponentQuestionResponseView> pendingResponses = new ArrayList<>();
	private List<FeedbackTicket> pendingFeedbackTickets = new ArrayList<>();
	private List<UserSecurity> pendingUsers = new ArrayList<>();
	private List<EvaluationView> pendingEvaluations = new ArrayList<>();

	public AdminActionReportModel()
	{
	}

	public boolean outstandingItems()
	{
		boolean outstanding = false;

		if (!pendingAdminEntries.isEmpty()
				|| !pendingUserEntries.isEmpty()
				|| !changeRequests.isEmpty()
				|| !pendingReviews.isEmpty()
				|| !pendingQuestions.isEmpty()
				|| !pendingResponses.isEmpty()
				|| !pendingFeedbackTickets.isEmpty()
				|| !pendingUsers.isEmpty()
				|| !pendingEvaluations.isEmpty()) {
			outstanding = true;
		}
		return outstanding;
	}

	public int totalActions()
	{
		int total = pendingAdminEntries.size()
				+ pendingUserEntries.size()
				+ changeRequests.size()
				+ pendingReviews.size()
				+ pendingQuestions.size()
				+ pendingResponses.size()
				+ pendingFeedbackTickets.size()
				+ pendingUsers.size()
				+ pendingEvaluations.size();
		return total;
	}

	@Override
	public <T> List<T> getData()
	{
		return new ArrayList<>();
	}

	public List<Component> getPendingAdminEntries()
	{
		return pendingAdminEntries;
	}

	public void setPendingAdminEntries(List<Component> pendingAdminEntries)
	{
		this.pendingAdminEntries = pendingAdminEntries;
	}

	public List<Component> getPendingUserEntries()
	{
		return pendingUserEntries;
	}

	public void setPendingUserEntries(List<Component> pendingUserEntries)
	{
		this.pendingUserEntries = pendingUserEntries;
	}

	public List<Component> getChangeRequests()
	{
		return changeRequests;
	}

	public void setChangeRequests(List<Component> changeRequests)
	{
		this.changeRequests = changeRequests;
	}

	public List<ComponentReviewView> getPendingReviews()
	{
		return pendingReviews;
	}

	public void setPendingReviews(List<ComponentReviewView> pendingReviews)
	{
		this.pendingReviews = pendingReviews;
	}

	public List<ComponentQuestionView> getPendingQuestions()
	{
		return pendingQuestions;
	}

	public void setPendingQuestions(List<ComponentQuestionView> pendingQuestions)
	{
		this.pendingQuestions = pendingQuestions;
	}

	public List<ComponentQuestionResponseView> getPendingResponses()
	{
		return pendingResponses;
	}

	public void setPendingResponses(List<ComponentQuestionResponseView> pendingResponses)
	{
		this.pendingResponses = pendingResponses;
	}

	public List<FeedbackTicket> getPendingFeedbackTickets()
	{
		return pendingFeedbackTickets;
	}

	public void setPendingFeedbackTickets(List<FeedbackTicket> pendingFeedbackTickets)
	{
		this.pendingFeedbackTickets = pendingFeedbackTickets;
	}

	public List<UserSecurity> getPendingUsers()
	{
		return pendingUsers;
	}

	public void setPendingUsers(List<UserSecurity> pendingUsers)
	{
		this.pendingUsers = pendingUsers;
	}

	public List<EvaluationView> getPendingEvaluations()
	{
		return pendingEvaluations;
	}

	public void setPendingEvaluations(List<EvaluationView> pendingEvaluations)
	{
		this.pendingEvaluations = pendingEvaluations;
	}

}
