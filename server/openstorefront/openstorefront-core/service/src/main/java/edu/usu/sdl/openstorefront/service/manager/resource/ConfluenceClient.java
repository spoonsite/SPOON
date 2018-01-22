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

import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.service.manager.PooledResourceManager;
import edu.usu.sdl.openstorefront.service.manager.model.ConnectionModel;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.Content;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.ContentSearchResult;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.ContentSearchResults;
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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

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
			spaceKey = StringProcessor.urlEncode(spaceKey);
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
			spaceKey = StringProcessor.urlEncode(spaceKey);
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

	/**
	 * Gets a confluence page
	 *
	 * @param spaceKey
	 * @param title
	 * @return Content of page or null if not found
	 */
	public Content getPage(String spaceKey, String title)
	{
		Content content = null;

		Client client = getClientCache();
		try {
			spaceKey = StringProcessor.urlEncode(spaceKey);
			title = StringProcessor.urlEncode(title);
			WebTarget target = client.target(connectionModel.getUrl() + BASE_API_URL + "content?spaceKey=" + spaceKey + "&title=" + title);

			ContentSearchResults results = target.request().buildGet().invoke(ContentSearchResults.class);

			if (!results.getResults().isEmpty()) {
				//get first result
				ContentSearchResult contentSearchResult = results.getResults().get(0);
				target = client.target(connectionModel.getUrl() + BASE_API_URL + "content/" + contentSearchResult.getId() + "?expand=body.view,version");
				content = target.request().buildGet().invoke(Content.class);
			}

		} finally {
			closeClient();
		}
		return content;
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
			String contentId = StringProcessor.urlEncode(content.getId());
			WebTarget target = client.target(connectionModel.getUrl() + BASE_API_URL + "content/" + contentId);

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
			contentId = StringProcessor.urlEncode(contentId);
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
			contentId = StringProcessor.urlEncode(contentId);
			WebTarget target = client.target(connectionModel.getUrl() + BASE_API_URL + "content/" + contentId + "/label");

			GenericEntity<List<Label>> entity = new GenericEntity<List<Label>>(labels)
			{
			};
			Response result = target.request().buildPost(Entity.json(entity)).invoke();
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
			contentId = StringProcessor.urlEncode(contentId);
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

	/**
	 * Confluence only support a sub-set of html and has it own markup. It's
	 * better to create a specific template for it. However, if need you use an
	 * html template and then use this to filter it. Adjust as needed.
	 *
	 * @See
	 * https://confluence.atlassian.com/doc/confluence-storage-format-790796544.html
	 * @See
	 * https://confluence.atlassian.com/conf53/confluence-storage-format-for-macros-411108832.html
	 * for macros
	 *
	 * @param input
	 * @return
	 */
	public static String confluenceSafeText(String input)
	{
		if (StringUtils.isNotBlank(input)) {
			Whitelist whiteList = Whitelist.basic().addTags(
					"h1", "h2", "h3", "h4", "h5", "h6",
					"table", "thead", "tbody", "th", "tr", "td"
			);

			input = Jsoup.clean(input, whiteList);
		}
		return input;
	}

	/**
	 * This will replace character Apply pre-markup since it would affect the
	 * markup; Use on data
	 *
	 * @param input
	 * @return
	 */
	public static String confluenceEscapeCharater(String input)
	{
		if (StringUtils.isNotBlank(input)) {
			input = input.replace("&", "&amp;");
			input = input.replace("<", "&lt;");
			input = input.replace(">", "&gt;");
		}
		return input;
	}

}
