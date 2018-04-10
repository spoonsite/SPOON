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

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import javax.persistence.Embedded;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Review Con")
public class ComponentReviewCon
		extends BaseComponent<ComponentReviewCon>
{

	@PK
	@NotNull
	@Embedded
	@ConsumeField
	@OneToOne(orphanRemoval = true)
	private ComponentReviewConPk componentReviewConPk;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ComponentReviewCon()
	{
	}

	@Override
	public String uniqueKey()
	{
		return getComponentReviewConPk().pkValue();
	}

	@Override
	protected void customKeyClear()
	{
		if (getComponentReviewConPk() != null) {
			getComponentReviewConPk().setComponentReviewId(null);
		}
	}

	public ComponentReviewConPk getComponentReviewConPk()
	{
		return componentReviewConPk;
	}

	public void setComponentReviewConPk(ComponentReviewConPk componentReviewConPk)
	{
		this.componentReviewConPk = componentReviewConPk;
	}

}
