/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.apiclient.rest.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.APIResponse;
import edu.usu.sdl.apiclient.AbstractService;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.openstorefront.core.entity.UserRegistration;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.UserRegistrationView;
import edu.usu.sdl.openstorefront.core.view.UserRegistrationWrapper;

/**
 *
 * @author ccummings
 */
public class UserRegistrationClient extends AbstractService
{

	String basePath = "api/v1/resource/userregistrations";

	public UserRegistrationClient(ClientAPI client)
	{
		super(client);
	}
	
	public UserRegistrationClient()
	{
		this(new ClientAPI(new ObjectMapper()));
	}
	
	public UserRegistration createUserRegistration(UserRegistration userRegistration, Boolean verifyEmail)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	public UserRegistration createUser(UserRegistration userRegistration)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void deleteUserRegistration(String registrationId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public UserRegistrationWrapper getUserRegistration(FilterQueryParams filterQueryParams)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public UserRegistrationView getUserRegistration(String registrationId)
	{	
		APIResponse response = client.httpGet(basePath + "/" + registrationId, null);
		UserRegistrationView registration = response.getResponse(UserRegistrationView.class);
		return registration;
	}

}
