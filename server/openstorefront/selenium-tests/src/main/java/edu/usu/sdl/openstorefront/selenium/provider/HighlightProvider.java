/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.selenium.provider;

import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.apiclient.rest.resource.HighlightClient;
import edu.usu.sdl.openstorefront.core.entity.Highlight;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class HighlightProvider
{

	HighlightClient client;
	private List<String> highlightIds;

	public HighlightProvider(ClientAPI apiClient)
	{
		client = new HighlightClient(apiClient);
		highlightIds = new ArrayList<>();
	}

	public Highlight createHighlight(String highlightName)
	{
		Highlight highlight = new Highlight();
		highlight.setTitle(highlightName);
		highlight.setDescription("Selenium highlight description!");
		highlight.setHighlightType(Highlight.TYPE_COMPONENT);
		highlight.setOrderingPosition(1);

		Highlight highlightAPI = client.postHighlight(highlight);
		highlightIds.add(highlightAPI.getHighlightId());

		return highlightAPI;
	}

	public void cleanup()
	{
		for (String id : highlightIds) {
			
			client.deleteHighlight(id);
		}
	}

}
