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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.api.ComponentService;
import edu.usu.sdl.openstorefront.service.api.ComponentServicePrivate;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.service.transfermodel.ComponentAll;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.storage.model.BaseComponent;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttributePk;
import edu.usu.sdl.openstorefront.storage.model.ComponentContact;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSchedule;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSection;
import edu.usu.sdl.openstorefront.storage.model.ComponentExternalDependency;
import edu.usu.sdl.openstorefront.storage.model.ComponentMedia;
import edu.usu.sdl.openstorefront.storage.model.ComponentMetadata;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestion;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.storage.model.ComponentResource;
import edu.usu.sdl.openstorefront.storage.model.ComponentReview;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewCon;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewConPk;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewPro;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewProPk;
import edu.usu.sdl.openstorefront.storage.model.ComponentTag;
import edu.usu.sdl.openstorefront.storage.model.ComponentTracking;
import edu.usu.sdl.openstorefront.storage.model.ReviewCon;
import edu.usu.sdl.openstorefront.storage.model.ReviewPro;
import edu.usu.sdl.openstorefront.storage.model.UserWatch;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import edu.usu.sdl.openstorefront.util.ServiceUtil;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentAttributeView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentContactView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentDetailView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentEvaluationView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentExternalDependencyView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentMediaView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentMetadataView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentQuestionResponseView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentQuestionView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentResourceView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentReviewProCon;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentReviewView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentSearchView;
import edu.usu.sdl.openstorefront.web.rest.model.RequiredForComponent;
import edu.usu.sdl.openstorefront.web.rest.model.SearchResultAttribute;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * Handles all component related entities
 *
 * @author dshurtleff
 * @author jlaw
 */
