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
import edu.usu.sdl.openstorefront.core.api.ComponentService;
import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.entity.BaseComponent;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
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
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewPro;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeTemplate;
import edu.usu.sdl.openstorefront.core.entity.ComponentVersionHistory;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryOption;
import edu.usu.sdl.openstorefront.core.entity.TemplateBlock;
import edu.usu.sdl.openstorefront.core.filter.ComponentSensitivityModel;
import edu.usu.sdl.openstorefront.core.model.BulkComponentAttributeChange;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.ComponentRestoreOptions;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminWrapper;
import edu.usu.sdl.openstorefront.core.view.ComponentDetailView;
import edu.usu.sdl.openstorefront.core.view.ComponentFilterParams;
import edu.usu.sdl.openstorefront.core.view.ComponentReviewView;
import edu.usu.sdl.openstorefront.core.view.ComponentSearchView;
import edu.usu.sdl.openstorefront.core.view.ComponentTrackingResult;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.core.view.RequiredForComponent;
import edu.usu.sdl.openstorefront.core.view.statistic.ComponentRecordStatistic;
import edu.usu.sdl.openstorefront.service.api.ComponentServicePrivate;
import edu.usu.sdl.openstorefront.service.component.CoreComponentServiceImpl;
import edu.usu.sdl.openstorefront.service.component.IntegrationComponentServiceImpl;
import edu.usu.sdl.openstorefront.service.component.SubComponentServiceImpl;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

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

	private static final Logger LOG = Logger.getLogger(ComponentServiceImpl.class.getName());

	private CoreComponentServiceImpl core;
	private SubComponentServiceImpl sub;
	private IntegrationComponentServiceImpl integration;

	public ComponentServiceImpl()
	{
		this.core = new CoreComponentServiceImpl(this);
		this.sub = new SubComponentServiceImpl(this);
		this.integration = new IntegrationComponentServiceImpl(this);
		this.core.init();
		this.sub.init();
		this.integration.init();
	}

	public ComponentServiceImpl(PersistenceService persistenceService)
	{
		super(persistenceService);

		this.core = new CoreComponentServiceImpl(this);
		this.sub = new SubComponentServiceImpl(this);
		this.integration = new IntegrationComponentServiceImpl(this);
		this.core.init();
		this.sub.init();
		this.integration.init();
	}

	@Override
	public <T extends BaseComponent> List<T> getBaseComponent(Class<T> subComponentClass, String componentId)
	{
		return sub.getBaseComponent(subComponentClass, componentId);
	}

	@Override
	public <T extends BaseComponent> List<T> getBaseComponent(Class<T> subComponentClass, String componentId, String activeStatus)
	{
		return sub.getBaseComponent(subComponentClass, componentId, activeStatus);
	}

	@Override
	public <T extends BaseComponent> T deactivateBaseComponent(Class<T> subComponentClass, Object pk)
	{
		return sub.deactivateBaseComponent(subComponentClass, pk);
	}

	@Override
	public <T extends BaseComponent> T activateBaseComponent(Class<T> subComponentClass, Object pk)
	{
		return sub.activateBaseComponent(subComponentClass, pk);
	}

	@Override
	public ComponentReview setReviewPending(String ComponentReviewId)
	{
		return sub.setReviewPending(ComponentReviewId);
	}

	@Override
	public ComponentQuestion setQuestionPending(String ComponentQuestionResponseId)
	{
		return sub.setQuestionPending(ComponentQuestionResponseId);

	}

	@Override
	public ComponentQuestionResponse setQuestionResponsePending(String ComponentQuestionResponseId)
	{
		return sub.setQuestionResponsePending(ComponentQuestionResponseId);
	}

	@Override
	public <T extends BaseComponent> void deleteBaseComponent(Class<T> subComponentClass, Object pk)
	{
		sub.deleteBaseComponent(subComponentClass, pk, true);
	}

	@Override
	public <T extends BaseComponent> void deleteAllBaseComponent(Class<T> subComponentClass, String componentId)
	{
		sub.deleteAllBaseComponent(subComponentClass, componentId);
	}

	@Override
	public void deactivateComponent(String componentId)
	{
		core.deactivateComponent(componentId);
	}

	@Override
	public void doDeactivateComponent(String componentId)
	{
		core.doDeactivateComponent(componentId);
	}

	@Override
	public Component activateComponent(String componentId)
	{
		return core.activateComponent(componentId);
	}

	@Override
	public String getComponentName(String componentId)
	{
		return core.getComponentName(componentId);
	}

	@Override
	public List<ComponentSearchView> getComponents()
	{
		return core.getComponents();
	}

	@Override
	public List<ComponentAttribute> getAttributesByComponentId(String componentId)
	{
		return sub.getAttributesByComponentId(componentId);
	}

	@Override
	public ComponentDetailView getComponentDetails(String componentId)
	{
		return core.getComponentDetails(componentId);
	}
	
	@Override
	public ComponentDetailView getComponentDetails(String componentId, String evaluationId)
	{
		return core.getComponentDetails(componentId, evaluationId);
	}

	@Override
	public void saveComponentAttribute(ComponentAttribute attribute)
	{
		getComponentServicePrivate().saveComponentAttribute(attribute, true);
	}

	@Override
	public void saveComponentAttribute(ComponentAttribute attribute, boolean updateLastActivity)
	{
		sub.saveComponentAttribute(attribute, updateLastActivity);
	}

	@Override
	public void saveComponentContact(ComponentContact contact)
	{
		sub.saveComponentContact(contact);
	}

	@Override
	public void saveComponentDependency(ComponentExternalDependency dependency)
	{
		sub.saveComponentDependency(dependency);
	}

	@Override
	public void saveComponentEvaluationSection(ComponentEvaluationSection section)
	{
		sub.saveComponentEvaluationSection(section);
	}

	@Override
	public void saveComponentEvaluationSection(List<ComponentEvaluationSection> sections)
	{
		sub.saveComponentEvaluationSection(sections);
	}

	@Override
	public ComponentMedia saveComponentMedia(ComponentMedia media)
	{
		return sub.saveComponentMedia(media);
	}

	@Override
	public void saveComponentMetadata(ComponentMetadata metadata)
	{
		sub.saveComponentMetadata(metadata);
	}

	@Override
	public ComponentRelationship saveComponentRelationship(ComponentRelationship componentRelationship)
	{
		return sub.saveComponentRelationship(componentRelationship);
	}

	@Override
	public void saveComponentQuestion(ComponentQuestion question)
	{
		sub.saveComponentQuestion(question);
	}

	@Override
	public void saveComponentQuestionResponse(ComponentQuestionResponse response)
	{
		sub.saveComponentQuestionResponse(response);
	}

	@Override
	public ComponentResource saveComponentResource(ComponentResource resource)
	{
		return sub.saveComponentResource(resource);
	}

	@Override
	public void saveComponentReview(ComponentReview review)
	{
		sub.saveComponentReview(review);
	}

	@Override
	public void saveComponentReviewCon(ComponentReviewCon con)
	{
		sub.saveComponentReviewCon(con);
	}

	@Override
	public void saveComponentReviewPro(ComponentReviewPro pro)
	{
		sub.saveComponentReviewPro(pro);
	}

	@Override
	public void saveComponentTag(ComponentTag tag)
	{
		sub.saveComponentTag(tag);
	}

	@Override
	public void doSaveComponentTag(ComponentTag tag, boolean updateLastActivity)
	{
		sub.doSaveComponentTag(tag, updateLastActivity);
	}

	@Override
	public void saveComponentTracking(ComponentTracking tracking)
	{
		core.saveComponentTracking(tracking);
	}

	@Override
	public RequiredForComponent saveComponent(RequiredForComponent component)
	{
		return core.saveComponent(component);
	}

	@Override
	public RequiredForComponent doSaveComponent(RequiredForComponent component)
	{
		return core.doSaveComponent(component);
	}

	@Override
	public ValidationResult checkComponentAttribute(ComponentAttribute attribute)
	{
		return sub.checkComponentAttribute(attribute);
	}

	@Override
	public void importComponents(List<ComponentAll> components, FileHistoryOption options)
	{
		core.importComponents(components, options);
	}

	@Override
	public ComponentAll saveFullComponent(ComponentAll componentAll)
	{
		return core.saveFullComponent(componentAll);
	}

	@Override
	public ComponentAll saveFullComponent(ComponentAll componentAll, FileHistoryOption options)
	{
		return core.saveFullComponent(componentAll, options);
	}

	@Override
	public void cascadeDeleteOfComponent(String componentId)
	{
		core.cascadeDeleteOfComponent(componentId);
	}

	@Override
	public List<ComponentTag> getTagCloud()
	{
		return sub.getTagCloud();
	}

	@Override
	public List<ComponentMetadata> getMetadata()
	{
		return sub.getMetadata();
	}

	@Override
	public List<ComponentReviewView> getReviewByUser(String username)
	{
		return sub.getReviewByUser(username);
	}

	@Override
	public void saveMediaFile(ComponentMedia media, InputStream fileInput)
	{
		sub.saveMediaFile(media, fileInput);
	}

	@Override
	public void saveResourceFile(ComponentResource resource, InputStream fileInput)
	{
		sub.saveResourceFile(resource, fileInput);
	}

	@Override
	public Boolean setLastViewDts(String componentId, String userId)
	{
		return core.setLastViewDts(componentId, userId);
	}

	@Override
	public List<Component> findRecentlyAdded(int maxResults)
	{
		return core.findRecentlyAdded(maxResults);
	}

	@Override
	public ValidationResult saveDetailReview(ComponentReview review, List<ComponentReviewPro> pros, List<ComponentReviewCon> cons)
	{
		return sub.saveDetailReview(review, pros, cons);
	}

	@Override
	public void mapComponentAttributes(Issue issue, ComponentIntegrationConfig integrationConfig)
	{
		integration.mapComponentAttributes(issue, integrationConfig);
	}

	@Override
	public List<ComponentSearchView> getSearchComponentList(List<String> componentIds)
	{
		return core.getSearchComponentList(componentIds);
	}

	@Override
	public void saveComponentIntegration(ComponentIntegration componentIntegration)
	{
		integration.saveComponentIntegration(componentIntegration);
	}

	@Override
	public void setStatusOnComponentIntegration(String componentId, String status)
	{
		integration.setStatusOnComponentIntegration(componentId, status);
	}

	@Override
	public List<ComponentIntegration> getComponentIntegrationModels(String activeStatus)
	{
		return integration.getComponentIntegrationModels(activeStatus);
	}

	@Override
	public void processComponentIntegration(String componentId, String integrationConfigId)
	{
		integration.processComponentIntegration(componentId, integrationConfigId);
	}

	@Override
	public ComponentIntegrationConfig saveComponentIntegrationConfig(ComponentIntegrationConfig integrationConfig)
	{
		return integration.saveComponentIntegrationConfig(integrationConfig);
	}

	@Override
	public void setStatusOnComponentIntegrationConfig(String integrationConfigId, String activeStatus)
	{
		integration.setStatusOnComponentIntegrationConfig(integrationConfigId, activeStatus);
	}

	@Override
	public void deleteComponentIntegration(String componentId)
	{
		integration.deleteComponentIntegration(componentId);
	}

	@Override
	public void deleteComponentIntegrationConfig(String integrationConfigId)
	{
		integration.deleteComponentIntegrationConfig(integrationConfigId);
	}

	@Override
	public ComponentAll getFullComponent(String componentId)
	{
		return core.getFullComponent(componentId);
	}

	@Override
	public void bulkComponentAttributeChange(BulkComponentAttributeChange bulkComponentAttributeChange)
	{
		sub.bulkComponentAttributeChange(bulkComponentAttributeChange);
	}

	@Override
	public ComponentTrackingResult getComponentTracking(FilterQueryParams filter, String componentId)
	{
		return core.getComponentTracking(filter, componentId);
	}

	@Override
	public ComponentAdminWrapper getFilteredComponents(ComponentFilterParams filter, String componentId)
	{
		return core.getFilteredComponents(filter, componentId);
	}

	@Override
	public Set<LookupModel> getTypeahead(String search)
	{
		return core.getTypeahead(search);
	}

	@Override
	public void submitComponentSubmission(String componentId)
	{
		core.submitComponentSubmission(componentId);
	}

	@Override
	public void processComponentUpdates()
	{
		core.processComponentUpdates();
	}

	@Override
	public void checkComponentCancelStatus(String componentId, String newApprovalStatus)
	{
		core.checkComponentCancelStatus(componentId, newApprovalStatus);
	}

	@Override
	public String getComponentApprovalStatus(String componentId)
	{
		return core.getComponentApprovalStatus(componentId);
	}

	@Override
	public boolean checkComponentApproval(String componentId)
	{
		return core.checkComponentApproval(componentId);
	}

	@Override
	public Component copy(String orignalComponentId)
	{
		return core.copy(orignalComponentId);
	}

	@Override
	public ComponentVersionHistory snapshotVersion(String componentId, String fileHistoryId)
	{
		return core.snapshotVersion(componentId, fileHistoryId);
	}

	@Override
	public Component restoreSnapshot(String versionHistoryId, ComponentRestoreOptions options)
	{
		return core.restoreSnapshot(versionHistoryId, options);
	}

	@Override
	public void deleteSnapshot(String versionHistoryId)
	{
		core.deleteSnapshot(versionHistoryId);
	}

	@Override
	public Component merge(String toMergeComponentId, String targetComponentId)
	{
		return core.merge(toMergeComponentId, targetComponentId);
	}

	@Override
	public List<ComponentRecordStatistic> findTopViewedComponents(Integer maxRecords)
	{
		return core.findTopViewedComponents(maxRecords);
	}

	public CoreComponentServiceImpl getCore()
	{
		return core;
	}

	public SubComponentServiceImpl getSub()
	{
		return sub;
	}

	public IntegrationComponentServiceImpl getIntegration()
	{
		return integration;
	}

	@Override
	public ComponentType saveComponentType(ComponentType componentType)
	{
		return core.saveComponentType(componentType);
	}

	@Override
	public void removeComponentType(String componentType, String newComponentType)
	{
		core.removeComponentType(componentType, newComponentType);
	}

	@Override
	public ComponentTypeTemplate saveComponentTemplate(ComponentTypeTemplate componentTypeTemplate)
	{
		return core.saveComponentTemplate(componentTypeTemplate);
	}

	@Override
	public void removeComponentTypeTemplate(String templateId)
	{
		core.removeComponentTypeTemplate(templateId);
	}

	@Override
	public void deleteComponentTypeTemplate(String templateId)
	{
		core.deleteComponentTypeTemplate(templateId);
	}

	@Override
	public Component approveComponent(String componentId)
	{
		return core.approveComponent(componentId);
	}

	@Override
	public ComponentDetailView viewSnapshot(String versionHistoryId)
	{
		return core.viewSnapshot(versionHistoryId);
	}

	@Override
	public Component changeOwner(String componentId, String newOwner)
	{
		return core.changeOwner(componentId, newOwner);
	}

	@Override
	public Component createPendingChangeComponent(String parentComponentId)
	{
		return core.createPendingChangeComponent(parentComponentId);
	}

	@Override
	public Component mergePendingChange(String componentIdOfPendingChange)
	{
		return core.mergePendingChange(componentIdOfPendingChange);
	}

	@Override
	public List<ComponentType> getAllComponentTypes()
	{
		return core.getAllComponentTypes();
	}

	@Override
	public void submitChangeRequest(String componentId)
	{
		core.submitChangeRequest(componentId);
	}

	@Override
	public void checkChangeRequestCancelStatus(String componentId, String newApprovalStatus)
	{
		core.checkChangeRequestCancelStatus(componentId, newApprovalStatus);
	}

	@Override
	public void saveTemplateBlock(TemplateBlock templateBlock)
	{
		core.saveTemplateBlock(templateBlock);
	}

	@Override
	public void deleteTemplateBlock(String templateBlockId)
	{
		core.deleteTemplateBlock(templateBlockId);
	}

	@Override
	public ComponentSensitivityModel getComponentSensitivity(String componentId)
	{
		return core.getComponentSensitivity(componentId);
	}

	@Override
	public String resolveComponentIcon(String componentId)
	{
		return core.resolveComponentIcon(componentId);
	}

	@Override
	public String resolveComponentTypeIcon(String componentType)
	{
		return core.resolveComponentTypeIcon(componentType);
	}

	@Override
	public String getComponentType(String componentId)
	{
		return core.getComponentType(componentId);
	}

}
