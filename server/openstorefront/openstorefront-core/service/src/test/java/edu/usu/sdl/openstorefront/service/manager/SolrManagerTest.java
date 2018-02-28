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
import edu.usu.sdl.openstorefront.service.search.IndexSearchResult;
import java.util.List;
import org.apache.solr.client.solrj.SolrClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ccummings
 */
public class SolrManagerTest
{
	
	public SolrManagerTest()
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
	 * Test of getInstance method, of class SolrManager.
	 */
	@Test
	public void testGetInstance_0args()
	{
		System.out.println("getInstance");
		SolrManager expResult = null;
		SolrManager result = SolrManager.getInstance();
	}

	/**
	 * Test of getInstance method, of class SolrManager.
	 */
	@Test
	public void testGetInstance_PropertiesManager_SolrClient()
	{
		System.out.println("getInstance");
		PropertiesManager propertiesManager = null;
		SolrClient client = null;
		SolrManager expResult = null;
		SolrManager result = SolrManager.getInstance(propertiesManager, client);

	}

	/**
	 * Test of init method, of class SolrManager.
	 */
	@Test
	public void testInit()
	{
		System.out.println("init");
		SolrManager instance = null;
		instance.init();
	}

	/**
	 * Test of cleanup method, of class SolrManager.
	 */
	@Test
	public void testCleanup()
	{
		System.out.println("cleanup");
		SolrManager instance = null;
		instance.cleanup();
	}

	/**
	 * Test of getServer method, of class SolrManager.
	 */
	@Test
	public void testGetServer()
	{
		System.out.println("getServer");
		SolrManager instance = null;
		SolrClient expResult = null;
		SolrClient result = instance.getServer();

	}

	/**
	 * Test of initialize method, of class SolrManager.
	 */
	@Test
	public void testInitialize()
	{
		System.out.println("initialize");
		SolrManager instance = null;
		instance.initialize();

	}

	/**
	 * Test of shutdown method, of class SolrManager.
	 */
	@Test
	public void testShutdown()
	{
		System.out.println("shutdown");
		SolrManager instance = null;
		instance.shutdown();

	}

	/**
	 * Test of isStarted method, of class SolrManager.
	 */
	@Test
	public void testIsStarted()
	{
		System.out.println("isStarted");
		SolrManager instance = null;
		boolean expResult = false;
		boolean result = instance.isStarted();

	}

	/**
	 * Test of search method, of class SolrManager.
	 */
	@Test
	public void testSearch()
	{
		System.out.println("search");
		SearchQuery searchQuery = null;
		FilterQueryParams filter = null;
		SolrManager instance = null;
		ComponentSearchWrapper expResult = null;
		ComponentSearchWrapper result = instance.search(searchQuery, filter);

	}

	/**
	 * Test of index method, of class SolrManager.
	 */
	@Test
	public void testIndex()
	{
		System.out.println("index");
		List<Component> components = null;
		SolrManager instance = null;
		instance.index(components);

	}

	/**
	 * Test of doIndexSearch method, of class SolrManager.
	 */
	@Test
	public void testDoIndexSearch_String_FilterQueryParams()
	{
		System.out.println("doIndexSearch");
		String query = "";
		FilterQueryParams filter = null;
		SolrManager instance = null;
		IndexSearchResult expResult = null;
		IndexSearchResult result = instance.doIndexSearch(query, filter);

	}

	/**
	 * Test of doIndexSearch method, of class SolrManager.
	 */
	@Test
	public void testDoIndexSearch_3args()
	{
		System.out.println("doIndexSearch");
		String query = "";
		FilterQueryParams filter = null;
		String[] addtionalFieldsToReturn = null;
		SolrManager instance = null;
		IndexSearchResult expResult = null;
		IndexSearchResult result = instance.doIndexSearch(query, filter, addtionalFieldsToReturn);

	}

	/**
	 * Test of searchSuggestions method, of class SolrManager.
	 */
	@Test
	public void testSearchSuggestions()
	{
		System.out.println("searchSuggestions");
		String query = "";
		int maxResult = 0;
		String componentType = "";
		SolrManager instance = null;
		List<SearchSuggestion> expResult = null;
		List<SearchSuggestion> result = instance.searchSuggestions(query, maxResult, componentType);

	}

	/**
	 * Test of deleteById method, of class SolrManager.
	 */
	@Test
	public void testDeleteById()
	{
		System.out.println("deleteById");
		String id = "";
		SolrManager instance = null;
		instance.deleteById(id);

	}

	/**
	 * Test of deleteAll method, of class SolrManager.
	 */
	@Test
	public void testDeleteAll()
	{
		System.out.println("deleteAll");
		SolrManager instance = null;
		instance.deleteAll();

	}

	/**
	 * Test of saveAll method, of class SolrManager.
	 */
	@Test
	public void testSaveAll()
	{
		System.out.println("saveAll");
		SolrManager instance = null;
		instance.saveAll();

	}

	/**
	 * Test of resetIndexer method, of class SolrManager.
	 */
	@Test
	public void testResetIndexer()
	{
		System.out.println("resetIndexer");
		SolrManager instance = null;
		instance.resetIndexer();

	}
	
}
