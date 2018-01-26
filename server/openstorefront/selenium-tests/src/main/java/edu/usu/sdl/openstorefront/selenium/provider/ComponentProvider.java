/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.selenium.provider;

import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.apiclient.rest.resource.ComponentRESTClient;
import edu.usu.sdl.openstorefront.common.exception.AttachedReferencesException;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.Organization;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminView;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminWrapper;
import edu.usu.sdl.openstorefront.core.view.ComponentFilterParams;
import static edu.usu.sdl.openstorefront.core.view.FilterQueryParams.FILTER_ALL;
import edu.usu.sdl.openstorefront.core.view.RequiredForComponent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class ComponentProvider
{
	ComponentRESTClient client;
	AttributeProvider attributeProvider;
	OrganizationProvider organizationProvider;
	ComponentTypeProvider compTypeProvider;
	List<String> componentIds;

	public ComponentProvider(AttributeProvider ap, OrganizationProvider op, ComponentTypeProvider ctp, ClientAPI apiClient)
	{
		client = new ComponentRESTClient(apiClient);
		attributeProvider = ap;
		organizationProvider = op;
		compTypeProvider = ctp;
		componentIds = new ArrayList<>();
	}

	public ComponentRESTClient getComponentRESTClient()
	{
		return client;
	}

	public Component createComponent(String componentName, String description, String orgName)
	{
		AttributeType attrType = attributeProvider.createAttribute("SeleniumAttribute", "POLARIS", "POLARIS-STAR");
		ComponentType compType = compTypeProvider.createComponentType("SeleniumEntryType");
		Organization organization = organizationProvider.createOrganization(orgName);
		Component component;
		ComponentAdminView adminView = getComponentByName(componentName);

		if (adminView != null) {
			component = adminView.getComponent();
		} else {
			component = new Component();
			component.setName(componentName);
			component.setDescription(description);
			component.setComponentType(compType.getComponentType());
			component.setOrganization(organization.getName());
			component.setApprovalState(ApprovalStatus.APPROVED);
			component.setLastActivityDts(TimeUtil.currentDate());

			List<ComponentAttribute> compAttributes = new ArrayList<>();

			List<AttributeCode> codes = attributeProvider.getListAttributeCodes(attrType.getAttributeType(), null);
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

			RequiredForComponent reqComponent = new RequiredForComponent();
			reqComponent.setComponent(component);
			reqComponent.setAttributes(compAttributes);

			reqComponent = client.createComponent(reqComponent);
			component = reqComponent.getComponent();
		}

		if (component != null) {
			componentIds.add(component.getComponentId());
		}

		return component;
	}

	public ComponentAdminView getComponentByName(String componentName)
	{
		ComponentFilterParams param = new ComponentFilterParams();
		param.setStatus(FILTER_ALL);
		param.setApprovalState(FILTER_ALL);
		param.setAll(Boolean.TRUE);
		param.setMax(Integer.MAX_VALUE);
		param.setComponentName(componentName);
		ComponentAdminWrapper compAdminWrapper = client.getComponentList(param);
		List<ComponentAdminView> views = compAdminWrapper.getComponents();
		if (views.isEmpty()) {
			return null;
		} else {
			return views.get(0);
		}
	}

	public void cleanup() throws AttachedReferencesException
	{
		for (String id : componentIds) {

			client.deleteComponent(id);
		}

		compTypeProvider.cleanup();
		organizationProvider.cleanup();
		attributeProvider.cleanup();
	}

}
