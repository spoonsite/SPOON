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
package edu.usu.sdl.openstorefront.web.rest.model;

import edu.usu.sdl.openstorefront.storage.model.ComponentResource;
import edu.usu.sdl.openstorefront.storage.model.ResourceType;
import edu.usu.sdl.openstorefront.util.TranslateUtil;

/**
 *
 * @author dshurtleff
 */
public class ComponentResourceView
{
	private ComponentResource componentResource;
	private ResourceType resourceType;

	public ComponentResourceView()
	{
	}

	public static ComponentResourceView toView(ComponentResource componentResource)
	{
		ComponentResourceView componentResourceView = new ComponentResourceView();
		componentResourceView.resourceType = new ResourceType();
		componentResourceView.resourceType.setDescription(TranslateUtil.translate(ResourceType.class, componentResource.getResourceType()));

		componentResourceView.componentResource = componentResource;
		String link = componentResource.getLink();
		if (componentResource.getResourceFileId() != null) {
			link = ComponentResource.LOCAL_RESOURCE_URL + componentResource.getResourceId();
		}
		componentResourceView.componentResource.setLink(link);
		
		return componentResourceView;
	}
}
