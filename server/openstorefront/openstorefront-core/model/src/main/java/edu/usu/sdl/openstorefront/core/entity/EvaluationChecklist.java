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
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Evalution checklist")
public class EvaluationChecklist
		extends StandardEntity<EvaluationChecklist>
		implements LoggableModel<EvaluationChecklist>
{

	@PK(generated = true)
	@NotNull
	private String checklistId;

	@NotNull
	@FK(ChecklistTemplate.class)
	private String checklistTemplateId;

	@NotNull
	@FK(Evaluation.class)
	private String evaluationId;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_32K)
	private String summary;

	@NotNull
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = WorkflowStatus.class)
	@FK(WorkflowStatus.class)
	private String workflowStatus;

	@ConsumeField
	@APIDescription("Private Summary is not published")
	private Boolean privateSummaryFlg;

	@ConsumeField
	@APIDescription("The whole checklist will not show")
	private Boolean privateChecklistFlg;

	public EvaluationChecklist()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		EvaluationChecklist evaluationChecklist = (EvaluationChecklist) entity;
		ServiceProxyFactory.getServiceProxy().getChangeLogService().findUpdateChanges(this, evaluationChecklist);
		super.updateFields(entity);

		setSummary(evaluationChecklist.getSummary());
		setWorkflowStatus(evaluationChecklist.getWorkflowStatus());
		setPrivateSummaryFlg(evaluationChecklist.getPrivateSummaryFlg());
		setPrivateChecklistFlg(evaluationChecklist.getPrivateChecklistFlg());

	}

	@Override
	public List<FieldChangeModel> findChanges(EvaluationChecklist updated)
	{
		Set<String> excludeFields = excludedChangeFields();
		excludeFields.add("checklistId");
		excludeFields.add("checklistTemplateId");
		excludeFields.add("evaluationId");
		List<FieldChangeModel> changes = FieldChangeModel.allChangedFields(excludeFields, this, updated);
		return changes;
	}

	@Override
	public String addRemoveComment()
	{
		return getSummary();
	}

	@Override
	public void setChangeParent(ChangeLog changeLog)
	{
		changeLog.setParentEntity(Evaluation.class.getSimpleName());
		changeLog.setParentEntityId(getEvaluationId());
	}

	public String getChecklistId()
	{
		return checklistId;
	}

	public void setChecklistId(String checklistId)
	{
		this.checklistId = checklistId;
	}

	public String getChecklistTemplateId()
	{
		return checklistTemplateId;
	}

	public void setChecklistTemplateId(String checklistTemplateId)
	{
		this.checklistTemplateId = checklistTemplateId;
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	public String getEvaluationId()
	{
		return evaluationId;
	}

	public void setEvaluationId(String evaluationId)
	{
		this.evaluationId = evaluationId;
	}

	public String getWorkflowStatus()
	{
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus)
	{
		this.workflowStatus = workflowStatus;
	}

	public Boolean getPrivateSummaryFlg()
	{
		return privateSummaryFlg;
	}

	public void setPrivateSummaryFlg(Boolean privateSummaryFlg)
	{
		this.privateSummaryFlg = privateSummaryFlg;
	}

	public Boolean getPrivateChecklistFlg()
	{
		return privateChecklistFlg;
	}

	public void setPrivateChecklistFlg(Boolean privateChecklistFlg)
	{
		this.privateChecklistFlg = privateChecklistFlg;
	}

}
