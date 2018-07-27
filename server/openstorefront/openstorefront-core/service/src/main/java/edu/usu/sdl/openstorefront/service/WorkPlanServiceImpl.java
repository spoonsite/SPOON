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
package edu.usu.sdl.openstorefront.service;

import java.util.Objects;

import edu.usu.sdl.openstorefront.core.api.WorkPlanService;
import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;

/**
 *
 * @author cyearsley
 */
public class WorkPlanServiceImpl
		extends ServiceProxy
		implements WorkPlanService
{

	@Override
	public WorkPlan saveWorkPlan(WorkPlan workPlan)
	{
		Objects.requireNonNull(workPlan);
		updateWorkPlanFields(workPlan);

		if (workPlan.getWorkPlanId() != null) {
			WorkPlan workPlanExample = new WorkPlan();
			workPlanExample.setWorkPlanId(workPlan.getWorkPlanId());

			workPlanExample = workPlanExample.findProxy();
			if (workPlanExample != null) {
					workPlanExample.updateFields(workPlan);
					persistenceService.persist(workPlanExample);
			}
		}
		else {
			workPlan.setWorkPlanId(persistenceService.generateId());
			workPlan.populateBaseCreateFields();

			workPlan.save();
		}

		return workPlan;
	}

	private void updateWorkPlanFields (WorkPlan workPlan)
	{
		workPlan.getSteps().forEach(step -> {
			step.populateBaseCreateFields();

			step.getActions().forEach(action -> {
				action.populateBaseCreateFields();
				action.setWorkPlanStepActionId(persistenceService.generateId());
			});
		});
	}

	@Override
	public void activateWorkPlan(String workPlanId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void removeWorkPlan(String workPlanId, String newWorkPlanId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public WorkPlanLink getWorkPlanForComponent(String componentId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void assignWorkPlanForComponent(String workPlanId, String workLinkId, String username, String roleGroup)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public WorkPlanLink moveComponentToStep(String workPlanId, String workLinkId, String workPlanStepId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
