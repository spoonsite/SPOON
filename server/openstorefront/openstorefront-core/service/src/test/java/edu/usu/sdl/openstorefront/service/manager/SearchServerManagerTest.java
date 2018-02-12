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
package edu.usu.sdl.openstorefront.service.manager;

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.service.search.SearchServer;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author ccummings
 */
public class SearchServerManagerTest
{

	public SearchServerManagerTest()
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
	 * Test of getSearchServer method, of class SearchServerManager.
	 */
	@Test
	public void testGetSearchServer()
	{
		System.out.println("getSearchServer");
		PropertiesManager mockPropertiesManager = Mockito.mock(PropertiesManager.class);
		Mockito.when(mockPropertiesManager.getModuleVersion()).thenReturn("3.0");

		SearchServerManager instance = null;
		SearchServer expResult = null;
		SearchServer result = instance.getSearchServer();
		Assert.assertEquals(expResult, result);
	}

	/**
	 * Test of getInstance method, of class SearchServerManager.
	 */
	@Test
	public void testGetInstance_PropertiesManager()
	{
		System.out.println("getInstance");
		PropertiesManager propertiesManager = null;
		SearchServerManager expResult = null;
		SearchServerManager result = SearchServerManager.getInstance(propertiesManager);
		Assert.assertEquals(expResult, result);
	}

	/**
	 * Test of updateSearchScore method, of class SearchServerManager.
	 */
	@Test
	public void testUpdateSearchScore()
	{
		System.out.println("updateSearchScore");
		String query = "";
		List<ComponentSearchView> views = null;
		SearchServerManager instance = null;
		instance.updateSearchScore(query, views);
	}

	/**
	 * Test of initialize method, of class SearchServerManager.
	 */
	@Test
	public void testInitialize()
	{
		System.out.println("initialize");
		SearchServerManager instance = null;
		instance.initialize();
	}

	/**
	 * Test of shutdown method, of class SearchServerManager.
	 */
	@Test
	public void testShutdown()
	{
		System.out.println("shutdown");
		SearchServerManager instance = null;
		instance.shutdown();
	}

	/**
	 * Test of isStarted method, of class SearchServerManager.
	 */
	@Test
	public void testIsStarted()
	{
		System.out.println("isStarted");
		SearchServerManager instance = null;
		boolean expResult = false;
		boolean result = instance.isStarted();
		Assert.assertEquals(expResult, result);
	}

}
