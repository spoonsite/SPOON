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
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Primary Key for Attribute Code")
@Embeddable
public class AttributeCodePk
		extends BasePK<AttributeCodePk>
{

	public static final String FIELD_ATTRIBUTE_CODE = "attributeCode";

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(CleanKeySanitizer.class)
	@ConsumeField
	@FK(AttributeType.class)
	private String attributeType;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(CleanKeySanitizer.class)
	@ConsumeField
	private String attributeCode;

	public AttributeCodePk()
	{
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof AttributeCodePk)) {
			return false;
		}
		return toKey().equals(((AttributeCodePk) obj).toKey());
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 89 * hash + Objects.hashCode(getAttributeType());
		hash = 89 * hash + Objects.hashCode(getAttributeCode());
		return hash;
	}

	@Override
	public String pkValue()
	{
		return toKey();
	}

	@Override
	public String toString()
	{
		return toKey();
	}

	public String toKey()
	{
		return getAttributeType() + ReflectionUtil.COMPOSITE_KEY_SEPERATOR + getAttributeCode();
	}

	/**
	 * Creates a AttributeCodePk from key
	 *
	 * @param key
	 * @return Null if it can't decode key
	 */
	public static AttributeCodePk fromKey(String key)
	{
		AttributeCodePk attributeCodePk = null;
		if (StringUtils.isNotBlank(key)) {
			String keySplit[] = key.split(ReflectionUtil.COMPOSITE_KEY_SEPERATOR);
			if (keySplit.length > 1) {
				attributeCodePk = new AttributeCodePk();
				attributeCodePk.setAttributeType(keySplit[0]);
				attributeCodePk.setAttributeCode(keySplit[1]);
			}
		}
		return attributeCodePk;
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
