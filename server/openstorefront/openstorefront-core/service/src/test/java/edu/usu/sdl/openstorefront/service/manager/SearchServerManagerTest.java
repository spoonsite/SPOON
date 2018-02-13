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
import static org.junit.Assert.fail;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author ccummings
 */
public class SearchServerManagerTest
{

	/**
	 * Test of initialize method, of class SearchServerManager.
	 */
	@Test
	public void testInitializeElasticsearch()
	{
		System.out.println("initialize");

		PropertiesManager propertiesManager = Mockito.mock(PropertiesManager.class);
		Mockito.when(propertiesManager.getValue(Mockito.any(), Mockito.any())).thenReturn(SearchServerManager.ELASTICSEARCH);

		SearchServerManager instance = new SearchServerManager(propertiesManager);
		instance.initialize();
		BaseSearchManager searchManager = instance.getSearchServer();
		if (!(searchManager instanceof ElasticSearchManager)) {
			fail("Expected Elasticsearch Manager");
		}
	}

	/**
	 * Test of initialize method, of class SearchServerManager.
	 */
	@Test
	public void testInitializeSolr()
	{
		System.out.println("initialize");

		PropertiesManager propertiesManager = Mockito.mock(PropertiesManager.class);
		Mockito.when(propertiesManager.getValue(Mockito.any(), Mockito.any())).thenReturn(SearchServerManager.SOLR);

		SearchServerManager instance = new SearchServerManager(propertiesManager);
		instance.initialize();
		BaseSearchManager searchManager = instance.getSearchServer();
		if (!(searchManager instanceof SolrManager)) {
			fail("Expected Solr Manager");
		}
	}

	/**
	 * Test of initialize method, of class SearchServerManager.
	 */
	@Test
	public void testInitializeDefault()
	{
		System.out.println("initialize");

		PropertiesManager propertiesManager = Mockito.mock(PropertiesManager.class);
		Mockito.when(propertiesManager.getValue(Mockito.any(), Mockito.any())).thenReturn("junk");

		SearchServerManager instance = new SearchServerManager(propertiesManager);
		instance.initialize();
		BaseSearchManager searchManager = instance.getSearchServer();
		if (!(searchManager instanceof ElasticSearchManager)) {
			fail("Expected Elasticsearch Manager");
		}
	}

	/**
	 * Test of shutdown method, of class SearchServerManager.
	 */
	@Test
	public void testShutdown()
	{
		System.out.println("shutdown");

		PropertiesManager propertiesManager = Mockito.mock(PropertiesManager.class);
		Mockito.when(propertiesManager.getValue(Mockito.any(), Mockito.any())).thenReturn(SearchServerManager.ELASTICSEARCH);

		SearchServerManager instance = new SearchServerManager(propertiesManager);
		instance.shutdown();

		if (instance.isStarted()) {
			fail("Expected to not be started");
		}

	}

}
