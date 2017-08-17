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

import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.ChecklistQuestion;
import edu.usu.sdl.openstorefront.core.entity.ChecklistTemplate;
import edu.usu.sdl.openstorefront.core.entity.ChecklistTemplateQuestion;
import edu.usu.sdl.openstorefront.core.sort.ChecklistTemplateQuestionComparator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ChecklistTemplateDetailView
{

	private String name;
	private String description;
	private String instructions;
	private List<ChecklistQuestionView> questions = new ArrayList<>();

	public ChecklistTemplateDetailView()
	{
	}

	public static ChecklistTemplateDetailView toView(ChecklistTemplate template)
	{
		Service service = ServiceProxyFactory.getServiceProxy();
		ChecklistTemplateDetailView view = new ChecklistTemplateDetailView();
		view.setName(template.getName());
		view.setDescription(template.getDescription());
		view.setInstructions(template.getInstructions());

		template.getQuestions().sort(new ChecklistTemplateQuestionComparator<>());
		for (ChecklistTemplateQuestion questionTemplate : template.getQuestions()) {
			ChecklistQuestion checklistQuestion = service.getChecklistService().findQuestion(questionTemplate.getQuestionId());
			view.getQuestions().add(ChecklistQuestionView.toView(checklistQuestion));
		}

		return view;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getInstructions()
	{
		return instructions;
	}

	public void setInstructions(String instructions)
	{
		this.instructions = instructions;
	}

	public List<ChecklistQuestionView> getQuestions()
	{
		return questions;
	}

	public void setQuestions(List<ChecklistQuestionView> questions)
	{
		this.questions = questions;
	}

}
