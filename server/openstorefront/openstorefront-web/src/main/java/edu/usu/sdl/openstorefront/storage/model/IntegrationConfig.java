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

import edu.usu.sdl.openstorefront.doc.ConsumeField;
import edu.usu.sdl.openstorefront.doc.ValidValueType;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.PK;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
public class IntegrationConfig
		extends BaseEntity
{

	@PK
	@NotNull
	private String integrationConfigId;

	@NotNull
	@ValidValueType(value = {}, lookupClass = IntegrationType.class)
	@ConsumeField
	private String integrationType;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String issueNumber;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@ConsumeField
	private String projectType;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@ConsumeField
	private String issueType;

	@NotNull
	@ValidValueType(value = {}, lookupClass = RunStatus.class)
	private String status;

	private String errorTicketNumber;
	private String errorMessage;
	private String potentialResolution;
	private Date lastStartTime;
	private Date lastEndTime;

	@NotNull
	@ConsumeField
	private String componentId;

	public IntegrationConfig()
	{

	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getIssueNumber()
	{
		return issueNumber;
	}

	public void setIssueNumber(String issueNumber)
	{
		this.issueNumber = issueNumber;
	}

	public String getIntegrationType()
	{
		return integrationType;
	}

	public void setIntegrationType(String integrationType)
	{
		this.integrationType = integrationType;
	}

	public String getProjectType()
	{
		return projectType;
	}

	public void setProjectType(String projectType)
	{
		this.projectType = projectType;
	}

	public String getIssueType()
	{
		return issueType;
	}

	public void setIssueType(String issueType)
	{
		this.issueType = issueType;
	}

	public String getIntegrationConfigId()
	{
		return integrationConfigId;
	}

	public void setIntegrationConfigId(String integrationConfigId)
	{
		this.integrationConfigId = integrationConfigId;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getErrorTicketNumber()
	{
		return errorTicketNumber;
	}

	public void setErrorTicketNumber(String errorTicketNumber)
	{
		this.errorTicketNumber = errorTicketNumber;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getPotentialResolution()
	{
		return potentialResolution;
	}

	public void setPotentialResolution(String potentialResolution)
	{
		this.potentialResolution = potentialResolution;
	}

	public Date getLastStartTime()
	{
		return lastStartTime;
	}

	public void setLastStartTime(Date lastStartTime)
	{
		this.lastStartTime = lastStartTime;
	}

	public Date getLastEndTime()
	{
		return lastEndTime;
	}

	public void setLastEndTime(Date lastEndTime)
	{
		this.lastEndTime = lastEndTime;
	}

}
