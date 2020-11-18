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
package edu.usu.sdl.openstorefront.web.action;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.ContentSection;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionMedia;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.GeneralMedia;
import edu.usu.sdl.openstorefront.core.entity.MediaFile;
import edu.usu.sdl.openstorefront.core.entity.Organization;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.SupportMedia;
import edu.usu.sdl.openstorefront.core.entity.TemporaryMedia;
import edu.usu.sdl.openstorefront.core.entity.UserSubmission;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionMedia;
import edu.usu.sdl.openstorefront.doc.security.LogicOperation;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.action.resolution.RangeResolutionBuilder;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

/**
 * Use to transmit media
 *
 * @author dshurtleff
 */
public class MediaAction
		extends BaseAction
{

	public static final String MISSING_IMAGE = "/image/close-red.png";
	public static final long MISSING_MEDIA_IMAGE_SIZE = 10898;

	private static final Logger LOG = Logger.getLogger(MediaAction.class.getName());

	@Validate(required = true, on = {"LoadMedia", "SectionMedia", "SupportMedia", "SubmissionMedia"})
	private String mediaId;

	@ValidateNestedProperties({
		@Validate(required = true, field = "mediaTypeCode", on = "UploadMedia")
		,
		@Validate(required = true, field = "componentId", on = "UploadMedia")
	})
	private ComponentMedia componentMedia;
	private MediaFile mediaFile;

	@Validate(required = true, on = "DataImage")
	private String imageData;

	@Validate(required = true, on = "DataImage")
	private String imageType;

	@Validate(required = true, on = {"UploadMedia", "UploadOrganizationLogo", "UploadSubmissionMedia"})
	private FileBean file;

	@Validate(required = true, on = "GeneralMedia")
	private String name;

	@ValidateNestedProperties({
		@Validate(required = true, field = "name", on = "UploadGeneralMedia")
	})
	private GeneralMedia generalMedia;

	@ValidateNestedProperties({
		@Validate(required = true, field = "title", on = "UploadSupportMedia")
		,
		@Validate(required = true, field = "orderNumber", on = "UploadSupportMedia")
	})
	private SupportMedia supportMedia;

	@ValidateNestedProperties({
		@Validate(required = true, field = "name", on = "UploadTemporaryMedia")
	})
	private TemporaryMedia temporaryMedia;

	@ValidateNestedProperties({
		@Validate(required = true, field = "mediaTypeCode", on = "UploadSectionMedia")
		,
		@Validate(required = true, field = "contentSectionId", on = "UploadSectionMedia")
	})
	private ContentSectionMedia contentSectionMedia;

	@Validate(required = true, on = {"OrganizationLogo", "UploadOrganizationLogo"})
	private String organizationId;

	@Validate(required = true, on = {"UploadSubmissionMedia"})
	private String userSubmissionId;

	@Validate(required = true, on = {"UploadSubmissionMedia"})
	private String submissionTemplateFieldId;

	private static final String ACCESS_DENIED = "Access denied";

	@DefaultHandler
	public Resolution audioTestPage()
	{
		//return new ForwardResolution("/WEB-INF/securepages/test/audioTest.jsp");
		return new ErrorResolution(HttpServletResponse.SC_FORBIDDEN, ACCESS_DENIED);
	}

	@ValidationMethod(on = {"UploadMedia", "UploadOrganizationLogo", "UploadGeneralMedia", "UploadSectionMedia", "UploadSubmissionMedia"})
	public void uploadHook(ValidationErrors errors)
	{
		checkUploadSizeValidation(errors, file, "file");
	}

	@HandlesEvent("LoadMedia")
	public Resolution sendMedia() throws FileNotFoundException
	{
		mediaFile = service.getPersistenceService().findById(MediaFile.class, mediaId);
		if (mediaFile == null) {
			componentMedia = service.getPersistenceService().findById(ComponentMedia.class, mediaId);
			componentMedia = filterEngine.filter(componentMedia, true);
			if (componentMedia == null || componentMedia.getFile() == null) {
				throw new OpenStorefrontRuntimeException("Media not Found", "Check media Id");
			}
			mediaFile = componentMedia.getFile();
		}

		InputStream in;
		long length;
		Path path = mediaFile.path();
		if (path != null && path.toFile().exists()) {
			in = new FileInputStream(path.toFile());
			length = path.toFile().length();
		} else {
			if (componentMedia != null) {
				Component component = service.getPersistenceService().findById(Component.class, componentMedia.getComponentId());
				LOG.log(Level.WARNING, MessageFormat.format("Media not on disk: {0} Check media record: {1} on component {2} ({3}) ", new Object[]{mediaFile.path(), mediaId, component.getName(), component.getComponentId()}));
			} else {
				LOG.log(Level.WARNING, MessageFormat.format("Media not on disk: {0} Check media file record: {1}", new Object[]{mediaFile.path(), mediaId}));
			}
			in = MediaAction.class.getResourceAsStream(MISSING_IMAGE);
			length = MISSING_MEDIA_IMAGE_SIZE;
		}
		return new RangeResolutionBuilder()
				.setContentType(mediaFile.getMimeType())
				.setInputStream(in)
				.setTotalLength(length)
				.setRequest(getContext().getRequest())
				.setFilename(mediaFile.getOriginalName())
				.createRangeResolution();
	}

	@HandlesEvent("UploadMedia")
	public Resolution uploadMedia()
	{
		Resolution resolution = null;
		Map<String, String> errors = new HashMap<>();

		if (componentMedia != null) {
			Component component = service.getPersistenceService().findById(Component.class, componentMedia.getComponentId());
			if (component != null) {
				boolean allow = false;
				if (SecurityUtil.hasPermission(SecurityPermission.ADMIN_ENTRY_MEDIA_MANAGEMENT)) {
					allow = true;
					LOG.log(Level.INFO, () -> SecurityUtil.adminAuditLogMessage(getContext().getRequest()));
				} else if (SecurityUtil.hasPermission(SecurityPermission.USER_EVALUATIONS_UPDATE)) {
					if (ApprovalStatus.APPROVED.equals(component.getApprovalState()) == false) {
						allow = true;
					}
				} else if (SecurityUtil.isCurrentUserTheOwner(component)) {
					if (ApprovalStatus.APPROVED.equals(component.getApprovalState()) == false) {
						allow = true;
					}
				}
				if (allow) {

					if (!doesFileExceedLimit(file)) {

						componentMedia.setActiveStatus(ComponentMedia.ACTIVE_STATUS);
						componentMedia.setUpdateUser(SecurityUtil.getCurrentUserName());
						componentMedia.setCreateUser(SecurityUtil.getCurrentUserName());

						ValidationModel validationModel = new ValidationModel(componentMedia);
						validationModel.setConsumeFieldsOnly(true);
						ValidationResult validationResult = ValidationUtil.validate(validationModel);
						if (validationResult.valid()) {
							try {
								service.getComponentService().saveMediaFile(componentMedia, file.getInputStream(), file.getContentType(), StringProcessor.getJustFileName(file.getFileName()));

								if (SecurityUtil.isEntryAdminUser() == false) {
									if (ApprovalStatus.PENDING.equals(component.getApprovalState())) {
										service.getComponentService().checkComponentCancelStatus(componentMedia.getComponentId(), ApprovalStatus.NOT_SUBMITTED);
									}
								}
							} catch (IOException ex) {
								throw new OpenStorefrontRuntimeException("Unable to able to save media.", "Contact System Admin. Check disk space and permissions.", ex);
							} finally {
								deleteUploadFile(file);
							}
						} else {
							errors.put("file", validationResult.toHtmlString());
						}
					}
				} else {
					resolution = new ErrorResolution(HttpServletResponse.SC_FORBIDDEN, ACCESS_DENIED);
				}
			} else {
				errors.put("componentMedia", "Missing component; check Component Id");
			}
		} else {
			errors.put("componentMedia", "Missing component media information");
		}
		if (resolution == null) {
			resolution = streamUploadResponse(errors);
		}
		return resolution;
	}

	@HandlesEvent("GeneralMedia")
	public Resolution generalMedia() throws FileNotFoundException
	{
		GeneralMedia generalMediaExample = new GeneralMedia();
		generalMediaExample.setName(name);
		generalMedia = service.getPersistenceService().queryOneByExample(generalMediaExample);
		if (generalMedia == null) {
			LOG.log(Level.FINE, MessageFormat.format("General Media with name: {0} is not found.", name));
			return new StreamingResolution("image/png")
			{

				@Override
				protected void stream(HttpServletResponse response) throws Exception
				{
					try (InputStream in = MediaAction.class.getResourceAsStream(MISSING_IMAGE)) {
						FileSystemManager.getInstance().copy(in, response.getOutputStream());
					}
				}

			}.setFilename("MediaNotFound.png");
		}

		InputStream in;
		long length;
		Path path = generalMedia.pathToMedia();
		if (path != null && path.toFile().exists()) {
			in = new FileInputStream(path.toFile());
			length = path.toFile().length();
		} else {
			LOG.log(Level.WARNING, MessageFormat.format("Media not on disk: {0} Check general media record: {1} ", new Object[]{generalMedia.pathToMedia(), generalMedia.getName()}));
			in = MediaAction.class.getResourceAsStream(MISSING_IMAGE);
			length = MISSING_MEDIA_IMAGE_SIZE;
		}

		return new RangeResolutionBuilder()
				.setContentType(generalMedia.getFile().getMimeType())
				.setInputStream(in)
				.setTotalLength(length)
				.setRequest(getContext().getRequest())
				.setFilename(generalMedia.getFile().getOriginalName())
				.createRangeResolution();
	}

	@RequireSecurity(SecurityPermission.ADMIN_MEDIA_UPLOAD)
	@HandlesEvent("UploadGeneralMedia")
	public Resolution uploadGeneralMedia()
	{
		Map<String, String> errors = new HashMap<>();

		LOG.log(Level.INFO, () -> SecurityUtil.adminAuditLogMessage(getContext().getRequest()));
		if (generalMedia != null) {
			generalMedia.setActiveStatus(ComponentMedia.ACTIVE_STATUS);
			generalMedia.setUpdateUser(SecurityUtil.getCurrentUserName());
			generalMedia.setCreateUser(SecurityUtil.getCurrentUserName());

			ValidationModel validationModel = new ValidationModel(generalMedia);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult validationResult = ValidationUtil.validate(validationModel);
			if (validationResult.valid()) {
				try {
					generalMedia = service.getSystemService().saveGeneralMedia(generalMedia, file.getInputStream(), file.getContentType(), StringProcessor.getJustFileName(file.getFileName()));
					return streamResults(generalMedia, MediaType.TEXT_HTML);
				} catch (IOException ex) {
					throw new OpenStorefrontRuntimeException("Unable to able to save media.", "Contact System Admin. Check disk space and permissions.", ex);
				} finally {
					deleteUploadFile(file);
				}
			} else {
				errors.put("file", validationResult.toHtmlString());
			}
		} else {
			errors.put("generalMedia", "Missing general media information");
		}
		return streamUploadResponse(errors);
	}

	@HandlesEvent("SupportMedia")
	public Resolution supportMedia() throws FileNotFoundException
	{
		SupportMedia supportMediaExample = new SupportMedia();
		supportMediaExample.setSupportMediaId(mediaId);

		supportMedia = supportMediaExample.find();
		if (supportMedia == null) {
			LOG.log(Level.FINE, MessageFormat.format("Support Media with name: {0} is not found.", name));
			return new StreamingResolution("image/png")
			{

				@Override
				protected void stream(HttpServletResponse response) throws Exception
				{
					try (InputStream in = MediaAction.class.getResourceAsStream(MISSING_IMAGE)) {
						FileSystemManager.getInstance().copy(in, response.getOutputStream());
					}
				}

			}.setFilename("MediaNotFound.png");
		}

		InputStream in;
		long length;
		Path path = supportMedia.pathToMedia();
		if (path != null && path.toFile().exists()) {
			in = new FileInputStream(path.toFile());
			length = path.toFile().length();
		} else {
			LOG.log(Level.WARNING, MessageFormat.format("Media not on disk: {0} Check support media record: {1} ", new Object[]{supportMedia.pathToMedia(), supportMedia.getSupportMediaId()}));
			in = MediaAction.class.getResourceAsStream(MISSING_IMAGE);
			length = MISSING_MEDIA_IMAGE_SIZE;
		}

		return new RangeResolutionBuilder()
				.setContentType(supportMedia.getFile().getMimeType())
				.setInputStream(in)
				.setTotalLength(length)
				.setRequest(getContext().getRequest())
				.setFilename(supportMedia.getFile().getOriginalName())
				.createRangeResolution();
	}

	@RequireSecurity(SecurityPermission.ADMIN_SUPPORT_MEDIA_CREATE)
	@HandlesEvent("UploadSupportMedia")
	public Resolution uploadSupportMedia()
	{
		Map<String, String> errors = new HashMap<>();

		LOG.log(Level.INFO, () -> SecurityUtil.adminAuditLogMessage(getContext().getRequest()));
		if (supportMedia != null) {

			ValidationModel validationModel = new ValidationModel(supportMedia);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult validationResult = ValidationUtil.validate(validationModel);
			if (validationResult.valid()) {
				try {
					MediaFile supportMediaFile = new MediaFile();
					supportMediaFile.setMimeType(file.getContentType());
					supportMediaFile.setOriginalName(StringProcessor.getJustFileName(file.getFileName()));

					supportMedia.setFile(supportMediaFile);
					supportMedia = service.getHelpSupportService().saveSupportMedia(supportMedia, file.getInputStream());
					return streamResults(supportMedia, MediaType.TEXT_HTML);
				} catch (IOException ex) {
					throw new OpenStorefrontRuntimeException("Unable to able to save media.", "Contact System Admin. Check disk space and permissions.", ex);
				} finally {
					deleteUploadFile(file);
				}
			} else {
				errors.put("file", validationResult.toHtmlString());
			}
		} else {
			errors.put("supportMedia", "Missing support media information");
		}
		return streamUploadResponse(errors);
	}

	@HandlesEvent("TemporaryMedia")
	public Resolution temporaryMedia() throws FileNotFoundException
	{
		TemporaryMedia temporaryMediaExample = new TemporaryMedia();
		temporaryMediaExample.setName(name);
		TemporaryMedia temporaryMediaFound = service.getPersistenceService().queryOneByExample(temporaryMediaExample);
		if (temporaryMediaFound == null) {
			LOG.log(Level.FINE, MessageFormat.format("Temporary Media with name: {0} is not found.", name));
			return new StreamingResolution("image/png")
			{

				@Override
				protected void stream(HttpServletResponse response) throws Exception
				{
					try (InputStream in = MediaAction.class.getResourceAsStream(MISSING_IMAGE)) {
						FileSystemManager.getInstance().copy(in, response.getOutputStream());
					}
				}

			}.setFilename("MediaNotFound.png");
		}

		InputStream in;
		long length;
		Path path = temporaryMediaFound.pathToMedia();
		if (path != null && path.toFile().exists()) {
			in = new FileInputStream(path.toFile());
			length = path.toFile().length();
		} else {
			LOG.log(Level.WARNING, MessageFormat.format("Media not on disk: {0} Check temporary media record: {1} ", new Object[]{temporaryMediaFound.pathToMedia(), temporaryMediaFound.getName()}));
			in = MediaAction.class.getResourceAsStream(MISSING_IMAGE);
			length = MISSING_MEDIA_IMAGE_SIZE;
		}

		return new RangeResolutionBuilder()
				.setContentType(temporaryMediaFound.getMimeType())
				.setInputStream(in)
				.setTotalLength(length)
				.setRequest(getContext().getRequest())
				.setFilename(temporaryMediaFound.getOriginalFileName())
				.createRangeResolution();
	}

	@RequireSecurity(value = {
		SecurityPermission.ADMIN_ENTRY_MEDIA_MANAGEMENT,
		SecurityPermission.ADMIN_ENTRY_EVALSECTION_MANAGEMENT,
		SecurityPermission.USER_EVALUATIONS_UPDATE
	}, logicOperator = LogicOperation.OR)
	@HandlesEvent("UploadSectionMedia")
	public Resolution uploadSectionMedia()
	{
		Map<String, String> errors = new HashMap<>();

		ValidationResult validationResult = contentSectionMedia.validate();
		if (validationResult.valid()) {
			try {
				contentSectionMedia = service.getContentSectionService().saveMedia(contentSectionMedia, file.getInputStream(), file.getContentType(), StringProcessor.getJustFileName(file.getFileName()));
				List<ContentSectionMedia> data = new ArrayList<>();
				data.add(contentSectionMedia);
				return streamResults(data, MediaType.TEXT_HTML);
			} catch (IOException ex) {
				throw new OpenStorefrontRuntimeException("Unable to able to save media.", "Contact System Admin. Check disk space and permissions.", ex);
			} finally {
				deleteUploadFile(file);
			}
		} else {
			errors.put("file", validationResult.toHtmlString());
		}
		return streamUploadResponse(errors);
	}

	@HandlesEvent("SectionMedia")
	@SuppressWarnings("squid:S1872")
	public Resolution sectionMedia() throws FileNotFoundException
	{
		ContentSectionMedia sectionMedia = new ContentSectionMedia();
		mediaFile = service.getPersistenceService().findById(MediaFile.class, mediaId);
		if (mediaFile == null) {
			sectionMedia.setContentSectionMediaId(mediaId);
			sectionMedia = sectionMedia.find();

			//Check component / or evaluate for access?
			if (sectionMedia != null) {
				ContentSection section = new ContentSection();
				section.setContentSectionId(sectionMedia.getContentSectionId());
				section = section.find();
				if (section != null) {
					if (Component.class.getSimpleName().equals(section.getEntity())) {
						Component component = new Component();
						component.setComponentId(section.getEntityId());
						component = component.find();
						component = filterEngine.filter(component);
						if (component == null) {
							sectionMedia = null;
						}
					} else if (Evaluation.class.getSimpleName().equals(section.getEntity())) {
						Evaluation evaluation = new Evaluation();
						evaluation.setEvaluationId(section.getEntityId());
						evaluation = evaluation.find();
						evaluation = filterEngine.filter(evaluation);
						if (evaluation == null) {
							sectionMedia = null;
						}
					}
				}
			}
			mediaFile = (sectionMedia != null ? sectionMedia.getFile() : null);
		}

		if (mediaFile == null) {
			LOG.log(Level.FINE, MessageFormat.format("Section Media with media id: {0} is not found.", mediaId));
			return new StreamingResolution("image/png")
			{
				@Override
				protected void stream(HttpServletResponse response) throws Exception
				{
					try (InputStream in = MediaAction.class.getResourceAsStream(MISSING_IMAGE)) {
						FileSystemManager.getInstance().copy(in, response.getOutputStream());
					}
				}
			}.setFilename("MediaNotFound.png");
		}

		InputStream in;
		long length;
		Path path = mediaFile.path();
		if (path != null && path.toFile().exists()) {
			in = new FileInputStream(path.toFile());
			length = path.toFile().length();
		} else {
			LOG.log(Level.WARNING, MessageFormat.format("Media not on disk: {0} Check section media file record: {1} ", new Object[]{mediaFile.path(), mediaFile.getMediaFileId()}));
			in = MediaAction.class.getResourceAsStream(MISSING_IMAGE);
			length = MISSING_MEDIA_IMAGE_SIZE;
		}

		return new RangeResolutionBuilder()
				.setContentType(mediaFile.getMimeType())
				.setInputStream(in)
				.setTotalLength(length)
				.setRequest(getContext().getRequest())
				.setFilename(mediaFile.getOriginalName())
				.createRangeResolution();
	}

	// @RequireSecurity(SecurityPermission.ADMIN_ORGANIZATION_UPDATE)
	@RequireSecurity(value = {SecurityPermission.ADMIN_ORGANIZATION_UPDATE,
		SecurityPermission.ADMIN_ORGANIZATION_CREATE},
			logicOperator = LogicOperation.OR)
	@HandlesEvent("UploadOrganizationLogo")
	public Resolution uploadOrganizationLogo()
	{
		Map<String, String> errors = new HashMap<>();

		Organization organizationExample = new Organization();
		organizationExample.setOrganizationId(organizationId);
		Organization organization = organizationExample.find();
		if (organization != null) {

			organization.setLogoOriginalFileName(StringProcessor.getJustFileName(file.getFileName()));
			organization.setLogoMimeType(file.getContentType());

			ValidationResult validationResult = organization.validate();
			if (validationResult.valid()) {
				try {
					service.getOrganizationService().saveOrganizationLogo(organization, file.getInputStream());
				} catch (IOException ex) {
					throw new OpenStorefrontRuntimeException("Unable to able to save media.", "Contact System Admin. Check disk space and permissions.", ex);
				} finally {
					deleteUploadFile(file);
				}
			} else {
				errors.put("file", validationResult.toHtmlString());
			}
		} else {
			errors.put("organization.name", "Unable to find organization. Check name and try again.");
		}
		return streamUploadResponse(errors);
	}

	@HandlesEvent("OrganizationLogo")
	public Resolution organizationLogo() throws FileNotFoundException
	{
		Organization organizationExample = new Organization();
		organizationExample.setOrganizationId(organizationId);
		Organization organization = organizationExample.find();

		if (organization != null) {
			InputStream in;
			long length;
			Path path = organization.pathToLogo();
			if (path != null && path.toFile().exists()) {
				in = new FileInputStream(path.toFile());
				length = path.toFile().length();
			} else {
				LOG.log(Level.WARNING, MessageFormat.format("Organization logo not on disk: {0} Check organization media record: {1} ", new Object[]{organization.pathToLogo(), organization.getOrganizationId()}));
				in = MediaAction.class.getResourceAsStream(MISSING_IMAGE);
				length = MISSING_MEDIA_IMAGE_SIZE;
			}

			return new RangeResolutionBuilder()
					.setContentType(organization.getLogoMimeType())
					.setInputStream(in)
					.setTotalLength(length)
					.setRequest(getContext().getRequest())
					.setFilename(organization.getLogoOriginalFileName())
					.createRangeResolution();
		} else {
			LOG.log(Level.FINE, MessageFormat.format("Organization with id: {0} is not found.", organizationId));
			return new StreamingResolution("image/png")
			{
				@Override
				protected void stream(HttpServletResponse response) throws Exception
				{
					try (InputStream in = MediaAction.class.getResourceAsStream(MISSING_IMAGE)) {
						FileSystemManager.getInstance().copy(in, response.getOutputStream());
					}
				}
			}.setFilename("MediaNotFound.png");
		}

	}

	/**
	 * This function is the endpoint for a "Download Image" button for Relationship charts.
	 * Due to a bug (server errored when the image size/resolution was too great), this button
	 * was removed rather than fixed. Button exists in Ext.js at git commit with SHA hash 
	 * 2f795533ee830bffa2c7d1f6eee3bbb45f2c4e1d before deletion. 
	 */
	@Deprecated
	@HandlesEvent("DataImage")
	public Resolution tranformDataImage()
	{
		String data[] = imageData.split(",");

		String mimeType = data[0].substring(data[0].indexOf(":") + 1, data[0].indexOf(";"));

		ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(data[1]));
		return new StreamingResolution(mimeType, in)
		{
		}.setFilename("visual." + imageType);
	}

	@HandlesEvent("UploadSubmissionMedia")
	public Resolution uploadSubmissionMedia()
	{
		Resolution resolution = null;

		Map<String, String> errors = new HashMap<>();

		UserSubmission userSubmission = new UserSubmission();
		userSubmission.setUserSubmissionId(userSubmissionId);
		userSubmission = userSubmission.find();
		if (userSubmission != null) {

			boolean allow = false;
			if (SecurityUtil.hasPermission(SecurityPermission.ADMIN_USER_SUBMISSIONS_UPDATE)) {
				allow = true;
				LOG.log(Level.INFO, () -> SecurityUtil.adminAuditLogMessage(getContext().getRequest()));
			} else if (SecurityUtil.isCurrentUserTheOwner(userSubmission)) {
				allow = true;

			}

			if (allow) {
				mediaFile = new MediaFile();
				mediaFile.setOriginalName(StringProcessor.getJustFileName(file.getFileName()));
				mediaFile.setMimeType(file.getContentType());

				try {
					UserSubmissionMedia userSubmissionMedia = service.getSubmissionFormService().saveSubmissionFormMedia(userSubmissionId, submissionTemplateFieldId, mediaFile, file.getInputStream());

					//it appears to be serializing a proxy...so we can make a copy of what is wanted
					UserSubmissionMedia cleanedMedia = new UserSubmissionMedia();
					cleanedMedia.setSubmissionMediaId(userSubmissionMedia.getSubmissionMediaId());
					cleanedMedia.setTemplateFieldId(userSubmissionMedia.getTemplateFieldId());
					cleanedMedia.setUserSubmissionId(userSubmissionMedia.getUserSubmissionId());
					cleanedMedia.setFile(new MediaFile());

					cleanedMedia.getFile().setMediaFileId(userSubmissionMedia.getFile().getMediaFileId());
					cleanedMedia.getFile().setOriginalName(userSubmissionMedia.getFile().getOriginalName());
					cleanedMedia.getFile().setMimeType(userSubmissionMedia.getFile().getMimeType());
					cleanedMedia.getFile().setFileType(userSubmissionMedia.getFile().getFileType());
					cleanedMedia.getFile().setFileName(userSubmissionMedia.getFile().getFileName());

					return streamResults(cleanedMedia, MediaType.TEXT_HTML);
				} catch (IOException ex) {
					throw new OpenStorefrontRuntimeException("Unable to able to save media.", "Contact System Admin. Check disk space and permissions.", ex);
				} finally {
					deleteUploadFile(file);
				}

			} else {
				resolution = new ErrorResolution(HttpServletResponse.SC_FORBIDDEN, ACCESS_DENIED);
			}
		} else {
			errors.put("userSubmissionId", "Unable to find user submission.");
		}

		if (resolution == null) {
			resolution = streamUploadResponse(errors);
		}

		return resolution;
	}

	public String getMediaId()
	{
		return mediaId;
	}

	public void setMediaId(String mediaId)
	{
		this.mediaId = mediaId;
	}

	public ComponentMedia getComponentMedia()
	{
		return componentMedia;
	}

	public void setComponentMedia(ComponentMedia componentMedia)
	{
		this.componentMedia = componentMedia;
	}

	public FileBean getFile()
	{
		return file;
	}

	public void setFile(FileBean file)
	{
		this.file = file;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public GeneralMedia getGeneralMedia()
	{
		return generalMedia;
	}

	public void setGeneralMedia(GeneralMedia generalMedia)
	{
		this.generalMedia = generalMedia;
	}

	public String getImageData()
	{
		return imageData;
	}

	public void setImageData(String imageData)
	{
		this.imageData = imageData;
	}

	public String getImageType()
	{
		return imageType;
	}

	public void setImageType(String imageType)
	{
		this.imageType = imageType;
	}

	public TemporaryMedia getTemporaryMedia()
	{
		return temporaryMedia;
	}

	public void setTemporaryMedia(TemporaryMedia temporaryMedia)
	{
		this.temporaryMedia = temporaryMedia;
	}

	public ContentSectionMedia getContentSectionMedia()
	{
		return contentSectionMedia;
	}

	public void setContentSectionMedia(ContentSectionMedia contentSectionMedia)
	{
		this.contentSectionMedia = contentSectionMedia;
	}

	public String getOrganizationId()
	{
		return organizationId;
	}

	public void setOrganizationId(String organizationId)
	{
		this.organizationId = organizationId;
	}

	public MediaFile getMediaFile()
	{
		return mediaFile;
	}

	public void setMediaFile(MediaFile mediaFile)
	{
		this.mediaFile = mediaFile;
	}

	public SupportMedia getSupportMedia()
	{
		return supportMedia;
	}

	public void setSupportMedia(SupportMedia supportMedia)
	{
		this.supportMedia = supportMedia;
	}

	public String getUserSubmissionId()
	{
		return userSubmissionId;
	}

	public void setUserSubmissionId(String userSubmissionId)
	{
		this.userSubmissionId = userSubmissionId;
	}

	public String getSubmissionTemplateFieldId()
	{
		return submissionTemplateFieldId;
	}

	public void setSubmissionTemplateFieldId(String submissionTemplateFieldId)
	{
		this.submissionTemplateFieldId = submissionTemplateFieldId;
	}

}
