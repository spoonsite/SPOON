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

import edu.usu.sdl.openstorefront.core.entity.AttributeType;
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

	@Override
	protected void runInternalTest()
	{
		SearchModel searchModel = new SearchModel();

		results.append("Compound Component Search<br>");

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

		doSearch(searchModel);

		//TODO: Add Attribute and then add number search
		//Also the search below should use and added attribute
		results.append("<br>Attribute Search<br>");
		searchModel = new SearchModel();
		searchElement = new SearchElement();
		searchElement.setSearchType(SearchOperation.SearchType.ATTRIBUTE);
		searchElement.setKeyField(AttributeType.DI2ELEVEL);
		searchElement.setKeyValue("LEVEL1");
		searchElement.setStringOperation(SearchOperation.StringOperation.EQUALS);
		searchModel.getSearchElements().add(searchElement);

		doSearch(searchModel);

		results.append("<br>Tag Search<br>");
		searchModel = new SearchModel();
		searchElement = new SearchElement();
		searchElement.setSearchType(SearchOperation.SearchType.TAG);
		searchElement.setValue("Visualization");
		searchElement.setCaseInsensitive(true);
		searchElement.setStringOperation(SearchOperation.StringOperation.STARTS_LIKE);
		searchElement.setMergeCondition(SearchOperation.MergeCondition.OR);
		searchModel.getSearchElements().add(searchElement);

		searchElement = new SearchElement();
		searchElement.setSearchType(SearchOperation.SearchType.TAG);
		searchElement.setValue("map");
		searchElement.setCaseInsensitive(true);
		searchElement.setStringOperation(SearchOperation.StringOperation.STARTS_LIKE);
		searchElement.setMergeCondition(SearchOperation.MergeCondition.OR);
		searchModel.getSearchElements().add(searchElement);

		doSearch(searchModel);

		results.append("<br>Contact Search<br>");
		searchModel = new SearchModel();
		searchElement = new SearchElement();
		searchElement.setSearchType(SearchOperation.SearchType.CONTACT);
		searchElement.setField("email");
		searchElement.setValue("c");
		searchElement.setCaseInsensitive(true);
		searchElement.setStringOperation(SearchOperation.StringOperation.STARTS_LIKE);
		searchModel.getSearchElements().add(searchElement);

		doSearch(searchModel);

		results.append("<br>Review Search<br>");
		searchModel = new SearchModel();
		searchElement = new SearchElement();
		searchElement.setSearchType(SearchOperation.SearchType.REVIEW);
		searchElement.setField("recommend");
		searchElement.setValue("true");
		searchElement.setCaseInsensitive(true);
		searchElement.setStringOperation(SearchOperation.StringOperation.STARTS_LIKE);
		searchModel.getSearchElements().add(searchElement);

		doSearch(searchModel);

		results.append("<br>User Ratings<br>");
		searchModel = new SearchModel();
		searchElement = new SearchElement();
		searchElement.setSearchType(SearchOperation.SearchType.USER_RATING);
		searchElement.setValue("2");
		searchElement.setCaseInsensitive(true);
		searchElement.setNumberOperation(SearchOperation.NumberOperation.GREATERTHAN);
		searchModel.getSearchElements().add(searchElement);

		doSearch(searchModel);

	}

	private void doSearch(SearchModel searchModel)
	{
		AdvanceSearchResult result = service.getSearchService().advanceSearch(searchModel);

		if (result.getValidationResult().valid()) {
			for (ComponentSearchView view : result.getResults()) {
				results.append(view.getName()).append(" Rating: ").append(view.getAverageRating()).append("<br>");
			}
		} else {
			failureReason.append(result.getValidationResult().toString());
		}

	}

	@Override
	public String getDescription()
	{
		return "Advance Search";
	}

}
