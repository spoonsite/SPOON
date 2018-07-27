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

import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStepAction;
import edu.usu.sdl.openstorefront.service.ServiceProxy;

/**
 *
 * @author dshurtleff
 */
public abstract class BaseWorkPlanStepAction
{

	protected WorkPlanLink workPlanLink;
	protected WorkPlan workPlan;
	protected WorkPlanStepAction currentStepAction;
	protected ServiceProxy service;

	public BaseWorkPlanStepAction(WorkPlanLink workPlanLink, WorkPlan workPlan, WorkPlanStepAction currentStepAction)
	{
		this.workPlanLink = workPlanLink;
		this.workPlan = workPlan;
		this.currentStepAction = currentStepAction;
		service = ServiceProxy.getProxy();
	}

	public BaseWorkPlanStepAction(WorkPlanLink workPlanLink, WorkPlan workPlan, WorkPlanStepAction currentStepAction, ServiceProxy service)
	{
		this.workPlanLink = workPlanLink;
		this.workPlan = workPlan;
		this.currentStepAction = currentStepAction;
		this.service = service;
	}

	public abstract void performAction();

}
