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
package edu.usu.sdl.openstorefront.service.api;

import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.web.rest.model.Article;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentSearchView;
import edu.usu.sdl.openstorefront.web.rest.model.FilterQueryParams;
import edu.usu.sdl.openstorefront.web.rest.model.SearchQuery;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public interface SearchService
{

	/**
	 * Find Recently Added
	 *
	 * @return
	 */
	public List<ComponentSearchView> getAll();

	/**
	 *
	 * @param query
	 * @param filter
	 * @return
	 */
	public List<ComponentSearchView> getSearchItems(SearchQuery query, FilterQueryParams filter);

	/**
	 *
	 * @param pk
	 * @param filter
	 * @return
	 */
	public List<ComponentSearchView> getSearchItems(AttributeCodePk pk, FilterQueryParams filter);

	/**
	 *
	 * @param component
	 */
	public void addComponent(Component component);

	/**
	 *
	 * @param article
	 */
	public void addArticle(Article article);

	/**
	 *
	 * @param id (Component Id or AttributeCodePK key)
	 */
	public void deleteIndex(String id);

	/**
	 *
	 */
	public void deleteAll();

}
