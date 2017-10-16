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
import edu.usu.sdl.apiclient.rest.service.ApplicationClient;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class ApplicationTestClient
		extends BaseTestClient
{
	private ApplicationClient apiApplication;
	private List<LookupModel> previousAppConfigs = new ArrayList<>();

	public ApplicationTestClient(ClientAPI client, APIClient apiClient)
	{
		super(client, apiClient);
		apiApplication = new ApplicationClient(client);
	}
	
	public LookupModel getCurrentConfigProp(String key)
	{
		
		LookupModel config = apiApplication.getConfigPropertiesForKey(key);
		previousAppConfigs.add(config);
		return config;
	}
	
	public void setConfigProperties(LookupModel model)
	{
		apiApplication.addConfigProperty(model);
	}

	@Override
	public void cleanup()
	{
		for (LookupModel config : previousAppConfigs) {
			
			setConfigProperties(config);
		}
	}

}
