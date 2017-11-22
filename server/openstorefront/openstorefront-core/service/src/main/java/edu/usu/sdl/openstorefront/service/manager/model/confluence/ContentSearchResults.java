/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.manager.model.confluence;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author dshurtleff
 */
public class ContentSearchResults
{

	private List<ContentSearchResult> results = new ArrayList<>();
	private int start;
	private int limit;
	private int size;

	@XmlElement(name = "_links", required = false)
	private Links links;

	public ContentSearchResults()
	{
	}

	public List<ContentSearchResult> getResults()
	{
		return results;
	}

	public void setResults(List<ContentSearchResult> results)
	{
		this.results = results;
	}

	public int getStart()
	{
		return start;
	}

	public void setStart(int start)
	{
		this.start = start;
	}

	public int getLimit()
	{
		return limit;
	}

	public void setLimit(int limit)
	{
		this.limit = limit;
	}

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	public Links getLinks()
	{
		return links;
	}

	public void setLinks(Links links)
	{
		this.links = links;
	}

}
