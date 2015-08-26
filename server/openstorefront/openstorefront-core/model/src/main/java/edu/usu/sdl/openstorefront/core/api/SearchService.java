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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.view.ArticleView;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.SearchQuery;
import java.util.List;

/**
 * Search AsyncService Interface
 *
 * @author dshurtleff
 */
public interface SearchService
		extends AsyncService
{

	/**
	 * Get all components and articles
	 *
	 * @return
	 */
	public List<ComponentSearchView> getAll();

	/**
	 * Performs a search
	 *
	 * @param query
	 * @param filter
	 * @return
	 */
	public List<ComponentSearchView> getSearchItems(SearchQuery query, FilterQueryParams filter);

	/**
	 * This is used for an architecture search
	 *
	 * @param pk
	 * @param filter
	 * @return
	 */
	public List<ComponentSearchView> architectureSearch(AttributeCodePk pk, FilterQueryParams filter);

	/**
	 * Indexes a component
	 *
	 * @param components
	 */
	public void indexComponents(List<Component> components);

	/**
	 * Indexes an article
	 *
	 * @param articles
	 */
	public void indexArticles(List<ArticleView> articles);

	/**
	 * Indexes articles and Components
	 *
	 * @param components
	 * @param articles
	 */
	public void indexArticlesAndComponents(List<ArticleView> articles, List<Component> components);

	/**
	 * Removes a search index
	 *
	 * @param id
	 */
	public void deleteById(String id);

	/**
	 * Updates all records in the index
	 */
	public void saveAll();

	/**
	 * This clears all records in the index CAUTION: deletes everything!
	 */
	public void deleteAll();

	/**
	 * This will do a complete refresh
	 */
	public void resetIndexer();

}
