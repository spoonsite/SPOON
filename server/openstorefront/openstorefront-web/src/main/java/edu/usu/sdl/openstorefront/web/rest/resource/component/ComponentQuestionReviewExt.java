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

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewCon;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewConPk;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewPro;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewProPk;
import edu.usu.sdl.openstorefront.core.entity.ReviewCon;
import edu.usu.sdl.openstorefront.core.entity.ReviewPro;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.view.ComponentQuestionResponseView;
import edu.usu.sdl.openstorefront.core.view.ComponentQuestionView;
import edu.usu.sdl.openstorefront.core.view.ComponentReviewProCon;
import edu.usu.sdl.openstorefront.core.view.ComponentReviewView;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public abstract class ComponentQuestionReviewExt
		extends ComponentExtendedSubResourceExt
{

	// <editor-fold defaultstate="collapsed"  desc="ComponentRESTResource QUESTION section">
	@GET
	@APIDescription("Get the questions associated with the specified component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentQuestion.class)
	@Path("/{id}/questions")
	public List<ComponentQuestion> getComponentQuestions(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentQuestion.class, componentId);
	}

	@GET
	@APIDescription("Get the questions associated with the specified component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentQuestionView.class)
	@Path("/{id}/questions/view")
	public Response getComponentQuestionView(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentQuestion questionExample = new ComponentQuestion();
		questionExample.setActiveStatus(filterQueryParams.getStatus());
		questionExample.setComponentId(componentId);

		List<ComponentQuestion> componentQuestions = service.getPersistenceService().queryByExample(questionExample);
		String user = SecurityUtil.getCurrentUserName();
		if (filterQueryParams.getStatus().equals(ComponentQuestion.ACTIVE_STATUS)) {
			ComponentQuestion pendingQuestionExample = new ComponentQuestion();
			pendingQuestionExample.setActiveStatus(ComponentQuestion.PENDING_STATUS);
			pendingQuestionExample.setComponentId(componentId);
			pendingQuestionExample.setCreateUser(user);
			componentQuestions.addAll(service.getPersistenceService().queryByExample(pendingQuestionExample));
		}
		componentQuestions = filterQueryParams.filter(componentQuestions);
		componentQuestions.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, ComponentQuestion.FIELD_CREATE_DTS));

		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);

		List<ComponentQuestionResponse> componentQuestionResponses = service.getPersistenceService().queryByExample(responseExample);
		Map<String, List<ComponentQuestionResponseView>> responseMap = new HashMap<>();
		for (ComponentQuestionResponse componentQuestionResponse : componentQuestionResponses) {
			if (responseMap.containsKey(componentQuestionResponse.getQuestionId())) {
				responseMap.get(componentQuestionResponse.getQuestionId()).add(ComponentQuestionResponseView.toView(componentQuestionResponse));
			} else {
				List<ComponentQuestionResponseView> responseViews = new ArrayList<>();
				responseViews.add(ComponentQuestionResponseView.toView(componentQuestionResponse));
				responseMap.put(componentQuestionResponse.getQuestionId(), responseViews);
			}
		}
		List<ComponentQuestionView> views = ComponentQuestionView.toViewList(componentQuestions, responseMap);

		GenericEntity<List<ComponentQuestionView>> entity = new GenericEntity<List<ComponentQuestionView>>(views)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Get the questions associated with the specified component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentQuestionView.class)
	@Path("/{id}/questions/details")
	public List<ComponentQuestionView> getDetailComponentQuestions(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		List<ComponentQuestionView> componentQuestionViews = new ArrayList<>();
		List<ComponentQuestion> questions = service.getComponentService().getBaseComponent(ComponentQuestion.class, componentId);
		for (ComponentQuestion question : questions) {
			ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
			responseExample.setActiveStatus(ComponentQuestionResponse.ACTIVE_STATUS);
			responseExample.setQuestionId(question.getQuestionId());

			List<ComponentQuestionResponse> responses = service.getPersistenceService().queryByExample(new QueryByExample<>(responseExample));
			ComponentQuestionView questionView = ComponentQuestionView.toView(question, ComponentQuestionResponseView.toViewList(responses));
			componentQuestionViews.add(questionView);
		}
		return componentQuestionViews;
	}

	@GET
	@APIDescription("Get a question for the specified component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentQuestion.class)
	@Path("/{id}/questions/{questionId}")
	public Response getComponentQuestionResponses(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId)
	{
		ComponentQuestion componentQuestion = service.getPersistenceService().findById(ComponentQuestion.class, questionId);
		if (componentQuestion != null) {
			checkBaseComponentBelongsToComponent(componentQuestion, componentId);
		}
		return sendSingleEntityResponse(componentQuestion);
	}

	@DELETE
	@APIDescription("Inactivates a question from the specified entity")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/questions/{questionId}")
	public Response deleteComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId)
	{
		Response response = Response.ok().build();
		ComponentQuestion componentQuestion = service.getPersistenceService().findById(ComponentQuestion.class, questionId);
		if (componentQuestion != null) {
			checkBaseComponentBelongsToComponent(componentQuestion, componentId);
			response = ownerCheck(componentQuestion, SecurityPermission.ADMIN_QUESTIONS_DELETE);
			if (response == null) {
				service.getComponentService().deactivateBaseComponent(ComponentQuestion.class, questionId);
				response = Response.ok().build();
			}
		}
		return response;
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_QUESTIONS_UPDATE)
	@APIDescription("Activates a question from the specified entity")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/questions/{questionId}/activate")
	public Response activateComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId)
	{
		ComponentQuestion componentQuestionExample = new ComponentQuestion();
		componentQuestionExample.setComponentId(componentId);
		componentQuestionExample.setQuestionId(questionId);

		ComponentQuestion componentQuestion = service.getPersistenceService().queryOneByExample(componentQuestionExample);
		if (componentQuestion != null) {
			service.getComponentService().activateBaseComponent(ComponentQuestion.class, questionId);
			componentQuestion.setActiveStatus(ComponentQuestion.ACTIVE_STATUS);
		}
		return sendSingleEntityResponse(componentQuestion);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_QUESTIONS_UPDATE)
	@APIDescription("Set a question to pending for the specified entity")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/questions/{questionId}/pending")
	public Response pendingComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId)
	{
		ComponentQuestion componentQuestionExample = new ComponentQuestion();
		componentQuestionExample.setComponentId(componentId);
		componentQuestionExample.setQuestionId(questionId);

		ComponentQuestion componentQuestion = service.getPersistenceService().queryOneByExample(componentQuestionExample);
		if (componentQuestion != null) {
			service.getComponentService().setQuestionPending(questionId);
			componentQuestion.setActiveStatus(ComponentQuestion.PENDING_STATUS);
		}
		return sendSingleEntityResponse(componentQuestion);
	}

	@POST
	@APIDescription("Add a new question to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentQuestion.class)
	@Path("/{id}/questions")
	public Response addComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentQuestion question)
	{
		question.setComponentId(componentId);
		return saveQuestion(question, true);
	}

	@PUT
	@APIDescription("Update a question associated with the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/questions/{questionId}")
	public Response updateComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@RequiredParam ComponentQuestion question)
	{
		Response response = Response.ok().build();
		ComponentQuestion componentQuestion = service.getPersistenceService().findById(ComponentQuestion.class, questionId);
		if (componentQuestion != null) {
			checkBaseComponentBelongsToComponent(componentQuestion, componentId);
			response = ownerCheck(componentQuestion, SecurityPermission.ADMIN_QUESTIONS_UPDATE);
			if (response == null) {
				question.setComponentId(componentId);
				question.setQuestionId(questionId);
				response = saveQuestion(question, false);
			}
		}
		return response;
	}

	private Response saveQuestion(ComponentQuestion question, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(question);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			if (PropertiesManager.getInstance().getValue(PropertiesManager.KEY_USER_REVIEW_AUTO_APPROVE, "true").equalsIgnoreCase("true")) {
				question.setActiveStatus(ComponentQuestion.ACTIVE_STATUS);
			} else {
				question.setActiveStatus(ComponentQuestion.PENDING_STATUS);
			}
			question.setCreateUser(SecurityUtil.getCurrentUserName());
			question.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentQuestion(question);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create(BASE_RESOURCE_PATH + question.getComponentId() + "/questions/" + question.getQuestionId())).entity(question).build();
		} else {
			return Response.ok(question).build();
		}
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed"  desc="ComponentRESTResource QUESTION RESPONSE section">
	@GET
	@APIDescription("Gets the responses for a given question associated to the specified component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentQuestionResponse.class)
	@Path("/{id}/questions/{questionId}/responses")
	public List<ComponentQuestionResponse> getComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId)
	{
		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);
		responseExample.setQuestionId(questionId);
		return service.getPersistenceService().queryByExample(responseExample);
	}

	@GET
	@APIDescription("Gets the responses for a given question associated to the specified component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentQuestionResponse.class)
	@Path("/{id}/questions/{questionId}/responses/{responseId}")
	public Response getComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@PathParam("responseId")
			@RequiredParam String responseId)
	{
		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);
		responseExample.setQuestionId(questionId);
		responseExample.setResponseId(responseId);
		ComponentQuestionResponse questionResponse = service.getPersistenceService().queryOneByExample(responseExample);
		return sendSingleEntityResponse(questionResponse);
	}

	@DELETE
	@APIDescription("Inactivates a response from the given question on the specified component")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/questions/{questionId}/responses/{responseId}")
	public Response deleteComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@PathParam("responseId")
			@RequiredParam String responseId)
	{
		Response response = Response.ok().build();
		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);
		responseExample.setQuestionId(questionId);
		responseExample.setResponseId(responseId);
		ComponentQuestionResponse questionResponse = service.getPersistenceService().queryOneByExample(responseExample);
		if (questionResponse != null) {
			response = ownerCheck(questionResponse, SecurityPermission.ADMIN_QUESTIONS_DELETE);
			if (response == null) {
				service.getComponentService().deactivateBaseComponent(ComponentQuestionResponse.class, responseId);
				response = Response.ok().build();
			}
		}
		return response;
	}

	@PUT
	@RequireSecurity
	@APIDescription("Activates a response from the given question on the specified component")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/questions/{questionId}/responses/{responseId}/activate")
	public Response activateComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@PathParam("responseId")
			@RequiredParam String responseId)
	{
		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);
		responseExample.setQuestionId(questionId);
		responseExample.setResponseId(responseId);
		ComponentQuestionResponse questionResponse = service.getPersistenceService().queryOneByExample(responseExample);
		if (questionResponse != null) {
			service.getComponentService().activateBaseComponent(ComponentQuestionResponse.class, responseId);
			questionResponse.setActiveStatus(ComponentQuestionResponse.ACTIVE_STATUS);
		}
		return sendSingleEntityResponse(questionResponse);
	}

	@PUT
	@RequireSecurity
	@APIDescription("Sets a response from the given question on the specified component to Pending")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/questions/{questionId}/responses/{responseId}/pending")
	public Response pendingComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@PathParam("responseId")
			@RequiredParam String responseId)
	{
		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);
		responseExample.setQuestionId(questionId);
		responseExample.setResponseId(responseId);
		ComponentQuestionResponse questionResponse = service.getPersistenceService().queryOneByExample(responseExample);
		if (questionResponse != null) {
			service.getComponentService().setQuestionResponsePending(responseId);
			questionResponse.setActiveStatus(ComponentQuestionResponse.PENDING_STATUS);
		}
		return sendSingleEntityResponse(questionResponse);
	}

	@POST
	@APIDescription("Add a response to the given question on the specified component")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentQuestionResponse.class)
	@Path("/{id}/questions/{questionId}/responses")
	public Response addComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@RequiredParam ComponentQuestionResponse response)
	{
		response.setComponentId(componentId);
		response.setQuestionId(questionId);
		return saveQuestionResponse(response, true);
	}

	@PUT
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/questions/{questionId}/responses/{responseId}")
	public Response updateComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@PathParam("responseId")
			@RequiredParam String responseId,
			@RequiredParam ComponentQuestionResponse questionResponseInput)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);
		responseExample.setQuestionId(questionId);
		responseExample.setResponseId(responseId);
		ComponentQuestionResponse questionResponse = service.getPersistenceService().queryOneByExample(responseExample);
		if (questionResponse != null) {
			response = ownerCheck(questionResponse, SecurityPermission.ADMIN_QUESTIONS_UPDATE);
			if (response == null) {
				questionResponseInput.setComponentId(componentId);
				questionResponseInput.setQuestionId(questionId);
				questionResponseInput.setResponseId(responseId);
				response = saveQuestionResponse(questionResponseInput, false);
			}
		}
		return response;
	}

	private Response saveQuestionResponse(ComponentQuestionResponse response, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(response);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			if (PropertiesManager.getInstance().getValue(PropertiesManager.KEY_USER_REVIEW_AUTO_APPROVE, "true").equalsIgnoreCase("true")) {
				response.setActiveStatus(ComponentQuestionResponse.ACTIVE_STATUS);
			} else {
				response.setActiveStatus(ComponentQuestionResponse.PENDING_STATUS);
			}
			response.setCreateUser(SecurityUtil.getCurrentUserName());
			response.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentQuestionResponse(response);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {

			return Response.created(URI.create(BASE_RESOURCE_PATH + response.getComponentId() + "/questions/" + response.getQuestionId() + "/responses/" + response.getResponseId())).entity(response).build();
		} else {
			return Response.ok(response).build();
		}
	}
	// </editor-fold>

	//<editor-fold defaultstate="collapsed"  desc="ComponentRESTResource REVIEW section">
	@GET
	@APIDescription("Get the reviews for a specified entity")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentReview.class)
	@Path("/{id}/reviews")
	public List<ComponentReview> getComponentReview(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentReview.class, componentId);
	}

	@GET
	@APIDescription("Get the reviews for a specified entity")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentReviewView.class)
	@Path("/{id}/reviews/view")
	public Response getComponentReviewView(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentReview reviewExample = new ComponentReview();
		reviewExample.setActiveStatus(filterQueryParams.getStatus());
		reviewExample.setComponentId(componentId);

		List<ComponentReview> componentReviews = service.getPersistenceService().queryByExample(reviewExample);

		if (filterQueryParams.getStatus().equals(ComponentReview.ACTIVE_STATUS)) {
			ComponentReview pendingReviewExample = new ComponentReview();
			pendingReviewExample.setActiveStatus(ComponentReview.PENDING_STATUS);
			pendingReviewExample.setCreateUser(SecurityUtil.getCurrentUserName());
			pendingReviewExample.setComponentId(componentId);

			List<ComponentReview> pendingComponentReviews = service.getPersistenceService().queryByExample(pendingReviewExample);
			componentReviews.addAll(pendingComponentReviews);
		}
		componentReviews = filterQueryParams.filter(componentReviews);
		List<ComponentReviewView> views = ComponentReviewView.toViewList(componentReviews);
		views.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_DESCENDING, ComponentReviewView.UPDATE_DATE_FIELD));

		GenericEntity<List<ComponentReviewView>> entity = new GenericEntity<List<ComponentReviewView>>(views)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Get the reviews for a specified user")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentReviewView.class)
	@Path("/reviews/{username}")
	public List<ComponentReviewView> getComponentReviewsByUsername(
			@PathParam("username")
			@RequiredParam String username)
	{
		return service.getComponentService().getReviewByUser(username);
	}

	@DELETE
	@APIDescription("Delete a review from the specified entity")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/reviews/{reviewId}")
	public Response deleteComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		Response response = Response.ok().build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			response = ownerCheck(componentReview, SecurityPermission.ADMIN_REVIEW_DELETE);
			if (response == null) {
				service.getComponentService().deactivateBaseComponent(ComponentReview.class, reviewId);
			}
		}
		return response;
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_REVIEW_UPDATE)
	@APIDescription("Activate a review on the specified component")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/reviews/{reviewId}/activate")
	public Response activateComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		ComponentReview componentReviewExample = new ComponentReview();
		componentReviewExample.setComponentId(componentId);
		componentReviewExample.setComponentReviewId(reviewId);

		ComponentReview componentReview = service.getPersistenceService().queryOneByExample(componentReviewExample);
		if (componentReview != null) {
			service.getComponentService().activateBaseComponent(ComponentReview.class, reviewId);
			componentReview.setActiveStatus(ComponentReview.ACTIVE_STATUS);
		}
		return sendSingleEntityResponse(componentReview);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_REVIEW_UPDATE)
	@APIDescription("Sets a review on the specified component to pending")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/reviews/{reviewId}/pending")
	public Response pendingComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		ComponentReview componentReviewExample = new ComponentReview();
		componentReviewExample.setComponentId(componentId);
		componentReviewExample.setComponentReviewId(reviewId);

		ComponentReview componentReview = service.getPersistenceService().queryOneByExample(componentReviewExample);
		if (componentReview != null) {
			service.getComponentService().setReviewPending(reviewId);
			componentReview.setActiveStatus(ComponentReview.PENDING_STATUS);
		}
		return sendSingleEntityResponse(componentReview);
	}

	@POST
	@APIDescription("Add a review to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReview.class)
	@Path("/{id}/reviews")
	public Response addComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentReview review)
	{
		review.setComponentId(componentId);
		return saveReview(review, true);
	}

	@PUT
	@APIDescription("Update a review associated with the given entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/reviews/{reviewId}")
	public Response updateComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@RequiredParam ComponentReview review)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview, SecurityPermission.ADMIN_REVIEW_UPDATE);
			if (response == null) {
				review.setComponentId(componentId);
				review.setComponentReviewId(reviewId);
				response = saveReview(review, false);
			}
		}
		return response;
	}

	private Response saveReview(ComponentReview review, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(review);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			review.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			review.setCreateUser(SecurityUtil.getCurrentUserName());
			review.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentReview(review);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create(BASE_RESOURCE_PATH + review.getComponentId() + "/review/" + review.getComponentReviewId())).entity(review).build();
		} else {
			return Response.ok(review).build();
		}
	}

	@POST
	@APIDescription("Add a review to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReviewView.class)
	@Path("/{id}/reviews/detail")
	public Response addComponentReviewDetail(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentReviewView review)
	{
		review.setComponentId(componentId);
		return saveFullReview(review, true);
	}

	@PUT
	@APIDescription("Update a review associated with the given entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/reviews/{reviewId}/detail")
	public Response updateComponentReviewDetail(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@RequiredParam ComponentReviewView review)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview, SecurityPermission.ADMIN_REVIEW_UPDATE);
			if (response == null) {
				review.setComponentId(componentId);
				review.setReviewId(reviewId);
				response = saveFullReview(review, false);
			}
		}
		return response;
	}

	private Response saveFullReview(ComponentReviewView review, Boolean post)
	{
		ComponentReview componentReview = new ComponentReview();
		componentReview.setComponentId(review.getComponentId());
		componentReview.setComponentReviewId(review.getReviewId());
		componentReview.setComment(review.getComment());
		componentReview.setLastUsed(review.getLastUsed());
		componentReview.setOrganization(review.getOrganization());
		componentReview.setRating(review.getRating());
		componentReview.setRecommend(review.isRecommend());
		componentReview.setTitle(review.getTitle());
		componentReview.setUserTimeCode(review.getUserTimeCode());
		componentReview.setUserTypeCode(review.getUserTypeCode());
		componentReview.setSecurityMarkingType(review.getSecurityMarkingType());
		componentReview.setDataSensitivity(review.getDataSensitivity());

		List<ComponentReviewPro> pros = new ArrayList<>();
		for (ComponentReviewProCon pro : review.getPros()) {
			ComponentReviewPro componentReviewPro = new ComponentReviewPro();
			ComponentReviewProPk reviewProPk = new ComponentReviewProPk();
			reviewProPk.setReviewPro(pro.getText());
			componentReviewPro.setComponentReviewProPk(reviewProPk);
			pros.add(componentReviewPro);
		}

		List<ComponentReviewCon> cons = new ArrayList<>();
		for (ComponentReviewProCon con : review.getCons()) {
			ComponentReviewCon componentReviewCon = new ComponentReviewCon();
			ComponentReviewConPk reviewConPk = new ComponentReviewConPk();
			reviewConPk.setReviewCon(con.getText());
			componentReviewCon.setComponentReviewConPk(reviewConPk);
			cons.add(componentReviewCon);
		}

		ValidationResult validationResult = service.getComponentService().saveDetailReview(componentReview, pros, cons);

		if (!validationResult.valid()) {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create(BASE_RESOURCE_PATH + componentReview.getComponentId() + "/review/" + componentReview.getComponentReviewId())).entity(review).build();
		} else {
			return Response.ok(review).build();
		}
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed"  desc="ComponentRESTResource REVIEW CON section">
	@GET
	@APIDescription("Get the cons associated to a review")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentReviewCon.class)
	@Path("/{id}/reviews/{reviewId}/cons")
	public List<ComponentReviewCon> getComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		ComponentReviewCon componentReviewConExample = new ComponentReviewCon();
		ComponentReviewConPk componentReviewConPk = new ComponentReviewConPk();
		componentReviewConPk.setComponentReviewId(reviewId);
		componentReviewConExample.setComponentReviewConPk(componentReviewConPk);
		componentReviewConExample.setComponentId(componentId);
		return service.getPersistenceService().queryByExample(new QueryByExample<>(componentReviewConExample));
	}

	@GET
	@APIDescription("Get the cons associated to a review")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentReviewCon.class)
	@Path("/{id}/reviews/{reviewId}/cons/{conId}")
	public Response getComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@PathParam("conId")
			@RequiredParam String conId)
	{
		ComponentReviewCon componentReviewConExample = new ComponentReviewCon();
		ComponentReviewConPk componentReviewConPk = new ComponentReviewConPk();
		componentReviewConPk.setComponentReviewId(reviewId);
		componentReviewConPk.setReviewCon(conId);
		componentReviewConExample.setComponentReviewConPk(componentReviewConPk);
		componentReviewConExample.setComponentId(componentId);

		ComponentReviewCon reviewCon = service.getPersistenceService().queryOneByExample(new QueryByExample<>(componentReviewConExample));
		return sendSingleEntityResponse(reviewCon);
	}

	@DELETE
	@APIDescription("Deletes all cons from the given review accociated with the specified entity")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/reviews/{reviewId}/cons")
	public Response deleteComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		Response response = Response.ok().build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview, SecurityPermission.ADMIN_REVIEW_DELETE);
			if (response == null) {
				ComponentReviewCon example = new ComponentReviewCon();
				ComponentReviewConPk pk = new ComponentReviewConPk();
				pk.setComponentReviewId(reviewId);
				example.setComponentReviewConPk(pk);
				service.getPersistenceService().deleteByExample(example);
			}
		}
		return response;
	}

	@POST
	@APIDescription("Add a cons to the given review associated with the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReviewCon.class)
	@Path("/{id}/reviews/{reviewId}/cons")
	public Response addComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@RequiredParam String text)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview, SecurityPermission.ADMIN_REVIEW_UPDATE);
			if (response == null) {
				ComponentReviewCon con = new ComponentReviewCon();
				ComponentReviewConPk pk = checkReviewConPk(reviewId, text);
				con.setComponentReviewConPk(pk);
				con.setActiveStatus(ComponentReviewCon.ACTIVE_STATUS);
				con.setComponentId(componentId);
				ValidationModel validationModel = new ValidationModel(con);
				validationModel.setConsumeFieldsOnly(true);
				ValidationResult validationResult = ValidationUtil.validate(validationModel);

				if (validationResult.valid()) {
					con.setCreateUser(SecurityUtil.getCurrentUserName());
					con.setUpdateUser(SecurityUtil.getCurrentUserName());
					service.getComponentService().saveComponentReviewCon(con);
					response = Response.created(URI.create(BASE_RESOURCE_PATH + con.getComponentId()
							+ "/reviews/" + con.getComponentReviewConPk().getComponentReviewId()
							+ "/cons/" + con.getComponentReviewConPk().getReviewCon())).entity(con).build();

				} else {
					response = Response.ok(validationResult.toRestError()).build();
				}
			}
		}
		return response;
	}

	private ComponentReviewConPk checkReviewConPk(String reviewId, String text)
	{
		ComponentReviewConPk pk = new ComponentReviewConPk();
		pk.setComponentReviewId(reviewId);
		ReviewCon conCode = service.getLookupService().getLookupEnity(ReviewCon.class, text);
		if (conCode == null) {
			conCode = service.getLookupService().getLookupEnityByDesc(ReviewCon.class, text);
			if (conCode == null) {
				pk.setReviewCon(null);
			} else {
				pk.setReviewCon(conCode.getCode());
			}
		} else {
			pk.setReviewCon(conCode.getCode());
		}
		return pk;
	}

	// </editor-fold>
	//<editor-fold defaultstate="collapsed"  desc="ComponentRESTResource REVIEW PRO section">
	@GET
	@APIDescription("Get the pros for a review associated with the given entity")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentReviewPro.class)
	@Path("/{id}/reviews/{reviewId}/pros")
	public List<ComponentReviewPro> getComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		ComponentReviewPro componentReviewProExample = new ComponentReviewPro();
		componentReviewProExample.setComponentId(componentId);
		ComponentReviewProPk componentReviewProPk = new ComponentReviewProPk();
		componentReviewProPk.setComponentReviewId(reviewId);
		componentReviewProExample.setComponentReviewProPk(componentReviewProPk);
		return service.getPersistenceService().queryByExample(new QueryByExample<>(componentReviewProExample));
	}

	@GET
	@APIDescription("Get the pros for a review associated with the given entity")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentReviewPro.class)
	@Path("/{id}/reviews/{reviewId}/pros/{proId}")
	public Response getComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@PathParam("proId")
			@RequiredParam String proId)
	{
		ComponentReviewPro componentReviewProExample = new ComponentReviewPro();
		componentReviewProExample.setComponentId(componentId);
		ComponentReviewProPk componentReviewProPk = new ComponentReviewProPk();
		componentReviewProPk.setComponentReviewId(reviewId);
		componentReviewProPk.setReviewPro(proId);
		componentReviewProExample.setComponentReviewProPk(componentReviewProPk);
		ComponentReviewPro componentReviewPro = service.getPersistenceService().queryOneByExample(new QueryByExample<>(componentReviewProExample));
		return sendSingleEntityResponse(componentReviewPro);
	}

	@DELETE
	@APIDescription("Deletes all pros from the review associated with a specified entity")
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{id}/reviews/{reviewId}/pros")
	public Response deleteComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		Response response = Response.ok().build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview, SecurityPermission.ADMIN_REVIEW_UPDATE);
			if (response == null) {
				ComponentReviewPro example = new ComponentReviewPro();
				ComponentReviewProPk pk = new ComponentReviewProPk();
				pk.setComponentReviewId(reviewId);
				example.setComponentReviewProPk(pk);
				service.getPersistenceService().deleteByExample(example);
				service.getComponentService().deactivateBaseComponent(ComponentReviewPro.class, pk);
			}
		}
		return response;
	}

	@POST
	@APIDescription("Add a pro to the review associated with the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReviewPro.class)
	@Path("/{id}/reviews/{reviewId}/pros")
	public Response addComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@RequiredParam String text)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview, SecurityPermission.ADMIN_REVIEW_UPDATE);
			if (response == null) {
				ComponentReviewPro pro = new ComponentReviewPro();
				ComponentReviewProPk pk = checkReviewProPk(reviewId, text);
				pro.setComponentReviewProPk(pk);
				pro.setActiveStatus(ComponentReviewPro.ACTIVE_STATUS);
				pro.setComponentId(componentId);

				ValidationModel validationModel = new ValidationModel(pro);
				validationModel.setConsumeFieldsOnly(true);
				ValidationResult validationResult = ValidationUtil.validate(validationModel);
				if (validationResult.valid()) {
					pro.setCreateUser(SecurityUtil.getCurrentUserName());
					pro.setUpdateUser(SecurityUtil.getCurrentUserName());
					service.getComponentService().saveComponentReviewPro(pro);
					response = Response.created(URI.create(BASE_RESOURCE_PATH + pro.getComponentId()
							+ "/reviews/" + pro.getComponentReviewProPk().getComponentReviewId()
							+ "/pros/" + pro.getComponentReviewProPk().getReviewPro())).entity(pro).build();
				} else {
					response = Response.ok(validationResult.toRestError()).build();
				}
			}
		}
		return response;
	}

	private ComponentReviewProPk checkReviewProPk(String reviewId, String text)
	{
		ComponentReviewProPk pk = new ComponentReviewProPk();
		pk.setComponentReviewId(reviewId);
		ReviewPro proCode = service.getLookupService().getLookupEnity(ReviewPro.class, text);
		if (proCode == null) {
			proCode = service.getLookupService().getLookupEnityByDesc(ReviewPro.class, text);
			if (proCode == null) {
				pk.setReviewPro(null);
			} else {
				pk.setReviewPro(proCode.getCode());
			}
		} else {
			pk.setReviewPro(proCode.getCode());
		}
		return pk;
	}
	// </editor-fold>

}
