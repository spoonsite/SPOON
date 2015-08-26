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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
	private List<ComponentAttribute> attributes = new ArrayList<>();

	private boolean componentChanged;
	private boolean attributeChanged;

	public RequiredForComponent()
	{
	}

	public ValidationResult checkForComplete()
	{
		ValidationResult validationResult = new ValidationResult();

		List<AttributeType> requireds = ServiceProxyFactory.getServiceProxy().getAttributeService().getRequiredAttributes();
		Set<String> requiredTypeSet = new HashSet<>();
		for (AttributeType attributeType : requireds) {
			requiredTypeSet.add(attributeType.getAttributeType());
		}

		Set<String> matchedAttributes = new HashSet<>();
		for (ComponentAttribute attribute : attributes) {
			String type = attribute.getComponentAttributePk().getAttributeType();
			if (requiredTypeSet.contains(type)) {
				matchedAttributes.add(type);
			}
		}
		if (matchedAttributes.size() < requiredTypeSet.size()) {
			RuleResult ruleResult = new RuleResult();
			ruleResult.setMessage("Missing Required Attributes");
			ruleResult.setEntityClassName(ComponentAttribute.class.getSimpleName());
			ruleResult.setFieldName("componentAttributes");
			ruleResult.setValidationRule("Must has required attributes");
			validationResult.getRuleResults().add(ruleResult);
		}
		return validationResult;
	}

	public Component getComponent()
	{
		return component;
	}

	public void setComponent(Component component)
	{
		this.component = component;
	}

	public List<ComponentAttribute> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(List<ComponentAttribute> attributes)
	{
		this.attributes = attributes;
	}

	public boolean isComponentChanged()
	{
		return componentChanged;
	}

	public void setComponentChanged(boolean componentChanged)
	{
		this.componentChanged = componentChanged;
	}

	public boolean isAttributeChanged()
	{
		return attributeChanged;
	}

	public void setAttributeChanged(boolean attributeChanged)
	{
		this.attributeChanged = attributeChanged;
	}

}
