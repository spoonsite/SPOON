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
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class AssignEntryAction
		extends BaseWorkPlanStepAction
{

	public AssignEntryAction(WorkPlanLink workPlanLink, WorkPlan workPlan, WorkPlanStepAction currentStepAction)
	{
		super(workPlanLink, workPlan, currentStepAction);
	}

	@Override
	public void performAction()
	{
		if (currentStepAction.getActionOption() != null) {
			workPlanLink.setCurrentUserAssigned(currentStepAction.getActionOption().getAssignUser());
			if (StringUtils.isNotBlank(currentStepAction.getActionOption().getAssignGroup())) {
				workPlanLink.setCurrentGroupAssigned(currentStepAction.getActionOption().getAssignGroup());
			}
			if (currentStepAction.getActionOption().getUnassignGroup()) {
				workPlanLink.setCurrentGroupAssigned(null);
			}
		} else {
			workPlanLink.setCurrentUserAssigned(null);
		}

		workPlanLink.save();

		//log workflow change assignment
	}

}
