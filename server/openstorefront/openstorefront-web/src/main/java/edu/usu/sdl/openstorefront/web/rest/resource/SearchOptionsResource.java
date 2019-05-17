/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.SearchOptions;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/searchoptions")
@APIDescription("Handles index search preferences")
public class SearchOptionsResource
		extends BaseResource
{

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_SEARCH_UPDATE)
	@APIDescription("Get the global search options for indexing. (Admin)")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SearchOptions.class)
	@Path("/global")
	public Response getGlobalSearchModel()
	{
		SearchOptions searchOptions = service.getSearchService().getGlobalSearchOptions();
		return Response.ok(searchOptions).build();
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_SEARCH_UPDATE)
	@APIDescription("Update global search options for indexing.")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(SearchOptions.class)
	@Path("/global")
	public Response updateGlobalSearchModel(
			SearchOptions incomingSearchOptions)
	{
		ValidationResult validationResult = incomingSearchOptions.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}
		service.getSearchService().saveGlobalSearchOptions(incomingSearchOptions);
		SearchOptions searchOptions = service.getSearchService().getGlobalSearchOptions();
		return Response.ok(searchOptions).build();
	}

}
