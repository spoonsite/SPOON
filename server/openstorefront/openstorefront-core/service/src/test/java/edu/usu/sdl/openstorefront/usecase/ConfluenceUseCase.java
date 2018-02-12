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
import edu.usu.sdl.openstorefront.service.manager.model.confluence.ContentVersion;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.RepresentationStorage;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.Space;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.SpaceResults;
import edu.usu.sdl.openstorefront.service.manager.resource.ConfluenceClient;
import java.io.IOException;
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
		connectionModel.setUsername(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_TOOLS_USER));
		connectionModel.setCredential(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_TOOLS_CREDENTIALS));

		try (ConfluenceClient client = new ConfluenceClient(connectionModel, null)) {
			SpaceResults results = client.getSpaces(0, 5);
			StringProcessor.printObject(results);
		}

	}

	@Test
	public void confluenceCreatePage() throws IOException
	{
		ConnectionModel connectionModel = new ConnectionModel();
		connectionModel.setUrl("https://confluence.di2e.net");
		connectionModel.setUsername(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_TOOLS_USER));
		connectionModel.setCredential(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_TOOLS_CREDENTIALS));

		try (ConfluenceClient client = new ConfluenceClient(connectionModel, null)) {
			Content content = new Content();
			content.setTitle("New Test Page");
			Space space = new Space();

			//change to personal space (make sure to allow permission for the integration user
			space.setKey("STORE");
			content.setSpace(space);

			ContentBody contentBody = new ContentBody();
			RepresentationStorage storage = new RepresentationStorage();

//			List<String> lines = Files.readAllLines(Paths.get("/test/conf.txt"));
//
//			StringJoiner sj = new StringJoiner("\n");
//			lines.forEach(line -> {
//				sj.add(line);
//			});
			storage.setValue(
					"<ac:structured-macro ac:name=\"ui-expand\">"
					+ "<ac:parameter ac:name=\"title\">My Title</ac:parameter>"
					+ "<ac:parameter ac:name=\"expanded\">false</ac:parameter>"
					+ "<ac:rich-text-body><b>Hello</b> World! </ac:rich-text-body>"
					+ "</ac:structured-macro>"
			//sj.toString()
			);

			contentBody.setStorage(storage);
			content.setBody(contentBody);

			Content savedContent = client.createPage(content);
			System.out.println(StringProcessor.printObject(savedContent));
//
			ContentVersion version = new ContentVersion();
			version.setNumber(2);
			content.setVersion(version);
			content.setId(savedContent.getId());
			content.getBody().getStorage().setValue("Updated");
			client.updatePage(content);

			//update
			client.deletePage(savedContent.getId());
		}

	}

	@Test
	public void confluenceGetPage()
	{
		ConnectionModel connectionModel = new ConnectionModel();
		connectionModel.setUrl("https://confluence.di2e.net");
		connectionModel.setUsername(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_TOOLS_USER));
		connectionModel.setCredential(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_TOOLS_CREDENTIALS));

		try (ConfluenceClient client = new ConfluenceClient(connectionModel, null)) {
			Content content = client.getPage("~devin.shurtleff", "MTest");
			System.out.println(StringProcessor.printObject(content));
			if (content.getVersion() != null) {
				System.out.println("Version: " + content.getVersion().getNumber());
			}
		}

	}

}
