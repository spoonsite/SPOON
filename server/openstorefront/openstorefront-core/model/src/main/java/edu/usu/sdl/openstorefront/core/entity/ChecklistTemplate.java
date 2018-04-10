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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Holds a template for a checklist")
public class ChecklistTemplate
		extends StandardEntity<ChecklistTemplate>
{

	@PK(generated = true)
	@NotNull
	private String checklistTemplateId;

	@NotNull
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	private String name;

	@NotNull
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	private String description;

	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_16K)
	@Sanitize(HTMLSanitizer.class)
	private String instructions;

	@ConsumeField
	@DataType(ChecklistTemplateQuestion.class)
	@Embedded
	@OneToMany(orphanRemoval = true)
	private List<ChecklistTemplateQuestion> questions;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ChecklistTemplate()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		ChecklistTemplate template = (ChecklistTemplate) entity;
		setDescription(template.getDescription());
		setName(template.getName());
		setQuestions(template.getQuestions());
		setInstructions(template.getInstructions());
	}

	public String getChecklistTemplateId()
	{
		return checklistTemplateId;
	}

	public void setChecklistTemplateId(String checklistTemplateId)
	{
		this.checklistTemplateId = checklistTemplateId;
	}

	public List<ChecklistTemplateQuestion> getQuestions()
	{
		return questions;
	}

	public void setQuestions(List<ChecklistTemplateQuestion> questions)
	{
		this.questions = questions;
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

}
