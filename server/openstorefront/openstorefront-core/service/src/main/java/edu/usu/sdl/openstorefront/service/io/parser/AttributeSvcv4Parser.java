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

import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryErrorType;
import edu.usu.sdl.openstorefront.core.model.AttributeAll;
import edu.usu.sdl.openstorefront.core.spi.parser.BaseAttributeParser;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.CSVReader;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.GenericReader;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

public class AttributeSvcv4Parser
		extends BaseAttributeParser
{

	private static final Logger LOG = Logger.getLogger(SvcAttributeParser.class.getName());

	private static final int UID = 0;
	private static final int CODE = 1;
	private static final int LABEL = 2;
	private static final int DEFINITION = 3;
	private static final int DESCRIPTION = 4;
	private static final int JCA_ALIGNMENT = 5;
	private static final int JCSFL_ALIGNMENT = 6;
	private static final int JARM_ALIGNMENT = 7;
	private static final int INTERNAL_COMMENTS = 8;

	private static final String HEADER_LINE = "TagValue_UID";

	//There is only one type; this format just loads codes
	//Initialize on the first record (Parser run in a single thread)
	private AttributeAll attributeAll = null;

	@Override
	protected GenericReader getReader(InputStream in)
	{
		return new CSVReader(in);
	}

	@Override
	public String checkFormat(String mimeType, InputStream input)
	{
		if (mimeType.contains("csv") || mimeType.contains("ms-excel")) {
			return "";
		} else {
			return "Invalid format. Please upload a CSV in SVCV-4 Format. (Mimetype incorrect.)";
		}
	}

	@Override
	protected <T> Object parseRecord(T record)
	{

		String[] data = (String[]) record;
		if (attributeAll == null) {
			attributeAll = defaultAttributeAll();
			attributeAll.getAttributeType().setAttributeType(AttributeType.DI2E_SVCV4);
			attributeAll.getAttributeType().setDescription("DI2E SvcV-4 Alignment");
			attributeAll.getAttributeType().setArchitectureFlg(Boolean.TRUE);
			attributeAll.getAttributeType().setAllowMultipleFlg(Boolean.TRUE);
			attributeAll.getAttributeType().setVisibleFlg(Boolean.TRUE);
			attributeAll.getAttributeType().setImportantFlg(Boolean.TRUE);
			attributeAll.getAttributeType().setRequiredFlg(Boolean.FALSE);
			attributeAll.getAttributeType().setAllowUserGeneratedCodes(Boolean.FALSE);
		}

		AttributeType attributeType = attributeAll.getAttributeType();

		if (data.length > DESCRIPTION) {

			String code = data[UID].trim().toUpperCase();
			if ("0".equals(code) == false) {
				code = StringUtils.stripStart(code, "0");
			}
			if (StringUtils.isNotBlank(code)) {
				if (HEADER_LINE.equalsIgnoreCase(code)) {
					//Skip header
					return null;
				}

				AttributeCode attributeCode = new AttributeCode();

				AttributeCodePk attributeCodePk = new AttributeCodePk();
				attributeCodePk.setAttributeCode(code);
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

				attributeAll.getAttributeCodes().add(attributeCode);
				return attributeAll;
			} else {
				fileHistoryAll.addError(FileHistoryErrorType.WARNING, MessageFormat.format("Skipping record: {0} + record is missing UID or UID doesn't resolve. (0 padding is removed)", currentRecordNumber));
			}

		} else {
			fileHistoryAll.addError(FileHistoryErrorType.WARNING, MessageFormat.format("Skipping record: {0} + record is missing required fields.", currentRecordNumber));
		}

		return null;
	}
}
