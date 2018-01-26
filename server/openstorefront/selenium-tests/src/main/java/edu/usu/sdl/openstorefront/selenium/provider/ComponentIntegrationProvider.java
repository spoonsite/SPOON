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
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentIntegration;
import edu.usu.sdl.openstorefront.core.entity.RunStatus;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class ComponentIntegrationProvider
{

	ComponentRESTClient client;
	List<String> integrationCompIds;
	
	public ComponentIntegrationProvider(ClientAPI apiClient)
	{
		client = new ComponentRESTClient(apiClient);
		integrationCompIds = new ArrayList<>();
	}
	
	public ComponentIntegration createComponentIntegration(Component component) throws InterruptedException
	{	
		ComponentIntegration integration = new ComponentIntegration();
		integration.setComponentId(component.getComponentId());
		integration.setStatus(RunStatus.COMPLETE);

		integration = client.saveIntegration(component.getComponentId(), integration);
		
		if (integration != null)
		{
			integrationCompIds.add(integration.getComponentId());
		}
		
		return integration;
	}
	
	public void cleanup()
	{
		for (String id : integrationCompIds) {
			
			client.deleteComponentConfig(id);
		}
	}
}
