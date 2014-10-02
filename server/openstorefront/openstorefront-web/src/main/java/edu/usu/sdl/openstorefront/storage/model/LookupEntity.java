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
import edu.usu.sdl.openstorefront.validation.BasicHTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
public abstract class LookupEntity
		extends BaseEntity
{

	@PK
	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	protected String code;

	@NotNull
	@ConsumeField
	@Sanitize(TextSanitizer.class)
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_DESCRIPTION)
	protected String description;

	@ConsumeField
	@Sanitize(BasicHTMLSanitizer.class)
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_DETAILED_DESCRIPTION)
	private String detailedDecription;

	private Integer sortOrder;

	public LookupEntity()
	{
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDetailedDecription()
	{
		return detailedDecription;
	}

	public void setDetailedDecription(String detailedDecription)
	{
		this.detailedDecription = detailedDecription;
	}

	public Integer getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	@Override
	public int hashCode()
	{
		int hash = 0;
		hash += (code != null ? code.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object)
	{
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof LookupEntity)) {
			return false;
		}
		LookupEntity other = (LookupEntity) object;
		if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return this.getClass().getName() + "[ code=" + code + " ]";
	}

}
