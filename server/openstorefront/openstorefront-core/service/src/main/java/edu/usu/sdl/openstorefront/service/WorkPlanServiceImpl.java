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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.api.WorkPlanService;
import edu.usu.sdl.openstorefront.core.entity.ChangeLog;
import edu.usu.sdl.openstorefront.core.entity.ChangeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStep;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cyearsley
 */
public class WorkPlanServiceImpl
		extends ServiceProxy
		implements WorkPlanService
{

	private static final Logger LOG = Logger.getLogger(WorkPlanServiceImpl.class.getName());

	@Override
	public WorkPlan saveWorkPlan(WorkPlan workPlan)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void activateWorkPlan(String workPlanId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void removeWorkPlan(String workPlanId, String newWorkPlanId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public WorkPlanLink getWorkPlanForComponent(String componentId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void assignWorkPlanForComponent(String workPlanId, String workLinkId, String username, String roleGroup)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void previousStep(WorkPlanLink workPlanLink)
	{
		WorkPlan workPlan = getWorkPlan(workPlanLink.getWorkPlanId());

		String previousStepId = null;
		for (WorkPlanStep step : workPlan.getSteps()) {
			if (step.getWorkPlanStepId().equals(step.getWorkPlanStepId())) {
				break;
			}
			previousStepId = step.getWorkPlanStepId();
		}

		if (previousStepId != null) {
			moveWorkLinkToStep(workPlanLink, previousStepId);
		}
	}

	@Override
	public void nextStep(WorkPlanLink workPlanLink)
	{
		WorkPlan workPlan = getWorkPlan(workPlanLink.getWorkPlanId());

		String nextStepId = null;
		boolean captureNext = false;
		for (WorkPlanStep step : workPlan.getSteps()) {
			if (captureNext) {
				nextStepId = step.getWorkPlanStepId();
				break;
			}
			if (step.getWorkPlanStepId().equals(step.getWorkPlanStepId())) {
				captureNext = true;
			}
		}

		if (nextStepId != null) {
			moveWorkLinkToStep(workPlanLink, nextStepId);
		}
	}

	@Override
	public WorkPlanLink moveWorkLinkToStep(WorkPlanLink workPlanLink, String workPlanStepId)
	{
		WorkPlan workPlan = getWorkPlan(workPlanLink.getWorkPlanId());
		String oldStepId = workPlanLink.getCurrentStepId();

		if (workPlanLink.getCurrentStepId().equals(workPlanStepId)) {
			LOG.log(Level.FINE, "Work Plan Link is already on step request.");
		} else {
			WorkPlanStep workPlanStep = workPlan.findWorkPlanStep(workPlanStepId);
			if (workPlanStep != null) {

				//TODO: apply actions
				workPlanLink.setCurrentStepId(workPlanStepId);
				workPlanLink.save();

				ChangeLog changeLog = new ChangeLog();
				changeLog.setChangeLogId(persistenceService.generateId());
				changeLog.setChangeType(ChangeType.WORKPLAN_CHANGE);
				if (workPlanLink.getComponentId() != null) {
					changeLog.setEntity(Component.class.getSimpleName());
					changeLog.setEntityId(workPlanLink.getComponentId());
				} else {
					changeLog.setEntity(Component.class.getSimpleName());
					changeLog.setEntityId(workPlanLink.getEvaluationId());
				}
				WorkPlanStep oldStep = workPlan.findWorkPlanStep(oldStepId);

				changeLog.setComment("WorkPlan plan updated. Step Changed from: "
						+ (oldStep != null ? oldStep.getName() : "N/A")
						+ " to " + workPlanStep.getName());

				changeLog.populateBaseCreateFields();
				getChangeLogService().saveChangeRecord(changeLog);

				//TODO: post notification
			} else {
				throw new OpenStorefrontRuntimeException("Unable to find Work Plan Step.", "Check data; step Id: " + workPlanStepId + " workplanId: " + workPlanLink.getWorkPlanId());
			}
		}
		return workPlanLink;
	}

	@Override
	public void syncWorkPlanLinks()
	{
		//process all entry (x at time)

	}

	@Override
	public void updatedWorkPlanLinkToMatchState(String componentId)
	{

	}

	@Override
	public WorkPlan getWorkPlan(String workplanId)
	{
		return getWorkPlan(workplanId, false);
	}

	public WorkPlan getWorkPlan(String workplanId, boolean throwError)
	{
		WorkPlan workPlan = new WorkPlan();
		workPlan.setWorkPlanId(workplanId);
		workPlan = workPlan.find();
		if (workPlan == null) {
			throw new OpenStorefrontRuntimeException("Unable to find work plan", "Check workPlanId: " + workplanId);
		}

		//make sure steps are in order
		if (workPlan.getSteps() == null) {
			workPlan.setSteps(new ArrayList<>());
		}
		workPlan.getSteps().sort((a, b) -> {
			return a.getStepOrder().compareTo(b.getStepOrder());
		});
		for (WorkPlanStep workPlanStep : workPlan.getSteps()) {
			if (workPlanStep.getActions() == null) {
				workPlanStep.setActions(new ArrayList<>());
			}
			workPlanStep.getActions().sort((a, b) -> {
				return a.getActionOrder().compareTo(b.getActionOrder());
			});
		}
		return workPlan;
	}

}
