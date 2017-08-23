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
package edu.usu.sdl.openstorefront.core.api;

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
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

/**
 * Services that handle all component classes
 *
 * @author dshurtleff
 * @author jlaw
 */
public interface ComponentService
		extends AsyncService
{

	/**
	 * This only returns the active
	 *
	 * @param <T>
	 * @param subComponentClass
	 * @param componentId
	 * @return
	 */
	public <T extends BaseComponent> List<T> getBaseComponent(Class<T> subComponentClass, String componentId);

	/**
	 * This can be use to get parts of the component (Eg. ComponentReview)
	 *
	 * @param <T>
	 * @param subComponentClass
	 * @param componentId
	 * @param activeStatus
	 * @return List of base components
	 */
	public <T extends BaseComponent> List<T> getBaseComponent(Class<T> subComponentClass, String componentId, String activeStatus);

	/**
	 * Deactivates a base component
	 *
	 * @param <T>
	 * @param subComponentClass
	 * @param pk
	 * @return
	 */
	public <T extends BaseComponent> T deactivateBaseComponent(Class<T> subComponentClass, Object pk);

	/**
	 * Activates a base component
	 *
	 * @param <T>
	 * @param subComponentClass
	 * @param pk
	 * @return
	 */
	public <T extends BaseComponent> T activateBaseComponent(Class<T> subComponentClass, Object pk);

	/**
	 * Set Review to pending
	 *
	 * @param ComponentReviewId
	 * @return
	 */
	public ComponentReview setReviewPending(String ComponentReviewId);

	/**
	 * Set Review to pending
	 *
	 * @param ComponentQuestionId
	 * @return
	 */
	public ComponentQuestion setQuestionPending(String ComponentQuestionId);

	/**
	 * Set Review to pending
	 *
	 * @param ComponentQuestionResponseId
	 * @return
	 */
	public ComponentQuestionResponse setQuestionResponsePending(String ComponentQuestionResponseId);

	/**
	 * Deletes a base component
	 *
	 * @param <T>
	 * @param subComponentClass
	 * @param pk (pk for the base component)
	 */
	public <T extends BaseComponent> void deleteBaseComponent(Class<T> subComponentClass, Object pk);

	/**
	 * Deletes base components for a component Id
	 *
	 * @param <T>
	 * @param subComponentClass
	 * @param componentId
	 */
	public <T extends BaseComponent> void deleteAllBaseComponent(Class<T> subComponentClass, String componentId);

	/**
	 * In-activates Component and removes all user watches for a component
	 *
	 * @param componentId
	 */
	public void deactivateComponent(String componentId);

	/**
	 * Activates a Component
	 *
	 * @param componentId
	 * @return Component updated or null if no component found
	 */
	public Component activateComponent(String componentId);

	/**
	 * High-speed component name lookup
	 *
	 * @param componentId
	 * @return Name or null if not found
	 */
	public String getComponentName(String componentId);

	/**
	 * High-speed component type lookup
	 *
	 * @param componentId
	 * @return ComponentType Code or null if not found
	 */
	public String getComponentType(String componentId);

	/**
	 * High-speed check for approval
	 *
	 * @param componentId
	 * @return true if approved
	 */
	public boolean checkComponentApproval(String componentId);

	/**
	 * Lookup approval status on a component
	 *
	 * @param componentId
	 * @return Status or null if component is not found
	 */
	public String getComponentApprovalStatus(String componentId);

	/**
	 * Gets component's data sensitivity
	 *
	 * @param componentId
	 * @return sensitivityModel or null if the component doesn't exist
	 */
	public ComponentSensitivityModel getComponentSensitivity(String componentId);

	/**
	 * Return the whole list of components. (the short view) Just Active and
	 * Approved components
	 *
	 * @return
	 */
	public List<ComponentSearchView> getComponents();

	/**
	 * Return the details object of the component attached to the given id. (the
	 * full view)
	 *
	 * @param componentId
	 * @return details or null if not found
	 */
	public ComponentDetailView getComponentDetails(String componentId);
	
	/**
	 * Return the details object of the component attached to the given componentId and evaluationId. (the
	 * full view)
	 *
	 * @param componentId
	 * @param evaluationId
	 * @return details or null if not found
	 */
	public ComponentDetailView getComponentDetails(String componentId, String evaluationId);

	/**
	 * Set the last view date for the component associated with the supplied id.
	 *
	 * @param componentId
	 * @param userId
	 * @return
	 */
	public Boolean setLastViewDts(String componentId, String userId);

	/**
	 * Return the details object of the component attached to the given id. (the
	 * full view)
	 *
	 * @param username
	 * @return
	 */
	public List<ComponentReviewView> getReviewByUser(String username);

	/**
	 * Pulls from cache
	 *
	 * @param componentId
	 * @return
	 */
	public List<ComponentAttribute> getAttributesByComponentId(String componentId);

	/**
	 * Gets all unique tags
	 *
	 * @return
	 */
	public List<ComponentTag> getTagCloud();

	/**
	 * Gets all unique metadata
	 *
	 * @return
	 */
	public List<ComponentMetadata> getMetadata();

	/**
	 * Saves a component Attribute
	 *
	 * @param attribute
	 */
	public void saveComponentAttribute(ComponentAttribute attribute);

	/**
	 * Saves a component Attribute
	 *
	 * @param attribute
	 * @param updateLastActivity
	 */
	public void saveComponentAttribute(ComponentAttribute attribute, boolean updateLastActivity);

	/**
	 *
	 * @param filter
	 * @param componentId
	 * @return
	 */
	public ComponentTrackingResult getComponentTracking(FilterQueryParams filter, String componentId);

	/**
	 * Validate Component Attribute and handling adding user code if it doesn't
	 * exist
	 *
	 * @param attribute
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public ValidationResult checkComponentAttribute(ComponentAttribute attribute);

	/**
	 *
	 * @param contact
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentContact(ComponentContact contact);

	/**
	 *
	 * @param dependency
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentDependency(ComponentExternalDependency dependency);

	/**
	 *
	 * @param section
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentEvaluationSection(ComponentEvaluationSection section);

	/**
	 * Save all sections and then updates component WARNING: All sections should
	 * be for the same component.
	 *
	 * @param sections
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentEvaluationSection(List<ComponentEvaluationSection> sections);

	/**
	 *
	 * @param media
	 * @return saved media record
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public ComponentMedia saveComponentMedia(ComponentMedia media);

	/**
	 *
	 * @param metadata
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentMetadata(ComponentMetadata metadata);

	/**
	 *
	 * @param question
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentQuestion(ComponentQuestion question);

	/**
	 * Saves a question response
	 *
	 * @param response
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentQuestionResponse(ComponentQuestionResponse response);

	/**
	 * Saves a component Resource
	 *
	 * @param resource
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public ComponentResource saveComponentResource(ComponentResource resource);

	/**
	 *
	 * @param review
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentReview(ComponentReview review);

	/**
	 *
	 * @param con
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentReviewCon(ComponentReviewCon con);

	/**
	 *
	 * @param pro
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentReviewPro(ComponentReviewPro pro);

	/**
	 * Adds a tags to a component
	 *
	 * @param tag
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentTag(ComponentTag tag);

	/**
	 * Saves a new relationship
	 *
	 * @param componentRelationship
	 * @return saved entity
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public ComponentRelationship saveComponentRelationship(ComponentRelationship componentRelationship);

	/**
	 *
	 * @param tracking
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentTracking(ComponentTracking tracking);

	/**
	 * This will save the component (skipping duplication checking)
	 *
	 * @param component
	 * @return
	 */
	public RequiredForComponent saveComponent(RequiredForComponent component);

	/**
	 * Get all component full entities Note: this uses a cache to avoid
	 * expensive query for use case where pulling the same is frequent.
	 *
	 * @param componentId
	 * @return
	 */
	public ComponentAll getFullComponent(String componentId);

	/**
	 * This save the full component; this meant for use in the importer. It will
	 * generate id and fill in missing data where possible. This will try to
	 * sync the component adding and updating where applicable
	 *
	 * @param componentAll
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public ComponentAll saveFullComponent(ComponentAll componentAll);

	/**
	 * @see saveFullComponent(ComponentAll componentAll);
	 * @param componentAll
	 * @param options (save options)
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public ComponentAll saveFullComponent(ComponentAll componentAll, FileHistoryOption options);

	/**
	 * Submits a component for Approval
	 *
	 * @param componentId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void submitComponentSubmission(String componentId);

	/**
	 * Submits a change request for Approval
	 *
	 * @param componentId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void submitChangeRequest(String componentId);

	/**
	 * This will handle syncing all the component of the list.
	 *
	 * @param components
	 * @param options
	 */
	public void importComponents(List<ComponentAll> components, FileHistoryOption options);

	/**
	 * Deletes the component and all related entities
	 *
	 * @param componentId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void cascadeDeleteOfComponent(String componentId);

	/**
	 * Saves component media to disk and sets the filename
	 *
	 * @param media
	 * @param fileInput
	 */
	public void saveMediaFile(ComponentMedia media, InputStream fileInput);

	/**
	 * Saves component resource to disk sets the filename
	 *
	 * @param resource
	 * @param fileInput
	 */
	public void saveResourceFile(ComponentResource resource, InputStream fileInput);

	/**
	 * Find Recently Added
	 *
	 * @param maxResults
	 * @return
	 */
	public List<Component> findRecentlyAdded(int maxResults);

	/**
	 * Save full review
	 *
	 * @param review
	 * @param pros
	 * @param cons
	 * @return Validation Results
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public ValidationResult saveDetailReview(ComponentReview review, List<ComponentReviewPro> pros, List<ComponentReviewCon> cons);

	/**
	 * This will grab components in an efficient manner, possible given the id's
	 *
	 * @param componentIds
	 * @return
	 */
	public List<ComponentSearchView> getSearchComponentList(List<String> componentIds);

	/**
	 * Saves an Component Integration
	 *
	 * @param integration
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentIntegration(ComponentIntegration integration);

	/**
	 * Saves an Component Integration config Note: this will create a component
	 * integration if it doesn't exist.
	 *
	 * @param integrationConfig
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public ComponentIntegrationConfig saveComponentIntegrationConfig(ComponentIntegrationConfig integrationConfig);

	/**
	 * Saves an Component Integration config
	 *
	 * @param integrationConfigId
	 * @param activeStatus
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void setStatusOnComponentIntegrationConfig(String integrationConfigId, String activeStatus);

	/**
	 * Gets Active Integrations
	 *
	 * @param activeStatus
	 * @return
	 */
	public List<ComponentIntegration> getComponentIntegrationModels(String activeStatus);

	/**
	 * Enable/Disables integration
	 *
	 * @param componentId
	 * @param status
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void setStatusOnComponentIntegration(String componentId, String status);

	/**
	 * This handling running call active integration configs for a component
	 *
	 * @param componentId
	 * @param integrationConfigId
	 */
	public void processComponentIntegration(String componentId, String integrationConfigId);

	/**
	 * This will delete the integration and all child configs
	 *
	 * @param componentId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteComponentIntegration(String componentId);

	/**
	 * This will delete the integration config
	 *
	 * @param integrationConfigId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteComponentIntegrationConfig(String integrationConfigId);

	/**
	 * Handle bulk changing of component Attributes...even across components
	 * Passed in Attribute should be Live ("proxy") entities.
	 *
	 * @param bulkComponentAttributeChange
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void bulkComponentAttributeChange(BulkComponentAttributeChange bulkComponentAttributeChange);

	/**
	 * Get components according to filter
	 *
	 * @param filter
	 * @param componentId
	 * @return
	 */
	public ComponentAdminWrapper getFilteredComponents(ComponentFilterParams filter, String componentId);

	/**
	 * Component name search Used for getting the name through a typeahead
	 *
	 * @param search
	 * @return
	 */
	public Set<LookupModel> getTypeahead(String search);

	/**
	 * This checks the component approval state and handle alerting about
	 * pending to not submitted state.
	 *
	 * @param componentId
	 * @param newApprovalStatus
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void checkComponentCancelStatus(String componentId, String newApprovalStatus);

	/**
	 * This checks the component approval state and handle alerting about
	 * pending to not submitted state.
	 *
	 * @param componentId
	 * @param newApprovalStatus
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void checkChangeRequestCancelStatus(String componentId, String newApprovalStatus);

	/**
	 * Copy a component and create a newId; this will copy all related data as
	 * well even media and resources. Note the Copy will be in Pending status
	 * and the name will have Copy
	 *
	 * @param orignalComponentId
	 * @return Top level component of the copy
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public Component copy(String orignalComponentId);

	/**
	 * Creates a version of the component and all related data
	 *
	 * @param componentId
	 * @param fileHistoryId
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public ComponentVersionHistory snapshotVersion(String componentId, String fileHistoryId);

	/**
	 * Loads a view a snapshot
	 *
	 * @param versionHistoryId
	 * @return View model or null it doesn't exist
	 */
	public ComponentDetailView viewSnapshot(String versionHistoryId);

	/**
	 * Restores a snapshot and replaces the live version (according to options)
	 *
	 * @param versionHistoryId
	 * @param options
	 * @return
	 */
	public Component restoreSnapshot(String versionHistoryId, ComponentRestoreOptions options);

	/**
	 * Removes a snapshot
	 *
	 * @param versionHistoryId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteSnapshot(String versionHistoryId);

	/**
	 * Merges components together. Affects contacts, media, resources,
	 * attributes, review, Q and A...etc This target base component remains
	 * unaffected. This can be used to clean up duplicates.
	 *
	 * @param toMergeComponentId
	 * @param targetComponentId
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public Component merge(String toMergeComponentId, String targetComponentId);

	/**
	 * Find the top viewed components
	 *
	 * @param maxRecords
	 * @return
	 */
	public List<ComponentRecordStatistic> findTopViewedComponents(Integer maxRecords);

	/**
	 * Get all component types (Cached)
	 *
	 * @return all defined component types
	 */
	public List<ComponentType> getAllComponentTypes();

	/**
	 * Save a component type
	 *
	 * @param componentType
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public ComponentType saveComponentType(ComponentType componentType);

	/**
	 * This just inActivates unless newComponent is supplied in which case it
	 * moves the data and deletes component type.
	 *
	 * @param componentType
	 * @param newComponentType (optional unless forcing delete)
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void removeComponentType(String componentType, String newComponentType);

	/**
	 * Saves a new template
	 *
	 * @param componentTypeTemplate
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public ComponentTypeTemplate saveComponentTemplate(ComponentTypeTemplate componentTypeTemplate);

	/**
	 * This just inActivates. Deleting would be dangerous as there likely
	 * existing data that would still need it
	 *
	 * @param templateId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void removeComponentTypeTemplate(String templateId);

	/**
	 * This is a Hard delete but it will fail if entry type are pointing to it.
	 *
	 * @param templateId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteComponentTypeTemplate(String templateId);

	/**
	 * Save a template block for use in a template
	 *
	 * @param templateBlock
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveTemplateBlock(TemplateBlock templateBlock);

	/**
	 * Hard delete of a template block
	 *
	 * @param templateBlockId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteTemplateBlock(String templateBlockId);

	/**
	 * Approves a component and triggers notification if requested component is
	 * already approved this does nothing.
	 *
	 * @param componentId
	 * @return Component Approved or null if not found
	 */
	public Component approveComponent(String componentId);

	/**
	 * This updates a components owner.
	 *
	 * @param componentId
	 * @param newOwner
	 * @return component modified
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public Component changeOwner(String componentId, String newOwner);

	/**
	 * Creates a pending component record for the given component Id
	 *
	 * @param parentComponentId
	 * @return Pending Change component
	 */
	public Component createPendingChangeComponent(String parentComponentId);

	/**
	 * Merges pending changes to the old component (effectively Replacing the
	 * old component)
	 *
	 *
	 * @param componentIdOfPendingChange
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public Component mergePendingChange(String componentIdOfPendingChange);

	/**
	 * Quick look up the entry icon
	 *
	 * @param componentId
	 * @return
	 */
	public String resolveComponentIcon(String componentId);

	/**
	 * Quick look up the entry type icon
	 *
	 * @param componentType
	 * @return
	 */
	public String resolveComponentTypeIcon(String componentType);

}