public class ComponentServiceImpl
		extends ServiceProxy
		implements ComponentService, ComponentServicePrivate
{

	private static final Logger log = Logger.getLogger(ComponentServiceImpl.class.getName());

	public ComponentServiceImpl()
	{
	}

	@Override
	public <T extends BaseComponent> List<T> getBaseComponent(Class<T> subComponentClass, String componentId)
	{
		return getBaseComponent(subComponentClass, componentId, BaseComponent.ACTIVE_STATUS);
	}

	@Override
	public <T extends BaseComponent> List<T> getBaseComponent(Class<T> subComponentClass, String componentId, String activeStatus)
	{
		try {
			T baseComponentExample = subComponentClass.newInstance();
			baseComponentExample.setComponentId(componentId);
			baseComponentExample.setActiveStatus(activeStatus);
			return persistenceService.queryByExample(subComponentClass, new QueryByExample(baseComponentExample));
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
	}

	@Override
	public <T extends BaseComponent> T deactivateBaseComponent(Class<T> subComponentClass, Object pk)
	{
		return deactivateBaseComponent(subComponentClass, pk, true);
	}

	private <T extends BaseComponent> T deactivateBaseComponent(Class<T> subComponentClass, Object pk, boolean updateComponentActivity)
	{
		T found = persistenceService.findById(subComponentClass, pk);
		if (found != null) {
			found.setActiveStatus(T.INACTIVE_STATUS);
			found.setUpdateDts(TimeUtil.currentDate());
			found.setUpdateUser(SecurityUtil.getCurrentUserName());
			persistenceService.persist(found);

			if (updateComponentActivity) {
				updateComponentLastActivity(found.getComponentId());
			}
		}
		return found;
	}

	@Override
	public <T extends BaseComponent> void deleteBaseComponent(Class<T> subComponentClass, Object pk)
	{
		deleteBaseComponent(subComponentClass, pk, true);
	}

	private <T extends BaseComponent> void deleteBaseComponent(Class<T> subComponentClass, Object pk, boolean updateComponentActivity)
	{
		T found = persistenceService.findById(subComponentClass, pk);
		if (found != null) {
			String componentId = found.getComponentId();
			persistenceService.delete(found);

			if (updateComponentActivity) {
				updateComponentLastActivity(componentId);
			}
		}
	}

	@Override
	public <T extends BaseComponent> void deleteAllBaseComponent(Class<T> subComponentClass, String componentId)
	{
		deleteAllBaseComponent(subComponentClass, componentId, true);
	}

	public <T extends BaseComponent> void deleteAllBaseComponent(Class<T> subComponentClass, String componentId, boolean updateComponentActivity)
	{
		try {
			T example = subComponentClass.newInstance();
			example.setComponentId(componentId);
			persistenceService.deleteByExample(example);

			if (updateComponentActivity) {
				updateComponentLastActivity(componentId);
			}
		} catch (InstantiationException | IllegalAccessException ex) {
			Logger.getLogger(ComponentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void deactivateComponent(String componentId)
	{
		Component component = persistenceService.findById(Component.class, componentId);
		if (component != null) {
			component.setActiveStatus(Component.INACTIVE_STATUS);
			component.setUpdateDts(TimeUtil.currentDate());
			component.setUpdateUser(SecurityUtil.getCurrentUserName());
			persistenceService.persist(component);
			getUserService().removeAllWatchesForComponent(componentId);
		}
	}

	@Override
	public Component activateComponent(String componentId)
	{
		Component component = persistenceService.findById(Component.class, componentId);
		if (component != null) {
			component.setActiveStatus(Component.ACTIVE_STATUS);
			component.setUpdateDts(TimeUtil.currentDate());
			component.setUpdateUser(SecurityUtil.getCurrentUserName());
			persistenceService.persist(component);
		}
		return component;
	}

	@Override
	public List<ComponentSearchView> getComponents()
	{
		List<ComponentSearchView> componentSearchViews = new ArrayList<>();

		Component componentExample = new Component();
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);
		List<Component> components = persistenceService.queryByExample(Component.class, new QueryByExample(componentExample));

		ComponentAttribute componentAttributeExample = new ComponentAttribute();
		componentAttributeExample.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
		List<ComponentAttribute> componentAttributes = persistenceService.queryByExample(ComponentAttribute.class, new QueryByExample(componentAttributeExample));
		Map<String, List<ComponentAttribute>> attributeMaps = new HashMap<>();
		for (ComponentAttribute attribute : componentAttributes) {
			if (attributeMaps.containsKey(attribute.getComponentAttributePk().getComponentId())) {
				List<ComponentAttribute> attributes = attributeMaps.get(attribute.getComponentAttributePk().getComponentId());
				attributes.add(attribute);
			} else {
				List<ComponentAttribute> attributes = new ArrayList<>();
				attributes.add(attribute);
				attributeMaps.put(attribute.getComponentAttributePk().getComponentId(), attributes);
			}
		}

		for (Component component : components) {
			ComponentSearchView componentSearchView = ComponentSearchView.toView(component);
			List<ComponentAttribute> attributes = attributeMaps.get(component.getComponentId());
			if (attributes == null) {
				attributes = new ArrayList<>();
			}
			componentSearchView.setAttributes(SearchResultAttribute.toViewList(attributes));
			componentSearchViews.add(componentSearchView);
		}

		return componentSearchViews;
	}

	@Override
	public List<ComponentAttribute> getAttributesByComponentId(String componentId)
	{
		ComponentAttribute example = new ComponentAttribute();
		ComponentAttributePk pk = new ComponentAttributePk();
		pk.setComponentId(componentId);
		example.setComponentAttributePk(pk);
		return persistenceService.queryByExample(ComponentAttribute.class, new QueryByExample(example));
	}

	@Override
	public ComponentDetailView getComponentDetails(String componentId)
	{

		ComponentDetailView result = new ComponentDetailView();
		Component tempComponent = persistenceService.findById(Component.class, componentId);
		Component tempParentComponent;
		if (tempComponent != null && tempComponent.getParentComponentId() != null) {
			tempParentComponent = persistenceService.findById(Component.class, tempComponent.getParentComponentId());
		} else {
			tempParentComponent = new Component();
		}
		result.setComponentDetails(tempComponent, tempParentComponent);

		UserWatch tempWatch = new UserWatch();
		tempWatch.setUsername(SecurityUtil.getCurrentUserName());
		tempWatch.setActiveStatus(UserWatch.ACTIVE_STATUS);
		tempWatch.setComponentId(componentId);
		UserWatch tempUserWatch = persistenceService.queryOneByExample(UserWatch.class, new QueryByExample(tempWatch));
		if (tempUserWatch != null) {
			result.setLastViewedDts(tempUserWatch.getLastViewDts());
		}
		List<ComponentAttribute> attributes = getAttributesByComponentId(componentId);
		result.setAttributes(ComponentAttributeView.toViewList(attributes));

		result.setComponentId(componentId);
		result.setTags(getBaseComponent(ComponentTag.class, componentId));

		List<ComponentResource> componentResources = getBaseComponent(ComponentResource.class, componentId);
		componentResources.forEach(resource -> {
			result.getResources().add(ComponentResourceView.toView(resource));
		});

		List<ComponentMetadata> componentMetadata = getBaseComponent(ComponentMetadata.class, componentId);
		componentMetadata.forEach(metadata -> {
			result.getMetadata().add(ComponentMetadataView.toView(metadata));
		});

		List<ComponentMedia> componentMedia = getBaseComponent(ComponentMedia.class, componentId);
		componentMedia.forEach(media -> {
			result.getComponentMedia().add(ComponentMediaView.toView(media));
		});

		List<ComponentExternalDependency> componentDependency = getBaseComponent(ComponentExternalDependency.class, componentId);
		componentDependency.forEach(dependency -> {
			result.getDependencies().add(ComponentExternalDependencyView.toView(dependency));
		});

		List<ComponentContact> componentContact = getBaseComponent(ComponentContact.class, componentId);
		componentContact.forEach(contact -> {
			result.getContacts().add(ComponentContactView.toView(contact));
		});

		result.setComponentViews(Integer.MIN_VALUE /*figure out a way to get component views*/);

		List<ComponentReview> tempReviews = getBaseComponent(ComponentReview.class, componentId);
		List<ComponentReviewView> reviews = new ArrayList();
		tempReviews.forEach(review -> {
			ComponentReviewPro tempPro = new ComponentReviewPro();
			ComponentReviewProPk tempProPk = new ComponentReviewProPk();
			ComponentReviewCon tempCon = new ComponentReviewCon();
			ComponentReviewConPk tempConPk = new ComponentReviewConPk();

			tempProPk.setComponentReviewId(review.getComponentReviewId());
			tempConPk.setComponentReviewId(review.getComponentReviewId());

			tempPro.setComponentReviewProPk(tempProPk);
			tempCon.setComponentReviewConPk(tempConPk);

			ComponentReviewView tempView = ComponentReviewView.toView(review);

			tempView.setPros(ComponentReviewProCon.toViewListPro(persistenceService.queryByExample(ComponentReviewPro.class, new QueryByExample(tempPro))));
			tempView.setCons(ComponentReviewProCon.toViewListCon(persistenceService.queryByExample(ComponentReviewCon.class, new QueryByExample(tempCon))));

			reviews.add(tempView);
		});
		result.setReviews(reviews);

		// Here we grab the responses to each question
		List<ComponentQuestionView> questionViews = new ArrayList<>();
		List<ComponentQuestion> questions = getBaseComponent(ComponentQuestion.class, componentId);
		questions.stream().forEach((question) -> {
			ComponentQuestionResponse tempResponse = new ComponentQuestionResponse();
			List<ComponentQuestionResponseView> responseViews;
			tempResponse.setQuestionId(question.getQuestionId());
			tempResponse.setActiveStatus(ComponentQuestionResponse.ACTIVE_STATUS);
			responseViews = ComponentQuestionResponseView.toViewList(persistenceService.queryByExample(ComponentQuestionResponse.class, new QueryByExample(tempResponse)));
			questionViews.add(ComponentQuestionView.toView(question, responseViews));
		});
		result.setQuestions(questionViews);

		List<ComponentEvaluationSchedule> evaluationSchedules = getBaseComponent(ComponentEvaluationSchedule.class, componentId);
		List<ComponentEvaluationSection> evaluationSections = getBaseComponent(ComponentEvaluationSection.class, componentId);
		result.setEvaluation(ComponentEvaluationView.toViewFromStorage(evaluationSchedules, evaluationSections));

		result.setToday(new Date());

		return result;
	}

	@Override
	public void saveComponentAttribute(ComponentAttribute attribute)
	{
		getComponentServicePrivate().saveComponentAttribute(attribute, true);
		getSearchService().addComponent(persistenceService.findById(Component.class, attribute.getComponentId()));
	}

	@Override
	public void saveComponentAttribute(ComponentAttribute attribute, boolean updateLastActivity)
	{
		AttributeType type = persistenceService.findById(AttributeType.class, attribute.getComponentAttributePk().getAttributeType());

		AttributeCodePk pk = new AttributeCodePk();
		pk.setAttributeCode(attribute.getComponentAttributePk().getAttributeCode());
		pk.setAttributeType(attribute.getComponentAttributePk().getAttributeType());
		AttributeCode code = persistenceService.findById(AttributeCode.class, pk);

		if (type != null && code != null) {
			ComponentAttribute oldAttribute = persistenceService.findById(ComponentAttribute.class, attribute.getComponentAttributePk());
			if (oldAttribute != null) {
				oldAttribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
				oldAttribute.setUpdateUser(attribute.getUpdateUser());
				oldAttribute.setUpdateDts(TimeUtil.currentDate());
				persistenceService.persist(oldAttribute);
			} else {
				if (type.getAllowMutlipleFlg() == false) {
					ComponentAttribute example = new ComponentAttribute();
					example.setComponentAttributePk(new ComponentAttributePk());
					example.getComponentAttributePk().setAttributeType(attribute.getComponentAttributePk().getAttributeType());

					ComponentAttribute test = persistenceService.queryOneByExample(ComponentAttribute.class, new QueryByExample(example));
					if (test != null) {
						throw new OpenStorefrontRuntimeException("Attribute Type doesn't allow multiple codes.  Type: " + type.getAttributeType(), "Check data passed in.");
					}
				}
				attribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
				attribute.setCreateDts(TimeUtil.currentDate());
				attribute.setUpdateDts(TimeUtil.currentDate());
				persistenceService.persist(attribute);
			}
			if (updateLastActivity) {
				updateComponentLastActivity(attribute.getComponentAttributePk().getComponentId());
			}

		} else {
			StringBuilder error = new StringBuilder();
			if (type == null) {
				error.append("Attribute type not found.  Type: ").append(attribute.getComponentAttributePk().getAttributeType());
			}

			if (code == null) {
				error.append("Attribute Code not found. Code: ").append(attribute.getComponentAttributePk());
			}

			throw new OpenStorefrontRuntimeException(error.toString(), "Check data passed in.");
		}
	}

	private void updateComponentLastActivity(String componentId)
	{
		Objects.requireNonNull(componentId, "Component Id is required");

		Component component = persistenceService.findById(Component.class, componentId);
		if (component != null) {
			component.setLastActivityDts(TimeUtil.currentDate());
			persistenceService.persist(component);
		} else {
			throw new OpenStorefrontRuntimeException("Component not found to update last Activity", "Check component Id: " + componentId);
		}
	}

	@Override
	public void saveComponentContact(ComponentContact contact)
	{
		saveComponentContact(contact, true);
	}

	private void saveComponentContact(ComponentContact contact, boolean updateLastActivity)
	{
		ComponentContact oldContact = persistenceService.findById(ComponentContact.class, contact.getContactId());
		if (oldContact != null) {
			oldContact.setActiveStatus(contact.getActiveStatus());
			oldContact.setContactType(contact.getContactType());
			oldContact.setEmail(contact.getEmail());
			oldContact.setFirstName(contact.getFirstName());
			oldContact.setLastName(contact.getLastName());
			oldContact.setOrganization(contact.getOrganization());
			oldContact.setPhone(contact.getPhone());
			oldContact.setUpdateDts(TimeUtil.currentDate());
			oldContact.setUpdateUser(contact.getUpdateUser());
			persistenceService.persist(oldContact);
		} else {
			contact.setActiveStatus(ComponentContact.ACTIVE_STATUS);
			contact.setContactId(persistenceService.generateId());
			contact.setCreateDts(TimeUtil.currentDate());
			contact.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(contact);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(contact.getComponentId());
		}
	}

	@Override
	public void saveComponentDependency(ComponentExternalDependency dependency)
	{
		saveComponentDependency(dependency, true);
	}

	private void saveComponentDependency(ComponentExternalDependency dependency, boolean updateLastActivity)
	{
		ComponentExternalDependency oldDependency = persistenceService.findById(ComponentExternalDependency.class, dependency.getDependencyId());
		if (oldDependency != null) {
			oldDependency.setComment(dependency.getComment());
			oldDependency.setDependancyReferenceLink(dependency.getDependancyReferenceLink());
			oldDependency.setDependencyName(dependency.getDependencyName());
			oldDependency.setVersion(dependency.getVersion());
			oldDependency.setActiveStatus(dependency.getActiveStatus());
			oldDependency.setUpdateDts(TimeUtil.currentDate());
			oldDependency.setUpdateUser(dependency.getUpdateUser());
			persistenceService.persist(oldDependency);
		} else {
			dependency.setActiveStatus(ComponentExternalDependency.ACTIVE_STATUS);
			dependency.setDependencyId(persistenceService.generateId());
			dependency.setCreateDts(TimeUtil.currentDate());
			dependency.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(dependency);
		}
		if (updateLastActivity) {
			updateComponentLastActivity(dependency.getComponentId());
		}
	}

	@Override
	public void saveComponentEvaluationSection(ComponentEvaluationSection section)
	{
		saveComponentEvaluationSection(section, true);
	}

	private void saveComponentEvaluationSection(ComponentEvaluationSection section, boolean updateLastActivity)
	{
		ComponentEvaluationSection oldSection = persistenceService.findById(ComponentEvaluationSection.class, section.getComponentEvaluationSectionPk());
		if (oldSection != null) {
			oldSection.setActiveStatus(section.getActiveStatus());
			oldSection.setScore(section.getScore());
			oldSection.setUpdateDts(TimeUtil.currentDate());
			oldSection.setUpdateUser(section.getUpdateUser());
			persistenceService.persist(oldSection);
		} else {
			section.setActiveStatus(ComponentEvaluationSection.ACTIVE_STATUS);
			section.setCreateDts(TimeUtil.currentDate());
			section.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(section);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(section.getComponentId());
		}
	}

	@Override
	public void saveComponentEvaluationSchedule(ComponentEvaluationSchedule schedule)
	{
		saveComponentEvaluationSchedule(schedule, true);
	}

	private void saveComponentEvaluationSchedule(ComponentEvaluationSchedule schedule, boolean updateLastActivity)
	{
		//check schedule code vs level attribute
		boolean levelMatches = false;
		List<AttributeCode> levelCodes = getAttributeService().findCodesForType(AttributeType.DI2ELEVEL);
		for (AttributeCode levelCode : levelCodes) {
			if (levelCode.getAttributeCodePk().getAttributeCode().equals(schedule.getComponentEvaluationSchedulePk().getEvaluationLevelCode())) {
				levelMatches = true;
				break;
			}
		}

		if (levelMatches == false) {
			throw new OpenStorefrontRuntimeException("The evaluation level code doesn't match level in the attributes.", " Check data and the attribute code for type: " + AttributeType.DI2ELEVEL);
		}

		ComponentEvaluationSchedule oldSchedule = persistenceService.findById(ComponentEvaluationSchedule.class, schedule.getComponentEvaluationSchedulePk());
		if (oldSchedule != null) {
			oldSchedule.setCompletionDate(schedule.getCompletionDate());
			oldSchedule.setLevelStatus(schedule.getLevelStatus());
			oldSchedule.setActiveStatus(schedule.getActiveStatus());
			oldSchedule.setUpdateDts(TimeUtil.currentDate());
			oldSchedule.setUpdateUser(schedule.getUpdateUser());
			persistenceService.persist(oldSchedule);
		} else {
			schedule.setActiveStatus(ComponentEvaluationSchedule.ACTIVE_STATUS);
			schedule.getComponentEvaluationSchedulePk().setComponentId(schedule.getComponentId());
			schedule.setCreateDts(TimeUtil.currentDate());
			schedule.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(schedule);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(schedule.getComponentId());
		}
	}

	@Override
	public void saveComponentMedia(ComponentMedia media)
	{
		saveComponentMedia(media, true);
	}

	private void saveComponentMedia(ComponentMedia media, boolean updateLastActivity)
	{
		ComponentMedia oldMedia = persistenceService.findById(ComponentMedia.class, media.getComponentMediaId());
		if (oldMedia != null) {
			oldMedia.setCaption(media.getCaption());
			oldMedia.setFileName(media.getFileName());
			oldMedia.setLink(media.getLink());
			oldMedia.setMediaTypeCode(media.getMediaTypeCode());
			oldMedia.setMimeType(media.getMimeType());
			oldMedia.setOriginalName(media.getOriginalName());
			oldMedia.setActiveStatus(media.getActiveStatus());
			oldMedia.setUpdateDts(TimeUtil.currentDate());
			oldMedia.setUpdateUser(media.getUpdateUser());
			persistenceService.persist(oldMedia);
		} else {
			media.setActiveStatus(ComponentMedia.ACTIVE_STATUS);
			media.setComponentMediaId(persistenceService.generateId());
			media.setCreateDts(TimeUtil.currentDate());
			media.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(media);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(media.getComponentId());
		}
	}

	@Override
	public void saveComponentMetadata(ComponentMetadata metadata)
	{
		saveComponentMetadata(metadata, true);
	}

	public void saveComponentMetadata(ComponentMetadata metadata, boolean updateLastActivity)
	{
		ComponentMetadata oldMetadata = persistenceService.findById(ComponentMetadata.class, metadata.getMetadataId());
		if (oldMetadata != null) {
			oldMetadata.setLabel(metadata.getLabel());
			oldMetadata.setValue(metadata.getValue());
			oldMetadata.setActiveStatus(metadata.getActiveStatus());
			oldMetadata.setUpdateDts(TimeUtil.currentDate());
			oldMetadata.setUpdateUser(metadata.getUpdateUser());
			persistenceService.persist(oldMetadata);
		} else {
			metadata.setActiveStatus(ComponentMetadata.ACTIVE_STATUS);
			metadata.setMetadataId(persistenceService.generateId());
			metadata.setCreateDts(TimeUtil.currentDate());
			metadata.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(metadata);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(metadata.getComponentId());
		}
	}

	@Override
	public void saveComponentQuestion(ComponentQuestion question)
	{
		saveComponentQuestion(question, true);
	}

	private void saveComponentQuestion(ComponentQuestion question, boolean updateLastActivity)
	{
		ComponentQuestion oldQuestion = persistenceService.findById(ComponentQuestion.class, question.getQuestionId());
		if (oldQuestion != null) {
			oldQuestion.setOrganization(question.getOrganization());
			oldQuestion.setQuestion(question.getQuestion());
			oldQuestion.setUserTypeCode(question.getUserTypeCode());
			oldQuestion.setActiveStatus(question.getActiveStatus());
			oldQuestion.setUpdateDts(TimeUtil.currentDate());
			oldQuestion.setUpdateUser(question.getUpdateUser());
			persistenceService.persist(oldQuestion);
		} else {
			question.setActiveStatus(ComponentQuestion.ACTIVE_STATUS);
			question.setQuestion(question.getQuestion());
			question.setQuestionId(persistenceService.generateId());
			question.setCreateDts(TimeUtil.currentDate());
			question.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(question);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(question.getComponentId());
		}
	}

	@Override
	public void saveComponentQuestionResponse(ComponentQuestionResponse response)
	{
		saveComponentQuestionResponse(response, true);
	}

	public void saveComponentQuestionResponse(ComponentQuestionResponse response, boolean updateLastActivity)
	{
		ComponentQuestionResponse oldResponse = persistenceService.findById(ComponentQuestionResponse.class, response.getResponseId());
		if (oldResponse != null) {
			oldResponse.setOrganization(response.getOrganization());
			oldResponse.setQuestionId(response.getQuestionId());
			oldResponse.setResponse(response.getResponse());
			oldResponse.setUserTypeCode(response.getUserTypeCode());
			oldResponse.setActiveStatus(response.getActiveStatus());
			oldResponse.setUpdateDts(TimeUtil.currentDate());
			oldResponse.setUpdateUser(response.getUpdateUser());
			persistenceService.persist(oldResponse);
		} else {
			response.setActiveStatus(ComponentQuestionResponse.ACTIVE_STATUS);
			response.setResponseId(persistenceService.generateId());
			response.setCreateDts(TimeUtil.currentDate());
			response.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(response);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(response.getComponentId());
		}
	}

	@Override
	public void saveComponentResource(ComponentResource resource)
	{
		saveComponentResource(resource, true);
	}

	private void saveComponentResource(ComponentResource resource, boolean updateLastActivity)
	{
		ComponentResource oldResource = persistenceService.findById(ComponentResource.class, resource.getResourceId());
		if (oldResource != null) {
			oldResource.setDescription(resource.getDescription());
			oldResource.setLink(resource.getLink());
			oldResource.setFileName(resource.getFileName());
			oldResource.setOriginalName(resource.getOriginalName());
			oldResource.setMimeType(resource.getMimeType());
			oldResource.setResourceType(resource.getResourceType());
			oldResource.setRestricted(resource.getRestricted());
			oldResource.setActiveStatus(resource.getActiveStatus());
			oldResource.setUpdateDts(TimeUtil.currentDate());
			oldResource.setUpdateUser(resource.getUpdateUser());
			persistenceService.persist(oldResource);
		} else {
			resource.setActiveStatus(ComponentResource.ACTIVE_STATUS);
			resource.setResourceId(persistenceService.generateId());
			resource.setCreateDts(TimeUtil.currentDate());
			resource.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(resource);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(resource.getComponentId());
		}
	}

	@Override
	public void saveComponentReview(ComponentReview review)
	{
		saveComponentReview(review, true);
	}

	private void saveComponentReview(ComponentReview review, boolean updateLastActivity)
	{
		ComponentReview oldReview = persistenceService.findById(ComponentReview.class, review.getComponentReviewId());
		if (oldReview != null) {
			oldReview.setComment(review.getComment());
			oldReview.setUserTimeCode(review.getUserTimeCode());
			oldReview.setLastUsed(review.getLastUsed());
			oldReview.setOrganization(review.getOrganization());
			oldReview.setRating(review.getRating());
			oldReview.setRecommend(review.getRecommend());
			oldReview.setTitle(review.getTitle());
			oldReview.setUserTypeCode(review.getUserTypeCode());
			oldReview.setActiveStatus(review.getActiveStatus());
			oldReview.setUpdateDts(TimeUtil.currentDate());
			oldReview.setUpdateUser(review.getUpdateUser());
			persistenceService.persist(oldReview);
		} else {
			review.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			review.setComponentReviewId(persistenceService.generateId());
			review.setCreateDts(TimeUtil.currentDate());
			review.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(review);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(review.getComponentId());
		}
	}

	@Override
	public void saveComponentReviewCon(ComponentReviewCon con)
	{
		saveComponentReviewCon(con, true);
	}

	private void saveComponentReviewCon(ComponentReviewCon con, boolean updateLastActivity)
	{
		ComponentReviewCon oldCon = persistenceService.findById(ComponentReviewCon.class, con.getComponentReviewConPk());
		if (oldCon != null) {
			oldCon.setActiveStatus(con.getActiveStatus());
			oldCon.setUpdateDts(TimeUtil.currentDate());
			oldCon.setUpdateUser(con.getUpdateUser());
			persistenceService.persist(oldCon);
		} else {
			con.setActiveStatus(ComponentReviewCon.ACTIVE_STATUS);
			con.setCreateDts(TimeUtil.currentDate());
			con.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(con);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(con.getComponentId());
		}
	}

	@Override
	public void saveComponentReviewPro(ComponentReviewPro pro)
	{
		saveComponentReviewPro(pro, true);
	}

	private void saveComponentReviewPro(ComponentReviewPro pro, boolean updateLastActivity)
	{
		ComponentReviewPro oldPro = persistenceService.findById(ComponentReviewPro.class, pro.getComponentReviewProPk());
		if (oldPro != null) {
			oldPro.setActiveStatus(pro.getActiveStatus());
			oldPro.setUpdateDts(TimeUtil.currentDate());
			oldPro.setUpdateUser(pro.getUpdateUser());
			persistenceService.persist(oldPro);
		} else {
			pro.setCreateDts(TimeUtil.currentDate());
			pro.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(pro);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(pro.getComponentId());
		}
	}

	@Override
	public void saveComponentTag(ComponentTag tag)
	{
		getComponentServicePrivate().saveComponentTag(tag, true);
		getSearchService().addComponent(persistenceService.findById(Component.class, tag.getComponentId()));
	}

	@Override
	public void saveComponentTag(ComponentTag tag, boolean updateLastActivity)
	{
		ComponentTag oldTag = persistenceService.findById(ComponentTag.class, tag.getTagId());
		if (oldTag != null) {
			oldTag.setText(tag.getText());
			oldTag.setActiveStatus(tag.getActiveStatus());
			oldTag.setUpdateDts(TimeUtil.currentDate());
			oldTag.setUpdateUser(tag.getUpdateUser());
			persistenceService.persist(oldTag);
		} else {
			tag.setActiveStatus(ComponentTag.ACTIVE_STATUS);
			tag.setTagId(persistenceService.generateId());
			tag.setCreateDts(TimeUtil.currentDate());
			tag.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(tag);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(tag.getComponentId());
		}
	}

	@Override
	public void saveComponentTracking(ComponentTracking tracking)
	{
		ComponentTracking oldTracking = persistenceService.findById(ComponentTracking.class, tracking.getComponentTrackingId());
		if (oldTracking != null) {
			oldTracking.setClientIp(tracking.getClientIp());
			oldTracking.setEventDts(tracking.getEventDts());
			oldTracking.setTrackEventTypeCode(tracking.getTrackEventTypeCode());
			oldTracking.setActiveStatus(tracking.getActiveStatus());
			oldTracking.setUpdateDts(TimeUtil.currentDate());
			oldTracking.setUpdateUser(tracking.getUpdateUser());
			persistenceService.persist(oldTracking);
		} else {
			tracking.setActiveStatus(ComponentTracking.ACTIVE_STATUS);
			tracking.setComponentTrackingId(persistenceService.generateId());
			tracking.setCreateDts(TimeUtil.currentDate());
			tracking.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(tracking);
		}
	}

	@Override
	public RequiredForComponent saveComponent(RequiredForComponent component)
	{
		getComponentServicePrivate().saveComponent(component, true);
		getSearchService().addComponent(component.getComponent());
		return component;
	}

	@Override
	public RequiredForComponent saveComponent(RequiredForComponent component, boolean test)
	{
		Component oldComponent = persistenceService.findById(Component.class, component.getComponent().getComponentId());

		ValidationResult validationResult = component.checkForComplete();
		if (validationResult.valid()) {
			if (oldComponent != null) {

				oldComponent.setName(component.getComponent().getName());
				oldComponent.setApprovalState(component.getComponent().getApprovalState());
				oldComponent.setApprovedUser(component.getComponent().getApprovedUser());
				oldComponent.setDescription(component.getComponent().getDescription());
				oldComponent.setGuid(component.getComponent().getGuid());
				oldComponent.setLastActivityDts(TimeUtil.currentDate());
				oldComponent.setOrganization(component.getComponent().getOrganization());
				oldComponent.setParentComponentId(component.getComponent().getParentComponentId());
				oldComponent.setReleaseDate(component.getComponent().getReleaseDate());
				oldComponent.setActiveStatus(component.getComponent().getActiveStatus());
				oldComponent.setUpdateDts(TimeUtil.currentDate());
				oldComponent.setUpdateUser(component.getComponent().getUpdateUser());
				persistenceService.persist(oldComponent);

				//remove all old attributes
				ComponentAttribute componentAttributeExample = new ComponentAttribute();
				componentAttributeExample.setComponentId(oldComponent.getComponentId());
				persistenceService.deleteByExample(componentAttributeExample);

				//add new attributes; Note: add all attributes at once....other call the other single attribute save
				component.getAttributes().forEach(attribute -> {
					attribute.setComponentId(oldComponent.getComponentId());
					attribute.getComponentAttributePk().setComponentId(oldComponent.getComponentId());
					saveComponentAttribute(attribute, false);
				});

			} else {

				if (StringUtils.isBlank(component.getComponent().getComponentId())) {
					component.getComponent().setComponentId(persistenceService.generateId());
				}
				component.getComponent().setActiveStatus(Component.ACTIVE_STATUS);
				component.getComponent().setCreateDts(TimeUtil.currentDate());
				component.getComponent().setUpdateDts(TimeUtil.currentDate());
				component.getComponent().setLastActivityDts(TimeUtil.currentDate());
				persistenceService.persist(component.getComponent());

				component.getAttributes().forEach(attribute -> {
					attribute.setComponentId(component.getComponent().getComponentId());
					attribute.getComponentAttributePk().setComponentId(component.getComponent().getComponentId());
					attribute.setCreateUser(component.getComponent().getCreateUser());
					attribute.setUpdateUser(component.getComponent().getUpdateUser());
					saveComponentAttribute(attribute, false);
				});
			}
		}
		return component;
	}

	@Override
	public Boolean checkComponentAttribute(ComponentAttribute attribute)
	{
		AttributeCodePk pk = new AttributeCodePk();
		pk.setAttributeCode(attribute.getComponentAttributePk().getAttributeCode());
		pk.setAttributeType(attribute.getComponentAttributePk().getAttributeType());
		if (persistenceService.findById(AttributeCode.class, pk) == null) {
			return Boolean.FALSE;
		}
		if (persistenceService.findById(AttributeType.class, attribute.getComponentAttributePk().getAttributeType()) == null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	public ComponentAll saveFullComponent(ComponentAll componentAll)
	{
		//check component
		Component component = componentAll.getComponent();
		if (StringUtils.isBlank(component.getCreateUser())) {
			component.setCreateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
		}

		if (StringUtils.isBlank(component.getUpdateUser())) {
			component.setUpdateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
		}

		if (StringUtils.isBlank(component.getApprovalState())) {
			component.setApprovalState(Component.APPROVAL_STATE_PENDING);
		}

		if (component.getLastActivityDts() != null) {
			component.setLastActivityDts(TimeUtil.currentDate());
		}

		if (StringUtils.isBlank(component.getGuid())) {
			component.setGuid(persistenceService.generateId());
		}

		//Check Attributes
		ValidationModel validationModel = new ValidationModel(component);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			RequiredForComponent requiredForComponent = new RequiredForComponent();
			requiredForComponent.setComponent(component);
			requiredForComponent.getAttributes().addAll(componentAll.getAttributes());

			//validate attributes
			for (ComponentAttribute componentAttribute : componentAll.getAttributes()) {
				validationModel = new ValidationModel(componentAttribute);
				validationModel.setConsumeFieldsOnly(true);
				validationResult = ValidationUtil.validate(validationModel);
				if (validationResult.valid() == false) {
					throw new OpenStorefrontRuntimeException(validationResult.toString());
				}
			}
			saveComponent(requiredForComponent);
		} else {
			throw new OpenStorefrontRuntimeException(validationResult.toString());
		}

		handleBaseComponetSave(ComponentContact.class, componentAll.getContacts(), component.getComponentId());
		handleBaseComponetSave(ComponentEvaluationSchedule.class, componentAll.getEvaluationSchedules(), component.getComponentId());
		handleBaseComponetSave(ComponentEvaluationSection.class, componentAll.getEvaluationSections(), component.getComponentId());
		handleBaseComponetSave(ComponentExternalDependency.class, componentAll.getExternalDependencies(), component.getComponentId());
		handleBaseComponetSave(ComponentMedia.class, componentAll.getMedia(), component.getComponentId());
		handleBaseComponetSave(ComponentMetadata.class, componentAll.getMetadata(), component.getComponentId());
		handleBaseComponetSave(ComponentResource.class, componentAll.getResources(), component.getComponentId());
		//Thesse are user data and they shouldn't be changed on sync (I'm leave it as a reminder)
//		handleBaseComponetSave(ComponentTag.class, componentAll.getTags(), component.getComponentId());
//		for (QuestionAll question : componentAll.getQuestions()) {
//			List<ComponentQuestion> questions = new ArrayList<>(1);
//			questions.add(question.getQuestion());
//			handleBaseComponetSave(ComponentQuestion.class, questions, component.getComponentId());
//			handleBaseComponetSave(ComponentQuestionResponse.class, question.getResponds(), component.getComponentId());
//		}
//
//		for (ReviewAll reviewAll : componentAll.getReviews()) {
//			List<ComponentReview> reviews = new ArrayList<>(1);
//			reviews.add(reviewAll.getComponentReview());
//			handleBaseComponetSave(ComponentReview.class, reviews, component.getComponentId());
//			handleBaseComponetSave(ComponentReviewPro.class, reviewAll.getPros(), component.getComponentId());
//			handleBaseComponetSave(ComponentReviewCon.class, reviewAll.getCons(), component.getComponentId());
//		}

		if (Component.INACTIVE_STATUS.equals(component.getActiveStatus())) {
			getUserService().removeAllWatchesForComponent(component.getComponentId());
		}

		return componentAll;
	}

	private <T extends BaseComponent> void handleBaseComponetSave(Class<T> baseComponentClass, List<T> baseComponents, String componentId)
	{
		for (BaseComponent baseComponent : baseComponents) {
			baseComponent.setComponentId(componentId);

			//validate
			ValidationModel validationModel = new ValidationModel(baseComponent);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult validationResult = ValidationUtil.validate(validationModel);
			if (validationResult.valid() == false) {
				throw new OpenStorefrontRuntimeException(validationResult.toString());
			}
		}
		//remove old ones
		try {
			T baseComponentExample = baseComponentClass.newInstance();
			baseComponentExample.setComponentId(componentId);
			persistenceService.deleteByExample(baseComponentExample);
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}

		//save new ones
		for (T baseComponent : baseComponents) {
			if (baseComponent instanceof ComponentContact) {
				saveComponentContact((ComponentContact) baseComponent, false);
			} else if (baseComponent instanceof ComponentEvaluationSchedule) {
				saveComponentEvaluationSchedule((ComponentEvaluationSchedule) baseComponent, false);
			} else if (baseComponent instanceof ComponentEvaluationSection) {
				saveComponentEvaluationSection((ComponentEvaluationSection) baseComponent, false);
			} else if (baseComponent instanceof ComponentExternalDependency) {
				saveComponentDependency((ComponentExternalDependency) baseComponent, false);
			} else if (baseComponent instanceof ComponentMedia) {
				saveComponentMedia((ComponentMedia) baseComponent, false);
			} else if (baseComponent instanceof ComponentMetadata) {
				saveComponentMetadata((ComponentMetadata) baseComponent, false);
			} else if (baseComponent instanceof ComponentResource) {
				saveComponentResource((ComponentResource) baseComponent, false);
			} else if (baseComponent instanceof ComponentTag) {
				saveComponentTag((ComponentTag) baseComponent, false);
			} else if (baseComponent instanceof ComponentQuestion) {
				saveComponentQuestion((ComponentQuestion) baseComponent, false);
			} else if (baseComponent instanceof ComponentQuestionResponse) {
				saveComponentQuestionResponse((ComponentQuestionResponse) baseComponent, false);
			} else if (baseComponent instanceof ComponentReview) {
				saveComponentReview((ComponentReview) baseComponent, false);
			} else if (baseComponent instanceof ComponentReviewPro) {
				saveComponentReviewPro((ComponentReviewPro) baseComponent, false);
			} else if (baseComponent instanceof ComponentReviewCon) {
				saveComponentReviewCon((ComponentReviewCon) baseComponent, false);
			} else {
				throw new OpenStorefrontRuntimeException("Save not supported for this base component: " + baseComponent.getClass().getName(), "Add support (Developement task)");
			}
		}
	}

	@Override
	public void cascadeDeleteOfComponent(String componentId)
	{
		Objects.requireNonNull(componentId, "Component Id is required.");
		log.log(Level.INFO, "Attempting to Removing component: " + componentId);

		Collection<Class<?>> entityClasses = DBManager.getConnection().getEntityManager().getRegisteredEntities();
		for (Class entityClass : entityClasses) {
			if (ServiceUtil.BASECOMPONENT_ENTITY.equals(entityClass.getSimpleName()) == false) {
				if (ServiceUtil.isSubClass(ServiceUtil.BASECOMPONENT_ENTITY, entityClass)) {
					try {
						deleteBaseComponent((BaseComponent) entityClass.newInstance(), componentId);
					} catch (InstantiationException | IllegalAccessException ex) {
						throw new OpenStorefrontRuntimeException("Class is not a base component class: " + entityClass.getName(), "Check class");
					}
				}
			}
		}
		Component component = persistenceService.findById(Component.class, componentId);
		persistenceService.delete(component);

		getUserService().removeAllWatchesForComponent(componentId);
	}

	private <T extends BaseComponent> void deleteBaseComponent(T example, String componentId)
	{
		example.setComponentId(componentId);
		persistenceService.deleteByExample(example);
	}

	@Override
	public List<ComponentTag> getTagCloud()
	{
		String query = "select * from ComponentTag where activeStatus='A' GROUP BY text";
		return persistenceService.query(query, null);
	}

	@Override
	public List<ComponentReviewView> getReviewByUser(String username)
	{
		ComponentReview example = new ComponentReview();
		example.setActiveStatus(ComponentReview.ACTIVE_STATUS);
		example.setCreateUser(username);
		List<ComponentReview> tempReviews = persistenceService.queryByExample(ComponentReview.class, new QueryByExample(example));
		List<ComponentReviewView> reviews = new ArrayList();
		tempReviews.forEach(review -> {
			ComponentReviewPro tempPro = new ComponentReviewPro();
			ComponentReviewProPk tempProPk = new ComponentReviewProPk();
			ComponentReviewCon tempCon = new ComponentReviewCon();
			ComponentReviewConPk tempConPk = new ComponentReviewConPk();

			tempProPk.setComponentReviewId(review.getComponentReviewId());
			tempConPk.setComponentReviewId(review.getComponentReviewId());

			tempPro.setComponentReviewProPk(tempProPk);
			tempCon.setComponentReviewConPk(tempConPk);

			ComponentReviewView tempView = ComponentReviewView.toView(review);

			tempView.setPros(ComponentReviewProCon.toViewListPro(persistenceService.queryByExample(ComponentReviewPro.class, new QueryByExample(tempPro))));
			tempView.setCons(ComponentReviewProCon.toViewListCon(persistenceService.queryByExample(ComponentReviewCon.class, new QueryByExample(tempCon))));

			reviews.add(tempView);
		});
		return reviews;
	}

	@Override
	public void saveMediaFile(ComponentMedia media, InputStream fileInput)
	{
		Objects.requireNonNull(media);
		Objects.requireNonNull(fileInput);

		String extension = OpenStorefrontConstant.getFileExtensionForMime(media.getMimeType());
		media.setFileName(media.getMediaTypeCode() + "-" + System.currentTimeMillis() + "-" + extension);

		try (InputStream in = fileInput) {
			Files.copy(in, media.pathToMedia());
			media.setUpdateUser(SecurityUtil.getCurrentUserName());
			saveComponentMedia(media);
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to store media file.", "Contact System Admin.  Check file permissions and disk space ", ex);
		}
	}

	@Override
	public void saveResourceFile(ComponentResource resource, InputStream fileInput)
	{
		Objects.requireNonNull(resource);
		Objects.requireNonNull(fileInput);

		String extension = OpenStorefrontConstant.getFileExtensionForMime(resource.getMimeType());
		resource.setFileName(resource.getResourceType() + "-" + System.currentTimeMillis() + "-" + extension);

		try (InputStream in = fileInput) {
			Files.copy(in, resource.pathToResource());
			resource.setUpdateUser(SecurityUtil.getCurrentUserName());
			saveComponentResource(resource);
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to store resource file.", "Contact System Admin.  Check file permissions and disk space ", ex);
		}
	}

	@Override
	public Boolean setLastViewDts(String componentId, String userId)
	{
		UserWatch example = new UserWatch();
		example.setComponentId(componentId);
		example.setUsername(userId);
		example = persistenceService.queryOneByExample(UserWatch.class, new QueryByExample(example));
		if (example != null) {
			UserWatch watch = persistenceService.findById(UserWatch.class, example.getUserWatchId());
			watch.setLastViewDts(TimeUtil.currentDate());
			persistenceService.persist(watch);
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public List<Component> findRecentlyAdded(int maxResults)
	{
		String query = "select from Component where activeStatus = :activeStatusParam "
				+ " and approvalState = :approvedStateParam "
				+ " order by approvedDts DESC LIMIT " + maxResults;

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("activeStatusParam", Component.ACTIVE_STATUS);
		parameters.put("approvedStateParam", Component.APPROVAL_STATE_APPROVED);

		return persistenceService.query(query, parameters);
	}

	@Override
	public ValidationResult saveDetailReview(ComponentReview review, List<ComponentReviewPro> pros, List<ComponentReviewCon> cons)
	{
		ValidationResult validationResult = new ValidationResult();

		ValidationModel validationModel = new ValidationModel(review);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult reviewResults = ValidationUtil.validate(validationModel);
		validationResult.merge(reviewResults);

		for (ComponentReviewPro reviewPro : pros) {
			ReviewPro proCode = getLookupService().getLookupEnity(ReviewPro.class, reviewPro.getComponentReviewProPk().getReviewPro());
			if (proCode == null) {
				proCode = getLookupService().getLookupEnityByDesc(ReviewPro.class, reviewPro.getComponentReviewProPk().getReviewPro());
				if (proCode == null) {
					reviewPro.getComponentReviewProPk().setReviewPro(null);
				} else {
					reviewPro.getComponentReviewProPk().setReviewPro(proCode.getCode());
				}
			} else {
				reviewPro.getComponentReviewProPk().setReviewPro(proCode.getCode());
			}
			validationModel = new ValidationModel(reviewPro);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult proResults = ValidationUtil.validate(validationModel);
			validationResult.merge(proResults);
		}

		for (ComponentReviewCon reviewCon : cons) {
			ReviewCon conCode = getLookupService().getLookupEnity(ReviewCon.class, reviewCon.getComponentReviewConPk().getReviewCon());
			if (conCode == null) {
				conCode = getLookupService().getLookupEnityByDesc(ReviewCon.class, reviewCon.getComponentReviewConPk().getReviewCon());
				if (conCode == null) {
					reviewCon.getComponentReviewConPk().setReviewCon(null);
				} else {
					reviewCon.getComponentReviewConPk().setReviewCon(conCode.getCode());
				}
			} else {
				reviewCon.getComponentReviewConPk().setReviewCon(conCode.getCode());
			}
			validationModel = new ValidationModel(reviewCon);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult conResults = ValidationUtil.validate(validationModel);
			validationResult.merge(conResults);
		}

		if (validationResult.valid()) {

			review.setActiveStatus(ComponentReview.ACTIVE_STATUS);
			review.setCreateUser(SecurityUtil.getCurrentUserName());
			review.setUpdateUser(SecurityUtil.getCurrentUserName());
			saveComponentReview(review);

			//delete existing pros
			ComponentReviewPro componentReviewProExample = new ComponentReviewPro();
			componentReviewProExample.setComponentId(review.getComponentId());
			persistenceService.deleteByExample(componentReviewProExample);

			for (ComponentReviewPro reviewPro : pros) {
				reviewPro.setComponentId(review.getComponentId());
				reviewPro.getComponentReviewProPk().setComponentReviewId(review.getComponentReviewId());
				reviewPro.setCreateUser(SecurityUtil.getCurrentUserName());
				reviewPro.setUpdateUser(SecurityUtil.getCurrentUserName());
				reviewPro.setActiveStatus(ComponentReviewPro.ACTIVE_STATUS);
				saveComponentReviewPro(reviewPro, false);
			}

			//delete existing cons
			ComponentReviewCon componentReviewConExample = new ComponentReviewCon();
			componentReviewConExample.setComponentId(review.getComponentReviewId());
			persistenceService.deleteByExample(componentReviewConExample);

			for (ComponentReviewCon reviewCon : cons) {
				reviewCon.setComponentId(review.getComponentId());
				reviewCon.getComponentReviewConPk().setComponentReviewId(review.getComponentReviewId());
				reviewCon.setCreateUser(SecurityUtil.getCurrentUserName());
				reviewCon.setUpdateUser(SecurityUtil.getCurrentUserName());
				reviewCon.setActiveStatus(ComponentReviewPro.ACTIVE_STATUS);
				saveComponentReviewCon(reviewCon, false);
			}

		}

		return validationResult;
	}

}
