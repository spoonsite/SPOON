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
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Notification Event")
public class NotificationEvent
		extends StandardEntity<NotificationEvent>
{

	@PK(generated = true)
	@NotNull
	private String eventId;

	@NotNull
	@ValidValueType(value = {}, lookupClass = NotificationEventType.class)
	@FK(NotificationEventType.class)
	private String eventType;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@ConsumeField
	private String message;

	@FK(UserProfile.class)
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	@ConsumeField
	private String username;

	@ConsumeField
	private String roleGroup;

	@APIDescription("The entity that event occured on")
	private String entityName;

	@APIDescription("The entity id that event occured on")
	private String entityId;

	@APIDescription("Internal Entity, if applicable")
	private String entityMetaDataStatus;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public NotificationEvent()
	{
	}

	public String getEventId()
	{
		return eventId;
	}

	public void setEventId(String eventId)
	{
		this.eventId = eventId;
	}

	public String getEventType()
	{
		return eventType;
	}

	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getRoleGroup()
	{
		return roleGroup;
	}

	public void setRoleGroup(String roleGroup)
	{
		this.roleGroup = roleGroup;
	}

	public String getEntityName()
	{
		return entityName;
	}

	public void setEntityName(String entityName)
	{
		this.entityName = entityName;
	}

	public String getEntityId()
	{
		return entityId;
	}

	public void setEntityId(String entityId)
	{
		this.entityId = entityId;
	}

	public String getEntityMetaDataStatus()
	{
		return entityMetaDataStatus;
	}

	public void setEntityMetaDataStatus(String entityMetaDataStatus)
	{
		this.entityMetaDataStatus = entityMetaDataStatus;
	}

}
