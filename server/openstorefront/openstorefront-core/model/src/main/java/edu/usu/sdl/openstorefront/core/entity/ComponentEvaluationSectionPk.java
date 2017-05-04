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

import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Evaluation section Primary Key")
@Embeddable
public class ComponentEvaluationSectionPk
		extends BasePK<ComponentEvaluationSectionPk>
{

	@NotNull
	@ConsumeField
	@FK(Component.class)
	private String componentId;

	@NotNull
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = EvaluationSection.class)
	@APIDescription("Evaluation section code")
	@FK(EvaluationSection.class)
	private String evaluationSection;

	public ComponentEvaluationSectionPk()
	{
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || (obj instanceof ComponentEvaluationSectionPk == false)) {
			return false;
		}
		ComponentEvaluationSectionPk compareObj = (ComponentEvaluationSectionPk) obj;
		return pkValue().equals(compareObj.pkValue());
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 23 * hash + Objects.hashCode(getComponentId());
		hash = 23 * hash + Objects.hashCode(getEvaluationSection());
		return hash;
	}

	@Override
	public String pkValue()
	{
		return getComponentId() + ReflectionUtil.COMPOSITE_KEY_SEPERATOR + getEvaluationSection();
	}

	@Override
	public String toString()
	{
		return pkValue();
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getEvaluationSection()
	{
		return evaluationSection;
	}

	public void setEvaluationSection(String evaluationSection)
	{
		this.evaluationSection = evaluationSection;
	}

}
