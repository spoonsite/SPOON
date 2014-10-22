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
package edu.usu.sdl.openstorefront.web.rest.resource;

import com.atlassian.jira.rest.client.domain.BasicProject;
import com.atlassian.jira.rest.client.domain.CimFieldInfo;
import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.doc.RequireAdmin;
import edu.usu.sdl.openstorefront.doc.RequiredParam;
import edu.usu.sdl.openstorefront.service.manager.model.JiraIssueModel;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.Integration;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.rest.model.GlobalIntegrationModel;
import edu.usu.sdl.openstorefront.web.viewmodel.LookupModel;
import java.net.URI;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jlaw
 */
@Path("v1/resource/integration")
@APIDescription("Integration models are resources used to connect component information to external sources.")
public class IntegrationResource
		extends BaseResource
{

	@GET
	@APIDescription("Gets all integration models from the database.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Component.class)
	public List<Integration> getIntegrations()
	{
		List<Integration> integrationModels = service.getSystemService().getIntegrationModels();
		return integrationModels;
	}

	@GET
	@APIDescription("Gets the global model from the database.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Component.class)
	@Path("/global")
	public Response getGlobalConfig()
	{
		GlobalIntegrationModel model = service.getSystemService().getGlobalConfig();
		if (model != null) {
			return Response.ok(model).build();
		} else {
			return Response.serverError().build();
		}
	}

	@POST
	@RequireAdmin
	@APIDescription("Save an integration model")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveIntegration(
		@RequiredParam Integration integration)
			
	{
		ValidationModel validationModel = new ValidationModel(integration);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			return Response.created(URI.create("v1/resource/components/" + service.getSystemService().saveIntegration(integration, true).getComponentId())).entity(integration).build();
		}
		else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@POST
	@RequireAdmin
	@APIDescription("Save a global integration model")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/global")
	public Response saveGlobal(
		@RequiredParam GlobalIntegrationModel integration)
	{
		ValidationModel validationModel = new ValidationModel(integration);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			return Response.created(URI.create("v1/resource/components/" + service.getSystemService().saveIntegration(integration, true).getJiraRefreshRate())).entity(integration).build();
		}
		else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@RequireAdmin
	@APIDescription("Update an integration model")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response updateComponentConfig(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam Integration integration
	)
	{
		ValidationModel validationModel = new ValidationModel(integration);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			return Response.created(URI.create("v1/resource/components/" + service.getSystemService().saveIntegration(integration, false).getComponentId())).entity(integration).build();
		}
		else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}
	
	@PUT
	@RequireAdmin
	@APIDescription("Updates a global integration model")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/global")
	public Response updateGlobalConfig(
			@PathParam("id")
			@RequiredParam String componentId,
			@RequiredParam GlobalIntegrationModel integration
	)
	{
		ValidationModel validationModel = new ValidationModel(integration);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			return Response.created(URI.create("v1/resource/components/" + service.getSystemService().saveIntegration(integration, false).getJiraRefreshRate())).entity(integration).build();
		}
		else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Inactivates Component and removes any assoicated user watches.")
	@Path("/{id}")
	public void deleteComponentConfig(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		service.getSystemService().deactivateIntegration(componentId);
	}
	
	@DELETE
	@RequireAdmin
	@APIDescription("Inactivates Component and removes any assoicated user watches.")
	@Path("/global")
	public void deleteGlobalConfig()
	{
		service.getSystemService().deactivateIntegration();
	}
	
	@GET
	@APIDescription("Gets the possible projects from Jira.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@Path("/projects")
	public List<LookupModel> getJiraProjects()
	{
		return service.getSystemService().getAllJiraProjects();
	}
	
	@GET
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
	@APIDescription("Gets the possible fields from the issue type.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(String.class)
	@Path("/projects/{projectCode}/{issueType}/fields")
	public Map<String, CimFieldInfo> getJiraIssueTypes(
			@PathParam("projectCode")
			@RequiredParam String code,
			@PathParam("issueType")
			@RequiredParam String type
	)
	{
		return service.getSystemService().getIssueTypeFields(code, type);
	}
}
