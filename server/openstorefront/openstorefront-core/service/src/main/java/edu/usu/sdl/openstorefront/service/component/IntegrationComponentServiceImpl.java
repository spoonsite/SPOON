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
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.AttributeXRefType;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentIntegrationConfig;
import edu.usu.sdl.openstorefront.core.entity.ErrorTypeCode;
import edu.usu.sdl.openstorefront.core.model.AttributeXrefModel;
import edu.usu.sdl.openstorefront.service.ComponentServiceImpl;
import static edu.usu.sdl.openstorefront.service.io.integration.JiraIntegrationHandler.STATUS_FIELD;
import java.text.MessageFormat;
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
}
