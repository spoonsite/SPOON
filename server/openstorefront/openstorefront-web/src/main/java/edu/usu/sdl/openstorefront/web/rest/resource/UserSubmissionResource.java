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

import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.ComponentCommentType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.UserSubmission;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionComment;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionMedia;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;
import edu.usu.sdl.openstorefront.core.view.RestErrorModel;
import edu.usu.sdl.openstorefront.core.view.UserSubmissionMediaView;
import edu.usu.sdl.openstorefront.core.view.UserSubmissionView;
import edu.usu.sdl.openstorefront.core.view.WorkPlanLinkView;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.manager.MailManager;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codemonkey.simplejavamail.email.Email;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.apache.commons.io.FilenameUtils;
import javax.mail.Message;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/usersubmissions")
@APIDescription("Provides access to User submissions")
public class UserSubmissionResource
		extends BaseResource
{

	Logger LOG = Logger.getLogger(UserSubmissionResource.class.getName());

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

		Response response = ownerCheck(userSubmission, SecurityPermission.ADMIN_USER_SUBMISSIONS_READ);
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

		Response response = ownerCheck(userSubmission, SecurityPermission.ADMIN_USER_SUBMISSIONS_READ);
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

	@POST
	@APIDescription("Accepts a zip file for use with the bulk upload process")
	@RequireSecurity(SecurityPermission.USER_SUBMISSIONS_CREATE)
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/upload/zip")
	public Response bulkUpload(
			@FormDataParam("file") InputStream uploadStream,
			@FormDataParam("file") FormDataContentDisposition fileDisposition
	)
	{
		RestErrorModel result = handleFileUpload(uploadStream, fileDisposition.getFileName());
		return Response.ok(result).build();
	}

	private RestErrorModel handleFileUpload(InputStream inStream,
			String fileName)
	{
		RestErrorModel restErrorModel = new RestErrorModel();
		String username = SecurityUtil.getCurrentUserName();

		String timeStamp = new SimpleDateFormat("dd-MM-YYYY").format(new Date());
		String filePath = FileSystemManager
				.getInstance()
				.getDir(FileSystemManager.BULK_UPLOAD_DIR)
				.toString()
				+ File.separator
				+ username
				+ File.separator
				+ timeStamp
				+ "_"
				+ StringProcessor.uniqueId()
				+ ".zip";

		String extension = FilenameUtils.getExtension(fileName);

		if (extension.equals("zip")) {
			File destFile = new File(filePath);
			try {
				FileUtils.copyInputStreamToFile(inStream, destFile);
			} catch (IOException ex) {
				LOG.log(Level.FINE, "Unable to read file: " + fileName, ex);
				restErrorModel.getErrors().put("message", "Unable to read file.");
				restErrorModel.getErrors().put("potentialResolution", "Make sure the file is in the proper format.");
				restErrorModel.setSuccess(false);
				return restErrorModel;
			}
		} else {
			restErrorModel.getErrors().put("message", "Uploaded file was not a zip file.");
			restErrorModel.getErrors().put("potentialResolution", "Ensure filename ends with .zip");
			restErrorModel.setSuccess(false);
			return restErrorModel;
		}

		// Send email to spoon support telling them that there is a new uploaded file.
		String recipientAddress = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_FEEDBACK_EMAIL);
		String senderAddress = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MAIL_FROM_ADDRESS);
		String senderName = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MAIL_FROM_NAME);
		if (recipientAddress == null) {
			recipientAddress = "";
		}
		if (senderAddress == null) {
			senderAddress = "";
		}
		if (senderName == null) {
			senderName = "";
		}

		if (!recipientAddress.isEmpty() && !senderAddress.isEmpty() && !senderName.isEmpty()) {
			Email email = MailManager.newEmail();
			email.setSubject("SpoonSite bulk Upload");
			email.setText("There is a new bulk upload to be reviewed at " + filePath);
			email.addRecipient("Admin", recipientAddress, Message.RecipientType.TO);
			email.setFromAddress(senderName, senderAddress);
			MailManager.send(email, true);
		} else {
			LOG.log(Level.WARNING, "Email doesn't have an email correctly defined.");
			restErrorModel.getErrors().put("message", "Could not send notification email.");
			restErrorModel.getErrors().put("potentialResolution", "Set feedback email, mail from addresss, and mail from name");
			restErrorModel.setSuccess(false);
			return restErrorModel;
		}

		restErrorModel.getErrors().put("message", "File uploaded sucessfully.");
		restErrorModel.setSuccess(true);
		return restErrorModel;
	}

	private RestErrorModel writeStreamToFile(InputStream stream, String fileLocation) throws IOException
	{
		RestErrorModel restErrorModel = new RestErrorModel();
		String maxSizeString = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MAX_POST_SIZE);
		int maxSize;
		if (maxSizeString != null) {
			try {
				maxSize = Integer.parseInt(maxSizeString);
			} catch (NumberFormatException ex) {
				LOG.log(Level.SEVERE, "Could not parse key KEY_MAX_POST_SIZE. Ensure that max.post.size is set to an integer.\n{0}", ex.toString());
				restErrorModel.getErrors().put("message", "Unable to determine max upload size.");
				restErrorModel.getErrors().put("potentialResolution", "Ensure that max.post.size is set to an integer.");
				restErrorModel.setSuccess(false);
				return restErrorModel;
			}
		} else {
			LOG.log(Level.SEVERE, "Could not find key KEY_MAX_POST_SIZE. Ensure that max.post.size is set to an integer.");
			restErrorModel.getErrors().put("message", "Unable to determine max upload size.");
			restErrorModel.getErrors().put("potentialResolution", "Ensure that max.post.size is set to an integer.");
			restErrorModel.setSuccess(false);
			return restErrorModel;
		}
		final int mBtoBytesConversionFactor = 1000000;
		maxSize *= mBtoBytesConversionFactor;

		int bytesReadIntoBuffer;
		int totalReads = 0;
		// Note buffer size is arbirtary
		byte[] buffer = new byte[1024];
		File uploadedFile = new File(fileLocation);

		try (OutputStream out = new FileOutputStream(uploadedFile)) {
			while ((bytesReadIntoBuffer = stream.read(buffer)) != -1) {
				out.write(buffer, 0, bytesReadIntoBuffer);
				totalReads += bytesReadIntoBuffer;
				if (totalReads >= maxSize) {
					break;
				}
			}
			out.flush();
		}

		if (totalReads >= maxSize) {
			if (!uploadedFile.delete()) {
				LOG.log(Level.SEVERE, "Could not delete file {0}", fileLocation);
			}
			restErrorModel.getErrors().put("message", "File was too large to upload.");
			restErrorModel.setSuccess(false);
		} else {
			restErrorModel.setSuccess(true);
		}

		return restErrorModel;
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
			response = ownerCheck(existing, SecurityPermission.ADMIN_USER_SUBMISSIONS_UPDATE);
			if (response == null) {
				userSubmission.setUserSubmissionId(submissionId);
				String newSubmissionName = userSubmission.getFields().get(1).getRawValue();
				if (newSubmissionName != null) {
					userSubmission.setSubmissionName(newSubmissionName);
				}
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
			response = ownerCheck(existing, SecurityPermission.ADMIN_USER_SUBMISSIONS_UPDATE);
			if (response == null) {
				UserSubmission savedSubmission = service.getSubmissionFormService().queueEntry(existing);
				ValidationResult validateResult = service.getSubmissionFormService().submitUserSubmissionForApproval(savedSubmission);
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
			response = ownerCheck(existing, SecurityPermission.ADMIN_USER_SUBMISSIONS_UPDATE);
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
			response = ownerCheck(userSubmission, SecurityPermission.ADMIN_USER_SUBMISSIONS_DELETE);
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
			response = ownerCheck(userSubmissionMedia, SecurityPermission.ADMIN_USER_SUBMISSIONS_DELETE);
			if (response == null) {
				service.getSubmissionFormService().deleteUserSubmissionMedia(mediaId);
				response = Response.noContent().build();
			}
		}
		return response;
	}

	//Add comment endpoints
	@GET
	@APIDescription("Gets the list of comments associated to an submission")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(UserSubmissionComment.class)
	@Path("/{id}/comments")
	public Response getComponentComment(
			@PathParam("id")
			@RequiredParam String userSubmissionId,
			@DefaultValue("false")
			@QueryParam("submissionOnly") boolean submissionOnly)
	{
		String ANONYMOUS = "Anonymous";
		String ADMIN = "Admin";
		UserSubmission userSubmission = new UserSubmission();
		userSubmission.setUserSubmissionId(userSubmissionId);
		userSubmission = userSubmission.find();
		if (userSubmission == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		String owner = userSubmission.entityOwner();

		UserSubmissionComment userSubmissionCommentExample = new UserSubmissionComment();
		userSubmissionCommentExample.setActiveStatus(UserSubmissionComment.ACTIVE_STATUS);
		userSubmissionCommentExample.setUserSubmissionId(userSubmissionId);
		List<UserSubmissionComment> comments = userSubmissionCommentExample.findByExample();

		if (SecurityUtil.hasPermission(SecurityPermission.ADMIN_ENTRY_COMMENT_MANAGEMENT)) {
			/*        SUPER-ADMIN            */
			if (submissionOnly) {
				comments = comments.stream().filter(comment -> ComponentCommentType.SUBMISSION.equals(comment.getCommentType())).collect(Collectors.toList());
				return Response.ok(commentsToGenericEntity(comments)).build();
			} else {
				return Response.ok(commentsToGenericEntity(comments)).build();
			}
		} else if (SecurityUtil.hasPermission(SecurityPermission.WORKFLOW_ADMIN_SUBMISSION_COMMENTS)) {
			/*        LIBRARIAN             */
			List<UserSubmissionComment> submissionComments = comments.stream().filter(comment -> ComponentCommentType.SUBMISSION.equals(comment.getCommentType())).collect(Collectors.toList());
			submissionComments.forEach((comment) -> {
				if (!SecurityUtil.isCurrentUserTheOwner(comment)) {
					comment.setCreateUser(ANONYMOUS);
					comment.setUpdateUser(ANONYMOUS);
				}
			});
			return Response.ok(commentsToGenericEntity(submissionComments)).build();
		} else if (SecurityUtil.isCurrentUserTheOwner(userSubmission)) {
			/*        ENTRY OWNER            */
			List<UserSubmissionComment> submissionComments;
			submissionComments = comments.stream().filter(comment
					-> ComponentCommentType.SUBMISSION.equals(comment.getCommentType())
					&& !Convert.toBoolean(comment.getPrivateComment())
			).collect(Collectors.toList());
			submissionComments.forEach((comment) -> {
				if (!comment.getCreateUser().equals(SecurityUtil.getCurrentUserName())
						&& !comment.getCreateUser().equals(owner)) {
					comment.setCreateUser(ANONYMOUS);
				}
				if (!comment.getUpdateUser().equals(SecurityUtil.getCurrentUserName())
						&& !comment.getUpdateUser().equals(owner)) {
					comment.setUpdateUser(ANONYMOUS);
				}
				if (Convert.toBoolean(comment.getAdminComment())) {
					comment.setCreateUser(ADMIN);
					comment.setUpdateUser(ADMIN);
				}
			});
			return Response.ok(commentsToGenericEntity(submissionComments)).build();
		} else {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	}

	@DELETE
	@APIDescription("Delete a comment by id from the specified entity")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}/comments/{commentId}")
	public Response deleteComponentCommentById(
			@PathParam("id")
			@RequiredParam String userSubmissionId,
			@PathParam("commentId")
			@RequiredParam String commentId)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();
		UserSubmissionComment example = new UserSubmissionComment();
		example.setCommentId(commentId);
		example.setUserSubmissionId(userSubmissionId);
		UserSubmissionComment userSubmissionComment = example.find();
		if (userSubmissionComment != null) {
			response = ownerCheck(userSubmissionComment, SecurityPermission.ADMIN_ENTRY_COMMENT_MANAGEMENT);
			if (response == null) {
				userSubmissionComment.delete();
			}
		}
		return response;
	}

	@PUT
	@APIDescription("Update a comment associated to the component")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}/comments/{commentId}")
	public Response updateComponentComment(
			@PathParam("id")
			@RequiredParam String userSubmissionId,
			@PathParam("commentId")
			@RequiredParam String commentId,
			UserSubmissionComment comment)
	{
		UserSubmissionComment commentExisting = new UserSubmissionComment();
		commentExisting.setCommentId(commentId);
		commentExisting.setUserSubmissionId(userSubmissionId);
		commentExisting = commentExisting.find();
		if (commentExisting == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		if (SecurityUtil.isCurrentUserTheOwner(commentExisting)) {
			comment.setUserSubmissionId(userSubmissionId);
			comment.setCommentId(commentId);
			return saveComment(comment, false);
		} else {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	}

	private GenericEntity<List<UserSubmissionComment>> commentsToGenericEntity(List<UserSubmissionComment> comments)
	{
		return new GenericEntity<List<UserSubmissionComment>>(comments)
		{
		};
	}

	private Response saveComment(UserSubmissionComment comment, Boolean isCreated)
	{
		ValidationModel validationModel = new ValidationModel(comment);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getSubmissionFormService().saveUserSubmissionComment(comment);
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		return isCreated ? Response.created(URI.create("v1/resource/usersubmissions/" + comment.getUserSubmissionId() + "/comments/" + comment.getCommentId())).entity(comment).build() : Response.ok(comment).build();
	}

	@POST
	@APIDescription("Add a single comment to the specified component")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(UserSubmissionComment.class)
	@Path("/{id}/comments")
	public Response createComponentComment(
			@PathParam("id")
			@RequiredParam String userSubmissionId,
			@RequiredParam UserSubmissionComment comment)
	{
		UserSubmission userSubmission = new UserSubmission();
		userSubmission.setUserSubmissionId(userSubmissionId);
		userSubmission = userSubmission.find();
		if (userSubmission == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		if (SecurityUtil.isCurrentUserTheOwner(userSubmission)
				|| SecurityUtil.hasPermission(SecurityPermission.ADMIN_ENTRY_COMMENT_MANAGEMENT)
				|| SecurityUtil.hasPermission(SecurityPermission.WORKFLOW_ADMIN_SUBMISSION_COMMENTS)) {
			comment.setUserSubmissionId(userSubmissionId);
			if (SecurityUtil.hasPermission(SecurityPermission.ADMIN_ENTRY_COMMENT_MANAGEMENT)
					|| SecurityUtil.hasPermission(SecurityPermission.WORKFLOW_ADMIN_SUBMISSION_COMMENTS)) {
				comment.setAdminComment(true);
			}
			return saveComment(comment, true);
		} else {
			return Response.status(Response.Status.FORBIDDEN).build();
		}
	}

	@GET
	@APIDescription("Get the worklink for a user submission")
	@RequireSecurity(SecurityPermission.USER_WORKPLAN_READ)
	@Produces(MediaType.APPLICATION_JSON)
	@DataType(WorkPlanLinkView.class)
	@Path("/{id}/worklink")
	public Response getUserSubmissionWorkLink(
			@PathParam("id")
			@RequiredParam String userSubmissionId)
	{
		WorkPlanLink workLink = service.getWorkPlanService().getWorkPlanLinkForSubmission(userSubmissionId);
		GenericEntity<WorkPlanLinkView> entity = new GenericEntity<WorkPlanLinkView>(WorkPlanLinkView.toView(workLink))
		{
		};
		return sendSingleEntityResponse(entity);
	}

}
