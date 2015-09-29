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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.view.RestErrorModel;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.apache.commons.lang.StringUtils;

/**
 * Component Submission
 *
 * @author dshurtleff
 */
@Path("v1/resource/componentsubmissions")
@APIDescription("Handles component submissions which relate to component resource")
public class ComponentSubmissionResource
		extends BaseResource
{

	//	The check for ApprovalStatus.APPROVED.equals(component.getApprovalState()) == false has been overriden
	//  This is so that a user may edit an approved component. If the desire for behavior changes, the code is still
	//  there, you just need to remove the '|| true'
	@GET
	@APIDescription("Get a list of components submission for the current user only. Requires login.<br>(Note: this only the top level component object)")
	@DataType(Component.class)
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSubmissionsForUser()
	{
		if (SecurityUtil.isLoggedIn()) {
			Component componentExample = new Component();
			componentExample.setCreateUser(SecurityUtil.getCurrentUserName());
			componentExample.setActiveStatus(Component.ACTIVE_STATUS);

			List<Component> components = service.getPersistenceService().queryByExample(Component.class, componentExample);
			GenericEntity<List<Component>> entity = new GenericEntity<List<Component>>(components)
			{
			};
			return sendSingleEntityResponse(entity);
		} else {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	}

	@POST
	@APIDescription("Creates a new Component Submission. Note: reviews are not saved.")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(ComponentAll.class)
	public Response createNewSubmission(ComponentAll componentAll)
	{
		String id = service.getPersistenceService().generateId();
		componentAll.getComponent().setComponentId(id);
		for (ComponentAttribute attribute : componentAll.getAttributes()) {
			attribute.getComponentAttributePk().setComponentId(id);
		}
		return handleSaveComponent(componentAll, ApprovalStatus.NOT_SUBMITTED, false);
	}

	@PUT
	@APIDescription("Updates Component Submission. Note: reviews are not saved.")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(ComponentAll.class)
	@Path("/{componentId}")
	public Response updateSubmission(
			@PathParam("componentId")
			@RequiredParam String componentId,
			ComponentAll componentAll)
	{
		if (componentAll.getComponent() != null) {
			componentAll.getComponent().setComponentId(componentId);
			for (ComponentAttribute attribute : componentAll.getAttributes()) {
				attribute.getComponentAttributePk().setComponentId(componentAll.getComponent().getComponentId());
			}
			return handleSaveComponent(componentAll, ApprovalStatus.NOT_SUBMITTED, true);
		}
		RestErrorModel restErrorModel = new RestErrorModel();
		restErrorModel.setSuccess(false);
		restErrorModel.getErrors().put("component", "Component needs to be set.");
		return sendSingleEntityResponse(restErrorModel);
	}

	@PUT
	@APIDescription("Updates Component Submission. Note: reviews are not saved.")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(ComponentAll.class)
	@Path("/{componentId}/setNotifyMe")
	public Response setNotifyMe(
			@PathParam("componentId")
			@RequiredParam String componentId,
			String email)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		Component component = service.getPersistenceService().findById(Component.class, componentId);
		if (component != null) {
			response = ownerAnonymousCheck(component);
			if (response == null) {
				response = Response.ok().build();
				if (email.isEmpty()) {
					email = null;
				}
				component.setNotifyOfApprovalEmail(email);
				service.getPersistenceService().persist(component);
			} else {
				response = Response.status(Response.Status.FORBIDDEN)
						.type(MediaType.TEXT_PLAIN)
						.entity("Unable to modify an approved submission.")
						.build();
			}
		}
		return response;

	}

	@PUT
	@APIDescription("Submits Component Submission for approval.")
	@Path("/{componentId}/submit")
	public Response submitComponent(
			@PathParam("componentId")
			@RequiredParam String componentId
	)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		Component component = service.getPersistenceService().findById(Component.class, componentId);
		if (component != null) {
			response = ownerAnonymousCheck(component);
			if (response == null) {
				if (ApprovalStatus.APPROVED.equals(component.getApprovalState()) == false || true) {
					service.getComponentService().submitComponentSubmission(componentId);
					response = Response.ok().build();
				} else {
					response = Response.status(Response.Status.FORBIDDEN)
							.type(MediaType.TEXT_PLAIN)
							.entity("Unable to modify an approved submission.")
							.build();
				}
			}
		}
		return response;
	}

	@PUT
	@APIDescription("Unsubmits Component Submission for approval.")
	@Path("/{componentId}/unsubmit")
	public Response unsubmitComponent(
			@PathParam("componentId")
			@RequiredParam String componentId
	)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		Component component = service.getPersistenceService().findById(Component.class, componentId);
		if (component != null) {
			response = ownerAnonymousCheck(component);
			if (response == null) {
				if (ApprovalStatus.APPROVED.equals(component.getApprovalState()) == false || true) {
					service.getComponentService().checkComponentCancelStatus(componentId, ApprovalStatus.NOT_SUBMITTED);
					response = Response.ok().build();
				} else {
					response = Response.status(Response.Status.FORBIDDEN)
							.type(MediaType.TEXT_PLAIN)
							.entity("Unable to modify an approved submission.")
							.build();
				}
			}
		}
		return response;
	}

	@PUT
	@APIDescription("Inactivates a incomplete Component Submission .")
	@Path("/{componentId}/inactivate")
	public Response inactivateComponent(
			@PathParam("componentId")
			@RequiredParam String componentId
	)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		Component component = service.getPersistenceService().findById(Component.class, componentId);
		if (component != null) {
			response = ownerAnonymousCheck(component);
			if (response == null) {
				if (ApprovalStatus.NOT_SUBMITTED.equals(component.getApprovalState())) {
					service.getComponentService().deactivateComponent(componentId);
					response = Response.ok().build();
				} else {
					response = Response.status(Response.Status.FORBIDDEN)
							.type(MediaType.TEXT_PLAIN)
							.entity("Can only modify a Not Submitted submission.")
							.build();
				}
			}
		}
		return response;
	}

	private Response handleSaveComponent(ComponentAll componentAll, String approveStatus, boolean update)
	{
		FileHistoryOption fileHistoryOption = new FileHistoryOption();
		fileHistoryOption.setUploadQuestions(true);
		fileHistoryOption.setUploadTags(true);

		//validate all pieces
		if (componentAll.getComponent() != null) {
			componentAll.getComponent().setApprovalState(approveStatus);
		}
		ValidationResult validationResult = componentAll.validate();
		if (validationResult.valid()) {
			if (update) {
				Response response = Response.status(Response.Status.NOT_FOUND).build();

				Component exstingComponent = service.getPersistenceService().findById(Component.class, componentAll.getComponent().getComponentId());
				if (exstingComponent != null) {
					response = ownerAnonymousCheck(exstingComponent);
					if (response == null) {
						if (ApprovalStatus.APPROVED.equals(exstingComponent.getApprovalState()) == false || true) {

							//Pull in existing media and resources (they may be saved seperately)
							ComponentMedia componentMediaExample = new ComponentMedia();
							componentMediaExample.setActiveStatus(ComponentMedia.ACTIVE_STATUS);
							componentMediaExample.setComponentId(exstingComponent.getComponentId());
							List<ComponentMedia> componentMedia = service.getPersistenceService().queryByExample(ComponentMedia.class, componentMediaExample);
							componentAll.getMedia().addAll(componentMedia);

							//clean out duplicate media
							Map<String, ComponentMedia> mediaMap = new HashMap<>();
							for (ComponentMedia media : componentAll.getMedia()) {
								String id = media.getComponentMediaId();
								if (StringUtils.isBlank(id)) {
									id = media.getLink();
								}
								if (mediaMap.containsKey(id) == false) {
									mediaMap.put(id, media);
								}
							}
							componentAll.getMedia().clear();
							componentAll.getMedia().addAll(mediaMap.values());

							ComponentResource componentResourceExample = new ComponentResource();
							componentResourceExample.setActiveStatus(ComponentResource.ACTIVE_STATUS);
							componentResourceExample.setComponentId(exstingComponent.getComponentId());
							List<ComponentResource> componentResources = service.getPersistenceService().queryByExample(ComponentResource.class, componentResourceExample);
							componentAll.getResources().addAll(componentResources);

							//clean out duplicate resouces
							Map<String, ComponentResource> resourceMap = new HashMap<>();
							for (ComponentResource resource : componentAll.getResources()) {
								String id = resource.getResourceId();
								if (StringUtils.isBlank(id)) {
									id = resource.getResourceType() + "-" + resource.getLink();
								}
								if (mediaMap.containsKey(id) == false) {
									resourceMap.put(id, resource);
								}
							}
							componentAll.getResources().clear();
							componentAll.getResources().addAll(resourceMap.values());

							service.getComponentService().checkComponentCancelStatus(componentAll.getComponent().getComponentId(), approveStatus);

							componentAll.populateCreateUpdateFields(false);
							componentAll.getComponent().setSubmittedDts(exstingComponent.getSubmittedDts());
							componentAll = service.getComponentService().saveFullComponent(componentAll, fileHistoryOption);

							response = Response.status(Response.Status.OK).entity(componentAll).build();
						} else {
							response = Response.status(Response.Status.FORBIDDEN)
									.type(MediaType.TEXT_PLAIN)
									.entity("Unable to modify an approved submission.")
									.build();
						}
					}
				}
				return response;
			} else {
				componentAll.populateCreateUpdateFields(false);
				componentAll = service.getComponentService().saveFullComponent(componentAll, fileHistoryOption);
				return Response.created(URI.create("v1/resource/componentsubmissions/" + componentAll.getComponent().getComponentId())).entity(componentAll).build();
			}
		} else {
			return sendSingleEntityResponse(validationResult.toRestError());
		}
	}

	@GET
	@APIDescription("Get a component submission. Must be the owner of the submission or submission must be anonymous")
	@DataType(ComponentAll.class)
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{componentId}")
	public Response getSubmission(@PathParam("componentId") String componentId)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentAll componentAll = service.getComponentService().getFullComponent(componentId);
		if (componentAll != null && componentAll.getComponent() != null) {
			response = ownerAnonymousCheck(componentAll.getComponent());
			if (response == null) {
				response = Response.ok(componentAll).build();
			}
		}
		return response;
	}

	@DELETE
	@APIDescription("Removes media from the specified component")
	@Path("/{id}/media/{mediaId}/force")
	public Response deleteComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("mediaId")
			@RequiredParam String mediaId)
	{
		Response response = Response.status(Response.Status.OK).build();
		ComponentMedia componentMediaExample = new ComponentMedia();
		componentMediaExample.setComponentMediaId(mediaId);
		componentMediaExample.setComponentId(componentId);

		ComponentMedia componentMedia = service.getPersistenceService().queryOneByExample(ComponentMedia.class, componentMediaExample);
		if (componentMedia != null) {
			response = ownerAnonymousCheck(componentMedia);
			if (response == null) {

				//Need to check component to make sure it's not approved.
				Component component = service.getPersistenceService().findById(Component.class, componentId);
				if (ApprovalStatus.APPROVED.equals(component.getApprovalState()) == false || true) {
					service.getComponentService().deleteBaseComponent(ComponentMedia.class, mediaId);
				} else {
					return Response.status(Response.Status.FORBIDDEN)
							.type(MediaType.TEXT_PLAIN)
							.entity("User cannot modify entity. Component is in a Approved state.")
							.build();
				}
			}
		}
		return response;
	}

	@DELETE
	@APIDescription("Remove a given resource from the specified component")
	@Path("/{id}/resources/{resourceId}/force")
	public Response deleteComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("resourceId")
			@RequiredParam String resourceId)
	{
		Response response = Response.status(Response.Status.OK).build();

		ComponentResource componentResourceExample = new ComponentResource();
		componentResourceExample.setComponentId(componentId);
		componentResourceExample.setResourceId(resourceId);
		ComponentResource componentResource = service.getPersistenceService().queryOneByExample(ComponentResource.class, componentResourceExample);
		if (componentResource != null) {
			response = ownerAnonymousCheck(componentResource);
			if (response == null) {
				Component component = service.getPersistenceService().findById(Component.class, componentId);
				if (ApprovalStatus.APPROVED.equals(component.getApprovalState()) == false || true) {
					service.getComponentService().deleteBaseComponent(ComponentResource.class, resourceId);
				} else {
					return Response.status(Response.Status.FORBIDDEN)
							.type(MediaType.TEXT_PLAIN)
							.entity("User cannot modify entity. Component is in a Approved state.")
							.build();
				}

			}
		}
		return response;
	}

	private Response ownerAnonymousCheck(StandardEntity entity)
	{
		if (SecurityUtil.isCurrentUserTheOwner(entity)
				|| OpenStorefrontConstant.ANONYMOUS_USER.equals(entity.getCreateUser())) {
			return null;
		} else {
			return Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN)
					.entity("User cannot modify resource.")
					.build();
		}
	}

}
