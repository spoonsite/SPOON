/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import java.util.List;

/**
 *
 * @author bmichaelis
 */
public class AttributeTypeListMerge
{
	
	@ConsumeField
	private AttributeTypeSave attributeTypeSave;
	
	@ConsumeField
	private List<String> attributesTypesToBeDeleted;

	/**
	 * @return the attributeTypeSave
	 */
	public AttributeTypeSave getAttributeTypeSave()
	{
		return attributeTypeSave;
	}

	/**
	 * @param attributeTypeSave the attributeTypeSave to set
	 */
	public void setAttributeTypeSave(AttributeTypeSave attributeTypeSave)
	{
		this.attributeTypeSave = attributeTypeSave;
	}

	/**
	 * @return the attributesTypesToBeDeleted
	 */
	public List<String> getAttributesTypesToBeDeleted()
	{
		return attributesTypesToBeDeleted;
	}

	/**
	 * @param attributesTypesToBeDeleted the attributesTypesToBeDeleted to set
	 */
	public void setAttributesTypesToBeDeleted(List<String> attributesTypesToBeDeleted)
	{
		this.attributesTypesToBeDeleted = attributesTypesToBeDeleted;
	}

	
}
