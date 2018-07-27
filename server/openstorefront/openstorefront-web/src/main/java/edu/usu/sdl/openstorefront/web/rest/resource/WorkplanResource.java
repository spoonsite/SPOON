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

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import java.util.List;
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
	@RequireSecurity(SecurityPermission.ADMIN_WORKPLAN_READ)
	@Produces({ MediaType.APPLICATION_JSON })
	@DataType(WorkPlan.class)
	@Path("/worklinks")
	public Response workLinkLookupAll()
	{
		WorkPlanLink workLinkExample = new WorkPlanLink();
		List<WorkPlanLink> workLinks = workLinkExample.findByExample();

		GenericEntity<List<WorkPlanLink>> entity = new GenericEntity<List<WorkPlanLink>>(workLinks) {
		};
		return sendSingleEntityResponse(entity);
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
		if (workPlan == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		GenericEntity<WorkPlan> entity = new GenericEntity<WorkPlan>(workPlan)
			{
			};
		
		return sendSingleEntityResponse(entity);
	}
	
	@POST
	@APIDescription("Creates a Work Plan")
	@RequireSecurity(SecurityPermission.ADMIN_WORKPLAN_CREATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response createWorkPlan(WorkPlan workPlan)
	{
		workPlan.populateBaseCreateFields();
		WorkPlan createdWorkPlan = service.getWorkPlanService().saveWorkPlan(workPlan);
		
		GenericEntity<WorkPlan> entity = new GenericEntity<WorkPlan>(createdWorkPlan)
			{
			};
		
		return sendSingleEntityResponse(entity);
	}
	
	@PUT
	@APIDescription("Updates a Work Plan")
	@RequireSecurity(SecurityPermission.ADMIN_WORKPLAN_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response updateWorkPlan(
			WorkPlan workPlan
	)
	{
		WorkPlan updatedWorkPlan = service.getWorkPlanService().saveWorkPlan(workPlan);
		
		GenericEntity<WorkPlan> entity = new GenericEntity<WorkPlan>(updatedWorkPlan)
			{
			};
		
		return sendSingleEntityResponse(entity);
	}
	
	@PUT
	@APIDescription("Activates a Work Plan")
	@RequireSecurity(SecurityPermission.ADMIN_WORKPLAN_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/activate")
	public Response activateWorkPlan(
			@PathParam("id") String workPlanId
	)
	{
		service.getWorkPlanService().activateWorkPlan(workPlanId);
		return Response.status(Response.Status.OK).build();
	}
	
	@PUT
	@APIDescription("Assigns a workplan for a component")
	@RequireSecurity(SecurityPermission.ADMIN_WORKPLAN_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{workPlanId}/worklinks/{workLinkId}")
	public Response assignWorkplanForComponent(
			@PathParam("workPlanId") String workPlanId,
			@PathParam("workLinkId") String workLinkId,
			@QueryParam("username") String username,
			@QueryParam("roleGroup") String roleGroup
	)
	{
		service.getWorkPlanService().assignWorkPlanForComponent(workPlanId, workLinkId, username, roleGroup);
		return Response.status(Response.Status.OK).build();
	}
	
	@PUT
	@APIDescription("Moves component to specified step")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{workPlanId}/worklinks/{workLinkId}/tostep/{workPlanStepId}")
	public Response moveComponentToStep(
			@PathParam("workPlanId") String workPlanId,
			@PathParam("workLinkId") String workLinkId,
			@PathParam("workPlanStepId") String workPlanStepId
	)
	{
		// TODO: check to see if the current user can move the component to the specified step
		WorkPlanLink workPlanLink = service.getWorkPlanService().moveComponentToStep(workPlanId, workLinkId, workPlanStepId);
		
		GenericEntity<WorkPlanLink> entity = new GenericEntity<WorkPlanLink>(workPlanLink)
			{
			};
		
		return sendSingleEntityResponse(entity);
	}
	
	@DELETE
	@APIDescription("Deletes a Work Plan and moves it's records to a target Work Plan (if specified)")
	@RequireSecurity(SecurityPermission.ADMIN_WORKPLAN_DELETE)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{workPlanId}")
	public void deleteWorkPlan(
			@PathParam("workPlanId") String removeWorkPlanId,
			@QueryParam("targetWorkPlanId") String targetWorkPlanId
	)
	{
		service.getWorkPlanService().removeWorkPlan(removeWorkPlanId, targetWorkPlanId);
	}
}
