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

import edu.usu.sdl.openstorefront.service.io.AttributeImporter;
import edu.usu.sdl.openstorefront.service.io.LookupImporter;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.service.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.service.manager.Initializable;
import edu.usu.sdl.openstorefront.service.manager.JobManager;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author dshurtleff
 */
@WebListener
public class ApplicationInit
		implements ServletContextListener
{

	private static final Logger log = Logger.getLogger(ApplicationInit.class.getName());

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		try {
			//Init Logger
			LogManager.getLogManager().readConfiguration(new FileInputStream(FileSystemManager.getConfig("logging.properties")));
		} catch (IOException ex) {
			log.log(Level.SEVERE, "Unable to initialize logger....check logging.properties of the application.", ex);
		}

		//Order is important
		startupManager(new DBManager());
		startupManager(new OSFCacheManager());
		startupManager(new LookupImporter());
		startupManager(new AttributeImporter());
		startupManager(new JobManager());
	}

	private void startupManager(Initializable initializable)
	{
		log.log(Level.INFO, MessageFormat.format("Starting up:{0}", initializable.getClass().getSimpleName()));
		initializable.initialize();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		//Shutdown in reverse order to make sure the dependancies are good.
		shutdownManager(new JobManager());
		shutdownManager(new OSFCacheManager());
		shutdownManager(new DBManager());
	}

	private void shutdownManager(Initializable initializable)
	{
		//On shutdown we want it to roll through
		log.log(Level.INFO, MessageFormat.format("Shutting down:{0}", initializable.getClass().getSimpleName()));
		try {
			initializable.shutdown();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Unable to Shutdown: " + initializable.getClass().getSimpleName(), e);
		}
	}

}
