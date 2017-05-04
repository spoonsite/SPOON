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

import edu.usu.sdl.openstorefront.core.annotation.DataType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class EvaluationStatistic
{
	private int published;
	private int unpublished;
	
	@DataType(WorkflowStats.class)
	private List<WorkflowStats> statusStats = new ArrayList<>();

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

	public List<WorkflowStats> getStatusStats()
	{
		return statusStats;
	}

	public void setStatusStats(List<WorkflowStats> statusStats)
	{
		this.statusStats = statusStats;
	}
	
}
