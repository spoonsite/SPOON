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
import static edu.usu.sdl.openstorefront.storage.model.LookupEntity.newLookup;
import edu.usu.sdl.openstorefront.util.SystemTable;
import java.util.HashMap;
import java.util.Map;

/**
 * Integration Types
 *
 * @author dshurtleff
 */
@SystemTable
@APIDescription("External Systems Integration Types")
public class IntegrationType
		extends LookupEntity
{

	public static final String JIRA = "JIRA";

	public IntegrationType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(JIRA, newLookup(IntegrationType.class, JIRA, "Jira"));
		return codeMap;
	}

}
