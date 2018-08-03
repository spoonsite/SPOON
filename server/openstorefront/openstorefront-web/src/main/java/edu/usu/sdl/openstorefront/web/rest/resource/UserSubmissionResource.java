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

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.UserSubmission;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionMedia;
import edu.usu.sdl.openstorefront.core.view.UserSubmissionMediaView;
import edu.usu.sdl.openstorefront.core.view.UserSubmissionView;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/usersubmissions")
@APIDescription("Provides access to User submissions")
public class UserSubmissionResource
		extends BaseResource
{

	@GET
	@APIDescription("Gets all user submissions for all users; Note: these are incomplete submissions")
	@RequireSecurity(SecurityPermission.ADMIN_USER_SUBMISSIONS_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserSubmission.class)
	public List<UserSubmission> getUserSubmissions()
	{
		UserSubmission userSubmissionExample = new UserSubmission();
		return userSubmissionExample.findByExample();
	}

	@GET
	@APIDescription("Gets all user submissions for all users; Note: these are incomplete submissions")
	@RequireSecurity(SecurityPermission.ADMIN_USER_SUBMISSIONS_READ)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserSubmissionView.class)
	@Path("/admin")
	public List<UserSubmissionView> getUserSubmissionsAdmin()
	{
		UserSubmission userSubmissionExample = new UserSubmission();
		return UserSubmissionView.toView(userSubmissionExample.findByExample());
	}

	@GET
	@RequireSecurity(SecurityPermission.USER_SUBMISSIONS_READ)
	@APIDescription("Gets user submissions for current user")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserSubmission.class)
	@Path("/currentuser")
	public List<UserSubmission> getUserSubmissionForUser()
	{
		UserSubmission userSubmissionExample = new UserSubmission();
		userSubmissionExample.setActiveStatus(UserSubmission.ACTIVE_STATUS);
		userSubmissionExample.setOwnerUsername(SecurityUtil.getCurrentUserName());
		return userSubmissionExample.findByExample();
	}

	@GET
	@RequireSecurity(SecurityPermission.USER_SUBMISSIONS_READ)
	@APIDescription("Gets a submission")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserSubmission.class)
	@Path("/{submissionId}")
	public Response getUserSubmission(
			@PathParam("submissionId") String submissionId
	)
	{
		UserSubmission userSubmission = new UserSubmission();
		userSubmission.setUserSubmissionId(submissionId);
		userSubmission = userSubmission.find();

		Response response = ownerCheck(userSubmission, SecurityPermission.ADMIN_USER_SUBMISSIONS);
		if (response == null) {
			response = sendSingleEntityResponse(userSubmission);
		}
		return response;
	}

	@GET
	@RequireSecurity(SecurityPermission.USER_SUBMISSIONS_READ)
	@APIDescription("Gets a submission media records")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserSubmissionMediaView.class)
	@Path("/{submissionId}/media")
	public Response getUserSubmissionMedia(
			@PathParam("submissionId") String submissionId
	)
	{
		UserSubmission userSubmission = new UserSubmission();
		userSubmission.setUserSubmissionId(submissionId);
		userSubmission = userSubmission.find();

		Response response = ownerCheck(userSubmission, SecurityPermission.ADMIN_USER_SUBMISSIONS);
		if (response == null) {
			UserSubmissionMedia media = new UserSubmissionMedia();
			media.setUserSubmissionId(submissionId);
			List<UserSubmissionMedia> allMedia = media.findByExample();

			GenericEntity<List<UserSubmissionMediaView>> entity = new GenericEntity<List<UserSubmissionMediaView>>(UserSubmissionMediaView.toView(allMedia))
			{
			};
			response = sendSingleEntityResponse(entity);
		}
		return response;
	}

	@POST
	@APIDescription("Creates a new Submission")
	@RequireSecurity(SecurityPermission.USER_SUBMISSIONS_CREATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(UserSubmission.class)
	public Response createUserSubmission(
			UserSubmission userSubmission
	)
	{
		return handleSaveSubmission(userSubmission, true);
	}

	//update submission	(submission - owner/admin) FIX Admin permission
	@PUT
	@RequireSecurity(SecurityPermission.USER_SUBMISSIONS_UPDATE)
	@APIDescription("Updates an user submission")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserSubmission.class)
	@Path("/{submissionId}")
	public Response updateUserSubmission(
			@PathParam("submissionId") String submissionId,
			UserSubmission userSubmission)
	{
		UserSubmission existing = new UserSubmission();
		existing.setUserSubmissionId(submissionId);
		existing = existing.find();

		Response response = Response.status(Response.Status.NOT_FOUND).build();
		if (existing != null) {
			response = ownerCheck(existing, SecurityPermission.ADMIN_USER_SUBMISSIONS);
			if (response == null) {
				userSubmission.setUserSubmissionId(submissionId);
				response = handleSaveSubmission(userSubmission, false);
			}
		}
		return response;
	}

	private Response handleSaveSubmission(UserSubmission userSubmission, boolean post)
	{
		ValidationResult validationResult = userSubmission.validate();
		if (validationResult.valid()) {
			userSubmission = service.getSubmissionFormService().saveUserSubmission(userSubmission);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create("v1/resource/usersubmissions/" + userSubmission.getUserSubmissionId())).entity(userSubmission).build();
		} else {
			return Response.ok(userSubmission).build();
		}
	}

	@PUT
	@APIDescription("Submits a submission for approval")
	@RequireSecurity(SecurityPermission.USER_SUBMISSIONS_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{submissionId}/submitforapproval")
	public Response submitForApproval(
			@PathParam("submissionId") String submissionId
	)
	{
		UserSubmission existing = new UserSubmission();
		existing.setUserSubmissionId(submissionId);
		existing = existing.find();

		Response response = Response.status(Response.Status.NOT_FOUND).build();
		if (existing != null) {
			response = ownerCheck(existing, SecurityPermission.ADMIN_USER_SUBMISSIONS);
			if (response == null) {
				ValidationResult validateResult = service.getSubmissionFormService().submitUserSubmissionForApproval(existing);
				if (!validateResult.valid()) {
					response = Response.ok(validateResult.toRestError()).build();
				} else {
					response = Response.ok().build();
				}
			}
		}
		return response;
	}

	@PUT
	@APIDescription("Submits a change request for approval")
	@RequireSecurity(SecurityPermission.USER_SUBMISSIONS_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{submissionId}/submitchangeforapproval")
	public Response submitChangeForApproval(
			@PathParam("submissionId") String submissionId
	)
	{
		UserSubmission existing = new UserSubmission();
		existing.setUserSubmissionId(submissionId);
		existing = existing.find();

		Response response = Response.status(Response.Status.NOT_FOUND).build();
		if (existing != null) {
			response = ownerCheck(existing, SecurityPermission.ADMIN_USER_SUBMISSIONS);
			if (response == null) {
				ValidationResult validateResult = service.getSubmissionFormService().submitChangeRequestForApproval(existing);
				if (!validateResult.valid()) {
					response = Response.ok(validateResult.toRestError()).build();
				} else {
					response = Response.ok().build();
				}
			} else {
				return Response.status(Response.Status.FORBIDDEN).build();
			}
		}
		return response;
	}

	@PUT
	@APIDescription("Reassign Owner on a submission")
	@RequireSecurity(SecurityPermission.ADMIN_USER_SUBMISSIONS_UPDATE)
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{submissionId}/reassignowner/{username}")
	public Response reassignOwner(
			@PathParam("submissionId") String submissionId,
			@PathParam("username")
			@APIDescription("URL Encode Username to avoid issues") String username
	)
	{
		UserSubmission userSubmission = new UserSubmission();
		userSubmission.setUserSubmissionId(submissionId);
		userSubmission = userSubmission.find();

		if (userSubmission != null) {
			service.getSubmissionFormService().reassignUserSubmission(submissionId, username);
			userSubmission.setOwnerUsername(username);
		}
		return sendSingleEntityResponse(userSubmission);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.USER_SUBMISSIONS_DELETE)
	@APIDescription("Deletes a submission")
	@Path("/{submissionId}")
	public Response deleteUserSubmission(
			@PathParam("submissionId") String submissionId
	)
	{
		UserSubmission userSubmission = new UserSubmission();
		userSubmission.setUserSubmissionId(submissionId);
		userSubmission = userSubmission.find();

		Response response = Response.noContent().build();
		if (userSubmission != null) {
			response = ownerCheck(userSubmission, SecurityPermission.ADMIN_USER_SUBMISSIONS);
			if (response == null) {
				service.getSubmissionFormService().deleteUserSubmission(submissionId);
				response = Response.noContent().build();
			}
		}
		return response;
	}

	@DELETE
	@RequireSecurity(SecurityPermission.USER_SUBMISSIONS_UPDATE)
	@APIDescription("Deletes a submission media")
	@Path("/{submissionId}/media/{mediaId}")
	public Response deleteUserSubmissionMedia(
			@PathParam("submissionId") String submissionId,
			@PathParam("mediaId") String mediaId
	)
	{
		UserSubmissionMedia userSubmissionMedia = new UserSubmissionMedia();
		userSubmissionMedia.setUserSubmissionId(submissionId);
		userSubmissionMedia.setSubmissionMediaId(mediaId);
		userSubmissionMedia = userSubmissionMedia.find();

		Response response = Response.noContent().build();
		if (userSubmissionMedia != null) {
			response = ownerCheck(userSubmissionMedia, SecurityPermission.ADMIN_USER_SUBMISSIONS);
			if (response == null) {
				service.getSubmissionFormService().deleteUserSubmissionMedia(mediaId);
				response = Response.noContent().build();
			}
		}
		return response;
	}

	//Add comment endpoints
}
