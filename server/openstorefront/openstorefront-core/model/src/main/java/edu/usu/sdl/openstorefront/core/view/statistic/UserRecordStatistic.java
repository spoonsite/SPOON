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
package edu.usu.sdl.openstorefront.core.view.statistic;

import java.util.Date;

/**
 *
 * @author dshurtleff
 */
public class UserRecordStatistic
{

	private String username;
	private Date loginDts;
	private String browser;
	private String browserVersion;

	public UserRecordStatistic()
	{
	}

	public Date getLoginDts()
	{
		return loginDts;
	}

	public void setLoginDts(Date loginDts)
	{
		this.loginDts = loginDts;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getBrowser()
	{
		return browser;
	}

	public void setBrowser(String browser)
	{
		this.browser = browser;
	}

	public String getBrowserVersion()
	{
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion)
	{
		this.browserVersion = browserVersion;
	}

}
