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
import edu.usu.sdl.openstorefront.storage.model.IntegrationConfig;

/**
 *
 * @author jlaw
 */
public class IntegrationView
{

	private String id;
	private String componentId;
	private String componentName;
	private String issueNumber;
	private String overRideRefreshRate;
	private String status;

	public IntegrationView()
	{

	}

	public IntegrationView toView(Integration integration, IntegrationConfig type)
	{
		ServiceProxy proxy = new ServiceProxy();
		IntegrationView view = new IntegrationView();
		Component temp = proxy.getPersistenceService().findById(Component.class, integration.getComponentId());

		view.setComponentName(temp.getName());
		view.setComponentId(integration.getComponentId());
		view.setOverRideRefreshRate(integration.getRefreshRate());
		view.setId(integration.getIntegrationId());
		view.setIssueNumber(type.getIssueNumber());
		view.setStatus(integration.getStatus());

		return view;
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
	 * @return the componentId
	 */
	public String getComponentId()
	{
		return componentId;
	}

	/**
	 * @param componentId the componentId to set
	 */
	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	/**
	 * @return the componentName
	 */
	public String getComponentName()
	{
		return componentName;
	}

	/**
	 * @param componentName the componentName to set
	 */
	public void setComponentName(String componentName)
	{
		this.componentName = componentName;
	}

	/**
	 * @return the overRideRefreshRate
	 */
	public String getOverRideRefreshRate()
	{
		return overRideRefreshRate;
	}

	/**
	 * @param overRideRefreshRate the overRideRefreshRate to set
	 */
	public void setOverRideRefreshRate(String overRideRefreshRate)
	{
		this.overRideRefreshRate = overRideRefreshRate;
	}

}
