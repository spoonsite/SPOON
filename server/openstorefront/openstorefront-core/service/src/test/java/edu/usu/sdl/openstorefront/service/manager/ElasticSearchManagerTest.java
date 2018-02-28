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
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.model.search.SearchSuggestion;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.SearchQuery;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.manager.resource.ElasticSearchClient;
import edu.usu.sdl.openstorefront.service.search.IndexSearchResult;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ccummings
 */
public class ElasticSearchManagerTest
{
	
	public ElasticSearchManagerTest()
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
	 * Test of getInstance method, of class ElasticSearchManager.
	 */
	@Test
	public void testGetInstance_0args()
	{
		System.out.println("getInstance");
		ElasticSearchManager expResult = null;
		ElasticSearchManager result = ElasticSearchManager.getInstance();

	}

	/**
	 * Test of getInstance method, of class ElasticSearchManager.
	 */
	@Test
	public void testGetInstance_PropertiesManager()
	{
		System.out.println("getInstance");
		PropertiesManager propertiesManager = null;
		ElasticSearchManager expResult = null;
		ElasticSearchManager result = ElasticSearchManager.getInstance(propertiesManager);

	}

	/**
	 * Test of getInstance method, of class ElasticSearchManager.
	 */
	@Test
	public void testGetInstance_3args()
	{
		System.out.println("getInstance");
		ServiceProxy service = null;
		PropertiesManager propertiesManager = null;
		BlockingQueue<ElasticSearchClient> clientPool = null;
		ElasticSearchManager expResult = null;
		ElasticSearchManager result = ElasticSearchManager.getInstance(service, propertiesManager, clientPool);

	}

	/**
	 * Test of getClient method, of class ElasticSearchManager.
	 */
	@Test
	public void testGetClient()
	{
		System.out.println("getClient");
		ElasticSearchManager instance = null;
		ElasticSearchClient expResult = null;
		ElasticSearchClient result = instance.getClient();

	}

	/**
	 * Test of releaseClient method, of class ElasticSearchManager.
	 */
	@Test
	public void testReleaseClient()
	{
		System.out.println("releaseClient");
		ElasticSearchClient client = null;
		ElasticSearchManager instance = null;

	}

	/**
	 * Test of getMaxConnections method, of class ElasticSearchManager.
	 */
	@Test
	public void testGetMaxConnections()
	{
		System.out.println("getMaxConnections");
		ElasticSearchManager instance = null;
		int expResult = 0;
		int result = instance.getMaxConnections();

	}

	/**
	 * Test of getAvailableConnections method, of class ElasticSearchManager.
	 */
	@Test
	public void testGetAvailableConnections()
	{
		System.out.println("getAvailableConnections");
		ElasticSearchManager instance = null;
		int expResult = 0;
		int result = instance.getAvailableConnections();

	}

	/**
	 * Test of shutdownPool method, of class ElasticSearchManager.
	 */
	@Test
	public void testShutdownPool()
	{
		System.out.println("shutdownPool");
		ElasticSearchManager instance = null;

	}

	/**
	 * Test of initialize method, of class ElasticSearchManager.
	 */
	@Test
	public void testInitialize()
	{
		System.out.println("initialize");
		ElasticSearchManager instance = null;
		instance.initialize();

	}

	/**
	 * Test of shutdown method, of class ElasticSearchManager.
	 */
	@Test
	public void testShutdown()
	{
		System.out.println("shutdown");
		ElasticSearchManager instance = null;
		instance.shutdown();

	}

	/**
	 * Test of search method, of class ElasticSearchManager.
	 */
	@Test
	public void testSearch()
	{
		System.out.println("search");
		SearchQuery searchQuery = null;
		FilterQueryParams filter = null;
		ElasticSearchManager instance = null;
		ComponentSearchWrapper expResult = null;
		ComponentSearchWrapper result = instance.search(searchQuery, filter);


	}

	/**
	 * Test of doIndexSearch method, of class ElasticSearchManager.
	 */
	@Test
	public void testDoIndexSearch_String_FilterQueryParams()
	{
		System.out.println("doIndexSearch");
		String query = "";
		FilterQueryParams filter = null;
		ElasticSearchManager instance = null;
		IndexSearchResult expResult = null;
		IndexSearchResult result = instance.doIndexSearch(query, filter);

	}

	/**
	 * Test of doIndexSearch method, of class ElasticSearchManager.
	 */
	@Test
	public void testDoIndexSearch_3args()
	{
		System.out.println("doIndexSearch");
		String query = "";
		FilterQueryParams filter = null;
		String[] addtionalFieldsToReturn = null;
		ElasticSearchManager instance = null;
		IndexSearchResult expResult = null;
		IndexSearchResult result = instance.doIndexSearch(query, filter, addtionalFieldsToReturn);

	}

	/**
	 * Test of searchSuggestions method, of class ElasticSearchManager.
	 */
	@Test
	public void testSearchSuggestions()
	{
		System.out.println("searchSuggestions");
		String query = "";
		int maxResult = 0;
		String componentType = "";
		ElasticSearchManager instance = null;
		List<SearchSuggestion> expResult = null;
		List<SearchSuggestion> result = instance.searchSuggestions(query, maxResult, componentType);

	}

	/**
	 * Test of index method, of class ElasticSearchManager.
	 */
	@Test
	public void testIndex()
	{
		System.out.println("index");
		List<Component> components = null;
		ElasticSearchManager instance = null;
		instance.index(components);

	}

	/**
	 * Test of deleteById method, of class ElasticSearchManager.
	 */
	@Test
	public void testDeleteById()
	{
		System.out.println("deleteById");
		String id = "";
		ElasticSearchManager instance = null;
		instance.deleteById(id);

	}

	/**
	 * Test of deleteAll method, of class ElasticSearchManager.
	 */
	@Test
	public void testDeleteAll()
	{
		System.out.println("deleteAll");
		ElasticSearchManager instance = null;
		instance.deleteAll();

	}

	/**
	 * Test of saveAll method, of class ElasticSearchManager.
	 */
	@Test
	public void testSaveAll()
	{
		System.out.println("saveAll");
		ElasticSearchManager instance = null;
		instance.saveAll();

	}

	/**
	 * Test of resetIndexer method, of class ElasticSearchManager.
	 */
	@Test
	public void testResetIndexer()
	{
		System.out.println("resetIndexer");
		ElasticSearchManager instance = null;
		instance.resetIndexer();

	}

	/**
	 * Test of isStarted method, of class ElasticSearchManager.
	 */
	@Test
	public void testIsStarted()
	{
		System.out.println("isStarted");
		ElasticSearchManager instance = null;
		boolean expResult = false;
		boolean result = instance.isStarted();

	}
	
}
