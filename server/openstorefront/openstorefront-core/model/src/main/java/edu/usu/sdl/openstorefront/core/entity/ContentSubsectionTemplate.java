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

import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
public class ContentSubsectionTemplate
		extends StandardEntity<ContentSubsectionTemplate>
{

	@PK(generated = true)
	@NotNull
	private String templateId;

	@NotNull
	@FK(value = ContentSectionTemplate.class, referencedField = "templateId")
	private String sectionTemplateId;

	@NotNull
	@FK(ContentSubSection.class)
	private String contentSubSectionId;

	public ContentSubsectionTemplate()
	{
	}

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	public String getContentSubSectionId()
	{
		return contentSubSectionId;
	}

	public void setContentSubSectionId(String contentSubSectionId)
	{
		this.contentSubSectionId = contentSubSectionId;
	}

	public String getSectionTemplateId()
	{
		return sectionTemplateId;
	}

	public void setSectionTemplateId(String sectionTemplateId)
	{
		this.sectionTemplateId = sectionTemplateId;
	}

}
