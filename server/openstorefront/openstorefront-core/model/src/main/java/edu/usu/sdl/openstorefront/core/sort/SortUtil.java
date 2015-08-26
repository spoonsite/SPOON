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
package edu.usu.sdl.openstorefront.core.sort;

import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.ResourceType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * General Complex Sorts
 *
 * @author dshurtleff
 */
public class SortUtil
{

	public static List<ComponentResource> sortComponentResource(List<ComponentResource> componentResources)
	{
		Objects.requireNonNull(componentResources, "Component Resource List must not be null");

		List<ComponentResource> componentResourcesSorted = new ArrayList<>();
		if (componentResources.isEmpty() == false) {
			Service serviceProxy = ServiceProxyFactory.getServiceProxy();
			List<ResourceType> resourceTypes = serviceProxy.getLookupService().findLookup(ResourceType.class);
			resourceTypes.sort(new LookupComparator<>());

			Map<String, List<ComponentResource>> resourceMap = new HashMap<>();
			for (ComponentResource componentResource : componentResources) {
				if (resourceMap.containsKey(componentResource.getResourceType())) {
					resourceMap.get(componentResource.getResourceType()).add(componentResource);
				} else {
					List<ComponentResource> resources = new ArrayList<>();
					resources.add(componentResource);
					resourceMap.put(componentResource.getResourceType(), resources);
				}
			}

			for (ResourceType resourceType : resourceTypes) {
				List<ComponentResource> temp = resourceMap.get(resourceType.getCode());
				if (temp != null) {
					componentResourcesSorted.addAll(temp);
				}
			}
		}
		return componentResourcesSorted;
	}

}
