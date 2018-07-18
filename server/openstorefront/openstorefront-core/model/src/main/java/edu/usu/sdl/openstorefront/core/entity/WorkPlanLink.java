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
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.Unique;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.validation.WorkPlanLinkComponentUniqueHandler;
import edu.usu.sdl.openstorefront.validation.WorkPlanLinkEvaluationUniqueHandler;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
public class WorkPlanLink
		extends StandardEntity<WorkPlanLink>
{

	private static final long serialVersionUID = 1L;

	@PK(generated = true)
	@NotNull
	private String workPlanLinkId;

	@NotNull
	@FK(WorkPlan.class)
	private String workPlanId;

	@ConsumeField
	@Unique(WorkPlanLinkComponentUniqueHandler.class)
	private String componentId;

	@ConsumeField
	@Unique(WorkPlanLinkEvaluationUniqueHandler.class)
	private String evaluationId;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@ValidValueType(value = {}, lookupClass = WorkPlanSubStatusType.class)
	@FK(WorkPlanSubStatusType.class)
	private String subStatus;

	private String currentStepId;

	@ConsumeField
	private String currentGroupAssigned;

	@ConsumeField
	private String currentUserAssigned;

	public WorkPlanLink()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		WorkPlanLink workPlanLink = (WorkPlanLink) entity;

		this.setCurrentStepId(workPlanLink.getCurrentStepId());
		this.setCurrentGroupAssigned(workPlanLink.getCurrentGroupAssigned());
		this.setCurrentUserAssigned(workPlanLink.getCurrentUserAssigned());
		this.setSubStatus(workPlanLink.getSubStatus());

	}

	public String getWorkPlanLinkId()
	{
		return workPlanLinkId;
	}

	public void setWorkPlanLinkId(String workPlanLinkId)
	{
		this.workPlanLinkId = workPlanLinkId;
	}

	public String getWorkPlanId()
	{
		return workPlanId;
	}

	public void setWorkPlanId(String workPlanId)
	{
		this.workPlanId = workPlanId;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getEvaluationId()
	{
		return evaluationId;
	}

	public void setEvaluationId(String evaluationId)
	{
		this.evaluationId = evaluationId;
	}

	public String getSubStatus()
	{
		return subStatus;
	}

	public void setSubStatus(String subStatus)
	{
		this.subStatus = subStatus;
	}

	public String getCurrentStepId()
	{
		return currentStepId;
	}

	public void setCurrentStepId(String currentStepId)
	{
		this.currentStepId = currentStepId;
	}

	public String getCurrentGroupAssigned()
	{
		return currentGroupAssigned;
	}

	public void setCurrentGroupAssigned(String currentGroupAssigned)
	{
		this.currentGroupAssigned = currentGroupAssigned;
	}

	public String getCurrentUserAssigned()
	{
		return currentUserAssigned;
	}

	public void setCurrentUserAssigned(String currentUserAssigned)
	{
		this.currentUserAssigned = currentUserAssigned;
	}

}
