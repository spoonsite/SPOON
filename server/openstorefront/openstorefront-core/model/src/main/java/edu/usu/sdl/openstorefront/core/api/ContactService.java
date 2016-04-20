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
	 * This will update existing contact or try to find one base on key and update that.
	 * If not if will add a contact
	 * 
	 * @param contact
	 * @return ContactService saved
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public Contact saveContact(ContactService contact);	
	
	
	
}
