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
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Checklist question response")
public class EvaluationChecklistResponse
		extends StandardEntity<EvaluationChecklistResponse>
		implements LoggableModel<EvaluationChecklistResponse>
{

	@PK(generated = true)
	@NotNull
	private String responseId;

	@NotNull
	@FK(EvaluationChecklist.class)
	private String checklistId;

	@NotNull
	@FK(ChecklistQuestion.class)
	private String questionId;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_32K)
	@Sanitize(HTMLSanitizer.class)
	private String response;

	@ConsumeField
	private BigDecimal score;

	@ConsumeField
	private Boolean notApplicable;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_32K)
	@Sanitize(HTMLSanitizer.class)
	private String privateNote;

	@NotNull
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = WorkflowStatus.class)
	@FK(WorkflowStatus.class)
	private String workflowStatus;

	@ConsumeField
	@APIDescription("Private information is not published; response and question will not show.")
	private Boolean privateFlg;

	private Integer sortOrder;

	@APIDescription("Indicates that an evaluator manually added or removed; This used for auto syncing.")
	private Boolean userAddRemoveFlg;

	public EvaluationChecklistResponse()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		EvaluationChecklistResponse checklistResponse = (EvaluationChecklistResponse) entity;
		ServiceProxyFactory.getServiceProxy().getChangeLogService().findUpdateChanges(this, checklistResponse);
		super.updateFields(entity);

		setQuestionId(checklistResponse.getQuestionId());
		setPrivateNote(checklistResponse.getPrivateNote());
		setResponse(checklistResponse.getResponse());
		setScore(checklistResponse.getScore());
		setNotApplicable(checklistResponse.getNotApplicable());
		setWorkflowStatus(checklistResponse.getWorkflowStatus());
		setPrivateFlg(checklistResponse.getPrivateFlg());
		setUserAddRemoveFlg(checklistResponse.getUserAddRemoveFlg());

	}

	@Override
	public List<FieldChangeModel> findChanges(EvaluationChecklistResponse updated)
	{
		Set<String> excludeFields = excludedChangeFields();
		excludeFields.add("responseId");
		excludeFields.add("checklistId");
		excludeFields.add("questionId");
		List<FieldChangeModel> changes = FieldChangeModel.allChangedFields(excludeFields, this, updated);
		return changes;
	}

	@Override
	public String addRemoveComment()
	{
		return getResponse();
	}

	@Override
	public void setChangeParent(ChangeLog changeLog)
	{
		changeLog.setParentEntity(EvaluationChecklist.class.getSimpleName());
		changeLog.setParentEntityId(getChecklistId());
		if (getQuestionId() != null) {
			ChecklistQuestion question = ServiceProxyFactory.getServiceProxy().getChecklistService().findQuestion(getQuestionId());
			String comment = changeLog.getComment();
			if (comment != null) {
				comment = "Question: " + question.getQid();
			}
			changeLog.setComment(comment);
		}
	}

	public String getResponseId()
	{
		return responseId;
	}

	public void setResponseId(String responseId)
	{
		this.responseId = responseId;
	}

	public String getChecklistId()
	{
		return checklistId;
	}

	public void setChecklistId(String checklistId)
	{
		this.checklistId = checklistId;
	}

	public String getQuestionId()
	{
		return questionId;
	}

	public void setQuestionId(String questionId)
	{
		this.questionId = questionId;
	}

	public String getResponse()
	{
		return response;
	}

	public void setResponse(String response)
	{
		this.response = response;
	}

	public BigDecimal getScore()
	{
		return score;
	}

	public void setScore(BigDecimal score)
	{
		this.score = score;
	}

	public String getPrivateNote()
	{
		return privateNote;
	}

	public void setPrivateNote(String privateNote)
	{
		this.privateNote = privateNote;
	}

	public String getWorkflowStatus()
	{
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus)
	{
		this.workflowStatus = workflowStatus;
	}

	public Boolean getNotApplicable()
	{
		return notApplicable;
	}

	public void setNotApplicable(Boolean notApplicable)
	{
		this.notApplicable = notApplicable;
	}

	public Boolean getPrivateFlg()
	{
		return privateFlg;
	}

	public void setPrivateFlg(Boolean privateFlg)
	{
		this.privateFlg = privateFlg;
	}

	public Integer getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	public Boolean getUserAddRemoveFlg()
	{
		return userAddRemoveFlg;
	}

	public void setUserAddRemoveFlg(Boolean userAddRemoveFlg)
	{
		this.userAddRemoveFlg = userAddRemoveFlg;
	}

}
