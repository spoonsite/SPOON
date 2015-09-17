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

import java.util.Date;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class TimeUtilTest
{

	public TimeUtilTest()
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
	 * Test of fromString method, of class TimeUtil.
	 */
	@Test
	public void testFromString()
	{
		System.out.println("fromString");
		String value = "2012-01-01T00:25:25.999Z";
		Date result = TimeUtil.fromString(value);
		Assert.assertNotNull(result);
	}

	/**
	 * Test of dateToString method, of class TimeUtil.
	 */
	@Test
	public void testDateToString()
	{
		System.out.println("dateToString");
		String result = TimeUtil.dateToString(new Date());
		System.out.println(result);
		Assert.assertNotNull(result);
	}

	/**
	 * Test of currentDate method, of class TimeUtil.
	 */
	@Test
	public void testCurrentDate()
	{
		System.out.println("currentDate");

		Date result = TimeUtil.currentDate();
		Assert.assertNotNull(result);
	}

}
