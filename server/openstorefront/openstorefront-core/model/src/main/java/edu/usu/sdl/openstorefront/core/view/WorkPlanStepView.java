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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.entity.WorkPlanStep;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStepRole;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class WorkPlanStepView
{

	private String workPlanStepId;
	private String name;
	private String description;
	private List<WorkPlanStepRole> roles = new ArrayList<>();
	private String roleLogicCondition;

	public WorkPlanStepView()
	{
	}

	public static WorkPlanStepView toView(WorkPlanStep workPlanStep)
	{
		WorkPlanStepView view = new WorkPlanStepView();

		view.setName(workPlanStep.getName());
		view.setDescription(workPlanStep.getDescription());
		view.setRoles(workPlanStep.getStepRole());
		view.setWorkPlanStepId(workPlanStep.getWorkPlanStepId());
		view.setRoleLogicCondition(workPlanStep.getRoleLogicCondition());

		return view;
	}

	public static List<WorkPlanStepView> toView(List<WorkPlanStep> steps)
	{
		List<WorkPlanStepView> views = new ArrayList<>();

		steps.forEach(step -> {
			views.add(toView(step));
		});

		return views;
	}

	public String getWorkPlanStepId()
	{
		return workPlanStepId;
	}

	public void setWorkPlanStepId(String workPlanStepId)
	{
		this.workPlanStepId = workPlanStepId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public List<WorkPlanStepRole> getRoles()
	{
		return roles;
	}

	public void setRoles(List<WorkPlanStepRole> roles)
	{
		this.roles = roles;
	}

	public String getRoleLogicCondition()
	{
		return roleLogicCondition;
	}

	public void setRoleLogicCondition(String roleLogicCondition)
	{
		this.roleLogicCondition = roleLogicCondition;
	}

}
