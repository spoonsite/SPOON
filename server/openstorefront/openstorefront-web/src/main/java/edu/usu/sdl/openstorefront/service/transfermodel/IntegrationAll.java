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
package edu.usu.sdl.openstorefront.service.transfermodel;

import edu.usu.sdl.openstorefront.storage.model.ComponentIntegration;
import edu.usu.sdl.openstorefront.storage.model.ComponentIntegrationConfig;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class IntegrationAll
{

	private ComponentIntegration integration;
	private List<ComponentIntegrationConfig> configs = new ArrayList<>();

	public IntegrationAll()
	{
	}

	public ComponentIntegration getIntegration()
	{
		return integration;
	}

	public void setIntegration(ComponentIntegration integration)
	{
		this.integration = integration;
	}

	public List<ComponentIntegrationConfig> getConfigs()
	{
		return configs;
	}

	public void setConfigs(List<ComponentIntegrationConfig> configs)
	{
		this.configs = configs;
	}

}
