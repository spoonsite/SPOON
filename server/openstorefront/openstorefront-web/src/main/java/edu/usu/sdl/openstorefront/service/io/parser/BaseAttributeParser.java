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
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
public abstract class BaseAttributeParser
{

	protected Map<AttributeType, List<AttributeCode>> attributeMap = new HashMap<>();

	public Map<AttributeType, List<AttributeCode>> parse(InputStream in)
	{
		try (CSVReader reader = new CSVReader(new InputStreamReader(in));) {
			internalParse(reader);
		} catch (Exception e) {
			throw new OpenStorefrontRuntimeException(e);
		}
		return attributeMap;
	}

	protected abstract void internalParse(CSVReader reader) throws IOException;

}
