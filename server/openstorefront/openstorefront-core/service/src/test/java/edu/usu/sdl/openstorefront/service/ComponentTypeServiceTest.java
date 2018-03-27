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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.core.api.ComponentService;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeTemplate;
import edu.usu.sdl.openstorefront.core.entity.RoleLink;
import edu.usu.sdl.openstorefront.core.entity.UserLink;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeNestedModel;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeOptions;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeRoleResolution;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeTemplateResolution;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeUserResolution;
import edu.usu.sdl.openstorefront.core.view.ComponentTypeView;
import edu.usu.sdl.openstorefront.service.component.ComponentTypeServiceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author dshurtleff
 */
public class ComponentTypeServiceTest
{

	private static final Logger LOG = Logger.getLogger(ComponentTypeServiceTest.class.getName());

	@Test
	public void getComponentType_All()
	{
		ComponentTypeServiceImpl mockCore = setupBaseMock();

		ComponentTypeOptions options = new ComponentTypeOptions();
		Mockito.when(mockCore.getComponentType(options)).thenCallRealMethod();
		ComponentTypeNestedModel nestedModel = mockCore.getComponentType(options);
		LOG.info(nestedModel.toString());

		assertEquals(nestedModel.getChildren().size(), 2);
		assertEquals(nestedModel.getChildren().get(0).getChildren().size(), 2);
		assertEquals(nestedModel.getChildren()
				.get(0).getChildren()
				.get(0).getChildren().size(), 1);
	}

	private ComponentTypeServiceImpl setupBaseMock()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

		Service service = Mockito.mock(Service.class);
		ComponentService componentService = Mockito.mock(ComponentService.class);
		Mockito.when(service.getComponentService()).thenReturn(componentService);

