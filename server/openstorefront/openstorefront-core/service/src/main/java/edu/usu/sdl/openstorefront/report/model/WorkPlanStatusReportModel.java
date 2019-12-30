/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class WorkPlanStatusReportModel
		extends BaseReportModel
{

	private List<WorkPlanStatusLineModel> data = new ArrayList<>();

	/**
	 * This holds a list of the workplan steps that were included for this report,
	 * and a count of how many entries were found in each step.
	 *
	 * This field was created for quick reference to this information in the report.
	 * @see WorkPlanStatusReport#writeCVS()
	 */
	private HashMap<String, Integer> stepEntryInstanceCount = new HashMap<>();

	public WorkPlanStatusReportModel()
	{
	}

	@Override
	public List<WorkPlanStatusLineModel> getData()
	{
		return data;
	}

	public void setData(List<WorkPlanStatusLineModel> data)
	{
		this.data = data;
	}

	public HashMap<String, Integer> getStepEntryInstanceCount()
	{
		return stepEntryInstanceCount;
	}

	public void setStepEntryInstanceCount(HashMap<String, Integer> metaData)
	{
		this.stepEntryInstanceCount = metaData;
	}
}
