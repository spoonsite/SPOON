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
import edu.usu.sdl.apiclient.rest.resource.OrganizationClient;
import edu.usu.sdl.openstorefront.common.exception.AttachedReferencesException;
import edu.usu.sdl.openstorefront.core.entity.Organization;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class OrganizationProvider
{
	OrganizationClient client;
	List<String> organizationIds;

	public OrganizationProvider(ClientAPI apiClient)
	{
		client = new OrganizationClient(apiClient);
		organizationIds = new ArrayList<>();
	}

	public Organization createOrganization(String name)
	{
		Organization organization = new Organization();
		organization.setName(name);
		organization = client.createOrganization(organization);

		organizationIds.add(organization.getOrganizationId());

		return organization;
	}

	public void cleanup() throws AttachedReferencesException
	{
		for (String id : organizationIds) {
			
			client.deleteOrganization(id);
		}
	}
}
