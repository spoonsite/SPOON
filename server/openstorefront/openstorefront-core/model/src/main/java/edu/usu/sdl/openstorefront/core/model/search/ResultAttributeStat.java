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

/**
 *
 * @author rfrazier
 */
public class ResultAttributeStat
{
	private String attributeCode;
	private String attributeCodeLabel;
	private String attributeType;
	private String attributeTypeLabel;
	private int count = 1;
	
	public ResultAttributeStat()
	{
	}

	public String getAttributeCode()
	{
		return attributeCode;
	}

	public void setAttributeCode(String attributeCode)
	{
		this.attributeCode = attributeCode;
	}

	public String getAttributeCodeLabel()
	{
		return attributeCodeLabel;
	}

	public void setAttributeCodeLabel(String attributeCodeLabel)
	{
		this.attributeCodeLabel = attributeCodeLabel;
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
}
