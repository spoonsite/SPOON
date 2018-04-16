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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.Contact;
import edu.usu.sdl.openstorefront.core.entity.ContactType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentContactView
		extends StandardEntityView
{

	private String componentContactId;
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

	public static ComponentContactView toView(ComponentContact contact)
	{
		ComponentContactView view = new ComponentContactView();
		Contact contactFull = contact.fullContact();

		view.setComponentContactId(contact.getComponentContactId());
		view.setEmail(contactFull.getEmail());
		view.setPositionDescription(TranslateUtil.translate(ContactType.class, contact.getContactType()));
		view.setContactType(contact.getContactType());
		view.setOrganization(contactFull.getOrganization());
		view.setPhone(contactFull.getPhone());
		view.setUpdateDts(contactFull.getUpdateDts());
		view.setFirstName(contactFull.getFirstName());
		view.setLastName(contactFull.getLastName());
		view.setActiveStatus(contact.getActiveStatus());
		view.setContactId(contactFull.getContactId());
		view.toStandardView(contact);

		if (contactFull.getLastName() == null || "".equals(contactFull.getLastName())) {
			view.setName(contactFull.getFirstName());
		} else {
			view.setName(contactFull.getFirstName() + " " + contactFull.getLastName());
		}
		return view;
	}

	public static List<ComponentContactView> toViewList(List<ComponentContact> contacts)
	{
		List<ComponentContactView> views = new ArrayList<>();
		contacts.forEach(contact -> {
			views.add(toView(contact));
		});

		return views;
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

	public Date getUpdateDts()
	{
		return updateDts;
	}

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

	public String getComponentContactId()
	{
		return componentContactId;
	}

	public void setComponentContactId(String componentContactId)
	{
		this.componentContactId = componentContactId;
	}

}
