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

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.TemplateBlock;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.net.URI;
import java.util.List;
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

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/templateblocks")
@APIDescription("Template blocks are used to define custom view for entries")
public class TemplateBlockResource
	extends BaseResource
{

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TEMPLATES)
	@APIDescription("Gets template blocks")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(TemplateBlock.class)
	public List<TemplateBlock> getTemplateBlocks()
	{
		TemplateBlock templateBlock = new TemplateBlock();
		templateBlock.setActiveStatus(TemplateBlock.ACTIVE_STATUS);
		return templateBlock.findByExample();
	}
		
	@GET
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TEMPLATES)
	@APIDescription("Gets template blocks")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(TemplateBlock.class)
	@Path("/{templateBlockId}")
	public Response getTemplateBlock(
		@PathParam("templateBlockId") String templateBlockId
	)
	{
		TemplateBlock templateBlock = new TemplateBlock();
		templateBlock.setTemplateBlockId(templateBlockId);		
		templateBlock = templateBlock.find();
		return sendSingleEntityResponse(templateBlock);
	}	
	
	@POST
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TEMPLATES)
	@APIDescription("Adds a new component type")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response createTemplateBlock(
			TemplateBlock templateBlock
	)
	{
		return handleSaveTemplateBlock(templateBlock, true);
	}
	
	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TEMPLATES)
	@APIDescription("Updates a component type")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{templateBlockId}")
	public Response updateComponentTypeTemplate(
			@PathParam("templateBlockId") String templateBlockId,
			TemplateBlock templateBlock
	)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		TemplateBlock found = new TemplateBlock();
		found.setTemplateBlockId(templateBlockId);		
		found = found.find();
		if (found != null) {
			templateBlock.setTemplateBlockId(templateBlockId);			
			response = handleSaveTemplateBlock(templateBlock, false);
		}
		return response;
	}	
	
	private Response handleSaveTemplateBlock(TemplateBlock templateBlock, boolean post)
	{
		ValidationResult validationResult = templateBlock.validate(true);
		if (validationResult.valid()) {
			service.getComponentService().saveTemplateBlock(templateBlock);
			
			if (post) {
				return Response.created(URI.create("v1/resource/templateblock/" + templateBlock.getTemplateBlockId())).entity(templateBlock).build();
			} else {
				return sendSingleEntityResponse(templateBlock);
			}
		}
		return sendSingleEntityResponse(validationResult.toRestError());
	}	
	
	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_TEMPLATES)
	@APIDescription("Inactivates a component type template")
	@Path("/{templateBlockId}")
	public void deleteNewEvent(
			@PathParam("templateBlockId") String templateBlockId
	)
	{
		service.getComponentService().deleteTemplateBlock(templateBlockId);		
	}	
}
