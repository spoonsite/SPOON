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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.api.ChecklistService;
import edu.usu.sdl.openstorefront.core.entity.ChecklistQuestion;
import edu.usu.sdl.openstorefront.core.entity.ChecklistTemplate;
import edu.usu.sdl.openstorefront.core.entity.ChecklistTemplateQuestion;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklist;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistResponse;
import edu.usu.sdl.openstorefront.core.entity.WorkflowStatus;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ChecklistServiceImpl
		extends ServiceProxy
		implements ChecklistService
{

	private static final Logger LOG = Logger.getLogger(ChecklistServiceImpl.class.getName());

	@Override
	public ChecklistQuestion findQuestion(String questionId)
	{
		Objects.requireNonNull(questionId);

		ChecklistQuestion found = null;

		Element element = OSFCacheManager.getChecklistQuestionCache().get(questionId);
		if (element != null) {
			found = (ChecklistQuestion) element.getObjectValue();
		} else {

			ChecklistQuestion checklistQuestion = new ChecklistQuestion();
			List<ChecklistQuestion> questions = checklistQuestion.findByExample();
			for (ChecklistQuestion question : questions) {
				if (question.getQuestionId().equals(questionId)) {
					found = question;
				}

				element = new Element(question.getQuestionId(), question);
				OSFCacheManager.getChecklistQuestionCache().put(element);
			}
		}

		return found;
	}

	@Override
	public void saveChecklistQuestion(List<ChecklistQuestion> questions)
	{
		for (ChecklistQuestion question : questions) {
			saveChecklistQuestion(question);
		}
	}

	@Override
	public ChecklistQuestion saveChecklistQuestion(ChecklistQuestion checklistQuestion)
	{
		Objects.requireNonNull(checklistQuestion);
		Objects.requireNonNull(checklistQuestion.getQid());

		ChecklistQuestion questionExisting = persistenceService.findById(ChecklistQuestion.class, checklistQuestion.getQuestionId());
		if (questionExisting == null) {
			ChecklistQuestion dupCheckQuestion = new ChecklistQuestion();
			dupCheckQuestion.setQid(checklistQuestion.getQid());
			questionExisting = dupCheckQuestion.findProxy();
		}

		if (questionExisting != null) {
			questionExisting.updateFields(checklistQuestion);
			questionExisting = persistenceService.persist(questionExisting);
		} else {
			if (StringUtils.isBlank(checklistQuestion.getQuestionId())) {
				checklistQuestion.setQuestionId(persistenceService.generateId());
			}
			checklistQuestion.populateBaseCreateFields();
			questionExisting = persistenceService.persist(checklistQuestion);
		}

		OSFCacheManager.getChecklistQuestionCache().removeAll();
		return questionExisting;
	}

	@Override
	public ChecklistTemplate copyChecklistTemplate(String checklistTemplateId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean isQuestionBeingUsed(String questionId)
	{
		Objects.requireNonNull(questionId);
		boolean inUse = false;

		ChecklistTemplate checklistTemplateExample = new ChecklistTemplate();

		//Check all templates
		List<ChecklistTemplate> checklistTemplates = checklistTemplateExample.findByExample();
		for (ChecklistTemplate checklistTemplate : checklistTemplates) {
			for (ChecklistTemplateQuestion question : checklistTemplate.getQuestions()) {
				if (questionId.equals(question.getQuestionId())) {
					inUse = true;
					break;
				}
			}
			if (inUse) {
				break;
			}
		}

		if (!inUse) {
			EvaluationChecklistResponse response = new EvaluationChecklistResponse();
			response.setQuestionId(questionId);

			long count = persistenceService.countByExample(response);
			if (count > 0) {
				inUse = true;
			}
		}

		return inUse;
	}

	@Override
	public void deleteQuestion(String questionId)
	{
		if (isQuestionBeingUsed(questionId)) {
			throw new OpenStorefrontRuntimeException("Unable to remove question.", "Remove all ties to the quesiton (templates, evaluation responses)");
		} else {
			ChecklistQuestion questionExisting = persistenceService.findById(ChecklistQuestion.class, questionId);
			if (questionExisting != null) {
				persistenceService.delete(questionExisting);
				OSFCacheManager.getChecklistQuestionCache().remove(questionExisting.getQuestionId());
			}
		}
	}

	@Override
	public boolean isChecklistTemplateBeingUsed(String checklistTemplateId)
	{
		Objects.requireNonNull(checklistTemplateId);
		boolean inUse = false;

		EvaluationChecklist evaluationChecklist = new EvaluationChecklist();
		evaluationChecklist.setChecklistTemplateId(checklistTemplateId);
		List<EvaluationChecklist> checkLists = evaluationChecklist.findByExample();
		if (!checkLists.isEmpty()) {
			inUse = true;
		}
		return inUse;
	}

	@Override
	public void deleteChecklistTemplate(String checklistTemplateId)
	{
		if (isChecklistTemplateBeingUsed(checklistTemplateId)) {
			throw new OpenStorefrontRuntimeException("Unable to remove checklist template.", "Remove all ties to the template (evaluation checklists)");
		} else {
			ChecklistTemplate existing = persistenceService.findById(ChecklistTemplate.class, checklistTemplateId);
			if (existing != null) {
				persistenceService.delete(existing);
			}
		}
	}

	@Override
	public void syncChecklistQuestions(String checklistId, List<String> questionIdsToKeep)
	{
		Objects.requireNonNull(checklistId);
		Objects.requireNonNull(questionIdsToKeep);

		WorkflowStatus initialStatus = WorkflowStatus.initalStatus();
		if (initialStatus == null) {
			throw new OpenStorefrontRuntimeException("Unable to get initial workflow status", "Add at least one workflow status.");
		}

		Set<String> questionKeepSet = questionIdsToKeep.stream()
				.collect(Collectors.toSet());

		EvaluationChecklistResponse checklistResponseExample = new EvaluationChecklistResponse();
		checklistResponseExample.setChecklistId(checklistId);

		List<EvaluationChecklistResponse> responsesInChecklist = checklistResponseExample.findByExampleProxy();

		Set<String> questionAlreadyThere = new HashSet<>();
		for (EvaluationChecklistResponse response : responsesInChecklist) {
			ChecklistQuestion question = findQuestion(response.getQuestionId());
			if (question == null) {
				question = new ChecklistQuestion();
				question.setQid("N/A Missing question");
			}

			if (questionKeepSet.contains(response.getQuestionId())) {
				if (EvaluationChecklistResponse.INACTIVE_STATUS.equals(response.getActiveStatus())) {
					getChangeLogService().logStatusChange(response, EvaluationChecklistResponse.ACTIVE_STATUS, "Question: " + question.getQid());

					response.setActiveStatus(EvaluationChecklistResponse.ACTIVE_STATUS);
					response.populateBaseUpdateFields();
					response.setUserAddRemoveFlg(Boolean.TRUE);
					persistenceService.persist(response);
				}
				questionAlreadyThere.add(response.getQuestionId());
			} else {
				//remove question
				getChangeLogService().logStatusChange(response, EvaluationChecklistResponse.INACTIVE_STATUS, "Question: " + question.getQid());

				response.setActiveStatus(EvaluationChecklistResponse.INACTIVE_STATUS);
				response.populateBaseUpdateFields();
				response.setUserAddRemoveFlg(Boolean.TRUE);
				persistenceService.persist(response);

			}
		}

		//look for questions to add
		for (String questionIdKeep : questionKeepSet) {
			if (questionAlreadyThere.contains(questionIdKeep) == false) {

				//make sure questions exist
				ChecklistQuestion question = findQuestion(questionIdKeep);
				if (question != null) {

					EvaluationChecklistResponse response = new EvaluationChecklistResponse();
					response.setChecklistId(checklistId);
					response.setResponseId(persistenceService.generateId());
					response.setQuestionId(questionIdKeep);
					response.setWorkflowStatus(initialStatus.getCode());
					response.populateBaseCreateFields();
					response.setUserAddRemoveFlg(Boolean.TRUE);
					persistenceService.persist(response);
					getChangeLogService().addEntityChange(response);
				} else {
					LOG.log(Level.WARNING, MessageFormat.format("Unable to find existing question to add new response. (skipping) QuestionId: {0}", questionIdKeep));
				}
			}
		}

	}

}
