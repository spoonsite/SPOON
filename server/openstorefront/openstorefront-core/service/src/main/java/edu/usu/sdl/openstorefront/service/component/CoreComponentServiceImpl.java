/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.orientechnologies.orient.core.record.impl.ODocument;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.LockSwitch;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOptionBuilder;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.AlertType;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.BaseComponent;
import edu.usu.sdl.openstorefront.core.entity.ChangeLog;
import edu.usu.sdl.openstorefront.core.entity.ChangeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.ComponentEvaluationSection;
import edu.usu.sdl.openstorefront.core.entity.ComponentExternalDependency;
import edu.usu.sdl.openstorefront.core.entity.ComponentIntegration;
import edu.usu.sdl.openstorefront.core.entity.ComponentIntegrationConfig;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.ComponentMetadata;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.entity.ComponentRelationship;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewCon;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewConPk;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewPro;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewProPk;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeTemplate;
import edu.usu.sdl.openstorefront.core.entity.ComponentUpdateQueue;
import edu.usu.sdl.openstorefront.core.entity.ComponentVersionHistory;
import edu.usu.sdl.openstorefront.core.entity.Evaluation;
import edu.usu.sdl.openstorefront.core.entity.FileDataMap;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.entity.ModificationType;
import edu.usu.sdl.openstorefront.core.entity.TemplateBlock;
import edu.usu.sdl.openstorefront.core.entity.TemporaryMedia;
import edu.usu.sdl.openstorefront.core.entity.TrackEventCode;
import edu.usu.sdl.openstorefront.core.entity.UserMessage;
import edu.usu.sdl.openstorefront.core.entity.UserMessageType;
import edu.usu.sdl.openstorefront.core.entity.UserWatch;
import edu.usu.sdl.openstorefront.core.filter.ComponentSensitivityModel;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.core.model.AlertContext;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.ComponentDeleteOptions;
import edu.usu.sdl.openstorefront.core.model.ComponentRestoreOptions;
import edu.usu.sdl.openstorefront.core.model.EvaluationAll;
import edu.usu.sdl.openstorefront.core.model.IntegrationAll;
import edu.usu.sdl.openstorefront.core.model.QuestionAll;
import edu.usu.sdl.openstorefront.core.model.ReviewAll;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.sort.SortUtil;
import edu.usu.sdl.openstorefront.core.util.EntityUtil;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminView;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminWrapper;
import edu.usu.sdl.openstorefront.core.view.ComponentAttributeView;
import edu.usu.sdl.openstorefront.core.view.ComponentContactView;
import edu.usu.sdl.openstorefront.core.view.ComponentDetailView;
import edu.usu.sdl.openstorefront.core.view.ComponentEvaluationView;
import edu.usu.sdl.openstorefront.core.view.ComponentExternalDependencyView;
import edu.usu.sdl.openstorefront.core.view.ComponentFilterParams;
import edu.usu.sdl.openstorefront.core.view.ComponentMediaView;
import edu.usu.sdl.openstorefront.core.view.ComponentMetadataView;
import edu.usu.sdl.openstorefront.core.view.ComponentQuestionResponseView;
import edu.usu.sdl.openstorefront.core.view.ComponentQuestionView;
import edu.usu.sdl.openstorefront.core.view.ComponentRelationshipView;
import edu.usu.sdl.openstorefront.core.view.ComponentResourceView;
import edu.usu.sdl.openstorefront.core.view.ComponentReviewProCon;
import edu.usu.sdl.openstorefront.core.view.ComponentReviewView;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.ComponentTrackingCompleteWrapper;
import edu.usu.sdl.openstorefront.core.view.ComponentTrackingResult;
import edu.usu.sdl.openstorefront.core.view.ComponentView;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.core.view.RequiredForComponent;
import edu.usu.sdl.openstorefront.core.view.statistic.ComponentRecordStatistic;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.ComponentServiceImpl;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;
import net.java.truevfs.access.TFileWriter;
import net.java.truevfs.access.TPath;
import net.java.truevfs.access.TVFS;
import net.java.truevfs.kernel.spec.FsSyncException;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Handles the basic
 *
 * @author dshurtleff
 */
