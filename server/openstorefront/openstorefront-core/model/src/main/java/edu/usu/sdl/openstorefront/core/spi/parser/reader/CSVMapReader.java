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
package edu.usu.sdl.openstorefront.core.spi.parser.reader;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.MapField;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.MapModel;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 *
 * @author dshurtleff
 */
public class CSVMapReader
		extends MappableReader
{

	private au.com.bytecode.opencsv.CSVReader reader;

	private char separator = ',';

	public CSVMapReader(InputStream in)
	{
		super(in);
	}

	public void setSeparator(char separator)
	{
		this.separator = separator;
	}

	@Override
	public void preProcess()
	{
		reader = new au.com.bytecode.opencsv.CSVReader(new InputStreamReader(in), separator);
	}

	@Override
	public MapModel nextRecord()
	{
		MapModel mapModel = null;
		try {
			currentRecordNumber++;
			String[] data = reader.readNext();

			if (data != null) {
				mapModel = readRecord(data);
			}
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		return mapModel;
	}

	private MapModel readRecord(String[] data)
	{
		MapModel mapModel = new MapModel();
		mapModel.setName("root");
		int columnNumber = 0;
		for (String column : data) {
			columnNumber++;
			MapField mapField = new MapField();
			mapField.setName("COLUMN-" + columnNumber);
			mapField.setValue(column);
			mapModel.getMapFields().add(mapField);
		}
		return mapModel;
	}

	@Override
	public MapModel findFields(InputStream in)
	{
		MapModel mapModel = new MapModel();

		//Read header line assume first line
		try (Reader csvStream = new InputStreamReader(in)) {
			reader = new au.com.bytecode.opencsv.CSVReader(csvStream, separator);

			String[] data = reader.readNext();
			if (data != null) {
				mapModel = readRecord(data);
			}
		} catch (IOException io) {
			throw new OpenStorefrontRuntimeException(io);
		}

		return mapModel;
	}

}
