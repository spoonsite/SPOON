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
@APIDescription("Define the type of modification")
public class ModificationType
		extends LookupEntity<ModificationType>
{

	public static final String IMPORT = "IMPORT";
	public static final String API = "API";
	public static final String EXTERNAL = "EXTERNAL";
	public static final String MERGE = "MERGE";

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ModificationType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();

		codeMap.put(IMPORT, newLookup(FileHistoryErrorType.class, IMPORT, "Import"));
		codeMap.put(API, newLookup(FileHistoryErrorType.class, API, "API"));
		codeMap.put(EXTERNAL, newLookup(FileHistoryErrorType.class, EXTERNAL, "External Pull"));
		codeMap.put(MERGE, newLookup(FileHistoryErrorType.class, MERGE, "Merge"));

		return codeMap;
	}

}
