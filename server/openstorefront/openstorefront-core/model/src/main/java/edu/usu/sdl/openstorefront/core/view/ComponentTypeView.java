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
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeTemplate;
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
	
	private String templateName;

	public ComponentTypeView()
	{
	}

	public static ComponentTypeView toTemplateView(ComponentType componentType)
	{
		ComponentTypeView view = new ComponentTypeView();
		try {
			BeanUtils.copyProperties(view, componentType);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}		
		if (componentType.getComponentTypeTemplate()!= null) {
			ComponentTypeTemplate template = new ComponentTypeTemplate();
			template.setTemplateId(componentType.getComponentTypeTemplate());
			template = template.find();
			if (template != null) {
				view.setTemplateName(template.getName());
			}
		}
		return view;
	}

	public static List<ComponentTypeView> toTemplateView(List<ComponentType> componentTypes)
	{
		List<ComponentTypeView> views = new ArrayList<>();
		componentTypes.forEach(componentType -> {
			views.add(toTemplateView(componentType));
		});
		return views;
	}	
	
	public String getTemplateName()
	{
		return templateName;
	}

	public void setTemplateName(String templateName)
	{
		this.templateName = templateName;
	}
		
}
