/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.validation.CleanKeySanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
public class TopicSearchItem
		extends StandardEntity
{

	@PK(generated = true)
	@NotNull
	private String entityId;
	
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String brandingId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@Sanitize(CleanKeySanitizer.class)
	@ConsumeField
	private String attributeType;

	public TopicSearchItem()
	{
	}

	/**
	 * @return the brandingId
	 */
	public String getBrandingId()
	{
		return brandingId;
	}

	/**
	 * @param brandingId the brandingId to set
	 */
	public void setBrandingId(String brandingId)
	{
		this.brandingId = brandingId;
	}

	/**
	 * @return the attributeType
	 */
	public String getAttributeType()
	{
		return attributeType;
	}

	/**
	 * @param attributeType the attributeType to set
	 */
	public void setAttributeType(String attributeType)
	{
		this.attributeType = attributeType;
	}

	/**
	 * @return the entityId
	 */
	public String getEntityId()
	{
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(String entityId)
	{
		this.entityId = entityId;
	}

}
