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

import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class Content
{

	private String id;
	private String type = "page";
	private String title;
	private Space space;
	private ContentBody body;
	private List<Ancestor> ancestors;

	public Content()
	{
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

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Space getSpace()
	{
		return space;
	}

	public void setSpace(Space space)
	{
		this.space = space;
	}

	public ContentBody getBody()
	{
		return body;
	}

	public void setBody(ContentBody body)
	{
		this.body = body;
	}

	public List<Ancestor> getAncestors()
	{
		return ancestors;
	}

	public void setAncestors(List<Ancestor> ancestors)
	{
		this.ancestors = ancestors;
	}

}
