/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.model.search.AdvanceSearchResult;
import edu.usu.sdl.openstorefront.core.model.search.SearchElement;
import edu.usu.sdl.openstorefront.core.model.search.SearchModel;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;

/**
 *
 * @author dshurtleff
 */
public class AdvanceSearchTest
		extends BaseTestCase
{

	public AdvanceSearchTest()
	{
		this.description = "Advance Search";
	}

	@Override
	protected void runInternalTest()
	{
		SearchModel searchModel = new SearchModel();

		SearchElement searchElement = new SearchElement();
		searchElement.setSearchType(SearchOperation.SearchType.COMPONENT);
		searchElement.setField("name");
		searchElement.setCaseInsensitive(true);
		searchElement.setStringOperation(SearchOperation.StringOperation.STARTS_LIKE);
		searchElement.setValue("a");

		searchModel.getSearchElements().add(searchElement);

		searchElement = new SearchElement();
		searchElement.setSearchType(SearchOperation.SearchType.COMPONENT);
		searchElement.setField("name");
		searchElement.setCaseInsensitive(true);
		searchElement.setStringOperation(SearchOperation.StringOperation.STARTS_LIKE);
		searchElement.setValue("b");
		searchElement.setMergeCondition(SearchOperation.MergeCondition.OR);

		searchModel.getSearchElements().add(searchElement);

		AdvanceSearchResult result = service.getSearchService().advanceSearch(searchModel);

		if (result.getValidationResult().valid()) {
			for (ComponentSearchView view : result.getResults()) {
				results.append(view.getName()).append("<br>");
			}
		} else {
			failureReason.append(result.getValidationResult().toString());
		}
	}

}
