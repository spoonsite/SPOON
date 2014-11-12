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
import edu.usu.sdl.openstorefront.service.manager.model.JiraIssueType;
import edu.usu.sdl.openstorefront.service.manager.resource.JiraClient;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class JiraIssueStatusTest
		extends BaseTestCase
{

	public JiraIssueStatusTest()
	{
		this.description = "Issue Status Test";
	}

	@Override
	protected void runInternalTest()
	{
		results.append("Pulling issue staus:  ").append("ASSET").append("<br>");
		try (JiraClient jiraClient = JiraManager.getClient()) {
			List<JiraIssueType> issueTypes = jiraClient.getProjectStatusForAllIssueTypes("ASSET");
			issueTypes.forEach(type -> {
				results.append(type.getName()).append("<br>");
				type.getStatuses().forEach(status -> {
					results.append(" status: ").append(status.getName()).append("<br>");
				});
			});
		}
	}

}
