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
package edu.usu.sdl.openstorefront.web.test.integration;

import edu.usu.sdl.openstorefront.service.manager.JiraManager;
import edu.usu.sdl.openstorefront.service.manager.resource.JiraClient;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;

/**
 *
 * @author dshurtleff
 */
public class JiraIssueTypeTest
		extends BaseTestCase
{

	public JiraIssueTypeTest()
	{
		this.description = "Issue Type Test";
	}

	@Override
	protected void runInternalTest()
	{
		try (JiraClient jiraClient = JiraManager.getClient()) {
			jiraClient.getIssueTypesForProject("ASSET").forEach(item -> {
				results.append("Type: ").append(item).append("<br>");
			});
		}
	}

}
