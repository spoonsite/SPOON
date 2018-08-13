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

import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStep;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStepRole;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class WorkPlanFullStepView
{

	private String workPlanStepId;
	private String name;
	private String description;

	@DataType(WorkPlanStepRole.class)
	private List<WorkPlanStepRole> stepRole = new ArrayList<>();

	private String roleLogicCondition;
	private Integer stepOrder;

	@DataType(WorkPlanStepActionView.class)
	private List<WorkPlanStepActionView> actions = new ArrayList<>();

	@DataType(WorkPlanStepEventView.class)
	private List<WorkPlanStepEventView> triggerEvents = new ArrayList<>();

	private String approvalStateToMatch;
	private String approvalStateToMatchDescription;

	public WorkPlanFullStepView()
	{
	}

	public static WorkPlanFullStepView toView(WorkPlanStep step)
	{
		WorkPlanFullStepView view = new WorkPlanFullStepView();

		view.setWorkPlanStepId(step.getWorkPlanStepId());
		view.setName(step.getName());
		view.setDescription(step.getDescription());
		if (step.getStepRole() != null) {
			view.setStepRole(step.getStepRole());
		}

		view.setRoleLogicCondition(step.getRoleLogicCondition());
		view.setStepOrder(step.getStepOrder());
		if (step.getActions() != null) {
			view.setActions(WorkPlanStepActionView.toView(step.getActions()));
		}

		if (step.getTriggerEvents() != null) {
			view.setTriggerEvents(WorkPlanStepEventView.toView(step.getTriggerEvents()));
		}
		view.setApprovalStateToMatch(step.getApprovalStateToMatch());
		view.setApprovalStateToMatchDescription(TranslateUtil.translate(ApprovalStatus.class, step.getApprovalStateToMatch()));

		return view;
	}

	public static List<WorkPlanFullStepView> toView(List<WorkPlanStep> steps)
	{
		List<WorkPlanFullStepView> views = new ArrayList<>();

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

	public List<WorkPlanStepRole> getStepRole()
	{
		return stepRole;
	}

	public void setStepRole(List<WorkPlanStepRole> stepRole)
	{
		this.stepRole = stepRole;
	}

	public String getRoleLogicCondition()
	{
		return roleLogicCondition;
	}

	public void setRoleLogicCondition(String roleLogicCondition)
	{
		this.roleLogicCondition = roleLogicCondition;
	}

	public Integer getStepOrder()
	{
		return stepOrder;
	}

	public void setStepOrder(Integer stepOrder)
	{
		this.stepOrder = stepOrder;
	}

	public List<WorkPlanStepActionView> getActions()
	{
		return actions;
	}

	public void setActions(List<WorkPlanStepActionView> actions)
	{
		this.actions = actions;
	}

	public List<WorkPlanStepEventView> getTriggerEvents()
	{
		return triggerEvents;
	}

	public void setTriggerEvents(List<WorkPlanStepEventView> triggerEvents)
	{
		this.triggerEvents = triggerEvents;
	}

	public String getApprovalStateToMatch()
	{
		return approvalStateToMatch;
	}

	public void setApprovalStateToMatch(String approvalStateToMatch)
	{
		this.approvalStateToMatch = approvalStateToMatch;
	}

	public String getApprovalStateToMatchDescription()
	{
		return approvalStateToMatchDescription;
	}

	public void setApprovalStateToMatchDescription(String approvalStateToMatchDescription)
	{
		this.approvalStateToMatchDescription = approvalStateToMatchDescription;
	}

}
