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
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.WorkflowStatus;
import edu.usu.sdl.openstorefront.core.view.ChecklistResponseView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
