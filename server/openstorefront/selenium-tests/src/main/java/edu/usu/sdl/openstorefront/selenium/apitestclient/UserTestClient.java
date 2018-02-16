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
package edu.usu.sdl.openstorefront.selenium.apitestclient;

import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.apiclient.rest.resource.UserClient;

/**
 *
 * @author rfrazier
 */
public class UserTestClient
		extends BaseTestClient
{

	private UserClient apiUserClient;
	
	public UserTestClient(ClientAPI client, APIClient apiClient)
	{
		super(client, apiClient);
		apiUserClient = new UserClient(client);
	}

	
	public void deleteAPIUser(String username) {
		apiUserClient.deleteUser(username);
	}
	
	
	@Override
	public void cleanup()
	{
		
	}
}
