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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.api.ContactService;
import edu.usu.sdl.openstorefront.core.entity.AlertType;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.Contact;
import edu.usu.sdl.openstorefront.core.model.AlertContext;
import edu.usu.sdl.openstorefront.core.model.ContactReference;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;

/**
 * Handles contacts
 *
 * @author dshurtleff
 */
public class ContactServiceImpl
		extends ServiceProxy
		implements ContactService
{

	@Override
	public Contact findContact(String contactId)
	{
		Contact contact = null;

		Element element = OSFCacheManager.getContactCache().get(contactId);
		if (element != null) {
			contact = (Contact) element.getObjectValue();
		} else {
			contact = getPersistenceService().findById(Contact.class, contactId);

			if (contact != null) {
				element = new Element(contactId, contact);
				OSFCacheManager.getContactCache().put(element);
			}
		}

		return contact;
	}

	@Override
	public Contact saveContact(Contact contact, boolean mergeSimilar)
	{
		Objects.requireNonNull(contact, "Contact required");

		Contact existing = getPersistenceService().findById(Contact.class, contact.getContactId());
		if (existing == null && mergeSimilar) {

			existing = new Contact();
			existing.setEmail(contact.getEmail());
			existing = existing.findProxy();

			if (existing == null) {
				existing = new Contact();
				existing.setFirstName(contact.getFirstName());
				existing.setLastName(contact.getLastName());
				existing = existing.findProxy();
			}
		}

		// Check For Existing Contact
		if (existing != null) {

			// Check If Contact Actually Changed
			if (!existing.findChanges(contact).isEmpty()) {

				// Configure Alert Notifying Of Changes
				AlertContext alertContext = new AlertContext();
				alertContext.setAlertType(AlertType.USER_DATA);
				alertContext.setDataTrigger(existing);
				getAlertService().checkAlert(alertContext);
			}

			// Update Existing Contact With New Values
			existing.updateFields(contact);

			// Store Changes & Return New Version
			contact = getPersistenceService().persist(existing);

		} else {
			contact.setContactId(getPersistenceService().generateId());
			contact.populateBaseCreateFields();
			contact = getPersistenceService().persist(contact);
		}
		OSFCacheManager.getContactCache().remove(contact.getContactId());

		return contact;
	}

	@Override
	public Contact saveContact(Contact contact)
	{
		return saveContact(contact, true);
	}

	@Override
	public void deleteContact(String contactId)
	{
		Contact contact = getPersistenceService().findById(Contact.class, contactId);
		if (contact != null) {
			List<ComponentContact> references = getComponentContacts(contactId);
			if (references.isEmpty()) {
				getPersistenceService().delete(contact);
			} else {
				throw new OpenStorefrontRuntimeException("Unable to delete contact; references attached. Contact: " + contact.getFirstName() + " " + contact.getLastName(), "Remove reference and try again.");
			}
			OSFCacheManager.getContactCache().remove(contactId);
		}
	}

	@Override
	public void inactiveContact(String contactId, boolean cascadeComponents)
	{
		handleStatusUpdate(contactId, cascadeComponents, Contact.INACTIVE_STATUS);
	}

	@Override
	public void activeContact(String contactId, boolean cascadeComponents)
	{
		handleStatusUpdate(contactId, cascadeComponents, Contact.ACTIVE_STATUS);
	}

	private void handleStatusUpdate(String contactId, boolean cascadeComponents, String activeStatus)
	{
		Contact contact = getPersistenceService().findById(Contact.class, contactId);
		if (contact != null) {
			contact.setActiveStatus(activeStatus);
			contact.populateBaseUpdateFields();
			getPersistenceService().persist(contact);

			if (cascadeComponents) {
				List<ComponentContact> referencedContacts = getComponentContacts(contactId);
				for (ComponentContact refContact : referencedContacts) {
					if (Contact.ACTIVE_STATUS.equals(activeStatus)) {
						getComponentService().activateBaseComponent(ComponentContact.class, refContact.getComponentContactId());
					} else {
						getComponentService().deactivateBaseComponent(ComponentContact.class, refContact.getComponentContactId());
					}
				}
			}
			OSFCacheManager.getContactCache().remove(contactId);

		} else {
			throw new OpenStorefrontRuntimeException("Unable to find contact", "Check input; id not set or doesn't exist. Id: " + contactId);
		}
	}

	@Override
	public void mergeContacts(String targetContactId, String mergeContactId)
	{
		Objects.requireNonNull(targetContactId);
		Objects.requireNonNull(mergeContactId);

		Contact target = getPersistenceService().findById(Contact.class, targetContactId);
		Contact merge = getPersistenceService().findById(Contact.class, mergeContactId);
		if (target != null) {
			if (merge != null) {
				if (target.getContactId().equals(merge.getContactId()) == false) {

					ComponentContact componentContact = new ComponentContact();
					componentContact.setContactId(mergeContactId);
					List<ComponentContact> componentContacts = componentContact.findByExampleProxy();
					for (ComponentContact contact : componentContacts) {
						contact.setContactId(targetContactId);
						contact.populateBaseUpdateFields();
						getPersistenceService().persist(contact);
					}

					getPersistenceService().delete(merge);

					OSFCacheManager.getContactCache().remove(targetContactId);
					OSFCacheManager.getContactCache().remove(mergeContactId);
				} else {
					throw new OpenStorefrontRuntimeException("Target and Merge Contact are the same. Unable to merge contact to itself.", "Check data. Merge Id: " + mergeContactId);
				}
			} else {
				throw new OpenStorefrontRuntimeException("Unable to find merge Contact", "Check data. Id: " + mergeContactId);
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find target Contact", "Check data. Id: " + targetContactId);
		}
	}

	@Override
	public List<ContactReference> findReferences(String contactId)
	{
		List<ContactReference> references = new ArrayList<>();
		ComponentContact componentContact = new ComponentContact();
		componentContact.setContactId(contactId);

		List<ComponentContact> referencedContacts = componentContact.findByExample();
		for (ComponentContact refContact : referencedContacts) {
			ContactReference reference = new ContactReference();
			reference.setContactId(refContact.getContactId());
			reference.setComponentId(refContact.getComponentId());
			reference.setComponentName(getComponentService().getComponentName(refContact.getComponentId()));
			references.add(reference);
		}

		return references;
	}

	private List<ComponentContact> getComponentContacts(String contactId)
	{
		ComponentContact componentContact = new ComponentContact();
		componentContact.setContactId(contactId);
		return componentContact.findByExample();
	}

	@Override
	public void convertContacts()
	{
		ComponentContact contactExample = new ComponentContact();
		List<ComponentContact> contacts = contactExample.findByExampleProxy();

		Map<String, List<ComponentContact>> contactMap = contacts.stream()
				.collect(Collectors.groupingBy(ComponentContact::uniqueKey));

		for (String key : contactMap.keySet()) {
			List<ComponentContact> componentContacts = contactMap.get(key);
			ComponentContact componentContact = componentContacts.get(0);

			Contact contact = componentContact.toContact();
			contact.setContactId(null);
			contact.setCreateUser(componentContact.getCreateUser());
			contact = saveContact(contact);
			for (ComponentContact oldContact : componentContacts) {
				if (StringUtils.isBlank(oldContact.getComponentContactId())) {
					oldContact.setComponentContactId(getPersistenceService().generateId());
				}
				oldContact.setContactId(contact.getContactId());
				getPersistenceService().persist(oldContact);
			}
		}
	}

}
