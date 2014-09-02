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

import edu.usu.sdl.openstorefront.doc.ConsumeField;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jlaw
 */
public class RequiredForComponent
{

	@NotNull
	@ConsumeField
	private Component component;

	@NotNull
	@ConsumeField
	@DataType(ComponentAttribute.class)
	private List<ComponentAttribute> attributes;

	public RequiredForComponent()
	{
	}

	public Boolean checkForComplete()
	{
		if (component == null || component.getClass() != Component.class) {
			return Boolean.FALSE;
		}
//		AttributeServiceImpl attributeService = new AttributeServiceImpl();
//		List<AttributeType> requireds = attributeService.getRequiredAttributes();
//		boolean found = false;
//		for (AttributeType required : requireds) {
//			for (ComponentAttribute attribute : attributes) {
//				if (required.getAttributeType().equals(attribute.getComponentAttributePk().getAttributeType())) {
//					found = true;
//					break;
//				}
//			}
//			if (!found) {
//				return Boolean.FALSE;
//			}
//			found = false;
//		}
		return Boolean.TRUE;
	}

	/**
	 * @return the component
	 */
	public Component getComponent()
	{
		return component;
	}

	/**
	 * @param component the component to set
	 */
	public void setComponent(Component component)
	{
		this.component = component;
	}

	/**
	 * @return the attributes
	 */
	public List<ComponentAttribute> getAttributes()
	{
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(List<ComponentAttribute> attributes)
	{
		this.attributes = attributes;
	}
}
