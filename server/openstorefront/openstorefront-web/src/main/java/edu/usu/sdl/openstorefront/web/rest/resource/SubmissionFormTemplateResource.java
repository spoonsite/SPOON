/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.web.rest.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormTemplate;
import edu.usu.sdl.openstorefront.core.entity.SubmissionTemplateStatus;
import edu.usu.sdl.openstorefront.core.entity.UserSubmission;
import edu.usu.sdl.openstorefront.core.model.VerifySubmissionTemplateResult;
import edu.usu.sdl.openstorefront.core.view.SubmissionFormTemplateView;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/submissiontemplates")
@APIDescription("Submission Template Resource")
public class SubmissionFormTemplateResource
		extends BaseResource
{

	@GET
	@APIDescription("Gets Submission Templates")
	@RequireSecurity(SecurityPermission.ADMIN_SUBMISSION_FORM_TEMPLATE_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SubmissionFormTemplateView.class)
	public List<SubmissionFormTemplateView> getSubmissionFormTemplates(
			@QueryParam("status") String status
	)
	{
		SubmissionFormTemplate submissionFormTemplate = new SubmissionFormTemplate();
		if (StringUtils.isNotBlank(status)) {
			submissionFormTemplate.setActiveStatus(status);
		}
		return SubmissionFormTemplateView.toView(submissionFormTemplate.findByExample());
	}

	@GET
	@APIDescription("Gets Submission Templates")
	@RequireSecurity(SecurityPermission.ADMIN_SUBMISSION_FORM_TEMPLATE_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/verified")
	@DataType(SubmissionFormTemplateView.class)
	public List<SubmissionFormTemplateView> getVerifiedSubmissionFormTemplates()
	{
		SubmissionFormTemplate submissionFormTemplate = new SubmissionFormTemplate();
		submissionFormTemplate.setActiveStatus(SubmissionFormTemplate.ACTIVE_STATUS);
		submissionFormTemplate.setTemplateStatus(SubmissionTemplateStatus.VERIFIED);
		return SubmissionFormTemplateView.toView(submissionFormTemplate.findByExample());
	}

	@GET
	@APIDescription("Gets Verified Templates")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SubmissionFormTemplateView.class)
	@Path("/{templateId}")
	public Response getSubmissionFormTemplate(
			@PathParam("templateId") String templateId
	)
	{
		SubmissionFormTemplate submissionFormTemplate = new SubmissionFormTemplate();
		submissionFormTemplate.setSubmissionTemplateId(templateId);
		submissionFormTemplate = submissionFormTemplate.find();

		SubmissionFormTemplateView view = null;
		if (submissionFormTemplate != null) {
			view = SubmissionFormTemplateView.toView(submissionFormTemplate);
		}
		return sendSingleEntityResponse(view);
	}

	@GET
	@APIDescription("Gets Template")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SubmissionFormTemplateView.class)
	@Path("/default")
	public Response getDefaultSubmissionFormTemplate()
	{
		SubmissionFormTemplate submissionFormTemplate = new SubmissionFormTemplate();
		submissionFormTemplate.setDefaultTemplate(Boolean.TRUE);
		submissionFormTemplate = submissionFormTemplate.find();

		SubmissionFormTemplateView view = null;
		if (submissionFormTemplate != null) {
			view = SubmissionFormTemplateView.toView(submissionFormTemplate);
		}
		return sendSingleEntityResponse(view);
	}

	@GET
	@APIDescription("Get template from an entry type, return default template if no valid template is available")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SubmissionFormTemplateView.class)
	@Path("/componenttype/{componentType}")
	public Response getSubmissionFormTemplateFromEntryType(@PathParam("componentType") String componentType)
	{
		SubmissionFormTemplate submissionFormTemplate = new SubmissionFormTemplate();
		submissionFormTemplate.setEntryType(componentType);
		submissionFormTemplate.setActiveStatus(SubmissionFormTemplate.ACTIVE_STATUS);
		submissionFormTemplate.setTemplateStatus(SubmissionTemplateStatus.VERIFIED);
		submissionFormTemplate = submissionFormTemplate.find();

		if (submissionFormTemplate != null) {
			SubmissionFormTemplateView view = SubmissionFormTemplateView.toView(submissionFormTemplate);
			return sendSingleEntityResponse(view);
		} else {
			return getDefaultSubmissionFormTemplate();
		}
	}

	@POST
	@APIDescription("Exports questions in JSON format.")
	@RequireSecurity(SecurityPermission.ADMIN_SUBMISSION_FORM_TEMPLATE_READ)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/export")
	public Response exportFormTemplate(
			@FormParam("id") List<String> multipleIds
	)
	{
		List<SubmissionFormTemplate> templates = new ArrayList<>();
		for (String id : multipleIds) {
			SubmissionFormTemplate template = new SubmissionFormTemplate();
			template.setSubmissionTemplateId(id);
			template = template.find();

			if (template != null) {
				templates.add(template);
			}
		}

		String data;
		try {
			data = StringProcessor.defaultObjectMapper().writeValueAsString(templates);
		} catch (JsonProcessingException ex) {
			throw new OpenStorefrontRuntimeException("Unable to export Submission Template.  Unable able to generate JSON.", ex);
		}

		Response.ResponseBuilder response = Response.ok(data);
		response.header("Content-Type", MediaType.APPLICATION_JSON);
		response.header("Content-Disposition", "attachment; filename=\"submissiontemplates.json\"");
		return response.build();
	}

	@POST
	@APIDescription("Creates a new Submission Template")
	@RequireSecurity(SecurityPermission.ADMIN_SUBMISSION_FORM_TEMPLATE_CREATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(SubmissionFormTemplate.class)
	public Response createSubmissionTemplate(
			SubmissionFormTemplate submissionFormTemplate
	)
	{
		return handleSaveSubmissionTemplate(submissionFormTemplate, true);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_SUBMISSION_FORM_TEMPLATE_UPDATE)
	@APIDescription("Updates an Submission Template")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(SubmissionFormTemplate.class)
	@Path("/{templateId}")
	public Response updateSubmissionTemplate(
			@PathParam("templateId") String templateId,
			SubmissionFormTemplate submissionFormTemplate)
	{
		SubmissionFormTemplate existing = new SubmissionFormTemplate();
		existing.setSubmissionTemplateId(templateId);
		existing = existing.find();

		Response response = Response.status(Response.Status.NOT_FOUND).build();
		if (existing != null) {
			submissionFormTemplate.setSubmissionTemplateId(templateId);
			response = handleSaveSubmissionTemplate(existing, false);
		}
		return response;
	}

	private Response handleSaveSubmissionTemplate(SubmissionFormTemplate submissionFormTemplate, boolean post)
	{
		ValidationResult validationResult = submissionFormTemplate.validate();
		if (validationResult.valid()) {
			submissionFormTemplate = service.getSubmissionFormService().saveSubmissionFormTemplate(submissionFormTemplate);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create("v1/resource/usersubmissions/" + submissionFormTemplate.getSubmissionTemplateId())).entity(submissionFormTemplate).build();
		} else {
			return Response.ok(submissionFormTemplate).build();
		}
	}

	@PUT
	@APIDescription("Activate Form template")
	@RequireSecurity(SecurityPermission.ADMIN_SUBMISSION_FORM_TEMPLATE_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{templateId}/activate")
	public Response activateTemplate(
			@PathParam("templateId") String templateId
	)
	{
		return updateStatus(templateId, SubmissionFormTemplate.ACTIVE_STATUS);
	}

	@PUT
	@APIDescription("Inactivate Form template")
	@RequireSecurity(SecurityPermission.ADMIN_SUBMISSION_FORM_TEMPLATE_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{templateId}/inactivate")
	public Response inactivateTemplate(
			@PathParam("templateId") String templateId
	)
	{
		return updateStatus(templateId, SubmissionFormTemplate.INACTIVE_STATUS);
	}

	private Response updateStatus(String templateId, String newStatus)
	{
		SubmissionFormTemplate targetSubmissionFormTemplate = new SubmissionFormTemplate();
		targetSubmissionFormTemplate.setSubmissionTemplateId(templateId);
		targetSubmissionFormTemplate = targetSubmissionFormTemplate.find();

		// set active status for the target template
		if (targetSubmissionFormTemplate != null) {
			service.getSubmissionFormService().toggleActiveStatus(templateId, newStatus);
			targetSubmissionFormTemplate.setActiveStatus(newStatus);
		}

		return sendSingleEntityResponse(targetSubmissionFormTemplate);
	}

	@PUT
	@APIDescription("Verifies template with temporary submission - it deletes the submission after verification")
	@RequireSecurity(SecurityPermission.ADMIN_SUBMISSION_FORM_TEMPLATE_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{templateId}/verify/{userSubmissionId}")
	public Response verifyTemplate(
			@PathParam("templateId") String templateId,
			@PathParam("userSubmissionId") String userSubmissionId
	)
	{
		SubmissionFormTemplate submissionFormTemplate = new SubmissionFormTemplate();
		submissionFormTemplate.setSubmissionTemplateId(templateId);
		submissionFormTemplate = submissionFormTemplate.find();
		if (submissionFormTemplate != null) {

			UserSubmission userSubmission = new UserSubmission();
			userSubmission.setUserSubmissionId(userSubmissionId);
			userSubmission = userSubmission.find();

			if (userSubmission != null) {
				VerifySubmissionTemplateResult result = service.getSubmissionFormService().verifySubmission(userSubmission);
				service.getSubmissionFormService().deleteUserSubmission(userSubmission.getUserSubmissionId());
				if (result.getValidationResult().valid()) {
					return Response.ok().build();
				} else {
					return sendSingleEntityResponse(result.getValidationResult().toRestError());
				}
			} else {
				return sendSingleEntityResponse(submissionFormTemplate);
			}

		}
		return sendSingleEntityResponse(submissionFormTemplate);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_SUBMISSION_FORM_TEMPLATE_DELETE)
	@APIDescription("Deletes a submission template")
	@Path("/{templateId}")
	public void deleteUserSubmission(
			@PathParam("templateId") String templateId
	)
	{
		service.getSubmissionFormService().deleteSubmissionFormTemplate(templateId);
	}

}
