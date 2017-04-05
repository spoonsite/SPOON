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
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.model.FieldChangeModel;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.List;
import java.util.Set;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Represents a fragment of a section")
public class ContentSubSection
		extends StandardEntity<ContentSubSection>
		implements LoggableModel<ContentSubSection>
{

	@PK(generated = true)
	@NotNull
	private String subSectionId;

	@NotNull
	@FK(ContentSection.class)
	private String contentSectionId;

	@NotNull
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	private String title;

	@NotNull
	@ConsumeField
	private Boolean hideTitle;

	@NotNull
	@ConsumeField
	private Boolean privateSection;

	@NotNull
	@ConsumeField
	private Boolean noContent;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_32K)
	@Sanitize(HTMLSanitizer.class)
	private String content;

	@NotNull
	@ConsumeField
	private Integer order;

	@ConsumeField
	@DataType(CustomField.class)
	@Embedded
	@OneToMany(orphanRemoval = true)
	private List<CustomField> customFields;

	public ContentSubSection()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		ContentSubSection contentSubSection = (ContentSubSection) entity;
		ServiceProxyFactory.getServiceProxy().getChangeLogService().findUpdateChanges(this, contentSubSection);
		super.updateFields(entity);

		setContent(contentSubSection.getContent());
		setCustomFields(contentSubSection.getCustomFields());
		setHideTitle(contentSubSection.getHideTitle());
		setNoContent(contentSubSection.getNoContent());
		setPrivateSection(contentSubSection.getPrivateSection());
		setContentSectionId(contentSubSection.getContentSectionId());
		setTitle(contentSubSection.getTitle());
		setOrder(contentSubSection.getOrder());

	}

	@Override
	public List<FieldChangeModel> findChanges(ContentSubSection updated)
	{
		Set<String> excludeFields = excludedChangeFields();
		excludeFields.add("subSectionId");
		excludeFields.add("contentSectionId");
		List<FieldChangeModel> changes = FieldChangeModel.allChangedFields(excludeFields, this, updated);
		return changes;
	}

	@Override
	public String addRemoveComment()
	{
		return getTitle();
	}

	@Override
	public void setChangeParent(ChangeLog changeLog)
	{
		changeLog.setParentEntity(ContentSection.class.getSimpleName());
		changeLog.setParentEntityId(getContentSectionId());
	}

	public String getSubSectionId()
	{
		return subSectionId;
	}

	public void setSubSectionId(String subSectionId)
	{
		this.subSectionId = subSectionId;
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

	public Boolean getPrivateSection()
	{
		return privateSection;
	}

	public void setPrivateSection(Boolean privateSection)
	{
		this.privateSection = privateSection;
	}

	public List<CustomField> getCustomFields()
	{
		return customFields;
	}

	public void setCustomFields(List<CustomField> customFields)
	{
		this.customFields = customFields;
	}

	public Boolean getHideTitle()
	{
		return hideTitle;
	}

	public void setHideTitle(Boolean hideTitle)
	{
		this.hideTitle = hideTitle;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public Boolean getNoContent()
	{
		return noContent;
	}

	public void setNoContent(Boolean noContent)
	{
		this.noContent = noContent;
	}

	public Integer getOrder()
	{
		return order;
	}

	public void setOrder(Integer order)
	{
		this.order = order;
	}

}
