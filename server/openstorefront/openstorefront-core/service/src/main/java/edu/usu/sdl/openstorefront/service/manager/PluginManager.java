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
package edu.usu.sdl.openstorefront.service.manager;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.entity.ApplicationProperty;
import edu.usu.sdl.openstorefront.core.entity.Plugin;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.job.PluginScanJob;
import edu.usu.sdl.openstorefront.service.manager.model.AddJobModel;
import edu.usu.sdl.openstorefront.service.manager.model.PluginModel;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * Handles loading extensions
 *
 * @author dshurtleff
 */
public class PluginManager
		implements Initializable
{

	private static final Logger log = Logger.getLogger(PluginManager.class.getName());

	private static AtomicBoolean loadingPlugins = new AtomicBoolean(false);	
	private static Map<String, Bundle> externalBundles = new ConcurrentHashMap<>();
	private static AtomicBoolean started = new AtomicBoolean(false);

	public static void init()
	{
		ServiceProxy service = ServiceProxy.getProxy();
		
		loadingPlugins.set(true);
		
		//start any stopped bundles
		Bundle bundles[] = OsgiManager.getFelix().getBundleContext().getBundles();
		for (Bundle bundle : bundles) {
			if (Bundle.INSTALLED == bundle.getState()
					|| Bundle.RESOLVED == bundle.getState()) {
				log.log(Level.INFO, MessageFormat.format("Starting bundle: {0}", bundle.getLocation()));

				Plugin plugin = new Plugin();
				plugin.setLocation(bundle.getLocation());
				plugin = plugin.find();
				service.getPluginService().activatePlugin(plugin.getPluginId());
			}
		}

		//Reload any external bundles
		service.getSystemService().saveProperty(ApplicationProperty.PLUGIN_LAST_LOAD_MAP, null);
		
		AddJobModel job = new AddJobModel();
		job.setJobClass(PluginScanJob.class);
		job.setJobName(PluginScanJob.class.getSimpleName());
		job.setDescription("Watch for plugin directory changes.");
		job.setSeconds(10);
		job.setRepeatForever(true);
		JobManager.addJob(job);
		
		loadingPlugins.set(false);
	}

	/**
	 * Loads bundle
	 *
	 * @param filename (the path will be automatically set
	 * @param bundleStream (Can be null, if so it will try to use the location)
	 * @return
	 */
	public static boolean installBundle(String location, InputStream bundleStream)
	{
		boolean success = false;

		try (InputStream in = bundleStream) {
			Bundle bundle = OsgiManager.getFelix().getBundleContext().getBundle(location);
			if (bundle == null
					|| Bundle.UNINSTALLED == bundle.getState()) {
				bundle = OsgiManager.getFelix().getBundleContext().installBundle(location, in);
				if (bundle != null) {
					if (Bundle.RESOLVED == bundle.getState() || Bundle.INSTALLED == bundle.getState()) {
						bundle.start();
						externalBundles.put(bundle.getLocation(), bundle);
						success = true;
					} else {
						throw new OpenStorefrontRuntimeException("Bundle did not resolve.");
					}
				}
			} else {
				if (Bundle.ACTIVE == bundle.getState()) {
					bundle.stop();
				}

				bundle.update(bundleStream);
				externalBundles.put(bundle.getLocation(), bundle);
				success = true;

				if (Bundle.RESOLVED == bundle.getState()
						|| Bundle.INSTALLED == bundle.getState()) {
					bundle.start();
				}
			}
		} catch (IOException | BundleException ex) {
			log.log(Level.SEVERE, MessageFormat.format("Unable to load plugin: {0}", location));
                        throw new OpenStorefrontRuntimeException(MessageFormat.format("Unable to install or load plugin: {0}", location), ex);
		}

		return success;
	}

	public static boolean uninstallBundle(String location)
	{
		boolean success = false;

		Bundle bundle = OsgiManager.getFelix().getBundleContext().getBundle(location);
		if (bundle != null
				&& (Bundle.INSTALLED == bundle.getState()
				|| Bundle.RESOLVED == bundle.getState()
				|| Bundle.ACTIVE == bundle.getState())) {
			try {
				if (Bundle.ACTIVE == bundle.getState()) {
					bundle.stop();
				}
				bundle.uninstall();
				externalBundles.remove(bundle.getLocation());

			} catch (BundleException ex) {
				log.log(Level.SEVERE, null, ex);
			}
		} else {
			if (bundle == null) {
				log.log(Level.WARNING, MessageFormat.format("Unable to find bundle. Bundle: {0}", location));
			} else {
				log.log(Level.WARNING, MessageFormat.format("Unable to uninstall; bundle is not in a stable state. Try again.  bundle: {0}", location));
			}
		}

		return success;
	}

	public static boolean startBundle(String location)
	{
		boolean success = false;

		Bundle bundle = OsgiManager.getFelix().getBundleContext().getBundle(location);
		if (bundle != null
				&& (Bundle.INSTALLED == bundle.getState()
				|| Bundle.RESOLVED == bundle.getState())) {
			try {
				if (Bundle.ACTIVE != bundle.getState()) {
					bundle.start();
				} else {
					log.log(Level.WARNING, MessageFormat.format("Bundle is already Active. Bundle: {0}", location));
				}
			} catch (BundleException ex) {
				log.log(Level.SEVERE, null, ex);
			}
		} else {
			if (bundle == null) {
				log.log(Level.WARNING, MessageFormat.format("Unable to find bundle. Bundle: {0}", location));
			} else {
				log.log(Level.WARNING, MessageFormat.format("Unable to start; bundle is not in a startable state. Try stop and starting again.  bundle: {0}", location));
			}
		}

		return success;
	}

	public static boolean stopBundle(String location)
	{
		boolean success = false;

		Bundle bundle = OsgiManager.getFelix().getBundleContext().getBundle(location);
		if (bundle != null
				&& (Bundle.ACTIVE == bundle.getState())) {
			try {
				bundle.stop();
			} catch (BundleException ex) {
				log.log(Level.SEVERE, null, ex);
			}
		} else {
			if (bundle == null) {
				log.log(Level.WARNING, MessageFormat.format("Unable to find bundle. Bundle: {0}", location));
			} else {
				log.log(Level.WARNING, MessageFormat.format("Unable to stop bundle; bundle is not in a stoppable state or already stopped.  bundle: {0}", location));
			}
		}

		return success;
	}

//	public static boolean installWar()
//	{
//		boolean success = false;
//
//
//		return success;
//	}
//
//	public static boolean uninstallWar()
//	{
//		boolean success = false;
//
//		return success;
//	}
	public static List<PluginModel> getAllPlugins()
	{
		List<PluginModel> plugins = new ArrayList<>();

		Bundle bundles[] = OsgiManager.getFelix().getBundleContext().getBundles();
		for (Bundle bundle : bundles) {
			PluginModel pluginModel = new PluginModel();
			pluginModel.setBundleId(bundle.getBundleId());
			if (StringUtils.isNotBlank(bundle.getSymbolicName())) {
				pluginModel.setName(bundle.getSymbolicName());
			} else {
				pluginModel.setName(bundle.getLocation());
			}
			pluginModel.setCurrentState(getBundleState(bundle));
			pluginModel.setLocation(bundle.getLocation());
			pluginModel.setLastModified(new Date(bundle.getLastModified()));
			pluginModel.setVersion(bundle.getVersion().toString());

			plugins.add(pluginModel);
		}

		return plugins;
	}

	private static String getBundleState(Bundle bundle)
	{
		String state = OpenStorefrontConstant.NOT_AVAILABLE;

		switch (bundle.getState()) {
			case Bundle.ACTIVE:
				state = "Active";
				break;
			case Bundle.INSTALLED:
				state = "Installed";
				break;
			case Bundle.RESOLVED:
				state = "Resolved";
				break;
			case Bundle.STARTING:
				state = "Starting";
				break;
			case Bundle.STOPPING:
				state = "Stopping";
				break;
			case Bundle.UNINSTALLED:
				state = "Uninstalled";
				break;
		}
		return state;
	}

	public static void cleanup()
	{
		ServiceProxy service = ServiceProxy.getProxy();
		//stop extension bundles
		for (Bundle bundle : externalBundles.values()) {
			log.log(Level.INFO, MessageFormat.format("Stopping: {0}", bundle.getLocation()));
			try {
				Plugin plugin = new Plugin();
				plugin.setLocation(bundle.getLocation());
				plugin = plugin.find();
				if (plugin != null) {
					service.getPluginService().inactivatePlugin(plugin.getPluginId());
				} else {
					log.log(Level.INFO, MessageFormat.format("Cannot find plugin metadata record for: {0}  Continuing.", bundle.getLocation()));
				}
			} catch (Exception e) {
				log.log(Level.WARNING, MessageFormat.format("Failed to unstalled: {0} (Continuing).", bundle.getLocation()));
			}
		}
	}
	
	public static boolean isLoadingPlugins() 
	{
		return loadingPlugins.get();
	}

	@Override
	public void initialize()
	{
		PluginManager.init();
		started.set(true);
	}

	@Override
	public void shutdown()
	{
		PluginManager.cleanup();
		started.set(false);
	}
	
	@Override
	public boolean isStarted()
	{
		return started.get();
	}	

}
