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
import edu.usu.sdl.openstorefront.validation.CleanKeySanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Section Attributes Primary Key")
@Embeddable
public class ContentSectionAttributePk
		extends BasePK<ContentSectionAttributePk>
{

	@NotNull
	@FK(ContentSection.class)
	private String contentSectionId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@Sanitize(CleanKeySanitizer.class)
	@ConsumeField
	@FK(AttributeType.class)
	private String attributeType;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@Sanitize(CleanKeySanitizer.class)
	@ConsumeField
	@FK(AttributeCode.class)
	private String attributeCode;

	public ContentSectionAttributePk()
	{
	}

	@Override
	public String pkValue()
	{
		return getContentSectionId() + ReflectionUtil.COMPOSITE_KEY_SEPERATOR
				+ getAttributeType() + ReflectionUtil.COMPOSITE_KEY_SEPERATOR
				+ getAttributeCode() + ReflectionUtil.COMPOSITE_KEY_SEPERATOR;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 53 * hash + Objects.hashCode(this.contentSectionId);
		hash = 53 * hash + Objects.hashCode(this.attributeType);
		hash = 53 * hash + Objects.hashCode(this.attributeCode);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ContentSectionAttributePk other = (ContentSectionAttributePk) obj;
		if (!Objects.equals(this.contentSectionId, other.contentSectionId)) {
			return false;
		}
		if (!Objects.equals(this.attributeType, other.attributeType)) {
			return false;
		}
		if (!Objects.equals(this.attributeCode, other.attributeCode)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return pkValue();
	}

	public String getContentSectionId()
	{
		return contentSectionId;
	}

	public void setContentSectionId(String contentSectionId)
	{
		this.contentSectionId = contentSectionId;
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
