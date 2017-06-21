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
import edu.usu.sdl.apiclient.rest.resource.ComponentTypeClient;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class ComponentTypeTestClient
		extends BaseTestClient
{

	private ComponentTypeClient apiComponentType;
	private static List<String> componentTypeIds = new ArrayList<>();

	public ComponentTypeTestClient(ClientAPI client, APIClient apiClient)
	{
		super(client, apiClient);
		apiComponentType = new ComponentTypeClient(client);
	}
	
	public ComponentType createAPIComponentType()
	{
		ComponentType compType = new ComponentType();
		compType.setComponentType("WAAABOOOM");
		compType.setLabel("This Waaaboom Label");
		compType.setDescription("Waaabooom Description");
		
		ComponentType apiCompType = apiComponentType.createNewComponentType(compType);
		componentTypeIds.add(compType.getComponentType());
		return apiCompType;
	}

	public void deleteAPIEntryType(String type)
	{
		apiComponentType.deleteComponentType(type, "COMP");
	}
	
	@Override
	public void cleanup()
	{
		for (String id : componentTypeIds) {
			deleteAPIEntryType(id);
		}
	}

}
