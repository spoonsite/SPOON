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
package edu.usu.sdl.openstorefront.service.message;

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.Highlight;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Holds information about recent changes
 *
 * @author dshurtleff
 */
public class RecentChangeMessage
{

	private Date lastRunDts;
	private List<Component> componentsAdded = new ArrayList<>();
	private List<Component> componentsUpdated = new ArrayList<>();
	private List<Highlight> highlightsAdded = new ArrayList<>();
	private List<Highlight> highlightsUpdated = new ArrayList<>();

	public RecentChangeMessage()
	{
	}

	public Date getLastRunDts()
	{
		return lastRunDts;
	}

	public void setLastRunDts(Date lastRunDts)
	{
		this.lastRunDts = lastRunDts;
	}

	public List<Component> getComponentsAdded()
	{
		return componentsAdded;
	}

	public void setComponentsAdded(List<Component> componentsAdded)
	{
		this.componentsAdded = componentsAdded;
	}

	public List<Component> getComponentsUpdated()
	{
		return componentsUpdated;
	}

	public void setComponentsUpdated(List<Component> componentsUpdated)
	{
		this.componentsUpdated = componentsUpdated;
	}

	public List<Highlight> getHighlightsAdded()
	{
		return highlightsAdded;
	}

	public void setHighlightsAdded(List<Highlight> highlightsAdded)
	{
		this.highlightsAdded = highlightsAdded;
	}

	public List<Highlight> getHighlightsUpdated()
	{
		return highlightsUpdated;
	}

	public void setHighlightsUpdated(List<Highlight> highlightsUpdated)
	{
		this.highlightsUpdated = highlightsUpdated;
	}

}
