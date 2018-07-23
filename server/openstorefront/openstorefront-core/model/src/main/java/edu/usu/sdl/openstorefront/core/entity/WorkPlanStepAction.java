/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Holds alert subscription information")
public class WorkPlanStepAction
		extends StandardEntity<WorkPlanStepAction>
{

	private static final long serialVersionUID = 1L;

	@PK(generated = true)
	@NotNull
	private String workPlanStepActionId;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = WorkPlanStepActionType.class)
	@FK(WorkPlanStepActionType.class)
	private String workPlanStepActionType;

	@NotNull
	@ConsumeField
	private Integer actionOrder;

	@Embedded
	@ConsumeField
	private WorkFlowStepActionOption actionOption;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public WorkPlanStepAction()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		WorkPlanStepAction stepAction = (WorkPlanStepAction) entity;
		this.setActionOrder(stepAction.getActionOrder());
		this.setWorkPlanStepActionType(stepAction.getWorkPlanStepActionType());

	}

	public String getWorkPlanStepActionId()
	{
		return workPlanStepActionId;
	}

	public void setWorkPlanStepActionId(String workPlanStepActionId)
	{
		this.workPlanStepActionId = workPlanStepActionId;
	}

	public String getWorkPlanStepActionType()
	{
		return workPlanStepActionType;
	}

	public void setWorkPlanStepActionType(String workPlanStepActionType)
	{
		this.workPlanStepActionType = workPlanStepActionType;
	}

	public Integer getActionOrder()
	{
		return actionOrder;
	}

	public void setActionOrder(Integer actionOrder)
	{
		this.actionOrder = actionOrder;
	}

}
