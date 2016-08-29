/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.test.contact;

import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.Contact;
import edu.usu.sdl.openstorefront.core.entity.ContactType;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.ContactReference;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class ContactServiceTest extends BaseTestCase
{
	private Contact existContact = null;
	private Contact newContact = null;

	@Override
	protected void runInternalTest()
	{
		existContact = new Contact();
		existContact.setFirstName("ExistingFirstName");
		existContact.setLastName("ExistingLastName");
		existContact.setOrganization("ExistingOrganization");
		String existContactEmail = getSystemEmail();
		existContact.setEmail(existContactEmail);
		existContact = service.getContactService().saveContact(existContact);

		ComponentAll componentAll = getTestComponent();
		ComponentContact existContactComp = new ComponentContact();
		existContactComp.setContactId(existContact.getContactId());
		existContactComp.setFirstName(existContact.getFirstName());
		existContactComp.setLastName(existContact.getLastName());
		existContactComp.setEmail(existContact.getEmail());
		existContactComp.setOrganization(existContact.getEmail());
		existContactComp.setContactType(ContactType.SUBMITTER);
		componentAll.getContacts().add(existContactComp);
		service.getComponentService().saveFullComponent(componentAll);

		// Inactivate Contact
		service.getContactService().inactiveContact(existContact.getContactId(), true);
		componentAll = service.getComponentService().getFullComponent(componentAll.getComponent().getComponentId());
		List<ComponentContact> existingContactsList = componentAll.getContacts();

		if (existingContactsList.isEmpty()) {
			results.append("Inactivate contact test:  Passed<br><br>");
		} else {
			failureReason.append("Contact failed to inactivate<br><br>");
		}

		// Activate Contact
		service.getContactService().activeContact(existContact.getContactId(), true);
		componentAll = service.getComponentService().getFullComponent(componentAll.getComponent().getComponentId());
		existingContactsList = componentAll.getContacts();

		if (!existingContactsList.isEmpty()) {
			results.append("Activate contact test:  Passed").append("<br><br>");
		} else {
			failureReason.append("Failed to activate contact<br><br>");
		}

		// Create 2nd ComponentContact and Contact
		newContact = new Contact();
		newContact.setFirstName("newFirstName");
		newContact.setLastName("newLastName");
		newContact.setOrganization("newOrganization");
		newContact.setEmail("test.contact@test.com");
		newContact = service.getContactService().saveContact(newContact);

		ComponentContact newContactComp = new ComponentContact();
		newContactComp.setContactId(newContact.getContactId());
		newContactComp.setFirstName(newContact.getFirstName());
		newContactComp.setLastName(newContact.getLastName());
		newContactComp.setEmail(newContact.getEmail());
		newContactComp.setOrganization(newContact.getEmail());
		newContactComp.setContactType(ContactType.SUBMITTER);
		componentAll.getContacts().add(newContactComp);
		service.getComponentService().saveFullComponent(componentAll);

		// Merge Contacts
		service.getContactService().mergeContacts(newContact.getContactId(), existContact.getContactId());
		componentAll = service.getComponentService().getFullComponent(componentAll.getComponent().getComponentId());
		List<ComponentContact> mergedContacts = componentAll.getContacts();

		if (mergedContacts.size() > 1) {
			if (mergedContacts.get(0).getContactId().equals(mergedContacts.get(1).getContactId())) {
				results.append("Merge Contacts Test:  Passed").append("<br><br>");
			}
		} else {
			failureReason.append("Merge Contacts Test:  Failed - Unable to merge contacts").append("<br>");
		}

		// find all contact references using newContact's ID
		List<ContactReference> foundContacts = service.getContactService().findReferences(newContact.getContactId());
		if (foundContacts.size() == 2) {
			results.append("Found references test:  Passed<br><br>");
		} else {
			failureReason.append("Found references test:  Failed - No references found with that contactId<br>");
		}
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();
		if (existContact != null) {
			service.getContactService().deleteContact(existContact.getContactId());
		}
		if (newContact != null) {
			service.getContactService().deleteContact(newContact.getContactId());
		}
	}

	@Override
	public String getDescription()
	{
		return "Contact Test";
	}
}
