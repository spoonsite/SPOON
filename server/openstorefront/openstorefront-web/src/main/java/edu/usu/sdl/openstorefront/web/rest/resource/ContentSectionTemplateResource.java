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
import edu.usu.sdl.openstorefront.core.entity.ChecklistTemplate;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionTemplate;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.view.ContentSectionTemplateView;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.doc.security.LogicOperation;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Provides access to template for content sections")
@Path("v1/resource/contentsectiontemplates")
public class ContentSectionTemplateResource
		extends BaseResource
{

	@GET
	@RequireSecurity(
			value = {SecurityPermission.USER_EVALUATIONS_READ, SecurityPermission.ADMIN_EVALUATION_TEMPLATE_SECTION_READ},
			logicOperator = LogicOperation.OR
	)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ContentSectionTemplate.class)
	@APIDescription("Gets sections templates")
	public List<ContentSectionTemplate> getContentSectionTemplates(@BeanParam FilterQueryParams filterQueryParams)
	{
		ContentSectionTemplate contentSectionTemplate = new ContentSectionTemplate();
		if (!filterQueryParams.getAll()) {
			contentSectionTemplate.setActiveStatus(filterQueryParams.getStatus());
		}
		List<ContentSectionTemplate> templates = contentSectionTemplate.findByExample();
		templates = filterQueryParams.filter(templates);
		return templates;
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_SECTION_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ContentSectionTemplate.class)
	@APIDescription("Gets a content template")
	@Path("/{templateId}")
	public Response getContentSectionTemplate(
			@PathParam("templateId") String templateId
	)
	{
		ContentSectionTemplate contentSectionTemplate = new ContentSectionTemplate();
		contentSectionTemplate.setTemplateId(templateId);
		contentSectionTemplate = contentSectionTemplate.find();
		return sendSingleEntityResponse(contentSectionTemplate);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_SECTION_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ContentSectionTemplateView.class)
	@APIDescription("Gets a the resolved template")
	@Path("/{templateId}/details")
	public Response getContentTemplateDetails(
			@PathParam("templateId") String templateId
	)
	{
		ContentSectionTemplate contentSectionTemplate = new ContentSectionTemplate();
		contentSectionTemplate.setTemplateId(templateId);
		contentSectionTemplate = contentSectionTemplate.find();

		return sendSingleEntityResponse(ContentSectionTemplateView.toView(contentSectionTemplate));
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_SECTION_CREATE)
	@APIDescription("Creates a content section template")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ContentSectionTemplateView.class)
	public Response createSectionTemplate(ContentSectionTemplateView view)
	{
		return saveContentTemplateView(view, true);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_SECTION_UPDATE)
	@APIDescription("Updates a content section template")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ContentSectionTemplateView.class)
	@Path("/{templateId}")
	public Response updateSectionTemplate(
			@PathParam("templateId") String templateId,
			ContentSectionTemplateView view
	)
	{
		ContentSectionTemplate existing = new ContentSectionTemplate();
		existing.setTemplateId(templateId);
		existing = existing.find();
		if (existing == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		view.getContentSectionTemplate().setTemplateId(templateId);
		return saveContentTemplateView(view, false);
	}

	private Response saveContentTemplateView(ContentSectionTemplateView view, boolean create)
	{
		ValidationResult validationResult = view.validate();
		if (validationResult.valid()) {
			String templateId = service.getContentSectionService().saveSectionTemplate(view);

			if (create) {
				return Response.created(URI.create("v1/resource/contentsectiontemplates/" + templateId + "/details")).entity(view).build();
			} else {
				return Response.ok(view).build();
			}
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_SECTION_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Activates a section template")
	@Path("/{templateId}/activate")
	public Response activateSectionTemplate(
			@PathParam("templateId") String templateId
	)
	{
		return updateStatus(templateId, ChecklistTemplate.ACTIVE_STATUS);
	}

	private Response updateStatus(String templateId, String status)
	{
		ContentSectionTemplate checklistSectionTemplate = new ContentSectionTemplate();
		checklistSectionTemplate.setTemplateId(templateId);
		checklistSectionTemplate = checklistSectionTemplate.find();
		if (checklistSectionTemplate != null) {
			checklistSectionTemplate.setActiveStatus(status);
			checklistSectionTemplate.save();
		}
		return sendSingleEntityResponse(checklistSectionTemplate);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_SECTION_READ)
	@Produces({MediaType.TEXT_PLAIN})
	@APIDescription("Check to see if checklist template is in use; returns true if in use or no content if not.")
	@Path("/{templateId}/inuse")
	public Response templateInUse(
			@PathParam("templateId") String templateId
	)
	{
		Boolean inUse;
		ContentSectionTemplate contentSectionTemplate = new ContentSectionTemplate();
		contentSectionTemplate.setTemplateId(templateId);
		contentSectionTemplate = contentSectionTemplate.find();
		if (contentSectionTemplate != null) {
			inUse = service.getContentSectionService().isContentTemplateBeingUsed(templateId);
			return Response.ok(inUse.toString()).build();

		} else {
			return sendSingleEntityResponse(null);
		}
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_SECTION_DELETE)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Inactivates a template section or removes on force")
	@Path("/{templateId}")
	public Response deleteContentSectionTemplate(
			@PathParam("templateId") String templateId,
			@QueryParam("force") boolean force
	)
	{
		if (force) {
			service.getContentSectionService().deleteContentTemplate(templateId);
			return Response.noContent().build();
		} else {
			return updateStatus(templateId, ChecklistTemplate.INACTIVE_STATUS);
		}
	}

}
