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

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 *
 * @author dshurtleff
 */
@Root(strict = false)
public class RelatedResource
{
	@Attribute(name="value")
	private String description;
	
	@Attribute(name="relationship")
	private String relationship;	
	
	@Attribute(name="href")
 	@Path("link")		
	private String link;
	
	@Attribute(name="type")
 	@Path("link")		
	private String type;

	public RelatedResource()
	{
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getRelationship()
	{
		return relationship;
	}

	public void setRelationship(String relationship)
	{
		this.relationship = relationship;
	}

}
