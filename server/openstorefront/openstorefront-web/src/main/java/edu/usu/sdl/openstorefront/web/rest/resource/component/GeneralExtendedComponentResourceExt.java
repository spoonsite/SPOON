/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.rest.resource.component;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.QueryType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.entity.ComponentVersionHistory;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.model.ComponentRestoreOptions;
import edu.usu.sdl.openstorefront.core.view.ComponentDetailView;
import edu.usu.sdl.openstorefront.core.view.ComponentTrackingWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.net.URI;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
public abstract class GeneralExtendedComponentResourceExt
		extends ComponentQuestionReviewExt
{

	// <editor-fold defaultstate="collapsed" desc="Version history">
	@GET
	@APIDescription("Gets all version history for a component")
	@Produces(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_READ)
	@DataType(ComponentVersionHistory.class)
	@Path("/{id}/versionhistory")
	public Response getComponentVersionHistory(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		ComponentVersionHistory versionHistory = new ComponentVersionHistory();
		versionHistory.setActiveStatus(ComponentVersionHistory.ACTIVE_STATUS);
		versionHistory.setComponentId(componentId);

		List<ComponentVersionHistory> versionHistories = service.getPersistenceService().queryByExample(versionHistory);

		GenericEntity<List<ComponentVersionHistory>> entity = new GenericEntity<List<ComponentVersionHistory>>(versionHistories)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets a version history record")
	@Produces(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_VERSION_READ)
	@DataType(ComponentVersionHistory.class)
	@Path("/{id}/versionhistory/{versionHistoryId}")
	public Response getComponentVersionHistoryRecord(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("versionHistoryId")
			@RequiredParam String versionHistoryId)
	{
		ComponentVersionHistory componentVersionHistory = new ComponentVersionHistory();
		componentVersionHistory.setVersionHistoryId(versionHistoryId);
		componentVersionHistory.setComponentId(componentId);
		componentVersionHistory = componentVersionHistory.find();
		return sendSingleEntityResponse(componentVersionHistory);
	}

	@GET
	@APIDescription("Gets the detail of a component version")
	@Produces(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_VERSION_READ)
	@DataType(ComponentDetailView.class)
	@Path("/{id}/versionhistory/{versionHistoryId}/view")
	public Response viewComponentVersion(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("versionHistoryId")
			@RequiredParam String versionHistoryId)
	{
		ComponentDetailView componentDetailView = service.getComponentService().viewSnapshot(versionHistoryId);
		return sendSingleEntityResponse(componentDetailView);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_VERSION_CREATE)
	@APIDescription("Create a version of the current component")
	@DataType(ComponentVersionHistory.class)
	@Path("/{id}/versionhistory")
	public Response snapshotComponent(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		Component component = new Component();
		component.setComponentId(componentId);
		component = component.find();
		if (component != null) {
			ComponentVersionHistory versionHistory = service.getComponentService().snapshotVersion(componentId, null);
			response = Response.created(URI.create(BASE_RESOURCE_PATH + component.getComponentId())).entity(versionHistory).build();
		}
		return response;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_VERSION_RESTORE)
	@APIDescription("Restores a version of the current component")
	@DataType(ComponentVersionHistory.class)
	@Path("/{id}/versionhistory/{versionHistoryId}/restore")
	public Response restoreSnapshot(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("versionHistoryId")
			@RequiredParam String versionHistoryId,
			ComponentRestoreOptions options)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		ComponentVersionHistory componentVersionHistory = new ComponentVersionHistory();
		componentVersionHistory.setVersionHistoryId(versionHistoryId);
		componentVersionHistory.setComponentId(componentId);
		componentVersionHistory = componentVersionHistory.find();
		if (componentVersionHistory != null) {
			if (options == null) {
				options = new ComponentRestoreOptions();
			}

			Component component = service.getComponentService().restoreSnapshot(versionHistoryId, options);
			response = sendSingleEntityResponse(component);
		}
		return response;
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_VERSION_DELETE)
	@APIDescription("Delete a version history record")
	@Path("/{id}/versionhistory/{versionHistoryId}")
	public void deleteVersionHistory(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("versionHistoryId")
			@RequiredParam String versionHistoryId)
	{
		//confirm that the version belong to the component
		ComponentVersionHistory componentVersionHistory = new ComponentVersionHistory();
		componentVersionHistory.setVersionHistoryId(versionHistoryId);
		componentVersionHistory.setComponentId(componentId);
		componentVersionHistory = componentVersionHistory.find();
		if (componentVersionHistory != null) {
			service.getComponentService().deleteSnapshot(versionHistoryId);
		}
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed"  desc="ComponentRESTResource TRACKING section">
	@GET
	@RequireSecurity(SecurityPermission.ADMIN_TRACKING_READ)
	@APIDescription("Get the list of tracking details on a specified component. Always sorts by create date.")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentTrackingWrapper.class)
	@Path("/{id}/tracking")
	public Response getActiveComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentTracking trackingExample = new ComponentTracking();
		trackingExample.setComponentId(componentId);
		trackingExample.setActiveStatus(filterQueryParams.getStatus());

		QueryByExample<ComponentTracking> queryByExample = new QueryByExample<>(trackingExample);
		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());

		ComponentTracking trackingOrderExample = new ComponentTracking();
		trackingOrderExample.setCreateDts(QueryByExample.DATE_FLAG);
		queryByExample.setOrderBy(trackingOrderExample);
		queryByExample.setSortDirection(OpenStorefrontConstant.SORT_DESCENDING);

		List<ComponentTracking> componentTrackings = service.getPersistenceService().queryByExample(queryByExample);

		long total = service.getPersistenceService().countByExample(new QueryByExample<>(QueryType.COUNT, trackingExample));
		return sendSingleEntityResponse(new ComponentTrackingWrapper(componentTrackings, total));
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_TRACKING_READ)
	@APIDescription("Get the list of tracking details on a specified component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentTracking.class)
	@Path("/{id}/tracking/{trackingId}")
	public Response getComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("id")
			@RequiredParam String trackingId)
	{
		ComponentTracking componentTrackingExample = new ComponentTracking();
		componentTrackingExample.setComponentId(componentId);
		componentTrackingExample.setComponentTrackingId(trackingId);
		ComponentTracking componentTracking = service.getPersistenceService().queryOneByExample(componentTrackingExample);
		return sendSingleEntityResponse(componentTracking);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_TRACKING_DELETE)
	@APIDescription("Remove a tracking entry from the specified component")
	@Path("/{id}/tracking/{trackingId}")
	public void deleteComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("id")
			@RequiredParam String trackingId)
	{
		ComponentTracking componentTrackingExample = new ComponentTracking();
		componentTrackingExample.setComponentId(componentId);
		componentTrackingExample.setComponentTrackingId(trackingId);
		ComponentTracking componentTracking = service.getPersistenceService().queryOneByExample(componentTrackingExample);
		if (componentTracking != null) {
			service.getComponentService().deactivateBaseComponent(ComponentTracking.class, trackingId);
		}
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_TRACKING_DELETE)
	@APIDescription("Remove all tracking entries from the specified component")
	@Path("/{id}/tracking")
	public void deleteAllComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		service.getComponentService().deleteAllBaseComponent(ComponentTracking.class, componentId);
	}
}
