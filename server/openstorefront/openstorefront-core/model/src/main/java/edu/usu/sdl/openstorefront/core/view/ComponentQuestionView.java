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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.UserTypeCode;
import edu.usu.sdl.openstorefront.core.model.QuestionAll;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
public class ComponentQuestionView
		extends StandardEntityView
{

	private String question;
	private String questionId;
	private String organization;
	private String username;
	private String userType;
	private String userTypeCode;
	private Date createDts;
	private Date updateDts;
	private String activeStatus;
	private Date questionUpdateDts;
	private String componentId;
	private String componentName;

	@DataType(ComponentQuestionResponseView.class)
	private List<ComponentQuestionResponseView> responses = new ArrayList<>();

	public static ComponentQuestionView toView(ComponentQuestion question, List<ComponentQuestionResponseView> responses)
	{
		Service service = ServiceProxyFactory.getServiceProxy();
		ComponentQuestionView view = new ComponentQuestionView();
		view.setComponentId(question.getComponentId());
		view.setComponentName(service.getComponentService().getComponentName(question.getComponentId()));
		view.setResponses(responses);
		view.setQuestion(question.getQuestion());
		view.setUsername(question.getCreateUser());
		view.setActiveStatus(question.getActiveStatus());
		UserTypeCode typeCode = service.getLookupService().getLookupEnity(UserTypeCode.class, question.getUserTypeCode());
		if (typeCode == null) {
			view.setUserType(null);
		} else {
			view.setUserType(typeCode.getDescription());
			view.setUserTypeCode(typeCode.getCode());
		}
		view.setOrganization(question.getOrganization());
		view.setQuestionId(question.getQuestionId());
		view.setQuestionUpdateDts(question.getUpdateDts());
		view.setCreateDts(question.getCreateDts());
		view.toStandardView(question);

		Date max;
		if (responses.size() > 0) {
			max = responses.get(0).getUpdateDts();
			for (ComponentQuestionResponseView response : responses) {
				if (response.getUpdateDts().compareTo(max) > 0) {
					max = response.getUpdateDts();
				}
			}
			view.setUpdateDts(max);
		}

		return view;
	}

	public static List<ComponentQuestionView> toViewListAll(List<QuestionAll> questions)
	{
		List<ComponentQuestionView> views = new ArrayList<>();
		questions.forEach(question -> {
			views.add(toView(question.getQuestion(), ComponentQuestionResponseView.toViewList(question.getResponds())));
		});
		return views;
	}

	public static List<ComponentQuestionView> toViewList(List<ComponentQuestion> questions, Map<String, List<ComponentQuestionResponseView>> responseMap)
	{
		List<ComponentQuestionView> views = new ArrayList<>();
		questions.forEach(question -> {
			List<ComponentQuestionResponseView> responses = responseMap.get(question.getQuestionId());
			if (responses == null) {
				responses = new ArrayList<>();
			}
			views.add(toView(question, responses));
		});
		return views;
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

	/**
	 * @return the questionId
	 */
	public String getQuestionId()
	{
		return questionId;
	}

	/**
	 * @param questionId the questionId to set
	 */
	public void setQuestionId(String questionId)
	{
		this.questionId = questionId;
	}

	public String getUserTypeCode()
	{
		return userTypeCode;
	}

	public void setUserTypeCode(String userTypeCode)
	{
		this.userTypeCode = userTypeCode;
	}

	/**
	 * @return the organization
	 */
	public String getOrganization()
	{
		return organization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public Date getQuestionUpdateDts()
	{
		return questionUpdateDts;
	}

	public void setQuestionUpdateDts(Date questionUpdateDts)
	{
		this.questionUpdateDts = questionUpdateDts;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getComponentName()
	{
		return componentName;
	}

	public void setComponentName(String componentName)
	{
		this.componentName = componentName;
	}
}
