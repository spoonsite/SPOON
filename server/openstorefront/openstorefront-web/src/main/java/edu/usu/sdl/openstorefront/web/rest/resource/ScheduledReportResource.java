/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportType;
import edu.usu.sdl.openstorefront.core.entity.ScheduledReport;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.view.ReportDetailView;
import edu.usu.sdl.openstorefront.core.view.ReportFilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.ScheduledReportView;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.net.URI;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/scheduledreports")
@APIDescription("Allows access to scheduled reports")
public class ScheduledReportResource
		extends BaseResource
{

	@GET
	@RequireSecurity(SecurityPermission.REPORTS_SCHEDULE_READ)
	@APIDescription("Gets scheduled report records.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ScheduledReportView.class)
	public Response getReports(@BeanParam ReportFilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		ScheduledReport reportExample = new ScheduledReport();
		reportExample.setActiveStatus(filterQueryParams.getStatus());
		if (SecurityUtil.hasPermission(SecurityPermission.REPORTS_ALL) == false) {
			reportExample.setCreateUser(SecurityUtil.getCurrentUserName());
		} else {
			if (!filterQueryParams.getShowAllUsers()) {
				reportExample.setCreateUser(SecurityUtil.getCurrentUserName());
			}
		}
		if (StringUtils.isNotBlank(filterQueryParams.getReportType())) {
			reportExample.setReportType(filterQueryParams.getReportType());
		}

		List<ScheduledReport> reports = service.getPersistenceService().queryByExample(reportExample);
		reports = filterQueryParams.filter(reports);

		GenericEntity<List<ScheduledReportView>> entity = new GenericEntity<List<ScheduledReportView>>(ScheduledReportView.toReportView(reports))
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@RequireSecurity(SecurityPermission.REPORTS_SCHEDULE_READ)
	@APIDescription("Gets a scheduled report record.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Report.class)
	@Path("/{id}")
	public Response getReport(
			@PathParam("id") String scheduleReportId
	)
	{
		ScheduledReport reportExample = new ScheduledReport();
		reportExample.setScheduleReportId(scheduleReportId);
		ScheduledReport report = service.getPersistenceService().queryOneByExample(reportExample);
		Response response = ownerCheck(report, SecurityPermission.REPORTS_ALL);
		if (response == null) {
			response = sendSingleEntityResponse(report);
		}
		return response;
	}

	@GET
	@RequireSecurity(SecurityPermission.REPORTS_SCHEDULE_READ)
	@APIDescription("Gets a scheduled report details.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ReportDetailView.class)
	@Path("/{id}/detail")
	public Response getReportDetails(
			@PathParam("id") String scheduleReportId
	)
	{
		ScheduledReport reportExample = new ScheduledReport();
		reportExample.setScheduleReportId(scheduleReportId);
		ScheduledReport report = reportExample.find();
		Response response = ownerCheck(report, SecurityPermission.REPORTS_ALL);
		if (response == null) {
			ReportDetailView reportDetailView = ReportDetailView.toView(report);
			response = sendSingleEntityResponse(reportDetailView);
		}
		return response;
	}

	@POST
	@RequireSecurity(SecurityPermission.REPORTS_SCHEDULE_CREATE)
	@APIDescription("Schedules a new report")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response postAlert(ScheduledReport scheduledReport)
	{
		return handleSaveScheduledReport(scheduledReport, true);
	}

	@PUT
	@RequireSecurity(SecurityPermission.REPORTS_SCHEDULE_UPDATE)
	@APIDescription("Updates a scheduled report record")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/{id}")
	public Response updateEntityValue(
			@PathParam("id")
			@RequiredParam String scheduledReportId,
			ScheduledReport scheduledReport)
	{

		ScheduledReport existing = service.getPersistenceService().findById(ScheduledReport.class, scheduledReportId);
		if (existing == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		Response response = ownerCheck(existing, SecurityPermission.REPORTS_ALL);
		if (response == null) {
			scheduledReport.setScheduleReportId(scheduledReportId);
			return handleSaveScheduledReport(scheduledReport, false);
		}
		return response;
	}

	private Response handleSaveScheduledReport(ScheduledReport scheduledReport, boolean post)
	{
		ValidationModel validationModel = new ValidationModel(scheduledReport);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);

		validationResult.merge(scheduledReport.customValidation());

		if (validationResult.valid()) {
			//check that user can run that report
			ReportType reportType = service.getLookupService().getLookupEnity(ReportType.class, scheduledReport.getReportType());
			boolean run = true;
			if (!SecurityUtil.hasPermission(reportType.getRequiredPermission())) {
				run = false;
			}
			if (run) {
				service.getReportService().saveScheduledReport(scheduledReport);
			} else {
				return Response.status(Response.Status.FORBIDDEN).build();
			}
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		if (post) {
			return Response.created(URI.create("v1/resource/scheduledreports/" + scheduledReport.getScheduleReportId())).entity(scheduledReport).build();
		} else {
			return Response.ok(scheduledReport).build();
		}
	}

	@PUT
	@RequireSecurity(SecurityPermission.REPORTS_SCHEDULE_UPDATE)
	@APIDescription("Activates a Scheduled Report")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ScheduledReport.class)
	@Path("/{id}/activate")
	public Response activatesAlert(
			@PathParam("id") String scheduleReportId)
	{
		ScheduledReport scheduledReport = new ScheduledReport();
		scheduledReport.setScheduleReportId(scheduleReportId);
		scheduledReport = scheduledReport.find();
		if (scheduledReport != null) {
			Response response = ownerCheck(scheduledReport, SecurityPermission.REPORTS_ALL);
			if (response == null) {
				service.getReportService().updateStatusOnScheduledReport(scheduleReportId, ScheduledReport.ACTIVE_STATUS);
				scheduledReport.setActiveStatus(ScheduledReport.ACTIVE_STATUS);
				return sendSingleEntityResponse(scheduledReport);
			} else {
				return response;
			}
		}
		return sendSingleEntityResponse(scheduledReport);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.REPORTS_SCHEDULE_DELETE)
	@APIDescription("Inactivates a scheduled report")
	@Path("/{id}")
	public void inactiveScheduledReport(
			@PathParam("id") String scheduleReportId)
	{
		ScheduledReport scheduledReport = new ScheduledReport();
		scheduledReport.setScheduleReportId(scheduleReportId);
		scheduledReport = scheduledReport.find();
		if (scheduledReport != null) {
			Response response = ownerCheck(scheduledReport, SecurityPermission.REPORTS_ALL);
			if (response == null) {
				service.getReportService().updateStatusOnScheduledReport(scheduleReportId, ScheduledReport.INACTIVE_STATUS);
			}
		}
	}

	@DELETE
	@RequireSecurity(SecurityPermission.REPORTS_SCHEDULE_DELETE)
	@APIDescription("Deletes a scheduled report record")
	@Path("/{id}/force")
	public void deleteScheduledReport(
			@PathParam("id") String scheduleReportId)
	{
		ScheduledReport scheduledReport = new ScheduledReport();
		scheduledReport.setScheduleReportId(scheduleReportId);
		scheduledReport = scheduledReport.find();
		if (scheduledReport != null) {
			Response response = ownerCheck(scheduledReport, SecurityPermission.REPORTS_ALL);
			if (response == null) {
				service.getReportService().deleteScheduledReport(scheduleReportId);
			}
		}
	}

}
