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

import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.PK;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
public class UserTracking
		extends BaseEntity
{

	@PK
	@NotNull
	private String trackingId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	private String trackEventTypeCode;

	private String browser;
	private String userAgent;
	private String browserVersion;

	@NotNull
	private Date eventDts;

	private Integer screenWidth;
	private Integer screenHeight;
	private Boolean mobileDevice;
	private String osPlatform;

	@NotNull
	private String clientIp;

	public UserTracking()
	{
	}

	public String getTrackingId()
	{
		return trackingId;
	}

	public void setTrackingId(String trackingId)
	{
		this.trackingId = trackingId;
	}

	public String getTrackEventTypeCode()
	{
		return trackEventTypeCode;
	}

	public void setTrackEventTypeCode(String trackEventTypeCode)
	{
		this.trackEventTypeCode = trackEventTypeCode;
	}

	public String getBrowser()
	{
		return browser;
	}

	public void setBrowser(String browser)
	{
		this.browser = browser;
	}

	public String getUserAgent()
	{
		return userAgent;
	}

	public void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}

	public String getBrowserVersion()
	{
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion)
	{
		this.browserVersion = browserVersion;
	}

	public Date getEventDts()
	{
		return eventDts;
	}

	public void setEventDts(Date eventDts)
	{
		this.eventDts = eventDts;
	}

	public Integer getScreenWidth()
	{
		return screenWidth;
	}

	public void setScreenWidth(Integer screenWidth)
	{
		this.screenWidth = screenWidth;
	}

	public Integer getScreenHeight()
	{
		return screenHeight;
	}

	public void setScreenHeight(Integer screenHeight)
	{
		this.screenHeight = screenHeight;
	}

	public Boolean getMobileDevice()
	{
		return mobileDevice;
	}

	public void setMobileDevice(Boolean mobileDevice)
	{
		this.mobileDevice = mobileDevice;
	}

	public String getOsPlatform()
	{
		return osPlatform;
	}

	public void setOsPlatform(String osPlatform)
	{
		this.osPlatform = osPlatform;
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
