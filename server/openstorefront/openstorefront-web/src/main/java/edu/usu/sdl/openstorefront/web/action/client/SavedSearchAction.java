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
package edu.usu.sdl.openstorefront.web.action.client;

import edu.usu.sdl.openstorefront.core.entity.SystemSearch;
import edu.usu.sdl.openstorefront.web.action.BaseAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

public class SavedSearchAction
		extends BaseAction
{

	private String savedSearchId;
	private SystemSearch savedSearch;
	private String searchRequest;

	@DefaultHandler
	public Resolution performSearch()
	{
		SystemSearch ss = new SystemSearch();
		ss.setSearchId(this.savedSearchId);
		this.savedSearch = (SystemSearch) ss.find();
		if (savedSearch != null) {
			setSearchRequest(savedSearch.getSearchRequest());
			return new ForwardResolution("/WEB-INF/securepages/main/searchResults.jsp");
		}
		return new ErrorResolution(404);
	}

	public String getSearchRequest()
	{
		return searchRequest;
	}

	public void setSearchRequest(String searchRequest)
	{
		this.searchRequest = searchRequest;
	}

	public SystemSearch getSavedSearch()
	{
		return savedSearch;
	}

	public void setSavedSearch(SystemSearch savedSearch)
	{
		this.savedSearch = savedSearch;
	}

	public String getSavedSearchId()
	{
		return savedSearchId;
	}

	public void setSavedSearchId(String savedSearchId)
	{
		this.savedSearchId = savedSearchId;
	}

}
