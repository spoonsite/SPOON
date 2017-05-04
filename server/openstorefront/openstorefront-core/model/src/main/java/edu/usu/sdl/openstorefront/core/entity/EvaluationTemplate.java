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
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
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
@APIDescription("This holds a template for an evaluation")
public class EvaluationTemplate
		extends StandardEntity<EvaluationTemplate>
{

	@PK(generated = true)
	@NotNull
	private String templateId;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String name;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String description;

	@NotNull
	@ConsumeField
	@FK(ChecklistTemplate.class)
	private String checklistTemplateId;

	@ConsumeField
	@Embedded
	@OneToMany(orphanRemoval = true)
	@DataType(EvaluationSectionTemplate.class)
	private List<EvaluationSectionTemplate> sectionTemplates;

	public EvaluationTemplate()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		EvaluationTemplate evaluationTemplate = (EvaluationTemplate) entity;

		setDescription(evaluationTemplate.getDescription());
		setName(evaluationTemplate.getName());
		setChecklistTemplateId(evaluationTemplate.getChecklistTemplateId());
		setSectionTemplates(evaluationTemplate.getSectionTemplates());

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

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getChecklistTemplateId()
	{
		return checklistTemplateId;
	}

	public void setChecklistTemplateId(String checklistTemplateId)
	{
		this.checklistTemplateId = checklistTemplateId;
	}

	public List<EvaluationSectionTemplate> getSectionTemplates()
	{
		return sectionTemplates;
	}

	public void setSectionTemplates(List<EvaluationSectionTemplate> sectionTemplates)
	{
		this.sectionTemplates = sectionTemplates;
	}

}
