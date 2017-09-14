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
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.api.EvaluationService;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.ChecklistQuestion;
import edu.usu.sdl.openstorefront.core.entity.ChecklistTemplate;
import edu.usu.sdl.openstorefront.core.entity.ChecklistTemplateQuestion;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ContentSection;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionMedia;
import edu.usu.sdl.openstorefront.core.entity.ContentSubSection;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklist;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistRecommendation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistResponse;
import edu.usu.sdl.openstorefront.core.entity.EvaluationSectionTemplate;
import edu.usu.sdl.openstorefront.core.entity.EvaluationTemplate;
import edu.usu.sdl.openstorefront.core.entity.WorkflowStatus;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.core.model.ChecklistAll;
import edu.usu.sdl.openstorefront.core.model.ContentSectionAll;
import edu.usu.sdl.openstorefront.core.model.EvaluationAll;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.view.ChecklistResponseView;
import edu.usu.sdl.openstorefront.core.view.EvaluationChecklistRecommendationView;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class EvaluationServiceImpl
		extends ServiceProxy
		implements EvaluationService
{

	private static final Logger LOG = Logger.getLogger(EvaluationServiceImpl.class.getName());

	@Override
	public String saveCheckListAll(ChecklistAll checklistAll)
	{
		Objects.requireNonNull(checklistAll);
		Objects.requireNonNull(checklistAll.getEvaluationChecklist());

		EvaluationChecklist evaluationChecklist = checklistAll.getEvaluationChecklist().save();

		EvaluationChecklistRecommendation recommendationDeleteExample = new EvaluationChecklistRecommendation();
		recommendationDeleteExample.setChecklistId(evaluationChecklist.getChecklistId());
		List<EvaluationChecklistRecommendation> recommendations = recommendationDeleteExample.findByExampleProxy();
		Map<String, List<EvaluationChecklistRecommendation>> existingRecs = recommendations.stream()
				.collect(Collectors.groupingBy(EvaluationChecklistRecommendation::getRecommendationId));

		for (EvaluationChecklistRecommendation recommendation : checklistAll.getRecommendations()) {

			if (recommendation instanceof EvaluationChecklistRecommendationView) {
				recommendation = ((EvaluationChecklistRecommendationView) recommendation).toRecommendation();
			}

			if (recommendation.getRecommendationId() != null && existingRecs.containsKey(recommendation.getRecommendationId())) {
				EvaluationChecklistRecommendation existing = existingRecs.get(recommendation.getRecommendationId()).get(0);
				existing.updateFields(recommendation);
				persistenceService.persist(existing);
			} else {
				recommendation.setChecklistId(evaluationChecklist.getChecklistId());
				if (StringUtils.isBlank(recommendation.getRecommendationId())) {
					recommendation.setRecommendationId(persistenceService.generateId());
				}
				recommendation.populateBaseCreateFields();
				persistenceService.persist(recommendation);
			}

		}

		//remove old responses replace with the new ones
		EvaluationChecklistResponse responseDeleteExample = new EvaluationChecklistResponse();
		responseDeleteExample.setChecklistId(evaluationChecklist.getChecklistId());
		List<EvaluationChecklistResponse> responses = responseDeleteExample.findByExampleProxy();
		Map<String, List<EvaluationChecklistResponse>> existingResponses = responses.stream()
				.collect(Collectors.groupingBy(EvaluationChecklistResponse::getResponseId));

		for (EvaluationChecklistResponse response : checklistAll.getResponses()) {

			if (response instanceof ChecklistResponseView) {
				ChecklistResponseView view = ((ChecklistResponseView) response);
				//find corrected question id base on the imported qid
				ChecklistQuestion question = new ChecklistQuestion();
				question.setQid(view.getQuestion().getQid());
				question = question.findProxy();

				response = view.toResponse();
				if (question != null) {
					response.setQuestionId(question.getQuestionId());
				}
			}

			if (response.getResponseId() != null && existingResponses.containsKey(response.getResponseId())) {
				EvaluationChecklistResponse existing = existingResponses.get(response.getResponseId()).get(0);
				existing.updateFields(response);
				persistenceService.persist(existing);
			} else {
				response.setChecklistId(evaluationChecklist.getChecklistId());
				if (StringUtils.isBlank(response.getResponseId())) {
					response.setResponseId(persistenceService.generateId());
				}
				response.populateBaseCreateFields();
				persistenceService.persist(response);
			}
		}

		return evaluationChecklist.getChecklistId();
	}

	@Override
	public ChecklistAll getChecklistAll(String checklistId)
	{
		return getChecklistAll(checklistId, false);
	}

	private ChecklistAll getChecklistAll(String checklistId, boolean publicInformationOnly)
	{
		Objects.requireNonNull(checklistId);

		ChecklistAll checklistAll = null;

		EvaluationChecklist evaluationChecklist = new EvaluationChecklist();
		evaluationChecklist.setChecklistId(checklistId);
		evaluationChecklist = evaluationChecklist.find();
		if (publicInformationOnly) {
			if (evaluationChecklist != null && Convert.toBoolean(evaluationChecklist.getPrivateChecklistFlg())) {
				evaluationChecklist = null;
			}
		}

		if (evaluationChecklist != null) {
			checklistAll = new ChecklistAll();
			checklistAll.setEvaluationChecklist(evaluationChecklist);

			if (publicInformationOnly) {
				if (Convert.toBoolean(evaluationChecklist.getPrivateSummaryFlg())) {
					evaluationChecklist.setSummary(null);
				}
			}

			EvaluationChecklistRecommendation recommendation = new EvaluationChecklistRecommendation();
			recommendation.setChecklistId(checklistId);
			recommendation.setActiveStatus(EvaluationChecklistRecommendation.ACTIVE_STATUS);
			List<EvaluationChecklistRecommendation> recommendations = recommendation.findByExample();
			if (publicInformationOnly) {
				recommendations = recommendations
						.stream()
						.filter(r -> Convert.toBoolean(r.getPrivateFlg()) == false)
						.collect(Collectors.toList());
			}
			checklistAll.getRecommendations().addAll(EvaluationChecklistRecommendationView.toView(recommendations));

			EvaluationChecklistResponse response = new EvaluationChecklistResponse();
			response.setChecklistId(checklistId);
			response.setActiveStatus(EvaluationChecklistResponse.ACTIVE_STATUS);
			List<EvaluationChecklistResponse> responses = response.findByExample();
			if (publicInformationOnly) {
				responses = responses
						.stream()
						.filter(r -> Convert.toBoolean(r.getPrivateFlg()) == false)
						.collect(Collectors.toList());
			}
			checklistAll.getResponses().addAll(ChecklistResponseView.toView(responses));

			//clear private notes
			if (publicInformationOnly) {
				for (EvaluationChecklistResponse evaluationChecklistResponse : checklistAll.getResponses()) {
					evaluationChecklistResponse.setPrivateNote(null);
				}
			}
		}
		return checklistAll;
	}

	@Override
	public String saveEvaluationAll(EvaluationAll evaluationAll)
	{
		Objects.requireNonNull(evaluationAll);
		Objects.requireNonNull(evaluationAll.getEvaluation());
		Objects.requireNonNull(evaluationAll.getCheckListAll());

		Evaluation savedEvalation = evaluationAll.getEvaluation().save();
		evaluationAll.getCheckListAll().getEvaluationChecklist().setEvaluationId(savedEvalation.getEvaluationId());
		saveCheckListAll(evaluationAll.getCheckListAll());
		for (ContentSectionAll contentSectionAll : evaluationAll.getContentSections()) {
			contentSectionAll.getSection().setEntityId(savedEvalation.getEvaluationId());
			contentSectionAll.getSection().setEntity(Evaluation.class.getSimpleName());
			getContentSectionService().saveAll(contentSectionAll);
		}

		return savedEvalation.getEvaluationId();
	}

	@Override
	public Evaluation createEvaluationFromTemplate(Evaluation evaluation)
	{
		Objects.requireNonNull(evaluation);
		Objects.requireNonNull(evaluation.getTemplateId());

		//create change request
		evaluation.setOriginComponentId(evaluation.getComponentId());
		Component changeRequest = getComponentService().createPendingChangeComponent(evaluation.getComponentId());
		evaluation.setComponentId(changeRequest.getComponentId());
		if (StringUtils.isBlank(evaluation.getWorkflowStatus())) {
			evaluation.setWorkflowStatus(WorkflowStatus.initalStatus().getCode());
		}
		evaluation.setEvaluationId(persistenceService.generateId());
		evaluation.setPublished(Boolean.FALSE);
		evaluation.setAllowNewSections(Convert.toBoolean(evaluation.getAllowNewSections()));
		evaluation.setAllowNewSubSections(Convert.toBoolean(evaluation.getAllowNewSubSections()));
		evaluation.populateBaseCreateFields();
		evaluation = persistenceService.persist(evaluation);

		EvaluationTemplate evaluationTemplate = new EvaluationTemplate();
		evaluationTemplate.setTemplateId(evaluation.getTemplateId());
		evaluationTemplate = evaluationTemplate.find();

		if (evaluationTemplate == null) {
			throw new OpenStorefrontRuntimeException("Unable to create evaluation", "Evaluation Template was not found.  Template Id: " + evaluation.getTemplateId());
		} else {

			WorkflowStatus initialStatus = WorkflowStatus.initalStatus();
			if (initialStatus == null) {
				throw new OpenStorefrontRuntimeException("Unable to get initial workflow status", "Add at least one workflow status.");
			}

			EvaluationChecklist checklist = new EvaluationChecklist();
			checklist.setChecklistId(persistenceService.generateId());
			checklist.setChecklistTemplateId(evaluationTemplate.getChecklistTemplateId());
			checklist.setEvaluationId(evaluation.getEvaluationId());
			checklist.setWorkflowStatus(initialStatus.getCode());
			checklist.populateBaseCreateFields();
			checklist = persistenceService.persist(checklist);

			ChecklistTemplate checklistTemplate = new ChecklistTemplate();
			checklistTemplate.setChecklistTemplateId(evaluationTemplate.getChecklistTemplateId());
			checklistTemplate = checklistTemplate.find();

			for (ChecklistTemplateQuestion question : checklistTemplate.getQuestions()) {
				EvaluationChecklistResponse response = new EvaluationChecklistResponse();
				response.setChecklistId(checklist.getChecklistId());
				response.setResponseId(persistenceService.generateId());
				response.setQuestionId(question.getQuestionId());
				response.setWorkflowStatus(initialStatus.getCode());
				response.setSortOrder(question.getSortOrder());
				response.populateBaseCreateFields();
				persistenceService.persist(response);
			}

			for (EvaluationSectionTemplate sectionTemplate : evaluationTemplate.getSectionTemplates()) {
				getContentSectionService().createSectionFromTemplate(Evaluation.class.getSimpleName(), evaluation.getEvaluationId(), sectionTemplate.getSectionTemplateId());

			}

		}
		return evaluation;
	}

	/**
	 * Update a List of evaluations to reflect the latest version of the
	 * templates they were based on
	 *
	 * @param evaluationIds
	 */
	@Override
	public void updateEvaluationsToLatestTemplateVersion(List<String> evaluationIds)
	{
		if (evaluationIds != null) {
			evaluationIds.forEach(evaluationId -> {
				Evaluation idExample = new Evaluation();
				idExample.setActiveStatus(Evaluation.ACTIVE_STATUS);
				idExample.setPublished(Boolean.FALSE);
				idExample.setEvaluationId(evaluationId);
				Evaluation existingEvaluation = idExample.find();
				if (existingEvaluation != null) {
					updateEvaluationToLatestTemplateVersion(existingEvaluation);
				} else {
					LOG.log(Level.WARNING, MessageFormat.format("(Skipping) Unable to find unpublished active evaluation for id: {0}", evaluationId));
				}
			});
		}
	}

	/**
	 * Update an evaluation to reflect the latest version of the template it was
	 * based on
	 *
	 * @param evaluation
	 * @return
	 */
	@Override
	public void updateEvaluationToLatestTemplateVersion(Evaluation evaluation)
	{
		Objects.requireNonNull(evaluation);
		Objects.requireNonNull(evaluation.getTemplateId());

		EvaluationTemplate exampleTemplate = new EvaluationTemplate();
		exampleTemplate.setTemplateId(evaluation.getTemplateId());
		EvaluationTemplate template = exampleTemplate.find();

		//FIXME: Add check for Evaluation; should not be published; perhap only active
		if (template != null) {
			updateContentSections(evaluation.getEvaluationId(), template.getSectionTemplates());
			updateChecklist(evaluation.getEvaluationId(), template.getChecklistTemplateId());
			//TODO: sync checklist (Add/Remove) Questions (Skip userAdded/Removed Questions)
		}
		// set template update flag to false
		Evaluation proxyExample = new Evaluation();
		proxyExample.setEvaluationId(evaluation.getEvaluationId());

		Evaluation proxy = proxyExample.findProxy();
		proxy.setTemplateUpdatePending(Boolean.FALSE);
		proxy.save();
	}

	private void updateContentSections(String evaluationId, List<EvaluationSectionTemplate> sectionTemplates)
	{
		Objects.requireNonNull(sectionTemplates);

		ContentSection contentSectionExample = new ContentSection();
		contentSectionExample.setActiveStatus(ContentSection.ACTIVE_STATUS);
		contentSectionExample.setEntity(Evaluation.class.getSimpleName());
		contentSectionExample.setEntityId(evaluationId);
		List<ContentSection> contentSections = persistenceService.queryByExample(contentSectionExample);
		// add new sections
		sectionTemplates.forEach((sectionTemplate) -> {
			boolean foundSection = false;
			for (ContentSection section : contentSections) {
				if (sectionTemplate.getSectionTemplateId().equals(section.getTemplateId())) {
					foundSection = true;
				}
			}
			if (!foundSection) {
				getContentSectionService().createSectionFromTemplate(Evaluation.class.getSimpleName(), evaluationId, sectionTemplate.getSectionTemplateId());
			}
		});
		// remove sections
		contentSections.forEach((section) -> {
			boolean foundSection = false;
			for (EvaluationSectionTemplate sectionTemplate : sectionTemplates) {
				if (sectionTemplate.getSectionTemplateId().equals(section.getTemplateId())) {
					foundSection = true;
				}
			}
			if (!foundSection) {
				getContentSectionService().deleteContentSection(section.getContentSectionId());
			}
		});
		// update sections
		//TODO: update section
	}

	private void updateChecklist(String evaluationId, String newChecklistTemplateId)
	{
		ChecklistTemplate templateExample = new ChecklistTemplate();
		templateExample.setChecklistTemplateId(newChecklistTemplateId);
		ChecklistTemplate template = templateExample.find();

		EvaluationChecklist checklist = new EvaluationChecklist();
		checklist.setEvaluationId(evaluationId);
		checklist = checklist.findProxy();
		checklist.setChecklistTemplateId(newChecklistTemplateId);
		checklist.save();

		String checklistId = checklist.getChecklistId();

		EvaluationChecklistResponse responseExample = new EvaluationChecklistResponse();
		responseExample.setChecklistId(checklistId);
		List<EvaluationChecklistResponse> responseList = responseExample.findByExampleProxy();

		//add questions
		template.getQuestions().forEach(question -> {
			boolean foundQuestion = false;
			for (EvaluationChecklistResponse response : responseList) {
				if (response.getQuestionId().equals(question.getQuestionId())) {
					foundQuestion = true;
					response.setSortOrder(question.getSortOrder());
					response.setActiveStatus(EvaluationChecklistResponse.ACTIVE_STATUS);
					response.save();
				}
			}
			if (!foundQuestion) {
				EvaluationChecklistResponse newResponse = new EvaluationChecklistResponse();
				newResponse.setChecklistId(checklistId);
				newResponse.setResponseId(persistenceService.generateId());
				newResponse.setQuestionId(question.getQuestionId());
				newResponse.setWorkflowStatus(WorkflowStatus.initalStatus().getCode());
				newResponse.setSortOrder(question.getSortOrder());
				newResponse.populateBaseCreateFields();
				persistenceService.persist(newResponse);
			}
		});

		//remove questions
		responseList.forEach(response -> {
			boolean foundQuestion = false;
			if (response.getActiveStatus().equals(EvaluationChecklistResponse.ACTIVE_STATUS)
					&& (response.getUserAddRemoveFlg() == null || !response.getUserAddRemoveFlg())) {
				for (ChecklistTemplateQuestion question : template.getQuestions()) {
					if (response.getQuestionId().equals(question.getQuestionId())) {
						foundQuestion = true;
					}
				}
				if (!foundQuestion) {
					//inactivate
					response.setActiveStatus(EvaluationChecklistResponse.INACTIVE_STATUS);
					response.save();
				}
			}
		});
	}

	@Override
	public EvaluationAll getEvaluation(String evaluationId)
	{
		return getEvaluation(evaluationId, false);
	}

	@Override
	public EvaluationAll getEvaluation(String evaluationId, boolean publicInformationOnly)
	{
		Objects.requireNonNull(evaluationId);

		EvaluationAll evaluationAll = null;

		Evaluation evaluation = new Evaluation();
		evaluation.setEvaluationId(evaluationId);
		evaluation = evaluation.find();
		evaluation = FilterEngine.filter(evaluation);
		if (evaluation != null) {
			evaluationAll = new EvaluationAll();
			evaluationAll.setEvaluation(evaluation);

			EvaluationChecklist evaluationChecklist = new EvaluationChecklist();
			evaluationChecklist.setActiveStatus(EvaluationChecklist.ACTIVE_STATUS);
			evaluationChecklist.setEvaluationId(evaluation.getEvaluationId());
			evaluationChecklist = evaluationChecklist.find();

			evaluationAll.setCheckListAll(getChecklistAll(evaluationChecklist.getChecklistId(), publicInformationOnly));

			ContentSection contentSectionExample = new ContentSection();
			contentSectionExample.setActiveStatus(ContentSection.ACTIVE_STATUS);
			contentSectionExample.setEntity(Evaluation.class.getSimpleName());
			contentSectionExample.setEntityId(evaluation.getEvaluationId());

			List<ContentSection> contentSections = contentSectionExample.findByExample();
			for (ContentSection contentSection : contentSections) {
				boolean keep = false;
				if (publicInformationOnly) {
					if (!contentSection.getPrivateSection()) {
						keep = true;
					}
				} else {
					keep = true;
				}

				if (keep) {
					ContentSectionAll contentSectionAll = getContentSectionService().getContentSectionAll(contentSection.getContentSectionId(), publicInformationOnly);
					evaluationAll.getContentSections().add(contentSectionAll);
				}
			}
		}

		return evaluationAll;
	}

	@Override
	public List<EvaluationAll> getPublishEvaluations(String componentId)
	{
		Objects.requireNonNull(componentId);

		List<EvaluationAll> evaluationAlls = new ArrayList<>();

		Evaluation evaluationExample = new Evaluation();
		evaluationExample.setOriginComponentId(componentId);
		evaluationExample.setActiveStatus(Evaluation.ACTIVE_STATUS);
		evaluationExample.setPublished(Boolean.TRUE);

		List<Evaluation> evaluations = evaluationExample.findByExample();
		evaluations.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_DESCENDING, Evaluation.FIELD_CREATE_DTS));
		for (Evaluation evaluation : evaluations) {
			evaluationAlls.add(getEvaluation(evaluation.getEvaluationId(), true));
		}

		return evaluationAlls;
	}

	@Override
	public EvaluationAll getLatestEvaluation(String componentId)
	{
		Objects.requireNonNull(componentId);

		Evaluation evaluationExample = new Evaluation();
		evaluationExample.setOriginComponentId(componentId);
		evaluationExample.setActiveStatus(Evaluation.ACTIVE_STATUS);

		List<Evaluation> evaluations = evaluationExample.findByExample();
		Evaluation latest = null;
		for (Evaluation evaluation : evaluations) {
			if (latest == null) {
				latest = evaluation;
			} else if (evaluation.getCreateDts().after(latest.getCreateDts())) {
				latest = evaluation;
			}
		}

		if (latest != null) {
			return getEvaluation(latest.getEvaluationId());
		}
		return null;
	}

	@Override
	public void deleteEvaluation(String evaluationId)
	{
		Evaluation evaluation = persistenceService.findById(Evaluation.class, evaluationId);
		if (evaluation != null) {

			//delete changeRequest if it exists
			Component changeRequest = persistenceService.findById(Component.class, evaluation.getComponentId());
			if (changeRequest != null) {
				getComponentService().cascadeDeleteOfComponent(changeRequest.getComponentId());
			}

			EvaluationChecklist evaluationChecklist = new EvaluationChecklist();
			evaluationChecklist.setEvaluationId(evaluationId);
			evaluationChecklist = evaluationChecklist.findProxy();
			if (evaluationChecklist != null) {

				EvaluationChecklistRecommendation recommendation = new EvaluationChecklistRecommendation();
				recommendation.setChecklistId(evaluationChecklist.getChecklistId());
				persistenceService.deleteByExample(recommendation);

				EvaluationChecklistResponse response = new EvaluationChecklistResponse();
				response.setChecklistId(evaluationChecklist.getChecklistId());
				persistenceService.deleteByExample(response);

				persistenceService.delete(evaluationChecklist);
			}

			ContentSection contentSection = new ContentSection();
			contentSection.setEntity(ContentSection.ENTITY_EVALUATION);
			contentSection.setEntityId(evaluationId);

			List<ContentSection> sections = contentSection.findByExample();
			for (ContentSection section : sections) {
				getContentSectionService().deleteContentSection(section.getContentSectionId());
			}
			getChangeLogServicePrivate().removeChangeLogs(Evaluation.class.getSimpleName(), evaluationId);

			persistenceService.delete(evaluation);
		}
	}

	@Override
	public void publishEvaluation(String evaluationId)
	{
		Evaluation evaluation = persistenceService.findById(Evaluation.class, evaluationId);
		if (evaluation != null) {

			Component originalComponent = getPersistenceService().findById(Component.class, evaluation.getOriginComponentId());
			if (originalComponent != null) {

				//merge change request
				Component changeRequest = persistenceService.findById(Component.class, evaluation.getComponentId());
				if (changeRequest != null) {
					getComponentService().mergePendingChange(changeRequest.getComponentId());
				}

				getChangeLogService().logFieldChange(evaluation, Evaluation.FIELD_PUBLISHED, evaluation.getPublished().toString(), Boolean.TRUE.toString());

				if (!ApprovalStatus.APPROVED.equals(originalComponent.getApprovalState())) {
					getComponentService().approveComponent(originalComponent.getComponentId());
				}

			} else {
				throw new OpenStorefrontRuntimeException("Unable find original entry.", "Check input");
			}

			evaluation.setPublished(Boolean.TRUE);
			evaluation.populateBaseUpdateFields();
			persistenceService.persist(evaluation);
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find Evaluation.", "Evaluation Id: " + evaluationId);
		}
	}

	@Override
	public void unpublishEvaluation(String evaluationId)
	{
		Evaluation evaluation = persistenceService.findById(Evaluation.class, evaluationId);
		if (evaluation != null) {

			getChangeLogService().logFieldChange(evaluation, Evaluation.FIELD_PUBLISHED, evaluation.getPublished().toString(), Boolean.FALSE.toString());

			evaluation.setPublished(Boolean.FALSE);
			evaluation.populateBaseUpdateFields();
			persistenceService.persist(evaluation);
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find Evaluation.", "Evaluation Id: " + evaluationId);
		}

	}

	@Override
	public void checkEvaluationComponent(String evaluationId)
	{
		Evaluation evaluationExisting = persistenceService.findById(Evaluation.class, evaluationId);
		if (evaluationExisting != null) {
			Component component = persistenceService.findById(Component.class, evaluationExisting.getComponentId());
			if (component == null) {
				Component changeRequest = getComponentService().createPendingChangeComponent(evaluationExisting.getOriginComponentId());

				evaluationExisting.setComponentId(changeRequest.getComponentId());

				//Don't update the evaluation user and date as it didn't actually change
				persistenceService.persist(evaluationExisting);
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find Evaluation: " + evaluationId, "Refresh to see if data still exists.");
		}
	}

	@Override
	public String copyEvaluation(String evaluationId)
	{
		Objects.requireNonNull(evaluationId);

		try {
			getChangeLogService().suspendSaving();

			EvaluationAll existing = getEvaluation(evaluationId);
			if (existing == null) {
				throw new OpenStorefrontRuntimeException("Unable to find evaluation to copy: " + evaluationId);
			}

			Evaluation evaluation = existing.getEvaluation();

			WorkflowStatus initial = WorkflowStatus.initalStatus();

			Component changeRequest = getComponentService().createPendingChangeComponent(evaluation.getOriginComponentId());
			evaluation.setComponentId(changeRequest.getComponentId());

			evaluation.setEvaluationId(persistenceService.generateId());
			evaluation.setWorkflowStatus(initial.getCode());
			evaluation.setVersion(StringUtils.left(evaluation.getVersion() + "-COPY", OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT));
			evaluation.setPublished(Boolean.FALSE);
			evaluation.populateBaseCreateFields();
			evaluation = persistenceService.persist(evaluation);

			String copyEvaluationId = evaluation.getEvaluationId();

			EvaluationChecklist checklist = existing.getCheckListAll().getEvaluationChecklist();
			checklist.setChecklistId(persistenceService.generateId());
			checklist.setWorkflowStatus(initial.getCode());
			checklist.setEvaluationId(copyEvaluationId);
			checklist.populateBaseCreateFields();
			checklist = persistenceService.persist(checklist);

			for (EvaluationChecklistRecommendation recommendation : existing.getCheckListAll().getRecommendations()) {
				EvaluationChecklistRecommendation newRecommendation = new EvaluationChecklistRecommendation();
				newRecommendation.setRecommendationId(persistenceService.generateId());
				newRecommendation.setChecklistId(checklist.getChecklistId());
				newRecommendation.updateFields(recommendation);

				newRecommendation.populateBaseCreateFields();
				persistenceService.persist(newRecommendation);
			}

			for (EvaluationChecklistResponse response : existing.getCheckListAll().getResponses()) {
				EvaluationChecklistResponse newResponse = new EvaluationChecklistResponse();
				newResponse.setQuestionId(response.getQuestionId());
				newResponse.setChecklistId(checklist.getChecklistId());
				newResponse.setResponseId(persistenceService.generateId());
				newResponse.updateFields(response);

				newResponse.setWorkflowStatus(initial.getCode());
				newResponse.populateBaseCreateFields();
				persistenceService.persist(newResponse);
			}

			for (ContentSectionAll sectionAll : existing.getContentSections()) {
				String existingSectionId = sectionAll.getSection().getContentSectionId();

				sectionAll.getSection().setContentSectionId(persistenceService.generateId());
				sectionAll.getSection().setEntity(Evaluation.class.getSimpleName());
				sectionAll.getSection().setEntityId(copyEvaluationId);
				sectionAll.getSection().setWorkflowStatus(initial.getCode());
				sectionAll.getSection().populateBaseCreateFields();
				ContentSection contentSection = persistenceService.persist(sectionAll.getSection());

				for (ContentSubSection subSection : sectionAll.getSubsections()) {
					subSection.setSubSectionId(persistenceService.generateId());
					subSection.setContentSectionId(contentSection.getContentSectionId());
					subSection.populateBaseCreateFields();
					persistenceService.persist(subSection);
				}

				ContentSectionMedia existingMedia = new ContentSectionMedia();
				existingMedia.setContentSectionId(existingSectionId);
				List<ContentSectionMedia> existingMediaRecords = existingMedia.findByExample();
				getContentSectionService().copySectionMedia(existingMediaRecords, contentSection);

			}

			return evaluation.getEvaluationId();
		} finally {
			getChangeLogService().resumeSaving();
		}
	}

	@Override
	public void approveEvaluationSummary(String evaluationId)
	{
		Objects.requireNonNull(evaluationId);

		Evaluation evaluation = getPersistenceService().findById(Evaluation.class, evaluationId);
		if (evaluation != null) {

			Component originalComponent = getPersistenceService().findById(Component.class, evaluation.getOriginComponentId());
			if (originalComponent != null) {

				Component component = getPersistenceService().findById(Component.class, evaluation.getComponentId());
				if (component != null) {
					LOG.log(Level.FINE, MessageFormat.format("Approving Change Request. Change Request Id: {0}", component.getComponentId()));
					getComponentService().mergePendingChange(component.getComponentId());
				} else {
					LOG.log(Level.FINE, MessageFormat.format("There is no outstanding change request to the evaluation. Id: {0}", evaluationId));
				}

				if (!ApprovalStatus.APPROVED.equals(originalComponent.getApprovalState())) {
					getComponentService().approveComponent(originalComponent.getComponentId());
				}

				evaluation.setLastSummaryApprovedDts(TimeUtil.currentDate());
				getPersistenceService().persist(evaluation);
			} else {
				throw new OpenStorefrontRuntimeException("Unable find original entry.", "Check input");
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable find evaluation to approve summary on.", "Check input");
		}

	}

}
