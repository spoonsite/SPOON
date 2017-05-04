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
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklist;
import edu.usu.sdl.openstorefront.core.view.ChecklistResponseView;
import edu.usu.sdl.openstorefront.core.view.EvaluationChecklistRecommendationView;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ChecklistAll
{

	private EvaluationChecklist evaluationChecklist;

	@DataType(EvaluationChecklistRecommendationView.class)
	private List<EvaluationChecklistRecommendationView> recommendations = new ArrayList<>();

	@DataType(ChecklistResponseView.class)
	private List<ChecklistResponseView> responses = new ArrayList<>();

	public ChecklistAll()
	{
	}

	public EvaluationChecklist getEvaluationChecklist()
	{
		return evaluationChecklist;
	}

	public void setEvaluationChecklist(EvaluationChecklist evaluationChecklist)
	{
		this.evaluationChecklist = evaluationChecklist;
	}

	public List<EvaluationChecklistRecommendationView> getRecommendations()
	{
		return recommendations;
	}

	public void setRecommendations(List<EvaluationChecklistRecommendationView> recommendations)
	{
		this.recommendations = recommendations;
	}

	public List<ChecklistResponseView> getResponses()
	{
		return responses;
	}

	public void setResponses(List<ChecklistResponseView> responses)
	{
		this.responses = responses;
	}

}
