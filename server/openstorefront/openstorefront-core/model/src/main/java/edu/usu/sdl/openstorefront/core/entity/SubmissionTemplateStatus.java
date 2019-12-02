/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
@APIDescription("Template Status")
public class SubmissionTemplateStatus
		extends LookupEntity<SubmissionTemplateStatus>
{

	private static final long serialVersionUID = 1L;

	public static final String VERIFIED = "VERIFIED";
	public static final String PENDING_VERIFICATION = "PENDING_VERIFICATION";
	public static final String INCOMPLETE = "INCOMPLETE";

	@SuppressWarnings({"squid:S1186"})
	public SubmissionTemplateStatus()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(VERIFIED, newLookup(SubmissionFormFieldType.class, VERIFIED, "Verfied"));
		codeMap.put(PENDING_VERIFICATION, newLookup(SubmissionFormFieldType.class, PENDING_VERIFICATION, "Pending Verification"));
		codeMap.put(INCOMPLETE, newLookup(SubmissionFormFieldType.class, INCOMPLETE, "Incomplete"));
		return codeMap;
	}

}
