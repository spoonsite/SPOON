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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeNestedModel;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeOptions;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.view.ComponentTypeView;
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
@Path("v1/resource/componenttypes")
@APIDescription("Component types are the definition of the components")
public class ComponentTypeResource
		extends BaseResource
{

	@GET
	@APIDescription("Gets component types")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentTypeView.class)
	public Response getComponentType(
			@QueryParam("status") String status,
			@QueryParam("all") boolean all
	)
	{
		ComponentType componentType = new ComponentType();
		if (status == null && all == false) {
			componentType.setActiveStatus(ComponentType.ACTIVE_STATUS);
		} else if (status != null && all == false) {
			componentType.setActiveStatus(status);
		}

		List<ComponentType> componentTypes = componentType.findByExample();
		componentTypes.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, ComponentType.FIELD_LABEL));
		GenericEntity<List<ComponentTypeView>> entity = new GenericEntity<List<ComponentTypeView>>(ComponentTypeView.toView(componentTypes))
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets nested component types")
	@Produces({
		MediaType.APPLICATION_JSON
	})
	@DataType(ComponentTypeNestedModel.class)
	@Path("/nested")
	public Response getNestedComponentType(
			@QueryParam("componentType") String componentType,
			@QueryParam("pullParents") boolean pullParents
	)
	{
		ComponentTypeOptions componentTypeOptions = new ComponentTypeOptions(componentType);
		componentTypeOptions.setPullParents(pullParents);

		ComponentTypeNestedModel nestedModel = service.getComponentService().getComponentType(componentTypeOptions);
		return sendSingleEntityResponse(nestedModel);
	}

	@GET
	@APIDescription("Gets submission component types")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentType.class)
	@Path("/submission")
	public Response getSubmissionComponentTyped()
	{
		ComponentType componentType = new ComponentType();
		componentType.setActiveStatus(ComponentType.ACTIVE_STATUS);
		componentType.setAllowOnSubmission(Boolean.TRUE);

		List<ComponentType> componentTypes = componentType.findByExample();
		componentTypes.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, ComponentType.FIELD_LABEL));
		GenericEntity<List<ComponentType>> entity = new GenericEntity<List<ComponentType>>(componentTypes)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets component types")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(LookupModel.class)
	@Path("/lookup")
	public Response getComponentTypeLookup(
			@QueryParam("status") String status,
			@QueryParam("all") boolean all
	)
	{
		List<LookupModel> lookups = new ArrayList<>();

		ComponentType componentType = new ComponentType();
		if (status == null && all == false) {
			componentType.setActiveStatus(ComponentType.ACTIVE_STATUS);
		} else if (status != null && all == false) {
			componentType.setActiveStatus(status);
		}

		List<ComponentType> componentTypes = componentType.findByExample();
		componentTypes.forEach(type
				-> {
			LookupModel lookupModel = new LookupModel();
			lookupModel.setCode(type.getComponentType());
			lookupModel.setDescription(type.getLabel());
			lookups.add(lookupModel);
		});
		lookups.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, LookupModel.DESCRIPTION_FIELD));

		GenericEntity<List<LookupModel>> entity = new GenericEntity<List<LookupModel>>(lookups)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets component type")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentType.class)
	@Path("/{type}")
	public Response getComponentTypeById(
			@PathParam("type") String type
	)
	{
		ComponentType componentType = new ComponentType();
		componentType.setComponentType(type);
		return sendSingleEntityResponse(componentType.find());
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TYPES)
	@APIDescription("Adds a new component type")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentType.class)
	public Response createNewComponentType(
			ComponentType componentType
	)
	{
		return handleSaveComponentType(componentType, true);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TYPES)
	@APIDescription("Updates a component type")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@Consumes(
			{
				MediaType.APPLICATION_JSON
			})
	@Path("/{type}")
	public Response updateComponentType(
			@PathParam("type") String type,
			ComponentType componentType
	)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		ComponentType found = new ComponentType();
		found.setComponentType(type);
		found = found.find();
		if (found != null) {
			componentType.setComponentType(type);
			response = handleSaveComponentType(componentType, false);
		}
		return response;
	}

	private Response handleSaveComponentType(ComponentType componentType, boolean post)
	{
		ValidationModel validationModel = new ValidationModel(componentType);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			componentType = service.getComponentService().saveComponentType(componentType);
			if (post) {
				return Response.created(URI.create("v1/resource/componenttypes/" + componentType.getComponentType())).entity(componentType).build();
			} else {
				return sendSingleEntityResponse(componentType);
			}
		}
		return sendSingleEntityResponse(validationResult.toRestError());
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TYPES)
	@APIDescription("Activates a component type")
	@Path("/{type}/activate")
	public Response activateComponentType(
			@PathParam("type") String type
	)
	{
		ComponentType changed = service.getComponentService().activateComponentType(type);
		return sendSingleEntityResponse(changed);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TYPES)
	@APIDescription("Inactivates a component type unless new type is specified then it moves the data and deletes the type")
	@Path("/{type}")
	public void deleteComponentType(
			@PathParam("type") String type,
			@QueryParam("newtype") String newType
	)
	{
		service.getComponentService().removeComponentType(type, newType);
	}

}
