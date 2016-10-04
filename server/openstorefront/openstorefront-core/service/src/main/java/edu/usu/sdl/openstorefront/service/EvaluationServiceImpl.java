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
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklist;
import edu.usu.sdl.openstorefront.core.model.ChecklistAll;
import edu.usu.sdl.openstorefront.core.model.ContentSectionAll;
import edu.usu.sdl.openstorefront.core.model.EvaluationAll;
import java.util.Objects;

/**
 *
 * @author dshurtleff
 */
public class EvaluationServiceImpl
		extends ServiceProxy
		implements EvaluationService
{

//	@Override
//	public Evaluation saveEvaluation(Evaluation evaluation)
//	{
//		Evaluation evaluationExisting = persistenceService.findById(Evaluation.class, evaluation.getEvaluationId());
//		if (evaluationExisting != null) {
//			evaluationExisting.updateFields(evaluation);
//			evaluationExisting = persistenceService.persist(evaluationExisting);
//		} else {
//			evaluation.setEvaluationId(persistenceService.generateId());
//			evaluation.populateBaseCreateFields();
//			evaluationExisting = persistenceService.persist(evaluation);
//		}
//		return evaluationExisting;
//	}
//
//	@Override
//	public EvaluationChecklist saveEvaluationChecklist(EvaluationChecklist evaluationChecklist)
//	{
//		EvaluationChecklist checklistExisting = persistenceService.findById(EvaluationChecklist.class, evaluationChecklist.getChecklistId());
//		if (checklistExisting != null) {
//			checklistExisting.updateFields(evaluationChecklist);
//			checklistExisting = persistenceService.persist(checklistExisting);
//		} else {
//			evaluationChecklist.setChecklistId(persistenceService.generateId());
//			evaluationChecklist.populateBaseCreateFields();
//			checklistExisting = persistenceService.persist(evaluationChecklist);
//		}
//		return checklistExisting;
//	}
//
//	@Override
//	public EvaluationChecklistRecommendation saveEvaluationChecklistRecommendation(EvaluationChecklistRecommendation evaluationChecklistRecommendation)
//	{
//		EvaluationChecklistRecommendation recommendationExisting = persistenceService.findById(EvaluationChecklistRecommendation.class, evaluationChecklistRecommendation.getRecommendationId());
//		if (recommendationExisting != null) {
//			recommendationExisting.updateFields(evaluationChecklistRecommendation);
//			recommendationExisting = persistenceService.persist(recommendationExisting);
//		} else {
//			evaluationChecklistRecommendation.setRecommendationId(persistenceService.generateId());
//			evaluationChecklistRecommendation.populateBaseCreateFields();
//			recommendationExisting = persistenceService.persist(recommendationExisting);
//		}
//		return recommendationExisting;
//	}
//
//	@Override
//	public EvaluationChecklistResponse saveEvaluationChecklistResponse(EvaluationChecklistResponse evaluationChecklistResponse)
//	{
//		EvaluationChecklistResponse responseExisting = persistenceService.findById(EvaluationChecklistResponse.class, evaluationChecklistResponse.getResponseId());
//		if (responseExisting != null) {
//			responseExisting.updateFields(evaluationChecklistResponse);
//			responseExisting = persistenceService.persist(responseExisting);
//		} else {
//			evaluationChecklistResponse.setResponseId(persistenceService.generateId());
//			evaluationChecklistResponse.populateBaseCreateFields();
//			responseExisting = persistenceService.persist(evaluationChecklistResponse);
//		}
//		return responseExisting;
//	}
//
//	@Override
//	public EvaluationTemplate saveEvaluationTemplate(EvaluationTemplate evaluationTemplate)
//	{
//		EvaluationTemplate templateExisting = persistenceService.findById(EvaluationTemplate.class, evaluationTemplate.getTemplateId());
//		if (templateExisting != null) {
//			templateExisting.updateFields(evaluationTemplate);
//			templateExisting = persistenceService.persist(templateExisting);
//		} else {
//			evaluationTemplate.setTemplateId(persistenceService.generateId());
//			evaluationTemplate.populateBaseCreateFields();
//			templateExisting = persistenceService.persist(evaluationTemplate);
//		}
//		return templateExisting;
//	}
	@Override
	public String saveCheckListAll(ChecklistAll checklistAll)
	{
		Objects.requireNonNull(checklistAll);
		Objects.requireNonNull(checklistAll.getEvaluationChecklist());

		EvaluationChecklist evaluationChecklist = checklistAll.getEvaluationChecklist().save();

		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

	}

	@Override
	public ChecklistAll getChecklistAll()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
			getContentSectionService().saveContentSectionAll(contentSectionAll);
		}

		return savedEvalation.getEvaluationId();
	}

	@Override
	public EvaluationAll createEvaluationFromTemplate(String templateId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public EvaluationAll getEvaluation(String evaluationId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public EvaluationAll getLatestEvaluation(String componentId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
