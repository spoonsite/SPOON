/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.rest.model;

import edu.usu.sdl.openstorefront.storage.model.ComponentQuestionResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentQuestionResponseView
{

	private String response;
	private String username;
	private String userType;
	private Date answeredDate;

	public ComponentQuestionResponseView()
	{
	}
	
	public static List<ComponentQuestionResponseView> toViewList(List<ComponentQuestionResponse> responses)
	{
		List<ComponentQuestionResponseView> viewList = new ArrayList();
		responses.forEach(response->{
			ComponentQuestionResponseView tempView = new ComponentQuestionResponseView();
			tempView.setAnsweredDate(response.getCreateDts());
			tempView.setResponse(response.getResponse());
			tempView.setUserType(response.getUserTypeCode());
			tempView.setUsername(response.getUpdateUser());
			viewList.add(tempView);
		});
		return viewList;
	}

	public String getResponse()
	{
		return response;
	}

	public void setResponse(String response)
	{
		this.response = response;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public Date getAnsweredDate()
	{
		return answeredDate;
	}

	public void setAnsweredDate(Date answeredDate)
	{
		this.answeredDate = answeredDate;
	}

	public String getUserType()
	{
		return userType;
	}

	public void setUserType(String userType)
	{
		this.userType = userType;
	}

}
