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

import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.CimFieldInfo;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.ServerInfo;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.FeedbackTicket;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.view.JiraIssueView;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.core.view.RestErrorModel;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.security.UserContext;
import edu.usu.sdl.openstorefront.service.manager.JiraManager;
import edu.usu.sdl.openstorefront.service.manager.model.JiraFieldInfoModel;
import edu.usu.sdl.openstorefront.service.manager.model.JiraIssueModel;
import edu.usu.sdl.openstorefront.service.manager.model.JiraIssueType;
import edu.usu.sdl.openstorefront.service.manager.model.JiraStats;
import edu.usu.sdl.openstorefront.service.manager.resource.JiraClient;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
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

	private static final Logger log = Logger.getLogger(JiraService.class.getName());

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_SYSTEM_MANAGEMENT)
	@APIDescription("Get status on the jira resource manager")
	@DataType(JiraStats.class)
	@Path("/stats")
	public Response getJiraManagerStatus()
	{
		JiraStats stats = new JiraStats();
		try (JiraClient jiraClient = JiraManager.getClient()) {
			ServerInfo serverInfo = jiraClient.getServerInfo();
			stats.popluateServerInfo(serverInfo);
		} catch (Exception e) {
			log.log(Level.FINER, "Jira Error", e);
			stats.setServerTitle("Unable to connect to JIRA.  Check connection setting and network.");
		}
		stats.setMaxConnections(JiraManager.getMaxConnections());
		stats.setRemainingConnections(JiraManager.getAvavilableConnections());
		return sendSingleEntityResponse(stats);
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Gets the possible projects from Jira.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@Path("/projects")
	public Response getJiraProjects()
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
			GenericEntity<List<LookupModel>> entity = new GenericEntity<List<LookupModel>>(lookupModels)
			{
			};
			return sendSingleEntityResponse(entity);
		} catch (Exception e) {
			log.log(Level.FINER, "Jira Error", e);
			RestErrorModel restErrorModel = new RestErrorModel();
			restErrorModel.setSuccess(false);
			restErrorModel.getErrors().put("projects", "Unable  to connect to jira.");
			return sendSingleEntityResponse(restErrorModel);
		}
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Gets the possible issues from a specific project in Jira.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(JiraIssueModel.class)
	@Path("/projects/{projectCode}")
	public Response getJiraIssueTypes(
			@PathParam("projectCode")
			@RequiredParam String code
	)
	{
		List<JiraIssueModel> jiraIssueModels;
		try (JiraClient jiraClient = JiraManager.getClient()) {
			jiraIssueModels = jiraClient.getIssueTypesForProject(code);

			GenericEntity<List<JiraIssueModel>> entity = new GenericEntity<List<JiraIssueModel>>(jiraIssueModels)
			{
			};
			return sendSingleEntityResponse(entity);
		} catch (Exception e) {
			log.log(Level.FINER, "Jira Error", e);
			RestErrorModel restErrorModel = new RestErrorModel();
			restErrorModel.setSuccess(false);
			restErrorModel.getErrors().put("projectCode", "Unable to find issues types for project or issues trying to connect to jira.");
			return sendSingleEntityResponse(restErrorModel);
		}
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Gets the issue ticket summary")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(String.class)
	@Path("/ticket/{ticketId}")
	public Response getJiraTicketSummary(
			@PathParam("ticketId")
			@RequiredParam String ticketId
	)
	{
		try (JiraClient jiraClient = JiraManager.getClient()) {
			Issue issue = jiraClient.getTicket(ticketId);
			if (issue != null) {
				return Response.ok(issue.getSummary()).build();
			} else {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
		} catch (Exception e) {
			log.log(Level.FINER, "Jira Error", e);
			RestErrorModel restErrorModel = new RestErrorModel();
			restErrorModel.setSuccess(false);
			restErrorModel.getErrors().put("ticketId", "Unable to get jira ticket information.");
			return sendSingleEntityResponse(restErrorModel);
		}
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Gets the possible fields from the issue type.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(JiraFieldInfoModel.class)
	@Path("/projects/{projectCode}/{issueType}/fields")
	public Response getJiraIssueTypes(
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
			GenericEntity<List<JiraFieldInfoModel>> entity = new GenericEntity<List<JiraFieldInfoModel>>(jiraFieldInfoModels)
			{
			};
			return sendSingleEntityResponse(entity);
		} catch (Exception e) {
			log.log(Level.FINER, "Jira Error", e);
			RestErrorModel restErrorModel = new RestErrorModel();
			restErrorModel.setSuccess(false);
			restErrorModel.getErrors().put("issuefIelds", "Unable to retrieve issue fields.  Check project code, issue type, and jira connection");
			return sendSingleEntityResponse(restErrorModel);
		}
	}

	@POST
	@APIDescription("Submit Feedback Ticket")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(JiraIssueView.class)
	@Path("/submitticket")
	public Response submitTicket(
			FeedbackTicket feedbackTicket
	)
	{
		ValidationModel validationModel = new ValidationModel(feedbackTicket);
		validationModel.setConsumeFieldsOnly(true);

		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {

			try (JiraClient jiraClient = JiraManager.getClient()) {

				UserContext userContext = SecurityUtil.getUserContext();
				if (userContext != null) {
					UserProfile userProfile = userContext.getUserProfile();
					feedbackTicket.setFirstname(userProfile.getFirstName());
					feedbackTicket.setLastname(userProfile.getLastName());
					feedbackTicket.setOrganization(userProfile.getOrganization());
					feedbackTicket.setEmail(userProfile.getEmail());
					feedbackTicket.setPhone(userProfile.getPhone());
					feedbackTicket.setUsername(userProfile.getUsername());
				}

				JiraIssueView jiraIssueView = null;
				BasicIssue issue = jiraClient.submitTicket(feedbackTicket);
				if (issue != null) {
					jiraIssueView = new JiraIssueView();
					jiraIssueView.setTicketId(issue.getKey());
					jiraIssueView.setId(Long.toString(issue.getId()));
					jiraIssueView.setUrl(issue.getSelf().toString());
				}
				return sendSingleEntityResponse(jiraIssueView);
			}
		} else {
			return sendSingleEntityResponse(validationResult.toRestError());
		}
	}

}
