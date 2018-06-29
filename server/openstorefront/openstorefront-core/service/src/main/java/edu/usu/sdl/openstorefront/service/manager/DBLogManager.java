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

import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.service.manager.resource.DBLogHandler;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Handles initing DB Logs
 *
 * @author dshurtleff
 */
public class DBLogManager
		implements Initializable
{

	private static final Logger log = Logger.getLogger(DBLogManager.class.getName());
	private static DBLogHandler logHandler;
	private static AtomicBoolean started = new AtomicBoolean(false);

	public static long getMaxLogEntries()
	{
		long max;
		String maxRecords = PropertiesManager.getInstance().getValueDefinedDefault(PropertiesManager.KEY_DBLOG_MAX_RECORD);
		max = Convert.toLong(maxRecords);
		return max;
	}

	public static void init()
	{
		String useDBLogger = PropertiesManager.getInstance().getValueDefinedDefault(PropertiesManager.KEY_DBLOG_ON);
		if (Convert.toBoolean(useDBLogger)) {
			log.log(Level.CONFIG, "Using DB Logger");

			logHandler = new DBLogHandler();
			logHandler.setLevel(Level.ALL);

			Logger rootLogger = LogManager.getLogManager().getLogger("");
			if (rootLogger != null) {
				rootLogger.addHandler(logHandler);
			}

			Logger openstorefrontLogger = LogManager.getLogManager().getLogger("edu.usu.sdl.openstorefront");
			if (openstorefrontLogger != null) {
				openstorefrontLogger.addHandler(logHandler);
			}
		}
	}

	public static void cleanup()
	{
		Logger rootLogger = LogManager.getLogManager().getLogger("");
		if (rootLogger != null) {
			rootLogger.removeHandler(logHandler);
		}

		Logger openstorefrontLogger = LogManager.getLogManager().getLogger("edu.usu.sdl.openstorefront");
		if (openstorefrontLogger != null) {
			openstorefrontLogger.removeHandler(logHandler);
		}
		if (logHandler != null) {
			logHandler.close();
		}
	}

	@Override
	public void initialize()
	{
		DBLogManager.init();
		started.set(true);		
	}

	@Override
	public void shutdown()
	{
		DBLogManager.cleanup();
		started.set(false);
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

}
