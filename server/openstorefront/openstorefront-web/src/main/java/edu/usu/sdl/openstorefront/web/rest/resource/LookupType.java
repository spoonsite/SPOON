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

package edu.usu.sdl.openstorefront.web.rest.resource;

import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.web.rest.model.LookupTypeEntity;
import edu.usu.sdl.openstorefront.web.rest.model.FilterQueryParams;
import edu.usu.sdl.openstorefront.web.rest.model.RestListResponse;
import edu.usu.sdl.openstorefront.web.viewmodel.LookupModel;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *  Lookup Entities
 * @author dshurtleff
 */
@Path("v1/resource/lookuptypes")
@APIDescription("A lookup entity provide a set of valid values for a given entity. For Example: this can be used to fill drop-downs with values.")
public class LookupType
	extends BaseResource
{
	
	@GET
	@APIDescription("Get a list of available lookup entities")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	public RestListResponse listEntitiies()
	{
		List<LookupModel> lookupModels = new ArrayList<>();
	
		
		return sendListResponse(lookupModels);		
	}	
	
	@GET
	@APIDescription("Get a entity type codes")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@Path("/{entity}")
	public RestListResponse getEntityValues(
			@PathParam("entity") 
			@RequiredParam		
			String entityName,
			@BeanParam
			FilterQueryParams filterQueryParams)
	{
		List<LookupModel> lookupModels = new ArrayList<>();
	
		
		return sendListResponse(lookupModels);		
	}
	
	@GET
	@APIDescription("Get a entity type detail for the given id")
	@Produces({MediaType.APPLICATION_JSON})	
	@Path("/{entity}/{id}")
	public LookupTypeEntity getEntityValues(
			@PathParam("entity") 
			@RequiredParam		
			String entityName,
			@PathParam("id")
			@RequiredParam
			String id)
	{
		LookupTypeEntity lookupTypeEntity = null;
	
		
		return lookupTypeEntity;		
	}	
	
}
