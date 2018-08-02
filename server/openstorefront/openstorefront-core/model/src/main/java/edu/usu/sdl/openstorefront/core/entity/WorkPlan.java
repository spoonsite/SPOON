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
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ra nethercott
 */
@APIDescription("Holds work plan information")
public class WorkPlan
		extends StandardEntity<WorkPlan>
{

	private static final long serialVersionUID = 1L;

	@PK(generated = true)
	@NotNull
	private String workPlanId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String name;

	@ConsumeField
	@Embedded
	@DataType(WorkPlanStep.class)
	@OneToMany(orphanRemoval = true)
	private List<WorkPlanStep> steps;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String inProgressColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String pendingColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String completeColor;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String subStatusColor;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = WorkPlanType.class)
	@FK(WorkPlanType.class)
	private String workPlanType;

	@ConsumeField
	@Embedded
	@OneToMany(orphanRemoval = true)
	@DataType(WorkPlanComponentType.class)
	private List<WorkPlanComponentType> componentTypes;

	@ConsumeField
	private String evaluationTemplateId;

	@ConsumeField
	private Boolean appliesToChildComponentTypes;

	private Boolean defaultWorkPlan;

	@ConsumeField
	@FK(SecurityRole.class)
	private String adminRole;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public WorkPlan()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		WorkPlan workPlan = (WorkPlan) entity;

		this.setAppliesToChildComponentTypes(workPlan.getAppliesToChildComponentTypes());
		this.setCompleteColor(workPlan.getCompleteColor());
		this.setComponentTypes(workPlan.getComponentTypes());
		this.setDefaultWorkPlan(workPlan.getDefaultWorkPlan());
		this.setEvaluationTemplateId(workPlan.getEvaluationTemplateId());
		this.setInProgressColor(workPlan.getInProgressColor());
		this.setName(workPlan.getName());
		this.setPendingColor(workPlan.getPendingColor());
		this.setSteps(workPlan.getSteps());
		this.setSubStatusColor(workPlan.getSubStatusColor());
		this.setWorkPlanType(workPlan.getWorkPlanType());
		this.setAdminRole(workPlan.getAdminRole());
	}

	public WorkPlanStep findWorkPlanStep(String workPlanStepId)
	{
		WorkPlanStep found = null;

		if (this.getSteps() != null) {
			for (WorkPlanStep step : getSteps()) {
				if (step.getWorkPlanStepId().equals(workPlanStepId)) {
					found = step;
				}
			}
		}
		return found;
	}

	public String getWorkPlanId()
	{
		return workPlanId;
	}

	public void setWorkPlanId(String workPlanId)
	{
		this.workPlanId = workPlanId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<WorkPlanStep> getSteps()
	{
		return steps;
	}

	public void setSteps(List<WorkPlanStep> steps)
	{
		this.steps = steps;
	}

	public String getInProgressColor()
	{
		return inProgressColor;
	}

	public void setInProgressColor(String inProgressColor)
	{
		this.inProgressColor = inProgressColor;
	}

	public String getPendingColor()
	{
		return pendingColor;
	}

	public void setPendingColor(String pendingColor)
	{
		this.pendingColor = pendingColor;
	}

	public String getCompleteColor()
	{
		return completeColor;
	}

	public void setCompleteColor(String completeColor)
	{
		this.completeColor = completeColor;
	}

	public String getSubStatusColor()
	{
		return subStatusColor;
	}

	public void setSubStatusColor(String subStatusColor)
	{
		this.subStatusColor = subStatusColor;
	}

	public String getWorkPlanType()
	{
		return workPlanType;
	}

	public void setWorkPlanType(String workPlanType)
	{
		this.workPlanType = workPlanType;
	}

	public List<WorkPlanComponentType> getComponentTypes()
	{
		return componentTypes;
	}

	public void setComponentTypes(List<WorkPlanComponentType> componentTypes)
	{
		this.componentTypes = componentTypes;
	}

	public Boolean getAppliesToChildComponentTypes()
	{
		return appliesToChildComponentTypes;
	}

	public void setAppliesToChildComponentTypes(Boolean appliesToChildComponentTypes)
	{
		this.appliesToChildComponentTypes = appliesToChildComponentTypes;
	}

	public Boolean getDefaultWorkPlan()
	{
		return defaultWorkPlan;
	}

	public void setDefaultWorkPlan(Boolean defaultWorkPlan)
	{
		this.defaultWorkPlan = defaultWorkPlan;
	}

	public String getEvaluationTemplateId()
	{
		return evaluationTemplateId;
	}

	public void setEvaluationTemplateId(String evaluationTemplateId)
	{
		this.evaluationTemplateId = evaluationTemplateId;
	}

	public String getAdminRole()
	{
		return adminRole;
	}

	public void setAdminRole(String adminRole)
	{
		this.adminRole = adminRole;
	}

}
