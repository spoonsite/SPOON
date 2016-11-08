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
import edu.usu.sdl.openstorefront.core.api.EvaluationService;
import edu.usu.sdl.openstorefront.core.entity.ChecklistTemplate;
import edu.usu.sdl.openstorefront.core.entity.ChecklistTemplateQuestion;
import edu.usu.sdl.openstorefront.core.entity.ContentSection;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionAttribute;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionMedia;
import edu.usu.sdl.openstorefront.core.entity.ContentSectionTemplate;
import edu.usu.sdl.openstorefront.core.entity.ContentSubSection;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklist;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistRecommendation;
import edu.usu.sdl.openstorefront.core.entity.EvaluationChecklistResponse;
import edu.usu.sdl.openstorefront.core.entity.EvaluationSectionTemplate;
import edu.usu.sdl.openstorefront.core.entity.EvaluationTemplate;
import edu.usu.sdl.openstorefront.core.entity.WorkflowStatus;
import edu.usu.sdl.openstorefront.core.model.ChecklistAll;
import edu.usu.sdl.openstorefront.core.model.ContentSectionAll;
import edu.usu.sdl.openstorefront.core.model.EvaluationAll;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class EvaluationServiceImpl
		extends ServiceProxy
		implements EvaluationService
{

	private static final Logger LOG = Logger.getLogger(EvaluationServiceImpl.class.getName());

	@Override
	public String saveCheckListAll(ChecklistAll checklistAll)
	{
		Objects.requireNonNull(checklistAll);
		Objects.requireNonNull(checklistAll.getEvaluationChecklist());

		EvaluationChecklist evaluationChecklist = checklistAll.getEvaluationChecklist().save();

		//remove old recommendations replace with the new ones
		EvaluationChecklistRecommendation recommendationDeleteExample = new EvaluationChecklistRecommendation();
		recommendationDeleteExample.setChecklistId(evaluationChecklist.getChecklistId());
		persistenceService.deleteByExample(recommendationDeleteExample);

		for (EvaluationChecklistRecommendation recommendation : checklistAll.getRecommendations()) {
			recommendation.setChecklistId(evaluationChecklist.getChecklistId());
			recommendation.setRecommendationId(persistenceService.generateId());
			recommendation.populateBaseCreateFields();
			persistenceService.persist(recommendation);
		}

		//remove old responses replace with the new ones
		EvaluationChecklistResponse responseDeleteExample = new EvaluationChecklistResponse();
		responseDeleteExample.setChecklistId(evaluationChecklist.getChecklistId());
		persistenceService.deleteByExample(responseDeleteExample);

		for (EvaluationChecklistResponse response : checklistAll.getResponses()) {
			response.setChecklistId(evaluationChecklist.getChecklistId());
			response.setResponseId(persistenceService.generateId());
			response.populateBaseCreateFields();
			persistenceService.persist(response);
		}

		return evaluationChecklist.getChecklistId();
	}

	@Override
	public ChecklistAll getChecklistAll(String checklistId)
	{
		Objects.requireNonNull(checklistId);

		ChecklistAll checklistAll = null;

		EvaluationChecklist evaluationChecklist = new EvaluationChecklist();
		evaluationChecklist.setChecklistId(checklistId);
		evaluationChecklist = evaluationChecklist.find();
		if (evaluationChecklist != null) {
			checklistAll = new ChecklistAll();
			checklistAll.setEvaluationChecklist(evaluationChecklist);

			EvaluationChecklistRecommendation recommendation = new EvaluationChecklistRecommendation();
			recommendation.setChecklistId(checklistId);
			recommendation.setActiveStatus(EvaluationChecklistRecommendation.ACTIVE_STATUS);
			checklistAll.getRecommendations().addAll(recommendation.findByExample());

			EvaluationChecklistResponse response = new EvaluationChecklistResponse();
			response.setChecklistId(checklistId);
			response.setActiveStatus(EvaluationChecklistResponse.ACTIVE_STATUS);
			checklistAll.getResponses().addAll(response.findByExample());
		}

		return checklistAll;
	}

	@Override
	public String saveEvaluationAll(EvaluationAll evaluationAll)
	{
		Objects.requireNonNull(evaluationAll);
		Objects.requireNonNull(evaluationAll.getEvaluation());
		Objects.requireNonNull(evaluationAll.getCheckListAll());

		Evaluation savedEvalation = evaluationAll.getEvaluation().save();
		evaluationAll.getCheckListAll().getEvaluationChecklist().setEvaluationId(savedEvalation.getEvaluationId());
		saveCheckListAll(evaluationAll.getCheckListAll());
		for (ContentSectionAll contentSectionAll : evaluationAll.getContentSections()) {
			contentSectionAll.getSection().setEntityId(savedEvalation.getEvaluationId());
			contentSectionAll.getSection().setEntity(Evaluation.class.getSimpleName());
			getContentSectionService().saveAll(contentSectionAll);
		}

		return savedEvalation.getEvaluationId();
	}

	@Override
	public Evaluation createEvaluationFromTemplate(Evaluation evaluation)
	{
		Objects.requireNonNull(evaluation);
		Objects.requireNonNull(evaluation.getTemplateId());

		evaluation.setEvaluationId(persistenceService.generateId());
		evaluation.setPublished(Boolean.FALSE);
		evaluation.populateBaseCreateFields();
		evaluation = persistenceService.persist(evaluation);

		EvaluationTemplate evaluationTemplate = new EvaluationTemplate();
		evaluationTemplate.setTemplateId(evaluation.getTemplateId());
		evaluationTemplate = evaluationTemplate.find();

		if (evaluationTemplate == null) {
			throw new OpenStorefrontRuntimeException("Unable to create evaluation", "Evaluation Template was not found.  Template Id: " + evaluation.getTemplateId());
		} else {

			WorkflowStatus initialStatus = WorkflowStatus.initalStatus();
			if (initialStatus == null) {
				throw new OpenStorefrontRuntimeException("Unable to get initial workflow status", "Add at least one workflow status.");
			}

			EvaluationChecklist checklist = new EvaluationChecklist();
			checklist.setChecklistId(persistenceService.generateId());
			checklist.setChecklistTemplateId(evaluationTemplate.getChecklistTemplateId());
			checklist.setEvaluationId(evaluation.getEvaluationId());
			checklist.setWorkflowStatus(initialStatus.getCode());
			checklist.populateBaseCreateFields();
			checklist = persistenceService.persist(checklist);

			ChecklistTemplate checklistTemplate = new ChecklistTemplate();
			checklistTemplate.setChecklistTemplateId(evaluationTemplate.getChecklistTemplateId());
			checklistTemplate = checklistTemplate.find();

			for (ChecklistTemplateQuestion question : checklistTemplate.getQuestions()) {
				EvaluationChecklistResponse response = new EvaluationChecklistResponse();
				response.setChecklistId(checklist.getChecklistId());
				response.setResponseId(persistenceService.generateId());
				response.setQuestionId(question.getQuestionId());
				response.setWorkflowStatus(initialStatus.getCode());
				response.populateBaseCreateFields();
				persistenceService.persist(response);
			}

			for (EvaluationSectionTemplate sectionTemplate : evaluationTemplate.getSectionTemplates()) {
				ContentSection templateSection = new ContentSection();
				templateSection.setEntity(ContentSectionTemplate.class.getSimpleName());
				templateSection.setEntityId(sectionTemplate.getSectionTemplateId());
				templateSection = templateSection.find();

				ContentSection contentSection = new ContentSection();
				contentSection.setContentSectionId(persistenceService.generateId());
				contentSection.setEntity(Evaluation.class.getSimpleName());
				contentSection.setEntityId(evaluation.getActiveStatus());
				contentSection.setContent(templateSection.getContent());
				contentSection.setNoContent(templateSection.getNoContent());
				contentSection.setPrivateSection(templateSection.getPrivateSection());
				contentSection.setWorkflowStatus(initialStatus.getCode());
				contentSection.populateBaseCreateFields();
				contentSection = persistenceService.persist(contentSection);

				//copy media
				ContentSectionMedia templateSectionMedia = new ContentSectionMedia();
				templateSectionMedia.setContentSectionId(templateSection.getContentSectionId());
				List<ContentSectionMedia> templateMediaRecords = templateSectionMedia.findByExample();
				for (ContentSectionMedia templateMedia : templateMediaRecords) {
					ContentSectionMedia sectionMedia = new ContentSectionMedia();
					sectionMedia.setContentSectionId(contentSection.getContentSectionId());
					sectionMedia.setMediaTypeCode(templateMedia.getMediaTypeCode());
					sectionMedia.setMimeType(templateMedia.getMimeType());
					sectionMedia.setOriginalName(templateMedia.getOriginalName());

					Path path = templateMedia.pathToMedia();
					if (path != null) {
						if (path.toFile().exists()) {
							try (InputStream in = new FileInputStream(path.toFile())) {
								getContentSectionService().saveMedia(sectionMedia, in);
							} catch (IOException ex) {
								LOG.log(Level.WARNING, MessageFormat.format("Unable to copy media from template.  Media path: {0} Original Name: {1}", new Object[]{path.toString(), templateMedia.getOriginalName()}), ex);
							}
						} else {
							LOG.log(Level.WARNING, MessageFormat.format("Unable to copy media from template.  Media path: {0} Original Name: {1}", new Object[]{path.toString(), templateMedia.getOriginalName()}));
						}
					} else {
						LOG.log(Level.WARNING, MessageFormat.format("Unable to copy media from template.  Media path: Doesn't exist? Original Name: {0}", templateMedia.getOriginalName()));
					}
				}

				ContentSectionAttribute templateSectionAttribute = new ContentSectionAttribute();
				ContentSectionAttributePk contentSectionAttributePk = new ContentSectionAttributePk();
				contentSectionAttributePk.setContentSectionId(templateSection.getContentSectionId());
				templateSectionAttribute.setContentSectionAttributePk(contentSectionAttributePk);

				List<ContentSectionAttribute> attributes = templateSectionAttribute.findByExample();
				for (ContentSectionAttribute attribute : attributes) {

					ContentSectionAttribute sectionAttribute = new ContentSectionAttribute();
					ContentSectionAttributePk sectionAttributePk = new ContentSectionAttributePk();
					sectionAttributePk.setContentSectionId(contentSection.getContentSectionId());
					sectionAttributePk.setAttributeCode(attribute.getContentSectionAttributePk().getAttributeCode());
					sectionAttributePk.setAttributeType(attribute.getContentSectionAttributePk().getAttributeType());
					sectionAttribute.setContentSectionAttributePk(sectionAttributePk);
					sectionAttribute.populateBaseCreateFields();
					persistenceService.persist(sectionAttribute);

				}

				ContentSubSection templateSubSectionExample = new ContentSubSection();
				templateSubSectionExample.setContentSectionId(templateSection.getContentSectionId());

				List<ContentSubSection> templateSubSections = templateSubSectionExample.findByExample();
				for (ContentSubSection templateSubSection : templateSubSections) {

					ContentSubSection subSection = new ContentSubSection();
					subSection.setContentSectionId(contentSection.getContentSectionId());
					subSection.setSubSectionId(persistenceService.generateId());
					subSection.setContent(templateSubSection.getContent());
					subSection.setNoContent(templateSubSection.getNoContent());
					subSection.setPrivateSection(templateSubSection.getPrivateSection());
					subSection.setCustomFields(templateSubSection.getCustomFields());
					subSection.populateBaseCreateFields();
					persistenceService.persist(subSection);

				}

			}

		}
		return evaluation;
	}

	@Override
	public EvaluationAll getEvaluation(String evaluationId)
	{
		Objects.requireNonNull(evaluationId);

		EvaluationAll evaluationAll = null;

		Evaluation evaluation = persistenceService.findById(Evaluation.class, evaluationId);
		if (evaluation != null) {
			evaluationAll = new EvaluationAll();
			evaluationAll.setEvaluation(evaluation);

			EvaluationChecklist evaluationChecklist = new EvaluationChecklist();
			evaluationChecklist.setActiveStatus(EvaluationChecklist.ACTIVE_STATUS);
			evaluationChecklist.setEvaluationId(evaluation.getEvaluationId());
			evaluationChecklist = evaluationChecklist.find();

			evaluationAll.setCheckListAll(getChecklistAll(evaluationChecklist.getChecklistId()));

			ContentSection contentSectionExample = new ContentSection();
			contentSectionExample.setActiveStatus(ContentSection.ACTIVE_STATUS);
			contentSectionExample.setEntity(Evaluation.class.getSimpleName());
			contentSectionExample.setEntityId(evaluation.getEvaluationId());

			List<ContentSection> contentSections = contentSectionExample.findByExample();
			for (ContentSection contentSection : contentSections) {
				ContentSectionAll contentSectionAll = getContentSectionService().getContentSectionAll(contentSection.getContentSectionId());
				evaluationAll.getContentSections().add(contentSectionAll);
			}
		}

		return evaluationAll;
	}

	@Override
	public EvaluationAll getLatestEvaluation(String componentId)
	{
		Objects.requireNonNull(componentId);

		Evaluation evaluationExample = new Evaluation();
		evaluationExample.setComponentId(componentId);
		evaluationExample.setActiveStatus(Evaluation.ACTIVE_STATUS);

		List<Evaluation> evaluations = evaluationExample.findByExample();
		Evaluation latest = null;
		for (Evaluation evaluation : evaluations) {
			if (latest == null) {
				latest = evaluation;
			} else if (evaluation.getCreateDts().after(latest.getCreateDts())) {
				latest = evaluation;
			}
		}

		if (latest != null) {
			return getEvaluation(latest.getEvaluationId());
		}
		return null;
	}

}
