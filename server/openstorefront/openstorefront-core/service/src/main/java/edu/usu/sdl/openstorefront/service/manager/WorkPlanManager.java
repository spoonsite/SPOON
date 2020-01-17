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
package edu.usu.sdl.openstorefront.service.manager;

import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.EntityEventType;
import edu.usu.sdl.openstorefront.core.entity.SecurityRole;
import edu.usu.sdl.openstorefront.core.entity.UserSubmission;
import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStep;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStepAction;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStepActionType;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStepEvent;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanType;
import edu.usu.sdl.openstorefront.core.model.EntityEventModel;
import edu.usu.sdl.openstorefront.core.model.EntityEventRegistrationModel;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.job.WorkPlanSyncJob;
import edu.usu.sdl.openstorefront.service.manager.model.AddJobModel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

/**
 * Handles WorkPlan Initializing
 *
 * @author dshurtleff
 */
public class WorkPlanManager
		implements Initializable
{

	private static final Logger LOG = Logger.getLogger(WorkPlanManager.class.getName());

	private static final String JOB_NAME = "WorkPlanSync";
	private static final Integer JOB_INTERVAL_MINUTES = 10;

	private static WorkPlanManager singleton = null;
	private AtomicBoolean started = new AtomicBoolean(false);
	private String registrationId;

	//private JobManager jobManager;
	private WorkPlanManager()
	{
	}

	public static WorkPlanManager getInstance()
	{
		if (singleton == null) {
			singleton = new WorkPlanManager();
		}
		return singleton;
	}

	@Override
	public void initialize()
	{
		ServiceProxy service = ServiceProxy.getProxy();

		initDefaultWorkflow(service);

		//add listener
		EntityEventRegistrationModel registrationModel = new EntityEventRegistrationModel();
		registrationModel.setRegistrationId(StringProcessor.uniqueId());
		registrationModel.setName("WorkPlan Event Handler");
		registrationModel.setListener((entityEventModel) -> {
			triggerHandler(entityEventModel);
			return true;
		});

		service.getEntityEventService().registerEventListener(registrationModel);
		registrationId = registrationModel.getRegistrationId();

		//add job
		AddJobModel addJobModel = new AddJobModel();
		addJobModel.setDescription("Syncs data with workplans");
		addJobModel.setJobGroup(JobManager.JOB_GROUP_SYSTEM);
		addJobModel.setJobName(JOB_NAME);
		addJobModel.setJobClass(WorkPlanSyncJob.class);
		addJobModel.setRepeatForever(true);
		addJobModel.setMinutes(JOB_INTERVAL_MINUTES);
		JobManager.addJob(addJobModel);

		started.set(true);
	}

	/**
	 * This function passes an event to all triggers that might be triggered by the event. The event is passed to
	 * all triggers regardless of whether the trigger is relevant or not, so it is up to the trigger itself to
	 * determine if the event in question is something that can fire the trigger.
	 *
	 * To add more triggers (Such as for various Active On options found in the WorkPlan management webpage) add
	 * another function call here.
	 *
	 * @param entityEventModel
	 */
	@SuppressWarnings("unchecked")
	protected void triggerHandler(EntityEventModel entityEventModel)
	{
		handleComponentEvent(entityEventModel);
		handleSubmissionEvent(entityEventModel);
	}

	private void handleSubmissionEvent(EntityEventModel entityEventModel)
	{
		if (EntityEventType.NEW_SUBMISSION_NOT_SUBMITTTED.equals(entityEventModel.getEventType())
				&& (entityEventModel.getEntityChanged() != null
				&& entityEventModel.getEntityChanged().getClass().isAssignableFrom(UserSubmission.class))) {
			ServiceProxy service = ServiceProxy.getProxy();
			UserSubmission userSubmission = (UserSubmission) entityEventModel.getEntityChanged();
			service.getWorkPlanService().getWorkPlanLinkForSubmission(userSubmission.getUserSubmissionId());
		}

	}

	private void handleComponentEvent(EntityEventModel entityEventModel)
	{
		//For now we are only look for events related to components (ignore bulk, rely on sync)
		Component component = null;
		if ((entityEventModel.getEntityChanged() != null
				&& entityEventModel.getEntityChanged().getClass().isAssignableFrom(Component.class))) {
			component = (Component) entityEventModel.getEntityChanged();
		}

		if (component != null) {
			ServiceProxy service = ServiceProxy.getProxy();
			WorkPlanLink workPlanLink = service.getWorkPlanService().getWorkPlanForComponent(component.getComponentId());

			WorkPlan workPlan = service.getWorkPlanService().getWorkPlan(workPlanLink.getWorkPlanId());

			String eventStep = null;
			for (WorkPlanStep step : workPlan.getSteps()) {
				if (step.getTriggerEvents() != null
						&& step.getTriggerEvents().stream().anyMatch(trigger -> {
							return trigger.getEntityEventType().equals(entityEventModel.getEventType());
						})) {

					//get first match
					eventStep = step.getWorkPlanStepId();
					break;
				}
			}

			if (eventStep != null && !eventStep.equals(workPlanLink.getCurrentStepId())) {
				service.getWorkPlanService().moveWorkLinkToStep(workPlanLink, eventStep, false);
			}
		}
	}

	private void initDefaultWorkflow(ServiceProxy service)
	{
		WorkPlan workPlanDefault = new WorkPlan();
		workPlanDefault.setDefaultWorkPlan(Boolean.TRUE);
		workPlanDefault.setWorkPlanType(WorkPlanType.COMPONENT);
		workPlanDefault = workPlanDefault.find();

		if (workPlanDefault == null) {
			LOG.log(Level.INFO, "Adding Default Entry Work Plan");

			workPlanDefault = new WorkPlan();
			workPlanDefault.setName("DEFAULT-ENTRY");
			workPlanDefault.setDefaultWorkPlan(Boolean.TRUE);
			workPlanDefault.setAdminRole(SecurityRole.ADMIN_ROLE);
			workPlanDefault.setWorkPlanType(WorkPlanType.COMPONENT);
			workPlanDefault.setPendingColor("cccccc");
			workPlanDefault.setInProgressColor("777aea");
			workPlanDefault.setCompleteColor("84d053");
			workPlanDefault.setSubStatusColor("ff0000");

			List<WorkPlanStep> workPlanSteps = new ArrayList<>();

			WorkPlanStep step = new WorkPlanStep();
			step.setWorkPlanStepId(service.getPersistenceService().generateId());
			step.setName("Not Submitted");
			step.setDescription("Entry is created; not ready for admin approval.");
			step.setApprovalStateToMatch(ApprovalStatus.NOT_SUBMITTED);
			step.setStepOrder(0);
			step.setTriggerEvents(new ArrayList<>());
			WorkPlanStepEvent workPlanStepEvent = new WorkPlanStepEvent();
			workPlanStepEvent.setEntityEventType(EntityEventType.CREATE);
			step.getTriggerEvents().add(workPlanStepEvent);
			step.setActions(new ArrayList<>());
			workPlanSteps.add(step);

			step = new WorkPlanStep();
			step.setWorkPlanStepId(service.getPersistenceService().generateId());
			step.setName("Pending");
			step.setDescription("Entry is waiting for admin approval");
			step.setApprovalStateToMatch(ApprovalStatus.PENDING);
			step.setStepOrder(0);
			step.setTriggerEvents(new ArrayList<>());
			workPlanStepEvent = new WorkPlanStepEvent();
			workPlanStepEvent.setEntityEventType(EntityEventType.PENDING);
			step.getTriggerEvents().add(workPlanStepEvent);

			workPlanStepEvent = new WorkPlanStepEvent();
			workPlanStepEvent.setEntityEventType(EntityEventType.PENDING_CHANGE_REQUEST);
			step.getTriggerEvents().add(workPlanStepEvent);
			step.setActions(new ArrayList<>());

			WorkPlanStepAction action = new WorkPlanStepAction();
			action.setWorkPlanStepActionId(service.getPersistenceService().generateId());
			action.setActionOrder(0);
			action.setWorkPlanStepActionType(WorkPlanStepActionType.PENDING_ENTRY);
			step.getActions().add(action);
			workPlanSteps.add(step);

			step = new WorkPlanStep();
			step.setWorkPlanStepId(service.getPersistenceService().generateId());
			step.setName("Approved");
			step.setDescription("Entry is Approved");
			step.setApprovalStateToMatch(ApprovalStatus.APPROVED);
			step.setStepOrder(0);
			step.setTriggerEvents(new ArrayList<>());

			workPlanStepEvent = new WorkPlanStepEvent();
			workPlanStepEvent.setEntityEventType(EntityEventType.APPROVED);
			step.getTriggerEvents().add(workPlanStepEvent);

			step.setActions(new ArrayList<>());

			action = new WorkPlanStepAction();
			action.setWorkPlanStepActionId(service.getPersistenceService().generateId());
			action.setActionOrder(0);
			action.setWorkPlanStepActionType(WorkPlanStepActionType.APPROVE_ENTRY);
			step.getActions().add(action);
			workPlanSteps.add(step);

			workPlanDefault.setSteps(workPlanSteps);
			service.getWorkPlanService().saveWorkPlan(workPlanDefault, false);

			LOG.log(Level.INFO, "Finish Adding Default Entry Work Plan");
		}

		//ADD SUPPORT: init default for Eval
	}

	@Override
	public void shutdown()
	{
		ServiceProxy serviceProxy = ServiceProxy.getProxy();

		if (StringUtils.isNotBlank(registrationId)) {
			serviceProxy.getEntityEventService().unregisterEventListener(registrationId);
		}

		JobManager.removeJob(JOB_NAME, JobManager.JOB_GROUP_SYSTEM);
		started.set(false);
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

}
