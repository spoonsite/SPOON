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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
@APIDescription("Tracking information on a component")
public class ComponentTracking
		extends BaseComponent<ComponentTracking>
{

	public static final String FIELD_EVENT_DTS = "eventDts";

	@PK(generated = true)
	@NotNull
	private String componentTrackingId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = TrackEventCode.class)
	@FK(TrackEventCode.class)
	private String trackEventTypeCode;

	@NotNull
	private Date eventDts;

	@NotNull
	private String clientIp;

	@FK(ResourceType.class)
	@APIDescription("External resource type")
	private String resourceType;
	private Boolean restrictedResouce;
	private String componentType;

	@APIDescription("External resource url")
	private String resourceLink;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ComponentTracking()
	{
	}

	@Override
	public String uniqueKey()
	{
		return getTrackEventTypeCode() + OpenStorefrontConstant.GENERAL_KEY_SEPARATOR + getEventDts();
	}

	@Override
	protected void customKeyClear()
	{
		setComponentTrackingId(null);
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

	public String getResourceLink()
	{
		return resourceLink;
	}

	public void setResourceLink(String resourceLink)
	{
		this.resourceLink = resourceLink;
	}

	public String getResourceType()
	{
		return resourceType;
	}

	public void setResourceType(String resourceType)
	{
		this.resourceType = resourceType;
	}

	public Boolean getRestrictedResouce()
	{
		return restrictedResouce;
	}

	public void setRestrictedResouce(Boolean restrictedResouce)
	{
		this.restrictedResouce = restrictedResouce;
	}

	public String getComponentType()
	{
		return componentType;
	}

	public void setComponentType(String componentType)
	{
		this.componentType = componentType;
	}

}
