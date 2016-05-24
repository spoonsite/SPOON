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
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 *
 * @author dshurtleff
 */
@Root(strict = false)
public class Resource
{
	@Element(required = false)
	private MetacardInfo metacardInfo;
	
	@Attribute(name = "value", required = false)
	@Path("identifier")		
	private String guid;
	
	@Element(name = "title", required = false)
	private Title title;
	
	@Attribute(name = "created", required = false)
	@Path("dates")			
	private String createDate;
	
	@Attribute(name = "value", required = false)
	@Path("type")				
	private String type;
	
	@Element(name="name", required = false)
	@Path("creator/unknown")				
	private String creatorName;
	
	@Element(name="contributor", required = false)
	private Contributor contributor;
	
	@Element(name="subjectCoverage", required = false)
	private SubjectCoverage subjectCoverage;
	
	@Element(name="geospatialCoverage", required = false)
	private GeospatialCoverage geospatialCoverage;

	public Resource()
	{
	}

	public String getGuid()
	{
		return guid;
	}

	public void setGuid(String guid)
	{
		this.guid = guid;
	}

	public MetacardInfo getMetacardInfo()
	{
		return metacardInfo;
	}

	public void setMetacardInfo(MetacardInfo metacardInfo)
	{
		this.metacardInfo = metacardInfo;
	}

	public String getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(String createDate)
	{
		this.createDate = createDate;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getCreatorName()
	{
		return creatorName;
	}

	public void setCreatorName(String creatorName)
	{
		this.creatorName = creatorName;
	}

	public Contributor getContributor()
	{
		return contributor;
	}

	public void setContributor(Contributor contributor)
	{
		this.contributor = contributor;
	}

	public SubjectCoverage getSubjectCoverage()
	{
		return subjectCoverage;
	}

	public void setSubjectCoverage(SubjectCoverage subjectCoverage)
	{
		this.subjectCoverage = subjectCoverage;
	}

	public GeospatialCoverage getGeospatialCoverage()
	{
		return geospatialCoverage;
	}

	public void setGeospatialCoverage(GeospatialCoverage geospatialCoverage)
	{
		this.geospatialCoverage = geospatialCoverage;
	}

	public void setTitle(Title title)
	{
		this.title = title;
	}

	public Title getTitle()
	{
		return title;
	}

}
