/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.selenium.apitestclient;

import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.apiclient.rest.resource.ComponentRESTClient;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentIntegration;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.Organization;
import edu.usu.sdl.openstorefront.core.entity.RunStatus;
import static edu.usu.sdl.openstorefront.core.entity.UserTypeCode.END_USER;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminView;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminWrapper;
import edu.usu.sdl.openstorefront.core.view.ComponentFilterParams;
import static edu.usu.sdl.openstorefront.core.view.FilterQueryParams.FILTER_ALL;
import edu.usu.sdl.openstorefront.core.view.RequiredForComponent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author ccummings
 */
public class ComponentRESTTestClient
		extends BaseTestClient
{

	private static Set<String> componentIds = new HashSet<>();
	private static Set<String> integrationCompIds = new HashSet<>();
	private ComponentRESTClient apiComponentREST;
	private AttributeTestClient apiAttributeTestClient = new AttributeTestClient(client, apiClient);

	public ComponentRESTTestClient(ClientAPI client, APIClient apiClient)
	{
		super(client, apiClient);
		apiComponentREST = new ComponentRESTClient(client);
	}

	public ComponentQuestion addAPIComponentQuestion(String question, Component component)
	{
		ComponentQuestion compQuestion = new ComponentQuestion();
		compQuestion.setQuestion(question);
		compQuestion.setOrganization(component.getOrganization());
		compQuestion.setUserTypeCode(END_USER);
		
		compQuestion = apiComponentREST.addComponentQuestion(component.getComponentId(), compQuestion);
		return compQuestion;
	}

	public ComponentAdminView getComponentByName(String componentName)
	{
		ComponentFilterParams param = new ComponentFilterParams();
		param.setStatus(FILTER_ALL);
		param.setApprovalState(FILTER_ALL);
		param.setAll(Boolean.TRUE);
		param.setMax(Integer.MAX_VALUE);
		param.setComponentName(componentName);
		ComponentAdminWrapper compAdminWrapper = apiComponentREST.getComponentList(param);
		List<ComponentAdminView> views = compAdminWrapper.getComponents();
		if (views.isEmpty()) {
			return null;
		} else {
			return views.get(0);
		}
	}
	
	public ComponentTag addTagToComponent(String tagName, Component component)
	{
		ComponentTag tag = new ComponentTag();
		tag.setText(tagName);
		tag.setActiveStatus("A");
		tag.setCreateUser("admin");
		tag.setCreateDts(TimeUtil.currentDate());
		tag.setUpdateUser("admin");
		tag.setUpdateDts(TimeUtil.currentDate());
		return apiComponentREST.addComponentTag(component.getComponentId(), tag);
	}

	public Component createAPIComponent(String componentName)
	{
		return createAPIComponent(componentName, "Arcturus-Canopus", "This an API test component", componentName + " Organization");
	}

	// Component is created with a required attribute
	public Component createAPIComponent(String componentName, String componentType, String description, String organization)
	{
		apiAttributeTestClient.createAPIAttribute("BETELGEUSE","POLARIS","Polaris-star Test");
		
		Component component;
		ComponentTypeTestClient componentTypeClient = apiClient.getComponentTypeTestClient();
		ComponentType type = componentTypeClient.createAPIComponentType(componentType);
		Organization entryOrg = apiClient.getOrganizationTestClient().createOrganization(organization);

		ComponentAdminView adminView = getComponentByName(componentName);
		if (adminView != null) {
			component = adminView.getComponent();
		} else {
			component = new Component();
			component.setName(componentName);
			component.setDescription(description);
			component.setComponentType(type.getComponentType());
			component.setOrganization(entryOrg.getName());
			component.setApprovalState(ApprovalStatus.APPROVED);
			component.setLastActivityDts(TimeUtil.currentDate());

			List<AttributeType> attrTypes = apiClient.getAttributeTestClient().getReqAttributeTypes(component.getComponentType());
			List<ComponentAttribute> compAttributes = new ArrayList<>();
			for (AttributeType attrType : attrTypes) {
				List<AttributeCode> codes = apiClient.getAttributeTestClient().getListAttributeCodes(attrType.getAttributeType(), null);
				if (!codes.isEmpty()) {
					AttributeCodePk codePk = codes.get(0).getAttributeCodePk();
					String attributeCode = codePk.getAttributeCode();
					String attributeType = codePk.getAttributeType();

					ComponentAttributePk compAttrPk = new ComponentAttributePk();
					compAttrPk.setAttributeCode(attributeCode);
					compAttrPk.setAttributeType(attributeType);

					ComponentAttribute compAttr = new ComponentAttribute();
					compAttr.setComponentAttributePk(compAttrPk);

					compAttributes.add(compAttr);
				}
			}

			RequiredForComponent reqComponent = new RequiredForComponent();
			reqComponent.setComponent(component);
			reqComponent.setAttributes(compAttributes);
			

			RequiredForComponent reqComponentAPI = apiComponentREST.createComponent(reqComponent);
			component = reqComponentAPI.getComponent();

		}

		if (component != null) {
			componentIds.add(component.getComponentId());
		}

		return component;
	}
	
	public ComponentIntegration createAPIComponentIntegration(String componentName) throws InterruptedException
	{
		Component integrationComp = createAPIComponent(componentName, "IntegrationTestType", "IntegrationTest", "IntegrationOrg");
		
		ComponentIntegration integration = new ComponentIntegration();
		integration.setComponentId(integrationComp.getComponentId());
		integration.setStatus(RunStatus.COMPLETE);
		
//		LOG.log(Level.INFO, "Component: {0}", integration.getComponentId());
//		
//		Thread.sleep(1000);
		
		integration = apiComponentREST.saveIntegration(integrationComp.getComponentId(), integration);
		
		if (integration != null)
		{
			integrationCompIds.add(integration.getComponentId());
		}
		
		return integration;
	}

	public void deleteAPIComponent(String id)
	{
		apiComponentREST.deleteComponent(id);
	}
	
	public void deleteAPIComponentIntegration(String componentId)
	{
		apiComponentREST.deleteComponentConfig(componentId);
	}

	@Override
	public void cleanup()
	{
		for (String id : componentIds) {
			deleteAPIComponent(id);
		}
	}

}
