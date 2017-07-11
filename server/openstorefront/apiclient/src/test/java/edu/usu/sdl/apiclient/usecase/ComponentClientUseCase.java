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
package edu.usu.sdl.apiclient.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.apiclient.ComponentService;
import edu.usu.sdl.apiclient.LoginModel;
import edu.usu.sdl.apiclient.view.ComponentResourceView;
import edu.usu.sdl.apiclient.view.LookupModel;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class ComponentClientUseCase
{

	protected LoginModel loginModel = new LoginModel();
	protected ClientAPI client = new ClientAPI(new ObjectMapper());

	@Before
	public void init()
	{
		//production uses IDToken1
		loginModel.setUsernameField("username");
		loginModel.setUsername("user");

		//production uses IDToken2
		loginModel.setPasswordField("password");
		loginModel.setPassword("user");

		loginModel.setSecurityUrl("http://localhost:8080/openstorefront/Login.action?Login");
		loginModel.setServerUrl("http://localhost:8080/openstorefront/");
		loginModel.setLogoffUrl("http://localhost:8080/openstorefront/Login.action?Logout");

		//prod
		//loginModel.setSecurityUrl("https://idam.di2e.net/openam/UI/Login?goto=https%3A%2F%2Fstorefront.di2e.net%3A443%2Fopenstorefront%2FLogin.action)";
		//loginModel.setServerUrl("https://storefront.di2e.net/openstorefront/");
		//loginModel.setLogoffUrl("https://idam.di2e.net/openam/UI/Logout/");
	}

	@Test
	public void testResourceLookup()
	{
		client.connect("user", "Secret1@", "http://localhost:8080/openstorefront/");
		ComponentService componentService = new ComponentService(client);
		List<LookupModel> components = componentService.getComponentLookupList();
		if (!components.isEmpty()) {
			List<ComponentResourceView> resources = componentService.getComponentResources(components.get(5).getCode());
			resources.forEach(resource -> {
				System.out.println(resource.getResourceTypeDesc() + " - " + resource.getLink());
			});
		}
	}

	@Test
	public void testAllResourceLookup()
	{
		client.connect("user", "Secret1@", "http://localhost:8080/openstorefront/");
		ComponentService componentService = new ComponentService(client);
		List<ComponentResourceView> resources = componentService.getComponentAllResources();
		resources.forEach(resource -> {
			System.out.println(resource.getResourceTypeDesc() + " - " + resource.getLink());
		});
	}

}
