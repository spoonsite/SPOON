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
package edu.usu.sdl.openstorefront.service.workplan;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStep;
import edu.usu.sdl.openstorefront.service.ServiceProxy;

/**
 *
 * @author dshurtleff
 */
public class WorkPlanEngine
{

	private static WorkPlanEngineFactory workPlanEngineFactory;

	private ServiceProxy service;

	private WorkPlanEngine()
	{
		service = ServiceProxy.getProxy();
	}

	private WorkPlanEngine(ServiceProxy service)
	{
		this.service = service;
	}

	/**
	 * Set the factory before calling if need to switch out the creation
	 *
	 * @return
	 */
	public static WorkPlanEngine newWorkPlanEngine()
	{
		if (workPlanEngineFactory != null) {
			return workPlanEngineFactory.createWorkPlan();
		}
		return new WorkPlanEngine();
	}

	public interface WorkPlanEngineFactory
	{

		WorkPlanEngine createWorkPlan();
	}

	public void previousStep(WorkPlanLink workPlanLink)
	{
		WorkPlan workPlan = getWorkPlan(workPlanLink.getWorkPlanId());

		for (WorkPlanStep step : workPlan.getSteps()) {

		}

		//workPlanLink.getCurrentStepId()
	}

	public void nextStep(WorkPlanLink workPlanLink)
	{

	}

	public void moveWorkLinkToStep(WorkPlanLink workPlanLink, String workPlanStep)
	{

	}

	public WorkPlanLink getWorkPlanLinkForComponent(String componentId)
	{

		return null;
	}

	public void syncWorkPlanLinks()
	{
		//process all entry (x at time)

	}

	public void updatedWorkPlanLinkToMatchState(Component component)
	{

	}

	private WorkPlan getWorkPlan(String workplanId)
	{
		WorkPlan workPlan = new WorkPlan();
		workPlan.setWorkPlanId(workplanId);
		workPlan = workPlan.find();
		if (workPlan == null) {
			throw new OpenStorefrontRuntimeException("Unable to find work plan", "Check workPlanId: " + workplanId);
		}

		//make sure steps are in order
		workPlan.getSteps().sort((a, b) -> {
			return a.getStepOrder().compareTo(b.getStepOrder());
		});
		return workPlan;
	}

	public static void setWorkPlanEngineFactory(WorkPlanEngineFactory aWorkPlanEngineFactory)
	{
		workPlanEngineFactory = aWorkPlanEngineFactory;
	}

}
