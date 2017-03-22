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
package edu.usu.sdl.openstorefront.core.view.statistic;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
public class EvaluationStatistic
{
	private int published;
	private int unpublished;
	private Map<String, Integer> statusStats = new HashMap<>();

	public EvaluationStatistic()
	{
	}

	public int getPublished()
	{
		return published;
	}

	public void setPublished(int published)
	{
		this.published = published;
	}

	public int getUnpublished()
	{
		return unpublished;
	}

	public void setUnpublished(int unpublished)
	{
		this.unpublished = unpublished;
	}

	public Map<String, Integer> getStatusStats()
	{
		return statusStats;
	}

	public void setStatusStats(Map<String, Integer> statusStats)
	{
		this.statusStats = statusStats;
	}	
	
}
