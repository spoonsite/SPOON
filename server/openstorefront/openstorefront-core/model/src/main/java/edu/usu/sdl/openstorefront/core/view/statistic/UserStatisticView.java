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
package edu.usu.sdl.openstorefront.core.view.statistic;

import edu.usu.sdl.openstorefront.core.annotation.DataType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class UserStatisticView
{

	private long activeUsers;
	private long activeUserReviews;
	private long activeUserQuestions;
	private long activeUserQuestionResponses;
	private long activeUserWatches;

	@DataType(UserRecordStatistic.class)
	private List<UserRecordStatistic> recentLogins = new ArrayList<>();

	public UserStatisticView()
	{
	}

	public long getActiveUsers()
	{
		return activeUsers;
	}

	public void setActiveUsers(long activeUsers)
	{
		this.activeUsers = activeUsers;
	}

	public long getActiveUserReviews()
	{
		return activeUserReviews;
	}

	public void setActiveUserReviews(long activeUserReviews)
	{
		this.activeUserReviews = activeUserReviews;
	}

	public long getActiveUserQuestions()
	{
		return activeUserQuestions;
	}

	public void setActiveUserQuestions(long activeUserQuestions)
	{
		this.activeUserQuestions = activeUserQuestions;
	}

	public long getActiveUserQuestionResponses()
	{
		return activeUserQuestionResponses;
	}

	public void setActiveUserQuestionResponses(long activeUserQuestionResponses)
	{
		this.activeUserQuestionResponses = activeUserQuestionResponses;
	}

	public long getActiveUserWatches()
	{
		return activeUserWatches;
	}

	public void setActiveUserWatches(long activeUserWatches)
	{
		this.activeUserWatches = activeUserWatches;
	}

	public List<UserRecordStatistic> getRecentLogins()
	{
		return recentLogins;
	}

	public void setRecentLogins(List<UserRecordStatistic> recentLogins)
	{
		this.recentLogins = recentLogins;
	}

}
