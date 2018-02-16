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
package edu.usu.sdl.openstorefront.selenium.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.apiclient.rest.resource.SystemSearchClient;
import edu.usu.sdl.openstorefront.core.entity.SystemSearch;
import edu.usu.sdl.openstorefront.core.model.search.SearchElement;
import edu.usu.sdl.openstorefront.core.model.search.SearchModel;
import edu.usu.sdl.openstorefront.core.model.search.SearchOperation;
import edu.usu.sdl.openstorefront.core.view.SystemSearchWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class SystemSearchProvider
{

	private SystemSearchClient client;
	private List<String> systemSearchIds;

	public SystemSearchProvider(ClientAPI apiClient)
	{
		client = new SystemSearchClient(apiClient);
		systemSearchIds = new ArrayList<>();
	}

	public SystemSearch createSystemSearch(String searchName) throws JsonProcessingException
	{
		SearchElement element = new SearchElement();
		element.setSearchType(SearchOperation.SearchType.COMPONENT);
		element.setField(searchName);
		element.setValue("Selenium API public saved search value");
		element.setMergeCondition(SearchOperation.MergeCondition.OR);
		List<SearchElement> searchElements = new ArrayList<>();
		searchElements.add(element);

		SearchModel model = new SearchModel();
		model.setSearchElements(searchElements);
		ObjectMapper objMapper = new ObjectMapper();
		String request = objMapper.writeValueAsString(model);

		SystemSearch search = new SystemSearch();
		search.setSearchName("An API Test Admin Saved Search");
		search.setSearchRequest(request);

		SystemSearch apiAdminSearch = client.createNewSearch(search);
		systemSearchIds.add(apiAdminSearch.getSearchId());

		return apiAdminSearch;
	}

	public SystemSearch getSystemSearchByName(String searchName)
	{
		SystemSearchWrapper searchWrapper = client.getAllSearches(null);
		List<SystemSearch> activeSearches = searchWrapper.getData();

		for (SystemSearch search : activeSearches) {

			if (search.getSearchName().equals(searchName))
			{
				return search;
			}
		}
		
		return null;
	}
	
	public void registerSearchId(String searchId)
	{
		systemSearchIds.add(searchId);
	}

	public void cleanup()
	{
		for (String id : systemSearchIds) {

			client.deleteSearch(id);
		}
	}
}
