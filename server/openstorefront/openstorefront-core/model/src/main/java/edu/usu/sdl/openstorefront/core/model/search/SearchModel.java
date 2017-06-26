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
package edu.usu.sdl.openstorefront.core.model.search;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class SearchModel
		implements Serializable
{

	private String sortField;
	private String sortDirection = OpenStorefrontConstant.SORT_ASCENDING;
	private int startOffset;
	private int max = Integer.MAX_VALUE;
	private String userSessionKey;

	@ConsumeField
	@DataType(SearchElement.class)
	private List<SearchElement> searchElements = new ArrayList<>();

	public SearchModel()
	{
	}

	public String searchKey()
	{
		StringBuilder key = new StringBuilder();
		key.append(toString());
		for (SearchElement searchElement : searchElements) {
			key.append(searchElement.toString());
		}
		return key.toString();
	}

	@Override
	public String toString()
	{
		return "SearchModel{" + "sortField=" + sortField + ", sortDirection=" + sortDirection + ", startOffset=" + startOffset + ", max=" + max + '}';
	}

	public String getSortField()
	{
		return sortField;
	}

	public void setSortField(String sortField)
	{
		this.sortField = sortField;
	}

	public String getSortDirection()
	{
		return sortDirection;
	}

	public void setSortDirection(String sortDirection)
	{
		this.sortDirection = sortDirection;
	}

	public int getStartOffset()
	{
		return startOffset;
	}

	public void setStartOffset(int startOffset)
	{
		this.startOffset = startOffset;
	}

	public int getMax()
	{
		return max;
	}

	public void setMax(int max)
	{
		this.max = max;
	}

	public List<SearchElement> getSearchElements()
	{
		return searchElements;
	}

	public void setSearchElements(List<SearchElement> searchElements)
	{
		this.searchElements = searchElements;
	}

	public String getUserSessionKey()
	{
		return userSessionKey;
	}

	public void setUserSessionKey(String userSessionKey)
	{
		this.userSessionKey = userSessionKey;
	}

}
