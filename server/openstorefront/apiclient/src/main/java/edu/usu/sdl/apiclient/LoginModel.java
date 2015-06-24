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
package edu.usu.sdl.apiclient;

/**
 *
 * @author dshurtleff
 */
public class LoginModel
{

	private String username;
	private String password;
	private String securityUrl;
	private String logoffUrl;
	private String serverUrl;
	private String usernameField;
	private String passwordField;

	public LoginModel(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	public LoginModel()
	{
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getSecurityUrl()
	{
		return securityUrl;
	}

	public void setSecurityUrl(String securityUrl)
	{
		this.securityUrl = securityUrl;
	}

	public String getServerUrl()
	{
		return serverUrl;
	}

	public void setServerUrl(String serverUrl)
	{
		this.serverUrl = serverUrl;
	}

	public String getUsernameField()
	{
		return usernameField;
	}

	public void setUsernameField(String usernameField)
	{
		this.usernameField = usernameField;
	}

	public String getPasswordField()
	{
		return passwordField;
	}

	public void setPasswordField(String passwordField)
	{
		this.passwordField = passwordField;
	}

	public String getLogoffUrl()
	{
		return logoffUrl;
	}

	public void setLogoffUrl(String logoffUrl)
	{
		this.logoffUrl = logoffUrl;
	}

}
