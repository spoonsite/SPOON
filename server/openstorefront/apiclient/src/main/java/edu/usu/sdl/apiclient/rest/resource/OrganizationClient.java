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

import edu.usu.sdl.apiclient.APIResponse;
import edu.usu.sdl.apiclient.AbstractService;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.openstorefront.common.exception.AttachedReferencesException;
import edu.usu.sdl.openstorefront.core.entity.Organization;
import edu.usu.sdl.openstorefront.core.model.OrgReference;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import java.util.List;
import javax.ws.rs.core.Response;

/**
 *
 * @author ccummings
 */
public class OrganizationClient
		extends AbstractService
{
	String basePath = "api/v1/resource/organizations";

	public OrganizationClient(ClientAPI client)
	{
		super(client);
	}

	public Organization createOrganization(Organization organization)
	{
		APIResponse response = client.httpPost(basePath, organization, null);
		return response.getResponse(Organization.class);
	}

	public void deleteOrganization(String organizationid) throws AttachedReferencesException
	{
		client.httpDelete(basePath + "/" + organizationid, null);
	}

	public Response extractFromData()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getEntryOrganizationRelations(String organizationId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getLookupList(boolean approvedComponentsOnly)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Organization getOrganization(String organizationId)
	{
		APIResponse response = client.httpGet(basePath + "/" + organizationId, null);
		Organization organization = response.getResponse(Organization.class);
		return organization;
	}

	public Organization getOrganizationByName(String name)
	{
		APIResponse response = client.httpGet(basePath + "/name/" + name, null);
		Organization organization = response.getResponse(Organization.class);
		return organization;
	}

	public Response getOrganizations(FilterQueryParams filterQueryParams)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<OrgReference> getReferences(String organizationId, boolean activeOnly, boolean approvedOnly)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<OrgReference> getReferencesByName(boolean activeOnly, boolean approvedOnly, String name)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public List<OrgReference> getReferencesNoOrg(boolean activeOnly, boolean approvedOnly)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Organization merge(String targetId, String mergeId)
	{
		APIResponse response = client.httpPost(basePath + "/" + targetId + "/merge/" + mergeId, null, null);
		return response.getResponse(Organization.class);
	}

	public Response updateOrganization(String organizationId, Organization organization)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
