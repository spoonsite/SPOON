/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.init;

import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.util.StringProcessor;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clean up bad attributes key (Remove after 1.5)
 *
 * @author dshurtleff
 */
public class AttributeCleanupInit
		extends ApplyOnceInit
{

	private static final Logger log = Logger.getLogger(AttributeCleanupInit.class.getName());

	public AttributeCleanupInit()
	{
		super("Attribute_Cleanup");
	}

	@Override
	protected String internalApply()
	{
		StringBuilder results = new StringBuilder();

		//Types are OK
		//check code
		int successCount = 0;
		int failCount = 0;
		List<AttributeCode> allCodes = service.getAttributeService().getAllAttributeCodes(null);
		for (AttributeCode attributeCode : allCodes) {
			String cleanCode = StringProcessor.cleanEntityKey(attributeCode.getAttributeCodePk().getAttributeCode());
			if (cleanCode.equals(attributeCode.getAttributeCodePk().getAttributeCode()) == false) {
				log.log(Level.INFO, MessageFormat.format("Found a bad code: {0}", attributeCode.getAttributeCodePk().toString()));

				//update the codes
				try {
					service.getAttributeService().changeAttributeCode(attributeCode.getAttributeCodePk(), cleanCode);
					log.log(Level.INFO, MessageFormat.format("Updated code to: {0}", cleanCode));
					successCount++;
				} catch (Exception e) {
					log.log(Level.WARNING, MessageFormat.format("Fail to update code to: {0}", cleanCode), e);
					failCount++;
				}
			}
		}
		results.append("Successfully Updated: ")
				.append(successCount)
				.append(" Failed: ")
				.append(failCount);

		return results.toString();
	}

}
