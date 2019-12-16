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
@APIDescription("Defines Changes that on an entity for the change log")
public class ChangeType
		extends LookupEntity<ChangeType>
{

	public static final String UPDATED = "UPDATED";
	public static final String ADDED = "ADDED";
	public static final String REMOVED = "REMOVED";
	public static final String SNAPSHOT = "SNAPSHOT";
	public static final String RESTORE = "RESTORE";
	public static final String WORKPLAN_CHANGE = "WORKPLAN_CHANGE";
	public static final String APPROVED = "APPROVED";
	public static final String SUBMITTED = "SUBMITTED";
	public static final String ACTIVATED = "ACTIVATED";
	public static final String INACTIVATED = "INACTIVATED";

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ChangeType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(UPDATED, newLookup(ChangeType.class, UPDATED, "Updated"));
		codeMap.put(ADDED, newLookup(ChangeType.class, ADDED, "Added"));
		codeMap.put(REMOVED, newLookup(ChangeType.class, REMOVED, "Removed"));
		codeMap.put(SNAPSHOT, newLookup(ChangeType.class, SNAPSHOT, "Snapshot"));
		codeMap.put(RESTORE, newLookup(ChangeType.class, RESTORE, "Restore"));
		codeMap.put(WORKPLAN_CHANGE, newLookup(ChangeType.class, WORKPLAN_CHANGE, "WorkPlan Change"));
		codeMap.put(APPROVED, newLookup(ChangeType.class, APPROVED, "Approved"));
		codeMap.put(SUBMITTED, newLookup(ChangeType.class, SUBMITTED, "Submitted"));
		codeMap.put(ACTIVATED, newLookup(ChangeType.class, ACTIVATED, "Activated"));
		codeMap.put(INACTIVATED, newLookup(ChangeType.class, INACTIVATED, "Inactivated"));
		return codeMap;
	}

}
