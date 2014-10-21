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

import com.atlassian.jira.rest.client.domain.CimFieldInfo;
import com.atlassian.jira.rest.client.domain.CimProject;
import edu.usu.sdl.openstorefront.service.manager.JiraManager;
import edu.usu.sdl.openstorefront.service.manager.resource.JiraClient;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;

/**
 *
 * @author dshurtleff
 */
public class JiraMetadataTest
		extends BaseTestCase
{

	public JiraMetadataTest()
	{
		this.description = "Jira Metadata test";
	}

	@Override
	protected void runInternalTest()
	{
		String projectKey = "ASSET";
		String issueKey = "Asset";

		results.append("Pulling metadata for:  ").append(projectKey).append(" : ").append(issueKey).append("<br>");
		try (JiraClient jiraClient = JiraManager.getClient()) {
			Iterable<CimProject> projects = jiraClient.getAllProjectMetaInformation(projectKey, issueKey);
			if (projects != null) {
				projects.forEach(project -> {
					results.append("Project:").append("</br>");
					results.append(project.getKey()).append("</br>");
					results.append(project.getName()).append("</br>");
					results.append(project.getSelf()).append("</br><br>");
					results.append("Types:").append("</br>");
					project.getIssueTypes().forEach(type -> {
						results.append(type.getName()).append("</br>");
						results.append(type.getDescription()).append("</br>");
						for (String fieldKey : type.getFields().keySet()) {
							CimFieldInfo fieldInfo = type.getFields().get(fieldKey);
							results.append(fieldKey).append("</br>");
							results.append(fieldInfo.getName()).append("</br>");
							results.append(fieldInfo.getId()).append("</br>");
						}
					});
				});

			} else {
				failureReason.append("Unable to get jira ticket");
			}
		}
	}

}
