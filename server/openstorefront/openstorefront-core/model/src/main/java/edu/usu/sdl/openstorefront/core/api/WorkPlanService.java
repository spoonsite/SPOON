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
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;

/**
 *
 * @author cyearsley
 */
public interface WorkPlanService
		extends AsyncService
{

	/**
	 * Saves a work plan
	 *
	 * @param workPlan
	 * @return
	 */
	WorkPlan saveWorkPlan(WorkPlan workPlan);

	/**
	 * Activates a plan / deactivate plan that other active plan that have the
	 * same entry types
	 *
	 * @param workPlanId
	 */
	void activateWorkPlan(String workPlanId);

	/**
	 * Deletes a workplan and transfer to a new workplan...
	 *
	 * @param workPlanId
	 * @param newWorkPlanId (if null) then it will go back to default
	 */
	void removeWorkPlan(String workPlanId, String newWorkPlanId);

	/**
	 * Pulls the current plan for the Component or creates one if needed
	 *
	 * @param componentId
	 * @return
	 */
	WorkPlanLink getWorkPlanForComponent(String componentId);

	/**
	 * Assigns a WorkPlan for a Component
	 *
	 * @param componentId
	 * @param username
	 * @param roleGroup
	 */
	void assignWorkPlanForComponent(String workPlanId, String workLinkId, String username, String roleGroup);

	/**
	 * Moves a Component to a new workflow step
	 *
	 * @param nextStepId
	 * @return
	 */
	WorkPlanLink moveWorkLinkToStep(WorkPlanLink workPlanLink, String workPlanStepId);

	/**
	 * Moves workPlanlink to previous step in it's workflow
	 *
	 * @param workPlanLink
	 */
	public void previousStep(WorkPlanLink workPlanLink);

	/**
	 * Moves workPlanlink to next step in it's workflow
	 *
	 * @param workPlanLink
	 */
	public void nextStep(WorkPlanLink workPlanLink);

	/**
	 * Updated all worklink to match sure the accurately reflects the data
	 */
	void syncWorkPlanLinks();

	/**
	 * Retrieves a workplan and make sure it all sorted.
	 *
	 * @param workplanId
	 * @return workplan or null if not found
	 */
	WorkPlan getWorkPlan(String workplanId);

	/**
	 * Sync work plan to the state of work plan
	 *
	 * @param componentId
	 */
	void updatedWorkPlanLinkToMatchState(String componentId);

}
