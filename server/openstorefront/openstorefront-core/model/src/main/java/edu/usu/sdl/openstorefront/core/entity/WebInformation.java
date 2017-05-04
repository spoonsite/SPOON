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
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Version;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Part of the feedback ticket")
@Embeddable
public class WebInformation
		implements Serializable
{

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String location;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String userAgent;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String referrer;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String screenResolution;

	@Version
	private String storageVersion;

	public WebInformation()
	{
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getUserAgent()
	{
		return userAgent;
	}

	public void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}

	public String getReferrer()
	{
		return referrer;
	}

	public void setReferrer(String referrer)
	{
		this.referrer = referrer;
	}

	public String getScreenResolution()
	{
		return screenResolution;
	}

	public void setScreenResolution(String screenResolution)
	{
		this.screenResolution = screenResolution;
	}

	public String getStorageVersion()
	{
		return storageVersion;
	}

	public void setStorageVersion(String storageVersion)
	{
		this.storageVersion = storageVersion;
	}

}
