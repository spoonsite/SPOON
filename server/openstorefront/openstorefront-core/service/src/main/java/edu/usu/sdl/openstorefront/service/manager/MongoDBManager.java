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

import com.mongodb.client.MongoClient;
import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * Handles Mongo DB Resource Keep in mind; we only Mongo connect as a client;
 * Mongo is expected to be running prior to the application
 *
 * @author dshurtleff
 */
public class MongoDBManager
		implements Initializable
{

	private static final Logger LOG = Logger.getLogger(MongoDBManager.class.getName());
	private static final String DEFAULT_DATABASE = "storefront";

	private AtomicBoolean started = new AtomicBoolean(false);

	private MongoClient mongoClient;
	private PropertiesManager propertiesManager;

	protected static MongoDBManager singleton = null;

	public static MongoDBManager getInstance()
	{
		if (singleton == null) {
			singleton = new MongoDBManager(PropertiesManager.getInstance());
		}
		return singleton;
	}

	public MongoDBManager(PropertiesManager propertiesManager)
	{
		this.mongoClient = mongoClient;
		this.propertiesManager = propertiesManager;
	}

	@Override
	public void initialize()
	{
		//connect and create client
		synchronized (this) {
			if (!isStarted()) {

			}
		}
	}

	@Override
	public void shutdown()
	{
		synchronized (this) {
			if (isStarted()) {
				if (mongoClient != null) {
					LOG.info("Shutting down Mongo Client...");
					mongoClient.close();
					started.set(false);
					LOG.info("Finished. (Get a new Instance and initialize) ");
				}
				singleton = null;
			} else {
				LOG.info("Mongo Client already shutdown.");
			}
		}
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

	//get a database (
}
