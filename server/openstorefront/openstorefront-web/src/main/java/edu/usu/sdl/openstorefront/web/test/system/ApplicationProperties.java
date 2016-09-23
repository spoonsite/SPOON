/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.test.system;

import edu.usu.sdl.openstorefront.core.entity.ApplicationProperty;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;

/**
 *
 * @author ccummings
 */
public class ApplicationProperties extends BaseTestCase
{

	private String propertyKey = "Test Property Key";

	@Override
	protected void runInternalTest()
	{
		String propertyValue = "Application Properties Test Value";

		service.getSystemService().saveProperty(propertyKey, propertyValue);
		ApplicationProperty appProperty = service.getSystemService().getProperty(propertyKey);

		if (propertyKey.equals(appProperty.getKey()) && propertyValue.equals(appProperty.getValue())) {
			results.append("Application property saved successfully<br><br>");
		} else {
			failureReason.append("Error saving application property<br><br>");
		}

		if (propertyValue.equals(service.getSystemService().getPropertyValue(propertyKey))) {
			results.append("Application property value test:  Passed<br><br>");
		} else {
			failureReason.append("Application propert value test:  Failed - unable to find app property with given value<br><br>");
		}

	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();
		service.getSystemService().saveProperty(propertyKey, null);
	}

	@Override
	public String getDescription()
	{
		return "AppProperties Test";
	}

}
