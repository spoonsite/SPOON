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
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
@APIDescription("Mapping of external categories to attributes")
public class AttributeXRefMap
		extends StandardEntity<AttributeXRefMap>
{

	@PK(generated = true)
	@NotNull
	private String xrefId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ConsumeField
	@FK(AttributeType.class)
	private String attributeType;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ConsumeField
	@APIDescription("application attribute code")
	private String localCode;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@ConsumeField
	@APIDescription("external system code")
	private String externalCode;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public AttributeXRefMap()
	{

	}

	public String getXrefId()
	{
		return xrefId;
	}

	public void setXrefId(String xrefId)
	{
		this.xrefId = xrefId;
	}

	public String getAttributeType()
	{
		return attributeType;
	}

	public void setAttributeType(String attributeType)
	{
		this.attributeType = attributeType;
	}

	public String getLocalCode()
	{
		return localCode;
	}

	public void setLocalCode(String localCode)
	{
		this.localCode = localCode;
	}

	public String getExternalCode()
	{
		return externalCode;
	}

	public void setExternalCode(String externalCode)
	{
		this.externalCode = externalCode;
	}

}
