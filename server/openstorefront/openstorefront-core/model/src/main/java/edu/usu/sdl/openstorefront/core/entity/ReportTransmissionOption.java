/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Holds all transmission options; It's de-normalized on purpose resolving
 * subclass is will create issues or complications on REST API and perhaps on
 * Storage due to nesting.
 *
 * @author dshurtleff
 */
@Embeddable
@APIDescription("Hold all transmission options; Note some Transmission type may require some options")
public class ReportTransmissionOption
		implements Serializable
{

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = ReportFormat.class)
	@ConsumeField
	@FK(ReportFormat.class)
	private String reportFormat;

	@ConsumeField
	@Embedded
	@DataType(EmailAddress.class)
	@OneToMany(orphanRemoval = true)
	private List<EmailAddress> emailAddresses;

	@ConsumeField
	private Boolean attachReport;

	@ConsumeField
	private Boolean postToEmailBody;

	@ConsumeField
	private String confluenceSpace;

	@ConsumeField
	private String confluencePage;

	@ConsumeField
	private String confluenceParentPageId;

	@ConsumeField
	private Boolean reportNotify;

	@Version
	private String storageVersion;

	public ReportTransmissionOption()
	{
	}

	public String getReportFormat()
	{
		return reportFormat;
	}

	public void setReportFormat(String reportFormat)
	{
		this.reportFormat = reportFormat;
	}

	public List<EmailAddress> getEmailAddresses()
	{
		return emailAddresses;
	}

	public void setEmailAddresses(List<EmailAddress> emailAddresses)
	{
		this.emailAddresses = emailAddresses;
	}

	public Boolean getAttachReport()
	{
		return attachReport;
	}

	public void setAttachReport(Boolean attachReport)
	{
		this.attachReport = attachReport;
	}

	public Boolean getPostToEmailBody()
	{
		return postToEmailBody;
	}

	public void setPostToEmailBody(Boolean postToEmailBody)
	{
		this.postToEmailBody = postToEmailBody;
	}

	public String getConfluenceSpace()
	{
		return confluenceSpace;
	}

	public void setConfluenceSpace(String confluenceSpace)
	{
		this.confluenceSpace = confluenceSpace;
	}

	public String getConfluencePage()
	{
		return confluencePage;
	}

	public void setConfluencePage(String confluencePage)
	{
		this.confluencePage = confluencePage;
	}

	public String getConfluenceParentPageId()
	{
		return confluenceParentPageId;
	}

	public void setConfluenceParentPageId(String confluenceParentPageId)
	{
		this.confluenceParentPageId = confluenceParentPageId;
	}

	public String getStorageVersion()
	{
		return storageVersion;
	}

	public void setStorageVersion(String storageVersion)
	{
		this.storageVersion = storageVersion;
	}
	
	public Boolean getReportNotify()
	{
		return reportNotify;
	}

	public void setReportNotify(Boolean reportNotify)
	{
		this.reportNotify = reportNotify;
	}

}
