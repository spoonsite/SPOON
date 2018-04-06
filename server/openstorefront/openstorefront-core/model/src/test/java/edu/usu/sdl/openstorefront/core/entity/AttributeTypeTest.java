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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.api.ComponentService;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author dshurtleff
 */
public class AttributeTypeTest
{

	public AttributeTypeTest()
	{
	}

	/**
	 * Test of updateFields method, of class AttributeType.
	 */
	@Test
	public void testUpdateFields()
	{
		System.out.println("updateFields");
		AttributeType entity = new AttributeType();
		entity.setAttributeType("Test");
		entity.setAllowMultipleFlg(Boolean.TRUE);
		entity.setAllowUserGeneratedCodes(Boolean.FALSE);
		entity.setRequiredFlg(Boolean.TRUE);
		entity.setArchitectureFlg(Boolean.TRUE);
		entity.setAttributeValueType(AttributeValueType.NUMBER);
		entity.setDescription("Test");
		entity.setDetailedDescription("Test Detail");
		entity.setHideOnSubmission(Boolean.TRUE);
		entity.setImportantFlg(Boolean.TRUE);
		entity.setVisibleFlg(Boolean.TRUE);
		AttributeType instance = new AttributeType();
		instance.updateFields(entity);

		assertEquals(instance.getAttributeType(), null);
		assertEquals(instance.getAllowMultipleFlg(), true);
	}

	/**
	 * Test of addOptionalComponentType method, of class AttributeType.
	 */
	@Test
	public void testAddOptionalComponentType_All()
	{
		System.out.println("addOptionalComponentType");

		setupMocks();

		String componentType = "";
		AttributeType instance = new AttributeType();
		instance.addOptionalComponentType(componentType);

		assertEquals(instance.getOptionalRestrictions().size(), 2);
	}

	/**
	 * Test of addOptionalComponentType method, of class AttributeType.
	 */
	@Test
	public void testAddOptionalComponentType_Single()
	{
		System.out.println("addOptionalComponentType");

		setupMocks();

		String componentType = "A";
		AttributeType instance = new AttributeType();
		instance.addOptionalComponentType(componentType);

		assertEquals(instance.getOptionalRestrictions().size(), 1);
	}

	/**
	 * Test of addRequiredComponentType method, of class AttributeType.
	 */
	@Test
	public void testAddRequiredComponentType()
	{
		System.out.println("addRequiredComponentType");
		setupMocks();

		String componentType = "A";
		AttributeType instance = new AttributeType();
		instance.addRequiredComponentType(componentType);

		assertEquals(instance.getRequiredRestrictions().size(), 1);
	}

	private void setupMocks()
	{
		Service service = Mockito.mock(Service.class);
		ComponentService componentService = Mockito.mock(ComponentService.class);
		Mockito.when(service.getComponentService()).thenReturn(componentService);

		List<ComponentType> componentTypes = new ArrayList<>();
		ComponentType componentType = new ComponentType();
		componentType.setComponentType("A");
		componentTypes.add(componentType);

		componentType = new ComponentType();
		componentType.setComponentType("B");
		componentTypes.add(componentType);
		Mockito.when(componentService.getAllComponentTypes()).thenReturn(componentTypes);

		ServiceProxyFactory.setTestService(service);

	}

	/**
	 * Test of hashCode method, of class AttributeType.
	 */
	@Test
	public void testHashCode()
	{
		System.out.println("hashCode");
		AttributeType instance = new AttributeType();
		int expResult = 0;
		int result = instance.hashCode();
		assertEquals(expResult, result);
	}

	/**
	 * Test of equals method, of class AttributeType.
	 */
	@Test
	public void testEquals()
	{
		System.out.println("equals");
		Object object = null;
		AttributeType instance = new AttributeType();
		boolean expResult = false;
		boolean result = instance.equals(object);
		assertEquals(expResult, result);
	}

}
