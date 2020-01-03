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
@APIDescription("Component Approval Mode")
public class ApprovalMode
		extends LookupEntity<ApprovalMode>
{

	public static final String AUTO = "A";
	public static final String REQUIRED = "R";
	public static final String NOT_ALLOWED = "N";

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ApprovalMode()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(AUTO, newLookup(ApprovalMode.class, AUTO, "Auto"));
		codeMap.put(REQUIRED, newLookup(ApprovalMode.class, REQUIRED, "Required"));
		codeMap.put(NOT_ALLOWED, newLookup(ApprovalMode.class, NOT_ALLOWED, "Not Allowed"));
		return codeMap;
	}

}
