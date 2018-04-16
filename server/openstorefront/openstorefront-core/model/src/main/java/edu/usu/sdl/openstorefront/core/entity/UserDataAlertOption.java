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
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Version;

/**
 *
 * @author dshurtleff
 */
@APIDescription("This is part of the alert entity")
@Embeddable
public class UserDataAlertOption
		implements Serializable
{

	@ConsumeField
	private Boolean alertOnTags;

	@ConsumeField
	private Boolean alertOnReviews;

	@ConsumeField
	private Boolean alertOnQuestions;

	@ConsumeField
	private Boolean alertOnContactUpdate;

	@ConsumeField
	private Boolean alertOnUserAttributeCodes;

	@Version
	private String storageVersion;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public UserDataAlertOption()
	{
	}

	public Boolean getAlertOnTags()
	{
		return alertOnTags;
	}

	public void setAlertOnTags(Boolean alertOnTags)
	{
		this.alertOnTags = alertOnTags;
	}

	public Boolean getAlertOnReviews()
	{
		return alertOnReviews;
	}

	public void setAlertOnReviews(Boolean alertOnReviews)
	{
		this.alertOnReviews = alertOnReviews;
	}

	public Boolean getAlertOnQuestions()
	{
		return alertOnQuestions;
	}

	public void setAlertOnQuestions(Boolean alertOnQuestions)
	{
		this.alertOnQuestions = alertOnQuestions;
	}

	public String getStorageVersion()
	{
		return storageVersion;
	}

	public void setStorageVersion(String storageVersion)
	{
		this.storageVersion = storageVersion;
	}

	public Boolean getAlertOnContactUpdate()
	{
		return alertOnContactUpdate;
	}

	public void setAlertOnContactUpdate(Boolean alertOnContactUpdate)
	{
		this.alertOnContactUpdate = alertOnContactUpdate;
	}

	public Boolean getAlertOnUserAttributeCodes()
	{
		return alertOnUserAttributeCodes;
	}

	public void setAlertOnUserAttributeCodes(Boolean alertOnUserAttributeCodes)
	{
		this.alertOnUserAttributeCodes = alertOnUserAttributeCodes;
	}

}
