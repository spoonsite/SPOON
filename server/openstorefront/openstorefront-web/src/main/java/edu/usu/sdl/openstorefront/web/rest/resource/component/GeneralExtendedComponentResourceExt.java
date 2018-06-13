/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.rest.resource.component;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.QueryType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentIntegration;
import edu.usu.sdl.openstorefront.core.entity.ComponentIntegrationConfig;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.entity.ComponentVersionHistory;
import edu.usu.sdl.openstorefront.core.entity.RunStatus;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.model.ComponentRestoreOptions;
import edu.usu.sdl.openstorefront.core.view.ComponentDetailView;
import edu.usu.sdl.openstorefront.core.view.ComponentIntegrationView;
import edu.usu.sdl.openstorefront.core.view.ComponentTrackingWrapper;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.RestErrorModel;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.service.manager.JobManager;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
public abstract class GeneralExtendedComponentResourceExt
		extends ComponentQuestionReviewExt
{

	// <editor-fold defaultstate="collapsed" desc="Version history">
	@GET
	@APIDescription("Gets all version history for a component")
	@Produces(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
	@DataType(ComponentVersionHistory.class)
	@Path("/{id}/versionhistory")
	public Response getComponentVersionHistory(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		ComponentVersionHistory versionHistory = new ComponentVersionHistory();
		versionHistory.setActiveStatus(ComponentVersionHistory.ACTIVE_STATUS);
		versionHistory.setComponentId(componentId);

		List<ComponentVersionHistory> versionHistories = service.getPersistenceService().queryByExample(versionHistory);

		GenericEntity<List<ComponentVersionHistory>> entity = new GenericEntity<List<ComponentVersionHistory>>(versionHistories)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets a version history record")
	@Produces(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
	@DataType(ComponentVersionHistory.class)
	@Path("/{id}/versionhistory/{versionHistoryId}")
	public Response getComponentVersionHistoryRecord(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("versionHistoryId")
			@RequiredParam String versionHistoryId)
	{
		ComponentVersionHistory componentVersionHistory = new ComponentVersionHistory();
		componentVersionHistory.setVersionHistoryId(versionHistoryId);
		componentVersionHistory.setComponentId(componentId);
		componentVersionHistory = componentVersionHistory.find();
		return sendSingleEntityResponse(componentVersionHistory);
	}

	@GET
	@APIDescription("Gets the detail of a component version")
	@Produces(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
	@DataType(ComponentDetailView.class)
	@Path("/{id}/versionhistory/{versionHistoryId}/view")
	public Response viewComponentVerison(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("versionHistoryId")
			@RequiredParam String versionHistoryId)
	{
		ComponentDetailView componentDetailView = service.getComponentService().viewSnapshot(versionHistoryId);
		return sendSingleEntityResponse(componentDetailView);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
	@APIDescription("Create a version of the current component")
	@DataType(ComponentVersionHistory.class)
	@Path("/{id}/versionhistory")
	public Response snapshotComponent(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		Component component = new Component();
		component.setComponentId(componentId);
		component = component.find();
		if (component != null) {
			ComponentVersionHistory versionHistory = service.getComponentService().snapshotVersion(componentId, null);
			response = Response.created(URI.create("v1/resource/components/" + component.getComponentId())).entity(versionHistory).build();
		}
		return response;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
	@APIDescription("Restores a version of the current component")
	@DataType(ComponentVersionHistory.class)
	@Path("/{id}/versionhistory/{versionHistoryId}/restore")
	public Response restoreSnapshot(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("versionHistoryId")
			@RequiredParam String versionHistoryId,
			ComponentRestoreOptions options)
	{
		Response response = Response.status(Response.Status.NOT_FOUND).build();

		ComponentVersionHistory componentVersionHistory = new ComponentVersionHistory();
		componentVersionHistory.setVersionHistoryId(versionHistoryId);
		componentVersionHistory.setComponentId(componentId);
		componentVersionHistory = componentVersionHistory.find();
		if (componentVersionHistory != null) {
			if (options == null) {
				options = new ComponentRestoreOptions();
			}

			Component component = service.getComponentService().restoreSnapshot(versionHistoryId, options);
			response = sendSingleEntityResponse(component);
		}
		return response;
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_ENTRY_MANAGEMENT)
	@APIDescription("Delete a version history record")
	@Path("/{id}/versionhistory/{versionHistoryId}")
	public void deleteVersionHistory(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("versionHistoryId")
			@RequiredParam String versionHistoryId)
	{
		//confirm that the version belong to the component
		ComponentVersionHistory componentVersionHistory = new ComponentVersionHistory();
		componentVersionHistory.setVersionHistoryId(versionHistoryId);
		componentVersionHistory.setComponentId(componentId);
		componentVersionHistory = componentVersionHistory.find();
		if (componentVersionHistory != null) {
			service.getComponentService().deleteSnapshot(versionHistoryId);
		}
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed"  desc="ComponentRESTResource TRACKING section">
	@GET
	@RequireSecurity(SecurityPermission.ADMIN_TRACKING)
	@APIDescription("Get the list of tracking details on a specified component. Always sorts by create date.")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentTrackingWrapper.class)
	@Path("/{id}/tracking")
	public Response getActiveComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ComponentTracking trackingExample = new ComponentTracking();
		trackingExample.setComponentId(componentId);
		trackingExample.setActiveStatus(filterQueryParams.getStatus());

		QueryByExample<ComponentTracking> queryByExample = new QueryByExample<>(trackingExample);
		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());

		ComponentTracking trackingOrderExample = new ComponentTracking();
		trackingOrderExample.setCreateDts(QueryByExample.DATE_FLAG);
		queryByExample.setOrderBy(trackingOrderExample);
		queryByExample.setSortDirection(OpenStorefrontConstant.SORT_DESCENDING);

		List<ComponentTracking> componentTrackings = service.getPersistenceService().queryByExample(queryByExample);

		long total = service.getPersistenceService().countByExample(new QueryByExample<>(QueryType.COUNT, trackingExample));
		return sendSingleEntityResponse(new ComponentTrackingWrapper(componentTrackings, total));
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_TRACKING)
	@APIDescription("Get the list of tracking details on a specified component")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentTracking.class)
	@Path("/{id}/tracking/{trackingId}")
	public Response getComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("id")
			@RequiredParam String trackingId)
	{
		ComponentTracking componentTrackingExample = new ComponentTracking();
		componentTrackingExample.setComponentId(componentId);
		componentTrackingExample.setComponentTrackingId(trackingId);
		ComponentTracking componentTracking = service.getPersistenceService().queryOneByExample(componentTrackingExample);
		return sendSingleEntityResponse(componentTracking);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_TRACKING)
	@APIDescription("Remove a tracking entry from the specified component")
	@Path("/{id}/tracking/{trackingId}")
	public void deleteComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId,
			@PathParam("id")
			@RequiredParam String trackingId)
	{
		ComponentTracking componentTrackingExample = new ComponentTracking();
		componentTrackingExample.setComponentId(componentId);
		componentTrackingExample.setComponentTrackingId(trackingId);
		ComponentTracking componentTracking = service.getPersistenceService().queryOneByExample(componentTrackingExample);
		if (componentTracking != null) {
			service.getComponentService().deactivateBaseComponent(ComponentTracking.class, trackingId);
		}
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_TRACKING)
	@APIDescription("Remove all tracking entries from the specified component")
	@Path("/{id}/tracking")
	public void deleteAllComponentTracking(
			@PathParam("id")
			@RequiredParam String componentId)
	{
		service.getComponentService().deleteAllBaseComponent(ComponentTracking.class, componentId);
	}
	// </editor-fold>

	// <editor-fold defaultstate="collapsed"  desc="Integrations">
	@GET
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Gets all integration models from the database.")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentIntegration.class)
	@Path("/integration")
	public List<ComponentIntegrationView> getIntegrations(
			@QueryParam("status")
			@DefaultValue("A")
			@APIDescription("Pass 'ALL' to view active and inactive") String status)
	{
		if (OpenStorefrontConstant.STATUS_VIEW_ALL.equals(status)) {
			status = null;
		}
		List<ComponentIntegration> integrationModels = service.getComponentService().getComponentIntegrationModels(status);
		List<ComponentIntegrationView> views = new ArrayList<>();
		for (ComponentIntegration temp : integrationModels) {
			views.add(ComponentIntegrationView.toView(temp));
		}
		return views;
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Gets a integration model")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentIntegration.class)
	@Path("/{id}/integration")
	public Response getIntegration(
			@PathParam("id") String componentId)
	{
		ComponentIntegration integrationExample = new ComponentIntegration();
		integrationExample.setComponentId(componentId);
		ComponentIntegration integration = service.getPersistenceService().queryOneByExample(integrationExample);
		ComponentIntegrationView view = null;
		if (integration != null) {
			view = ComponentIntegrationView.toView(integration);
		}
		return sendSingleEntityResponse(view);
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Saves a component integration model")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration")
	public Response saveIntegration(
			@PathParam("componentId")
			@RequiredParam String componentId,
			ComponentIntegration integration)
	{
		ValidationModel validationModel = new ValidationModel(integration);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			service.getComponentService().saveComponentIntegration(integration);
			return Response.created(URI.create("v1/resource/components/" + componentId + "/integration")).entity(integration).build();
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Updates a component integration refresh Time")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration/cron")
	public Response saveRefreshRate(
			@PathParam("componentId")
			@RequiredParam String componentId,
			String cron)
	{
		ComponentIntegration integration = service.getPersistenceService().findById(ComponentIntegration.class, componentId);
		if (integration != null) {
			integration.setRefreshRate(cron);
			ValidationModel validationModel = new ValidationModel(integration);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult validationResult = ValidationUtil.validate(validationModel);
			if (validationResult.valid()) {
				service.getComponentService().saveComponentIntegration(integration);
				return Response.created(URI.create("v1/resource/components/" + componentId + "/integration")).entity(integration).build();
			} else {
				return Response.ok(validationResult.toRestError()).build();
			}
		} else {
			return Response.ok().build();
		}

	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Removes the integration override time")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration/cron")
	public Response deleteRefreshRate(
			@PathParam("componentId")
			@RequiredParam String componentId)
	{
		ComponentIntegration integration = service.getPersistenceService().findById(ComponentIntegration.class, componentId);
		if (integration != null) {
			integration.setRefreshRate(null);
			ValidationModel validationModel = new ValidationModel(integration);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult validationResult = ValidationUtil.validate(validationModel);
			if (validationResult.valid()) {
				service.getComponentService().saveComponentIntegration(integration);
				return Response.created(URI.create("v1/resource/components/" + componentId + "/integration")).entity(integration).build();
			} else {
				return Response.ok(validationResult.toRestError()).build();
			}
		} else {
			return Response.ok().build();
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Activates  a component integration model")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration/activate")
	public Response activateIntegration(
			@PathParam("componentId")
			@RequiredParam String componentId)
	{
		ComponentIntegration componentIntegration = service.getPersistenceService().findById(ComponentIntegration.class, componentId);
		if (componentIntegration != null) {
			service.getComponentService().setStatusOnComponentIntegration(componentId, ComponentIntegration.ACTIVE_STATUS);
		}
		return sendSingleEntityResponse(componentIntegration, Response.Status.NOT_MODIFIED);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Inactivates a component integration model")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration/inactivate")
	public Response inactiveIntegration(
			@PathParam("componentId")
			@RequiredParam String componentId)
	{
		ComponentIntegration componentIntegration = service.getPersistenceService().findById(ComponentIntegration.class, componentId);
		if (componentIntegration != null) {
			service.getComponentService().setStatusOnComponentIntegration(componentId, ComponentIntegration.INACTIVE_STATUS);
		}
		return sendSingleEntityResponse(componentIntegration, Response.Status.NOT_MODIFIED);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Toggle status for multiple component integration models. Consumes a list of componentId strings")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/integration/togglemultiple")
	public void toggleMultipleIntegrations(
			@RequiredParam
			@DataType(String.class) List<String> componentIds)
	{
		for (String componentId : componentIds) {
			ComponentIntegration componentIntegration = service.getPersistenceService().findById(ComponentIntegration.class, componentId);
			if (componentIntegration != null) {
				if (componentIntegration.getActiveStatus().equals(ComponentIntegration.ACTIVE_STATUS)) {
					service.getComponentService().setStatusOnComponentIntegration(componentId, ComponentIntegration.INACTIVE_STATUS);
				} else {
					service.getComponentService().setStatusOnComponentIntegration(componentId, ComponentIntegration.ACTIVE_STATUS);
				}
			}
		}

	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Removes component integration and all child configs.")
	@Path("/{componentId}/integration")
	public Response deleteComponentConfig(
			@PathParam("componentId")
			@RequiredParam String componentId)
	{
		ComponentIntegration integration = service.getPersistenceService().findById(ComponentIntegration.class, componentId);
		if (integration.getActiveStatus().equals(ComponentIntegration.ACTIVE_STATUS) && integration.getStatus().equals(RunStatus.WORKING)) {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		} else {
			service.getComponentService().deleteComponentIntegration(componentId);
			return Response.ok().build();
		}
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Runs a full component integration")
	@Path("/{componentId}/integration/run")
	public Response runComponentIntegration(
			@PathParam("componentId")
			@RequiredParam String componentId)
	{
		ComponentIntegration integration = service.getPersistenceService().findById(ComponentIntegration.class, componentId);
		if (integration != null) {
			if (integration.getActiveStatus().equals(ComponentIntegration.INACTIVE_STATUS)) {
				if (integration.getStatus().equals(RunStatus.WORKING)) {
					integration.setStatus(RunStatus.COMPLETE);
					service.getComponentService().saveComponentIntegration(integration);

				}
				return Response.status(Response.Status.NOT_MODIFIED).build();
			} else {
				if (!integration.getStatus().equals(RunStatus.WORKING)) {
					JobManager.runComponentIntegrationNow(componentId, null);
					return Response.ok().build();
				}
				return Response.status(Response.Status.NOT_MODIFIED).build();
			}
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Runs all active component integrations")
	@Path("/integrations/run")
	public Response runAllComponentIntegration()
	{
		List<ComponentIntegration> componentIntegrations = service.getComponentService().getComponentIntegrationModels(ComponentIntegration.ACTIVE_STATUS);
		for (ComponentIntegration componentIntegration : componentIntegrations) {
			JobManager.runComponentIntegrationNow(componentIntegration.getComponentId(), null);
		}
		return Response.ok().build();
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Gets all component integration configs")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentIntegrationConfig.class)
	@Path("/{componentId}/integration/configs")
	public List<ComponentIntegrationConfig> getIntegrationConfigs(
			@PathParam("componentId") String componentId)
	{
		List<ComponentIntegrationConfig> configs;
		ComponentIntegrationConfig integrationConfigExample = new ComponentIntegrationConfig();
		integrationConfigExample.setComponentId(componentId);
		configs = service.getPersistenceService().queryByExample(integrationConfigExample);
		return configs;
	}

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Gets all component integration configs")
	@Produces(
			{
				MediaType.APPLICATION_JSON
			})
	@DataType(ComponentIntegrationConfig.class)
	@Path("/{componentId}/integration/configs/{configId}")
	public Response getIntegrationConfigs(
			@PathParam("componentId") String componentId,
			@PathParam("configId") String configId)
	{
		ComponentIntegrationConfig integrationConfigExample = new ComponentIntegrationConfig();
		integrationConfigExample.setComponentId(componentId);
		integrationConfigExample.setIntegrationConfigId(configId);
		ComponentIntegrationConfig integrationConfig = service.getPersistenceService().queryOneByExample(integrationConfigExample);
		return sendSingleEntityResponse(integrationConfig);
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Saves a component integration model")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration/configs")
	public Response saveIntegrationConfig(
			@PathParam("componentId")
			@RequiredParam String componentId,
			ComponentIntegrationConfig integrationConfig)
	{
		integrationConfig.setComponentId(componentId);

		ValidationModel validationModel = new ValidationModel(integrationConfig);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);

		if (validationResult.valid()) {
			//check for exsiting config with the same ticket
			ComponentIntegrationConfig configExample = new ComponentIntegrationConfig();
			configExample.setComponentId(integrationConfig.getComponentId());
			configExample.setIntegrationType(integrationConfig.getIntegrationType());
			configExample.setProjectType(integrationConfig.getProjectType());
			configExample.setIssueType(integrationConfig.getIssueType());
			configExample.setIssueNumber(integrationConfig.getIssueNumber());

			GenerateStatementOption option = new GenerateStatementOption();
			option.setMethod(GenerateStatementOption.METHOD_UPPER_CASE);

			QueryByExample<ComponentIntegrationConfig> configQueryExample = new QueryByExample<>();
			configQueryExample.getFieldOptions().put(ComponentIntegrationConfig.FIELD_ISSUENUMBER, option);
			configQueryExample.setExample(configExample);

			long count = service.getPersistenceService().countByExample(configQueryExample);
			if (count > 0) {
				RestErrorModel restErrorModel = new RestErrorModel();
				restErrorModel.getErrors().put(ComponentIntegrationConfig.FIELD_ISSUENUMBER, "Issue number needs to be unique per project.");
				return Response.status(Response.Status.CONFLICT).entity(restErrorModel).build();
			} else {
				integrationConfig.setActiveStatus(ComponentIntegrationConfig.ACTIVE_STATUS);
				integrationConfig = service.getComponentService().saveComponentIntegrationConfig(integrationConfig);
				return Response.created(URI.create("v1/resource/components/" + componentId + "/integration/configs/" + integrationConfig.getIntegrationConfigId())).entity(integrationConfig).build();
			}
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Updates a component integration model")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration/configs/{integrationConfigId}")
	public Response updateIntegrationConfig(
			@PathParam("componentId")
			@RequiredParam String componentId,
			@PathParam("integrationConfigId")
			@RequiredParam String integrationConfigId,
			ComponentIntegrationConfig integrationConfig)
	{
		integrationConfig.setComponentId(componentId);
		integrationConfig.setIntegrationConfigId(integrationConfigId);

		ComponentIntegrationConfig configExample = new ComponentIntegrationConfig();
		configExample.setComponentId(componentId);
		configExample.setIntegrationConfigId(integrationConfigId);

		configExample = service.getPersistenceService().queryOneByExample(configExample);
		if (configExample != null) {
			ValidationModel validationModel = new ValidationModel(integrationConfig);
			validationModel.setConsumeFieldsOnly(true);
			ValidationResult validationResult = ValidationUtil.validate(validationModel);

			if (validationResult.valid()) {
				//check for exsiting config with the same ticket
				configExample = new ComponentIntegrationConfig();
				configExample.setComponentId(integrationConfig.getComponentId());
				configExample.setIntegrationConfigId(integrationConfig.getIntegrationConfigId());
				configExample.setIntegrationType(integrationConfig.getIntegrationType());
				configExample.setProjectType(integrationConfig.getProjectType());
				configExample.setIssueType(integrationConfig.getIssueType());
				configExample.setIssueNumber(integrationConfig.getIssueNumber());

				GenerateStatementOption configIdOption = new GenerateStatementOption();
				configIdOption.setOperation(GenerateStatementOption.OPERATION_NOT_EQUALS);

				GenerateStatementOption issueNumberOption = new GenerateStatementOption();
				issueNumberOption.setMethod(GenerateStatementOption.METHOD_UPPER_CASE);

				QueryByExample<ComponentIntegrationConfig> configQueryExample = new QueryByExample<>();
				configQueryExample.getFieldOptions().put("integrationConfigId", configIdOption);
				configQueryExample.getFieldOptions().put("issueNumber", issueNumberOption);
				configQueryExample.setExample(configExample);

				long count = service.getPersistenceService().countByExample(configQueryExample);
				if (count > 0) {
					RestErrorModel restErrorModel = new RestErrorModel();
					restErrorModel.getErrors().put("issueNumber", "Issue number needs to be unique per project.");
					return Response.status(Response.Status.CONFLICT).entity(restErrorModel).build();
				} else {
					integrationConfig.setActiveStatus(ComponentIntegrationConfig.ACTIVE_STATUS);
					integrationConfig = service.getComponentService().saveComponentIntegrationConfig(integrationConfig);
					return Response.ok(integrationConfig).build();
				}
			} else {
				return Response.ok(validationResult.toRestError()).build();
			}
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Activates a component integration config")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration/configs/{configId}/activate")
	public Response activateIntegrationConfig(
			@PathParam("componentId")
			@RequiredParam String componentId,
			@PathParam("configId") String configId)
	{
		ComponentIntegrationConfig integrationConfigExample = new ComponentIntegrationConfig();
		integrationConfigExample.setComponentId(componentId);
		integrationConfigExample.setIntegrationConfigId(configId);
		ComponentIntegrationConfig integrationConfig = service.getPersistenceService().queryOneByExample(integrationConfigExample);

		if (integrationConfig != null) {
			service.getComponentService().setStatusOnComponentIntegrationConfig(configId, ComponentIntegrationConfig.ACTIVE_STATUS);
		}
		return sendSingleEntityResponse(integrationConfig, Response.Status.NOT_MODIFIED);
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Saves a component integration model")
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{componentId}/integration/configs/{configId}/inactivate")
	public Response inactiveIntegrationConfig(
			@PathParam("componentId")
			@RequiredParam String componentId,
			@PathParam("configId") String configId)
	{
		ComponentIntegrationConfig integrationConfigExample = new ComponentIntegrationConfig();
		integrationConfigExample.setComponentId(componentId);
		integrationConfigExample.setIntegrationConfigId(configId);
		ComponentIntegrationConfig integrationConfig = service.getPersistenceService().queryOneByExample(integrationConfigExample);

		if (integrationConfig != null) {
			service.getComponentService().setStatusOnComponentIntegrationConfig(configId, ComponentIntegrationConfig.INACTIVE_STATUS);
		}
		return sendSingleEntityResponse(integrationConfig, Response.Status.NOT_MODIFIED);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Deletes component integration config")
	@Path("/{componentId}/integration/configs/{configId}")
	public void deleteComponentIntegrationConfig(
			@PathParam("componentId")
			@RequiredParam String componentId,
			@PathParam("configId") String configId)
	{
		ComponentIntegrationConfig integrationConfigExample = new ComponentIntegrationConfig();
		integrationConfigExample.setComponentId(componentId);
		integrationConfigExample.setIntegrationConfigId(configId);
		ComponentIntegrationConfig integrationConfig = service.getPersistenceService().queryOneByExample(integrationConfigExample);

		if (integrationConfig != null) {
			service.getComponentService().deleteComponentIntegrationConfig(configId);
		}
	}

	@POST
	@RequireSecurity(SecurityPermission.ADMIN_INTEGRATION)
	@APIDescription("Runs a component integration config.")
	@Path("/{componentId}/integration/configs/{configId}/run")
	public Response runComponentIntegrationConfig(
			@PathParam("componentId")
			@RequiredParam String componentId,
			@PathParam("configId") String configId)
	{
		ComponentIntegrationConfig integrationConfigExample = new ComponentIntegrationConfig();
		integrationConfigExample.setComponentId(componentId);
		integrationConfigExample.setIntegrationConfigId(configId);
		ComponentIntegrationConfig integrationConfig = service.getPersistenceService().queryOneByExample(integrationConfigExample);

		if (integrationConfig != null) {
			JobManager.runComponentIntegrationNow(componentId, configId);
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	// </editor-fold>

}
