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
package edu.usu.sdl.openstorefront.core.model.search;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rfrazier
 */
public class ResultAttributeStat
{
	private Map<String, ResultCodeStat> codeMap = new HashMap<>();
	private String attributeType;
	private String attributeTypeLabel;
	private String attributeUnit;
	private int count = 1;
	
	public ResultAttributeStat()
	{
	}

	public Map<String, ResultCodeStat> getCodeMap()
	{
		return codeMap;
	}

	public void setCodeMap(Map<String, ResultCodeStat> codeMap)
	{
		this.codeMap = codeMap;
	}

	public String getAttributeType()
	{
		return attributeType;
	}

	public void setAttributeType(String attributeType)
	{
		this.attributeType = attributeType;
	}

	public String getAttributeTypeLabel()
	{
		return attributeTypeLabel;
	}

	public void setAttributeTypeLabel(String attributeTypeLabel)
	{
		this.attributeTypeLabel = attributeTypeLabel;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}
		
	public void incrementCount()
	{
		this.count += 1;
	}

	public String getAttributeUnit()
	{
		return attributeUnit;
	}

	public void setAttributeUnit(String attributeUnit)
	{
		this.attributeUnit = attributeUnit;
	}
}
