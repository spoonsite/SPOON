/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.rest.resource;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.view.ComponentFilterParams;
import edu.usu.sdl.openstorefront.core.view.ComponentSimpleAttributeView;
import edu.usu.sdl.openstorefront.core.view.ComponentSimpleWrapper;
import edu.usu.sdl.openstorefront.core.view.MultipleIds;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Component Attributes")
@Path("v1/resource/componentattributes")
public class ComponentAttributeResource
		extends BaseResource
{

	@GET
	@APIDescription("Get the components which contain a specified attribute type and code")
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(ComponentSimpleWrapper.class)
	public Response getComponentsWithAttributeCode(
			@QueryParam("attributeType") String type,
			@QueryParam("attributeCode") String code,
			@BeanParam ComponentFilterParams filterParams)
	{
		ValidationResult validationResult = filterParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentSimpleWrapper simpleWrapper = new ComponentSimpleWrapper();

		List<ComponentSimpleAttributeView> components = new ArrayList<>();

		ComponentAttribute componentAttributeExample = new ComponentAttribute();
		ComponentAttributePk componentAttributePk = new ComponentAttributePk();
		componentAttributePk.setAttributeType(type);
		componentAttributePk.setAttributeCode(code);
		componentAttributeExample.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
		componentAttributeExample.setComponentAttributePk(componentAttributePk);

		List<ComponentAttribute> attributeComponents = componentAttributeExample.findByExample();
		attributeComponents = filterEngine.filter(attributeComponents, true);

		for (ComponentAttribute attributeComponent : attributeComponents) {
			ComponentSimpleAttributeView view = new ComponentSimpleAttributeView();
			view.setComponentId(attributeComponent.getComponentId());
			view.setName(service.getComponentService().getComponentName(attributeComponent.getComponentId()));
			view.setComponentType(service.getComponentService().getComponentTypeForComponent(attributeComponent.getComponentId()));
			components.add(view);
		}
		int totalResults = 0;
		components = components.stream().filter(c -> {
			boolean keep = true;
			if (StringUtils.isNotBlank(filterParams.getComponentName())) {
				if (!c.getName().contains(filterParams.getComponentName())) {
					keep = false;
				}
			}
			if (keep) {
				if (StringUtils.isNotBlank(filterParams.getComponentType())
						&& !ComponentType.ALL.equals(filterParams.getComponentType())) {
					if (!c.getComponentType().equals(filterParams.getComponentType())) {
						keep = false;
					}
				}
			}

			return keep;
		}).collect(Collectors.toList());
		totalResults = components.size();
		components = filterParams.filter(components);

//		for (ComponentSimpleAttributeView view : components) {
//			List<ComponentAttribute> attributes = service.getComponentService().getAttributesByComponentId(view.getComponentId());
//			List<ComponentAttributeView> attributeView = ComponentAttributeView.toViewList(attributes);
//			view.setAttributes(attributeView);
//		}
		simpleWrapper.setData(components);
		simpleWrapper.setResults(components.size());
		simpleWrapper.setTotalNumber(totalResults);

		return sendSingleEntityResponse(simpleWrapper);
	}

	@POST
	@APIDescription("Assign attribute to components")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{attributeType}/{attributeCode}")
	@DataType(ComponentAttribute.class)
	public Response assignAttribute(
			@PathParam("attributeType") String type,
			@PathParam("attributeCode") String code,
			MultipleIds ids
	)
	{
		List<ComponentAttribute> attributes = new ArrayList<>();
		for (String componentId : ids.getIds()) {

			ComponentAttributePk componentAttributePk = new ComponentAttributePk();
			componentAttributePk.setComponentId(componentId);
			componentAttributePk.setAttributeCode(code);
			componentAttributePk.setAttributeType(type);

			ComponentAttribute attribute = new ComponentAttribute();
			attribute.setComponentAttributePk(componentAttributePk);
			attribute.setComponentId(componentId);

			service.getComponentService().saveComponentAttribute(attribute);
			attributes.add(attribute);
		}
		GenericEntity<List<ComponentAttribute>> entity = new GenericEntity<List<ComponentAttribute>>(attributes)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@DELETE
	@APIDescription("Removes Attribute from components")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{attributeType}/{attributeCode}")
	public void removeAttribute(
			@PathParam("attributeType") String type,
			@PathParam("attributeCode") String code,
			MultipleIds ids
	)
	{
		for (String componentId : ids.getIds()) {

			ComponentAttributePk componentAttributePk = new ComponentAttributePk();
			componentAttributePk.setComponentId(componentId);
			componentAttributePk.setAttributeCode(code);
			componentAttributePk.setAttributeType(type);
			service.getComponentService().deleteBaseComponent(ComponentAttribute.class, componentAttributePk);
		}
	}

}
