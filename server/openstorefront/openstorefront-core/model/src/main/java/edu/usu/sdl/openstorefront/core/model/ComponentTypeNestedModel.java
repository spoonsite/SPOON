/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.model;

import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.view.ComponentTypeView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds the hierarchy of component types.
 *
 * @author dshurtleff
 */
public class ComponentTypeNestedModel
		implements Serializable
{

	private static final long serialVersionUID = 1L;

	private ComponentTypeView componentType;
	private List<ComponentTypeNestedModel> children = new ArrayList<>();

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ComponentTypeNestedModel()
	{
	}

	public List<String> getComponentTypeChildren()
	{
		return findChildrenTypes(this, new ArrayList<>());
	}

	private List<String> findChildrenTypes(ComponentTypeNestedModel nestedModel, List<String> childrenTypes)
	{
		if (nestedModel != null) {
			if (nestedModel.getComponentType() != null) {
				childrenTypes.add(nestedModel.getComponentType().getComponentType());
			}
		} else {
			return new ArrayList<>();
		}

		List<ComponentTypeNestedModel> childrenLocal = nestedModel.getChildren();
		if (childrenLocal.isEmpty()) {
			return childrenTypes;
		}

		for (ComponentTypeNestedModel child : childrenLocal) {
			childrenTypes.addAll(findChildrenTypes(child, new ArrayList<>()));
		}

		return childrenTypes;
	}

	/**
	 * This will put on the parents and the requested componentType Keep mind
	 * the order is not maintained. The map would just contain the component
	 * type and it's ancestors.
	 *
	 * @param nestedModel
	 * @param componentTypeId
	 * @return
	 */
	public Map<String, ComponentType> findParents(ComponentTypeNestedModel nestedModel, String componentTypeId)
	{
		Map<String, ComponentType> componentMap = new HashMap<>();

		if (nestedModel.getComponentType() != null
				&& nestedModel.getComponentType().getComponentType().equals(componentTypeId)) {
			componentMap.put(nestedModel.getComponentType().getComponentType(), nestedModel.getComponentType());
		} else {
			boolean found = false;

			if (nestedModel.getComponentType() != null) {
				//put on parent
				componentMap.put(nestedModel.getComponentType().getComponentType(), nestedModel.getComponentType());
			}
			for (ComponentTypeNestedModel child : nestedModel.getChildren()) {
				if (child.getComponentType() != null
						&& child.getComponentType().getComponentType().equals(componentTypeId)) {
					componentMap.put(child.getComponentType().getComponentType(), child.getComponentType());
					found = true;
					break;
				}
			}
			if (!found) {
				//check children
				for (ComponentTypeNestedModel child : nestedModel.getChildren()) {
					//make sure it is a child
					if (childOfType(child, componentTypeId)) {
						Map<String, ComponentType> childMap = findParents(child, componentTypeId);
						componentMap.putAll(childMap);
					}
				}
			}
		}

		return componentMap;
	}

	private boolean childOfType(ComponentTypeNestedModel parent, String componentTypeId)
	{
		boolean childFound = false;

		for (ComponentTypeNestedModel child : parent.getChildren()) {
			if (child.getComponentType().getComponentType().equals(componentTypeId)) {
				childFound = true;
			} else {
				if (childOfType(child, componentTypeId)) {
					childFound = true;
				}
			}
		}

		return childFound;
	}

	@Override
	public String toString()
	{
		return addChildTypeLabels(this);
	}

	private String addChildTypeLabels(ComponentTypeNestedModel nestedModel)
	{
		if (nestedModel.getChildren().isEmpty()) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		String root = "root";
		if (nestedModel.getComponentType() != null) {
			root = nestedModel.getComponentType().getLabel();
		}

		for (ComponentTypeNestedModel child : nestedModel.getChildren()) {
			sb.append(root).append("<-");
			sb.append(child.getComponentType().getLabel());
			sb.append("\n");
		}
		for (ComponentTypeNestedModel child : nestedModel.getChildren()) {
			sb.append(addChildTypeLabels(child));
		}

		return sb.toString();
	}

	public ComponentTypeView getComponentType()
	{
		return componentType;
	}

	public void setComponentType(ComponentTypeView componentType)
	{
		this.componentType = componentType;
	}

	public List<ComponentTypeNestedModel> getChildren()
	{
		return children;
	}

	public void setChildren(List<ComponentTypeNestedModel> children)
	{
		this.children = children;
	}

}
