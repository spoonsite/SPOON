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
package edu.usu.sdl.openstorefront.service.io.parser;

import au.com.bytecode.opencsv.CSVReader;
import com.fasterxml.jackson.core.type.TypeReference;
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.transfermodel.AttributeAll;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.util.StringProcessor;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class MainAttributeParser
		extends BaseAttributeParser
{

	private static final Logger log = Logger.getLogger(MainAttributeParser.class.getName());

	@Override
	public String getHEADER()
	{
		return "JSON";
	}

	@Override
	public Map<AttributeType, List<AttributeCode>> parse(InputStream in)
	{
		try {
			List<AttributeAll> attributeAllRecords = StringProcessor.defaultObjectMapper().readValue(in, new TypeReference<List<AttributeAll>>()
			{
			});
			for (AttributeAll attributeAll : attributeAllRecords) {
				attributeMap.put(attributeAll.getAttributeType(), attributeAll.getAttributeCodes());
			}
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to process attribute file.", "Check the format and confirm data is correct JSON for an attribute.", ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
					log.log(Level.WARNING, "Unable to close attribute import file.", ex);
				}
			}
		}
		return attributeMap;
	}

	@Override
	protected void internalParse(CSVReader reader) throws IOException
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
