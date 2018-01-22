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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class UsageReportModel
		extends BaseReportModel
{

	private long numberOfLogins;
	private long currentActiveWatches;
	private long componentViews;
	private long componentResourcesClicks;

	private List<UsageReportLineModel> data = new ArrayList<>();

	public UsageReportModel()
	{
	}

	public long getNumberOfLogins()
	{
		return numberOfLogins;
	}

	public void setNumberOfLogins(long numberOfLogins)
	{
		this.numberOfLogins = numberOfLogins;
	}

	public long getCurrentActiveWatches()
	{
		return currentActiveWatches;
	}

	public void setCurrentActiveWatches(long currentActiveWatches)
	{
		this.currentActiveWatches = currentActiveWatches;
	}

	public long getComponentViews()
	{
		return componentViews;
	}

	public void setComponentViews(long componentViews)
	{
		this.componentViews = componentViews;
	}

	public long getComponentResourcesClicks()
	{
		return componentResourcesClicks;
	}

	public void setComponentResourcesClicks(long componentResourcesClicks)
	{
		this.componentResourcesClicks = componentResourcesClicks;
	}

	@Override
	public List<UsageReportLineModel> getData()
	{
		return data;
	}

	public void setData(List<UsageReportLineModel> data)
	{
		this.data = data;
	}

}
