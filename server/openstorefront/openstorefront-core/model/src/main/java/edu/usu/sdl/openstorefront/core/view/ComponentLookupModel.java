/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentLookupModel
		extends LookupModel
{

	private String componentType;
	private String componentTypeLabel;

	public static ComponentLookupModel toView(Component component)
	{
		ComponentLookupModel view = new ComponentLookupModel();
		view.setCode(component.getComponentId());
		view.setDescription(component.getName());
		view.setComponentType(component.getComponentType());
		view.setComponentTypeLabel(TranslateUtil.translateComponentType(component.getComponentType()));
		return view;
	}

	public static List<ComponentLookupModel> toView(List<Component> components)
	{
		List<ComponentLookupModel> views = new ArrayList<>();
		components.forEach(c -> {
			views.add(toView(c));
		});
		return views;
	}

	public String getComponentType()
	{
		return componentType;
	}

	public void setComponentType(String componentType)
	{
		this.componentType = componentType;
	}

	public String getComponentTypeLabel()
	{
		return componentTypeLabel;
	}

	public void setComponentTypeLabel(String componentTypeLabel)
	{
		this.componentTypeLabel = componentTypeLabel;
	}

}
