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
import edu.usu.sdl.apiclient.rest.service.ApplicationClient;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class ApplicationProvider
{

	private ApplicationClient client;
	private List<LookupModel> previousSystemConfigs;

	public ApplicationProvider(ClientAPI apiClient)
	{
		client = new ApplicationClient(apiClient);
		previousSystemConfigs = new ArrayList<>();
	}

	public LookupModel getSystemConfigProperty(String key)
	{
		return client.getConfigPropertiesForKey(key);
	}

	public void updateSystemConfigProperty(String key, String newValue)
	{
		LookupModel config = client.getConfigPropertiesForKey(key);
		previousSystemConfigs.add(config);
		config.setDescription(newValue);

		client.addConfigProperty(config);
	}

	public void cleanup()
	{
		for (LookupModel model : previousSystemConfigs) {
			
			client.addConfigProperty(model);
		}
	}
}
