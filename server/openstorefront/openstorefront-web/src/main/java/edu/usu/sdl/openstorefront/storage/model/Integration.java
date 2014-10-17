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
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.PK;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
public class Integration
{
	@PK
	@NotNull
	private String integrationId;
	
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CRON)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String refreshRate;
	
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GUID)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String issueNumber;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String status;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GUID)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String errorNumber;
	
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GUID)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String componentId;
	
	
	
	
	public Integration() {
		
	}
	
	/**
	 * @return the refreshRate
	 */
	public String getRefreshRate()
	{
		return refreshRate;
	}

	/**
	 * @param refreshRate the refreshRate to set
	 */
	public void setRefreshRate(String refreshRate)
	{
		this.refreshRate = refreshRate;
	}

	/**
	 * @return the issueNumber
	 */
	public String getIssueNumber()
	{
		return issueNumber;
	}

	/**
	 * @param issueNumber the issueNumber to set
	 */
	public void setIssueNumber(String issueNumber)
	{
		this.issueNumber = issueNumber;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * @return the errorNumber
	 */
	public String getErrorNumber()
	{
		return errorNumber;
	}

	/**
	 * @param errorNumber the errorNumber to set
	 */
	public void setErrorNumber(String errorNumber)
	{
		this.errorNumber = errorNumber;
	}

	/**
	 * @return the integrationId
	 */
	public String getIntegrationId()
	{
		return integrationId;
	}

	/**
	 * @param integrationId the integrationId to set
	 */
	public void setIntegrationId(String integrationId)
	{
		this.integrationId = integrationId;
	}

	/**
	 * @return the componentId
	 */
	public String getComponentId()
	{
		return componentId;
	}

	/**
	 * @param componentId the componentId to set
	 */
	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}
}
