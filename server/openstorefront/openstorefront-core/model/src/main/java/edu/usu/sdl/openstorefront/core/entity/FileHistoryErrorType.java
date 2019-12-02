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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
@SystemTable
@APIDescription("Defines error types")
public class FileHistoryErrorType
		extends LookupEntity<FileHistoryErrorType>
{

	public static final String SYSTEM = "SYSTEM";
	public static final String PARSE = "PARSE";
	public static final String VALIDATION = "VALIDATION";
	public static final String FORMAT = "FORMAT";
	public static final String WARNING = "WARNING";
	public static final String MAPPING = "MAPPING";

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public FileHistoryErrorType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(SYSTEM, newLookup(FileHistoryErrorType.class, SYSTEM, "System"));
		codeMap.put(PARSE, newLookup(FileHistoryErrorType.class, PARSE, "Parse"));
		codeMap.put(VALIDATION, newLookup(FileHistoryErrorType.class, VALIDATION, "Validation"));
		codeMap.put(FORMAT, newLookup(FileHistoryErrorType.class, FORMAT, "Format"));
		codeMap.put(WARNING, newLookup(FileHistoryErrorType.class, WARNING, "System"));
		codeMap.put(MAPPING, newLookup(FileHistoryErrorType.class, MAPPING, "System"));

		return codeMap;
	}

}
