/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author dshurtleff
 */
public class DBManager
		implements Initializable

{

	private PropertiesManager propertiesManager;

	private AtomicBoolean started = new AtomicBoolean(false);
	protected static DBManager singleton = null;
	private OrientDBManager orientDBManager;
	private MongoDBManager mongoDBManager;

	public static DBManager getInstance()
	{
		if (singleton == null) {
			singleton = new DBManager(PropertiesManager.getInstance());
		}
		return singleton;
	}

	public DBManager(PropertiesManager propertiesManager)
	{
		this.propertiesManager = propertiesManager;
	}

	@Override
	public void initialize()
	{
		boolean useMongo = Convert.toBoolean(propertiesManager.getValue(PropertiesManager.KEY_DB_USE_MONGO, "false"));

		Initializable actualManager;

		if (useMongo) {
			mongoDBManager = MongoDBManager.getInstance();
			actualManager = mongoDBManager;
		} else {
			orientDBManager = OrientDBManager.getInstance();
			actualManager = orientDBManager;
		}
		actualManager.initialize();
		started.set(true);
	}

	public boolean usingMongo()
	{
		return mongoDBManager != null;
	}

	public boolean usingOrient()
	{
		return orientDBManager != null;
	}

	/**
	 *
	 * @return Active manager instance or null if neither is active
	 */
	public Initializable getActiveDatabaseManager()
	{
		if (orientDBManager != null) {
			return orientDBManager;
		} else {
			return mongoDBManager;
		}
	}

	@Override
	public void shutdown()
	{
		Initializable actualManager = getActiveDatabaseManager();
		if (actualManager != null) {
			actualManager.shutdown();
			orientDBManager = null;
			mongoDBManager = null;
		}
		started.set(false);
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

}
