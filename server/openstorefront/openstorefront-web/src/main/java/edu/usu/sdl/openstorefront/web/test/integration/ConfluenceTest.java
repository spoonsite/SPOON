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
import edu.usu.sdl.openstorefront.service.manager.resource.ConfluenceClient;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class ConfluenceTest
		extends BaseTestCase
{

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

		//delete if exists
		//create page
		//update page
		//add label
		//delete label
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
		ConfluenceClient client = ConfluenceManager.getPoolInstance().getClient();
		if (client.deletePage(contentId)) {
			results.append("Deleted Test Page");
		}
	}

}
