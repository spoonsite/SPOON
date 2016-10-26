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

import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.service.manager.model.ConnectionModel;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class ConfluenceManager
		implements Initializable
{
	private static final Logger log = Logger.getLogger(ConfluenceManager.class.getName());
	private static Client client;

	
	
	private static AtomicBoolean started = new AtomicBoolean(false);

	public static void init()
	{
		log.log(Level.FINE, "Initializing Confluence Connection");
		ConnectionModel connectionModel = new ConnectionModel();
		connectionModel.setUrl(PropertiesManager.getValue(PropertiesManager.KEY_CONFLUENCE_URL));
		connectionModel.setUsername(PropertiesManager.getValue(PropertiesManager.KEY_TOOLS_USER));
		connectionModel.setCredential(PropertiesManager.getValue(PropertiesManager.KEY_TOOLS_CREDENTIALS));

		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(connectionModel.getUsername(), connectionModel.getCredential());
		client = ClientBuilder.newClient().register(feature);
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
		client = null;
		started.set(false);
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

	public static Client getClient()
	{
		return client;
	}

	public static void setClient(Client client)
	{
		ConfluenceManager.client = client;
	}

	public static AtomicBoolean getStarted()
	{
		return started;
	}

	public static void setStarted(AtomicBoolean started)
	{
		ConfluenceManager.started = started;
	}
	
}
