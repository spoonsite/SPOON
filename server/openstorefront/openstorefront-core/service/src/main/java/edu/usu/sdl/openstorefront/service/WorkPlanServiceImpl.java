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
import edu.usu.sdl.openstorefront.common.util.RetryUtil;
import edu.usu.sdl.openstorefront.core.api.WorkPlanService;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.ChangeLog;
import edu.usu.sdl.openstorefront.core.entity.ChangeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;
import edu.usu.sdl.openstorefront.core.entity.NotificationEventType;
import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanComponentType;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStep;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStepAction;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeNestedModel;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeOptions;
import edu.usu.sdl.openstorefront.core.model.EvaluationAll;
import edu.usu.sdl.openstorefront.core.model.WorkPlanRemoveMigration;
import edu.usu.sdl.openstorefront.core.model.WorkPlanStepMigration;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.service.workplan.BaseWorkPlanStepAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;

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
		Objects.requireNonNull(workPlan);
		updateWorkPlanFields(workPlan);

		if (workPlan.getWorkPlanId() != null) {
			WorkPlan workPlanExample = new WorkPlan();
			workPlanExample.setWorkPlanId(workPlan.getWorkPlanId());

			workPlanExample = workPlanExample.findProxy();
			if (workPlanExample != null) {
				workPlanExample.updateFields(workPlan);
				persistenceService.persist(workPlanExample);
			}
		} else {
			workPlan.setWorkPlanId(persistenceService.generateId());
			workPlan.populateBaseCreateFields();

			workPlan.save();
		}
		clearCache();

		return workPlan;
	}

	private void updateWorkPlanFields(WorkPlan workPlan)
	{
		workPlan.getSteps().forEach(step -> {
			step.populateBaseCreateFields();

			step.getActions().forEach(action -> {
				action.populateBaseCreateFields();
				action.setWorkPlanStepActionId(persistenceService.generateId());
			});
		});
	}

	@Override
	public void activateWorkPlan(String workPlanId)
	{
		//make only allows one active with the same entry type
		//other ones are inactivated.

		WorkPlan workPlan = persistenceService.findById(WorkPlan.class, workPlanId);
		if (workPlan != null) {
			WorkPlan workPlanExample = new WorkPlan();
			List<WorkPlan> existing = workPlanExample.findByExampleProxy();
			existing.removeIf((plan) -> {
				return plan.getWorkPlanId().equals(workPlan.getWorkPlanId());
			});

			if (workPlan.getComponentTypes() != null) {
				Set<String> componentTypesToMatch = workPlan.getComponentTypes().stream()
						.map(WorkPlanComponentType::getComponentType)
						.collect(Collectors.toSet());

				List<WorkPlan> workPlanToInactivate = new ArrayList<>();
				for (WorkPlan existingWorkPlan : existing) {
					if (existingWorkPlan.getComponentTypes() != null) {
						Set<String> existingComponentTypes = existingWorkPlan.getComponentTypes().stream()
								.map(WorkPlanComponentType::getComponentType)
								.collect(Collectors.toSet());

						if (componentTypesToMatch.stream().anyMatch(existingComponentTypes::contains)) {
							workPlanToInactivate.add(existingWorkPlan);
						}
					}
				}

				workPlanToInactivate.forEach(inactivePlan -> {
					inactivePlan.setActiveStatus(WorkPlan.INACTIVE_STATUS);
					inactivePlan.populateBaseUpdateFields();
					persistenceService.persist(inactivePlan);
				});

			}
			//evalutions will need a separate check

		} else {
			throw new OpenStorefrontRuntimeException("Unable to activate workplan.", "Check data and Refresh. WorkplanId: " + workPlanId);
		}
	}

	@Override
	public void removeWorkPlan(String workPlanId, WorkPlanRemoveMigration workPlanRemoveMigration)
	{
		WorkPlan workPlan = persistenceService.findById(WorkPlan.class, workPlanId);
		if (workPlan != null) {

			if (workPlanRemoveMigration != null) {
				WorkPlanLink workPlanLinkExample = new WorkPlanLink();
				workPlanLinkExample.setWorkPlanId(workPlan.getWorkPlanId());
				List<WorkPlanLink> workPlanLinks = workPlanLinkExample.findByExampleProxy();

				for (WorkPlanLink workPlanLink : workPlanLinks) {
					workPlanLink.setWorkPlanId(workPlanRemoveMigration.getWorkPlanId());

					Map<String, String> stepConvertMap = workPlanRemoveMigration.getStepMigrations()
							.stream()
							.collect(Collectors.toMap(WorkPlanStepMigration::getFromStepId, WorkPlanStepMigration::getToStepId));

					if (stepConvertMap.containsKey(workPlanLink.getCurrentStepId())) {
						String newStep = stepConvertMap.get(workPlanLink.getCurrentStepId());
						workPlanLink.setCurrentStepId(newStep);
						//Note: were not apply actions
					}

					workPlanLink.populateBaseUpdateFields();
					persistenceService.persist(workPlanLink);
				}
			}

			//remove links
			WorkPlanLink workplanLink = new WorkPlanLink();
			workplanLink.setWorkPlanId(workPlan.getWorkPlanId());
			persistenceService.deleteByExample(workplanLink);

			persistenceService.delete(workPlan);

			clearCache();
		}
	}

	@Override
	public WorkPlanLink getWorkPlanForComponent(String componentId)
	{
		Objects.requireNonNull(componentId);

		WorkPlanLink workPlanLink;

		//look for link
		workPlanLink = new WorkPlanLink();
		workPlanLink.setActiveStatus(WorkPlanLink.ACTIVE_STATUS);
		workPlanLink.setComponentId(componentId);

		workPlanLink = workPlanLink.find();

		if (workPlanLink != null) {
			//Exists: check for type vs workplan
			String componentType = getComponentService().getComponentTypeForComponent(componentId);
			WorkPlan workPlan = getWorkPlan(workPlanLink.getWorkPlanId());
			Set<String> workPlanType = workPlan.getComponentTypes().stream()
					.map(WorkPlanComponentType::getComponentType)
					.collect(Collectors.toSet());
			if (!workPlanType.contains(componentType)) {
				//move to plan
				WorkPlan newWorkPlan = getWorkPlanForComponentType(componentType);
				String stepId = matchWorkPlanStepWithStatus(newWorkPlan, componentId);

				workPlanLink.setWorkPlanId(newWorkPlan.getWorkPlanId());
				workPlanLink.setCurrentStepId(stepId);

				workPlanLink.save();
			}
		} else {
			workPlanLink = createWorkPlanLink(componentId);
		}

		return workPlanLink;
	}

	private WorkPlanLink createWorkPlanLink(String componentId)
	{
		WorkPlanLink workPlanLink = new WorkPlanLink();
		workPlanLink.setComponentId(componentId);
		workPlanLink.setWorkPlanLinkId(persistenceService.generateId());

		//get workplan
		String componentType = getComponentService().getComponentTypeForComponent(componentId);
		if (StringUtils.isNotBlank(componentType)) {
			WorkPlan workPlan = getWorkPlanForComponentType(componentType);

			ComponentTypeOptions componentTypeOptions = new ComponentTypeOptions();
			componentTypeOptions.setComponentType(componentType);
			ComponentTypeNestedModel nestedModel = getComponentService().getComponentType(componentTypeOptions);

			workPlanLink.setWorkPlanId(workPlan.getWorkPlanId());

			if (!nestedModel.getComponentType().getRoles().getRoles().isEmpty()) {
				//assign to first group
				workPlanLink.setCurrentGroupAssigned(nestedModel.getComponentType().getRoles().getRoles().get(0));
			}
			workPlanLink.setCurrentStepId(matchWorkPlanStepWithStatus(workPlan, componentId));
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find entry.", "Check data and refresh. ComponentId: " + componentId);
		}

		RetryUtil.retryAction(3, () -> {
			workPlanLink.save();
		});

		return workPlanLink;
	}

	private String matchWorkPlanStepWithStatus(WorkPlan workPlan, String componentId)
	{
		Component component = new Component();
		component.setComponentId(componentId);
		component = component.find();

		initSortWorkPlan(workPlan);

		WorkPlanStep step = workPlan.getSteps().get(0);
		for (WorkPlanStep workPlanStep : workPlan.getSteps()) {
			if (StringUtils.isNotBlank(workPlanStep.getApprovalStateToMatch())
					&& workPlanStep.getApprovalStateToMatch().equals(component.getApprovalState())) {
				step = workPlanStep;
			}
		}

		return step.getWorkPlanStepId();
	}

	@Override
	public void assignWorkPlan(String workPlanId, String workPlanLinkId, String username, String roleGroup)
	{
		Objects.requireNonNull(workPlanId);
		Objects.requireNonNull(workPlanLinkId);

		//find link
		WorkPlanLink workPlanLink = new WorkPlanLink();
		workPlanLink.setWorkPlanId(workPlanId);
		workPlanLink.setWorkPlanLinkId(workPlanLinkId);
		workPlanLink = workPlanLink.findProxy();

		if (workPlanLink != null) {
			workPlanLink.setCurrentUserAssigned(username);
			if (StringUtils.isNotBlank(roleGroup)) {
				workPlanLink.setCurrentGroupAssigned(roleGroup);
			}
			persistenceService.persist(workPlanLink);
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find Work Plan Link");
		}
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

				applyStepActions(workPlanLink, workPlan, workPlanStep);

				workPlanLink.setCurrentStepId(workPlanStepId);
				workPlanLink.save();

				logWorkPlanChange(workPlanLink, workPlan, oldStepId, workPlanStep);

				if (StringUtils.isNotBlank(workPlanLink.getCurrentUserAssigned())) {
					postNoticationOfWorkPlanChange(workPlanLink);
				}

			} else {
				throw new OpenStorefrontRuntimeException("Unable to find Work Plan Step.", "Check data; step Id: " + workPlanStepId + " workplanId: " + workPlanLink.getWorkPlanId());
			}
		}
		return workPlanLink;
	}

	private void logWorkPlanChange(WorkPlanLink workPlanLink, WorkPlan workPlan, String oldStepId, WorkPlanStep workPlanStep)
	{
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
	}

	private void postNoticationOfWorkPlanChange(WorkPlanLink workPlanLink)
	{
		NotificationEvent notificationEvent = new NotificationEvent();
		notificationEvent.setEventType(NotificationEventType.REPORT);
		notificationEvent.setUsername(workPlanLink.getCurrentUserAssigned());

		String workPlanItemName = "N/A";
		if (workPlanLink.getComponentId() != null) {
			workPlanItemName = getComponentService().getComponentName(workPlanLink.getComponentId());
		} else if (workPlanLink.getEvaluationId() != null) {
			EvaluationAll evaluationAll = getEvaluationService().getEvaluation(workPlanLink.getEvaluationId());
			String entryName = getComponentService().getComponentName(evaluationAll.getEvaluation().getComponentId());
			if (StringUtils.isNotBlank(entryName)) {
				entryName = getComponentService().getComponentName(evaluationAll.getEvaluation().getOriginComponentId());
			}

			workPlanItemName = "Evaluation - " + entryName;
		}
		notificationEvent.setMessage("Work Plan Updated for: " + workPlanItemName);
		notificationEvent.setEntityName(WorkPlanLink.class.getSimpleName());
		notificationEvent.setEntityId(workPlanLink.getWorkPlanLinkId());
		getNotificationService().postEvent(notificationEvent);
	}

	private void applyStepActions(WorkPlanLink workPlanLink, WorkPlan workPlan, WorkPlanStep workPlanStep)
	{
		if (workPlanStep.getActions() != null) {
			for (WorkPlanStepAction action : workPlanStep.getActions()) {
				BaseWorkPlanStepAction.createAndPerformAction(workPlanLink, workPlan, action);
			}
		}
	}

	@Override
	public void syncWorkPlanLinks()
	{
		//process all entries (x at time)
		int maxResultsToProcess = 200;

		Component componentExample = new Component();
		long totalCount = persistenceService.countByExample(componentExample);

		int startIndex = 0;
		int synced = 0;
		while (startIndex < totalCount) {

			QueryByExample<Component> queryByExample = new QueryByExample<>(componentExample);
			queryByExample.setFirstResult(startIndex);
			queryByExample.setMaxResults(maxResultsToProcess);

			List<Component> components = persistenceService.queryByExample(queryByExample);
			for (Component component : components) {
				//ignore return
				createWorkPlanLink(component.getComponentId());

				synced++;
			}

			startIndex += maxResultsToProcess;
		}
		LOG.log(Level.FINE, "Synced {0} worklinks.", synced);

		//Sync Evaluations: Future
	}

	@Override
	public void updatedWorkPlanLinkToMatchState(String componentId)
	{
		//Assume when this method; the system needs to put the component
		//in the state that matches the component
		WorkPlanLink workPlanLink = getWorkPlanForComponent(componentId);

		WorkPlan workPlan = getWorkPlan(workPlanLink.getWorkPlanId());

		String stepId = matchWorkPlanStepWithStatus(workPlan, componentId);
		if (!workPlanLink.getCurrentStepId().equals(stepId)) {

			//apply actions?
			moveWorkLinkToStep(workPlanLink, stepId);
		}
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

		initSortWorkPlan(workPlan);
		return workPlan;
	}

	private void initSortWorkPlan(WorkPlan workPlan)
	{
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
	}

	@Override
	public WorkPlan getWorkPlanForComponentType(String componentType)
	{
		WorkPlan workPlan = null;

		//check cache
		Element element = OSFCacheManager.getWorkPlanTypeCache().get(componentType);
		if (element != null) {
			workPlan = (WorkPlan) element.getObjectValue();
		} else {
			WorkPlan workPlanExample = new WorkPlan();
			workPlanExample.setActiveStatus(WorkPlan.ACTIVE_STATUS);
			List<WorkPlan> workPlanAll = workPlanExample.findByExample();

			for (WorkPlan workPlanItem : workPlanAll) {
				if (workPlanItem.getComponentTypes() != null) {
					for (WorkPlanComponentType workPlanComponentType : workPlanItem.getComponentTypes()) {

						Element cacheElement = new Element(workPlanComponentType.getComponentType(), workPlan);
						OSFCacheManager.getWorkPlanTypeCache().put(cacheElement);

						if (workPlanItem.getAppliesToChildComponentTypes()) {
							ComponentTypeOptions componentTypeOptions = new ComponentTypeOptions();
							componentTypeOptions.setPullParents(false);
							componentTypeOptions.setComponentType(componentType);
							componentTypeOptions.setPullChildren(true);

							ComponentTypeNestedModel nestedModel = getComponentService().getComponentType(componentTypeOptions);

							List<String> children = nestedModel.findComponentTypeChildren();
							for (String childType : children) {
								cacheElement = new Element(childType, workPlan);
								OSFCacheManager.getWorkPlanTypeCache().put(cacheElement);
							}
						}
					}
				}
			}

			element = OSFCacheManager.getWorkPlanTypeCache().get(componentType);
			if (element != null) {
				workPlan = (WorkPlan) element.getObjectValue();
			}
		}

		//on miss; fill cache
		if (workPlan == null) {
			WorkPlan workPlanExample = new WorkPlan();
			workPlanExample.setDefaultWorkPlan(Boolean.TRUE);
			workPlan = workPlanExample.find();
		}
		return workPlan;
	}

	private void clearCache()
	{
		OSFCacheManager.getWorkPlanTypeCache().removeAll();
	}

}
