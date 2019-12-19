/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.entity.ErrorTypeCode;
import edu.usu.sdl.openstorefront.service.manager.model.ConnectionModel;
import edu.usu.sdl.openstorefront.service.manager.resource.ConfluenceClient;
import java.text.MessageFormat;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This handles the general confluence client pool
 *
 * @author dshurtleff
 */
public class ConfluenceManager
		implements Initializable, PooledResourceManager<ConfluenceClient>
{

	private static final Logger LOG = Logger.getLogger(ConfluenceManager.class.getName());

	private BlockingQueue<ConfluenceClient> clientPool;
	private int maxPoolSize;
	private static ConfluenceManager poolInstance;

	private static AtomicBoolean started = new AtomicBoolean(false);

	public ConfluenceManager()
	{
	}

	public ConfluenceManager(BlockingQueue<ConfluenceClient> clientPool, int maxPoolSize)
	{
		this.clientPool = clientPool;
		this.maxPoolSize = maxPoolSize;
	}

	public static void init()
	{
		LOG.log(Level.FINE, "Initializing Confluence Pool");

		String poolSize = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_CONFLUENCE_POOL_SIZE, "20");
		int maxPoolSize = Convert.toInteger(poolSize);
		BlockingQueue<ConfluenceClient> clientPool = new ArrayBlockingQueue<>(maxPoolSize, true);
		poolInstance = new ConfluenceManager(clientPool, maxPoolSize);

		LOG.log(Level.FINE, MessageFormat.format("Filling Pool to: {0}", poolSize));
		ConnectionModel connectionModel = new ConnectionModel();
		connectionModel.setUrl(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_CONFLUENCE_URL));
		connectionModel.setUsername(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_TOOLS_USER));
		connectionModel.setCredential(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_TOOLS_CREDENTIALS));

		for (int i = 0; i < maxPoolSize; i++) {
			ConfluenceClient client = new ConfluenceClient(connectionModel, poolInstance);
			if (!clientPool.offer(client)) {
				LOG.log(Level.FINER, "Client not added to Pool; Pool was full.");
			}
		}
	}

	public static AtomicBoolean getStarted()
	{
		return started;
	}

	public static void setStarted(AtomicBoolean started)
	{
		ConfluenceManager.started = started;
	}

	public static ConfluenceManager getPoolInstance()
	{
		return poolInstance;
	}

	@Override
	public void initialize()
	{
		ConfluenceManager.init();
		started.set(true);
	}

	@Override
	public void shutdown()
	{
		if (poolInstance != null) {
			if (poolInstance.getAvailableConnections() != poolInstance.getMaxConnections()) {
				LOG.log(Level.WARNING, () -> MessageFormat.format("{0} confluence connections were in process. ", poolInstance.getAvailableConnections()));
			}
			LOG.log(Level.FINE, "Stopping pool.");
			poolInstance.shutdownPool();
		}
		started.set(false);
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

	@Override
	public ConfluenceClient getClient()
	{
		int waitTimeSeconds = Convert.toInteger(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_CONFLUENCE_CONNECTION_WAIT_TIME, "60"));
		try {
			ConfluenceClient client = clientPool.poll(waitTimeSeconds, TimeUnit.SECONDS);
			if (client == null) {
				throw new OpenStorefrontRuntimeException("Unable to retrieve Confluence Connection in time.  No resource available.", "Adjust confluence pool size appropriate to load or try again", ErrorTypeCode.INTEGRATION);
			}
			return client;
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
			throw new OpenStorefrontRuntimeException("Unable to retrieve Confluence Connection - wait interrupted.  No resource available.", "Adjust confluence pool size appropriate to load.", ex, ErrorTypeCode.INTEGRATION);
		}
	}

	@Override
	public void releaseClient(ConfluenceClient confluenceClient)
	{
		if (!clientPool.offer(confluenceClient)) {
			LOG.log(Level.FINER, "Client not added to Pool; Pool was full.");
		}
	}

	@Override
	public int getMaxConnections()
	{
		return maxPoolSize;
	}

	@Override
	public int getAvailableConnections()
	{
		return maxPoolSize - clientPool.remainingCapacity();
	}

	@Override
	public void shutdownPool()
	{
		for (ConfluenceClient client : clientPool) {
			client.close();
		}
		clientPool.clear();
	}

}
