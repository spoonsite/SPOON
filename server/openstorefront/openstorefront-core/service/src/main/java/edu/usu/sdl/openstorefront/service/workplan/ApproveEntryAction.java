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

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStep;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStepAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ApproveEntryAction
		extends BaseWorkPlanStepAction
{

	private static final Logger LOG = Logger.getLogger(ApproveEntryAction.class.getName());

	public ApproveEntryAction(WorkPlanLink workPlanLink, WorkPlan workPlan, WorkPlanStepAction currentStepAction, WorkPlanStep workPlanStep)
	{
		super(workPlanLink, workPlan, currentStepAction, workPlanStep);
	}

	@Override
	public void performAction()
	{
		if (StringUtils.isNotBlank(workPlanLink.getComponentId())) {
			Component component = new Component();
			component.setComponentId(workPlanLink.getComponentId());
			component = component.find();

			if (component.getPendingChangeId() != null) {
				service.getComponentService().mergePendingChange(workPlanLink.getComponentId());
				LOG.log(Level.FINEST, () -> "Approved - Change Request: Component Id - " + workPlanLink.getComponentId());
			} else {
				service.getComponentService().approveComponent(workPlanLink.getComponentId());
				LOG.log(Level.FINEST, () -> "Approved: Component Id - " + workPlanLink.getComponentId());
			}

		} else {
			LOG.log(Level.FINEST, "Unable to approve; No component Id");
		}
	}

}
