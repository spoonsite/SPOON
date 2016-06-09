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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
		String url = "";
		String expResult = "";
		String result = StringProcessor.getResourceNameFromUrl(url);
		assertEquals(expResult, result);

		url = "http:/google.com/test";
		expResult = "test";
		result = StringProcessor.getResourceNameFromUrl(url);
		assertEquals(expResult, result);

		url = "http:/google.com/test?query=p";
		expResult = "test?query=p";
		result = StringProcessor.getResourceNameFromUrl(url);
		assertEquals(expResult, result);

		url = "query=p";
		expResult = "query=p";
		result = StringProcessor.getResourceNameFromUrl(url);
		assertEquals(expResult, result);
	}

	/**
	 * Test of extractUrls method, of class StringProcessor.
	 */
	@Test
	public void testExtractUrls()
	{
		System.out.println("extractUrls");
		String text = "";
		List<String> result = StringProcessor.extractUrls(text);
		if (result.size() > 0) {
			Assert.fail("Unexpected size from results. " + result.size());
		}

		text = "a http://google.com";
		result = StringProcessor.extractUrls(text);
		System.out.println(Arrays.toString(result.toArray(new String[0])));

		text = "a http://google.com stuff  http://yahoo.com";
		result = StringProcessor.extractUrls(text);
		System.out.println(Arrays.toString(result.toArray(new String[0])));

	}

	/**
	 * Test of stripeExtendedChars method, of class StringProcessor.
	 */
	@Test
	public void testStripeExtendedChars()
	{
		System.out.println("stripeExtendedChars");
		String data = "A";
		String expResult = "A";
		String result = StringProcessor.stripeExtendedChars(data);
		assertEquals(expResult, result);

		data = "A█";
		expResult = "A ";
		result = StringProcessor.stripeExtendedChars(data);
		assertEquals(expResult, result);

	}

	/**
	 * Test of createHrefUrls method, of class StringProcessor.
	 */
	@Test
	public void testCreateHrefUrls()
	{
		System.out.println("createHrefUrls");
		String text = "http://google.com";
		boolean showFullURL = false;
		String expResult = "<a href='http://google.com' title='http://google.com' target='_blank'> google.com</a>";
		String result = StringProcessor.createHrefUrls(text, showFullURL);
		assertEquals(expResult, result);

	}

	/**
	 * Test of stripeFieldJSON method, of class StringProcessor.
	 *
	 * @throws com.fasterxml.jackson.core.JsonProcessingException
	 */
	@Test
	public void testStripeFieldJSON() throws JsonProcessingException
	{
		System.out.println("stripeFieldJSON");
		TestModel userTypeCode = new TestModel();
		userTypeCode.setCode("Test");
		userTypeCode.setDescription("Test2");

		Set<String> fieldsToKeep = new HashSet<>();
		fieldsToKeep.add("code");
		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();
		String result = StringProcessor.stripeFieldJSON(objectMapper.writeValueAsString(userTypeCode), fieldsToKeep);
		System.out.println(result);
		if (result.contains("description")) {
			Assert.fail("Description shouldn't be in reults");
		}
	}

	/**
	 * Test of printObject method, of class StringProcessor.
	 */
	@Test
	public void testPrintObject()
	{
		System.out.println("printObject");
		TestModel userTypeCode = new TestModel();
		userTypeCode.setCode("Test");
		userTypeCode.setDescription("Test2");
		String result = StringProcessor.printObject(userTypeCode);
		System.out.println(result);

	}

	/**
	 * Test of ellipseString method, of class StringProcessor.
	 */
	@Test
	public void testEllipseString()
	{
		System.out.println("eclipseString");
		String data = "This is a test string that is really long.  Does it all show?";
		String result = StringProcessor.ellipseString(data, 20);
		System.out.println(result);
		if (result.endsWith("...") == false) {
			Assert.fail("It didn't eclispe the text");
		}
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

	/**
	 * Test of stripHtml method, of class StringProcessor.
	 */
	@Test
	public void testStripHtml()
	{
		System.out.println("stripHtml");
		String text = "<a href=\"http://clavin.bericotechnologies.com\" rel=\"nofollow\"> http://clavin.bericotechnologies.com</a>";
		String expResult = "http://clavin.bericotechnologies.com";
		String result = StringProcessor.stripHtml(text);
		System.out.println(result);
		assertEquals(expResult, result);

	}

	/**
	 * Test of archtecureCodeToDecimal method, of class StringProcessor.
	 */
	@Test
	public void testArchtecureCodeToDecimal()
	{
		System.out.println("archtecureCodeToDecimal");
		String code = "1.2.1.1";
		BigDecimal result = StringProcessor.archtecureCodeToDecimal(code);
		System.out.println(result.toPlainString());

		code = "1";
		result = StringProcessor.archtecureCodeToDecimal(code);
		System.out.println(result.toPlainString());

		code = "1.";
		result = StringProcessor.archtecureCodeToDecimal(code);
		System.out.println(result.toPlainString());

		code = "1.2";
		result = StringProcessor.archtecureCodeToDecimal(code);
		System.out.println(result.toPlainString());

	}

	@Test
	public void testExtactFileName() 
	{
		System.out.println(StringProcessor.getJustFileName(".../akfjaklf/askjdkl/Test.png"));	
		System.out.println(StringProcessor.getJustFileName("Test.png"));
		System.out.println(StringProcessor.getJustFileName("\\hera\\aaa\\akfjaklf\\Test.png"));
		System.out.println(StringProcessor.getJustFileName("\\hera/aaa\\akfjaklf/Test.png"));
	}	
	
}
