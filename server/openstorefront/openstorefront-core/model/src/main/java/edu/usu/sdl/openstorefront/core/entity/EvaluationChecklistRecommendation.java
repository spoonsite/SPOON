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
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
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
@APIDescription("Evalution recommendation")
public class EvaluationChecklistRecommendation
		extends StandardEntity<EvaluationChecklistRecommendation>
		implements LoggableModel<EvaluationChecklistRecommendation>
{

	@PK(generated = true)
	@NotNull
	private String recommendationId;

	@NotNull
	@FK(EvaluationChecklist.class)
	private String checklistId;

	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_32K)
	private String recommendation;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_32K)
	private String reason;

	@NotNull
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = RecommendationType.class)
	@FK(RecommendationType.class)
	private String recommendationType;

	@ConsumeField
	@APIDescription("Private information is not published; recommendation will not show.")
	private Boolean privateFlg;

	public EvaluationChecklistRecommendation()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		EvaluationChecklistRecommendation evaluationChecklistRecommendation = (EvaluationChecklistRecommendation) entity;
		ServiceProxyFactory.getServiceProxy().getChangeLogService().findUpdateChanges(this, evaluationChecklistRecommendation);
		super.updateFields(entity);

		setReason(evaluationChecklistRecommendation.getReason());
		setRecommendation(evaluationChecklistRecommendation.getRecommendation());
		setRecommendationType(evaluationChecklistRecommendation.getRecommendationType());
		setPrivateFlg(evaluationChecklistRecommendation.getPrivateFlg());

	}

	@Override
	public List<FieldChangeModel> findChanges(EvaluationChecklistRecommendation updated)
	{
		Set<String> excludeFields = excludedChangeFields();
		excludeFields.add("recommendationId");
		excludeFields.add("checklistId");
		List<FieldChangeModel> changes = FieldChangeModel.allChangedFields(excludeFields, this, updated);
		return changes;
	}

	@Override
	public String addRemoveComment()
	{
		return getRecommendation();
	}

	@Override
	public void setChangeParent(ChangeLog changeLog)
	{
		changeLog.setParentEntity(EvaluationChecklist.class.getSimpleName());
		changeLog.setParentEntityId(getChecklistId());

		String comment = changeLog.getComment();
		if (comment != null) {
			comment = StringProcessor.ellipseString(getRecommendation(), 60);
		}
		changeLog.setComment(comment);
	}

	public String getRecommendationId()
	{
		return recommendationId;
	}

	public void setRecommendationId(String recommendationId)
	{
		this.recommendationId = recommendationId;
	}

	public String getChecklistId()
	{
		return checklistId;
	}

	public void setChecklistId(String checklistId)
	{
		this.checklistId = checklistId;
	}

	public String getRecommendation()
	{
		return recommendation;
	}

	public void setRecommendation(String recommendation)
	{
		this.recommendation = recommendation;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}

	public String getRecommendationType()
	{
		return recommendationType;
	}

	public void setRecommendationType(String recommendationType)
	{
		this.recommendationType = recommendationType;
	}

	public Boolean getPrivateFlg()
	{
		return privateFlg;
	}

	public void setPrivateFlg(Boolean privateFlg)
	{
		this.privateFlg = privateFlg;
	}

}
