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
package edu.usu.sdl.openstorefront.util;

import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentMedia;
import edu.usu.sdl.openstorefront.storage.model.UserTypeCode;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class ReflectionUtilTest
{

	public ReflectionUtilTest()
	{
	}

	@BeforeClass
	public static void setUpClass()
	{
	}

	@AfterClass
	public static void tearDownClass()
	{
	}

	@Before
	public void setUp()
	{
	}

	@After
	public void tearDown()
	{
	}

	/**
	 * Test of isComplexClass method, of class ReflectionUtil.
	 */
	@Test
	public void testIsComplexClass()
	{
		System.out.println("isComplexClass");

		boolean expResult = true;
		boolean result = ReflectionUtil.isComplexClass(UserTypeCode.class);
		assertEquals(expResult, result);

		expResult = false;
		result = ReflectionUtil.isComplexClass(String.class);
		assertEquals(expResult, result);
	}

	/**
	 * Test of isCollectionClass method, of class ReflectionUtil.
	 */
	@Test
	public void testIsCollectionClass()
	{
		System.out.println("isCollectionClass");

		boolean expResult = true;
		boolean result = ReflectionUtil.isCollectionClass(List.class);
		assertEquals(expResult, result);

		expResult = false;
		result = ReflectionUtil.isCollectionClass(UserTypeCode.class);
		assertEquals(expResult, result);
	}

	/**
	 * Test of getAllFields method, of class ReflectionUtil.
	 */
	@Test
	public void testGetAllFields()
	{
		System.out.println("getAllFields");

		List<Field> result = ReflectionUtil.getAllFields(UserTypeCode.class);
		System.out.println("Use Type Fields: ");
		for (Field field : result) {
			System.out.println(field.getName());
		}
	}

	/**
	 * Test of isSubLookupEntity method, of class ReflectionUtil.
	 */
	@Test
	public void testIsSubLookupEntity()
	{
		System.out.println("isSubLookupEntity");

		boolean expResult = false;
		boolean result = ReflectionUtil.isSubLookupEntity(Component.class);
		assertEquals(expResult, result);

		expResult = true;
		result = ReflectionUtil.isSubLookupEntity(UserTypeCode.class);
		assertEquals(expResult, result);

	}

	/**
	 * Test of isSubClass method, of class ReflectionUtil.
	 */
	@Test
	public void testIsSubClass()
	{
		System.out.println("isSubClass");

		boolean expResult = true;
		boolean result = ReflectionUtil.isSubClass(ReflectionUtil.BASECOMPONENT_ENTITY, ComponentMedia.class);
		assertEquals(expResult, result);

		expResult = false;
		result = ReflectionUtil.isSubClass(ReflectionUtil.BASECOMPONENT_ENTITY, UserTypeCode.class);
		assertEquals(expResult, result);

	}

	/**
	 * Test of compareObjects method, of class ReflectionUtil.
	 */
	@Test
	public void testCompareObjects()
	{
		System.out.println("compareObjects");

		UserTypeCode userTypeCode = new UserTypeCode();
		userTypeCode.setCode("Test");
		userTypeCode.setDescription("Test");

		boolean consumeFieldsOnly = true;
		boolean expResult = false;
		boolean result = ReflectionUtil.isObjectsDifferent(userTypeCode, userTypeCode, consumeFieldsOnly);
		assertEquals(expResult, result);

		UserTypeCode userTypeCodeDiff = new UserTypeCode();
		userTypeCodeDiff.setCode("Test");
		userTypeCodeDiff.setDescription("Test2");

		expResult = true;
		result = ReflectionUtil.isObjectsDifferent(userTypeCode, userTypeCodeDiff, consumeFieldsOnly);
		assertEquals(expResult, result);

	}

	/**
	 * Test of compareFields method, of class ReflectionUtil.
	 */
	@Test
	public void testCompareFields()
	{
		System.out.println("compareFields");

		boolean expResult = true;
		boolean result = ReflectionUtil.isFieldsDifferent("Test", "TEST");
		assertEquals(expResult, result);

		expResult = false;
		result = ReflectionUtil.isFieldsDifferent("Test", "Test");
		assertEquals(expResult, result);

	}

	/**
	 * Test of isObjectsDifferent method, of class ReflectionUtil.
	 */
	@Test
	public void testIsObjectsDifferent()
	{
		System.out.println("isObjectsDifferent");

		Component component = new Component();
		component.setName("Test");

		boolean consumeFieldsOnly = true;
		boolean expResult = false;
		boolean result = ReflectionUtil.isObjectsDifferent(component, component, consumeFieldsOnly);
		assertEquals(expResult, result);

	}

	/**
	 * Test of isFieldsDifferent method, of class ReflectionUtil.
	 */
	@Test
	public void testIsFieldsDifferent()
	{
		System.out.println("isFieldsDifferent");
		Object original = "Test";
		Object newField = "Test2";
		boolean expResult = true;
		boolean result = ReflectionUtil.isFieldsDifferent(original, newField);
		assertEquals(expResult, result);

	}

	/**
	 * Test of getPKField method, of class ReflectionUtil.
	 */
	@Test
	public void testGetPKField() throws IllegalArgumentException, IllegalAccessException
	{
		System.out.println("getPKField");
		Component entity = new Component();
		entity.setComponentId("Test");

		Field result = ReflectionUtil.getPKField(entity);
		result.setAccessible(true);
		if ("Test".equals(result.get(entity)) == false) {
			fail("Unable to get Id");
		}

	}

	/**
	 * Test of isPKFieldGenerated method, of class ReflectionUtil.
	 */
	@Test
	public void testIsPKFieldGenerated()
	{
		System.out.println("isPKFieldGenerated");
		Component entity = new Component();
		boolean expResult = true;
		boolean result = ReflectionUtil.isPKFieldGenerated(entity);
		assertEquals(expResult, result);

	}

	/**
	 * Test of getPKFieldValue method, of class ReflectionUtil.
	 */
	@Test
	public void testGetPKFieldValue()
	{
		System.out.println("getPKFieldValue");
		Component entity = new Component();
		entity.setComponentId("Test");
		String expResult = "Test";
		String result = ReflectionUtil.getPKFieldValue(entity);
		assertEquals(expResult, result);

	}

	/**
	 * Test of updatePKFieldValue method, of class ReflectionUtil.
	 */
	@Test
	public void testUpdatePKFieldValue()
	{
		System.out.println("updatePKFieldValue");
		Component entity = new Component();
		String value = "Test";
		ReflectionUtil.updatePKFieldValue(entity, value);
		assertEquals("Test", entity.getComponentId());
	}

	/**
	 * Test of compareConsumeFields method, of class ReflectionUtil.
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
		int result = ReflectionUtil.compareConsumeFields(component, component2);
		assertEquals(expResult, result);

		component2 = new Component();
		component2.setName("Test2");
		expResult = -1;
		result = ReflectionUtil.compareConsumeFields(component, component2);
		assertEquals(expResult, result);

	}

}
