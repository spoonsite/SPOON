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
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.ChecklistQuestion;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistResponse;
import edu.usu.sdl.openstorefront.core.entity.WorkflowStatus;
import edu.usu.sdl.openstorefront.core.sort.ChecklistResponseViewComparator;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class ChecklistResponseView
		extends EvaluationChecklistResponse
{

	private ChecklistQuestionView question;
	private String workflowStatusDescription;

	public ChecklistResponseView()
	{
	}

	public static ChecklistResponseView toView(EvaluationChecklistResponse response)
	{
		ChecklistResponseView checklistResponseView = new ChecklistResponseView();
		try {
			BeanUtils.copyProperties(checklistResponseView, response);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		checklistResponseView.setWorkflowStatusDescription(TranslateUtil.translate(WorkflowStatus.class, checklistResponseView.getWorkflowStatus()));

		Service service = ServiceProxyFactory.getServiceProxy();
		ChecklistQuestion questionFound = service.getChecklistService().findQuestion(response.getQuestionId());
		if (questionFound != null) {
			checklistResponseView.setQuestion(ChecklistQuestionView.toView(questionFound));
		}

		return checklistResponseView;
	}

	public static List<ChecklistResponseView> toView(List<EvaluationChecklistResponse> responses)
	{
		List<ChecklistResponseView> views = new ArrayList<>();

		for (EvaluationChecklistResponse response : responses) {
			views.add(toView(response));
		}
		views.sort(new ChecklistResponseViewComparator<>());

		return views;
	}

	public EvaluationChecklistResponse toResponse()
	{
		EvaluationChecklistResponse response = new EvaluationChecklistResponse();
		try {
			BeanUtils.copyProperties(response, this);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		return response;
	}

	public ChecklistQuestionView getQuestion()
	{
		return question;
	}

	public void setQuestion(ChecklistQuestionView question)
	{
		this.question = question;
	}

	public String getWorkflowStatusDescription()
	{
		return workflowStatusDescription;
	}

	public void setWorkflowStatusDescription(String workflowStatusDescription)
	{
		this.workflowStatusDescription = workflowStatusDescription;
	}

}
