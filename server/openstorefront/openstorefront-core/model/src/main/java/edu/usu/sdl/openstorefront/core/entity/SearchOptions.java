/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.PK;

/**
 *
 * @author bmichaelis
 * @author dshurtleff
 */
@APIDescription("Search options controls which fields the index search searches.")
public class SearchOptions
		extends StandardEntity<SearchOptions>
{

	@PK(generated = true)
	private String searchOptionsId;

	private Boolean globalFlag;

	@APIDescription("Flag for using Component organizations in searches")
	@ConsumeField
	private Boolean searchOrganization;

	@APIDescription("Flag for using Component name in searches")
	@ConsumeField
	private Boolean searchName;

	@APIDescription("Flag for using Component Description in searches")
	@ConsumeField
	private Boolean searchDescription;

	@APIDescription("Flag for using Component Attributes in searches")
	@ConsumeField
	private Boolean searchAttributes;

	@APIDescription("Flag for using Component Tags in searches")
	@ConsumeField
	private Boolean searchTags;

	public SearchOptions()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);
		SearchOptions searchOptionsModel = (SearchOptions) entity;

		this.setSearchOrganization(searchOptionsModel.getSearchOrganization());
		this.setSearchName(searchOptionsModel.getSearchName());
		this.setSearchDescription(searchOptionsModel.getSearchDescription());
		this.setSearchAttributes(searchOptionsModel.getSearchAttributes());
		this.setSearchTags(searchOptionsModel.getSearchTags());
	}

	public Boolean getGlobalFlag()
	{
		return globalFlag;
	}

	public void setGlobalFlag(Boolean globalFlag)
	{
		this.globalFlag = globalFlag;
	}

	public String getSearchOptionsId()
	{
		return searchOptionsId;
	}

	public void setSearchOptionsId(String searchOptionsId)
	{
		this.searchOptionsId = searchOptionsId;
	}

	public Boolean getSearchOrganization()
	{
		return searchOrganization;
	}

	public void setSearchOrganization(Boolean searchOrganization)
	{
		this.searchOrganization = searchOrganization;
	}

	public Boolean getSearchName()
	{
		return searchName;
	}

	public void setSearchName(Boolean searchName)
	{
		this.searchName = searchName;
	}

	public Boolean getSearchDescription()
	{
		return searchDescription;
	}

	public void setSearchDescription(Boolean searchDescription)
	{
		this.searchDescription = searchDescription;
	}

	public Boolean getSearchAttributes()
	{
		return searchAttributes;
	}

	public void setSearchAttributes(Boolean searchAttributes)
	{
		this.searchAttributes = searchAttributes;
	}

	public Boolean getSearchTags()
	{
		return searchTags;
	}

	public void setSearchTags(Boolean searchTags)
	{
		this.searchTags = searchTags;
	}

}
