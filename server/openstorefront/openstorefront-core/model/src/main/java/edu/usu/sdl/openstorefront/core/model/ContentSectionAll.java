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
package edu.usu.sdl.openstorefront.core.model;

import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.ContentSection;
import edu.usu.sdl.openstorefront.core.entity.ContentSubSection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ContentSectionAll
{

	@ConsumeField
	private ContentSection section;

	@ConsumeField
	@DataType(ContentSubSection.class)
	private List<ContentSubSection> subsections = new ArrayList<>();

	public ContentSectionAll()
	{
	}

	public ContentSection getSection()
	{
		return section;
	}

	public void setSection(ContentSection section)
	{
		this.section = section;
	}

	public List<ContentSubSection> getSubsections()
	{
		return subsections;
	}

	public void setSubsections(List<ContentSubSection> subsections)
	{
		this.subsections = subsections;
	}

}
