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
package edu.usu.sdl.openstorefront.usecase;

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.service.manager.model.ConnectionModel;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.Content;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.ContentBody;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.RepresentationStorage;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.Space;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.SpaceResults;
import edu.usu.sdl.openstorefront.service.manager.resource.ConfluenceClient;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class ConfluenceUseCase
{

	@Test
	public void confluenceConnect()
	{

		ConnectionModel connectionModel = new ConnectionModel();
		connectionModel.setUrl("https://confluence.di2e.net");
		connectionModel.setUsername(PropertiesManager.getValue(PropertiesManager.KEY_TOOLS_USER));
		connectionModel.setCredential(PropertiesManager.getValue(PropertiesManager.KEY_TOOLS_CREDENTIALS));

		try (ConfluenceClient client = new ConfluenceClient(connectionModel, null)) {
			SpaceResults results = client.getSpaces(0, 5);
			StringProcessor.printObject(results);
		}

	}

	@Test
	public void confluenceCreatePage()
	{
		ConnectionModel connectionModel = new ConnectionModel();
		connectionModel.setUrl("https://confluence.di2e.net");
		connectionModel.setUsername(PropertiesManager.getValue(PropertiesManager.KEY_TOOLS_USER));
		connectionModel.setCredential(PropertiesManager.getValue(PropertiesManager.KEY_TOOLS_CREDENTIALS));

		try (ConfluenceClient client = new ConfluenceClient(connectionModel, null)) {
			Content content = new Content();
			content.setTitle("New Test Page");
			Space space = new Space();
			space.setKey("STORE");
			content.setSpace(space);

			ContentBody contentBody = new ContentBody();
			RepresentationStorage storage = new RepresentationStorage();
			storage.setValue("<h1>New Page from API</h1>");
			contentBody.setStorage(storage);

			Content savedContent = client.createPage(content);
			StringProcessor.printObject(savedContent);
		}

	}

}
