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
package edu.usu.sdl.openstorefront.web.rest.resource;

import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.doc.RequireAdmin;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.service.query.QueryType;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.BaseComponent;
import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttributePk;
import edu.usu.sdl.openstorefront.storage.model.ComponentContact;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSchedule;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSchedulePk;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSection;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSectionPk;
import edu.usu.sdl.openstorefront.storage.model.ComponentExternalDependency;
import edu.usu.sdl.openstorefront.storage.model.ComponentMedia;
import edu.usu.sdl.openstorefront.storage.model.ComponentMetadata;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestion;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.storage.model.ComponentResource;
import edu.usu.sdl.openstorefront.storage.model.ComponentReview;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewCon;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewConPk;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewPro;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewProPk;
import edu.usu.sdl.openstorefront.storage.model.ComponentTag;
import edu.usu.sdl.openstorefront.storage.model.ComponentTracking;
import edu.usu.sdl.openstorefront.storage.model.ReviewCon;
import edu.usu.sdl.openstorefront.storage.model.ReviewPro;
import edu.usu.sdl.openstorefront.storage.model.TrackEventCode;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentDetailView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentQuestionResponseView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentQuestionView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentReviewProCon;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentReviewView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentSearchView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentTrackingWrapper;
import edu.usu.sdl.openstorefront.web.rest.model.FilterQueryParams;
import edu.usu.sdl.openstorefront.web.rest.model.RequiredForComponent;
import edu.usu.sdl.openstorefront.web.viewmodel.RestErrorModel;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jersey.repackaged.com.google.common.collect.Lists;

/**
 * ComponentRESTResource Resource
 *
 * @author dshurtleff
 * @author jlaw
 */
