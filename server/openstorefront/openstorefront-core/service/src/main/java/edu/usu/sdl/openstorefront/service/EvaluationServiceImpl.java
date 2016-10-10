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

import edu.usu.sdl.openstorefront.core.api.EvaluationService;
import edu.usu.sdl.openstorefront.core.entity.ContentSection;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklist;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistRecommendation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistResponse;
import edu.usu.sdl.openstorefront.core.model.ChecklistAll;
import edu.usu.sdl.openstorefront.core.model.ContentSectionAll;
import edu.usu.sdl.openstorefront.core.model.EvaluationAll;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author dshurtleff
 */
public class EvaluationServiceImpl
		extends ServiceProxy
		implements EvaluationService
{

	@Override
	public String saveCheckListAll(ChecklistAll checklistAll)
	{
		Objects.requireNonNull(checklistAll);
		Objects.requireNonNull(checklistAll.getEvaluationChecklist());

		EvaluationChecklist evaluationChecklist = checklistAll.getEvaluationChecklist().save();

		//remove old recommendations replace with the new ones
		EvaluationChecklistRecommendation recommendationDeleteExample = new EvaluationChecklistRecommendation();
		recommendationDeleteExample.setChecklistId(evaluationChecklist.getChecklistId());
		persistenceService.deleteByExample(recommendationDeleteExample);

		for (EvaluationChecklistRecommendation recommendation : checklistAll.getRecommendations()) {
			recommendation.setChecklistId(evaluationChecklist.getChecklistId());
			recommendation.setRecommendationId(persistenceService.generateId());
			recommendation.populateBaseCreateFields();
			persistenceService.persist(recommendation);
		}

		//remove old responses replace with the new ones
		EvaluationChecklistResponse responseDeleteExample = new EvaluationChecklistResponse();
		responseDeleteExample.setChecklistId(evaluationChecklist.getChecklistId());
		persistenceService.deleteByExample(responseDeleteExample);

		for (EvaluationChecklistResponse response : checklistAll.getResponses()) {
			response.setChecklistId(evaluationChecklist.getChecklistId());
			response.setResponseId(persistenceService.generateId());
			response.populateBaseCreateFields();
			persistenceService.persist(response);
		}

		return evaluationChecklist.getChecklistId();
	}

	@Override
	public ChecklistAll getChecklistAll(String checklistId)
	{
		Objects.requireNonNull(checklistId);

		ChecklistAll checklistAll = null;

		EvaluationChecklist evaluationChecklist = new EvaluationChecklist();
		evaluationChecklist.setChecklistId(checklistId);
		evaluationChecklist = evaluationChecklist.find();
		if (evaluationChecklist != null) {
			checklistAll = new ChecklistAll();
			checklistAll.setEvaluationChecklist(evaluationChecklist);

			EvaluationChecklistRecommendation recommendation = new EvaluationChecklistRecommendation();
			recommendation.setChecklistId(checklistId);
			recommendation.setActiveStatus(EvaluationChecklistRecommendation.ACTIVE_STATUS);
			checklistAll.getRecommendations().addAll(recommendation.findByExample());

			EvaluationChecklistResponse response = new EvaluationChecklistResponse();
			response.setChecklistId(checklistId);
			response.setActiveStatus(EvaluationChecklistResponse.ACTIVE_STATUS);
			checklistAll.getResponses().addAll(response.findByExample());
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
	public EvaluationAll createEvaluationFromTemplate(String templateId, String componentId)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public EvaluationAll getEvaluation(String evaluationId)
	{
		Objects.requireNonNull(evaluationId);

		EvaluationAll evaluationAll = null;

		Evaluation evaluation = persistenceService.findById(Evaluation.class, evaluationId);
		if (evaluation != null) {
			evaluationAll = new EvaluationAll();
			evaluationAll.setEvaluation(evaluation);

			EvaluationChecklist evaluationChecklist = new EvaluationChecklist();
			evaluationChecklist.setActiveStatus(EvaluationChecklist.ACTIVE_STATUS);
			evaluationChecklist.setEvaluationId(evaluation.getEvaluationId());
			evaluationChecklist = evaluationChecklist.find();

			evaluationAll.setCheckListAll(getChecklistAll(evaluationChecklist.getChecklistId()));

			ContentSection contentSectionExample = new ContentSection();
			contentSectionExample.setActiveStatus(ContentSection.ACTIVE_STATUS);
			contentSectionExample.setEntity(Evaluation.class.getSimpleName());
			contentSectionExample.setEntityId(evaluation.getEvaluationId());

			List<ContentSection> contentSections = contentSectionExample.findByExample();
			for (ContentSection contentSection : contentSections) {
				ContentSectionAll contentSectionAll = getContentSectionService().getContentSectionAll(contentSection.getContentSectionId());
				evaluationAll.getContentSections().add(contentSectionAll);
			}
		}

		return evaluationAll;
	}

	@Override
	public EvaluationAll getLatestEvaluation(String componentId)
	{
		Objects.requireNonNull(componentId);

		Evaluation evaluationExample = new Evaluation();
		evaluationExample.setComponentId(componentId);
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

}
