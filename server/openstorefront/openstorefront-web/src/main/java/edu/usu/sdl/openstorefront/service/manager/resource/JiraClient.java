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
package edu.usu.sdl.openstorefront.service.manager.resource;

import com.atlassian.jira.rest.client.GetCreateIssueMetadataOptions;
import com.atlassian.jira.rest.client.GetCreateIssueMetadataOptionsBuilder;
import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.domain.BasicProject;
import com.atlassian.jira.rest.client.domain.CimFieldInfo;
import com.atlassian.jira.rest.client.domain.CimProject;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.ServerInfo;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.service.manager.JiraManager;
import edu.usu.sdl.openstorefront.service.manager.model.ConnectionModel;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents Client
 *
 * @author dshurtleff
 */
public class JiraClient
		implements AutoCloseable
{

	private static final Logger log = Logger.getLogger(JiraClient.class.getName());

	private final ConnectionModel connectionModel;
	private JiraRestClient restClient;
	private boolean alive = true;

	public JiraClient(ConnectionModel connectionModel)
	{
		this.connectionModel = connectionModel;
		AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		try {
			restClient = factory.createWithBasicHttpAuthentication(new URI(connectionModel.getUrl()), connectionModel.getUsername(), connectionModel.getCredential());
		} catch (URISyntaxException ex) {
			throw new OpenStorefrontRuntimeException("Jira Server url is mal-formed ", "Check server url in properties.", ex);
		}
	}

	private JiraRestClient getRestClient()
	{
		if (alive) {
			return restClient;
		} else {
			throw new OpenStorefrontRuntimeException("Jira client has been closed.", "Obtain a new client from Manager.");
		}
	}

	public Issue getTicket(String ticket)
	{

		return getRestClient().getIssueClient().getIssue(ticket).claim();
	}

	public Iterable<CimProject> getAllProjectMetaInformation(String projectKey, String issueTypeName)
	{
		GetCreateIssueMetadataOptions options = new GetCreateIssueMetadataOptionsBuilder()
				.withProjectKeys(projectKey)
				.withExpandedIssueTypesFields()
				.withIssueTypeNames(issueTypeName)
				.build();
		Iterable<CimProject> cimProjects = getRestClient().getIssueClient().getCreateIssueMetadata(options).claim();
		return cimProjects;
	}

	public Map<String, CimFieldInfo> getProjectIssueTypeFields(String projectKey, String issueTypeName)
	{
		Map<String, CimFieldInfo> fields = new HashMap<>();
		Iterable<CimProject> cimProjects = getAllProjectMetaInformation(projectKey, issueTypeName);
		cimProjects.forEach(project -> {
			project.getIssueTypes().forEach(type -> {
				fields.putAll(type.getFields());
			});
		});
		return fields;
	}

	public Iterable<BasicProject> getAllProjects()
	{
		Iterable<BasicProject> projects = getRestClient().getProjectClient().getAllProjects().claim();
		return projects;
	}

	public List<String> getIssueTypesForProject(String projectKey)
	{
		List<String> issueTypeNames = new ArrayList<>();
		GetCreateIssueMetadataOptions options = new GetCreateIssueMetadataOptionsBuilder()
				.withProjectKeys(projectKey)
				.build();
		Iterable<CimProject> cimProjects = getRestClient().getIssueClient().getCreateIssueMetadata(options).claim();
		cimProjects.forEach(project -> {
			project.getIssueTypes().forEach(type -> {
				issueTypeNames.add(type.getName());
			});
		});
		return issueTypeNames;
	}

	public ServerInfo getServerInfo()
	{
		ServerInfo serverInfo = getRestClient().getMetadataClient().getServerInfo().claim();
		return serverInfo;
	}

	@Override
	public void close()
	{
		log.log(Level.FINEST, "Releasing jira connection.");
		alive = false;
		JiraManager.releaseClient(this);
	}

	public boolean isAlive()
	{
		return alive;
	}

}
