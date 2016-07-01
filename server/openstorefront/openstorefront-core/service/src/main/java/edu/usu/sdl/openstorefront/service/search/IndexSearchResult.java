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

import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class IndexSearchResult
{

	private List<SolrComponentModel> resultsList = new ArrayList<>();
	private List<ComponentSearchView> searchViews = new ArrayList<>();
	private long totalResults = 0;
	private float maxScore;

	public IndexSearchResult()
	{
	}

	public List<SolrComponentModel> getResultsList()
	{
		return resultsList;
	}

	public void setResultsList(List<SolrComponentModel> resultsList)
	{
		this.resultsList = resultsList;
	}

	public long getTotalResults()
	{
		return totalResults;
	}

	public void setTotalResults(long totalResults)
	{
		this.totalResults = totalResults;
	}

	public float getMaxScore()
	{
		return maxScore;
	}

	public void setMaxScore(float maxScore)
	{
		this.maxScore = maxScore;
	}

	public List<ComponentSearchView> getSearchViews()
	{
		return searchViews;
	}

	public void setSearchViews(List<ComponentSearchView> searchViews)
	{
		this.searchViews = searchViews;
	}

}
