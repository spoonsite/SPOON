/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeTemplate;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
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

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/componenttypetemplates")
@APIDescription("Component type templates define the view for component")
public class ComponentTypeTemplateResource
		extends BaseResource
{

	@GET
	@APIDescription("Gets component type templates")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTypeTemplate.class)
	public Response getComponentTypeTemplate(
			@QueryParam("status") String status,
			@QueryParam("all") boolean all
	)
	{
		ComponentTypeTemplate componentTypeTemplate = new ComponentTypeTemplate();
		if (status == null && all == false) {
			componentTypeTemplate.setActiveStatus(ComponentTypeTemplate.ACTIVE_STATUS);
		} else if (status != null && all == false) {
			componentTypeTemplate.setActiveStatus(status);
		}

		List<ComponentTypeTemplate> componentTypeTemplates = componentTypeTemplate.findByExample();
		GenericEntity<List<ComponentTypeTemplate>> entity = new GenericEntity<List<ComponentTypeTemplate>>(componentTypeTemplates)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets component type templates")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTypeTemplate.class)
	@Path("/lookup")
	public Response getTemplateLookup(
			@QueryParam("status") String status,
			@QueryParam("all") boolean all
	)
	{
		List<LookupModel> lookups = new ArrayList<>();

		ComponentTypeTemplate componentTypeTemplate = new ComponentTypeTemplate();
		if (status == null && all == false) {
			componentTypeTemplate.setActiveStatus(ComponentTypeTemplate.ACTIVE_STATUS);
		} else if (status != null && all == false) {
			componentTypeTemplate.setActiveStatus(status);
		}

		List<ComponentTypeTemplate> componentTypeTemplates = componentTypeTemplate.findByExample();

		componentTypeTemplates.forEach(template -> {
			LookupModel lookupModel = new LookupModel();
			lookupModel.setCode(template.getTemplateId());
			lookupModel.setDescription(template.getName());
			lookups.add(lookupModel);
		});

		GenericEntity<List<LookupModel>> entity = new GenericEntity<List<LookupModel>>(lookups)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets a component type template")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentTypeTemplate.class)
	@Path("/{templateId}")
	public Response getComponentTypeTemplateById(
			@PathParam("templateId") String templateCode
	)
	{
		ComponentTypeTemplate componentType = new ComponentTypeTemplate();
		componentType.setTemplateId(templateCode);
		return sendSingleEntityResponse(componentType.find());
	}
	
	@GET
	@APIDescription("Gets any attached component types")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentType.class)
	@Path("/{templateId}/attached")
	public Response getAttachedComponeTypes(
			@PathParam("templateId") String templateId
	)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		
		ComponentTypeTemplate componentTypeTemplate = new ComponentTypeTemplate();
		componentTypeTemplate.setTemplateId(templateId);
		componentTypeTemplate = componentTypeTemplate.find();
		if (componentTypeTemplate != null) {
			ComponentType componentType = new ComponentType();
			componentType.setComponentTypeTemplate(templateId);
			List<ComponentType> types = componentType.findByExample();
			GenericEntity<List<ComponentType>> entity = new GenericEntity<List<ComponentType>>(types)
			{
			};
			response = sendSingleEntityResponse(entity);
		}
		return response;
	}	

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TEMPLATES)
	@APIDescription("Adds a new component type")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response createNewComponentTypeTemplate(
			ComponentTypeTemplate componentType
	)
	{
		return handleSaveComponentTypeTemplate(componentType, true);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TEMPLATES)
	@APIDescription("Updates a component type")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{templateId}")
	public Response updateComponentTypeTemplate(
			@PathParam("templateId") String templateId,
			ComponentTypeTemplate template
	)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		ComponentTypeTemplate found = new ComponentTypeTemplate();
		found.setTemplateId(templateId);
		found = found.find();
		if (found != null) {
			template.setTemplateId(templateId);
			response = handleSaveComponentTypeTemplate(template, false);
		}
		return response;
	}

	private Response handleSaveComponentTypeTemplate(ComponentTypeTemplate componentTypeTemplate, boolean post)
	{
		ValidationModel validationModel = new ValidationModel(componentTypeTemplate);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			componentTypeTemplate = service.getComponentService().saveComponentTemplate(componentTypeTemplate);
			if (post) {
				return Response.created(URI.create("v1/resource/componenttypetemplates/" + componentTypeTemplate.getTemplateId())).entity(componentTypeTemplate).build();
			} else {
				return sendSingleEntityResponse(componentTypeTemplate);
			}
		}
		return sendSingleEntityResponse(validationResult.toRestError());
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TEMPLATES)
	@APIDescription("Activates a component type template")
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{templateId}/activate")
	public Response activateComponentTypeTemplate(
			@PathParam("templateId") String templateId
	)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		ComponentTypeTemplate found = new ComponentTypeTemplate();
		found.setTemplateId(templateId);
		found = found.find();
		if (found != null) {
			service.getPersistenceService().setStatusOnEntity(ComponentTypeTemplate.class, templateId, StandardEntity.ACTIVE_STATUS);
			found.setActiveStatus(StandardEntity.ACTIVE_STATUS);
			response = sendSingleEntityResponse(found);
		}
		return response;
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TEMPLATES)
	@APIDescription("Inactivates a component type template")
	@Path("/{templateId}")
	public void inactiveTemplate(
			@PathParam("templateId") String templateId
	)
	{
		service.getComponentService().removeComponentTypeTemplate(templateId);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TEMPLATES)
	@APIDescription("Delete component type template; if not attached")
	@Path("/{templateId}/force")
	public void deleteTemplate(
			@PathParam("templateId") String templateId
	)
	{
		service.getComponentService().deleteComponentTypeTemplate(templateId);
	}
	
	
}
