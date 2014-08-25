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
package edu.usu.sdl.openstorefront.service.manager;

import com.orientechnologies.orient.object.db.OObjectDatabasePool;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class DBManager
{

	private static final Logger log = Logger.getLogger(DBManager.class.getName());

	private static OServer server;
	private static OObjectDatabasePool globalInstance;

	/**
	 * Called once at application Startup
	 */
	public static void initialize()
	{
		try {
			log.info("Starting Orient DB...");
			server = OServerMain.create();

			String home = FileSystemManager.getDir(FileSystemManager.DB_DIR).getPath();
			System.setProperty("ORIENTDB_HOME", home);
			server.setServerRootDirectory(home);

			server.startup(FileSystemManager.getConfig("orientdb-server-config.xml"));
			server.activate();

			String dbFileDir = home + "/databases/openstorefront";
			File dbFile = new File(dbFileDir);
			if (dbFile.exists() == false) {
				log.log(Level.INFO, "Creating DB at {0}", dbFileDir);
				OObjectDatabaseTx db = new OObjectDatabaseTx("plocal:" + dbFileDir).create();
				db.close();
				log.log(Level.INFO, "Done");
			}

			globalInstance = OObjectDatabasePool.global(Integer.parseInt(PropertiesManager.getValue(PropertiesManager.KEY_DB_CONNECT_MIN)), Integer.parseInt(PropertiesManager.getValue(PropertiesManager.KEY_DB_CONNECT_MAX)));

			try (OObjectDatabaseTx db = getConnection()) {
				db.getEntityManager().registerEntityClasses("edu.usu.sdl.openstorefront.storage.model");
				//db.getEntityManager().registerEntityClass(TestEntity.class);
//				db.getEntityManager().getRegisteredEntities().forEach(entityClass -> {
//					db..browseClass(entityClass);
//
//				});
			}

			log.info("Finished.");
		} catch (Exception ex) {
			log.log(Level.SEVERE, "Error occuring starting orient", ex);
		}
	}

	/**
	 * Called once at application shutdown
	 */
	public static void shutdown()
	{
		log.info("Shutting down Orient DB...");
		globalInstance.close();
		server.shutdown();

		log.info("Finished.");
	}

	public static OObjectDatabaseTx getConnection()
	{
		return globalInstance.acquire("remote:localhost/openstorefront", PropertiesManager.getValue(PropertiesManager.KEY_DB_USER), PropertiesManager.getValue(PropertiesManager.KEY_DB_PASSWORD));
	}

}
