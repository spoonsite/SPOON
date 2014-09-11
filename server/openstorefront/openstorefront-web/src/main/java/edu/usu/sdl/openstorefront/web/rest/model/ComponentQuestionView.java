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

import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestion;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentQuestionView
{

	private String question;
	private String username;
	private String userType;
	private Date createDts;
	private Date updateDts;

	@DataType(ComponentQuestionResponseView.class)
	private List<ComponentQuestionResponseView> responses = new ArrayList<>();

	public ComponentQuestionView()
	{
	}

	public static ComponentQuestionView toView(ComponentQuestion question, List<ComponentQuestionResponseView> responses)
	{
		ComponentQuestionView view = new ComponentQuestionView();
		view.setResponses(responses);
		view.setQuestion(question.getQuestion());
		view.setUsername(question.getCreateUser());
		view.setUserType(question.getUserType());
		Date max;
		if (responses.size() > 0) {
			max = responses.get(0).getUpdateDts();
			for(ComponentQuestionResponseView response: responses){
				if (response.getUpdateDts().compareTo(max) > 0) {
					max = response.getUpdateDts();
				}
			}
			view.setUpdateDts(max);
		}

		return view;
	}

	public List<ComponentQuestionResponseView> getResponses()
	{
		return responses;
	}

	public void setResponses(List<ComponentQuestionResponseView> responses)
	{
		this.responses = responses;
	}

	/**
	 * @return the question
	 */
	public String getQuestion()
	{
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question)
	{
		this.question = question;
	}

	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * @return the userType
	 */
	public String getUserType()
	{
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(String userType)
	{
		this.userType = userType;
	}

	/**
	 * @return the createDts
	 */
	public Date getCreateDts()
	{
		return createDts;
	}

	/**
	 * @param createDts the createDts to set
	 */
	public void setCreateDts(Date createDts)
	{
		this.createDts = createDts;
	}

	/**
	 * @return the updateDts
	 */
	public Date getUpdateDts()
	{
		return updateDts;
	}

	/**
	 * @param updateDts the updateDts to set
	 */
	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}
}
