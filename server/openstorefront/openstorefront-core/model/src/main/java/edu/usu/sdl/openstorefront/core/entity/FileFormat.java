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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.SystemTable;
import static edu.usu.sdl.openstorefront.core.entity.LookupEntity.newLookup;
import java.util.HashMap;
import java.util.Map;

@SystemTable
@APIDescription("Defines the supported upload formats")
public class FileFormat
		extends LookupEntity
{

	public static final String COMPONENT_STANDARD = "CMP_STANDARD";
	public static final String COMPONENT_ER2 = "CMP_ER2";
	public static final String COMPONENT_DESCRIBE = "CMP_DESCRIBE";
	public static final String ATTRIBUTE_STANDARD = "ATTR_STANDARD";
	public static final String ATTRIBUTE_SVCV4 = "ATTR_SVCV4";

	private String fileType;
	private String parserClass;
	private String fileRequirements;
	private boolean supportsDataMap;

	public FileFormat()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(COMPONENT_STANDARD, newLookup(FileFormat.class, COMPONENT_STANDARD, "Standard Format (ZIP, JSON)"));
		codeMap.put(COMPONENT_ER2, newLookup(FileFormat.class, COMPONENT_ER2, "ER2 Format (XML)"));
		codeMap.put(COMPONENT_DESCRIBE, newLookup(FileFormat.class, COMPONENT_DESCRIBE, "Describe (XML)"));
		codeMap.put(ATTRIBUTE_STANDARD, newLookup(FileFormat.class, ATTRIBUTE_STANDARD, "Standard Format (JSON)"));
		codeMap.put(ATTRIBUTE_SVCV4, newLookup(FileFormat.class, ATTRIBUTE_SVCV4, "Svcv4 Sparx export (CSV)"));

		//Add extra metadata
		((FileFormat) codeMap.get(COMPONENT_STANDARD)).setFileType(FileType.COMPONENT);
		((FileFormat) codeMap.get(COMPONENT_ER2)).setFileType(FileType.COMPONENT);
		((FileFormat) codeMap.get(COMPONENT_DESCRIBE)).setFileType(FileType.COMPONENT);
		((FileFormat) codeMap.get(ATTRIBUTE_STANDARD)).setFileType(FileType.ATTRIBUTE);
		((FileFormat) codeMap.get(ATTRIBUTE_SVCV4)).setFileType(FileType.ATTRIBUTE);

		((FileFormat) codeMap.get(COMPONENT_STANDARD)).setParserClass("edu.usu.sdl.openstorefront.service.io.parser.ComponentStandardParser");
		((FileFormat) codeMap.get(COMPONENT_ER2)).setParserClass("edu.usu.sdl.openstorefront.service.io.parser.ComponentER2Parser");
		((FileFormat) codeMap.get(COMPONENT_DESCRIBE)).setParserClass("edu.usu.sdl.openstorefront.service.io.parser.ComponentDescribeParser");
		((FileFormat) codeMap.get(ATTRIBUTE_STANDARD)).setParserClass("edu.usu.sdl.openstorefront.service.io.parser.AttributeStandardParser");
		((FileFormat) codeMap.get(ATTRIBUTE_SVCV4)).setParserClass("edu.usu.sdl.openstorefront.service.io.parser.AttributeSvcv4Parser");

		StringBuilder requirements = new StringBuilder();
		requirements.append("ZIP with media and JSON data containing component records or just the JSON file.  See Export.");
		((FileFormat) codeMap.get(COMPONENT_STANDARD)).setFileRequirements(requirements.toString());

		requirements = new StringBuilder();
		requirements.append("XML of ER2 Asset data");
		((FileFormat) codeMap.get(COMPONENT_ER2)).setFileRequirements(requirements.toString());
		
		requirements = new StringBuilder();
		requirements.append("XML of Describe Record");
		((FileFormat) codeMap.get(COMPONENT_DESCRIBE)).setFileRequirements(requirements.toString());
		

		requirements = new StringBuilder();
		requirements.append("JSON data containing the Attribute Types and Codes.  See Export.");
		((FileFormat) codeMap.get(ATTRIBUTE_STANDARD)).setFileRequirements(requirements.toString());

		requirements = new StringBuilder();
		requirements.append("CSV in SvcV-4 Functionality Description Format");
		((FileFormat) codeMap.get(ATTRIBUTE_SVCV4)).setFileRequirements(requirements.toString());
		return codeMap;
	}

	public String getFileType()
	{
		return fileType;
	}

	public void setFileType(String fileType)
	{
		this.fileType = fileType;
	}

	public String getParserClass()
	{
		return parserClass;
	}

	public void setParserClass(String parserClass)
	{
		this.parserClass = parserClass;
	}

	public String getFileRequirements()
	{
		return fileRequirements;
	}

	public void setFileRequirements(String fileRequirements)
	{
		this.fileRequirements = fileRequirements;
	}

	public boolean getSupportsDataMap()
	{
		return supportsDataMap;
	}

	public void setSupportsDataMap(boolean supportsDataMap)
	{
		this.supportsDataMap = supportsDataMap;
	}

}
