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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This base abstraction for attributes
 *
 * @author dshurtleff
 */
@APIDescription("This is base abstraction for attributes")
public abstract class Attribute
		extends BaseEntity<Attribute>
{

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@FK(AttributeType.class)
	private String attributeType;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@FK(AttributeCode.class)
	private String attributeCode;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public Attribute()
	{
	}

	public abstract String ownerId();

	/**
	 * This compare just the attribute information
	 *
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(Attribute o)
	{
		int value = ReflectionUtil.compareObjects(getAttributeType(), o.getAttributeType());
		if (value == 0) {
			value = ReflectionUtil.compareObjects(getAttributeCode(), o.getAttributeCode());
		}
		return value;
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

}
