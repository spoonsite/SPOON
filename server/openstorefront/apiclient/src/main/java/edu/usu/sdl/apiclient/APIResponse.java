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
package edu.usu.sdl.apiclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.usu.sdl.openstorefront.core.view.RestErrorModel;
import java.io.IOException;

/**
 *
 * @author dshurtleff
 */
public class APIResponse
{

	private int responseCode;
	private String responseBody;
	private ObjectMapper objectMapper;

	public APIResponse(int responseCode, String responseBody, ObjectMapper objectMapper)
	{
		this(objectMapper);
		this.responseCode = responseCode;
		this.responseBody = responseBody;
	}

	public APIResponse(ObjectMapper objectMapper)
	{
		this.objectMapper = objectMapper;
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		SimpleModule module = new SimpleModule();
		module.addDeserializer(RestErrorModel.class, new RestErrorModelDeserializer());
		objectMapper.registerModule(module);
	}

	public int getResponseCode()
	{
		return responseCode;
	}

	public void setResponseCode(int responseCode)
	{
		this.responseCode = responseCode;
	}

	public String getResponseBody()
	{
		return responseBody;
	}

	public void setResponseBody(String responseBody)
	{
		this.responseBody = responseBody;
	}

	public <T extends Object> T getResponse(Class<T> valueType)
	{
		try {
			T dataModel = objectMapper.readValue(this.getResponseBody(), valueType);
			return dataModel;
		} catch (IOException ex) {
			throw new HandlingException(ex);
		}
	}

	public <T extends Object> T getList(TypeReference valueTypeRef)
	{
		try {
			T dataModel = objectMapper.readValue(this.getResponseBody(), valueTypeRef);
			return dataModel;
		} catch (IOException ex) {
			throw new HandlingException(ex);
		}
	}
}
