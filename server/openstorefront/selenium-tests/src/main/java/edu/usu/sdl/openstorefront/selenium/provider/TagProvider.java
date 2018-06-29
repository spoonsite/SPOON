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
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;

/**
 *
 * @author ccummings
 */
public class TagProvider
{
	ComponentRESTClient client;
	
	public TagProvider(ClientAPI apiClient)
	{
		client = new ComponentRESTClient(apiClient);
	}
	
	public void addTagToComponent(Component component, String tagName)
	{
		ComponentTag tag = new ComponentTag();
		tag.setText(tagName);
		tag.setActiveStatus("A");
		tag.setCreateUser("admin");
		tag.setCreateDts(TimeUtil.currentDate());
		tag.setUpdateUser("admin");
		tag.setUpdateDts(TimeUtil.currentDate());
		client.addComponentTag(component.getComponentId(), tag);
	}
}
