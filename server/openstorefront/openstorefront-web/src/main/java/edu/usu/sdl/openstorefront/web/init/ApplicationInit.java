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

import edu.usu.sdl.openstorefront.service.job.LookupImporter;
import edu.usu.sdl.openstorefront.service.manager.DBManager;
import edu.usu.sdl.openstorefront.service.manager.JobManager;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
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
		log.info("Initing DB Manager...");
		DBManager.initialize();

		log.info("Initing Cache Manager...");
		OSFCacheManager.initialize();

		log.info("Initing LookupImporter");
		LookupImporter lookupImporter = new LookupImporter();
		lookupImporter.initImport();

		log.info("Initing Job Manager...");
		JobManager.initialize();

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		//Shutdown in reverse order to make sure the dependancies are good.

		log.info("Shutting down Job Manager...");
		JobManager.shutdown();

		log.info("Shutting down Cache Manager...");
		OSFCacheManager.shutdown();

		log.info("Shutting down DB Manager...");
		DBManager.shutdown();
	}

}
