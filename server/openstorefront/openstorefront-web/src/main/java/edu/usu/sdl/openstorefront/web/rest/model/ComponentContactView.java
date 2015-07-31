/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.rest.model;

import edu.usu.sdl.openstorefront.storage.model.ComponentContact;
import edu.usu.sdl.openstorefront.storage.model.ContactType;
import edu.usu.sdl.openstorefront.util.TranslateUtil;
import java.util.Date;

/**
 *
 * @author dshurtleff
 */
public class ComponentContactView
		extends StandardEntityView
{

	private String contactId;
	private String positionDescription;
	private String contactType;
	private String name;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String organization;
	private Date updateDts;
	private String activeStatus;

	public ComponentContactView()
	{
	}

	public static ComponentContactView toView(ComponentContact contact)
	{
		ComponentContactView view = new ComponentContactView();
		view.setEmail(contact.getEmail());
		view.setPositionDescription(TranslateUtil.translate(ContactType.class, contact.getContactType()));
		view.setContactType(contact.getContactType());
		view.setOrganization(contact.getOrganization());
		view.setPhone(contact.getPhone());
		view.setUpdateDts(contact.getUpdateDts());
		view.setFirstName(contact.getFirstName());
		view.setLastName(contact.getLastName());
		view.setActiveStatus(contact.getActiveStatus());
		view.setContactId(contact.getContactId());
		view.toStandardView(contact);

		if (contact.getLastName() == null || "".equals(contact.getLastName())) {
			view.setName(contact.getFirstName());
		} else {
			view.setName(contact.getFirstName() + " " + contact.getLastName());
		}
		return view;
	}

	public String getPositionDescription()
	{
		return positionDescription;
	}

	public void setPositionDescription(String positionDescription)
	{
		this.positionDescription = positionDescription;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	/**
	 * @return the updateDts
	 */
	public Date getUpdateDts()
	{
		return updateDts;
	}

	/**
	 * @param updateDts the updateDts to set
	 */
	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getContactType()
	{
		return contactType;
	}

	public void setContactType(String contactType)
	{
		this.contactType = contactType;
	}

	public String getActiveStatus()
	{
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public String getContactId()
	{
		return contactId;
	}

	public void setContactId(String contactId)
	{
		this.contactId = contactId;
	}

}
