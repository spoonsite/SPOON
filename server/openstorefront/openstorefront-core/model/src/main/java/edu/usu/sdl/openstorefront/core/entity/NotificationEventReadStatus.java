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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Holds read status")
public class NotificationEventReadStatus
		extends BaseEntity<NotificationEventReadStatus>
{

	public static final String FIELD_EVENTID = "eventId";
	public static final String FIELD_USERNAME = "username";

	@PK(generated = true)
	@NotNull
	private String readStatusId;

	@NotNull
	@FK(NotificationEvent.class)
	private String eventId;

	@FK(value = UserProfile.class, softReference = true)
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	@ConsumeField
	private String username;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public NotificationEventReadStatus()
	{
	}

	public String getReadStatusId()
	{
		return readStatusId;
	}

	public void setReadStatusId(String readStatusId)
	{
		this.readStatusId = readStatusId;
	}

	public String getEventId()
	{
		return eventId;
	}

	public void setEventId(String eventId)
	{
		this.eventId = eventId;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

}
