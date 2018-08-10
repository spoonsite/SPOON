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
import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class WorkPlanView
{

	private String workPlanId;
	private String name;

	@DataType(WorkPlanFullStepView.class)
	private List<WorkPlanFullStepView> steps = new ArrayList<>();

	private String inProgressColor;
	private String pendingColor;
	private String completeColor;
	private String subStatusColor;
	private String workPlanType;
	private String workPlanTypeDescription;

	@DataType(WorkPlanComponentTypeView.class)
	private List<WorkPlanComponentTypeView> componentTypes = new ArrayList<>();

	private String evaluationTemplateId;
	private Boolean appliesToChildComponentTypes;
	private Boolean defaultWorkPlan;
	private String adminRole;
	private String activeStatus;
	private String createUser;
	private String updateUser;
	private Date createDts;
	private Date updateDts;

	public WorkPlanView()
	{
	}

	public static WorkPlanView toView(WorkPlan workPlan)
	{
		WorkPlanView view = new WorkPlanView();

		view.setWorkPlanId(workPlan.getWorkPlanId());
		view.setName(workPlan.getName());
		view.setInProgressColor(workPlan.getInProgressColor());
		view.setPendingColor(workPlan.getPendingColor());
		view.setCompleteColor(workPlan.getCompleteColor());
		view.setSubStatusColor(workPlan.getSubStatusColor());
		view.setWorkPlanType(workPlan.getWorkPlanType());
		view.setWorkPlanTypeDescription(TranslateUtil.translate(WorkPlanType.class, workPlan.getWorkPlanType()));
		view.setEvaluationTemplateId(workPlan.getEvaluationTemplateId());
		view.setAppliesToChildComponentTypes(workPlan.getAppliesToChildComponentTypes());
		view.setDefaultWorkPlan(workPlan.getDefaultWorkPlan());
		view.setAdminRole(workPlan.getAdminRole());
		view.setActiveStatus(workPlan.getActiveStatus());
		view.setCreateUser(workPlan.getCreateUser());
		view.setCreateDts(workPlan.getCreateDts());
		view.setUpdateUser(workPlan.getUpdateUser());
		view.setUpdateDts(workPlan.getUpdateDts());

		if (workPlan.getSteps() != null) {
			view.setSteps(WorkPlanFullStepView.toView(workPlan.getSteps()));
		}

		if (workPlan.getComponentTypes() != null) {
			view.setComponentTypes(WorkPlanComponentTypeView.toView(workPlan.getComponentTypes()));
		}

		return view;
	}

	public static List<WorkPlanView> toView(List<WorkPlan> plans)
	{
		List<WorkPlanView> views = new ArrayList<>();

		plans.forEach(plan -> {
			views.add(toView(plan));
		});

		return views;
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

	public List<WorkPlanFullStepView> getSteps()
	{
		return steps;
	}

	public void setSteps(List<WorkPlanFullStepView> steps)
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

	public String getWorkPlanTypeDescription()
	{
		return workPlanTypeDescription;
	}

	public void setWorkPlanTypeDescription(String workPlanTypeDescription)
	{
		this.workPlanTypeDescription = workPlanTypeDescription;
	}

	public List<WorkPlanComponentTypeView> getComponentTypes()
	{
		return componentTypes;
	}

	public void setComponentTypes(List<WorkPlanComponentTypeView> componentTypes)
	{
		this.componentTypes = componentTypes;
	}

	public String getEvaluationTemplateId()
	{
		return evaluationTemplateId;
	}

	public void setEvaluationTemplateId(String evaluationTemplateId)
	{
		this.evaluationTemplateId = evaluationTemplateId;
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

	public String getAdminRole()
	{
		return adminRole;
	}

	public void setAdminRole(String adminRole)
	{
		this.adminRole = adminRole;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}

	public String getUpdateUser()
	{
		return updateUser;
	}

	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}

	public Date getCreateDts()
	{
		return createDts;
	}

	public void setCreateDts(Date createDts)
	{
		this.createDts = createDts;
	}

	public Date getUpdateDts()
	{
		return updateDts;
	}

	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

}
