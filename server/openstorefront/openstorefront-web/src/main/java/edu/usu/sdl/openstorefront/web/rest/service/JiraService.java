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
package edu.usu.sdl.openstorefront.web.rest.service;

import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.CimFieldInfo;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.ServerInfo;
import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.doc.RequireAdmin;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.service.manager.JiraManager;
import edu.usu.sdl.openstorefront.service.manager.model.JiraFieldInfoModel;
import edu.usu.sdl.openstorefront.service.manager.model.JiraIssueModel;
import edu.usu.sdl.openstorefront.service.manager.model.JiraIssueType;
import edu.usu.sdl.openstorefront.service.manager.resource.JiraClient;
import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;
import edu.usu.sdl.openstorefront.web.viewmodel.JiraStats;
import edu.usu.sdl.openstorefront.web.viewmodel.LookupModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
@Path("v1/service/jira")
@APIDescription("Provides jira integration related information")
public class JiraService
		extends BaseResource
{

	@GET
	@RequireAdmin
	@APIDescription("Get status on the jira resource manager")
	@DataType(JiraStats.class)
	@Path("/stats")
	public Response getJiraManagerStatus()
	{
		JiraStats stats = new JiraStats();
		try (JiraClient jiraClient = JiraManager.getClient()) {
			ServerInfo serverInfo = jiraClient.getServerInfo();
			stats.setServerInfo(serverInfo);
		}
		stats.setMaxConnections(JiraManager.getMaxConnections());
		stats.setRemainingConnections(JiraManager.getAvavilableConnections());
		return sendSingleEntityResponse(stats);
	}

	@GET
	@RequireAdmin
	@APIDescription("Gets the possible projects from Jira.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@Path("/projects")
	public List<LookupModel> getJiraProjects()
	{
		List<LookupModel> lookupModels = new ArrayList<>();
		try (JiraClient jiraClient = JiraManager.getClient()) {
			Iterable<BasicProject> projects = jiraClient.getAllProjects();
			for (BasicProject project : projects) {
				LookupModel lookupModel = new LookupModel();
				lookupModel.setDescription(project.getName());
				lookupModel.setCode(project.getKey());
				lookupModels.add(lookupModel);
			}
		}
		return lookupModels;
	}

	@GET
	@RequireAdmin
	@APIDescription("Gets the possible issues from a specific project in Jira.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(String.class)
	@Path("/projects/{projectCode}")
	public List<JiraIssueModel> getJiraIssueTypes(
			@PathParam("projectCode")
			@RequiredParam String code
	)
	{
		List<JiraIssueModel> jiraIssueModels;
		try (JiraClient jiraClient = JiraManager.getClient()) {
			jiraIssueModels = jiraClient.getIssueTypesForProject(code);
		}
		return jiraIssueModels;
	}

	@GET
	@RequireAdmin
	@APIDescription("Gets the possible issues from a specific project in Jira.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(String.class)
	@Path("/getTicket/{ticketId}")
	public Response getJiraTicket(
			@PathParam("ticketId")
			@RequiredParam String ticketId
	)
	{
		LookupModel response = new LookupModel();
		response.setDescription(ticketId);
		try (JiraClient jiraClient = JiraManager.getClient()) {
			Issue issue = jiraClient.getTicket(ticketId);
			if (issue != null) {
				return Response.ok(issue.getSummary()).build();
			} else {
				return Response.ok(response).build();
			}
		}
	}

	@GET
	@RequireAdmin
	@APIDescription("Gets the possible fields from the issue type.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(JiraFieldInfoModel.class)
	@Path("/projects/{projectCode}/{issueType}/fields")
	public List<JiraFieldInfoModel> getJiraIssueTypes(
			@PathParam("projectCode")
			@RequiredParam String projectCode,
			@PathParam("issueType")
			@RequiredParam String issueType
	)
	{
		List<JiraFieldInfoModel> jiraFieldInfoModels = new ArrayList<>();
		Map<String, CimFieldInfo> results;
		List<JiraIssueType> statuses;
		JiraIssueType jiraIssueType = null;
		try (JiraClient jiraClient = JiraManager.getClient()) {
			results = jiraClient.getProjectIssueTypeFields(projectCode, issueType);
			if (results != null) {
				for (Map.Entry<String, CimFieldInfo> entry : results.entrySet()) {
					String key = entry.getKey();
					CimFieldInfo cimFieldInfo = entry.getValue();
					if (cimFieldInfo.getAllowedValues() != null) {
						JiraFieldInfoModel model = JiraFieldInfoModel.toView(key, cimFieldInfo);
						if (model != null) {
							jiraFieldInfoModels.add(model);
						}
					}
				}
			}
			statuses = jiraClient.getProjectStatusForAllIssueTypes(projectCode);
			for (int i = 0; i < statuses.size(); i++) {
				if (statuses.get(i).getName().equals(issueType)) {
					jiraIssueType = statuses.get(i);
					break;
				}
			}
			if (jiraIssueType != null) {
				jiraFieldInfoModels.add(JiraFieldInfoModel.toView(jiraIssueType));
			}
		}
		return jiraFieldInfoModels;
	}

}
