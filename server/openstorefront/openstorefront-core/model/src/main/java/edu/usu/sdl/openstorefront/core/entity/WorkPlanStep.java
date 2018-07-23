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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Holds work plan step information")
public class WorkPlanStep
		extends StandardEntity<WorkPlanStep>
{

	private static final long serialVersionUID = 1L;

	@PK(generated = true)
	@NotNull
	private String workPlanStepId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String name;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_1K)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String description;

	@ConsumeField
	@Embedded
	@DataType(WorkPlanStepRole.class)
	@OneToMany(orphanRemoval = true)
	private List<WorkPlanStepRole> stepRole;

	@ConsumeField
	@ValidValueType(value = {OpenStorefrontConstant.AND_CONDITION, OpenStorefrontConstant.OR_CONDITION})
	private String roleLogicCondition;

	@NotNull
	@ConsumeField
	private Integer stepOrder;

	@ConsumeField
	@Embedded
	@DataType(WorkPlanStepAction.class)
	@OneToMany(orphanRemoval = true)
	private List<WorkPlanStepAction> actions;

	@ConsumeField
	@Embedded
	@DataType(WorkPlanStepEvent.class)
	@OneToMany(orphanRemoval = true)
	private List<WorkPlanStepEvent> triggerEvents;

	@ValidValueType(value
			= {}, lookupClass = ApprovalStatus.class)
	@ConsumeField
	@APIDescription("Status of an approval on an entry that this step would match")
	@FK(ApprovalStatus.class)
	private String approvalStateToMatch;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public WorkPlanStep()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		WorkPlanStep workPlanStep = (WorkPlanStep) entity;

		this.setName(workPlanStep.getName());
		this.setDescription(workPlanStep.getDescription());
		this.setActions(workPlanStep.getActions());
		this.setApprovalStateToMatch(workPlanStep.getApprovalStateToMatch());
		this.setStepOrder(workPlanStep.getStepOrder());
		this.setTriggerEvents(workPlanStep.getTriggerEvents());
		this.setStepRole(workPlanStep.getStepRole());
		this.setRoleLogicCondition(workPlanStep.getRoleLogicCondition());

	}

	public String getWorkPlanStepId()
	{
		return workPlanStepId;
	}

	public void setWorkPlanStepId(String wPStepId)
	{
		this.workPlanStepId = wPStepId;
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

	public Integer getStepOrder()
	{
		return stepOrder;
	}

	public void setStepOrder(Integer stepOrder)
	{
		this.stepOrder = stepOrder;
	}

	public List<WorkPlanStepAction> getActions()
	{
		return actions;
	}

	public void setActions(List<WorkPlanStepAction> actions)
	{
		this.actions = actions;
	}

	public String getApprovalStateToMatch()
	{
		return approvalStateToMatch;
	}

	public void setApprovalStateToMatch(String approvalStateToMatch)
	{
		this.approvalStateToMatch = approvalStateToMatch;
	}

	public List<WorkPlanStepEvent> getTriggerEvents()
	{
		return triggerEvents;
	}

	public void setTriggerEvents(List<WorkPlanStepEvent> triggerEvents)
	{
		this.triggerEvents = triggerEvents;
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
}
