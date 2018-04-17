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
@APIDescription("Tracking Event Types")
public class TrackEventCode
		extends LookupEntity<TrackEventCode>
{

	public static final String LOGIN = "L";
	public static final String VIEW = "V";
	public static final String EXTERNAL_VIEW = "EXTVIEW";
	public static final String EXTERNAL_LINK_CLICK = "ELC";
	public static final String COMPONENT_SYNC = "SYNC";

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(LOGIN, newLookup(TrackEventCode.class, LOGIN, "Login"));
		codeMap.put(VIEW, newLookup(TrackEventCode.class, VIEW, "View"));
		codeMap.put(EXTERNAL_VIEW, newLookup(TrackEventCode.class, EXTERNAL_VIEW, "External View"));
		codeMap.put(EXTERNAL_LINK_CLICK, newLookup(TrackEventCode.class, EXTERNAL_LINK_CLICK, "External Link Click"));
		codeMap.put(COMPONENT_SYNC, newLookup(TrackEventCode.class, COMPONENT_SYNC, "Component Sync"));
		return codeMap;
	}

}
