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
package edu.usu.sdl.openstorefront.selenium.apitestclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.openstorefront.selenium.util.PropertiesUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class APIClient
{

	private List<BaseTestClient> testClients = new ArrayList<>();
	private ClientAPI client;
	private ContactTestClient apiContactClient;
	private AttributeTestClient apiAttributeClient;
	private ComponentTypeTestClient apiComponentTypeClient;
	private HighlightTestClient apiHighlightClient;
	private UserSavedSearchTestClient apiUserSavedSearchClient;
	private SystemSearchTestClient apiSystemSearchClient;

	public APIClient()
	{
		client = new ClientAPI(new ObjectMapper());
		String server = PropertiesUtil.getProperties().getProperty("test.server", "http://localhost:8080/openstorefront/");
		String username = PropertiesUtil.getProperties().getProperty("test.username");
		String password = PropertiesUtil.getProperties().getProperty("test.password");
		client.connect(username, password, server);
	}
	
	public SystemSearchTestClient getSystemSearchTestClient()
	{
		if (apiSystemSearchClient == null) {
			apiSystemSearchClient = new SystemSearchTestClient(client, this);
			testClients.add(apiSystemSearchClient);
		}
		return apiSystemSearchClient;
	}
	
	public UserSavedSearchTestClient getUserSavedSearchClient()
	{
		if (apiUserSavedSearchClient == null) {
			apiUserSavedSearchClient = new UserSavedSearchTestClient(client, this);
			testClients.add(apiUserSavedSearchClient);
		}
		return apiUserSavedSearchClient;
	}
	
	public HighlightTestClient getHighlightTestClient()
	{
		if (apiHighlightClient == null) {
			apiHighlightClient = new HighlightTestClient(client, this);
			testClients.add(apiHighlightClient);
		}
		return apiHighlightClient;
	}

	public ContactTestClient getContactTestClient()
	{
		if (apiContactClient == null) {
			apiContactClient = new ContactTestClient(client, this);
			testClients.add(apiContactClient);
		}
		return apiContactClient;
	}
	
	public AttributeTestClient getAttributeTestClient()
	{
		if (apiAttributeClient == null) {
			apiAttributeClient = new AttributeTestClient(client, this);
			testClients.add(apiAttributeClient);
		}
		return apiAttributeClient;
	}
	
	public ComponentTypeTestClient getComponentTypeTestClient()
	{
		if (apiComponentTypeClient == null) {
			apiComponentTypeClient = new ComponentTypeTestClient(client, this);
			testClients.add(apiComponentTypeClient);
		}
		return apiComponentTypeClient;
	}
	
	public void cleanup()
	{
		for (BaseTestClient client : testClients) {
			client.cleanup();
		}
		
		client.disconnect();
	}
}
