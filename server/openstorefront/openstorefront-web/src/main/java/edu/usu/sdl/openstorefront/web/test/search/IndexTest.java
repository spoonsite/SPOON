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

import edu.usu.sdl.openstorefront.service.transfermodel.ComponentAll;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentSearchView;
import edu.usu.sdl.openstorefront.web.rest.model.FilterQueryParams;
import edu.usu.sdl.openstorefront.web.rest.model.SearchQuery;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import edu.usu.sdl.openstorefront.web.test.component.ComponentTest;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class IndexTest
		extends BaseTestCase
{

	public IndexTest()
	{
		this.description = "Index Test";
	}

	@Override
	protected void runInternalTest()
	{
		ComponentAll componentAll = ComponentTest.createTestComponent();
		try {
			results.append("Adding Component Index...");
			service.getSearchService().addIndex(componentAll.getComponent());

			results.append("Searching Component Index...");
			SearchQuery query = new SearchQuery();
			query.setQuery(componentAll.getComponent().getName());
			List<ComponentSearchView> searchViews = service.getSearchService().getSearchItems(query, FilterQueryParams.defaultFilter());
			results.append("Results...").append("<br><br>");
			searchViews.forEach(view -> {
				results.append(view.getName()).append("   Type:").append(view.getListingType()).append("<br>");
			});
			if (searchViews.size() < 1) {
				failureReason.append("Unable able to find added component");
			}

		} finally {
			ComponentTest.deleteComponent(componentAll.getComponent().getComponentId());
		}
	}

}
