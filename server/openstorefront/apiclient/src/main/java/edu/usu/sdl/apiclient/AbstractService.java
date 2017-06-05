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

import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public abstract class AbstractService
{

	private static Logger log = Logger.getLogger(AbstractService.class.getName());
	protected ClientAPI client;

	public AbstractService(ClientAPI client)
	{
		this.client = client;
	}

	public void connect(String username, String password, String serverURL) {
		
		LoginModel loginModel = new LoginModel();
		loginModel.setServerUrl(serverURL);
		loginModel.setSecurityUrl(serverURL + "/Login.action?Login");
		loginModel.setLogoffUrl(serverURL + "/Login.action?Logout"); 

		loginModel.setUsernameField("username");
		loginModel.setUsername(username);

		loginModel.setPasswordField("password");
		loginModel.setPassword(password);
		
		client.connect(loginModel);
	}
}
