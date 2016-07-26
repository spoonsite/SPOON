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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
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
		List<String> falseStrings = Arrays.asList("F", "1.1.2", "1.", "test", "", "null");

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

    /**
     * Test of toLong method, of class Convert.
     */
    @Test
    public void testToLong() {
        System.out.println("toLong");
        Object data = null;
        Long expResult = null;
        Long result = Convert.toLong(data);
        
        List<String> trueStrings = Arrays.asList("10023", "4788294", "3");
        List<String> falseStrings = Arrays.asList("123.456", "fName", "C", "1.23.3", "9223372036854775909",
                "", "null");
        
        for (String testLong : trueStrings) {
            result = Convert.toLong(testLong);
            if (result == null) {
                fail("Failed on: " + testLong);
            }
        }
        
        for (String testLong : falseStrings) {
            result = Convert.toLong(testLong);
            assertEquals(expResult, result);
        }
        
    }

    /**
     * Test of toBigDecimal method, of class Convert.
     */
    @Test
    public void testToBigDecimal_Object() {
        System.out.println("toBigDecimal");
        Object data = null;
        BigDecimal expResult = null;
        BigDecimal result = Convert.toBigDecimal(data);

        List<String> trueStrings = Arrays.asList("28983", "3", "0.33", "123.48");
        List<String> falseStrings = Arrays.asList("Fred", "84.233.4", "A", "1,333", "", "null");
        
        for (String testBigDecimal : trueStrings) {
            result = Convert.toBigDecimal(testBigDecimal);
            if (result == null) {
                fail("Failed on: " + testBigDecimal);
            }
        }
        
        for (String testBigDecimal : falseStrings) {
            result = Convert.toBigDecimal(testBigDecimal);
            assertEquals(expResult, result);
        }
        
    }

    /**
     * Test of toBigDecimal method, of class Convert.
     */
    @Test
    public void testToBigDecimalWithDefault() {
        System.out.println("toBigDecimal");
        Object data = null;
        BigDecimal defaultDecimal = null;
        BigDecimal expResult = null;
        BigDecimal result = Convert.toBigDecimal(data, defaultDecimal);
        
        List<String> trueStrings = Arrays.asList("47833", "2", "0.00050", "45.67");
        List<String> falseStrings = Arrays.asList("Test", "123O", "F", "a", "", "null");
        
        for (String testBigDecimal : trueStrings) {
            result = Convert.toBigDecimal(testBigDecimal, defaultDecimal);
            if (result == null) {
                fail("Failed on: " + testBigDecimal);
            }
        }
        
        for (String testBigDecimal : falseStrings) {
            result = Convert.toBigDecimal(testBigDecimal, defaultDecimal);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of toDate method, of class Convert.
     */
    @Test
    public void testToDate() {
        System.out.println("toDate");
        String dateString = "";
        Date expResult = null;
        Date result = Convert.toDate(dateString);
        
        List<String> validDates = Arrays.asList("07/04/2016 12:00:01 PST ", 
                "1999-05-10 08:30:44 MST ", "1997-04-30T12:08:56.235-0700",
                "01 Jul 2016 11:33:54 PM PST","12/25/2003","2001-08-12", "1997-04-30T12:08:56.235'-0700");
        List<String> partiallyValidDates = Arrays.asList("13/12/202 25:00: AFFAT", "1997-04-3012:08:56235-0700", 
                "01 January 01 11:33:54 PM PST", "122519803543");
        List<String> invalidDates = Arrays.asList("", "null", "thisis1test", "D", "12.344.555");
        
        for (String testDate : validDates) {
            result = Convert.toDate(testDate);
            if(result == null) {
                fail("Incorrect date format: " + testDate);
            }
        }
        
        for (String testDate : partiallyValidDates) {
            result = Convert.toDate(testDate);
            if(result == null) {
                fail("Incorrect date format: " + testDate);
            }
            else {
                System.out.println("Given date: " + testDate + " vs parsed date " + result);
            }
        }
        
        for (String testDate : invalidDates) {
            result = Convert.toDate(testDate);
            assertEquals(expResult, result);
        }
    }

}
