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
package edu.usu.sdl.apiclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.view.ComponentResourceView;
import edu.usu.sdl.apiclient.view.LookupModel;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class ComponentService
		extends AbstractService
{

	private static final Logger LOG = Logger.getLogger(ComponentService.class.getName());

	public ComponentService(ClientAPI client)
	{
		super(client);
	}
	
	public ComponentService() {
		
		this(new ClientAPI(new ObjectMapper()));
	}

	public List<LookupModel> getComponentLookupList()
	{
		List<LookupModel> lookupModels;
		APIResponse response = client.httpGet("api/v1/resource/components/lookup/", null);
		lookupModels = response.getList(new TypeReference<List<LookupModel>>()
			{
			});
		return lookupModels;
	}

	public List<ComponentResourceView> getComponentResources(String componentId)
	{
		List<ComponentResourceView> resources;
		APIResponse response = client.httpGet("api/v1/resource/components/" + componentId + "/resources/view", null);
		resources = response.getList(new TypeReference<List<ComponentResourceView>>()
			{
			});
		return resources;
	}

	/**
	 * This is only available in 1.4+
	 *
	 * @return
	 */
	public List<ComponentResourceView> getComponentAllResources()
	{
		List<ComponentResourceView> resources;
		APIResponse response = client.httpGet("api/v1/resource/components/resources", null);
		resources = response.getList(new TypeReference<List<ComponentResourceView>>()
			{
			});
		return resources;
	}

}
