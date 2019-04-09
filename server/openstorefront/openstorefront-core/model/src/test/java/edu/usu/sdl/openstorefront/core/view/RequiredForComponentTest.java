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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.api.AttributeService;
import edu.usu.sdl.openstorefront.core.api.ComponentService;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeRestriction;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeNestedModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author dshurtleff
 */
public class RequiredForComponentTest
{

	@Test
	public void checkForComplete_Valid()
	{
		System.out.println("checkForComplete_Valid");

		setupMocksforAttributeCheck();
		RequiredForComponent requiredForComponent = new RequiredForComponent();
		Component component = new Component();
		component.setComponentType("A");
		requiredForComponent.setComponent(component);

		ComponentAttribute componentAttribute = new ComponentAttribute();
		ComponentAttributePk componentAttributePk = new ComponentAttributePk();
		componentAttributePk.setAttributeType("O");
		componentAttribute.setComponentAttributePk(componentAttributePk);
		requiredForComponent.getAttributes().add(componentAttribute);

		componentAttribute = new ComponentAttribute();
		componentAttributePk = new ComponentAttributePk();
		componentAttributePk.setAttributeType("R");
		componentAttribute.setComponentAttributePk(componentAttributePk);
		requiredForComponent.getAttributes().add(componentAttribute);

		ValidationResult result = requiredForComponent.checkForComplete();
		Assert.assertEquals(true, result.valid());

	}

	@Test
	public void checkForComplete_InValid()
	{
		System.out.println("checkForComplete_InValid");

		setupMocksforAttributeCheck();

		RequiredForComponent requiredForComponent = new RequiredForComponent();
		Component component = new Component();
		component.setComponentType("C");
		requiredForComponent.setComponent(component);

		ValidationResult result = requiredForComponent.checkForComplete();
		Assert.assertEquals(false, result.valid());

	}

	@Test
	public void checkForComplete_Valid_No_Restricted()
	{
		System.out.println("checkForComplete_InValidWithParent");
		setupMocksforAttributeCheck();

		RequiredForComponent requiredForComponent = new RequiredForComponent();
		Component component = new Component();
		component.setComponentType("A");
		requiredForComponent.setComponent(component);

		ComponentAttribute componentAttribute = new ComponentAttribute();
		ComponentAttributePk componentAttributePk = new ComponentAttributePk();
		componentAttributePk.setAttributeType("O");
		componentAttribute.setComponentAttributePk(componentAttributePk);
		requiredForComponent.getAttributes().add(componentAttribute);

		ValidationResult result = requiredForComponent.checkForComplete();
		Assert.assertEquals(true, result.valid());
	}

	private void setupMocksforAttributeCheck()
	{
		AttributeService attributeService = Mockito.mock(AttributeService.class);
		ComponentService componentService = Mockito.mock(ComponentService.class);

		Service service = Mockito.mock(Service.class);
		Mockito.when(service.getAttributeService()).thenReturn(attributeService);
		Mockito.when(service.getComponentService()).thenReturn(componentService);
		ServiceProxyFactory.setTestService(service);

//		List<AttributeType> attributeTypeRequired = new ArrayList<>();
//		AttributeType attributeType = new AttributeType();
//		attributeType.setAttributeType("O");
//		attributeType.setDescription("Open");
//		attributeTypeRequired.add(attributeType);
		AttributeType attributeType = new AttributeType();
		attributeType.setRequiredFlg(Boolean.TRUE);
		attributeType.setAttributeType("R");
		attributeType.setDescription("Restricted");

		List<ComponentTypeRestriction> optionalRestrictions = new ArrayList<>();
		optionalRestrictions.add(new ComponentTypeRestriction("D"));

		List<ComponentTypeRestriction> requiredRestrictions = new ArrayList<>();
		requiredRestrictions.add(new ComponentTypeRestriction("B"));
		requiredRestrictions.add(new ComponentTypeRestriction("C"));

		attributeType.setOptionalRestrictions(optionalRestrictions);
		attributeType.setRequiredRestrictions(requiredRestrictions);
		//	attributeTypeRequired.add(attributeType);

		List<AttributeType> requiredAttributes = new ArrayList<>();
		requiredAttributes.add(attributeType);
		Mockito.when(attributeService.findRequiredAttributes("B", false, null)).thenReturn(requiredAttributes);
		Mockito.when(attributeService.findRequiredAttributes("C", false, null)).thenReturn(requiredAttributes);
		Mockito.when(attributeService.findRequiredAttributes("A", false, null)).thenReturn(new ArrayList<>());

		ComponentTypeNestedModel nestedModel = new ComponentTypeNestedModel();

		ComponentType componentType = new ComponentType();
		componentType.setComponentType("A");
		componentType.setLabel("A");
		nestedModel.setComponentType(ComponentTypeView.toView(componentType));

		ComponentTypeNestedModel childModel = new ComponentTypeNestedModel();
		componentType = new ComponentType();
		componentType.setComponentType("B");
		componentType.setLabel("B");
		componentType.setParentComponentType("A");
		childModel.setComponentType(ComponentTypeView.toView(componentType));
		nestedModel.getChildren().add(childModel);

		ComponentTypeNestedModel childModel2 = new ComponentTypeNestedModel();
		componentType = new ComponentType();
		componentType.setComponentType("C");
		componentType.setLabel("C");
		componentType.setParentComponentType("B");
		childModel2.setComponentType(ComponentTypeView.toView(componentType));
		childModel.getChildren().add(childModel2);

		Mockito.when(componentService.getComponentType(Mockito.any())).thenReturn(nestedModel);

	}

}
