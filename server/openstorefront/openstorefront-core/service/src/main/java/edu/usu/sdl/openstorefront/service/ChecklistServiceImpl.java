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
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistResponse;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author dshurtleff
 */
public class ChecklistServiceImpl
		extends ServiceProxy
		implements ChecklistService
{

	@Override
	public ChecklistQuestion saveChecklistQuestion(ChecklistQuestion checklistQuestion)
	{
		ChecklistQuestion questionExisting = persistenceService.findById(ChecklistQuestion.class, checklistQuestion.getQuestionId());
		if (questionExisting != null) {
			questionExisting.updateFields(checklistQuestion);
			questionExisting = persistenceService.persist(questionExisting);
		} else {
			checklistQuestion.setQuestionId(persistenceService.generateId());
			checklistQuestion.populateBaseCreateFields();
			questionExisting = persistenceService.persist(checklistQuestion);
		}

		//TODO: Add Question Cache and Cleanup
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
			}
		}
	}

}
