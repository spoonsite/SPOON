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
import edu.usu.sdl.openstorefront.validation.CleanKeySanitizer;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
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

	@FK(SubmissionFormSection.class)
	@NotNull
	private String sectionId;

	@NotNull
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = SubmissionFormFieldType.class)
	@FK(SubmissionFormFieldType.class)
	private String fieldType;

	@NotNull
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = SubmissionFormFieldMappingType.class)
	@FK(SubmissionFormFieldMappingType.class)
	private String mappingType;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	private String fieldName;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(CleanKeySanitizer.class)
	private String attributeType;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(CleanKeySanitizer.class)
	private String attributeCode;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@ValidValueType(value = {}, lookupClass = ContactType.class)
	@FK(ContactType.class)
	@APIDescription("Leave null to prompt for type")
	private String contactType;

	@ConsumeField
	@ValidValueType(value = {}, lookupClass = RelationshipType.class)
	@FK(RelationshipType.class)
	@APIDescription("This is used with child entries to determine the type of relationsip")
	private String relationshipType;

	@ConsumeField
	private String subSubmissionTemplateId;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_80)
	@Sanitize(TextSanitizer.class)
	private String questionNumber;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(HTMLSanitizer.class)
	private String staticContent;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(TextSanitizer.class)
	private String label;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(BasicHTMLSanitizer.class)
	private String labelTooltip;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_60)
	@Sanitize(TextSanitizer.class)
	private String labelAlign;

	@ConsumeField
	private Boolean required;

	@ConsumeField
	private Boolean requireComment;

	@ConsumeField
	private String requiredCommentOnValue;

	@ConsumeField
	private Boolean allowHTMLInComment;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(TextSanitizer.class)
	private String childEntryType;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	private String commentLabel;

	@NotNull
	@ConsumeField
	@Min(0)
	@Max(Integer.MAX_VALUE)
	private Integer fieldOrder;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
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
		this.setFieldName(submissionFormField.getFieldName());
		this.setFieldOrder(submissionFormField.getFieldOrder());
		this.setFieldType(submissionFormField.getFieldType());
		this.setLabel(submissionFormField.getLabel());
		this.setLabelTooltip(submissionFormField.getLabelTooltip());
		this.setLabelAlign(submissionFormField.getLabelAlign());
		this.setMappingType(submissionFormField.getMappingType());
		this.setRequireComment(submissionFormField.getRequireComment());
		this.setRequired(submissionFormField.getRequired());
		this.setRequiredCommentOnValue(submissionFormField.getRequiredCommentOnValue());
		this.setAttributeType(submissionFormField.getAttributeType());
		this.setAttributeCode(submissionFormField.getAttributeCode());
		this.setContactType(submissionFormField.getContactType());
		this.setRelationshipType(submissionFormField.getRelationshipType());
		this.setChildEntryType(submissionFormField.getChildEntryType());

		this.setSectionId(submissionFormField.getSectionId());

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

	public String getRequiredCommentOnValue()
	{
		return requiredCommentOnValue;
	}

	public void setRequiredCommentOnValue(String requiredCommentOnValue)
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

	public String getAttributeType()
	{
		return attributeType;
	}

	public void setAttributeType(String attributeType)
	{
		this.attributeType = attributeType;
	}

	public String getSubSubmissionTemplateId()
	{
		return subSubmissionTemplateId;
	}

	public void setSubSubmissionTemplateId(String subSubmissionTemplateId)
	{
		this.subSubmissionTemplateId = subSubmissionTemplateId;
	}

	public String getSectionId()
	{
		return sectionId;
	}

	public void setSectionId(String sectionId)
	{
		this.sectionId = sectionId;
	}

	public String getQuestionNumber()
	{
		return questionNumber;
	}

	public void setQuestionNumber(String questionNumber)
	{
		this.questionNumber = questionNumber;
	}

	public String getStaticContent()
	{
		return staticContent;
	}

	public void setStaticContent(String staticContent)
	{
		this.staticContent = staticContent;
	}

	public String getChildEntryType()
	{
		return childEntryType;
	}

	public void setChildEntryType(String childEntryType)
	{
		this.childEntryType = childEntryType;
	}

	public String getRelationshipType()
	{
		return relationshipType;
	}

	public void setRelationshipType(String relationshipType)
	{
		this.relationshipType = relationshipType;
	}

	public String getLabelAlign()
	{
		return labelAlign;
	}

	public void setLabelAlign(String labelAlign)
	{
		this.labelAlign = labelAlign;
	}

	public String getContactType()
	{
		return contactType;
	}

	public void setContactType(String contactType)
	{
		this.contactType = contactType;
	}

	public String getAttributeCode()
	{
		return attributeCode;
	}

	public void setAttributeCode(String attributeCode)
	{
		this.attributeCode = attributeCode;
	}

}
