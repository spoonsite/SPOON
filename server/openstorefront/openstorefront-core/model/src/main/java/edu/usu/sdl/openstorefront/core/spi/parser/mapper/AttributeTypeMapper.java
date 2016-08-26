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
package edu.usu.sdl.openstorefront.core.spi.parser.mapper;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
public class AttributeTypeMapper
{
	private String attributeType;
	private String defaultMappedCode;
	private boolean addMissingCode;
	private Map<String, String> codeMap = new HashMap<>();

	public AttributeTypeMapper()
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

	public String getDefaultMappedCode()
	{
		return defaultMappedCode;
	}

	public void setDefaultMappedCode(String defaultMappedCode)
	{
		this.defaultMappedCode = defaultMappedCode;
	}

	public boolean getAddMissingCode()
	{
		return addMissingCode;
	}

	public void setAddMissingCode(boolean addMissingCode)
	{
		this.addMissingCode = addMissingCode;
	}

	public Map<String, String> getCodeMap()
	{
		return codeMap;
	}

	public void setCodeMap(Map<String, String> codeMap)
	{
		this.codeMap = codeMap;
	}

}
