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
import edu.usu.sdl.openstorefront.core.entity.UserSavedSearch;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.UserSavedSearchWrapper;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
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
@Path("v1/resource/usersavedsearches")
@APIDescription("Saved searches")
public class UserSavedSearchResource
	extends BaseResource
{
	
	@GET
	@APIDescription("Get a list of saved searches")
	@RequireSecurity(SecurityPermission.ADMIN_SEARCH)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserSavedSearchWrapper.class)
	public Response getAllSearches(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		UserSavedSearch userSavedSearch = new UserSavedSearch();
		if (Convert.toBoolean(filterQueryParams.getAll()) == false) {
			userSavedSearch.setActiveStatus(filterQueryParams.getStatus());
		}

		UserSavedSearch userSearchStartExample = new UserSavedSearch();
		userSearchStartExample.setCreateDts(filterQueryParams.getStart());

		UserSavedSearch userSearchEndExample = new UserSavedSearch();
		userSearchEndExample.setCreateDts(filterQueryParams.getEnd());

		QueryByExample queryByExample = new QueryByExample(userSavedSearch);

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(userSearchStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(userSearchEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());
		queryByExample.setSortDirection(filterQueryParams.getSortOrder());

		UserSavedSearch userSearchSortExample = new UserSavedSearch();
		Field sortField = ReflectionUtil.getField(userSearchSortExample, filterQueryParams.getSortField());
		if (sortField != null) {
			BeanUtil.setPropertyValue(sortField.getName(), userSearchSortExample, QueryByExample.getFlagForType(sortField.getType()));
			queryByExample.setOrderBy(userSearchSortExample);
		}

		List<UserSavedSearch> userSavedSearches = service.getPersistenceService().queryByExample(queryByExample);

		UserSavedSearchWrapper userSavedSearchWrapper = new UserSavedSearchWrapper();
		userSavedSearchWrapper.getData().addAll(userSavedSearches);
		userSavedSearchWrapper.setTotalNumber(service.getPersistenceService().countByExample(queryByExample));

		return sendSingleEntityResponse(userSavedSearchWrapper);				
	}	
		
	@GET
	@APIDescription("Get saved searches for current user")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserSavedSearch.class)
	@Path("/user/current")
	public List<UserSavedSearch> getCurrentUserSearches()
	{
		UserSavedSearch userSavedSearch = new UserSavedSearch();
		userSavedSearch.setActiveStatus(UserSavedSearch.ACTIVE_STATUS);		
		userSavedSearch.setUsername(SecurityUtil.getCurrentUserName());		
		return userSavedSearch.findByExample();
	}	
	
	@GET
	@APIDescription("Get saved searches for a user")
	@RequireSecurity(SecurityPermission.ADMIN_SEARCH)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserSavedSearch.class)
	@Path("/user/{username}")
	public List<UserSavedSearch> getUserSearchesForUser(
		@PathParam("username") String username
	)
	{
		UserSavedSearch userSavedSearch = new UserSavedSearch();
		userSavedSearch.setActiveStatus(UserSavedSearch.ACTIVE_STATUS);		
		userSavedSearch.setUsername(username);		
		return userSavedSearch.findByExample();		
	}	
		
	@GET
	@APIDescription("Get saved search by id (must be admin or owner)")	
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserSavedSearch.class)
	@Path("/{searchId}")
	public Response getUserSearch(
		@PathParam("searchId") String userSearchId
	)
	{
		UserSavedSearch userSavedSearch = new UserSavedSearch();
		userSavedSearch.setUserSearchId(userSearchId);
		userSavedSearch = (UserSavedSearch) userSavedSearch.find();
		
		Response response = ownerCheck(userSavedSearch, SecurityPermission.ADMIN_SEARCH);
		if (response == null)
		{
			response = sendSingleEntityResponse(userSavedSearch);
		}		
		return response;
	}	
	
	@POST
	@APIDescription("Saves a search")	
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(UserSavedSearch.class)
	public Response createNewSearch(
		UserSavedSearch search	
	) 
	{
		search.setUsername(SecurityUtil.getCurrentUserName());
		return handleSave(search, true);
	}
	
	@PUT
	@APIDescription("Updates a search")	
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(UserSavedSearch.class)
	@Path("/{searchId}")
	public Response updateSearch(
		@PathParam("searchId") String searchId,	
		UserSavedSearch search	
	) 
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		
		UserSavedSearch userSavedSearch = new UserSavedSearch();
		userSavedSearch.setUserSearchId(searchId);
		userSavedSearch = (UserSavedSearch) userSavedSearch.find();
		if (userSavedSearch != null) {
			response = ownerCheck(userSavedSearch, SecurityPermission.ADMIN_SEARCH);
			if (response == null) {
				search.setUserSearchId(searchId);			
				
				//In this case we are not allowing to set the user name
				search.setUsername(userSavedSearch.getUsername());				
				response = handleSave(search, false);
			}
		}
		
		return response;
	}
	
	private Response handleSave(UserSavedSearch search, boolean post) 
	{
		ValidationModel validationModel = new ValidationModel(search);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
						
			search = service.getUserService().saveUserSearch(search);
			
			if (post) {
				return Response.created(URI.create("v1/resource/usersavedsearches" + search.getUserSearchId())).entity(search).build();
			} else {
				return Response.ok(search).build();
			}			
		} else {
			return sendSingleEntityResponse(validationResult.toRestError());
		}		
	}
	
	@DELETE
	@APIDescription("Deletes saved search (must be owner or admin).")
	@Path("/{searchId}")
	public Response deleteUserSearch(
		@PathParam("searchId") String userSearchId
	)
	{
		UserSavedSearch userSavedSearch = new UserSavedSearch();
		userSavedSearch.setUserSearchId(userSearchId);
		userSavedSearch = (UserSavedSearch) userSavedSearch.find();
		
		Response response = ownerCheck(userSavedSearch, SecurityPermission.ADMIN_SEARCH);
		if (response == null)
		{
			service.getUserService().deleteUserSearch(userSearchId);			
			response = Response.ok().build();
		}		
		return response;
	}
	
}
