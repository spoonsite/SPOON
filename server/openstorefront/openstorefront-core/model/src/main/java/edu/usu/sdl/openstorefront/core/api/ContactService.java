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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.entity.Contact;
import edu.usu.sdl.openstorefront.core.model.ContactReference;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public interface ContactService
	extends AsyncService
{
	/**
	 * This find the contact by id (checking cache first)
	 * @param contactId
	 * @return 
	 */
	public Contact findContact(String contactId);
	
	/**
	 * If mergeSimilar is true this will update existing contact or try to find one base on key and update that.
	 * If not if will add a contact
	 * 
	 * @param contact
	 * @return ContactService saved
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public Contact saveContact(Contact contact,boolean mergeSimilar);

	/**
	 * This will update existing contact or try to find one base on key and update that.
	 * If not if will add a contact
	 * 
	 * @param contact
	 * @return ContactService saved
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public Contact saveContact(Contact contact);	
	
	/**
	 * This attempt to delete a contact (hard delete)
	 * Can only delete contact that are not being referenced
	 * 
	 * @param contactId 
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteContact(String contactId);
	
	/**
	 * Inactivate a contact; optional inactivating contact associated with Entries
	 * @param contactId
	 * @param cascadeComponents 
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void inactiveContact(String contactId, boolean cascadeComponents);
	
	/**
	 * Inactivate a contact; optional activating contact associated with Entries
	 * @param contactId
	 * @param cascadeComponents 
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void activeContact(String contactId, boolean cascadeComponents);	
	
	/**
	 * Merges the references from one contact to the other and then removes
	 * the merge contact.  This doesn't not change the target contact data.
	 * @param targetContatId
	 * @param mergeContactId 
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void mergeContacts(String targetContactId, String mergeContactId);	
	
	/**
	 * Finds the component references
	 * @param contactId
	 * @return references found
	 */
	public List<ContactReference> findReferences(String contactId);
	
	/**
	 * This will pull all old component contacts
	 * 1. de-dup 
	 * 2. extract the contacts to the contact entity.
	 * 
	 */
	public void convertContacts();			
		
}
