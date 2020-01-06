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
import java.util.HashMap;
import java.util.Map;

@SystemTable
@APIDescription("Defines the supported upload formats")
public class FileFormat
		extends LookupEntity<FileFormat>
{

	public static final String COMPONENT_STANDARD = "CMP_STANDARD";
	public static final String COMPONENT_MAPPED_CSV = "CMP_MAP_CSV";
	public static final String COMPONENT_MAPPED_TSV = "CMP_MAP_TSV";
	public static final String COMPONENT_MAPPED_EXCEL = "CMP_MAP_EXCEL";
	public static final String COMPONENT_MAPPED_JSON = "CMP_MAP_JSON";
	public static final String COMPONENT_MAPPED_XML = "CMP_MAP_XML";

	public static final String ATTRIBUTE_STANDARD = "ATTR_STANDARD";
	public static final String ATTRIBUTE_MAPPED_CSV = "ATTR_MAP_CSV";
	public static final String ATTRIBUTE_MAPPED_TSV = "ATTR_MAP_TSV";
	public static final String ATTRIBUTE_MAPPED_EXCEL = "ATTR_MAP_EXCEL";
	public static final String ATTRIBUTE_MAPPED_JSON = "ATTR_MAP_JSON";
	public static final String ATTRIBUTE_MAPPED_XML = "ATTR_MAP_XML";

	public static final String SUBMISSION_TEMPLATE_STANDARD = "SUBMTEMP_STANDARD";
	public static final String CHECKLIST_QUESTIONS_STANDARD = "CHKQ_STANDARD";

	private String fileType;
	private String parserClass;
	private String fileRequirements;
	private boolean supportsDataMap;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public FileFormat()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();

		FileFormat fileFormat = newLookup(FileFormat.class, COMPONENT_STANDARD, "Standard Format (ZIP, JSON)");
		fileFormat.setFileType(FileType.COMPONENT);
		fileFormat.setFileRequirements("ZIP with media and JSON data containing component records or just the JSON file.  See Export.");
		fileFormat.setParserClass("edu.usu.sdl.openstorefront.service.io.parser.ComponentStandardParser");
		fileFormat.setSupportsDataMap(false);
		addFormat(codeMap, fileFormat);

		fileFormat = newLookup(FileFormat.class, COMPONENT_MAPPED_CSV, "Component Mapped CSV");
		fileFormat.setFileType(FileType.COMPONENT);
		fileFormat.setFileRequirements("Mapped Comma-Separated-Value File");
		fileFormat.setParserClass("edu.usu.sdl.openstorefront.service.io.parser.ComponentMappedCSVParser");
		fileFormat.setSupportsDataMap(true);
		addFormat(codeMap, fileFormat);

		fileFormat = newLookup(FileFormat.class, COMPONENT_MAPPED_TSV, "Component Mapped TSV");
		fileFormat.setFileType(FileType.COMPONENT);
		fileFormat.setFileRequirements("Mapped Tab-Separated-Value file");
		fileFormat.setParserClass("edu.usu.sdl.openstorefront.service.io.parser.ComponentMappedTSVParser");
		fileFormat.setSupportsDataMap(true);
		addFormat(codeMap, fileFormat);

		fileFormat = newLookup(FileFormat.class, COMPONENT_MAPPED_EXCEL, "Component Mapped Excel (XLSX)");
		fileFormat.setFileType(FileType.COMPONENT);
		fileFormat.setFileRequirements("Mapped Excel file (XLSX)");
		fileFormat.setParserClass("edu.usu.sdl.openstorefront.service.io.parser.ComponentMappedExcelParser");
		fileFormat.setSupportsDataMap(true);
		addFormat(codeMap, fileFormat);

		fileFormat = newLookup(FileFormat.class, COMPONENT_MAPPED_JSON, "Component Mapped JSON");
		fileFormat.setFileType(FileType.COMPONENT);
		fileFormat.setFileRequirements("Mapped JSON file");
		fileFormat.setParserClass("edu.usu.sdl.openstorefront.service.io.parser.ComponentMappedJsonParser");
		fileFormat.setSupportsDataMap(true);
		addFormat(codeMap, fileFormat);

		fileFormat = newLookup(FileFormat.class, COMPONENT_MAPPED_XML, "Component Mapped XML");
		fileFormat.setFileType(FileType.COMPONENT);
		fileFormat.setFileRequirements("Mapped XML file");
		fileFormat.setParserClass("edu.usu.sdl.openstorefront.service.io.parser.ComponentMappedXMLParser");
		fileFormat.setSupportsDataMap(true);
		addFormat(codeMap, fileFormat);

		//Attributes
		fileFormat = newLookup(FileFormat.class, ATTRIBUTE_STANDARD, "Standard Format (JSON)");
		fileFormat.setFileType(FileType.ATTRIBUTE);
		fileFormat.setFileRequirements("JSON data containing the Attribute Types and Codes.  See Export.");
		fileFormat.setParserClass("edu.usu.sdl.openstorefront.service.io.parser.AttributeStandardParser");
		fileFormat.setSupportsDataMap(false);
		addFormat(codeMap, fileFormat);

		fileFormat = newLookup(FileFormat.class, ATTRIBUTE_MAPPED_CSV, "Attribute Mapped CSV");
		fileFormat.setFileType(FileType.ATTRIBUTE);
		fileFormat.setFileRequirements("Mapped Comma-Separated-Value File (Remove header line before upload)");
		fileFormat.setParserClass("edu.usu.sdl.openstorefront.service.io.parser.AttributeMappedCSVParser");
		fileFormat.setSupportsDataMap(true);
		addFormat(codeMap, fileFormat);

		fileFormat = newLookup(FileFormat.class, ATTRIBUTE_MAPPED_TSV, "Attribute Mapped TSV");
		fileFormat.setFileType(FileType.ATTRIBUTE);
		fileFormat.setFileRequirements("Mapped Tab-Separated-Value File (Remove header line before upload");
		fileFormat.setParserClass("edu.usu.sdl.openstorefront.service.io.parser.AttributeMappedTSVParser");
		fileFormat.setSupportsDataMap(true);
		addFormat(codeMap, fileFormat);

		fileFormat = newLookup(FileFormat.class, ATTRIBUTE_MAPPED_EXCEL, "Attribute Mapped Excel (XLSX)");
		fileFormat.setFileType(FileType.ATTRIBUTE);
		fileFormat.setFileRequirements("Mapped Excel file (XLSX) (Remove header line before upload)");
		fileFormat.setParserClass("edu.usu.sdl.openstorefront.service.io.parser.AttributeMappedExcelParser");
		fileFormat.setSupportsDataMap(true);
		addFormat(codeMap, fileFormat);

		fileFormat = newLookup(FileFormat.class, ATTRIBUTE_MAPPED_JSON, "Attribute Mapped JSON");
		fileFormat.setFileType(FileType.ATTRIBUTE);
		fileFormat.setFileRequirements("Mapped JSON file");
		fileFormat.setParserClass("edu.usu.sdl.openstorefront.service.io.parser.AttributeMappedJsonParser");
		fileFormat.setSupportsDataMap(true);
		addFormat(codeMap, fileFormat);

		fileFormat = newLookup(FileFormat.class, ATTRIBUTE_MAPPED_XML, "Attribute Mapped XML");
		fileFormat.setFileType(FileType.ATTRIBUTE);
		fileFormat.setFileRequirements("Mapped XML file");
		fileFormat.setParserClass("edu.usu.sdl.openstorefront.service.io.parser.AttributeMappedXMLParser");
		fileFormat.setSupportsDataMap(true);
		addFormat(codeMap, fileFormat);

		fileFormat = newLookup(FileFormat.class, CHECKLIST_QUESTIONS_STANDARD, "Checklist Question Standard");
		fileFormat.setFileType(FileType.CHECKLISTQUESTIONS);
		fileFormat.setFileRequirements("JSON file of checklist questions");
		fileFormat.setParserClass("edu.usu.sdl.openstorefront.service.io.parser.ChecklistQuestionStandardParser");
		fileFormat.setSupportsDataMap(false);
		addFormat(codeMap, fileFormat);

		fileFormat = newLookup(FileFormat.class, SUBMISSION_TEMPLATE_STANDARD, "Submission Template Standard");
		fileFormat.setFileType(FileType.SUBMISSION_TEMPLATE);
		fileFormat.setFileRequirements("JSON file of a Submission Template");
		fileFormat.setParserClass("edu.usu.sdl.openstorefront.service.io.parser.SubmissionTemplateStandardParser");
		fileFormat.setSupportsDataMap(false);
		addFormat(codeMap, fileFormat);

		return codeMap;
	}

	private void addFormat(Map<String, LookupEntity> codeMap, FileFormat fileFormat)
	{
		codeMap.put(fileFormat.getCode(), fileFormat);
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
