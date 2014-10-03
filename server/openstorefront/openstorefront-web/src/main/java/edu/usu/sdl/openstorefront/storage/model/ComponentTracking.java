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
package edu.usu.sdl.openstorefront.storage.model;

import edu.usu.sdl.openstorefront.doc.ValidValueType;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.PK;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
public class ComponentTracking
		extends BaseComponent
{

	@PK
	@NotNull
	private String componentTrackingId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = TrackEventCode.class)
	private String trackEventTypeCode;

	@NotNull
	private Date eventDts;

	@NotNull
	private String clientIp;

	public ComponentTracking()
	{
	}

	public String getComponentTrackingId()
	{
		return componentTrackingId;
	}

	public void setComponentTrackingId(String componentTrackingId)
	{
		this.componentTrackingId = componentTrackingId;
	}

	public String getTrackEventTypeCode()
	{
		return trackEventTypeCode;
	}

	public void setTrackEventTypeCode(String trackEventTypeCode)
	{
		this.trackEventTypeCode = trackEventTypeCode;
	}

	public Date getEventDts()
	{
		return eventDts;
	}

	public void setEventDts(Date eventDts)
	{
		this.eventDts = eventDts;
	}

	public String getClientIp()
	{
		return clientIp;
	}

	public void setClientIp(String clientIp)
	{
		this.clientIp = clientIp;
	}

}
