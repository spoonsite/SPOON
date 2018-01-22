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

import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.apiclient.rest.resource.OrganizationClient;
import edu.usu.sdl.openstorefront.common.exception.AttachedReferencesException;
import edu.usu.sdl.openstorefront.core.entity.Organization;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ccummings
 */
public class OrganizationTestClient
		extends BaseTestClient
{

	private static Set<String> organizationIds = new HashSet<>();
	private OrganizationClient apiOrganization;
	
	public OrganizationTestClient(ClientAPI client, APIClient apiClient)
	{
		super(client, apiClient);
		apiOrganization = new OrganizationClient(client);
	}
	
	public Organization createOrganization(String name)
	{
		Organization organization = new Organization();
		organization.setName(name);
		organization = apiOrganization.createOrganization(organization);
		organizationIds.add(organization.getOrganizationId());
		
		return organization;
	}
	
	public Organization getOrganizationByName(String name)
	{
		Organization organization = apiOrganization.getOrganizationByName(name);
		return organization;
	}
	
	public Organization getOrganizationById(String id)
	{
		Organization organization = apiOrganization.getOrganization(id);
		return organization;
	}
	
	public void deleteOrganization(String id) throws AttachedReferencesException
	{
		apiOrganization.deleteOrganization(id);
	}

	@Override
	public void cleanup()
	{
		for (String id : organizationIds) {
			
			try {
				deleteOrganization(id);
			} catch (AttachedReferencesException ex) {
				Logger.getLogger(OrganizationTestClient.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
	}

}
