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
import java.util.Stack;

/**
 *
 * @author ccummings
 */
public class APIClient
{

	private Stack<BaseTestClient> testClients = new Stack<BaseTestClient>();
	private ClientAPI client;
	private ContactTestClient apiContactClient;
	private AttributeTestClient apiAttributeClient;
	private ApplicationTestClient apiApplicationClient;
	private ComponentTypeTestClient apiComponentTypeClient;
	private HighlightTestClient apiHighlightClient;
	private UserSavedSearchTestClient apiUserSavedSearchClient;
	private SystemSearchTestClient apiSystemSearchClient;
	private ComponentRESTTestClient apiComponentRESTClient;
	private UserRegistrationTestClient apiUserRegistrationClient;
	private OrganizationTestClient apiOrganizationClient;

	public APIClient()
	{
		client = new ClientAPI(new ObjectMapper());
		String server = PropertiesUtil.getProperties().getProperty("test.server", "http://localhost:8080/openstorefront/");
		String username = PropertiesUtil.getProperties().getProperty("test.username");
		String password = PropertiesUtil.getProperties().getProperty("test.password");
		client.connect(username, password, server);
	}
	
	public ApplicationTestClient getApplicationTestClient()
	{
		if (apiApplicationClient == null) {
			apiApplicationClient = new ApplicationTestClient(client, this);
			testClients.push(apiApplicationClient);
		}
		
		return apiApplicationClient;
	}
	
	public ComponentRESTTestClient getComponentRESTTestClient() 
	{
		if (apiComponentRESTClient == null) {
			apiComponentRESTClient = new ComponentRESTTestClient(client, this);
			testClients.push(apiComponentRESTClient);
		}
		return apiComponentRESTClient;
	}
	
	public SystemSearchTestClient getSystemSearchTestClient()
	{
		if (apiSystemSearchClient == null) {
			apiSystemSearchClient = new SystemSearchTestClient(client, this);
			testClients.push(apiSystemSearchClient);
		}
		return apiSystemSearchClient;
	}
	
	public UserSavedSearchTestClient getUserSavedSearchClient()
	{
		if (apiUserSavedSearchClient == null) {
			apiUserSavedSearchClient = new UserSavedSearchTestClient(client, this);
			testClients.push(apiUserSavedSearchClient);
		}
		return apiUserSavedSearchClient;
	}
	
	public HighlightTestClient getHighlightTestClient()
	{
		if (apiHighlightClient == null) {
			apiHighlightClient = new HighlightTestClient(client, this);
			testClients.push(apiHighlightClient);
		}
		return apiHighlightClient;
	}

	public ContactTestClient getContactTestClient()
	{
		if (apiContactClient == null) {
			apiContactClient = new ContactTestClient(client, this);
			testClients.push(apiContactClient);
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
			testClients.push(apiComponentTypeClient);
		}
		return apiComponentTypeClient;
	}
	
	public UserRegistrationTestClient getUserRegistrationTestClient()
	{
		if (apiUserRegistrationClient == null) {
			apiUserRegistrationClient = new UserRegistrationTestClient(client, this);
			testClients.push(apiUserRegistrationClient);
		}
		return apiUserRegistrationClient;
	}
	
	public OrganizationTestClient getOrganizationTestClient()
	{
		if (apiOrganizationClient == null) {
			apiOrganizationClient = new OrganizationTestClient(client, this);
			testClients.push(apiOrganizationClient);
		}
		return apiOrganizationClient;
	}
	
	public void cleanup()
	{
		for (BaseTestClient client : testClients) {
			System.out.println("##### In BaseTestClient Cleanup");
			client.cleanup();
		}
		
		client.disconnect();
	}
}
