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
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
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
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentDetailView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentReviewView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentSearchView;
import edu.usu.sdl.openstorefront.web.rest.model.RequiredForComponent;
import edu.usu.sdl.openstorefront.web.viewmodel.RestErrorModel;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
	@DataType(Component.class)
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

			//TODO:  What to do if the id don't match any component....is that an error or just return the ones it can find.
		});

		return componentViews;
	}

	@GET
	@APIDescription("Gets a component <br>(Note: this only the top level component object)")
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
	@APIDescription("Create a component")
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
	@APIDescription("Update a component")
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
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentAttribute.class)
	@Path("/{id}/attribute")
	public List<ComponentAttribute> getComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentAttribute.class, componentId);
	}

	@GET
	@APIDescription("Get the code for a specified attribute type of an entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(AttributeCode.class)
	@Path("/{id}/attribute/{attributeType}")
	public List<AttributeCode> getComponentAttribute(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("attributeType")
			@RequiredParam String attributeType)
	{
		List<ComponentAttribute> attributes = service.getComponentService().getBaseComponent(ComponentAttribute.class, componentId);
		List<AttributeCode> attributeCodes = new ArrayList<>();
		for (Iterator<ComponentAttribute> iter = attributes.listIterator(); iter.hasNext();) {
			ComponentAttribute a = iter.next();
			if (!a.getComponentAttributePk().getAttributeType().equals(attributeType)) {
				iter.remove();
			} else {
				attributeCodes.add(new AttributeCode());
				// TODO: Implement getAttributeCode
				//attributeCodes.add(service.getAttributeService().getAttributeCode(a.getComponentAttributePk().getAttributeCode()));
			}
		}
		return attributeCodes;
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove an attribute from the entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/attribute")
	public void deleteComponentAttributes(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("attributeType")
			@RequiredParam String attributeType,
			@PathParam("attributeCode")
			@RequiredParam String attributeCode)
	{
		ComponentAttribute attribute = new ComponentAttribute();
		attribute.setComponentId(componentId);
		service.getPersistenceService().deleteByExample(attribute);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove an attribute from the entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/attribute/{attributeType}/{attributeCode}")
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
	@Path("/{id}/attribute")
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
		return Response.created(URI.create(attribute.getComponentAttributePk().getAttributeType() + attribute.getComponentAttributePk().getAttributeCode())).entity(attribute).build();
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
	@APIDescription("Remove a dependency from the entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/dependency/{dependencyId}")
	public void deleteComponentDependency(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("dependencyId")
			@RequiredParam String dependencyId)
	{
		//TODO:  Validate that the contact belongs to the component that the are try to delete
		service.getComponentService().deactivateBaseComponent(ComponentExternalDependency.class, dependencyId);
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
		dependency.setComponentId(componentId);
		dependency.setDependencyId(dependencyId);
		return saveDependency(dependency, false);
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
			return Response.created(URI.create(dependency.getDependencyId())).entity(dependency).build();
		} else {
			return Response.ok(dependency).build();
		}
	}

	// ComponentRESTResource CONTACT section
	@GET
	@APIDescription("Remove a contact from the entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentContact.class)
	@Path("/{id}/contact")
	public List<ComponentContact> getComponentContact(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentContact.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a contact from the entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/contact/{contactId}")
	public void deleteComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("contactId")
			@RequiredParam String contactId)
	{
		//TODO:  Validate that the contact belongs to the component that the are try to delete

		service.getComponentService().deactivateBaseComponent(ComponentContact.class, contactId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a contact to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentContact.class)
	@Path("/{id}/contact")
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
	@APIDescription("Update a contact associated to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/contact/{contactId}")
	public Response updateComponentContact(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("contactId")
			@RequiredParam String contactId,
			ComponentContact contact)
	{
		contact.setComponentId(componentId);
		contact.setContactId(contactId);
		return saveContact(contact, false);
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
			return Response.created(URI.create(contact.getContactId())).entity(contact).build();
		} else {
			return Response.ok(contact).build();
		}
	}

	// ComponentRESTResource Evaluation Section section
	@GET
	@APIDescription("Gets an evaluation section associated to the entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentEvaluationSection.class)
	@Path("/{id}/section")
	public List<ComponentEvaluationSection> getComponentEvaluationSection(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentEvaluationSection.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Removes an evaluation section from the entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/section/{evalSection}")
	public void deleteComponentEvaluationSection(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("evalSection")
			@RequiredParam String evalSection)
	{
		ComponentEvaluationSectionPk pk = new ComponentEvaluationSectionPk();
		pk.setComponentId(componentId);
		pk.setEvaulationSection(evalSection);
		service.getComponentService().deactivateBaseComponent(ComponentEvaluationSection.class, (Object) pk);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Removes an evaluation section from the entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/section/all")
	public void deleteAllComponentEvaluationSections(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		service.getComponentService().deleteBaseComponent(ComponentEvaluationSection.class, componentId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add an evaluation section to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentEvaluationSection.class)
	@Path("/{id}/section")
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
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/section/{evalSection}")
	public Response updateComponentEvaluationSection(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("evalSection")
			@RequiredParam String evalSection,
			@RequiredParam ComponentEvaluationSection section)
	{
		section.setComponentId(componentId);
		section.getComponentEvaluationSectionPk().setComponentId(componentId);
		section.getComponentEvaluationSectionPk().setEvaulationSection(evalSection);
		return saveSection(section, false);
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
			// TODO: How does this work with composite keys?
			return Response.created(URI.create(section.getComponentEvaluationSectionPk().getEvaulationSection())).entity(section).build();
		} else {
			return Response.ok(section).build();
		}
	}

	// ComponentRESTResource Evaluation Schedule section
	@GET
	@APIDescription("Gets a list of evaluation schedules associated to the entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentEvaluationSchedule.class)
	@Path("/{id}/schedule")
	public List<ComponentEvaluationSchedule> getComponentEvaluationSchedule(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentEvaluationSchedule.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Removes the specified evaluation schedule from the entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/schedule/{evalLevel}")
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
	@APIDescription("Adds a component evaluation schedule to the entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentEvaluationSchedule.class)
	@Path("/{id}/schedule")
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
	@Path("/{id}/schedule/{evalLevel}")
	public Response updateComponentEvaluationSchedule(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("evalLevel")
			@RequiredParam String evalLevel,
			@RequiredParam ComponentEvaluationSchedule schedule)
	{
		schedule.setComponentId(componentId);
		schedule.getComponentEvaluationSchedulePk().setComponentId(componentId);
		schedule.getComponentEvaluationSchedulePk().setEvaluationLevelCode(evalLevel);
		return saveSchedule(schedule, false);
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
			// TODO: How does this work with composite keys?
			return Response.created(URI.create(schedule.getComponentEvaluationSchedulePk().getEvaluationLevelCode())).entity(schedule).build();
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
		service.getComponentService().deactivateBaseComponent(ComponentMedia.class, mediaId);
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
		media.setComponentId(componentId);
		media.setComponentMediaId(mediaId);
		return saveMedia(media, false);
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
			return Response.created(URI.create("v1/resource/components/{id}/media/" + media.getComponentMediaId())).entity(media).build();
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
	@APIDescription("Removes metadata from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/metadata/{metadataId}")
	public void deleteComponentMetadata(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("metadataId")
			@RequiredParam String metadataId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentMetadata.class, metadataId);
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
		metadata.setMetadataId(metadataId);
		metadata.setComponentId(componentId);
		return saveMetadata(metadata, false);
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
			// TODO: How does this work with composite keys?
			return Response.created(URI.create(metadata.getMetadataId())).build();
		} else {
			return Response.ok(metadata).build();
		}
	}

	// ComponentRESTResource QUESTION section
	@GET
	@APIDescription("Get the questions associated with the specified entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentQuestion.class)
	@Path("/{id}/question")
	public List<ComponentQuestion> getComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentQuestion.class, componentId);
	}

	// ComponentRESTResource QUESTION section
	@GET
	@APIDescription("Get the questions associated with the specified entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentQuestion.class)
	@Path("/{id}/question/{questionId}/responses")
	public List<ComponentQuestionResponse> getComponentQuestionResponses(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId)
	{
		List<ComponentQuestionResponse> responses = service.getComponentService().getBaseComponent(ComponentQuestionResponse.class, componentId);
		for (Iterator<ComponentQuestionResponse> iter = responses.listIterator(); iter.hasNext();) {
			ComponentQuestionResponse a = iter.next();
			if (!a.getQuestionId().equals(questionId)) {
				iter.remove();
			}
		}
		return responses;
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a question from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/question/{questionId}")
	public void deleteComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentQuestion.class, questionId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a new question to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentQuestion.class)
	@Path("/{id}/question")
	public Response addComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentQuestion question)
	{
		question.setComponentId(componentId);
		return saveQuestion(question, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Update a question associated with the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/question/{questionId}")
	public Response updateComponentQuestion(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("questionId")
			@RequiredParam String questionId,
			@RequiredParam ComponentQuestion question)
	{
		question.setComponentId(componentId);
		question.setQuestionId(questionId);
		return saveQuestion(question, false);
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
			// TODO: How does this work with composite keys?
			return Response.created(URI.create(question.getQuestionId())).entity(question).build();
		} else {
			return Response.ok(question).build();
		}
	}

	// ComponentRESTResource QUESTION RESPONSE section
	@GET
	@APIDescription("Gets the responses for a given question associated to the specified entity ")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentQuestionResponse.class)
	@Path("/{id}/response")
	public List<ComponentQuestionResponse> getComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam String questionId)
	{
		return service.getComponentService().getBaseComponent(ComponentQuestionResponse.class, questionId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a response from the given question on the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/response/{responseId}")
	public void deleteComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("responseId")
			@RequiredParam String responseId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentQuestionResponse.class, responseId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a response to the given question on the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentQuestionResponse.class)
	@Path("/{id}/response/{questionId}")
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
	@RequireAdmin
	@APIDescription("Gets full component details (This the packed view for displaying)")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/response/{responseId}")
	public Response updateComponentQuestionResponse(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("responseId")
			@RequiredParam String responseId,
			@RequiredParam ComponentQuestionResponse response)
	{
		response.setComponentId(componentId);
		response.setResponseId(responseId);
		return saveQuestionResponse(response, false);
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
			// TODO: How does this work with composite keys?
			return Response.created(URI.create(response.getResponseId())).entity(response).build();
		} else {
			return Response.ok(response).build();
		}
	}

	// ComponentRESTResource RESOURCE section
	@GET
	@APIDescription("Get the resources associated to the given entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentResource.class)
	@Path("/{id}/resource")
	public List<ComponentResource> getComponentResource(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentResource.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a given resource from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/resource/{resourceId}")
	public void deleteComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("resourceId")
			@RequiredParam String resourceId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentResource.class, resourceId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a resource to the given entity.  Use a form to POST Resource.action?UploadResource to upload file.  "
			+ "To upload: pass the componentResource.resourceType...etc and 'file'.")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentRESTResource.class)
	@Path("/{id}/resource")
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
	@Path("/{id}/resource/{resourceId}")
	public Response updateComponentResource(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("resourceId")
			@RequiredParam String resourceId,
			@RequiredParam ComponentResource resource)
	{
		resource.setComponentId(componentId);
		resource.setResourceId(resourceId);
		return saveResource(resource, false);
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
			// TODO: How does this work with composite keys?
			return Response.created(URI.create(resource.getResourceId())).entity(resource).build();
		} else {
			return Response.ok(resource).build();
		}
	}

	// ComponentRESTResource REVIEW section
	@GET
	@APIDescription("Get the reviews for a specified entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentReview.class)
	@Path("/{id}/review")
	public List<ComponentReview> getComponentReview(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentReview.class, componentId);
	}

	@GET
	@APIDescription("Get the reviews for a specified entity")
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
	@RequireAdmin
	@APIDescription("Remove a review from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/review/{reviewId}")
	public void deleteComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentReview.class, reviewId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a review to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReview.class)
	@Path("/{id}/review")
	public Response addComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentReview review)
	{
		review.setComponentId(componentId);
		return saveReview(review, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Update a review associated with the given entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/review/{reviewId}")
	public Response updateComponentReview(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@RequiredParam ComponentReview review)
	{
		review.setComponentId(componentId);
		review.setComponentReviewId(reviewId);
		return saveReview(review, false);
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
			// TODO: How does this work with composite keys?
			return Response.created(URI.create(review.getComponentReviewId())).entity(review).build();
		} else {
			return Response.ok(review).build();
		}
	}

	// ComponentRESTResource REVIEW CON section
	@GET
	@APIDescription("Get the cons associated to a review on the specified entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentReviewCon.class)
	@Path("/{id}/con")
	public List<ComponentReviewCon> getComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentReviewCon.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a con from the given review accociated with the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/review/{reviewId}/con")
	public void deleteComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@RequiredParam String con)
	{
		ComponentReviewCon example = new ComponentReviewCon();
		ComponentReviewConPk pk = new ComponentReviewConPk();
		pk.setComponentReviewId(reviewId);
//		pk.setReviewCon(con);
		example.setComponentReviewConPk(pk);
		service.getPersistenceService().deleteByExample(example);
		service.getComponentService().deactivateBaseComponent(ComponentReviewCon.class, pk);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a con to the given review associated with the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReviewCon.class)
	@Path("/{id}/review/{reviewId}/con")
	public Response addComponentReviewCon(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@RequiredParam String text)
	{
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
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		// Again, how are we going to handle composite keys?
		return Response.created(URI.create(con.getComponentReviewConPk().getComponentReviewId())).entity(con).build();
	}

	// ComponentRESTResource REVIEW PRO section
	@GET
	@APIDescription("Get the pros for a review associated with the given entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentReviewPro.class)
	@Path("/{id}/pro")
	public List<ComponentReviewPro> getComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentReviewPro.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a pro from the review associated with a specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/review/{reviewId}/pro")
	public void deleteComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@RequiredParam String pro)
	{
		ComponentReviewPro example = new ComponentReviewPro();
		ComponentReviewProPk pk = new ComponentReviewProPk();
		pk.setComponentReviewId(reviewId);
//		pk.setReviewPro(pro);
		example.setComponentReviewProPk(pk);
		service.getPersistenceService().deleteByExample(example);
		service.getComponentService().deactivateBaseComponent(ComponentReviewPro.class, pk);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add a pro to the review associated with the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentReviewPro.class)
	@Path("/{id}/review/{reviewId}/pro")
	public Response addComponentReviewPro(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("reviewId")
			@RequiredParam String reviewId,
			@RequiredParam String text)
	{
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
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		// Again, how are we going to handle composite keys?
		return Response.created(URI.create(pro.getComponentReviewProPk().getComponentReviewId())).entity(pro).build();
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
	@APIDescription("Get the tag list for a specified entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTag.class)
	@Path("/{id}/tags")
	public List<ComponentTag> getComponentTag(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentTag.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a tag from the specified entity")
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
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a tag from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/tag/{tagId}")
	public void deleteComponentTag(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("tagId")
			@RequiredParam String tagId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentTag.class, tagId);
	}

	@POST
	@RequireAdmin
	@APIDescription("Add tags to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentTag.class)
	@Path("/{id}/tags")
	public Response addComponentTags(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam List<ComponentTag> tags)
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
					return Response.created(URI.create(verified.get(0).getTagId())).entity(entity).build();
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
	@RequireAdmin
	@APIDescription("Add a tag to the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentTag.class)
	@Path("/{id}/tag")
	public Response addComponentTag(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentTag tag)
	{
		tag.setActiveStatus(ComponentTag.ACTIVE_STATUS);
		tag.setComponentId(componentId);
		ValidationModel validationModel = new ValidationModel(tag);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			tag.setCreateUser(SecurityUtil.getCurrentUserName());
			tag.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentTag(tag);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		return Response.created(URI.create(tag.getTagId())).entity(tag).build();
	}

	// ComponentRESTResource TRACKING section
	@GET
	@RequireAdmin
	@APIDescription("Get the list of tracking details on a specified entity")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTracking.class)
	@Path("/{id}/tracking")
	public List<ComponentTracking> getComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		return service.getComponentService().getBaseComponent(ComponentTracking.class, componentId);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a tracking entry from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/tracking/{trackingId}")
	public void deleteComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("id")
			@RequiredParam String trackingId)
	{
		service.getComponentService().deactivateBaseComponent(ComponentTracking.class, trackingId);
	}

	@POST
	@APIDescription("Add a tracking entry for the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@DataType(ComponentTracking.class)
	@Path("/{id}/tracking")
	public Response addComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam ComponentTracking tracking)
	{
		tracking.setComponentId(componentId);
		return saveTracking(tracking, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Update a tracking entry for the specified entity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/tracking/{trackingId}")
	public Response updateComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("trackingId")
			@RequiredParam String trackingId,
			@RequiredParam ComponentTracking tracking)
	{
		tracking.setComponentTrackingId(trackingId);
		tracking.setComponentId(componentId);
		return saveTracking(tracking, false);
	}

	private Response saveTracking(ComponentTracking tracking, Boolean post)
	{
		ValidationModel validationModel = new ValidationModel(tracking);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			tracking.setActiveStatus(ComponentTracking.ACTIVE_STATUS);
			tracking.setCreateUser(SecurityUtil.getCurrentUserName());
			tracking.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getComponentService().saveComponentTracking(tracking);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			// TODO: How does this work with composite keys?
			return Response.created(URI.create(tracking.getComponentTrackingId())).entity(tracking).build();
		} else {
			return Response.ok(tracking).build();
		}
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
}
