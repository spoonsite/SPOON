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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.annotation.SystemTable;
import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.entity.LookupEntity;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.sort.LookupComparator;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.GenericLookupEntity;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.apache.commons.lang.StringUtils;

/**
 * Lookup Entities
 *
 * @author dshurtleff
 */
@Path("v1/resource/lookuptypes")
@APIDescription("A lookup entity provides a set of valid values for a given entity. For Example: this can be used to fill drop-downs with values.")
public class LookupTypeResource
		extends BaseResource
{

	@GET
	@APIDescription("Get a list of available lookup entities")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@SuppressWarnings("squid:S1872")
	public List<LookupModel> listEntitiies(
			@QueryParam("systemTables") Boolean systemTables
	)
	{
		List<LookupModel> lookupModels = new ArrayList<>();

		Collection<Class<?>> entityClasses = DBManager.getInstance().getConnection().getEntityManager().getRegisteredEntities();
		for (Class entityClass : entityClasses) {
			if (ReflectionUtil.LOOKUP_ENTITY.equals(entityClass.getSimpleName()) == false) {
				if (ReflectionUtil.isSubLookupEntity(entityClass)) {
					boolean add = true;

					if (systemTables != null) {
						@SuppressWarnings("unchecked")
						SystemTable systemTable = (SystemTable) entityClass.getAnnotation(SystemTable.class);
						if (systemTables == false) {
							if (systemTable != null) {
								add = false;
							}
						} else if (systemTable == null) {
							add = false;
						}
					}

					if (add) {
						LookupModel lookupModel = new LookupModel();

						lookupModel.setCode(entityClass.getSimpleName());
						@SuppressWarnings("unchecked")
						APIDescription aPIDescription = (APIDescription) entityClass.getAnnotation(APIDescription.class);
						if (aPIDescription != null) {
							lookupModel.setDescription(aPIDescription.value());
						}
						lookupModels.add(lookupModel);
					}
				}
			}
		}
		return lookupModels;
	}

	@GET
	@APIDescription("Get entity type codes")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(value = GenericLookupEntity.class, actualClassName = "LookupEntity")
	@Path("/{entity}")
	@SuppressWarnings("unchecked")
	public Response getEntityValues(
			@PathParam("entity")
			@RequiredParam String entityName,
			@BeanParam FilterQueryParams filterQueryParams,
			@QueryParam("addSelect") boolean addSelect)
	{
		checkEntity(entityName);

		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		List<LookupEntity> lookups = new ArrayList<>();
		try {
			Class lookupClass = Class.forName(DBManager.getInstance().getEntityModelPackage() + "." + entityName);
			lookups = service.getLookupService().findLookup(lookupClass, filterQueryParams.getStatus());
		} catch (ClassNotFoundException e) {
			throw new OpenStorefrontRuntimeException(" (System Issue) Unable to find entity: " + entityName, "System error...contact support.", e);
		}
		lookups = filterQueryParams.filter(lookups);
		lookups.sort(new LookupComparator<>());

		if (addSelect) {
			LookupEntity select = new LookupEntity()
			{
			};
			select.setDescription("*Select*");
			lookups.add(select);
		}

		GenericEntity<List<LookupEntity>> entity = new GenericEntity<List<LookupEntity>>(lookups)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Exports codes in csv formt. POST to Upload.action?UploadLookup&entityName=entity and then the file to import codes (Requires Admin)")
	@RequireSecurity(SecurityPermission.ADMIN_LOOKUPS)
	@Produces("text/csv")
	@Path("/{entity}/export")
	@SuppressWarnings("unchecked")
	public Response exportEntityValues(
			@PathParam("entity")
			@RequiredParam String entityName,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		checkEntity(entityName);

		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		StringBuilder data = new StringBuilder();
		List<LookupEntity> lookups = new ArrayList<>();
		try {
			Class lookupClass = Class.forName(DBManager.getInstance().getEntityModelPackage() + "." + entityName);
			lookups = service.getLookupService().findLookup(lookupClass, filterQueryParams.getStatus());
		} catch (ClassNotFoundException e) {
			throw new OpenStorefrontRuntimeException(" (System Issue) Unable to find entity: " + entityName, "System error...contact support.", e);
		}
		lookups = filterQueryParams.filter(lookups);

		for (LookupEntity lookup : lookups) {
			data.append(lookup.export());
		}

		ResponseBuilder response = Response.ok(data.toString());
		response.header("Content-Type", "application/csv");
		response.header("Content-Disposition", "attachment; filename=\"" + entityName + ".csv\"");
		return response.build();
	}

	@GET
	@APIDescription("Get entity type codes.  Lighter Model for dropdown boxes")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(value = GenericLookupEntity.class, actualClassName = "LookupEntity")
	@Path("/{entity}/view")
	public Response getEntityValuesView(
			@PathParam("entity")
			@RequiredParam String entityName,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		checkEntity(entityName);

		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		List<LookupModel> lookupViews = new ArrayList<>();
		try {
			Class lookupClass = Class.forName(DBManager.getInstance().getEntityModelPackage() + "." + entityName);
			@SuppressWarnings("unchecked")
			List<LookupEntity> lookups = service.getLookupService().findLookup(lookupClass, filterQueryParams.getStatus());
			lookups.sort(new LookupComparator<>());
			for (LookupEntity lookupEntity : lookups) {
				LookupModel lookupModel = new LookupModel();
				lookupModel.setCode(lookupEntity.getCode());
				lookupModel.setDescription(lookupEntity.getDescription());
				lookupViews.add(lookupModel);
			}
		} catch (ClassNotFoundException e) {
			throw new OpenStorefrontRuntimeException(" (System Issue) Unable to find entity: " + entityName, "System error...contact support.", e);
		}
		lookupViews = filterQueryParams.windowData(lookupViews);
		GenericEntity<List<LookupModel>> entity = new GenericEntity<List<LookupModel>>(lookupViews)
		{
		};

		return sendSingleEntityResponse(entity);
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_LOOKUPS)
	@APIDescription("Adds a new code to a given entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{entity}")
	public Response postNewEntity(
			@PathParam("entity")
			@RequiredParam String entityName,
			GenericLookupEntity genericLookupEntity)
	{
		return handlePostPutCode(entityName, genericLookupEntity, true);
	}

	private Response handlePostPutCode(String entityName, LookupEntity lookupEntity, boolean post)
	{
		checkEntity(entityName);
		ValidationModel validationModel = new ValidationModel(lookupEntity);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			try {
				Class lookupClass = Class.forName(DBManager.getInstance().getEntityModelPackage() + "." + entityName);
				LookupEntity newLookupEntity = (LookupEntity) lookupClass.newInstance();
				newLookupEntity.setCode(lookupEntity.getCode());
				newLookupEntity.setDescription(lookupEntity.getDescription());
				newLookupEntity.setDetailedDescription(lookupEntity.getDetailedDescription());
				newLookupEntity.setSortOrder(lookupEntity.getSortOrder());

				newLookupEntity.setActiveStatus(LookupEntity.ACTIVE_STATUS);
				newLookupEntity.setCreateUser(SecurityUtil.getCurrentUserName());
				newLookupEntity.setUpdateUser(SecurityUtil.getCurrentUserName());
				service.getLookupService().saveLookupValue(newLookupEntity);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				throw new OpenStorefrontRuntimeException(" (System Issue) Unable to process entity. " + entityName, "System error...contact support.", e);
			}
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		LookupEntity lookupEntityCreated = service.getLookupService().getLookupEnity(entityName, lookupEntity.getCode());
		if (post) {
			return Response.created(URI.create("v1/resource/lookuptypes/"
					+ entityName + "/"
					+ StringProcessor.urlEncode(lookupEntity.getCode()))).entity(lookupEntityCreated).build();
		} else {
			return Response.ok(lookupEntityCreated).build();
		}
	}

	@GET
	@APIDescription("Get an entity type for a given id")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(value = GenericLookupEntity.class, actualClassName = "LookupEntity")
	@Path("/{entity}/{code}")
	public Response getEntityValue(
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
			Class lookupClass = Class.forName(DBManager.getInstance().getEntityModelPackage() + "." + entityName);
			@SuppressWarnings("unchecked")
			Object value = persistenceService.findById(lookupClass, code);
			if (value != null) {
				lookupEntity = (LookupEntity) persistenceService.unwrapProxyObject(value);
			}
		} catch (ClassNotFoundException e) {
			throw new OpenStorefrontRuntimeException("(System Issue) Unable to find entity: " + entityName, "System error...contact support.", e);
		}
		Response response;
		if (lookupEntity == null) {
			response = Response.status(Response.Status.NOT_FOUND).entity(lookupEntity).build();
		} else {
			response = Response.ok(lookupEntity).build();
		}
		return response;
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_LOOKUPS)
	@APIDescription("Updates descriptions for a given entity and code.")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{entity}/{code}")
	public Response updateEntityValue(
			@PathParam("entity")
			@RequiredParam String entityName,
			@PathParam("code")
			@APIDescription("Passing the code in the body is optional")
			@RequiredParam String code,
			GenericLookupEntity genericLookupEntity)
	{

		LookupEntity lookupEntity = service.getLookupService().getLookupEnity(entityName, code.toUpperCase());
		if (lookupEntity == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		genericLookupEntity.setCode(code.toUpperCase());
		return handlePostPutCode(entityName, genericLookupEntity, false);
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_LOOKUPS)
	@APIDescription("Activates a given entity code.")
	@Path("/{entity}/{code}/activate")
	public Response activeEntityCode(
			@PathParam("entity")
			@RequiredParam String entityName,
			@PathParam("code")
			@RequiredParam String code)
	{
		LookupEntity lookupEntity = null;
		try {
			Class lookupClass = Class.forName(DBManager.getInstance().getEntityModelPackage() + "." + entityName);
			@SuppressWarnings("unchecked")
			Object value = service.getPersistenceService().findById(lookupClass, code);
			if (value != null) {
				lookupEntity = (LookupEntity) service.getPersistenceService().unwrapProxyObject(value);
			}
		} catch (ClassNotFoundException e) {
			throw new OpenStorefrontRuntimeException("(System Issue) Unable to find entity: " + entityName, "System error...contact support.", e);
		}
		if (lookupEntity == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} else {
			service.getLookupService().updateLookupStatus(lookupEntity, LookupEntity.ACTIVE_STATUS);
		}
		return Response.ok().build();
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_LOOKUPS)
	@APIDescription("Remove a code from the entity")
	@Path("/{entity}/{code}")
	@SuppressWarnings("unchecked")
	public void deleteEntityValue(
			@PathParam("entity")
			@RequiredParam String entityName,
			@PathParam("code")
			@RequiredParam String code)
	{
		checkEntity(entityName);
		try {
			Class lookupClass = Class.forName(DBManager.getInstance().getEntityModelPackage() + "." + entityName);
			service.getLookupService().removeValue(lookupClass, code);
		} catch (ClassNotFoundException e) {
			throw new OpenStorefrontRuntimeException("(System Issue) Unable to find entity: " + entityName, "System error...contact support.", e);
		}
	}

	private void checkEntity(String entityName)
	{
		boolean valid = false;
		if (ReflectionUtil.LOOKUP_ENTITY.equals(entityName) == false) {
			try {
				Class lookupClass = Class.forName(DBManager.getInstance().getEntityModelPackage() + "." + entityName);
				valid = ReflectionUtil.isSubLookupEntity(lookupClass);
			} catch (ClassNotFoundException e) {
				valid = false;
			}
		}
		if (!valid) {
			throw new OpenStorefrontRuntimeException("Lookup Type not found", "Check entity name passed in. (Case-Sensitive and should be Camel-Cased)");
		}
	}

}
