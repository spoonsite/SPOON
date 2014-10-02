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
package edu.usu.sdl.openstorefront.web.rest.service;

import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.sort.RecentlyAddedViewComparator;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentSearchView;
import edu.usu.sdl.openstorefront.web.rest.model.FilterQueryParams;
import edu.usu.sdl.openstorefront.web.rest.model.RecentlyAddedView;
import edu.usu.sdl.openstorefront.web.rest.model.SearchQuery;
import edu.usu.sdl.openstorefront.web.rest.model.SearchResult;
import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Search Service
 *
 * @author dshurtleff
 */
@Path("v1/service/search")
@APIDescription("Provides access to search listing in the application")
public class Search
		extends BaseResource
{

	@GET
	@APIDescription("Searches listing according to parameters.  (Components, Articles)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SearchResult.class)
	public List<SearchResult> searchListing(
			@BeanParam SearchQuery query,
			@BeanParam FilterQueryParams filter)
	{
		List<SearchResult> searchResults = new ArrayList<>();
		return searchResults;
	}

	@GET
	@APIDescription("Used to retrieve all possible search results.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Component.class)
	@Path("/all")
	public List<ComponentSearchView> getAllForSearch()
	{
		return service.getSearchService().getAll();
	}

	@GET
	@APIDescription("Gets the recently added items")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(RecentlyAddedView.class)
	@Path("/recent")
	public List<RecentlyAddedView> getRecentlyAdded(
			@DefaultValue("5")
			@QueryParam("max") int maxResults)
	{
		List<RecentlyAddedView> recentlyAddedViews = new ArrayList<>();

		List<Component> components = service.getComponentService().findRecentlyAdded(maxResults);
		List<AttributeCode> attributeCodes = service.getAttributeService().findRecentlyAddedArticles(maxResults);

		for (Component component : components) {
			RecentlyAddedView recentlyAddedView = new RecentlyAddedView();
			recentlyAddedView.setListingType(OpenStorefrontConstant.ListingType.COMPONENT);
			recentlyAddedView.setComponentId(component.getComponentId());
			recentlyAddedView.setName(component.getName());
			recentlyAddedView.setDescription(component.getDescription());
			recentlyAddedView.setAddedDts(component.getApprovedDts());
			recentlyAddedViews.add(recentlyAddedView);
		}

		for (AttributeCode attributeCode : attributeCodes) {
			RecentlyAddedView recentlyAddedView = new RecentlyAddedView();
			recentlyAddedView.setListingType(OpenStorefrontConstant.ListingType.ARTICLE);
			recentlyAddedView.setArticleAttributeType(attributeCode.getAttributeCodePk().getAttributeType());
			recentlyAddedView.setArticleAttributeCode(attributeCode.getAttributeCodePk().getAttributeCode());
			recentlyAddedView.setDescription(attributeCode.getDescription());
			recentlyAddedView.setName(attributeCode.getLabel());
			recentlyAddedView.setAddedDts(attributeCode.getUpdateDts());
			recentlyAddedViews.add(recentlyAddedView);
		}

		recentlyAddedViews.sort(new RecentlyAddedViewComparator<>());
		if (recentlyAddedViews.size() > maxResults) {
			recentlyAddedViews = recentlyAddedViews.subList(0, maxResults);
		}

		return recentlyAddedViews;
	}

}
