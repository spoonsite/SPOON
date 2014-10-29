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
package edu.usu.sdl.openstorefront.web.rest.model;

import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.storage.model.AttributeXRefMap;
import java.util.List;

/**
 *
 * @author jlaw
 */
public class AttributeXrefMapView
{

	private String projectType;
	private String issueType;
	private String fieldKey;
	private String attributeType;
	private String fieldName;
	private String fieldId;
	private String attributeName;

	@DataType(AttributeXRefMap.class)
	private List<AttributeXRefMap> mapping;

	public AttributeXrefMapView()
	{

	}

	public String getFieldKey()
	{
		return fieldKey;
	}

	public void setFieldKey(String fieldKey)
	{
		this.fieldKey = fieldKey;
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

	public String getFieldId()
	{
		return fieldId;
	}

	public void setFieldId(String fieldId)
	{
		this.fieldId = fieldId;
	}

	public String getAttributeName()
	{
		return attributeName;
	}

	public void setAttributeName(String attributeName)
	{
		this.attributeName = attributeName;
	}

	public List<AttributeXRefMap> getMapping()
	{
		return mapping;
	}

	public void setMapping(List<AttributeXRefMap> mapping)
	{
		this.mapping = mapping;
	}

}
