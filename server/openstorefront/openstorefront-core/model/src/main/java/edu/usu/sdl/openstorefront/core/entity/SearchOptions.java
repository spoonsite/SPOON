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
import edu.usu.sdl.openstorefront.core.annotation.PK;

/**
 *
 * @author bmichaelis
 */
@APIDescription("Search options are set by the application administrator.")
public class SearchOptions
		extends StandardEntity<SearchOptions>
{
	private Boolean globalFlag;

	@PK(generated = true)
	private String searchOptionsId;

	@APIDescription("Flag for using Component organizations in searches")
	private Boolean canUseOrganizationsInSearch;

	@APIDescription("Flag for using Component name in searches")
	private Boolean canUseNameInSearch;

	@APIDescription("Flag for using Component Description in searches")
	private Boolean canUseDescriptionInSearch;
	
	public SearchOptions()
	{

	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);
		SearchOptions searchOptionsModel = (SearchOptions) entity;

		setGlobalFlag(searchOptionsModel.getGlobalFlag());
		setSearchOptionsId(searchOptionsModel.getSearchOptionsId());
		setCanUseOrganizationsInSearch(searchOptionsModel.getCanUseOrganizationsInSearch());
		setCanUseNameInSearch(searchOptionsModel.getCanUseNameInSearch());
		setCanUseDescriptionInSearch(searchOptionsModel.getCanUseDescriptionInSearch());
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
	
	public Boolean getCanUseOrganizationsInSearch()
	{
		return canUseOrganizationsInSearch;
	}

	public void setCanUseOrganizationsInSearch(Boolean canUseOrganizationsInSearch)
	{
		this.canUseOrganizationsInSearch = canUseOrganizationsInSearch;
	}

	public Boolean getCanUseNameInSearch()
	{
		return canUseNameInSearch;
	}

	public void setCanUseNameInSearch(Boolean canUseNameInSearch)
	{
		this.canUseNameInSearch = canUseNameInSearch;
	}

	public Boolean getCanUseDescriptionInSearch()
	{
		return canUseDescriptionInSearch;
	}

	public void setCanUseDescriptionInSearch(Boolean canUseDescriptionInSearch)
	{
		this.canUseDescriptionInSearch = canUseDescriptionInSearch;
	}
	
}
