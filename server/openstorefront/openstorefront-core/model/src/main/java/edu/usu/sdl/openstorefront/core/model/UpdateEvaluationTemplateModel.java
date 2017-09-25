/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.core.model;

import edu.usu.sdl.openstorefront.core.entity.EvaluationTemplate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kbair
 */
public class UpdateEvaluationTemplateModel
{

	private EvaluationTemplate evaluationTemplate;
	private List<String> evaluationIdsToUpdate = new ArrayList<>();

	/**
	 * @return the evaluationTemplate
	 */
	public EvaluationTemplate getEvaluationTemplate()
	{
		return evaluationTemplate;
	}

	/**
	 * @param evaluationTemplate the evaluationTemplate to update
	 */
	public void setEvaluationTemplate(EvaluationTemplate evaluationTemplate)
	{
		this.evaluationTemplate = evaluationTemplate;
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
