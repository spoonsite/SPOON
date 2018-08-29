/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.web.rest.resource.component;

import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.ComponentRelationship;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.sort.SortUtil;
import edu.usu.sdl.openstorefront.core.view.ComponentAttributeView;
import edu.usu.sdl.openstorefront.core.view.ComponentContactView;
import edu.usu.sdl.openstorefront.core.view.ComponentMediaView;
import edu.usu.sdl.openstorefront.core.view.ComponentRelationshipView;
import edu.usu.sdl.openstorefront.core.view.ComponentResourceView;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.RestErrorModel;
import edu.usu.sdl.openstorefront.core.view.TagView;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
import jersey.repackaged.com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
public abstract class ComponentCommonSubResourceExt
		extends GeneralComponentResourceExt
{

	// <editor-fold defaultstate="collapsed"  desc="ComponentRESTResource ATTRIBUTE Section">
	@GET
	@APIDescription("Gets attributes for a component")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentAttribute.class)
	@Path("/{id}/attributes")
	public Response getComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentAttribute componentAttributeExample = new ComponentAttribute();
		componentAttributeExample.setActiveStatus(filterQueryParams.getStatus());
		componentAttributeExample.setComponentId(componentId);
		List<ComponentAttribute> componentAttributes = service.getPersistenceService().queryByExample(componentAttributeExample);
		componentAttributes = filterQueryParams.filter(componentAttributes);

		GenericEntity<List<ComponentAttribute>> entity = new GenericEntity<List<ComponentAttribute>>(componentAttributes)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets attributes for a component")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentAttributeView.class)
	@Path("/{id}/attributes/view")
	public Response getComponentAttributeView(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentAttribute componentAttributeExample = new ComponentAttribute();
		componentAttributeExample.setActiveStatus(filterQueryParams.getStatus());
		componentAttributeExample.setComponentId(componentId);
		List<ComponentAttribute> componentAttributes = service.getPersistenceService().queryByExample(componentAttributeExample);
		componentAttributes = filterQueryParams.filter(componentAttributes);
		List<ComponentAttributeView> componentAttributeViews = ComponentAttributeView.toViewList(componentAttributes);

		GenericEntity<List<ComponentAttributeView>> entity = new GenericEntity<List<ComponentAttributeView>>(componentAttributeViews)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets attributes for component and a given type")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentAttribute.class)
	@Path("/{id}/attributes/{attributeType}")
	public List<ComponentAttribute> getComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("attributeType")
			@RequiredParam String attributeType)
	{
		ComponentAttribute componentAttributeExample = new ComponentAttribute();
		componentAttributeExample.setComponentId(componentId);
		ComponentAttributePk componentAttributePk = new ComponentAttributePk();
		componentAttributePk.setAttributeType(attributeType);
		componentAttributeExample.setComponentAttributePk(componentAttributePk);
		return service.getPersistenceService().queryByExample(new QueryByExample<>(componentAttributeExample));
	}

	@GET
	@APIDescription("Get the codes for a specified attribute type of an entity")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(AttributeCode.class)
	@Path("/{id}/attributes/{attributeType}/codes")
	public List<AttributeCode> getComponentAttributeCodes(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("attributeType")
			@RequiredParam String attributeType)
	{
		List<AttributeCode> attributeCodes = new ArrayList<>();

		ComponentAttribute componentAttributeExample = new ComponentAttribute();
		componentAttributeExample.setComponentId(componentId);
		ComponentAttributePk componentAttributePk = new ComponentAttributePk();
		componentAttributePk.setAttributeType(attributeType);
		componentAttributeExample.setComponentAttributePk(componentAttributePk);
		List<ComponentAttribute> componentAttributes = service.getPersistenceService().queryByExample(new QueryByExample<>(componentAttributeExample));
		for (ComponentAttribute attribute : componentAttributes) {
			AttributeCodePk attributeCodePk = new AttributeCodePk();
			attributeCodePk.setAttributeCode(attribute.getComponentAttributePk().getAttributeCode());
			attributeCodePk.setAttributeType(attribute.getComponentAttributePk().getAttributeType());
			AttributeCode attributCode = service.getAttributeService().findCodeForType(attributeCodePk);
			if (attributCode != null) {
				attributeCodes.add(attributCode);
			}
		}
		return attributeCodes;
	}

	@DELETE
	@APIDescription("Remove all attributes from the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/attributes")
	public void deleteComponentAttributes(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_ATTR_MANAGEMENT);
		if (response == null) {
			ComponentAttribute attribute = new ComponentAttribute();
			attribute.setComponentId(componentId);
			service.getPersistenceService().deleteByExample(attribute);
		}
	}

	@DELETE
	@APIDescription("Inactivates an attribute")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/attributes/{attributeType}/{attributeCode}")
	public Response inactivateComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("attributeType")
			@RequiredParam String attributeType,
			@PathParam("attributeCode")
			@RequiredParam String attributeCode)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_ATTR_MANAGEMENT);
		if (response != null) {
			return response;
		}
		ComponentAttributePk pk = new ComponentAttributePk();
		pk.setAttributeCode(attributeCode);
		pk.setAttributeType(attributeType);
		pk.setComponentId(componentId);
		service.getComponentService().deactivateBaseComponent(ComponentAttribute.class, pk);
		return Response.ok().build();
	}

	@DELETE
	@APIDescription("Delete an attribute from the entity (Hard Removal)")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/attributes/{attributeType}/{attributeCode}/force")
	public Response deleteComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("attributeType")
			@RequiredParam String attributeType,
			@PathParam("attributeCode")
			@RequiredParam String attributeCode)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_ATTR_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentAttributePk pk = new ComponentAttributePk();
		pk.setAttributeCode(attributeCode);
		pk.setAttributeType(attributeType);
		pk.setComponentId(componentId);
		service.getComponentService().deleteBaseComponent(ComponentAttribute.class, pk);
		return Response.ok().build();
	}

	@PUT
	@APIDescription("Activates an attribute on the component")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/attributes/{attributeType}/{attributeCode}/activate")
	public Response activateComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("attributeType")
			@RequiredParam String attributeType,
			@PathParam("attributeCode")
			@RequiredParam String attributeCode)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_ATTR_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentAttributePk pk = new ComponentAttributePk();
		pk.setAttributeCode(attributeCode);
		pk.setAttributeType(attributeType);
		pk.setComponentId(componentId);
		service.getComponentService().activateBaseComponent(ComponentAttribute.class, pk);
		ComponentAttribute componentAttribute = service.getPersistenceService().findById(ComponentAttribute.class, pk);
		return sendSingleEntityResponse(componentAttribute);
	}

	@POST
	@APIDescription("Add a list of attributes to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentAttribute.class)
	@Path("/{id}/attributeList")
	public Response addComponentAttributes(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam List<ComponentAttribute> attributeList)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_ATTR_MANAGEMENT);
		if (response != null) {
			return response;
		}
		ValidationResult validationResult = new ValidationResult();
		attributeList.forEach((attribute) -> {
			validationResult.merge(saveAttribute(componentId, attribute));
		});
		if (!validationResult.valid()) {
			return Response.ok(validationResult.toRestError()).build();
		}
		return Response.ok().build();
	}

	@POST
	@APIDescription("Add an attribute to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentAttribute.class)
	@Path("/{id}/attributes")
	public Response addComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentAttribute attribute)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_ATTR_MANAGEMENT);
		if (response != null) {
			return response;
		}
		ValidationResult validationResult = saveAttribute(componentId, attribute);
		if (!validationResult.valid()) {
			return Response.ok(validationResult.toRestError()).build();
		}
		return Response.created(URI.create(BASE_RESOURCE_PATH
				+ attribute.getComponentAttributePk().getComponentId() + "/attributes/"
				+ StringProcessor.urlEncode(attribute.getComponentAttributePk().getAttributeType()) + "/"
				+ StringProcessor.urlEncode(attribute.getComponentAttributePk().getAttributeCode()))).entity(attribute).build();
	}

	private ValidationResult saveAttribute(String componentId, ComponentAttribute attribute)
	{
		attribute.setComponentId(componentId);
		attribute.getComponentAttributePk().setComponentId(componentId);

		ValidationModel validationModel = new ValidationModel(attribute);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		validationResult.merge(service.getComponentService().checkComponentAttribute(attribute));
		if (validationResult.valid()) {
			service.getComponentService().saveComponentAttribute(attribute);
		}
		return validationResult;
	}
	// </editor-fold>

	//<editor-fold defaultstate="collapsed"  desc="ComponentRESTResource CONTACT section">
	@GET
	@APIDescription("Gets all contact for a component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentContact.class)
	@Path("/{id}/contacts")
	public List<ComponentContact> getComponentContact(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentContact.class, componentId);
	}

	@GET
	@APIDescription("Gets all contact for a component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentContactView.class)
	@Path("/{id}/contacts/view")
	public Response getComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentContact componentContactExample = new ComponentContact();
		componentContactExample.setActiveStatus(filterQueryParams.getStatus());
		componentContactExample.setComponentId(componentId);

		List<ComponentContact> contacts = service.getPersistenceService().queryByExample(componentContactExample);
		List<ComponentContactView> contactViews = new ArrayList<>();
		contacts.forEach(contact -> {
			contactViews.add(ComponentContactView.toView(contact));
		});

		GenericEntity<List<ComponentContactView>> entity = new GenericEntity<List<ComponentContactView>>(contactViews)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@DELETE
	@APIDescription("Delete a contact from the component")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/contacts/{componentContactId}")
	public Response deleteComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("componentContactId")
			@RequiredParam String componentContactId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_CONTACT_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentContact componentContact = service.getPersistenceService().findById(ComponentContact.class, componentContactId);
		if (componentContact != null) {
			checkBaseComponentBelongsToComponent(componentContact, componentId);
			service.getComponentService().deactivateBaseComponent(ComponentContact.class, componentContactId);
		}
		return Response.ok().build();
	}

	@DELETE
	@APIDescription("Delete a contact from the component")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/contacts/{componentContactId}/force")
	public Response hardDeleteComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("componentContactId")
			@RequiredParam String componentContactId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_CONTACT_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentContact componentContact = service.getPersistenceService().findById(ComponentContact.class, componentContactId);
		if (componentContact != null) {
			checkBaseComponentBelongsToComponent(componentContact, componentId);
			service.getComponentService().deleteBaseComponent(ComponentContact.class, componentContactId);
		}
		return Response.ok().build();
	}

	@PUT
	@APIDescription("Activate a contact on the component")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/contacts/{componentContactId}/activate")
	public Response activateComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("componentContactId")
			@RequiredParam String componentContactId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_CONTACT_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentContact componentContact = service.getPersistenceService().findById(ComponentContact.class, componentContactId);
		if (componentContact != null) {
			checkBaseComponentBelongsToComponent(componentContact, componentId);
			service.getComponentService().activateBaseComponent(ComponentContact.class, componentContactId);
			componentContact = service.getPersistenceService().findById(ComponentContact.class, componentContactId);
		}
		return sendSingleEntityResponse(componentContact);
	}

	@POST
	@APIDescription("Add a contact to the component")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentContact.class)
	@Path("/{id}/contacts")
	public Response addComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentContact contact)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_CONTACT_MANAGEMENT);
		if (response != null) {
			return response;
		}

		contact.setComponentId(componentId);
		return saveContact(contact, true);
	}

	@PUT
	@APIDescription("Update a contact associated to the component")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/contacts/{componentContactId}")
	public Response updateComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("componentContactId")
			@RequiredParam String componentContactId,
			ComponentContact contact)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_CONTACT_MANAGEMENT);
		if (response != null) {
			return response;
		}

		response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentContact componentContact = service.getPersistenceService().findById(ComponentContact.class, componentContactId);
		if (componentContact != null) {
			checkBaseComponentBelongsToComponent(componentContact, componentId);
			contact.setComponentId(componentId);
			contact.setComponentContactId(componentContactId);
			response = saveContact(contact, false);
		}
		return response;
	}

	private Response saveContact(ComponentContact contact, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(contact);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			contact.setActiveStatus(ComponentContact.ACTIVE_STATUS);
			contact.setCreateUser(SecurityUtil.getCurrentUserName());
			contact.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentContact(contact, true, false);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create(BASE_RESOURCE_PATH + contact.getComponentId() + "/contacts/" + contact.getContactId())).entity(contact).build();
		} else {
			return Response.ok(contact).build();
		}
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed"  desc="ComponentRESTResource RESOURCE section">
	@GET
	@APIDescription("Get the resources associated to the given component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentResource.class)
	@Path("/{id}/resources")
	public List<ComponentResource> getComponentResource(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		List<ComponentResource> componentResources = service.getComponentService().getBaseComponent(ComponentResource.class, componentId);
		componentResources = SortUtil.sortComponentResource(componentResources);
		return componentResources;
	}

	@GET
	@APIDescription("Get the resources associated to the given component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentResourceView.class)
	@Path("/{id}/resources/view")
	public Response getComponentResourceView(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentResource componentResourceExample = new ComponentResource();
		componentResourceExample.setActiveStatus(filterQueryParams.getStatus());
		componentResourceExample.setComponentId(componentId);
		List<ComponentResource> componentResources = service.getPersistenceService().queryByExample(componentResourceExample);
		componentResources = filterQueryParams.filter(componentResources);
		List<ComponentResourceView> views = ComponentResourceView.toViewList(componentResources);

		GenericEntity<List<ComponentResourceView>> entity = new GenericEntity<List<ComponentResourceView>>(views)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Get a resource associated to the given component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentResource.class)
	@Path("/{id}/resources/{resourceId}")
	public Response getComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("resourceId")
			@RequiredParam String resourceId)
	{
		ComponentResource componentResourceExample = new ComponentResource();
		componentResourceExample.setComponentId(componentId);
		componentResourceExample.setResourceId(resourceId);
		ComponentResource componentResource = service.getPersistenceService().queryOneByExample(componentResourceExample);
		return sendSingleEntityResponse(componentResource);
	}

	@DELETE
	@APIDescription("Inactivates a given resource from the specified component")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/resources/{resourceId}")
	public Response inactivateComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("resourceId")
			@RequiredParam String resourceId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_RESOURCE_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentResource componentResourceExample = new ComponentResource();
		componentResourceExample.setComponentId(componentId);
		componentResourceExample.setResourceId(resourceId);
		ComponentResource componentResource = service.getPersistenceService().queryOneByExample(componentResourceExample);
		if (componentResource != null) {
			service.getComponentService().deactivateBaseComponent(ComponentResource.class, resourceId);
		}
		return Response.ok().build();
	}

	@DELETE
	@APIDescription("Delete a given resource from the specified component")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/resources/{resourceId}/force")
	public Response deleteComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("resourceId")
			@RequiredParam String resourceId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_RESOURCE_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentResource componentResourceExample = new ComponentResource();
		componentResourceExample.setComponentId(componentId);
		componentResourceExample.setResourceId(resourceId);
		ComponentResource componentResource = service.getPersistenceService().queryOneByExample(componentResourceExample);
		if (componentResource != null) {
			service.getComponentService().deleteBaseComponent(ComponentResource.class, resourceId);
		}
		return Response.ok().build();
	}

	@PUT
	@APIDescription("Activates a resource")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/resources/{resourceId}/activate")
	public Response activateComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("resourceId")
			@RequiredParam String resourceId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_RESOURCE_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentResource componentResourceExample = new ComponentResource();
		componentResourceExample.setComponentId(componentId);
		componentResourceExample.setResourceId(resourceId);
		ComponentResource componentResource = service.getPersistenceService().queryOneByExample(componentResourceExample);
		if (componentResource != null) {
			service.getComponentService().activateBaseComponent(ComponentResource.class, resourceId);
			componentResource.setActiveStatus(Component.ACTIVE_STATUS);
		}
		return sendSingleEntityResponse(componentResource);
	}

	@POST
	@APIDescription("Add a resource to the given entity.  Use a form to POST Resource.action?UploadResource to upload file.  "
			+ "To upload: pass the componentResource.resourceType...etc and 'file'.")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentResource.class)
	@Path("/{id}/resources")
	public Response addComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentResource resource)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_RESOURCE_MANAGEMENT);
		if (response != null) {
			return response;
		}

		resource.setComponentId(componentId);
		return saveResource(resource, true);
	}

	@PUT
	@APIDescription("Update a resource associated with a given entity. Use a form to POST Resource.action?UploadResource to upload file. "
			+ " To upload: pass the componentResource.resourceType...etc and 'file'.")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/resources/{resourceId}")
	public Response updateComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("resourceId")
			@RequiredParam String resourceId,
			@RequiredParam ComponentResource resource)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_RESOURCE_MANAGEMENT);
		if (response != null) {
			return response;
		}

		response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentResource componentResourceExample = new ComponentResource();
		componentResourceExample.setComponentId(componentId);
		componentResourceExample.setResourceId(resourceId);
		ComponentResource componentResource = service.getPersistenceService().queryOneByExample(componentResourceExample);
		if (componentResource != null) {
			if (StringUtils.isBlank(resource.getLink())) {
				resource.setFile(componentResource.getFile());
			}
			resource.setComponentId(componentId);
			resource.setResourceId(resourceId);
			response = saveResource(resource, false);
		}
		return response;
	}

	private Response saveResource(ComponentResource resource, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(resource);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			resource.setActiveStatus(ComponentResource.ACTIVE_STATUS);
			resource.setCreateUser(SecurityUtil.getCurrentUserName());
			resource.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentResource(resource);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create(BASE_RESOURCE_PATH + resource.getComponentId() + "/resources/" + resource.getResourceId())).entity(resource).build();
		} else {
			return Response.ok(resource).build();
		}
	}
	// </editor-fold>

	// <editor-fold  defaultstate="collapsed"  desc="ComponentRESTResource MEDIA section">
	@GET
	@APIDescription("Gets the list of media associated to an entity")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentMedia.class)
	@Path("/{id}/media")
	public List<ComponentMedia> getComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentMedia.class, componentId);
	}

	@GET
	@APIDescription("Get the resources associated to the given component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentMediaView.class)
	@Path("/{id}/media/view")
	public Response getComponentMediaView(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentMedia componentMediaExample = new ComponentMedia();
		componentMediaExample.setActiveStatus(filterQueryParams.getStatus());
		componentMediaExample.setComponentId(componentId);
		List<ComponentMedia> componentMedia = service.getPersistenceService().queryByExample(componentMediaExample);
		componentMedia = filterQueryParams.filter(componentMedia);
		List<ComponentMediaView> views = ComponentMediaView.toViewList(componentMedia);

		GenericEntity<List<ComponentMediaView>> entity = new GenericEntity<List<ComponentMediaView>>(views)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Get a media item associated to the given component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentMedia.class)
	@Path("/{id}/media/{mediaId}")
	public Response getComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("mediaId")
			@RequiredParam String mediaId)
	{
		ComponentMedia componentMediaExample = new ComponentMedia();
		componentMediaExample.setComponentId(componentId);
		componentMediaExample.setComponentMediaId(mediaId);
		ComponentMedia componentMedia = service.getPersistenceService().queryOneByExample(componentMediaExample);
		return sendSingleEntityResponse(componentMedia);
	}

	@DELETE
	@APIDescription("Inactivates media from the specified component")
	@Path("/{id}/media/{mediaId}")
	public Response inactivateComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("mediaId")
			@RequiredParam String mediaId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MEDIA_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentMedia componentMedia = service.getPersistenceService().findById(ComponentMedia.class, mediaId);
		if (componentMedia != null) {
			checkBaseComponentBelongsToComponent(componentMedia, componentId);
			service.getComponentService().deactivateBaseComponent(ComponentMedia.class, mediaId);
		}
		return Response.ok().build();
	}

	@DELETE
	@APIDescription("Deletes media from the specified entity")
	@Path("/{id}/media/{mediaId}/force")
	public Response deleteComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("mediaId")
			@RequiredParam String mediaId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MEDIA_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentMedia componentMedia = service.getPersistenceService().findById(ComponentMedia.class, mediaId);
		if (componentMedia != null) {
			checkBaseComponentBelongsToComponent(componentMedia, componentId);
			service.getComponentService().deleteBaseComponent(ComponentMedia.class, mediaId);
		}
		return Response.ok().build();
	}

	@PUT
	@APIDescription("Activates media from the specified component")
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(ComponentMedia.class)
	@Path("/{id}/media/{mediaId}/activate")
	public Response activateComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("mediaId")
			@RequiredParam String mediaId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MEDIA_MANAGEMENT);
		if (response != null) {
			return response;
		}

		ComponentMedia componentMediaExample = new ComponentMedia();
		componentMediaExample.setComponentId(componentId);
		componentMediaExample.setComponentMediaId(mediaId);
		ComponentMedia componentMedia = service.getPersistenceService().queryOneByExample(componentMediaExample);
		if (componentMedia != null) {
			checkBaseComponentBelongsToComponent(componentMedia, componentId);
			service.getComponentService().activateBaseComponent(ComponentMedia.class, mediaId);
			componentMedia.setActiveStatus(ComponentMedia.ACTIVE_STATUS);
		}
		return sendSingleEntityResponse(componentMedia);
	}

	@POST
	@APIDescription("Add media to the specified entity (leave the fileName blank if you want your supplied link to be it's location) "
			+ " Use a form to POST Media.action?UploadMedia to upload file.  To upload: pass the componentMedia.mediaTypeCode...etc and 'file'.")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentMedia.class)
	@Path("/{id}/media")
	public Response addComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentMedia media)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MEDIA_MANAGEMENT);
		if (response != null) {
			return response;
		}
		media.setComponentId(componentId);
		return saveMedia(media, true);
	}

	@PUT
	@APIDescription("Update media associated to the specified entity (leave the fileName blank if you want your supplied link to be it's location) "
			+ " Use a form to POST Media.action?UploadMedia to upload file.  To upload: pass the componentMedia.mediaTypeCode...etc and 'file'.")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/media/{mediaId}")
	public Response updateComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("mediaId")
			@RequiredParam String mediaId,
			@RequiredParam ComponentMedia media)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_MEDIA_MANAGEMENT);
		if (response != null) {
			return response;
		}

		response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentMedia componentMedia = service.getPersistenceService().findById(ComponentMedia.class, mediaId);
		if (componentMedia != null) {
			checkBaseComponentBelongsToComponent(componentMedia, componentId);
			if (StringUtils.isBlank(media.getLink())) {
				media.setFile(componentMedia.getFile());
			}
			media.setComponentId(componentId);
			media.setComponentMediaId(mediaId);
			response = saveMedia(media, false);
		}
		return response;
	}

	private Response saveMedia(ComponentMedia media, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(media);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			media.setActiveStatus(ComponentMedia.ACTIVE_STATUS);
			media.setCreateUser(SecurityUtil.getCurrentUserName());
			media.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentMedia(media);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create(BASE_RESOURCE_PATH + media.getComponentId() + "/media/" + media.getComponentMediaId())).entity(media).build();
		} else {
			return Response.ok(media).build();
		}
	}
	// </editor-fold>

	//<editor-fold defaultstate="collapsed"  desc="ComponentRESTResource TAG section">
	@GET
	@APIDescription("Get the entire tag list (Tag Cloud), excluding the tags already used by a some component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTag.class)
	@Path("/{id}/tagsfree")
	public List<ComponentTag> getFreeComponentTags(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		Component componentExample = new Component();
		componentExample.setComponentId(componentId);
		Component component = componentExample.find();

		if (component != null) {
			ComponentTag componentTagExample = new ComponentTag();
			componentTagExample.setComponentId(componentId);
			List<ComponentTag> componentTags = componentTagExample.findByExample();

			List<ComponentTag> allTags = service.getComponentService().getTagCloud();
			List<ComponentTag> filteredTags = new ArrayList<>();

			for (ComponentTag tag : allTags) {
				boolean pass = true;
				for (ComponentTag myTag : componentTags) {
					if (myTag.getText().equalsIgnoreCase(tag.getText())) {
						pass = false;
						break;
					}
				}
				if (pass) {
					filteredTags.add(tag);
				}
			}

			return filteredTags;
		} else {
			return service.getComponentService().getTagCloud();
		}
	}

	@GET
	@APIDescription("Get the entire tag list (Tag Cloud)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTag.class)
	@Path("/tags")
	public List<ComponentTag> getComponentTags()
	{
		return service.getComponentService().getTagCloud();
	}

	@GET
	@APIDescription("Get all the tag list for a specified component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTag.class)
	@Path("/{id}/tags")
	public List<ComponentTag> getComponentTag(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentTag.class, componentId);
	}

	@GET
	@APIDescription("Get all the tag list for a specified component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(TagView.class)
	@Path("/{id}/tagsview")
	public List<TagView> getComponentTagView(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		List<ComponentTag> tags = service.getComponentService().getBaseComponent(ComponentTag.class, componentId);
		return TagView.toView(tags);
	}

	@GET
	@APIDescription("Get a tag for a component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTag.class)
	@Path("/{id}/tags/{tagId}")
	public Response getComponentTag(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("tagId")
			@RequiredParam String tagId)
	{
		ComponentTag componentTagExample = new ComponentTag();
		componentTagExample.setComponentId(componentId);
		componentTagExample.setTagId(tagId);
		ComponentTag componentTag = service.getPersistenceService().queryOneByExample(new QueryByExample<>(componentTagExample));
		return sendSingleEntityResponse(componentTag);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TAG_MANAGEMENT)
	@APIDescription("Delete all tags from the specified component")
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(ComponentTag.class)
	@Path("/{id}/tags")
	public void deleteComponentTags(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		ComponentTag example = new ComponentTag();
		example.setComponentId(componentId);
		service.getComponentService().deleteAllBaseComponent(ComponentTag.class, componentId);
	}

	@DELETE
	@APIDescription("Delete a tag by id from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/tags/{tagId}")
	public Response deleteComponentTagById(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("tagId")
			@RequiredParam String tagId)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentTag example = new ComponentTag();
		example.setTagId(tagId);
		example.setComponentId(componentId);
		ComponentTag componentTag = service.getPersistenceService().queryOneByExample(new QueryByExample<>(example));
		if (componentTag != null) {
			response = ownerCheck(componentTag, SecurityPermission.ADMIN_ENTRY_TAG_MANAGEMENT);
			if (response == null) {
				service.getComponentService().deleteBaseComponent(ComponentTag.class, tagId);
			}
		}
		return response;
	}

	@DELETE
	@APIDescription("Delete a single tag from the specified entity by the Tag Text")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/tags/text")
	public Response deleteComponentTag(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentTag example)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		ComponentTag componentTagExample = new ComponentTag();
		componentTagExample.setComponentId(componentId);
		componentTagExample.setText(example.getText());
		ComponentTag tag = service.getPersistenceService().queryOneByExample(new QueryByExample<>(componentTagExample));

		if (tag != null) {
			response = ownerCheck(tag, SecurityPermission.ADMIN_ENTRY_TAG_MANAGEMENT);
			if (response == null) {
				service.getComponentService().deleteBaseComponent(ComponentTag.class, tag.getTagId());
				response = Response.ok().build();
			}
		}
		return response;
	}

	@POST
	@APIDescription("Add tags to the specified component")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentTag.class)
	@Path("/{id}/tags/list")
	public Response addComponentTags(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam
			@DataType(ComponentTag.class) List<ComponentTag> tags)
	{
		Boolean valid = Boolean.TRUE;
		List<ComponentTag> verified = new ArrayList<>();
		List<RestErrorModel> unVerified = new ArrayList<>();
		if (!tags.isEmpty()) {
			for (ComponentTag tag : tags) {
				tag.setActiveStatus(ComponentTag.ACTIVE_STATUS);
				tag.setComponentId(componentId);
				ValidationModel validationModel = new ValidationModel(tag);
				validationModel.setConsumeFieldsOnly(true);
				ValidationResult validationResult = ValidationUtil.validate(validationModel);
				if (validationResult.valid()) {
					tag.setCreateUser(SecurityUtil.getCurrentUserName());
					tag.setUpdateUser(SecurityUtil.getCurrentUserName());
					verified.add(tag);
				} else {
					valid = Boolean.FALSE;
					unVerified.add(validationResult.toRestError());
				}
			}
			if (valid) {
				if (!verified.isEmpty()) {
					verified.stream().forEach((tag) -> {
						service.getComponentService().saveComponentTag(tag);
					});
					GenericEntity<List<ComponentTag>> entity = new GenericEntity<List<ComponentTag>>(Lists.newArrayList(verified))
					{
					};
					return Response.created(URI.create("v1/resource/components/" + verified.get(0).getComponentId() + "/tags/" + verified.get(0).getTagId())).entity(entity).build();
				} else {
					return Response.notAcceptable(null).build();
				}
			} else {
				GenericEntity<List<RestErrorModel>> entity = new GenericEntity<List<RestErrorModel>>(Lists.newArrayList(unVerified))
				{
				};
				return Response.ok(entity).build();
			}
		} else {
			return Response.notAcceptable(null).build();
		}
	}

	@POST
	@APIDescription("Add a single tag to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentTag.class)
	@Path("/{id}/tags")
	public Response addComponentTag(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentTag tag)
	{
		tag.setActiveStatus(ComponentTag.ACTIVE_STATUS);
		tag.setComponentId(componentId);

		List<ComponentTag> currentTags = service.getComponentService().getBaseComponent(ComponentTag.class, componentId);
		Boolean cont = Boolean.TRUE;
		if (currentTags != null && !currentTags.isEmpty()) {
			for (ComponentTag item : currentTags) {
				if (item.getText().equals(tag.getText())) {
					cont = Boolean.FALSE;
				}
			}
		}

		ValidationModel validationModel = new ValidationModel(tag);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			tag.setCreateUser(SecurityUtil.getCurrentUserName());
			tag.setUpdateUser(SecurityUtil.getCurrentUserName());
			if (cont) {
				service.getComponentService().saveComponentTag(tag);
			}
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		return Response.created(URI.create(BASE_RESOURCE_PATH + tag.getComponentId() + "/tags/" + tag.getTagId())).entity(tag).build();
	}
	// </editor-fold>

	//<editor-fold defaultstate="collapsed"  desc="ComponentRESTResource Relationships section">
	@GET
	@APIDescription("Get all direct relationship for a specified component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentRelationshipView.class)
	@Path("/{id}/relationships")
	public Response getComponentRelationships(
			@PathParam("id")
			@RequiredParam String componentId,
			@QueryParam("pullAllChildren") boolean pullAllChildren,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentRelationship componentRelationship = new ComponentRelationship();
		componentRelationship.setComponentId(componentId);
		componentRelationship.setActiveStatus(filterQueryParams.getStatus());
		List<ComponentRelationship> relationships = componentRelationship.findByExample();

		if (pullAllChildren) {
			relationships.addAll(pullChildrenRelationships(relationships, filterQueryParams));
		}
		relationships = filterQueryParams.filter(relationships);

		List<ComponentRelationshipView> views = ComponentRelationshipView.toViewList(relationships);

		GenericEntity<List<ComponentRelationshipView>> entity = new GenericEntity<List<ComponentRelationshipView>>(views)
		{
		};

		return sendSingleEntityResponse(entity);
	}

	private List<ComponentRelationship> pullChildrenRelationships(List<ComponentRelationship> parents, FilterQueryParams filterQueryParams)
	{
		List<ComponentRelationship> foundRelationships = new ArrayList<>();

		for (ComponentRelationship parent : parents) {
			ComponentRelationship componentRelationship = new ComponentRelationship();
			componentRelationship.setComponentId(parent.getRelatedComponentId());
			componentRelationship.setActiveStatus(filterQueryParams.getStatus());
			List<ComponentRelationship> children = componentRelationship.findByExample();

			foundRelationships.addAll(children);
			foundRelationships.addAll(pullChildrenRelationships(children, filterQueryParams));
		}

		return foundRelationships;
	}

	@GET
	@APIDescription("Gets approved relationships (direct and indirect) for a specified component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentRelationshipView.class)
	@Path("/{id}/relationships/all")
	public Response getComponentAllRelationships(
			@PathParam("id")
			@RequiredParam String componentId
	)
	{
		List<ComponentRelationshipView> views = new ArrayList<>();

		//Pull relationships direct relationships
		ComponentRelationship componentRelationshipExample = new ComponentRelationship();
		componentRelationshipExample.setActiveStatus(ComponentRelationship.ACTIVE_STATUS);
		componentRelationshipExample.setComponentId(componentId);
		views.addAll(ComponentRelationshipView.toViewList(componentRelationshipExample.findByExample()));
		views = views.stream().filter(r -> r.getTargetApproved()).collect(Collectors.toList());

		//Pull indirect
		componentRelationshipExample = new ComponentRelationship();
		componentRelationshipExample.setActiveStatus(ComponentRelationship.ACTIVE_STATUS);
		componentRelationshipExample.setRelatedComponentId(componentId);
		List<ComponentRelationshipView> relationshipViews = ComponentRelationshipView.toViewList(componentRelationshipExample.findByExample());
		relationshipViews = relationshipViews.stream().filter(r -> r.getOwnerApproved()).collect(Collectors.toList());
		views.addAll(relationshipViews);

		GenericEntity<List<ComponentRelationshipView>> entity = new GenericEntity<List<ComponentRelationshipView>>(views)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Get a relationship entity for a specified component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentRelationship.class)
	@Path("/{id}/relationships/{relationshipId}")
	public Response getComponentRelationship(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("relationshipId")
			@RequiredParam String relationshipId)
	{
		ComponentRelationship relationshipExample = new ComponentRelationship();
		relationshipExample.setComponentRelationshipId(relationshipId);
		relationshipExample.setComponentId(componentId);
		return sendSingleEntityResponse(relationshipExample.find());
	}

	@POST
	@APIDescription("Save a new component relationship")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentRelationship.class)
	@Path("/{id}/relationships")
	public Response addComponentRelationship(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentRelationship relationship)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_RELATIONSHIP_MANAGEMENT);
		if (response != null) {
			return response;
		}
		relationship.setComponentId(componentId);

		return handleSaveRelationship(relationship, true);
	}

	private Response handleSaveRelationship(ComponentRelationship relationship, boolean post)
	{
		ValidationResult validationResult = relationship.validate(true);
		if (validationResult.valid()) {
			relationship = service.getComponentService().saveComponentRelationship(relationship);

			if (post) {
				return Response.created(URI.create(BASE_RESOURCE_PATH
						+ relationship.getComponentId()
						+ "/relationships/"
						+ relationship.getComponentRelationshipId())).entity(relationship).build();
			} else {
				return Response.ok(relationship).build();
			}
		} else {
			return sendSingleEntityResponse(validationResult.toRestError());
		}
	}

	@PUT
	@APIDescription("Updates a component relationship")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentRelationship.class)
	@Path("/{id}/relationships/{relationshipId}")
	public Response updateComponentRelationship(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("relationshipId")
			@RequiredParam String relationshipId,
			@RequiredParam ComponentRelationship relationship)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_RELATIONSHIP_MANAGEMENT);
		if (response != null) {
			return response;
		}

		response = Response.status(Response.Status.NOT_FOUND).build();

		ComponentRelationship existing = new ComponentRelationship();
		existing.setComponentId(componentId);
		existing.setComponentRelationshipId(relationshipId);
		existing = existing.find();
		if (existing != null) {
			relationship.setComponentId(componentId);
			relationship.setComponentRelationshipId(relationshipId);
			response = handleSaveRelationship(relationship, false);
		}

		return response;
	}

	@DELETE
	@APIDescription("Deletes a relationship for a specified component")
	@Path("/{id}/relationships/{relationshipId}")
	public Response deleteRelationship(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("relationshipId")
			@RequiredParam String relationshipId)
	{
		Response response = checkComponentOwner(componentId, SecurityPermission.ADMIN_ENTRY_RELATIONSHIP_MANAGEMENT);
		if (response != null) {
			return response;
		}

		service.getComponentService().deleteBaseComponent(ComponentRelationship.class, relationshipId);
		return Response.ok().build();
	}

	// </editor-fold>
}