public class CoreComponentServiceImpl
		extends BaseComponentServiceImpl
{

	private static final Logger LOG = Logger.getLogger(CoreComponentServiceImpl.class.getName());

	private static final String COPY_MARKER = "- COPY";

	public CoreComponentServiceImpl(ComponentServiceImpl componentService)
	{
		super(componentService);
	}

	public void deactivateComponent(String componentId)
	{
		doDeactivateComponent(componentId);
		cleanupCache(componentId);
		componentService.getUserService().removeAllWatchesForComponent(componentId);
		componentService.getSearchService().deleteById(componentId);
	}

	public void doDeactivateComponent(String componentId)
	{
		Component component = persistenceService.findById(Component.class, componentId);
		if (component != null) {
			component.setActiveStatus(Component.INACTIVE_STATUS);
			component.setUpdateDts(TimeUtil.currentDate());
			component.setUpdateUser(SecurityUtil.getCurrentUserName());
			persistenceService.persist(component);
			componentService.getUserService().removeAllWatchesForComponent(componentId);
			cleanupCache(componentId);
		}
	}

	public Component activateComponent(String componentId)
	{
		Component component = persistenceService.findById(Component.class, componentId);
		if (component != null) {
			component.setActiveStatus(Component.ACTIVE_STATUS);
			component.setUpdateDts(TimeUtil.currentDate());
			component.setUpdateUser(SecurityUtil.getCurrentUserName());
			persistenceService.persist(component);

			cleanupCache(componentId);
			List<Component> componentsToIndex = new ArrayList<>();
			componentsToIndex.add(component);
			componentService.getSearchService().indexComponents(componentsToIndex);
		}
		return component;
	}

	public String getComponentName(String componentId)
	{
		String componentName = null;
		Element element = OSFCacheManager.getComponentLookupCache().get(componentId);
		if (element != null) {
			componentName = (String) element.getObjectValue();
		} else {
			String query = "select componentId, name from " + Component.class.getSimpleName();
			List<ODocument> documents = persistenceService.query(query, null);
			for (ODocument document : documents) {
				Element newElement = new Element(document.field("componentId"), document.field("name"));
				if (document.field("componentId").equals(componentId)) {
					componentName = (String) document.field("name");
				}
				OSFCacheManager.getComponentLookupCache().put(newElement);
			}
		}
		return componentName;
	}

	public String getComponentType(String componentId)
	{
		String componentType = null;
		Element element = OSFCacheManager.getComponentTypeComponentCache().get(componentId);
		if (element != null) {
			componentType = (String) element.getObjectValue();
		} else {
			String query = "select componentId, componentType from " + Component.class.getSimpleName();
			List<ODocument> documents = persistenceService.query(query, null);
			for (ODocument document : documents) {
				Element newElement = new Element(document.field("componentId"), document.field("componentType"));
				if (document.field("componentId").equals(componentId)) {
					componentType = (String) document.field("componentType");
				}
				OSFCacheManager.getComponentTypeComponentCache().put(newElement);
			}
		}
		return componentType;
	}

	public List<ComponentSearchView> getComponents()
	{
		List<ComponentSearchView> componentSearchViews = new ArrayList<>();

		String query = "select componentId from " + Component.class.getSimpleName()
				+ " where activeStatus='" + Component.ACTIVE_STATUS
				+ "' and approvalState='" + ApprovalStatus.APPROVED + "'";

		List<ODocument> documents = persistenceService.query(query, null);

		List<String> componentIds = new ArrayList<>();
		for (ODocument document : documents) {
			componentIds.add(document.field("componentId"));
		}
		componentSearchViews = getSearchComponentList(componentIds);

		return componentSearchViews;
	}

	public ComponentDetailView getComponentDetails(String componentId, String evaluationId)
	{
		EvaluationAll currentEvaluation = componentService.getEvaluationService().getEvaluation(evaluationId, true);
		ComponentDetailView componentDetailView = getComponentDetails(componentId);
		List<EvaluationAll> existingEvaluations;

		// If the evaluation is not published, use the origin component Id
		if (!currentEvaluation.getEvaluation().getPublished()) {
			componentId = componentService.getEvaluationService().getEvaluation(evaluationId).getEvaluation().getOriginComponentId();
			existingEvaluations = componentService.getEvaluationService().getPublishEvaluations(componentId);
		} else {
			existingEvaluations = componentDetailView.getFullEvaluations();
		}

		// set current evaluation as the first in the list
		existingEvaluations.removeIf(obj -> obj.getEvaluation().getEvaluationId().equals(evaluationId));
		existingEvaluations.add(0, currentEvaluation);
		componentDetailView.setFullEvaluations(existingEvaluations);

		return componentDetailView;
	}

	public ComponentDetailView getComponentDetails(String componentId)
	{

		ComponentDetailView result = new ComponentDetailView();
		Component tempComponent = persistenceService.findById(Component.class, componentId);
		tempComponent = FilterEngine.filter(tempComponent);

		if (tempComponent == null) {
			return null;
		}

		result.setApprovalState(tempComponent.getApprovalState());

		//Pull relationships direct relationships
		ComponentRelationship componentRelationshipExample = new ComponentRelationship();
		componentRelationshipExample.setActiveStatus(ComponentRelationship.ACTIVE_STATUS);
		componentRelationshipExample.setComponentId(componentId);
		List<ComponentRelationship> directRelationships = componentRelationshipExample.findByExample();
		directRelationships = FilterEngine.filter(directRelationships, true);

		result.getRelationships().addAll(ComponentRelationshipView.toViewList(directRelationships));
		result.setRelationships(result.getRelationships().stream().filter(r -> r.getTargetApproved()).collect(Collectors.toList()));

		//Pull indirect
		componentRelationshipExample = new ComponentRelationship();
		componentRelationshipExample.setActiveStatus(ComponentRelationship.ACTIVE_STATUS);
		componentRelationshipExample.setRelatedComponentId(componentId);

		List<ComponentRelationship> inDirectRelationships = componentRelationshipExample.findByExample();
		inDirectRelationships = FilterEngine.filter(inDirectRelationships, true);

		List<ComponentRelationshipView> relationshipViews = ComponentRelationshipView.toViewList(inDirectRelationships);
		relationshipViews = relationshipViews.stream().filter(r -> r.getOwnerApproved()).collect(Collectors.toList());
		result.getRelationships().addAll(relationshipViews);

		UserWatch tempWatch = new UserWatch();
		tempWatch.setUsername(SecurityUtil.getCurrentUserName());
		tempWatch.setActiveStatus(UserWatch.ACTIVE_STATUS);
		tempWatch.setComponentId(componentId);
		UserWatch tempUserWatch = persistenceService.queryOneByExample(new QueryByExample(tempWatch));
		if (tempUserWatch != null) {
			result.setLastViewedDts(tempUserWatch.getLastViewDts());
		}
		List<ComponentAttribute> attributes = componentService.getAttributesByComponentId(componentId);
		result.setAttributes(ComponentAttributeView.toViewList(attributes));

		result.setComponentId(componentId);
		result.setTags(componentService.getBaseComponent(ComponentTag.class, componentId));

		List<ComponentResource> componentResources = componentService.getBaseComponent(ComponentResource.class, componentId);

		componentResources = SortUtil.sortComponentResource(componentResources);
		componentResources.forEach(resource
				-> {
			result.getResources().add(ComponentResourceView.toView(resource));
		});

		List<ComponentMetadata> componentMetadata = componentService.getBaseComponent(ComponentMetadata.class, componentId);
		componentMetadata.forEach(metadata
				-> {
			result.getMetadata().add(ComponentMetadataView.toView(metadata));
		});

		List<ComponentMedia> componentMedia = componentService.getBaseComponent(ComponentMedia.class, componentId);
		componentMedia.forEach(media
				-> {
			result.getComponentMedia().add(ComponentMediaView.toView(media));
		});

		List<ComponentExternalDependency> componentDependency = componentService.getBaseComponent(ComponentExternalDependency.class, componentId);
		componentDependency.forEach(dependency
				-> {
			result.getDependencies().add(ComponentExternalDependencyView.toView(dependency));
		});

		List<ComponentContact> componentContact = componentService.getBaseComponent(ComponentContact.class, componentId);
		componentContact.forEach(contact
				-> {
			result.getContacts().add(ComponentContactView.toView(contact));
		});

		ComponentTracking componentTrackingExample = new ComponentTracking();
		componentTrackingExample.setActiveStatus(ComponentTracking.ACTIVE_STATUS);
		componentTrackingExample.setTrackEventTypeCode(TrackEventCode.VIEW);
		componentTrackingExample.setComponentId(componentId);
		result.setComponentViews(persistenceService.countByExample(componentTrackingExample));

		List<ComponentReview> tempReviews = new ArrayList();
		List<ComponentReview> tempApprovedReviews = componentService.getBaseComponent(ComponentReview.class, componentId);
		List<ComponentReview> tempPendingReviews = componentService.getBaseComponent(ComponentReview.class, componentId, ComponentReview.PENDING_STATUS);
		String currentUser = SecurityUtil.getCurrentUserName();
		tempPendingReviews.forEach(review
				-> {
			if (review.getCreateUser().equals(currentUser)) {
				tempReviews.add(review);
			}
		});
		tempReviews.addAll(tempApprovedReviews);
		List<ComponentReviewView> reviews = new ArrayList();
		tempReviews.forEach(review
				-> {
			ComponentReviewPro tempPro = new ComponentReviewPro();
			ComponentReviewProPk tempProPk = new ComponentReviewProPk();
			ComponentReviewCon tempCon = new ComponentReviewCon();
			ComponentReviewConPk tempConPk = new ComponentReviewConPk();

			tempProPk.setComponentReviewId(review.getComponentReviewId());
			tempConPk.setComponentReviewId(review.getComponentReviewId());

			tempPro.setComponentReviewProPk(tempProPk);
			tempCon.setComponentReviewConPk(tempConPk);

			ComponentReviewView tempView = ComponentReviewView.toView(review);

			tempView.setPros(ComponentReviewProCon.toViewListPro(persistenceService.queryByExample(new QueryByExample(tempPro))));
			tempView.setCons(ComponentReviewProCon.toViewListCon(persistenceService.queryByExample(new QueryByExample(tempCon))));

			reviews.add(tempView);
		});
		reviews.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_DESCENDING, ComponentReviewView.UPDATE_DATE_FIELD));
		result.setReviews(reviews);

		// Here we grab the responses to each question
		List<ComponentQuestionView> questionViews = new ArrayList<>();
		List<ComponentQuestion> questions = componentService.getBaseComponent(ComponentQuestion.class, componentId);
		List<ComponentQuestion> pendingQuestions = componentService.getBaseComponent(ComponentQuestion.class, componentId, ComponentQuestion.PENDING_STATUS);
		pendingQuestions.forEach(question
				-> {
			if (question.getCreateUser().equals(currentUser)) {
				questions.add(question);
			}
		});
		questions.stream().forEach((question)
				-> {
			ComponentQuestionResponse tempResponse = new ComponentQuestionResponse();
			List<ComponentQuestionResponseView> responseViews;
			tempResponse.setQuestionId(question.getQuestionId());
			tempResponse.setActiveStatus(ComponentQuestionResponse.ACTIVE_STATUS);
			List<ComponentQuestionResponse> activeResponses = tempResponse.findByExample();
			activeResponses = FilterEngine.filter(activeResponses);

			ComponentQuestionResponse tempPendingResponse = new ComponentQuestionResponse();
			tempPendingResponse.setQuestionId(question.getQuestionId());
			tempPendingResponse.setActiveStatus(ComponentQuestionResponse.PENDING_STATUS);
			tempPendingResponse.setCreateUser(currentUser);

			List<ComponentQuestionResponse> responses = FilterEngine.filter(tempPendingResponse.findByExample());
			responses.addAll(activeResponses);

			responseViews = ComponentQuestionResponseView.toViewList(responses);
			questionViews.add(ComponentQuestionView.toView(question, responseViews));
		});
		result.setQuestions(questionViews);

		List<ComponentEvaluationSection> evaluationSections = componentService.getBaseComponent(ComponentEvaluationSection.class, componentId);
		result.setEvaluation(ComponentEvaluationView.toViewFromStorage(evaluationSections));

		List<EvaluationAll> publicEvaluations = componentService.getEvaluationService().getPublishEvaluations(componentId);
		result.setFullEvaluations(publicEvaluations);

		result.setToday(new Date());

		result.setComponentDetails(tempComponent);
		return result;
	}

	public void saveComponentTracking(ComponentTracking tracking)
	{
		ComponentTracking oldTracking = persistenceService.findById(ComponentTracking.class, tracking.getComponentTrackingId());
		if (oldTracking != null) {
			oldTracking.setClientIp(tracking.getClientIp());
			oldTracking.setEventDts(tracking.getEventDts());
			oldTracking.setTrackEventTypeCode(tracking.getTrackEventTypeCode());
			oldTracking.setActiveStatus(tracking.getActiveStatus());
			oldTracking.setResourceLink(tracking.getResourceLink());
			oldTracking.setResourceType(tracking.getResourceType());
			oldTracking.setRestrictedResouce(tracking.getRestrictedResouce());
			oldTracking.populateBaseUpdateFields();
			persistenceService.persist(oldTracking);
		} else {
			tracking.populateBaseCreateFields();
			tracking.setComponentTrackingId(persistenceService.generateId());
			persistenceService.persist(tracking);
		}
	}

	public RequiredForComponent saveComponent(RequiredForComponent component)
	{
		componentService.getComponentServicePrivate().doSaveComponent(component);
		updateComponentLastActivity(component.getComponent().getComponentId());

		return component;
	}

	/**
	 * This skips the external component duplication check. Don't use for
	 * External Data Imports
	 *
	 * @param component
	 * @return
	 */
	public RequiredForComponent doSaveComponent(RequiredForComponent component)
	{
		FileHistoryOption option = new FileHistoryOption();
		option.setSkipDuplicationCheck(Boolean.TRUE);
		return doSaveComponent(component, option);
	}

	/**
	 * This try to locate the component; use for duplicate protection.
	 *
	 * @param componentToLookFor
	 * @return component or null if not found
	 */
	private Component findExistingComponent(Component componentToLookFor)
	{
		Component oldComponent = persistenceService.findById(Component.class, componentToLookFor.getComponentId());

		if (StringUtils.isNotBlank(componentToLookFor.getPendingChangeId())) {
			//Change request; only check for id
			return oldComponent;
		}

		//Duplicate protection; check External id
		if (oldComponent == null && StringUtils.isNotBlank(componentToLookFor.getExternalId())) {
			Component componentCheck = new Component();
			componentCheck.setExternalId(componentToLookFor.getExternalId());
			oldComponent = componentCheck.findProxy();
			if (oldComponent != null && StringUtils.isNotBlank(oldComponent.getPendingChangeId())) {
				//ignore change request
				oldComponent = null;
			}
		}

		//check name
		if (oldComponent == null && StringUtils.isNotBlank(componentToLookFor.getName())) {
			Component componentCheck = new Component();
			componentCheck.setName(componentToLookFor.getName());
			oldComponent = componentCheck.findProxy();
			if (oldComponent != null && StringUtils.isNotBlank(oldComponent.getPendingChangeId())) {
				//ignore change request
				oldComponent = null;
			}
		}
		return oldComponent;
	}

	public RequiredForComponent doSaveComponent(RequiredForComponent component, FileHistoryOption options)
	{
		Objects.requireNonNull(component);
		Objects.requireNonNull(options, "Options are required; pass new one for defaults");

		Component oldComponent = null;
		if (Convert.toBoolean(options.getSkipDuplicationCheck()) == false) {
			oldComponent = findExistingComponent(component.getComponent());
		} else {
			//our id lookup only
			oldComponent = persistenceService.findById(Component.class, component.getComponent().getComponentId());
		}

		EntityUtil.setDefaultsOnFields(component.getComponent());

		ValidationResult validationResult = new ValidationResult();
		Component attachedComponent = null;
		if (Convert.toBoolean(options.getSkipRequiredAttributes()) == false) {
			validationResult = component.checkForComplete();
		}
		if (validationResult.valid()) {
			boolean approved = false;
			if (oldComponent != null) {

				//Set id as if may not have been set
				component.getComponent().setComponentId(oldComponent.getComponentId());

				if (component.getComponent().compareTo(oldComponent) != 0) {

					if ((ApprovalStatus.PENDING.equals(oldComponent.getApprovalState()) || ApprovalStatus.NOT_SUBMITTED.equals(oldComponent.getApprovalState()))
							&& ApprovalStatus.APPROVED.equals(component.getComponent().getApprovalState())) {
						component.getComponent().setApprovedUser(SecurityUtil.getCurrentUserName());
						component.getComponent().setApprovedDts(TimeUtil.currentDate());
						approved = true;
					} else if (ApprovalStatus.APPROVED.equals(oldComponent.getApprovalState())
							&& (ApprovalStatus.PENDING.equals(component.getComponent().getApprovalState())) || ApprovalStatus.NOT_SUBMITTED.equals(component.getComponent().getApprovalState())) {
						component.getComponent().setApprovedUser(null);
						component.getComponent().setApprovedDts(null);
					}
					oldComponent.updateFields(component.getComponent());
					if (component.getUpdateVersion()) {
						Integer version = component.getComponent().getRecordVersion();
						if (version == null) {
							version = 1;
						} else {
							version++;
						}
						component.getComponent().setRecordVersion(version);
					}

					attachedComponent = persistenceService.persist(oldComponent);
					component.setComponentChanged(true);
				}

				for (ComponentAttribute componentAttribute : component.getAttributes()) {
					componentAttribute.getComponentAttributePk().setComponentId(oldComponent.getComponentId());
				}
				component.setAttributeChanged(handleBaseComponentSave(ComponentAttribute.class, component.getAttributes(), oldComponent.getComponentId()));

			} else {

				if (StringUtils.isBlank(component.getComponent().getComponentId())) {
					component.getComponent().setComponentId(persistenceService.generateId());
				}
				component.getComponent().populateBaseCreateFields();
				component.getComponent().setLastActivityDts(TimeUtil.currentDate());
				if (component.getComponent().getRecordVersion() == null) {
					component.getComponent().setRecordVersion(1);
				}

				if (ApprovalStatus.APPROVED.equals(component.getComponent().getApprovalState())) {
					if (StringUtils.isBlank(component.getComponent().getApprovedUser())) {
						component.getComponent().setApprovedUser(SecurityUtil.getCurrentUserName());
					}
					if (component.getComponent().getApprovedDts() == null) {
						component.getComponent().setApprovedDts(TimeUtil.currentDate());
					}
					approved = true;
				}

				attachedComponent = persistenceService.persist(component.getComponent());
				component.setComponentChanged(true);

				component.getAttributes().forEach(attribute
						-> {
					attribute.setComponentId(component.getComponent().getComponentId());
					attribute.getComponentAttributePk().setComponentId(component.getComponent().getComponentId());
					attribute.setCreateUser(component.getComponent().getCreateUser());
					attribute.setUpdateUser(component.getComponent().getUpdateUser());
					componentService.getSub().saveComponentAttribute(attribute, false, true);
				});
				component.setAttributeChanged(true);
			}
			componentService.getOrganizationService().addOrganization(component.getComponent().getOrganization());

			// Convert inline-referenced temporary media to entry-specific media
			String description = component.getComponent().getDescription();
			Document descriptionDoc = Jsoup.parse(description);

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
						ComponentMedia componentMedia;
						if (processedConversions.containsKey(tempMediaId)) {
							componentMedia = persistenceService.findById(ComponentMedia.class, processedConversions.get(tempMediaId));
						} else {
							componentMedia = new ComponentMedia();
							componentMedia.setActiveStatus(Component.ACTIVE_STATUS);
							componentMedia.setComponentId(component.getComponent().getComponentId());
							componentMedia.setUpdateUser(SecurityUtil.getCurrentUserName());
							componentMedia.setCreateUser(SecurityUtil.getCurrentUserName());
							componentMedia.setOriginalName(existingTemporaryMedia.getOriginalFileName());
							componentMedia.setMimeType(existingTemporaryMedia.getMimeType());
							componentMedia.setUsedInline(true);
							componentMedia.setHideInDisplay(false);
							if (existingTemporaryMedia.getOriginalSourceURL().equals("fileUpload")) {
								//stripe generated part of name
								String nameParts[] = existingTemporaryMedia.getName().split(OpenStorefrontConstant.GENERAL_KEY_SEPARATOR);
								componentMedia.setCaption(nameParts[0]);
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

							componentMedia.setMediaTypeCode(mediaTypeCode);

							try {
								Path path = existingTemporaryMedia.pathToMedia();
								InputStream in = new FileInputStream(path.toFile());
								componentMedia.setComponentMediaId(persistenceService.generateId());
								componentMedia.populateBaseCreateFields();
								componentMedia.setFileName(componentMedia.getComponentMediaId());
								Files.copy(in, componentMedia.pathToMedia());
								persistenceService.persist(componentMedia);
								persistenceService.commit();
								processedConversions.put(tempMediaId, componentMedia.getComponentMediaId());
							} catch (IOException ex) {
								throw new OpenStorefrontRuntimeException("Failed to convert temporary media to component media", ex);
							}
						}
						// Replace converted url
						String replaceUrl = "LoadMedia&mediaId=".concat(componentMedia.getComponentMediaId());
						String newUrl = url.substring(0, url.indexOf("TemporaryMedia")).concat(replaceUrl);
						LOG.log(Level.FINE, MessageFormat.format("TemporaryMedia Conversion: Replacing {0} with {1}", url, newUrl));
						mediaItem.attr("src", newUrl);
					} else {
						LOG.log(Level.WARNING, MessageFormat.format("Unable to find existing temporary media for temporaryID: {0}", tempMediaId));
					}
				}
			}
			// Save new html to description
			if (attachedComponent != null) {
				String newDescription = descriptionDoc.toString();
				attachedComponent.setDescription(newDescription);
				component.getComponent().setDescription(newDescription);

				// Delete temporary media that was converted.
				for (String temporaryId : processedConversions.keySet()) {
					componentService.getSystemService().removeTemporaryMedia(temporaryId);
				}

				persistenceService.persist(attachedComponent);
			}

			if (approved) {
				sendApprovalNotification(component.getComponent());
			}
			cleanupCache(component.getComponent().getComponentId());
		} else {
			throw new OpenStorefrontRuntimeException(validationResult.toString());
		}
		return component;
	}

	private void sendApprovalNotification(Component component)
	{
		if (StringUtils.isNotBlank(component.getNotifyOfApprovalEmail())) {
			UserMessage userMessage = new UserMessage();
			userMessage.setEmailAddress(component.getNotifyOfApprovalEmail());
			userMessage.setComponentId(component.getComponentId());
			userMessage.setUserMessageType(UserMessageType.APPROVAL_NOTIFICATION);
			userMessage.setCreateUser(OpenStorefrontConstant.SYSTEM_USER);
			userMessage.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
			componentService.getUserService().queueUserMessage(userMessage);
		}
	}

	private void sendChangeRequestNotification(Component component)
	{
		if (StringUtils.isNotBlank(component.getNotifyOfApprovalEmail())) {
			UserMessage userMessage = new UserMessage();
			userMessage.setEmailAddress(component.getNotifyOfApprovalEmail());
			userMessage.setComponentId(component.getComponentId());
			userMessage.setUserMessageType(UserMessageType.CHANGE_REQUEST_APPROVAL_NOTIFICATION);
			userMessage.setCreateUser(OpenStorefrontConstant.SYSTEM_USER);
			userMessage.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
			componentService.getUserService().queueUserMessage(userMessage);
		}
	}

	public void importComponents(List<ComponentAll> components, FileHistoryOption options)
	{
		List<Component> componentsToIndex = new ArrayList<>();
		components.forEach(component
				-> {
			Component existing = findExistingComponent(component.getComponent());
			if (existing != null) {
				component.getComponent().setComponentId(existing.getComponentId());
				snapshotVersion(component.getComponent().getComponentId(), component.getComponent().getFileHistoryId());
			}
			saveFullComponent(component, options, false);
			componentsToIndex.add(component.getComponent());
		});
		componentService.getSearchService().indexComponents(componentsToIndex);
	}

	public ComponentAll saveFullComponent(ComponentAll componentAll)
	{
		return saveFullComponent(componentAll, new FileHistoryOption(), true);
	}

	public ComponentAll saveFullComponent(ComponentAll componentAll, FileHistoryOption options)
	{
		return saveFullComponent(componentAll, options, true);
	}

	private ComponentAll saveFullComponent(ComponentAll componentAll, FileHistoryOption options, boolean updateIndex)
	{
		Objects.requireNonNull(componentAll);
		Objects.requireNonNull(options, "Options are required; pass new one for defaults");

		LockSwitch lockSwitch = new LockSwitch();

		//check component
		Component component = componentAll.getComponent();
		if (StringUtils.isBlank(component.getCreateUser())) {
			component.setCreateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
		}

		if (StringUtils.isBlank(component.getUpdateUser())) {
			component.setUpdateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
		}

		if (StringUtils.isBlank(component.getApprovalState())) {
			component.setApprovalState(ApprovalStatus.APPROVED);
			component.setApprovedUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
			component.setApprovedDts(TimeUtil.currentDate());
		}

		if (component.getLastActivityDts() != null) {
			component.setLastActivityDts(TimeUtil.currentDate());
		}

		if (Convert.toBoolean(options.getSkipDuplicationCheck()) == false) {
			Component oldComponent = findExistingComponent(component);
			if (oldComponent != null) {
				component.setComponentId(oldComponent.getComponentId());
			}
		}

		//Check Attributes
		ValidationModel validationModel = new ValidationModel(component);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			RequiredForComponent requiredForComponent = new RequiredForComponent();
			requiredForComponent.setComponent(component);
			requiredForComponent.getAttributes().addAll(componentAll.getAttributes());

			requiredForComponent = doSaveComponent(requiredForComponent, options);
			lockSwitch.setSwitched(requiredForComponent.isComponentChanged());
			lockSwitch.setSwitched(requiredForComponent.isAttributeChanged());
		} else {
			throw new OpenStorefrontRuntimeException(validationResult.toString());
		}

		lockSwitch.setSwitched(handleBaseComponentSave(ComponentContact.class, componentAll.getContacts(), component.getComponentId()));
		lockSwitch.setSwitched(handleBaseComponentSave(ComponentEvaluationSection.class, componentAll.getEvaluationSections(), component.getComponentId()));
		lockSwitch.setSwitched(handleBaseComponentSave(ComponentExternalDependency.class, componentAll.getExternalDependencies(), component.getComponentId()));
		lockSwitch.setSwitched(handleBaseComponentSave(ComponentMedia.class, componentAll.getMedia(), component.getComponentId()));
		lockSwitch.setSwitched(handleBaseComponentSave(ComponentMetadata.class, componentAll.getMetadata(), component.getComponentId()));
		lockSwitch.setSwitched(handleBaseComponentSave(ComponentResource.class, componentAll.getResources(), component.getComponentId()));
		lockSwitch.setSwitched(handleBaseComponentSave(ComponentRelationship.class, componentAll.getRelationships(), component.getComponentId()));

		if (Convert.toBoolean(options.getUploadTags())) {
			lockSwitch.setSwitched(handleBaseComponentSave(ComponentTag.class, componentAll.getTags(), component.getComponentId()));
		}

		if (Convert.toBoolean(options.getUploadQuestions())) {
			List<ComponentQuestion> questions = new ArrayList<>();

			// We will use a HashMap and the uniqueKey() method to keep track of which responses belong to which
			// question for setting each responses questionIds below. The uniqueKey() method has limitations in
			// uniqueness, see the implementation for more info.
			Map<String, QuestionAll> questionAllMap = new HashMap<>();
			for (QuestionAll question : componentAll.getQuestions()) {
				questions.add(question.getQuestion());
				questionAllMap.put(question.getQuestion().uniqueKey(), question);
			}
			// We now send the questions to be saved.
			lockSwitch.setSwitched(handleBaseComponentSave(ComponentQuestion.class, questions, component.getComponentId()));

			// After this point, we are assuming that the 'questions' list that we sent in to be saved
			// still refers to the same questions.  However, after saving, they should all have proper questionIds.
			// Since we know the questionIds, we can now go through the responses to set the questionId on them.
			// We loop through and use the previously-created map to grab the respective responses.
			List<ComponentQuestionResponse> responses = new ArrayList<>();
			for (ComponentQuestion question : questions) {
				QuestionAll questionAll = questionAllMap.get(question.uniqueKey());
				List<ComponentQuestionResponse> theseResponses = questionAll.getResponds();
				for (ComponentQuestionResponse response : theseResponses) {
					response.setQuestionId(question.getQuestionId());
					response.setComponentId(component.getComponentId());
					responses.add(response);
				}
			}
			lockSwitch.setSwitched(handleBaseComponentSave(ComponentQuestionResponse.class, responses, component.getComponentId()));

		}

		if (Convert.toBoolean(options.getUploadReviews())) {
			List<ComponentReview> reviews = new ArrayList<>();
			List<ComponentReviewCon> componentCons = new ArrayList<>();
			List<ComponentReviewPro> componentPros = new ArrayList<>();
			Map<String, ReviewAll> reviewAllMap = new HashMap<>();
			for (ReviewAll reviewAll : componentAll.getReviews()) {
				reviews.add(reviewAll.getComponentReview());
				reviewAllMap.put(reviewAll.getComponentReview().uniqueKey(), reviewAll);
				componentCons.addAll(reviewAll.getCons());
				componentPros.addAll(reviewAll.getPros());
			}
			lockSwitch.setSwitched(handleBaseComponentSave(ComponentReview.class, reviews, component.getComponentId()));
			for (ComponentReview review : reviews) {
				ReviewAll reviewAll = reviewAllMap.get(review.uniqueKey());
				List<ComponentReviewPro> pros = reviewAll.getPros();
				List<ComponentReviewCon> cons = reviewAll.getCons();
				for (ComponentReviewPro pro : pros) {
					pro.getComponentReviewProPk().setComponentReviewId(review.getComponentReviewId());
					pro.setComponentId(component.getComponentId());
				}
				for (ComponentReviewCon con : cons) {
					con.getComponentReviewConPk().setComponentReviewId(review.getComponentReviewId());
					con.setComponentId(component.getComponentId());
				}
			}
			lockSwitch.setSwitched(handleBaseComponentSave(ComponentReviewPro.class, componentPros, component.getComponentId()));
			lockSwitch.setSwitched(handleBaseComponentSave(ComponentReviewCon.class, componentCons, component.getComponentId()));
		}

		if (Convert.toBoolean(options.getUploadIntegration())) {
			if (componentAll.getIntegrationAll() != null) {
				//Note: this should effect watches or component updating.

				componentAll.getIntegrationAll().getIntegration().setComponentId(component.getComponentId());
				validationModel = new ValidationModel(componentAll.getIntegrationAll().getIntegration());
				validationModel.setConsumeFieldsOnly(true);
				validationResult = ValidationUtil.validate(validationModel);
				if (validationResult.valid()) {

					integration.saveComponentIntegration(componentAll.getIntegrationAll().getIntegration());

					for (ComponentIntegrationConfig config : componentAll.getIntegrationAll().getConfigs()) {
						validationModel = new ValidationModel(config);
						validationModel.setConsumeFieldsOnly(true);
						validationResult = ValidationUtil.validate(validationModel);
						if (validationResult.valid()) {
							config.setComponentId(component.getComponentId());
							integration.saveComponentIntegrationConfig(config);
						}
					}
				} else {
					throw new OpenStorefrontRuntimeException(validationResult.toString());
				}
			}
		}

		if (Component.INACTIVE_STATUS.equals(component.getActiveStatus())) {
			componentService.getUserService().removeAllWatchesForComponent(component.getComponentId());
			componentService.getSearchService().deleteById(component.getComponentId());
		}

		if (lockSwitch.isSwitched()) {
			componentService.getUserService().checkComponentWatches(component);

			if (updateIndex) {
				List<Component> componentsToIndex = new ArrayList<>();
				componentsToIndex.add(component);
				componentService.getSearchService().indexComponents(componentsToIndex);
			}
		}

		return componentAll;
	}

	private <T extends BaseComponent> boolean handleBaseComponentSave(Class<T> baseComponentClass, List<T> baseComponents, String componentId)
	{
		boolean changed = false;

		//get existing
		List<T> existingComponents = componentService.getSub().getBaseComponent(baseComponentClass, componentId, null);
		Map<String, T> existingMap = new HashMap<>();
		for (T entity : existingComponents) {
			existingMap.put(EntityUtil.getPKFieldValue(entity), entity);
		}

		Set<String> inputMap = new HashSet<>();
		for (BaseComponent baseComponent : baseComponents) {
			baseComponent.setComponentId(componentId);

			//Set Composite keys
			if (baseComponent instanceof ComponentEvaluationSection) {
				ComponentEvaluationSection evaluationSection = (ComponentEvaluationSection) baseComponent;
				if (evaluationSection.getComponentEvaluationSectionPk() != null) {
					evaluationSection.getComponentEvaluationSectionPk().setComponentId(componentId);
				}
			}

			//validate
			ValidationModel validationModel = new ValidationModel(baseComponent);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult validationResult = ValidationUtil.validate(validationModel);
			if (validationResult.valid() == false) {
				throw new OpenStorefrontRuntimeException(validationResult.toString());
			}
			inputMap.add(EntityUtil.getPKFieldValue(baseComponent));

			//Look for match
			boolean match = false;
			for (T entity : existingComponents) {
				//compare
				if (entity.compareTo(baseComponent) == 0) {
					match = true;
					inputMap.add(EntityUtil.getPKFieldValue(entity));
					break;
				}
			}

			//save new ones
			if (match == false) {
				changed = true;
				if (baseComponent instanceof ComponentContact) {
					sub.saveComponentContact((ComponentContact) baseComponent, false);
				} else if (baseComponent instanceof ComponentAttribute) {
					sub.saveComponentAttribute((ComponentAttribute) baseComponent, false, true);
				} else if (baseComponent instanceof ComponentEvaluationSection) {
					sub.saveComponentEvaluationSection((ComponentEvaluationSection) baseComponent, false);
				} else if (baseComponent instanceof ComponentExternalDependency) {
					sub.saveComponentDependency((ComponentExternalDependency) baseComponent, false);
				} else if (baseComponent instanceof ComponentMedia) {
					sub.saveComponentMedia((ComponentMedia) baseComponent, false);
				} else if (baseComponent instanceof ComponentMetadata) {
					sub.saveComponentMetadata((ComponentMetadata) baseComponent, false);
				} else if (baseComponent instanceof ComponentResource) {
					sub.saveComponentResource((ComponentResource) baseComponent, false);
				} else if (baseComponent instanceof ComponentRelationship) {
					sub.saveComponentRelationship((ComponentRelationship) baseComponent, false);
				} else if (baseComponent instanceof ComponentTag) {
					sub.doSaveComponentTag((ComponentTag) baseComponent, false);
				} else if (baseComponent instanceof ComponentQuestion) {
					sub.saveComponentQuestion((ComponentQuestion) baseComponent, false);
				} else if (baseComponent instanceof ComponentQuestionResponse) {
					sub.saveComponentQuestionResponse((ComponentQuestionResponse) baseComponent, false);
				} else if (baseComponent instanceof ComponentReview) {
					sub.saveComponentReview((ComponentReview) baseComponent, false);
				} else if (baseComponent instanceof ComponentReviewPro) {
					sub.saveComponentReviewPro((ComponentReviewPro) baseComponent, false);
				} else if (baseComponent instanceof ComponentReviewCon) {
					sub.saveComponentReviewCon((ComponentReviewCon) baseComponent, false);
				} else {
					throw new OpenStorefrontRuntimeException("Save not supported for this base component: " + baseComponent.getClass().getName(), "Add support (Developement task)");
				}
			}
		}

		//remove old existing records
		for (String existing : existingMap.keySet()) {
			if (inputMap.contains(existing) == false) {
				BaseComponent oldEnity = existingMap.get(existing);
				try {
					Field pkField = EntityUtil.getPKField(oldEnity);
					if (pkField != null) {
						pkField.setAccessible(true);
						if (oldEnity instanceof ComponentTag) {
							sub.deleteBaseComponent(baseComponentClass, pkField.get(oldEnity), false);
						} else {
							sub.deactivateBaseComponent(baseComponentClass, pkField.get(oldEnity), false, oldEnity.getUpdateUser());
						}
					} else {
						throw new OpenStorefrontRuntimeException("Unable to find PK field on entity.", "Check enity: " + oldEnity.getClass().getName());
					}
				} catch (IllegalArgumentException | IllegalAccessException ex) {
					throw new OpenStorefrontRuntimeException(ex);
				}
				changed = true;
			}
		}

		return changed;
	}

	public void cascadeDeleteOfComponent(String componentId)
	{
		cascadeDeleteOfComponent(componentId, new ComponentDeleteOptions());
	}

	public void cascadeDeleteOfComponent(String componentId, ComponentDeleteOptions option)
	{
		Objects.requireNonNull(componentId, "Component Id is required.");
		LOG.log(Level.INFO, MessageFormat.format("Attempting to Removing component: {0}", componentId));

		Collection<Class<?>> entityClasses = DBManager.getConnection().getEntityManager().getRegisteredEntities();
		for (Class entityClass : entityClasses) {
			if (ReflectionUtil.BASECOMPONENT_ENTITY.equals(entityClass.getSimpleName()) == false) {
				if (ReflectionUtil.isSubClass(ReflectionUtil.BASECOMPONENT_ENTITY, entityClass)) {
					if (option.getIgnoreClasses().contains(entityClass.getSimpleName()) == false) {
						try {
							deleteBaseComponent((BaseComponent) entityClass.newInstance(), componentId);
						} catch (InstantiationException | IllegalAccessException ex) {
							throw new OpenStorefrontRuntimeException("Class is not a base component class: " + entityClass.getName(), "Check class");
						}
					}
				}
			}
		}
		if (option.getIgnoreClasses().contains(ComponentIntegration.class.getSimpleName()) == false) {
			integration.deleteComponentIntegration(componentId);
		}

		//Delete relationships pointed to this asset
		if (option.getIgnoreClasses().contains(ComponentRelationship.class.getSimpleName()) == false) {
			ComponentRelationship componentRelationship = new ComponentRelationship();
			componentRelationship.setRelatedComponentId(componentId);
			persistenceService.deleteByExample(componentRelationship);
		}

		if (option.getIgnoreClasses().contains(Evaluation.class.getSimpleName()) == false) {
			Evaluation evaluationExample = new Evaluation();
			evaluationExample.setOriginComponentId(componentId);

			List<Evaluation> evaluations = evaluationExample.findByExample();
			for (Evaluation evaluation : evaluations) {
				componentService.getEvaluationService().deleteEvaluation(evaluation.getEvaluationId());
			}
		}

		//Delete change log records
		if (option.getIgnoreClasses().contains(ChangeLog.class.getSimpleName()) == false) {
			componentService.getChangeLogServicePrivate().removeChangeLogs(Component.class.getSimpleName(), componentId);
		}

		//delete change requests
		Component changeRequestExample = new Component();
		changeRequestExample.setPendingChangeId(componentId);
		List<Component> changeRequests = changeRequestExample.findByExample();
		for (Component component : changeRequests) {
			//delete everything as the record will be ophaned.
			cascadeDeleteOfComponent(component.getComponentId());
		}

		Component component = persistenceService.findById(Component.class, componentId);
		persistenceService.delete(component);

		if (option.getRemoveWatches()) {
			componentService.getUserService().removeAllWatchesForComponent(componentId);
		}
		componentService.getSearchService().deleteById(componentId);
		cleanupCache(componentId);
	}

	private <T extends BaseComponent> void deleteBaseComponent(T example, String componentId)
	{
		example.setComponentId(componentId);
		if (example instanceof ComponentResource) {
			List<T> foundRecords = example.findByExample();
			for (T found : foundRecords) {
				sub.removeLocalResource((ComponentResource) found);
			}
		} else if (example instanceof ComponentMedia) {
			List<T> foundRecords = example.findByExample();
			for (T found : foundRecords) {
				sub.removeLocalMedia((ComponentMedia) found);
			}
		}
		persistenceService.deleteByExample(example);

	}

	public Boolean setLastViewDts(String componentId, String userId)
	{
		UserWatch example = new UserWatch();
		example.setComponentId(componentId);
		example.setUsername(userId);
		example = persistenceService.queryOneByExample(new QueryByExample(example));
		if (example != null) {
			UserWatch watch = persistenceService.findById(UserWatch.class, example.getUserWatchId());
			watch.setLastViewDts(TimeUtil.currentDate());
			persistenceService.persist(watch);
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public List<Component> findRecentlyAdded(int maxResults)
	{
		String dataFilterRestriction = FilterEngine.queryComponentRestriction();
		if (StringUtils.isNotBlank(dataFilterRestriction)) {
			dataFilterRestriction = " and " + dataFilterRestriction;
		}

		String query = "select from Component where activeStatus = :activeStatusParam "
				+ " and approvalState = :approvedStateParam "
				+ dataFilterRestriction
				+ " order by approvedDts DESC LIMIT " + maxResults;

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("activeStatusParam", Component.ACTIVE_STATUS);
		parameters.put("approvedStateParam", ApprovalStatus.APPROVED);

		return persistenceService.query(query, parameters);
	}

	public List<ComponentSearchView> getSearchComponentList(List<String> componentIds)
	{
		List<ComponentSearchView> componentSearchViews = new ArrayList<>();
		if (componentIds.isEmpty() == false) {

			String dataFilterRestriction = FilterEngine.queryComponentRestriction();
			if (StringUtils.isNotBlank(dataFilterRestriction)) {
				dataFilterRestriction += " and ";
			}

			//get all active data
			StringBuilder componentQuery = new StringBuilder();
			componentQuery.append("select from Component where activeStatus='")
					.append(Component.ACTIVE_STATUS)
					.append("'and approvalState='")
					.append(ApprovalStatus.APPROVED)
					.append("' and ")
					.append(dataFilterRestriction)
					.append(" componentId IN :componentIdsParams");

			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("componentIdsParams", componentIds);
			List<Component> components = persistenceService.query(componentQuery.toString(), paramMap, true);

			StringBuilder componentAttributeQuery = new StringBuilder();
			componentAttributeQuery.append("select componentAttributePk.attributeType as attributeType, componentAttributePk.attributeCode as attributeCode, componentId from ComponentAttribute where activeStatus='")
					.append(Component.ACTIVE_STATUS)
					.append("' and componentId IN :componentIdsParams");

			List<ODocument> documents = persistenceService.query(componentAttributeQuery.toString(), paramMap, false);
			List<ComponentAttribute> componentAttributes = new ArrayList<>();
			for (ODocument document : documents) {
				ComponentAttribute componentAttribute = new ComponentAttribute();

				ComponentAttributePk componentAttributePk = new ComponentAttributePk();
				componentAttributePk.setComponentId(document.field("componentId"));
				componentAttributePk.setAttributeCode(document.field("attributeCode"));
				componentAttributePk.setAttributeType(document.field("attributeType"));
				componentAttribute.setComponentAttributePk(componentAttributePk);
				componentAttribute.setComponentId(componentAttributePk.getComponentId());
				componentAttributes.add(componentAttribute);
			}
			Map<String, List<ComponentAttribute>> attributeMap = new HashMap<>();
			for (ComponentAttribute componentAttribute : componentAttributes) {
				if (attributeMap.containsKey(componentAttribute.getComponentId())) {
					attributeMap.get(componentAttribute.getComponentId()).add(componentAttribute);
				} else {
					List<ComponentAttribute> attributes = new ArrayList<>();
					attributes.add(componentAttribute);
					attributeMap.put(componentAttribute.getComponentId(), attributes);
				}
			}

			StringBuilder componentReviewQuery = new StringBuilder();
			componentReviewQuery.append("select from ComponentReview where activeStatus='").append(Component.ACTIVE_STATUS).append("' and componentId IN :componentIdsParams");
			List<ComponentReview> componentReviews = persistenceService.query(componentReviewQuery.toString(), paramMap, true);
			Map<String, List<ComponentReview>> reviewMap = new HashMap<>();
			for (ComponentReview componentReview : componentReviews) {
				if (reviewMap.containsKey(componentReview.getComponentId())) {
					reviewMap.get(componentReview.getComponentId()).add(componentReview);
				} else {
					List<ComponentReview> reviews = new ArrayList<>();
					reviews.add(componentReview);
					reviewMap.put(componentReview.getComponentId(), reviews);
				}
			}

			StringBuilder componentTagQuery = new StringBuilder();
			componentTagQuery.append("select from ComponentTag where activeStatus='").append(Component.ACTIVE_STATUS).append("' and componentId IN :componentIdsParams");
			List<ComponentTag> componentTags = persistenceService.query(componentTagQuery.toString(), paramMap, true);
			Map<String, List<ComponentTag>> tagMap = new HashMap<>();
			for (ComponentTag componentTag : componentTags) {
				if (tagMap.containsKey(componentTag.getComponentId())) {
					tagMap.get(componentTag.getComponentId()).add(componentTag);
				} else {
					List<ComponentTag> tags = new ArrayList<>();
					tags.add(componentTag);
					tagMap.put(componentTag.getComponentId(), tags);
				}
			}

			componentService.getAttributeServicePrivate().warmAttributeCaches();

			//group by component
			for (Component component : components) {
				List<ComponentAttribute> attributes = attributeMap.get(component.getComponentId());
				if (attributes == null) {
					attributes = new ArrayList<>();
				}

				List<ComponentReview> reviews = reviewMap.get(component.getComponentId());
				if (reviews == null) {
					reviews = new ArrayList<>();
				}

				List<ComponentTag> tags = tagMap.get(component.getComponentId());
				if (tags == null) {
					tags = new ArrayList<>();
				}
				ComponentSearchView componentSearchView = ComponentSearchView.toView(component, attributes, reviews, tags);
				componentSearchViews.add(componentSearchView);
			}

		}

		return componentSearchViews;
	}

	public ComponentAll getFullComponent(String componentId)
	{
		ComponentAll componentAll = null;
		if (StringUtils.isNotBlank(componentId)) {
			Element element = OSFCacheManager.getComponentCache().get(componentId);
			if (element != null) {
				componentAll = (ComponentAll) element.getObjectValue();
			} else {
				componentAll = new ComponentAll();

				Component componentExample = new Component();
				componentExample.setComponentId(componentId);
				componentAll.setComponent(persistenceService.queryOneByExample(componentExample));

				if (componentAll.getComponent() != null) {
					componentAll.setAttributes(sub.getAttributesByComponentId(componentId));
					componentAll.setContacts(sub.getBaseComponent(ComponentContact.class, componentId));
					componentAll.setEvaluationSections(sub.getBaseComponent(ComponentEvaluationSection.class, componentId));
					componentAll.setExternalDependencies(sub.getBaseComponent(ComponentExternalDependency.class, componentId));
					componentAll.setMedia(sub.getBaseComponent(ComponentMedia.class, componentId));
					componentAll.setMetadata(sub.getBaseComponent(ComponentMetadata.class, componentId));
					componentAll.setRelationships(sub.getBaseComponent(ComponentRelationship.class, componentId));
					componentAll.setResources(sub.getBaseComponent(ComponentResource.class, componentId));
					componentAll.setResources(SortUtil.sortComponentResource(componentAll.getResources()));

					componentAll.setTags(sub.getBaseComponent(ComponentTag.class, componentId));

					List<QuestionAll> allQuestions = new ArrayList<>();
					List<ComponentQuestion> questions = sub.getBaseComponent(ComponentQuestion.class, componentId);
					for (ComponentQuestion question : questions) {
						QuestionAll questionAll = new QuestionAll();
						questionAll.setQuestion(question);

						ComponentQuestionResponse questionResponseExample = new ComponentQuestionResponse();
						questionResponseExample.setActiveStatus(ComponentQuestionResponse.ACTIVE_STATUS);
						questionResponseExample.setQuestionId(question.getQuestionId());
						questionAll.setResponds(persistenceService.queryByExample(questionResponseExample));
						allQuestions.add(questionAll);
					}
					componentAll.setQuestions(allQuestions);

					List<ReviewAll> allReviews = new ArrayList<>();
					List<ComponentReview> componentReviews = sub.getBaseComponent(ComponentReview.class, componentId);
					for (ComponentReview componentReview : componentReviews) {
						ReviewAll reviewAll = new ReviewAll();
						reviewAll.setComponentReview(componentReview);

						ComponentReviewPro componentReviewProExample = new ComponentReviewPro();
						ComponentReviewProPk componentReviewProExamplePk = new ComponentReviewProPk();
						componentReviewProExamplePk.setComponentReviewId(componentReview.getComponentReviewId());
						componentReviewProExample.setComponentReviewProPk(componentReviewProExamplePk);
						reviewAll.setPros(persistenceService.queryByExample(componentReviewProExample));

						ComponentReviewCon componentReviewConExample = new ComponentReviewCon();
						ComponentReviewConPk componentReviewConExamplePk = new ComponentReviewConPk();
						componentReviewConExamplePk.setComponentReviewId(componentReview.getComponentReviewId());
						componentReviewConExample.setComponentReviewConPk(componentReviewConExamplePk);
						reviewAll.setCons(persistenceService.queryByExample(componentReviewConExample));

						allReviews.add(reviewAll);
					}
					componentAll.setReviews(allReviews);

					ComponentIntegration componentIntegrationExample = new ComponentIntegration();
					componentIntegrationExample.setActiveStatus(ComponentIntegration.ACTIVE_STATUS);
					componentIntegrationExample.setComponentId(componentId);

					ComponentIntegration componentIntegration = persistenceService.queryOneByExample(componentIntegrationExample);
					if (componentIntegration != null) {
						IntegrationAll integrationAll = new IntegrationAll();
						integrationAll.setIntegration(componentIntegration);

						ComponentIntegrationConfig configExample = new ComponentIntegrationConfig();
						configExample.setActiveStatus(ComponentIntegrationConfig.ACTIVE_STATUS);
						configExample.setComponentId(componentId);
						integrationAll.setConfigs(persistenceService.queryByExample(configExample));
						componentAll.setIntegrationAll(integrationAll);
					}

					element = new Element(componentId, componentAll);
					OSFCacheManager.getComponentCache().put(element);
				}
			}
		}

		if (componentAll != null) {
			Component componentToFilter = componentAll.getComponent();
			componentToFilter = FilterEngine.filter(componentToFilter);
			if (componentToFilter == null) {
				componentAll = null;
			}
		}

		return componentAll;
	}

	public ComponentTrackingResult getComponentTracking(FilterQueryParams filter, String componentId)
	{
		ComponentTrackingResult result = new ComponentTrackingResult();

		List<String> componentIdInResults = new ArrayList<>();
		if (StringUtils.isNotBlank(filter.getComponentName())) {
			String query = "select componentId from " + Component.class.getSimpleName() + " where name.toLowerCase() like :componentName ";
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("componentName", "%" + filter.getComponentName().toLowerCase().trim() + "%");

			List<ODocument> documents = persistenceService.query(query, parameterMap);
			for (ODocument document : documents) {
				componentIdInResults.add(document.field("componentId"));
			}
		}

		Map<String, Object> parameterMap = new HashMap<>();
		StringBuilder primaryQuery = new StringBuilder();
		primaryQuery.append("select from ").append(ComponentTracking.class.getSimpleName()).append(" where ");
		primaryQuery.append(" activeStatus = :activeStatus ");

		parameterMap.put("activeStatus", filter.getStatus());

		if (StringUtils.isNotBlank(componentId)) {
			primaryQuery.append(" and componentId = :componentId ");
			parameterMap.put("componentId", componentId);
		}

		if (filter.getStartDts() != null && filter.getStartDts().getDate() != null) {
			primaryQuery.append(" and eventDts >= :startDts ");
			parameterMap.put("startDts", filter.getStartDts().getDate());
		}

		if (filter.getEndDts() != null && filter.getEndDts().getDate() != null) {
			primaryQuery.append(" and eventDts <= :endDts ");
			parameterMap.put("endDts", filter.getEndDts().getDate());
		}

		if (StringUtils.isNotBlank(filter.getName())) {
			primaryQuery.append(" and updateUser.toLowerCase() like :nameSearch ");
			parameterMap.put("nameSearch", "%" + filter.getName().toLowerCase().trim() + "%");
		}

		if (!componentIdInResults.isEmpty()) {
			primaryQuery.append(" and componentId IN :componentIdList ");
			parameterMap.put("componentIdList", componentIdInResults);
		}

		if (StringUtils.isNotBlank(filter.getSortField()) && StringUtils.isNotBlank(filter.getSortOrder())) {
			primaryQuery.append(" ORDER BY ");
			primaryQuery.append(filter.getSortField());
			primaryQuery.append(" ");
			primaryQuery.append(filter.getSortOrder());
		}

		List<ComponentTracking> componentTrackings = persistenceService.query(primaryQuery.toString(), parameterMap);

		result.setCount(componentTrackings.size());
		for (ComponentTracking item : componentTrackings) {
			ComponentTrackingCompleteWrapper wrapper = new ComponentTrackingCompleteWrapper();
			wrapper.setData(item);
			wrapper.setName(getComponentName(item.getComponentId()));
			wrapper.setComponentTypeLabel(TranslateUtil.translateComponentType(item.getComponentType()));
			result.getResult().add(wrapper);
		}
		result.setResult(filter.filter(result.getResult()));

		return result;
	}

	public ComponentAdminWrapper getFilteredComponents(ComponentFilterParams filter, String componentId)
	{
		ComponentAdminWrapper result = new ComponentAdminWrapper();

		Component componentExample = new Component();
		if (ComponentFilterParams.FILTER_ALL.equalsIgnoreCase(filter.getStatus()) == false) {
			if (StringUtils.isNotBlank(filter.getStatus())) {
				componentExample.setActiveStatus(filter.getStatus());
			}
		}
		componentExample.setComponentId(componentId);

		if (ApprovalStatus.FILTER_ALL.equalsIgnoreCase(filter.getApprovalState()) == false) {
			if (StringUtils.isNotBlank(filter.getApprovalState())) {
				componentExample.setApprovalState(filter.getApprovalState());
			}
		}
		if (ComponentType.ALL.equalsIgnoreCase(filter.getComponentType()) == false) {
			if (StringUtils.isNotBlank(filter.getComponentType())) {
				componentExample.setComponentType(filter.getComponentType());
			}
		}

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();

		// If given, filter the search by name
		if (StringUtils.isNotBlank(filter.getComponentName())) {
			Component componentLikeExample = new Component();
			componentLikeExample.setName("%" + filter.getComponentName().toLowerCase() + "%");

			// Define A Special Lookup Operation (ILIKE)
			specialOperatorModel.setExample(componentLikeExample);
			specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LIKE);
			specialOperatorModel.getGenerateStatementOption().setMethod(GenerateStatementOption.METHOD_LOWER_CASE);
		}

		QueryByExample queryByExample = new QueryByExample(componentExample);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		//TODO: consider moving the filtering work to the DB
		List<Component> components = persistenceService.queryByExample(queryByExample);

		components = FilterEngine.filter(components);

		//filter out pending changes
		components = components.stream().filter(c -> c.getActiveStatus().equals(Component.PENDING_STATUS) == false).collect(Collectors.toList());

		result.setTotalNumber(components.size());

		ComponentIntegrationConfig integrationConfigExample = new ComponentIntegrationConfig();
		integrationConfigExample.setActiveStatus(ComponentIntegrationConfig.ACTIVE_STATUS);

		List<ComponentIntegrationConfig> componentIntegrationConfigs = persistenceService.queryByExample(integrationConfigExample);
		Map<String, List<ComponentIntegrationConfig>> configMap = new HashMap<>();
		componentIntegrationConfigs.forEach(config
				-> {
			if (configMap.containsKey(config.getComponentId())) {
				configMap.get(config.getComponentId()).add(config);
			} else {
				List<ComponentIntegrationConfig> configList = new ArrayList<>();
				configList.add(config);
				configMap.put(config.getComponentId(), configList);
			}
		});

		Component pendingChangeExample = new Component();
		pendingChangeExample.setPendingChangeId(QueryByExample.STRING_FLAG);

		QueryByExample queryPendingChanges = new QueryByExample(new Component());

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(pendingChangeExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_NOT_NULL);
		queryPendingChanges.getExtraWhereCauses().add(specialOperatorModel);

		List<Component> pendingChanges = persistenceService.queryByExample(queryPendingChanges);
		Map<String, List<Component>> pendingChangesMap = pendingChanges.stream().collect(Collectors.groupingBy(Component::getPendingChangeId));
		List<ComponentView> componentViews = new ArrayList<>();
		for (Component component : components) {

			ComponentView componentView = ComponentView.toView(component);

			List<Component> pendingChangesList = pendingChangesMap.get(componentView.getComponentId());

			if (pendingChangesList != null) {
				componentView.setNumberOfPendingChanges(pendingChangesList.size());
				if (pendingChangesList.size() > 0) {
					Component changeComponent = pendingChangesList.get(0);
					componentView.setPendingChangeComponentId(changeComponent.getComponentId());
					componentView.setPendingChangeSubmitDts(changeComponent.getSubmittedDts());
					componentView.setPendingChangeSubmitUser(changeComponent.getCreateUser());
					componentView.setStatusOfPendingChange(TranslateUtil.translate(ApprovalStatus.class, changeComponent.getApprovalState()));
				}
			} else {
				componentView.setNumberOfPendingChanges(0);
			}

			componentViews.add(componentView);
		}
		componentViews = filter.filter(componentViews);

		List<ComponentAdminView> componentAdminViews = new ArrayList<>();
		for (ComponentView componentView : componentViews) {
			ComponentAdminView componentAdminView = new ComponentAdminView();
			componentAdminView.setComponent(componentView);
			StringBuilder configs = new StringBuilder();
			List<ComponentIntegrationConfig> configList = configMap.get(componentView.getComponentId());
			if (configList != null) {
				configList.forEach(config
						-> {
					if (StringUtils.isNotBlank(config.getIssueNumber())) {
						configs.append("(").append(config.getIntegrationType()).append(" - ").append(config.getIssueNumber()).append(") ");
					} else {
						configs.append("(").append(config.getIntegrationType()).append(") ");
					}
				});
			}
			componentAdminView.setIntegrationManagement(configs.toString());
			componentAdminViews.add(componentAdminView);
		}
		result.setComponents(componentAdminViews);

		return result;
	}

	public Set<LookupModel> getTypeahead(String search)
	{
		Set<LookupModel> results = new HashSet<>();

		Map<String, Object> params = new HashMap<>();
		search = "%" + search.toLowerCase() + "%";
		params.put("search", search);
		String query = "SELECT FROM " + Component.class.getSimpleName() + " WHERE name.toLowerCase() LIKE :search LIMIT 10";
		List<Component> components = persistenceService.query(query, params);
		for (Component component : components) {
			LookupModel temp = new LookupModel();
			temp.setCode(component.getComponentId());
			temp.setDescription(component.getName());
			results.add(temp);
		}
		return results;
	}

	public void submitComponentSubmission(String componentId)
	{
		Component component = persistenceService.findById(Component.class, componentId);
		if (component != null) {
			if (ApprovalStatus.APPROVED.equals(component.getApprovalState()) == false) {

				component.setApprovalState(ApprovalStatus.PENDING);
				component.setSubmittedDts(TimeUtil.currentDate());
				component.setUpdateUser(SecurityUtil.getCurrentUserName());
				component.populateBaseUpdateFields();
				persistenceService.persist(component);

				AlertContext alertContext = new AlertContext();
				alertContext.setAlertType(AlertType.COMPONENT_SUBMISSION);
				alertContext.setDataTrigger(component);
				componentService.getAlertService().checkAlert(alertContext);
			} else {
				throw new OpenStorefrontRuntimeException("Component: " + component.getName() + " is already Approved. Id: " + componentId);
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find component to submit.", "Check data");
		}
	}

	public void submitChangeRequest(String componentId)
	{
		Component component = persistenceService.findById(Component.class, componentId);
		if (component != null) {
			if (ApprovalStatus.APPROVED.equals(component.getApprovalState()) == false) {

				component.setApprovalState(ApprovalStatus.PENDING);
				component.setSubmittedDts(TimeUtil.currentDate());
				component.setUpdateUser(SecurityUtil.getCurrentUserName());
				component.populateBaseUpdateFields();
				component.setActiveStatus(Component.PENDING_STATUS);
				persistenceService.persist(component);

				AlertContext alertContext = new AlertContext();
				alertContext.setAlertType(AlertType.CHANGE_REQUEST);
				alertContext.setDataTrigger(component);
				componentService.getAlertService().checkAlert(alertContext);
			} else {
				throw new OpenStorefrontRuntimeException("Change request: " + component.getName() + " is already Approved.  Id: " + componentId);
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find change record to submit.", "Check data");
		}
	}

	public void processComponentUpdates()
	{
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		try {
			ComponentUpdateQueue updateQueueExample = new ComponentUpdateQueue();
			updateQueueExample.setNodeId(PropertiesManager.getNodeName());

			List<ComponentUpdateQueue> componentUpdateQueues = persistenceService.queryByExample(updateQueueExample);
			if (componentUpdateQueues.isEmpty() == false) {
				//Get the latest entries
				Map<String, ComponentUpdateQueue> componentMap = new HashMap<>();
				for (ComponentUpdateQueue updateQueue : componentUpdateQueues) {
					if (componentMap.containsKey(updateQueue.getUpdateId())) {
						ComponentUpdateQueue existing = componentMap.get(updateQueue.getUpdateId());
						if (existing.getUpdateDts().before(updateQueue.getUpdateDts())) {
							componentMap.put(updateQueue.getUpdateId(), updateQueue);
						}
					} else {
						componentMap.put(updateQueue.getUpdateId(), updateQueue);
					}
				}

				try {
					List<Component> componentsToIndex = new ArrayList<>();
					for (ComponentUpdateQueue componentUpdate : componentMap.values()) {
						String componentId = componentUpdate.getComponentId();

						//critical block try each record separately
						try {
							Component component = persistenceService.findById(Component.class, componentId);
							if (component != null) {
								component.setLastActivityDts(componentUpdate.getUpdateDts());
								component.setLastModificationType(componentUpdate.getModificationType());
								Integer version = component.getRecordVersion();
								if (version == null) {
									version = 1;
								} else {
									version++;
								}
								component.setRecordVersion(version);
								persistenceService.persist(component);
								componentService.getUserService().checkComponentWatches(component);
								componentsToIndex.add(component);
							} else {
								LOG.log(Level.FINE, MessageFormat.format("Component not found to update last Activity. Component may have been removed.  Check component id: {0}", componentId));
							}
						} catch (Exception e) {
							LOG.log(Level.SEVERE, "Fail to update component.  Check data on component id: " + componentId, e);
						}
					}
					componentService.getSearchService().indexComponents(componentsToIndex);
				} finally {
					//remove processed records (must remove them to avoid looping when there are issues
					for (ComponentUpdateQueue updateQueue : componentUpdateQueues) {
						ComponentUpdateQueue componentUpdateQueue = persistenceService.findById(ComponentUpdateQueue.class, updateQueue.getUpdateId());
						if (componentUpdateQueue != null) {
							persistenceService.delete(componentUpdateQueue);
						}
					}
				}
			}
		} finally {
			lock.unlock();
		}

	}

	public void checkComponentCancelStatus(String componentId, String newApprovalStatus)
	{
		checkCancelStatus(componentId, newApprovalStatus, AlertType.COMPONENT_SUBMISSION);
	}

	public void checkChangeRequestCancelStatus(String componentId, String newApprovalStatus)
	{
		checkCancelStatus(componentId, newApprovalStatus, AlertType.CHANGE_REQUEST);
	}

	private void checkCancelStatus(String componentId, String newApprovalStatus, String alertType)
	{
		Component existingComponent = persistenceService.findById(Component.class, componentId);

		if ((ApprovalStatus.PENDING.equals(existingComponent.getApprovalState())
				|| ApprovalStatus.APPROVED.equals(existingComponent.getApprovalState()))
				&& ApprovalStatus.NOT_SUBMITTED.equals(newApprovalStatus)) {

			existingComponent.setApprovalState(newApprovalStatus);
			existingComponent.setUpdateUser(SecurityUtil.getCurrentUserName());
			existingComponent.populateBaseUpdateFields();
			persistenceService.persist(existingComponent);

			AlertContext alertContext = new AlertContext();
			alertContext.setAlertType(alertType);
			alertContext.setDataTrigger(existingComponent);
			componentService.getAlertService().checkAlert(alertContext);

		}
	}

	public String getComponentApprovalStatus(String componentId)
	{
		String approvalStatus = null;

		String query = "select approvalState from " + Component.class.getSimpleName() + " where componentId = :componentIdParam";
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("componentIdParam", componentId);

		List<ODocument> documents = persistenceService.query(query, parameters);
		//There should be only one or none
		for (ODocument document : documents) {
			approvalStatus = document.field("approvalState");
		}
		return approvalStatus;
	}

	public boolean checkComponentApproval(String componentId)
	{
		boolean approved = false;
		Element element = OSFCacheManager.getComponentApprovalCache().get(componentId);
		if (element != null) {
			String approvalState = (String) element.getObjectValue();
			if (StringUtils.isNotBlank(approvalState)) {
				approved = true;
			}
		} else {
			String query = "select componentId, approvalState from " + Component.class.getSimpleName() + " where approvalState = :approvalStateParam and activeStatus = :activeStatusParam";
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("approvalStateParam", ApprovalStatus.APPROVED);
			parameters.put("activeStatusParam", Component.ACTIVE_STATUS);
			List<ODocument> documents = persistenceService.query(query, parameters);
			for (ODocument document : documents) {
				Element newElement = new Element(document.field("componentId"), document.field("approvalState"));
				if (document.field("componentId").equals(componentId)) {
					String approvalState = (String) document.field("approvalState");
					if (StringUtils.isNotBlank(approvalState)) {
						approved = true;
					}
				}
				OSFCacheManager.getComponentApprovalCache().put(newElement);
			}
		}
		return approved;
	}

	public ComponentSensitivityModel getComponentSensitivity(String componentId)
	{
		ComponentSensitivityModel componentSensitivityModel = new ComponentSensitivityModel();
		componentSensitivityModel.setComponentId(componentId);

		Element element = OSFCacheManager.getComponentDataRestrictionCache().get(componentId);
		if (element != null) {
			componentSensitivityModel = (ComponentSensitivityModel) element.getObjectValue();
		} else {
			String query = "select componentId, dataSource, dataSensitivity from " + Component.class.getSimpleName() + " where dataSource IS NOT NULL OR dataSensitivity IS NOT NULL";
			Map<String, Object> parameters = new HashMap<>();

			List<ODocument> documents = persistenceService.query(query, parameters);
			for (ODocument document : documents) {
				ComponentSensitivityModel cacheSensitivityModel = new ComponentSensitivityModel();
				cacheSensitivityModel.setComponentId(document.field("componentId"));
				cacheSensitivityModel.setDataSensitivity(document.field("dataSensitivity"));
				cacheSensitivityModel.setDataSource(document.field("dataSource"));

				Element newElement = new Element(document.field("componentId"), cacheSensitivityModel);
				if (document.field("componentId").equals(componentId)) {
					componentSensitivityModel.setComponentId(document.field("componentId"));
					componentSensitivityModel.setDataSensitivity(document.field("dataSensitivity"));
					componentSensitivityModel.setDataSource(document.field("dataSource"));
				}
				OSFCacheManager.getComponentDataRestrictionCache().put(newElement);
			}

			//add the missed cases
			query = "select componentId from " + Component.class.getSimpleName() + " where dataSource IS NULL AND dataSensitivity IS NULL";
			documents = persistenceService.query(query, null);
			for (ODocument document : documents) {
				String foundId = document.field("componentId");
				ComponentSensitivityModel cacheSensitivityModel = new ComponentSensitivityModel();
				cacheSensitivityModel.setComponentId(foundId);
				Element newElement = new Element(foundId, cacheSensitivityModel);
				OSFCacheManager.getComponentDataRestrictionCache().put(newElement);
			}

		}

		return componentSensitivityModel;
	}

	public Component copy(String orignalComponentId)
	{
		try {
			componentService.getChangeLogService().suspendSaving();

			cleanupCache(orignalComponentId);
			ComponentAll componentAll = getFullComponent(orignalComponentId);
			if (componentAll != null) {

				//check the copy name it should be unique
				String newName = componentAll.getComponent().getName() + COPY_MARKER;

				String validNewName = newName;
				boolean nameIsUnique = false;
				int count = 1;
				while (nameIsUnique == false) {
					Component componentSearch = new Component();
					componentSearch.setName(validNewName.toLowerCase());

					QueryByExample queryByExample = new QueryByExample(componentSearch);
					queryByExample.getFieldOptions().put(Component.FIELD_NAME,
							new GenerateStatementOptionBuilder()
									.setMethod(GenerateStatementOption.METHOD_LOWER_CASE)
									.build());

					Component existing = persistenceService.queryOneByExample(queryByExample);
					if (existing == null) {
						nameIsUnique = true;
					} else {
						validNewName = newName + count;
						count++;
					}
				}

				componentAll.getComponent().setComponentId(null);
				componentAll.getComponent().setName(validNewName);
				componentAll.getComponent().setApprovalState(ApprovalStatus.NOT_SUBMITTED);
				componentAll.getComponent().setApprovedDts(null);
				componentAll.getComponent().setApprovedUser(null);
				componentAll.getComponent().setExternalId(null);

				clearBaseComponentKey(componentAll.getAttributes());
				clearBaseComponentKey(componentAll.getContacts());
				clearBaseComponentKey(componentAll.getEvaluationSections());
				clearBaseComponentKey(componentAll.getExternalDependencies());
				clearBaseComponentKey(componentAll.getMetadata());
				clearBaseComponentKey(componentAll.getRelationships());
				clearBaseComponentKey(componentAll.getTags());

				//Don't copy these item
				componentAll.setIntegrationAll(null);
				componentAll.setReviews(new ArrayList<>());
				componentAll.setQuestions(new ArrayList<>());

				//Handle local media seperately
				List<ComponentMedia> localMedia = new ArrayList<>();
				for (int i = componentAll.getMedia().size() - 1; i >= 0; i--) {
					ComponentMedia media = componentAll.getMedia().get(i);
					if (media.getFileName() != null) {
						localMedia.add(componentAll.getMedia().remove(i));
					} else {
						media.clearKeys();
					}
				}

				//Handle local resource seperately
				List<ComponentResource> localResources = new ArrayList<>();
				for (int i = componentAll.getResources().size() - 1; i >= 0; i--) {
					ComponentResource resource = componentAll.getResources().get(i);
					if (resource.getFileName() != null) {
						localResources.add(componentAll.getResources().remove(i));
					} else {
						resource.clearKeys();
					}
				}

				FileHistoryOption fileHistoryOption = new FileHistoryOption();
				fileHistoryOption.setSkipDuplicationCheck(true);
				fileHistoryOption.setSkipRequiredAttributes(true);

				componentAll = saveFullComponent(componentAll, fileHistoryOption);

				//copy over local resources
				for (ComponentMedia media : localMedia) {
					media.setComponentId(componentAll.getComponent().getComponentId());
					Path oldPath = media.pathToMedia();
					if (oldPath != null) {
						media.setComponentMediaId(persistenceService.generateId());
						media.setFileName(media.getComponentMediaId());
						Path newPath = media.pathToMedia();
						try {
							Files.copy(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException ex) {
							throw new OpenStorefrontRuntimeException("Failed to copy media", "check disk permissions and space", ex);
						}
						media.populateBaseUpdateFields();
						persistenceService.persist(media);
					}
				}

				for (ComponentResource resource : localResources) {
					resource.setComponentId(componentAll.getComponent().getComponentId());
					Path oldPath = resource.pathToResource();
					if (oldPath != null) {
						resource.setResourceId(persistenceService.generateId());
						resource.setFileName(resource.getResourceId());
						Path newPath = resource.pathToResource();
						try {
							Files.copy(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException ex) {
							throw new OpenStorefrontRuntimeException("Failed to copy resource", "check disk permissions and space", ex);
						}
						resource.populateBaseUpdateFields();
						persistenceService.persist(resource);
					}
				}
				cleanupCache(orignalComponentId);
				cleanupCache(componentAll.getComponent().getComponentId());

				return componentAll.getComponent();
			} else {
				throw new OpenStorefrontRuntimeException("Unable to find component to copy  id: " + orignalComponentId, "Must sure component still exists");
			}
		} finally {
			componentService.getChangeLogService().resumeSaving();
		}
	}

	private <T extends BaseComponent> void clearBaseComponentKey(List<T> entities)
	{
		for (T component : entities) {
			component.clearKeys();
		}
	}

	public ComponentVersionHistory snapshotVersion(String componentId, String fileHistoryId)
	{
		ComponentVersionHistory versionHistory = new ComponentVersionHistory();

		ComponentAll componentAll = getFullComponent(componentId);
		if (componentAll != null) {
			versionHistory.setComponentId(componentId);
			versionHistory.setFileHistoryId(fileHistoryId);
			versionHistory.setVersionHistoryId(persistenceService.generateId());
			Integer version = componentAll.getComponent().getRecordVersion();
			if (version == null) {
				version = 1;
			}
			versionHistory.setVersion(version);
			versionHistory.populateBaseCreateFields();

			componentService.getChangeLogService().logOtherChange(componentAll.getComponent(), ChangeType.SNAPSHOT, "Version: " + version);

			try {
				String componentJson = StringProcessor.defaultObjectMapper().writeValueAsString(componentAll);
				String archiveName = versionHistory.pathToFile().toString();
				File entry = new TFile(archiveName + "/componentAll.json");
				try (Writer writer = new TFileWriter(entry)) {
					writer.write(componentJson);
				} catch (IOException io) {
					throw new OpenStorefrontRuntimeException("Unable to snapshot component.", io);
				}

				Set<String> fileNameMediaSet = new HashSet<>();
				Set<String> fileNameResourceSet = new HashSet<>();

				//media
				for (ComponentMedia componentMedia : componentAll.getMedia()) {
					java.nio.file.Path mediaPath = componentMedia.pathToMedia();
					if (mediaPath != null) {
						String name = mediaPath.getFileName().toString();
						if (fileNameMediaSet.contains(name) == false) {
							java.nio.file.Path archiveMediaPath = new TPath(archiveName + "/media/" + name);

							TFile source = new TFile(mediaPath.toString());
							TFile destination = new TFile(archiveMediaPath.toString());
							if (destination.isArchive() || destination.isDirectory()) {
								destination = new TFile(destination, source.getName());
							}
							source.toFile().cp_rp(destination);
							fileNameMediaSet.add(name);
						}
					}
				}

				//localreources
				for (ComponentResource componentResource : componentAll.getResources()) {
					java.nio.file.Path resourcePath = componentResource.pathToResource();
					if (resourcePath != null) {
						String name = resourcePath.getFileName().toString();
						if (fileNameResourceSet.contains(name) == false) {
							java.nio.file.Path archiveResourcePath = new TPath(archiveName + "/resources/" + name);

							TFile source = new TFile(resourcePath.toString());
							TFile destination = new TFile(archiveResourcePath.toString());
							if (destination.isArchive() || destination.isDirectory()) {
								destination = new TFile(destination, source.getName());
							}
							source.toFile().cp_rp(destination);
							fileNameResourceSet.add(name);
						}
					}
				}

			} catch (JsonProcessingException | FsSyncException ex) {
				throw new OpenStorefrontRuntimeException("Unable to snapshot component.", ex);
			} catch (IOException ex) {
				throw new OpenStorefrontRuntimeException("Unable to snapshot component.", ex);
			} finally {
				try {
					TVFS.umount();
				} catch (FsSyncException ex) {
					throw new OpenStorefrontRuntimeException("Unable to unable to unmount snapshot...it may be unreadable.", ex);
				}
			}
			persistenceService.persist(versionHistory);

		} else {
			throw new OpenStorefrontRuntimeException("Unable to find component: " + componentId, "Must sure component still exists");
		}
		return versionHistory;
	}

	public ComponentDetailView viewSnapshot(String versionHistoryId)
	{
		ComponentDetailView componentDetailView = null;

		ComponentVersionHistory versionHistory = persistenceService.findById(ComponentVersionHistory.class, versionHistoryId);
		if (versionHistory != null) {

			ComponentAll archivedVersion = null;
			TFile archive = new TFile(versionHistory.pathToFile().toFile());
			TFile files[] = archive.listFiles();
			if (files != null) {
				for (TFile file : files) {
					if (file.isFile()) {
						try (InputStream inTemp = new TFileInputStream(file)) {
							archivedVersion = StringProcessor.defaultObjectMapper().readValue(inTemp, new TypeReference<ComponentAll>()
							{
							});
						} catch (IOException ex) {
							throw new OpenStorefrontRuntimeException(ex);
						}
					}
				}
				if (archivedVersion != null) {
					componentDetailView = ComponentDetailView.toView(archivedVersion);
				}
			} else {
				String componentName = getComponentName(versionHistory.getComponentId());
				LOG.log(Level.WARNING, MessageFormat.format("There is no files in the snapshot for component: {0} version: {1} ", componentName, versionHistory.getVersionHistoryId()));
			}
		}

		return componentDetailView;
	}

	public Component restoreSnapshot(String versionHistoryId, ComponentRestoreOptions options)
	{
		ComponentVersionHistory versionHistory = persistenceService.findById(ComponentVersionHistory.class, versionHistoryId);
		if (versionHistory != null) {

			//Get Version (only the component so we don't wipe out the restored resources)
			ComponentAll archivedVersion = null;
			TFile archive = new TFile(versionHistory.pathToFile().toFile());
			TFile files[] = archive.listFiles();
			if (files != null) {
				for (TFile file : files) {
					if (file.isFile()) {
						try (InputStream inTemp = new TFileInputStream(file)) {
							archivedVersion = StringProcessor.defaultObjectMapper().readValue(inTemp, new TypeReference<ComponentAll>()
							{
							});
						} catch (IOException ex) {
							throw new OpenStorefrontRuntimeException(ex);
						}
					}
				}
			} else {
				String componentName = getComponentName(versionHistory.getComponentId());
				LOG.log(Level.WARNING, MessageFormat.format("There is no files in the snapshot for component: {0} version: {1} ", componentName, versionHistory.getVersionHistoryId()));
			}

			if (archivedVersion != null) {

				//delete existing (keep watches, other entities if requested)
				ComponentDeleteOptions deleteOptions = new ComponentDeleteOptions();
				deleteOptions.setRemoveWatches(false);
				if (options.getRestoreIntegration() == false) {
					deleteOptions.getIgnoreClasses().add(ComponentIntegration.class.getSimpleName());
				}
				if (options.getRestoreQuestions() == false) {
					deleteOptions.getIgnoreClasses().add(ComponentQuestion.class.getSimpleName());
					deleteOptions.getIgnoreClasses().add(ComponentQuestionResponse.class.getSimpleName());
				}
				if (options.getRestoreReviews() == false) {
					deleteOptions.getIgnoreClasses().add(ComponentReview.class.getSimpleName());
					deleteOptions.getIgnoreClasses().add(ComponentReviewCon.class.getSimpleName());
					deleteOptions.getIgnoreClasses().add(ComponentReviewPro.class.getSimpleName());
				}
				if (options.getRestoreTags() == false) {
					deleteOptions.getIgnoreClasses().add(ComponentTag.class.getSimpleName());
				}
				//Always leave versions
				deleteOptions.getIgnoreClasses().add(ComponentVersionHistory.class.getSimpleName());
				deleteOptions.getIgnoreClasses().add(ChangeLog.class.getSimpleName());
				cascadeDeleteOfComponent(versionHistory.getComponentId(), deleteOptions);

				//copy resources
				archive = new TFile(versionHistory.pathToFile().toFile());
				TFile allFiles[] = archive.listFiles();
				if (allFiles != null) {
					for (TFile file : allFiles) {
						if (file.isDirectory() && "media".equalsIgnoreCase(file.getName())) {
							TFile mediaFiles[] = file.listFiles();
							if (mediaFiles != null) {
								for (TFile mediaFile : mediaFiles) {
									try {
										TFile source = mediaFile;
										TFile destination = new TFile(FileSystemManager.getDir(FileSystemManager.MEDIA_DIR).toPath().resolve(mediaFile.getName()).toFile());
										if (destination.isArchive() || destination.isDirectory()) {
											destination = new TFile(destination, source.getName());
										}
										source.toFile().cp_rp(destination);

									} catch (IOException ex) {
										LOG.log(Level.WARNING, MessageFormat.format("Failed to copy media to path file: {0}", mediaFile.getName()), ex);
									}
								}
							}
						} else if (file.isDirectory() && "resources".equalsIgnoreCase(file.getName())) {
							TFile resourceFiles[] = file.listFiles();
							if (resourceFiles != null) {
								for (TFile resourceFile : resourceFiles) {
									try {
										TFile source = resourceFile;
										TFile destination = new TFile(FileSystemManager.getDir(FileSystemManager.RESOURCE_DIR).toPath().resolve(resourceFile.getName()).toFile());
										if (destination.isArchive() || destination.isDirectory()) {
											destination = new TFile(destination, source.getName());
										}
										source.toFile().cp_rp(destination);

									} catch (IOException ex) {
										LOG.log(Level.WARNING, MessageFormat.format("Failed to copy resource to path file: {0}", resourceFile.getName()), ex);
									}
								}
							}
						}
					}
				} else {
					String componentName = getComponentName(versionHistory.getComponentId());
					LOG.log(Level.WARNING, MessageFormat.format("There is no files in the snapshot for component: {0} version: {1} ", componentName, versionHistory.getVersionHistoryId()));
				}

				//save old version (keep in mind the update date will reflect now.)
				FileHistoryOption fileHistoryOptions = new FileHistoryOption();
				fileHistoryOptions.setSkipRequiredAttributes(Boolean.TRUE);
				fileHistoryOptions.setUploadQuestions(options.getRestoreQuestions());
				fileHistoryOptions.setUploadReviews(options.getRestoreReviews());
				fileHistoryOptions.setUploadTags(options.getRestoreTags());
				fileHistoryOptions.setUploadIntegration(options.getRestoreIntegration());

				saveFullComponent(archivedVersion, fileHistoryOptions);

				componentService.getChangeLogService().logOtherChange(archivedVersion.getComponent(), ChangeType.RESTORE, "Version: " + versionHistory.getVersion());

				cleanupCache(archivedVersion.getComponent().getComponentId());
				return archivedVersion.getComponent();
			} else {
				throw new OpenStorefrontRuntimeException("Unable to restore version.", "Make sure version archive is not corrupt.");
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find version: " + versionHistoryId, "Must sure component version still exists");
		}
	}

	public void deleteSnapshot(String versionHistoryId)
	{
		ComponentVersionHistory versionHistory = persistenceService.findById(ComponentVersionHistory.class, versionHistoryId);
		if (versionHistory != null) {
			Path path = versionHistory.pathToFile();
			if (path != null) {
				if (path.toFile().exists()) {
					if (path.toFile().delete() == false) {
						LOG.log(Level.WARNING, "Unable to delete component version: {0}", path.toString());
					}
				}
			}
			persistenceService.delete(versionHistory);
		}
	}

	public Component merge(String toMergeComponentId, String targetComponentId)
	{
		return merge(toMergeComponentId, targetComponentId, null);
	}

	private Component merge(String toMergeComponentId, String targetComponentId, FileHistoryOption saveOptions)
	{
		if (toMergeComponentId.equals(targetComponentId)) {
			throw new OpenStorefrontRuntimeException("Target Component and Merge Component cannot be the same.");
		}

		ComponentAll mergeComponent = getFullComponent(toMergeComponentId);
		ComponentAll targetComponent = getFullComponent(targetComponentId);
		if (mergeComponent != null) {
			if (targetComponent != null) {

				//Keep these fields
				mergeComponent.getComponent().setActiveStatus(targetComponent.getComponent().getActiveStatus());
				mergeComponent.getComponent().setApprovalState(targetComponent.getComponent().getApprovalState());
				mergeComponent.getComponent().setApprovedUser(targetComponent.getComponent().getApprovedUser());
				mergeComponent.getComponent().setApprovedDts(targetComponent.getComponent().getApprovedDts());
				mergeComponent.getComponent().setRecordVersion(targetComponent.getComponent().getRecordVersion());
				mergeComponent.getComponent().setLastModificationType(ModificationType.MERGE);
				componentService.setModificationType(ModificationType.MERGE);

				targetComponent.getComponent().updateFields(mergeComponent.getComponent());

				//a merge mashes together sub-enties from Merge to target
				mergeSubEntities(mergeComponent.getAttributes(), targetComponent.getAttributes());
				mergeSubEntities(mergeComponent.getContacts(), targetComponent.getContacts());
				mergeSubEntities(mergeComponent.getExternalDependencies(), targetComponent.getExternalDependencies());
				mergeSubEntities(mergeComponent.getRelationships(), targetComponent.getRelationships());
				mergeSubEntities(mergeComponent.getMedia(), targetComponent.getMedia());
				mergeSubEntities(mergeComponent.getMetadata(), targetComponent.getMetadata());
				mergeSubEntities(mergeComponent.getTags(), targetComponent.getTags());
				mergeSubEntities(mergeComponent.getResources(), targetComponent.getResources());

				Set<String> targetReviewKey = targetComponent.getReviews().stream().map(review
						-> {
					return review.getComponentReview().uniqueKey();
				}).collect(Collectors.toSet());

				for (ReviewAll reviewAll : mergeComponent.getReviews()) {
					if (targetReviewKey.contains(reviewAll.getComponentReview().uniqueKey()) == false) {
						reviewAll.getComponentReview().setComponentId(targetComponentId);
						reviewAll.getComponentReview().clearKeys();
						for (ComponentReviewPro componentReviewPro : reviewAll.getPros()) {
							componentReviewPro.clearKeys();
						}
						for (ComponentReviewCon componentReviewCon : reviewAll.getCons()) {
							componentReviewCon.clearKeys();
						}
						targetComponent.getReviews().add(reviewAll);
					}
				}

				Set<String> targetQuestionKey = targetComponent.getQuestions().stream().map(question
						-> {
					return question.getQuestion().uniqueKey();
				}).collect(Collectors.toSet());

				for (QuestionAll questionAll : mergeComponent.getQuestions()) {
					if (targetQuestionKey.contains(questionAll.getQuestion().uniqueKey()) == false) {
						questionAll.getQuestion().setComponentId(targetComponentId);
						questionAll.getQuestion().clearKeys();
						for (ComponentQuestionResponse response : questionAll.getResponds()) {
							response.setComponentId(targetComponentId);
							response.clearKeys();
						}
						targetComponent.getQuestions().add(questionAll);
					}
				}

				FileHistoryOption options = new FileHistoryOption();
				options.setSkipRequiredAttributes(Boolean.TRUE);
				options.setUploadQuestions(Boolean.TRUE);
				options.setUploadReviews(Boolean.TRUE);
				options.setUploadTags(Boolean.TRUE);

				if (saveOptions != null) {
					options = saveOptions;
				}
				saveFullComponent(targetComponent, options);

				//move evaluations over to target
				//Keep in mind the change requests will need to be discarded (cascade delete should take care of it)
				//New one need to be created upon editing the evaluation
				Evaluation existingEvaluations = new Evaluation();
				existingEvaluations.setOriginComponentId(mergeComponent.getComponent().getComponentId());
				List<Evaluation> evaluations = existingEvaluations.findByExample();
				for (Evaluation evaluation : evaluations) {
					evaluation.setOriginComponentId(targetComponentId);
					persistenceService.persist(evaluation);
				}

				//move any watches from merge to target
				UserWatch userWatchExample = new UserWatch();
				userWatchExample.setComponentId(toMergeComponentId);
				List<UserWatch> userWatches = userWatchExample.findByExampleProxy();
				for (UserWatch userWatch : userWatches) {
					userWatch.setComponentId(targetComponentId);
					userWatch.populateBaseUpdateFields();
					persistenceService.persist(userWatch);
				}

				//remove mergeComponent
				cascadeDeleteOfComponent(mergeComponent.getComponent().getComponentId());

				cleanupCache(toMergeComponentId);
				cleanupCache(targetComponentId);

				//Re-pull
				targetComponent = getFullComponent(targetComponentId);

			} else {
				throw new OpenStorefrontRuntimeException("Unable to find target component: " + targetComponentId, "Check data");
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find merge component: " + toMergeComponentId, "Check data");
		}
		return targetComponent.getComponent();
	}

	private <T extends BaseComponent> void mergeSubEntities(List<T> entities, List<T> targetEntities)
	{
		//If there is bad data remove it from initial target
		for (int i = targetEntities.size() - 1; i >= 0; i--) {
			if (StringUtils.isBlank(targetEntities.get(i).uniqueKey())) {
				T badRecord = targetEntities.remove(i);
				LOG.log(Level.WARNING, MessageFormat.format("Bad record (found during merge...it was removed): {0}", StringProcessor.printObject(badRecord)));
			}
		}

		Map<String, List<T>> targetKeyMap = targetEntities.stream().collect(Collectors.groupingBy(T::uniqueKey));
		for (T entity : entities) {

			if (targetKeyMap.containsKey(entity.uniqueKey()) == false) {
				entity.clearKeys();
				targetEntities.add(entity);
			}
		}

		// Create Temporary Target Entities List
		List<T> tempTargetEntities = new ArrayList<>();

		// Copy Target Entities
		targetEntities.forEach(item -> tempTargetEntities.add(item));

		Map<String, List<T>> mergeKeyMap = entities.stream().collect(Collectors.groupingBy(T::uniqueKey));
		for (T targetEntity : tempTargetEntities) {

			if (mergeKeyMap.containsKey(targetEntity.uniqueKey()) == false) {
				targetEntities.remove(targetEntity);
			}
		}
	}

	public List<ComponentRecordStatistic> findTopViewedComponents(Integer maxRecords)
	{
		List<ComponentRecordStatistic> recordStatistics = new ArrayList<>();

		String limit = "";
		if (maxRecords != null) {
			limit = "LIMIT " + maxRecords;
		}

		String query = "select count(*) as views, componentId from " + ComponentTracking.class.getSimpleName() + " group by componentId order by views DESC " + limit;

		List<ODocument> documents = persistenceService.query(query, null);
		for (ODocument document : documents) {
			ComponentRecordStatistic componentRecordStatistic = new ComponentRecordStatistic();
			componentRecordStatistic.setComponentId(document.field("componentId"));
			componentRecordStatistic.setViews(document.field("views"));
			componentRecordStatistic.setComponentName(getComponentName(componentRecordStatistic.getComponentId()));
			recordStatistics.add(componentRecordStatistic);
		}

		return recordStatistics;
	}

	public List<ComponentType> getAllComponentTypes()
	{
		List<ComponentType> componentTypes;
		Element element = OSFCacheManager.getComponentTypeCache().get(OSFCacheManager.ALLCODE_KEY);
		if (element != null) {
			componentTypes = (List<ComponentType>) element.getObjectValue();
		} else {
			ComponentType componentType = new ComponentType();
			componentTypes = componentType.findByExample();
			element = new Element(OSFCacheManager.ALLCODE_KEY, componentTypes);
			OSFCacheManager.getComponentTypeCache().put(element);
		}

		return componentTypes;
	}

	public ComponentType saveComponentType(ComponentType componentType)
	{
		ComponentType existing = persistenceService.findById(ComponentType.class, componentType.getComponentType());
		if (existing != null) {
			existing.updateFields(componentType);
			componentType = persistenceService.persist(existing);
		} else {
			componentType.populateBaseCreateFields();
			componentType = persistenceService.persist(componentType);
		}
		OSFCacheManager.getComponentTypeCache().removeAll();

		return componentType;
	}

	public void removeComponentType(String componentType, String newComponentType)
	{
		ComponentType componentTypeFound = persistenceService.findById(ComponentType.class, componentType);
		if (componentTypeFound != null) {

			boolean inactivate = true;
			if (StringUtils.isNotBlank(newComponentType)) {
				ComponentType newType = persistenceService.findById(ComponentType.class, newComponentType);

				if (newType != null) {
					//migrate data
					Component setComponent = new Component();
					setComponent.setComponentType(newComponentType);

					Component whereComponent = new Component();
					whereComponent.setComponentType(componentType);

					persistenceService.updateByExample(Component.class, setComponent, whereComponent);

					FileDataMap setfileDataMap = new FileDataMap();
					setfileDataMap.setDefaultComponentType(newComponentType);

					FileDataMap wherefileDataMap = new FileDataMap();
					wherefileDataMap.setDefaultComponentType(componentType);
					persistenceService.updateByExample(FileDataMap.class, setfileDataMap, wherefileDataMap);

					//remove restrictions
					AttributeType attributeTypeExample = new AttributeType();
					List<AttributeType> allAttributes = attributeTypeExample.findByExample();
					List<AttributeType> updateAttributes = new ArrayList<>();
					for (AttributeType attributeType : allAttributes) {

						boolean addToUpdate = false;
						if (attributeType.getRequiredRestrictions() != null && !attributeType.getRequiredRestrictions().isEmpty()) {
							for (int i = attributeType.getRequiredRestrictions().size() - 1; i >= 0; i--) {
								String checkType = attributeType.getRequiredRestrictions().get(i).getComponentType();
								if (checkType.equals(componentType)) {
									attributeType.getRequiredRestrictions().remove(i);
									addToUpdate = true;
								}
							}
						}

						if (attributeType.getAssociatedComponentTypes() != null && !attributeType.getAssociatedComponentTypes().isEmpty()) {
							for (int i = attributeType.getAssociatedComponentTypes().size() - 1; i >= 0; i--) {
								String checkType = attributeType.getAssociatedComponentTypes().get(i).getComponentType();
								if (checkType.equals(componentType)) {
									attributeType.getAssociatedComponentTypes().remove(i);
									addToUpdate = true;
								}
							}
						}

						if (addToUpdate) {
							updateAttributes.add(attributeType);
						}

						for (AttributeType attributeTypeUpdated : updateAttributes) {
							componentService.getAttributeService().saveAttributeType(attributeTypeUpdated, false);
						}
					}

					//remove
					inactivate = false;
					persistenceService.delete(componentTypeFound);
				} else {
					LOG.log(Level.WARNING, MessageFormat.format("Unable to find new component type: {0}  to migrate data to.  Inactivating component type: {1}", new Object[]{
						newComponentType, componentType
					}));
				}
			}

			if (inactivate) {
				componentTypeFound.setActiveStatus(ComponentType.INACTIVE_STATUS);
				componentTypeFound.populateBaseUpdateFields();
				persistenceService.persist(componentTypeFound);
			}
			OSFCacheManager.getComponentCache().removeAll();
			OSFCacheManager.getComponentTypeCache().removeAll();
		}
	}

	public ComponentTypeTemplate saveComponentTemplate(ComponentTypeTemplate componentTypeTemplate)
	{
		Objects.requireNonNull(componentTypeTemplate);

		ComponentTypeTemplate existing = persistenceService.findById(ComponentTypeTemplate.class, componentTypeTemplate.getTemplateId());
		if (existing != null) {
			existing.updateFields(componentTypeTemplate);
			componentTypeTemplate = persistenceService.persist(existing);
		} else {
			if (StringUtil.isBlank(componentTypeTemplate.getTemplateId())) {
				componentTypeTemplate.setTemplateId(persistenceService.generateId());
			}
			componentTypeTemplate.populateBaseCreateFields();
			componentTypeTemplate = persistenceService.persist(componentTypeTemplate);
		}
		return componentTypeTemplate;
	}

	public void removeComponentTypeTemplate(String templateId)
	{
		ComponentTypeTemplate template = persistenceService.findById(ComponentTypeTemplate.class, templateId);
		if (template != null) {
			template.setActiveStatus(ComponentType.INACTIVE_STATUS);
			template.populateBaseUpdateFields();
			persistenceService.persist(template);
		}
	}

	public void deleteComponentTypeTemplate(String templateId)
	{
		ComponentTypeTemplate template = persistenceService.findById(ComponentTypeTemplate.class, templateId);
		if (template != null) {
			ComponentType componentType = new ComponentType();
			componentType.setComponentTypeTemplate(templateId);

			List<ComponentType> types = componentType.findByExample();
			if (types.isEmpty()) {
				persistenceService.delete(template);
			} else {
				throw new OpenStorefrontRuntimeException("Unable to delete; Entry types are point to the template.", "Remove the template from entry types (both active and inactive) and try again.");
			}
		}
	}

	public Component approveComponent(String componentId)
	{
		Component component = persistenceService.findById(Component.class, componentId);
		if (component != null) {
			if (ApprovalStatus.APPROVED.equals(component.getApprovalState()) == false) {
				component.setApprovedUser(SecurityUtil.getCurrentUserName());
				component.setApprovedDts(TimeUtil.currentDate());
				component.setApprovalState(ApprovalStatus.APPROVED);
				component.populateBaseUpdateFields();
				persistenceService.persist(component);

				sendApprovalNotification(component);
				updateComponentLastActivity(componentId);
			}
		}
		return component;
	}

	public Component changeOwner(String componentId, String newOwner)
	{
		Component component = persistenceService.findById(Component.class, componentId);
		if (component != null) {
			component.setCreateUser(newOwner);
			component.populateBaseUpdateFields();
			persistenceService.persist(component);
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find component.", "Check id passed: " + componentId);
		}
		return component;
	}

	public Component createPendingChangeComponent(String parentComponentId)
	{
		//copy (this is a seperate transaction)
		Component component = ServiceProxy.getProxy().getComponentService().copy(parentComponentId);

		//then set component to pending changes
		component = persistenceService.findById(Component.class, component.getComponentId());

		component.setActiveStatus(Component.PENDING_STATUS);
		component.setApprovalState(ApprovalStatus.NOT_SUBMITTED);
		component.setName(component.getName().replace(COPY_MARKER, ""));
		component.setApprovedUser(null);
		component.setApprovedDts(null);
		component.setSubmittedDts(null);
		component.setPendingChangeId(parentComponentId);
		persistenceService.persist(component);

		return component;
	}

	public Component mergePendingChange(String componentIdOfPendingChange)
	{
		Component mergedComponent = null;
		Component pendingChangeComponent = persistenceService.findById(Component.class, componentIdOfPendingChange);
		if (pendingChangeComponent != null) {

			Component mainComponent = persistenceService.findById(Component.class, pendingChangeComponent.getPendingChangeId());
			if (mainComponent != null) {

				LOG.log(Level.FINEST, "Merge Component");
				FileHistoryOption fileHistoryOption = new FileHistoryOption();
				fileHistoryOption.setUploadTags(Boolean.TRUE);
				fileHistoryOption.setSkipRequiredAttributes(Boolean.TRUE);
				mergedComponent = merge(componentIdOfPendingChange, mainComponent.getComponentId(), fileHistoryOption);

				LOG.log(Level.FINEST, "Send notification of approval (if requested)");
				//Temp change just to carry of the notification request
				mergedComponent.setNotifyOfApprovalEmail(pendingChangeComponent.getNotifyOfApprovalEmail());
				sendChangeRequestNotification(mergedComponent);

			} else {
				LOG.log(Level.WARNING, "Parent component doesn't exist unable to merge pending change. (Removing pending change)");
				cascadeDeleteOfComponent(componentIdOfPendingChange);
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find pending change component.", "Refresh and check data.");
		}

		return mergedComponent;
	}

	public void saveTemplateBlock(TemplateBlock templateBlock)
	{
		TemplateBlock existing = persistenceService.findById(TemplateBlock.class, templateBlock.getTemplateBlockId());
		if (existing != null) {
			existing.updateFields(templateBlock);
			persistenceService.persist(existing);
		} else {
			templateBlock.setTemplateBlockId(persistenceService.generateId());
			templateBlock.populateBaseCreateFields();
			persistenceService.persist(templateBlock);
		}
	}

	public void deleteTemplateBlock(String templateBlockId)
	{
		TemplateBlock existing = persistenceService.findById(TemplateBlock.class, templateBlockId);
		if (existing != null) {
			persistenceService.delete(existing);
		}
	}

	public String resolveComponentIcon(String componentId)
	{
		String iconMediaId = null;
		Element element = OSFCacheManager.getComponentIconCache().get(componentId);
		if (element != null) {
			String componentMediaId = (String) element.getObjectValue();
			if (StringUtils.isNotBlank(componentMediaId)) {
				iconMediaId = componentMediaId;
			}
		} else {
			ComponentMedia componentMediaExample = new ComponentMedia();
			componentMediaExample.setActiveStatus(ComponentMedia.ACTIVE_STATUS);
			componentMediaExample.setIconFlag(Boolean.TRUE);

			List<ComponentMedia> allIconMedia = componentMediaExample.findByExample();
			Set<String> mediaWithIcons = new HashSet<>();
			for (ComponentMedia componentMedia : allIconMedia) {
				Element newElement = new Element(componentMedia.getComponentId(), componentMedia.getComponentMediaId());
				OSFCacheManager.getComponentIconCache().put(newElement);
				if (componentMedia.getComponentId().equals(componentId)) {
					iconMediaId = componentMedia.getComponentMediaId();
				}
				mediaWithIcons.add(componentMedia.getComponentId());
			}
			if (iconMediaId == null) {
				//set all missing icon to blank to warm cache
				Element newElement = new Element(componentId, "");
				OSFCacheManager.getComponentIconCache().put(newElement);
			}
			String query = "select componentId from " + Component.class.getSimpleName();
			List<ODocument> documents = persistenceService.query(query, null);
			for (ODocument document : documents) {
				String id = document.field("componentId");
				if (!mediaWithIcons.contains(id)) {
					Element newElement = new Element(id, "");
					OSFCacheManager.getComponentIconCache().put(newElement);
				}
			}

		}
		return iconMediaId;
	}

	public String resolveComponentTypeIcon(String componentType)
	{
		String typeIcon = null;
		List<ComponentType> componentTypes = getAllComponentTypes();
		for (ComponentType componentTypeLocal : componentTypes) {
			if (componentTypeLocal.getComponentType().equals(componentType)) {
				typeIcon = componentTypeLocal.getIconUrl();
			}
		}

		return typeIcon;
	}

}
