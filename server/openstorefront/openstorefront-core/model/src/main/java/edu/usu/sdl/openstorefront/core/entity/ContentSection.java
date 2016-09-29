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
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Represents and section of content")
public class ContentSection
{

	@PK(generated = true)
	@NotNull
	private String contentSectionId;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_255)
	private String entityId;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_255)
	private String entity;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	private String title;

	@NotNull
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_64K)
	@Sanitize(HTMLSanitizer.class)
	private String content;

	@NotNull
	@ConsumeField
	private Boolean privateSection;

	@NotNull
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = WorkflowStatus.class)
	@FK(WorkflowStatus.class)
	private String workflowStatus;

	public ContentSection()
	{
	}

	public String getContentSectionId()
	{
		return contentSectionId;
	}

	public void setContentSectionId(String contentSectionId)
	{
		this.contentSectionId = contentSectionId;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public Boolean getPrivateSection()
	{
		return privateSection;
	}

	public void setPrivateSection(Boolean privateSection)
	{
		this.privateSection = privateSection;
	}

	public String getWorkflowStatus()
	{
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus)
	{
		this.workflowStatus = workflowStatus;
	}

	public String getEntityId()
	{
		return entityId;
	}

	public void setEntityId(String entityId)
	{
		this.entityId = entityId;
	}

	public String getEntity()
	{
		return entity;
	}

	public void setEntity(String entity)
	{
		this.entity = entity;
	}

}
