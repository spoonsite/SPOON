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
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class SvcAttributeParser
		extends BaseAttributeParser
{

	private static final Logger log = Logger.getLogger(SvcAttributeParser.class.getName());

	private static final int CODE = 1;
	private static final int LABEL = 2;
	private static final int DEFINITION = 3;
	private static final int DESCRIPTION = 4;

	@Override
	protected void internalParse(CSVReader reader) throws IOException
	{
		AttributeType attributeType = new AttributeType();
		attributeType.setAttributeType(AttributeType.DI2E_SVCV4);
		attributeType.setDescription("DI2E SvcV-4 Alignment");

		//Default to true....Later an admin would need to determine which ones should only allow one.
		attributeType.setAllowMutlipleFlg(Boolean.TRUE);
		attributeType.setArchitectureFlg(Boolean.TRUE);
		attributeType.setVisibleFlg(Boolean.TRUE);
		attributeType.setImportantFlg(Boolean.TRUE);
		attributeType.setRequiredFlg(Boolean.FALSE);

		attributeMap.put(attributeType, new ArrayList<>());

		int lineNumber = 0;
		List<String[]> allLines = reader.readAll();
		for (int i = 1; i < allLines.size(); i++) {
			lineNumber = i;
			String data[] = allLines.get(i);

			if (data.length > DESCRIPTION) {

				if (StringUtils.isNotBlank(data[CODE].trim())) {
					AttributeCode attributeCode = new AttributeCode();
					AttributeCodePk attributeCodePk = new AttributeCodePk();
					attributeCodePk.setAttributeCode(data[CODE].trim().toUpperCase());
					attributeCodePk.setAttributeType(attributeType.getAttributeType());
					attributeCode.setAttributeCodePk(attributeCodePk);

					StringBuilder desc = new StringBuilder();
					desc.append("<b>Definition:</b>").append(data[DEFINITION].replace("\n", "<br>")).append("<br>");
					desc.append("<b>Description:</b>").append(data[DESCRIPTION].replace("\n", "<br>"));
					attributeCode.setDescription(desc.toString());
					attributeCode.setLabel(data[CODE].toUpperCase().trim() + " " + data[LABEL].trim());

					attributeMap.get(attributeType).add(attributeCode);
				}
			} else {
				log.log(Level.WARNING, MessageFormat.format("Skipping line: {0} + line is mssing required fields.", lineNumber));
			}
		}
	}

}
