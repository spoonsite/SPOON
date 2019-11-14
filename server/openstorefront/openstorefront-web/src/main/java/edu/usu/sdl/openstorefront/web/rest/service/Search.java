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

import au.com.bytecode.opencsv.CSVWriter;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.model.TaskRequest;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.SearchOptions;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.model.search.AdvanceSearchResult;
import edu.usu.sdl.openstorefront.core.model.search.SearchModel;
import edu.usu.sdl.openstorefront.core.model.search.SearchSuggestion;
import edu.usu.sdl.openstorefront.core.sort.ComponentSearchViewComparator;
import edu.usu.sdl.openstorefront.core.sort.RecentlyAddedViewComparator;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.ListingStats;
import edu.usu.sdl.openstorefront.core.view.RecentlyAddedView;
import edu.usu.sdl.openstorefront.core.view.RestErrorModel;
import edu.usu.sdl.openstorefront.core.view.SearchQuery;
import edu.usu.sdl.openstorefront.core.view.statistic.ComponentRecordStatistic;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.service.search.SearchStatTable;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	private static final Logger LOG = Logger.getLogger(Search.class.getName());
	private static final String USER_SEARCH_KEY = "UserSearchKey";

	@Context
	private HttpServletRequest request;

	@GET
	@APIDescription("Searches for listing based on given parameters.  (Components, Articles)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentSearchView.class)
	public Response searchListing(
			@QueryParam("paging") @APIDescription("Returns the data wrapped for paging") boolean paging,
			@BeanParam SearchQuery query,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentSearchWrapper results = service.getSearchService().getSearchItems(query, filterQueryParams);
		results.getData().sort(new ComponentSearchViewComparator());
		if (paging) {
			return sendSingleEntityResponse(results);
		} else {
			GenericEntity<List<ComponentSearchView>> entity = new GenericEntity<List<ComponentSearchView>>(results.getData())
			{
			};
			return sendSingleEntityResponse(entity);
		}
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_SEARCH_UPDATE)
	@APIDescription("Get the search options for indexing. (Admin)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SearchOptions.class)
	@Path("/options")
	public Response updateSearchModel()
	{
		SearchOptions searchOptionsExample = new SearchOptions();
		SearchOptions searchOptions = searchOptionsExample.find();

		if (searchOptions == null) {
			// Return the default.
			searchOptions = new SearchOptions();
			searchOptions.setCanUseDescriptionInSearch(Boolean.TRUE);
			searchOptions.setCanUseNameInSearch(Boolean.TRUE);
			searchOptions.setCanUseOrganizationsInSearch(Boolean.TRUE);
		}

		return Response.ok(searchOptions).build();
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_SEARCH_UPDATE)
	@APIDescription("Update the search options for indexing.")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(SearchOptions.class)
	@Path("/options")
	public Response updateSearchModel(
			SearchOptions incomingSearchOptions)
	{
		ValidationResult validationResult = incomingSearchOptions.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}
		SearchOptions searchOptionsExample = new SearchOptions();
		searchOptionsExample.setGlobalFlag(Boolean.TRUE);
		searchOptionsExample.setActiveStatus(SearchOptions.ACTIVE_STATUS);
		SearchOptions searchOptions = searchOptionsExample.find();

		if (searchOptions == null) {
			searchOptions = new SearchOptions();
		}
		searchOptions.setCanUseDescriptionInSearch(incomingSearchOptions.getCanUseDescriptionInSearch());
		searchOptions.setCanUseNameInSearch(incomingSearchOptions.getCanUseNameInSearch());
		searchOptions.setCanUseOrganizationsInSearch(incomingSearchOptions.getCanUseOrganizationsInSearch());

		incomingSearchOptions.setGlobalFlag(Boolean.TRUE);
		//service.getSearchService().saveSearchOptions(searchOptions); // TODO: @Ryan Frazier unable to compile after merge

		return Response.ok(searchOptions).build();
	}

	@POST
	@APIDescription("Advance search for listing ")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(ComponentSearchView.class)
	@Path("/advance")
	public Response advanceSearch(
			@QueryParam("paging") @APIDescription("Returns the data wrapped for paging") boolean paging,
			@BeanParam FilterQueryParams filterQueryParams,
			SearchModel searchModel)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		if (searchModel == null) {
			RestErrorModel restErrorModel = new RestErrorModel();
			restErrorModel.getErrors().put("searchModel", "Search Model must be passed to this call.**See API documentation");
			return sendSingleEntityResponse(restErrorModel);
		}
		searchModel.setStartOffset(filterQueryParams.getOffset());
		searchModel.setMax(filterQueryParams.getMax());
		searchModel.setSortField(filterQueryParams.getSortField());
		searchModel.setSortDirection(filterQueryParams.getSortOrder());

		String userSearchKey = (String) request.getSession().getAttribute(USER_SEARCH_KEY);
		if (userSearchKey == null) {
			userSearchKey = StringProcessor.uniqueId();
			request.getSession().setAttribute(USER_SEARCH_KEY, userSearchKey);
		}
		searchModel.setUserSessionKey(userSearchKey);

		AdvanceSearchResult result = service.getSearchService().advanceSearch(searchModel);
		if (result.getValidationResult().valid()) {
			if (paging) {
				ComponentSearchWrapper searchWrapper = new ComponentSearchWrapper();
				searchWrapper.setData(result.getResults());
				searchWrapper.setResults(result.getResults().size());
				searchWrapper.setTotalNumber(result.getTotalNumber());
				searchWrapper.setMeta(result.getMeta());
				return sendSingleEntityResponse(searchWrapper);
			} else {
				GenericEntity<List<ComponentSearchView>> entity = new GenericEntity<List<ComponentSearchView>>(result.getResults())
				{
				};
				return sendSingleEntityResponse(entity);
			}
		} else {
			return sendSingleEntityResponse(result.getValidationResult().toRestError());
		}
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_SEARCH_DELETE)
	@APIDescription("Removes all indexes from the search engine")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/clearElasticsearch")
	public Response clearElasticsearch()
	{
		service.getSearchService().deleteAll();
		return Response.noContent().build();
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_SEARCH_UPDATE)
	@APIDescription("Removes all indexes from Elasticsearch and then reindexes current components and articles")
	@Path("/resetElasticsearch")
	public Response resetElasticsearch()
	{
		TaskRequest taskRequest = new TaskRequest();
		taskRequest.setAllowMultiple(false);
		taskRequest.setName("Resetting Indexer");
		taskRequest.setDetails("Reindexing components and articles");
		service.getAsyncProxy(service.getSearchService(), taskRequest).resetIndexer();

		// workaround to make attributes in the search filter reset, 
		// so that newly added attributes will appear
		SearchStatTable.setThereIsNewAttributeTypeSaved(true);
		
		return Response.ok().build();
	}

	@GET
	@APIDescription("Searches listing by architecture.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentSearchView.class)
	@Path("/attribute/{type}/{code}")
	public Response searchListing(
			@PathParam("type")
			@RequiredParam String type,
			@PathParam("code")
			@RequiredParam String code,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		AttributeCodePk pk = new AttributeCodePk();

		pk.setAttributeCode(code);
		pk.setAttributeType(type);

		List<ComponentSearchView> results = service.getSearchService().architectureSearch(pk, filterQueryParams);
		results.sort(new ComponentSearchViewComparator());
		GenericEntity<List<ComponentSearchView>> entity = new GenericEntity<List<ComponentSearchView>>(results)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Retrieves all possible search results.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentSearchView.class)
	@Path("/all")
	public List<ComponentSearchView> getAllForSearch()
	{
		List<ComponentSearchView> results = service.getSearchService().getAll();
		Collections.sort(results, new ComponentSearchViewComparator());
		return results;
	}

	@GET
	@APIDescription("Gets recently added items")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(RecentlyAddedView.class)
	@Path("/recent")
	public List<RecentlyAddedView> getRecentlyAdded(
			@DefaultValue("5")
			@QueryParam("max") int maxResults)
	{
		List<RecentlyAddedView> recentlyAddedViews = new ArrayList<>();

		//We only need to look at components now (all types)
		List<Component> components = service.getComponentService().findRecentlyAdded(maxResults);

		for (Component component : components) {
			recentlyAddedViews.add(RecentlyAddedView.toView(component));
		}

		recentlyAddedViews.sort(new RecentlyAddedViewComparator<>());
		if (recentlyAddedViews.size() > maxResults) {
			recentlyAddedViews = recentlyAddedViews.subList(0, maxResults);
		}

		return recentlyAddedViews;
	}

	@GET
	@APIDescription("Get top viewed entries")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ComponentRecordStatistic.class)
	@Path("/topviewed")
	public Response getTopViewed(
			@DefaultValue("5")
			@QueryParam("max") int maxResults)
	{
		List<ComponentRecordStatistic> topViewed = service.getComponentService().findTopViewedComponents(maxResults);

		GenericEntity<List<ComponentRecordStatistic>> entity = new GenericEntity<List<ComponentRecordStatistic>>(topViewed)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Get Listing Stats")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ListingStats.class)
	@Path("/stats")
	public Response getListingStats()
	{
		ListingStats listingStats = service.getComponentService().getComponentListingStats();
		return Response.ok(listingStats).build();
	}

	@POST
	@APIDescription("Export a set of entries")
	@Produces({"application/csv"})
	@Path("/export")
	public Response export(
			@FormParam("multipleIds")
			@RequiredParam List<String> ids
	)
	{
		StringWriter writer = new StringWriter();
		CSVWriter csvWriter = new CSVWriter(writer);

		String header[] = {
			"Name",
			"Organization",
			"Description",
			"Last Updated Dts",
			"Entry Type"
		};
		csvWriter.writeNext(header);

		SimpleDateFormat sdf = TimeUtil.standardDateFormater();
		List<ComponentSearchView> views = service.getComponentService().getSearchComponentList(ids);
		for (ComponentSearchView view : views) {
			String data[] = {
				view.getName(),
				view.getOrganization(),
				StringProcessor.stripHtml(view.getDescription()),
				sdf.format(view.getLastActivityDts()),
				view.getComponentTypeDescription()
			};
			csvWriter.writeNext(data);
		}
		try {
			csvWriter.close();
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "CsvWriter failed to close");
		}

		Response.ResponseBuilder response = Response.ok(writer.toString());
		response.header("Content-Type", "application/csv");
		response.header("Content-Disposition", "attachment; filename=\"searchResults.csv\"");
		return response.build();
	}

	@GET
	@APIDescription("Gets search suggestions")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SearchSuggestion.class)
	@Path("/suggestions")
	public List<SearchSuggestion> getSearchSuggestions(
			@QueryParam("query")
			@DefaultValue("*") String query,
			@QueryParam("max")
			@DefaultValue("6") int maxResults,
			@QueryParam("componentType") String componentType
	)
	{
		List<SearchSuggestion> suggestions = service.getSearchService().searchSuggestions(query, maxResults, componentType);
		return suggestions;
	}

}
