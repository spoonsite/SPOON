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

import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.api.query.WhereClauseGroup;
import edu.usu.sdl.openstorefront.core.entity.ChecklistTemplate;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ContentSection;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionMedia;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionTemplate;
import edu.usu.sdl.openstorefront.core.entity.ContentSubSection;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklist;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistRecommendation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistResponse;
import edu.usu.sdl.openstorefront.core.entity.EvaluationComment;
import edu.usu.sdl.openstorefront.core.entity.EvaluationTemplate;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.WorkflowStatus;
import edu.usu.sdl.openstorefront.core.model.ContentSectionAll;
import edu.usu.sdl.openstorefront.core.model.EvaluationAll;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.view.ChecklistResponseView;
import edu.usu.sdl.openstorefront.core.view.ComponentDetailView;
import edu.usu.sdl.openstorefront.core.view.ContentSectionMediaView;
import edu.usu.sdl.openstorefront.core.view.EvaluationChecklistRecommendationView;
import edu.usu.sdl.openstorefront.core.view.EvaluationFilterParams;
import edu.usu.sdl.openstorefront.core.view.EvaluationInfoView;
import edu.usu.sdl.openstorefront.core.view.EvaluationView;
import edu.usu.sdl.openstorefront.core.view.EvaluationViewWrapper;
import edu.usu.sdl.openstorefront.core.view.statistic.EvaluationStatistic;
import edu.usu.sdl.openstorefront.core.view.statistic.WorkflowStats;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.lang.reflect.Field;
import java.net.URI;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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

	private static final Logger LOG = Logger.getLogger(EvaluationResource.class.getSimpleName());

	@GET
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(EvaluationViewWrapper.class)
	@APIDescription("Gets Evaluations")
	public Response getEvaluations(@BeanParam EvaluationFilterParams evaluationFilterParams)
	{
		ValidationResult validationResult = evaluationFilterParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		Evaluation evaluationExample = new Evaluation();
		evaluationExample.setActiveStatus(evaluationFilterParams.getStatus());
		if (StringUtils.isNotBlank(evaluationFilterParams.getWorkflowStatus())) {
			evaluationExample.setWorkflowStatus(evaluationFilterParams.getWorkflowStatus());
		}

		if (StringUtils.isNotBlank(evaluationFilterParams.getAssignedUser())) {
			evaluationExample.setAssignedUser(evaluationFilterParams.getAssignedUser());
		}

		if (StringUtils.isNotBlank(evaluationFilterParams.getAssignedGroup())) {
			evaluationExample.setAssignedGroup(evaluationFilterParams.getAssignedGroup());
		}

		if (evaluationFilterParams.getPublished() != null) {
			evaluationExample.setPublished(Convert.toBoolean(evaluationFilterParams.getPublished()));
		}

		if (StringUtils.isNotBlank(evaluationFilterParams.getTemplateId())) {
			evaluationExample.setTemplateId(evaluationFilterParams.getTemplateId());
		}

		Evaluation startExample = new Evaluation();
		startExample.setUpdateDts(evaluationFilterParams.getStart());

		Evaluation endExample = new Evaluation();
		endExample.setUpdateDts(evaluationFilterParams.getEnd());

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

		queryByExample.setAdditionalWhere(filterEngine.queryStandardRestriction());

		//get component ids
		if (StringUtils.isNotBlank(evaluationFilterParams.getComponentName())) {
			// If given, filter the search by name
			Component componentLikeExample = new Component();
			componentLikeExample.setName("%" + evaluationFilterParams.getComponentName().toLowerCase() + "%");

			QueryByExample componentQueryByExample = new QueryByExample(new Component());
			componentQueryByExample.setLikeExample(componentLikeExample);
			// Define Lookup Operation (ILIKE)
			componentQueryByExample.getLikeExampleOption().setMethod(GenerateStatementOption.METHOD_LOWER_CASE);

			List<Component> components = service.getPersistenceService().queryByExample(componentQueryByExample);
			// get list of ids
			List<String> ids = components.stream().map(x -> x.getComponentId()).collect(Collectors.toList());

			if (!ids.isEmpty()) {
				Evaluation idInExample = new Evaluation();
				idInExample.setComponentId(QueryByExample.STRING_FLAG);
				SpecialOperatorModel componentIdGroup = new SpecialOperatorModel(idInExample);
				componentIdGroup.getGenerateStatementOption().setParameterValues(ids);
				componentIdGroup.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_IN);

				Evaluation originIdInExample = new Evaluation();
				originIdInExample.setOriginComponentId(QueryByExample.STRING_FLAG);
				SpecialOperatorModel originIdGroup = new SpecialOperatorModel(originIdInExample);
				originIdGroup.getGenerateStatementOption().setParameterValues(ids);
				originIdGroup.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_IN);

				WhereClauseGroup group = new WhereClauseGroup();
				group.getStatementOption().setCondition(GenerateStatementOption.CONDITION_OR);
				group.getExtraWhereClause().add(componentIdGroup);
				group.getExtraWhereClause().add(originIdGroup);
				queryByExample.getExtraWhereCauses().add(group);
			}
		}

		//get Evaluation Template ids
		if (StringUtils.isNotBlank(evaluationFilterParams.getChecklistTemplateId())) {
			// If given, filter the search by name
			EvaluationTemplate templateExample = new EvaluationTemplate();
			templateExample.setChecklistTemplateId(evaluationFilterParams.getChecklistTemplateId());

			List<EvaluationTemplate> templates = templateExample.findByExample();
			// get list of ids
			List<String> ids = templates.stream().map(x -> x.getTemplateId()).collect(Collectors.toList());

			if (!ids.isEmpty()) {

				Evaluation idInExample = new Evaluation();
				idInExample.setTemplateId(QueryByExample.STRING_FLAG);
				SpecialOperatorModel templateIdGroup = new SpecialOperatorModel(idInExample);
				templateIdGroup.getGenerateStatementOption().setParameterValues(ids);
				templateIdGroup.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_IN);

				queryByExample.getExtraWhereCauses().add(templateIdGroup);
			}
		}

		Evaluation evaluationSortExample = new Evaluation();
		Field sortField = ReflectionUtil.getField(evaluationSortExample, evaluationFilterParams.getSortField());

		if (sortField != null) {

			queryByExample.setMaxResults(evaluationFilterParams.getMax());
			queryByExample.setFirstResult(evaluationFilterParams.getOffset());
			queryByExample.setSortDirection(evaluationFilterParams.getSortOrder());
			BeanUtil.setPropertyValue(sortField.getName(), evaluationSortExample, QueryByExample.getFlagForType(sortField.getType()));
			queryByExample.setOrderBy(evaluationSortExample);
		}

		List<Evaluation> evaluations = service.getPersistenceService().queryByExample(queryByExample);
		List<EvaluationView> views = EvaluationView.toView(evaluations);

		if (sortField == null) {
			views = evaluationFilterParams.filter(views);
		}

		EvaluationViewWrapper evaluationViewWrapper = new EvaluationViewWrapper();
		evaluationViewWrapper.getData().addAll(views);
		evaluationViewWrapper.setTotalNumber(service.getPersistenceService().countByExample(queryByExample));

		return sendSingleEntityResponse(evaluationViewWrapper);
	}

	@GET
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_READ)
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
		evaluation = filterEngine.filter(evaluation);
		if (evaluation != null) {
			return sendSingleEntityResponse(EvaluationView.toView(evaluation));
		} else {
			return sendSingleEntityResponse(evaluation);
		}
	}

	@GET
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentDetailView.class)
	@APIDescription("Get component view (including evals) for published and current evaluation (whether published or not)")
	@Path("/{evaluationId}/componentdetails")
	public Response getEvaluationComponentDetails(
			@PathParam("evaluationId") String evaluationId
	)
	{
		String componentId = service.getEvaluationService().getEvaluation(evaluationId).getEvaluation().getComponentId();
		Component componentExample = new Component();
		componentExample.setComponentId(componentId);
		List<Component> components = componentExample.findByExample();

		if (components.isEmpty()) {
			componentId = service.getEvaluationService().getEvaluation(evaluationId).getEvaluation().getOriginComponentId();
		}

		ComponentDetailView componentDetail = service.getComponentService().getComponentDetails(componentId, evaluationId);

		if (componentDetail != null) {
			return sendSingleEntityResponse(componentDetail);
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@GET
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("True if there has been a change to the template, that was not updated in the evaluation; otherwise False")
	@Path("/{evaluationId}/checkTemplateUpdate")
	public String checkTemplateUpdate(
			@PathParam("evaluationId") String evaluationId
	)
	{
		Evaluation evaluation = new Evaluation();
		evaluation.setEvaluationId(evaluationId);
		evaluation = evaluation.find();
		Boolean result = evaluation != null && evaluation.getTemplateUpdatePending() != null && evaluation.getTemplateUpdatePending();
		return "{ \"result\": " + result.toString() + " }";
	}

	@GET
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(EvaluationStatistic.class)
	@APIDescription("Get Evaluation statistics")
	@Path("/statistics")
	public Response getEvaluationStats(
			@QueryParam("assignedUser") String assignedUser,
			@QueryParam("assignedGroup") String assignedGroup
	)
	{
		EvaluationStatistic evaluationStatistic = new EvaluationStatistic();

		List<WorkflowStatus> workflowStatuses = service.getLookupService().findLookup(WorkflowStatus.class);
		Map<String, WorkflowStats> statusMap = new HashMap<>();
		for (WorkflowStatus status : workflowStatuses) {
			WorkflowStats stat = new WorkflowStats();
			stat.setStatus(status.getCode());
			stat.setStatusLabel(status.getDescription());
			stat.setStatusOrder(status.getSortOrder());

			statusMap.put(status.getCode(), stat);
		}

		Evaluation evaluationExample = new Evaluation();
		evaluationExample.setActiveStatus(Evaluation.ACTIVE_STATUS);
		if (StringUtils.isNotBlank(assignedUser)) {
			evaluationExample.setAssignedUser(assignedUser);
		}
		if (StringUtils.isNotBlank(assignedGroup)) {
			evaluationExample.setAssignedGroup(assignedGroup);
		}

		List<Evaluation> evaluations = evaluationExample.findByExample();
		for (Evaluation evaluation : evaluations) {
			if (evaluation.getPublished()) {
				evaluationStatistic.setPublished(evaluationStatistic.getPublished() + 1);
			} else {
				evaluationStatistic.setUnpublished(evaluationStatistic.getUnpublished() + 1);

				WorkflowStats stat = statusMap.get(evaluation.getWorkflowStatus());
				if (stat != null) {
					stat.setCount(stat.getCount() + 1);
				}
			}
		}

		evaluationStatistic.getStatusStats().addAll(statusMap.values());
		evaluationStatistic.getStatusStats().sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, WorkflowStats.FIELD_STATUSORDER));

		return sendSingleEntityResponse(evaluationStatistic);
	}

	@GET
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_READ)
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

	@GET
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(EvaluationInfoView.class)
	@APIDescription("Gets an evaluation information status")
	@Path("/{evaluationId}/info")
	public Response getEvaluationInfo(
			@PathParam("evaluationId") String evaluationId
	)
	{
		EvaluationInfoView evaluationInfoView = null;
		EvaluationAll evaluationAll = service.getEvaluationService().getEvaluation(evaluationId);
		if (evaluationAll != null) {
			evaluationInfoView = new EvaluationInfoView();
			evaluationInfoView.setEvaluationId(evaluationId);
			evaluationInfoView.setLastChangeDate(evaluationAll.calcLastChangeDate());
			evaluationInfoView.setProgessPercent(evaluationAll.calcProgress());
		}

		return sendSingleEntityResponse(evaluationInfoView);
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_MANAGEMENT_CREATE)
	@APIDescription("Creates an evaluation from template ")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Evaluation.class)
	public Response createEvaluation(Evaluation evaluation)
	{
		//optional
		evaluation.setAllowNewSections(Convert.toBoolean(evaluation.getAllowNewSections()));

		//Not currently used
		evaluation.setAllowNewSubSections(Boolean.FALSE);

		ValidationResult validationResult = evaluation.validate();
		if (validationResult.valid()) {
			evaluation = service.getEvaluationService().createEvaluationFromTemplate(evaluation);

			return Response.created(URI.create("v1/resource/evaluation/" + evaluation.getEvaluationId())).entity(evaluation).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_MANAGEMENT_CREATE)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Copies an full evaluation putting the new evaluation in a pending state.")
	@DataType(Evaluation.class)
	@Path("/{evaluationId}/copy")
	public Response copyEvaluation(
			@PathParam("evaluationId") String evaluationId
	)
	{
		Evaluation evaluationExisting = new Evaluation();
		evaluationExisting.setEvaluationId(evaluationId);
		evaluationExisting = evaluationExisting.find();
		if (evaluationExisting != null) {
			String newEvaluationId = service.getEvaluationService().copyEvaluation(evaluationId);

			Evaluation newEvaluation = new Evaluation();
			newEvaluation.setEvaluationId(newEvaluationId);
			newEvaluation = newEvaluation.find();
			return Response.created(URI.create("v1/resource/evaluation/" + newEvaluation.getEvaluationId())).entity(newEvaluation).build();
		} else {
			return sendSingleEntityResponse(evaluationExisting);
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_PUBLISH_SUMMARY)
	@APIDescription("Approves the entry summary change request and approves the entry if not, approved")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Evaluation.class)
	@Path("/{evaluationId}/publishsummary")
	public Response approveEvaluationSummary(
			@PathParam("evaluationId") String evaluationId
	)
	{
		Evaluation evaluation = new Evaluation();
		evaluation.setEvaluationId(evaluationId);
		evaluation = evaluation.find();
		if (evaluation != null) {
			service.getEvaluationService().approveEvaluationSummary(evaluationId);
		}
		return sendSingleEntityResponse(evaluation);
	}

	@PUT
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_ASSIGN_USER)
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@APIDescription("Updates the assigned user for an evaluation.")
	@DataType(Evaluation.class)
	@Path("/{evaluationId}/assignuser/{username}")
	public Response updateEvaluationAssignedUser(
		@PathParam("evaluationId") String evaluationId,
		@PathParam("username") String username
	)
	{
		Evaluation evaluationExisting = new Evaluation();
		evaluationExisting.setEvaluationId(evaluationId);
		evaluationExisting = evaluationExisting.find();

		if (evaluationExisting != null) {

				evaluationExisting.setAssignedUser(username);
				evaluationExisting.populateBaseUpdateFields();
				evaluationExisting.save();

				return Response.ok(evaluationExisting).build();
		} else {
			return sendSingleEntityResponse(evaluationExisting);
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@APIDescription("Updates an evaluation; Only fields that user should update some field have may have additional restrictions.")
	@DataType(Evaluation.class)
	@Path("/{evaluationId}")
	public Response updateEvaluation(
			@PathParam("evaluationId") String evaluationId,
			Evaluation evaluation
	)
	{
		Evaluation evaluationExisting = new Evaluation();
		evaluationExisting.setEvaluationId(evaluationId);
		evaluationExisting = evaluationExisting.find();
		if (evaluationExisting != null) {

			evaluation.setEvaluationId(evaluationId);
			ValidationResult validationResult = evaluation.validate();
			if (validationResult.valid()) {

				evaluationExisting.setVersion(evaluation.getVersion());
				evaluationExisting.setWorkflowStatus(evaluation.getWorkflowStatus());
				evaluationExisting.setAssignedGroup(evaluation.getAssignedGroup());
				evaluationExisting.setDataSensitivity(evaluation.getDataSensitivity());
				evaluationExisting.setSecurityMarkingType(evaluation.getSecurityMarkingType());
				evaluationExisting.setAssignedUser(evaluation.getAssignedUser());
				evaluationExisting.populateBaseUpdateFields();
				evaluationExisting.save();

				return Response.ok(evaluationExisting).build();
			} else {
				return sendSingleEntityResponse(validationResult.toRestError());
			}
		} else {
			return sendSingleEntityResponse(evaluation);
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@APIDescription("Updates an evaluation; to reflect changes in the template. Unpublished only.")
	@DataType(Evaluation.class)
	@Path("/{evaluationId}/updateTemplate")
	public Response updateEvaluationTemplate(
			@PathParam("evaluationId") String evaluationId)
	{
		Evaluation evaluation = new Evaluation();
		evaluation.setEvaluationId(evaluationId);
		evaluation = evaluation.find();
		if (evaluation != null) {
			if (evaluation.getPublished()) {
				LOG.log(Level.WARNING, MessageFormat.format("Cannot update published evaluation: {0}", evaluation.getEvaluationId()));
				return Response.status(Response.Status.FORBIDDEN).build();
			} else {
				service.getEvaluationService().updateEvaluationToLatestTemplateVersion(evaluation);
				return Response.ok(evaluation).build();
			}
		} else {
			return sendSingleEntityResponse(evaluation);
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_UPDATE)
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
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_ALLOW_NEW_SECTIONS)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Toggles the allow new section flag")
	@Path("/{evaluationId}/allownewsections")
	public Response toggleAllowNewSectionEvaluation(
			@PathParam("evaluationId") String evaluationId
	)
	{
		Evaluation evaluation = new Evaluation();
		evaluation.setEvaluationId(evaluationId);
		evaluation = evaluation.find();
		if (evaluation != null) {

			if (evaluation.getAllowNewSections()) {
				evaluation.setAllowNewSections(Boolean.FALSE);
			} else {
				evaluation.setAllowNewSections(Boolean.TRUE);
			}
			evaluation.save();

			return Response.ok(evaluation).build();
		} else {
			return sendSingleEntityResponse(evaluation);
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_ALLOW_QUESTION_MANAGEMENT)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Toggles the question management flag")
	@Path("/{evaluationId}/allowquestionmanagement")
	public Response toggleAllowQuestionManagement(
			@PathParam("evaluationId") String evaluationId
	)
	{
		Evaluation evaluation = new Evaluation();
		evaluation.setEvaluationId(evaluationId);
		evaluation = evaluation.find();
		if (evaluation != null) {

			if (Convert.toBoolean(evaluation.getAllowQuestionManagement())) {
				evaluation.setAllowQuestionManagement(Boolean.FALSE);
			} else {
				evaluation.setAllowQuestionManagement(Boolean.TRUE);
			}
			evaluation.save();

			return Response.ok(evaluation).build();
		} else {
			return sendSingleEntityResponse(evaluation);
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TOGGLE_PUBLISH)
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
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TOGGLE_PUBLISH)
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
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_ACTIVATE)
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
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_MANAGEMENT_DELETE)
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

	@POST
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Adds a new section to an evaluation based on a section template. Evaluation must allow adding Sections")
	@DataType(ContentSectionAll.class)
	@Path("/{evaluationId}/sections/{sectionTemplateId}")
	public Response addSection(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("sectionTemplateId") String sectionTemplateId
	)
	{
		Evaluation evaluation = new Evaluation();
		evaluation.setEvaluationId(evaluationId);
		evaluation = evaluation.find();

		ContentSectionTemplate contentSectionTemplate = new ContentSectionTemplate();
		contentSectionTemplate.setTemplateId(sectionTemplateId);
		contentSectionTemplate = contentSectionTemplate.find();
		if (evaluation != null && contentSectionTemplate != null) {

			if (evaluation.getAllowNewSections()) {
				String sectionId = service.getContentSectionService().createSectionFromTemplate(Evaluation.class.getSimpleName(), evaluationId, sectionTemplateId);
				ContentSectionAll contentSectionAll = service.getContentSectionService().getContentSectionAll(sectionId, false);
				return Response.created(URI.create("v1/resource/evaluations/" + evaluationId + "/sections/" + sectionId)).entity(contentSectionAll).build();
			} else {
				return Response
						.status(Response.Status.FORBIDDEN)
						.type(MediaType.MEDIA_TYPE_WILDCARD)
						.entity("Evaluation doesn't allow adding sections")
						.build();
			}
		} else {
			return sendSingleEntityResponse(null);
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@APIDescription("Save a section and it's subsections")
	@DataType(ContentSectionAll.class)
	@Path("/{evaluationId}/sections/{sectionId}")
	public Response saveSection(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("sectionId") String sectionId,
			ContentSectionAll contentSectionAll
	)
	{
		ContentSection contentSection = new ContentSection();
		contentSection.setEntity(Evaluation.class.getSimpleName());
		contentSection.setEntityId(evaluationId);
		contentSection.setContentSectionId(sectionId);
		contentSection = contentSection.find();
		if (contentSection != null) {
			contentSectionAll.getSection().setContentSectionId(sectionId);
			contentSectionAll.getSection().setEntity(Evaluation.class.getSimpleName());
			contentSectionAll.getSection().setEntityId(evaluationId);

			service.getContentSectionService().saveAll(contentSectionAll);

			//reload sections to get updated changes from DB
			ContentSection dbSection = new ContentSection();
			dbSection.setContentSectionId(sectionId);

			ContentSubSection contentSubSectionExample = new ContentSubSection();
			contentSubSectionExample.setContentSectionId(sectionId);

			contentSectionAll.setSection(dbSection.find());
			contentSectionAll.setSubsections(contentSubSectionExample.findByExample());
			return Response.ok(contentSectionAll).build();
		} else {
			return sendSingleEntityResponse(contentSection);
		}
	}

	@DELETE
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_UPDATE)
	@Produces({MediaType.WILDCARD})
	@APIDescription("Deletes a section and it's subsections; Evaluation must allow for adding sections")
	@DataType(ContentSectionAll.class)
	@Path("/{evaluationId}/sections/{sectionId}")
	public Response deleteSection(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("sectionId") String sectionId
	)
	{
		Evaluation evaluation = new Evaluation();
		evaluation.setEvaluationId(evaluationId);
		evaluation = evaluation.find();
		if (evaluation != null) {
			if (evaluation.getAllowNewSections()) {
				service.getContentSectionService().deleteContentSection(sectionId);
				return Response.status(Response.Status.NO_CONTENT).build();
			} else {
				return Response
						.status(Response.Status.FORBIDDEN)
						.type(MediaType.MEDIA_TYPE_WILDCARD)
						.entity("Evaluation doesn't allow adding sections")
						.build();
			}
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	//remove section
	//add sub section to section
	//remove sub section to section
	@GET
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ContentSectionMediaView.class)
	@APIDescription("Gets media for a section")
	@Path("/{evaluationId}/sections/{sectionId}/media")
	public Response getSectionMedia(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("sectionId") String sectionId
	)
	{
		ContentSection contentSection = new ContentSection();
		contentSection.setEntity(Evaluation.class.getSimpleName());
		contentSection.setEntityId(evaluationId);
		contentSection.setContentSectionId(sectionId);
		contentSection = contentSection.find();
		if (contentSection != null) {

			ContentSectionMedia contentSectionMedia = new ContentSectionMedia();
			contentSectionMedia.setContentSectionId(sectionId);
			List<ContentSectionMedia> media = contentSectionMedia.findByExample();
			GenericEntity<List<ContentSectionMediaView>> mediaEntity = new GenericEntity<List<ContentSectionMediaView>>(ContentSectionMediaView.toView(media))
			{
			};
			return sendSingleEntityResponse(mediaEntity);
		} else {
			return sendSingleEntityResponse(contentSection);
		}
	}

	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(ContentSectionMedia.class)
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_UPDATE)
	@APIDescription("Update the flags on the section media. To add media post to MediaUpload.action?UploadSectionMedia&contentSectionMedia...&file")
	@Path("/{evaluationId}/sections/{sectionId}/media/{sectionMediaId}")
	public Response updateSectionMedia(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("sectionId") String sectionId,
			@PathParam("sectionMediaId") String sectionMediaId,
			ContentSectionMedia sectionMedia
	)
	{
		ContentSectionMedia contentSectionMedia = null;

		ContentSection contentSection = new ContentSection();
		contentSection.setEntity(Evaluation.class.getSimpleName());
		contentSection.setEntityId(evaluationId);
		contentSection.setContentSectionId(sectionId);
		contentSection = contentSection.find();
		if (contentSection != null) {
			contentSectionMedia = new ContentSectionMedia();
			contentSectionMedia.setContentSectionId(sectionId);
			contentSectionMedia.setContentSectionMediaId(sectionMediaId);
			contentSectionMedia = contentSectionMedia.find();
			if (contentSectionMedia != null) {
				if (StringUtils.isNotBlank(sectionMedia.getMediaTypeCode())) {
					contentSectionMedia.setMediaTypeCode(sectionMedia.getMediaTypeCode());
				}
				contentSectionMedia.setPrivateMedia(Convert.toBoolean(sectionMedia.getPrivateMedia()));
				contentSectionMedia.setCaption(sectionMedia.getCaption());
				contentSectionMedia.save();
			}
		}
		return sendSingleEntityResponse(contentSectionMedia);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_UPDATE)
	@APIDescription("Deletes media for a section")
	@Path("/{evaluationId}/sections/{sectionId}/media/{sectionMediaId}")
	public void deleteSectionMedia(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("sectionId") String sectionId,
			@PathParam("sectionMediaId") String sectionMediaId
	)
	{
		ContentSection contentSection = new ContentSection();
		contentSection.setEntity(Evaluation.class.getSimpleName());
		contentSection.setEntityId(evaluationId);
		contentSection.setContentSectionId(sectionId);
		contentSection = contentSection.find();
		if (contentSection != null) {
			ContentSectionMedia contentSectionMedia = new ContentSectionMedia();
			contentSectionMedia.setContentSectionId(sectionId);
			contentSectionMedia.setContentSectionMediaId(sectionMediaId);
			contentSectionMedia = contentSectionMedia.find();
			if (contentSectionMedia != null) {
				service.getContentSectionService().deleteMedia(sectionMediaId);
			}
		}
	}

	@GET
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_READ)
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
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_READ)
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
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_UPDATE)
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
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_UPDATE)
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
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_UPDATE)
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
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Delete a comment. (must be owner or admin)")
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

			response = ownerCheck(evaluationComment, SecurityPermission.ADMIN_EVALUATION_DELETE_COMMENT);
			if (response == null) {
				evaluationComment.delete();
				response = Response.noContent().build();
			}
		}
		return response;
	}

	@GET
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ContentSection.class)
	@APIDescription("Get active sections for an evaluation")
	@Path("/{evaluationId}/sections")
	public Response getEvaluationSections(
			@PathParam("evaluationId") String evaluationId
	)
	{
		ContentSection section = new ContentSection();
		section.setEntity(Evaluation.class.getSimpleName());
		section.setEntityId(evaluationId);
		section.setActiveStatus(ContentSection.ACTIVE_STATUS);

		List<ContentSection> sections = section.findByExample();
		GenericEntity<List<ContentSection>> sectionEntity = new GenericEntity<List<ContentSection>>(sections)
		{
		};
		return sendSingleEntityResponse(sectionEntity);
	}

	@GET
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ContentSection.class)
	@APIDescription("Gets a section for an evaluation")
	@Path("/{evaluationId}/sections/{sectionId}")
	public Response getEvaluationSection(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("sectionId") String sectionId
	)
	{
		ContentSection section = new ContentSection();
		section.setEntity(Evaluation.class.getSimpleName());
		section.setEntityId(evaluationId);
		section.setContentSectionId(sectionId);
		section = section.find();
		return sendSingleEntityResponse(section);
	}

	@GET
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ContentSectionAll.class)
	@APIDescription("Gets a section and subsections for an evaluation")
	@Path("/{evaluationId}/sections/{sectionId}/details")
	public Response getEvaluationSectionDeatils(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("sectionId") String sectionId
	)
	{
		ContentSectionAll contentSectionAll = null;

		ContentSection section = new ContentSection();
		section.setEntity(Evaluation.class.getSimpleName());
		section.setEntityId(evaluationId);
		section.setContentSectionId(sectionId);
		section = section.find();
		if (section != null) {
			contentSectionAll = service.getContentSectionService().getContentSectionAll(sectionId, false);
		}
		return sendSingleEntityResponse(contentSectionAll);
	}

	@GET
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(EvaluationChecklist.class)
	@APIDescription("Get a checklist for an evaluation")
	@Path("/{evaluationId}/checklist/{checklistId}")
	public Response getEvaluationChecklist(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("checklistId") String checklistId
	)
	{
		EvaluationChecklist checklist = new EvaluationChecklist();
		checklist.setEvaluationId(evaluationId);
		checklist.setChecklistId(checklistId);
		checklist = checklist.find();

		return sendSingleEntityResponse(checklist);
	}

	@PUT
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_UPDATE)
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
				evaluationChecklist.setEvaluationId(evaluationId);
				evaluationChecklist.setChecklistId(checklistId);
				evaluationChecklist = evaluationChecklist.save();
				return Response.ok(evaluationChecklist).build();
			} else {
				return Response.ok(result.toRestError()).build();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@PUT
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@APIDescription("Add/Remove (inactivate) questions from evaluation checklist. Evaluation must be marked to allow changing questions.")
	@Path("/{evaluationId}/checklist/{checklistId}/syncquestions")
	public Response syncChecklistQuestions(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("checklistId") String checklistId,
			@DataType(String.class) List<String> questionIdsToKeep
	)
	{
		EvaluationChecklist existing = new EvaluationChecklist();
		existing.setEvaluationId(evaluationId);
		existing.setChecklistId(checklistId);
		existing = existing.find();
		if (existing != null) {

			Evaluation evaluation = new Evaluation();
			evaluation.setEvaluationId(evaluationId);
			evaluation = evaluation.find();
			if (evaluation != null && Convert.toBoolean(evaluation.getAllowQuestionManagement())) {
				service.getChecklistService().syncChecklistQuestions(checklistId, questionIdsToKeep);
				return Response.ok().build();
			} else {
				Response.status(Response.Status.FORBIDDEN).build();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@GET
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ChecklistResponseView.class)
	@APIDescription("Gets checklist responses for an evaluation")
	@Path("/{evaluationId}/checklist/{checklistId}/responses")
	public List<ChecklistResponseView> getChecklistResponse(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("checklistId") String checklistId
	)
	{
		EvaluationChecklistResponse responseExample = new EvaluationChecklistResponse();
		responseExample.setChecklistId(checklistId);
		responseExample.setActiveStatus(EvaluationChecklistResponse.ACTIVE_STATUS);

		List<ChecklistResponseView> views = ChecklistResponseView.toView(responseExample.findByExample());
		return views;
	}

	@GET
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ChecklistResponseView.class)
	@APIDescription("Get's a checklist response for an evaluation")
	@Path("/{evaluationId}/checklist/{checklistId}/responses/{responseId}")
	public Response getChecklistResponse(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("checklistId") String checklistId,
			@PathParam("responseId") String responseId
	)
	{
		EvaluationChecklistResponse responseExample = new EvaluationChecklistResponse();
		responseExample.setChecklistId(checklistId);
		responseExample.setResponseId(responseId);

		EvaluationChecklistResponse checklistResponse = responseExample.find();

		return sendSingleEntityResponse(ChecklistResponseView.toView(checklistResponse));
	}

	@PUT
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(ChecklistResponseView.class)
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
					checklistResponse.setQuestionId(existing.getQuestionId());
					checklistResponse = checklistResponse.save();
					return Response.ok(ChecklistResponseView.toView(checklistResponse)).build();
				} else {
					return Response.ok(result.toRestError()).build();
				}
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@GET
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(EvaluationChecklistRecommendationView.class)
	@APIDescription("Adds a checklist recommendation for an evaluation")
	@Path("/{evaluationId}/checklist/{checklistId}/recommendations")
	public List<EvaluationChecklistRecommendationView> addChecklistRecommendation(
			@PathParam("evaluationId") String evaluationId,
			@PathParam("checklistId") String checklistId
	)
	{
		EvaluationChecklistRecommendation recommendationExample = new EvaluationChecklistRecommendation();
		recommendationExample.setChecklistId(checklistId);
		recommendationExample.setActiveStatus(EvaluationChecklistRecommendation.ACTIVE_STATUS);

		List<EvaluationChecklistRecommendation> recommendations = recommendationExample.findByExample();
		return EvaluationChecklistRecommendationView.toView(recommendations);
	}

	@POST
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_UPDATE)
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
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_UPDATE)
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
	@RequireSecurity(SecurityPermission.USER_EVALUATIONS_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Delete a recommendation.")
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
