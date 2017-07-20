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
package edu.usu.sdl.openstorefront.selenium.util;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author ccummings
 */
public class PropertiesUtil
{

	protected static Properties properties;

	private PropertiesUtil()
	{

	}

	public static Properties getProperties()
	{
		if (properties == null) {
			properties = new Properties();
			File propertyFile = FileSystemManager.getConfig("testconfig.properties");
			try (InputStream in = new FileInputStream(propertyFile)) {
				properties.load(in);
			} catch (IOException ex) {
				throw new OpenStorefrontRuntimeException("Unable to load Configuration file.");
			}
		}
		return properties;
	}
}
