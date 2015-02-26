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
package edu.usu.sdl.openstorefront.storage.model;

import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.util.SystemTable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
@SystemTable
@APIDescription("Type of Error")
public class ErrorTypeCode
		extends LookupEntity
{

	public static final String SYSTEM = "SYS";
	public static final String REST_API = "API";
	public static final String USER_ERROR = "USER";
	public static final String AUTO_SYSTEM = "AUTO";
	public static final String IMPORT = "IMP";

	public ErrorTypeCode()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(SYSTEM, newLookup(ErrorTypeCode.class, SYSTEM, "System"));
		codeMap.put(REST_API, newLookup(ErrorTypeCode.class, REST_API, "REST Api"));
		codeMap.put(USER_ERROR, newLookup(ErrorTypeCode.class, USER_ERROR, "User Error"));
		codeMap.put(AUTO_SYSTEM, newLookup(ErrorTypeCode.class, AUTO_SYSTEM, "Auto System"));
		codeMap.put(IMPORT, newLookup(ErrorTypeCode.class, IMPORT, "Import"));
		return codeMap;
	}

}
