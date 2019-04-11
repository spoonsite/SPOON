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
package edu.usu.sdl.openstorefront.web.rest.resource;

import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.entity.SearchOptions;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
/**
 * Search Options Resource
 * 
 * @author gfowler
 */

 @Path("v1/resource/searchoptions")
 @APIDescription("Provides access to search options (Admin)")
 public class SearchOptionsResource
        extends BaseResource
{

    @GET
	@RequireSecurity(SecurityPermission.ADMIN_SEARCH_UPDATE)
	@APIDescription("Get the search options for indexing. (Admin)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SearchOptions.class)
	@Path("/global")
	public Response updateSearchModel()
	{
		SearchOptions searchOptions = service.getSearchService().getGlobalSearchOptions();

		return Response.ok(searchOptions).build();
	}
    
    @PUT
	@RequireSecurity(SecurityPermission.ADMIN_SEARCH_UPDATE)
	@APIDescription("Update the search options for indexing. (Admin)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(SearchOptions.class)
	@Path("/global")
	public Response updateSearchModel(
			SearchOptions incomingSearchOptions)
    {
        ValidationResult validationResult = incomingSearchOptions.validate();
        if(!validationResult.valid()){
            return sendSingleEntityResponse(validationResult.toRestError());
        }
       
        return Response.ok(service.getSearchService().saveGlobalSearchOptions(incomingSearchOptions)).build();
	}
	
	@GET
	@APIDescription("Get the search options for indexing. (User)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SearchOptions.class)
	@Path("/user/{username}")
	public Response updateSearchModelUser(
			@PathParam("username")
			@RequiredParam String username)
	{
		SearchOptions searchOptions = service.getSearchService().getGlobalSearchOptions();

		return Response.ok(searchOptions).build();
	}
    
    @PUT
	@APIDescription("Update the search options for indexing. (User)")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(SearchOptions.class)
	@Path("/user/{username}")
	public Response updateSearchModelUser(
			@PathParam("username")
			@RequiredParam String username,
			SearchOptions incomingSearchOptions)
    {
        ValidationResult validationResult = incomingSearchOptions.validate();
        if(!validationResult.valid()){
            return sendSingleEntityResponse(validationResult.toRestError());
        }
       
        return Response.ok(service.getSearchService().saveUserSearchOptions(incomingSearchOptions, username)).build();
    }

}