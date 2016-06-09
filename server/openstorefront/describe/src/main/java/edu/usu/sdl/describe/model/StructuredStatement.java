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
package edu.usu.sdl.describe.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author dshurtleff
 */
@Root(strict = false)
public class StructuredStatement
{
	@Element(name = "searchProvider", required = false)
	private SearchProvider searchProvider;

	@Element(name = "resource", required = false)
	private Resource resource;
	
	@Element(name="contentCollection", required = false)
	private ContentCollection contentCollection;
	
	public StructuredStatement()
	{
	}

	public SearchProvider getSearchProvider()
	{
		return searchProvider;
	}

	public void setSearchProvider(SearchProvider searchProvider)
	{
		this.searchProvider = searchProvider;
	}

	public Resource getResource()
	{
		return resource;
	}

	public void setResource(Resource resource)
	{
		this.resource = resource;
	}

	public ContentCollection getContentCollection()
	{
		return contentCollection;
	}

	public void setContentCollection(ContentCollection contentCollection)
	{
		this.contentCollection = contentCollection;
	}
	
}
