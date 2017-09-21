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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.entity.ChecklistQuestion;
import edu.usu.sdl.openstorefront.core.entity.ChecklistTemplate;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public interface ChecklistService
		extends AsyncService
{

	/**
	 * This will check a cache first and then load the question
	 *
	 * @param questionId
	 * @return
	 */
	public ChecklistQuestion findQuestion(String questionId);

	/**
	 * Saves a checklist question into the question pool
	 *
	 * This will dedup based on QID
	 *
	 * @param checklistQuestion
	 * @return saved question
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public ChecklistQuestion saveChecklistQuestion(ChecklistQuestion checklistQuestion);

	/**
	 * This will save a set of questions
	 *
	 * @param questions
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveChecklistQuestion(List<ChecklistQuestion> questions);

	/**
	 * Checks all ties to a question
	 *
	 * @param questionId
	 * @return
	 */
	public boolean isQuestionBeingUsed(String questionId);

	/**
	 * Remove questions ONLY if it's not in use
	 *
	 * @param questionId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteQuestion(String questionId);

	/**
	 * Copies the template
	 *
	 * @param checklistTemplateId
	 * @return the copy
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public ChecklistTemplate copyChecklistTemplate(String checklistTemplateId);

	/**
	 * Checks all ties to a checklist template
	 *
	 * @param checklistTemplateId
	 * @return
	 */
	public boolean isChecklistTemplateBeingUsed(String checklistTemplateId);

	/**
	 * Remove Checklist template ONLY if it's not in use
	 *
	 * @param checklistTemplateId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteChecklistTemplate(String checklistTemplateId);

	/**
	 * This will sync (Add/Remove questions to match input) Removed question
	 * will be in-activated to make it a safe delete
	 *
	 * @param checklistId
	 * @param questionIdsToKeep
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void syncChecklistQuestions(String checklistId, List<String> questionIdsToKeep);

}
