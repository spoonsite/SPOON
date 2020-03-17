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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;
import edu.usu.sdl.openstorefront.core.model.WorkPlanModel;
import edu.usu.sdl.openstorefront.core.model.WorkPlanRemoveMigration;
import edu.usu.sdl.openstorefront.core.model.WorkPlanStepMigration;
import java.util.List;

/**
 *
 * @author cyearsley
 */
public interface WorkPlanService
		extends AsyncService
{

	/**
	 * Saves a work plan
	 *
	 * @param workPlan
	 * @return
	 */
	WorkPlan saveWorkPlan(WorkPlan workPlan);

	/**
	 * Updates/Saves a workplan from a workPlanModel
	 *
	 * @param workPlanModel
	 * @return
	 */
	public WorkPlan saveWorkPlan(WorkPlanModel workPlanModel);

	/**
	 * @see saveWorkPlan(WorkPlan workPlan)
	 *
	 * @param workPlan
	 * @param setAsInactive
	 * @return
	 */
	WorkPlan saveWorkPlan(WorkPlan workPlan, boolean setAsInactive);

	/**
	 * Activates a plan / deactivate plan that other active plan that have the
	 * same entry types
	 *
	 * @param workPlanId
	 */
	void activateWorkPlan(String workPlanId);

	/**
	 * Deletes a workplan and transfer to a new workplan...
	 *
	 * @param workPlanId
	 * @param workPlanRemoveMigration (optional)
	 */
	void removeWorkPlan(String workPlanId, WorkPlanRemoveMigration workPlanRemoveMigration);

	/**
	 * Pulls the current plan for the Component or creates one if needed
	 *
	 * @param componentId
	 * @return
	 */
	WorkPlanLink getWorkPlanForComponent(String componentId);

	/**
	 * Assigns a WorkPlan for a worklink
	 *
	 * @param workPlanId
	 * @param workPlanLinkId
	 * @param username
	 * @param roleGroup
	 */
	void assignWorkPlan(String workPlanId, String workPlanLinkId, String username, String roleGroup);

	/**
	 * Moves a Component to a new workplan step
	 *
	 * @param workPlanLink
	 * @param workPlanStepId
	 * @param checkRoles
	 * @return
	 */
	WorkPlanLink moveWorkLinkToStep(WorkPlanLink workPlanLink, String workPlanStepId, boolean checkRoles);

	/**
	 * Moves workPlanlink to previous step in it's workplan
	 *
	 * @param workPlanLink
	 */
	public void previousStep(WorkPlanLink workPlanLink);

	/**
	 * Moves workPlanlink to next step in it's workplan
	 *
	 * @param workPlanLink
	 */
	public void nextStep(WorkPlanLink workPlanLink);

	/**
	 * Loops through all the components and checks that there is a WorkPlan Link in the current
	 * active WorkPlan. If there is not, it will create one and place it in according to "Approval Status To Match"
	 * step marker.
	 */
	void syncWorkPlanLinks();

	/**
	 * Retrieves a workplan and make sure it all sorted.
	 *
	 * @param workplanId
	 * @return workplan or null if not found
	 */
	WorkPlan getWorkPlan(String workplanId);

	/**
	 * Finds the WorkPlan Step that has a "Approval State To Match" (i.e., "Pending", or "Approved")
	 * that matches the component's Approval Status, then moves the WorkPlan Link of said component
	 * into that step.
	 *
	 * Finds the last WorkPlan Step that matches, so if there is more than one step that is found
	 * matching, the last occurring one is the one the WorkPlanLink is moved into.
	 *
	 * @param componentId The UUID of the component to update
	 */
	void updatedWorkPlanLinkToMatchState(String componentId);

	/**
	 * Finds the work plan for component Type
	 *
	 * @param componentType
	 * @return WorkPlan (if not found, the default will be returned)
	 */
	WorkPlan getWorkPlanForComponentType(String componentType);

	/**
	 * Resolves step-to-step migrations in a work plan
	 *
	 * @param workPlanId
	 * @param migrations a list a migrations to be performed
	 */
	void resolveWorkPlanStepMigration(String workPlanId, List<WorkPlanStepMigration> migrations);

	/**
	 * Updates plans and remove the component type
	 *
	 * @param componentType
	 */
	void removeComponentTypeFromWorkPlans(String componentType);

	/**
	 * Updates plans and remove the component type
	 *
	 * @param securityRole
	 */
	void removeSecurityRole(String securityRole);

	/**
	 * Removes all work links for a component
	 *
	 * @param componentId
	 */
	void removeWorkPlanlinkForComponent(String componentId);

	/**
	 * Hard-delete of a work plan link; Keep in mind a new one will get create
	 * if the underlying data (eg. component) still exists
	 *
	 * @param workPlanLinkId
	 */
	void removeWorkPlanLink(String workPlanLinkId);

	/**
	 * Checks the current user to see if they have permission for the requested
	 * step.
	 *
	 * @param workPlan
	 * @param workPlanStepId
	 * @return true if the user has permission for the step
	 */
	boolean checkRolesOnStep(WorkPlan workPlan, String workPlanStepId);

	/**
	 * This will create a link (if it doesn't exist) otherwise it will return
	 * existing
	 *
	 * @param userSubmissionId
	 * @return the link
	 */
	WorkPlanLink getWorkPlanLinkForSubmission(String userSubmissionId);

	/**
	 * Removes all links for a submission
	 *
	 * @param userSubmissionId
	 */
	void removeWorkPlanLinkForSubmission(String userSubmissionId);

}
