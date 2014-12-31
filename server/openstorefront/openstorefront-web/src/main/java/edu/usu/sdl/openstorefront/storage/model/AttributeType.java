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
package edu.usu.sdl.openstorefront.storage.model;

import edu.usu.sdl.openstorefront.doc.ConsumeField;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.PK;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
public class AttributeType
		extends BaseEntity
{

	@PK
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ConsumeField
	private String attributeType;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_DESCRIPTION)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String description;

	@NotNull
	@ConsumeField
	private Boolean visibleFlg;

	@NotNull
	@ConsumeField
	private Boolean requiredFlg;

	@NotNull
	@ConsumeField
	private Boolean architectureFlg;

	@NotNull
	@ConsumeField
	private Boolean importantFlg;

	@NotNull
	@ConsumeField
	private Boolean allowMultipleFlg;

	public static final String DI2E_SVCV4 = "DI2E-SVCV4-A";
	public static final String TYPE = "TYPE";
	public static final String DI2ELEVEL = "DI2ELEVEL";

	public AttributeType()
	{
	}

	@Override
	public int hashCode()
	{
		int hash = 0;
		hash += (attributeType != null ? attributeType.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof AttributeType)) {
			return false;
		}
		AttributeType other = (AttributeType) object;
		if (this.attributeType == null && other.attributeType == null) {
			return super.equals(object);
		}
		if ((this.attributeType == null && other.attributeType != null) || (this.attributeType != null && !this.attributeType.equals(other.attributeType))) {
			return false;
		}
		return true;
	}

	public String getAttributeType()
	{
		return attributeType;
	}

	public void setAttributeType(String attributeType)
	{
		this.attributeType = attributeType;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Boolean getVisibleFlg()
	{
		return visibleFlg;
	}

	public void setVisibleFlg(Boolean visibleFlg)
	{
		this.visibleFlg = visibleFlg;
	}

	public Boolean getRequiredFlg()
	{
		return requiredFlg;
	}

	public void setRequiredFlg(Boolean requiredFlg)
	{
		this.requiredFlg = requiredFlg;
	}

	public Boolean getArchitectureFlg()
	{
		return architectureFlg;
	}

	public void setArchitectureFlg(Boolean architectureFlg)
	{
		this.architectureFlg = architectureFlg;
	}

	public Boolean getImportantFlg()
	{
		return importantFlg;
	}

	public void setImportantFlg(Boolean importantFlg)
	{
		this.importantFlg = importantFlg;
	}

	public Boolean getAllowMultipleFlg()
	{
		return allowMultipleFlg;
	}

	public void setAllowMultipleFlg(Boolean allowMultipleFlg)
	{
		this.allowMultipleFlg = allowMultipleFlg;
	}

}
