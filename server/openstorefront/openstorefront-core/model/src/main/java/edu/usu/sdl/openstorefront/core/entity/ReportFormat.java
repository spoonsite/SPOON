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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.SystemTable;
import static edu.usu.sdl.openstorefront.core.entity.LookupEntity.newLookup;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
@SystemTable
@APIDescription("Defines report output formats")
public class ReportFormat
		extends LookupEntity<ReportFormat>
{

	public static final String CSV = "text-csv";
	public static final String XLSX = "application-xlsx";
	public static final String PDF = "application-pdf";
	public static final String HTML = "text-html";

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ReportFormat()
	{
	}

	public static String mimeType(String formatCode)
	{
		return formatCode.replace("-", "/");
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(CSV, newLookup(ReportFormat.class, CSV, "Comma-Separated Values (CSV)"));
		codeMap.put(XLSX, newLookup(ReportFormat.class, XLSX, "Excel"));
		codeMap.put(PDF, newLookup(ReportFormat.class, PDF, "Portable Document Format (PDF)"));
		codeMap.put(HTML, newLookup(ReportFormat.class, HTML, "HTML"));
		return codeMap;
	}

}
