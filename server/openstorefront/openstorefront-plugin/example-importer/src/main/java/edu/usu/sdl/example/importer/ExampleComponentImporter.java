/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.example.importer;

import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.spi.parser.BaseComponentParser;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.ComponentMapper;
import java.io.InputStream;

/**
 * This is an example of using the attribute mapping
 *
 * @author dshurtleff
 */
public class ExampleComponentImporter
		extends BaseComponentParser
{

	public static final String FORMAT_CODE = "MAPEXAMPLECMP";

	@Override
	public String checkFormat(String mimeType, InputStream input)
	{
		//valid (return message for invalid)
		return "";
	}

	@Override
	protected String handlePreviewOfRecord(Object data)
	{
		//preview of first record parsed
		return "Only Mapping the attributes";
	}

	@Override
	protected <T> Object parseRecord(T record)
	{
		ComponentMapper componentMapper = new ComponentMapper(() -> {
			ComponentAll componentAll = defaultComponentAll();
			componentAll.getComponent().setDescription(null);
			return componentAll;
		}, fileHistoryAll);

		//This is just using the Attribute mapping suppport
		//not the field mapping
		ComponentAll componentAll = new ComponentAll();

		//example sample data
		ComponentAttribute attribute = new ComponentAttribute();
		ComponentAttributePk attributePk = new ComponentAttributePk();
		attributePk.setAttributeType("A-TEST");
		attributePk.setAttributeCode("apple");
		attribute.setComponentAttributePk(attributePk);

		componentAll.getAttributes().add(attribute);

		attribute = new ComponentAttribute();
		attributePk = new ComponentAttributePk();
		attributePk.setAttributeType("B-TEST");
		attributePk.setAttributeCode("banana");
		attribute.setComponentAttributePk(attributePk);

		componentAll.getAttributes().add(attribute);

		componentMapper.applyAttributeMapping(componentAll, null);

		//Map fields into componentAll
		//
		//
		//Return componentAll to save; Return null to skip
		return null;
	}

}
