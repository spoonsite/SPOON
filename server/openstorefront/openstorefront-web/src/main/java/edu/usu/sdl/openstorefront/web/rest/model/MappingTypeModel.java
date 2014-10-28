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

import edu.usu.sdl.openstorefront.storage.model.XRefAttributeMap;
import java.util.List;

/**
 *
 * @author jlaw
 */
public class MappingTypeModel
{

	private String projectType;
	private String issueType;
	private String fieldKey;
	private String attributeType;
	private String fieldName;
	private String fieldId;
	private String attributeName;
	private List<XRefAttributeMap> mapping;

	public MappingTypeModel()
	{

	}

	/**
	 * @return the projectType
	 */
	public String getProjectType()
	{
		return projectType;
	}

	/**
	 * @param projectType the projectType to set
	 */
	public void setProjectType(String projectType)
	{
		this.projectType = projectType;
	}

	/**
	 * @return the issueType
	 */
	public String getIssueType()
	{
		return issueType;
	}

	/**
	 * @param issueType the issueType to set
	 */
	public void setIssueType(String issueType)
	{
		this.issueType = issueType;
	}

	/**
	 * @return the attributeType
	 */
	public String getAttributeType()
	{
		return attributeType;
	}

	/**
	 * @param attributeType the attributeType to set
	 */
	public void setAttributeType(String attributeType)
	{
		this.attributeType = attributeType;
	}

	/**
	 * @return the fieldKey
	 */
	public String getFieldKey()
	{
		return fieldKey;
	}

	/**
	 * @param fieldKey the fieldKey to set
	 */
	public void setFieldKey(String fieldKey)
	{
		this.fieldKey = fieldKey;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName()
	{
		return fieldName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	/**
	 * @return the fieldId
	 */
	public String getFieldId()
	{
		return fieldId;
	}

	/**
	 * @param fieldId the fieldId to set
	 */
	public void setFieldId(String fieldId)
	{
		this.fieldId = fieldId;
	}

	/**
	 * @return the attributeName
	 */
	public String getAttributeName()
	{
		return attributeName;
	}

	/**
	 * @param attributeName the attributeName to set
	 */
	public void setAttributeName(String attributeName)
	{
		this.attributeName = attributeName;
	}

	/**
	 * @return the mapping
	 */
	public List<XRefAttributeMap> getMapping()
	{
		return mapping;
	}

	/**
	 * @param mapping the mapping to set
	 */
	public void setMapping(List<XRefAttributeMap> mapping)
	{
		this.mapping = mapping;
	}
}
