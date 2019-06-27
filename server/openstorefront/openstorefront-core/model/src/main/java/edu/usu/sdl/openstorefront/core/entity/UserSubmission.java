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
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.jsoup.helper.StringUtil;

/**
 *
 * @author dshurtleff
 */
public class UserSubmission
		extends StandardEntity<UserSubmission>
{

	private static final long serialVersionUID = 1L;

	private Boolean isQueued = false;

	@PK(generated = true)
	@NotNull
	private String userSubmissionId;

	@NotNull
	@ConsumeField
	@FK(SubmissionTemplateStatus.class)
	private String templateId;

	@NotNull
	@ConsumeField
	@APIDescription("Type of listing")
	@FK(value = ComponentType.class, enforce = true)
	private String componentType;

	@ConsumeField
	@FK(Component.class)
	private String originalComponentId;

	@ConsumeField
	@DataType(UserSubmissionField.class)
	@Embedded
	@OneToMany(orphanRemoval = true)
	private List<UserSubmissionField> fields;

	@NotNull
	private String ownerUsername;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_COMPONENT_NAME)
	@Sanitize(TextSanitizer.class)
	private String submissionName;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public UserSubmission()
	{
	}

	@Override
	public String entityOwner()
	{
		return getOwnerUsername();
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		UserSubmission userSubmission = (UserSubmission) entity;
		this.setFields(userSubmission.getFields());
		if (getFields() != null) {
			for (UserSubmissionField userSubmissionField : getFields()) {
				if (StringUtil.isBlank(userSubmissionField.getFieldId())) {
					userSubmissionField.setFieldId(StringProcessor.uniqueId());
				}
			}
		}
		this.setSubmissionName(userSubmission.getSubmissionName());
		this.setIsQueued(userSubmission.getIsQueued());
	}

	public String getUserSubmissionId()
	{
		return userSubmissionId;
	}

	public void setUserSubmissionId(String userSubmissionId)
	{
		this.userSubmissionId = userSubmissionId;
	}

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

	public String getOriginalComponentId()
	{
		return originalComponentId;
	}

	public void setOriginalComponentId(String originalComponentId)
	{
		this.originalComponentId = originalComponentId;
	}

	public List<UserSubmissionField> getFields()
	{
		return fields;
	}

	public void setFields(List<UserSubmissionField> fields)
	{
		this.fields = fields;
	}

	public String getComponentType()
	{
		return componentType;
	}

	public void setComponentType(String componentType)
	{
		this.componentType = componentType;
	}

	public String getOwnerUsername()
	{
		return ownerUsername;
	}

	public void setOwnerUsername(String ownerUsername)
	{
		this.ownerUsername = ownerUsername;
	}

	public String getSubmissionName()
	{
		return submissionName;
	}

	public void setSubmissionName(String submissionName)
	{
		this.submissionName = submissionName;
	}

	public Boolean getIsQueued() {
		return isQueued;
	}

	public void setIsQueued(Boolean isQueued) {
		this.isQueued = isQueued;
	}
}
