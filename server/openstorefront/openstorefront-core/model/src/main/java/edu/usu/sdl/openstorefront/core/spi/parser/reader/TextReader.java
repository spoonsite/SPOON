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
package edu.usu.sdl.openstorefront.core.spi.parser.reader;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author dshurtleff
 */
public class TextReader
		extends GenericReader<String>
{

	private BufferedReader bin;

	public TextReader(InputStream in)
	{
		super(in);
		bin = new BufferedReader(new InputStreamReader(in));
	}

	@Override
	public String nextRecord()
	{
		try {
			String line = bin.readLine();
			if (line != null) {
				currentRecordNumber++;
			}
			return line;
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
	}

}
