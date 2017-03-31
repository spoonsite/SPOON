/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.usecase;

import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class PostmanCollection
{

	@Test
	public void generatedPostmanCollection()
	{
		String fileName = "c:/temp/postmanCollection.json";
		String id = "d4de36f5-d5d6-7512-7490-96155b17972c";
		String urlBase = "http://store-dev.usu.di2e.net/openstorefront/api/v1/resource/attributes/attributetypes/DI2E-SVCV4-A/attributecodes/";

		PostmanCollectionModel postmanCollectionModel = new PostmanCollectionModel();
		postmanCollectionModel.setId(id);
		postmanCollectionModel.setName("Bad Code Deletes");
		postmanCollectionModel.setTimestamp(TimeUtil.currentDate());

		for (int i = 1; i < 380; i++) {
			String code = StringUtils.leftPad("" + i, 4, "0");
			PostmanRequest postmanRequest = new PostmanRequest();
			postmanRequest.setCollectionId(postmanCollectionModel.getId());
			postmanRequest.setId(UUID.randomUUID().toString());
			postmanRequest.setName("Delete: " + code);
			postmanRequest.setMethod("DELETE");
			postmanRequest.setHeaders("Content-Type: application/json");
			postmanRequest.setDataMode("params");
			postmanRequest.setTimestamp(TimeUtil.currentDate());
			postmanRequest.setVersion(2);
			postmanRequest.setUrl(urlBase + code + "/force");
			postmanRequest.setDescription("Delete code:  " + code);
			postmanCollectionModel.getRequests().add(postmanRequest);
		}

		try (OutputStream out = new FileOutputStream(fileName)) {
			StringProcessor.defaultObjectMapper().writeValue(out, postmanCollectionModel);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private class PostmanCollectionModel
	{

		private String id;
		private String name;
		private Date timestamp;
		private List<PostmanRequest> requests = new ArrayList<>();

		public PostmanCollectionModel()
		{
		}

		public String getId()
		{
			return id;
		}

		public void setId(String id)
		{
			this.id = id;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public Date getTimestamp()
		{
			return timestamp;
		}

		public void setTimestamp(Date timestamp)
		{
			this.timestamp = timestamp;
		}

		public List<PostmanRequest> getRequests()
		{
			return requests;
		}

		public void setRequests(List<PostmanRequest> requests)
		{
			this.requests = requests;
		}

	}

	private class PostmanRequest
	{

		private String collectionId;
		private String id;
		private String name;
		private String description;
		private String url;
		private String method;
		private String headers;
		private List<String> data = new ArrayList<>();
		private String dataMode;
		private Date timestamp;
		private List<String> responses = new ArrayList<>();
		private int version;

		public PostmanRequest()
		{
		}

		public String getCollectionId()
		{
			return collectionId;
		}

		public void setCollectionId(String collectionId)
		{
			this.collectionId = collectionId;
		}

		public String getId()
		{
			return id;
		}

		public void setId(String id)
		{
			this.id = id;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getDescription()
		{
			return description;
		}

		public void setDescription(String description)
		{
			this.description = description;
		}

		public String getUrl()
		{
			return url;
		}

		public void setUrl(String url)
		{
			this.url = url;
		}

		public String getMethod()
		{
			return method;
		}

		public void setMethod(String method)
		{
			this.method = method;
		}

		public String getHeaders()
		{
			return headers;
		}

		public void setHeaders(String headers)
		{
			this.headers = headers;
		}

		public List<String> getData()
		{
			return data;
		}

		public void setData(List<String> data)
		{
			this.data = data;
		}

		public String getDataMode()
		{
			return dataMode;
		}

		public void setDataMode(String dataMode)
		{
			this.dataMode = dataMode;
		}

		public Date getTimestamp()
		{
			return timestamp;
		}

		public void setTimestamp(Date timestamp)
		{
			this.timestamp = timestamp;
		}

		public int getVersion()
		{
			return version;
		}

		public void setVersion(int version)
		{
			this.version = version;
		}

		public List<String> getResponses()
		{
			return responses;
		}

		public void setResponses(List<String> responses)
		{
			this.responses = responses;
		}

	}
}
