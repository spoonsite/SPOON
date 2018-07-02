/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentSimpleAttributeView
{

	private String componentId;
	private String name;
	private String componentType;
	private String componentTypeDescription;

	@DataType(ComponentAttributeView.class)
	private List<ComponentAttributeView> attributes = new ArrayList<>();

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getComponentType()
	{
		return componentType;
	}

	public void setComponentType(String componentType)
	{
		this.componentType = componentType;
	}

	public String getComponentTypeDescription()
	{
		return componentTypeDescription;
	}

	public void setComponentTypeDescription(String componentTypeDescription)
	{
		this.componentTypeDescription = componentTypeDescription;
	}

	public List<ComponentAttributeView> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(List<ComponentAttributeView> attributes)
	{
		this.attributes = attributes;
	}

}
