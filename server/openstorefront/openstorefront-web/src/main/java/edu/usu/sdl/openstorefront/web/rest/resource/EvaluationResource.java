/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.ChecklistTemplate;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklist;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistRecommendation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistResponse;
import edu.usu.sdl.openstorefront.core.entity.EvaluationComment;
import edu.usu.sdl.openstorefront.core.entity.EvaluationTemplate;
import edu.usu.sdl.openstorefront.core.model.EvaluationAll;
import edu.usu.sdl.openstorefront.core.view.EvaluationView;
import edu.usu.sdl.openstorefront.core.view.EvaluationViewWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.doc.security.RequireAdmin;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.lang.reflect.Field;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sourceforge.stripes.util.bean.BeanUtil;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Provides access to evaluations across components resources")
@Path("v1/resource/evaluations")
public class EvaluationResource
		extends BaseResource
{

	@GET
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(EvaluationViewWrapper.class)
	@APIDescription("Gets Evaluations")
	public Response getEvaluations(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		Evaluation evaluationExample = new Evaluation();
		evaluationExample.setActiveStatus(filterQueryParams.getStatus());

		Evaluation startExample = new Evaluation();
		startExample.setUpdateDts(filterQueryParams.getStart());

		Evaluation endExample = new Evaluation();
		endExample.setUpdateDts(filterQueryParams.getEnd());

		QueryByExample queryByExample = new QueryByExample(evaluationExample);

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(startExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(endExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());
		queryByExample.setSortDirection(filterQueryParams.getSortOrder());

		Evaluation evaluationSortExample = new Evaluation();
		Field sortField = ReflectionUtil.getField(evaluationSortExample, filterQueryParams.getSortField());
		if (sortField != null) {
			BeanUtil.setPropertyValue(sortField.getName(), evaluationSortExample, QueryByExample.getFlagForType(sortField.getType()));
			queryByExample.setOrderBy(evaluationSortExample);
		}

		List<Evaluation> evaluations = service.getPersistenceService().queryByExample(Evaluation.class, queryByExample);

		EvaluationViewWrapper evaluationViewWrapper = new EvaluationViewWrapper();
		evaluationViewWrapper.getData().addAll(EvaluationView.toView(evaluations));
		evaluationViewWrapper.setTotalNumber(service.getPersistenceService().countByExample(queryByExample));

		return sendSingleEntityResponse(evaluationViewWrapper);
	}

	@GET
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(EvaluationView.class)
	@APIDescription("Gets an evaluation")
	@Path("/{evaluationId}")
	public Response getEvaluation(
			@PathParam("evaluationId") String evaluationId
	)
	{
		Evaluation evaluation = new Evaluation();
		evaluation.setEvaluationId(evaluationId);
		evaluation = evaluation.find();
		if (evaluation != null) {
			return sendSingleEntityResponse(EvaluationView.toView(evaluation));
		} else {
			return sendSingleEntityResponse(evaluation);
		}
	}

	@GET
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(EvaluationAll.class)
	@APIDescription("Gets an evaluation")
	@Path("/{evaluationId}/details")
	public Response getEvaluationDetails(
			@PathParam("evaluationId") String evaluationId
	)
	{
		EvaluationAll evaluationAll = service.getEvaluationService().getEvaluation(evaluationId);
		return sendSingleEntityResponse(evaluationAll);
	}

	@POST
	@RequireAdmin
	@APIDescription("Creates an evaluation from template ")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Evaluation.class)
	public Response createEvaluation(Evaluation evaluation)
	{
		ValidationResult validationResult = evaluation.validate();
		if (validationResult.valid()) {
			evaluation = service.getEvaluationService().createEvaluationFromTemplate(evaluation);

			return Response.created(URI.create("v1/resource/evaluation/" + evaluation.getEvaluationId())).entity(evaluation).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Make sure change request exists for the evaluation; It will create new one if needed.")
	@DataType(Evaluation.class)
	@Path("/{evaluationId}/checkentry")
	public Response checkEvaluationEntry(
			@PathParam("evaluationId") String evaluationId
	)
	{
		Evaluation evaluation = new Evaluation();
		evaluation.setEvaluationId(evaluationId);
		evaluation = evaluation.find();
		if (evaluation != null) {
			service.getEvaluationService().checkEvaluationComponent(evaluationId);
			evaluation = new Evaluation();
			evaluation.setEvaluationId(evaluationId);
			evaluation = evaluation.find();
			return Response.ok(evaluation).build();
		} else {
			return sendSingleEntityResponse(evaluation);
		}
	}	
	
	@PUT
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Publish an evaluation")
	@Path("/{evaluationId}/publish")
	public Response publishEvaluation(
			@PathParam("evaluationId") String evaluationId
	)
	{
		Evaluation evaluation = new Evaluation();
		evaluation.setEvaluationId(evaluationId);
		evaluation = evaluation.find();
		if (evaluation != null) {
			service.getEvaluationService().publishEvaluation(evaluationId);
			return Response.ok().build();
		} else {
			return sendSingleEntityResponse(evaluation);
		}
	}

	@PUT
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Unpublish an evaluation")
	@Path("/{evaluationId}/unpublish")
	public Response unpublishEvaluation(
			@PathParam("evaluationId") String evaluationId
	)
	{
		Evaluation evaluation = new Evaluation();
		evaluation.setEvaluationId(evaluationId);
		evaluation = evaluation.find();
		if (evaluation != null) {
			service.getEvaluationService().unpublishEvaluation(evaluationId);
			return Response.ok().build();
		} else {
			return sendSingleEntityResponse(evaluation);
		}
	}

	@PUT
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Activates an evaluation")
	@Path("/{evaluationId}/activate")
	public Response activateEvaluation(
			@PathParam("evaluationId") String evaluationId
	)
	{
		return updateStatus(evaluationId, ChecklistTemplate.ACTIVE_STATUS);
	}

	private Response updateStatus(String evaluationId, String status)
	{
		Evaluation evaluation = new Evaluation();
		evaluation.setEvaluationId(evaluationId);
		evaluation = evaluation.find();
		if (evaluation != null) {
			evaluation.setActiveStatus(status);
			evaluation.save();
		}
		return sendSingleEntityResponse(evaluation);
	}

	@DELETE
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Inactivates or hard removes a evaluation")
	@Path("/{evaluationId}")
	public Response deleteEvaluation(
			@PathParam("evaluationId") String evaluationId,
			@QueryParam("force") boolean force
	)
	{
		if (force) {
			service.getEvaluationService().deleteEvaluation(evaluationId);
			return Response.noContent().build();
		} else {
			return updateStatus(evaluationId, EvaluationTemplate.INACTIVE_STATUS);
		}
	}

	//delete (later)
	//add section
	//remove section
	//add sub section to section
	//remove sub section to section
	@GET
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(EvaluationComment.class)
	@APIDescription("Gets all evaluation comments")
	@Path("/{evaluationId}/comments")
	public Response getAllEvaluationComments(
			@PathParam("evaluationId") String evaluationId,
			@QueryParam("entity") String entity,
			@QueryParam("entityId") String entityId
	)
	{
		EvaluationComment evaluationComment = new EvaluationComment();
		evaluationComment.setActiveStatus(EvaluationComment.ACTIVE_STATUS);
		evaluationComment.setEvaluationId(evaluationId);
		if (StringUtils.isNotBlank(entity)) {
			evaluationComment.setEntity(entity);
		}
		if (StringUtils.isNotBlank(entityId)) {
			evaluationComment.setEntityId(entityId);
		}

		List<EvaluationComment> evaluationComments = evaluationComment.findByExample();

		GenericEntity<List<EvaluationComment>> commentEntity = new GenericEntity<List<EvaluationComment>>(evaluationComments)
		{
		};
		return sendSingleEntityResponse(commentEntity);
	}

	@GET
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(EvaluationComment.class)
	@APIDescription("Gets an evaluation comment")
	@Path("/{evaluationId}/comments/{commentId}")
	public Response getEvaluationComments(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("commentId") String commentId
	)
	{
		EvaluationComment evaluationComment = new EvaluationComment();
		evaluationComment.setEvaluationId(evaluationId);
		evaluationComment.setCommentId(commentId);

		evaluationComment = evaluationComment.find();
		return sendSingleEntityResponse(evaluationComment);
	}

	@POST
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(EvaluationComment.class)
	@APIDescription("Adds a comment")
	@Path("/{evaluationId}/comments")
	public Response postComment(
			@PathParam("evaluationId") String evaluationId,
			EvaluationComment comment
	)
	{
		comment.setEvaluationId(evaluationId);
		return handleCommentSave(comment, true);
	}

	@PUT
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(EvaluationComment.class)
	@APIDescription("Updates a comment")
	@Path("/{evaluationId}/comments/{commentId}")
	public Response updateComment(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("commentId") String commentId,
			EvaluationComment comment
	)
	{
		EvaluationComment evaluationComment = new EvaluationComment();
		evaluationComment.setEvaluationId(evaluationId);
		evaluationComment.setCommentId(commentId);
		evaluationComment = evaluationComment.find();
		if (evaluationComment != null) {
			comment.setEvaluationId(evaluationId);
			comment.setCommentId(commentId);
			return handleCommentSave(comment, false);
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	private Response handleCommentSave(EvaluationComment comment, boolean post)
	{
		ValidationResult validationResult = comment.validate();
		if (validationResult.valid()) {
			comment = comment.save();

			if (post) {
				return Response.created(URI.create("v1/resource/evaluations/" + comment.getCommentId())).entity(comment).build();
			} else {
				return Response.ok(comment).build();
			}
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(EvaluationComment.class)
	@APIDescription("Toggles acknowlege flag on an evaluation")
	@Path("/{evaluationId}/comments/{commentId}/acknowlege")
	public Response toggleAcknowlegeComment(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("commentId") String commentId
	)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		EvaluationComment evaluationComment = new EvaluationComment();
		evaluationComment.setEvaluationId(evaluationId);
		evaluationComment.setCommentId(commentId);
		evaluationComment = evaluationComment.find();
		if (evaluationComment != null) {
			if (evaluationComment.getAcknowledge()) {
				evaluationComment.setAcknowledge(Boolean.FALSE);
			} else {
				evaluationComment.setAcknowledge(Boolean.TRUE);
			}
			evaluationComment = evaluationComment.save();
			response = Response.ok(evaluationComment).build();
		}
		return response;
	}

	@DELETE
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Remove a comment. (must be owner or admin)")
	@Path("/{evaluationId}/comments/{commentId}")
	public Response deleteComment(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("commentId") String commentId
	)
	{
		Response response = Response.noContent().build();

		EvaluationComment evaluationComment = new EvaluationComment();
		evaluationComment.setEvaluationId(evaluationId);
		evaluationComment.setCommentId(commentId);
		evaluationComment = evaluationComment.find();
		if (evaluationComment != null) {

			response = ownerCheck(evaluationComment);
			if (response == null) {
				evaluationComment.delete();
				response = Response.noContent().build();
			}
		}
		return response;
	}

	//update evaluation informaion (version)
	@PUT
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(EvaluationChecklist.class)
	@APIDescription("Update a checklist for an evaluation")
	@Path("/{evaluationId}/checklist/{checklistId}")
	public Response updateEvaluationChecklist(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("checklistId") String checklistId,
			EvaluationChecklist evaluationChecklist
	)
	{
		EvaluationChecklist existing = new EvaluationChecklist();
		existing.setEvaluationId(evaluationId);
		existing.setChecklistId(checklistId);
		existing = existing.find();
		if (existing != null) {
			ValidationResult result = evaluationChecklist.validate(true);
			if (result.valid()) {
				existing.updateFields(evaluationChecklist);
				existing = existing.save();
				return Response.ok(existing).build();
			} else {
				return Response.ok(result.toRestError()).build();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@PUT
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(EvaluationChecklistResponse.class)
	@APIDescription("Update a checklist response for an evaluation")
	@Path("/{evaluationId}/checklist/{checklistId}/responses/{responseId}")
	public Response updateChecklistResponse(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("checklistId") String checklistId,
			@PathParam("responseId") String responseId,
			EvaluationChecklistResponse checklistResponse
	)
	{
		//check owner of checklist
		EvaluationChecklist checklist = new EvaluationChecklist();
		checklist.setEvaluationId(evaluationId);
		checklist.setChecklistId(checklistId);
		checklist = checklist.find();
		if (checklist != null) {
			EvaluationChecklistResponse existing = new EvaluationChecklistResponse();
			existing.setChecklistId(checklistId);
			existing.setResponseId(responseId);
			existing = existing.find();
			if (existing != null) {
				ValidationResult result = checklistResponse.validate();
				if (result.valid()) {
					checklistResponse.setChecklistId(checklistId);
					checklistResponse.setResponseId(responseId);
					checklistResponse = checklistResponse.save();
					return Response.ok(checklistResponse).build();
				} else {
					return Response.ok(result.toRestError()).build();
				}
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@POST
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(EvaluationChecklistRecommendation.class)
	@APIDescription("Adds a checklist recommendation for an evaluation")
	@Path("/{evaluationId}/checklist/{checklistId}/recommendations")
	public Response addChecklistRecommendation(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("checklistId") String checklistId,
			EvaluationChecklistRecommendation recommendation
	)
	{
		recommendation.setChecklistId(checklistId);
		return handleSaveRecommendation(recommendation, true);
	}

	@PUT
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(EvaluationChecklistRecommendation.class)
	@APIDescription("Update a checklist for an evaluation")
	@Path("/{evaluationId}/checklist/{checklistId}/recommendations/{recommendationId}")
	public Response updateChecklistRecommendation(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("checklistId") String checklistId,
			@PathParam("recommendationId") String recommendationId,
			EvaluationChecklistRecommendation recommendation
	)
	{
		EvaluationChecklist checklist = new EvaluationChecklist();
		checklist.setEvaluationId(evaluationId);
		checklist.setChecklistId(checklistId);
		checklist = checklist.find();
		if (checklist != null) {
			EvaluationChecklistRecommendation existing = new EvaluationChecklistRecommendation();
			existing.setChecklistId(checklistId);
			existing.setRecommendationId(recommendationId);
			existing = existing.find();
			if (existing != null) {
				recommendation.setChecklistId(checklistId);
				recommendation.setRecommendationId(recommendationId);
				return handleSaveRecommendation(recommendation, true);
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	private Response handleSaveRecommendation(EvaluationChecklistRecommendation recommendation, boolean post)
	{
		ValidationResult validationResult = recommendation.validate();

		if (validationResult.valid()) {
			recommendation = recommendation.save();
			if (post) {
				return Response.created(URI.create("v1/resource/evaluations/" + recommendation.getRecommendationId())).entity(recommendation).build();
			} else {
				return Response.ok(recommendation).build();
			}
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@DELETE
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Remove a recommendation.")
	@Path("/{evaluationId}/checklist/{checklistId}/recommendations/{recommendationId}")
	public Response deleteRecommendation(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("checklistId") String checklistId,
			@PathParam("recommendationId") String recommendationId
	)
	{
		Response response = Response.noContent().build();

		EvaluationChecklist checklist = new EvaluationChecklist();
		checklist.setEvaluationId(evaluationId);
		checklist.setChecklistId(checklistId);
		checklist = checklist.find();
		if (checklist != null) {
			EvaluationChecklistRecommendation existing = new EvaluationChecklistRecommendation();
			existing.setChecklistId(checklistId);
			existing.setRecommendationId(recommendationId);
			existing = existing.find();
			if (existing != null) {
				existing.delete();
			}
		}
		return response;
	}

}
