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
import edu.usu.sdl.openstorefront.core.entity.ContentSubsectionTemplate;
import edu.usu.sdl.openstorefront.core.view.ContentSectionTemplateView;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.doc.security.RequireAdmin;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ContentSectionTemplate.class)
	@APIDescription("Gets templates")
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
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ContentSectionTemplate.class)
	@APIDescription("Gets a content template")
	@Path("{templateId}")
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
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ContentSectionTemplateView.class)
	@APIDescription("Gets a the resolved template")
	@Path("{templateId}/details")
	public Response getContentTemplateDetails(
			@PathParam("templateId") String templateId
	)
	{
		ContentSectionTemplate contentSectionTemplate = new ContentSectionTemplate();
		contentSectionTemplate.setTemplateId(templateId);
		contentSectionTemplate = contentSectionTemplate.find();

		return sendSingleEntityResponse(ContentSectionTemplateView.toView(contentSectionTemplate));
	}

	//TODO: Handle creating a section before the template.
//	@POST
//	@RequireAdmin
//	@APIDescription("Creates a content section template")
//	@Consumes({MediaType.APPLICATION_JSON})
//	@Produces({MediaType.APPLICATION_JSON})
//	@DataType(ContentSectionTemplate.class)
//	public Response createSectionTemplate(ContentSectionTemplate contentSectionTemplate)
//	{
//		return saveContentTemplate(contentSectionTemplate, true);
//	}
//
//	@PUT
//	@RequireAdmin
//	@APIDescription("Updates a content section template")
//	@Consumes({MediaType.APPLICATION_JSON})
//	@Produces({MediaType.APPLICATION_JSON})
//	@DataType(ContentSectionTemplate.class)
//	@Path("/{templateId}")
//	public Response updateSectionTemplate(
//			@PathParam("templateId") String templateId,
//			ContentSectionTemplate contentSectionTemplate
//	)
//	{
//		ContentSectionTemplate existing = new ContentSectionTemplate();
//		existing.setTemplateId(templateId);
//		existing = existing.find();
//		if (existing == null) {
//			return Response.status(Response.Status.NOT_FOUND).build();
//		}
//		contentSectionTemplate.setTemplateId(templateId);
//		return saveContentTemplate(contentSectionTemplate, false);
//	}
//
//	private Response saveContentTemplate(ContentSectionTemplate contentSectionTemplate, boolean create)
//	{
//		ValidationResult validationResult = contentSectionTemplate.validate();
//		if (validationResult.valid()) {
//			contentSectionTemplate = contentSectionTemplate.save();
//
//			if (create) {
//				return Response.created(URI.create("v1/resource/contentsectiontemplates/" + contentSectionTemplate.getTemplateId())).entity(contentSectionTemplate).build();
//			} else {
//				return Response.ok(contentSectionTemplate).build();
//			}
//		} else {
//			return Response.ok(validationResult.toRestError()).build();
//		}
//	}
	@PUT
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Activates a section template")
	@Path("{sectionId}")
	public Response activateSectionTemplate(
			@PathParam("sectionId") String sectionId
	)
	{
		return updateStatus(sectionId, ChecklistTemplate.ACTIVE_STATUS);
	}

	private Response updateStatus(String sectionId, String status)
	{
		ContentSectionTemplate checklistSectionTemplate = new ContentSectionTemplate();
		checklistSectionTemplate.setContentSectionId(sectionId);
		checklistSectionTemplate = checklistSectionTemplate.find();
		if (checklistSectionTemplate != null) {
			checklistSectionTemplate.setActiveStatus(status);
			checklistSectionTemplate.save();
		}
		return sendSingleEntityResponse(checklistSectionTemplate);
	}

	@DELETE
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Inactivates a template section or removes on force")
	@Path("{sectionId}")
	public Response deleteChecklistSection(
			@PathParam("sectionId") String sectionId,
			@QueryParam("force") boolean force
	)
	{
		if (force) {
			ContentSectionTemplate checklistSectionTemplate = new ContentSectionTemplate();
			checklistSectionTemplate.setContentSectionId(sectionId);
			checklistSectionTemplate = checklistSectionTemplate.find();
			if (checklistSectionTemplate != null) {
				checklistSectionTemplate.delete();
			}
			return Response.noContent().build();
		} else {
			return updateStatus(sectionId, ChecklistTemplate.INACTIVE_STATUS);
		}
	}

	@GET
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ContentSubsectionTemplate.class)
	@APIDescription("Gets subsection for a template")
	@Path("{templateId}/subsections")
	public List<ContentSubsectionTemplate> getContentSubsectionTemplates(
			@BeanParam FilterQueryParams filterQueryParams,
			@PathParam("templateId") String templateId
	)
	{
		ContentSubsectionTemplate contentSubsectionTemplate = new ContentSubsectionTemplate();
		contentSubsectionTemplate.setTemplateId(templateId);
		if (!filterQueryParams.getAll()) {
			contentSubsectionTemplate.setActiveStatus(filterQueryParams.getStatus());
		}
		List<ContentSubsectionTemplate> templates = contentSubsectionTemplate.findByExample();
		templates = filterQueryParams.filter(templates);
		return templates;
	}

	//TODO: Handle creating a sub-section before the template.
