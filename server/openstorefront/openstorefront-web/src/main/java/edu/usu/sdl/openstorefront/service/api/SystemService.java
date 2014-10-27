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

import com.atlassian.jira.rest.client.api.domain.CimFieldInfo;
import edu.usu.sdl.openstorefront.service.ServiceInterceptor;
import edu.usu.sdl.openstorefront.service.TransactionInterceptor;
import edu.usu.sdl.openstorefront.service.manager.model.JiraIssueModel;
import edu.usu.sdl.openstorefront.service.transfermodel.AttributeXrefModel;
import edu.usu.sdl.openstorefront.service.transfermodel.ErrorInfo;
import edu.usu.sdl.openstorefront.storage.model.ApplicationProperty;
import edu.usu.sdl.openstorefront.storage.model.Highlight;
import edu.usu.sdl.openstorefront.storage.model.Integration;
import edu.usu.sdl.openstorefront.storage.model.XRefAttributeType;
import edu.usu.sdl.openstorefront.web.rest.model.GlobalIntegrationModel;
import edu.usu.sdl.openstorefront.web.viewmodel.LookupModel;
import edu.usu.sdl.openstorefront.web.viewmodel.SystemErrorModel;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
public interface SystemService
{

	/**
	 * Gets Full application Property
	 *
	 * @param key
	 * @return null if property is not found
	 */
	public ApplicationProperty getProperty(String key);

	/**
	 * Gets property Value
	 *
	 * @param key
	 * @return null if property is not found
	 */
	public String getPropertyValue(String key);

	/**
	 * Save a property to Application Properties. Application properties are
	 * used to store application specific state. If the property should be admin
	 * configurable it should be store in the properties file
	 *
	 * @param key
	 * @param value
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveProperty(String key, String value);

	/**
	 * Saves Highlights
	 *
	 * @param highlights
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveHightlight(List<Highlight> highlights);

	/**
	 * Saves Highlights (generates Id on create)
	 *
	 * @param highlight
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveHightlight(Highlight highlight);

	/**
	 * Inactive Hightlight
	 *
	 * @param hightlightId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void removeHighlight(String hightlightId);

	/**
	 * Delete all old and save new highlights This assume to be called by the
	 * importer and is meant to sync the DB with the file
	 *
	 * @param highlights
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void syncHighlights(List<Highlight> highlights);

	/**
	 * This handle the error and generate an error ticket
	 *
	 * @param errorInfo
	 * @return SystemErrorModel
	 */
	public SystemErrorModel generateErrorTicket(ErrorInfo errorInfo);

	/**
	 * Retrieves the error ticket from the file system
	 *
	 * @param errorTicketId
	 * @return
	 */
	public String errorTicketInfo(String errorTicketId);

	/**
	 * Removes excess errors beyond max....deleting oldest first
	 */
	public void cleanupOldErrors();

	/**
	 * Gets Active Integrations
	 *
	 * @param activeStatus
	 * @return
	 */
	public List<Integration> getIntegrationModels(String activeStatus);

	/**
	 *
	 * @return
	 */
	public GlobalIntegrationModel getGlobalConfig();

	/**
	 *
	 * @param integration
	 * @param isPost
	 * @return
	 */
	public Integration saveIntegration(Integration integration, boolean isPost);

	/**
	 *
	 * @param integration
	 * @param isPost
	 * @return
	 */
	public GlobalIntegrationModel saveIntegration(GlobalIntegrationModel integration, boolean isPost);

	/**
	 *
	 * @param componentId
	 */
	public void deactivateIntegration(String componentId);

	/**
	 *
	 */
	public void deactivateIntegration();

	/**
	 *
	 * @return
	 */
	public List<LookupModel> getAllJiraProjects();

	/**
	 *
	 * @param code
	 * @return
	 */
	public List<JiraIssueModel> getAllProjectIssueTypes(String code);

	public Map<String, CimFieldInfo> getIssueTypeFields(String code, String type);

	/**
	 * This handling running call active integration configs for a component
	 *
	 * @param componentId
	 * @param integrationConfigId
	 */
	public void processIntegration(String componentId, String integrationConfigId);

	/**
	 * Gets the active xref types for an IntegrationType
	 *
	 * @param attributeXrefModel
	 * @return
	 */
	public List<XRefAttributeType> getXrefAttributeTypes(AttributeXrefModel attributeXrefModel);

	/**
	 * Gets the code mappings
	 *
	 * @return Attribute key, external code, our code
	 */
	public Map<String, Map<String, String>> getXrefAttributeMapFieldMap();

}
