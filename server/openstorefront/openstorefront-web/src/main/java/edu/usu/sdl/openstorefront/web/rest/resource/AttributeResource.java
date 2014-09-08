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
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.web.rest.model.AttributeCodeView;
import edu.usu.sdl.openstorefront.web.rest.model.AttributeTypeView;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/attributes")
@APIDescription("Attributes are the filterable data asscoated with the listings.")
public class AttributeResource
		extends BaseResource
{

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

		return attributeTypeViews;
	}

//	@GET
//	@APIDescription("Gets attributes types based on filter")
//	@Produces({MediaType.APPLICATION_JSON})
//	@DataType(AttributeType.class)
//	@Path("/attributetypes")
//	public List<AttributeType> getAttributeTypes(@BeanParam FilterQueryParams filterQueryParams)
//	{
//
//		return
//	}
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

//	@GET
//	@APIDescription("Gets attribute code base on filter")
//	@Produces({MediaType.APPLICATION_JSON})
//	@DataType(AttributeCode.class)
//	@Path("/attributetypes/{type}/attributecodes")
//	public List<AttributeCode> getAttributeCodes(@BeanParam FilterQueryParams filterQueryParams)
//	{
//
//		return
//	}
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
		AttributeCode attributeCode = service.getPersistenceService().findById(AttributeCode.class, attributeCodePk);

		if (attributeCode == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} else {
			return Response.ok(attributeCode).build();
		}
	}

//	@GET
//	@APIDescription("Gets article if it existing for the given type and code.")
//	@Path("/{type}/attributeCode/{code}/article")
//	public Response getAttributeArticle()
//	{
//
//		return Response.ok().build();
//	}
	//put article
	//delete article
	//Post Type
	//Put Type
	//Delete Type
	//Post Code
	//Put Code
	//Delete Code
}
