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

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.Assert;
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
public class StringProcessorTest
{

	public StringProcessorTest()
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
	 * Test of defaultObjectMapper method, of class StringProcessor.
	 */
	@Test
	public void testDefaultObjectMapper()
	{
		System.out.println("defaultObjectMapper");

		ObjectMapper result = StringProcessor.defaultObjectMapper();
		Assert.assertNotNull(result);
	}

	/**
	 * Test of getResourceNameFromUrl method, of class StringProcessor.
	 */
	@Test
	public void testGetResourceNameFromUrl()
	{
		System.out.println("getResourceNameFromUrl");
//		String url = "";
//		String expResult = "";
//		String result = StringProcessor.getResourceNameFromUrl(url);
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
	}

	/**
	 * Test of extractUrls method, of class StringProcessor.
	 */
	@Test
	public void testExtractUrls()
	{
		System.out.println("extractUrls");
//		String text = "";
//		List<String> expResult = null;
//		List<String> result = StringProcessor.extractUrls(text);
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
	}

	/**
	 * Test of stripeExtendedChars method, of class StringProcessor.
	 */
	@Test
	public void testStripeExtendedChars()
	{
		System.out.println("stripeExtendedChars");
//		String data = "";
//		String expResult = "";
//		String result = StringProcessor.stripeExtendedChars(data);
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
	}

	/**
	 * Test of createHrefUrls method, of class StringProcessor.
	 */
	@Test
	public void testCreateHrefUrls_String()
	{
		System.out.println("createHrefUrls");
//		String text = "";
//		String expResult = "";
//		String result = StringProcessor.createHrefUrls(text);
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
	}

	/**
	 * Test of createHrefUrls method, of class StringProcessor.
	 */
	@Test
	public void testCreateHrefUrls_String_boolean()
	{
		System.out.println("createHrefUrls");
//		String text = "";
//		boolean showFullURL = false;
//		String expResult = "";
//		String result = StringProcessor.createHrefUrls(text, showFullURL);
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
	}

	/**
	 * Test of stripeFieldJSON method, of class StringProcessor.
	 */
	@Test
	public void testStripeFieldJSON()
	{
		System.out.println("stripeFieldJSON");
//		String json = "";
//		Set<String> fieldsToKeep = null;
//		String expResult = "";
//		String result = StringProcessor.stripeFieldJSON(json, fieldsToKeep);
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
	}

	/**
	 * Test of printObject method, of class StringProcessor.
	 */
	@Test
	public void testPrintObject()
	{
		System.out.println("printObject");
//		Object o = null;
//		String expResult = "";
//		String result = StringProcessor.printObject(o);
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
	}

	/**
	 * Test of eclipseString method, of class StringProcessor.
	 */
	@Test
	public void testEclipseString()
	{
		System.out.println("eclipseString");
//		String data = "";
//		int max_length = 0;
//		String expResult = "";
//		String result = StringProcessor.eclipseString(data, max_length);
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
	}

	/**
	 * Test of blankIfNull method, of class StringProcessor.
	 */
	@Test
	public void testBlankIfNull_String()
	{
		System.out.println("blankIfNull");
//		String text = "";
//		String expResult = "";
//		String result = StringProcessor.blankIfNull(text);
//		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
	}

	/**
	 * Test of blankIfNull method, of class StringProcessor.
	 */
	@Test
	public void testBlankIfNull_Object()
	{
		System.out.println("blankIfNull");
		Object text = null;
		String expResult = "";
		String result = StringProcessor.blankIfNull(text);
		assertEquals(expResult, result);

	}

}
