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
package edu.usu.sdl.openstorefront.service.transfermodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.io.ExportImport;
import edu.usu.sdl.openstorefront.storage.model.BaseComponent;
import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.storage.model.ComponentContact;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSection;
import edu.usu.sdl.openstorefront.storage.model.ComponentExternalDependency;
import edu.usu.sdl.openstorefront.storage.model.ComponentMedia;
import edu.usu.sdl.openstorefront.storage.model.ComponentMetadata;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestion;
import edu.usu.sdl.openstorefront.storage.model.ComponentResource;
import edu.usu.sdl.openstorefront.storage.model.ComponentReview;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewCon;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewPro;
import edu.usu.sdl.openstorefront.storage.model.ComponentTag;
import edu.usu.sdl.openstorefront.util.StringProcessor;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This is full composite Model of the component
 *
 * @author dshurtleff
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComponentAll
		implements ExportImport
{

	private Component component;

	@DataType(ComponentAttribute.class)
	private List<ComponentAttribute> attributes = new ArrayList<>();

	@DataType(ComponentContact.class)
	private List<ComponentContact> contacts = new ArrayList<>();

	@DataType(ComponentEvaluationSection.class)
	private List<ComponentEvaluationSection> evaluationSections = new ArrayList<>();

	@DataType(ComponentExternalDependency.class)
	private List<ComponentExternalDependency> externalDependencies = new ArrayList<>();

	@DataType(ComponentMedia.class)
	private List<ComponentMedia> media = new ArrayList<>();

	@DataType(ComponentMetadata.class)
	private List<ComponentMetadata> metadata = new ArrayList<>();

	@DataType(QuestionAll.class)
	private List<QuestionAll> questions = new ArrayList<>();

	@DataType(ComponentResource.class)
	private List<ComponentResource> resources = new ArrayList<>();

	@DataType(ReviewAll.class)
	private List<ReviewAll> reviews = new ArrayList<>();

	@DataType(ComponentTag.class)
	private List<ComponentTag> tags = new ArrayList<>();

	private IntegrationAll integrationAll;

	public ComponentAll()
	{
	}

	/**
	 * Validates all entities
	 *
	 * @return
	 */
	public ValidationResult validate()
	{
		ValidationModel validationModel = new ValidationModel(component);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		validationResult.merge(validateSubComponent(attributes));
		validationResult.merge(validateSubComponent(contacts));
		validationResult.merge(validateSubComponent(evaluationSections));
		validationResult.merge(validateSubComponent(externalDependencies));
		validationResult.merge(validateSubComponent(media));
		validationResult.merge(validateSubComponent(metadata));
		validationResult.merge(validateSubComponent(resources));
		validationResult.merge(validateSubComponent(tags));

		for (QuestionAll questionAll : questions) {
			List<ComponentQuestion> componentQuestions = new ArrayList<>();
			componentQuestions.add(questionAll.getQuestion());
			validationResult.merge(validateSubComponent(componentQuestions));
			validationResult.merge(validateSubComponent(questionAll.getResponds()));
		}

		for (ReviewAll reviewAll : reviews) {
			List<ComponentReview> componentReviews = new ArrayList<>();
			componentReviews.add(reviewAll.getComponentReview());
			validationResult.merge(validateSubComponent(componentReviews));

			for (ComponentReviewPro reviewPro : reviewAll.getPros()) {
				validationModel = new ValidationModel(reviewPro);
				validationModel.setConsumeFieldsOnly(true);
				validationResult.merge(ValidationUtil.validate(validationModel));
			}

			for (ComponentReviewCon reviewCon : reviewAll.getCons()) {
				validationModel = new ValidationModel(reviewCon);
				validationModel.setConsumeFieldsOnly(true);
				validationResult.merge(ValidationUtil.validate(validationModel));
			}
		}

		return validationResult;
	}

	public void populateCreateUpdateFields(boolean update)
	{
		populateCreateUpdateFieldsBaseComponent(component, update);
		populateCreateUpdateFieldsBaseComponent(attributes, update);
		populateCreateUpdateFieldsBaseComponent(contacts, update);
		populateCreateUpdateFieldsBaseComponent(evaluationSections, update);
		populateCreateUpdateFieldsBaseComponent(externalDependencies, update);
		populateCreateUpdateFieldsBaseComponent(media, update);
		populateCreateUpdateFieldsBaseComponent(metadata, update);
		populateCreateUpdateFieldsBaseComponent(resources, update);
		populateCreateUpdateFieldsBaseComponent(tags, update);

		for (QuestionAll questionAll : questions) {
			populateCreateUpdateFieldsBaseComponent(questionAll.getQuestion(), update);
			populateCreateUpdateFieldsBaseComponent(questionAll.getResponds(), update);
		}

		for (ReviewAll reviewAll : reviews) {
			populateCreateUpdateFieldsBaseComponent(reviewAll.getComponentReview(), update);
			populateCreateUpdateFieldsBaseComponent(reviewAll.getPros(), update);
			populateCreateUpdateFieldsBaseComponent(reviewAll.getCons(), update);
		}
	}

	private <T extends BaseEntity> void populateCreateUpdateFieldsBaseComponent(T baseEntity, boolean update)
	{
		List<T> entities = new ArrayList<>();
		entities.add(baseEntity);
		populateCreateUpdateFieldsBaseComponent(entities, update);
	}

	private <T extends BaseEntity> void populateCreateUpdateFieldsBaseComponent(List<T> baseEntities, boolean update)
	{
		for (BaseEntity baseEntity : baseEntities) {
			if (update) {
				baseEntity.populateBaseUpdateFields();
			} else {
				baseEntity.populateBaseCreateFields();
			}
		}
	}

	private <T extends BaseComponent> ValidationResult validateSubComponent(List<T> baseComponents)
	{
		ValidationResult validationResult = new ValidationResult();
		for (BaseComponent baseComponent : baseComponents) {
			ValidationModel validationModel = new ValidationModel(baseComponent);
			validationModel.setConsumeFieldsOnly(true);
			validationResult.merge(ValidationUtil.validate(validationModel));
		}
		return validationResult;
	}

	@Override
	public String export()
	{
		String componentJson = null;
		try {
			componentJson = StringProcessor.defaultObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException ex) {
			throw new OpenStorefrontRuntimeException("Unable to export component.", ex);
		}
		return componentJson;
	}

	@Override
	public void importData(String[] data)
	{
		Objects.requireNonNull(data, "Input data cannot be null");
		try {
			ComponentAll componentAll = StringProcessor.defaultObjectMapper().readValue(data[0], new TypeReference<ComponentAll>()
			{
			});
			if (componentAll != null) {
				this.component = componentAll.getComponent();
				this.attributes = componentAll.getAttributes();
				this.contacts = componentAll.getContacts();
				this.evaluationSections = componentAll.getEvaluationSections();
				this.externalDependencies = componentAll.getExternalDependencies();
				this.media = componentAll.getMedia();
				this.metadata = componentAll.getMetadata();
				this.questions = componentAll.getQuestions();
				this.resources = componentAll.getResources();
				this.reviews = componentAll.getReviews();
				this.tags = componentAll.getTags();
			} else {
				throw new OpenStorefrontRuntimeException("Unable to import component.", "Make sure the data is in the correct format");
			}
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to import component. Data could not be real", ex);
		}
	}

	public Component getComponent()
	{
		return component;
	}

	public void setComponent(Component component)
	{
		this.component = component;
	}

	public List<ComponentAttribute> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(List<ComponentAttribute> attributes)
	{
		this.attributes = attributes;
	}

	public List<ComponentContact> getContacts()
	{
		return contacts;
	}

	public void setContacts(List<ComponentContact> contacts)
	{
		this.contacts = contacts;
	}

	public List<ComponentEvaluationSection> getEvaluationSections()
	{
		return evaluationSections;
	}

	public void setEvaluationSections(List<ComponentEvaluationSection> evaluationSections)
	{
		this.evaluationSections = evaluationSections;
	}

	public List<ComponentExternalDependency> getExternalDependencies()
	{
		return externalDependencies;
	}

	public void setExternalDependencies(List<ComponentExternalDependency> externalDependencies)
	{
		this.externalDependencies = externalDependencies;
	}

	public List<ComponentMedia> getMedia()
	{
		return media;
	}

	public void setMedia(List<ComponentMedia> media)
	{
		this.media = media;
	}

	public List<ComponentMetadata> getMetadata()
	{
		return metadata;
	}

	public void setMetadata(List<ComponentMetadata> metadata)
	{
		this.metadata = metadata;
	}

	public List<QuestionAll> getQuestions()
	{
		return questions;
	}

	public void setQuestions(List<QuestionAll> questions)
	{
		this.questions = questions;
	}

	public List<ComponentResource> getResources()
	{
		return resources;
	}

	public void setResources(List<ComponentResource> resources)
	{
		this.resources = resources;
	}

	public List<ComponentTag> getTags()
	{
		return tags;
	}

	public void setTags(List<ComponentTag> tags)
	{
		this.tags = tags;
	}

	public List<ReviewAll> getReviews()
	{
		return reviews;
	}

	public void setReviews(List<ReviewAll> reviews)
	{
		this.reviews = reviews;
	}

	public IntegrationAll getIntegrationAll()
	{
		return integrationAll;
	}

	public void setIntegrationAll(IntegrationAll integrationAll)
	{
		this.integrationAll = integrationAll;
	}

}
