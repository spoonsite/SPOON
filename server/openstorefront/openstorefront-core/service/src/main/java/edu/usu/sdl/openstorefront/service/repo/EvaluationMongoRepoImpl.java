/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.repo;

import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.ChecklistQuestion;
import edu.usu.sdl.openstorefront.core.entity.Tag;
import edu.usu.sdl.openstorefront.core.view.CheckQuestionFilterParams;
import edu.usu.sdl.openstorefront.core.view.ChecklistQuestionView;
import edu.usu.sdl.openstorefront.core.view.ChecklistQuestionWrapper;
import edu.usu.sdl.openstorefront.service.repo.api.EvaluationRepo;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sourceforge.stripes.util.bean.BeanUtil;

/**
 *
 * @author dshurtleff
 */
public class EvaluationMongoRepoImpl
		extends BaseMongoRepo
		implements EvaluationRepo
{

	@Override
	public ChecklistQuestionWrapper findChecklistQuestions(CheckQuestionFilterParams filterQueryParams)
	{
		ChecklistQuestion checklistExample = new ChecklistQuestion();
		checklistExample.setActiveStatus(filterQueryParams.getStatus());

		ChecklistQuestion checklistStartExample = new ChecklistQuestion();
		checklistStartExample.setUpdateDts(filterQueryParams.getStart());

		ChecklistQuestion checklistEndExample = new ChecklistQuestion();
		checklistEndExample.setUpdateDts(filterQueryParams.getEnd());

		QueryByExample<ChecklistQuestion> queryByExample = new QueryByExample<>(checklistExample);

		SpecialOperatorModel<ChecklistQuestion> specialOperatorModel = new SpecialOperatorModel<>();
		specialOperatorModel.setExample(checklistStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel<>();
		specialOperatorModel.setExample(checklistEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		if (filterQueryParams.getTags().isEmpty()) {
			queryByExample.setMaxResults(filterQueryParams.getMax());
			queryByExample.setFirstResult(filterQueryParams.getOffset());
			queryByExample.setSortDirection(filterQueryParams.getSortOrder());

			ChecklistQuestion checklistQuestionSortExample = new ChecklistQuestion();
			Field sortField = ReflectionUtil.getField(checklistQuestionSortExample, filterQueryParams.getSortField());
			if (sortField != null) {
				BeanUtil.setPropertyValue(sortField.getName(), checklistQuestionSortExample, QueryByExample.getFlagForType(sortField.getType()));
				queryByExample.setOrderBy(checklistQuestionSortExample);
			}
		}

		List<ChecklistQuestion> questions = service.getPersistenceService().queryByExample(queryByExample);

		long totalCountForTagsFilter = 0;
		if (!filterQueryParams.getTags().isEmpty()) {
			//post filter
			Set<String> tagInSet = new HashSet<>(filterQueryParams.getTags());
			questions.removeIf(question -> {
				boolean containsAny = false;
				for (Tag tag : question.getTags()) {
					if (tagInSet.contains(tag.getTag())) {
						containsAny = true;
					}
				}
				return !containsAny;
			});
			totalCountForTagsFilter = questions.size();

			//then sort/window
			questions = filterQueryParams.filter(questions);
		}

		ChecklistQuestionWrapper checklistQuestionWrapper = new ChecklistQuestionWrapper();
		checklistQuestionWrapper.getData().addAll(ChecklistQuestionView.toView(questions));
		if (filterQueryParams.getTags().isEmpty()) {
			checklistQuestionWrapper.setTotalNumber(service.getPersistenceService().countByExample(queryByExample));
		} else {
			checklistQuestionWrapper.setTotalNumber(totalCountForTagsFilter);
		}

		return checklistQuestionWrapper;
	}

}
