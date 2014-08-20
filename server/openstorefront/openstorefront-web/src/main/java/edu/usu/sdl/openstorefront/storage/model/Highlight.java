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
package edu.usu.sdl.openstorefront.storage.model;

/**
 *
 * @author dshurtleff
 */
public class Highlight
		extends BaseEntity
{

	private String highlightId;
	private String highlightType;
	private String title;
	private String description;
	private String link;

	public Highlight()
	{
	}

	public String getHighlightId()
	{
		return highlightId;
	}

	public void setHighlightId(String highlightId)
	{
		this.highlightId = highlightId;
	}

	public String getHighlightType()
	{
		return highlightType;
	}

	public void setHighlightType(String highlightType)
	{
		this.highlightType = highlightType;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

}
