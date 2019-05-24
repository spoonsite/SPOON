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
package edu.usu.sdl.openstorefront.service.search;

import edu.usu.sdl.openstorefront.core.model.search.SearchElement;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class IndexSearchHandler
		extends BaseSearchHandler
{

	public IndexSearchHandler(SearchElement searchElement)
	{
		super(searchElement);
	}

	@Override
	protected ValidationResult internalValidate()
	{
		ValidationResult validationResult = new ValidationResult();
		//nothing to vaildate set the value to set the query
		return validationResult;
	}

	@Override
	public List<String> processSearch()
	{
		IndexSearchResult searchResult = serviceProxy.getSearchServicePrivate().doIndexSearch(searchElement.getValue(), FilterQueryParams.defaultFilter());

		List<String> results = new ArrayList<>();
		for (SolrComponentModel componentModel : searchResult.getResultsList()) {
			results.add(componentModel.getId());
		}

		return results;
	}

}
