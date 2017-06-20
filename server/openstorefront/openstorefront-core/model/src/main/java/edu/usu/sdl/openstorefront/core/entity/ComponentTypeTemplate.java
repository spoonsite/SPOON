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
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Definable template for displaying a component")
public class ComponentTypeTemplate
		extends StandardEntity<ComponentTypeTemplate>
{

	@PK(generated = true)
	@NotNull
	private String templateId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String name;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_1MB)
	@ConsumeField
	@APIDescription("This contains the full template code")
	private String template;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_32K)
	@ConsumeField
	@APIDescription("This is used to persist the template design")
	private String preTemplateCode;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_32K)
	@ConsumeField
	@APIDescription("This is used to persist the template design")
	private String postTemplateCode;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_32K)
	@APIDescription("This is used to persist the visual designer")
	private String templateBlocks;

	public ComponentTypeTemplate()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		ComponentTypeTemplate componentTypeTemplate = (ComponentTypeTemplate) entity;
		this.setName(componentTypeTemplate.getName());
		this.setTemplate(componentTypeTemplate.getTemplate());
		this.setPreTemplateCode(componentTypeTemplate.getPreTemplateCode());
		this.setPostTemplateCode(componentTypeTemplate.getPostTemplateCode());
		this.setTemplateBlocks(componentTypeTemplate.getTemplateBlocks());
	}

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getTemplate()
	{
		return template;
	}

	public void setTemplate(String template)
	{
		this.template = template;
	}

	public String getPreTemplateCode()
	{
		return preTemplateCode;
	}

	public void setPreTemplateCode(String preTemplateCode)
	{
		this.preTemplateCode = preTemplateCode;
	}

	public String getPostTemplateCode()
	{
		return postTemplateCode;
	}

	public void setPostTemplateCode(String postTemplateCode)
	{
		this.postTemplateCode = postTemplateCode;
	}

	public String getTemplateBlocks()
	{
		return templateBlocks;
	}

	public void setTemplateBlocks(String templateBlocks)
	{
		this.templateBlocks = templateBlocks;
	}

}
