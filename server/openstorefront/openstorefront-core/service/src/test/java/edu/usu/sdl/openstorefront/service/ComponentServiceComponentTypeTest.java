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
import edu.usu.sdl.openstorefront.core.entity.RoleLink;
import edu.usu.sdl.openstorefront.core.entity.UserLink;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeNestedModel;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeOptions;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeRoleResolution;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeUserResolution;
import edu.usu.sdl.openstorefront.service.component.CoreComponentServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author dshurtleff
 */
public class ComponentServiceComponentTypeTest
{

	private static final Logger LOG = Logger.getLogger(ComponentServiceComponentTypeTest.class.getName());

	@Test
	public void getComponentType_All()
	{
		CoreComponentServiceImpl mockCore = Mockito.mock(CoreComponentServiceImpl.class);

		Service service = Mockito.mock(Service.class);
		ComponentService componentService = Mockito.mock(ComponentService.class);
		Mockito.when(service.getComponentService()).thenReturn(componentService);
		//Mockito.when(componentService.findTemplateForComponentType(Mockito.any())).thenReturn(null);

		ServiceProxyFactory.setTestService(service);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		//mock the component service
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

	@Test
	public void getComponentType_SubChild()
	{
		CoreComponentServiceImpl mockCore = Mockito.mock(CoreComponentServiceImpl.class);

		Service service = Mockito.mock(Service.class);
		ComponentService componentService = Mockito.mock(ComponentService.class);
		Mockito.when(service.getComponentService()).thenReturn(componentService);
		//Mockito.when(componentService.findTemplateForComponentType(Mockito.any())).thenReturn(null);

		ServiceProxyFactory.setTestService(service);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		//mock the component service
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
		CoreComponentServiceImpl mockCore = Mockito.mock(CoreComponentServiceImpl.class);

		Service service = Mockito.mock(Service.class);
		ComponentService componentService = Mockito.mock(ComponentService.class);
		Mockito.when(service.getComponentService()).thenReturn(componentService);
		//Mockito.when(componentService.findTemplateForComponentType(Mockito.any())).thenReturn(null);

		ServiceProxyFactory.setTestService(service);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		//mock the component service
		ComponentTypeOptions options = new ComponentTypeOptions();
		options.setComponentType("C");
		options.setPullParents(true);
		Mockito.when(mockCore.getComponentType(options)).thenCallRealMethod();
		ComponentTypeNestedModel nestedModel = mockCore.getComponentType(options);
		LOG.info(nestedModel.toString());

		assertEquals(nestedModel.getChildren().size(), 2);
		assertEquals(nestedModel.getChildren().get(0).getChildren().size(), 1);
	}

//	@Test
//	public void findTemplateForComponentType_Direct()
//	{
//
//	}
//
//	@Test
//	public void findTemplateForComponentType_Parent()
//	{
//
//	}
	@Test
	public void findRoleGroupsForComponentType_Direct()
	{
		CoreComponentServiceImpl mockCore = Mockito.mock(CoreComponentServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.findRoleGroupsForComponentType("B")).thenCallRealMethod();

		ComponentTypeRoleResolution resolution = mockCore.findRoleGroupsForComponentType("B");
		assertEquals(resolution.getRoles().size(), 1);
	}

	@Test
	public void findRoleGroupsForComponentType_Parent()
	{
		CoreComponentServiceImpl mockCore = Mockito.mock(CoreComponentServiceImpl.class);

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
		CoreComponentServiceImpl mockCore = Mockito.mock(CoreComponentServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.findUserForComponentType("B")).thenCallRealMethod();

		ComponentTypeUserResolution resolution = mockCore.findUserForComponentType("B");
		assertEquals(resolution.getUsernames().size(), 1);
	}

	@Test
	public void findUserForComponentType_Parent()
	{
		CoreComponentServiceImpl mockCore = Mockito.mock(CoreComponentServiceImpl.class);

		List<ComponentType> componentTypes = getMockData();
		Mockito.when(mockCore.getAllComponentTypes()).thenReturn(componentTypes);

		Mockito.when(mockCore.findUserForComponentType("C")).thenCallRealMethod();

		ComponentTypeUserResolution resolution = mockCore.findUserForComponentType("C");

		assertEquals(resolution.getAncestorComponentType(), "B");
		assertEquals(resolution.getUsernames().size(), 1);
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
