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
package edu.usu.sdl.openstorefront.web.rest.model;

import edu.usu.sdl.openstorefront.sort.BeanComparator;
import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

/**
 *
 * @author dshurtleff
 */
public class FilterQueryParams
{

	@QueryParam("max")
	@DefaultValue("20000000")
	private int max;

	@QueryParam("sortField")
	@DefaultValue("description")
	private String sortField;

	@QueryParam("sortOrder")
	@DefaultValue(OpenStorefrontConstant.SORT_DESCENDING)
	private String sortOrder;

	@QueryParam("offset")
	@DefaultValue("0")
	private int offset;

	@QueryParam("status")
	@DefaultValue(BaseEntity.ACTIVE_STATUS)
	private String status;

	public FilterQueryParams()
	{
	}

	public static FilterQueryParams defaultFilter()
	{
		FilterQueryParams filterQueryParams = new FilterQueryParams();
		filterQueryParams.setMax(Integer.MAX_VALUE);
		filterQueryParams.setOffset(0);
		filterQueryParams.setStatus(BaseEntity.ACTIVE_STATUS);
		filterQueryParams.setSortField("description");
		filterQueryParams.setSortOrder(OpenStorefrontConstant.SORT_DESCENDING);
		return filterQueryParams;
	}

	/**
	 * This will apply everything but status it assume that was applied all
	 * ready
	 *
	 * @param <T>
	 * @param data
	 * @return
	 */
	public <T> List<T> filter(List<T> data)
	{
		List<T> results = new ArrayList<>();

		//window
		if (offset < data.size() && max > 0) {
			int count = 0;
			for (int i = offset; i < data.size(); i++) {
				results.add(data.get(i));
				count++;
				if (count >= max) {
					break;
				}
			}
		}
		//sort
		Collections.sort(results, new BeanComparator<>(sortOrder, sortField));
		return results;
	}

	public int getMax()
	{
		return max;
	}

	public void setMax(int max)
	{
		this.max = max;
	}

	public String getSortField()
	{
		return sortField;
	}

	public void setSortField(String sortField)
	{
		this.sortField = sortField;
	}

	public String getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(String sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	public int getOffset()
	{
		return offset;
	}

	public void setOffset(int offset)
	{
		this.offset = offset;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

}
