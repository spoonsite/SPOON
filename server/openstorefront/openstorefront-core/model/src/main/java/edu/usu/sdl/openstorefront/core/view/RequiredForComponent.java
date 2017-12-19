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
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeRestriction;
import edu.usu.sdl.openstorefront.validation.RuleResult;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

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
	private boolean updateVersion = true;

	public RequiredForComponent()
	{
	}

	public ValidationResult checkForComplete()
	{
		ValidationResult validationResult = new ValidationResult();

		List<AttributeType> requireds = ServiceProxyFactory.getServiceProxy().getAttributeService().getRequiredAttributes();

		requireds.removeIf(attributeType -> checkAttributeAssociation(attributeType, component.getComponentType()));

		Map<String, AttributeType> requiredTypeMap = new HashMap<>();
		for (AttributeType attributeType : requireds) {
			boolean add = true;
			if (attributeType.getRequiredRestrictions() != null
					&& !attributeType.getRequiredRestrictions().isEmpty()) {
				if (component.getComponentType() != null) {
					boolean found = false;
					for (ComponentTypeRestriction componentTypeRestriction : attributeType.getRequiredRestrictions()) {
						if (component.getComponentType().equals(componentTypeRestriction.getComponentType())) {
							found = true;
						}
					}
					if (!found) {
						add = false;
					}
				}
			}
			if (add) {
				requiredTypeMap.put(attributeType.getAttributeType(), attributeType);
			}
		}

		Set<String> inSetAttributeType = new HashSet<>();
		for (ComponentAttribute attribute : attributes) {
			inSetAttributeType.add(attribute.getComponentAttributePk().getAttributeType());
		}

		//set defaults if missing
		for (String type : requiredTypeMap.keySet()) {
			AttributeType attributeType = requiredTypeMap.get(type);

			if (StringUtils.isNotBlank(attributeType.getDefaultAttributeCode())) {
				if (!inSetAttributeType.contains(type)) {
					ComponentAttribute componentAttribute = new ComponentAttribute();
					ComponentAttributePk componentAttributePk = new ComponentAttributePk();
					componentAttributePk.setAttributeType(type);
					componentAttributePk.setAttributeCode(attributeType.getDefaultAttributeCode());
					componentAttribute.setComponentAttributePk(componentAttributePk);
					attributes.add(componentAttribute);
				}
			}
		}

		Set<String> matchedAttributes = new HashSet<>();
		Set<String> missingAttributes = new HashSet<>();
		for (ComponentAttribute attribute : attributes) {
			String type = attribute.getComponentAttributePk().getAttributeType();
			if (requiredTypeMap.containsKey(type)) {
				matchedAttributes.add(type);
			} else {
				missingAttributes.add(type);
			}
		}
		if (matchedAttributes.size() < requiredTypeMap.size()) {
			StringJoiner joiner = new StringJoiner(", ");
			for (String type : matchedAttributes) {
				joiner.add(type);
			}

			RuleResult ruleResult = new RuleResult();
			ruleResult.setMessage("Missing Required Attributes");
			ruleResult.setEntityClassName(ComponentAttribute.class.getSimpleName());
			ruleResult.setFieldName("componentAttributes");
			ruleResult.setValidationRule("Must have required attributes. Missing: " + joiner.toString());
			validationResult.getRuleResults().add(ruleResult);
		}
		return validationResult;
	}

	private boolean checkAttributeAssociation(AttributeType attributeType, String componentType)
	{
		boolean remove = false;

		if (attributeType.getAssociatedComponentTypes() != null
				&& !attributeType.getAssociatedComponentTypes().isEmpty()) {
			if (component.getComponentType() != null) {
				boolean found = false;
				for (ComponentTypeRestriction componentTypeRestriction : attributeType.getAssociatedComponentTypes()) {
					if (component.getComponentType().equals(componentTypeRestriction.getComponentType())) {
						found = true;
					}
				}
				if (!found) {
					remove = true;
				}
			}
		}

		return remove;
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

	public boolean getUpdateVersion()
	{
		return updateVersion;
	}

	public void setUpdateVersion(boolean updateVersion)
	{
		this.updateVersion = updateVersion;
	}

}
