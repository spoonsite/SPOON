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
package edu.usu.sdl.openstorefront.web.rest.model;

import edu.usu.sdl.openstorefront.storage.model.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author dshurtleff
 */
public class ComponentRelationshipView
{

	private String componentId;
	private String name;
	private Date updateDts;

	public ComponentRelationshipView()
	{
	}

	public static ComponentRelationshipView toView(Component component)
	{
		Objects.requireNonNull(component, "Component Required");
		ComponentRelationshipView relationshipView = new ComponentRelationshipView();
		relationshipView.setComponentId(component.getComponentId());
		relationshipView.setName(component.getName());
		return relationshipView;
	}

	public static List<ComponentRelationshipView> toViewList(List<Component> components)
	{
		List<ComponentRelationshipView> views = new ArrayList<>();
		components.forEach(component -> {
			views.add(toView(component));
		});
		return views;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the updateDts
	 */
	public Date getUpdateDts()
	{
		return updateDts;
	}

	/**
	 * @param updateDts the updateDts to set
	 */
	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}

}
