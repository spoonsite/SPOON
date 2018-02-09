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
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.validation.BasicHTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@Embeddable
public class SubmissionFormField
		extends StandardEntity<SubmissionFormField>
{

	private static final long serialVersionUID = 1L;

	@PK(generated = true)
	@NotNull
	private String fieldId;

	@FK(SubmissionFormStep.class)
	@NotNull
	private String stepId;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@ValidValueType(value = {}, lookupClass = SubmissionFormFieldType.class)
	@FK(SubmissionFormFieldType.class)
	private String fieldType;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@ValidValueType(value = {}, lookupClass = SubmissionFormFieldMappingType.class)
	@FK(SubmissionFormFieldMappingType.class)
	private String mappingType;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	@APIDescription("Typically should match existing entity name")
	private String entityName;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	private String fieldName;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(TextSanitizer.class)
	private String label;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(BasicHTMLSanitizer.class)
	private String labelTooltip;

	@ConsumeField
	private Boolean required;

	@ConsumeField
	private Boolean requireComment;

	@ConsumeField
	private Boolean requiredCommentOnValue;

	@ConsumeField
	private Boolean allowHTMLInComment;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	private String commentLabel;

	@NotNull
	@ConsumeField
	@Min(0)
	@Max(Integer.MAX_VALUE)
	private Integer fieldOrder;

	public SubmissionFormField()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		SubmissionFormField submissionFormField = (SubmissionFormField) entity;

		this.setAllowHTMLInComment(submissionFormField.getAllowHTMLInComment());
		this.setCommentLabel(submissionFormField.getCommentLabel());
		this.setEntityName(submissionFormField.getEntityName());
		this.setFieldName(submissionFormField.getFieldName());
		this.setFieldOrder(submissionFormField.getFieldOrder());
		this.setFieldType(submissionFormField.getFieldType());
		this.setLabel(submissionFormField.getLabel());
		this.setLabelTooltip(submissionFormField.getLabelTooltip());
		this.setMappingType(submissionFormField.getMappingType());
		this.setRequireComment(submissionFormField.getRequireComment());
		this.setRequired(submissionFormField.getRequired());
		this.setRequiredCommentOnValue(submissionFormField.getRequiredCommentOnValue());
		this.setStepId(submissionFormField.getStepId());

	}

	public String getStepId()
	{
		return stepId;
	}

	public void setStepId(String stepId)
	{
		this.stepId = stepId;
	}

	public String getFieldType()
	{
		return fieldType;
	}

	public void setFieldType(String fieldType)
	{
		this.fieldType = fieldType;
	}

	public String getMappingType()
	{
		return mappingType;
	}

	public void setMappingType(String mappingType)
	{
		this.mappingType = mappingType;
	}

	public String getEntityName()
	{
		return entityName;
	}

	public void setEntityName(String entityName)
	{
		this.entityName = entityName;
	}

	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getLabelTooltip()
	{
		return labelTooltip;
	}

	public void setLabelTooltip(String labelTooltip)
	{
		this.labelTooltip = labelTooltip;
	}

	public Boolean getRequired()
	{
		return required;
	}

	public void setRequired(Boolean required)
	{
		this.required = required;
	}

	public Boolean getRequireComment()
	{
		return requireComment;
	}

	public void setRequireComment(Boolean requireComment)
	{
		this.requireComment = requireComment;
	}

	public Boolean getRequiredCommentOnValue()
	{
		return requiredCommentOnValue;
	}

	public void setRequiredCommentOnValue(Boolean requiredCommentOnValue)
	{
		this.requiredCommentOnValue = requiredCommentOnValue;
	}

	public Boolean getAllowHTMLInComment()
	{
		return allowHTMLInComment;
	}

	public void setAllowHTMLInComment(Boolean allowHTMLInComment)
	{
		this.allowHTMLInComment = allowHTMLInComment;
	}

	public String getCommentLabel()
	{
		return commentLabel;
	}

	public void setCommentLabel(String commentLabel)
	{
		this.commentLabel = commentLabel;
	}

	public Integer getFieldOrder()
	{
		return fieldOrder;
	}

	public void setFieldOrder(Integer fieldOrder)
	{
		this.fieldOrder = fieldOrder;
	}

	public String getFieldId()
	{
		return fieldId;
	}

	public void setFieldId(String fieldId)
	{
		this.fieldId = fieldId;
	}

}
