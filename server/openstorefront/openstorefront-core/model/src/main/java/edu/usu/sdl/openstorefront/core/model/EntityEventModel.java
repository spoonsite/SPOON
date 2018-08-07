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
package edu.usu.sdl.openstorefront.core.model;

import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.EntityEventType;
import java.util.Date;

/**
 *
 * @author dshurtleff
 */
public class EntityEventModel
{

	/**
	 * @see EntityEventType
	 */
	private String eventType;
	private Class bulkClassAffected;
	private Object entityChanged;

	//may be null
	private Object oldEntity;
	private Date eventTime = TimeUtil.currentDate();

	public EntityEventModel()
	{
	}

	@Override
	public String toString()
	{
		return "Event Type: " + eventType
				+ " Entity: " + (entityChanged != null ? entityChanged.getClass().getSimpleName() : "N/A")
				+ " Time: " + eventTime.toString();
	}

	public String getEventType()
	{
		return eventType;
	}

	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}

	public Object getEntityChanged()
	{
		return entityChanged;
	}

	public void setEntityChanged(Object entityChanged)
	{
		this.entityChanged = entityChanged;
	}

	public Object getOldEntity()
	{
		return oldEntity;
	}

	public void setOldEntity(Object oldEntity)
	{
		this.oldEntity = oldEntity;
	}

	public Date getEventTime()
	{
		return eventTime;
	}

	public void setEventTime(Date eventTime)
	{
		this.eventTime = eventTime;
	}

	public Class getBulkClassAffected()
	{
		return bulkClassAffected;
	}

	public void setBulkClassAffected(Class bulkClassAffected)
	{
		this.bulkClassAffected = bulkClassAffected;
	}

}
