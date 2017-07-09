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
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.view.ComponentAttributeView;
import edu.usu.sdl.openstorefront.core.view.ComponentFilterParams;
import edu.usu.sdl.openstorefront.core.view.ComponentSimpleAttributeView;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
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
	@DataType(ComponentSimpleAttributeView.class)	
	public List<ComponentSimpleAttributeView> getComponentsWithAttributeCode(
			@QueryParam("attributeType")
			String type,
			@QueryParam("attributeCode")
			String code,
			ComponentFilterParams filterParams)
	{
		List<ComponentSimpleAttributeView> components = new ArrayList<>();

		ComponentAttribute componentAttributeExample = new ComponentAttribute();
		ComponentAttributePk componentAttributePk = new ComponentAttributePk();
		
		componentAttributePk.setAttributeType(type);
		componentAttributePk.setAttributeCode(code);
		componentAttributeExample.setActiveStatus(AttributeCode.ACTIVE_STATUS);
		componentAttributeExample.setComponentAttributePk(componentAttributePk);
		List<ComponentAttribute> attributeComponents = service.getPersistenceService().queryByExample(new QueryByExample(componentAttributeExample));
		
		for (ComponentAttribute attributeComponent : attributeComponents) {
			ComponentSimpleAttributeView view = new ComponentSimpleAttributeView();
			view.setComponentId(code);
			view.setName(service.getComponentService().getComponentName(attributeComponent.getComponentId()));
			//view.setComponentType(service.getComponentService().get);
			
			
			components.add(view);
		}
		components = components.stream().filter(c->{
			boolean keep = true;
			if (StringUtils.isNotBlank(filterParams.getComponentName())) {
				if (!c.getName().contains(filterParams.getComponentName())) {
					keep = false;
				}
			}
			if (keep) {
				if (StringUtils.isNotBlank(filterParams.getComponentType())) {
					if (!c.getComponentType().equals(filterParams.getComponentType())) {
						keep = false;
					}
				}
			}
			
			return keep;
		}).collect(Collectors.toList());
				
		components = filterParams.filter(components);
		
		for (ComponentSimpleAttributeView view : components) {			
			List<ComponentAttribute> attributes = service.getComponentService().getAttributesByComponentId(view.getComponentId());
			List<ComponentAttributeView> attributeView = ComponentAttributeView.toViewList(attributes);
			view.setAttributes(attributeView);
		}
		
		return components;
	}

	//post assign
//	@POST
//	@APIDescription("Assign Attribute to components")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/{attributeType}/{attributeCode}")
//	public 
//	
	//remove attributes
//	@DELETE
	
	
}
