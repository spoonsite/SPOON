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

import edu.usu.sdl.openstorefront.core.view.ComponentTypeView;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the hierarchy of component types.
 *
 * @author dshurtleff
 */
public class ComponentTypeNestedModel
{

	private ComponentTypeView componentType;
	private List<ComponentTypeNestedModel> children = new ArrayList<>();

	public ComponentTypeNestedModel()
	{
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
			sb.append(root).append("->");
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
