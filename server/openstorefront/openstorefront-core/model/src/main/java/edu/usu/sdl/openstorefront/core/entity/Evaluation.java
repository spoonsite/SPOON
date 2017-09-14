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
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.model.FieldChangeModel;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Represents an entry evaluation")
public class Evaluation
		extends StandardEntity<Evaluation>
		implements LoggableModel<Evaluation>
{

	public static final String FIELD_PUBLISHED = "published";

	@PK(generated = true)
	@NotNull
	private String evaluationId;

	@ConsumeField
	@NotNull
	@FK(Component.class)
	@APIDescription("Id of the change request")
	private String componentId;

	@NotNull
	@FK(Component.class)
	@APIDescription("Id of the entry")
	private String originComponentId;

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
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = WorkflowStatus.class)
	@FK(WorkflowStatus.class)
	private String workflowStatus;

	@NotNull
	private Boolean published;

	@NotNull
	@ConsumeField
	private Boolean allowNewSections;

	@NotNull
	@ConsumeField
	private Boolean allowNewSubSections;

	@ConsumeField
	private Boolean allowQuestionManagement;

	@ConsumeField
	@APIDescription("True if there has been a change to the template, that was not updated in the evaluation; otherwise False")
	private Boolean templateUpdatePending;

	@ConsumeField
	private Date lastSummaryApprovedDts;

	public Evaluation()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		Evaluation evaluation = (Evaluation) entity;
		ServiceProxyFactory.getServiceProxy().getChangeLogService().findUpdateChanges(this, evaluation);
		super.updateFields(entity);

		setAssignedGroup(evaluation.getAssignedGroup());
		setAssignedUser(evaluation.getAssignedUser());
		setComponentId(evaluation.getComponentId());
		setTemplateId(evaluation.getTemplateId());
		setPublished(evaluation.getPublished());
		setVersion(evaluation.getVersion());
		setWorkflowStatus(evaluation.getWorkflowStatus());
		setAllowNewSections(evaluation.getAllowNewSections());
		setAllowNewSubSections(evaluation.getAllowNewSubSections());
		setAllowQuestionManagement(evaluation.getAllowQuestionManagement());
		setTemplateUpdatePending(evaluation.getTemplateUpdatePending());
		setLastSummaryApprovedDts(evaluation.getLastSummaryApprovedDts());
	}

	@Override
	public List<FieldChangeModel> findChanges(Evaluation updated)
	{
		Set<String> excludeFields = excludedChangeFields();
		excludeFields.add("evaluationId");
		excludeFields.add("componentId");
		excludeFields.add("originComponentId");
		excludeFields.add("templateId");
		List<FieldChangeModel> changes = FieldChangeModel.allChangedFields(excludeFields, this, updated);
		return changes;
	}

	@Override
	public String addRemoveComment()
	{
		return getVersion();
	}

	@Override
	public void setChangeParent(ChangeLog changeLog)
	{
		//top-level
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

	public String getOriginComponentId()
	{
		return originComponentId;
	}

	public void setOriginComponentId(String originComponentId)
	{
		this.originComponentId = originComponentId;
	}

	public String getWorkflowStatus()
	{
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus)
	{
		this.workflowStatus = workflowStatus;
	}

	public Boolean getAllowNewSections()
	{
		return allowNewSections;
	}

	public void setAllowNewSections(Boolean allowNewSections)
	{
		this.allowNewSections = allowNewSections;
	}

	public Boolean getAllowNewSubSections()
	{
		return allowNewSubSections;
	}

	public void setAllowNewSubSections(Boolean allowNewSubSections)
	{
		this.allowNewSubSections = allowNewSubSections;
	}

	public Boolean getAllowQuestionManagement()
	{
		return allowQuestionManagement;
	}

	public void setAllowQuestionManagement(Boolean allowQuestionManagement)
	{
		this.allowQuestionManagement = allowQuestionManagement;
	}

	/**
	 * @return True if there has been a change to the template, that was not
	 * updated in the evaluation; otherwise False
	 */
	public Boolean getTemplateUpdatePending()
	{
		return templateUpdatePending;
	}

	/**
	 * @param templateUpdatePending True if there has been a change to the
	 * template, that was not updated in the evaluation; otherwise False
	 */
	public void setTemplateUpdatePending(Boolean templateUpdatePending)
	{
		this.templateUpdatePending = templateUpdatePending;
	}

	public Date getLastSummaryApprovedDts()
	{
		return lastSummaryApprovedDts;
	}

	public void setLastSummaryApprovedDts(Date lastSummaryApprovedDts)
	{
		this.lastSummaryApprovedDts = lastSummaryApprovedDts;
	}

}
