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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class SearchServerManager
		implements Initializable
{

	private static final Logger LOG = Logger.getLogger(SearchServerManager.class.getName());

	public static final String SOLR = "solr";
	public static final String ELASTICSEARCH = "elasticsearch";

	private AtomicBoolean started = new AtomicBoolean(false);
	private BaseSearchManager searchServer = null;
	private static SearchServerManager singleton = null;
	private PropertiesManager propertiesManager;

	public BaseSearchManager getSearchServer()
	{
		return searchServer;
	}

	protected SearchServerManager(PropertiesManager propManager)
	{
		this.propertiesManager = propManager;
	}

	public static SearchServerManager getInstance()
	{
		return getInstance(PropertiesManager.getInstance());
	}

	public static SearchServerManager getInstance(PropertiesManager propertiesManager)
	{
		if (singleton == null) {
			singleton = new SearchServerManager(propertiesManager);
		}
		return singleton;
	}

	private void init()
	{
		String searchImplementation = propertiesManager.getValue(PropertiesManager.KEY_SEARCH_SERVER, ELASTICSEARCH).toLowerCase();

		LOG.log(Level.CONFIG, () -> "Using " + searchImplementation + " as search server.");
		switch (searchImplementation) {
			case SOLR:
				searchServer = SolrManager.getInstance(propertiesManager, null);
				break;

			case ELASTICSEARCH:
				searchServer = ElasticSearchManager.getInstance(propertiesManager);
				break;
			default:
				LOG.config("Unsupported Search Server. Switching to Elasticsearch.");
				searchServer = ElasticSearchManager.getInstance(propertiesManager);
		}
		((Initializable) searchServer).initialize();
	}

	private void cleanup()
	{
		if (searchServer != null) {
			((Initializable) searchServer).shutdown();
		}
	}

	@Override
	public void initialize()
	{
		init();
		started.set(true);
	}

	@Override
	public void shutdown()
	{
		cleanup();
		started.set(false);
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

}
