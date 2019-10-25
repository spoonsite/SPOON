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
package edu.usu.sdl.openstorefront.validation;

import edu.usu.sdl.openstorefront.core.entity.EmailAddress;
import edu.usu.sdl.openstorefront.core.entity.UserTypeCode;
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
public class ValidationUtilTest
{

	public ValidationUtilTest()
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
	 * Test of isValid method, of class ValidationUtil.
	 */
	@Test
	public void testIsValid()
	{
		System.out.println("isValid");

		UserTypeCode userTypeCode = new UserTypeCode();
		userTypeCode.setCode("Test");
		userTypeCode.setDescription("Test Description");

		ValidationModel validateModel = new ValidationModel(userTypeCode);
		validateModel.setConsumeFieldsOnly(true);

		boolean expResult = true;
		boolean result = ValidationUtil.isValid(validateModel);
		assertEquals(expResult, result);

		validateModel = new ValidationModel(userTypeCode);

		expResult = false;
		result = ValidationUtil.isValid(validateModel);
		assertEquals(expResult, result);

		EmailAddress ea = new EmailAddress();
		ea.setEmail("test_testEmail@company.com");
		ea.setStorageVersion("3.1.2");

		validateModel = new ValidationModel(ea);
		expResult = true;
		result = ValidationUtil.isValid(validateModel);
		assertEquals(expResult, result);

	}

	/**
	 * Test of validate method, of class ValidationUtil.
	 */
	@Test
	public void testValidate()
	{
		System.out.println("validate");
		UserTypeCode userTypeCode = new UserTypeCode();
		userTypeCode.setCode("Test");
		userTypeCode.setDescription("Test Description");

		ValidationModel validateModel = new ValidationModel(userTypeCode);
		validateModel.setConsumeFieldsOnly(true);

		ValidationResult result = ValidationUtil.validate(validateModel);
		System.out.println("Any Valid Consume: " + result.toString());
		if (result.valid() == false) {
			fail("Failed validation when it was expected to pass.");
		}
		System.out.println("-------------------------------");

		validateModel = new ValidationModel(userTypeCode);
		result = ValidationUtil.validate(validateModel);
		System.out.println("Failed: " + result.toString());

		validateModel = new ValidationModel(userTypeCode);
		validateModel.setSanitize(true);

		result = ValidationUtil.validate(validateModel);
		if (result.valid() == true) {
			fail("Failed validation when it was expected to pass");
		} else {
			System.out.println("Test Passed");
		}

		System.out.println("-------------------------------");

		validateModel = new ValidationModel(userTypeCode);
		validateModel.setDataObject(null);
		validateModel.setAcceptNull(true);
		result = ValidationUtil.validate(validateModel);

		if (result.valid() == false) {
			fail("Failed validation when it was expected to pass");
		} else {
			System.out.println("Test Passed");
		}
		System.out.println("-------------------------------");

	}
}
