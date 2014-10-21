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

import com.atlassian.jira.rest.client.domain.Issue;
import edu.usu.sdl.openstorefront.service.manager.JiraManager;
import edu.usu.sdl.openstorefront.service.manager.resource.JiraClient;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;

/**
 *
 * @author dshurtleff
 */
public class JiraIssueTest
		extends BaseTestCase
{

	public JiraIssueTest()
	{
		this.description = "Jira Issue Test";
	}

	@Override
	protected void runInternalTest()
	{
		String issueKey = "ASSET-151";
		results.append("Pulling issue:  ").append(issueKey).append("<br>");
		try (JiraClient jiraClient = JiraManager.getClient()) {
			Issue issue = jiraClient.getTicket(issueKey);
			if (issue != null) {
				results.append(issue.getSummary()).append("<br>");
				results.append(issue.getDescription()).append("<br>");
			} else {
				failureReason.append("Unable to get jira ticket");
			}
		}
	}

}
