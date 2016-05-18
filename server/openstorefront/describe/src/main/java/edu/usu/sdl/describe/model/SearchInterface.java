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

import edu.usu.sdl.describe.parser.SearchInterfaceConverter;
import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

/**
 *
 * @author dshurtleff
 */
@Root(strict = false)
@Convert(SearchInterfaceConverter.class)
public class SearchInterface
{
	@Element
	private Service service;
	
	private List<RelatedResource> relatedResources = new ArrayList<>();

	public SearchInterface()
	{
	}

	public Service getService()
	{
		return service;
	}

	public void setService(Service service)
	{
		this.service = service;
	}

	public List<RelatedResource> getRelatedResources()
	{
		return relatedResources;
	}

	public void setRelatedResources(List<RelatedResource> relatedResources)
	{
		this.relatedResources = relatedResources;
	}
	
	
	
}
