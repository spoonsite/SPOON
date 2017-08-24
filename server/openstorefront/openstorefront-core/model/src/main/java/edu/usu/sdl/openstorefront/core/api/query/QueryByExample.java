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
package edu.usu.sdl.openstorefront.core.api.query;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Query by Example model
 *
 * @author dshurtleff
 * @param <T>
 */
public class QueryByExample<T extends BaseEntity>
{

	public static final String LIKE_SYMBOL = "%";

	public static final String STRING_FLAG = "X";
	public static final int INT_FLAG = 1;
	public static final Boolean BOOLEAN_FLAG = Boolean.TRUE;
	public static final Date DATE_FLAG = new Date();

	private QueryType queryType = QueryType.SELECT;
	private T example;
	private GenerateStatementOption exampleOption = new GenerateStatementOptionBuilder().build();
	private Map<String, GenerateStatementOption> fieldOptions = new HashMap<>();
	private Integer firstResult;
	private Integer maxResults;
	private String distinctField;
	private Integer timeout;
	private TimeoutStrategy timeoutStrategy = TimeoutStrategy.RETURN;
	private boolean parallelQuery;
	private boolean returnNonProxied = true;
	private T orderBy;
	private String sortDirection = OpenStorefrontConstant.SORT_ASCENDING;
	private T groupBy;
	private T likeExample;
	private GenerateStatementOption likeExampleOption = new GenerateStatementOptionBuilder().setOperation(GenerateStatementOption.OPERATION_LIKE).build();
	private T inExample;
	private GenerateStatementOption inExampleOption = new GenerateStatementOptionBuilder().setOperation(GenerateStatementOption.OPERATION_IN).build();
	private List<WhereClause> extraWhereCauses = new ArrayList<>();
	private String additionalWhere;
	private Map<String, Object> extraParamMapping = new HashMap<>();

	public QueryByExample()
	{
	}

	public static Object getFlagForType(Class fieldType)
	{
		Object trigger = null;

		if (fieldType.getName().equals(Boolean.class.getName())) {
			trigger = Boolean.TRUE;
		} else if (fieldType.getName().equals(Integer.class.getName())) {
			trigger = 1;
		} else if (fieldType.getName().equals(BigDecimal.class.getName())) {
			trigger = BigDecimal.ONE;
		} else {
			try {
				trigger = fieldType.newInstance();
			} catch (InstantiationException | IllegalAccessException ex) {
				throw new OpenStorefrontRuntimeException("Unable to create a query trigger.", ex);
			}
		}
		return trigger;
	}

	public QueryByExample(T example)
	{
		this.example = example;
	}

	public QueryByExample(QueryType queryType, T example)
	{
		this.queryType = queryType;
		this.example = example;
	}

	public QueryType getQueryType()
	{
		return queryType;
	}

	public void setQueryType(QueryType queryType)
	{
		this.queryType = queryType;
	}

	public T getExample()
	{
		return example;
	}

	public void setExample(T example)
	{
		this.example = example;
	}

	public Integer getFirstResult()
	{
		return firstResult;
	}

	public void setFirstResult(Integer firstResult)
	{
		this.firstResult = firstResult;
	}

	public Integer getMaxResults()
	{
		return maxResults;
	}

	public void setMaxResults(Integer maxResults)
	{
		this.maxResults = maxResults;
	}

	public String getDistinctField()
	{
		return distinctField;
	}

	public void setDistinctField(String distinctField)
	{
		this.distinctField = distinctField;
	}

	public Integer getTimeout()
	{
		return timeout;
	}

	public void setTimeout(Integer timeout)
	{
		this.timeout = timeout;
	}

	public TimeoutStrategy getTimeoutStrategy()
	{
		return timeoutStrategy;
	}

	public void setTimeoutStrategy(TimeoutStrategy timeoutStrategy)
	{
		this.timeoutStrategy = timeoutStrategy;
	}

	public boolean isParallelQuery()
	{
		return parallelQuery;
	}

	public void setParallelQuery(boolean parallelQuery)
	{
		this.parallelQuery = parallelQuery;
	}

	public boolean isReturnNonProxied()
	{
		return returnNonProxied;
	}

	public void setReturnNonProxied(boolean returnNonProxied)
	{
		this.returnNonProxied = returnNonProxied;
	}

	public T getOrderBy()
	{
		return orderBy;
	}

	public void setOrderBy(T orderBy)
	{
		this.orderBy = orderBy;
	}

	public String getSortDirection()
	{
		return sortDirection;
	}

	public void setSortDirection(String sortDirection)
	{
		this.sortDirection = sortDirection;
	}

	public T getGroupBy()
	{
		return groupBy;
	}

	public void setGroupBy(T groupBy)
	{
		this.groupBy = groupBy;
	}

	public T getLikeExample()
	{
		return likeExample;
	}

	public void setLikeExample(T likeExample)
	{
		this.likeExample = likeExample;
	}

	public T getInExample()
	{
		return inExample;
	}

	public void setInExample(T inExample)
	{
		this.inExample = inExample;
	}

	public GenerateStatementOption getInExampleOption()
	{
		return inExampleOption;
	}

	public void setInExampleOption(GenerateStatementOption inExampleOption)
	{
		this.inExampleOption = inExampleOption;
	}

	public List<WhereClause> getExtraWhereCauses()
	{
		return extraWhereCauses;
	}

	public void setExtraWhereCauses(List<WhereClause> extraWhereCauses)
	{
		this.extraWhereCauses = extraWhereCauses;
	}

	public GenerateStatementOption getExampleOption()
	{
		return exampleOption;
	}

	public void setExampleOption(GenerateStatementOption exampleOption)
	{
		this.exampleOption = exampleOption;
	}

	public GenerateStatementOption getLikeExampleOption()
	{
		return likeExampleOption;
	}

	public void setLikeExampleOption(GenerateStatementOption likeExampleOption)
	{
		this.likeExampleOption = likeExampleOption;
	}

	public Map<String, GenerateStatementOption> getFieldOptions()
	{
		return fieldOptions;
	}

	public void setFieldOptions(Map<String, GenerateStatementOption> fieldOptions)
	{
		this.fieldOptions = fieldOptions;
	}

	public String getAdditionalWhere()
	{
		return additionalWhere;
	}

	public void setAdditionalWhere(String additionalWhere)
	{
		this.additionalWhere = additionalWhere;
	}

	public Map<String, Object> getExtraParamMapping()
	{
		return extraParamMapping;
	}

	public void setExtraParamMapping(Map<String, Object> extraParamMapping)
	{
		this.extraParamMapping = extraParamMapping;
	}

}
