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
import static edu.usu.sdl.openstorefront.core.entity.LookupEntity.newLookup;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
@SystemTable
@APIDescription("Describes the type of comment for categorization")
public class ComponentCommentType
		extends LookupEntity<ChangeType>
{

	private static final long serialVersionUID = 1L;

	public static final String ADJUSTMENT_REQUIRED = "ADJUSTMENT";
	public static final String EXPIRE = "EXPIRE";
	public static final String INFORMATION = "INFO";
	public static final String MERGE = "MERGE";
	public static final String ADMINISTRATION = "ADMIN";

	@SuppressWarnings({"squid:S1186"})
	public ComponentCommentType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(ADJUSTMENT_REQUIRED, newLookup(ComponentCommentType.class, ADJUSTMENT_REQUIRED, "Adjustment Required"));
		codeMap.put(EXPIRE, newLookup(ComponentCommentType.class, EXPIRE, "Expired"));
		codeMap.put(INFORMATION, newLookup(ComponentCommentType.class, INFORMATION, "General Information"));
		codeMap.put(MERGE, newLookup(ComponentCommentType.class, MERGE, "Merge"));
		codeMap.put(ADMINISTRATION, newLookup(ComponentCommentType.class, ADMINISTRATION, "Administration"));
		return codeMap;
	}
}
