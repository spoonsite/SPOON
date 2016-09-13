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
package edu.usu.sdl.openstorefront.service.io.parser;

import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.spi.parser.BaseComponentParser;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.ComponentMapper;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.MapModel;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.GenericReader;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.XMLMapReader;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentMappedXMLParser
		extends BaseComponentParser
{

	private List<ComponentAll> componentAlls;

	@Override
	public String checkFormat(String mimeType, InputStream input)
	{
		if (mimeType.contains("xml")) {
			return "";
		} else {
			return "Invalid format. Please upload a XML file.";
		}
	}

	@Override
	protected GenericReader getReader(InputStream in)
	{
		return new XMLMapReader(in);
	}

	@Override
	protected String handlePreviewOfRecord(Object data)
	{
		return super.handlePreviewOfRecord(componentAlls);
	}

	@Override
	protected <T> Object parseRecord(T record)
	{
		MapModel mapModel = (MapModel) record;

		ComponentMapper componentMapper = new ComponentMapper(() -> {
			ComponentAll componentAll = defaultComponentAll();
			return componentAll;
		}, fileHistoryAll);

		componentAlls = componentMapper.multiMapData(mapModel);
		for (ComponentAll componentAll : componentAlls) {
			postProcessFields(componentAll);
		}
		addMultipleRecords(componentAlls);

		return null;
	}

}
