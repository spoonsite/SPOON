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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.api.PluginService;
import edu.usu.sdl.openstorefront.core.entity.Plugin;
import edu.usu.sdl.openstorefront.core.view.PluginView;
import edu.usu.sdl.openstorefront.service.api.PluginServicePrivate;
import edu.usu.sdl.openstorefront.service.manager.PluginManager;
import edu.usu.sdl.openstorefront.service.manager.model.PluginModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles Plugins
 *
 * @author dshurtleff
 */
public class PluginServiceImpl
		extends ServiceProxy
		implements PluginService, PluginServicePrivate
{

	private static final Logger log = Logger.getLogger(PluginServiceImpl.class.getName());

	@Override
	public Plugin installPlugin(String filename, InputStream pluginInput)
	{
		return installPlugin(filename, pluginInput, false);
	}

	@Override
	public Plugin installPlugin(String filename, InputStream pluginInput, boolean filenameSafe)
	{
		//Check for Id first as file name maybe pickup from the directory
		Plugin plugin = getPersistenceService().findById(Plugin.class, filename);
		if (plugin == null) {
			//Check for an original name match
			Plugin existing = new Plugin();
			existing.setOriginalFilename(filename);
			plugin = existing.find();
		}

		if (plugin != null) {
			//This always starts it up
			plugin.setActiveStatus(Plugin.ACTIVE_STATUS);

			//This install maybe a direct update with a different name
			plugin.setOriginalFilename(filename);
			plugin.populateBaseUpdateFields();
		} else {
			plugin = new Plugin();
			plugin.setPluginId(getPersistenceService().generateId());
			if (filenameSafe) {
				plugin.setActualFilename(filename);
			} else {
				plugin.setActualFilename(plugin.getPluginId());
			}
			plugin.setOriginalFilename(filename);
			plugin.setLocation(getLocationFromFilename(plugin));
			plugin.setPluginType(StringProcessor.getFileExtension(filename));
			plugin.populateBaseCreateFields();
		}

		//save file into plugin dir
		if (filenameSafe == false) {
			try (InputStream in = pluginInput; OutputStream out = new FileOutputStream(plugin.fullPath())) {
				//save plugin
				FileSystemManager.getInstance().copy(in, out);
			} catch (Exception ex) {
				throw new OpenStorefrontRuntimeException("Unable to save plugin.", "See log for more details.", ex);
			}
		}
		try {
			PluginManager.installBundle(plugin.getLocation(), new FileInputStream(plugin.fullPath()));
			getPersistenceService().persist(plugin);
		} catch (Exception ex) {
			throw new OpenStorefrontRuntimeException("Failed to install plugin.", "Make sure file was actually contained a plugin. (jar, war). See log for more details.", ex);
		}

		return plugin;
	}

	private String getLocationFromFilename(Plugin plugin)
	{
		File file = new File(plugin.fullPath());
		return file.toURI().toString();
	}

	@Override
	public void uninstallPlugin(String pluginId)
	{
		//find plug in record
		Plugin plugin = getPersistenceService().findById(Plugin.class, pluginId);
		if (plugin != null) {
			//uninstall plugin
			PluginManager.uninstallBundle(plugin.getLocation());

			//move plugin to unstallled area
			File file = new File(plugin.fullPath());
			if (file.exists()) {
				Path source = file.toPath();
				Path newdir = FileSystemManager.getInstance().getDir(FileSystemManager.PLUGIN_UNINSTALLED_DIR).toPath();

				try {
					Files.move(source, newdir.resolve(source.getFileName()), REPLACE_EXISTING);
				} catch (IOException ex) {
					throw new OpenStorefrontRuntimeException("Failed to move Plugin to uninstalled directory", "Check permissions; Disk Space", ex);
				}
			}

			//delete  plugin record
			getPersistenceService().delete(plugin);
		} else {
			log.log(Level.WARNING, MessageFormat.format("Unable to find plugin to uninstall: {0}", pluginId));
		}
	}

	@Override
	public void failPlugin(String filename)
	{
		//move plugin to failed area
		File file = new File(filename);
		if (file.exists()) {
			Path source = file.toPath();
			Path newdir = FileSystemManager.getInstance().getDir(FileSystemManager.PLUGIN_FAILED_DIR).toPath();

			try {
				Files.move(source, newdir.resolve(source.getFileName()), REPLACE_EXISTING);
			} catch (IOException ex) {
				throw new OpenStorefrontRuntimeException("Failed to move Plugin to failed directory", "Check permissions; Disk Space", ex);
			}
		}
	}

	@Override
	public void inactivatePlugin(String pluginId)
	{
		//find plug in record
		Plugin plugin = getPersistenceService().findById(Plugin.class, pluginId);

		if (plugin != null) {
			//stop plug
			PluginManager.stopBundle(plugin.getLocation());

			//inactivate
			plugin.setActiveStatus(Plugin.INACTIVE_STATUS);
			getPersistenceService().persist(plugin);
		} else {
			log.log(Level.WARNING, MessageFormat.format("Unable to find plugin to inactivate: {0}", pluginId));
		}
	}

	@Override
	public void activatePlugin(String pluginId)
	{
		//find plug in record
		Plugin plugin = getPersistenceService().findById(Plugin.class, pluginId);

		if (plugin != null) {
			//start plug
			PluginManager.startBundle(plugin.getLocation());

			//activate
			plugin.setActiveStatus(Plugin.ACTIVE_STATUS);
			getPersistenceService().persist(plugin);
		} else {
			log.log(Level.WARNING, MessageFormat.format("Unable to find plugin to activate: {0}", pluginId));
		}
	}

	@Override
	public List<PluginView> findAllPlugins()
	{
		List<PluginView> pluginViews = new ArrayList<>();

		//Find all records
		Plugin pluginExample = new Plugin();
		List<Plugin> plugins = pluginExample.findByExampleProxy();
		Map<String, Plugin> pluginMetaDataMap = new HashMap<>();
		plugins.forEach(plugin -> {
			pluginMetaDataMap.put(plugin.getLocation(), plugin);
		});

		List<PluginModel> pluginModels = PluginManager.getAllPlugins();
		for (PluginModel pluginModel : pluginModels) {

			Plugin plugin = pluginMetaDataMap.get(pluginModel.getLocation());
			PluginView view;
			if (plugin != null) {
				view = PluginView.toView(plugin);
				view.setCoreModule(false);
			} else {
				view = new PluginView();
				view.setCoreModule(true);
				view.setLocation(pluginModel.getLocation());
			}
			view.setPluginRuntimeState(pluginModel.getCurrentState());
			view.setLastModifed(pluginModel.getLastModified());
			view.setName(pluginModel.getName());
			view.setDescription(pluginModel.getDescription());
			view.setVersion(pluginModel.getVersion());
			view.setRuntimeId(pluginModel.getBundleId());

			pluginViews.add(view);
		}
		return pluginViews;
	}

}
