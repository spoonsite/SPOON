/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.doc.RequireAdmin;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttributePk;
import edu.usu.sdl.openstorefront.storage.model.ComponentContact;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSchedule;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSchedulePk;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSection;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSectionPk;
import edu.usu.sdl.openstorefront.storage.model.ComponentMedia;
import edu.usu.sdl.openstorefront.storage.model.ComponentMetadata;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestion;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.storage.model.ComponentResource;
import edu.usu.sdl.openstorefront.storage.model.ComponentReview;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewCon;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewConPk;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewPro;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewProPk;
import edu.usu.sdl.openstorefront.storage.model.ComponentTag;
import edu.usu.sdl.openstorefront.storage.model.ComponentTracking;
import edu.usu.sdl.openstorefront.util.ServiceUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentDetailView;
import edu.usu.sdl.openstorefront.web.rest.model.RequiredForComponent;
import java.net.URI;
import java.util.ArrayList;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * ComponentRESTResource Resource
 *
 * @author dshurtleff
 * @author jlaw
 */
@Path("v1/resource/components")
@APIDescription("Components are the central resource of the system.  The majority of the listing are components.")
public class ComponentRESTResource
		extends BaseResource
{

	// COMPONENT GENERAL FUNCTIONS
	@GET
	@APIDescription("Get a list of components <br>(Note: this only the top level component object, See Component Detail for composite resource.)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Component.class)
	public List<Component> getComponents()
	{
		List<Component> componentViews = service.getComponentService().getComponents();
		return componentViews;
	}

	@GET
	@APIDescription("Get a list of components by an id set.  If it can't find a component for a griven id it's not returned.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Component.class)
	@Path("/list")
	public List<Component> batchGetComponents(
			@QueryParam("idList")
			@RequiredParam List<String> idList
	)
	{
		List<Component> componentViews = new ArrayList<>();
		idList.forEach(componentId -> {
			Component view = service.getPersistenceService().findById(Component.class, componentId);
			if (view != null) {
				componentViews.add(view);
			}

			//TODO:  What to do if the id don't match any component....is that an error or just return the ones it can find.
		});

		return componentViews;
	}

	@GET
	@APIDescription("Gets a component <br>(Note: this only the top level component object)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Component.class)
	@Path("/{id}")
	public Component getComponentSingle(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		Component view = service.getPersistenceService().findById(Component.class, componentId);
		return view;
	}

	@POST
	@RequireAdmin
	@APIDescription("Create a component")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createComponent(
			@RequiredParam RequiredForComponent component)
	{
		ValidationModel validationModel = new ValidationModel(component);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {

			component.getComponent().setCreateUser(ServiceUtil.getCurrentUserName());
			component.getComponent().setUpdateUser(ServiceUtil.getCurrentUserName());

			service.getComponentService().saveComponent(component);
			return Response.created(URI.create("v1/resource/components/" + component.getComponent().getComponentId())).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@RequireAdmin
	@APIDescription("Update a component")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response updateComponent(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam RequiredForComponent component)
	{
		component.getComponent().setComponentId(componentId);
		component.getAttributes().forEach(attribute -> {
			attribute.getComponentAttributePk().setComponentId(componentId);
			attribute.setComponentId(componentId);
		});
		ValidationModel validationModel = new ValidationModel(component);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			return Response.ok().build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@GET
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentDetailView.class)
	@Path("/{id}/detail")
	public ComponentDetailView getComponentDetails(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		ComponentDetailView componentDetail = service.getComponentService().getComponentDetails(componentId);
		return componentDetail;
	}

	// ComponentRESTResource ATTRIBUTE Section
	@GET
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentAttribute.class)
	@Path("/{id}/attribute")
	public List<ComponentAttribute> getComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentAttribute.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove an attribute from the entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/attribute")
	public void deleteComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentAttributePk attributePk)
	{
		service.getComponentService().deactivateBaseComponent(ComponentAttribute.class, attributePk);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add an attribute to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentAttribute.class)
	@Path("/{id}/attribute")
	public Response addComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentAttribute attribute)
	{
		ValidationModel validationModel = new ValidationModel(attribute);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getComponentService().saveComponentAttribute(attribute);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		return Response.created(URI.create(attribute.getComponentAttributePk().getAttributeType() + attribute.getComponentAttributePk().getAttributeCode())).build();
	}

	// ComponentRESTResource CONTACT section
	@GET
	@APIDescription("Remove a contact from the entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentContact.class)
	@Path("/{id}/contact")
	public List<ComponentContact> getComponentContact(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentContact.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a contact from the entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/contact/{contactId}")
	public void deleteComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("contactId")
			@RequiredParam String contactId)
	{
		//TODO:  Validate that the contact belongs to the component that the are try to delete

		service.getComponentService().deactivateBaseComponent(ComponentContact.class, contactId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a contact to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentContact.class)
	@Path("/{id}/contact")
	public Response addComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentContact contact)
	{
		return saveContact(contact, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Update a contact associated to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/contact")
	public Response updateComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			ComponentContact contact)
	{
		return saveContact(contact, false);
	}

	private Response saveContact(ComponentContact contact, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(contact);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getComponentService().saveComponentContact(contact);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create(contact.getContactId())).build();
		} else {
			return Response.ok().build();
		}
	}

	// ComponentRESTResource Evaluation Section section
	@GET
	@APIDescription("Gets an evaluation section associated to the entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentEvaluationSection.class)
	@Path("/{id}/section")
	public List<ComponentEvaluationSection> getComponentEvaluationSection(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentEvaluationSection.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Removes an evaluation section from the entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/section")
	public void deleteComponentEvaluationSection(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentEvaluationSectionPk evaluationId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentEvaluationSection.class, (Object) evaluationId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add an evaluation section to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentEvaluationSection.class)
	@Path("/{id}/section")
	public Response addComponentEvaluationSection(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentEvaluationSection section)
	{
		return saveSection(section, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/section")
	public Response updateComponentEvaluationSection(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentEvaluationSection section)
	{
		return saveSection(section, false);
	}

	private Response saveSection(ComponentEvaluationSection section, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(section);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getComponentService().saveComponentEvaluationSection(section);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			// TODO: How does this work with composite keys?
			return Response.created(URI.create(section.getComponentEvaluationSectionPk().getEvaulationSection())).build();
		} else {
			return Response.ok().build();
		}
	}

	// ComponentRESTResource Evaluation Schedule section
	@GET
	@APIDescription("Gets a list of evaluation schedules associated to the entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentEvaluationSchedule.class)
	@Path("/{id}/schedule")
	public List<ComponentEvaluationSchedule> getComponentEvaluationSchedule(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentEvaluationSchedule.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Removes the specified evaluation schedule from the entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/schedule")
	public void deleteComponentEvaluationSchedule(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentEvaluationSchedulePk scheduleId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentEvaluationSchedule.class, scheduleId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Adds a component evaluation schedule to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentEvaluationSchedule.class)
	@Path("/{id}/schedule")
	public Response addComponentEvaluationSchedule(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentEvaluationSchedule schedule)
	{
		return saveSchedule(schedule, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Updates a component evaluation schedule associated to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/schedule")
	public Response updateComponentEvaluationSchedule(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentEvaluationSchedule schedule)
	{
		return saveSchedule(schedule, false);
	}

	private Response saveSchedule(ComponentEvaluationSchedule schedule, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(schedule);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getComponentService().saveComponentEvaluationSchedule(schedule);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			// TODO: How does this work with composite keys?
			return Response.created(URI.create(schedule.getComponentEvaluationSchedulePk().getEvaluationLevelCode())).build();
		} else {
			return Response.ok().build();
		}
	}

	// ComponentRESTResource MEDIA section
	@GET
	@APIDescription("Gets the list of media associated to an entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentMedia.class)
	@Path("/{id}/media")
	public List<ComponentMedia> getComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentMedia.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Removes media from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(ComponentMedia.class)
	@Path("/{id}/media")
	public void deleteComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam String mediaId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentMedia.class, mediaId);
	}

	// TODO: Figure out how to recieve the actual media object?
	@POST
	@RequireAdmin
	@APIDescription("Add media to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentMedia.class)
	@Path("/{id}/media")
	public Response addComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentMedia media)
	{
		return saveMedia(media, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Update media associated to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/media")
	public Response updateComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentMedia media)
	{
		return saveMedia(media, false);
	}

	private Response saveMedia(ComponentMedia media, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(media);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getComponentService().saveComponentMedia(media);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			// TODO: How does this work with composite keys?
			return Response.created(URI.create(media.getComponentMediaId())).build();
		} else {
			return Response.ok().build();
		}
	}

	// ComponentRESTResource METADATA section
	@GET
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentMetadata.class)
	@Path("/{id}/metadata")
	public List<ComponentMetadata> getComponentMetadata(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentMetadata.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Removes metadata from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/metadata")
	public void deleteComponentMetadata(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam String metadataId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentMetadata.class, metadataId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add metadata to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentMetadata.class)
	@Path("/{id}/metadata")
	public Response addComponentMetadata(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentMetadata metadata)
	{
		return saveMetadata(metadata, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Update metadata associated to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/metadata")
	public Response updateComponentMetadata(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentMetadata metadata)
	{
		return saveMetadata(metadata, false);
	}

	private Response saveMetadata(ComponentMetadata metadata, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(metadata);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getComponentService().saveComponentMetadata(metadata);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			// TODO: How does this work with composite keys?
			return Response.created(URI.create(metadata.getMetadataId())).build();
		} else {
			return Response.ok().build();
		}
	}

	// ComponentRESTResource QUESTION section
	@GET
	@APIDescription("Get the questions associated with the specified entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentQuestion.class)
	@Path("/{id}/question")
	public List<ComponentQuestion> getComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentQuestion.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a question from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/question/{questionId}")
	public void deleteComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId)
	{
		//TODO:  Validate that the question belongs to the component that htey are try to delete

		service.getComponentService().deactivateBaseComponent(ComponentQuestion.class, questionId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a new question to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentQuestion.class)
	@Path("/{id}/question")
	public Response addComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentQuestion question)
	{
		return saveQuestion(question, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Update a question associated with the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/question")
	public Response updateComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentQuestion question)
	{
		return saveQuestion(question, false);
	}

	private Response saveQuestion(ComponentQuestion question, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(question);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getComponentService().saveComponentQuestion(question);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			// TODO: How does this work with composite keys?
			return Response.created(URI.create(question.getQuestionId())).build();
		} else {
			return Response.ok().build();
		}
	}

	// ComponentRESTResource QUESTION RESPONSE section
	@GET
	@APIDescription("Gets the responses for a given question associated to the specified entity ")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentQuestionResponse.class)
	@Path("/{id}/response")
	public List<ComponentQuestionResponse> getComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam String questionId)
	{
		return service.getComponentService().getBaseComponent(ComponentQuestionResponse.class, questionId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a response from the given question on the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/response")
	public void deleteComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam String questionResponseId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentQuestionResponse.class, questionResponseId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a response to the given question on the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentQuestionResponse.class)
	@Path("/{id}/response")
	public Response addComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentQuestionResponse response)
	{
		return saveQuestionResponse(response, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/response")
	public Response updateComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentQuestionResponse response)
	{
		return saveQuestionResponse(response, false);
	}

	private Response saveQuestionResponse(ComponentQuestionResponse response, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(response);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getComponentService().saveComponentQuestionResponse(response);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			// TODO: How does this work with composite keys?
			return Response.created(URI.create(response.getResponseId())).build();
		} else {
			return Response.ok().build();
		}
	}

	// ComponentRESTResource RESOURCE section
	@GET
	@APIDescription("Get the resources associated to the given entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentResource.class)
	@Path("/{id}/resource")
	public List<ComponentResource> getComponentResource(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentResource.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a given resource from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/resource")
	public void deleteComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam String resourceId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentResource.class, resourceId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a resource to the given entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentRESTResource.class)
	@Path("/{id}/resource")
	public Response addComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentResource resource)
	{
		return saveResource(resource, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Update a resource associated with a given entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/resource")
	public Response updateComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentResource resource)
	{
		return saveResource(resource, false);
	}

	private Response saveResource(ComponentResource resource, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(resource);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getComponentService().saveComponentResource(resource);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			// TODO: How does this work with composite keys?
			return Response.created(URI.create(resource.getResourceId())).build();
		} else {
			return Response.ok().build();
		}
	}

	// ComponentRESTResource REVIEW section
	@GET
	@APIDescription("Get the reviews for a specified entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentReview.class)
	@Path("/{id}/review")
	public List<ComponentReview> getComponentReview(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentReview.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a review from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/review")
	public void deleteComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam String reviewId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentReview.class, reviewId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a review to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReview.class)
	@Path("/{id}/review")
	public Response addComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentReview review)
	{
		return saveReview(review, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Update a review associated with the given entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/review")
	public Response updateComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentReview review)
	{
		return saveReview(review, false);
	}

	private Response saveReview(ComponentReview review, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(review);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getComponentService().saveComponentReview(review);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			// TODO: How does this work with composite keys?
			return Response.created(URI.create(review.getComponentReviewId())).build();
		} else {
			return Response.ok().build();
		}
	}

	// ComponentRESTResource REVIEW CON section
	@GET
	@APIDescription("Get the cons associated to a review on the specified entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentReviewCon.class)
	@Path("/{id}/con")
	public List<ComponentReviewCon> getComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentReviewCon.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a con from the given review accociated with the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/con")
	public void deleteComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentReviewConPk conId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentReviewCon.class, conId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a con to the given review associated with the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReviewCon.class)
	@Path("/{id}/con")
	public Response addComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentReviewCon con)
	{
		ValidationModel validationModel = new ValidationModel(con);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getComponentService().saveComponentReviewCon(con);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		// Again, how are we going to handle composite keys?
		return Response.created(URI.create(con.getComponentReviewConPk().getReviewCon())).build();
	}

	// ComponentRESTResource REVIEW PRO section
	@GET
	@APIDescription("Get the pros for a review associated with the given entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentReviewPro.class)
	@Path("/{id}/pro")
	public List<ComponentReviewPro> getComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentReviewPro.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a pro from the review associated with a specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/pro")
	public void deleteComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentReviewProPk proId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentReviewPro.class, proId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a pro to the review associated with the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReviewPro.class)
	@Path("/{id}/pro")
	public Response addComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentReviewPro pro)
	{
		ValidationModel validationModel = new ValidationModel(pro);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getComponentService().saveComponentReviewPro(pro);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		// Again, how are we going to handle composite keys?
		return Response.created(URI.create(pro.getComponentReviewProPk().getReviewPro())).build();
	}

	// ComponentRESTResource TAG section
	@GET
	@APIDescription("Get the tag list for a specified entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTag.class)
	@Path("/{id}/tag")
	public List<ComponentTag> getComponentTag(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentTag.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a tag from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/tag")
	public void deleteComponentTag(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam String tagId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentTag.class, tagId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a tag to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentTag.class)
	@Path("/{id}/tag")
	public Response addComponentTag(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentTag tag)
	{
		ValidationModel validationModel = new ValidationModel(tag);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getComponentService().saveComponentTag(tag);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		return Response.created(URI.create(tag.getTagId())).build();
	}

	// ComponentRESTResource TRACKING section
	@GET
	@RequireAdmin
	@APIDescription("Get the list of tracking details on a specified entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTracking.class)
	@Path("/{id}/tracking")
	public List<ComponentTracking> getComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentTracking.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a tracking entry from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/tracking")
	public void deleteComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam String trackingId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentTracking.class, trackingId);
	}

	@POST
	@APIDescription("Add a tracking entry for the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentTracking.class)
	@Path("/{id}/tracking")
	public Response addComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentTracking tracking)
	{
		return saveTracking(tracking, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Update a tracking entry for the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/tracking")
	public Response updateComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentTracking tracking)
	{
		return saveTracking(tracking, false);
	}

	private Response saveTracking(ComponentTracking tracking, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(tracking);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getComponentService().saveComponentTracking(tracking);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			// TODO: How does this work with composite keys?
			return Response.created(URI.create(tracking.getComponentTrackingId())).build();
		} else {
			return Response.ok().build();
		}
	}

}
