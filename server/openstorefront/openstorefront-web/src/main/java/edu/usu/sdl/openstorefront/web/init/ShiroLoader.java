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
package edu.usu.sdl.openstorefront.web.init;

import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import java.io.File;
import javax.servlet.ServletContextEvent;
import org.apache.shiro.web.env.EnvironmentLoaderListener;

/**
 * Allows for the config to init before it's loaded.
 *
 * @author dshurtleff
 */
public class ShiroLoader
		extends EnvironmentLoaderListener
{

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		File configFile = FileSystemManager.getInstance().getConfig("shiro.ini");
		sce.getServletContext().setInitParameter(CONFIG_LOCATIONS_PARAM, configFile.toURI().toString());
		super.contextInitialized(sce);
	}
}
