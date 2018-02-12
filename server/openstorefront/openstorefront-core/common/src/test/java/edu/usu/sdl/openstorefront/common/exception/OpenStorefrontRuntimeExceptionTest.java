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
package edu.usu.sdl.openstorefront.common.exception;

import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class OpenStorefrontRuntimeExceptionTest
{

	private static final Logger LOG = Logger.getLogger(OpenStorefrontRuntimeExceptionTest.class.getName());

	public OpenStorefrontRuntimeExceptionTest()
	{
	}

	/**
	 * Test of getPotentialResolution method, of class
	 * OpenStorefrontRuntimeException.
	 */
	@Test
	public void testGetPotentialResolution()
	{
		LOG.info("getPotentialResolution");

		LOG.info("Scenario 1: With Resolution");
		OpenStorefrontRuntimeException instance = new OpenStorefrontRuntimeException("Test", "Check");
		String expResult = "Check";
		String result = instance.getPotentialResolution();
		assertEquals(expResult, result);

		LOG.info("Scenario 2: Without Resolution");
		instance = new OpenStorefrontRuntimeException("Test");
		expResult = null;
		result = instance.getPotentialResolution();
		assertEquals(expResult, result);

	}

	/**
	 * Test of getErrorTypeCode method, of class OpenStorefrontRuntimeException.
	 */
	@Test
	public void testGetErrorTypeCode()
	{
		LOG.info("getErrorTypeCode");

		LOG.info("Scenario 1: With Code");
		OpenStorefrontRuntimeException instance = new OpenStorefrontRuntimeException("test", "bad", "505");
		String expResult = "505";
		String result = instance.getErrorTypeCode();
		assertEquals(expResult, result);

		LOG.info("Scenario 2: Without Code");
		instance = new OpenStorefrontRuntimeException("test", "bad");
		expResult = "SYS";
		result = instance.getErrorTypeCode();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getLocalizedMessage method, of class
	 * OpenStorefrontRuntimeException.
	 */
	@Test
	public void testGetLocalizedMessage()
	{
		LOG.info("getLocalizedMessage");

		LOG.info("Scenario 1: nothing");
		OpenStorefrontRuntimeException instance = new OpenStorefrontRuntimeException();
		String expResult = "null  Error Type Code: SYS";
		String result = instance.getLocalizedMessage();
		assertEquals(expResult, result);

		LOG.info("Scenario 2: message");
		instance = new OpenStorefrontRuntimeException("test");
		expResult = "test  Error Type Code: SYS";
		result = instance.getLocalizedMessage();
		assertEquals(expResult, result);

		LOG.info("Scenario 3: potential messsage");
		instance = new OpenStorefrontRuntimeException("test", "bad");
		expResult = "test  Potential Resolution: bad  Error Type Code: SYS";
		result = instance.getLocalizedMessage();
		assertEquals(expResult, result);

		LOG.info("Scenario 4: all");
		instance = new OpenStorefrontRuntimeException("test", "bad", "505");
		expResult = "test  Potential Resolution: bad  Error Type Code: 505";
		result = instance.getLocalizedMessage();
		assertEquals(expResult, result);

	}

}
