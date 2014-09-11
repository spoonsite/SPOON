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
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
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
import edu.usu.sdl.openstorefront.storage.model.UserWatch;
import edu.usu.sdl.openstorefront.util.TimeUtil;
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
import edu.usu.sdl.openstorefront.web.rest.model.ComponentReviewView;
import edu.usu.sdl.openstorefront.web.rest.model.RequiredForComponent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 * @author jlaw
 */
public class ComponentServiceImpl
		extends ServiceProxy
		implements ComponentService
{

	private static final Logger log = Logger.getLogger(ComponentServiceImpl.class.getName());

	public ComponentServiceImpl()
	{
	}

	@Override
	public <T extends BaseComponent> List<T> getBaseComponent(Class<T> subComponentClass, String componentId)
	{
		return getBaseComponent(subComponentClass, componentId, false);
	}

	@Override
	public <T extends BaseComponent> List<T> getBaseComponent(Class<T> subComponentClass, String componentId, boolean all)
	{
		try {
			T baseComponentExample = subComponentClass.newInstance();
			baseComponentExample.setComponentId(componentId);
			if (all == false) {
				baseComponentExample.setActiveStatus(BaseComponent.ACTIVE_STATUS);
			}
			return persistenceService.queryByExample(subComponentClass, new QueryByExample(baseComponentExample));
		}
		catch (InstantiationException | IllegalAccessException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
	}

	@Override
	public <T extends BaseComponent> T deactivateBaseComponent(Class<T> subComponentClass, Object pk)
	{
		return deactivateBaseComponent(subComponentClass, pk, false);
	}

	@Override
	public <T extends BaseComponent> T deactivateBaseComponent(Class<T> subComponentClass, Object pk, boolean all)
	{
		T found = persistenceService.findById(subComponentClass, pk);
		if (found != null) {
			found.setActiveStatus(T.INACTIVE_STATUS);
			persistenceService.persist(found);
		}
		return found;
	}

	@Override
	public <T extends BaseComponent> void deleteBaseComponent(Class<T> subComponentClass, String componentId)
	{
		deleteBaseComponent(subComponentClass, componentId, false);
	}

	@Override
	public <T extends BaseComponent> void deleteBaseComponent(Class<T> subComponentClass, String componentId, Boolean all)
	{
		try {
			T example = subComponentClass.newInstance();
			example.setComponentId(componentId);
			persistenceService.deleteByExample(example);
		}
		catch (InstantiationException | IllegalAccessException ex) {
			Logger.getLogger(ComponentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public List<Component> getComponents()
	{
		Component componentExample = new Component();
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);
		return persistenceService.queryByExample(Component.class, new QueryByExample(componentExample));
	}

	@Override
	public ComponentDetailView getComponentDetails(String componentId)
	{

		ComponentDetailView result = new ComponentDetailView();
		Component tempComponent = persistenceService.findById(Component.class, componentId);
		Component tempParentComponent;
		if (tempComponent != null && tempComponent.getParentComponentId() != null) {
			tempParentComponent = persistenceService.findById(Component.class, tempComponent.getParentComponentId());
		}
		else {
			tempParentComponent = new Component();
		}
		result.setComponentDetails(tempComponent, tempParentComponent);

		UserWatch tempWatch = new UserWatch();
		// TODO: take this out of the comments once we're in production.
		//tempWatch.setUsername(ServiceUtil.getCurrentUserName());
		tempWatch.setActiveStatus(UserWatch.ACTIVE_STATUS);
		tempWatch.setComponentId(componentId);
		UserWatch tempUserWatch = persistenceService.queryByOneExample(UserWatch.class, new QueryByExample(tempWatch));
		if (tempUserWatch != null) {
			result.setLastViewedDts(tempUserWatch.getLastViewDts());
		}
		List<ComponentAttribute> attributes = this.getAttributeService().getAttributesByComponentId(componentId);
		result.setAttributes(ComponentAttributeView.toViewList(attributes));

		result.setLastActivityDts(tempComponent.getLastActivityDts());

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

		// Here we grab the pros and cons for the reviews.
		List<ComponentReview> tempReviews = getBaseComponent(ComponentReview.class, componentId);
		List<ComponentReviewView> reviews = new ArrayList();
		tempReviews.forEach(review -> {
			ComponentReviewPro tempPro = new ComponentReviewPro();

			ComponentReviewProPk tempProPk = new ComponentReviewProPk();
			tempProPk.setComponentReviewId(review.getComponentReviewId());
			tempPro.setComponentReviewProPk(tempProPk);

			ComponentReviewCon tempCon = new ComponentReviewCon();

			ComponentReviewConPk tempConPk = new ComponentReviewConPk();
			tempConPk.setComponentReviewId(review.getComponentReviewId());
			tempCon.setComponentReviewConPk(tempConPk);

			ComponentReviewView tempView = ComponentReviewView.toView(review);
			tempView.setPros(persistenceService.queryByExample(ComponentReviewPro.class, new QueryByExample(tempPro)));
			tempView.setCons(persistenceService.queryByExample(ComponentReviewCon.class, new QueryByExample(tempPro)));
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
		AttributeType type = persistenceService.findById(AttributeType.class, attribute.getComponentAttributePk().getAttributeType());
		AttributeCodePk pk = new AttributeCodePk();
		pk.setAttributeCode(attribute.getComponentAttributePk().getAttributeCode());
		pk.setAttributeType(attribute.getComponentAttributePk().getAttributeType());
		AttributeCode code = persistenceService.findById(AttributeCode.class, pk);
		if (type != null && code != null) {
			ComponentAttribute oldAttribute = persistenceService.findById(ComponentAttribute.class, attribute.getComponentAttributePk());
			if (oldAttribute != null) {
				oldAttribute.setActiveStatus(attribute.getActiveStatus());
				oldAttribute.setComponentAttributePk(attribute.getComponentAttributePk());
				oldAttribute.setUpdateDts(TimeUtil.currentDate());
				persistenceService.persist(oldAttribute);
			}
			else {
				ComponentAttribute example = new ComponentAttribute();
				example.setComponentAttributePk(new ComponentAttributePk());
				example.getComponentAttributePk().setAttributeType(attribute.getComponentAttributePk().getAttributeType());
				ComponentAttribute test = persistenceService.queryByOneExample(ComponentAttribute.class, new QueryByExample(example));
				if ((test == null) || (type.getAllowMutlipleFlg() && !code.getAttributeCodePk().getAttributeCode().equals(attribute.getComponentAttributePk().getAttributeCode()))) {
					attribute.setCreateDts(TimeUtil.currentDate());
					attribute.setUpdateDts(TimeUtil.currentDate());
					persistenceService.persist(attribute);
				}
			}
		}

	}

	@Override
	public void saveComponentContact(ComponentContact contact)
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
		}
		else {
			contact.setContactId(persistenceService.generateId());
			contact.setCreateDts(TimeUtil.currentDate());
			contact.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(contact);
		}
	}

	@Override
	public void saveComponentDependency(ComponentExternalDependency dependency)
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
		}
		else {
			dependency.setDependencyId(persistenceService.generateId());
			dependency.setCreateDts(TimeUtil.currentDate());
			dependency.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(dependency);
		}
	}

	@Override
	public void saveComponentEvaluationSection(ComponentEvaluationSection section)
	{
		ComponentEvaluationSection oldSection = persistenceService.findById(ComponentEvaluationSection.class, section.getComponentEvaluationSectionPk());
		if (oldSection != null) {
			oldSection.setActiveStatus(section.getActiveStatus());
			oldSection.setScore(section.getScore());
			oldSection.setUpdateDts(TimeUtil.currentDate());
			oldSection.setUpdateUser(section.getUpdateUser());
			persistenceService.persist(oldSection);
		}
		else {
			section.setCreateDts(TimeUtil.currentDate());
			section.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(section);
		}
	}

	@Override
	public void saveComponentEvaluationSchedule(ComponentEvaluationSchedule schedule)
	{
		ComponentEvaluationSchedule oldSchedule = persistenceService.findById(ComponentEvaluationSchedule.class, schedule.getComponentEvaluationSchedulePk());
		if (oldSchedule != null) {
			oldSchedule.setCompletionDate(schedule.getCompletionDate());
			oldSchedule.setLevelStatus(schedule.getLevelStatus());
			oldSchedule.setActiveStatus(schedule.getActiveStatus());
			oldSchedule.setUpdateDts(TimeUtil.currentDate());
			oldSchedule.setUpdateUser(schedule.getUpdateUser());
			persistenceService.persist(oldSchedule);
		}
		else {
			schedule.getComponentEvaluationSchedulePk().setComponentId(schedule.getComponentId());
			schedule.setCreateDts(TimeUtil.currentDate());
			schedule.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(schedule);
		}
	}

	@Override
	public void saveComponentMedia(ComponentMedia media)
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
		}
		else {
			media.setComponentMediaId(persistenceService.generateId());
			media.setCreateDts(TimeUtil.currentDate());
			media.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(media);
		}
	}

	@Override
	public void saveComponentMetadata(ComponentMetadata metadata)
	{
		ComponentMetadata oldMetadata = persistenceService.findById(ComponentMetadata.class, metadata.getMetadataId());
		if (oldMetadata != null) {
			oldMetadata.setLabel(metadata.getLabel());
			oldMetadata.setValue(metadata.getValue());
			oldMetadata.setActiveStatus(metadata.getActiveStatus());
			oldMetadata.setUpdateDts(TimeUtil.currentDate());
			oldMetadata.setUpdateUser(metadata.getUpdateUser());
			persistenceService.persist(oldMetadata);
		}
		else {
			metadata.setMetadataId(persistenceService.generateId());
			metadata.setCreateDts(TimeUtil.currentDate());
			metadata.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(metadata);
		}
	}

	@Override
	public void saveComponentQuestion(ComponentQuestion question)
	{
		ComponentQuestion oldQuestion = persistenceService.findById(ComponentQuestion.class, question.getQuestionId());
		if (oldQuestion != null) {
			oldQuestion.setOrganization(question.getOrganization());
			oldQuestion.setQuestion(question.getQuestion());
			oldQuestion.setUserType(question.getUserType());
			oldQuestion.setActiveStatus(question.getActiveStatus());
			oldQuestion.setUpdateDts(TimeUtil.currentDate());
			oldQuestion.setUpdateUser(question.getUpdateUser());
			persistenceService.persist(oldQuestion);
		}
		else {
			question.setQuestionId(persistenceService.generateId());
			question.setCreateDts(TimeUtil.currentDate());
			question.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(question);
		}
	}

	@Override
	public void saveComponentQuestionResponse(ComponentQuestionResponse response)
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
		}
		else {
			response.setResponseId(persistenceService.generateId());
			response.setCreateDts(TimeUtil.currentDate());
			response.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(response);
		}
	}

	@Override
	public void saveComponentResource(ComponentResource resource)
	{
		ComponentResource oldResource = persistenceService.findById(ComponentResource.class, resource.getResourceId());
		if (oldResource != null) {
			oldResource.setDescription(resource.getDescription());
			oldResource.setLink(resource.getLink());
			oldResource.setName(resource.getName());
			oldResource.setResourceFileId(resource.getResourceFileId());
			oldResource.setResourceType(resource.getResourceType());
			oldResource.setRestricted(resource.getRestricted());
			oldResource.setActiveStatus(resource.getActiveStatus());
			oldResource.setUpdateDts(TimeUtil.currentDate());
			oldResource.setUpdateUser(resource.getUpdateUser());
			persistenceService.persist(oldResource);
		}
		else {
			resource.setResourceId(persistenceService.generateId());
			resource.setCreateDts(TimeUtil.currentDate());
			resource.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(resource);
		}
	}

	@Override
	public void saveComponentReview(ComponentReview review)
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
		}
		else {
			review.setComponentReviewId(persistenceService.generateId());
			review.setCreateDts(TimeUtil.currentDate());
			review.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(review);
		}
	}

	@Override
	public void saveComponentReviewCon(ComponentReviewCon con)
	{
		ComponentReviewCon oldCon = persistenceService.findById(ComponentReviewCon.class, con.getComponentReviewConPk());
		if (oldCon != null) {
			oldCon.setText(con.getText());
			oldCon.getComponentReviewConPk().setReviewCon(con.getText());
			oldCon.setActiveStatus(con.getActiveStatus());
			oldCon.setUpdateDts(TimeUtil.currentDate());
			oldCon.setUpdateUser(con.getUpdateUser());
			persistenceService.persist(oldCon);
		}
		else {
			con.setCreateDts(TimeUtil.currentDate());
			con.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(con);
		}
	}

	@Override
	public void saveComponentReviewPro(ComponentReviewPro pro)
	{
		ComponentReviewPro oldPro = persistenceService.findById(ComponentReviewPro.class, pro.getComponentReviewProPk());
		if (oldPro != null) {
			oldPro.setText(pro.getText());
			oldPro.getComponentReviewProPk().setReviewPro(pro.getText());
			oldPro.setActiveStatus(pro.getActiveStatus());
			oldPro.setUpdateDts(TimeUtil.currentDate());
			oldPro.setUpdateUser(pro.getUpdateUser());
			persistenceService.persist(oldPro);
		}
		else {
			pro.setCreateDts(TimeUtil.currentDate());
			pro.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(pro);
		}
	}

	@Override
	public void saveComponentTag(ComponentTag tag)
	{
		ComponentTag oldTag = persistenceService.findById(ComponentTag.class, tag.getTagId());
		if (oldTag != null) {
			oldTag.setText(tag.getText());
			oldTag.setActiveStatus(tag.getActiveStatus());
			oldTag.setUpdateDts(TimeUtil.currentDate());
			oldTag.setUpdateUser(tag.getUpdateUser());
			persistenceService.persist(oldTag);
		}
		else {
			tag.setTagId(persistenceService.generateId());
			tag.setCreateDts(TimeUtil.currentDate());
			tag.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(tag);
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
		}
		else {
			tracking.setComponentTrackingId(persistenceService.generateId());
			tracking.setCreateDts(TimeUtil.currentDate());
			tracking.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(tracking);
		}
	}

	@Override
	public RequiredForComponent saveComponent(RequiredForComponent component)
	{
		//uncomment this to remove all of the components to start clean
		// Component temp = new Component();
		// temp.setActiveStatus(Component.ACTIVE_STATUS);
		// persistenceService.deleteByExample(temp);
		// return component;
		Component oldComponent = new Component();
		oldComponent = persistenceService.findById(Component.class, component.getComponent().getComponentId());

		//TODO: Validate attribute (type, codes)
		if (oldComponent == null && component.checkForComplete()) {

			//new
			component.getComponent().setComponentId(persistenceService.generateId());
			component.getComponent().setActiveStatus(Component.ACTIVE_STATUS);
			component.getComponent().setCreateDts(TimeUtil.currentDate());
			component.getComponent().setUpdateDts(TimeUtil.currentDate());
			component.getComponent().setLastActivityDts(TimeUtil.currentDate());
			persistenceService.persist(component.getComponent());

			component.getAttributes().forEach(attribute -> {
				attribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
				ComponentAttributePk pk = new ComponentAttributePk();
				pk.setAttributeCode(attribute.getComponentAttributePk().getAttributeCode());
				pk.setAttributeType(attribute.getComponentAttributePk().getAttributeType());
				pk.setComponentId(component.getComponent().getComponentId());
				attribute.setComponentAttributePk(pk);
				attribute.setComponentId(component.getComponent().getComponentId());
				attribute.setCreateUser(component.getComponent().getCreateUser());
				attribute.setUpdateUser(component.getComponent().getUpdateUser());
				attribute.setCreateDts(TimeUtil.currentDate());
				attribute.setUpdateDts(TimeUtil.currentDate());
				persistenceService.persist(attribute);
			});
			return component;
		}
		else if (component.checkForComplete()) {
			//FIXME: Finish the Update 
			oldComponent.setName(component.getComponent().getName());
			oldComponent.setApprovalState(component.getComponent().getApprovalState());
			oldComponent.setApprovedUser(component.getComponent().getApprovedUser());
			oldComponent.setDescription(component.getComponent().getDescription());
			oldComponent.setGuid(component.getComponent().getGuid());
			oldComponent.setComponentId(component.getComponent().getComponentId());
			oldComponent.setLastActivityDts(TimeUtil.currentDate());
			oldComponent.setOrganization(component.getComponent().getOrganization());
			oldComponent.setParentComponentId(component.getComponent().getParentComponentId());
			oldComponent.setReleaseDate(component.getComponent().getReleaseDate());
			oldComponent.setActiveStatus(component.getComponent().getActiveStatus());
			oldComponent.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(oldComponent);
			return component;
		}
		return null;
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

}
