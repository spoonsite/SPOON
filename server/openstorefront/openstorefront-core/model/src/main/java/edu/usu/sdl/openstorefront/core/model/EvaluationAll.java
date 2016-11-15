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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class EvaluationAll
{

	private Evaluation evaluation;
	private ChecklistAll checkListAll;

	@DataType(ContentSectionAll.class)
	private List<ContentSectionAll> contentSections = new ArrayList<>();

	public EvaluationAll()
	{
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
