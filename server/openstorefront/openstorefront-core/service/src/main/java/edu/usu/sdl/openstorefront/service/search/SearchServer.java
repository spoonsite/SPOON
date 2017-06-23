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
package edu.usu.sdl.openstorefront.service.search;

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.model.search.SearchSuggestion;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.SearchQuery;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public interface SearchServer
{

	/**
	 * Performs search
	 *
	 * @param indexQuery
	 * @return
	 */
	public ComponentSearchWrapper search(SearchQuery searchQuery, FilterQueryParams filter);

	/**
	 * Performs the actual search
	 *
	 * @param query
	 * @param filter
	 * @return
	 */
	public IndexSearchResult doIndexSearch(String query, FilterQueryParams filter);

	/**
	 * Performs the actual search
	 *
	 * @param query
	 * @param filter
	 * @param addtionalFieldsToReturn
	 * @return
	 */
	public IndexSearchResult doIndexSearch(String query, FilterQueryParams filter, String[] addtionalFieldsToReturn);

	/**
	 * Finds search suggestions
	 *
	 * @param query
	 * @param maxResult
	 * @return
	 */
	public List<SearchSuggestion> searchSuggestions(String query, int maxResult, String componentType);

	/**
	 * Indexes components (Requires additional querying to build index)
	 *
	 * @param components
	 */
	public void index(List<Component> components);

	/**
	 * Delete an index by the Index id
	 *
	 * @param id
	 */
	public void deleteById(String id);

	/**
	 * Deletes all indexes
	 */
	public void deleteAll();

	/**
	 * Save all to index
	 */
	public void saveAll();

	/**
	 * Re-indexes all records
	 */
	public void resetIndexer();

}
