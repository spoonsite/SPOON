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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.validation.CleanKeySanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
@APIDescription("Primary Key")
@Embeddable
public class ComponentAttributePk
		extends BasePK<ComponentAttributePk>
{

	@NotNull
	@FK(Component.class)
	private String componentId;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(CleanKeySanitizer.class)
	@FK(AttributeType.class)
	private String attributeType;

	//Delay sanitizing user created codes until before saving
	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@FK(AttributeCode.class)
	private String attributeCode;

	public ComponentAttributePk()
	{
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || (obj instanceof ComponentAttributePk == false)) {
			return false;
		}
		ComponentAttributePk compareObj = (ComponentAttributePk) obj;
		return pkValue().equals(compareObj.pkValue());
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 37 * hash + Objects.hashCode(getComponentId());
		hash = 37 * hash + Objects.hashCode(getAttributeType());
		hash = 37 * hash + Objects.hashCode(getAttributeCode());
		return hash;
	}

	@Override
	public String pkValue()
	{
		return getComponentId() + ReflectionUtil.COMPOSITE_KEY_SEPERATOR
				+ getAttributeType() + ReflectionUtil.COMPOSITE_KEY_SEPERATOR
				+ getAttributeCode() + ReflectionUtil.COMPOSITE_KEY_SEPERATOR;
	}

	@Override
	public String toString()
	{
		return pkValue();
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
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
