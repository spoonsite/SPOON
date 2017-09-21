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

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.ChecklistQuestion;
import edu.usu.sdl.openstorefront.core.entity.ChecklistTemplate;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.Tag;
import edu.usu.sdl.openstorefront.core.view.CheckQuestionFilterParams;
import edu.usu.sdl.openstorefront.core.view.ChecklistQuestionView;
import edu.usu.sdl.openstorefront.core.view.ChecklistQuestionWrapper;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.lang.reflect.Field;
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
@APIDescription("Provides access to evaluations resources")
@Path("v1/resource/checklistquestions")
public class ChecklistQuestionResource
		extends BaseResource
{

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ChecklistQuestionWrapper.class)
	@APIDescription("Gets Checklist questions")
	public Response getQuestions(@BeanParam CheckQuestionFilterParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ChecklistQuestion checklistExample = new ChecklistQuestion();
		checklistExample.setActiveStatus(filterQueryParams.getStatus());

		ChecklistQuestion checklistStartExample = new ChecklistQuestion();
		checklistStartExample.setUpdateDts(filterQueryParams.getStart());

		ChecklistQuestion checklistEndExample = new ChecklistQuestion();
		checklistEndExample.setUpdateDts(filterQueryParams.getEnd());

		QueryByExample queryByExample = new QueryByExample(checklistExample);

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(checklistStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(checklistEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		if (!filterQueryParams.getTags().isEmpty()) {
			queryByExample.setAdditionalWhere(" tags in :tagsParams ");
			queryByExample.getExtraParamMapping().put("tagsParams", filterQueryParams.getTags());
		}

		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());
		queryByExample.setSortDirection(filterQueryParams.getSortOrder());

		ChecklistQuestion checklistQuestionSortExample = new ChecklistQuestion();
		Field sortField = ReflectionUtil.getField(checklistQuestionSortExample, filterQueryParams.getSortField());
		if (sortField != null) {
			BeanUtil.setPropertyValue(sortField.getName(), checklistQuestionSortExample, QueryByExample.getFlagForType(sortField.getType()));
			queryByExample.setOrderBy(checklistQuestionSortExample);
		}

		List<ChecklistQuestion> questions = service.getPersistenceService().queryByExample(queryByExample);

		ChecklistQuestionWrapper checklistQuestionWrapper = new ChecklistQuestionWrapper();
		checklistQuestionWrapper.getData().addAll(ChecklistQuestionView.toView(questions));
		checklistQuestionWrapper.setTotalNumber(service.getPersistenceService().countByExample(queryByExample));

		return sendSingleEntityResponse(checklistQuestionWrapper);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ChecklistQuestionView.class)
	@APIDescription("Gets a checklist question")
	@Path("/{questionId}")
	public Response getQuestion(
			@PathParam("questionId") String questionId
	)
	{
		ChecklistQuestion checklistQuestion = new ChecklistQuestion();
		checklistQuestion.setQuestionId(questionId);
		checklistQuestion = checklistQuestion.find();
		return sendSingleEntityResponse(ChecklistQuestionView.toView(checklistQuestion));
	}

	@GET
	@APIDescription("Exports questions in JSON format.")
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/export")
	public Response exportQuestions(
			@QueryParam("status") String status
	)
	{
		ChecklistQuestion checklistExample = new ChecklistQuestion();
		if (StringUtils.isNotBlank(status)) {
			checklistExample.setActiveStatus(status);
		} else {
			checklistExample.setActiveStatus(ChecklistQuestion.ACTIVE_STATUS);
		}

		List<ChecklistQuestion> checklistQuestions = checklistExample.findByExample();

		String data;
		try {
			data = StringProcessor.defaultObjectMapper().writeValueAsString(checklistQuestions);
		} catch (JsonProcessingException ex) {
			throw new OpenStorefrontRuntimeException("Unable to export checklist.  Unable able to generate JSON.", ex);
		}

		Response.ResponseBuilder response = Response.ok(data);
		response.header("Content-Type", MediaType.APPLICATION_JSON);
		response.header("Content-Disposition", "attachment; filename=\"checklistquestions.json\"");
		return response.build();
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION)
	@APIDescription("Creates a checklist question")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ChecklistQuestion.class)
	public Response createChecklistQuestion(ChecklistQuestion checklistQuestion)
	{
		checklistQuestion.setQuestionId(null);
		return saveChecklistQuestion(checklistQuestion, true);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION)
	@APIDescription("Updates a checklist question")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ChecklistTemplate.class)
	@Path("/{questionId}")
	public Response updateChecklistQuestion(
			@PathParam("questionId") String questionId,
			ChecklistQuestion checklistQuestion)
	{
		ChecklistQuestion existing = new ChecklistQuestion();
		existing.setQuestionId(questionId);
		existing = existing.find();
		if (existing == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		checklistQuestion.setQuestionId(questionId);
		return saveChecklistQuestion(checklistQuestion, false);
	}

	private Response saveChecklistQuestion(ChecklistQuestion checklistQuestion, boolean create)
	{
		ValidationResult validationResult = checklistQuestion.validate();

		//check uniqueness
		boolean checkUnique = true;
		if (checklistQuestion.getQuestionId() != null) {
			ChecklistQuestion existing = new ChecklistQuestion();
			existing.setQuestionId(checklistQuestion.getQuestionId());
			existing = existing.find();
			if (existing != null
					&& existing.getQid().equals(checklistQuestion.getQid())) {
				checkUnique = false;
			}
		}

		if (checkUnique) {
			ChecklistQuestion dupCheck = new ChecklistQuestion();
			dupCheck.setQid(checklistQuestion.getQid());
			dupCheck = dupCheck.find();
			if (dupCheck != null) {
				RuleResult ruleResult = new RuleResult();
				ruleResult.setEntityClassName(ChecklistQuestion.class.getSimpleName());
				ruleResult.setFieldName(ChecklistQuestion.FIELD_QID);
				ruleResult.setMessage("There is already a question with qid");
				ruleResult.setInvalidData(checklistQuestion.getQid());
				ruleResult.setValidationRule("Unqiue qid check");
				validationResult.getRuleResults().add(ruleResult);
			}
		}

		if (validationResult.valid()) {
			checklistQuestion = checklistQuestion.save();

			if (create) {
				return Response.created(URI.create("v1/resource/checklistquestions/" + checklistQuestion.getQuestionId())).entity(checklistQuestion).build();
			} else {
				return Response.ok(checklistQuestion).build();
			}
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Activates a Question")
	@Path("/{questionId}/activate")
	public Response activateChecklistQuestion(
			@PathParam("questionId") String questionId
	)
	{
		return updateStatus(questionId, ChecklistTemplate.ACTIVE_STATUS);
	}

	private Response updateStatus(String questionId, String status)
	{
		ChecklistQuestion checklistQuestion = new ChecklistQuestion();
		checklistQuestion.setQuestionId(questionId);
		checklistQuestion = checklistQuestion.find();
		if (checklistQuestion != null) {
			checklistQuestion.setActiveStatus(status);
			checklistQuestion.save();
		}
		return sendSingleEntityResponse(checklistQuestion);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION)
	@Produces({MediaType.TEXT_PLAIN})
	@APIDescription("Check to see if question is in use; returns true if in use or no content if not.")
	@Path("/{questionId}/inuse")
	public Response questionInUse(
			@PathParam("questionId") String questionId
	)
	{
		Boolean inUse;
		ChecklistQuestion checklistQuestion = new ChecklistQuestion();
		checklistQuestion.setQuestionId(questionId);
		checklistQuestion = checklistQuestion.find();
		if (checklistQuestion != null) {
			inUse = service.getChecklistService().isQuestionBeingUsed(questionId);
			return Response.ok(inUse.toString()).build();

		} else {
			return sendSingleEntityResponse(null);
		}
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Tag.class)
	@APIDescription("Gets all distinct tags across questions")
	@Path("/tags")
	public Response getQuestionTags()
	{
		ChecklistQuestion checklistQuestionExample = new ChecklistQuestion();
		List<ChecklistQuestion> questions = checklistQuestionExample.findByExample();
		Map<String, Tag> distinctTags = new HashMap<>();
		for (ChecklistQuestion checklistQuestion : questions) {
			if (checklistQuestion.getTags() != null) {
				for (Tag tag : checklistQuestion.getTags()) {
					if (distinctTags.containsKey(tag.getTag()) == false) {
						distinctTags.put(tag.getTag(), tag);
					}
				}
			}
		}

		GenericEntity<List<Tag>> entity = new GenericEntity<List<Tag>>(new ArrayList<>(distinctTags.values()))
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST_QUESTION)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Inactivates a question or remove only if not in use")
	@Path("/{questionId}")
	public Response deleteChecklistQuestion(
			@PathParam("questionId") String questionId,
			@QueryParam("force") boolean force
	)
	{
		if (force) {
			service.getChecklistService().deleteQuestion(questionId);
			return Response.noContent().build();
		} else {
			return updateStatus(questionId, ChecklistTemplate.INACTIVE_STATUS);
		}
	}
}
