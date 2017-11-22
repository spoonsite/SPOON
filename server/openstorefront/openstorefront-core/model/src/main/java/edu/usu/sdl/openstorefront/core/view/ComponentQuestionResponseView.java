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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.entity.UserTypeCode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentQuestionResponseView
		extends StandardEntityView
{

	private String response;
	private String responseId;
	private String questionId;
	private String questionText;
	private String componentId;
	private String componentName;
	private String organization;
	private String username;
	private String userType;
	private String userTypeCode;
	private Date answeredDate;
	private Date updateDts;
	private String activeStatus;

	public ComponentQuestionResponseView()
	{
	}

	public static ComponentQuestionResponseView toView(ComponentQuestionResponse response)
	{
		Service service = ServiceProxyFactory.getServiceProxy();

		ComponentQuestionResponseView tempView = new ComponentQuestionResponseView();
		tempView.setAnsweredDate(response.getUpdateDts());
		tempView.setOrganization(response.getOrganization());
		tempView.setResponse(response.getResponse());
		tempView.setQuestionId(response.getQuestionId());

		ComponentQuestion question = new ComponentQuestion();
		question.setQuestionId(response.getQuestionId());
		question = question.find();
		if (question != null) {
			tempView.setQuestionText(question.getQuestion());
		} else {
			tempView.setQuestionText(OpenStorefrontConstant.NOT_AVAILABLE);
		}

		tempView.setComponentId(response.getComponentId());
		tempView.setComponentName(service.getComponentService().getComponentName(response.getComponentId()));
		tempView.setActiveStatus(response.getActiveStatus());
		UserTypeCode typeCode = service.getLookupService().getLookupEnity(UserTypeCode.class, response.getUserTypeCode());
		if (typeCode != null) {
			tempView.setUserTypeCode(typeCode.getCode());
			tempView.setUserType(typeCode.getDescription());
		}
		tempView.setUsername(response.getUpdateUser());
		tempView.setUpdateDts(response.getUpdateDts());
		tempView.setResponseId(response.getResponseId());
		tempView.toStandardView(response);

		return tempView;
	}

	public static List<ComponentQuestionResponseView> toViewList(List<ComponentQuestionResponse> responses)
	{
		List<ComponentQuestionResponseView> viewList = new ArrayList();
		responses.forEach(response -> {
			viewList.add(toView(response));
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

	public String getUserTypeCode()
	{
		return userTypeCode;
	}

	public void setUserTypeCode(String userTypeCode)
	{
		this.userTypeCode = userTypeCode;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public String getResponseId()
	{
		return responseId;
	}

	public void setResponseId(String responseId)
	{
		this.responseId = responseId;
	}

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public String getUserType()
	{
		return userType;
	}

	public void setUserType(String userType)
	{
		this.userType = userType;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public String getQuestionId()
	{
		return questionId;
	}

	public void setQuestionId(String questionId)
	{
		this.questionId = questionId;
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

	public String getQuestionText()
	{
		return questionText;
	}

	public void setQuestionText(String questionText)
	{
		this.questionText = questionText;
	}

}
