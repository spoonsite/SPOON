/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Represents a section of help")
public class HelpSection
		extends BaseEntity<HelpSection>
{

	@PK(generated = true)
	@NotNull
	private String id;

	@NotNull
	private String title;

	@NotNull
	private String sectionNumber;

	@NotNull
	@APIDescription("Used to create the hierarchy of the help")
	private Integer headerLevel;
	private String content;
	private Boolean adminSection;
	private String permission;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public HelpSection()
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

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getSectionNumber()
	{
		return sectionNumber;
	}

	public void setSectionNumber(String sectionNumber)
	{
		this.sectionNumber = sectionNumber;
	}

	public Integer getHeaderLevel()
	{
		return headerLevel;
	}

	public void setHeaderLevel(Integer headerLevel)
	{
		this.headerLevel = headerLevel;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String context)
	{
		this.content = context;
	}

	public Boolean getAdminSection()
	{
		return adminSection;
	}

	public void setAdminSection(Boolean adminSection)
	{
		this.adminSection = adminSection;
	}

	public String getPermission()
	{
		return permission;
	}

	public void setPermission(String permission)
	{
		this.permission = permission;
	}

}
