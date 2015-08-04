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

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import com.orientechnologies.orient.core.record.impl.ODocument;
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.api.ComponentService;
import edu.usu.sdl.openstorefront.service.api.ComponentServicePrivate;
import edu.usu.sdl.openstorefront.service.io.integration.BaseIntegrationHandler;
import static edu.usu.sdl.openstorefront.service.io.integration.JiraIntegrationHandler.STATUS_FIELD;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.service.manager.JobManager;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.service.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.service.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.service.query.QueryType;
import edu.usu.sdl.openstorefront.service.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.service.transfermodel.AlertContext;
import edu.usu.sdl.openstorefront.service.transfermodel.AttributeXrefModel;
import edu.usu.sdl.openstorefront.service.transfermodel.BulkComponentAttributeChange;
import edu.usu.sdl.openstorefront.service.transfermodel.ComponentAll;
import edu.usu.sdl.openstorefront.service.transfermodel.ComponentUploadOption;
import edu.usu.sdl.openstorefront.service.transfermodel.ErrorInfo;
import edu.usu.sdl.openstorefront.service.transfermodel.IntegrationAll;
import edu.usu.sdl.openstorefront.service.transfermodel.QuestionAll;
import edu.usu.sdl.openstorefront.service.transfermodel.ReviewAll;
import edu.usu.sdl.openstorefront.sort.BeanComparator;
import edu.usu.sdl.openstorefront.sort.SortUtil;
import edu.usu.sdl.openstorefront.storage.model.AlertType;
import edu.usu.sdl.openstorefront.storage.model.ApprovalStatus;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.storage.model.AttributeXRefType;
import edu.usu.sdl.openstorefront.storage.model.BaseComponent;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttributePk;
import edu.usu.sdl.openstorefront.storage.model.ComponentContact;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSection;
import edu.usu.sdl.openstorefront.storage.model.ComponentExternalDependency;
import edu.usu.sdl.openstorefront.storage.model.ComponentIntegration;
import edu.usu.sdl.openstorefront.storage.model.ComponentIntegrationConfig;
import edu.usu.sdl.openstorefront.storage.model.ComponentMedia;
import edu.usu.sdl.openstorefront.storage.model.ComponentMetadata;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestion;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.storage.model.ComponentRelationship;
import edu.usu.sdl.openstorefront.storage.model.ComponentResource;
import edu.usu.sdl.openstorefront.storage.model.ComponentReview;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewCon;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewConPk;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewPro;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewProPk;
import edu.usu.sdl.openstorefront.storage.model.ComponentTag;
import edu.usu.sdl.openstorefront.storage.model.ComponentTracking;
import edu.usu.sdl.openstorefront.storage.model.ComponentType;
import edu.usu.sdl.openstorefront.storage.model.ComponentUpdateQueue;
import edu.usu.sdl.openstorefront.storage.model.ErrorTypeCode;
import edu.usu.sdl.openstorefront.storage.model.ReviewCon;
import edu.usu.sdl.openstorefront.storage.model.ReviewPro;
import edu.usu.sdl.openstorefront.storage.model.RunStatus;
import edu.usu.sdl.openstorefront.storage.model.TrackEventCode;
import edu.usu.sdl.openstorefront.storage.model.UserMessage;
import edu.usu.sdl.openstorefront.storage.model.UserMessageType;
import edu.usu.sdl.openstorefront.storage.model.UserWatch;
import edu.usu.sdl.openstorefront.util.Convert;
import edu.usu.sdl.openstorefront.util.LockSwitch;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentAdminView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentAdminWrapper;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentAttributeView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentContactView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentDetailView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentEvaluationView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentExternalDependencyView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentFilterParams;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentMediaView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentMetadataView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentQuestionResponseView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentQuestionView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentRelationshipView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentResourceView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentReviewProCon;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentReviewView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentSearchView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentTrackingCompleteWrapper;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentTrackingResult;
import edu.usu.sdl.openstorefront.web.rest.model.FilterQueryParams;
import edu.usu.sdl.openstorefront.web.rest.model.RequiredForComponent;
import edu.usu.sdl.openstorefront.web.rest.model.SearchResultAttribute;
import edu.usu.sdl.openstorefront.web.viewmodel.LookupModel;
import edu.usu.sdl.openstorefront.web.viewmodel.SystemErrorModel;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import net.sf.ehcache.Element;
import net.sourceforge.stripes.util.bean.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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

	public ComponentServiceImpl(PersistenceService persistenceService)
	{
		super(persistenceService);
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
		return deactivateBaseComponent(subComponentClass, pk, true, null);
	}

	private <T extends BaseComponent> T deactivateBaseComponent(Class<T> subComponentClass, Object pk, boolean updateComponentActivity, String updateUser)
	{
		T found = persistenceService.findById(subComponentClass, pk);
		if (found != null) {
			found.setActiveStatus(T.INACTIVE_STATUS);
			found.setUpdateDts(TimeUtil.currentDate());
			if (StringUtils.isBlank(updateUser)) {
				updateUser = SecurityUtil.getCurrentUserName();
			}
			found.setUpdateUser(updateUser);
			persistenceService.persist(found);

			if (updateComponentActivity) {
				updateComponentLastActivity(found.getComponentId());
			}
		}
		return found;
	}

	@Override
	public <T extends BaseComponent> T activateBaseComponent(Class<T> subComponentClass, Object pk)
	{
		T found = persistenceService.findById(subComponentClass, pk);
		if (found != null) {
			found.setActiveStatus(T.ACTIVE_STATUS);
			found.populateBaseUpdateFields();
			persistenceService.persist(found);

			updateComponentLastActivity(found.getComponentId());
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
			if (found instanceof ComponentResource) {
				removeLocalResource((ComponentResource) found);
			}
			if (found instanceof ComponentMedia) {
				removeLocalMedia((ComponentMedia) found);
			}

			persistenceService.delete(found);

			if (updateComponentActivity) {
				updateComponentLastActivity(componentId);
			}
		}
	}

	private void removeLocalResource(ComponentResource componentResource)
	{
		//Note: this can't be rolled back
		Path path = componentResource.pathToResource();
		if (path != null) {
			if (path.toFile().exists()) {
				path.toFile().delete();
			}
		}
	}

	private void removeLocalMedia(ComponentMedia componentMedia)
	{
		//Note: this can't be rolled back
		Path path = componentMedia.pathToMedia();
		if (path != null) {
			if (path.toFile().exists()) {
				path.toFile().delete();
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

			if (subComponentClass.getName().equals(ComponentResource.class.getName())) {
				List<T> resources = persistenceService.queryByExample(subComponentClass, example);
				resources.forEach(resource -> {
					removeLocalResource((ComponentResource) resource);
				});
			}
			if (subComponentClass.getName().equals(ComponentMedia.class.getName())) {
				List<T> media = persistenceService.queryByExample(subComponentClass, example);
				media.forEach(mediaItem -> {
					removeLocalMedia((ComponentMedia) mediaItem);
				});
			}
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
		doDeactivateComponent(componentId);
		getUserService().removeAllWatchesForComponent(componentId);
		getSearchService().deleteById(componentId);
	}

	@Override
	public void doDeactivateComponent(String componentId)
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
			List<Component> componentsToIndex = new ArrayList<>();
			componentsToIndex.add(component);
			getSearchService().indexComponents(componentsToIndex);
		}
		return component;
	}

	@Override
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

	@Override
	public List<ComponentSearchView> getComponents()
	{
		List<ComponentSearchView> componentSearchViews = new ArrayList<>();

		Component componentExample = new Component();
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);
		componentExample.setApprovalState(ApprovalStatus.APPROVED);
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
		example.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
		return persistenceService.queryByExample(ComponentAttribute.class, new QueryByExample(example));
	}

	@Override
	public ComponentDetailView getComponentDetails(String componentId)
	{

		ComponentDetailView result = new ComponentDetailView();
		Component tempComponent = persistenceService.findById(Component.class, componentId);
		if (tempComponent == null) {
			throw new OpenStorefrontRuntimeException("Unable to find component.", "Check id: " + componentId);
		}

		result.setComponentDetails(tempComponent);
		result.setApprovalState(tempComponent.getApprovalState());

		//Pull relationships direct relationships
		ComponentRelationship componentRelationshipExample = new ComponentRelationship();
		componentRelationshipExample.setComponentId(componentId);
		result.getRelationships().addAll(ComponentRelationshipView.toViewList(componentRelationshipExample.findByExample()));

		//Pull indirect
		componentRelationshipExample = new ComponentRelationship();
		componentRelationshipExample.setRelatedComponentId(componentId);
		result.getRelationships().addAll(ComponentRelationshipView.toViewList(componentRelationshipExample.findByExample()));

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
		componentResources = SortUtil.sortComponentResource(componentResources);
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

		ComponentTracking componentTrackingExample = new ComponentTracking();
		componentTrackingExample.setActiveStatus(ComponentTracking.ACTIVE_STATUS);
		componentTrackingExample.setTrackEventTypeCode(TrackEventCode.VIEW);
		componentTrackingExample.setComponentId(componentId);
		result.setComponentViews(persistenceService.countByExample(componentTrackingExample));

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

		List<ComponentEvaluationSection> evaluationSections = getBaseComponent(ComponentEvaluationSection.class, componentId);
		result.setEvaluation(ComponentEvaluationView.toViewFromStorage(evaluationSections));

		result.setToday(new Date());

		return result;
	}

	@Override
	public void saveComponentAttribute(ComponentAttribute attribute)
	{
		getComponentServicePrivate().saveComponentAttribute(attribute, true);
	}

	@Override
	public void saveComponentAttribute(ComponentAttribute attribute, boolean updateLastActivity)
	{
		Objects.requireNonNull(attribute, "Requires Component Attrubute");
		Objects.requireNonNull(attribute.getComponentAttributePk(), "Requires Component Attrubute PK");
		Objects.requireNonNull(attribute.getComponentAttributePk().getAttributeType(), "Requires Component Attrubute PK Attribute Type");
		Objects.requireNonNull(attribute.getComponentAttributePk().getAttributeCode(), "Requires Component Attrubute PK Attribute Code");
		Objects.requireNonNull(attribute.getComponentAttributePk().getComponentId(), "Requires Component Attrubute PK Component Id");

		AttributeType type = persistenceService.findById(AttributeType.class, attribute.getComponentAttributePk().getAttributeType());

		AttributeCodePk pk = new AttributeCodePk();
		pk.setAttributeCode(attribute.getComponentAttributePk().getAttributeCode());
		pk.setAttributeType(attribute.getComponentAttributePk().getAttributeType());
		AttributeCode code = persistenceService.findById(AttributeCode.class, pk);

		if (type != null && code != null) {
			if (type.getAllowMultipleFlg() == false) {
				ComponentAttribute example = new ComponentAttribute();
				example.setComponentAttributePk(new ComponentAttributePk());
				example.getComponentAttributePk().setAttributeType(attribute.getComponentAttributePk().getAttributeType());
				example.getComponentAttributePk().setComponentId(attribute.getComponentAttributePk().getComponentId());
				persistenceService.deleteByExample(example);
			}

			ComponentAttribute oldAttribute = persistenceService.findById(ComponentAttribute.class, attribute.getComponentAttributePk());
			if (oldAttribute != null) {
				oldAttribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
				oldAttribute.updateFields(attribute);
				persistenceService.persist(oldAttribute);
			} else {
				attribute.populateBaseCreateFields();
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

		cleanupCache(componentId);

		ComponentUpdateQueue componentUpdateQueue = new ComponentUpdateQueue();
		componentUpdateQueue.setComponentId(componentId);
		componentUpdateQueue.setUpdateDts(TimeUtil.currentDate());
		componentUpdateQueue.setNodeId(OpenStorefrontConstant.getNodeName());
		componentUpdateQueue.setUpdateId(persistenceService.generateId());
		componentUpdateQueue.populateBaseCreateFields();
		persistenceService.persist(componentUpdateQueue);

	}

	private void cleanupCache(String componentId)
	{
		Objects.requireNonNull(componentId, "Component Id is required");

		OSFCacheManager.getComponentCache().remove(componentId);
		OSFCacheManager.getComponentLookupCache().remove(componentId);
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
			oldContact.updateFields(contact);
			persistenceService.persist(oldContact);
		} else {
			contact.setContactId(persistenceService.generateId());
			contact.populateBaseCreateFields();
			persistenceService.persist(contact);
		}
		getOrganizationService().addOrganization(contact.getOrganization());

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
			oldDependency.updateFields(dependency);
			persistenceService.persist(oldDependency);
		} else {
			dependency.setDependencyId(persistenceService.generateId());
			dependency.populateBaseCreateFields();
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

	@Override
	public void saveComponentEvaluationSection(List<ComponentEvaluationSection> sections)
	{
		sections.forEach(section -> {
			saveComponentEvaluationSection(section, false);
		});
		if (!sections.isEmpty()) {
			updateComponentLastActivity(sections.get(0).getComponentId());
		}
	}

	private void saveComponentEvaluationSection(ComponentEvaluationSection section, boolean updateLastActivity)
	{
		ComponentEvaluationSection oldSection = persistenceService.findById(ComponentEvaluationSection.class, section.getComponentEvaluationSectionPk());
		if (oldSection != null) {
			oldSection.updateFields(section);
			persistenceService.persist(oldSection);
		} else {
			section.populateBaseCreateFields();
			persistenceService.persist(section);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(section.getComponentId());
		}
	}

	@Override
	public ComponentMedia saveComponentMedia(ComponentMedia media)
	{
		return saveComponentMedia(media, true);
	}

	private ComponentMedia saveComponentMedia(ComponentMedia media, boolean updateLastActivity)
	{
		ComponentMedia oldMedia = persistenceService.findById(ComponentMedia.class, media.getComponentMediaId());
		if (oldMedia != null) {
			if (StringUtils.isNotBlank(media.getLink())) {
				removeLocalMedia(oldMedia);
			}
			oldMedia.updateFields(media);
			persistenceService.persist(oldMedia);
			media = oldMedia;
		} else {
			media.setComponentMediaId(persistenceService.generateId());
			media.updateFields(media);
			persistenceService.persist(media);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(media.getComponentId());
		}
		return media;
	}

	@Override
	public void saveComponentMetadata(ComponentMetadata metadata)
	{
		saveComponentMetadata(metadata, true);
	}

	private void saveComponentMetadata(ComponentMetadata metadata, boolean updateLastActivity)
	{
		ComponentMetadata oldMetadata = persistenceService.findById(ComponentMetadata.class, metadata.getMetadataId());
		if (oldMetadata != null) {
			oldMetadata.updateFields(metadata);
			persistenceService.persist(oldMetadata);
		} else {
			metadata.setMetadataId(persistenceService.generateId());
			metadata.populateBaseCreateFields();
			persistenceService.persist(metadata);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(metadata.getComponentId());
		}
	}

	@Override
	public ComponentRelationship saveComponentRelationship(ComponentRelationship componentRelationship)
	{
		return saveComponentRelationship(componentRelationship, true);
	}

	private ComponentRelationship saveComponentRelationship(ComponentRelationship componentRelationship, boolean updateLastActivity)
	{
		ComponentRelationship componentRelationshipExisting = persistenceService.findById(ComponentRelationship.class, componentRelationship.getComponentRelationshipId());

		if (componentRelationshipExisting == null) {
			//handle duplicates
			ComponentRelationship relationshipCheck = new ComponentRelationship();
			relationshipCheck.setRelationshipType(componentRelationship.getRelationshipType());
			relationshipCheck.setComponentId(componentRelationship.getComponentId());
			relationshipCheck.setRelatedComponentId(componentRelationship.getRelatedComponentId());

			QueryByExample queryByExample = new QueryByExample(relationshipCheck);
			queryByExample.setReturnNonProxied(false);

			componentRelationshipExisting = persistenceService.queryOneByExample(ComponentRelationship.class, queryByExample);
		}

		if (componentRelationshipExisting != null) {
			componentRelationshipExisting.updateFields(componentRelationship);
			componentRelationship = persistenceService.persist(componentRelationshipExisting);
		} else {
			componentRelationship.setComponentRelationshipId(persistenceService.generateId());
			componentRelationship.populateBaseCreateFields();
			componentRelationship = persistenceService.persist(componentRelationship);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(componentRelationship.getComponentId());
		}
		return componentRelationship;
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
			oldQuestion.updateFields(question);
			persistenceService.persist(oldQuestion);
			question = oldQuestion;
		} else {
			question.setQuestionId(persistenceService.generateId());
			question.populateBaseCreateFields();
			persistenceService.persist(question);
		}
		handleUserDataAlert(question);
		getOrganizationService().addOrganization(question.getOrganization());

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
			oldResponse.updateFields(response);
			persistenceService.persist(oldResponse);
			response = oldResponse;
		} else {
			response.setResponseId(persistenceService.generateId());
			response.populateBaseCreateFields();
			persistenceService.persist(response);
		}
		handleUserDataAlert(response);
		getOrganizationService().addOrganization(response.getOrganization());

		if (updateLastActivity) {
			updateComponentLastActivity(response.getComponentId());
		}
	}

	@Override
	public ComponentResource saveComponentResource(ComponentResource resource)
	{
		return saveComponentResource(resource, true);
	}

	private ComponentResource saveComponentResource(ComponentResource resource, boolean updateLastActivity)
	{
		ComponentResource oldResource = persistenceService.findById(ComponentResource.class, resource.getResourceId());
		if (oldResource != null) {

			if (StringUtils.isNotBlank(resource.getLink())) {
				removeLocalResource(oldResource);
			}
			oldResource.updateFields(resource);
			persistenceService.persist(oldResource);
			resource = oldResource;
		} else {
			resource.setResourceId(persistenceService.generateId());
			resource.populateBaseCreateFields();
			persistenceService.persist(resource);
		}

		if (updateLastActivity) {
			updateComponentLastActivity(resource.getComponentId());
		}
		return resource;
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
			oldReview.updateFields(review);
			persistenceService.persist(oldReview);
			review = oldReview;
		} else {
			review.setComponentReviewId(persistenceService.generateId());
			review.populateBaseCreateFields();
			persistenceService.persist(review);
		}
		handleUserDataAlert(review);
		getOrganizationService().addOrganization(review.getOrganization());

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
			oldCon.updateFields(con);
			persistenceService.persist(oldCon);
		} else {
			con.populateBaseCreateFields();
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
			pro.setActiveStatus(ComponentReviewPro.ACTIVE_STATUS);
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
		getComponentServicePrivate().doSaveComponentTag(tag, true);
	}

	@Override
	public void doSaveComponentTag(ComponentTag tag, boolean updateLastActivity)
	{
		ComponentTag oldTag = persistenceService.findById(ComponentTag.class, tag.getTagId());
		if (oldTag != null) {
			oldTag.updateFields(tag);
			persistenceService.persist(oldTag);
			tag = oldTag;
		} else {
			tag.setTagId(persistenceService.generateId());
			tag.populateBaseCreateFields();
			persistenceService.persist(tag);
		}
		handleUserDataAlert(tag);

		if (updateLastActivity) {
			updateComponentLastActivity(tag.getComponentId());
		}
	}

	private void handleUserDataAlert(Object data)
	{
		AlertContext alertContext = new AlertContext();
		alertContext.setAlertType(AlertType.USER_DATA);
		alertContext.setDataTrigger(data);
		getAlertService().checkAlert(alertContext);
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

	@Override
	public RequiredForComponent saveComponent(RequiredForComponent component)
	{
		getComponentServicePrivate().doSaveComponent(component);
		List<Component> componentsToIndex = new ArrayList<>();
		componentsToIndex.add(component.getComponent());
		getSearchService().indexComponents(componentsToIndex);
		return component;
	}

	@Override
	public RequiredForComponent doSaveComponent(RequiredForComponent component)
	{
		Component oldComponent = persistenceService.findById(Component.class, component.getComponent().getComponentId());

		ReflectionUtil.setDefaultsOnFields(component.getComponent());

		ValidationResult validationResult = component.checkForComplete();
		if (validationResult.valid()) {
			boolean approved = false;
			if (oldComponent != null) {

				if (component.getComponent().compareTo(oldComponent) != 0) {

					if ((ApprovalStatus.PENDING.equals(oldComponent.getApprovalState()) || ApprovalStatus.NOT_SUBMITTED.equals(oldComponent.getApprovalState()))
							&& ApprovalStatus.APPROVED.equals(component.getComponent().getApprovalState())) {
						oldComponent.setApprovalState(component.getComponent().getApprovalState());

						if (StringUtils.isBlank(component.getComponent().getApprovedUser())) {
							component.getComponent().setApprovedUser(SecurityUtil.getCurrentUserName());
						}
						if (component.getComponent().getApprovedDts() == null) {
							component.getComponent().setApprovedDts(TimeUtil.currentDate());
						}
						approved = true;
					} else if (ApprovalStatus.APPROVED.equals(oldComponent.getApprovalState())
							&& (ApprovalStatus.PENDING.equals(component.getComponent().getApprovalState())) || ApprovalStatus.NOT_SUBMITTED.equals(component.getComponent().getApprovalState())) {
						component.getComponent().setApprovedUser(null);
						component.getComponent().setApprovedDts(null);
					}
					oldComponent.updateFields(component.getComponent());

					persistenceService.persist(oldComponent);
					component.setComponentChanged(true);
				}

				component.getAttributes().forEach(attribute -> {
					attribute.getComponentAttributePk().setComponentId(oldComponent.getComponentId());
				});
				component.setAttributeChanged(handleBaseComponetSave(ComponentAttribute.class, component.getAttributes(), oldComponent.getComponentId()));

			} else {

				if (StringUtils.isBlank(component.getComponent().getComponentId())) {
					component.getComponent().setComponentId(persistenceService.generateId());
				}
				component.getComponent().populateBaseCreateFields();
				component.getComponent().setLastActivityDts(TimeUtil.currentDate());

				if (ApprovalStatus.APPROVED.equals(component.getComponent().getApprovalState())) {
					if (StringUtils.isBlank(component.getComponent().getApprovedUser())) {
						component.getComponent().setApprovedUser(SecurityUtil.getCurrentUserName());
					}
					if (component.getComponent().getApprovedDts() == null) {
						component.getComponent().setApprovedDts(TimeUtil.currentDate());
					}
					approved = true;
				}

				persistenceService.persist(component.getComponent());
				component.setComponentChanged(true);

				component.getAttributes().forEach(attribute -> {
					attribute.setComponentId(component.getComponent().getComponentId());
					attribute.getComponentAttributePk().setComponentId(component.getComponent().getComponentId());
					attribute.setCreateUser(component.getComponent().getCreateUser());
					attribute.setUpdateUser(component.getComponent().getUpdateUser());
					saveComponentAttribute(attribute, false);
				});
				component.setAttributeChanged(true);
			}
			getOrganizationService().addOrganization(component.getComponent().getOrganization());

			if (approved) {
				if (StringUtils.isNotBlank(component.getComponent().getNotifyOfApprovalEmail())) {
					UserMessage userMessage = new UserMessage();
					userMessage.setEmailAddress(component.getComponent().getNotifyOfApprovalEmail());
					userMessage.setComponentId(component.getComponent().getComponentId());
					userMessage.setUserMessageType(UserMessageType.APPROVAL_NOTIFICATION);
					userMessage.setCreateUser(OpenStorefrontConstant.SYSTEM_USER);
					userMessage.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
					getUserService().queueUserMessage(userMessage);
				}
			}
			cleanupCache(component.getComponent().getComponentId());
		} else {
			throw new OpenStorefrontRuntimeException(validationResult.toString());
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
	public void importComponents(List<ComponentAll> components, ComponentUploadOption options)
	{
		List<Component> componentsToIndex = new ArrayList<>();
		components.forEach(component -> {
			saveFullComponent(component, options, false);
			componentsToIndex.add(component.getComponent());
		});
		getSearchService().indexComponents(componentsToIndex);
	}

	@Override
	public ComponentAll saveFullComponent(ComponentAll componentAll)
	{
		return saveFullComponent(componentAll, new ComponentUploadOption(), true);
	}

	@Override
	public ComponentAll saveFullComponent(ComponentAll componentAll, ComponentUploadOption options)
	{
		return saveFullComponent(componentAll, options, true);
	}

	private ComponentAll saveFullComponent(ComponentAll componentAll, ComponentUploadOption options, boolean updateIndex)
	{
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

		//Check Attributes
		ValidationModel validationModel = new ValidationModel(component);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			RequiredForComponent requiredForComponent = new RequiredForComponent();
			requiredForComponent.setComponent(component);
			requiredForComponent.getAttributes().addAll(componentAll.getAttributes());

			requiredForComponent = doSaveComponent(requiredForComponent);
			lockSwitch.setSwitched(requiredForComponent.isComponentChanged());
			lockSwitch.setSwitched(requiredForComponent.isAttributeChanged());
		} else {
			throw new OpenStorefrontRuntimeException(validationResult.toString());
		}

		lockSwitch.setSwitched(handleBaseComponetSave(ComponentContact.class, componentAll.getContacts(), component.getComponentId()));
		lockSwitch.setSwitched(handleBaseComponetSave(ComponentEvaluationSection.class, componentAll.getEvaluationSections(), component.getComponentId()));
		lockSwitch.setSwitched(handleBaseComponetSave(ComponentExternalDependency.class, componentAll.getExternalDependencies(), component.getComponentId()));
		lockSwitch.setSwitched(handleBaseComponetSave(ComponentMedia.class, componentAll.getMedia(), component.getComponentId()));
		lockSwitch.setSwitched(handleBaseComponetSave(ComponentMetadata.class, componentAll.getMetadata(), component.getComponentId()));
		lockSwitch.setSwitched(handleBaseComponetSave(ComponentResource.class, componentAll.getResources(), component.getComponentId()));
		lockSwitch.setSwitched(handleBaseComponetSave(ComponentRelationship.class, componentAll.getRelationships(), component.getComponentId()));

		if (options.getUploadTags()) {
			lockSwitch.setSwitched(handleBaseComponetSave(ComponentTag.class, componentAll.getTags(), component.getComponentId()));
		}
		if (options.getUploadQuestions()) {
			for (QuestionAll question : componentAll.getQuestions()) {
				List<ComponentQuestion> questions = new ArrayList<>(1);
				questions.add(question.getQuestion());
				lockSwitch.setSwitched(handleBaseComponetSave(ComponentQuestion.class, questions, component.getComponentId()));
				lockSwitch.setSwitched(handleBaseComponetSave(ComponentQuestionResponse.class, question.getResponds(), component.getComponentId()));
			}
		}

		if (options.getUploadReviews()) {
			for (ReviewAll reviewAll : componentAll.getReviews()) {
				List<ComponentReview> reviews = new ArrayList<>(1);
				reviews.add(reviewAll.getComponentReview());
				lockSwitch.setSwitched(handleBaseComponetSave(ComponentReview.class, reviews, component.getComponentId()));
				lockSwitch.setSwitched(handleBaseComponetSave(ComponentReviewPro.class, reviewAll.getPros(), component.getComponentId()));
				lockSwitch.setSwitched(handleBaseComponetSave(ComponentReviewCon.class, reviewAll.getCons(), component.getComponentId()));
			}
		}

		if (options.getUploadIntegration()) {
			if (componentAll.getIntegrationAll() != null) {
				//Note: this should effect watches or component updating.

				componentAll.getIntegrationAll().getIntegration().setComponentId(component.getComponentId());
				validationModel = new ValidationModel(componentAll.getIntegrationAll().getIntegration());
				validationModel.setConsumeFieldsOnly(true);
				validationResult = ValidationUtil.validate(validationModel);
				if (validationResult.valid()) {

					saveComponentIntegration(componentAll.getIntegrationAll().getIntegration());

					for (ComponentIntegrationConfig config : componentAll.getIntegrationAll().getConfigs()) {
						validationModel = new ValidationModel(config);
						validationModel.setConsumeFieldsOnly(true);
						validationResult = ValidationUtil.validate(validationModel);
						if (validationResult.valid()) {
							config.setComponentId(component.getComponentId());
							saveComponentIntegrationConfig(config);
						}
					}
				} else {
					throw new OpenStorefrontRuntimeException(validationResult.toString());
				}
			}
		}

		if (Component.INACTIVE_STATUS.equals(component.getActiveStatus())) {
			getUserService().removeAllWatchesForComponent(component.getComponentId());
			getSearchService().deleteById(component.getComponentId());
		}

		if (lockSwitch.isSwitched()) {
			getUserService().checkComponentWatches(component);

			if (updateIndex) {
				List<Component> componentsToIndex = new ArrayList<>();
				componentsToIndex.add(component);
				getSearchService().indexComponents(componentsToIndex);
			}
		}

		return componentAll;
	}

	private <T extends BaseComponent> boolean handleBaseComponetSave(Class<T> baseComponentClass, List<T> baseComponents, String componentId)
	{
		boolean changed = false;

		//get existing
		List<T> existingComponents = getBaseComponent(baseComponentClass, componentId, null);
		Map<String, T> existingMap = new HashMap<>();
		for (T entity : existingComponents) {
			existingMap.put(ReflectionUtil.getPKFieldValue(entity), entity);
		}

		Set<String> inputMap = new HashSet<>();
		for (BaseComponent baseComponent : baseComponents) {
			baseComponent.setComponentId(componentId);

			//validate
			ValidationModel validationModel = new ValidationModel(baseComponent);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult validationResult = ValidationUtil.validate(validationModel);
			if (validationResult.valid() == false) {
				throw new OpenStorefrontRuntimeException(validationResult.toString());
			}
			inputMap.add(ReflectionUtil.getPKFieldValue(baseComponent));

			//Look for match
			boolean match = false;
			for (T entity : existingComponents) {
				//compare
				if (entity.compareTo(baseComponent) == 0) {
					match = true;
					inputMap.add(ReflectionUtil.getPKFieldValue(entity));
					break;
				}
			}

			//save new ones
			if (match == false) {
				changed = true;
				if (baseComponent instanceof ComponentContact) {
					saveComponentContact((ComponentContact) baseComponent, false);
				} else if (baseComponent instanceof ComponentAttribute) {
					saveComponentAttribute((ComponentAttribute) baseComponent, false);
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
				} else if (baseComponent instanceof ComponentRelationship) {
					saveComponentRelationship((ComponentRelationship) baseComponent, false);
				} else if (baseComponent instanceof ComponentTag) {
					doSaveComponentTag((ComponentTag) baseComponent, false);
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

		//remove old existing records
		for (String existing : existingMap.keySet()) {
			if (inputMap.contains(existing) == false) {
				BaseComponent oldEnity = existingMap.get(existing);
				try {
					Field pkField = ReflectionUtil.getPKField(oldEnity);
					if (pkField != null) {
						pkField.setAccessible(true);
						deactivateBaseComponent(baseComponentClass, pkField.get(oldEnity), false, oldEnity.getUpdateUser());
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

	@Override
	public void cascadeDeleteOfComponent(String componentId)
	{
		Objects.requireNonNull(componentId, "Component Id is required.");
		log.log(Level.INFO, MessageFormat.format("Attempting to Removing component: {0}", componentId));

		Collection<Class<?>> entityClasses = DBManager.getConnection().getEntityManager().getRegisteredEntities();
		for (Class entityClass : entityClasses) {
			if (ReflectionUtil.BASECOMPONENT_ENTITY.equals(entityClass.getSimpleName()) == false) {
				if (ReflectionUtil.isSubClass(ReflectionUtil.BASECOMPONENT_ENTITY, entityClass)) {
					try {
						deleteBaseComponent((BaseComponent) entityClass.newInstance(), componentId);
					} catch (InstantiationException | IllegalAccessException ex) {
						throw new OpenStorefrontRuntimeException("Class is not a base component class: " + entityClass.getName(), "Check class");
					}
				}
			}
		}
		deleteComponentIntegration(componentId);

		//Delete child relationships
		String parentClearUpdate = "update component set parentComponentId = null where parentComponentId = :parentComponentIdParam";
		Map<String, Object> params = new HashMap<>();
		params.put("parentComponentIdParam", componentId);
		persistenceService.runDbCommand(parentClearUpdate, params);

		Component component = persistenceService.findById(Component.class, componentId);
		persistenceService.delete(component);

		getUserService().removeAllWatchesForComponent(componentId);
		getSearchService().deleteById(componentId);
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

		if (StringUtils.isBlank(media.getComponentMediaId())) {
			media = saveComponentMedia(media);
		}
		media.setFileName(media.getComponentMediaId());
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

		if (StringUtils.isBlank(resource.getResourceId())) {
			resource.setResourceId(persistenceService.generateId());
		}
		resource.setFileName(resource.getResourceId());
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
		parameters.put("approvedStateParam", ApprovalStatus.APPROVED);

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
			saveComponentReview(review, false);

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

			updateComponentLastActivity(review.getComponentId());
		}

		return validationResult;
	}

	@Override
	public void mapComponentAttributes(Issue issue, ComponentIntegrationConfig integrationConfig)
	{
		Objects.requireNonNull(issue, "Jira Issue Required");
		Objects.requireNonNull(integrationConfig, "Integration Config Required");

		log.finer("Pull Xref Mapping");
		AttributeXrefModel attributeXrefModel = new AttributeXrefModel();
		attributeXrefModel.setIntegrationType(integrationConfig.getIntegrationType());
		attributeXrefModel.setProjectKey(integrationConfig.getProjectType());
		attributeXrefModel.setIssueType(integrationConfig.getIssueType());

		List<AttributeXRefType> xrefAttributeTypes = getAttributeService().getAttributeXrefTypes(attributeXrefModel);
		Map<String, Map<String, String>> xrefAttributeMaps = getAttributeService().getAttributeXrefMapFieldMap();

		boolean componentChanged = false;
		for (AttributeXRefType xrefAttributeType : xrefAttributeTypes) {

			String jiraValue = null;
			if (STATUS_FIELD.equals(xrefAttributeType.getFieldName())) {
				jiraValue = issue.getStatus().getName();
			} else {
				IssueField jiraField = issue.getField(xrefAttributeType.getFieldId());
				if (jiraField != null) {
					if (jiraField.getValue() instanceof JSONObject) {
						JSONObject json = (JSONObject) jiraField.getValue();
						try {
							jiraValue = json.getString("value");
						} catch (JSONException ex) {
							throw new OpenStorefrontRuntimeException("Unable to get field value from: " + jiraField.getValue(), ErrorTypeCode.INTEGRATION);
						}
					} else {
						if (jiraField.getValue() != null) {
							jiraValue = jiraField.getValue().toString();
						}
					}
				} else {
					throw new OpenStorefrontRuntimeException("Unable to find Jira Field: " + xrefAttributeType.getFieldName(), "Update mapping to match jira.", ErrorTypeCode.INTEGRATION);
				}
			}

			if (StringUtils.isBlank(jiraValue)) {
				AttributeType attributeType = persistenceService.findById(AttributeType.class, xrefAttributeType.getAttributeType());
				if (attributeType != null) {
					if (Convert.toBoolean(attributeType.getRequiredFlg()) == false) {
						log.log(Level.FINEST, "Jira Value is Blank....remove any existing component attribute since Attribute type is not require.");
						ComponentAttributePk componentAttributePk = new ComponentAttributePk();
						componentAttributePk.setComponentId(integrationConfig.getComponentId());
						componentAttributePk.setAttributeType(xrefAttributeType.getAttributeType());
						ComponentAttribute componentAttributeExample = new ComponentAttribute();
						componentAttributeExample.setComponentAttributePk(componentAttributePk);
						persistenceService.deleteByExample(componentAttributeExample);

						componentChanged = true;
					} else {
						log.log(Level.WARNING, MessageFormat.format("Attribute Type is required and Integration is returned a empty value.  Keeping exisiting value on component: {0}  Attribute Type: {1}",
								new Object[]{getComponentService().getComponentName(integrationConfig.getComponentId()), attributeType.getDescription()}));
					}
				} else {
					throw new OpenStorefrontRuntimeException("Unable to find Attribute", "Check Integration mapping.", ErrorTypeCode.INTEGRATION);
				}
			} else {
				String ourAttributeCode = xrefAttributeMaps.get(xrefAttributeType.getAttributeType()).get(jiraValue);

				if (ourAttributeCode != null) {
					ComponentAttributePk componentAttributePk = new ComponentAttributePk();
					componentAttributePk.setComponentId(integrationConfig.getComponentId());
					componentAttributePk.setAttributeType(xrefAttributeType.getAttributeType());
					componentAttributePk.setAttributeCode(ourAttributeCode);

					if (StringUtils.isBlank(componentAttributePk.getComponentId())) {
						throw new OpenStorefrontRuntimeException("Component Id is required", ErrorTypeCode.INTEGRATION);
					}

					AttributeCodePk attributeCodePk = new AttributeCodePk();
					attributeCodePk.setAttributeType(componentAttributePk.getAttributeType());
					attributeCodePk.setAttributeCode(componentAttributePk.getAttributeCode());

					AttributeCode attributeCode = persistenceService.findById(AttributeCode.class, attributeCodePk);
					if (attributeCode != null) {

						AttributeType attributeType = persistenceService.findById(AttributeType.class, attributeCode.getAttributeCodePk().getAttributeType());
						if (Convert.toBoolean(attributeType.getAllowMultipleFlg()) == false) {
							ComponentAttributePk componentAttributeExamplePk = new ComponentAttributePk();
							componentAttributeExamplePk.setComponentId(integrationConfig.getComponentId());
							componentAttributeExamplePk.setAttributeType(attributeCode.getAttributeCodePk().getAttributeType());
							ComponentAttribute componentAttributeExample = new ComponentAttribute();
							componentAttributeExample.setComponentAttributePk(componentAttributeExamplePk);

							long typeCount = persistenceService.countByExample(componentAttributeExample);
							if (typeCount > 1) {
								ComponentAttribute example = new ComponentAttribute();
								example.setComponentAttributePk(new ComponentAttributePk());
								example.getComponentAttributePk().setAttributeType(componentAttributePk.getAttributeType());
								example.getComponentAttributePk().setComponentId(componentAttributePk.getComponentId());
								persistenceService.deleteByExample(example);
							}
						}

						ComponentAttribute existingAttribute = persistenceService.findById(ComponentAttribute.class, componentAttributePk);
						if (existingAttribute == null || ComponentAttribute.INACTIVE_STATUS.equals(existingAttribute.getActiveStatus())) {

							ComponentAttribute componentAttribute = new ComponentAttribute();
							componentAttribute.setComponentAttributePk(componentAttributePk);
							componentAttribute.setComponentId(componentAttributePk.getComponentId());
							componentAttribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
							componentAttribute.setCreateUser(OpenStorefrontConstant.SYSTEM_USER);
							componentAttribute.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
							saveComponentAttribute(componentAttribute, false);
							componentChanged = true;
						} else {
							log.log(Level.FINEST, "Attibute already exists in that state...skipping");
						}
					} else {
						throw new OpenStorefrontRuntimeException("Unable to find attribute code.  Attribute Type: " + componentAttributePk.getAttributeType() + " Code: " + componentAttributePk.getAttributeCode(),
								"Check Integration Mapping (Attributes and Input)", ErrorTypeCode.INTEGRATION);
					}
				} else {
					throw new OpenStorefrontRuntimeException("Unable to find Mapping for Jira Field value: " + jiraValue, ErrorTypeCode.INTEGRATION);
				}
			}
		}

		if (componentChanged) {
			updateComponentLastActivity(integrationConfig.getComponentId());
		}
	}

	@Override
	public List<ComponentSearchView> getSearchComponentList(List<String> componentIds)
	{
		List<ComponentSearchView> componentSearchViews = new ArrayList<>();
		if (componentIds.isEmpty() == false) {

			//get all active data
			StringBuilder componentQuery = new StringBuilder();
			componentQuery.append("select from Component where activeStatus='")
					.append(Component.ACTIVE_STATUS)
					.append("'and approvalState='")
					.append(ApprovalStatus.APPROVED)
					.append("' and componentId IN :componentIdsParams");

			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("componentIdsParams", componentIds);
			List<Component> components = persistenceService.query(componentQuery.toString(), paramMap, Component.class, true);

			StringBuilder componentAttributeQuery = new StringBuilder();
			componentAttributeQuery.append("select from ComponentAttribute where activeStatus='").append(Component.ACTIVE_STATUS).append("' and componentId IN :componentIdsParams");
			List<ComponentAttribute> componentAttributes = persistenceService.query(componentAttributeQuery.toString(), paramMap, ComponentAttribute.class, true);
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
			List<ComponentReview> componentReviews = persistenceService.query(componentReviewQuery.toString(), paramMap, ComponentReview.class, true);
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
			List<ComponentTag> componentTags = persistenceService.query(componentTagQuery.toString(), paramMap, ComponentTag.class, true);
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

	@Override
	public void saveComponentIntegration(ComponentIntegration integration)
	{
		ComponentIntegration componentIntegration = persistenceService.findById(ComponentIntegration.class, integration.getComponentId());
		if (componentIntegration != null) {
			componentIntegration.setActiveStatus(integration.getActiveStatus());
			componentIntegration.setLastEndTime(integration.getLastEndTime());
			componentIntegration.setLastStartTime(integration.getLastStartTime());
			componentIntegration.setRefreshRate(integration.getRefreshRate());
			componentIntegration.setStatus(integration.getStatus());
			componentIntegration.setUpdateUser(SecurityUtil.getCurrentUserName());
			componentIntegration.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(componentIntegration);
			integration = componentIntegration;
		} else {
			integration.setStatus(RunStatus.COMPLETE);
			integration.populateBaseCreateFields();
			persistenceService.persist(integration);
		}
		JobManager.updateComponentIntegrationJob(integration);
	}

	@Override
	public void setStatusOnComponentIntegration(String componentId, String status)
	{
		ComponentIntegration componentIntegration = persistenceService.findById(ComponentIntegration.class, componentId);
		if (componentIntegration != null) {
			componentIntegration.setActiveStatus(status);
			componentIntegration.setUpdateDts(TimeUtil.currentDate());
			componentIntegration.setUpdateUser(SecurityUtil.getCurrentUserName());
			persistenceService.persist(componentIntegration);

			if (Component.ACTIVE_STATUS.equals(status)) {
				JobManager.updateComponentIntegrationJob(componentIntegration);
			} else {
				JobManager.removeComponentIntegrationJob(componentId);
			}
		} else {
			throw new OpenStorefrontRuntimeException("Component Integration doesn't exist", "Check input", ErrorTypeCode.INTEGRATION);
		}
	}

	@Override
	public List<ComponentIntegration> getComponentIntegrationModels(String activeStatus)
	{
		ComponentIntegration integrationExample = new ComponentIntegration();
		integrationExample.setActiveStatus(activeStatus);
		List<ComponentIntegration> integrations = persistenceService.queryByExample(ComponentIntegration.class, integrationExample);
		return integrations;
	}

	@Override
	public void processComponentIntegration(String componentId, String integrationConfigId)
	{
		ComponentIntegration integrationExample = new ComponentIntegration();
		integrationExample.setActiveStatus(ComponentIntegration.ACTIVE_STATUS);
		integrationExample.setComponentId(componentId);
		ComponentIntegration integration = persistenceService.queryOneByExample(ComponentIntegration.class, integrationExample);
		if (integration != null) {

			boolean run = true;
			if (RunStatus.WORKING.equals(integration.getStatus())) {
				//check for override
				String overrideTime = PropertiesManager.getValue(PropertiesManager.KEY_JOB_WORKING_STATE_OVERRIDE, "30");
				if (integration.getLastStartTime() != null) {
					LocalDateTime maxLocalDateTime = LocalDateTime.ofInstant(integration.getLastStartTime().toInstant(), ZoneId.systemDefault());
					maxLocalDateTime.plusMinutes(Convert.toLong(overrideTime));
					if (maxLocalDateTime.compareTo(LocalDateTime.now()) <= 0) {
						log.log(Level.FINE, "Overriding the working state...assume it was stuck.");
						run = true;
					} else {
						run = false;
					}
				} else {
					throw new OpenStorefrontRuntimeException("Missing Last Start time.  Data is corrupt.", "Delete the job (Integration) and recreate it.", ErrorTypeCode.INTEGRATION);
				}
			}

			if (run) {
				Component component = persistenceService.findById(Component.class, integration.getComponentId());
				ComponentIntegration liveIntegration = persistenceService.findById(ComponentIntegration.class, integration.getComponentId());

				log.log(Level.FINE, MessageFormat.format("Processing Integration for: {0}", component.getName()));

				liveIntegration.setStatus(RunStatus.WORKING);
				liveIntegration.setLastStartTime(TimeUtil.currentDate());
				liveIntegration.setUpdateDts(TimeUtil.currentDate());
				liveIntegration.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
				persistenceService.persist(liveIntegration);

				ComponentIntegrationConfig integrationConfigExample = new ComponentIntegrationConfig();
				integrationConfigExample.setActiveStatus(ComponentIntegrationConfig.ACTIVE_STATUS);
				integrationConfigExample.setComponentId(componentId);
				integrationConfigExample.setIntegrationConfigId(integrationConfigId);

				List<ComponentIntegrationConfig> integrationConfigs = persistenceService.queryByExample(ComponentIntegrationConfig.class, integrationConfigExample);
				boolean errorConfig = false;
				if (integrationConfigs.isEmpty() == false) {
					for (ComponentIntegrationConfig integrationConfig : integrationConfigs) {
						ComponentIntegrationConfig liveConfig = persistenceService.findById(ComponentIntegrationConfig.class, integrationConfig.getIntegrationConfigId());
						try {
							log.log(Level.FINE, MessageFormat.format("Working on {1} Configuration for Integration for: {0}", component.getName(), integrationConfig.getIntegrationType()));

							liveConfig.setStatus(RunStatus.WORKING);
							liveConfig.setLastStartTime(TimeUtil.currentDate());
							liveConfig.setUpdateDts(TimeUtil.currentDate());
							liveConfig.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
							persistenceService.persist(liveConfig);

							BaseIntegrationHandler baseIntegrationHandler = BaseIntegrationHandler.getIntegrationHandler(integrationConfig);
							if (baseIntegrationHandler != null) {
								baseIntegrationHandler.processConfig();
							} else {
								throw new OpenStorefrontRuntimeException("Intergration handler not supported for " + integrationConfig.getIntegrationType(), "Add handler", ErrorTypeCode.INTEGRATION);
							}

							liveConfig.setStatus(RunStatus.COMPLETE);
							liveConfig.setLastEndTime(TimeUtil.currentDate());
							liveConfig.setUpdateDts(TimeUtil.currentDate());
							liveConfig.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
							persistenceService.persist(liveConfig);

							log.log(Level.FINE, MessageFormat.format("Completed {1} Configuration for Integration for: {0}", component.getName(), integrationConfig.getIntegrationType()));
						} catch (Exception e) {
							errorConfig = true;
							//This is a critical loop
							ErrorInfo errorInfo = new ErrorInfo(e, null);
							SystemErrorModel errorModel = getSystemService().generateErrorTicket(errorInfo);

							//put in fail state
							liveConfig.setStatus(RunStatus.ERROR);
							liveConfig.setErrorMessage(errorModel.getMessage());
							liveConfig.setErrorTicketNumber(errorModel.getErrorTicketNumber());
							liveConfig.setLastEndTime(TimeUtil.currentDate());
							liveConfig.setUpdateDts(TimeUtil.currentDate());
							liveConfig.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
							persistenceService.persist(liveConfig);

							log.log(Level.FINE, MessageFormat.format("Failed on {1} Configuration for Integration for: {0}", component.getName(), integrationConfig.getIntegrationType()), e);
						}
					}
				} else {
					log.log(Level.WARNING, MessageFormat.format("No Active Integration configs for: {0} (Integration is doing nothing)", component.getName()));
				}

				if (errorConfig) {
					liveIntegration.setStatus(RunStatus.ERROR);
				} else {
					liveIntegration.setStatus(RunStatus.COMPLETE);
				}
				liveIntegration.setLastEndTime(TimeUtil.currentDate());
				liveIntegration.setUpdateDts(TimeUtil.currentDate());
				liveIntegration.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
				persistenceService.persist(liveIntegration);

				log.log(Level.FINE, MessageFormat.format("Completed Integration for: {0}", component.getName()));
			} else {
				log.log(Level.FINE, MessageFormat.format("Not time to run integration or the system is currently working on the integration. Component Id: {0}", componentId));
			}
		} else {
			log.log(Level.WARNING, MessageFormat.format("There is no active integration for this component. Id: {0}", componentId));
		}
	}

	@Override
	public ComponentIntegrationConfig saveComponentIntegrationConfig(ComponentIntegrationConfig integrationConfig)
	{
		ComponentIntegration componentIntegration = persistenceService.findById(ComponentIntegration.class, integrationConfig.getComponentId());
		if (componentIntegration == null) {
			componentIntegration = new ComponentIntegration();
			componentIntegration.setComponentId(integrationConfig.getComponentId());
			saveComponentIntegration(componentIntegration);
		}

		ComponentIntegrationConfig componentIntegrationConfig = persistenceService.findById(ComponentIntegrationConfig.class, integrationConfig.getIntegrationConfigId());
		if (componentIntegrationConfig != null) {
			componentIntegrationConfig.setActiveStatus(integrationConfig.getActiveStatus());
			componentIntegrationConfig.setIntegrationType(integrationConfig.getIntegrationType());
			componentIntegrationConfig.setIssueNumber(integrationConfig.getIssueNumber());
			componentIntegrationConfig.setIssueType(integrationConfig.getIssueType());
			componentIntegrationConfig.setProjectType(integrationConfig.getProjectType());
			componentIntegrationConfig.setUpdateUser(integrationConfig.getUpdateUser());
			componentIntegrationConfig.populateBaseUpdateFields();
			persistenceService.persist(componentIntegrationConfig);
			integrationConfig = componentIntegrationConfig;
		} else {
			integrationConfig.setIntegrationConfigId(persistenceService.generateId());
			integrationConfig.populateBaseCreateFields();
			integrationConfig.setStatus(RunStatus.COMPLETE);
			persistenceService.persist(integrationConfig);
		}
		return integrationConfig;
	}

	@Override
	public void setStatusOnComponentIntegrationConfig(String integrationConfigId, String activeStatus)
	{
		ComponentIntegrationConfig componentIntegrationConfig = persistenceService.findById(ComponentIntegrationConfig.class, integrationConfigId);
		if (componentIntegrationConfig != null) {
			componentIntegrationConfig.setActiveStatus(activeStatus);
			componentIntegrationConfig.setUpdateDts(TimeUtil.currentDate());
			componentIntegrationConfig.setUpdateUser(SecurityUtil.getCurrentUserName());
			persistenceService.persist(componentIntegrationConfig);
		} else {
			throw new OpenStorefrontRuntimeException("Component Integration Config doesn't exist", "Check input", ErrorTypeCode.INTEGRATION);
		}
	}

	@Override
	public void deleteComponentIntegration(String componentId)
	{
		Objects.requireNonNull(componentId, "Component Id required");

		ComponentIntegration componentIntegration = persistenceService.findById(ComponentIntegration.class, componentId);
		if (componentIntegration != null) {
			ComponentIntegrationConfig integrationConfigExample = new ComponentIntegrationConfig();
			integrationConfigExample.setComponentId(componentId);
			persistenceService.deleteByExample(integrationConfigExample);
			persistenceService.delete(componentIntegration);

			JobManager.removeComponentIntegrationJob(componentId);
		}
	}

	@Override
	public void deleteComponentIntegrationConfig(String integrationConfigId)
	{
		ComponentIntegrationConfig componentIntegrationConfig = persistenceService.findById(ComponentIntegrationConfig.class, integrationConfigId);
		if (componentIntegrationConfig != null) {
			persistenceService.delete(componentIntegrationConfig);
		}
	}

	@Override
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
				componentAll.setComponent(persistenceService.queryOneByExample(Component.class, componentExample));

				if (componentAll.getComponent() != null) {
					componentAll.setAttributes(getAttributesByComponentId(componentId));
					componentAll.setContacts(getBaseComponent(ComponentContact.class, componentId));
					componentAll.setEvaluationSections(getBaseComponent(ComponentEvaluationSection.class, componentId));
					componentAll.setExternalDependencies(getBaseComponent(ComponentExternalDependency.class, componentId));
					componentAll.setMedia(getBaseComponent(ComponentMedia.class, componentId));
					componentAll.setMetadata(getBaseComponent(ComponentMetadata.class, componentId));
					componentAll.setRelationships(getBaseComponent(ComponentRelationship.class, componentId));
					componentAll.setResources(getBaseComponent(ComponentResource.class, componentId));
					componentAll.setResources(SortUtil.sortComponentResource(componentAll.getResources()));

					componentAll.setTags(getBaseComponent(ComponentTag.class, componentId));

					List<QuestionAll> allQuestions = new ArrayList<>();
					List<ComponentQuestion> questions = getBaseComponent(ComponentQuestion.class, componentId);
					for (ComponentQuestion question : questions) {
						QuestionAll questionAll = new QuestionAll();
						questionAll.setQuestion(question);

						ComponentQuestionResponse questionResponseExample = new ComponentQuestionResponse();
						questionResponseExample.setActiveStatus(ComponentQuestionResponse.ACTIVE_STATUS);
						questionResponseExample.setQuestionId(question.getQuestionId());
						questionAll.setResponds(persistenceService.queryByExample(ComponentQuestionResponse.class, questionResponseExample));
						allQuestions.add(questionAll);
					}
					componentAll.setQuestions(allQuestions);

					List<ReviewAll> allReviews = new ArrayList<>();
					List<ComponentReview> componentReviews = getBaseComponent(ComponentReview.class, componentId);
					for (ComponentReview componentReview : componentReviews) {
						ReviewAll reviewAll = new ReviewAll();
						reviewAll.setComponentReview(componentReview);

						ComponentReviewPro componentReviewProExample = new ComponentReviewPro();
						ComponentReviewProPk componentReviewProExamplePk = new ComponentReviewProPk();
						componentReviewProExamplePk.setComponentReviewId(componentReview.getComponentReviewId());
						componentReviewProExample.setComponentReviewProPk(componentReviewProExamplePk);
						reviewAll.setPros(persistenceService.queryByExample(ComponentReviewPro.class, componentReviewProExample));

						ComponentReviewCon componentReviewConExample = new ComponentReviewCon();
						ComponentReviewConPk componentReviewConExamplePk = new ComponentReviewConPk();
						componentReviewConExamplePk.setComponentReviewId(componentReview.getComponentReviewId());
						componentReviewConExample.setComponentReviewConPk(componentReviewConExamplePk);
						reviewAll.setCons(persistenceService.queryByExample(ComponentReviewCon.class, componentReviewConExample));

						allReviews.add(reviewAll);
					}
					componentAll.setReviews(allReviews);

					ComponentIntegration componentIntegrationExample = new ComponentIntegration();
					componentIntegrationExample.setActiveStatus(ComponentIntegration.ACTIVE_STATUS);
					componentIntegrationExample.setComponentId(componentId);

					ComponentIntegration integration = persistenceService.queryOneByExample(ComponentIntegration.class, componentIntegrationExample);
					if (integration != null) {
						IntegrationAll integrationAll = new IntegrationAll();
						integrationAll.setIntegration(integration);

						ComponentIntegrationConfig configExample = new ComponentIntegrationConfig();
						configExample.setActiveStatus(ComponentIntegrationConfig.ACTIVE_STATUS);
						configExample.setComponentId(componentId);
						integrationAll.setConfigs(persistenceService.queryByExample(ComponentIntegrationConfig.class, configExample));
						componentAll.setIntegrationAll(integrationAll);
					}

					element = new Element(componentId, componentAll);
					OSFCacheManager.getComponentCache().put(element);
				}
			}
		}

		return componentAll;
	}

	@Override
	public void bulkComponentAttributeChange(BulkComponentAttributeChange bulkComponentAttributeChange)
	{
		Set<String> componentIdSet = new HashSet<>();

		for (ComponentAttribute componentAttribute : bulkComponentAttributeChange.getAttributes()) {

			componentAttribute.populateBaseUpdateFields();
			switch (bulkComponentAttributeChange.getOpertionType()) {
				case ACTIVATE:
					componentIdSet.add(componentAttribute.getComponentId());
					componentAttribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
					persistenceService.persist(componentAttribute);
					break;
				case INACTIVE:
					componentIdSet.add(componentAttribute.getComponentId());
					componentAttribute.setActiveStatus(ComponentAttribute.INACTIVE_STATUS);
					persistenceService.persist(componentAttribute);
					break;
				case DELETE:
					persistenceService.delete(componentAttribute);
					break;
			}
		}
		componentIdSet.forEach(componentId -> {
			updateComponentLastActivity(componentId);
		});
	}

	@Override
	public ComponentTrackingResult getComponentTracking(FilterQueryParams filter, String componentId)
	{
		ComponentTrackingResult result = new ComponentTrackingResult();

		ComponentTracking componentTrackingExample = new ComponentTracking();
		componentTrackingExample.setActiveStatus(filter.getStatus());
		componentTrackingExample.setComponentId(componentId);

		ComponentTracking componentTrackingStartExample = new ComponentTracking();
		componentTrackingStartExample.setEventDts(filter.getStart());

		ComponentTracking componentTrackingEndExample = new ComponentTracking();
		componentTrackingEndExample.setEventDts(filter.getEnd());

		QueryByExample queryByExample = new QueryByExample(componentTrackingExample);

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(componentTrackingStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(componentTrackingEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParamaterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		queryByExample.setMaxResults(filter.getMax());
		queryByExample.setFirstResult(filter.getOffset());
		queryByExample.setSortDirection(filter.getSortOrder());

		ComponentTracking componentTrackingOrderExample = new ComponentTracking();
		Field sortField = ReflectionUtil.getField(componentTrackingOrderExample, filter.getSortField());
		if (sortField != null) {
			BeanUtil.setPropertyValue(sortField.getName(), componentTrackingOrderExample, QueryByExample.getFlagForType(sortField.getType()));
			queryByExample.setOrderBy(componentTrackingOrderExample);
		}

		List<ComponentTracking> componentTrackings = persistenceService.queryByExample(ComponentTracking.class, queryByExample);

		for (ComponentTracking item : componentTrackings) {
			ComponentTrackingCompleteWrapper wrapper = new ComponentTrackingCompleteWrapper();
			wrapper.setData(item);
			wrapper.setName(getComponentName(item.getComponentId()));
			result.getResult().add(wrapper);
		}

		if (filter.getSortField().equals(ComponentTrackingCompleteWrapper.FIELD_NAME)) {
			result.getResult().sort(new BeanComparator<>(filter.getSortOrder(), filter.getSortField()));
		}

		queryByExample.setQueryType(QueryType.COUNT);
		result.setCount(persistenceService.countByExample(queryByExample));

		return result;
	}

	@Override
	public ComponentAdminWrapper getFilteredComponents(ComponentFilterParams filter, String componentId)
	{
		ComponentAdminWrapper result = new ComponentAdminWrapper();

		Component componentExample = new Component();
		if (!filter.getAll()) {
			componentExample.setActiveStatus(filter.getStatus());
		}
		componentExample.setComponentId(componentId);
		componentExample.setApprovalState(filter.getApprovalState());
		componentExample.setComponentType(filter.getComponentType());
		if (componentExample != null && componentExample.getComponentType() != null && componentExample.getComponentType().equals(ComponentType.ALL)){
			componentExample.setComponentType(null);
		}

		QueryByExample queryByExample = new QueryByExample(componentExample);

		List<Component> components = persistenceService.queryByExample(Component.class, queryByExample);
		result.setTotalNumber(components.size());
		components = filter.filter(components);

		ComponentIntegrationConfig integrationConfigExample = new ComponentIntegrationConfig();
		integrationConfigExample.setActiveStatus(ComponentIntegrationConfig.ACTIVE_STATUS);

		List<ComponentIntegrationConfig> componentIntegrationConfigs = persistenceService.queryByExample(ComponentIntegrationConfig.class, integrationConfigExample);
		Map<String, List<ComponentIntegrationConfig>> configMap = new HashMap<>();
		componentIntegrationConfigs.forEach(config -> {
			if (configMap.containsKey(config.getComponentId())) {
				configMap.get(config.getComponentId()).add(config);
			} else {
				List<ComponentIntegrationConfig> configList = new ArrayList<>();
				configList.add(config);
				configMap.put(config.getComponentId(), configList);
			}
		});

		List<ComponentAdminView> componentAdminViews = new ArrayList<>();
		for (Component component : components) {
			ComponentAdminView componentAdminView = new ComponentAdminView();
			componentAdminView.setComponent(component);
			StringBuilder configs = new StringBuilder();
			List<ComponentIntegrationConfig> configList = configMap.get(component.getComponentId());
			if (configList != null) {
				configList.forEach(config -> {
					if (StringUtils.isNotBlank(config.getIssueNumber())) {
						configs.append("(").append(config.getIntegrationType()).append(" - ").append(config.getIssueNumber()).append(") ");
					} else {
						configs.append("(").append(config.getIntegrationType()).append(") ");
					}
				});
			}
			componentAdminView.setIntegrationManagement(configs.toString());
			componentAdminView.setComponent(component);
			componentAdminViews.add(componentAdminView);
		}

		result.setComponents(componentAdminViews);

		return result;
	}

	@Override
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

	@Override
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
				getAlertService().checkAlert(alertContext);
			} else {
				throw new OpenStorefrontRuntimeException("Component: " + component.getName() + " is already Approved. Id: " + componentId);
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find component to submit.", "Check data");
		}
	}

	@Override
	public void processComponentUpdates()
	{
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		try {
			ComponentUpdateQueue updateQueueExample = new ComponentUpdateQueue();
			updateQueueExample.setNodeId(OpenStorefrontConstant.getNodeName());

			List<ComponentUpdateQueue> componentUpdateQueues = persistenceService.queryByExample(ComponentUpdateQueue.class, updateQueueExample);
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

				List<Component> componentsToIndex = new ArrayList<>();
				for (ComponentUpdateQueue componentUpdate : componentMap.values()) {
					String componentId = componentUpdate.getComponentId();

					Component component = persistenceService.findById(Component.class, componentId);
					if (component != null) {
						component.setLastActivityDts(componentUpdate.getUpdateDts());
						persistenceService.persist(component);
						getUserService().checkComponentWatches(component);
						componentsToIndex.add(component);
					} else {
						log.log(Level.FINE, "Component not found to update last Activity. Component may have been removed.", "Check component Id: " + componentId);
					}
				}
				getSearchService().indexComponents(componentsToIndex);

				//remove processed records
				for (ComponentUpdateQueue updateQueue : componentUpdateQueues) {
					ComponentUpdateQueue componentUpdateQueue = persistenceService.findById(ComponentUpdateQueue.class, updateQueue.getUpdateId());
					if (componentUpdateQueue != null) {
						persistenceService.delete(componentUpdateQueue);
					}
				}
			}
		} finally {
			lock.unlock();
		}

	}

	@Override
	public void checkComponentCancelStatus(String componentId, String newApprovalStatus)
	{
		Component existingComponent = persistenceService.findById(Component.class, componentId);

		if (ApprovalStatus.PENDING.equals(existingComponent.getApprovalState())
				&& ApprovalStatus.NOT_SUBMITTED.equals(newApprovalStatus)) {
			//cancel submission
			existingComponent.setApprovalState(newApprovalStatus);
			existingComponent.setUpdateUser(SecurityUtil.getCurrentUserName());
			existingComponent.populateBaseUpdateFields();
			persistenceService.persist(existingComponent);

			AlertContext alertContext = new AlertContext();
			alertContext.setAlertType(AlertType.COMPONENT_SUBMISSION);
			alertContext.setDataTrigger(existingComponent);
			getAlertService().checkAlert(alertContext);
		}
	}

	@Override
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

}
