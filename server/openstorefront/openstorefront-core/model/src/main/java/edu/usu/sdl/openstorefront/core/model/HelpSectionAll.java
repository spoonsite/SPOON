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
package edu.usu.sdl.openstorefront.core.model;

import edu.usu.sdl.openstorefront.core.entity.HelpSection;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the help hierarchy
 *
 * @author dshurtleff
 */
public class HelpSectionAll
{

	private HelpSection helpSection;
	private List<HelpSectionAll> childSections = new ArrayList<>();

	public HelpSectionAll()
	{
	}

	public HelpSection getHelpSection()
	{
		return helpSection;
	}

	public void setHelpSection(HelpSection helpSection)
	{
		this.helpSection = helpSection;
	}

	public List<HelpSectionAll> getChildSections()
	{
		return childSections;
	}

	public void setChildSections(List<HelpSectionAll> childSections)
	{
		this.childSections = childSections;
	}
	
}
