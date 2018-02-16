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
package edu.usu.sdl.openstorefront.web.test.search;

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.service.manager.SearchServerManager;
import edu.usu.sdl.openstorefront.service.search.IndexSearchResult;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class IndexServerTest
	extends BaseTestCase
{

	@Override
	public String getDescription()
	{
		return "Index Server Test";
	}

	@Override
	protected void initializeTest()
	{
		super.initializeTest(); 		
		SearchServerManager.getInstance().getSearchServer().deleteAll();	
	}

	
	@Override
	protected void runInternalTest()
	{		
		//Empty Search
		addResultsLines("Checking Empty index.");
		FilterQueryParams filterQueryParams = FilterQueryParams.defaultFilter();
		filterQueryParams.setSortField("name");
		
		IndexSearchResult searchResults = SearchServerManager.getInstance().getSearchServer().doIndexSearch("*", filterQueryParams);
		addResultsLines("Results found (There may be a delay): " + searchResults.getTotalResults());
		
		//Add Record
		ComponentAll componentAll = getTestComponent();
		List<Component> components = new ArrayList<>();
		components.add(componentAll.getComponent());
		SearchServerManager.getInstance().getSearchServer().index(components);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			Logger.getLogger(IndexServerTest.class.getName()).log(Level.SEVERE, null, ex);
		}
		//Search for record
		searchResults = SearchServerManager.getInstance().getSearchServer().doIndexSearch("Test", filterQueryParams);
		addResultsLines("Results found: " + searchResults.getTotalResults());
		if (searchResults.getTotalResults() == 0) {
			addFailLines("Unable to find indexed record");			
		}
		
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest(); 
		SearchServerManager.getInstance().getSearchServer().resetIndexer();
	}

}
