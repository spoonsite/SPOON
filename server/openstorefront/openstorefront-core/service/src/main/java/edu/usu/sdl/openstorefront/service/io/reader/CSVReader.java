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
package edu.usu.sdl.openstorefront.service.io.reader;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CSVReader
		extends GenericReader<String[]>
{

	private au.com.bytecode.opencsv.CSVReader reader;

	public CSVReader(InputStream in)
	{
		super(in);
		reader = new au.com.bytecode.opencsv.CSVReader(new InputStreamReader(in));
	}

	@Override
	public String[] nextRecord()
	{
		try {
			currentRecordNumber++;
			return reader.readNext();
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
	}

}
