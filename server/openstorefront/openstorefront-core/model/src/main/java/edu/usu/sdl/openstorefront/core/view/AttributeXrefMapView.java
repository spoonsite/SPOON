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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.AttributeXRefMap;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author jlaw
 */
public class AttributeXrefMapView
{

	private String projectType;
	private String issueType;
	private String attributeType;
	private String fieldName;
	private String fieldId;
	private String attributeName;

	@DataType(AttributeXRefMap.class)
	private List<AttributeXRefMap> mapping;

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 47 * hash + Objects.hashCode(this.projectType);
		hash = 47 * hash + Objects.hashCode(this.issueType);
		hash = 47 * hash + Objects.hashCode(this.attributeType);
		hash = 47 * hash + Objects.hashCode(this.fieldName);
		hash = 47 * hash + Objects.hashCode(this.fieldId);
		hash = 47 * hash + Objects.hashCode(this.attributeName);
		hash = 47 * hash + Objects.hashCode(this.mapping);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AttributeXrefMapView other = (AttributeXrefMapView) obj;
		if (!Objects.equals(this.projectType, other.projectType)) {
			return false;
		}
		if (!Objects.equals(this.issueType, other.issueType)) {
			return false;
		}
		if (!Objects.equals(this.attributeType, other.attributeType)) {
			return false;
		}
		if (!Objects.equals(this.fieldName, other.fieldName)) {
			return false;
		}
		if (!Objects.equals(this.fieldId, other.fieldId)) {
			return false;
		}
		if (!Objects.equals(this.attributeName, other.attributeName)) {
			return false;
		}
		if (!Objects.equals(this.mapping, other.mapping)) {
			return false;
		}
		return true;
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
