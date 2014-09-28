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
package edu.usu.sdl.openstorefront.service.query;

import edu.usu.sdl.openstorefront.storage.model.BaseEntity;

/**
 * Query by Example model
 *
 * @author dshurtleff
 * @param <T>
 */
public class QueryByExample<T extends BaseEntity>
{

	private QueryType queryType = QueryType.SELECT;
	private T example;
	private Integer firstResult;
	private Integer maxResults;
	private String distinctField;
	private Integer timeout;
	private TimeoutStrategy timeoutStrategy = TimeoutStrategy.RETURN;
	private boolean parallelQuery;
	private boolean returnNonProxied = true;

	public QueryByExample()
	{
	}

	public QueryByExample(T example)
	{
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

}
