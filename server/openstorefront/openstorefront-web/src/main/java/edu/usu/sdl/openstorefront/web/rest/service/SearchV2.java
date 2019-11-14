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

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.entity.AttributeSearchType;
import edu.usu.sdl.openstorefront.core.view.SearchFilterOptions;
import edu.usu.sdl.openstorefront.core.view.SearchFilters;
import edu.usu.sdl.openstorefront.service.manager.SearchServerManager;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.elasticsearch.action.search.SearchResponse;

/**
 * Search Service
 *
 * @author gfowler
 */
@Path("v2/service/search")
@APIDescription("Provides access to search listing in the application")
public class SearchV2
		extends BaseResource
{
	private static final Logger LOG = Logger.getLogger(SearchV2.class.getName());

	@Context
    private HttpServletRequest request;
    
    @POST
	@APIDescription("Searches for listing based on given parameters")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response searchListing(SearchFilters searchFilters)
	{
		ValidationResult validationResult = searchFilters.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ObjectMapper mapper = new ObjectMapper();

		try {
			AttributeSearchType[] attributeSearchType = mapper.readValue(
					searchFilters.getAttributes(),
					TypeFactory.defaultInstance().constructArrayType(AttributeSearchType.class));
			
			ArrayList<AttributeSearchType> list = new ArrayList<AttributeSearchType>(Arrays.asList(attributeSearchType));

			searchFilters.setAttributeSearchType(list);

			SearchResponse searchResponse = SearchServerManager.getInstance().getSearchServer()
					.indexSearchV2(searchFilters);

			if (searchResponse != null) {
				return Response.ok(searchResponse.toString()).build();
			} else {
				return Response.ok("Search was not formatted correctly").build();
			}
		} catch (JsonProcessingException ex) {
			LOG.log(Level.SEVERE, null, ex);
			return Response.ok("Search was not formatted correctly").build();
		}
	}
}