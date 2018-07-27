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
package edu.usu.sdl.openstorefront.service.workplan.action;

import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStepAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ActivateEntryAction
		extends BaseWorkPlanStepAction
{

	private static final Logger LOG = Logger.getLogger(ActivateEntryAction.class.getName());

	public ActivateEntryAction(WorkPlanLink workPlanLink, WorkPlan workPlan, WorkPlanStepAction currentStepAction)
	{
		super(workPlanLink, workPlan, currentStepAction);
	}

	@Override
	public void performAction()
	{
		if (StringUtils.isNotBlank(workPlanLink.getComponentId())) {
			service.getComponentService().activateComponent(workPlanLink.getComponentId());
			LOG.log(Level.FINEST, () -> "Activate: Component Id - " + workPlanLink.getComponentId());
		} else {
			LOG.log(Level.FINEST, "Unable to activate; No component Id");
		}
	}

}
