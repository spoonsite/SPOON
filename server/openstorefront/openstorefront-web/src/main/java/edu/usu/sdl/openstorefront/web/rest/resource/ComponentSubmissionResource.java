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

import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.service.transfermodel.AlertContext;
import edu.usu.sdl.openstorefront.service.transfermodel.ComponentAll;
import edu.usu.sdl.openstorefront.service.transfermodel.ComponentUploadOption;
import edu.usu.sdl.openstorefront.storage.model.AlertType;
import edu.usu.sdl.openstorefront.storage.model.ApprovalStatus;
import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentMedia;
import edu.usu.sdl.openstorefront.storage.model.ComponentResource;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.web.viewmodel.RestErrorModel;
import java.net.URI;
import java.util.List;
import javax.ws.rs.Consumes;
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
 * Component Submission
 *
 * @author dshurtleff
 */
@Path("v1/resource/componentsubmissions")
@APIDescription("Handles component submissions which relate to component resource")
public class ComponentSubmissionResource
		extends BaseResource
{

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
			return handleSaveComponent(componentAll, ApprovalStatus.NOT_SUBMITTED, true);
		}
		RestErrorModel restErrorModel = new RestErrorModel();
		restErrorModel.setSuccess(false);
		restErrorModel.getErrors().put("component", "Component needs to be set.");
		return sendSingleEntityResponse(restErrorModel);
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
			response = ownerCheck(component);
			if (response == null) {
				if (ApprovalStatus.APPROVED.equals(component.getApprovalState()) == false) {
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

	private Response handleSaveComponent(ComponentAll componentAll, String approveStatus, boolean update)
	{
		ComponentUploadOption componentUploadOption = new ComponentUploadOption();
		componentUploadOption.setUploadQuestions(true);
		componentUploadOption.setUploadTags(true);

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
					response = ownerCheck(exstingComponent);
					if (response == null) {
						if (ApprovalStatus.APPROVED.equals(exstingComponent.getApprovalState()) == false) {

							//Pull in existing media and resources (they may be saved seperately)
							ComponentMedia componentMediaExample = new ComponentMedia();
							componentMediaExample.setActiveStatus(ComponentMedia.ACTIVE_STATUS);
							componentMediaExample.setComponentId(exstingComponent.getComponentId());
							List<ComponentMedia> componentMedia = service.getPersistenceService().queryByExample(ComponentMedia.class, componentMediaExample);
							componentAll.getMedia().addAll(componentMedia);

							ComponentResource componentResourceExample = new ComponentResource();
							componentResourceExample.setActiveStatus(ComponentResource.ACTIVE_STATUS);
							componentResourceExample.setComponentId(exstingComponent.getComponentId());
							List<ComponentResource> componentResources = service.getPersistenceService().queryByExample(ComponentResource.class, componentResourceExample);
							componentAll.getResources().addAll(componentResources);

							componentAll.populateCreateUpdateFields(true);
							componentAll.getComponent().setSubmittedDts(exstingComponent.getSubmittedDts());
							componentAll = service.getComponentService().saveFullComponent(componentAll, componentUploadOption);

							if (ApprovalStatus.PENDING.equals(exstingComponent.getApprovalState())
									&& ApprovalStatus.NOT_SUBMITTED.equals(approveStatus)) {
								//cancel submission
								AlertContext alertContext = new AlertContext();
								alertContext.setAlertType(AlertType.COMPONENT_SUBMISSION);
								alertContext.setDataTrigger(componentAll);
								service.getAlertService().checkAlert(alertContext);
							}

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
				componentAll = service.getComponentService().saveFullComponent(componentAll, componentUploadOption);
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
			response = ownerCheck(componentAll.getComponent());
			if (response == null) {
				response = Response.ok(componentAll).build();
			}
		}
		return response;
	}

	private Response ownerCheck(BaseEntity entity)
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
