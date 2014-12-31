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
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.service.transfermodel.Architecture;
import edu.usu.sdl.openstorefront.sort.AttributeCodeArchComparator;
import edu.usu.sdl.openstorefront.sort.AttributeCodeComparator;
import edu.usu.sdl.openstorefront.sort.AttributeCodeViewComparator;
import edu.usu.sdl.openstorefront.sort.AttributeTypeViewComparator;
import edu.usu.sdl.openstorefront.storage.model.ArticleTracking;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.storage.model.AttributeXRefMap;
import edu.usu.sdl.openstorefront.storage.model.AttributeXRefType;
import edu.usu.sdl.openstorefront.storage.model.ComponentIntegration;
import edu.usu.sdl.openstorefront.storage.model.LookupEntity;
import edu.usu.sdl.openstorefront.storage.model.TrackEventCode;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.rest.model.AttributeCodeView;
import edu.usu.sdl.openstorefront.web.rest.model.AttributeTypeView;
import edu.usu.sdl.openstorefront.web.rest.model.AttributeXRefView;
import edu.usu.sdl.openstorefront.web.rest.model.AttributeXrefMapView;
import edu.usu.sdl.openstorefront.web.rest.model.FilterQueryParams;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
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
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/attributes")
@APIDescription("Attributes are the filterable data associated with the listings.")
public class AttributeResource
		extends BaseResource
{

	@Context
	HttpServletRequest request;

	@GET
	@APIDescription("Gets all active attributes and codes for the attributes in view based model.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(AttributeTypeView.class)
	public List<AttributeTypeView> getAttributeView(
			@QueryParam("all")
			@APIDescription("Setting force to true attempts to interrupt the job otherwise it's a more graceful shutdown.")
			@DefaultValue("false") boolean all)
	{
		List<AttributeTypeView> attributeTypeViews = new ArrayList<>();

		AttributeType attributeTypeExample = new AttributeType();
		if (!all) {
			attributeTypeExample.setActiveStatus(AttributeType.ACTIVE_STATUS);
		}
		List<AttributeType> attributeTypes = service.getPersistenceService().queryByExample(AttributeType.class, new QueryByExample(attributeTypeExample));
		for (AttributeType attributeType : attributeTypes) {
			AttributeTypeView attributeTypeView = AttributeTypeView.toView(attributeType);
			List<AttributeCode> attributeCodes = service.getAttributeService().findCodesForType(attributeType.getAttributeType(), all);
			attributeCodes.stream().forEach(code -> {
				attributeTypeView.getCodes().add(AttributeCodeView.toView(code));
			});
			attributeTypeViews.add(attributeTypeView);
		}
		attributeTypeViews.sort(new AttributeTypeViewComparator<>());
		for (AttributeTypeView attributeTypeView : attributeTypeViews) {
			if (attributeTypeView.getArchitectureFlg()) {
				attributeTypeView.getCodes().sort(new AttributeCodeArchComparator<>());
			}
			else {
				attributeTypeView.getCodes().sort(new AttributeCodeViewComparator<>());
			}
		}

		return attributeTypeViews;
	}

	@GET
	@APIDescription("Gets codes with articles.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(AttributeCode.class)
	@Path("/allcodeswitharticles")
	public Response getAllCodesWithArticles(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		List<AttributeCode> attributeCodes = service.getAttributeService().findRecentlyAddedArticles(filterQueryParams.getMax(), filterQueryParams.getStatus());
		attributeCodes = filterQueryParams.filter(attributeCodes);

		GenericEntity<List<AttributeCode>> entity = new GenericEntity<List<AttributeCode>>(attributeCodes)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets attributes types based on filter")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(AttributeType.class)
	@Path("/attributetypes")
	public Response getAttributeTypes(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		AttributeType attributeTypeExample = new AttributeType();
		attributeTypeExample.setActiveStatus(filterQueryParams.getStatus());
		List<AttributeType> attributeTypes = service.getPersistenceService().queryByExample(AttributeType.class, new QueryByExample(attributeTypeExample));
		attributeTypes = filterQueryParams.filter(attributeTypes);
		GenericEntity<List<AttributeType>> entity = new GenericEntity<List<AttributeType>>(attributeTypes)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets attribute type")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(AttributeType.class)
	@Path("/attributetypes/{type}")
	public Response getAttributeTypeById(
			@PathParam("type")
			@RequiredParam String type,
			@QueryParam("view")
			@APIDescription("Setting forces the attribute to return the view model.")
			@DefaultValue("false") boolean view,
			@QueryParam("all")
			@APIDescription("Setting forces the attribute to return the view model.")
			@DefaultValue("false") boolean all)
	{
		if (!view) {
			AttributeType attributeType = service.getPersistenceService().findById(AttributeType.class, type);
			if (attributeType == null) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			else {
				return Response.ok(attributeType).build();
			}
		}
		else {
			AttributeTypeView attributeTypeView = new AttributeTypeView();
			AttributeType typeObj = service.getPersistenceService().findById(AttributeType.class, type);
			if (typeObj != null) {
				attributeTypeView = AttributeTypeView.toView(typeObj);
			}
			else {
				typeObj = new AttributeType();
				typeObj.setAttributeType(AttributeType.TYPE);
			}
			List<AttributeCode> attributeCodes = service.getAttributeService().findCodesForType(typeObj.getAttributeType(), all);
			for (AttributeCode code : attributeCodes) {
				attributeTypeView.getCodes().add(AttributeCodeView.toView(code));
			}
			if (attributeTypeView.getArchitectureFlg()) {
				attributeTypeView.getCodes().sort(new AttributeCodeArchComparator<>());
			}
			else {
				attributeTypeView.getCodes().sort(new AttributeCodeViewComparator<>());
			}
			return Response.ok(attributeTypeView).build();
		}
	}

	@GET
	@APIDescription("Gets attribute type")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(AttributeType.class)
	@Path("/attributetypes/{type}/{code}")
	public Response getAttributeCodeById(
			@PathParam("type")
			@RequiredParam String type,
			@PathParam("code")
			@RequiredParam String code)
	{
		AttributeCodePk pk = new AttributeCodePk();
		pk.setAttributeCode(code);
		pk.setAttributeType(type);
		AttributeCode attributeCode = service.getPersistenceService().findById(AttributeCode.class, pk);
		if (attributeCode == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		else {
			return Response.ok(attributeCode).build();
		}
	}

	@GET
	@APIDescription("Gets attribute code base on filter. Always sort by sort Order or label")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(AttributeCode.class)
	@Path("/attributetypes/{type}/attributecodes")
	public Response getAttributeCodes(
			@PathParam("type")
			@RequiredParam String type,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		AttributeCode attributeCodeExample = new AttributeCode();
		attributeCodeExample.setActiveStatus(filterQueryParams.getStatus());
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeType(type.toUpperCase());
		attributeCodeExample.setAttributeCodePk(attributeCodePk);

		List<AttributeCode> attributeCodes = service.getPersistenceService().queryByExample(AttributeCode.class, new QueryByExample(attributeCodeExample));
		attributeCodes = filterQueryParams.filter(attributeCodes);
		attributeCodes.sort(new AttributeCodeComparator<>());

		GenericEntity<List<AttributeCode>> entity = new GenericEntity<List<AttributeCode>>(attributeCodes)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets architecture")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Architecture.class)
	@Path("/attributetypes/{type}/architecture")
	public Response getArchitecture(
			@PathParam("type")
			@RequiredParam String type)
	{
		Architecture architecture = null;
		AttributeType attributeType = service.getAttributeService().findType(type);
		if (attributeType != null) {
			architecture = service.getAttributeService().generateArchitecture(type);
		}
		return sendSingleEntityResponse(architecture);
	}

	@GET
	@APIDescription("Gets all active attributes and codes for the attributes in view based model.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(AttributeCode.class)
	@Path("/attributetypes/{type}/attributecodes/{code}")
	public Response getAttributeCode(
			@PathParam("type")
			@RequiredParam String type,
			@PathParam("code")
			@RequiredParam String code)
	{
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeCode(code);
		attributeCodePk.setAttributeType(type);
		AttributeCode attributeCode = service.getPersistenceService().detach(service.getPersistenceService().findById(AttributeCode.class, attributeCodePk));

		return sendSingleEntityResponse(attributeCode);
	}

	@GET
	@APIDescription("Gets article if it existing for the given type and code.")
	@Produces({MediaType.WILDCARD})
	@Path("/attributetypes/{type}/attributecodes/{code}/article")
	public Response getAttributeArticle(
			@PathParam("type")
			@RequiredParam String type,
			@PathParam("code")
			@RequiredParam String code)
	{
		type = type.toUpperCase();
		code = code.toUpperCase();
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeCode(code);
		attributeCodePk.setAttributeType(type);
		String articleData = service.getAttributeService().getArticle(attributeCodePk);
		if (StringUtils.isNotBlank(articleData)) {
			ArticleTracking articleTracking = new ArticleTracking();
			articleTracking.setAttributeCode(code);
			articleTracking.setAttributeType(type);
			articleTracking.setClientIp(request.getRemoteAddr());
			articleTracking.setEventDts(TimeUtil.currentDate());
			articleTracking.setTrackEventTypeCode(TrackEventCode.VIEW);
			service.getAttributeService().addArticleTrackEvent(articleTracking);
			return Response.ok(articleData).build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@PUT
	@RequireAdmin
	@APIDescription("Updates article")
	@Consumes({MediaType.TEXT_HTML})
	@Path("/attributetypes/{type}/attributecodes/{code}/article")
	public Response updateEntityValue(
			@PathParam("type")
			@RequiredParam String type,
			@PathParam("code")
			@RequiredParam String code,
			String article)
	{
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeCode(code.toUpperCase());
		attributeCodePk.setAttributeType(type.toUpperCase());
		service.getAttributeService().saveArticle(attributeCodePk, article);
		return Response.ok().build();
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Deletes article")
	@Path("/attributetypes/{type}/attributecodes/{code}/article")
	public void deleteArticle(
			@PathParam("type")
			@RequiredParam String type,
			@PathParam("code")
			@RequiredParam String code)
	{
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeCode(code.toUpperCase());
		attributeCodePk.setAttributeType(type.toUpperCase());
		service.getAttributeService().deleteArticle(attributeCodePk);
	}

	@POST
	@RequireAdmin
	@APIDescription("Adds a new attribute type")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/attributetypes")
	public Response postNewEntity(AttributeType attributeType)
	{
		return handleAttributePostPutType(attributeType, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Updates a attribute type")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/attributetypes/{type}")
	public Response updateEntityValue(
			@PathParam("type")
			@RequiredParam String type,
			AttributeType attributeType)
	{
		AttributeType existing = service.getPersistenceService().findById(AttributeType.class, type);
		if (existing != null) {
			attributeType.setAttributeType(type.toUpperCase());
			return handleAttributePostPutType(attributeType, true);
		}
		else {
			throw new OpenStorefrontRuntimeException("Unable to find existing type.", "Make sure type exists before call PUT");
		}
	}

	private Response handleAttributePostPutType(AttributeType attributeType, boolean post)
	{
		ValidationModel validationModel = new ValidationModel(attributeType);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			attributeType.setActiveStatus(LookupEntity.ACTIVE_STATUS);
			attributeType.setCreateUser(SecurityUtil.getCurrentUserName());
			attributeType.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getAttributeService().saveAttributeType(attributeType, false);
		}
		else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			AttributeType attributeTypeCreated = service.getPersistenceService().findById(AttributeType.class, attributeType.getAttributeType());
			return Response.created(URI.create("v1/resource/attributes/attributetypes/" + attributeType.getAttributeType())).entity(attributeTypeCreated).build();
		}
		else {
			return Response.ok().build();
		}
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a type (In-activates).  Note: this doesn't remove all attribute type associations.")
	@Path("/attributetypes/{type}")
	public void deleteAttributeType(
			@PathParam("type")
			@RequiredParam String type)
	{
		service.getAttributeService().removeAttributeType(type.toUpperCase());
	}

	@POST
	@RequireAdmin
	@APIDescription("Activate a type (In-activates).  Note: this doesn't remove all attribute type associations.")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/attributetypes/{type}")
	public void activateType(
			@PathParam("type")
			@RequiredParam String type)
	{
		service.getAttributeService().activateType(type.toUpperCase());
	}

	@PUT
	@RequireAdmin
	@APIDescription("Updates a attribute code")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/attributetypes/{type}/sortorder")
	public Response updateAttributeCode(
			@PathParam("type")
			@RequiredParam String type,
			AttributeTypeView attributeType)
	{
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeType(type);

		for (AttributeCodeView code : attributeType.getCodes()) {
			attributeCodePk.setAttributeCode(code.getCode());
			service.getAttributeService().saveSortOrder(attributeCodePk, code.getSortOrder());
			OSFCacheManager.getAttributeCache().remove(attributeCodePk.getAttributeCode());
		}
		OSFCacheManager.getAttributeCache().remove(attributeCodePk.getAttributeType());
		OSFCacheManager.getAttributeCache().remove(attributeCodePk.getAttributeType() + "-allCodes");

		return Response.ok(attributeType).build();
	}

	@POST
	@RequireAdmin
	@APIDescription("Adds a new attribute code")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/attributetypes/{type}/attributecodes")
	public Response postAttributeCode(
			@PathParam("type")
			@RequiredParam String type,
			AttributeCode attributeCode)
	{
		attributeCode.getAttributeCodePk().setAttributeType(type);
		return handleAttributePostPutCode(attributeCode, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Updates a attribute code")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/attributetypes/{type}/attributecodes/{code}")
	public Response updateAttributeCode(
			@PathParam("type")
			@RequiredParam String type,
			@PathParam("code")
			@RequiredParam String code,
			AttributeCode attributeCode)
	{
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeCode(code);
		attributeCodePk.setAttributeType(type);
		attributeCode.setAttributeCodePk(attributeCodePk);
		AttributeCode existing = service.getPersistenceService().findById(AttributeCode.class, attributeCodePk);
		if (existing != null) {
			return handleAttributePostPutCode(attributeCode, false);
		}
		else {
			throw new OpenStorefrontRuntimeException("Unable to find existing code.", "Make sure type exists before call PUT");
		}
	}

	private Response handleAttributePostPutCode(AttributeCode attributeCode, boolean post)
	{
		ValidationModel validationModel = new ValidationModel(attributeCode);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			attributeCode.setActiveStatus(LookupEntity.ACTIVE_STATUS);
			attributeCode.setCreateUser(SecurityUtil.getCurrentUserName());
			attributeCode.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getAttributeService().saveAttributeCode(attributeCode, false);
		}
		else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			AttributeCode attributeCodeCreated = service.getPersistenceService().findById(AttributeCode.class, attributeCode.getAttributeCodePk());
			return Response.created(URI.create("v1/resource/attributes/attributetypes/"
					+ attributeCode.getAttributeCodePk().getAttributeType()
					+ "/attributecodes/"
					+ attributeCode.getAttributeCodePk().getAttributeCode())).entity(attributeCodeCreated).build();
		}
		else {
			return Response.ok(attributeCode).build();
		}
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a Code (In-activates).  Note: this doesn't remove all attribute type associations.")
	@Path("/attributetypes/{type}/attributecodes/{code}")
	public void deleteAttributeCode(
			@PathParam("type")
			@RequiredParam String type,
			@PathParam("code")
			@RequiredParam String code)
	{
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeCode(code.toUpperCase());
		attributeCodePk.setAttributeType(type.toUpperCase());
		service.getAttributeService().removeAttributeCode(attributeCodePk);
	}

	@POST
	@RequireAdmin
	@APIDescription("Activate a Code (activates).")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/attributetypes/{type}/attributecodes/{code}/activate")
	public void activateCode(
			@PathParam("type")
			@RequiredParam String type,
			@PathParam("code")
			@RequiredParam String code)
	{
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeCode(code.toUpperCase());
		attributeCodePk.setAttributeType(type.toUpperCase());
		service.getAttributeService().activateCode(attributeCodePk);
	}

	@GET
	@APIDescription("Gets the list of active mapping for attributes to fields")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(AttributeXrefMapView.class)
	@Path("/attributexreftypes/detail")
	public List<AttributeXrefMapView> getMappingTypes()
	{
		List<AttributeXrefMapView> attributeXrefMapViews = new ArrayList<>();

		AttributeXRefType example = new AttributeXRefType();
		example.setActiveStatus(AttributeXRefType.ACTIVE_STATUS);
		List<AttributeXRefType> types = service.getPersistenceService().queryByExample(AttributeXRefType.class, new QueryByExample(example));

		for (AttributeXRefType type : types) {
			AttributeXrefMapView model = new AttributeXrefMapView();
			AttributeType attType = service.getPersistenceService().findById(AttributeType.class, type.getAttributeType());
			model.setAttributeName(attType.getDescription());
			model.setAttributeType(type.getAttributeType());
			model.setFieldName(type.getFieldName());
			model.setFieldId(type.getFieldId());
			model.setIssueType(type.getIssueType());
			model.setProjectType(type.getProjectType());

			AttributeXRefMap tempMap = new AttributeXRefMap();
			tempMap.setActiveStatus(AttributeXRefMap.ACTIVE_STATUS);
			tempMap.setAttributeType(type.getAttributeType());
			model.setMapping(service.getPersistenceService().queryByExample(AttributeXRefMap.class, tempMap));

			attributeXrefMapViews.add(model);
		}

		return attributeXrefMapViews;
	}

	@GET
	@APIDescription("Gets the list of mapping for attributes to fields")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(AttributeXrefMapView.class)
	@Path("/attributexreftypes/detail/distinct")
	public List<AttributeXrefMapView> getDistinctProjectMappings()
	{
		Set<AttributeXrefMapView> attributeXrefMapViews = new HashSet<>();

		AttributeXRefType example = new AttributeXRefType();
		example.setActiveStatus(AttributeXRefType.ACTIVE_STATUS);
		List<AttributeXRefType> types = service.getPersistenceService().queryByExample(AttributeXRefType.class, new QueryByExample(example));

		for (AttributeXRefType type : types) {
			AttributeXrefMapView model = new AttributeXrefMapView();
			model.setIssueType(type.getIssueType());
			model.setProjectType(type.getProjectType());
			attributeXrefMapViews.add(model);
		}

		return new ArrayList<>(attributeXrefMapViews);
	}

	@GET
	@APIDescription("Gets the list of mapping for attributes to fields base on the type passed in.  It will show inactive types as well.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(AttributeXrefMapView.class)
	@Path("/attributexreftypes/{attributeType}/detail")
	public Response getMappingType(
			@PathParam("attributeType") String attributeType)
	{
		AttributeXrefMapView model = null;

		AttributeXRefType example = new AttributeXRefType();
		example.setAttributeType(attributeType);
		AttributeXRefType attributeXRefType = service.getPersistenceService().queryOneByExample(AttributeXRefType.class, example);

		if (attributeXRefType != null) {
			model = new AttributeXrefMapView();
			AttributeType attType = service.getPersistenceService().findById(AttributeType.class, attributeXRefType.getAttributeType());
			model.setAttributeName(attType.getDescription());
			model.setAttributeType(attributeXRefType.getAttributeType());
			model.setFieldName(attributeXRefType.getFieldName());
			model.setFieldId(attributeXRefType.getFieldId());
			model.setIssueType(attributeXRefType.getIssueType());
			model.setProjectType(attributeXRefType.getProjectType());

			AttributeXRefMap tempMap = new AttributeXRefMap();
			tempMap.setActiveStatus(AttributeXRefMap.ACTIVE_STATUS);
			tempMap.setAttributeType(attributeXRefType.getAttributeType());
			model.setMapping(service.getPersistenceService().queryByExample(AttributeXRefMap.class, tempMap));
		}
		return sendSingleEntityResponse(model);
	}

	@POST
	@RequireAdmin
	@APIDescription("Save a attribute cross-ref mapping")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/attributexreftypes/detail")
	public Response saveMapping(
			@RequiredParam AttributeXRefView attributeXref)
	{
		attributeXref.getType().setCreateDts(TimeUtil.currentDate());
		attributeXref.getType().setUpdateDts(TimeUtil.currentDate());
		attributeXref.getType().setCreateUser(SecurityUtil.getCurrentUserName());
		attributeXref.getType().setUpdateUser(SecurityUtil.getCurrentUserName());
		attributeXref.getType().setActiveStatus(ComponentIntegration.ACTIVE_STATUS);
		ValidationModel validationModel = new ValidationModel(attributeXref.getType());
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);

		for (AttributeXRefMap map : attributeXref.getMap()) {
			map.setCreateDts(TimeUtil.currentDate());
			map.setUpdateDts(TimeUtil.currentDate());
			map.setCreateUser(SecurityUtil.getCurrentUserName());
			map.setUpdateUser(SecurityUtil.getCurrentUserName());
			map.setActiveStatus(ComponentIntegration.ACTIVE_STATUS);

			validationModel = new ValidationModel(map);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult mapValidationResult = ValidationUtil.validate(validationModel);
			validationResult.merge(mapValidationResult);
		}
		if (validationResult.valid()) {
			service.getAttributeService().saveAttributeXrefMap(attributeXref);

			return Response.created(URI.create("v1/resource/attributes/attributexreftypes/" + attributeXref.getType().getAttributeType() + "/detail")).build();
		}
		else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a type and all mapping")
	@Path("/attributexreftypes/{attributeType}")
	public void deleteMappingType(
			@PathParam("attributeType")
			@RequiredParam String type)
	{
		service.getAttributeService().deleteAttributeXrefType(type);
	}

}