@Path("v1/resource/components")
@APIDescription("Components are the central resource of the system.  The majority of the listing are components.")
public class ComponentRESTResource
		extends BaseResource
{

	@Context
	HttpServletRequest request;

	// COMPONENT GENERAL FUNCTIONS
	@GET
	@APIDescription("Get a list of components <br>(Note: this only the top level component object, See Component Detail for composite resource.)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentSearchView.class)
	public List<ComponentSearchView> getComponents()
	{
		return service.getComponentService().getComponents();
	}

	@GET
	@APIDescription("Get a list of components by an id set.  If it can't find a component for a griven id it's not returned.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Component.class)
	@Path("/list")
	public List<Component> batchGetComponents(
			@QueryParam("idList")
			@RequiredParam List<String> idList
	)
	{
		List<Component> componentViews = new ArrayList<>();
		idList.forEach(componentId -> {
			Component view = service.getPersistenceService().findById(Component.class, componentId);
			if (view != null) {
				componentViews.add(view);
			}
		});

		return componentViews;
	}

	@GET
	@APIDescription("Gets a component <br>(Note: this only the top level component object only)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Component.class)
	@Path("/{id}")
	public Response getComponentSingle(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		Component view = service.getPersistenceService().findById(Component.class, componentId);
		return sendSingleEnityResponse(view);
	}

	@POST
	@RequireAdmin
	@APIDescription("Creates a component")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createComponent(
			@RequiredParam RequiredForComponent component)
	{
		ValidationModel validationModel = new ValidationModel(component);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			component.getComponent().setActiveStatus(Component.ACTIVE_STATUS);
			component.getComponent().setCreateUser(SecurityUtil.getCurrentUserName());
			component.getComponent().setUpdateUser(SecurityUtil.getCurrentUserName());
			return Response.created(URI.create("v1/resource/components/" + service.getComponentService().saveComponent(component).getComponent().getComponentId())).entity(component).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@RequireAdmin
	@APIDescription("Updates a component")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response updateComponent(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam RequiredForComponent component)
	{
		component.getComponent().setComponentId(componentId);
		component.getAttributes().forEach(attribute -> {
			attribute.getComponentAttributePk().setComponentId(componentId);
			attribute.setComponentId(componentId);
		});
		ValidationModel validationModel = new ValidationModel(component);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			component.getComponent().setActiveStatus(Component.ACTIVE_STATUS);
			component.getComponent().setCreateUser(SecurityUtil.getCurrentUserName());
			component.getComponent().setUpdateUser(SecurityUtil.getCurrentUserName());
			return Response.ok(service.getComponentService().saveComponent(component)).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@RequireAdmin
	@APIDescription("Activates a component")
	@Path("/{id}/activate")
	public Response activateComponent(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		Component view = service.getPersistenceService().findById(Component.class, componentId);
		if (view != null) {
			view = service.getComponentService().activateComponent(componentId);
		}
		return sendSingleEnityResponse(view);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Inactivates Component and removes any assoicated user watches.")
	@Path("/{id}")
	public void deleteComponentSingle(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		service.getComponentService().deactivateComponent(componentId);
	}

	@GET
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentDetailView.class)
	@Path("/{id}/detail")
	public Response getComponentDetails(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		ComponentDetailView componentDetail = service.getComponentService().getComponentDetails(componentId);
		//Track Views
		if (componentDetail != null) {
			ComponentTracking componentTracking = new ComponentTracking();
			componentTracking.setClientIp(request.getRemoteAddr());
			componentTracking.setComponentId(componentId);
			componentTracking.setEventDts(TimeUtil.currentDate());
			componentTracking.setTrackEventTypeCode(TrackEventCode.VIEW);
			componentTracking.setActiveStatus(ComponentTracking.ACTIVE_STATUS);
			componentTracking.setCreateUser(SecurityUtil.getCurrentUserName());
			componentTracking.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentTracking(componentTracking);
		}
		service.getComponentService().setLastViewDts(componentId, SecurityUtil.getCurrentUserName());
		return sendSingleEnityResponse(componentDetail);
	}

	// ComponentRESTResource ATTRIBUTE Section
	@GET
	@APIDescription("Gets attributes for a component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentAttribute.class)
	@Path("/{id}/attributes")
	public List<ComponentAttribute> getComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentAttribute.class, componentId);
	}

	@GET
	@APIDescription("Gets attributes for component and a given type")
	@Produces({MediaType.APPLICATION_JSON})
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
		List<ComponentAttribute> componentAttributes = service.getPersistenceService().queryByExample(ComponentAttribute.class, new QueryByExample(componentAttributeExample));
		return componentAttributes;
	}

	@GET
	@APIDescription("Get the codes for a specified attribute type of an entity")
	@Produces({MediaType.APPLICATION_JSON})
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
		List<ComponentAttribute> componentAttributes = service.getPersistenceService().queryByExample(ComponentAttribute.class, new QueryByExample(componentAttributeExample));
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
	@RequireAdmin
	@APIDescription("Remove all attributes from the entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/attributes")
	public void deleteComponentAttributes(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		ComponentAttribute attribute = new ComponentAttribute();
		attribute.setComponentId(componentId);
		service.getPersistenceService().deleteByExample(attribute);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove an attribute from the entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/attributes/{attributeType}/{attributeCode}")
	public void deleteComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("attributeType")
			@RequiredParam String attributeType,
			@PathParam("attributeCode")
			@RequiredParam String attributeCode)
	{
		ComponentAttributePk pk = new ComponentAttributePk();
		pk.setAttributeCode(attributeCode);
		pk.setAttributeType(attributeType);
		pk.setComponentId(componentId);
		service.getComponentService().deactivateBaseComponent(ComponentAttribute.class, pk);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add an attribute to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentContact.class)
	@Path("/{id}/attributes")
	public Response addComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentAttribute attribute)
	{
		attribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
		attribute.setComponentId(componentId);
		attribute.getComponentAttributePk().setComponentId(componentId);

		ValidationModel validationModel = new ValidationModel(attribute);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid() && service.getComponentService().checkComponentAttribute(attribute)) {
			attribute.setCreateUser(SecurityUtil.getCurrentUserName());
			attribute.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentAttribute(attribute);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		return Response.created(URI.create("v1/resource/components/" + attribute.getComponentAttributePk().getComponentId() + "/attributes/" + attribute.getComponentAttributePk().getAttributeType() + "/" + attribute.getComponentAttributePk().getAttributeCode())).entity(attribute).build();
	}

	// ComponentRESTResource DEPENDENCY section
	@GET
	@APIDescription("Get the dependencies from the entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentContact.class)
	@Path("/{id}/dependency")
	public List<ComponentExternalDependency> getComponentDependency(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentExternalDependency.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Removes a dependency from the component")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/dependency/{dependencyId}")
	public void deleteComponentDependency(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("dependencyId")
			@RequiredParam String dependencyId)
	{
		ComponentExternalDependency componentExternalDependency = service.getPersistenceService().findById(ComponentExternalDependency.class, dependencyId);
		if (componentExternalDependency != null) {
			checkBaseComponentBelongsToComponent(componentExternalDependency, componentId);
			service.getComponentService().deactivateBaseComponent(ComponentExternalDependency.class, dependencyId);
		}
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a dependency to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentContact.class)
	@Path("/{id}/dependency")
	public Response addComponentDependency(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentExternalDependency dependency)
	{
		dependency.setComponentId(componentId);
		return saveDependency(dependency, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Update a contact associated to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/dependency/{dependencyId}")
	public Response updateComponentDependency(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("dependencyId")
			@RequiredParam String dependencyId,
			ComponentExternalDependency dependency)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentExternalDependency componentExternalDependency = service.getPersistenceService().findById(ComponentExternalDependency.class, dependencyId);
		if (componentExternalDependency != null) {
			checkBaseComponentBelongsToComponent(componentExternalDependency, componentId);
			dependency.setComponentId(componentId);
			dependency.setDependencyId(dependencyId);
			response = saveDependency(dependency, false);
		}
		return response;
	}

	private Response saveDependency(ComponentExternalDependency dependency, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(dependency);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			dependency.setActiveStatus(ComponentExternalDependency.ACTIVE_STATUS);
			dependency.setCreateUser(SecurityUtil.getCurrentUserName());
			dependency.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentDependency(dependency);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create("v1/resource/components/" + dependency.getComponentId() + "/dependency/" + dependency.getDependencyId())).entity(dependency).build();
		} else {
			return Response.ok(dependency).build();
		}
	}

	// ComponentRESTResource CONTACT section
	@GET
	@APIDescription("Gets all contact for a component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentContact.class)
	@Path("/{id}/contacts")
	public List<ComponentContact> getComponentContact(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentContact.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a contact from the component")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/contacts/{contactId}")
	public void deleteComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("contactId")
			@RequiredParam String contactId)
	{

		ComponentContact componentContact = service.getPersistenceService().findById(ComponentContact.class, contactId);
		if (componentContact != null) {
			checkBaseComponentBelongsToComponent(componentContact, componentId);
			service.getComponentService().deactivateBaseComponent(ComponentContact.class, contactId);
		}
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a contact to the component")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentContact.class)
	@Path("/{id}/contacts")
	public Response addComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentContact contact)
	{
		contact.setComponentId(componentId);
		return saveContact(contact, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Update a contact associated to the component")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/contacts/{contactId}")
	public Response updateComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("contactId")
			@RequiredParam String contactId,
			ComponentContact contact)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentContact componentContact = service.getPersistenceService().findById(ComponentContact.class, contactId);
		if (componentContact != null) {
			checkBaseComponentBelongsToComponent(componentContact, componentId);
			contact.setComponentId(componentId);
			contact.setContactId(contactId);
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
			service.getComponentService().saveComponentContact(contact);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create("v1/resource/components/" + contact.getComponentId() + "/contacts/" + contact.getContactId())).entity(contact).build();
		} else {
			return Response.ok(contact).build();
		}
	}

	// ComponentRESTResource Evaluation Section section
	@GET
	@APIDescription("Gets an evaluation section associated to the component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentEvaluationSection.class)
	@Path("/{id}/sections")
	public List<ComponentEvaluationSection> getComponentEvaluationSection(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentEvaluationSection.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Removes an evaluation section from the component")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/sections/{evalSection}")
	public void deleteComponentEvaluationSection(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("evalSection")
			@RequiredParam String evalSection)
	{
		ComponentEvaluationSectionPk pk = new ComponentEvaluationSectionPk();
		pk.setComponentId(componentId);
		pk.setEvaulationSection(evalSection);
		service.getComponentService().deactivateBaseComponent(ComponentEvaluationSection.class, pk);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Removes all evaluation section from the component")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/sections")
	public void deleteAllComponentEvaluationSections(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		service.getComponentService().deleteAllBaseComponent(ComponentEvaluationSection.class, componentId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Adds an evaluation section to the component")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentEvaluationSection.class)
	@Path("/{id}/sections")
	public Response addComponentEvaluationSection(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentEvaluationSection section)
	{
		section.setComponentId(componentId);
		section.getComponentEvaluationSectionPk().setComponentId(componentId);
		return saveSection(section, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Updates an evaluation section for a component")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/sections/{evalSectionId}")
	public Response updateComponentEvaluationSection(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("evalSectionId")
			@RequiredParam String evalSectionId,
			@RequiredParam ComponentEvaluationSection section)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentEvaluationSectionPk pk = new ComponentEvaluationSectionPk();
		pk.setComponentId(componentId);
		pk.setEvaulationSection(evalSectionId);
		ComponentEvaluationSection componentEvaluationSection = service.getPersistenceService().findById(ComponentEvaluationSection.class, pk);
		if (componentEvaluationSection != null) {
			section.setComponentId(componentId);
			section.setComponentEvaluationSectionPk(pk);
			response = saveSection(section, false);
		}
		return response;
	}

	private Response saveSection(ComponentEvaluationSection section, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(section);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			section.setActiveStatus(ComponentEvaluationSection.ACTIVE_STATUS);
			section.setCreateUser(SecurityUtil.getCurrentUserName());
			section.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentEvaluationSection(section);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create("v1/resource/components/" + section.getComponentId() + "/sections/" + section.getComponentEvaluationSectionPk().getEvaulationSection())).entity(section).build();
		} else {
			return Response.ok(section).build();
		}
	}

	// ComponentRESTResource Evaluation Schedule section
	@GET
	@APIDescription("Gets a list of evaluation schedules associated to the component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentEvaluationSchedule.class)
	@Path("/{id}/schedules")
	public List<ComponentEvaluationSchedule> getComponentEvaluationSchedule(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentEvaluationSchedule.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Removes the specified evaluation schedule from the component")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/schedules/{evalLevel}")
	public void deleteComponentEvaluationSchedule(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("evalLevel")
			@RequiredParam String evalLevel)
	{
		ComponentEvaluationSchedulePk pk = new ComponentEvaluationSchedulePk();
		pk.setComponentId(componentId);
		pk.setEvaluationLevelCode(evalLevel);
		service.getComponentService().deactivateBaseComponent(ComponentEvaluationSchedule.class, pk);
	}

	@POST
	@RequireAdmin
	@APIDescription("Adds a component evaluation schedule to the component")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentEvaluationSchedule.class)
	@Path("/{id}/schedules")
	public Response addComponentEvaluationSchedule(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentEvaluationSchedule schedule)
	{
		schedule.setComponentId(componentId);
		schedule.getComponentEvaluationSchedulePk().setComponentId(componentId);
		return saveSchedule(schedule, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Updates a component evaluation schedule associated to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/schedules/{evalLevel}")
	public Response updateComponentEvaluationSchedule(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("evalLevel")
			@RequiredParam String evalLevel,
			@RequiredParam ComponentEvaluationSchedule schedule)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentEvaluationSchedulePk pk = new ComponentEvaluationSchedulePk();
		pk.setComponentId(componentId);
		pk.setEvaluationLevelCode(evalLevel);
		ComponentEvaluationSection componentEvaluationSection = service.getPersistenceService().findById(ComponentEvaluationSection.class, pk);
		if (componentEvaluationSection != null) {
			schedule.setComponentId(componentId);
			schedule.setComponentEvaluationSchedulePk(pk);
			response = saveSchedule(schedule, false);
		}
		return response;
	}

	private Response saveSchedule(ComponentEvaluationSchedule schedule, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(schedule);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			schedule.setActiveStatus(ComponentEvaluationSchedule.ACTIVE_STATUS);
			schedule.setCreateUser(SecurityUtil.getCurrentUserName());
			schedule.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentEvaluationSchedule(schedule);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {

			return Response.created(URI.create("v1/resource/components/" + schedule.getComponentId() + "/schedules/" + schedule.getComponentEvaluationSchedulePk().getEvaluationLevelCode())).entity(schedule).build();
		} else {
			return Response.ok(schedule).build();
		}
	}

	// ComponentRESTResource MEDIA section
	@GET
	@APIDescription("Gets the list of media associated to an entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentMedia.class)
	@Path("/{id}/media")
	public List<ComponentMedia> getComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentMedia.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Removes media from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(ComponentMedia.class)
	@Path("/{id}/media/{mediaId}")
	public void deleteComponentMedia(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("mediaId")
			@RequiredParam String mediaId)
	{
		ComponentMedia componentMedia = service.getPersistenceService().findById(ComponentMedia.class, mediaId);
		if (componentMedia != null) {
			checkBaseComponentBelongsToComponent(componentMedia, componentId);
			service.getComponentService().deactivateBaseComponent(ComponentMedia.class, mediaId);
		}
	}

	@POST
	@RequireAdmin
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
		media.setComponentId(componentId);
		return saveMedia(media, true);
	}

	@PUT
	@RequireAdmin
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
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentMedia componentMedia = service.getPersistenceService().findById(ComponentMedia.class, mediaId);
		if (componentMedia != null) {
			checkBaseComponentBelongsToComponent(componentMedia, componentId);
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
			return Response.created(URI.create("v1/resource/components/" + media.getComponentId() + "/media/" + media.getComponentMediaId())).entity(media).build();
		} else {
			return Response.ok(media).build();
		}
	}

	// ComponentRESTResource METADATA section
	@GET
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentMetadata.class)
	@Path("/{id}/metadata")
	public List<ComponentMetadata> getComponentMetadata(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentMetadata.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Removes metadata from the specified component")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/metadata/{metadataId}")
	public void deleteComponentMetadata(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("metadataId")
			@RequiredParam String metadataId)
	{
		ComponentMetadata componentMetadata = service.getPersistenceService().findById(ComponentMetadata.class, metadataId);
		if (componentMetadata != null) {
			checkBaseComponentBelongsToComponent(componentMetadata, componentId);
			service.getComponentService().deactivateBaseComponent(ComponentMetadata.class, metadataId);
		}
	}

	@POST
	@RequireAdmin
	@APIDescription("Add metadata to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentMetadata.class)
	@Path("/{id}/metadata")
	public Response addComponentMetadata(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentMetadata metadata)
	{
		metadata.setComponentId(componentId);
		return saveMetadata(metadata, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Update metadata associated to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/metadata/{metadataId}")
	public Response updateComponentMetadata(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("metadataId")
			@RequiredParam String metadataId,
			@RequiredParam ComponentMetadata metadata)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentMetadata componentMetadata = service.getPersistenceService().findById(ComponentMetadata.class, metadataId);
		if (componentMetadata != null) {
			checkBaseComponentBelongsToComponent(componentMetadata, componentId);
			metadata.setMetadataId(metadataId);
			metadata.setComponentId(componentId);
			response = saveMetadata(metadata, false);
		}
		return response;
	}

	private Response saveMetadata(ComponentMetadata metadata, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(metadata);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			metadata.setActiveStatus(ComponentMetadata.ACTIVE_STATUS);
			metadata.setCreateUser(SecurityUtil.getCurrentUserName());
			metadata.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentMetadata(metadata);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {

			return Response.created(URI.create("v1/resource/components/" + metadata.getComponentId() + "/media/" + metadata.getMetadataId())).build();
		} else {
			return Response.ok(metadata).build();
		}
	}

	// ComponentRESTResource QUESTION section
	@GET
	@APIDescription("Get the questions associated with the specified entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentQuestion.class)
	@Path("/{id}/questions")
	public List<ComponentQuestion> getComponentQuestions(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentQuestion.class, componentId);
	}

	@GET
	@APIDescription("Get the questions associated with the specified component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentQuestionView.class)
	@Path("/{id}/questions/details")
	public List<ComponentQuestionView> getDetailComponentQuestions(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		List<ComponentQuestionView> componentQuestionViews = new ArrayList<>();
		List<ComponentQuestion> questions = service.getComponentService().getBaseComponent(ComponentQuestion.class, componentId);
		for (ComponentQuestion question : questions) {
			ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
			responseExample.setQuestionId(question.getQuestionId());

			List<ComponentQuestionResponse> responses = service.getPersistenceService().queryByExample(ComponentQuestionResponse.class, new QueryByExample(responseExample));
			ComponentQuestionView questionView = ComponentQuestionView.toView(question, ComponentQuestionResponseView.toViewList(responses));
			componentQuestionViews.add(questionView);
		}
		return componentQuestionViews;
	}

	@GET
	@APIDescription("Get a question for the specified component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentQuestion.class)
	@Path("/{id}/questions/{questionId}")
	public Response getComponentQuestionResponses(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId)
	{
		ComponentQuestion componentQuestion = service.getPersistenceService().findById(ComponentQuestion.class, questionId);
		if (componentQuestion != null) {
			checkBaseComponentBelongsToComponent(componentQuestion, componentId);
		}
		return sendSingleEnityResponse(componentQuestion);
	}

	@DELETE
	@APIDescription("Remove a question from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/questions/{questionId}")
	public Response deleteComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId)
	{
		Response response = Response.ok().build();
		ComponentQuestion componentQuestion = service.getPersistenceService().findById(ComponentQuestion.class, questionId);
		if (componentQuestion != null) {
			checkBaseComponentBelongsToComponent(componentQuestion, componentId);
			response = ownerCheck(componentQuestion);
			if (response == null) {
				service.getComponentService().deactivateBaseComponent(ComponentQuestion.class, questionId);
				response = Response.ok().build();
			}
		}
		return response;
	}

	@POST
	@APIDescription("Add a new question to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentQuestion.class)
	@Path("/{id}/questions")
	public Response addComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentQuestion question)
	{
		question.setComponentId(componentId);
		return saveQuestion(question, true);
	}

	@PUT
	@APIDescription("Update a question associated with the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/questions/{questionId}")
	public Response updateComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@RequiredParam ComponentQuestion question)
	{
		Response response = Response.ok().build();
		ComponentQuestion componentQuestion = service.getPersistenceService().findById(ComponentQuestion.class, questionId);
		if (componentQuestion != null) {
			checkBaseComponentBelongsToComponent(componentQuestion, componentId);
			response = ownerCheck(componentQuestion);
			if (response == null) {
				question.setComponentId(componentId);
				question.setQuestionId(questionId);
				response = saveQuestion(question, false);
			}
		}
		return response;
	}

	private Response saveQuestion(ComponentQuestion question, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(question);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			question.setActiveStatus(ComponentQuestion.ACTIVE_STATUS);
			question.setCreateUser(SecurityUtil.getCurrentUserName());
			question.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentQuestion(question);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create("v1/resource/components/" + question.getComponentId() + "/questions/" + question.getQuestionId())).entity(question).build();
		} else {
			return Response.ok(question).build();
		}
	}

	// ComponentRESTResource QUESTION RESPONSE section
	@GET
	@APIDescription("Gets the responses for a given question associated to the specified v ")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentQuestionResponse.class)
	@Path("/{id}/questions/{questionId}/responses")
	public List<ComponentQuestionResponse> getComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId)
	{
		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);
		responseExample.setQuestionId(questionId);
		List<ComponentQuestionResponse> responses = service.getPersistenceService().queryByExample(ComponentQuestionResponse.class, responseExample);
		return responses;
	}

	@GET
	@APIDescription("Gets the responses for a given question associated to the specified v ")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentQuestionResponse.class)
	@Path("/{id}/questions/{questionId}/responses/{responseId}")
	public Response getComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@PathParam("responseId")
			@RequiredParam String responseId)
	{
		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);
		responseExample.setQuestionId(questionId);
		responseExample.setResponseId(responseId);
		ComponentQuestionResponse questionResponse = service.getPersistenceService().queryOneByExample(ComponentQuestionResponse.class, responseExample);
		return sendSingleEnityResponse(questionResponse);
	}

	@DELETE
	@APIDescription("Remove a response from the given question on the specified component")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/questions/{questionId}/responses/{responseId}")
	public Response deleteComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@PathParam("responseId")
			@RequiredParam String responseId)
	{
		Response response = Response.ok().build();
		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);
		responseExample.setQuestionId(questionId);
		responseExample.setResponseId(responseId);
		ComponentQuestionResponse questionResponse = service.getPersistenceService().queryOneByExample(ComponentQuestionResponse.class, responseExample);
		if (questionResponse != null) {
			response = ownerCheck(questionResponse);
			if (response == null) {
				service.getComponentService().deactivateBaseComponent(ComponentQuestionResponse.class, responseId);
				response = Response.ok().build();
			}
		}
		return response;
	}

	@POST
	@APIDescription("Add a response to the given question on the specified component")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentQuestionResponse.class)
	@Path("/{id}/questions/{questionId}/responses")
	public Response addComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@RequiredParam ComponentQuestionResponse response)
	{
		response.setComponentId(componentId);
		response.setQuestionId(questionId);
		return saveQuestionResponse(response, true);
	}

	@PUT
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/questions/{questionId}/responses/{responseId}")
	public Response updateComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@PathParam("responseId")
			@RequiredParam String responseId,
			@RequiredParam ComponentQuestionResponse questionResponseInput)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		ComponentQuestionResponse responseExample = new ComponentQuestionResponse();
		responseExample.setComponentId(componentId);
		responseExample.setQuestionId(questionId);
		responseExample.setResponseId(responseId);
		ComponentQuestionResponse questionResponse = service.getPersistenceService().queryOneByExample(ComponentQuestionResponse.class, responseExample);
		if (questionResponse != null) {
			response = ownerCheck(questionResponse);
			if (response == null) {
				questionResponseInput.setComponentId(componentId);
				questionResponseInput.setQuestionId(questionId);
				questionResponseInput.setResponseId(responseId);
				response = saveQuestionResponse(questionResponseInput, false);
			}
		}
		return response;
	}

	private Response saveQuestionResponse(ComponentQuestionResponse response, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(response);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			response.setActiveStatus(ComponentQuestionResponse.ACTIVE_STATUS);
			response.setCreateUser(SecurityUtil.getCurrentUserName());
			response.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentQuestionResponse(response);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {

			return Response.created(URI.create("v1/resource/components/" + response.getComponentId() + "/questions/" + response.getQuestionId() + "/responses/" + response.getResponseId())).entity(response).build();
		} else {
			return Response.ok(response).build();
		}
	}

	// ComponentRESTResource RESOURCE section
	@GET
	@APIDescription("Get the resources associated to the given component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentResource.class)
	@Path("/{id}/resources")
	public List<ComponentResource> getComponentResource(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentResource.class, componentId);
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
		ComponentResource componentResource = service.getPersistenceService().queryOneByExample(ComponentResource.class, componentResourceExample);
		return sendSingleEnityResponse(componentResource);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a given resource from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/resources/{resourceId}")
	public void deleteComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("resourceId")
			@RequiredParam String resourceId)
	{
		ComponentResource componentResourceExample = new ComponentResource();
		componentResourceExample.setComponentId(componentId);
		componentResourceExample.setResourceId(resourceId);
		ComponentResource componentResource = service.getPersistenceService().queryOneByExample(ComponentResource.class, componentResourceExample);
		if (componentResource != null) {
			service.getComponentService().deactivateBaseComponent(ComponentResource.class, resourceId);
		}
	}

	@POST
	@RequireAdmin
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
		resource.setComponentId(componentId);
		return saveResource(resource, true);
	}

	@PUT
	@RequireAdmin
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
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentResource componentResourceExample = new ComponentResource();
		componentResourceExample.setComponentId(componentId);
		componentResourceExample.setResourceId(resourceId);
		ComponentResource componentResource = service.getPersistenceService().queryOneByExample(ComponentResource.class, componentResourceExample);
		if (componentResource != null) {
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
			return Response.created(URI.create("v1/resource/components/" + resource.getComponentId() + "/resources/" + resource.getResourceId())).entity(resource).build();
		} else {
			return Response.ok(resource).build();
		}
	}

	// ComponentRESTResource REVIEW section
	@GET
	@APIDescription("Get the reviews for a specified entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentReview.class)
	@Path("/{id}/reviews")
	public List<ComponentReview> getComponentReview(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentReview.class, componentId);
	}

	@GET
	@APIDescription("Get the reviews for a specified user")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentReview.class)
	@Path("/reviews/{username}")
	public List<ComponentReviewView> getComponentReviewsByUsername(
			@PathParam("username")
			@RequiredParam String username)
	{
		return service.getComponentService().getReviewByUser(username);
	}

	@DELETE
	@APIDescription("Remove a review from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/reviews/{reviewId}")
	public Response deleteComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		Response response = Response.ok().build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			response = ownerCheck(componentReview);
			if (response == null) {
				service.getComponentService().deactivateBaseComponent(ComponentReview.class, reviewId);
			}
		}
		return response;
	}

	@POST
	@APIDescription("Add a review to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReview.class)
	@Path("/{id}/reviews")
	public Response addComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentReview review)
	{
		review.setComponentId(componentId);
		return saveReview(review, true);
	}

	@PUT
	@APIDescription("Update a review associated with the given entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/reviews/{reviewId}")
	public Response updateComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@RequiredParam ComponentReview review)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview);
			if (response == null) {
				review.setComponentId(componentId);
				review.setComponentReviewId(reviewId);
				response = saveReview(review, false);
			}
		}
		return response;
	}

	private Response saveReview(ComponentReview review, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(review);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			review.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			review.setCreateUser(SecurityUtil.getCurrentUserName());
			review.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentReview(review);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create("v1/resource/components/" + review.getComponentId() + "/review/" + review.getComponentReviewId())).entity(review).build();
		} else {
			return Response.ok(review).build();
		}
	}

	@POST
	@APIDescription("Add a review to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReviewView.class)
	@Path("/{id}/reviews/detail")
	public Response addComponentReviewDetail(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentReviewView review)
	{
		review.setComponentId(componentId);
		return saveFullReview(review, true);
	}

	@PUT
	@APIDescription("Update a review associated with the given entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/reviews/{reviewId}/detail")
	public Response updateComponentReviewDetail(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@RequiredParam ComponentReviewView review)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview);
			if (response == null) {
				review.setComponentId(componentId);
				review.setReviewId(reviewId);
				response = saveFullReview(review, false);
			}
		}
		return response;
	}

	private Response saveFullReview(ComponentReviewView review, Boolean post)
	{
		ComponentReview componentReview = new ComponentReview();
		componentReview.setComponentId(review.getComponentId());
		componentReview.setComponentReviewId(review.getReviewId());
		componentReview.setComment(review.getComment());
		componentReview.setLastUsed(review.getLastUsed());
		componentReview.setOrganization(review.getOrganization());
		componentReview.setRating(review.getRating());
		componentReview.setRecommend(review.isRecommend());
		componentReview.setTitle(review.getTitle());
		componentReview.setUserTimeCode(review.getUsedTimeCode());
		componentReview.setUserTypeCode(review.getUserType());

		List<ComponentReviewPro> pros = new ArrayList<>();
		for (ComponentReviewProCon pro : review.getPros()) {
			ComponentReviewPro componentReviewPro = new ComponentReviewPro();
			ComponentReviewProPk reviewProPk = new ComponentReviewProPk();
			reviewProPk.setReviewPro(pro.getText());
			componentReviewPro.setComponentReviewProPk(reviewProPk);
			pros.add(componentReviewPro);
		}

		List<ComponentReviewCon> cons = new ArrayList<>();
		for (ComponentReviewProCon con : review.getCons()) {
			ComponentReviewCon componentReviewCon = new ComponentReviewCon();
			ComponentReviewConPk reviewConPk = new ComponentReviewConPk();
			reviewConPk.setReviewCon(con.getText());
			componentReviewCon.setComponentReviewConPk(reviewConPk);
			cons.add(componentReviewCon);
		}

		ValidationResult validationResult = service.getComponentService().saveDetailReview(componentReview, pros, cons);

		if (validationResult.valid() == false) {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create("v1/resource/components/" + componentReview.getComponentId() + "/review/" + componentReview.getComponentReviewId())).entity(review).build();
		} else {
			return Response.ok(review).build();
		}
	}

	// ComponentRESTResource REVIEW CON section
	@GET
	@APIDescription("Get the cons associated to a review")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentReviewCon.class)
	@Path("/{id}/reviews/{reviewId}/cons")
	public List<ComponentReviewCon> getComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		ComponentReviewCon componentReviewConExample = new ComponentReviewCon();
		ComponentReviewConPk componentReviewConPk = new ComponentReviewConPk();
		componentReviewConPk.setComponentReviewId(reviewId);
		componentReviewConExample.setComponentReviewConPk(componentReviewConPk);
		componentReviewConExample.setComponentId(componentId);
		return service.getPersistenceService().queryByExample(ComponentReviewCon.class, new QueryByExample(componentReviewConExample));
	}

	@GET
	@APIDescription("Get the cons associated to a review")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentReviewCon.class)
	@Path("/{id}/reviews/{reviewId}/cons/{conId}")
	public Response getComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@PathParam("conId")
			@RequiredParam String conId)
	{
		ComponentReviewCon componentReviewConExample = new ComponentReviewCon();
		ComponentReviewConPk componentReviewConPk = new ComponentReviewConPk();
		componentReviewConPk.setComponentReviewId(reviewId);
		componentReviewConPk.setReviewCon(conId);
		componentReviewConExample.setComponentReviewConPk(componentReviewConPk);
		componentReviewConExample.setComponentId(componentId);

		ComponentReviewCon reviewCon = service.getPersistenceService().queryOneByExample(ComponentReviewCon.class, new QueryByExample(componentReviewConExample));
		return sendSingleEnityResponse(reviewCon);
	}

	@DELETE
	@APIDescription("Removes all cons from the given review accociated with the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/reviews/{reviewId}/cons")
	public Response deleteComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		Response response = Response.ok().build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview);
			if (response == null) {
				ComponentReviewCon example = new ComponentReviewCon();
				ComponentReviewConPk pk = new ComponentReviewConPk();
				pk.setComponentReviewId(reviewId);
				example.setComponentReviewConPk(pk);
				service.getPersistenceService().deleteByExample(example);
			}
		}
		return response;
	}

	@POST
	@APIDescription("Add a cons to the given review associated with the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReviewCon.class)
	@Path("/{id}/reviews/{reviewId}/cons")
	public Response addComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@RequiredParam String text)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview);
			if (response == null) {
				ComponentReviewCon con = new ComponentReviewCon();
				ComponentReviewConPk pk = new ComponentReviewConPk();
				pk.setComponentReviewId(reviewId);
				ReviewCon conCode = service.getLookupService().getLookupEnity(ReviewCon.class, text);
				if (conCode == null) {
					conCode = service.getLookupService().getLookupEnityByDesc(ReviewCon.class, text);
					if (conCode == null) {
						pk.setReviewCon(null);
					} else {
						pk.setReviewCon(conCode.getCode());
					}
				} else {
					pk.setReviewCon(conCode.getCode());
				}
				con.setComponentReviewConPk(pk);
				con.setActiveStatus(ComponentReviewCon.ACTIVE_STATUS);
				con.setComponentId(componentId);
				ValidationModel validationModel = new ValidationModel(con);
				validationModel.setConsumeFieldsOnly(true);
				ValidationResult validationResult = ValidationUtil.validate(validationModel);

				if (validationResult.valid()) {
					con.setCreateUser(SecurityUtil.getCurrentUserName());
					con.setUpdateUser(SecurityUtil.getCurrentUserName());
					service.getComponentService().saveComponentReviewCon(con);
					response = Response.created(URI.create("v1/resource/components/" + con.getComponentId()
							+ "/reviews/" + con.getComponentReviewConPk().getComponentReviewId()
							+ "/cons/" + con.getComponentReviewConPk().getReviewCon())).entity(con).build();

				} else {
					response = Response.ok(validationResult.toRestError()).build();
				}
			}
		}
		return response;
	}

	// ComponentRESTResource REVIEW PRO section
	@GET
	@APIDescription("Get the pros for a review associated with the given entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentReviewPro.class)
	@Path("/{id}/reviews/{reviewId}/pros")
	public List<ComponentReviewPro> getComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		ComponentReviewPro componentReviewProExample = new ComponentReviewPro();
		componentReviewProExample.setComponentId(componentId);
		ComponentReviewProPk componentReviewProPk = new ComponentReviewProPk();
		componentReviewProPk.setComponentReviewId(reviewId);
		componentReviewProExample.setComponentReviewProPk(componentReviewProPk);
		return service.getPersistenceService().queryByExample(ComponentReviewPro.class, new QueryByExample(componentReviewProExample));
	}

	@GET
	@APIDescription("Get the pros for a review associated with the given entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentReviewPro.class)
	@Path("/{id}/reviews/{reviewId}/pros/{proId}")
	public Response getComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@PathParam("proId")
			@RequiredParam String proId)
	{
		ComponentReviewPro componentReviewProExample = new ComponentReviewPro();
		componentReviewProExample.setComponentId(componentId);
		ComponentReviewProPk componentReviewProPk = new ComponentReviewProPk();
		componentReviewProPk.setComponentReviewId(reviewId);
		componentReviewProPk.setReviewPro(proId);
		componentReviewProExample.setComponentReviewProPk(componentReviewProPk);
		ComponentReviewPro componentReviewPro = service.getPersistenceService().queryOneByExample(ComponentReviewPro.class, new QueryByExample(componentReviewProExample));
		return sendSingleEnityResponse(componentReviewPro);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Removes all pros from the review associated with a specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/reviews/{reviewId}/pros")
	public Response deleteComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		Response response = Response.ok().build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview);
			if (response == null) {
				ComponentReviewPro example = new ComponentReviewPro();
				ComponentReviewProPk pk = new ComponentReviewProPk();
				pk.setComponentReviewId(reviewId);
				example.setComponentReviewProPk(pk);
				service.getPersistenceService().deleteByExample(example);
				service.getComponentService().deactivateBaseComponent(ComponentReviewPro.class, pk);
			}
		}
		return response;
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a pro to the review associated with the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReviewPro.class)
	@Path("/{id}/reviews/{reviewId}/pros")
	public Response addComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@RequiredParam String text)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		ComponentReview componentReview = service.getPersistenceService().findById(ComponentReview.class, reviewId);
		if (componentReview != null) {
			checkBaseComponentBelongsToComponent(componentReview, componentId);
			response = ownerCheck(componentReview);
			if (response == null) {
				ComponentReviewPro pro = new ComponentReviewPro();
				ComponentReviewProPk pk = new ComponentReviewProPk();
				pk.setComponentReviewId(reviewId);
				ReviewPro proCode = service.getLookupService().getLookupEnity(ReviewPro.class, text);
				if (proCode == null) {
					proCode = service.getLookupService().getLookupEnityByDesc(ReviewPro.class, text);
					if (proCode == null) {
						pk.setReviewPro(null);
					} else {
						pk.setReviewPro(proCode.getCode());
					}
				} else {
					pk.setReviewPro(proCode.getCode());
				}
				pro.setComponentReviewProPk(pk);
				pro.setActiveStatus(ComponentReviewPro.ACTIVE_STATUS);
				pro.setComponentId(componentId);

				ValidationModel validationModel = new ValidationModel(pro);
				validationModel.setConsumeFieldsOnly(true);
				ValidationResult validationResult = ValidationUtil.validate(validationModel);
				if (validationResult.valid()) {
					pro.setCreateUser(SecurityUtil.getCurrentUserName());
					pro.setUpdateUser(SecurityUtil.getCurrentUserName());
					service.getComponentService().saveComponentReviewPro(pro);
					response = Response.created(URI.create("v1/resource/components/" + pro.getComponentId()
							+ "/reviews/" + pro.getComponentReviewProPk().getComponentReviewId()
							+ "/pros/" + pro.getComponentReviewProPk().getReviewPro())).entity(pro).build();
				} else {
					response = Response.ok(validationResult.toRestError()).build();
				}
			}
		}
		return response;
	}

	// ComponentRESTResource TAG section
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
		ComponentTag componentTag = service.getPersistenceService().queryOneByExample(ComponentTag.class, new QueryByExample(componentTagExample));
		return sendSingleEnityResponse(componentTag);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove all tag from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(ComponentTag.class)
	@Path("/{id}/tags")
	public void deleteComponentTags(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		ComponentTag example = new ComponentTag();
		example.setComponentId(componentId);
		service.getPersistenceService().deleteByExample(example);
		Component temp = service.getPersistenceService().findById(Component.class, componentId);
		service.getSearchService().addIndex(temp);
	}

	@DELETE
	@APIDescription("Remove a tag by id from the specified entity")
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
		ComponentTag componentTag = service.getPersistenceService().queryOneByExample(ComponentTag.class, new QueryByExample(example));
		if (componentTag != null) {
			response = ownerCheck(componentTag);
			if (response == null) {
				service.getComponentService().deactivateBaseComponent(ComponentTag.class, tagId);
				Component temp = service.getPersistenceService().findById(Component.class, componentId);
				service.getSearchService().addIndex(temp);
			}
		}
		return response;
	}

	@DELETE
	@APIDescription("Remove a single tag from the specified entity by the Tag Text")
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
		ComponentTag tag = service.getPersistenceService().queryOneByExample(ComponentTag.class, new QueryByExample(componentTagExample));

		if (tag != null) {
			response = ownerCheck(tag);
			if (response == null) {
				service.getComponentService().deleteBaseComponent(ComponentTag.class, tag.getTagId());
				Component temp = service.getPersistenceService().findById(Component.class, componentId);
				service.getSearchService().addIndex(temp);
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
		if (tags.size() > 0) {
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
				if (verified.size() > 0) {
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
		if (currentTags != null && currentTags.size() > 0) {
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
		return Response.created(URI.create("v1/resource/components/" + tag.getComponentId() + "/tags/" + tag.getTagId())).entity(tag).build();
	}

	// ComponentRESTResource TRACKING section
	@GET
	@RequireAdmin
	@APIDescription("Get the list of tracking details on a specified component. Always sorts by create date.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTracking.class)
	@Path("/{id}/tracking")
	public Response getActiveComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ComponentTracking trackingExample = new ComponentTracking();
		trackingExample.setComponentId(componentId);
		trackingExample.setActiveStatus(filterQueryParams.getStatus());

		QueryByExample<ComponentTracking> queryByExample = new QueryByExample(trackingExample);
		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());

		ComponentTracking trackingOrderExample = new ComponentTracking();
		trackingOrderExample.setCreateDts(QueryByExample.DATE_FLAG);
		queryByExample.setOrderBy(trackingOrderExample);
		queryByExample.setSortDirection(OpenStorefrontConstant.SORT_DESCENDING);

		List<ComponentTracking> componentTrackings = service.getPersistenceService().queryByExample(ComponentTracking.class, queryByExample);

		long total = service.getPersistenceService().countByExample(new QueryByExample(QueryType.COUNT, trackingExample));
		return sendSingleEnityResponse(new ComponentTrackingWrapper(componentTrackings, total));
	}

	@GET
	@RequireAdmin
	@APIDescription("Get the list of tracking details on a specified component")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTracking.class)
	@Path("/{id}/tracking/{trackingId}")
	public Response getComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("id")
			@RequiredParam String trackingId)
	{
		ComponentTracking componentTrackingExample = new ComponentTracking();
		componentTrackingExample.setComponentId(componentId);
		componentTrackingExample.setComponentTrackingId(trackingId);
		ComponentTracking componentTracking = service.getPersistenceService().queryOneByExample(ComponentTracking.class, componentTrackingExample);
		return sendSingleEnityResponse(componentTracking);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a tracking entry from the specified component")
	@Path("/{id}/tracking/{trackingId}")
	public void deleteComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("id")
			@RequiredParam String trackingId)
	{
		ComponentTracking componentTrackingExample = new ComponentTracking();
		componentTrackingExample.setComponentId(componentId);
		componentTrackingExample.setComponentTrackingId(trackingId);
		ComponentTracking componentTracking = service.getPersistenceService().queryOneByExample(ComponentTracking.class, componentTrackingExample);
		if (componentTracking != null) {
			service.getComponentService().deactivateBaseComponent(ComponentTracking.class, trackingId);
		}
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove all tracking entries from the specified component")
	@Path("/{id}/tracking")
	public void deleteAllComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		service.getComponentService().deleteAllBaseComponent(ComponentTracking.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Delete component and all related entities")
	@Path("/{id}/cascade")
	public void deleteComponentTag(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		service.getComponentService().cascadeDeleteOfComponent(componentId);
	}

	private void checkBaseComponentBelongsToComponent(BaseComponent component, String componentId)
	{
		if (component.getComponentId().equals(componentId) == false) {
			throw new OpenStorefrontRuntimeException("Entity does not belong to component", "Check input.");
		}
	}

	private Response ownerCheck(BaseEntity entity)
	{
		if (SecurityUtil.isCurrentUserTheOwner(entity)
				|| SecurityUtil.isAdminUser()) {
			return null;
		} else {
			return Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN)
					.entity("User cannot modify resource.")
					.build();
		}
	}

}
