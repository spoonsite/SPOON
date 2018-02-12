/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.selenium.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.openstorefront.selenium.util.PropertiesUtil;

/**
 *
 * @author ccummings
 */
public class ClientApiProvider
{
	private ClientAPI apiClient;
	
	public ClientApiProvider()
	{
		apiClient = new ClientAPI(new ObjectMapper());
		String server = PropertiesUtil.getProperties().getProperty("test.server", "http://localhost:8080/openstorefront/");
		String username = PropertiesUtil.getProperties().getProperty("test.username");
		String password = PropertiesUtil.getProperties().getProperty("test.password");
		apiClient.connect(username, password, server);
	}
	
	public void clientDisconnect()
	{
		apiClient.disconnect();
	}
	
	public ClientAPI getAPIClient()
	{
		return apiClient;
	}
	
}
