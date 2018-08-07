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
package edu.usu.sdl.openstorefront.web.rest.resource;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.Contact;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.model.ContactReference;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.view.ContactViewWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sourceforge.stripes.util.bean.BeanUtil;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/contacts")
@APIDescription("Provides access to contacts")
public class ContactResource
		extends BaseResource
{

	@GET
	@APIDescription("Gets distinct contacts")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Contact.class)
	@Path("/filtered")
	public List<Contact> getAllContacts()
	{
		Contact contact = new Contact();
		contact.setActiveStatus(Contact.ACTIVE_STATUS);
		List<Contact> contacts = contact.findByExample();

		contacts.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, Contact.FIELD_FIRSTNAME));

		return contacts;
	}

	@GET
	@APIDescription("Gets contact records")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ContactViewWrapper.class)
	public Response getContacts(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		Contact contactExample = new Contact();
		contactExample.setActiveStatus(filterQueryParams.getStatus());

		Contact contactStartExample = new Contact();
		contactStartExample.setUpdateDts(filterQueryParams.getStart());

		Contact contactEndExample = new Contact();
		contactEndExample.setUpdateDts(filterQueryParams.getEnd());

		QueryByExample queryByExample = new QueryByExample(contactExample);

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(contactStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(contactEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());
		queryByExample.setSortDirection(filterQueryParams.getSortOrder());

		Contact contactSortExample = new Contact();
		Field sortField = ReflectionUtil.getField(contactSortExample, filterQueryParams.getSortField());
		if (sortField != null) {
			BeanUtil.setPropertyValue(sortField.getName(), contactSortExample, QueryByExample.getFlagForType(sortField.getType()));
			queryByExample.setOrderBy(contactSortExample);
		}

		List<Contact> contacts = service.getPersistenceService().queryByExample(queryByExample);

		ContactViewWrapper contactViewWrapper = new ContactViewWrapper();
		contactViewWrapper.getData().addAll(contacts);
		contactViewWrapper.setTotalNumber(service.getPersistenceService().countByExample(queryByExample));

		return sendSingleEntityResponse(contactViewWrapper);
	}

	@GET
	@APIDescription("Gets a contact record")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Contact.class)
	@Path("/{contactId}")
	public Response getContact(
			@PathParam("contactId") String contactId
	)
	{
		Contact contact = new Contact();
		contact.setContactId(contactId);
		return sendSingleEntityResponse(contact.find());
	}

	@GET
	@APIDescription("Gets contact references")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ContactReference.class)
	@Path("/{contactId}/references")
	public Response getContactReferences(
			@PathParam("contactId") String contactId
	)
	{
		Contact contact = new Contact();
		contact.setContactId(contactId);
		contact = contact.find();
		if (contact != null) {
			List<ContactReference> references = service.getContactService().findReferences(contactId);
			GenericEntity<List<ContactReference>> entity = new GenericEntity<List<ContactReference>>(references)
			{
			};
			return sendSingleEntityResponse(entity);
		}
		return sendSingleEntityResponse(null);
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_CONTACT_MANAGEMENT_CREATE)
	@APIDescription("Creates a contact")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Contact.class)
	public Response createContact(Contact contact)
	{
		return handleSaveContact(contact, true);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_CONTACT_MANAGEMENT_UPDATE)
	@APIDescription("Updates a contact")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Contact.class)
	@Path("/{contactId}")
	public Response updateContact(
			@PathParam("contactId") String contactId,
			Contact contact)
	{
		Contact existing = new Contact();
		existing.setContactId(contactId);
		existing = existing.find();
		if (existing == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		contact.setContactId(contactId);
		return handleSaveContact(contact, false);
	}

	private Response handleSaveContact(Contact contact, boolean post)
	{
		ValidationResult validationResult = contact.validate(true);
		if (validationResult.valid()) {

			contact = service.getContactService().saveContact(contact,false);

			if (post) {
				return Response.created(URI.create("v1/resource/contacts/" + contact.getContactId())).entity(contact).build();
			} else {
				return Response.ok(contact).build();
			}
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_CONTACT_MANAGEMENT_DELETE)
	@APIDescription("Deletes a contact")
	@Path("/{contactId}")
	public void deleteContact(
			@PathParam("contactId") String contactId
	)
	{
		service.getContactService().deleteContact(contactId);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_CONTACT_MANAGEMENT_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Inactivates a contact")
	@DataType(Contact.class)
	@Path("/{contactId}/inactivate")
	public Response inactivatesContact(
			@PathParam("contactId") String contactId,
			@QueryParam("includeReferences") boolean includeReferences
	)
	{
		return handleStatusUpdate(contactId, includeReferences, false);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_CONTACT_MANAGEMENT_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Activates a contact")
	@DataType(Contact.class)
	@Path("/{contactId}/activate")
	public Response activateContact(
			@PathParam("contactId") String contactId,
			@QueryParam("includeReferences") boolean includeReferences
	)
	{
		return handleStatusUpdate(contactId, includeReferences, true);
	}

	private Response handleStatusUpdate(String contactId, boolean includeReferences, boolean activate)
	{
		Contact contact = new Contact();
		contact.setContactId(contactId);
		contact = contact.find();
		if (contact != null) {

			if (activate) {
				service.getContactService().activeContact(contactId, includeReferences);
			} else {
				service.getContactService().inactiveContact(contactId, includeReferences);
			}
		}
		return sendSingleEntityResponse(contact);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_CONTACT_MANAGEMENT_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Merge contacts")
	@DataType(Contact.class)
	@Path("/{targetContactId}/merge/{mergeContactId}")
	public Response mergeContacts(
			@PathParam("targetContactId") String targetContactId,
			@PathParam("mergeContactId") String mergeContactId
	)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		Contact targetContact = service.getPersistenceService().findById(Contact.class, targetContactId);
		Contact mergeContact = service.getPersistenceService().findById(Contact.class, mergeContactId);

		if (targetContact != null && mergeContact != null) {
			service.getContactService().mergeContacts(targetContactId, mergeContactId);
			response = Response.ok(targetContact).build();
		}

		return response;
	}

}
