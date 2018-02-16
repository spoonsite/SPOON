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
package edu.usu.sdl.openstorefront.service.manager.resource;

import edu.usu.sdl.openstorefront.service.manager.ElasticSearchManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 *
 * @author dshurtleff
 */
public class ElasticSearchClient
		implements AutoCloseable
{

	private Logger LOG = Logger.getLogger(ElasticSearchClient.class.getName());

	private ElasticSearchManager elasticSearchManager;
	private RestHighLevelClient client;
	private String serverUrl;
	private Integer port;

	public ElasticSearchClient(String serverUrl, Integer port)
	{
		this.serverUrl = serverUrl;
		this.port = port;
		this.elasticSearchManager = ElasticSearchManager.getInstance();
	}

	public ElasticSearchClient(String serverUrl, Integer port, ElasticSearchManager elasticSearchManager)
	{
		this.elasticSearchManager = elasticSearchManager;
		this.serverUrl = serverUrl;
		this.port = port;
	}

	public RestHighLevelClient getInstance()
	{
		if (client == null) {
			RestClientBuilder builder = RestClient.builder(new HttpHost(serverUrl, port, "http"));
			client = new RestHighLevelClient(builder);
		}
		return client;
	}

	@Override
	public void close()
	{
		if (client != null) {
			try {
				client.close();
			} catch (IOException ex) {
				LOG.log(Level.SEVERE, "Failed to close client. (Release internal client)", ex);
			} finally {
				client = null;
			}
		}
		if (elasticSearchManager != null) {
			elasticSearchManager.releaseClient(this);
		}
	}

}
