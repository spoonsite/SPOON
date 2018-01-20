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
package edu.usu.sdl.openstorefront.selenium.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.ClientAPI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author ccummings
 */
public class CleanupServiceImpl implements CleanupService
{
	private Stack<Class> providers;
	private Map<Class, List<String>> providerValues;
	private ClientAPI client;
	
	public CleanupServiceImpl()
	{
		providers = new Stack<>();
		providerValues = new HashMap<>();
		client = new ClientAPI(new ObjectMapper());
	}
	
	public Stack<Class> getProviders()
	{
		return providers;
	}
	
	public void Register(Class classType, String objId)
	{
		if (!providers.contains(classType))
		{
			providers.push(classType);
		}
		
		if (!providerValues.containsKey(classType))
		{
			providerValues.put(classType, new ArrayList<>());
		}
		
		providerValues.get(classType).add(objId);
	}
	
	@Override
	public void cleanup()
	{
		while (!providers.empty())
		{
			Class type = providers.pop();

			switch(type.getSimpleName())
			{
				case "ComponentProvider":
					System.out.println("Cleanup method for comp provider");
					break;
				case "AttributeProvider":
					System.out.println("Cleanup method for attr provider");
				default:
					break;
			}
		}
	}
	
}
