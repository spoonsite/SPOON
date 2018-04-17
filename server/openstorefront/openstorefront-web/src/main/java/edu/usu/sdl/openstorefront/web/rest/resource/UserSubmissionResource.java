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
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
@Path("v1/resource/usersubmissions")
@APIDescription("Provides access to User submissions")
public class UserSubmissionResource
		extends BaseResource
{

	//get all submissions (admin)
	@GET
	@APIDescription("Gets all user submissions for all users; Note: these are incomplete submissions")
	@RequireSecurity(SecurityPermission.USER_SUBMISSIONS)
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserSubmission.class)
	public List<UserSubmission> getUserSubmissions()
	{
		UserSubmission userSubmissionExample = new UserSubmission();
		return userSubmissionExample.findByExample();
	}

	//get submissions for user (owner)
	@GET
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

	//get submissions for user (owner/admin) REPLACE WITH CORRECT PERMISSION
	@GET
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

		Response response = ownerCheck(userSubmission, SecurityPermission.USER_SUBMISSIONS);
		if (response == null) {
			response = sendSingleEntityResponse(userSubmission);
		}
		return response;
	}

	//create submission (just submission permission)
	//update submission	(submission - owner/admin)
	//submit for approval (owner)
	//create change request (owner/admin)
	//submit change request (owner)
	//reassign owner (admin)
	@PUT
	@APIDescription("Reassign Owner on a submission")
	@RequireSecurity(SecurityPermission.USER_SUBMISSIONS)
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

	//delete submission (owner/admin)
	@DELETE
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
			response = ownerCheck(userSubmission, SecurityPermission.USER_SUBMISSIONS);
			if (response == null) {
				service.getSubmissionFormService().deleteUserSubmission(submissionId);
				response = Response.noContent().build();
			}
		}
		return response;
	}

}
