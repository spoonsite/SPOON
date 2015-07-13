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
import edu.usu.sdl.apiclient.view.ComponentResourceView;
import edu.usu.sdl.apiclient.view.LookupModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class ComponentService
		extends AbstractService
{

	private static Logger log = Logger.getLogger(ComponentService.class.getName());

	public ComponentService(LoginModel loginModel)
	{
		super(loginModel);
	}

	public List<LookupModel> getComponentLookupList()
	{
		List<LookupModel> lookupModels = new ArrayList<>();
		APIResponse response = callAPI("api/v1/resource/components/lookup/", null);
		try {
			lookupModels = getObjectMapper().readValue(response.getResponseBody(), new TypeReference<List<LookupModel>>()
			{
			});
		} catch (IOException ex) {
			throw new HandlingException(ex);
		}
		return lookupModels;
	}

	public List<ComponentResourceView> getComponentResources(String componentId)
	{
		List<ComponentResourceView> resources = new ArrayList<>();
		APIResponse response = callAPI("api/v1/resource/components/" + componentId + "/resources/view", null);
		try {
			resources = getObjectMapper().readValue(response.getResponseBody(), new TypeReference<List<ComponentResourceView>>()
			{
			});
		} catch (IOException ex) {
			throw new HandlingException(ex);
		}
		return resources;
	}

	/**
	 * This is only available in 1.4+
	 *
	 * @return
	 */
	public List<ComponentResourceView> getComponentAllResources()
	{
		List<ComponentResourceView> resources = new ArrayList<>();
		APIResponse response = callAPI("api/v1/resource/components/resources", null);
		try {
			resources = getObjectMapper().readValue(response.getResponseBody(), new TypeReference<List<ComponentResourceView>>()
			{
			});
		} catch (IOException ex) {
			throw new HandlingException(ex);
		}
		return resources;
	}

}
