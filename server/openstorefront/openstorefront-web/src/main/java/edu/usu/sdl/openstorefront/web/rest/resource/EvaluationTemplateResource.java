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
import edu.usu.sdl.openstorefront.core.entity.EvaluationTemplate;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.model.UpdateEvaluationTemplateModel;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.doc.security.LogicOperation;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Provides access to evaluations templates resources")
@Path("v1/resource/evaluationtemplates")
public class EvaluationTemplateResource
		extends BaseResource
{

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(EvaluationTemplate.class)
	@APIDescription("Gets Evaluation templates")
	public Response getEvaluationTemplates(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		EvaluationTemplate evaluationTemplate = new EvaluationTemplate();
		if (!filterQueryParams.getAll()) {
			evaluationTemplate.setActiveStatus(filterQueryParams.getStatus());
		}
		List<EvaluationTemplate> templates = evaluationTemplate.findByExample();

		GenericEntity<List<EvaluationTemplate>> entity = new GenericEntity<List<EvaluationTemplate>>(templates)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ChecklistTemplate.class)
	@APIDescription("Gets a template")
	@Path("/{templateId}")
	public Response getEvaluationTemplate(
			@PathParam("templateId") String templateId
	)
	{
		EvaluationTemplate evaluationTemplate = new EvaluationTemplate();
		evaluationTemplate.setTemplateId(templateId);
		evaluationTemplate = evaluationTemplate.find();
		return sendSingleEntityResponse(evaluationTemplate);
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_CREATE)
	@APIDescription("Creates an evaluation template")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(EvaluationTemplate.class)
	public Response createEvaluationTemplate(UpdateEvaluationTemplateModel model)
	{
		EvaluationTemplate evaluationTemplate = model.getEvaluationTemplate();
		ValidationResult validationResult = evaluationTemplate.validate();
		if (validationResult.valid()) {
			evaluationTemplate = evaluationTemplate.save();
			return Response.created(URI.create("v1/resource/evaluationtemplates/" + evaluationTemplate.getTemplateId())).entity(evaluationTemplate).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_UPDATE)
	@APIDescription("Updates a evaluation template")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ChecklistTemplate.class)
	@Path("/{templateId}")
	public Response updateChecklistTemplate(
			@PathParam("templateId") String templateId,
			UpdateEvaluationTemplateModel model)
	{
		EvaluationTemplate evaluationTemplate = model.getEvaluationTemplate();
		EvaluationTemplate existingTemplate = new EvaluationTemplate();
		existingTemplate.setTemplateId(templateId);
		existingTemplate = existingTemplate.find();
		if (existingTemplate == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		evaluationTemplate.setTemplateId(templateId);
		ValidationResult validationResult = evaluationTemplate.validate();

		if (validationResult.valid()) {
			evaluationTemplate = evaluationTemplate.save();
			service.getEvaluationService().setEvaluationUpdatePending(templateId, model.getEvaluationIdsToUpdate());
			service.getEvaluationService().updateEvaluationsToLatestTemplateVersion(model.getEvaluationIdsToUpdate());
			return Response.ok(evaluationTemplate).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Activates a template")
	@Path("/{templateId}/activate")
	public Response activateEvaluationTemplate(
			@PathParam("templateId") String templateId
	)
	{
		return updateStatus(templateId, EvaluationTemplate.ACTIVE_STATUS);
	}

	private Response updateStatus(String templateId, String status)
	{
		EvaluationTemplate evaluationTemplate = new EvaluationTemplate();
		evaluationTemplate.setTemplateId(templateId);
		evaluationTemplate = evaluationTemplate.find();
		if (evaluationTemplate != null) {
			evaluationTemplate.setActiveStatus(status);
			evaluationTemplate.save();
		}
		return sendSingleEntityResponse(evaluationTemplate);
	}

	@DELETE
	@RequireSecurity(value = {SecurityPermission.ADMIN_EVALUATION_TEMPLATE_DELETE, SecurityPermission.ADMIN_EVALUATION_TEMPLATE_UPDATE},
		logicOperator = LogicOperation.OR
	)
	@Produces({MediaType.APPLICATION_JSON})
	@APIDescription("Inactivates or hard removes a template")
	@Path("/{templateId}")
	public Response deleteEvaluationTemplate(
			@PathParam("templateId") String templateId,
			@QueryParam("force") boolean force
	)
	{
		if (force) {
			if (SecurityUtil.hasPermission(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_DELETE)) {
				EvaluationTemplate evaluationTemplate = new EvaluationTemplate();
				evaluationTemplate.setTemplateId(templateId);
				evaluationTemplate = evaluationTemplate.find();
				if (evaluationTemplate != null) {
					evaluationTemplate.delete();
				}
				return Response.noContent().build();
			}
			else {
				return Response.status(Response.Status.FORBIDDEN).build();
			}
		} else {
			if (SecurityUtil.hasPermission(SecurityPermission.ADMIN_EVALUATION_TEMPLATE_UPDATE)) {
				return updateStatus(templateId, EvaluationTemplate.INACTIVE_STATUS);
			}
			else {
				return Response.status(Response.Status.FORBIDDEN).build();
			}
		}
	}

}
