/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.web.test.integration;

import edu.usu.sdl.openstorefront.service.manager.ConfluenceManager;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.Content;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.ContentBody;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.ContentVersion;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.Label;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.RepresentationStorage;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.Space;
import edu.usu.sdl.openstorefront.service.manager.resource.ConfluenceClient;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ConfluenceTest
		extends BaseTestCase
{

	public static final String PAGE_TITLE = "AUTO-TEST-PAGE";
	public static final String SPACE_KEY = "STORE";

	private String contentId;

	@Override
	public String getDescription()
	{
		return "Confluence Create Test";
	}

	@Override
	protected void runInternalTest()
	{
		//check for old data
		addResultsLines("Checking for old page");
		try (ConfluenceClient client = ConfluenceManager.getPoolInstance().getClient()) {
			Content content = client.getPage(SPACE_KEY, PAGE_TITLE);
			if (content != null) {
				addResultsLines("Found old Page");
				deletePage(content.getId());
			}

			//create page
			addResultsLines("Create Test Page");
			Content newContent = new Content();

			newContent.setTitle(PAGE_TITLE);
			Space space = new Space();
			space.setKey(SPACE_KEY);
			newContent.setSpace(space);

			ContentBody contentBody = new ContentBody();
			RepresentationStorage storage = new RepresentationStorage();
			storage.setValue("<h1>New Test Page from API</h1>");
			contentBody.setStorage(storage);
			newContent.setBody(contentBody);

			Content savedContent = client.createPage(newContent);
			contentId = savedContent.getId();
			addResultsLines("Posted Page");

			addResultsLines("Updating Page");
			newContent.setId(savedContent.getId());
			newContent.getBody().getStorage().setValue("<h2>Update Content</h2>");
			ContentVersion version = new ContentVersion();
			version.setNumber(2);
			newContent.setVersion(version);
			client.updatePage(newContent);
			addResultsLines("Updated Page");

			addResultsLines("Add Label to Page");
			List<Label> labels = new ArrayList<>();
			Label label = new Label();
			label.setName("TEST");
			labels.add(label);
			client.addLabel(savedContent.getId(), labels);

			addResultsLines("Delete Label");
			client.deleteLabel(savedContent.getId(), "TEST");

			addResultsLines("Checking to see if page posted/updated");
			Content foundContent = client.getPage(SPACE_KEY, PAGE_TITLE);
			if (foundContent != null) {
				if (foundContent.getBody().getView().getValue().contains("Update")) {
					addResultsLines("Found Page and it was updated");
				} else {
					addFailLines("Found page.  Not able to determine update status");
				}
			} else {
				addFailLines("Unable to find test page on confluence");
			}

		}
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();

		//clean up new page
		if (StringUtils.isNotBlank(contentId)) {
			deletePage(contentId);
		}
	}

	private void deletePage(String contentId)
	{
		try (ConfluenceClient client = ConfluenceManager.getPoolInstance().getClient()) {
			if (client.deletePage(contentId)) {
				addResultsLines("Deleted Test Page");
			}
		}
	}

}
