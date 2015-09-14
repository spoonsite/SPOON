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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.api.model.TaskRequest;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.core.view.ReportView;
import edu.usu.sdl.openstorefront.core.view.ReportWrapper;
import edu.usu.sdl.openstorefront.core.view.RequestEntity;
import edu.usu.sdl.openstorefront.doc.annotation.RequiredParam;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import net.sourceforge.stripes.util.bean.BeanUtil;

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
	@APIDescription("Gets report records.")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ReportView.class)
	public Response getReports(@BeanParam FilterQueryParams filterQueryParams)
	{
		ValidationResult validationResult = filterQueryParams.validate();
		if (!validationResult.valid()) {
			return sendSingleEntityResponse(validationResult.toRestError());
		}

		Report reportExample = new Report();
		reportExample.setActiveStatus(filterQueryParams.getStatus());
		if (SecurityUtil.isAdminUser() == false) {
			reportExample.setCreateUser(SecurityUtil.getCurrentUserName());
		}

		Report reportStartExample = new Report();
		reportStartExample.setCreateDts(filterQueryParams.getStart());

		Report reportEndExample = new Report();
		reportEndExample.setCreateDts(filterQueryParams.getEnd());

		QueryByExample queryByExample = new QueryByExample(reportExample);

		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(reportStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(reportEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		queryByExample.setMaxResults(filterQueryParams.getMax());
		queryByExample.setFirstResult(filterQueryParams.getOffset());
		queryByExample.setSortDirection(filterQueryParams.getSortOrder());

		Report reportSortExample = new Report();
		Field sortField = ReflectionUtil.getField(reportSortExample, filterQueryParams.getSortField());
		if (sortField != null) {
			BeanUtil.setPropertyValue(sortField.getName(), reportSortExample, QueryByExample.getFlagForType(sortField.getType()));
			queryByExample.setOrderBy(reportSortExample);
		}

		List<Report> reports = service.getPersistenceService().queryByExample(Report.class, queryByExample);

		ReportWrapper reportWrapper = new ReportWrapper();
		reportWrapper.getData().addAll(ReportView.toReportView(reports));
		reportWrapper.setTotalNumber(service.getPersistenceService().countByExample(queryByExample));

		return sendSingleEntityResponse(reportWrapper);
	}

	@GET
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
		Response response = ownerCheck(report);
		if (response == null) {
			response = sendSingleEntityResponse(report);
		}
		return response;
	}

	@GET
	@APIDescription("Gets the actual report")
	@Produces({MediaType.WILDCARD})
	@DataType(Report.class)
	@Path("/{id}/report")
	public Response getReportData(
			@PathParam("id") String reportId
	)
	{
		Report reportExample = new Report();
		reportExample.setReportId(reportId);
		Report report = service.getPersistenceService().queryOneByExample(Report.class, reportExample);
		if (report != null) {

			Response response = ownerCheck(report);
			if (response == null) {

				java.nio.file.Path path = report.pathToReport();

				if (path.toFile().exists()) {
					String extenstion = OpenStorefrontConstant.getFileExtensionForMime(ReportFormat.mimeType(report.getReportFormat()));
					Response.ResponseBuilder responseBuilder = Response.ok(new StreamingOutput()
					{

						@Override
						public void write(OutputStream output) throws IOException, WebApplicationException
						{
							Files.copy(path, output);
						}

					});
					responseBuilder.header("Content-Disposition", "attachment; filename=\"" + TranslateUtil.translate(ReportType.class, report.getReportType()) + extenstion + "\"");
					response = responseBuilder.build();
				}
			}
			return response;
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}

	@GET
	@APIDescription("Gets report supported formats")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(ReportType.class)
	@Path("/reporttypes")
	public Response getReportTypeForUser()
	{
		List<ReportType> reportTypes = service.getLookupService().findLookup(ReportType.class);

		if (SecurityUtil.isAdminUser() == false) {
			reportTypes = reportTypes.stream().filter(r -> r.getAdminOnly() == false).collect(Collectors.toList());
		}

		GenericEntity<List<ReportType>> entity = new GenericEntity<List<ReportType>>(reportTypes)
		{
		};
		return sendSingleEntityResponse(entity);
	}

	@GET
	@APIDescription("Gets report supported formats")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@Path("/{reportType}/formats")
	public Response getReportFormats(
			@PathParam("reportType") String reportType
	)
	{
		Map<String, List<String>> reportFormatMap = service.getReportService().getSupportedFormats();

		List<LookupModel> formats = new ArrayList<>();
		List<String> formatList = reportFormatMap.get(reportType);
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
	@APIDescription("Generates a new report")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response postAlert(Report report)
	{
		ValidationModel validationModel = new ValidationModel(report);
		validationModel.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModel);
		if (validationResult.valid()) {

			//check that user can run that report
			ReportType reportType = service.getLookupService().getLookupEnity(ReportType.class, report.getReportType());
			boolean run = true;
			if (reportType.getAdminOnly()) {
				if (SecurityUtil.isAdminUser() == false) {
					run = false;
				}
			}

			if (run) {
				report = service.getReportService().queueReport(report);

				TaskRequest taskRequest = new TaskRequest();
				taskRequest.setAllowMultiple(true);
				taskRequest.setName(TaskRequest.TASKNAME_REPORT);
				taskRequest.setDetails("Report: " + report.getReportType() + " Report id: " + report.getReportId() + " for user: " + SecurityUtil.getCurrentUserName());
				taskRequest.getTaskData().put(TaskRequest.DATAKEY_REPORT_ID, report.getReportId());
				service.getAsyncProxy(service.getReportService(), taskRequest).generateReport(report);
			} else {
				return Response.status(Response.Status.FORBIDDEN).build();
			}
		} else {
			return Response.ok(validationResult.toRestError()).build();
		}
		return Response.created(URI.create("v1/resource/reports/" + report.getReportId())).entity(report).build();
	}

	@DELETE
	@APIDescription("Deletes a report")
	@Path("/{id}")
	public void deleteReport(
			@PathParam("id") String reportId)
	{
		Report report = new Report();
		report.setReportId(reportId);
		report = report.find();
		handleDeleteReport(report);
	}

	private void handleDeleteReport(Report report)
	{
		if (report != null) {
			if (ownerCheck(report) == null) {
				service.getReportService().deleteReport(report.getReportId());
			}
		}
	}

	@DELETE
	@APIDescription("Deletes group of reports")
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/delete")
	public void deleteReports(
			@RequiredParam RequestEntity reportIds)
	{
		for (String reportId : reportIds.getEntity()) {
			Report report = new Report();
			report.setReportId(reportId);
			report = report.find();
			handleDeleteReport(report);
		}
	}

}
