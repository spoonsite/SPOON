/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.rest.resource;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;
import edu.usu.sdl.openstorefront.core.entity.NotificationEventReadStatus;
import edu.usu.sdl.openstorefront.core.entity.NotificationEventType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.NotificationEventView;
import edu.usu.sdl.openstorefront.core.view.NotificationEventWrapper;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.net.URI;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/notificationevent")
@APIDescription("Notification events are application generated events sent to users.")
public class NotificationEventResource
		extends BaseResource
{

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_MESSAGE_MANAGEMENT_READ)
	@APIDescription("Gets notification event records for all users.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(NotificationEventWrapper.class)
	@Path("/all")
	public Response getAllEvents(@BeanParam FilterQueryParams filterQueryParams)
	{
		return handleGetEvents(null, filterQueryParams);
	}

	@GET
	@APIDescription("Gets notification event records for the current user.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(NotificationEventWrapper.class)
	public Response getUserEvents(
			@BeanParam FilterQueryParams filterQueryParams)
	{
		return handleGetEvents(SecurityUtil.getCurrentUserName(), filterQueryParams);
	}

	private Response handleGetEvents(String username, FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}
		NotificationEventWrapper notificationEventWrapper = service.getNotificationService().getAllEventsForUser(username, filterQueryParams);
		return sendSingleEntityResponse(notificationEventWrapper);
	}

	@GET
	@APIDescription("Gets a notification event record")
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{eventId}")
	public Response getEvent(@PathParam("eventId") String eventId)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		NotificationEvent notificationEvent = new NotificationEvent();
		notificationEvent.setEventId(eventId);
		notificationEvent = notificationEvent.find();
		if (notificationEvent != null) {

			response = ownerCheck(notificationEvent, SecurityPermission.ADMIN_MESSAGE_MANAGEMENT_READ);
			if (response == null) {
				return sendSingleEntityResponse(notificationEvent);
			}
		}
		return response;
	}

	@PUT
	@APIDescription("Marks event as read")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{eventId}")
	@DataType(NotificationEventView.class)
	public Response putNewEvent(
			@PathParam("eventId") String eventId,
			NotificationEventReadStatus readStatus
	)
	{
		NotificationEventView view = null;

		NotificationEvent notificationEvent = new NotificationEvent();
		notificationEvent.setEventId(eventId);
		notificationEvent = notificationEvent.find();
		if (notificationEvent != null) {
			service.getNotificationService().markEventAsRead(eventId, readStatus.getUsername());

			view = NotificationEventView.toView(notificationEvent);
			view.setReadMessage(true);
		}
		return sendSingleEntityResponse(view);
	}

	@DELETE
	@APIDescription("Marks event as unread")
	@Path("/{eventId}/{username}")
	public void deleteNewEvent(
			@PathParam("eventId") String eventId,
			@PathParam("username") String username
	)
	{
		service.getNotificationService().markEventAsUnRead(eventId, username);
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_NOTIFICATION_EVENT_CREATE)
	@APIDescription("Posts a new notification event")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(NotificationEvent.class)
	public Response postNewEvent(
			NotificationEvent notificationEvent
	)
	{
		notificationEvent.setEventType(NotificationEventType.ADMIN);

		ValidationModel validationModel = new ValidationModel(notificationEvent);
		validationModel.setConsumeFieldsOnly(true);

		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			notificationEvent = service.getNotificationService().postEvent(notificationEvent);

			return Response.created(URI.create("v1/resource/notificationevent/" + notificationEvent.getEventId())).entity(notificationEvent).build();
		}
		return sendSingleEntityResponse(validationResult.toRestError());
	}

	@DELETE
	@APIDescription("Deletes a notification event")
	@Path("/{eventId}")
	public void deleteNotificationEvent(
			@PathParam("eventId") String eventId)
	{
		NotificationEvent notificationEvent = new NotificationEvent();
		notificationEvent.setEventId(eventId);
		notificationEvent = notificationEvent.find();
		if (notificationEvent != null) {
			//only allow deleting of user notifications unless admin
			if (SecurityUtil.isEntryAdminUser()
					|| (notificationEvent.getUsername() != null
					&& SecurityUtil.getCurrentUserName().equals(notificationEvent.getUsername()))) {
				service.getNotificationService().deleteEvent(eventId);
			}
		}
	}

	@DELETE
	@APIDescription("Deletes all notification events for the current user only")
	@Path("/currentuser")
	public void clearUserNotificationEvents()
	{
		service.getNotificationService().deleteEventsForUser(SecurityUtil.getCurrentUserName());
	}

}
