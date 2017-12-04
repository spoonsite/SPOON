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

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author dshurtleff
 */
public class ContentSearchResult
{

	private String id;
	private String type;
	private String status;
	private String title;

	@XmlElement(name = "_links", required = false)
	private Links links;

	@XmlElement(name = "_expandable", required = false)
	private SpaceExpandable expandable;

	public ContentSearchResult()
	{
	}

	public Links getLinks()
	{
		return links;
	}

	public void setLinks(Links links)
	{
		this.links = links;
	}

	public SpaceExpandable getExpandable()
	{
		return expandable;
	}

	public void setExpandable(SpaceExpandable expandable)
	{
		this.expandable = expandable;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

}
