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

import edu.usu.sdl.openstorefront.core.api.ComponentService;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.view.ComponentTypeView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author dshurtleff
 */
public class ComponentTypeNestedModelTest
{

	public ComponentTypeNestedModelTest()
	{
	}

	/**
	 * Test Find Parents
	 */
	@Test
	public void testfindParents()
	{
		System.out.println("findParents");

		ComponentTypeNestedModel instance = getData();
		Map<String, ComponentType> typeMap = instance.findParents(instance, "C");

		assertEquals(true, typeMap.keySet().contains("A"));
		assertEquals(true, typeMap.keySet().contains("B"));
		assertEquals(true, typeMap.keySet().contains("C"));
	}

	/**
	 * Test of toString method, of class ComponentTypeNestedModel.
	 */
	@Test
	public void testToString()
	{
		System.out.println("toString");

		ComponentTypeNestedModel instance = getData();

		String expResult = "root<-Fruit\n"
				+ "root<-Table\n"
				+ "Fruit<-Apple\n"
				+ "Fruit<-Orange\n"
				+ "Apple<-Gala\n";

		String result = instance.toString();

		System.out.println(result);

		assertEquals(expResult, result);
	}

	private ComponentTypeNestedModel getData()
	{
		ComponentTypeNestedModel nestedModel = new ComponentTypeNestedModel();

		Service service = Mockito.mock(Service.class);
		ComponentService componentService = Mockito.mock(ComponentService.class);
		Mockito.when(service.getComponentService()).thenReturn(componentService);
		ServiceProxyFactory.setTestService(service);

		ComponentTypeNestedModel childModel = new ComponentTypeNestedModel();

		ComponentType aType = new ComponentType();
		aType.setComponentType("A");
		aType.setLabel("Fruit");
		childModel.setComponentType(ComponentTypeView.toView(aType));

		ComponentTypeNestedModel subChildModel = new ComponentTypeNestedModel();
		aType = new ComponentType();
		aType.setComponentType("B");
		aType.setLabel("Apple");
		aType.setParentComponentType("A");
		subChildModel.setComponentType(ComponentTypeView.toView(aType));

		ComponentTypeNestedModel subChildModel2 = new ComponentTypeNestedModel();
		aType = new ComponentType();
		aType.setComponentType("C");
		aType.setLabel("Gala");
		aType.setParentComponentType("B");
		subChildModel2.setComponentType(ComponentTypeView.toView(aType));
		subChildModel.getChildren().add(subChildModel2);
		childModel.getChildren().add(subChildModel);

		subChildModel = new ComponentTypeNestedModel();
		aType = new ComponentType();
		aType.setComponentType("E");
		aType.setLabel("Orange");
		aType.setParentComponentType("A");
		subChildModel.setComponentType(ComponentTypeView.toView(aType));
		childModel.getChildren().add(subChildModel);

		nestedModel.getChildren().add(childModel);

		childModel = new ComponentTypeNestedModel();
		aType = new ComponentType();
		aType.setComponentType("D");
		aType.setLabel("Table");
		childModel.setComponentType(ComponentTypeView.toView(aType));
		nestedModel.getChildren().add(childModel);

		return nestedModel;
	}

	@Test
	public void getComponentTypeChildren()
	{
		// Assemble
		List<ComponentTypeNestedModel> nm1Children = new ArrayList<>();
		ComponentTypeNestedModel nm1 = new ComponentTypeNestedModel();
		ComponentTypeView nm1View = new ComponentTypeView();
		nm1View.setComponentType("A1");
		nm1View.setLabel("Fruit");
		nm1View.setComponentTypeTemplate("test");
		nm1.setComponentType(nm1View);

		ComponentTypeNestedModel nm1_1 = new ComponentTypeNestedModel();
		ComponentTypeView nm1View_1 = new ComponentTypeView();
		nm1View_1.setComponentType("A2");
		nm1View_1.setLabel("Apple");
		nm1View_1.setComponentTypeTemplate("test");
		nm1_1.setComponentType(nm1View_1);
		nm1Children.add(nm1_1);

		ComponentTypeNestedModel nm1_2 = new ComponentTypeNestedModel();
		ComponentTypeView nm1View_2 = new ComponentTypeView();
		nm1View_2.setComponentType("A3");
		nm1View_2.setLabel("Orange");
		nm1View_2.setComponentTypeTemplate("test");
		nm1_2.setComponentType(nm1View_2);
		nm1Children.add(nm1_2);

		ComponentTypeNestedModel nm2 = new ComponentTypeNestedModel();
		ComponentTypeView nm2View = new ComponentTypeView();
		nm2View.setComponentType("B");
		nm2View.setLabel("Veggie");
		nm2View.setComponentTypeTemplate("test");
		nm2.setComponentType(nm2View);

		nm1_2.setChildren(Arrays.asList(nm2));

		nm1.setChildren(nm1Children);

		// Act
		List<String> entryTypeChildren = nm1.findComponentTypeChildren();

		// Assert
		assertEquals(Arrays.asList("A1", "A2", "A3", "B"), entryTypeChildren);
	}

	@Test
	public void getComponentTypeChildrenEmptyNestedModel()
	{
		ComponentTypeNestedModel nestedModel = new ComponentTypeNestedModel();

		// Act
		List<String> entryTypeChildren = nestedModel.findComponentTypeChildren();

		// Assert
		assertEquals(new ArrayList<>(), entryTypeChildren);
	}

}
