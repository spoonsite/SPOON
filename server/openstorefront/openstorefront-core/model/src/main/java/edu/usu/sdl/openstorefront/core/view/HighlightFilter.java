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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class HighlightFilter
{

	public static final String FILTER_ALL = "ALL";

	@QueryParam("max")
	@DefaultValue("20000000")
	@Min(0)
	@Max(Integer.MAX_VALUE)
	private int max;

	@QueryParam("sortField")
	@DefaultValue("orderingPosition")
	@Size(min = 0, max = 255)
	@Sanitize(TextSanitizer.class)
	private String sortField;

	@QueryParam("sortOrder")
	@DefaultValue(OpenStorefrontConstant.SORT_ASCENDING)
	@Size(min = 0, max = 4)
	@Sanitize(TextSanitizer.class)
	private String sortOrder;

	@QueryParam("offset")
	@DefaultValue("0")
	@Min(0)
	@Max(Integer.MAX_VALUE)
	private int offset;

	@QueryParam("status")
	@DefaultValue(StandardEntity.ACTIVE_STATUS)
	@Size(min = 0, max = 3)
	@Sanitize(TextSanitizer.class)
	private String status;

	@QueryParam("all")
	@DefaultValue("false")
	private Boolean all;

	public ValidationResult validate()
	{
		ValidationModel validationModel = new ValidationModel(this);
		return ValidationUtil.validate(validationModel);
	}

	public static HighlightFilter defaultFilter()
	{
		HighlightFilter filterQueryParams = new HighlightFilter();
		filterQueryParams.setMax(Integer.MAX_VALUE);
		filterQueryParams.setOffset(0);
		filterQueryParams.setStatus(StandardEntity.ACTIVE_STATUS);
		filterQueryParams.setAll(false);
		filterQueryParams.setSortField("description");
		filterQueryParams.setSortOrder(OpenStorefrontConstant.SORT_ASCENDING);
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
		//sort
		if (StringUtils.isNotBlank(sortField)) {
			Collections.sort(data, new BeanComparator<>(sortOrder, sortField));
		}
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

	public Boolean getAll()
	{
		return all;
	}

	public void setAll(Boolean all)
	{
		this.all = all;
	}

}
