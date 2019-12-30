/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
@APIDescription("Defines the archives")
public class SystemArchiveType
		extends LookupEntity<SystemArchiveType>
{

	public static final String DBEXPORT = "DBEXPORT";
	public static final String FULL = "FULLEXPORT";
	public static final String GENERAL = "GENERAL";

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public SystemArchiveType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(DBEXPORT, newLookup(SystemArchiveType.class, DBEXPORT, "DB Export"));
		codeMap.put(FULL, newLookup(SystemArchiveType.class, FULL, "Full Export"));
		codeMap.put(GENERAL, newLookup(SystemArchiveType.class, GENERAL, "General"));
		return codeMap;
	}

}
