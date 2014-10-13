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
package edu.usu.sdl.openstorefront.web.test.component;

import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.transfermodel.ComponentAll;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttributePk;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentTest
		extends BaseTestCase
{

	public ComponentTest()
	{
		this.description = "Component Test";
	}

	@Override
	protected void runInternalTest()
	{
		results.append("Create component").append("<br>");
		ComponentAll testComponentAll = ComponentTest.createTestComponent();

		results.append("Delete component").append("<br>");
		deleteComponent(testComponentAll.getComponent().getComponentId());
	}

	public static ComponentAll createTestComponent()
	{
		ComponentAll componentAll = new ComponentAll();
		ServiceProxy serviceProxy = new ServiceProxy();

		Component component = new Component();
		component.setName("Test Component");
		component.setDescription("Test Description");
		component.setOrganization("Test");
		component.setApprovalState(Component.APPROVAL_STATE_PENDING);
		component.setGuid("5555555");
		component.setLastActivityDts(TimeUtil.currentDate());
		component.setActiveStatus(Component.ACTIVE_STATUS);
		componentAll.setComponent(component);

		List<AttributeType> attributeTypes = serviceProxy.getAttributeService().getRequiredAttributes();
		for (AttributeType type : attributeTypes) {
			ComponentAttribute componentAttribute = new ComponentAttribute();
			componentAttribute.setCreateUser(TEST_USER);
			componentAttribute.setUpdateUser(TEST_USER);
			componentAttribute.setActiveStatus(ComponentAttribute.ACTIVE_STATUS);
			ComponentAttributePk componentAttributePk = new ComponentAttributePk();
			componentAttributePk.setAttributeType(type.getAttributeType());
			List<AttributeCode> attributeCodes = serviceProxy.getAttributeService().findCodesForType(type.getAttributeType());
			componentAttributePk.setAttributeCode(attributeCodes.get(0).getAttributeCodePk().getAttributeCode());
			componentAttribute.setComponentAttributePk(componentAttributePk);
			componentAll.getAttributes().add(componentAttribute);
		}

		componentAll = serviceProxy.getComponentService().saveFullComponent(componentAll);
		return componentAll;
	}

	public static void deleteComponent(String componentId)
	{
		ServiceProxy serviceProxy = new ServiceProxy();
		serviceProxy.getComponentService().cascadeDeleteOfComponent(componentId);
	}
}
