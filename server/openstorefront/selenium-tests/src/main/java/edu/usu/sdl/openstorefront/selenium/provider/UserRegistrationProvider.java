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

import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.apiclient.rest.resource.UserClient;
import edu.usu.sdl.apiclient.rest.resource.UserRegistrationClient;
import edu.usu.sdl.openstorefront.core.entity.UserRegistration;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class UserRegistrationProvider
{
	UserRegistrationClient registrationClient;
	UserClient userClient;
	List<String> accountSignupIds;
	
	public UserRegistrationProvider(ClientAPI apiClient)
	{
		registrationClient = new UserRegistrationClient(apiClient);
		userClient = new UserClient(apiClient);
		accountSignupIds = new ArrayList<>();
	}
	
	public UserRegistration getUserRegistration(String registrationId)
	{
		return registrationClient.getUserRegistration(registrationId);
	}
	
	// Deletes only the user registration
	public void deleteUserRegistration(String registrationId)
	{
		registrationClient.deleteUserRegistration(registrationId);
	}
	
	// Deletes the entire user (account, registration, etc)
	public void deleteUser(String registrationId)
	{
		userClient.deleteUser(registrationId);
	}
	
	public void registerUser(String username)
	{
		accountSignupIds.add(username);
	}

	public void cleanup()
	{
		for (String id : accountSignupIds) {
			
			userClient.deleteUser(id);
		}
	}
}
