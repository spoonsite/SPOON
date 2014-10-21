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

import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.Integration;
import edu.usu.sdl.openstorefront.storage.model.IntegrationTransType;

/**
 *
 * @author jlaw
 */
public class IntegrationView
{
	private String id;
	private IntegrationTransType type;
	private Component component;
	private String issueNumber;
	private String status;
	private String errorMessage;

	public IntegrationView() {
		
	}
	
	public IntegrationView toView(Integration integration, IntegrationTransType type){
		ServiceProxy proxy = new ServiceProxy();
		IntegrationView view = new IntegrationView();
		
		view.setId(integration.getIntegrationId());
		view.setType(type);
		view.setComponent(proxy.getPersistenceService().findById(Component.class, integration.getComponentId()));
		view.setIssueNumber(integration.getIssueNumber());
		view.setStatus(integration.getStatus());
		view.setErrorMessage(integration.getErrorMessage());
		
		return view;
	}

	/**
	 * @return the component
	 */
	public Component getComponent()
	{
		return component;
	}

	/**
	 * @param component the component to set
	 */
	public void setComponent(Component component)
	{
		this.component = component;
	}

	/**
	 * @return the issueNumber
	 */
	public String getIssueNumber()
	{
		return issueNumber;
	}

	/**
	 * @param issueNumber the issueNumber to set
	 */
	public void setIssueNumber(String issueNumber)
	{
		this.issueNumber = issueNumber;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public IntegrationTransType getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(IntegrationTransType type)
	{
		this.type = type;
	}
}
