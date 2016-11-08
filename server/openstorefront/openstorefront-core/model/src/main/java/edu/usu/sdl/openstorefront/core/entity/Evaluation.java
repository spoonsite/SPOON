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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Represents an entry evaluation")
public class Evaluation
		extends StandardEntity<Evaluation>
{

	@PK(generated = true)
	@NotNull
	private String evaluationId;

	@ConsumeField
	@NotNull
	@FK(Component.class)
	private String componentId;

	@ConsumeField
	@NotNull
	@APIDescription("Soft reference to the template use to create the evaluation; Template may be removed.")
	@FK(EvaluationTemplate.class)
	private String templateId;

	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String version;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	@FK(value = UserProfile.class, referencedField = "username")
	private String assignedUser;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String assignedGroup;

	@NotNull
	private Boolean published;

	public Evaluation()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		Evaluation evaluation = (Evaluation) entity;
		setAssignedGroup(evaluation.getAssignedGroup());
		setAssignedUser(evaluation.getAssignedUser());
		setComponentId(evaluation.getComponentId());
		setTemplateId(evaluation.getTemplateId());
		setPublished(evaluation.getPublished());
		setVersion(evaluation.getVersion());

	}

	public String getEvaluationId()
	{
		return evaluationId;
	}

	public void setEvaluationId(String evaluationId)
	{
		this.evaluationId = evaluationId;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getAssignedUser()
	{
		return assignedUser;
	}

	public void setAssignedUser(String assignedUser)
	{
		this.assignedUser = assignedUser;
	}

	public String getAssignedGroup()
	{
		return assignedGroup;
	}

	public void setAssignedGroup(String assignedGroup)
	{
		this.assignedGroup = assignedGroup;
	}

	public Boolean getPublished()
	{
		return published;
	}

	public void setPublished(Boolean published)
	{
		this.published = published;
	}

	public String getTemplateId()
	{
		return templateId;
	}

	public void setTemplateId(String templateId)
	{
		this.templateId = templateId;
	}

}
