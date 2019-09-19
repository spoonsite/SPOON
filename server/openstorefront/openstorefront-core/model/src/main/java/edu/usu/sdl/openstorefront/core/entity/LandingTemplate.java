/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Version;
import javax.validation.constraints.Size;

/**
 * @deprecated in v2.10, as part of removal of custom Landing pages feature.
 * @author dshurtleff
 */
@APIDescription("This is part of branding; DEPRECATED")
@Embeddable
public class LandingTemplate
		implements Serializable
{

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_1MB)
	@ConsumeField
	@APIDescription("This holds generated template code")
	private String generatedCode;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_32K)
	@ConsumeField
	@APIDescription("Holds the structure code of the start of the template")
	private String preStructureCode;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_32K)
	@ConsumeField
	@APIDescription("Holds pre template code")
	private String preTemplateCode;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_32K)
	@ConsumeField
	@APIDescription("Holds pre template code")
	private String postTemplateCode;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_32K)
	@ConsumeField
	@APIDescription("Holds the structure code of the end of the template")
	private String postStructureCode;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_32K)
	@APIDescription("This is used to persist the visual designer")
	private String templateBlocks;

	@Version
	private String storageVersion;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public LandingTemplate()
	{
	}

	public String fullTemplate()
	{
		String fullTemplate
				= StringProcessor.blankIfNull(getPreStructureCode())
				+ StringProcessor.blankIfNull(getPreTemplateCode())
				+ StringProcessor.blankIfNull(getGeneratedCode())
				+ StringProcessor.blankIfNull(getPostTemplateCode())
				+ StringProcessor.blankIfNull(getPostStructureCode());

		return fullTemplate;
	}

	public String getStorageVersion()
	{
		return storageVersion;
	}

	public void setStorageVersion(String storageVersion)
	{
		this.storageVersion = storageVersion;
	}

	public String getPreStructureCode()
	{
		return preStructureCode;
	}

	public void setPreStructureCode(String preStructureCode)
	{
		this.preStructureCode = preStructureCode;
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

	public String getPostStructureCode()
	{
		return postStructureCode;
	}

	public void setPostStructureCode(String postStructureCode)
	{
		this.postStructureCode = postStructureCode;
	}

	public String getTemplateBlocks()
	{
		return templateBlocks;
	}

	public void setTemplateBlocks(String templateBlocks)
	{
		this.templateBlocks = templateBlocks;
	}

	public String getGeneratedCode()
	{
		return generatedCode;
	}

	public void setGeneratedCode(String generatedCode)
	{
		this.generatedCode = generatedCode;
	}

}
