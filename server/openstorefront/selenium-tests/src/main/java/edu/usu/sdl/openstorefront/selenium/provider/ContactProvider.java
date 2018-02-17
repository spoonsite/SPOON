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
import edu.usu.sdl.apiclient.rest.resource.ContactClient;
import edu.usu.sdl.openstorefront.core.entity.Contact;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class ContactProvider
{
	ContactClient client;
	List<String> contactIds;
	
	public ContactProvider(ClientAPI apiClient)
	{
		client = new ContactClient(apiClient);
		contactIds = new ArrayList<>();
	}
	
	public Contact createAPIContact(String firstName, String lastName, String email, String organization)
	{
		Contact contact = new Contact();
		contact.setFirstName(firstName);
		contact.setLastName(lastName);
		contact.setEmail(email);
		contact.setOrganization(organization);

		Contact contactAPI = client.createContact(contact);
		
		contactIds.add(contactAPI.getContactId());

		return contactAPI;
	}
	
	public Contact getContactByName(String firstName, String lastName)
	{
		List<Contact> contacts = client.getAllContacts();
		
		for (Contact contact : contacts) {
			
			if (contact.getFirstName().equals(firstName) && contact.getLastName().equals(lastName))
			{
				return contact;
			}
		}
		
		return null;
	}
	
	public void registerContact(String contactId)
	{
		contactIds.add(contactId);
	}
	
	public void cleanup()
	{
		for (String id : contactIds) {
			
			client.deleteContact(id);
		}
	}
}
