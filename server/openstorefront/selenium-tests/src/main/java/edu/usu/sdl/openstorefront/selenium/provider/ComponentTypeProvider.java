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

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.apiclient.rest.resource.ComponentTypeClient;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;

/**
 *
 * @author ccummings
 */
public class ComponentTypeProvider
{
	ClientAPI apiClient;
	ComponentTypeClient client;
	
	public ComponentTypeProvider()
	{
		apiClient = new ClientAPI(new ObjectMapper());
		client = new ComponentTypeClient(apiClient);
	}
	
	public ComponentType createComponentType(String type)
	{
		ComponentType compType = new ComponentType();
		compType.setComponentType(type);
		compType.setLabel(type + " - test label");
		compType.setDescription(type + " - test description");
		compType.setAllowOnSubmission(Boolean.TRUE);
		compType.setDataEntryAttributes(Boolean.TRUE);
		compType.setDataEntryContacts(Boolean.TRUE);
		compType.setDataEntryQuestions(Boolean.TRUE);
		compType.setDataEntryReviews(Boolean.TRUE);

		ComponentType apiCompType = client.createNewComponentType(compType);
				
		return apiCompType;
	}
	
	public void cleanup()
	{
		
	}
}
