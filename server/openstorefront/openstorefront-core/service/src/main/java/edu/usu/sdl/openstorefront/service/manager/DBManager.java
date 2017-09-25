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

import com.orientechnologies.orient.core.command.OCommandOutputListener;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.db.tool.ODatabaseExport;
import com.orientechnologies.orient.core.db.tool.ODatabaseImport;
import com.orientechnologies.orient.object.db.OObjectDatabasePool;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import edu.usu.sdl.openstorefront.service.manager.model.DatabaseStatusListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles DB Resource
 *
 * @author dshurtleff
 */
public class DBManager
		implements Initializable
{

	private static final Logger LOG = Logger.getLogger(DBManager.class.getName());

	public static final String ENTITY_MODEL_PACKAGE = "edu.usu.sdl.openstorefront.core.entity";

	private static AtomicBoolean started = new AtomicBoolean(false);
	private static OServer server;
	private static final String REMOTE_URL = "remote:localhost/openstorefront";

	private static OObjectDatabasePool globalInstance;

	@Override
	public void initialize()
	{
		DBManager.init();
	}

	@Override
	public void shutdown()
	{
		DBManager.cleanup();
	}

	/**
	 * Called once at application Startup
	 */
	public static void init()
	{

		try {
			LOG.info("Starting Orient DB...");
			server = OServerMain.create();

			String home = FileSystemManager.getDir(FileSystemManager.DB_DIR).getPath();
			System.setProperty("ORIENTDB_HOME", home);
			System.setProperty("ORIENTDB_ROOT_PASSWORD", PropertiesManager.getValue(PropertiesManager.KEY_DB_AT));
			server.setServerRootDirectory(home);

			server.startup(FileSystemManager.getConfig("orientdb-server-config.xml"));
			server.activate();

			String dbFileDir = home + "/databases/openstorefront";
			File dbFile = new File(dbFileDir);
			if (dbFile.exists() == false) {
				LOG.log(Level.INFO, "Creating DB at %s", dbFileDir);
				OObjectDatabaseTx db = new OObjectDatabaseTx("plocal:" + dbFileDir).create();
				db.close();
				LOG.log(Level.INFO, "Done");
			}

			//TODO: switch OPartitionedDatabasePool	after version 2.3
			globalInstance = OObjectDatabasePool.global(Integer.parseInt(PropertiesManager.getValue(PropertiesManager.KEY_DB_CONNECT_MIN)), Integer.parseInt(PropertiesManager.getValue(PropertiesManager.KEY_DB_CONNECT_MAX)));

			try (OObjectDatabaseTx db = getConnection()) {
				db.getEntityManager().registerEntityClasses(ENTITY_MODEL_PACKAGE, BaseEntity.class.getClassLoader());
			}

			started.set(true);
			LOG.info("Finished.");
		} catch (Exception ex) {
			LOG.log(Level.SEVERE, "Error occuring starting orient", ex);
			throw new OpenStorefrontRuntimeException(ex);
		}
	}

	/**
	 * Called once at application shutdown
	 */
	public static void cleanup()
	{

		LOG.info("Shutting down Orient DB...");
		if (globalInstance != null) {
			globalInstance.close();
		}
		if (server != null) {
			server.shutdown();
		}
		started.set(false);
		LOG.info("Finished.");
	}

	public static OObjectDatabaseTx getConnection()
	{
		return globalInstance.acquire(REMOTE_URL, PropertiesManager.getValue(PropertiesManager.KEY_DB_USER), PropertiesManager.getValue(PropertiesManager.KEY_DB_AT));
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

	public static void exportDB(OutputStream out) throws IOException
	{
		exportDB(out, null);
	}

	public static void exportDB(OutputStream out, DatabaseStatusListener dbListener) throws IOException
	{
		ODatabaseDocumentTx db = new ODatabaseDocumentTx(REMOTE_URL);
		db.open(PropertiesManager.getValue(PropertiesManager.KEY_DB_USER), PropertiesManager.getValue(PropertiesManager.KEY_DB_AT));
		try (OutputStream closableOut = out) {
			OCommandOutputListener listener = (String iText) -> {
				if (LOG.isLoggable(Level.FINEST)) {
					LOG.log(Level.FINEST, iText);
				}
			};
			if (dbListener != null) {
				listener = (String iText) -> {
					dbListener.statusUpdate(iText);
				};
			}

			ODatabaseExport export = new ODatabaseExport(db, closableOut, listener);
			export.exportDatabase();
			export.close();
		} finally {
			db.close();
		}
	}

	public static void importDB(InputStream in) throws IOException
	{
		importDB(in, null);
	}

	public static void importDB(InputStream in, DatabaseStatusListener dbListener) throws IOException
	{
		ODatabaseDocumentTx db = new ODatabaseDocumentTx(REMOTE_URL);
		db.open(PropertiesManager.getValue(PropertiesManager.KEY_DB_USER), PropertiesManager.getValue(PropertiesManager.KEY_DB_AT));
		try (InputStream closableIn = in) {
			OCommandOutputListener listener = (String iText) -> {
				if (LOG.isLoggable(Level.FINEST)) {
					LOG.log(Level.FINEST, iText);
				}
			};
			if (dbListener != null) {
				listener = (String iText) -> {
					dbListener.statusUpdate(iText);
				};
			}

			ODatabaseImport dbImport = new ODatabaseImport(db, closableIn, listener);
			dbImport.importDatabase();
			dbImport.close();
		} finally {
			db.close();
		}
	}

}
