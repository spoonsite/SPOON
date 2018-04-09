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
@APIDescription("This is part of the file history")
@Embeddable
public class FileHistoryOption
		implements Serializable
{

	@ConsumeField
	private Boolean uploadReviews;

	@ConsumeField
	private Boolean uploadQuestions;

	@ConsumeField
	private Boolean uploadTags;

	@ConsumeField
	private Boolean uploadIntegration;

	@ConsumeField
	private Boolean skipRequiredAttributes;

	@ConsumeField
	private Boolean skipDuplicationCheck;

	@Version
	private String storageVersion;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public FileHistoryOption()
	{
	}

	public Boolean getUploadReviews()
	{
		return uploadReviews;
	}

	public void setUploadReviews(Boolean uploadReviews)
	{
		this.uploadReviews = uploadReviews;
	}

	public Boolean getUploadQuestions()
	{
		return uploadQuestions;
	}

	public void setUploadQuestions(Boolean uploadQuestions)
	{
		this.uploadQuestions = uploadQuestions;
	}

	public Boolean getUploadTags()
	{
		return uploadTags;
	}

	public void setUploadTags(Boolean uploadTags)
	{
		this.uploadTags = uploadTags;
	}

	public Boolean getUploadIntegration()
	{
		return uploadIntegration;
	}

	public void setUploadIntegration(Boolean uploadIntegration)
	{
		this.uploadIntegration = uploadIntegration;
	}

	public String getStorageVersion()
	{
		return storageVersion;
	}

	public void setStorageVersion(String storageVersion)
	{
		this.storageVersion = storageVersion;
	}

	public Boolean getSkipRequiredAttributes()
	{
		return skipRequiredAttributes;
	}

	public void setSkipRequiredAttributes(Boolean skipRequiredAttributes)
	{
		this.skipRequiredAttributes = skipRequiredAttributes;
	}

	public Boolean getSkipDuplicationCheck()
	{
		return skipDuplicationCheck;
	}

	public void setSkipDuplicationCheck(Boolean skipDuplicationCheck)
	{
		this.skipDuplicationCheck = skipDuplicationCheck;
	}

}
