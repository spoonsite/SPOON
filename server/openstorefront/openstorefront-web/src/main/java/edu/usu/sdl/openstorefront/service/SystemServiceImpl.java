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

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.CimFieldInfo;
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.api.SystemService;
import edu.usu.sdl.openstorefront.service.io.integration.BaseIntegrationHandler;
import edu.usu.sdl.openstorefront.service.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.service.manager.JiraManager;
import edu.usu.sdl.openstorefront.service.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.service.manager.model.JiraFieldInfoModel;
import edu.usu.sdl.openstorefront.service.manager.model.JiraIssueModel;
import edu.usu.sdl.openstorefront.service.manager.model.JiraIssueType;
import edu.usu.sdl.openstorefront.service.manager.resource.JiraClient;
import edu.usu.sdl.openstorefront.service.transfermodel.AttributeXrefModel;
import edu.usu.sdl.openstorefront.service.transfermodel.ErrorInfo;
import edu.usu.sdl.openstorefront.storage.model.ApplicationProperty;
import edu.usu.sdl.openstorefront.storage.model.ErrorTicket;
import edu.usu.sdl.openstorefront.storage.model.Highlight;
import edu.usu.sdl.openstorefront.storage.model.Integration;
import edu.usu.sdl.openstorefront.storage.model.IntegrationConfig;
import edu.usu.sdl.openstorefront.storage.model.RunStatus;
import edu.usu.sdl.openstorefront.storage.model.XRefAttributeMap;
import edu.usu.sdl.openstorefront.storage.model.XRefAttributeType;
import edu.usu.sdl.openstorefront.util.Convert;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.rest.model.GlobalIntegrationModel;
import edu.usu.sdl.openstorefront.web.viewmodel.LookupModel;
import edu.usu.sdl.openstorefront.web.viewmodel.SystemErrorModel;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * Handles System related entities
 *
 * @author dshurtleff
 */
