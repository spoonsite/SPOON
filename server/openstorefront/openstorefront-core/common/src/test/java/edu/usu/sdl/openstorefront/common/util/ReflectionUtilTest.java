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
package edu.usu.sdl.openstorefront.common.util;

import java.awt.Component;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
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
		boolean result = ReflectionUtil.isComplexClass(TestModel.class);
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
		result = ReflectionUtil.isCollectionClass(TestModel.class);
		assertEquals(expResult, result);
	}

	/**
	 * Test of getAllFields method, of class ReflectionUtil.
	 */
	@Test
	public void testGetAllFields()
	{
		System.out.println("getAllFields");

		List<Field> result = ReflectionUtil.getAllFields(TestModel.class);
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

		expResult = false;
		result = ReflectionUtil.isSubLookupEntity(TestModel.class);
		assertEquals(expResult, result);

	}

	/**
	 * Test of isSubClass method, of class ReflectionUtil.
	 */
	@Test
	public void testIsSubClass()
	{
		System.out.println("isSubClass");

		boolean expResult = false;
		boolean result = ReflectionUtil.isSubClass(ReflectionUtil.BASECOMPONENT_ENTITY, TestModel.class);
		assertEquals(expResult, result);

		expResult = false;
		result = ReflectionUtil.isSubClass(ReflectionUtil.BASECOMPONENT_ENTITY, TestModel.class);
		assertEquals(expResult, result);

	}

	/**
	 * Test of compareFields method, of class EntityUtil.
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
	 * Test of isFieldsDifferent method, of class EntityUtil.
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

}
