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
package edu.usu.sdl.apiclient;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.BooleanNode;
import edu.usu.sdl.openstorefront.core.view.RestErrorModel;
import java.io.IOException;

/**
 *
 * @author ccummings
 */
public class RestErrorModelDeserializer
		extends StdDeserializer<RestErrorModel>
{

	public RestErrorModelDeserializer()
	{
		this(null);
	}

	public RestErrorModelDeserializer(Class<RestErrorModel> rem)
	{
		super(rem);
	}

	@Override
	public RestErrorModel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException
	{
		RestErrorModel errorModel = new RestErrorModel();
		JsonNode node = jp.getCodec().readTree(jp);
		boolean success = ((BooleanNode) node.get("success")).booleanValue();
		errorModel.setSuccess(success);

		for (JsonNode n : node.findValues("errors")) {
			
			String key = n.findValue("key").asText();
			String value = n.findValue("value").asText();
			errorModel.getErrors().putIfAbsent(key, value);
		}

		return errorModel;
	}

}
