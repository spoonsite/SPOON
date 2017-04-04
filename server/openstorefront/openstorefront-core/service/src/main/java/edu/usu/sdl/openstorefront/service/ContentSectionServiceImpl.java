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
import edu.usu.sdl.openstorefront.core.api.ContentSectionService;
import edu.usu.sdl.openstorefront.core.entity.ContentSection;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionMedia;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionTemplate;
import edu.usu.sdl.openstorefront.core.entity.ContentSubSection;
import edu.usu.sdl.openstorefront.core.entity.EvaluationSectionTemplate;
import edu.usu.sdl.openstorefront.core.entity.EvaluationTemplate;
import edu.usu.sdl.openstorefront.core.model.ContentSectionAll;
import edu.usu.sdl.openstorefront.core.view.ContentSectionTemplateView;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

		ContentSection contentSection = contentSectionAll.getSection().save();

		ContentSubSection contentSubSectionExample = new ContentSubSection();
		contentSubSectionExample.setContentSectionId(contentSection.getContentSectionId());
		List<ContentSubSection> subSections = contentSubSectionExample.findByExample();
		Map<String, List<ContentSubSection>> subSectionMap = subSections.stream()
				.collect(Collectors.groupingBy(ContentSubSection::getSubSectionId));

		for (ContentSubSection subSection : contentSectionAll.getSubsections()) {
			if (subSection.getSubSectionId() != null && subSectionMap.containsKey(subSection.getSubSectionId())) {
				ContentSubSection existing = subSectionMap.get(subSection.getSubSectionId()).get(0);
				existing.updateFields(subSection);
				persistenceService.persist(existing);
			} else {
				subSection.setContentSectionId(contentSection.getContentSectionId());
				subSection.populateBaseCreateFields();
				persistenceService.persist(subSection);
			}
		}

		return contentSection.getContentSectionId();
	}

	@Override
	public ContentSectionAll getContentSectionAll(String contentSectionId)
	{
		ContentSectionAll contentSectionAll = null;

		ContentSection contentSection = persistenceService.findById(ContentSection.class, contentSectionId);
		if (contentSection != null) {
			contentSectionAll = new ContentSectionAll();
			contentSectionAll.setSection(contentSection);

			ContentSubSection contentSubSectionExample = new ContentSubSection();
			contentSubSectionExample.setActiveStatus(ContentSection.ACTIVE_STATUS);
			contentSubSectionExample.setContentSectionId(contentSection.getContentSectionId());

			List<ContentSubSection> subSections = contentSubSectionExample.findByExample();
			contentSectionAll.getSubsections().addAll(subSections);
		}

		return contentSectionAll;
	}

	@Override
	public ContentSectionMedia saveMedia(ContentSectionMedia contentSectionMedia, InputStream in)
	{
		Objects.requireNonNull(contentSectionMedia);
		Objects.requireNonNull(in);

		if (contentSectionMedia.getContentSectionMediaId() == null) {
			getChangeLogService().addEntityChange(contentSectionMedia);
		}

		ContentSectionMedia savedMedia = contentSectionMedia.save();

		savedMedia.setFileName(savedMedia.getContentSectionMediaId());
		try (InputStream fileInput = in) {
			Files.copy(fileInput, savedMedia.pathToMedia(), StandardCopyOption.REPLACE_EXISTING);
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

	@Override
	public void deleteMedia(String contentSectionMediaId)
	{
		ContentSectionMedia existing = persistenceService.findById(ContentSectionMedia.class, contentSectionMediaId);
		if (existing != null) {
			Path mediaPath = existing.pathToMedia();
			if (mediaPath != null) {
				if (mediaPath.toFile().exists()) {
					if (mediaPath.toFile().delete()) {
						LOG.log(Level.WARNING, MessageFormat.format("Unable to delete local content section media. Path: {0}", mediaPath.toString()));
					}
				}
			}
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
		persistenceService.deleteByExample(contentSectionMedia);

		ContentSubSection contentSubSection = new ContentSubSection();
		contentSubSection.setContentSectionId(contentSectionId);
		persistenceService.deleteByExample(contentSubSection);

		ContentSection contentSection = persistenceService.findById(ContentSection.class, contentSectionId);
		if (contentSection != null) {
			persistenceService.delete(contentSection);
			getChangeLogService().removeEntityChange(ContentSection.class, contentSection);
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

}
