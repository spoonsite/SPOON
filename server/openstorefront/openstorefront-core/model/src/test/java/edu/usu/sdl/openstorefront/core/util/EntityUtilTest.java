/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.util;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.security.UserRecord;
import java.lang.reflect.Field;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class EntityUtilTest
{

	/**
	 * Test of isObjectsDifferent method, of class EntityUtil.
	 */
	@Test
	public void testIsObjectsDifferent()
	{
		System.out.println("isObjectsDifferent");

		Component component = new Component();
		component.setName("Test");

		boolean consumeFieldsOnly = true;
		boolean expResult = false;
		boolean result = EntityUtil.isObjectsDifferent(component, component, consumeFieldsOnly);
		assertEquals(expResult, result);

		Component component2 = new Component();
		component2.setName("Test_2");
		expResult = true;
		result = EntityUtil.isObjectsDifferent(component, component2, consumeFieldsOnly);
		assertEquals(expResult, result);
		
		UserRecord userRecord1 = new UserRecord();
		userRecord1.setFirstName("Bob");
		userRecord1.setLastName("Test");
		userRecord1.setOrganization("Test");
		userRecord1.setEmail("Check@test.com");
		userRecord1.setPhone("555-555-5555");
		userRecord1.setUsername("Check");
		
		UserRecord userRecord2 = new UserRecord();
		userRecord2.setFirstName("Bob");
		userRecord2.setLastName("Test");
		userRecord2.setOrganization("Test");
		userRecord2.setEmail("newemail@test.com");
		userRecord2.setPhone("555-555-5555");
		userRecord2.setUsername("Check");		
		
		expResult = true;
		result = EntityUtil.isObjectsDifferent(userRecord1, userRecord2, false);
		assertEquals(expResult, result);		
	}
	
	/**
	 * Test failure isObjectsDifferent method
	 * should throw exception if objects are different
	 */
	@Test(expected = OpenStorefrontRuntimeException.class)
	public void testIsObjectsDifferentFailure()
	{
		System.out.println("isObjectsDifferent");
		
		UserRecord userRecord1 = new UserRecord();
		userRecord1.setFirstName("Bob");
		userRecord1.setLastName("Test");
		userRecord1.setOrganization("Test");
		userRecord1.setEmail("Check@test.com");
		userRecord1.setPhone("555-555-5555");
		userRecord1.setUsername("Check");
		
		Component component = new Component();
		component.setName("Test");		
		
		boolean expResult = true;
		boolean result = EntityUtil.isObjectsDifferent(userRecord1, component, false);
		assertEquals(expResult, result);		
	}

	/**
	 * Test of compareConsumeFields method, of class EntityUtil.
	 */
	@Test
	public void testCompareConsumeFields()
	{
		System.out.println("compareConsumeFields");
		Component component = new Component();
		component.setName("Test");
		component.setActiveStatus(Component.ACTIVE_STATUS);

		Component component2 = new Component();
		component2.setName("Test");
		component2.setActiveStatus(Component.ACTIVE_STATUS);
		int expResult = 0;
		int result = EntityUtil.compareConsumeFields(component, component2);
		assertEquals(expResult, result);

		component2 = new Component();
		component2.setName("Test2");
		expResult = -1;
		result = EntityUtil.compareConsumeFields(component, component2);
		assertEquals(expResult, result);

		expResult = 1;
		result = EntityUtil.compareConsumeFields(null, component2);
		assertEquals(expResult, result);
	}

	/**
	 * Test of getPKField method, of class EntityUtil.
	 *
	 * @throws java.lang.IllegalAccessException
	 */
	@Test
	public void testGetPKField() throws IllegalArgumentException, IllegalAccessException
	{
		System.out.println("getPKField");
		Component entity = new Component();
		entity.setComponentId("Test");

		Field result = EntityUtil.getPKField(entity);
		result.setAccessible(true);
		if ("Test".equals(result.get(entity)) == false) {
			fail("Unable to get Id");
		}
	}

	/**
	 * Test of isPKFieldGenerated method, of class EntityUtil.
	 */
	@Test
	public void testIsPKFieldGenerated()
	{
		System.out.println("isPKFieldGenerated");
		Component entity = new Component();
		boolean expResult = true;
		boolean result = EntityUtil.isPKFieldGenerated(entity);
		assertEquals(expResult, result);

		System.out.println(result = EntityUtil.isPKFieldGenerated(entity));

		try {
			result = EntityUtil.isPKFieldGenerated(null);
			fail("Entity cannot be null");
		} catch (NullPointerException e) {

		}

		// Test AttributeType Class and EmailAddress Class
		AttributeType aType = new AttributeType();
		aType.setAttributeType("attribute");
		aType.setActiveStatus(AttributeType.ACTIVE_STATUS);
		expResult = false;
		result = EntityUtil.isPKFieldGenerated(aType);
		System.out.println(result);
		assertEquals(expResult, result);

		AttributeCode aCode = new AttributeCode();
		result = EntityUtil.isPKFieldGenerated(aType);
		System.out.println(result);
		assertEquals(expResult, result);

	}

	/**
	 * Test of getPKFieldValue method, of class EntityUtil.
	 */
	@Test
	public void testGetPKFieldValue()
	{
		System.out.println("getPKFieldValue");
		Component entity = new Component();
		entity.setComponentId("Test");
		String expResult = "Test";
		String result = EntityUtil.getPKFieldValue(entity);
		assertEquals(expResult, result);

		AttributeType aType = new AttributeType();
		aType.setAttributeType("Attribute PK");
		expResult = "Attribute PK";
		result = EntityUtil.getPKFieldValue(aType);
		assertEquals(expResult, result);

		AttributeCode aCode = new AttributeCode();
		AttributeCodePk aCodePk = new AttributeCodePk();
		aCodePk.setAttributeCode("Attribute Code PK");
		aCodePk.setAttributeType("Attribute Type PK");
		aCode.setAttributeCodePk(aCodePk);
		result = EntityUtil.getPKFieldValue(aCode);
		expResult = aCodePk.toKey();
		assertEquals(result, expResult);
	}

	/**
	 * Test of updatePKFieldValue method, of class EntityUtil.
	 */
	@Test
	public void testUpdatePKFieldValue()
	{
		System.out.println("updatePKFieldValue");
		Component entity = new Component();
		String value = "Test";
		EntityUtil.updatePKFieldValue(entity, value);
		assertEquals("Test", entity.getComponentId());

		AttributeType aType = new AttributeType();
		aType.setAttributeType("Attribute PK old");
		EntityUtil.updatePKFieldValue(aType, "Attribute PK new");
		String expResult = "Attribute PK new";
		String result = EntityUtil.getPKFieldValue(aType);
		System.out.println(result);
		assertEquals(expResult, result);

		AttributeCode aCode = new AttributeCode();
		AttributeCodePk aCodePk = new AttributeCodePk();
		aCodePk.setAttributeCode("Attribute Code PK old");
		aCodePk.setAttributeType("Attribute Type PK old");
		aCode.setAttributeCodePk(aCodePk);

		try {
			EntityUtil.updatePKFieldValue(aCode, "Attribute Code PK new");
			result = EntityUtil.getPKFieldValue(aCode);
			System.out.println(result);
		} catch (OpenStorefrontRuntimeException e) {
			System.out.println("Test Passed");
		}
	}
}
