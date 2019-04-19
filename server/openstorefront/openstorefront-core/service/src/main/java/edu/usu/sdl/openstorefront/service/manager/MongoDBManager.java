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

import com.mongodb.MongoClientSettings;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 * Handles Mongo DB Resource Keep in mind; we only connect to Mongo as a client;
 * Mongo is expected to be running prior to the application
 *
 * @author dshurtleff
 */
public class MongoDBManager
		implements Initializable
{

	private static final Logger LOG = Logger.getLogger(MongoDBManager.class.getName());
	public static final String DEFAULT_DATABASE = "storefront";

	private AtomicBoolean started = new AtomicBoolean(false);

	private MongoClient mongoClient;
	private PropertiesManager propertiesManager;
	private CodecRegistry pojoCodecRegistry;
	private boolean supportsTransactions;

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
		this.propertiesManager = propertiesManager;
	}

	@Override
	public void initialize()
	{
		//connect and create client
		synchronized (this) {
			if (!isStarted()) {
				String connectionUrl = propertiesManager.getValueDefinedDefault(PropertiesManager.KEY_MONGO_CONNECTION_URL);
				mongoClient = MongoClients.create(connectionUrl);

				pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
						fromProviders(PojoCodecProvider.builder().automatic(true).build()));

				//check for transaction support; only supported in certain cases
				LOG.log(Level.INFO, "Checking for Transaction Support");

				try (ClientSession session = mongoClient.startSession()) {
					session.startTransaction();
					session.commitTransaction();
					supportsTransactions = true;
				} catch (Exception e) {
					LOG.log(Level.WARNING, () -> "Transaction Support will be disabled. " + e.getMessage());
				}
				if (supportsTransactions) {
					LOG.log(Level.INFO, "Transactions supported");
				} else {
					LOG.log(Level.INFO, "Transactions not supported");
				}

				started.set(true);
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
					LOG.info("Finished. (Get a new Instance and initialize for futher use)");
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
	public MongoDatabase getConnection()
	{
		MongoDatabase db = null;
		if (mongoClient != null) {
			db = mongoClient
					.getDatabase(propertiesManager.getValue(PropertiesManager.KEY_MONGO_DATABASE, DEFAULT_DATABASE))
					.withCodecRegistry(pojoCodecRegistry);
		} else {
			throw new OpenStorefrontRuntimeException("Client is not initialized", "Make sure Mongo Manager is started");
		}
		return db;
	}

	public MongoClient getClient()
	{
		if (mongoClient != null) {
			return mongoClient;
		} else {
			started.set(false);
			throw new OpenStorefrontRuntimeException("Client is not initialized", "Make sure Mongo Manager is started");
		}
	}

	public boolean supportTransactions()
	{
		return supportsTransactions;
	}

}
