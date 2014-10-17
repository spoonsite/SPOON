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

package edu.usu.sdl.openstorefront.web.rest.model;

import edu.usu.sdl.openstorefront.doc.ConsumeField;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
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
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_FIRSTNAME)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String username;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GUID)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String password;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CRON)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String refreshRate;

	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
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
}
