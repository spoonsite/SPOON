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
package edu.usu.sdl.openstorefront.usecase;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.ResourceType;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminView;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminWrapper;
import edu.usu.sdl.openstorefront.core.view.ComponentResourceView;
import edu.usu.sdl.openstorefront.core.view.ComponentView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 * This was for stats gather
 *
 * @author dshurtleff
 */
public class ResourceLinkUseCase
{

	@Test
	public void parseData() throws FileNotFoundException, IOException
	{
		ObjectMapper mapper = StringProcessor.defaultObjectMapper();

		ComponentAdminWrapper componentAdminWrapper = mapper.readValue(new FileInputStream("/test/storefront/rawComponents.txt"), ComponentAdminWrapper.class);

		Map<String, ComponentView> viewMapper = new HashMap<>();
		for (ComponentAdminView componentAdminView : componentAdminWrapper.getComponents()) {
			viewMapper.put(componentAdminView.getComponent().getComponentId(), componentAdminView.getComponent());
		}

		System.out.println("Entry Name	Resource Link");

		List<ComponentResourceView> resources = mapper.readValue(new FileInputStream("/test/storefront/rawResources.txt"), new TypeReference<List<ComponentResourceView>>()
		{
		});
		for (ComponentResourceView resourceView : resources) {
			if (ResourceType.CODE.equals(resourceView.getResourceType())) {
				if (resourceView.getLink().toLowerCase().contains("di2e.net")) {
					StringBuilder sb = new StringBuilder();
					ComponentView componentView = viewMapper.get(resourceView.getComponentId());
					if (componentView != null) {
						sb.append(componentView.getName()).append("	");
						sb.append(resourceView.getLink()).append("	");

						System.out.println(sb.toString());
					}

				}
			}
		}

	}

}
