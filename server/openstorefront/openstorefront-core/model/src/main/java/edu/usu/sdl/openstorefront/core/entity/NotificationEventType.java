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
import static edu.usu.sdl.openstorefront.core.entity.LookupEntity.newLookup;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
@SystemTable
@APIDescription("Define the event types")
public class NotificationEventType
		extends LookupEntity<NotificationEventType>
{

	public static final String WATCH = "WATCH";
	public static final String IMPORT = "IMPORT";
	public static final String TASK = "TASK";
	public static final String REPORT = "REPORT";
	public static final String ADMIN = "ADMIN";
	public static final String SUBMISSION = "SUBMISSION";
	public static final String CHANGE_REQUEST = "CHANGERQT";
	public static final String WORKPLAN_CHANGE = "WORKPLAN_CHANGE";

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public NotificationEventType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();

		codeMap.put(WATCH, newLookup(NotificationEventType.class, WATCH, "Watch"));
		codeMap.put(IMPORT, newLookup(NotificationEventType.class, IMPORT, "Import"));
		codeMap.put(TASK, newLookup(NotificationEventType.class, TASK, "Task"));
		codeMap.put(REPORT, newLookup(NotificationEventType.class, REPORT, "Report"));
		codeMap.put(ADMIN, newLookup(NotificationEventType.class, ADMIN, "Admin"));
		codeMap.put(SUBMISSION, newLookup(NotificationEventType.class, SUBMISSION, "Submission"));
		codeMap.put(CHANGE_REQUEST, newLookup(NotificationEventType.class, CHANGE_REQUEST, "Change Request"));
		codeMap.put(WORKPLAN_CHANGE, newLookup(NotificationEventType.class, WORKPLAN_CHANGE, "Workplan change"));

		return codeMap;
	}

}
