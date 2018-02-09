/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.entity.SubmissionFormStep;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class SubmissionFormStepView
{

	private String stepId;
	private String templateId;
	private String name;
	private String instructions;
	private Integer stepOrder;
	private List<SubmissionFormFieldView> fields = new ArrayList<>();

	@SuppressWarnings("empty")
	public SubmissionFormStepView()
	{
	}

	public static SubmissionFormStepView toView(SubmissionFormStep step)
	{
		SubmissionFormStepView view = new SubmissionFormStepView();
		view.setStepId(step.getStepId());
		view.setTemplateId(step.getTemplateId());
		view.setName(step.getName());
		view.setInstructions(step.getInstructions());
		view.setStepOrder(step.getStepOrder());

		if (step.getFields() != null) {
			step.getFields().forEach(field -> {
				view.getFields().add(SubmissionFormFieldView.toView(field));
			});
		}

		return view;
	}

	public static List<SubmissionFormStepView> toView(List<SubmissionFormStep> steps)
	{
		List<SubmissionFormStepView> views = new ArrayList<>();
		steps.forEach(step -> {
			views.add(toView(step));
		});
		return views;
	}

	public String getStepId()
	{
		return stepId;
	}

	public void setStepId(String stepId)
	{
		this.stepId = stepId;
	}

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getInstructions()
	{
		return instructions;
	}

	public void setInstructions(String instructions)
	{
		this.instructions = instructions;
	}

	public Integer getStepOrder()
	{
		return stepOrder;
	}

	public void setStepOrder(Integer stepOrder)
	{
		this.stepOrder = stepOrder;
	}

	public List<SubmissionFormFieldView> getFields()
	{
		return fields;
	}

	public void setFields(List<SubmissionFormFieldView> fields)
	{
		this.fields = fields;
	}

}