@SuppressWarnings("ClassWithMultipleLoggers")
public class SystemServiceImpl
		extends ServiceProxy
		implements SystemService
{

	private static final Logger log = Logger.getLogger(SystemServiceImpl.class.getName());
	private static final Logger errorLog = Logger.getLogger(OpenStorefrontConstant.ERROR_LOGGER);

	@Override
	public ApplicationProperty getProperty(String key)
	{
		ApplicationProperty applicationProperty = null;
		applicationProperty = persistenceService.findById(ApplicationProperty.class, key);
		return applicationProperty;
	}

	@Override
	public String getPropertyValue(String key)
	{
		ApplicationProperty property = getProperty(key);
		if (property != null) {
			return property.getValue();
		}
		return null;
	}

	@Override
	public void saveProperty(String key, String value)
	{
		ApplicationProperty existingProperty = persistenceService.findById(ApplicationProperty.class, key);
		if (existingProperty != null) {
			existingProperty.setValue(value);
			existingProperty.setUpdateDts(TimeUtil.currentDate());
			existingProperty.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
			persistenceService.persist(existingProperty);
		} else {
			ApplicationProperty property = new ApplicationProperty();
			property.setKey(key);
			property.setValue(value);
			property.setActiveStatus(ApplicationProperty.ACTIVE_STATUS);
			property.setCreateDts(TimeUtil.currentDate());
			property.setUpdateDts(TimeUtil.currentDate());
			property.setCreateUser(OpenStorefrontConstant.SYSTEM_USER);
			property.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
			persistenceService.persist(property);
		}
	}

	@Override
	public void saveHightlight(List<Highlight> highlights)
	{
		for (Highlight hightlight : highlights) {
			saveHightlight(hightlight);
		}
	}

	@Override
	public void saveHightlight(Highlight highlight)
	{
		Highlight existing = null;
		if (StringUtils.isNotBlank(highlight.getHighlightId())) {
			existing = persistenceService.findById(Highlight.class, highlight.getHighlightId());
		}
		if (existing != null) {
			existing.setDescription(highlight.getDescription());
			existing.setHighlightType(highlight.getHighlightType());
			existing.setLink(highlight.getLink());
			existing.setTitle(highlight.getTitle());
			existing.setActiveStatus(Highlight.ACTIVE_STATUS);
			existing.setUpdateDts(TimeUtil.currentDate());
			existing.setUpdateUser(highlight.getUpdateUser());
			persistenceService.persist(existing);

		} else {
			highlight.setHighlightId(persistenceService.generateId());
			highlight.setActiveStatus(Highlight.ACTIVE_STATUS);
			highlight.setCreateDts(TimeUtil.currentDate());
			highlight.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(highlight);
		}
	}

	@Override
	public void removeHighlight(String hightlightId)
	{
		Highlight highlight = persistenceService.findById(Highlight.class, hightlightId);
		if (highlight != null) {
			highlight.setActiveStatus(Highlight.INACTIVE_STATUS);
			highlight.setUpdateUser(SecurityUtil.getCurrentUserName());
			highlight.setUpdateDts(TimeUtil.currentDate());
			persistenceService.persist(highlight);
		}
	}

	@Override
	public void syncHighlights(List<Highlight> highlights)
	{
		int removeCount = persistenceService.deleteByExample(new Highlight());
		log.log(Level.FINE, MessageFormat.format("Old Highlights removed: {0}", removeCount));

		for (Highlight highlight : highlights) {
			try {
				ValidationModel validationModel = new ValidationModel(highlight);
				validationModel.setConsumeFieldsOnly(true);
				ValidationResult validationResult = ValidationUtil.validate(validationModel);
				if (validationResult.valid()) {
					highlight.setCreateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
					highlight.setUpdateUser(OpenStorefrontConstant.SYSTEM_ADMIN_USER);
					getSystemService().saveHightlight(highlight);
				}

			} catch (Exception e) {
				log.log(Level.SEVERE, "Unable to save highlight.  Title: " + highlight.getTitle(), e);
			}
		}
	}

	@Override
	public SystemErrorModel generateErrorTicket(ErrorInfo errorInfo)
	{
		Objects.requireNonNull(errorInfo);

		errorLog.log(Level.SEVERE, "System Error Occured", errorInfo.getError());

		SystemErrorModel systemErrorModel = new SystemErrorModel();
		systemErrorModel.setMessage(errorInfo.getError().getMessage());
		if (errorInfo.getError() instanceof OpenStorefrontRuntimeException) {
			OpenStorefrontRuntimeException openStorefrontRuntimeException = (OpenStorefrontRuntimeException) errorInfo.getError();
			systemErrorModel.setPotentialResolution(openStorefrontRuntimeException.getPotentialResolution());
			errorInfo.setErrorTypeCode(openStorefrontRuntimeException.getErrorTypeCode());
		}
		try {

			String ticketNumber = persistenceService.generateId();
			StringBuilder ticket = new StringBuilder();
			ticket.append("TicketNumber: ").append(ticketNumber).append("\n");
			ticket.append("Client IP: ").append(errorInfo.getClientIp()).append("\n");
			ticket.append("User: ").append(SecurityUtil.getCurrentUserName()).append("\n");
			ticket.append("Request: ").append(errorInfo.getRequestUrl()).append("\n");
			ticket.append("Request Method: ").append(errorInfo.getRequestMethod()).append("\n");
			ticket.append("Input Data: \n").append(errorInfo.getInputData()).append("\n\n");
			ticket.append("StackTrace: ").append("\n\n");

			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			errorInfo.getError().printStackTrace(printWriter);
			ticket.append(stringWriter.toString());

			systemErrorModel.setErrorTicketNumber(ticketNumber);
			ErrorTicket errorTicket = new ErrorTicket();
			errorTicket.setErrorTicketId(ticketNumber);
			errorTicket.setTicketFile(ticketNumber);
			errorTicket.setClientIp(errorInfo.getClientIp());
			errorTicket.setCalledAction(errorInfo.getRequestUrl() + " Method: " + errorInfo.getRequestMethod());
			errorTicket.setErrorTypeCode(errorInfo.getErrorTypeCode());
			errorTicket.setInput(errorInfo.getInputData());
			errorTicket.setActiveStatus(ErrorTicket.ACTIVE_STATUS);
			errorTicket.setCreateDts(TimeUtil.currentDate());
			errorTicket.setUpdateDts(TimeUtil.currentDate());
			errorTicket.setCreateUser(SecurityUtil.getCurrentUserName());
			errorTicket.setUpdateUser(SecurityUtil.getCurrentUserName());
			persistenceService.persist(errorTicket);

			//save file
			Path path = Paths.get(FileSystemManager.getDir(FileSystemManager.ERROR_TICKET_DIR).getPath() + "/" + errorTicket.getTicketFile());
			Files.write(path, ticket.toString().getBytes());

		} catch (Throwable t) {
			//NOTE: this is a critial path.  if an error is thrown and not catch it would result in a info link or potential loop.
			//So that's why there is a catch all here.
			log.log(Level.SEVERE, "Error was thrown while processing the error", t);
		}
		return systemErrorModel;
	}

	@Override
	public String errorTicketInfo(String errorTicketId)
	{
		String ticketData = null;
		ErrorTicket errorTicket = persistenceService.findById(ErrorTicket.class, errorTicketId);
		if (errorTicket != null) {
			Path path = Paths.get(FileSystemManager.getDir(FileSystemManager.ERROR_TICKET_DIR).getPath() + "/" + errorTicket.getTicketFile());
			try {
				byte data[] = Files.readAllBytes(path);
				ticketData = new String(data);
			} catch (IOException io) {
				//We don't want to throw an error here if there something going on with the system.
				ticketData = "Unable to retrieve ticket information.  (Check log for more details) Message: " + io.getMessage();
				log.log(Level.WARNING, ticketData, io);
			}
		}
		return ticketData;
	}

	@Override
	public void cleanupOldErrors()
	{
		long count = persistenceService.countClass(ErrorTicket.class);
		long max = Long.parseLong(PropertiesManager.getValue(PropertiesManager.KEY_MAX_ERROR_TICKETS, OpenStorefrontConstant.ERRORS_MAX_COUNT_DEFAULT));

		if (count > max) {

			//query ticket
			long limit = count - max;
			String query = "SELECT FROM ErrorTicket ORDER BY updateDts ASC LIMIT " + limit;
			List<ErrorTicket> errorTickets = persistenceService.query(query, null);
			errorTickets.stream().forEach((errorTicket) -> {
				Path path = Paths.get(FileSystemManager.getDir(FileSystemManager.ERROR_TICKET_DIR).getPath() + "/" + errorTicket.getTicketFile());
				if (path.toFile().exists()) {
					path.toFile().delete();
				}
				persistenceService.delete(errorTicket);
			});
		}
	}

	@Override
	public List<Integration> getIntegrationModels(String activeStatus)
	{
		Integration integrationExample = new Integration();
		integrationExample.setActiveStatus(activeStatus);
		List<Integration> integrations = persistenceService.queryByExample(Integration.class, integrationExample);
		return integrations;
	}

	@Override
	public Integration saveIntegration(Integration integration, boolean isPost)
	{
		// save Integration model here and return it.
		return integration;
	}

	@Override
	public void deactivateIntegration(String componentId)
	{
		// deactivate the Integration here...
	}

	@Override
	public GlobalIntegrationModel saveIntegration(GlobalIntegrationModel integration, boolean isPost)
	{
		// save the global configuratoin
		return integration;
	}

	@Override
	public void deactivateIntegration()
	{
		// TODO: deactivate all integration models.
	}

	@Override
	public GlobalIntegrationModel getGlobalConfig()
	{
		return new GlobalIntegrationModel();
	}

	@Override
	public List<LookupModel> getAllJiraProjects()
	{
		List<LookupModel> response = new ArrayList<>();
		try (JiraClient jiraClient = JiraManager.getClient()) {
			Iterable<BasicProject> projects = jiraClient.getAllProjects();
			for (BasicProject project : projects) {
				LookupModel temp = new LookupModel();
				temp.setDescription(project.getName());
				temp.setCode(project.getKey());
				response.add(temp);
			}
		}
		return response;
	}

	@Override
	public List<JiraIssueModel> getAllProjectIssueTypes(String code)
	{
		List<JiraIssueModel> response = new ArrayList<>();
		try (JiraClient jiraClient = JiraManager.getClient()) {
			response = jiraClient.getIssueTypesForProject(code);
		}
		return response;
	}

	@Override
	public List<JiraFieldInfoModel> getIssueTypeFields(String projectCode, String issueType)
	{
		List<JiraFieldInfoModel> response = new ArrayList<>();
		Map<String, CimFieldInfo> results = null;
		List<JiraIssueType> statuses = new ArrayList<>();
		JiraIssueType jiraIssueType = null;
		try (JiraClient jiraClient = JiraManager.getClient()) {
			results = jiraClient.getProjectIssueTypeFields(projectCode, issueType);
			if (results != null) {
				for (Map.Entry<String, CimFieldInfo> entry : results.entrySet()) {
					String key = entry.getKey();
					CimFieldInfo cimFieldInfo = entry.getValue();
					if (cimFieldInfo.getAllowedValues() != null) {
						JiraFieldInfoModel model = JiraFieldInfoModel.toView(key, cimFieldInfo);
						if (model != null) {
							response.add(model);
						}
					}
				}
			}
			statuses = jiraClient.getProjectStatusForAllIssueTypes(projectCode);
			for (int i = 0; i < statuses.size(); i++) {
				if (statuses.get(i).getName().equals(jiraIssueType)) {
					jiraIssueType = statuses.get(i);
					break;
				}
			}
			if (jiraIssueType != null) {
				response.add(JiraFieldInfoModel.toView(jiraIssueType));
			}
		}
		return response;
	}

	@Override
	public void processIntegration(String componentId, String integrationConfigId)
	{
		Integration integrationExample = new Integration();
		integrationExample.setActiveStatus(Integration.ACTIVE_STATUS);
		integrationExample.setComponentId(componentId);
		Integration integration = persistenceService.queryOneByExample(Integration.class, integrationExample);
		if (integration != null) {

			boolean run = true;
			if (RunStatus.WORKING.equals(integration.getStatus())) {
				//check for override
				String overrideTime = PropertiesManager.getValue(PropertiesManager.KEY_JOB_WORKING_STATE_OVERRIDE, "30");
				if (integration.getLastStartTime() != null) {
					LocalDateTime maxLocalDateTime = LocalDateTime.ofInstant(integration.getLastStartTime().toInstant(), ZoneId.systemDefault());
					maxLocalDateTime.plusMinutes(Convert.toLong(overrideTime));
					if (maxLocalDateTime.compareTo(LocalDateTime.now()) <= 0) {
						log.log(Level.FINE, "Overriding the working it assume it was stuck.");
						run = true;
					} else {
						run = false;
					}
				} else {
					throw new OpenStorefrontRuntimeException("Missing Last Start time.  Data is corrupt.", "Delete the job and recreate it.");
				}
			}

			if (run) {

				integration.setStatus(RunStatus.WORKING);
				integration.setLastStartTime(TimeUtil.currentDate());
				integration.setUpdateDts(TimeUtil.currentDate());
				integration.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
				persistenceService.persist(integration);

				IntegrationConfig integrationConfigExample = new IntegrationConfig();
				integrationConfigExample.setActiveStatus(IntegrationConfig.ACTIVE_STATUS);
				integrationConfigExample.setComponentId(componentId);
				integrationConfigExample.setIntegrationConfigId(integrationConfigId);

				List<IntegrationConfig> integrationConfigs = persistenceService.queryByExample(IntegrationConfig.class, integrationConfigExample);
				boolean errorConfig = false;
				if (integrationConfigs.isEmpty()) {
					for (IntegrationConfig integrationConfig : integrationConfigs) {
						try {
							integrationConfig.setStatus(RunStatus.WORKING);
							integrationConfig.setLastStartTime(TimeUtil.currentDate());
							integrationConfig.setUpdateDts(TimeUtil.currentDate());
							integrationConfig.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
							persistenceService.persist(integrationConfig);

							BaseIntegrationHandler baseIntegrationHandler = BaseIntegrationHandler.getIntegrationHandler(integrationConfig);
							if (baseIntegrationHandler != null) {
								baseIntegrationHandler.processConfig();
							} else {
								throw new OpenStorefrontRuntimeException("Intergration handler not supported for " + integrationConfig.getIntegrationType(), "Add handler");
							}

							integrationConfig.setStatus(RunStatus.COMPLETE);
							integrationConfig.setLastEndTime(TimeUtil.currentDate());
							integrationConfig.setUpdateDts(TimeUtil.currentDate());
							integrationConfig.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
							persistenceService.persist(integrationConfig);
						} catch (Exception e) {
							errorConfig = true;
							//This is a critical loop
							ErrorInfo errorInfo = new ErrorInfo(e, null);
							SystemErrorModel errorModel = generateErrorTicket(errorInfo);

							//put in fail state
							integrationConfig.setStatus(RunStatus.ERROR);
							integrationConfig.setErrorMessage(errorModel.getMessage());
							integrationConfig.setErrorTicketNumber(errorModel.getErrorTicketNumber());
							integrationConfig.setLastEndTime(TimeUtil.currentDate());
							integrationConfig.setUpdateDts(TimeUtil.currentDate());
							integrationConfig.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
							persistenceService.persist(integrationConfig);
						}
					}
				}

				if (errorConfig) {
					integration.setStatus(RunStatus.ERROR);
				} else {
					integration.setStatus(RunStatus.COMPLETE);
				}
				integration.setLastEndTime(TimeUtil.currentDate());
				integration.setUpdateDts(TimeUtil.currentDate());
				integration.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
				persistenceService.persist(integration);

			} else {
				log.log(Level.FINE, MessageFormat.format("Not time to run integration or the system is currently working on the integration. Component Id: {0}", componentId));
			}
		} else {
			log.log(Level.WARNING, MessageFormat.format("There are no active integration for this component. Id: {0}", componentId));
		}
	}

	@Override
	public List<XRefAttributeType> getXrefAttributeTypes(AttributeXrefModel attributeXrefModel)
	{
		XRefAttributeType xrefAttributeTypeExample = new XRefAttributeType();
		xrefAttributeTypeExample.setActiveStatus(XRefAttributeType.ACTIVE_STATUS);
		xrefAttributeTypeExample.setIntegrationType(attributeXrefModel.getIntegrationType());
		xrefAttributeTypeExample.setProjectType(attributeXrefModel.getProjectKey());
		xrefAttributeTypeExample.setIssueType(attributeXrefModel.getIssueType());
		List<XRefAttributeType> xrefAttributeTypes = persistenceService.queryByExample(XRefAttributeType.class, xrefAttributeTypeExample);
		return xrefAttributeTypes;
	}

	@Override
	public Map<String, Map<String, String>> getXrefAttributeMapFieldMap()
	{
		Map<String, Map<String, String>> attributeCodeMap = new HashMap<>();

		XRefAttributeMap xrefAttributeMapExample = new XRefAttributeMap();
		xrefAttributeMapExample.setActiveStatus(XRefAttributeMap.ACTIVE_STATUS);

		List<XRefAttributeMap> xrefAttributeMaps = persistenceService.queryByExample(XRefAttributeMap.class, xrefAttributeMapExample);
		for (XRefAttributeMap xrefAttributeMap : xrefAttributeMaps) {

			if (attributeCodeMap.containsKey(xrefAttributeMap.getAttributeType())) {
				Map<String, String> codeMap = attributeCodeMap.get(xrefAttributeMap.getAttributeType());
				if (codeMap.containsKey(xrefAttributeMap.getExternalCode())) {

					//should only have one external code if there's a dup will only use one.
					//(however, which  code  is used dependa on the order that came in.  which is not  determinate)
					//First one we hit wins
					log.log(Level.WARNING, MessageFormat.format("Duplicate external code for attribute type: {0} Code: {1}", new Object[]{xrefAttributeMap.getAttributeType(), xrefAttributeMap.getExternalCode()}));
				} else {
					codeMap.put(xrefAttributeMap.getExternalCode(), xrefAttributeMap.getLocalCode());
				}
			} else {
				Map<String, String> codeMap = new HashMap<>();
				codeMap.put(xrefAttributeMap.getExternalCode(), xrefAttributeMap.getLocalCode());
				attributeCodeMap.put(xrefAttributeMap.getAttributeType(), codeMap);
			}
		}

		return attributeCodeMap;
	}

}
