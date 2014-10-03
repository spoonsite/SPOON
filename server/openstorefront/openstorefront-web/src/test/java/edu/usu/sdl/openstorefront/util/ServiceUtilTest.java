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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class ServiceUtilTest
{

	public ServiceUtilTest()
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
	 * Test of isComplexClass method, of class ServiceUtil.
	 */
	@Test
	public void testIsComplexClass()
	{
		System.out.println("isComplexClass");

		boolean expResult = true;
		boolean result = ServiceUtil.isComplexClass(UserTypeCode.class);
		assertEquals(expResult, result);

		expResult = false;
		result = ServiceUtil.isComplexClass(String.class);
		assertEquals(expResult, result);
	}

	/**
	 * Test of isCollectionClass method, of class ServiceUtil.
	 */
	@Test
	public void testIsCollectionClass()
	{
		System.out.println("isCollectionClass");

		boolean expResult = true;
		boolean result = ServiceUtil.isCollectionClass(List.class);
		assertEquals(expResult, result);

		expResult = false;
		result = ServiceUtil.isCollectionClass(UserTypeCode.class);
		assertEquals(expResult, result);
	}

	/**
	 * Test of getAllFields method, of class ServiceUtil.
	 */
	@Test
	public void testGetAllFields()
	{
		System.out.println("getAllFields");

		List<Field> result = ServiceUtil.getAllFields(UserTypeCode.class);
		System.out.println("Use Type Fields: ");
		for (Field field : result) {
			System.out.println(field.getName());
		}
	}

	/**
	 * Test of isSubLookupEntity method, of class ServiceUtil.
	 */
	@Test
	public void testIsSubLookupEntity()
	{
		System.out.println("isSubLookupEntity");

		boolean expResult = false;
		boolean result = ServiceUtil.isSubLookupEntity(Component.class);
		assertEquals(expResult, result);

		expResult = true;
		result = ServiceUtil.isSubLookupEntity(UserTypeCode.class);
		assertEquals(expResult, result);

	}

	/**
	 * Test of isSubClass method, of class ServiceUtil.
	 */
	@Test
	public void testIsSubClass()
	{
		System.out.println("isSubClass");

		boolean expResult = true;
		boolean result = ServiceUtil.isSubClass(ServiceUtil.BASECOMPONENT_ENTITY, ComponentMedia.class);
		assertEquals(expResult, result);

		expResult = false;
		result = ServiceUtil.isSubClass(ServiceUtil.BASECOMPONENT_ENTITY, UserTypeCode.class);
		assertEquals(expResult, result);

	}

	/**
	 * Test of compareObjects method, of class ServiceUtil.
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
		boolean result = ServiceUtil.isObjectsDifferent(userTypeCode, userTypeCode, consumeFieldsOnly);
		assertEquals(expResult, result);

		UserTypeCode userTypeCodeDiff = new UserTypeCode();
		userTypeCode.setCode("Test");
		userTypeCode.setDescription("Test2");

		expResult = true;
		result = ServiceUtil.isObjectsDifferent(userTypeCode, userTypeCodeDiff, consumeFieldsOnly);
		assertEquals(expResult, result);

	}

	/**
	 * Test of compareFields method, of class ServiceUtil.
	 */
	@Test
	public void testCompareFields()
	{
		System.out.println("compareFields");

		boolean expResult = true;
		boolean result = ServiceUtil.isFieldsDifferent("Test", "TEST");
		assertEquals(expResult, result);

		expResult = false;
		result = ServiceUtil.isFieldsDifferent("Test", "Test");
		assertEquals(expResult, result);

	}

}
