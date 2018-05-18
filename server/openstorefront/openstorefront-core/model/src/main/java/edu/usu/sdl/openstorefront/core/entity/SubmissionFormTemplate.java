/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubmissionFormTemplate
		extends StandardEntity<SubmissionFormTemplate>
{

	private static final long serialVersionUID = 1L;

	@PK(generated = true)
	@NotNull
	private String submissionTemplateId;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	private String name;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_1K)
	@Sanitize(TextSanitizer.class)
	private String description;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = SubmissionTemplateStatus.class)
	@FK(SubmissionTemplateStatus.class)
	private String templateStatus;

	@ConsumeField
	@DataType(SubmissionFormSection.class)
	@Embedded
	@OneToMany(orphanRemoval = true)
	private List<SubmissionFormSection> sections;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public SubmissionFormTemplate()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		SubmissionFormTemplate template = (SubmissionFormTemplate) entity;

		this.setName(template.getName());
		this.setDescription(template.getDescription());
		this.setTemplateStatus(template.getTemplateStatus());
		this.setSections(template.getSections());
		updateSectionLinks();

	}

	public void updateSectionLinks()
	{
		if (getSections() != null) {
			for (SubmissionFormSection section : getSections()) {
				section.setTemplateId(getSubmissionTemplateId());
				section.updateFieldLinks();
			}
		}
	}

	public String getSubmissionTemplateId()
	{
		return submissionTemplateId;
	}

	public void setSubmissionTemplateId(String submissionTemplateId)
	{
		this.submissionTemplateId = submissionTemplateId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getTemplateStatus()
	{
		return templateStatus;
	}

	public void setTemplateStatus(String templateStatus)
	{
		this.templateStatus = templateStatus;
	}

	public List<SubmissionFormSection> getSections()
	{
		return sections;
	}

	public void setSections(List<SubmissionFormSection> sections)
	{
		this.sections = sections;
	}

}
