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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.model.FieldChangeModel;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Holds a link to a contact")
public class ComponentContact
		extends BaseComponent<ComponentContact>
		implements OrganizationModel, LoggableModel<ComponentContact>
{

	@PK(generated = true)
	@NotNull
	private String componentContactId;

	@FK(Contact.class)
	@NotNull
	private String contactId;

	@NotNull
	@ConsumeField
	@ValidValueType(value = {}, lookupClass = ContactType.class)
	@FK(ContactType.class)
	private String contactType;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String firstName;

	@ConsumeField
	@Sanitize(TextSanitizer.class)
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_LASTNAME)
	private String lastName;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_EMAIL)
	@Sanitize(TextSanitizer.class)
	private String email;

	@ConsumeField
	@Sanitize(TextSanitizer.class)
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String phone;

	@NotNull
	@ConsumeField
	@Sanitize(TextSanitizer.class)
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@FK(value = Organization.class, referencedField = "name", softReference = true)
	private String organization;

	public ComponentContact()
	{
	}

	public Contact fullContact()
	{
		Contact contact = ServiceProxyFactory.getServiceProxy().getContactService().findContact(getContactId());
		if (contact != null) {
			return contact;
		} else {
			return toContact();
		}
	}

	public Contact toContact()
	{
		Contact contact = new Contact();
		contact.setContactId(this.getContactId());
		contact.setEmail(this.getEmail());
		contact.setFirstName(this.getFirstName());
		contact.setLastName(this.getLastName());
		contact.setOrganization(this.getOrganization());
		contact.setPhone(this.getPhone());

		return contact;
	}

	@Override
	public String uniqueKey()
	{
		if (StringUtils.isNotBlank(getEmail())) {
			return getEmail();
		} else if (StringUtils.isNotBlank(getPhone())) {
			return getPhone();
		} else {
			return getFirstName() + OpenStorefrontConstant.GENERAL_KEY_SEPARATOR + getLastName();
		}
	}

	@Override
	public void updateFields(StandardEntity entity)
	{
		ComponentContact contact = (ComponentContact) entity;
		ServiceProxyFactory.getServiceProxy().getChangeLogService().findUpdateChanges(this, contact);

		super.updateFields(entity);

		this.setContactId(contact.getContactId());
		this.setContactType(contact.getContactType());
		this.setEmail(contact.getEmail());
		this.setFirstName(contact.getFirstName());
		this.setLastName(contact.getLastName());
		this.setOrganization(contact.getOrganization());
		this.setPhone(contact.getPhone());

	}

	@Override
	public List<FieldChangeModel> findChanges(ComponentContact updated)
	{
		Set<String> excludeFields = excludedChangeFields();
		excludeFields.add("componentContactId");
		excludeFields.add("contactId");

		//covered in contact
		excludeFields.add("firstName");
		excludeFields.add("lastName");
		excludeFields.add("email");
		excludeFields.add("phone");
		excludeFields.add("organization");

		List<FieldChangeModel> changes = FieldChangeModel.allChangedFields(excludeFields, this, updated);
		return changes;
	}

	@Override
	public String addRemoveComment()
	{
		return TranslateUtil.translate(ContactType.class, getContactType()) + " - " + getFirstName() + ", " + getLastName();
	}

	@Override
	protected void customKeyClear()
	{
		setContactId(null);
		setComponentContactId(null);
	}

	public String getContactId()
	{
		return contactId;
	}

	public void setContactId(String contactId)
	{
		this.contactId = contactId;
	}

	public String getContactType()
	{
		return contactType;
	}

	public void setContactType(String contactType)
	{
		this.contactType = contactType;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	@Override
	public String getOrganization()
	{
		return organization;
	}

	@Override
	public void setOrganization(String organization)
	{
		this.organization = organization;
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

	public String getComponentContactId()
	{
		return componentContactId;
	}

	public void setComponentContactId(String componentContactId)
	{
		this.componentContactId = componentContactId;
	}

}
