/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.service.search.SearchServer;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class SearchServerManager
	implements Initializable
{
	private static final Logger log = Logger.getLogger(SearchServerManager.class.getName());
	
	private static final String SOLR = "solr";
	private static final String ELASTICSEARCH = "elasticsearch";
	
	private static SearchServer searchServer;

	public static SearchServer getSearchServer()
	{
		return searchServer;
	}
	
	public static void init()
	{	
		String searchImplementation = PropertiesManager.getValue(PropertiesManager.KEY_SEARCH_SERVER,  SOLR).toLowerCase();
		switch(searchImplementation) 
		{
			case SOLR:
			{
				searchServer = new SolrManager();
			}
			break;
				
			case ELASTICSEARCH:
			{
				searchServer = new ElasticSearchManager();
			}
			break;
			default:
			{
				searchServer = new SolrManager();				
			}			
		}
		((Initializable)searchServer).initialize();
	}
	
	public static void cleanup()
	{
		if (searchServer != null) {
			((Initializable)searchServer).shutdown();
		}
	}	
	
	@Override
	public void initialize()
	{
		SearchServerManager.init();
	}

	@Override
	public void shutdown()
	{
		SearchServerManager.cleanup();
	}

}
