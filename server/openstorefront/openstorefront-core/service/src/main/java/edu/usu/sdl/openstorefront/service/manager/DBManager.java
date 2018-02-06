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

	private AtomicBoolean started = new AtomicBoolean(false);
	private OServer server;
	private static OObjectDatabasePool globalInstance;
	private final String entityModelPackage;
	private final String url;
	private String dbFileDir;

	// <editor-fold defaultstate="collapsed" desc="Singleton getInstance() Methods">
	protected static DBManager singleton = null;

	public static DBManager getInstance()
	{
		if (singleton == null) {
			singleton = new DBManager("remote:localhost/openstorefront", "edu.usu.sdl.openstorefront.core.entity");
		}
		return singleton;
	}

	// </editor-fold>
	protected DBManager(String url, String entityModelPackage)
	{
		this.url = url;
		this.entityModelPackage = entityModelPackage;
	}

	/**
	 * Called once at application Startup
	 */
	@Override
	public void initialize()
	{
		synchronized (this) {
			if (!isStarted()) {
				try {
					LOG.info("Starting Orient DB...");
					server = OServerMain.create();

					String home = FileSystemManager.getInstance().getDir(FileSystemManager.DB_DIR).getPath();
					System.setProperty("ORIENTDB_HOME", home);
					System.setProperty("ORIENTDB_ROOT_PASSWORD", PropertiesManager.getInstance().getValue(PropertiesManager.KEY_DB_AT));
					server.setServerRootDirectory(home);

					server.startup(FileSystemManager.getInstance().getConfig("orientdb-server-config.xml"));
					server.activate();
					this.dbFileDir = home + "/databases/openstorefront";
					createDatabase();
					createPool();

					started.set(true);
					LOG.info("Finished.");
				} catch (Exception ex) {
					LOG.log(Level.SEVERE, "Error occuring starting orient", ex);
					throw new OpenStorefrontRuntimeException(ex);
				}
			}
		}
	}

	/**
	 * Must use the Object database pool otherwise the connect will not be set
	 * correctly, In 3.x there is a new Object pool : ODatabaseObjectPool.java
	 * look to switch to that. Also 3.x Object API will exist it just they not
	 * add features to the api. Unless we need some feature it's not worth
	 * switching to as the we would need to provide the bindings. We can also
	 * use the new multi-model api in conjuction as needed for additional
	 * features.
	 */
	protected synchronized void createPool()
	{
		globalInstance = new OObjectDatabasePool(url, PropertiesManager.getInstance().getValue(PropertiesManager.KEY_DB_USER), PropertiesManager.getInstance().getValue(PropertiesManager.KEY_DB_AT));

		globalInstance.setup(Integer.parseInt(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_DB_CONNECT_MIN)), Integer.parseInt(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_DB_CONNECT_MAX)));

		try (OObjectDatabaseTx db = getConnection()) {
			db.getEntityManager().registerEntityClasses(entityModelPackage, BaseEntity.class.getClassLoader());
		}
	}

	protected synchronized void createDatabase()
	{
		File dbFile = new File(this.dbFileDir);
		if (dbFile.exists() == false) {
			LOG.log(Level.INFO, "Creating DB at %s", this.dbFileDir);
			try(ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:" + this.dbFileDir)){
				db.create();
				db.close();
			}
			LOG.log(Level.INFO, "Done");
		}
	}

	/**
	 * Called once at application shutdown
	 */
	@Override
	public void shutdown()
	{
		synchronized (this) {
			if (isStarted()) {
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
			singleton = null;
		}
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

	public OObjectDatabaseTx getConnection()
	{
		return globalInstance.acquire();
	}

	public String getEntityModelPackage()
	{
		return entityModelPackage;
	}

	protected String getURL()
	{
		return url;
	}

	// <editor-fold defaultstate="collapsed" desc="Import/Export Methods">
	public void exportDB(OutputStream out) throws IOException
	{
		singleton.exportDB(out, null);
	}

	public void exportDB(OutputStream out, DatabaseStatusListener dbListener) throws IOException
	{
		ODatabaseDocumentTx db = new ODatabaseDocumentTx(url);
		db.open(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_DB_USER), PropertiesManager.getInstance().getValue(PropertiesManager.KEY_DB_AT));
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

	public void importDB(InputStream in) throws IOException
	{
		singleton.importDB(in, null);
	}

	public void importDB(InputStream in, DatabaseStatusListener dbListener) throws IOException
	{
		ODatabaseDocumentTx db = new ODatabaseDocumentTx(url);
		db.open(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_DB_USER), PropertiesManager.getInstance().getValue(PropertiesManager.KEY_DB_AT));
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
	// </editor-fold>
}
