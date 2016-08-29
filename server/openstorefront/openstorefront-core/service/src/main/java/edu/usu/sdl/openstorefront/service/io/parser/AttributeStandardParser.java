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

import com.fasterxml.jackson.core.type.TypeReference;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.model.AttributeAll;
import edu.usu.sdl.openstorefront.core.spi.parser.BaseAttributeParser;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.GenericReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AttributeStandardParser
		extends BaseAttributeParser
{

	private static final Logger LOG = Logger.getLogger(ComponentStandardParser.class.getName());

	@Override
	public String checkFormat(String mimeType, InputStream input)
	{
		if (mimeType.contains("json") || mimeType.contains("text") || mimeType.contains("stream")) {
			return "";
		} else {
			return "Invalid format. Please upload a JSON file in standard format. (Mimetype incorrect.)";
		}
	}

	@Override
	protected GenericReader getReader(InputStream in)
	{
		return new GenericReader<AttributeAll>(in)
		{

			private List<AttributeAll> listOfAttributeAlls = new ArrayList<>();

			@Override
			public void preProcess()
			{
				try (InputStream inTemp = in) {
					listOfAttributeAlls = StringProcessor.defaultObjectMapper().readValue(inTemp, new TypeReference<List<AttributeAll>>()
					{
					});
				} catch (IOException ex) {
					throw new OpenStorefrontRuntimeException(ex);
				}
			}

			@Override
			public AttributeAll nextRecord()
			{
				if (listOfAttributeAlls.size() > 0) {
					currentRecordNumber++;
					return listOfAttributeAlls.remove(0);
				} else {
					return null;
				}
			}

		};
	}

	@Override
	protected <T> Object parseRecord(T record)
	{
		AttributeAll attributeAll = (AttributeAll) record;
		return attributeAll;
	}

}
