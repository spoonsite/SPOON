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
import edu.usu.sdl.openstorefront.util.StringProcessor;
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

	private static final int UID = 0;
	private static final int CODE = 1;
	private static final int LABEL = 2;
	private static final int DEFINITION = 3;
	private static final int DESCRIPTION = 4;
	private static final int JCA_ALIGNMENT = 5;
	private static final int JCSFL_ALIGNMENT = 6;
	private static final int JARM_ALIGNMENT = 7;
	private static final int INTERNAL_COMMENTS = 8;
	
	
	@Override
	protected void internalParse(CSVReader reader) throws IOException
	{
		AttributeType attributeType = new AttributeType();
		attributeType.setAttributeType(AttributeType.DI2E_SVCV4);
		attributeType.setDescription("DI2E SvcV-4 Alignment");

		//Default to true....Later an admin would need to determine which ones should only allow one.
		attributeType.setAllowMultipleFlg(Boolean.TRUE);
		attributeType.setArchitectureFlg(Boolean.TRUE);
		attributeType.setVisibleFlg(Boolean.TRUE);
		attributeType.setImportantFlg(Boolean.TRUE);
		attributeType.setRequiredFlg(Boolean.FALSE);

		attributeMap.put(attributeType, new ArrayList<>());

		int lineNumber;
		List<String[]> allLines = reader.readAll();
		for (int i = 1; i < allLines.size(); i++) {
			lineNumber = i;
			String data[] = allLines.get(i);

			if (data.length > DESCRIPTION) {

				if (StringUtils.isNotBlank(data[CODE].trim())) {
					AttributeCode attributeCode = new AttributeCode();
					AttributeCodePk attributeCodePk = new AttributeCodePk();
					attributeCodePk.setAttributeCode(data[UID].trim().toUpperCase());
					attributeCodePk.setAttributeType(attributeType.getAttributeType());
					attributeCode.setAttributeCodePk(attributeCodePk);

					StringBuilder desc = new StringBuilder();
					desc.append("<b>Definition:</b>").append(StringProcessor.stripeExtendedChars(data[DEFINITION].trim().replace("\n", "<br>"))).append("<br>");
					desc.append("<b>Description:</b>").append(StringProcessor.stripeExtendedChars(data[DESCRIPTION].trim().replace("\n", "<br>"))).append("<br>");
					desc.append("<b>JCA Alignment:</b>").append(StringProcessor.stripeExtendedChars(data[JCA_ALIGNMENT].trim().replace("\n", "<br>"))).append("<br>");
					desc.append("<b>JCSFL Alignment:</b>").append(StringProcessor.stripeExtendedChars(data[JCSFL_ALIGNMENT].trim().replace("\n", "<br>"))).append("<br>");
					desc.append("<b>JARM/ESL Alignment:</b>").append(StringProcessor.stripeExtendedChars(data[JARM_ALIGNMENT].trim().replace("\n", "<br>"))).append("<br>");
					attributeCode.setDescription(desc.toString());
					attributeCode.setArchitectureCode(data[CODE].trim().toUpperCase());
					attributeCode.setLabel(data[CODE].toUpperCase().trim() + " " + data[LABEL].trim());

					attributeMap.get(attributeType).add(attributeCode);
				}
			} else {
				log.log(Level.WARNING, MessageFormat.format("Skipping line: {0} + line is mssing required fields.", lineNumber));
			}
		}
	}

	@Override
	public String getHEADER()
	{
		//TagValue_UID,TagValue_Number,TagValue_Service Name,TagNotes_Service Definition,TagNotes_Service Description,TagValue_Example Specification,TagValue_Example Solution,TagValue_DI2E Framework 
		return "TagValue_UID";
	}

}