		ServiceProxyFactory.setTestService(service);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		return mockCore;
	}

	@Test
	public void getComponentType_SubChild()
	{
		ComponentTypeServiceImpl mockCore = setupBaseMock();

		ComponentTypeOptions options = new ComponentTypeOptions();
		options.setComponentType("B");
		Mockito.when(mockCore.getComponentType(options)).thenCallRealMethod();
		ComponentTypeNestedModel nestedModel = mockCore.getComponentType(options);
		LOG.info(nestedModel.toString());

		assertEquals(nestedModel.getChildren().size(), 1);
	}

	@Test
	public void getComponentType_Parent()
	{
		ComponentTypeServiceImpl mockCore = setupBaseMock();

		ComponentTypeOptions options = new ComponentTypeOptions();
		options.setComponentType("C");
		options.setPullParents(true);
		Mockito.when(mockCore.getComponentType(options)).thenCallRealMethod();
		ComponentTypeNestedModel nestedModel = mockCore.getComponentType(options);
		LOG.info(nestedModel.toString());

		assertEquals(nestedModel.getChildren().size(), 2);
		assertEquals(nestedModel.getChildren().get(0).getChildren().size(), 1);
	}

	@Test
	public void getComponentType_Top_Parent_Only()
	{
		ComponentTypeServiceImpl mockCore = setupBaseMock();

		ComponentTypeOptions options = new ComponentTypeOptions();
		options.setComponentType("C");
		options.setPullParents(true);
		options.setPullChildren(false);
		Mockito.when(mockCore.getComponentType(options)).thenCallRealMethod();
		ComponentTypeNestedModel nestedModel = mockCore.getComponentType(options);
		LOG.info(nestedModel.toString());

		assertEquals(nestedModel.getComponentType().getLabel(), "Fruit");
		assertEquals(nestedModel.getChildren().size(), 0);
	}

	@Test
	public void getComponentType_Self()
	{
		ComponentTypeServiceImpl mockCore = setupBaseMock();

		ComponentTypeOptions options = new ComponentTypeOptions();
		options.setComponentType("C");
		options.setPullParents(false);
		options.setPullChildren(false);
		Mockito.when(mockCore.getComponentType(options)).thenCallRealMethod();
		ComponentTypeNestedModel nestedModel = mockCore.getComponentType(options);
		LOG.info(nestedModel.getComponentType().getLabel());

		assertEquals(nestedModel.getComponentType().getLabel(), "Gala");
		assertEquals(nestedModel.getChildren().size(), 0);
	}

	@Test
	public void findTemplateForComponentType_Direct()
	{
		ComponentTypeServiceImpl mockCore = setupBaseMock();

		ComponentTypeTemplate template = new ComponentTypeTemplate();
		template.setName("test");
		Mockito.when(mockCore.getComponentTypeTemplate("test")).thenReturn(template);

		Mockito.when(mockCore.findTemplateForComponentType("A")).thenCallRealMethod();
		ComponentTypeTemplateResolution templateResolution = mockCore.findTemplateForComponentType("A");
		LOG.info(templateResolution.toString());

		assertEquals(templateResolution.getTemplateName(), "test");
	}

	@Test
	public void findTemplateForComponentType_Parent()
	{
		ComponentTypeServiceImpl mockCore = setupBaseMock();

		ComponentTypeTemplate template = new ComponentTypeTemplate();
		template.setName("test");
		Mockito.when(mockCore.getComponentTypeTemplate("test")).thenReturn(template);

		Mockito.when(mockCore.findTemplateForComponentType("B")).thenCallRealMethod();
		ComponentTypeTemplateResolution templateResolution = mockCore.findTemplateForComponentType("B");
		LOG.info(templateResolution.toString());

		assertEquals(templateResolution.getTemplateName(), "test");
	}

	@Test
	public void findRoleGroupsForComponentType_Direct()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.findRoleGroupsForComponentType("B")).thenCallRealMethod();

		ComponentTypeRoleResolution resolution = mockCore.findRoleGroupsForComponentType("B");
		assertEquals(resolution.getRoles().size(), 1);
	}

	@Test
	public void findRoleGroupsForComponentType_Parent()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.findRoleGroupsForComponentType("C")).thenCallRealMethod();

		ComponentTypeRoleResolution resolution = mockCore.findRoleGroupsForComponentType("C");

		assertEquals(resolution.getAncestorComponentType(), "B");
		assertEquals(resolution.getRoles().size(), 1);

	}

	@Test
	public void findUserForComponentType_Direct()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.findUserForComponentType("B")).thenCallRealMethod();

		ComponentTypeUserResolution resolution = mockCore.findUserForComponentType("B");
		assertEquals(resolution.getUsernames().size(), 1);
	}

	@Test
	public void findUserForComponentType_Parent()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.findUserForComponentType("C")).thenCallRealMethod();

		ComponentTypeUserResolution resolution = mockCore.findUserForComponentType("C");

		assertEquals(resolution.getAncestorComponentType(), "B");
		assertEquals(resolution.getUsernames().size(), 1);
	}

	@Test
	public void findIconForComponentType_Direct()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.resolveComponentTypeIcon("B")).thenCallRealMethod();

		String url = mockCore.resolveComponentTypeIcon("B");

		assertEquals(url, "test.png");
	}

	@Test
	public void findIconForComponentType_Parent()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.resolveComponentTypeIcon("C")).thenCallRealMethod();

		String url = mockCore.resolveComponentTypeIcon("C");

		assertEquals(url, "test.png");
	}

	@Test
	public void getComponentTypeChildren()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

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
		Mockito.when(mockCore.getComponentTypeChildren(nm1)).thenCallRealMethod();
		List<String> entryTypeChildren = mockCore.getComponentTypeChildren(nm1);

		// Assert
		assertEquals(Arrays.asList("A1", "A2", "A3", "B"), entryTypeChildren);
	}
	
	@Test
	public void getComponentTypeChildrenEmptyNestedModel()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);
		
		// Act
		Mockito.when(mockCore.getComponentTypeChildren(null)).thenCallRealMethod();
		List<String> entryTypeChildren = mockCore.getComponentTypeChildren(null);
		
		// Assert
		assertEquals(new ArrayList<>(), entryTypeChildren);
	}

	private List<ComponentType> getMockData()
	{
		List<ComponentType> componentTypes = new ArrayList<>();
		ComponentType aType = new ComponentType();
		aType.setComponentType("A");
		aType.setLabel("Fruit");
		aType.setComponentTypeTemplate("test");
		componentTypes.add(aType);

		aType = new ComponentType();
		aType.setComponentType("B");
		aType.setLabel("Apple");
		aType.setIconUrl("test.png");
		aType.setParentComponentType("A");

		RoleLink roleLink = new RoleLink();
		roleLink.setRoleName("test");
		aType.setAssignedGroups(new ArrayList<>());
		aType.getAssignedGroups().add(roleLink);

		UserLink userLink = new UserLink();
		userLink.setUsername("test");
		aType.setAssignedUsers(new ArrayList<>());
		aType.getAssignedUsers().add(userLink);

		componentTypes.add(aType);

		aType = new ComponentType();
		aType.setComponentType("C");
		aType.setLabel("Gala");
		aType.setParentComponentType("B");
		componentTypes.add(aType);

		aType = new ComponentType();
		aType.setComponentType("C-Child");
		aType.setLabel("Washington Gala");
		aType.setParentComponentType("C");
		componentTypes.add(aType);

		aType = new ComponentType();
		aType.setComponentType("E");
		aType.setLabel("Orange");
		aType.setParentComponentType("A");

		roleLink = new RoleLink();
		roleLink.setRoleName("test");
		aType.setAssignedGroups(new ArrayList<>());
		aType.getAssignedGroups().add(roleLink);

		userLink = new UserLink();
		userLink.setUsername("test");
		aType.setAssignedUsers(new ArrayList<>());
		aType.getAssignedUsers().add(userLink);
		componentTypes.add(aType);

		aType = new ComponentType();
		aType.setComponentType("D");
		aType.setLabel("Table");
		componentTypes.add(aType);

		return componentTypes;
	}

}
