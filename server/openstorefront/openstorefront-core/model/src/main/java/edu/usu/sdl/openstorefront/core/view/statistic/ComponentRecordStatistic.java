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

import java.util.Date;

/**
 *
 * @author dshurtleff
 */
public class ComponentRecordStatistic
{

	private String componentId;
	private String componentName;
	private long views;
	private String viewedUsername;
	private Date viewDts;

	public ComponentRecordStatistic()
	{
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

	public long getViews()
	{
		return views;
	}

	public void setViews(long views)
	{
		this.views = views;
	}

	public String getViewedUsername()
	{
		return viewedUsername;
	}

	public void setViewedUsername(String viewedUsername)
	{
		this.viewedUsername = viewedUsername;
	}

	public Date getViewDts()
	{
		return viewDts;
	}

	public void setViewDts(Date viewDts)
	{
		this.viewDts = viewDts;
	}

}
