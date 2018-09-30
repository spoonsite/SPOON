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

import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStep;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStepAction;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStepRole;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeRoleResolution;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author bmichaelis
 */
public class AssignEntryTypeGroupAction
		extends BaseWorkPlanStepAction
{
	public AssignEntryTypeGroupAction(WorkPlanLink workPlanLink, WorkPlan workPlan, WorkPlanStepAction currentStepAction, WorkPlanStep workPlanStep)
	{
		super(workPlanLink, workPlan, currentStepAction, workPlanStep);
	}
	
	@Override
	public void performAction()
	{
		if (currentStepAction.getActionOption() != null) {
			if (StringUtils.isNotBlank(workPlanLink.getComponentId())) {
				String componentType = service.getComponentService().getComponentTypeForComponent(workPlanLink.getComponentId());
				if (StringUtils.isNotBlank(componentType)) {
					ComponentTypeRoleResolution roleResolution = service.getComponentService().findRoleGroupsForComponentType(componentType);
					if (roleResolution != null) {
						//assign to first group
						workPlanLink.setCurrentGroupAssigned(roleResolution.getRoles().get(0));
						List<WorkPlanStepRole> roleList = new ArrayList<WorkPlanStepRole>();
						WorkPlanStepRole workPlanStepRole = new WorkPlanStepRole();
						workPlanStepRole.setSecurityRole(roleResolution.getRoles().get(0));
						roleList.add(workPlanStepRole);
						workPlanStep.setStepRole(roleList);
					}
				}
			}
		} else {
			workPlanLink.setCurrentUserAssigned(null);
		}
		workPlanLink.save();
	}
	
}

























