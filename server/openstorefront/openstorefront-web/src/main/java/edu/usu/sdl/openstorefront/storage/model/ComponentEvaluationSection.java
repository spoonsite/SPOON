/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.storage.model;

import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.ConsumeField;
import edu.usu.sdl.openstorefront.util.PK;
import java.math.BigDecimal;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
public class ComponentEvaluationSection
		extends BaseComponent
{

	@PK
	@ConsumeField
	@NotNull
	private ComponentEvaluationSectionPk componentEvaluationSectionPk;

	@Min(1)
	@Max(5)
	@ConsumeField
	@APIDescription("Evaluation score (Depricated)")
	private Integer score;

	@Min(1)
	@Max(5)
	@ConsumeField
	@APIDescription("Evaluation score")
	private BigDecimal actualScore;

	@ConsumeField
	private Boolean notAvailable;

	public ComponentEvaluationSection()
	{
	}

	public ComponentEvaluationSectionPk getComponentEvaluationSectionPk()
	{
		return componentEvaluationSectionPk;
	}

	public void setComponentEvaluationSectionPk(ComponentEvaluationSectionPk componentEvaluationSectionPk)
	{
		this.componentEvaluationSectionPk = componentEvaluationSectionPk;
	}

	public Integer getScore()
	{
		return score;
	}

	public void setScore(Integer score)
	{
		this.score = score;
	}

	public Boolean getNotAvailable()
	{
		return notAvailable;
	}

	public void setNotAvailable(Boolean notAvailable)
	{
		this.notAvailable = notAvailable;
	}

	public BigDecimal getActualScore()
	{
		return actualScore;
	}

	public void setActualScore(BigDecimal actualScore)
	{
		this.actualScore = actualScore;
	}

}
