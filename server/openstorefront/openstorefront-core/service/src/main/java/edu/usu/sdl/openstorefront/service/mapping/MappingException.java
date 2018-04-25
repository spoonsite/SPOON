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
package edu.usu.sdl.openstorefront.service.mapping;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class MappingException
		extends Exception
{

	private static final long serialVersionUID = 1L;

	private String detailedInfo;
	private String sectionName;
	private String fieldLabel;
	private String fieldType;
	private String fieldName;
	private String mappingType;
	private String rawValue;

	public MappingException(String message)
	{
		super(message);
	}

	public MappingException(Throwable cause)
	{
		super(cause);
	}

	public MappingException(String message, Throwable cause)
	{
		super(message, cause);
	}

	@Override
	public String getLocalizedMessage()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(super.getLocalizedMessage());

		if (StringUtils.isNotBlank(getDetailedInfo())) {
			sb.append("  Details: ").append(getDetailedInfo());
		}

		if (StringUtils.isNotBlank(getSectionName())) {
			sb.append("  Section: ").append(getSectionName());
		}

		if (StringUtils.isNotBlank(getFieldLabel())) {
			sb.append("  Field label: ").append(getFieldLabel());
		}

		if (StringUtils.isNotBlank(getFieldType())) {
			sb.append("  Field Type: ").append(getFieldType());
		}

		if (StringUtils.isNotBlank(getFieldName())) {
			sb.append("  Field Name: ").append(getFieldName());
		}

		if (StringUtils.isNotBlank(getMappingType())) {
			sb.append("  Mapping Type: ").append(getMappingType());
		}

		if (StringUtils.isNotBlank(getRawValue())) {
			sb.append("  Raw Value: ").append(getRawValue());
		}

		return sb.toString();
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

	public String getRawValue()
	{
		return rawValue;
	}

	public void setRawValue(String rawValue)
	{
		this.rawValue = rawValue;
	}

	public String getDetailedInfo()
	{
		return detailedInfo;
	}

	public void setDetailedInfo(String detailedInfo)
	{
		this.detailedInfo = detailedInfo;
	}

	public String getSectionName()
	{
		return sectionName;
	}

	public void setSectionName(String sectionName)
	{
		this.sectionName = sectionName;
	}

	public String getFieldLabel()
	{
		return fieldLabel;
	}

	public void setFieldLabel(String fieldLabel)
	{
		this.fieldLabel = fieldLabel;
	}

	public String getFieldName()
	{
		return fieldName;
	}

	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

}
