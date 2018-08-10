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
package edu.usu.sdl.openstorefront.web.rest.resource;

import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.SystemSearch;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.SystemSearchWrapper;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sourceforge.stripes.util.bean.BeanUtil;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/systemsearches")
@APIDescription("Public or system wide saved searches")
public class SystemSearchResource
		extends BaseResource
{

	@GET
	@APIDescription("Get a list of saved searches")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SystemSearchWrapper.class)
	public Response getAllSearches(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		SystemSearch systemSearch = new SystemSearch();
		if (Convert.toBoolean(filterQueryParams.getAll()) == false) {
			systemSearch.setActiveStatus(filterQueryParams.getStatus());
		}

		SystemSearch searchStartExample = new SystemSearch();
		searchStartExample.setCreateDts(filterQueryParams.getStart());

		SystemSearch searchEndExample = new SystemSearch();
		searchEndExample.setCreateDts(filterQueryParams.getEnd());

		QueryByExample queryByExample = new QueryByExample(systemSearch);

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(searchStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(searchEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());
		queryByExample.setSortDirection(filterQueryParams.getSortOrder());

		SystemSearch searchSortExample = new SystemSearch();
		Field sortField = ReflectionUtil.getField(searchSortExample, filterQueryParams.getSortField());
		if (sortField != null) {
			BeanUtil.setPropertyValue(sortField.getName(), searchSortExample, QueryByExample.getFlagForType(sortField.getType()));
			queryByExample.setOrderBy(searchSortExample);
		}

		List<SystemSearch> searches = service.getPersistenceService().queryByExample(queryByExample);

		SystemSearchWrapper searchWrapper = new SystemSearchWrapper();
		searchWrapper.getData().addAll(searches);
		searchWrapper.setTotalNumber(service.getPersistenceService().countByExample(queryByExample));

		return sendSingleEntityResponse(searchWrapper);
	}

	@GET
	@APIDescription("Get a saved search associated with a searchId")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SystemSearch.class)
	@Path("/{searchId}")
	public Response getSearch(
			@PathParam("searchId") String searchId
	)
	{
		SystemSearch systemSearch = new SystemSearch();
		systemSearch.setSearchId(searchId);
		systemSearch = (SystemSearch) systemSearch.find();
		return sendSingleEntityResponse(systemSearch);
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_SEARCH_CREATE)
	@APIDescription("Saves a search")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(SystemSearch.class)
	public Response createNewSearch(
			SystemSearch search
	)
	{
		return handleSave(search, true);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_SEARCH_UPDATE)
	@APIDescription("Updates a search")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(SystemSearch.class)
	@Path("/{searchId}")
	public Response updateSearch(
			@PathParam("searchId") String searchId,
			SystemSearch search
	)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		SystemSearch systemSearch = new SystemSearch();
		systemSearch.setSearchId(searchId);
		systemSearch = (SystemSearch) systemSearch.find();
		if (systemSearch != null) {
			search.setSearchId(searchId);
			response = handleSave(search, false);
		}

		return response;
	}

	private Response handleSave(SystemSearch search, boolean post)
	{
		ValidationResult validationResult = search.validate(true);
		if (validationResult.valid()) {

			search = service.getSearchService().saveSearch(search);

			if (post) {
				return Response.created(URI.create("v1/resource/systemsearches" + search.getSearchId())).entity(search).build();
			} else {
				return Response.ok(search).build();
			}
		} else {
			return sendSingleEntityResponse(validationResult.toRestError());
		}
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_SEARCH_DELETE)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Inactivates a search")
	@Path("/{searchId}")
	public Response deleteSearch(
			@PathParam("searchId") String searchId
	)
	{
		Response response = Response.status(Response.Status.FORBIDDEN).build();

		SystemSearch systemSearch = new SystemSearch();
		systemSearch.setSearchId(searchId);
		systemSearch = (SystemSearch) systemSearch.find();
		if (systemSearch != null) {
			service.getSearchService().inactivateSearch(searchId);
			response = Response.ok(systemSearch).build();
		}
		return response;
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_SEARCH_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Activates a search")
	@Path("/{searchId}/activate")
	public Response activateSearch(
			@PathParam("searchId") String searchId
	)
	{
		Response response = Response.status(Response.Status.FORBIDDEN).build();

		SystemSearch systemSearch = new SystemSearch();
		systemSearch.setSearchId(searchId);
		systemSearch = (SystemSearch) systemSearch.find();
		if (systemSearch != null) {
			service.getSearchService().activateSearch(searchId);
			response = Response.ok(systemSearch).build();
		}
		return response;
	}

}
