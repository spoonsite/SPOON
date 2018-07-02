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

import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.ComponentIntegration;
import edu.usu.sdl.openstorefront.core.entity.ComponentIntegrationConfig;
import java.text.ParseException;
import java.util.Date;
import net.redhogs.cronparser.CronExpressionDescriptor;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jlaw
 */
public class ComponentIntegrationView
		extends StandardEntityView
{

	private String componentId;
	private String componentName;
	private String refreshRate;
	private String cronExpressionDescription;
	private String status;
	private Date lastStartTime;
	private Date lastEndTime;
	private String activeStatus;
	private String errorMessage;

	public static ComponentIntegrationView toView(ComponentIntegration integration, ComponentIntegrationConfig integrationConfig)
	{
		ComponentIntegrationView view = toView(integration);
		view.setErrorMessage(integrationConfig.getErrorMessage());
		return view;
	}

	public static ComponentIntegrationView toView(ComponentIntegration integration)
	{
		Service serviceProxy = ServiceProxyFactory.getServiceProxy();
		ComponentIntegrationView view = new ComponentIntegrationView();
		String componentName = serviceProxy.getComponentService().getComponentName(integration.getComponentId());

		view.setComponentName(componentName);
		view.setComponentId(integration.getComponentId());
		view.setRefreshRate(integration.getRefreshRate());
		view.setStatus(integration.getStatus());
		view.setLastEndTime(integration.getLastEndTime());
		view.setLastStartTime(integration.getLastStartTime());
		view.setActiveStatus(integration.getActiveStatus());
		view.toStandardView(integration);

		if (StringUtils.isNotBlank(view.getRefreshRate())) {
			try {
				view.setCronExpressionDescription(CronExpressionDescriptor.getDescription(integration.getRefreshRate()));
			} catch (ParseException ex) {
				view.setCronExpressionDescription("Unable to parse expression");
			}
		}

		return view;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
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

	public String getRefreshRate()
	{
		return refreshRate;
	}

	public void setRefreshRate(String refreshRate)
	{
		this.refreshRate = refreshRate;
	}

	public Date getLastStartTime()
	{
		return lastStartTime;
	}

	public void setLastStartTime(Date lastStartTime)
	{
		this.lastStartTime = lastStartTime;
	}

	public Date getLastEndTime()
	{
		return lastEndTime;
	}

	public void setLastEndTime(Date lastEndTime)
	{
		this.lastEndTime = lastEndTime;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getCronExpressionDescription()
	{
		return cronExpressionDescription;
	}

	public void setCronExpressionDescription(String cronExpressionDescription)
	{
		this.cronExpressionDescription = cronExpressionDescription;
	}

}
