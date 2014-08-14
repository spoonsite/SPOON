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

package edu.usu.sdl.openstorefront.validation;

/**
 *
 * @author dshurtleff
 */
public class RuleResult
{
	private String message;
	private String invalidData;
	private String validationRule;
	private String entityClassName;
	private String fieldName;

	public RuleResult()
	{
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Message: ").append(message).append("\n");	
		sb.append("Field Name: ").append(fieldName).append("\n");
		sb.append("Value: ").append(invalidData).append("\n");		
		sb.append("Validation Rule: ").append(validationRule).append("\n");
		sb.append("Part of Type: ").append(entityClassName).append("\n");		
		return sb.toString();
	}
	
	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getInvalidData()
	{
		return invalidData;
	}

	public void setInvalidData(String invalidData)
	{
		this.invalidData = invalidData;
	}

	public String getValidationRule()
	{
		return validationRule;
	}

	public void setValidationRule(String validationRule)
	{
		this.validationRule = validationRule;
	}

	public String getEntityClassName()
	{
		return entityClassName;
	}

	public void setEntityClassName(String entityClassName)
	{
		this.entityClassName = entityClassName;
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
