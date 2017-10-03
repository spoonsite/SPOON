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

import edu.usu.sdl.openstorefront.service.manager.PooledResourceManager;
import edu.usu.sdl.openstorefront.service.manager.model.ConnectionModel;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.Content;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.Label;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.NewSpace;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.Space;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.SpaceResults;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

/**
 * Client connection to confluence
 *
 * @author dshurtleff
 */
public class ConfluenceClient
		implements AutoCloseable
{

	private static final Logger LOG = Logger.getLogger(ConfluenceClient.class.getName());

	private static final String BASE_API_URL = "/rest/api/";

	private final ConnectionModel connectionModel;
	private PooledResourceManager confluenceManager;

	private Client clientCache;

	public ConfluenceClient(ConnectionModel connectionModel, PooledResourceManager pooledResourceManager)
	{
		this.connectionModel = connectionModel;
		this.confluenceManager = pooledResourceManager;
	}

	private Client getClientCache()
	{
		if (clientCache == null) {
			HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(connectionModel.getUsername(), connectionModel.getCredential());
			clientCache = ClientBuilder.newClient().register(feature);
		}
		return clientCache;
	}

	private void closeClient()
	{
		if (clientCache != null) {
			clientCache.close();
			clientCache = null;
		}
	}

	public SpaceResults getSpaces(int start, int limit)
	{
		Client client = getClientCache();
		try {
			WebTarget target = client.target(connectionModel.getUrl() + BASE_API_URL + "space?start=" + start + "&limit=" + limit);
			SpaceResults results = target.request().buildGet().invoke(SpaceResults.class);
			return results;
		} finally {
			closeClient();
		}
	}

	public Space getSpace(String spaceKey)
	{
		Objects.requireNonNull(spaceKey);

		Client client = getClientCache();
		try {
			WebTarget target = client.target(connectionModel.getUrl() + BASE_API_URL + "space/" + spaceKey);
			Space result = target.request().buildGet().invoke(Space.class);
			return result;
		} finally {
			closeClient();
		}
	}

	public NewSpace createSpace(NewSpace newSpace)
	{
		Client client = getClientCache();
		try {
			String privateSpace = "";
			if (newSpace.getPrivateSpace()) {
				privateSpace = "/_private";
			}
			WebTarget target = client.target(connectionModel.getUrl() + BASE_API_URL + "space" + privateSpace);

			NewSpace result = target.request().buildPost(Entity.json(newSpace)).invoke(NewSpace.class);
			return result;
		} finally {
			closeClient();
		}
	}

	public boolean deleteSpace(String spaceKey)
	{
		Objects.requireNonNull(spaceKey);
		boolean success = false;

		Client client = getClientCache();
		try {
			WebTarget target = client.target(connectionModel.getUrl() + BASE_API_URL + "space/" + spaceKey);
			Response response = target.request().buildDelete().invoke();
			if (response.getStatus() < 500) {
				success = true;
			}

		} finally {
			closeClient();
		}
		return success;
	}

	public Content getPage(String spaceKey, String title)
	{
		Client client = getClientCache();
		try {
			WebTarget target = client.target(connectionModel.getUrl() + BASE_API_URL + "content?spaceKey=" + spaceKey + "&title=" + title);

			Content result = target.request().buildGet().invoke(Content.class);
			return result;
		} finally {
			closeClient();
		}
	}

	public Content createPage(Content content)
	{
		Objects.requireNonNull(content);
		Client client = getClientCache();
		try {
			WebTarget target = client.target(connectionModel.getUrl() + BASE_API_URL + "content");

			Content result = target.request().buildPost(Entity.json(content)).invoke(Content.class);
			return result;
		} finally {
			closeClient();
		}
	}

	public Content updatePage(Content content)
	{
		Objects.requireNonNull(content);
		Objects.requireNonNull(content.getId());

		Client client = getClientCache();
		try {
			WebTarget target = client.target(connectionModel.getUrl() + BASE_API_URL + "content");

			Content result = target.request().buildPut(Entity.json(content)).invoke(Content.class);
			return result;
		} finally {
			closeClient();
		}
	}

	public boolean deletePage(String contentId)
	{
		Objects.requireNonNull(contentId);
		boolean success = false;

		Client client = getClientCache();
		try {
			WebTarget target = client.target(connectionModel.getUrl() + BASE_API_URL + "content/" + contentId);
			Response response = target.request().buildDelete().invoke();
			if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
				success = true;
			}

		} finally {
			closeClient();
		}
		return success;
	}

//	public void getAttachment()
//	{
//
//	}
//
//	public void createAttachment()
//	{
//
//	}
	public void addLabel(String contentId, List<Label> labels)
	{
		Client client = getClientCache();
		try {
			WebTarget target = client.target(connectionModel.getUrl() + BASE_API_URL + "content/" + contentId + "/label");

			Response result = target.request().buildPost(Entity.json(labels)).invoke();

			//TODO: What is the response?
		} finally {
			closeClient();
		}
	}

	public boolean deleteLabel(String contentId, String label)
	{
		Objects.requireNonNull(contentId);
		boolean success = false;

		Client client = getClientCache();
		try {
			WebTarget target = client.target(connectionModel.getUrl() + BASE_API_URL + "content/" + contentId + "/label?name=" + label);
			Response response = target.request().buildDelete().invoke();
			if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
				success = true;
			}
		} finally {
			closeClient();
		}
		return success;
	}

	@Override
	public void close()
	{
		closeClient();
		if (confluenceManager != null) {
			confluenceManager.releaseClient(this);
		}
	}

}
