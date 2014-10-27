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

import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.doc.RequireAdmin;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.service.manager.model.JiraFieldInfoModel;
import edu.usu.sdl.openstorefront.service.manager.model.JiraIssueModel;
import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;
import edu.usu.sdl.openstorefront.web.viewmodel.LookupModel;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
	@APIDescription("Gets the possible projects from Jira.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@Path("/projects")
	public List<LookupModel> getJiraProjects()
	{
		return service.getSystemService().getAllJiraProjects();
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
		return service.getSystemService().getAllProjectIssueTypes(code);
	}

	@GET
	@RequireAdmin
	@APIDescription("Gets the possible fields from the issue type.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(JiraFieldInfoModel.class)
	@Path("/projects/{projectCode}/{issueType}/fields")
	public List<JiraFieldInfoModel> getJiraIssueTypes(
			@PathParam("projectCode")
			@RequiredParam String code,
			@PathParam("issueType")
			@RequiredParam String type
	)
	{
		return service.getSystemService().getIssueTypeFields(code, type);
	}

}
