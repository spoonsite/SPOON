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
package edu.usu.sdl.openstorefront.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the Architecture hierarchy
 *
 * @author dshurtleff
 */
public class Architecture
{

	private String name;
	private String description;
	private String attributeType;
	private String attributeCode;
	private String originalAttributeCode;
	private String architectureCode;
	private Integer sortOrder;
	private List<Architecture> children = new ArrayList<>();

	public Architecture()
	{
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getAttributeType()
	{
		return attributeType;
	}

	public void setAttributeType(String attributeType)
	{
		this.attributeType = attributeType;
	}

	public String getAttributeCode()
	{
		return attributeCode;
	}

	public void setAttributeCode(String attributeCode)
	{
		this.attributeCode = attributeCode;
	}

	public List<Architecture> getChildren()
	{
		return children;
	}

	public void setChildren(List<Architecture> children)
	{
		this.children = children;
	}

	public String getArchitectureCode()
	{
		return architectureCode;
	}

	public void setArchitectureCode(String architectureCode)
	{
		this.architectureCode = architectureCode;
	}

	public String getOriginalAttributeCode()
	{
		return originalAttributeCode;
	}

	public void setOriginalAttributeCode(String originalAttributeCode)
	{
		this.originalAttributeCode = originalAttributeCode;
	}

	public Integer getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder)
	{
		this.sortOrder = sortOrder;
	}

}
