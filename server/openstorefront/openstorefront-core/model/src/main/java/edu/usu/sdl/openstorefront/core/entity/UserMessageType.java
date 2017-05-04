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
@APIDescription("Valid types of User Messages")
public class UserMessageType
		extends LookupEntity<UserMessageType>
{

	public static final String COMPONENT_WATCH = "CWATCH";
	public static final String USER_DATA_ALERT = "USERDATA";
	public static final String SYSTEM_ERROR_ALERT = "SYSERROR";
	public static final String COMPONENT_SUBMISSION_ALERT = "CMPSUB";
	public static final String APPROVAL_NOTIFICATION = "APPNOT";
	public static final String CHANGE_REQUEST_APPROVAL_NOTIFICATION = "CHGREQAPP";
	public static final String CHANGE_REQUEST_ALERT = "CHGREQ";
	public static final String USER_REGISTRATION = "USERREG";
	public static final String USER_NEED_APPROVAL = "USERAPPR";

	public UserMessageType()
	{
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(COMPONENT_WATCH, newLookup(UserMessageType.class, COMPONENT_WATCH, "Entry Watch"));
		codeMap.put(USER_DATA_ALERT, newLookup(UserMessageType.class, USER_DATA_ALERT, "User Data Alert"));
		codeMap.put(SYSTEM_ERROR_ALERT, newLookup(UserMessageType.class, SYSTEM_ERROR_ALERT, "System Error Alert"));
		codeMap.put(COMPONENT_SUBMISSION_ALERT, newLookup(UserMessageType.class, COMPONENT_SUBMISSION_ALERT, "Entry Submission Alert"));
		codeMap.put(APPROVAL_NOTIFICATION, newLookup(UserMessageType.class, APPROVAL_NOTIFICATION, "Entry Approval Notification"));
		codeMap.put(CHANGE_REQUEST_APPROVAL_NOTIFICATION, newLookup(UserMessageType.class, CHANGE_REQUEST_APPROVAL_NOTIFICATION, "Change Request Approval"));
		codeMap.put(CHANGE_REQUEST_ALERT, newLookup(UserMessageType.class, CHANGE_REQUEST_ALERT, "Change Request Alert"));
		codeMap.put(USER_REGISTRATION, newLookup(UserMessageType.class, USER_REGISTRATION, "User Registration"));
		codeMap.put(USER_NEED_APPROVAL, newLookup(UserMessageType.class, USER_NEED_APPROVAL, "User Needs Approval"));
		return codeMap;
	}

}
