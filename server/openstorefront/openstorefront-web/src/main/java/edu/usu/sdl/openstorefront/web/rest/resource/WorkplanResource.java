/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.web.rest.resource;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.SecurityRole;
import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;
import edu.usu.sdl.openstorefront.core.model.WorkPlanModel;
import edu.usu.sdl.openstorefront.core.model.WorkPlanRemoveMigration;
import edu.usu.sdl.openstorefront.core.view.WorkPlanLinkView;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author cyearsley
 */
@Path("v1/resource/workplans")
@APIDescription("A workplan represents the state of said record/entity")
public class WorkplanResource
		extends BaseResource
{

	@GET
	@APIDescription("Gets a list of all Workplans.")
	@RequireSecurity(SecurityPermission.ADMIN_WORKPLAN_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(WorkPlan.class)
	public Response workplanLookupAll()
	{
		WorkPlan workPlanExample = new WorkPlan();
		List<WorkPlan> workPlans = workPlanExample.findByExample();

		GenericEntity<List<WorkPlan>> entity = new GenericEntity<List<WorkPlan>>(workPlans)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets a list of all Worklinks.")
	@RequireSecurity(SecurityPermission.WORKFLOW_LINK_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(WorkPlanLinkView.class)
	@Path("/worklinks")
	public Response workLinkLookupAll(
			@QueryParam("showfinal") boolean showfinal,
			@APIDescription("Shows links in steps the user may not have access to move steps. (For view only)")
			@QueryParam("showallsteps") boolean showAllSteps
	)
	{
		//depends on the user
		WorkPlanLink workLinkExample = new WorkPlanLink();
		workLinkExample.setActiveStatus(WorkPlanLink.ACTIVE_STATUS);
		List<WorkPlanLink> workLinks = workLinkExample.findByExample();

		Map<String, WorkPlan> workPlanMap = new HashMap<>();
		for (WorkPlanLink link : workLinks) {
			if (!workPlanMap.containsKey(link.getWorkPlanId())) {
				WorkPlan workPlan = service.getWorkPlanService().getWorkPlan(link.getWorkPlanId());
				workPlanMap.put(link.getWorkPlanId(), workPlan);
			}
		}

		if (!showfinal) {
			workLinks.removeIf(link -> {
				return workPlanMap.get(link.getWorkPlanId()).lastStep(link.getCurrentStepId());
			});
		}

		if (!SecurityUtil.hasPermission(SecurityPermission.WORKFLOW_LINK_READ_ALL)) {
			//get worklinks assigned to them and assigned to there groups (remove other names)
			workLinks = workLinks.stream()
					.filter(link -> {
						return SecurityUtil.getCurrentUserName().equals(link.getCurrentUserAssigned())
								|| linkPartOfUserGroup(link);
					}).collect(Collectors.toList());

			//Remove workflow that in step that current user doesn't have access
			if (!showAllSteps) {
				workLinks.removeIf(link -> {
					return !doesUserHaveAccessToCurrentStep(link, workPlanMap);
				});
			}

			//filter names
			workLinks.forEach(link -> {
				if (StringUtils.isNotBlank(link.getCurrentUserAssigned())
						&& !SecurityUtil.getCurrentUserName().equals(link.getCurrentUserAssigned())) {
					link.setCurrentUserAssigned(OpenStorefrontConstant.GENERIC_ADMIN_USER);
				}
			});
		}

		GenericEntity<List<WorkPlanLinkView>> entity = new GenericEntity<List<WorkPlanLinkView>>(WorkPlanLinkView.toView(workLinks))
		{
		};
		return sendSingleEntityResponse(entity);
	}

	private boolean doesUserHaveAccessToCurrentStep(WorkPlanLink workPlanLink, Map<String, WorkPlan> workPlanMap)
	{
		WorkPlan workPlan = workPlanMap.get(workPlanLink.getWorkPlanId());
		return service.getWorkPlanService().checkRolesOnStep(workPlan, workPlanLink.getCurrentStepId());
	}

	private boolean linkPartOfUserGroup(WorkPlanLink workPlanLink)
	{
		boolean partOfGroup = false;

		Set<String> userRoles = SecurityUtil.getUserContext().roles();
		if (StringUtils.isNotBlank(workPlanLink.getCurrentGroupAssigned())) {
			if (userRoles.contains(workPlanLink.getCurrentGroupAssigned())) {
				partOfGroup = true;
			}
		}

		return partOfGroup;
	}

	@GET
	@APIDescription("Gets a Worklink")
	@RequireSecurity(SecurityPermission.WORKFLOW_LINK_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(WorkPlanLinkView.class)
	@Path("/worklinks/{linkId}")
	public Response getWorkLink(
			@PathParam("linkId") String workLinkId
	)
	{
		WorkPlanLinkView view = null;
		WorkPlanLink workPlanLink = new WorkPlanLink();
		workPlanLink.setWorkPlanLinkId(workLinkId);
		workPlanLink = workPlanLink.find();
		if (workPlanLink != null) {
			view = WorkPlanLinkView.toView(workPlanLink);
		}
		return sendSingleEntityResponse(view);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_WORKPLAN_READ)
	@APIDescription("Gets a single Work Plan")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(WorkPlan.class)
	@Path("/{id}")
	public Response workPlanSingleLookup(@PathParam("id") String workPlanId)
	{
		WorkPlan workPlan = new WorkPlan();
		workPlan.setWorkPlanId(workPlanId);
		workPlan = workPlan.find();
		return sendSingleEntityResponse(workPlan);
	}

	@POST
	@APIDescription("Creates a Work Plan")
	@RequireSecurity(SecurityPermission.ADMIN_WORKPLAN_CREATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(WorkPlan.class)
	public Response createWorkPlan(WorkPlan workPlan)
	{
		ValidationResult validationResult = workPlan.validate();
		if (validationResult.valid()) {
			workPlan.populateBaseCreateFields();
			WorkPlan createdWorkPlan = service.getWorkPlanService().saveWorkPlan(workPlan);
			return sendSingleEntityResponse(createdWorkPlan);
		} else {
			return sendSingleEntityResponse(validationResult.toRestError());
		}
	}

	@PUT
	@APIDescription("Updates a Work Plan")
	@RequireSecurity(SecurityPermission.ADMIN_WORKPLAN_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(WorkPlan.class)
	public Response updateWorkPlan(
			WorkPlanModel workPlanModel
	)
	{
		ValidationResult validationResult = workPlanModel.getWorkPlan().validate();
		if (validationResult.valid()) {
			WorkPlan updatedWorkPlan = service.getWorkPlanService().saveWorkPlan(workPlanModel);
			return sendSingleEntityResponse(updatedWorkPlan);
		} else {
			return sendSingleEntityResponse(validationResult.toRestError());
		}
	}

	@PUT
	@APIDescription("Activates a Work Plan")
	@RequireSecurity(SecurityPermission.ADMIN_WORKPLAN_UPDATE)
	@Path("/{id}/activate")
	public Response activateWorkPlan(
			@PathParam("id") String workPlanId
	)
	{
		WorkPlan workPlan = new WorkPlan();
		workPlan.setWorkPlanId(workPlanId);
		workPlan = workPlan.find();
		if (workPlan != null) {
			service.getWorkPlanService().activateWorkPlan(workPlanId);
			return Response.ok().build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@PUT
	@APIDescription("Assigns a workPlanLink to a user and/or group")
	@RequireSecurity(SecurityPermission.WORKFLOW_LINK_ASSIGN_ANY)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(WorkPlanLinkView.class)
	@Path("/{workPlanId}/worklinks/{workLinkId}/assign")
	public Response assignWorkplanForComponent(
			@PathParam("workPlanId") String workPlanId,
			@PathParam("workLinkId") String workLinkId,
			@QueryParam("username") String username,
			@QueryParam("roleGroup") String roleGroup
	)
	{
		return handleAssign(workPlanId, workLinkId, username, roleGroup);
	}

	@PUT
	@APIDescription("Assigns a workPlanLink to current user")
	@RequireSecurity(SecurityPermission.WORKFLOW_LINK_ASSIGN)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(WorkPlanLinkView.class)
	@Path("/{workPlanId}/worklinks/{workLinkId}/assigntome")
	public Response assignWorkplanLinkToMe(
			@PathParam("workPlanId") String workPlanId,
			@PathParam("workLinkId") String workLinkId
	)
	{
		return handleAssign(workPlanId, workLinkId, SecurityUtil.getCurrentUserName(), null);
	}

	@PUT
	@APIDescription("Assigns a workPlanLink to current user")
	@RequireSecurity(SecurityPermission.WORKFLOW_LINK_ASSIGN)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(WorkPlanLinkView.class)
	@Path("/{workPlanId}/worklinks/{workLinkId}/unassign")
	public Response unassignWorkplanLink(
			@PathParam("workPlanId") String workPlanId,
			@PathParam("workLinkId") String workLinkId
	)
	{
		return handleAssign(workPlanId, workLinkId, null, null);
	}

	@PUT
	@APIDescription("Assigns a workPlanLink to the admin group")
	@RequireSecurity(SecurityPermission.WORKFLOW_LINK_ASSIGN)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(WorkPlanLinkView.class)
	@Path("/{workPlanId}/worklinks/{workLinkId}/assignToAdmin")
	public Response assignWorkplanLinkToAdmin(
			@PathParam("workPlanId") String workPlanId,
			@PathParam("workLinkId") String workLinkId
	)
	{
		WorkPlan workPlan = service.getWorkPlanService().getWorkPlan(workPlanId);
		if (workPlan != null) {
			String adminGroup = StringUtils.isNotBlank(workPlan.getAdminRole()) ? workPlan.getAdminRole() : SecurityRole.ADMIN_ROLE;
			return handleAssign(workPlanId, workLinkId, null, adminGroup);
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	private Response handleAssign(String workPlanId, String workLinkId, String username, String roleGroup)
	{
		WorkPlanLinkView view = null;

		WorkPlanLink workPlanLinkExample = new WorkPlanLink();
		workPlanLinkExample.setWorkPlanId(workPlanId);
		workPlanLinkExample.setWorkPlanLinkId(workLinkId);
		WorkPlanLink workPlanLink = workPlanLinkExample.find();

		if (workPlanLink != null) {
			service.getWorkPlanService().assignWorkPlan(workPlanId, workLinkId, username, roleGroup);
			workPlanLink = workPlanLinkExample.find();
			view = WorkPlanLinkView.toView(workPlanLink);
		}

		return sendSingleEntityResponse(view);
	}

	@PUT
	@APIDescription("Moves component to specified step")
	@RequireSecurity(SecurityPermission.WORKFLOW_LINK_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(WorkPlanLinkView.class)
	@Path("/{workPlanId}/worklinks/{workLinkId}/tostep/{workPlanStepId}")
	public Response moveComponentToStep(
			@PathParam("workPlanId") String workPlanId,
			@PathParam("workLinkId") String workLinkId,
			@PathParam("workPlanStepId") String workPlanStepId
	)
	{
		WorkPlanLinkView view = null;

		WorkPlanLink workPlanLink = new WorkPlanLink();
		workPlanLink.setWorkPlanId(workPlanId);
		workPlanLink.setWorkPlanLinkId(workLinkId);
		workPlanLink = workPlanLink.find();

		if (workPlanLink != null) {
			workPlanLink = service.getWorkPlanService().moveWorkLinkToStep(workPlanLink, workPlanStepId, true);
			view = WorkPlanLinkView.toView(workPlanLink);
		}

		return sendSingleEntityResponse(view);
	}

	@DELETE
	@APIDescription("Deletes a Work Plan and moves it's records to a target Work Plan (if specified)")
	@RequireSecurity(SecurityPermission.ADMIN_WORKPLAN_DELETE)
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(WorkPlanRemoveMigration.class)
	@Path("/{workPlanId}")
	public void deleteWorkPlan(
			@PathParam("workPlanId") String removeWorkPlanId,
			@APIDescription("Optional; if not set existing worklinks will use default workplan") WorkPlanRemoveMigration workPlanRemoveMigration
	)
	{
		service.getWorkPlanService().removeWorkPlan(removeWorkPlanId, workPlanRemoveMigration);
	}

	@DELETE
	@APIDescription("Gets a Worklink")
	@RequireSecurity(SecurityPermission.WORKFLOW_LINK_DELETE)
	@Path("/worklinks/{linkId}")
	public void deleteWorkLink(
			@PathParam("linkId") String workLinkId
	)
	{
		service.getWorkPlanService().removeWorkPlanLink(workLinkId);
	}

}
