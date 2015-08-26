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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.Highlight;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireAdmin;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.net.URI;
import java.util.Collections;
import java.util.List;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Handles the highlight data
 *
 * @author dshurtleff
 */
@Path("v1/resource/highlights")
@APIDescription("Highlights are component articles and other items the admin want to display on the UI")
public class HighlightResource
		extends BaseResource
{

	@GET
	@APIDescription("Gets all active highlights")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Highlight.class)
	public List<Highlight> getHighlights(
			@QueryParam("all")
			@APIDescription("Setting force to true pulls inactivated highlights as well.")
			@DefaultValue("false") boolean all
	)
	{
		Highlight highlightExample = new Highlight();
		if (!all) {
			highlightExample.setActiveStatus(Highlight.ACTIVE_STATUS);
		}
		List<Highlight> highlights = service.getPersistenceService().queryByExample(Highlight.class, new QueryByExample(highlightExample));
		Collections.sort(highlights, new BeanComparator<>(OpenStorefrontConstant.SORT_DESCENDING, Highlight.FIELD_TITLE));
		return highlights;
	}

	@GET
	@APIDescription("Gets a Highlight")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Highlight.class)
	@Path("/{id}")
	public Response getHighlightById(
			@PathParam("id")
			@RequiredParam String id)
	{
		Highlight highlight = service.getPersistenceService().findById(Highlight.class, id);
		if (highlight == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} else {
			return Response.ok(highlight).build();
		}
	}

	@POST
	@RequireAdmin
	@APIDescription("Creates a new Highlight")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response postHighlight(Highlight highlight)
	{
		return handlePostPutHighlight(highlight, true);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Updates a highlight")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}")
	public Response updateEntityValue(
			@PathParam("id")
			@RequiredParam String id,
			Highlight highlight)
	{

		Highlight existing = service.getPersistenceService().findById(Highlight.class, id);
		if (existing == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		highlight.setHighlightId(existing.getHighlightId());
		return handlePostPutHighlight(highlight, false);
	}

	private Response handlePostPutHighlight(Highlight highlight, boolean post)
	{
		ValidationModel validationModel = new ValidationModel(highlight);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {

			highlight.setCreateUser(SecurityUtil.getCurrentUserName());
			highlight.setUpdateUser(SecurityUtil.getCurrentUserName());
			service.getSystemService().saveHightlight(highlight);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create("v1/resource/highlights/" + highlight.getHighlightId())).entity(highlight).build();
		} else {
			return Response.ok(highlight).build();
		}
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Deactivates a highlight")
	@Path("/{id}/deactivate")
	public void deactivateHighlight(
			@PathParam("id")
			@RequiredParam String id)
	{
		service.getSystemService().removeHighlight(id);
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Deletes a highlight")
	@Path("/{id}/delete")
	public void deleteHighlight(
			@PathParam("id")
			@RequiredParam String id)
	{
		service.getSystemService().deleteHighlight(id);
	}

	@PUT
	@RequireAdmin
	@APIDescription("Activates a highlight")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/activate")
	public void activateHighlight(
			@PathParam("id")
			@RequiredParam String id)
	{
		service.getSystemService().activateHighlight(id);
	}

}
