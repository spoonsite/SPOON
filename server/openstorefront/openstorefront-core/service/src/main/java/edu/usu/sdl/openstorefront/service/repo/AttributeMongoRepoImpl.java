/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.repo;

import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.service.repo.api.AttributeRepo;

/**
 *
 * @author dshurtleff
 */
public class AttributeMongoRepoImpl
		extends BaseMongoRepo
		implements AttributeRepo
{

	@Override
	public void changeAttributeCode(AttributeCodePk attributeCodePk, String newCode)
	{

		//update Component Attribute (old code to new code)
		ComponentAttribute componentAttributeExample = new ComponentAttribute();
		ComponentAttributePk componentAttributePkExample = new ComponentAttributePk();
		componentAttributePkExample.setAttributeCode(attributeCodePk.getAttributeCode());
		componentAttributePkExample.setAttributeType(attributeCodePk.getAttributeType());
		componentAttributeExample.setComponentAttributePk(componentAttributePkExample);

		ComponentAttribute componentAttributeSetExample = new ComponentAttribute();
		ComponentAttributePk componentAttributePkSetExample = new ComponentAttributePk();
		componentAttributePkSetExample.setAttributeCode(newCode);
		componentAttributeSetExample.setComponentAttributePk(componentAttributePkSetExample);

		service.getPersistenceService().updateByExample(ComponentAttribute.class, componentAttributeSetExample, componentAttributeExample);

		//update Attribute (old code to new code)
		AttributeCode attributeCodeExample = new AttributeCode();
		AttributeCodePk attributeCodePkExample = new AttributeCodePk();
		attributeCodePkExample.setAttributeCode(attributeCodePk.getAttributeCode());
		attributeCodePkExample.setAttributeType(attributeCodePk.getAttributeType());
		attributeCodeExample.setAttributeCodePk(attributeCodePkExample);

		AttributeCode attributeCodeSetExample = new AttributeCode();
		AttributeCodePk attributeCodePkSetExample = new AttributeCodePk();
		attributeCodePkSetExample.setAttributeCode(newCode);
		attributeCodeSetExample.setAttributeCodePk(attributeCodePkSetExample);

		service.getPersistenceService().updateByExample(AttributeCode.class, attributeCodeSetExample, attributeCodeExample);

	}

}
