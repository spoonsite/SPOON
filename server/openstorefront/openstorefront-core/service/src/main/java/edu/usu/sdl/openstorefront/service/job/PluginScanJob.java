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
package edu.usu.sdl.openstorefront.service.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.ApplicationProperty;
import edu.usu.sdl.openstorefront.core.entity.Plugin;
import edu.usu.sdl.openstorefront.service.manager.JobManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * Watches the plugin dir
 *
 * @author dshurtleff
 */
@DisallowConcurrentExecution
public class PluginScanJob
		extends BaseJob
{

	private static final Logger LOG = Logger.getLogger(PluginScanJob.class.getName());

	@Override
	protected void executeInternaljob(JobExecutionContext context)
	{
		File pluginDir = FileSystemManager.getInstance().getDir(FileSystemManager.PLUGIN_DIR);

		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();

		String fileMapJson = service.getSystemService().getPropertyValue(ApplicationProperty.PLUGIN_LAST_LOAD_MAP);
		Map<String, Long> fileMap = null;
		if (StringUtils.isNotBlank(fileMapJson)) {
			try {
				fileMap = objectMapper.readValue(fileMapJson, new TypeReference<Map<String, Long>>()
				{
				});
			} catch (IOException e) {
				LOG.log(Level.WARNING, "Unable to restore plugin file map.  Starting over; should be able to continue.");
			}
		} else {
			fileMapJson = "";
		}

		if (fileMap == null) {
			fileMap = new ConcurrentHashMap<>();
		}

		Set<String> newFiles = new HashSet<>();
		for (File plugin : pluginDir.listFiles()) {
			if (plugin.isFile()) {
				newFiles.add(plugin.getPath());

				Long lastModTime = fileMap.get(plugin.getPath());
				boolean loadPlugin = false;
				if (lastModTime != null) {
					//check for update
					if (plugin.lastModified() > lastModTime) {
						LOG.log(Level.INFO, MessageFormat.format("Plugin: {0} was updated...", plugin.getName()));
						loadPlugin = true;
					}
				} else {
					//new Plugin
					loadPlugin = true;
				}

				if (loadPlugin) {
					LOG.log(Level.INFO, MessageFormat.format("Found plugin: {0} attempting to load.", plugin.getName()));
					try (InputStream in = new FileInputStream(plugin)) {
						service.getPluginServicePrivate().installPlugin(plugin.getName(), in, true);
						LOG.log(Level.INFO, MessageFormat.format("Loaded plugin: {0} successfully.", plugin.getName()));
					} catch (Exception ioe) {
						LOG.log(Level.SEVERE, MessageFormat.format("Failed Plugin: {0} failed to load.", plugin.getName()), ioe);
						try {
							service.getPluginService().failPlugin(plugin.getPath());
							LOG.log(Level.INFO, MessageFormat.format("Failed Plugin: {0} removed. ", plugin.getName()));
						} catch (Exception ex) {
							LOG.log(Level.SEVERE, MessageFormat.format("Failed Plugin: {0} could not be removed.  See log.", plugin.getName()), ex);
						}
					} finally {
						fileMap.put(plugin.getPath(), plugin.lastModified());
					}
				}
			}
		}

		//look for removed files
		List<String> keysToRemove = new ArrayList<>();
		for (String path : fileMap.keySet()) {
			if (newFiles.contains(path) == false) {
				//file was remove
				LOG.log(Level.INFO, MessageFormat.format("Plugin: {0} was removed...", path));

				String filename = Paths.get(path).getFileName().toString();
				Plugin pluginExample = new Plugin();
				pluginExample.setActualFilename(filename);
				pluginExample = pluginExample.find();
				if (pluginExample != null) {
					LOG.log(Level.INFO, MessageFormat.format("Uninstalling plugin: {0} ", path));
					try {
						service.getPluginService().uninstallPlugin(pluginExample.getPluginId());
						LOG.log(Level.INFO, MessageFormat.format("Plugin: {0} uninstalled. ", path));
					} catch (Exception e) {
						LOG.log(Level.INFO, MessageFormat.format("Failed to uninstalled:  {0} See log.  Try to use admin tools to uninstall.", path), e);
					}
				} else {
					LOG.log(Level.INFO, MessageFormat.format("Plugin: {0} was not installed", path));
				}
				keysToRemove.add(path);
			}
		}

		for (String key : keysToRemove) {
			fileMap.remove(key);
		}

		try {
			String updatedMap = objectMapper.writeValueAsString(fileMap);
			if (fileMapJson.equals(updatedMap) == false) {
				service.getSystemService().saveProperty(ApplicationProperty.PLUGIN_LAST_LOAD_MAP, updatedMap);
			}
		} catch (JsonProcessingException ex) {
			LOG.log(Level.SEVERE, "Unable to save FileMap.  This can cause spinning.  Pausing job.", ex);
			JobManager.pauseJob(PluginScanJob.class.getSimpleName(), JobManager.JOB_GROUP_SYSTEM);
		}

	}

}
