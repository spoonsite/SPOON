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
package edu.usu.sdl.openstorefront.web.test.search;

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.SearchQuery;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class IndexTest
		extends BaseTestCase
{

	@Override
	protected void runInternalTest()
	{
		ComponentAll componentAll = getTestComponent();

		results.append("Adding Component Index...<br>");
		List<Component> componentsToIndex = new ArrayList<>();
		componentsToIndex.add(componentAll.getComponent());
		service.getSearchService().indexComponents(componentsToIndex);

		try {
			//Delay make sure index is ready
			Thread.sleep(200);
		} catch (InterruptedException ex) {
			//ignore 
		}

		results.append("Searching Component Index...<br>");
		SearchQuery query = new SearchQuery();
		query.setQuery(componentAll.getComponent().getName());
		ComponentSearchWrapper searchViews = service.getSearchService().getSearchItems(query, FilterQueryParams.defaultFilter());
		results.append("Results...").append("<br><br>");
		searchViews.getData().forEach(view -> {
			results.append(view.getName()).append("   Type:").append(view.getListingType()).append("<br>");
		});
		if (searchViews.getData().size() < 1) {
			failureReason.append("Unable able to find added component<br>");
		}

	}

	@Override
	public String getDescription()
	{
		return "Index Test";
	}

}
