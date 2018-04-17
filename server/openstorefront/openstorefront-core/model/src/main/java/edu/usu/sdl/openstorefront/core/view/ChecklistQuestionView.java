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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.ChecklistQuestion;
import edu.usu.sdl.openstorefront.core.entity.EvaluationSection;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class ChecklistQuestionView
		extends ChecklistQuestion
{

	private String evaluationSectionDescription;

	public static ChecklistQuestionView toView(ChecklistQuestion checklistQuestion)
	{
		ChecklistQuestionView checklistQuestionView = new ChecklistQuestionView();
		try {
			BeanUtils.copyProperties(checklistQuestionView, checklistQuestion);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		checklistQuestionView.setEvaluationSectionDescription(TranslateUtil.translate(EvaluationSection.class, checklistQuestion.getEvaluationSection()));

		return checklistQuestionView;
	}

	public static List<ChecklistQuestionView> toView(List<ChecklistQuestion> questions)
	{
		List<ChecklistQuestionView> views = new ArrayList<>();

		for (ChecklistQuestion checklistQuestion : questions) {
			views.add(toView(checklistQuestion));
		}
		return views;
	}

	public String getEvaluationSectionDescription()
	{
		return evaluationSectionDescription;
	}

	public void setEvaluationSectionDescription(String evaluationSectionDescription)
	{
		this.evaluationSectionDescription = evaluationSectionDescription;
	}

}
