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
import edu.usu.sdl.openstorefront.core.entity.ChecklistTemplateQuestion;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.model.UpdateEvaluationChecklistModel;
import edu.usu.sdl.openstorefront.core.view.ChecklistTemplateDetailView;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
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
@APIDescription("Provides access to checklist resources")
@Path("v1/resource/checklisttemplates")
public class ChecklistTemplateResource
		extends BaseResource
{

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ChecklistTemplate.class)
	@APIDescription("Gets templates")
	public List<ChecklistTemplate> getchecklistTemplates(@BeanParam FilterQueryParams filterQueryParams)
	{
		ChecklistTemplate checklistTemplateExample = new ChecklistTemplate();
		if (!filterQueryParams.getAll()) {
			checklistTemplateExample.setActiveStatus(filterQueryParams.getStatus());
		}
		List<ChecklistTemplate> templates = checklistTemplateExample.findByExample();
		templates = filterQueryParams.filter(templates);
		return templates;
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ChecklistTemplate.class)
	@APIDescription("Gets a template")
	@Path("/{templateId}")
	public Response getChecklistTemplate(
			@PathParam("templateId") String templateId
	)
	{
		ChecklistTemplate checklistTemplate = new ChecklistTemplate();
		checklistTemplate.setChecklistTemplateId(templateId);
		checklistTemplate = checklistTemplate.find();
		return sendSingleEntityResponse(checklistTemplate);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ChecklistTemplateDetailView.class)
	@APIDescription("Gets a detailed template")
	@Path("/{templateId}/details")
	public Response getchecklistTemplateDetails(
			@PathParam("templateId") String templateId
	)
	{
		ChecklistTemplateDetailView view = null;
		ChecklistTemplate checklistTemplate = new ChecklistTemplate();
		checklistTemplate.setChecklistTemplateId(templateId);
		checklistTemplate = checklistTemplate.find();
		if (checklistTemplate != null) {
			view = ChecklistTemplateDetailView.toView(checklistTemplate);
		}
		return sendSingleEntityResponse(view);
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST)
	@APIDescription("Creates a checklist template")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ChecklistTemplate.class)
	public Response createChecklistTemplate(UpdateEvaluationChecklistModel model)
	{
		return saveChecklistTemplate(model.getChecklistTemplate(), true, null);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST)
	@APIDescription("Updates a checklist template")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ChecklistTemplate.class)
	@Path("/{templateId}")
	public Response updateChecklistTemplate(
			@PathParam("templateId") String templateId,
			UpdateEvaluationChecklistModel model)
	{
		ChecklistTemplate existing = new ChecklistTemplate();
		existing.setChecklistTemplateId(templateId);
		existing = existing.find();
		if (existing == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		model.getChecklistTemplate().setChecklistTemplateId(templateId);
		return saveChecklistTemplate(model.getChecklistTemplate(), false, model.getEvaluationIdsToUpdate());
	}

	private Response saveChecklistTemplate(ChecklistTemplate checklistTemplate, boolean create, List<String> evaluationIdsToUpdate)
	{
		ValidationResult validationResult = checklistTemplate.validate();
		if (validationResult.valid()) {
			//mark the order
			int order = 1;
			for (ChecklistTemplateQuestion templateQuestion : checklistTemplate.getQuestions()) {
				templateQuestion.setSortOrder(order++);
			}
			checklistTemplate = checklistTemplate.save();

			if (create) {
				return Response.created(URI.create("v1/resource/checklisttemplates/" + checklistTemplate.getChecklistTemplateId())).entity(checklistTemplate).build();
			} else {
				service.getEvaluationService().updateEvaluationsToLatestTemplateVersion(evaluationIdsToUpdate);
				return Response.ok(checklistTemplate).build();
			}
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Activates a template")
	@Path("/{templateId}/activate")
	public Response activateChecklistTemplate(
			@PathParam("templateId") String templateId
	)
	{
		return updateStatus(templateId, ChecklistTemplate.ACTIVE_STATUS);
	}

	private Response updateStatus(String templateId, String status)
	{
		ChecklistTemplate checklistTemplate = new ChecklistTemplate();
		checklistTemplate.setChecklistTemplateId(templateId);
		checklistTemplate = checklistTemplate.find();
		if (checklistTemplate != null) {
			checklistTemplate.setActiveStatus(status);
			checklistTemplate.save();
		}
		return sendSingleEntityResponse(checklistTemplate);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST)
	@Produces({MediaType.TEXT_PLAIN})
	@APIDescription("Check to see if checklist template is in use; returns true if in use or no content if not.")
	@Path("/{templateId}/inuse")
	public Response checklistInUse(
			@PathParam("templateId") String templateId
	)
	{
		Boolean inUse;
		ChecklistTemplate checklistTemplate = new ChecklistTemplate();
		checklistTemplate.setChecklistTemplateId(templateId);
		checklistTemplate = checklistTemplate.find();
		if (checklistTemplate != null) {
			inUse = service.getChecklistService().isChecklistTemplateBeingUsed(templateId);
			return Response.ok(inUse.toString()).build();

		} else {
			return sendSingleEntityResponse(null);
		}
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CHECKLIST)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Inactivates or hard removes a template only if it's not in use")
	@Path("/{templateId}")
	public Response deleteChecklistTemplate(
			@PathParam("templateId") String templateId,
			@QueryParam("force") boolean force
	)
	{
		if (force) {
			service.getChecklistService().deleteChecklistTemplate(templateId);
			return Response.noContent().build();
		} else {
			return updateStatus(templateId, ChecklistTemplate.INACTIVE_STATUS);
		}
	}

}
