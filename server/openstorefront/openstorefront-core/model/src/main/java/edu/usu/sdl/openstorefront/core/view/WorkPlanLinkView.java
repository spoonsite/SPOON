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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.UserSubmission;
import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLinkType;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStep;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanSubStatusType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class WorkPlanLinkView
		extends WorkPlanLink
{

	private static final long serialVersionUID = 1L;

	private String workPlanName;
	private List<WorkPlanStepView> steps = new ArrayList<>();
	private String linkType;
	private String linkName;
	private String componentType;
	private String componentTypeDescription;
	private String componentTypeFullDescription;
	private String subStatusDescription;
	private WorkPlanStepView currentStep;
	private String workPlanInProgressColor;
	private String workPlanPendingColor;
	private String workPlanCompleteColor;
	private String workPlanSubStatusColor;

	public WorkPlanLinkView()
	{
	}

	public static WorkPlanLinkView toView(WorkPlanLink workPlanLink)
	{
		WorkPlanLinkView view = new WorkPlanLinkView();

		try {
			BeanUtils.copyProperties(view, workPlanLink);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}

		Service service = ServiceProxyFactory.getServiceProxy();

		WorkPlan workPlan = service.getWorkPlanService().getWorkPlan(workPlanLink.getWorkPlanId());
		view.setWorkPlanName(workPlan.getName());
		view.setWorkPlanCompleteColor(workPlan.getCompleteColor());
		view.setWorkPlanInProgressColor(workPlan.getInProgressColor());
		view.setWorkPlanPendingColor(workPlan.getPendingColor());
		view.setWorkPlanSubStatusColor(workPlan.getSubStatusColor());
		view.setSteps(WorkPlanStepView.toView(workPlan.getSteps()));
		view.setSubStatusDescription(TranslateUtil.translate(WorkPlanSubStatusType.class, workPlanLink.getSubStatus()));
		view.setLinkType(TranslateUtil.translate(WorkPlanLinkType.class, WorkPlanLinkType.typeForWorkLink(workPlanLink)));

		if (workPlanLink.getComponentId() != null) {
			view.setLinkName(service.getComponentService().getComponentName(workPlanLink.getComponentId()));
			view.setComponentType(service.getComponentService().getComponentTypeForComponent(workPlanLink.getComponentId()));
			view.setComponentTypeDescription(TranslateUtil.translateComponentType(view.getComponentType()));
			view.setComponentTypeFullDescription(service.getComponentService().getComponentTypeParentsString(view.getComponentType(), false));
		} else if (workPlanLink.getUserSubmissionId() != null) {
			UserSubmission userSubmission = new UserSubmission();
			userSubmission.setUserSubmissionId(workPlanLink.getUserSubmissionId());
			userSubmission = userSubmission.find();

			if (userSubmission != null) {
				view.setLinkName(userSubmission.getSubmissionName());
				view.setComponentType(userSubmission.getComponentType());
				view.setComponentTypeDescription(TranslateUtil.translateComponentType(view.getComponentType()));
				view.setComponentTypeFullDescription(service.getComponentService().getComponentTypeParentsString(view.getComponentType(), false));
			}
		}
		//add handling for evaluations

		WorkPlanStep step = workPlan.findWorkPlanStep(workPlanLink.getCurrentStepId());
		if (step != null) {
			view.setCurrentStep(WorkPlanStepView.toView(step));
		}

		return view;
	}

	public static List<WorkPlanLinkView> toView(List<WorkPlanLink> links)
	{
		List<WorkPlanLinkView> views = new ArrayList<>();

		links.forEach(link -> {
			views.add(toView(link));
		});

		return views;
	}

	public String getWorkPlanName()
	{
		return workPlanName;
	}

	public void setWorkPlanName(String workPlanName)
	{
		this.workPlanName = workPlanName;
	}

	public List<WorkPlanStepView> getSteps()
	{
		return steps;
	}

	public void setSteps(List<WorkPlanStepView> steps)
	{
		this.steps = steps;
	}

	public String getLinkType()
	{
		return linkType;
	}

	public void setLinkType(String linkType)
	{
		this.linkType = linkType;
	}

	public String getLinkName()
	{
		return linkName;
	}

	public void setLinkName(String linkName)
	{
		this.linkName = linkName;
	}

	public String getSubStatusDescription()
	{
		return subStatusDescription;
	}

	public void setSubStatusDescription(String subStatusDescription)
	{
		this.subStatusDescription = subStatusDescription;
	}

	public WorkPlanStepView getCurrentStep()
	{
		return currentStep;
	}

	public void setCurrentStep(WorkPlanStepView currentStep)
	{
		this.currentStep = currentStep;
	}

	public String getComponentType()
	{
		return componentType;
	}

	public void setComponentType(String componentType)
	{
		this.componentType = componentType;
	}

	public String getComponentTypeDescription()
	{
		return componentTypeDescription;
	}

	public void setComponentTypeDescription(String componentTypeDescription)
	{
		this.componentTypeDescription = componentTypeDescription;
	}

	public String getComponentTypeFullDescription()
	{
		return componentTypeFullDescription;
	}

	public void setComponentTypeFullDescription(String componentTypeFullDescription)
	{
		this.componentTypeFullDescription = componentTypeFullDescription;
	}

	public String getWorkPlanInProgressColor()
	{
		return workPlanInProgressColor;
	}

	public void setWorkPlanInProgressColor(String workPlanInProgressColor)
	{
		this.workPlanInProgressColor = workPlanInProgressColor;
	}

	public String getWorkPlanPendingColor()
	{
		return workPlanPendingColor;
	}

	public void setWorkPlanPendingColor(String workPlanPendingColor)
	{
		this.workPlanPendingColor = workPlanPendingColor;
	}

	public String getWorkPlanCompleteColor()
	{
		return workPlanCompleteColor;
	}

	public void setWorkPlanCompleteColor(String workPlanCompleteColor)
	{
		this.workPlanCompleteColor = workPlanCompleteColor;
	}

	public String getWorkPlanSubStatusColor()
	{
		return workPlanSubStatusColor;
	}

	public void setWorkPlanSubStatusColor(String workPlanSubStatusColor)
	{
		this.workPlanSubStatusColor = workPlanSubStatusColor;
	}

}
