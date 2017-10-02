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
package edu.usu.sdl.openstorefront.core.model;

import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.WorkflowStatus;
import edu.usu.sdl.openstorefront.core.view.ChecklistResponseView;
import edu.usu.sdl.openstorefront.core.view.EvaluationChecklistRecommendationView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class EvaluationAll
{

	private static final Logger LOG = Logger.getLogger(EvaluationAll.class.getName());

	private Evaluation evaluation;
	private ChecklistAll checkListAll;

	@DataType(ContentSectionAll.class)
	private List<ContentSectionAll> contentSections = new ArrayList<>();

	public EvaluationAll()
	{
	}

	/**
	 * This calcs the process of an evaluation based on status of the individual
	 * parts.
	 *
	 * @return
	 */
	public BigDecimal calcProgress()
	{
		BigDecimal progress = BigDecimal.ZERO;

		BigDecimal total = BigDecimal.ZERO;
		BigDecimal completed = BigDecimal.ZERO;

		WorkflowStatus finalState = WorkflowStatus.finalStatus();
		if (finalState != null) {

			total = total.add(BigDecimal.ONE);
			if (evaluation != null) {
				if (finalState.getCode().equals(checkListAll.getEvaluationChecklist().getWorkflowStatus())) {
					completed = completed.add(BigDecimal.ONE);
				}
			}

			total = total.add(BigDecimal.ONE);
			if (checkListAll != null) {
				if (finalState.getCode().equals(checkListAll.getEvaluationChecklist().getWorkflowStatus())) {
					completed = completed.add(BigDecimal.ONE);
				}

				for (ChecklistResponseView responseView : checkListAll.getResponses()) {
					total = total.add(BigDecimal.ONE);
					if (finalState.getCode().equals(responseView.getWorkflowStatus())) {
						completed = completed.add(BigDecimal.ONE);
					}
				}
			}

			for (ContentSectionAll contentSectionAll : contentSections) {
				if (contentSectionAll.getSection() != null) {
					total = total.add(BigDecimal.ONE);
					if (finalState.getCode().equals(contentSectionAll.getSection().getWorkflowStatus())) {
						completed = completed.add(BigDecimal.ONE);
					}
				}
			}
		} else {
			LOG.warning("Unable to calc progress on evaluation; missing final workflow state.");
		}

		if (total.compareTo(BigDecimal.ZERO) > 0) {
			progress = completed.divide(total, 1, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100));
		}

		return progress;
	}

	/**
	 * This will find the last change data of any of the evaluation information.
	 * Note: this expect the evaluation to loaded. Also this is an expensive
	 * call
	 *
	 * @return Latest date of change
	 */
	public Date calcLastChangeDate()
	{
		Date lastChangeDate = evaluation.getUpdateDts();

		if (checkListAll != null) {
			if (lastChangeDate.before(checkListAll.getEvaluationChecklist().getUpdateDts())) {
				lastChangeDate = checkListAll.getEvaluationChecklist().getUpdateDts();
			}

			for (ChecklistResponseView responseView : checkListAll.getResponses()) {
				if (lastChangeDate.before(responseView.getUpdateDts())) {
					lastChangeDate = responseView.getUpdateDts();
				}
			}

			for (EvaluationChecklistRecommendationView checklistRecommendationView : checkListAll.getRecommendations()) {
				if (lastChangeDate.before(checklistRecommendationView.getUpdateDts())) {
					lastChangeDate = checklistRecommendationView.getUpdateDts();
				}
			}
		}

		for (ContentSectionAll contentSectionAll : contentSections) {
			//subsection are updated at the same time as evaluations
			if (lastChangeDate.before(contentSectionAll.getSection().getUpdateDts())) {
				lastChangeDate = contentSectionAll.getSection().getUpdateDts();
			}
		}

		//check change request;
		Component component = new Component();
		component.setComponentId(evaluation.getComponentId());
		component = component.find();
		if (component != null) {
			//the last activity date should cover all the changes
			if (lastChangeDate.before(component.getLastActivityDts())) {
				lastChangeDate = component.getLastActivityDts();
			}
		}

		return lastChangeDate;
	}

	public Evaluation getEvaluation()
	{
		return evaluation;
	}

	public void setEvaluation(Evaluation evaluation)
	{
		this.evaluation = evaluation;
	}

	public ChecklistAll getCheckListAll()
	{
		return checkListAll;
	}

	public void setCheckListAll(ChecklistAll checkListAll)
	{
		this.checkListAll = checkListAll;
	}

	public List<ContentSectionAll> getContentSections()
	{
		return contentSections;
	}

	public void setContentSections(List<ContentSectionAll> contentSections)
	{
		this.contentSections = contentSections;
	}

}
