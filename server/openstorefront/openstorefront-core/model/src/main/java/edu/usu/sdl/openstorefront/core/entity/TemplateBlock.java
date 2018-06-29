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
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Used to capture custom template block for the template designer")
public class TemplateBlock
		extends StandardEntity<TemplateBlock>
{

	@PK(generated = true)
	@NotNull
	private String templateBlockId;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String name;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_64K)
	private String codeBlock;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public TemplateBlock()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		TemplateBlock templateBlock = (TemplateBlock) entity;
		this.setName(templateBlock.getName());
		this.setCodeBlock(templateBlock.getCodeBlock());
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCodeBlock()
	{
		return codeBlock;
	}

	public void setCodeBlock(String codeBlock)
	{
		this.codeBlock = codeBlock;
	}

	public String getTemplateBlockId()
	{
		return templateBlockId;
	}

	public void setTemplateBlockId(String templateBlockId)
	{
		this.templateBlockId = templateBlockId;
	}

}
