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

import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormSection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubmissionFormSectionView
{

	private String sectionId;
	private String templateId;
	private String name;
	private String instructions;
	private Integer sectionOrder;

	@DataType(SubmissionFormFieldView.class)
	private List<SubmissionFormFieldView> fields = new ArrayList<>();

	@SuppressWarnings({"squid:S1186"})
	public SubmissionFormSectionView()
	{
	}

	public static SubmissionFormSectionView toView(SubmissionFormSection section)
	{
		SubmissionFormSectionView view = new SubmissionFormSectionView();
		view.setSectionId(section.getSectionId());
		view.setTemplateId(section.getTemplateId());
		view.setName(section.getName());
		view.setInstructions(section.getInstructions());
		view.setSectionOrder(section.getSectionOrder());

		if (section.getFields() != null) {
			section.getFields().sort((a, b) -> {
				return a.getFieldOrder().compareTo(b.getFieldOrder());
			});

			section.getFields().forEach(field -> {
				view.getFields().add(SubmissionFormFieldView.toView(field));
			});
		}

		return view;
	}

	public static List<SubmissionFormSectionView> toView(List<SubmissionFormSection> sections)
	{
		List<SubmissionFormSectionView> views = new ArrayList<>();
		sections.forEach(section -> {
			views.add(toView(section));
		});
		return views;
	}

	public String getSectionId()
	{
		return sectionId;
	}

	public void setSectionId(String sectionId)
	{
		this.sectionId = sectionId;
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

	public Integer getSectionOrder()
	{
		return sectionOrder;
	}

	public void setSectionOrder(Integer sectionOrder)
	{
		this.sectionOrder = sectionOrder;
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
