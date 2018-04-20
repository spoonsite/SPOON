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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeRoleResolution;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeTemplateResolution;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeUserResolution;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class ComponentTypeView
		extends ComponentType
{

	private static final long serialVersionUID = 1L;

	private ComponentTypeTemplateResolution template;
	private ComponentTypeUserResolution users;
	private ComponentTypeRoleResolution roles;
	private String parentLabel;

	public ComponentTypeView()
	{
	}

	public static ComponentTypeView toView(ComponentType componentType)
	{
		ComponentTypeView view = new ComponentTypeView();
		try {
			BeanUtils.copyProperties(view, componentType);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}

		Service service = ServiceProxyFactory.getServiceProxy();
		view.setTemplate(service.getComponentService().findTemplateForComponentType(componentType.getComponentType()));
		view.setUsers(service.getComponentService().findUserForComponentType(componentType.getComponentType()));
		view.setRoles(service.getComponentService().findRoleGroupsForComponentType(componentType.getComponentType()));
		view.setParentLabel(service.getComponentService().getComponentTypeParentsString(componentType.getComponentType(), true));
		return view;
	}

	public static List<ComponentTypeView> toView(List<ComponentType> componentTypes)
	{
		List<ComponentTypeView> views = new ArrayList<>();
		componentTypes.forEach(componentType -> {
			views.add(ComponentTypeView.toView(componentType));
		});
		return views;
	}

	public String getParentLabel()
	{
		return parentLabel;
	}

	public void setParentLabel(String parentLabel)
	{
		this.parentLabel = parentLabel;
	}

	public ComponentTypeTemplateResolution getTemplate()
	{
		return template;
	}

	public void setTemplate(ComponentTypeTemplateResolution template)
	{
		this.template = template;
	}

	public ComponentTypeUserResolution getUsers()
	{
		return users;
	}

	public void setUsers(ComponentTypeUserResolution users)
	{
		this.users = users;
	}

	public ComponentTypeRoleResolution getRoles()
	{
		return roles;
	}

	public void setRoles(ComponentTypeRoleResolution roles)
	{
		this.roles = roles;
	}

}
