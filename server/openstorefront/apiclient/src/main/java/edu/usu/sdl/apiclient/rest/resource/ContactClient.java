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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.APIResponse;
import edu.usu.sdl.apiclient.AbstractService;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.openstorefront.core.entity.Contact;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import java.util.List;
import javax.ws.rs.core.Response;

/**
 *
 * @author ccummings
 */
public class ContactClient extends AbstractService
{
	String basePath = "api/v1/resource/contacts";

	public ContactClient(ClientAPI client)
	{
		super(client);
	}
	
	public ContactClient()
	{
		this(new ClientAPI(new ObjectMapper()));
	}

	public Response activateContact(String contactId, boolean includeReferences)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Contact createContact(Contact contact)
	{
		APIResponse response = client.httpPost(basePath, contact, null);
		return response.getResponse(Contact.class);
	}

	public void deleteContact(String contactId)
	{
		client.httpDelete(basePath + "/" + contactId, null);
	}

	public List<Contact> getAllContacts()
	{
		APIResponse response = client.httpGet(basePath + "/filtered", null);
		return response.getList(new TypeReference<List<Contact>>(){});
	}

	public Response getContact(String contactId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getContactReferences(String contactId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response getContacts(FilterQueryParams filterQueryParams)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response inactivatesContact(String contactId, boolean includeReferences)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response mergeContacts(String targetContactId, String mergeContactId)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Response updateContact(String contactId, Contact contact)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
