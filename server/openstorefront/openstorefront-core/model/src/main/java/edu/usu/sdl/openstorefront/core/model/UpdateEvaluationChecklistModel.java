/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.ChecklistTemplate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kbair
 */
public class UpdateEvaluationChecklistModel
{

	@ConsumeField
	private ChecklistTemplate checklistTemplate;

	@ConsumeField
	@DataType(String.class)
	private List<String> evaluationIdsToUpdate = new ArrayList<>();

	/**
	 * @return the checklistTemplate
	 */
	public ChecklistTemplate getChecklistTemplate()
	{
		return checklistTemplate;
	}

	/**
	 * @param checklistTemplate the checklistTemplate to set
	 */
	public void setChecklistTemplate(ChecklistTemplate checklistTemplate)
	{
		this.checklistTemplate = checklistTemplate;
	}

	/**
	 * @return the evaluationIdsToUpdate
	 */
	public List<String> getEvaluationIdsToUpdate()
	{
		return evaluationIdsToUpdate;
	}

	/**
	 * @param evaluationIdsToUpdate the list of id's to update
	 */
	public void setEvaluationIdsToUpdate(List<String> evaluationIdsToUpdate)
	{
		this.evaluationIdsToUpdate = evaluationIdsToUpdate;
	}
}
