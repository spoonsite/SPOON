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

package edu.usu.sdl.openstorefront.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ServiceUtil
{
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public static ObjectMapper defaultObjectMapper()
	{
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		return objectMapper;
	}
	
	public static String getResourceNameFromUrl(String url)
	{
		String resource = url;
		if (StringUtils.isNotBlank(url))
		{
			resource = url.substring(url.lastIndexOf("/") + 1, url.length());
		}
		return resource;
	}
	
	public static List<String> extractUrls(String text)
	{
		List<String> urls = new ArrayList<>();
		
		String tokens[] = text.split(" ");
		for (String token : tokens)
		{
			if (token.trim().toLowerCase().startsWith("http://") ||
			    token.trim().toLowerCase().startsWith("https://"))
			{
				urls.add(token.trim());
			}
		}
		
		return urls;
	}
	
	public static String createHrefUrls(String text)
	{
		String replacedText = text;
		List<String> urls = extractUrls(text);		
		for (String url : urls)
		{
			String link = "<a href='" + url + "' title='" + url + "' target='_blank'> " + getResourceNameFromUrl(url) + "</a>";
			 replacedText = replacedText.replace(url, link);
		}		
		return replacedText;
	}
	
	public static String stripeFieldJSON(String json, Set<String> fieldsToKeep)
	{
		ObjectMapper mapper = defaultObjectMapper();
		
		try
		{
			JsonNode rootNode = mapper.readTree(json);			
			processNode(rootNode, fieldsToKeep);
			
			 Object jsonString = mapper.readValue(rootNode.toString(), Object.class);			 
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonString);
		} catch (IOException ex)
		{
			throw new OpenStorefrontRuntimeException(ex);
		}		
	}
	
	/**
	 * This only goes down one level
	 * @param rootNode
	 * @param fieldsToKeep 
	 */
	private static void processNode(JsonNode rootNode, Set<String> fieldsToKeep)
	{
		if (rootNode instanceof ObjectNode)
		{
			ObjectNode object = (ObjectNode) rootNode;
			object.retain(fieldsToKeep);
		}
		else
		{
			for (JsonNode childNode : rootNode) 
			{
				processNode(childNode, fieldsToKeep);
			}
		}		
	}
	
	public static String printObject(Object o)
	{
		StringBuilder sb = new StringBuilder();
		
		if (o != null)
		{
			try
			{
				Map fieldMap = BeanUtils.describe(o);
				fieldMap.keySet().stream().forEach((key) ->
				{
					sb.append(key).append(" = ").append(fieldMap.get(key)).append("\n");										
				});
			}
			catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex)
			{
				Logger.getLogger(ServiceUtil.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		else
		{
			sb.append(o);
		}
		
		return sb.toString();
	}
	
}
