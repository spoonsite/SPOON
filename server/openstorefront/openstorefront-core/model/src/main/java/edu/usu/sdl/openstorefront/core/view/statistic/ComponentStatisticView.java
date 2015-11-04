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

import edu.usu.sdl.openstorefront.core.annotation.DataType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentStatisticView
{

	private long numberOfApprovedEntries;
	private long numberOfPendingEntries;
	private long numberOfNotSubmited;
	private long numberOfAttributesTypes;
	private long numberOfAttributesCodes;

	@DataType(ComponentRecordStatistic.class)
	private List<ComponentRecordStatistic> recentlyViewed = new ArrayList<>();

	@DataType(ComponentRecordStatistic.class)
	private List<ComponentRecordStatistic> topViewed = new ArrayList<>();

	public ComponentStatisticView()
	{
	}

	public long getNumberOfApprovedEntries()
	{
		return numberOfApprovedEntries;
	}

	public void setNumberOfApprovedEntries(long numberOfApprovedEntries)
	{
		this.numberOfApprovedEntries = numberOfApprovedEntries;
	}

	public long getNumberOfPendingEntries()
	{
		return numberOfPendingEntries;
	}

	public void setNumberOfPendingEntries(long numberOfPendingEntries)
	{
		this.numberOfPendingEntries = numberOfPendingEntries;
	}

	public long getNumberOfNotSubmited()
	{
		return numberOfNotSubmited;
	}

	public void setNumberOfNotSubmited(long numberOfNotSubmited)
	{
		this.numberOfNotSubmited = numberOfNotSubmited;
	}

	public long getNumberOfAttributesCodes()
	{
		return numberOfAttributesCodes;
	}

	public void setNumberOfAttributesCodes(long numberOfAttributesCodes)
	{
		this.numberOfAttributesCodes = numberOfAttributesCodes;
	}

	public List<ComponentRecordStatistic> getRecentlyViewed()
	{
		return recentlyViewed;
	}

	public void setRecentlyViewed(List<ComponentRecordStatistic> recentlyViewed)
	{
		this.recentlyViewed = recentlyViewed;
	}

	public List<ComponentRecordStatistic> getTopViewed()
	{
		return topViewed;
	}

	public void setTopViewed(List<ComponentRecordStatistic> topViewed)
	{
		this.topViewed = topViewed;
	}

	public long getNumberOfAttributesTypes()
	{
		return numberOfAttributesTypes;
	}

	public void setNumberOfAttributesTypes(long numberOfAttributesTypes)
	{
		this.numberOfAttributesTypes = numberOfAttributesTypes;
	}

}
