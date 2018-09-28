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
@APIDescription("Defines the work plan action types available")
public class WorkPlanStepActionType
		extends LookupEntity<WorkPlanStepActionType>
{

	private static final long serialVersionUID = 1L;

	public static final String EMAIL = "EMAIL";
	public static final String ASSIGN_ENTRY = "ASSIGN_ENTRY";
	public static final String ASSIGN_ENTRY_GROUP = "ASSIGN_ENTRY_GROUP";
	public static final String APPROVE_ENTRY = "APPROVE_ENTRY";
	public static final String PENDING_ENTRY = "PENDING_ENTRY";
	public static final String INACTIVATE_ENTRY = "INACTIVATE_ENTRY";
	public static final String ACTIVATE_ENTRY = "ACTIVATE_ENTRY";

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public WorkPlanStepActionType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(EMAIL, newLookup(WorkPlanStepActionType.class, EMAIL, "Email"));
		codeMap.put(ASSIGN_ENTRY, newLookup(WorkPlanStepActionType.class, ASSIGN_ENTRY, "Assign Entry"));
		codeMap.put(ASSIGN_ENTRY_GROUP, newLookup(WorkPlanStepActionType.class, ASSIGN_ENTRY_GROUP, "Assign Entry to Entry Type Group"));
		codeMap.put(APPROVE_ENTRY, newLookup(WorkPlanStepActionType.class, APPROVE_ENTRY, "Approve Entry"));
		codeMap.put(PENDING_ENTRY, newLookup(WorkPlanStepActionType.class, PENDING_ENTRY, "Set Entry to Pending"));
		codeMap.put(INACTIVATE_ENTRY, newLookup(WorkPlanStepActionType.class, INACTIVATE_ENTRY, "Inactivate Entry"));
		codeMap.put(ACTIVATE_ENTRY, newLookup(WorkPlanStepActionType.class, ACTIVATE_ENTRY, "Activate Entry"));

		return codeMap;
	}

}
