/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
@APIDescription("Define type of crossreference")
public class AttributeXRefType
		extends StandardEntity<AttributeXRefType>
{

	@PK
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ConsumeField
	@FK(AttributeType.class)
	private String attributeType;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@ConsumeField
	private String fieldName;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@ConsumeField
	private String fieldId;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@ConsumeField
	private String projectType;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@ConsumeField
	private String issueType;

	@NotNull
	@ValidValueType(value = {}, lookupClass = IntegrationType.class)
	@ConsumeField
	@FK(IntegrationType.class)
	private String integrationType;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public AttributeXRefType()
	{

	}

	public String getAttributeType()
	{
		return attributeType;
	}

	public void setAttributeType(String attributeType)
	{
		this.attributeType = attributeType;
	}

	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	public String getProjectType()
	{
		return projectType;
	}

	public void setProjectType(String projectType)
	{
		this.projectType = projectType;
	}

	public String getIssueType()
	{
		return issueType;
	}

	public void setIssueType(String issueType)
	{
		this.issueType = issueType;
	}

	public String getIntegrationType()
	{
		return integrationType;
	}

	public void setIntegrationType(String integrationType)
	{
		this.integrationType = integrationType;
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
