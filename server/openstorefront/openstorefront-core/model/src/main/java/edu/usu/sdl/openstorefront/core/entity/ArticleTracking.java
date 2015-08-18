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
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import java.util.Date;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author dshurtleff
 */
@APIDescription("Tracks article events")
public class ArticleTracking
		extends StandardEntity
{

	@PK(generated = true)
	@NotNull
	private String articleTrackingId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	private String attributeType;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	private String attributeCode;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = TrackEventCode.class)
	private String trackEventTypeCode;

	@NotNull
	private Date eventDts;

	@NotNull
	@APIDescription("The viewing client ip")
	private String clientIp;

	public ArticleTracking()
	{
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 67 * hash + Objects.hashCode(this.articleTrackingId);
		hash = 67 * hash + Objects.hashCode(this.attributeType);
		hash = 67 * hash + Objects.hashCode(this.attributeCode);
		hash = 67 * hash + Objects.hashCode(this.trackEventTypeCode);
		hash = 67 * hash + Objects.hashCode(this.eventDts);
		hash = 67 * hash + Objects.hashCode(this.clientIp);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ArticleTracking other = (ArticleTracking) obj;
		if (!Objects.equals(this.articleTrackingId, other.articleTrackingId)) {
			return false;
		}
		if (!Objects.equals(this.attributeType, other.attributeType)) {
			return false;
		}
		if (!Objects.equals(this.attributeCode, other.attributeCode)) {
			return false;
		}
		if (!Objects.equals(this.trackEventTypeCode, other.trackEventTypeCode)) {
			return false;
		}
		if (!Objects.equals(this.eventDts, other.eventDts)) {
			return false;
		}
		if (!Objects.equals(this.clientIp, other.clientIp)) {
			return false;
		}
		return true;
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

	public String getArticleTrackingId()
	{
		return articleTrackingId;
	}

	public void setArticleTrackingId(String articleTrackingId)
	{
		this.articleTrackingId = articleTrackingId;
	}

	public String getAttributeType()
	{
		return attributeType;
	}

	public void setAttributeType(String attributeType)
	{
		this.attributeType = attributeType;
	}

	public String getAttributeCode()
	{
		return attributeCode;
	}

	public void setAttributeCode(String attributeCode)
	{
		this.attributeCode = attributeCode;
	}

}
