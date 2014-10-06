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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.util.NamedList;

/**
 * Handles the connection to Solr
 *
 * @author dshurtleff
 */
public class SolrManager
		implements Initializable
{

	private static final Logger log = Logger.getLogger(SolrManager.class.getName());

	//Should reuse server to avoid leaks according to docs.
	private static SolrServer solrServer;

	public static void init()
	{
		String url = PropertiesManager.getValue(PropertiesManager.KEY_SOLR_URL);
		if (StringUtils.isNotBlank(url)) {
			log.log(Level.INFO, MessageFormat.format("Connecting to Solr at {0}", url));
			solrServer = new HttpSolrServer(url, new DefaultHttpClient());
		} else {
			log.log(Level.WARNING, "Solr property (" + PropertiesManager.KEY_SOLR_URL + ") is not set in openstorefront.properties. Search service unavailible. Using Mock");
			solrServer = new SolrServer()
			{

				@Override
				public NamedList<Object> request(SolrRequest request) throws SolrServerException, IOException
				{
					NamedList<Object> results = new NamedList<>();
					log.log(Level.INFO, "Mock Solr recieved request: " + request);
					return results;
				}

				@Override
				public void shutdown()
				{
					//do nothing
				}
			};
		}
	}

	public static void cleanup()
	{
		if (solrServer != null) {
			solrServer.shutdown();
		}
	}

	public static SolrServer getServer()
	{
		return solrServer;
	}

	@Override
	public void initialize()
	{
		SolrManager.init();
	}

	@Override
	public void shutdown()
	{
		SolrManager.cleanup();
	}

}
