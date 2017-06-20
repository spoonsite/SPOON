/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.apiclient.usecase;

import edu.usu.sdl.apiclient.rest.service.ApplicationImpl;
import edu.usu.sdl.openstorefront.core.view.LoggerView;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import java.util.logging.Level;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ccummings
 */
public class ApplicationImplUseCase
{

	public ApplicationImplUseCase()
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
	 * Test of getConfigPropertiesForKey method, of class ApplicationImpl.
	 */
	@Test
	public void testGetConfigPropertiesForKey()
	{
		System.out.println("getConfigPropertiesForKey");
		// Arrange
//		ObjectMapper objMapper = new ObjectMapper();
		String key = "app.title";
		// Act
		ApplicationImpl instance = new ApplicationImpl();
		instance.connect("admin", "Secret1@", "http://localhost:8080/openstorefront/");
		LookupModel result = instance.getConfigPropertiesForKey(key);
		// Assert
		Assert.assertEquals(key, result.getCode());
		Assert.assertEquals("DI2E Clearinghouse (Dev)", result.getDescription());

	}

	@Test
	public void testAddConfigProperty()
	{

		System.out.println("addConfigProperty");

		LookupModel lookupModel = new LookupModel();
		lookupModel.setCode("test.addProp");
		lookupModel.setDescription("Unit Test Option");

		ApplicationImpl instance = new ApplicationImpl();
		instance.connect("admin", "Secret1@", "http://localhost:8080/openstorefront/");
		LookupModel result = instance.addConfigProperty(lookupModel);

		Assert.assertEquals("test.addProp", result.getCode());
		Assert.assertEquals("Unit Test Option", result.getDescription());
	}

	@Test
	public void testRemoveConfigProperty()
	{

		System.out.println("removeConfigProperties");

		String key = "test.addProp";
		// Act
		ApplicationImpl instance = new ApplicationImpl();
		instance.connect("admin", "Secret1@", "http://localhost:8080/openstorefront/");
		instance.removeConfigProperties(key);

		LookupModel result = instance.getConfigPropertiesForKey(key);

		Assert.assertEquals(key, result.getCode());
		Assert.assertEquals(null, result.getDescription());

	}
	
	@Test
	public void testUpdateApplicationProperty()
	{
		System.out.println("updateApplicationProperty");

		ApplicationImpl instance = new ApplicationImpl();
		instance.connect("admin", "Secret1@", "http://localhost:8080/openstorefront/");
		LoggerView result = instance.updateApplicationProperty("edu.usu.sdl.openstorefront", Level.INFO.getName());

		Assert.assertEquals("INFO", result.getLevel());
		Assert.assertEquals("edu.usu.sdl.openstorefront", result.getName());
	}

}
