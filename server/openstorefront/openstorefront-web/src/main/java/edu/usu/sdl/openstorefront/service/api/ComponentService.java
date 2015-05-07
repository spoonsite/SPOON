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
package edu.usu.sdl.openstorefront.service.api;

import edu.usu.sdl.openstorefront.service.ServiceInterceptor;
import edu.usu.sdl.openstorefront.service.TransactionInterceptor;
import edu.usu.sdl.openstorefront.service.transfermodel.BulkComponentAttributeChange;
import edu.usu.sdl.openstorefront.service.transfermodel.ComponentAll;
import edu.usu.sdl.openstorefront.service.transfermodel.ComponentUploadOption;
import edu.usu.sdl.openstorefront.storage.model.BaseComponent;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.storage.model.ComponentContact;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSection;
import edu.usu.sdl.openstorefront.storage.model.ComponentExternalDependency;
import edu.usu.sdl.openstorefront.storage.model.ComponentIntegration;
import edu.usu.sdl.openstorefront.storage.model.ComponentIntegrationConfig;
import edu.usu.sdl.openstorefront.storage.model.ComponentMedia;
import edu.usu.sdl.openstorefront.storage.model.ComponentMetadata;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestion;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.storage.model.ComponentResource;
import edu.usu.sdl.openstorefront.storage.model.ComponentReview;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewCon;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewPro;
import edu.usu.sdl.openstorefront.storage.model.ComponentTag;
import edu.usu.sdl.openstorefront.storage.model.ComponentTracking;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentAdminWrapper;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentDetailView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentFilterParams;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentReviewView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentSearchView;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentTrackingResult;
import edu.usu.sdl.openstorefront.web.rest.model.FilterQueryParams;
import edu.usu.sdl.openstorefront.web.rest.model.RequiredForComponent;
import edu.usu.sdl.openstorefront.web.viewmodel.LookupModel;
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
	 * @return
	 */
	public ComponentDetailView getComponentDetails(String componentId);

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
	 * Saves a component Attribute
	 *
	 * @param attribute
	 */
	public void saveComponentAttribute(ComponentAttribute attribute);

	/**
	 *
	 * @param filter
	 * @param componentId
	 * @return
	 */
	public ComponentTrackingResult getComponentTracking(FilterQueryParams filter, String componentId);

	/**
	 *
	 * @param attribute
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public Boolean checkComponentAttribute(ComponentAttribute attribute);

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
	 *
	 * @param response
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentQuestionResponse(ComponentQuestionResponse response);

	/**
	 *
	 * @param resource
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
	 *
	 * @param tag
	 */
	public void saveComponentTag(ComponentTag tag);

	/**
	 *
	 * @param tracking
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveComponentTracking(ComponentTracking tracking);

	/**
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
	public ComponentAll saveFullComponent(ComponentAll componentAll, ComponentUploadOption options);

	/**
	 * Submits a component for Approval
	 *
	 * @param componentId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void submitComponentSubmission(String componentId);

	/**
	 * This will handle syncing all the component of the list.
	 *
	 * @param components
	 * @param options
	 */
	public void importComponents(List<ComponentAll> components, ComponentUploadOption options);

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

}
