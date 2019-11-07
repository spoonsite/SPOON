/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.core.entity.BaseEntity;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kbair
 */
public class MemoryDBManager
		extends OrientDBManager
{

	private static final Logger LOG = Logger.getLogger(MemoryDBManager.class.getName());

	private static final String DB_USER = "admin";
	private static final String DB_PW = "admin";

	private static OObjectDatabaseTx globalInstance;

	// <editor-fold defaultstate="collapsed" desc="Singleton getInstance() Methods">
	public static MemoryDBManager getInstance(String url, String entityModelPackage, String configFile, FileSystemManager fileSystemManager, PropertiesManager propertiesManager)
	{
		if (singleton == null) {
			singleton = new MemoryDBManager(url, entityModelPackage, configFile, fileSystemManager, propertiesManager);
		}
		return (MemoryDBManager) singleton;
	}

	// </editor-fold>
	protected MemoryDBManager(String url, String entityModelPackage, String configFile, FileSystemManager fileSystemManager, PropertiesManager propertiesManager)
	{
		super(url, entityModelPackage, configFile, fileSystemManager, propertiesManager);
	}

	@Override
	protected synchronized void createPool()
	{
		try (OObjectDatabaseTx db = getConnection()) {
			db.getEntityManager().registerEntityClasses(getEntityModelPackage(), BaseEntity.class.getClassLoader());
		}
	}

	@Override
	protected synchronized void createDatabase()
	{
		LOG.log(Level.INFO, "Creating Memory DB");
		globalInstance = new OObjectDatabaseTx(new ODatabaseDocumentTx(getURL()));
		globalInstance.create();
		globalInstance.close();
		LOG.log(Level.INFO, "Done");
	}

	@Override
	public OObjectDatabaseTx getConnection()
	{
		if (globalInstance.isClosed()) {
			globalInstance.open(DB_USER, DB_PW);
		}
		return globalInstance;
	}

	public synchronized void reset()
	{
		if (globalInstance.exists()) {
			getConnection().drop();
		}
		createDatabase();
		createPool();
	}
}
