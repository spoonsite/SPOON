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
package edu.usu.sdl.core.init;

import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.validation.CleanKeySanitizer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Remove after 2.5 - This is use to fix Attribute Type keys that have # in them
 *
 * @author dshurtleff
 */
public class AttributeKeyCorrectionInit
		extends ApplyOnceInit
{

	private static final Logger LOG = Logger.getLogger(AttributeKeyCorrectionInit.class.getName());

	public AttributeKeyCorrectionInit()
	{
		super("BadAttributeType-Init");
	}

	@Override
	protected String internalApply()
	{
		int fixTypes = 0;

		//check attribute type
		AttributeType attributeTypeExample = new AttributeType();
		List<AttributeType> attributeTypes = attributeTypeExample.findByExampleProxy();
		for (AttributeType attributeType : attributeTypes) {
			if (attributeType.getAttributeType().contains("#")) {

				String oldType = attributeType.getAttributeType();
				LOG.log(Level.INFO, "Found attribute type to cleanup: " + oldType);

				CleanKeySanitizer sanitizer = new CleanKeySanitizer();
				String newType = (String) sanitizer.santize(oldType);

				attributeType.setAttributeType(newType);
				service.getPersistenceService().persist(attributeType);

				//fix codes with bad type
				AttributeCode attribueCodeExample = new AttributeCode();
				AttributeCodePk attributeCodePk = new AttributeCodePk();
				attributeCodePk.setAttributeType(oldType);
				attribueCodeExample.setAttributeCodePk(attributeCodePk);

				List<AttributeCode> attributeCodes = attribueCodeExample.findByExampleProxy();
				for (AttributeCode attributeCode : attributeCodes) {
					LOG.log(Level.INFO, "Fixing code: " + attributeCode.getAttributeCodePk().toString());

					attributeCode.getAttributeCodePk().setAttributeType(newType);
					service.getPersistenceService().persist(attributeCode);
				}

				//fix bad componentAttribute
				ComponentAttribute componentAttributeExample = new ComponentAttribute();
				ComponentAttributePk componentAttributePk = new ComponentAttributePk();
				componentAttributePk.setAttributeType(oldType);
				componentAttributeExample.setComponentAttributePk(componentAttributePk);

				List<ComponentAttribute> componentAttributes = componentAttributeExample.findByExampleProxy();
				for (ComponentAttribute componentAttribute : componentAttributes) {
					LOG.log(Level.INFO, "Fixing component attribute code: " + componentAttribute.getComponentAttributePk().toString());

					componentAttribute.getComponentAttributePk().setAttributeType(newType);
					service.getPersistenceService().persist(componentAttribute);
				}

				fixTypes++;
			}
		}

		return "Fixed: " + fixTypes;
	}

}
