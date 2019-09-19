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

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.search.SearchSuggestion;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.SearchFilters;
import edu.usu.sdl.openstorefront.core.view.SearchQuery;
import edu.usu.sdl.openstorefront.core.view.SearchResultAttribute;
import edu.usu.sdl.openstorefront.service.search.IndexSearchResult;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;

/**
 *
 * @author dshurtleff
 */
public abstract class BaseSearchManager
{

	protected static final String WILDCARD_CHARACTER = "*";

	/**
	 * Performs search
	 *
	 * @param indexQuery
	 * @return
	 */
	public abstract ComponentSearchWrapper search(SearchQuery searchQuery, FilterQueryParams filter);

	/**
	 * Performs the actual search
	 *
	 * @param query
	 * @param filter
	 * @return
	 */
	public abstract IndexSearchResult doIndexSearch(String query, FilterQueryParams filter);

	/**
	 * Performs the actual search
	 *
	 * @param query
	 * @param filter
	 * @param addtionalFieldsToReturn
	 * @return
	 */
	public abstract IndexSearchResult doIndexSearch(String query, FilterQueryParams filter, String[] addtionalFieldsToReturn);

	/**
	 * Version 2 of the search, used for the Vue site
	 * 
	 */
	public abstract SearchResponse indexSearchV2(SearchFilters searchFilters);

	/**
	 * Finds search suggestions
	 *
	 * @param query
	 * @param maxResult
	 * @return
	 */
	public abstract List<SearchSuggestion> searchSuggestions(String query, int maxResult, String componentType);

	/**
	 * Indexes components (Requires additional querying to build index)
	 *
	 * @param components
	 */
	public abstract void index(List<Component> components);

	/**
	 * These entities can be indexed more efficiently
	 *
	 * @param componentAlls
	 */
	public abstract void indexFullComponents(List<ComponentAll> componentAlls);

	/**
	 * Updates a single component in the elasticsearch index using a componentID
	 * 
	 * @param componentId
	 * @return UpdateResponse from elasticsearch
	 */
	public abstract UpdateResponse updateSingleComponent(String componentId);

	/**
	 * Updates a single component in the elasticsearch index using a component
	 * 
	 * @param component
	 * @return UpdateResponse from elasticsearch
	 */
	public abstract UpdateResponse updateSingleComponent(Component component);

	/**
	 * Delete an index by the Index id
	 *
	 * @param id
	 */
	public abstract void deleteById(String id);

	/**
	 * Deletes all indexes
	 */
	public abstract void deleteAll();

	/**
	 * Save all to index
	 */
	public abstract void saveAll();

	/**
	 * Re-indexes all records
	 */
	public abstract void resetIndexer();

	/**
	 * Handle Scoring in a consistent way
	 *
	 * @param query
	 * @param views
	 */
	public void updateSearchScore(String query, List<ComponentSearchView> views)
	{
		if (StringUtils.isNotBlank(query)) {
			String queryNoWild = query.replace(WILDCARD_CHARACTER, "").toLowerCase();
			for (ComponentSearchView view : views) {
				float score = 0;

				score += scoreName(view, queryNoWild);
				score += scoreOrganization(view, queryNoWild);
				score += scoreDescription(view, queryNoWild);
				score += scoreTags(view, queryNoWild);
				score += scoreAttributes(view, queryNoWild);

				score = score / 150f;
				if (score > 1) {
					score = 1;
				}

				view.setSearchScore(score);
			}
		}
	}

	private float scoreName(ComponentSearchView view, String queryNoWild)
	{
		float score = 0;
		if (StringUtils.isNotBlank(view.getName())
				&& view.getName().toLowerCase().contains(queryNoWild)) {
			score += 100;
		}
		return score;
	}

	private float scoreOrganization(ComponentSearchView view, String queryNoWild)
	{
		float score = 0;
		if (StringUtils.isNotBlank(view.getOrganization())
				&& view.getOrganization().toLowerCase().contains(queryNoWild)) {
			score += 50;
		}
		return score;
	}

	private float scoreDescription(ComponentSearchView view, String queryNoWild)
	{
		float score = 0;
		if (StringUtils.isNotBlank(view.getDescription())) {
			int count = StringUtils.countMatches(view.getDescription().toLowerCase(), queryNoWild);
			score += count * 5;
		}
		return score;
	}

	private float scoreTags(ComponentSearchView view, String queryNoWild)
	{
		float score = 0;
		for (ComponentTag tag : view.getTags()) {
			int count = StringUtils.countMatches(tag.getText().toLowerCase(), queryNoWild);
			score += count * 5;
		}
		return score;
	}

	private float scoreAttributes(ComponentSearchView view, String queryNoWild)
	{
		float score = 0;
		for (SearchResultAttribute attribute : view.getAttributes()) {
			int count = StringUtils.countMatches(attribute.getLabel().toLowerCase(), queryNoWild);
			score += count * 5;

			count = StringUtils.countMatches(attribute.getTypeLabel().toLowerCase(), queryNoWild);
			score += count * 5;
		}
		return score;
	}

}
