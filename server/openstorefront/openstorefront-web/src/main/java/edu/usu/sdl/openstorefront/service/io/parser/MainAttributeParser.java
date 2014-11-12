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
import edu.usu.sdl.openstorefront.util.Convert;
import edu.usu.sdl.openstorefront.util.StringProcessor;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
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

	private static final int TYPE = 0;
	private static final int DESC = 1;
	private static final int ARCH_FLG = 2;
	private static final int VISIBLE = 3;
	private static final int IMPORTANT = 4;
	private static final int REQUIRED = 5;
	private static final int CODE = 6;
	private static final int CODE_LABEL = 7;
	private static final int CODE_DESCRIPTION = 8;
	private static final int EXTERNAL_LINK = 9;
	private static final int GROUP = 10;
	private static final int SORT_ORDER = 11;

	private static final String HEADER = "Attribute Type";

	@Override
	protected void internalParse(CSVReader reader) throws IOException
	{
		int lineNumber = 1;
		String data[] = reader.readNext();
		while (data != null) {
			if (data.length > EXTERNAL_LINK
					&& HEADER.equals(data[TYPE].trim()) == false) {

				AttributeType attributeType = new AttributeType();
				attributeType.setAttributeType(data[TYPE].trim().toUpperCase());
				attributeType.setDescription(data[DESC].trim());

				//Default to true....Later an admin would need to determine which ones should only allow one.
				attributeType.setAllowMutlipleFlg(Boolean.TRUE);
				attributeType.setArchitectureFlg(Convert.toBoolean(data[ARCH_FLG]));
				attributeType.setVisibleFlg(Convert.toBoolean(data[VISIBLE]));
				attributeType.setImportantFlg(Convert.toBoolean(data[IMPORTANT]));
				attributeType.setRequiredFlg(Convert.toBoolean(data[REQUIRED]));

				AttributeCode attributeCode = new AttributeCode();
				AttributeCodePk attributeCodePk = new AttributeCodePk();
				attributeCodePk.setAttributeCode(data[CODE].trim().toUpperCase());
				attributeCodePk.setAttributeType(attributeType.getAttributeType());
				attributeCode.setAttributeCodePk(attributeCodePk);
				attributeCode.setDescription(StringProcessor.stripeExtendedChars(data[CODE_DESCRIPTION].trim()));
				attributeCode.setLabel(StringProcessor.stripeExtendedChars(data[CODE_LABEL].trim()));
				attributeCode.setDetailUrl(data[EXTERNAL_LINK].trim());

				if (data.length > GROUP) {
					attributeCode.setGroupCode(data[GROUP].trim());
				}

				if (data.length > SORT_ORDER) {
					attributeCode.setSortOrder(Convert.toInteger(data[SORT_ORDER].trim()));
				}

				if (attributeMap.containsKey(attributeType)) {
					attributeMap.get(attributeType).add(attributeCode);
				} else {
					List<AttributeCode> attributeCodes = new ArrayList<>();
					attributeCodes.add(attributeCode);
					attributeMap.put(attributeType, attributeCodes);
				}

			} else {
				if (data.length > TYPE && HEADER.equals(data[TYPE]) == false) {
					log.log(Level.WARNING, MessageFormat.format("Line: {0} is missing fields. (Skipping)  data length: {1}", new Object[]{lineNumber, data.length}));
				}
			}
			data = reader.readNext();
			lineNumber++;
		}
	}

}
