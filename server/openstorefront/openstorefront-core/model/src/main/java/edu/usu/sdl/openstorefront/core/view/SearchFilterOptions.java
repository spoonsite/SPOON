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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.entity.SearchOptions;

/**
 *
 * @author gfowler
 */
@APIDescription("Search Options used to filter search results.")
public class SearchFilterOptions
{
	@APIDescription("Flag for using Component organizations in searches")
	@ConsumeField
	private Boolean canUseOrganizationsInSearch;

	@APIDescription("Flag for using Component name in searches")
	@ConsumeField
	private Boolean canUseNameInSearch;

	@APIDescription("Flag for using Component description in searches")
	@ConsumeField
	private Boolean canUseDescriptionInSearch;

	@APIDescription("Flag for using Component Tags in searches")
	@ConsumeField
	private Boolean canUseTagsInSearch;

	@APIDescription("Flag for using Component Attributes in searches")
	@ConsumeField
	private Boolean canUseAttributesInSearch;

	public SearchFilterOptions() {
	}

	public void setDefaultSearchOptions() {
		setCanUseDescriptionInSearch(Boolean.TRUE);
		setCanUseNameInSearch(Boolean.TRUE);
		setCanUseOrganizationsInSearch(Boolean.TRUE);
		setCanUseTagsInSearch(Boolean.TRUE);
		setCanUseAttributesInSearch(Boolean.TRUE);
	}

	public Boolean areAllOptionsOff() {
		return (!canUseOrganizationsInSearch && !canUseNameInSearch && !canUseDescriptionInSearch
				&& !canUseAttributesInSearch && !canUseTagsInSearch);
    }

    public SearchFilterOptions populateFromSearchOptions(SearchOptions searchOptions) {
        setCanUseDescriptionInSearch(searchOptions.getCanUseDescriptionInSearch());
		setCanUseNameInSearch(searchOptions.getCanUseNameInSearch());
		setCanUseOrganizationsInSearch(searchOptions.getCanUseOrganizationsInSearch());
		setCanUseTagsInSearch(searchOptions.getCanUseTagsInSearch());
		setCanUseAttributesInSearch(searchOptions.getCanUseAttributesInSearch());
        return this;
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

	public Boolean getCanUseTagsInSearch()
	{
		return canUseTagsInSearch;
	}

	public void setCanUseTagsInSearch(Boolean canUseTagsInSearch)
	{
		this.canUseTagsInSearch = canUseTagsInSearch;
	}

	public Boolean getCanUseAttributesInSearch()
	{
		return canUseAttributesInSearch;
	}

	public void setCanUseAttributesInSearch(Boolean canUseAttributesInSearch)
	{
		this.canUseAttributesInSearch = canUseAttributesInSearch;
	}
}
