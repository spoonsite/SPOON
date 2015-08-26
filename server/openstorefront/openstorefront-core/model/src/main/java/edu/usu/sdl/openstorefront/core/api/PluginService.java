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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.entity.Plugin;
import edu.usu.sdl.openstorefront.core.view.PluginView;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public interface PluginService
		extends AsyncService
{

	/**
	 * Add or Update an existing plugin
	 *
	 * @param filename or pluginId (if it's the plugin id it's a direct update)
	 * @param pluginInput
	 * @return
	 */
	public Plugin installPlugin(String filename, InputStream pluginInput);

	/**
	 * Uninstall a plug
	 *
	 * @param pluginId
	 */
	public void uninstallPlugin(String pluginId);

	/**
	 * Inactivates a plugin and stopped it. Does nothing if already inactive
	 *
	 * @param pluginId
	 */
	public void inactivatePlugin(String pluginId);

	/**
	 * Activates a plug and starts it. Does nothing if already active
	 *
	 * @param pluginId
	 */
	public void activatePlugin(String pluginId);

	/**
	 * This will get all plugins known to the system.
	 *
	 * @return
	 */
	public List<PluginView> findAllPlugins();

}