//	@POST
//	@RequireAdmin
//	@APIDescription("Creates a content section template")
//	@Consumes({MediaType.APPLICATION_JSON})
//	@Produces({MediaType.APPLICATION_JSON})
//	@DataType(ContentSubsectionTemplate.class)
//	@Path("/{templateId}/subsection")
//	public Response createSubSsctionTemplate(ContentSubsectionTemplate contentSubsectionTemplate)
//	{
//		return saveContentSubTemplate(contentSubsectionTemplate, true);
//	}
//
//	@PUT
//	@RequireAdmin
//	@APIDescription("Updates a content sub-section template")
//	@Consumes({MediaType.APPLICATION_JSON})
//	@Produces({MediaType.APPLICATION_JSON})
//	@DataType(ContentSectionTemplate.class)
//	@Path("/{templateId}/subsection/{subsectionId}")
//	public Response updateSubsectionTemplate(
//			@PathParam("templateId") String templateId,
//			@PathParam("subsectionId") String subsectionId,
//			ContentSubsectionTemplate contentSubsectionTemplate
//	)
//	{
//		ContentSubsectionTemplate existing = new ContentSubsectionTemplate();
//		existing.setTemplateId(templateId);
//		existing = existing.find();
//		if (existing == null) {
//			return Response.status(Response.Status.NOT_FOUND).build();
//		}
//		contentSubsectionTemplate.setTemplateId(templateId);
//		return saveContentSubTemplate(contentSubsectionTemplate, false);
//	}
//
//	private Response saveContentSubTemplate(ContentSubsectionTemplate contentSubsectionTemplate, boolean create)
//	{
//		ValidationResult validationResult = contentSubsectionTemplate.validate();
//		if (validationResult.valid()) {
//			contentSubsectionTemplate = contentSubsectionTemplate.save();
//
//			if (create) {
//				return Response.created(URI.create("v1/resource/contentsectiontemplates/" + contentSubsectionTemplate.getTemplateId())).entity(contentSubsectionTemplate).build();
//			} else {
//				return Response.ok(contentSubsectionTemplate).build();
//			}
//		} else {
//			return Response.ok(validationResult.toRestError()).build();
//		}
//	}
	@DELETE
	@RequireAdmin
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("removes on sub-section template")
	@Path("{templateId}/subsections/{sectionId}")
	public void deleteChecklistSubSectionTemplate(
			@PathParam("templateId") String templateId,
			@PathParam("sectionId") String sectionId
	)
	{
		ContentSectionTemplate checklistSubSectionTemplate = new ContentSectionTemplate();
		checklistSubSectionTemplate.setContentSectionId(sectionId);
		checklistSubSectionTemplate.setTemplateId(templateId);
		checklistSubSectionTemplate = checklistSubSectionTemplate.find();
		if (checklistSubSectionTemplate != null) {
			checklistSubSectionTemplate.delete();
		}

	}

}
