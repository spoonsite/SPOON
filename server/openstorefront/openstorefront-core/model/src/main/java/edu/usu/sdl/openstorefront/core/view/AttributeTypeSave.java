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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeRestriction;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper to workaround moxy binding issue
 *
 */
public class AttributeTypeSave
{

	@ConsumeField
	private AttributeType attributeType;

	@DataType(ComponentTypeRestriction.class)
	@ConsumeField
	private List<ComponentTypeRestriction> requiredComponentType = new ArrayList<>();

	@DataType(ComponentTypeRestriction.class)
	@ConsumeField
	private List<ComponentTypeRestriction> optionalComponentTypes = new ArrayList<>();

	public AttributeType getAttributeType()
	{
		return attributeType;
	}

	public void setAttributeType(AttributeType attributeType)
	{
		this.attributeType = attributeType;
	}

	public List<ComponentTypeRestriction> getRequiredComponentType()
	{
		return requiredComponentType;
	}

	public void setRequiredComponentType(List<ComponentTypeRestriction> requiredComponentType)
	{
		this.requiredComponentType = requiredComponentType;
	}

	public List<ComponentTypeRestriction> getOptionalComponentTypes()
	{
		return optionalComponentTypes;
	}

	public void setOptionalComponentTypes(List<ComponentTypeRestriction> optionalComponentTypes)
	{
		this.optionalComponentTypes = optionalComponentTypes;
	}

}
