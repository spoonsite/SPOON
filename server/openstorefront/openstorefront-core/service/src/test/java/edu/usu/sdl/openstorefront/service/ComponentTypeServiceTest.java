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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
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
import edu.usu.sdl.openstorefront.service.component.ComponentTypeServiceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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

		assertEquals(2, nestedModel.getChildren().size());
		assertEquals(2, nestedModel.getChildren().get(0).getChildren().size());
		assertEquals(1, nestedModel.getChildren()
				.get(0).getChildren()
				.get(0).getChildren().size());
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

		assertEquals(1, nestedModel.getChildren().size());
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

		assertEquals(2, nestedModel.getChildren().size());
		assertEquals(1, nestedModel.getChildren().get(0).getChildren().size());
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

		assertEquals("Fruit", nestedModel.getComponentType().getLabel());
		assertEquals(0, nestedModel.getChildren().size());
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

		assertEquals("Gala", nestedModel.getComponentType().getLabel());
		assertEquals(0, nestedModel.getChildren().size());
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

		assertEquals("test", templateResolution.getTemplateName());
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

		assertEquals("test", templateResolution.getTemplateName());
	}

	@Test
	public void findRoleGroupsForComponentType_Direct()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.findRoleGroupsForComponentType("B")).thenCallRealMethod();

		ComponentTypeRoleResolution resolution = mockCore.findRoleGroupsForComponentType("B");
		assertEquals(1, resolution.getRoles().size());
	}

	@Test
	public void findRoleGroupsForComponentType_Parent()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.findRoleGroupsForComponentType("C")).thenCallRealMethod();

		ComponentTypeRoleResolution resolution = mockCore.findRoleGroupsForComponentType("C");

		assertEquals("B", resolution.getAncestorComponentType());
		assertEquals(1, resolution.getRoles().size());

	}

	@Test
	public void findUserForComponentType_Direct()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.findUserForComponentType("B")).thenCallRealMethod();

		ComponentTypeUserResolution resolution = mockCore.findUserForComponentType("B");
		assertEquals(1, resolution.getUsernames().size());
	}

	@Test
	public void findUserForComponentType_Parent()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.findUserForComponentType("C")).thenCallRealMethod();

		ComponentTypeUserResolution resolution = mockCore.findUserForComponentType("C");

		assertEquals("B", resolution.getAncestorComponentType());
		assertEquals(1, resolution.getUsernames().size());
	}

	@Test
	public void findIconForComponentType_Direct()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.resolveComponentTypeIcon("B")).thenCallRealMethod();

		String url = mockCore.resolveComponentTypeIcon("B");

		assertEquals("test.png", url);
	}

	@Test
	public void findIconForComponentType_Parent()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.resolveComponentTypeIcon("C")).thenCallRealMethod();

		String url = mockCore.resolveComponentTypeIcon("C");

		assertEquals("test.png", url);
	}

	@Test
	public void getComponentTypeParents()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.getComponentTypeParents("C", false)).thenCallRealMethod();

		List<String> componentTypeList = mockCore.getComponentTypeParents("C", false)
				.stream()
				.map(ct -> ct.getComponentType())
				.collect(Collectors.toList());

		assertEquals(componentTypeList, new ArrayList<>(Arrays.asList("B", "A")));
	}

	@Test
	public void getComponentTypeParents_noParents()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.getComponentTypeParents("A", false)).thenCallRealMethod();

		List<ComponentType> componentTypeList = mockCore.getComponentTypeParents("A", false);

		assertEquals(componentTypeList, new ArrayList<>());
	}

	@Test
	public void getComponentTypeParentsString()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.getComponentTypeParentsString("C", false)).thenCallRealMethod();
		Mockito.when(mockCore.getComponentTypeParents("C", false)).thenCallRealMethod();

		Mockito.when(mockCore.getComponentTypeParentsString("C", true)).thenCallRealMethod();
		Mockito.when(mockCore.getComponentTypeParents("C", true)).thenCallRealMethod();

		String componentTypeList_notReversed = mockCore.getComponentTypeParentsString("C", false);
		String componentTypeList_reversed = mockCore.getComponentTypeParentsString("C", true);

		assertEquals("Gala < Apple < Fruit", componentTypeList_notReversed);
		assertEquals("Fruit > Apple > Gala", componentTypeList_reversed);

	}

	@Test(expected = OpenStorefrontRuntimeException.class)
	public void getComponentTypeParentsString_Invalid()
	{
		ComponentTypeServiceImpl mockCore = Mockito.mock(ComponentTypeServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.getComponentTypeParentsString("TEST_VALUE", true)).thenCallRealMethod();
		Mockito.when(mockCore.getComponentTypeParents("TEST_VALUE", true)).thenCallRealMethod();
		mockCore.getComponentTypeParentsString("TEST_VALUE", true);
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
