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
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.service.transfermodel.Architecture;
import edu.usu.sdl.openstorefront.sort.AttributeCodeArchComparator;
import edu.usu.sdl.openstorefront.sort.AttributeCodeViewComparator;
import edu.usu.sdl.openstorefront.sort.AttributeTypeViewComparator;
import edu.usu.sdl.openstorefront.storage.model.ArticleTracking;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.storage.model.LookupEntity;
import edu.usu.sdl.openstorefront.storage.model.TrackEventCode;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.rest.model.AttributeCodeView;
import edu.usu.sdl.openstorefront.web.rest.model.AttributeTypeView;
import edu.usu.sdl.openstorefront.web.rest.model.FilterQueryParams;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/attributes")
@APIDescription("Attributes are the filterable data asscoated with the listings.")
public class AttributeResource
		extends BaseResource
{

	@Context
	HttpServletRequest request;

	@GET
	@APIDescription("Gets all active attributes and codes for the attributes in view based model.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(AttributeTypeView.class)
	public List<AttributeTypeView> getAttributeView()
	{
		List<AttributeTypeView> attributeTypeViews = new ArrayList<>();

		AttributeType attributeTypeExample = new AttributeType();
		attributeTypeExample.setActiveStatus(AttributeType.ACTIVE_STATUS);
		List<AttributeType> attributeTypes = service.getPersistenceService().queryByExample(AttributeType.class, new QueryByExample(attributeTypeExample));
		for (AttributeType attributeType : attributeTypes) {
			AttributeTypeView attributeTypeView = AttributeTypeView.toView(attributeType);
			List<AttributeCode> attributeCodes = service.getAttributeService().findCodesForType(attributeType.getAttributeType());
			attributeCodes.stream().forEach(code -> {
				attributeTypeView.getCodes().add(AttributeCodeView.toView(code));
			});
			attributeTypeViews.add(attributeTypeView);
		}
		attributeTypeViews.sort(new AttributeTypeViewComparator<>());
		for (AttributeTypeView attributeTypeView : attributeTypeViews) {
			if (attributeTypeView.getArchtechtureFlg()) {
				attributeTypeView.getCodes().sort(new AttributeCodeArchComparator<>());
			} else {
				attributeTypeView.getCodes().sort(new AttributeCodeViewComparator<>());
			}
		}

		return attributeTypeViews;
	}

	@GET
	@APIDescription("Gets attributes types based on filter")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(AttributeType.class)
	@Path("/attributetypes")
	public List<AttributeType> getAttributeTypes(@BeanParam FilterQueryParams filterQueryParams)
	{
		AttributeType attributeTypeExample = new AttributeType();
		attributeTypeExample.setActiveStatus(filterQueryParams.getStatus());
		List<AttributeType> attributeTypes = service.getPersistenceService().queryByExample(AttributeType.class, new QueryByExample(attributeTypeExample));
		attributeTypes = filterQueryParams.filter(attributeTypes);
		return attributeTypes;
	}

	@GET
	@APIDescription("Gets attribute type")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(AttributeType.class)
	@Path("/attributetypes/{type}")
	public Response getAttributeTypeById(
			@PathParam("type")
			@RequiredParam String type)
	{
		AttributeType attributeType = service.getPersistenceService().findById(AttributeType.class, type);
		if (attributeType == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} else {
			return Response.ok(attributeType).build();
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
		} else {
			return Response.ok(attributeCode).build();
		}
	}

	@GET
	@APIDescription("Gets attribute code base on filter")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(AttributeCode.class)
	@Path("/attributetypes/{type}/attributecodes")
	public List<AttributeCode> getAttributeCodes(
			@PathParam("type")
			@RequiredParam String type,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		AttributeCode attributeCodeExample = new AttributeCode();
		attributeCodeExample.setActiveStatus(filterQueryParams.getStatus());
		AttributeCodePk attributeCodePk = new AttributeCodePk();
		attributeCodePk.setAttributeType(type.toUpperCase());
		attributeCodeExample.setAttributeCodePk(attributeCodePk);

		List<AttributeCode> attributeCodes = service.getPersistenceService().queryByExample(AttributeCode.class, new QueryByExample(attributeCodeExample));
		attributeCodes = filterQueryParams.filter(attributeCodes);

		return attributeCodes;
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
		return sendSingleEnityResponse(architecture);
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

		return sendSingleEnityResponse(attributeCode);
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
		} else {
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
			service.getAttributeService().saveAttributeType(attributeType);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			AttributeType attributeTypeCreated = service.getPersistenceService().findById(AttributeType.class, attributeType.getAttributeType());
			return Response.created(URI.create("v1/resource/attributes/attributetypes/" + attributeType.getAttributeType())).entity(attributeTypeCreated).build();
		} else {
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
			return handleAttributePostPutCode(attributeCode, true);
		} else {
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
			service.getAttributeService().saveAttributeCode(attributeCode);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			AttributeCode attributeCodeCreated = service.getPersistenceService().findById(AttributeCode.class, attributeCode.getAttributeCodePk());
			return Response.created(URI.create("v1/resource/attributes/attributetypes/"
					+ attributeCode.getAttributeCodePk().getAttributeType()
					+ "/attributecodes/"
					+ attributeCode.getAttributeCodePk().getAttributeCode())).entity(attributeCodeCreated).build();
		} else {
			return Response.ok().build();
		}
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Remove a type (In-activates).  Note: this doesn't remove all attribute type associations.")
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

}
