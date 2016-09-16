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
import javax.persistence.OneToOne;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Referenced Metadata Attribute")
public class ComponentAttribute
		extends BaseComponent<ComponentAttribute>
{

	@PK
	@ConsumeField
	@OneToOne(orphanRemoval = true)
	private ComponentAttributePk componentAttributePk;

	public ComponentAttribute()
	{
	}

	@Override
	public String uniqueKey()
	{
		return getComponentAttributePk().getAttributeType()
				+ OpenStorefrontConstant.GENERAL_KEY_SEPARATOR
				+ getComponentAttributePk().getAttributeCode();
	}

	@Override
	protected void customKeyClear()
	{
		//Leave the other key items
		if (getComponentAttributePk() != null) {
			getComponentAttributePk().setComponentId(null);
		}
	}

	public ComponentAttributePk getComponentAttributePk()
	{
		return componentAttributePk;
	}

	public void setComponentAttributePk(ComponentAttributePk componentAttributePk)
	{
		this.componentAttributePk = componentAttributePk;
	}

}
