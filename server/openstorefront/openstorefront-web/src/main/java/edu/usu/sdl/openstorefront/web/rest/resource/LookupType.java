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
import edu.usu.sdl.openstorefront.doc.RequireAdmin;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.PersistenceService;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.service.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.storage.model.LookupEntity;
import edu.usu.sdl.openstorefront.util.ServiceUtil;
import edu.usu.sdl.openstorefront.web.rest.model.FilterQueryParams;
import edu.usu.sdl.openstorefront.web.rest.model.RestListResponse;
import edu.usu.sdl.openstorefront.web.viewmodel.LookupModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;

/**
 * Lookup Entities
 *
 * @author dshurtleff
 */
@Path("v1/resource/lookuptypes")
@APIDescription("A lookup entity provide a set of valid values for a given entity. For Example: this can be used to fill drop-downs with values.")
public class LookupType
		extends BaseResource
{

	private static final Logger log = Logger.getLogger(LookupType.class.getName());

	@GET
	@APIDescription("Get a list of available lookup entities")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	public RestListResponse listEntitiies()
	{
		List<LookupModel> lookupModels = new ArrayList<>();

		Collection<Class<?>> entityClasses = DBManager.getConnection().getEntityManager().getRegisteredEntities();
		for (Class entityClass : entityClasses) {
			if (ServiceUtil.LOOKUP_ENTITY.equals(entityClass.getSimpleName()) == false) {
				if (ServiceUtil.isSubLookupEntity(entityClass)) {
					File codeFile = FileSystemManager.getImportLookup(entityClass.getSimpleName() + ".csv");
					if (codeFile.exists()) {
						LookupModel lookupModel = new LookupModel();

						lookupModel.setCode(entityClass.getSimpleName());
						APIDescription aPIDescription = (APIDescription) entityClass.getAnnotation(APIDescription.class);
						if (aPIDescription != null) {
							lookupModel.setDescription(aPIDescription.value());
						}
						lookupModels.add(lookupModel);
					}
				}
			}
		}
		return sendListResponse(lookupModels);
	}

	@GET
	@APIDescription("Get a entity type codes")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupEntity.class)
	@Path("/{entity}")
	public List<LookupEntity> getEntityValues(
			@PathParam("entity")
			@RequiredParam String entityName,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		checkEntity(entityName);

		boolean all = false;
		if (LookupEntity.ACTIVE_STATUS.equals(filterQueryParams.getStatus()) == false) {
			all = true;
		}
		List<LookupEntity> lookups = new ArrayList<>();
		try {
			Class lookupClass = Class.forName(DBManager.ENTITY_MODEL_PACKAGE + "." + entityName);
			lookups = service.getLookupService().findLookup(lookupClass, all);
		} catch (ClassNotFoundException e) {
			throw new OpenStorefrontRuntimeException(" (System Issue) Unable to find entity: " + entityName, "System error...contact support.", e);
		}
		return lookups;
	}

//	@POST
//	@RequireAdmin
//	@APIDescription("Adds a new code to a given entity")
//	@Produces({MediaType.APPLICATION_JSON})
//	@DataType(LookupModel.class)
//	@Path("/{entity}")
//	public RestListResponse getEntityValues(
//			@PathParam("entity")
//			@RequiredParam String entityName,
//			@BeanParam FilterQueryParams filterQueryParams)
//	{
//		checkEntity(entityName);
//
//		boolean all = false;
//		if (LookupEntity.ACTIVE_STATUS.equals(filterQueryParams.getStatus()) == false) {
//			all = true;
//		}
//		List<LookupEntity> lookups = new ArrayList<>();
//		try {
//			Class lookupClass = Class.forName(DBManager.ENTITY_MODEL_PACKAGE + "." + entityName);
//			lookups = service.getLookupService().findLookup(lookupClass, all);
//		} catch (ClassNotFoundException e) {
//			throw new OpenStorefrontRuntimeException(" (System Issue) Unable to find entity: " + entityName, "System error...contact support.", e);
//		}
//		return sendListResponse(lookups);
//	}
	@GET
	@APIDescription("Get a entity type detail for the given id")
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{entity}/{code}")
	public LookupEntity getEntityValues(
			@PathParam("entity")
			@RequiredParam String entityName,
			@PathParam("code")
			@RequiredParam String code)
	{
		if (StringUtils.isBlank(code)) {
			throw new OpenStorefrontRuntimeException("Id is a required field");
		}

		LookupEntity lookupEntity = null;
		checkEntity(entityName);
		PersistenceService persistenceService = service.getPersistenceService();
		try {
			Class lookupClass = Class.forName(DBManager.ENTITY_MODEL_PACKAGE + "." + entityName);
			Object value = persistenceService.findById(lookupClass, code);
			if (value != null) {
				lookupEntity = (LookupEntity) persistenceService.unwrapProxyObject(lookupClass, value);
			}
		} catch (ClassNotFoundException e) {
			throw new OpenStorefrontRuntimeException("(System Issue) Unable to find entity: " + entityName, "System error...contact support.", e);
		}
		return lookupEntity;
	}

	@PUT
	@APIDescription("Updates descriptions for a given entity and code.")
	@Consumes({MediaType.APPLICATION_JSON})
	@RequireAdmin
	@Path("/{entity}/{code}")
	public LookupEntity updateEntityValue(
			@PathParam("entity")
			@RequiredParam String entityName,
			@PathParam("code")
			@RequiredParam String code)
	{
		LookupEntity lookupEntity = null;

		return lookupEntity;
	}

//	@PUT
//	@APIDescription("Updates descriptions for a given entity and code.")
//	@Consumes({MediaType.APPLICATION_JSON})
//	@RequireAdmin
//	@Path("/{entity}/{code}")
//	public LookupTypeEntity updateEntityValue(
//			@PathParam("entity")
//			@RequiredParam String entityName,
//			@PathParam("code")
//			@RequiredParam String code)
//	{
//		LookupTypeEntity lookupTypeEntity = null;
//		checkEntity(entityName);
//		try {
//			Class lookupClass = Class.forName(DBManager.ENTITY_MODEL_PACKAGE + "." + entityName);
//			lookupTypeEntity = (LookupTypeEntity) service.getPersistenceService().findById(lookupClass, code);
//		} catch (ClassNotFoundException e) {
//			throw new OpenStorefrontRuntimeException(" (System Issue) Unable to find entity: " + entityName, "System error...contact support.", e);
//		}
//		return lookupTypeEntity;
//	}
	private void checkEntity(String entityName)
	{
		boolean valid = false;
		if (ServiceUtil.LOOKUP_ENTITY.equals(entityName) == false) {
			try {
				Class lookupClass = Class.forName(DBManager.ENTITY_MODEL_PACKAGE + "." + entityName);
				valid = ServiceUtil.isSubLookupEntity(lookupClass);
			} catch (ClassNotFoundException e) {
				valid = false;
			}
		}
		if (!valid) {
			throw new OpenStorefrontRuntimeException("Lookup Type not found", "Check entity name passed in.");
		}
	}

}
