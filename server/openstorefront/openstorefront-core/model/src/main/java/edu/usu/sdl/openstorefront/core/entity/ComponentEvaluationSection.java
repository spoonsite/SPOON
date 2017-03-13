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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Evaluation section score")
public class ComponentEvaluationSection
		extends BaseComponent<ComponentEvaluationSection>
{

	@PK
	@ConsumeField
	@NotNull
	@Embedded
	@OneToOne(orphanRemoval = true)
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

	@Override
	public String uniqueKey()
	{
		String key = getComponentEvaluationSectionPk().getEvaluationSection()
					+ OpenStorefrontConstant.GENERAL_KEY_SEPARATOR  
					+ getActualScore()
					+ OpenStorefrontConstant.GENERAL_KEY_SEPARATOR  
					+ getNotAvailable();
		return key;
	}

	@Override
	protected void customKeyClear()
	{
		if (getComponentEvaluationSectionPk() != null) {
			getComponentEvaluationSectionPk().setComponentId(null);
		}
	}

	@Override
	public void updateFields(StandardEntity entity)
	{
		super.updateFields(entity);

		ComponentEvaluationSection section = (ComponentEvaluationSection) entity;
		this.setActualScore(section.getActualScore());
		this.setScore(null);
		this.setNotAvailable(section.getNotAvailable());

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
