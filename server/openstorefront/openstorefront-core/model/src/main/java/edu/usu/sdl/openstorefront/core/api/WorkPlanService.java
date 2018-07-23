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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import java.util.List;

/**
 *
 * @author cyearsley
 */
public interface WorkPlanService
		extends AsyncService
{
	/**
	 * Gets all WorkPlans
	 *
	 * @return List<WorkPlan>
	 */
	public List<WorkPlan> getWorkPlans();
	
	/**
	 * Gets a single workplan
	 *
	 * @return WorkPlan
	 */
	public WorkPlan getWorkPlan(String id);
	
	/**
	 * Create a workplan
	 *
	 * @return WorkPlan
	 */
	public WorkPlan createWorkPlan(WorkPlan workPlan);
	
	/**
	 * Updates a workplan
	 *
	 * @return WorkPlan
	 */
	public WorkPlan updateWorkPlan(String workPlanId, WorkPlan newWorkPlan);
	
	/**
	 * Deletes a workplan
	 *
	 * @param workPlanId
	 * @return
	 */
	public void deleteWorkPlan(String workPlanId);
}
