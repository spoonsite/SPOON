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

import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.doc.RequireAdmin;
import edu.usu.sdl.openstorefront.service.manager.model.TaskRequest;
import edu.usu.sdl.openstorefront.storage.model.Report;
import edu.usu.sdl.openstorefront.storage.model.ReportFormat;
import edu.usu.sdl.openstorefront.util.TranslateUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import edu.usu.sdl.openstorefront.web.rest.model.FilterQueryParams;
import edu.usu.sdl.openstorefront.web.viewmodel.LookupModel;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
@Path("v1/resource/reports")
@APIDescription("System generated hard reports")
public class ReportResource
		extends BaseResource
{

	@GET
	@RequireAdmin
	@APIDescription("Gets report records.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Report.class)
	public Response getReports(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		Report reportExample = new Report();
		reportExample.setActiveStatus(Report.ACTIVE_STATUS);
		List<Report> reports = service.getPersistenceService().queryByExample(Report.class, reportExample);
		reports = filterQueryParams.filter(reports);

		GenericEntity<List<Report>> entity = new GenericEntity<List<Report>>(reports)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@RequireAdmin
	@APIDescription("Gets a report record.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Report.class)
	@Path("/{id}")
	public Response getReport(
			@PathParam("id") String reportId
	)
	{
		Report reportExample = new Report();
		reportExample.setReportId(reportId);
		Report report = service.getPersistenceService().queryOneByExample(Report.class, reportExample);
		return sendSingleEntityResponse(report);
	}

	@GET
	@RequireAdmin
	@APIDescription("Gets report supported formats")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@Path("/{id}/formats")
	public Response getReportFormats(
			@PathParam("id") String reportId
	)
	{
		Map<String, List<String>> reportFormatMap = service.getReportService().getSupportedFormats();

		List<LookupModel> formats = new ArrayList<>();
		List<String> formatList = reportFormatMap.get(reportId);
		for (String format : formatList) {
			LookupModel lookupModel = new LookupModel();
			lookupModel.setCode(format);
			lookupModel.setDescription(TranslateUtil.translate(ReportFormat.class, format));
			formats.add(lookupModel);
		}

		GenericEntity<List<LookupModel>> entity = new GenericEntity<List<LookupModel>>(formats)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@POST
	@RequireAdmin
	@APIDescription("Generates a new report")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response postAlert(Report report)
	{
		ValidationModel validationModel = new ValidationModel(report);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {
			report = service.getReportService().queueReport(report);

			TaskRequest taskRequest = new TaskRequest();
			taskRequest.setAllowMultiple(true);
			taskRequest.setName("Generating Report: " + report.getReportType() + " id: " + report.getReportId());
			service.getAyncProxy(service.getReportService(), taskRequest).generateReport(report);

		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		return Response.created(URI.create("v1/resource/reports/" + report.getReportId())).entity(report).build();
	}

	@DELETE
	@RequireAdmin
	@APIDescription("Deletes a report")
	@Path("/{id}")
	public void deleteReport(
			@PathParam("id") String reportId)
	{
		service.getReportService().deleteReport(reportId);
	}

}
