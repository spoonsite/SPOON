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
@APIDescription("Highlight new item type: link, component, article")
public class HighlightType
		extends LookupEntity<HighlightType>
{

	public static final String COMPONENT = "C";
	public static final String ARTICLE = "A";
	public static final String EXTERNAL_LINK = "EL";

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public HighlightType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(COMPONENT, newLookup(HighlightType.class, COMPONENT, "Component"));
		codeMap.put(ARTICLE, newLookup(HighlightType.class, ARTICLE, "Article"));
		codeMap.put(EXTERNAL_LINK, newLookup(HighlightType.class, EXTERNAL_LINK, "External Link"));
		return codeMap;
	}

}
