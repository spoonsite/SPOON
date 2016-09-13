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
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author dshurtleff
 */
public class ExcelMapReader
		extends MappableReader
{

	private Workbook workbook;
	private Sheet sheet;
	private int row = 0;

	public ExcelMapReader(InputStream in)
	{
		super(in);
	}

	@Override
	public void preProcess()
	{
		try (InputStream workbookStream = in) {
			workbook = WorkbookFactory.create(workbookStream);
			sheet = workbook.getSheetAt(0);
		} catch (IOException | InvalidFormatException | EncryptedDocumentException ioe) {
			throw new OpenStorefrontRuntimeException("Unable to open excel file.", " Check file and format", ioe);
		}
	}

	@Override
	public MapModel nextRecord()
	{
		MapModel mapModel = null;

		currentRecordNumber++;
		if (sheet.getRow(row) != null) {
			mapModel = readRecord(sheet.getRow(row));
		}
		row++;

		return mapModel;
	}

	private MapModel readRecord(Row row)
	{
		MapModel mapModel = new MapModel();
		mapModel.setName("root");
		int columnNumber = 0;

		while (row.getCell(columnNumber) != null) {
			Cell cell = row.getCell(columnNumber);
			MapField mapField = new MapField();
			mapField.setName("COLUMN-" + (columnNumber + 1));
			if (cell.getCellType() == Cell.CELL_TYPE_STRING
					|| cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				mapField.setValue(cell.getStringCellValue());
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				mapField.setValue("" + cell.getNumericCellValue());
			} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
				mapField.setValue("" + cell.getBooleanCellValue());
			} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				mapField.setValue("" + cell.getRichStringCellValue());
			} else {
				mapField.setValue("");
			}
			mapModel.getMapFields().add(mapField);
			columnNumber++;
		}
		return mapModel;
	}

	@Override
	public MapModel findFields(InputStream in)
	{
		MapModel mapModel = new MapModel();
		preProcess();

		//read first row
		if (sheet.getRow(0) != null) {
			mapModel = readRecord(sheet.getRow(0));
		}

		return mapModel;
	}

}
