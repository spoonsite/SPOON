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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@Embeddable
public class SubmissionFormStep
		extends StandardEntity<SubmissionFormStep>
{

	private static final long serialVersionUID = 1L;

	@PK(generated = true)
	@NotNull
	private String stepId;

	@FK(SubmissionFormTemplate.class)
	@NotNull
	private String templateId;

	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	@NotNull
	private String name;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_64K)
	@Sanitize(HTMLSanitizer.class)
	private String instructions;

	@ConsumeField
	@NotNull
	@Min(0)
	@Max(Integer.MAX_VALUE)
	private Integer stepOrder;

	@ConsumeField
	@DataType(SubmissionFormField.class)
	@Embedded
	@OneToMany(orphanRemoval = true)
	private List<SubmissionFormField> fields;

	public SubmissionFormStep()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		SubmissionFormStep step = (SubmissionFormStep) entity;
		this.setName(step.getName());
		this.setInstructions(step.getInstructions());
		this.setStepOrder(step.getStepOrder());
		this.setFields(step.getFields());
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

	public List<SubmissionFormField> getFields()
	{
		return fields;
	}

	public void setFields(List<SubmissionFormField> fields)
	{
		this.fields = fields;
	}

}
