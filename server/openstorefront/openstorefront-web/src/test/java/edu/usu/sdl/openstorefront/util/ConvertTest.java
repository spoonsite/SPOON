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

import java.util.Arrays;
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
public class ConvertTest
{

	public ConvertTest()
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
	 * Test of toBoolean method, of class Convert.
	 */
	@Test
	public void testToBoolean()
	{
		System.out.println("toBoolean");
		Object data = null;
		boolean expResult = false;
		boolean result = Convert.toBoolean(data);
		assertEquals(expResult, result);

		List<String> trueStrings = Arrays.asList("T", "1", "t", "true");
		List<String> falseStrings = Arrays.asList("F", "0", "f", "false");

		expResult = true;
		for (String test : trueStrings) {
			result = Convert.toBoolean(test);
			assertEquals(expResult, result);
		}

		expResult = false;
		for (String test : falseStrings) {
			result = Convert.toBoolean(test);
			assertEquals(expResult, result);
		}
	}

	/**
	 * Test of toInteger method, of class Convert.
	 */
	@Test
	public void testToInteger()
	{
		System.out.println("toInteger");
		Object data = null;
		Integer expResult = null;
		Integer result = Convert.toInteger(data);
		assertEquals(expResult, result);

		List<String> trueStrings = Arrays.asList("1", "5000");
		List<String> falseStrings = Arrays.asList("F", "1.1.2", "1.", "test");

		for (String test : trueStrings) {
			result = Convert.toInteger(test);
			if (result == null) {
				fail("Failed on: " + test);
			}
		}

		for (String test : falseStrings) {
			result = Convert.toInteger(test);
			assertEquals(expResult, result);
		}

	}

}
