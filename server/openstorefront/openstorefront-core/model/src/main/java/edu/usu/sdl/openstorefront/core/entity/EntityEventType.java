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
@APIDescription("Defines event type that occur on an entity")
public class EntityEventType
		extends LookupEntity<WorkPlanStepActionType>
{

	private static final long serialVersionUID = 1L;

	public static final String CREATE = "CREATE";
	public static final String UPDATE = "UPDATE";
	public static final String DELETE = "DELETE";
	public static final String ENTRY_DELETE = "ENTRY_DELETE";
	public static final String ACTIVATED = "ACTIVATED";
	public static final String DEACTIVATED = "DEACTIVATED";
	public static final String APPROVED = "APPROVED";
	public static final String PENDING = "PENDING";
	public static final String PENDING_CHANGE_REQUEST = "PENDING_CHANGE_REQUEST";
	public static final String NEW_SUBMISSION_NOT_SUBMITTTED = "NOTSUBMITTED";
	public static final String PUBLISH_EVALUATION = "PUBLISH_EVALUATION";
	public static final String UNPUBLISH_EVALUATION = "UNPUBLISH_EVALUATION";

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public EntityEventType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(CREATE, newLookup(EntityEventType.class, CREATE, "Create"));
		codeMap.put(UPDATE, newLookup(EntityEventType.class, UPDATE, "Update"));
		codeMap.put(DELETE, newLookup(EntityEventType.class, DELETE, "Delete"));
		codeMap.put(ENTRY_DELETE, newLookup(EntityEventType.class, ENTRY_DELETE, "Entry Delete"));
		codeMap.put(ACTIVATED, newLookup(EntityEventType.class, ACTIVATED, "Activate"));
		codeMap.put(DEACTIVATED, newLookup(EntityEventType.class, DEACTIVATED, "Deactivated"));
		codeMap.put(APPROVED, newLookup(EntityEventType.class, APPROVED, "Approved"));
		codeMap.put(PENDING, newLookup(EntityEventType.class, PENDING, "Pending"));
		codeMap.put(PENDING_CHANGE_REQUEST, newLookup(EntityEventType.class, PENDING_CHANGE_REQUEST, "Pending Change Request"));
		codeMap.put(NEW_SUBMISSION_NOT_SUBMITTTED, newLookup(EntityEventType.class, NEW_SUBMISSION_NOT_SUBMITTTED, "New Submission (Not Submitted)"));
		codeMap.put(PUBLISH_EVALUATION, newLookup(EntityEventType.class, PUBLISH_EVALUATION, "Published Evaluation"));
		codeMap.put(UNPUBLISH_EVALUATION, newLookup(EntityEventType.class, UNPUBLISH_EVALUATION, "Unpublish Evaluation"));

		return codeMap;
	}

}
