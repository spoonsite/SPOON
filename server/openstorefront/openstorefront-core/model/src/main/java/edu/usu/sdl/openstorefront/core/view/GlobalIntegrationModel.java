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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
public class GlobalIntegrationModel
{

	//10 am server time (UTC) every day
	public static final String DEFAULT_REFRESH_RATE = "0 0 10 * * ?";

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CRON)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String jiraRefreshRate;

	private String cronExpressionDescription;

	public String getJiraRefreshRate()
	{
		return jiraRefreshRate;
	}

	public void setJiraRefreshRate(String jiraRefreshRate)
	{
		this.jiraRefreshRate = jiraRefreshRate;
	}

	public String getCronExpressionDescription()
	{
		return cronExpressionDescription;
	}

	public void setCronExpressionDescription(String cronExpressionDescription)
	{
		this.cronExpressionDescription = cronExpressionDescription;
	}

}
