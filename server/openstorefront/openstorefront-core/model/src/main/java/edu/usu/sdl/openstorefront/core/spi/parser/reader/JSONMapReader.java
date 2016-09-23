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
package edu.usu.sdl.openstorefront.core.spi.parser.reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.MapField;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.MapModel;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class JSONMapReader
		extends MappableReader
{

	private MapModel rootModel;
	private List<MapModel> records = new ArrayList<>();
	private Iterator<MapModel> recordIterator;

	public JSONMapReader(InputStream in)
	{
		super(in);
	}

	@Override
	public void preProcess()
	{
		rootModel = findFields(in);
		totalRecords = rootModel.getArrayFields().size();
		records.add(rootModel);
		recordIterator = records.iterator();
	}

	@Override
	public MapModel nextRecord()
	{
		if (recordIterator.hasNext()) {
			MapModel mapModel = recordIterator.next();
			currentRecordNumber += mapModel.getArrayFields().size();
			return mapModel;
		} else {
			return null;
		}
	}

	@Override
	public MapModel findFields(InputStream in)
	{
		MapModel mapModel = new MapModel();
		mapModel.setName("root");

		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();
		try (InputStream jsonIn = in) {
			JsonNode jsonNode = objectMapper.readTree(jsonIn);
			if (jsonNode != null
					&& jsonNode.isArray()) {
				ArrayNode arrayNode = (ArrayNode) jsonNode;
				for (JsonNode childNode : arrayNode) {
					parseTree(childNode, mapModel, "");
				}
			} else {
				parseTree(jsonNode, mapModel, "");
			}
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to open json file.", " Check file and format", ex);
		}

		return mapModel;
	}

	private void parseTree(JsonNode root, MapModel mapModel, String parent)
	{
		if (root == null) {
			return;
		}

		Iterator<Map.Entry<String, JsonNode>> fieldIterator = root.fields();
		while (fieldIterator.hasNext()) {
			Entry<String, JsonNode> entry = fieldIterator.next();

			if (entry.getValue().isObject()) {
				String newParent = parent;
				if (StringUtils.isNotBlank(parent)) {
					newParent = parent + "." + entry.getKey();
				} else {
					newParent = entry.getKey();
				}
				MapModel childModel = new MapModel();
				childModel.setName(newParent);
				mapModel.getArrayFields().add(childModel);

				parseTree(entry.getValue(), childModel, newParent);
			} else if (entry.getValue().isArray()) {
				String newParent = parent;
				if (StringUtils.isNotBlank(parent)) {
					newParent = parent + "." + entry.getKey();
				} else {
					newParent = entry.getKey();
				}
				if (entry.getValue() instanceof ArrayNode) {
					ArrayNode arrayNode = (ArrayNode) entry.getValue();
					for (JsonNode childNode : arrayNode) {
						parseTree(childNode, mapModel, newParent);
					}
				}
			} else {
				MapField mapField = new MapField();

				if (StringUtils.isNotBlank(parent)) {
					mapField.setName(parent + "." + entry.getKey());
				} else {
					mapField.setName(entry.getKey());
				}
				mapField.setValue(entry.getValue().asText());
				mapModel.getMapFields().add(mapField);
			}
		}

	}

}
