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

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueField;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.AttributeXRefType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentIntegration;
import edu.usu.sdl.openstorefront.core.entity.ComponentIntegrationConfig;
import edu.usu.sdl.openstorefront.core.entity.ErrorTypeCode;
import edu.usu.sdl.openstorefront.core.entity.RunStatus;
import edu.usu.sdl.openstorefront.core.model.AttributeXrefModel;
import edu.usu.sdl.openstorefront.core.model.ErrorInfo;
import edu.usu.sdl.openstorefront.core.view.SystemErrorModel;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.ComponentServiceImpl;
import edu.usu.sdl.openstorefront.service.io.integration.BaseIntegrationHandler;
import static edu.usu.sdl.openstorefront.service.io.integration.JiraIntegrationHandler.STATUS_FIELD;
import edu.usu.sdl.openstorefront.service.manager.JobManager;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author dshurtleff
 */
public class IntegrationComponentServiceImpl
		extends BaseComponentServiceImpl
{

	private static final Logger LOG = Logger.getLogger(IntegrationComponentServiceImpl.class.getName());

	public IntegrationComponentServiceImpl(ComponentServiceImpl componentService)
	{
		super(componentService);
	}

	public void mapComponentAttributes(Issue issue, ComponentIntegrationConfig integrationConfig)
	{
		Objects.requireNonNull(issue, "Jira Issue Required");
		Objects.requireNonNull(integrationConfig, "Integration Config Required");

		LOG.finer("Pull Xref Mapping");
		AttributeXrefModel attributeXrefModel = new AttributeXrefModel();
		attributeXrefModel.setIntegrationType(integrationConfig.getIntegrationType());
		attributeXrefModel.setProjectKey(integrationConfig.getProjectType());
		attributeXrefModel.setIssueType(integrationConfig.getIssueType());

		List<AttributeXRefType> xrefAttributeTypes = componentService.getAttributeService().getAttributeXrefTypes(attributeXrefModel);
		Map<String, Map<String, String>> xrefAttributeMaps = componentService.getAttributeService().getAttributeXrefMapFieldMap();

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
					} else if (jiraField.getValue() != null) {
						jiraValue = jiraField.getValue().toString();
					}
				} else {
					throw new OpenStorefrontRuntimeException("Unable to find Jira Field: " + xrefAttributeType.getFieldName(), "Update mapping to match jira.", ErrorTypeCode.INTEGRATION);
				}
			}

			if (StringUtils.isBlank(jiraValue)) {
				AttributeType attributeType = persistenceService.findById(AttributeType.class, xrefAttributeType.getAttributeType());
				if (attributeType != null) {
					if (Convert.toBoolean(attributeType.getRequiredFlg()) == false) {
						LOG.log(Level.FINEST, "Jira Value is Blank....remove any existing component attribute since Attribute type is not require.");
						ComponentAttributePk componentAttributePk = new ComponentAttributePk();
						componentAttributePk.setComponentId(integrationConfig.getComponentId());
						componentAttributePk.setAttributeType(xrefAttributeType.getAttributeType());
						ComponentAttribute componentAttributeExample = new ComponentAttribute();
						componentAttributeExample.setComponentAttributePk(componentAttributePk);
						persistenceService.deleteByExample(componentAttributeExample);

						componentChanged = true;
					} else {
						LOG.log(Level.WARNING, MessageFormat.format("Attribute Type is required and Integration is returned a empty value.  Keeping exisiting value on component: {0}  Attribute Type: {1}",
								new Object[]{core.getComponentName(integrationConfig.getComponentId()), attributeType.getDescription()}));
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
							sub.saveComponentAttribute(componentAttribute, false);
							componentChanged = true;
						} else {
							LOG.log(Level.FINEST, "Attibute already exists in that state...skipping");
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

	public List<ComponentIntegration> getComponentIntegrationModels(String activeStatus)
	{
		ComponentIntegration integrationExample = new ComponentIntegration();
		integrationExample.setActiveStatus(activeStatus);
		List<ComponentIntegration> integrations = persistenceService.queryByExample(integrationExample);
		return integrations;
	}

	public void processComponentIntegration(String componentId, String integrationConfigId)
	{
		ComponentIntegration integrationExample = new ComponentIntegration();
		integrationExample.setActiveStatus(ComponentIntegration.ACTIVE_STATUS);
		integrationExample.setComponentId(componentId);
		ComponentIntegration componentIntegration = persistenceService.queryOneByExample(integrationExample);
		if (componentIntegration != null) {

			boolean run = true;
			if (RunStatus.WORKING.equals(componentIntegration.getStatus())) {
				//check for override
				String overrideTime = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_JOB_WORKING_STATE_OVERRIDE, "30");
				if (componentIntegration.getLastStartTime() != null) {
					LocalDateTime maxLocalDateTime = LocalDateTime.ofInstant(componentIntegration.getLastStartTime().toInstant(), ZoneId.systemDefault());
					maxLocalDateTime = maxLocalDateTime.plusMinutes(Convert.toLong(overrideTime));
					if (maxLocalDateTime.compareTo(LocalDateTime.now()) <= 0) {
						LOG.log(Level.FINE, "Overriding the working state...assume it was stuck.");
						run = true;
					} else {
						run = false;
					}
				} else {
					throw new OpenStorefrontRuntimeException("Missing Last Start time.  Data is corrupt.", "Delete the job (Integration) and recreate it.", ErrorTypeCode.INTEGRATION);
				}
			}

			if (run) {
				Component component = persistenceService.findById(Component.class, componentIntegration.getComponentId());
				ComponentIntegration liveIntegration = persistenceService.findById(ComponentIntegration.class, componentIntegration.getComponentId());

				LOG.log(Level.FINE, MessageFormat.format("Processing Integration for: {0}", component.getName()));

				liveIntegration.setStatus(RunStatus.WORKING);
				liveIntegration.setLastStartTime(TimeUtil.currentDate());
				liveIntegration.setUpdateDts(TimeUtil.currentDate());
				liveIntegration.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
				persistenceService.persist(liveIntegration);

				ComponentIntegrationConfig integrationConfigExample = new ComponentIntegrationConfig();
				integrationConfigExample.setActiveStatus(ComponentIntegrationConfig.ACTIVE_STATUS);
				integrationConfigExample.setComponentId(componentId);
				integrationConfigExample.setIntegrationConfigId(integrationConfigId);

				List<ComponentIntegrationConfig> integrationConfigs = persistenceService.queryByExample(integrationConfigExample);
				boolean errorConfig = false;
				if (integrationConfigs.isEmpty() == false) {
					for (ComponentIntegrationConfig integrationConfig : integrationConfigs) {
						ComponentIntegrationConfig liveConfig = persistenceService.findById(ComponentIntegrationConfig.class, integrationConfig.getIntegrationConfigId());
						try {
							LOG.log(Level.FINE, MessageFormat.format("Working on {1} Configuration for Integration for: {0}", component.getName(), integrationConfig.getIntegrationType()));

							liveConfig.setStatus(RunStatus.WORKING);
							liveConfig.setErrorMessage(null);
							liveConfig.setErrorTicketNumber(null);
							liveConfig.setPotentialResolution(null);
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

							LOG.log(Level.FINE, MessageFormat.format("Completed {1} Configuration for Integration for: {0}", component.getName(), integrationConfig.getIntegrationType()));
						} catch (Exception e) {
							errorConfig = true;
							//This is a critical loop
							ErrorInfo errorInfo = new ErrorInfo(e, null);
							SystemErrorModel errorModel = componentService.getSystemService().generateErrorTicket(errorInfo);

							//put in fail state
							liveConfig.setStatus(RunStatus.ERROR);
							liveConfig.setErrorMessage(errorModel.getMessage());
							liveConfig.setErrorTicketNumber(errorModel.getErrorTicketNumber());
							liveConfig.setLastEndTime(TimeUtil.currentDate());
							liveConfig.setUpdateDts(TimeUtil.currentDate());
							liveConfig.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
							persistenceService.persist(liveConfig);

							LOG.log(Level.FINE, MessageFormat.format("Failed on {1} Configuration for Integration for: {0}", component.getName(), integrationConfig.getIntegrationType()), e);
						}
					}
				} else {
					LOG.log(Level.WARNING, MessageFormat.format("No Active Integration configs for: {0} (Integration is doing nothing)", component.getName()));
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

				LOG.log(Level.FINE, MessageFormat.format("Completed Integration for: {0}", component.getName()));
			} else {
				LOG.log(Level.FINE, MessageFormat.format("Not time to run integration or the system is currently working on the integration. Component Id: {0}", componentId));
			}
		} else {
			LOG.log(Level.WARNING, MessageFormat.format("There is no active integration for this component. Id: {0}", componentId));
		}
	}

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

	public void deleteComponentIntegrationConfig(String integrationConfigId)
	{
		ComponentIntegrationConfig componentIntegrationConfig = persistenceService.findById(ComponentIntegrationConfig.class, integrationConfigId);
		if (componentIntegrationConfig != null) {
			persistenceService.delete(componentIntegrationConfig);
		}
	}

}
