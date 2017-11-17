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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.api.ContentSectionService;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ContentSection;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionMedia;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionTemplate;
import edu.usu.sdl.openstorefront.core.entity.ContentSubSection;
import edu.usu.sdl.openstorefront.core.entity.EvaluationSectionTemplate;
import edu.usu.sdl.openstorefront.core.entity.EvaluationTemplate;
import edu.usu.sdl.openstorefront.core.entity.MediaFile;
import edu.usu.sdl.openstorefront.core.entity.TemporaryMedia;
import edu.usu.sdl.openstorefront.core.entity.WorkflowStatus;
import edu.usu.sdl.openstorefront.core.model.ContentSectionAll;
import edu.usu.sdl.openstorefront.core.util.MediaFileType;
import edu.usu.sdl.openstorefront.core.view.ContentSectionTemplateView;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author dshurtleff
 */
public class ContentSectionServiceImpl
		extends ServiceProxy
		implements ContentSectionService
{

	private static final Logger LOG = Logger.getLogger(FeedbackServiceImpl.class.getName());

	@Override
	public String saveAll(ContentSectionAll contentSectionAll)
	{
		Objects.requireNonNull(contentSectionAll);
		Objects.requireNonNull(contentSectionAll.getSection(), "Content Section is required");

		contentSectionAll.getSection().setContent(parseTemporaryMedia(contentSectionAll.getSection().getContentSectionId(), contentSectionAll.getSection().getContent()));
		ContentSection contentSection = contentSectionAll.getSection().save();

		ContentSubSection contentSubSectionExample = new ContentSubSection();
		contentSubSectionExample.setContentSectionId(contentSection.getContentSectionId());
		List<ContentSubSection> subSections = contentSubSectionExample.findByExampleProxy();
		Map<String, List<ContentSubSection>> subSectionMap = subSections.stream()
				.collect(Collectors.groupingBy(ContentSubSection::getSubSectionId));

		for (ContentSubSection subSection : contentSectionAll.getSubsections()) {
			if (subSection.getSubSectionId() != null && subSectionMap.containsKey(subSection.getSubSectionId())) {
				ContentSubSection existing = subSectionMap.get(subSection.getSubSectionId()).get(0);
				subSection.setContent(parseTemporaryMedia(contentSection.getContentSectionId(), subSection.getContent()));
				existing.updateFields(subSection);
				persistenceService.persist(existing);
			} else {
				subSection.setContentSectionId(contentSection.getContentSectionId());
				subSection.setContent(parseTemporaryMedia(contentSection.getContentSectionId(), subSection.getContent()));
				subSection.populateBaseCreateFields();
				persistenceService.persist(subSection);
			}
		}

		return contentSection.getContentSectionId();
	}

	/**
	 * Convert inline-referenced temporary media to section-specific media
	 *
	 * @param sectionId Id of the content section
	 * @param content html content to check
	 * @return updated html content that reflects the changed urls
	 */
	private String parseTemporaryMedia(String sectionId, String content)
	{
		Document descriptionDoc = Jsoup.parse(content);

		Elements media = descriptionDoc.select("[src]");

		// Map <temporaryId, componentMediaId>
		Map<String, String> processedConversions = new HashMap<>();
		for (org.jsoup.nodes.Element mediaItem : media) {
			String url = mediaItem.attr("src");
			if (url.contains("Media.action?TemporaryMedia")) {
				// This src url contains temporary media -- we should convert it.
				String tempMediaId = url.substring(url.indexOf("&name=") + "&name=".length());
				tempMediaId = StringProcessor.urlDecode(tempMediaId);

				TemporaryMedia existingTemporaryMedia = persistenceService.findById(TemporaryMedia.class, tempMediaId);
				if (existingTemporaryMedia != null) {
					// Check map if we've already processed this temporary media, otherwise, do conversion
					ContentSectionMedia contentSectionMedia;
					if (processedConversions.containsKey(tempMediaId)) {
						contentSectionMedia = persistenceService.findById(ContentSectionMedia.class, processedConversions.get(tempMediaId));
					} else {
						contentSectionMedia = new ContentSectionMedia();
						contentSectionMedia.setActiveStatus(Component.ACTIVE_STATUS);
						contentSectionMedia.setContentSectionId(sectionId);
						contentSectionMedia.setUpdateUser(SecurityUtil.getCurrentUserName());
						contentSectionMedia.setCreateUser(SecurityUtil.getCurrentUserName());
						contentSectionMedia.setPrivateMedia(Boolean.FALSE);
						if (existingTemporaryMedia.getOriginalSourceURL().equals("fileUpload")) {
							//stripe generated part of name
							String nameParts[] = existingTemporaryMedia.getName().split(OpenStorefrontConstant.GENERAL_KEY_SEPARATOR);
							contentSectionMedia.setCaption(nameParts[0]);
						}

						// Set Media Type Code based on the mimetype stored in temporary (as retrieved from server)
						String mediaTypeCode;
						String mimeType = existingTemporaryMedia.getMimeType();
						if (mimeType.contains("image")) {
							mediaTypeCode = "IMG";
						} else if (mimeType.contains("video")) {
							mediaTypeCode = "VID";
						} else if (mimeType.contains("audio")) {
							mediaTypeCode = "AUD";
						} else {
							mediaTypeCode = "OTH";
						}

						contentSectionMedia.setMediaTypeCode(mediaTypeCode);

						try {
							Path path = existingTemporaryMedia.pathToMedia();
							InputStream in = new FileInputStream(path.toFile());
							contentSectionMedia.setContentSectionMediaId(persistenceService.generateId());
							contentSectionMedia.populateBaseCreateFields();
							contentSectionMedia.setFile(saveMediaFile(new MediaFile(), in, existingTemporaryMedia.getMimeType(), existingTemporaryMedia.getOriginalFileName()));
							persistenceService.persist(contentSectionMedia);
							//NOTE: (KB) commit so that we can find section media via sql
							persistenceService.commit();
							processedConversions.put(tempMediaId, contentSectionMedia.getContentSectionMediaId());
						} catch (IOException ex) {
							throw new OpenStorefrontRuntimeException("Failed to convert temporary media to section media", ex);
						}
					}
					// Replace converted url
					String replaceUrl = "SectionMedia&mediaId=".concat(contentSectionMedia.getFile().getMediaFileId());
					String newUrl = url.substring(0, url.indexOf("TemporaryMedia")).concat(replaceUrl);
					LOG.log(Level.FINE, MessageFormat.format("TemporaryMedia Conversion: Replacing {0} with {1}", url, newUrl));
					mediaItem.attr("src", newUrl);
				} else {
					LOG.log(Level.WARNING, MessageFormat.format("Unable to find existing temporary media for temporaryID: {0}", tempMediaId));
				}
			}
		}
		return descriptionDoc.toString();
	}

	@Override
	public ContentSectionAll getContentSectionAll(String contentSectionId, boolean publicInformationOnly)
	{
		ContentSectionAll contentSectionAll = null;

		//Non-proxy version
		ContentSection contentSection = new ContentSection();
		contentSection.setContentSectionId(contentSectionId);
		contentSection = contentSection.find();
		if (contentSection != null) {
			if (publicInformationOnly && contentSection.getPrivateSection()) {
				return null;
			}

			contentSectionAll = new ContentSectionAll();
			contentSectionAll.setSection(contentSection);

			ContentSubSection contentSubSectionExample = new ContentSubSection();
			contentSubSectionExample.setActiveStatus(ContentSection.ACTIVE_STATUS);
			contentSubSectionExample.setContentSectionId(contentSection.getContentSectionId());

			List<ContentSubSection> subSections = contentSubSectionExample.findByExample();
			for (ContentSubSection subSection : subSections) {
				boolean keep = false;
				if (publicInformationOnly) {
					if (!subSection.getPrivateSection()) {
						keep = true;
					}
				} else {
					keep = true;
				}

				if (keep) {
					contentSectionAll.getSubsections().add(subSection);
				}
			}
		}

		return contentSectionAll;
	}

	@Override
	public ContentSectionMedia saveMedia(ContentSectionMedia contentSectionMedia, InputStream fileInput, String mimeType, String originalFileName)
	{
		Objects.requireNonNull(contentSectionMedia);

		ContentSectionMedia savedMedia = contentSectionMedia.save();
		if (contentSectionMedia.getContentSectionMediaId() == null) {
			getChangeLogService().addEntityChange(savedMedia);
		}
		try {
			savedMedia.setFile(saveMediaFile(savedMedia.getFile(), fileInput, mimeType, originalFileName));
			persistenceService.persist(savedMedia);
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to store media file.", "Contact System Admin.  Check file permissions and disk space ", ex);
		}

		//Note: proxied media caused an overflow on serialization
		ContentSectionMedia updatedMedia = new ContentSectionMedia();
		updatedMedia.setContentSectionMediaId(savedMedia.getContentSectionMediaId());
		updatedMedia = updatedMedia.find();

		return updatedMedia;
	}

	private MediaFile saveMediaFile(MediaFile media, InputStream fileInput, String mimeType, String originalFileName) throws IOException
	{
		Objects.requireNonNull(fileInput);
		if (media == null) {
			media = new MediaFile();
		}
		media.setFileName(persistenceService.generateId() + OpenStorefrontConstant.getFileExtensionForMime(mimeType));
		media.setMimeType(mimeType);
		media.setOriginalName(originalFileName);
		media.setFileType(MediaFileType.MEDIA);
		Path path = Paths.get(MediaFileType.MEDIA.getPath() + "/" + media.getFileName());
		Files.copy(fileInput, path, StandardCopyOption.REPLACE_EXISTING);
		return media;
	}

	@Override
	public void deleteMedia(String contentSectionMediaId)
	{
		ContentSectionMedia existing = persistenceService.findById(ContentSectionMedia.class, contentSectionMediaId);
		if (existing != null) {
			removeLocalMedia(existing);
			persistenceService.delete(existing);
			getChangeLogService().removeEntityChange(ContentSectionMedia.class, existing);
		}
	}

	@Override
	public String saveSectionTemplate(ContentSectionTemplateView templateView)
	{
		ContentSectionTemplate template = persistenceService.findById(ContentSectionTemplate.class, templateView.getContentSectionTemplate().getTemplateId());
		if (template != null) {
			template.updateFields(templateView.getContentSectionTemplate());
			template = persistenceService.persist(template);
		} else {
			templateView.getContentSectionTemplate().setTemplateId(persistenceService.generateId());
			templateView.getContentSectionTemplate().populateBaseCreateFields();
			template = persistenceService.persist(templateView.getContentSectionTemplate());
		}

		//for this case we need to do full refresh of sub-sections only
		//Since the input contain adds and removes
		ContentSection contentSectionExisting = new ContentSection();
		contentSectionExisting.setEntity(ContentSectionTemplate.class.getSimpleName());
		contentSectionExisting.setEntityId(template.getTemplateId());
		contentSectionExisting = contentSectionExisting.find();
		if (contentSectionExisting != null) {
			ContentSectionMedia contentSectionMedia = new ContentSectionMedia();
			contentSectionMedia.setContentSectionId(contentSectionExisting.getContentSectionId());
			persistenceService.deleteByExample(contentSectionMedia);

			ContentSubSection contentSubSection = new ContentSubSection();
			contentSubSection.setContentSectionId(contentSectionExisting.getContentSectionId());
			persistenceService.deleteByExample(contentSubSection);
		}

		ContentSectionAll contentSectionAll = new ContentSectionAll();
		templateView.getContentSection().setEntity(ContentSectionTemplate.class.getSimpleName());
		templateView.getContentSection().setEntityId(template.getTemplateId());

		contentSectionAll.setSection(templateView.getContentSection());
		contentSectionAll.setSubsections(templateView.getSubSections());

		saveAll(contentSectionAll);

		return template.getTemplateId();
	}

	@Override
	public void deleteContentSection(String contentSectionId)
	{
		Objects.requireNonNull(contentSectionId);

		ContentSectionMedia contentSectionMedia = new ContentSectionMedia();
		contentSectionMedia.setContentSectionId(contentSectionId);
		List<ContentSectionMedia> media = persistenceService.queryByExample(contentSectionMedia);
		if (media != null) {
			media.forEach(item -> {
				deleteMedia(item.getContentSectionMediaId());
			});
		}

		ContentSubSection contentSubSection = new ContentSubSection();
		contentSubSection.setContentSectionId(contentSectionId);
		persistenceService.deleteByExample(contentSubSection);

		ContentSection contentSection = persistenceService.findById(ContentSection.class, contentSectionId);
		if (contentSection != null) {
			persistenceService.delete(contentSection);
			getChangeLogService().removeEntityChange(ContentSection.class, contentSection);
		}
	}

	void removeLocalMedia(ContentSectionMedia componentMedia)
	{
		if (componentMedia.getFile() != null) {
			//Note: this can't be rolled back
			ContentSectionMedia example = new ContentSectionMedia();
			example.setFile(new MediaFile());
			example.getFile().setMediaFileId(componentMedia.getFile().getMediaFileId());

			long count = persistenceService.countByExample(example);
			if (count == 1) {
				MediaFile mediaFile = persistenceService.findById(MediaFile.class, componentMedia.getFile().getMediaFileId());
				if (mediaFile != null) {
					Path path = mediaFile.path();
					if (path != null) {
						if (path.toFile().exists()) {
							if (path.toFile().delete() == false) {
								LOG.log(Level.WARNING, MessageFormat.format("Unable to delete local media. Path: {0}", path.toString()));
							}
						}
					}
					persistenceService.delete(mediaFile);
				}
			}
		}
	}

	@Override
	public boolean isContentTemplateBeingUsed(String templateId)
	{
		boolean inUse = false;

		EvaluationTemplate evaluationTemplateExample = new EvaluationTemplate();
		List<EvaluationTemplate> evaluationTemplates = evaluationTemplateExample.findByExample();
		for (EvaluationTemplate evaluationTemplate : evaluationTemplates) {
			for (EvaluationSectionTemplate sectionTemplate : evaluationTemplate.getSectionTemplates()) {
				if (sectionTemplate.getSectionTemplateId().equals(templateId)) {
					inUse = true;
				}
			}
		}

		return inUse;
	}

	@Override
	public void deleteContentTemplate(String templateId)
	{
		if (isContentTemplateBeingUsed(templateId)) {
			throw new OpenStorefrontRuntimeException("Unable to remove content template.", "Remove all ties to the template (see evaluation templates)");
		} else {
			ContentSectionTemplate template = persistenceService.findById(ContentSectionTemplate.class, templateId);
			if (template != null) {

				ContentSection contentSectionExample = new ContentSection();
				contentSectionExample.setEntity(ContentSectionTemplate.class.getSimpleName());
				contentSectionExample.setEntityId(templateId);

				ContentSection contentSection = contentSectionExample.find();
				if (contentSection != null) {
					deleteContentSection(contentSection.getContentSectionId());
				}

				persistenceService.delete(template);
			}
		}
	}

	@Override
	public String createSectionFromTemplate(String entity, String entityId, String sectionTemplateId)
	{
		Objects.requireNonNull(entity, "Entity Class name required");
		Objects.requireNonNull(entityId);
		Objects.requireNonNull(sectionTemplateId);

		ContentSectionTemplate template = persistenceService.findById(ContentSectionTemplate.class, sectionTemplateId);
		if (template != null) {

			ContentSection templateSection = new ContentSection();
			templateSection.setEntity(ContentSectionTemplate.class.getSimpleName());
			templateSection.setEntityId(sectionTemplateId);
			templateSection = templateSection.find();

			WorkflowStatus initialStatus = WorkflowStatus.initalStatus();
			if (initialStatus == null) {
				throw new OpenStorefrontRuntimeException("Unable to get initial workflow status", "Add at least one workflow status.");
			}

			ContentSection contentSection = new ContentSection();
			contentSection.setContentSectionId(persistenceService.generateId());
			contentSection.setEntity(entity);
			contentSection.setEntityId(entityId);
			contentSection.setTitle(templateSection.getTitle());
			contentSection.setContent(templateSection.getContent());
			contentSection.setNoContent(templateSection.getNoContent());
			contentSection.setPrivateSection(templateSection.getPrivateSection());
			contentSection.setWorkflowStatus(initialStatus.getCode());
			contentSection.setTemplateId(sectionTemplateId);
			contentSection.populateBaseCreateFields();
			contentSection = persistenceService.persist(contentSection);

			//copy media
			ContentSectionMedia templateSectionMedia = new ContentSectionMedia();
			templateSectionMedia.setContentSectionId(templateSection.getContentSectionId());
			List<ContentSectionMedia> templateMediaRecords = templateSectionMedia.findByExample();
			copySectionMedia(templateMediaRecords, contentSection);

			ContentSubSection templateSubSectionExample = new ContentSubSection();
			templateSubSectionExample.setContentSectionId(templateSection.getContentSectionId());

			List<ContentSubSection> templateSubSections = templateSubSectionExample.findByExample();
			for (ContentSubSection templateSubSection : templateSubSections) {

				ContentSubSection subSection = new ContentSubSection();
				subSection.setContentSectionId(contentSection.getContentSectionId());
				subSection.setSubSectionId(persistenceService.generateId());
				subSection.setTitle(templateSubSection.getTitle());
				subSection.setContent(templateSubSection.getContent());
				subSection.setNoContent(templateSubSection.getNoContent());
				subSection.setHideTitle(templateSubSection.getHideTitle());
				subSection.setOrder(templateSubSection.getOrder());
				subSection.setPrivateSection(templateSubSection.getPrivateSection());
				subSection.setCustomFields(templateSubSection.getCustomFields());
				subSection.populateBaseCreateFields();
				persistenceService.persist(subSection);

			}

			return contentSection.getContentSectionId();
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find template", "Check inpute template Id: " + sectionTemplateId);
		}

	}

	@Override
	public void copySectionMedia(List<ContentSectionMedia> originalMedia, ContentSection newSection)
	{
		for (ContentSectionMedia templateMedia : originalMedia) {
			ContentSectionMedia sectionMedia = new ContentSectionMedia();
			sectionMedia.setContentSectionId(newSection.getContentSectionId());
			sectionMedia.setMediaTypeCode(templateMedia.getMediaTypeCode());
			if (templateMedia.getFile() != null) {
				MediaFile file = persistenceService.findById(MediaFile.class, templateMedia.getFile().getMediaFileId());
				sectionMedia.setFile(file);
			}
			if (sectionMedia.getPrivateMedia() == null) {
				sectionMedia.setPrivateMedia(Boolean.FALSE);
			} else {
				sectionMedia.setPrivateMedia(templateMedia.getPrivateMedia());
			}
			sectionMedia.save();
		}
	}

}
