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
package edu.usu.sdl.openstorefront.report;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.entity.Branding;
import edu.usu.sdl.openstorefront.core.entity.ErrorTypeCode;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportOption;
import edu.usu.sdl.openstorefront.core.entity.ReportOutput;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.entity.ReportType;
import edu.usu.sdl.openstorefront.core.entity.SecurityRole;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.report.model.BaseReportModel;
import edu.usu.sdl.openstorefront.report.output.BaseOutput;
import edu.usu.sdl.openstorefront.report.output.ReportWriter;
import edu.usu.sdl.openstorefront.security.UserContext;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public abstract class BaseReport<T extends BaseReportModel>
{

	private static final Logger LOG = Logger.getLogger(BaseReport.class.getName());

	protected Report report;
	protected Service service;
	protected FilterEngine filterEngine;

	//Note: not thread-safe; don't make static need a new one per thread
	protected SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");

	private Branding branding;

	public BaseReport(Report report)
	{
		initBaseReport(report, ServiceProxy.getProxy(), null);
	}

	public BaseReport(Report report, Service service)
	{
		initBaseReport(report, service, null);
	}

	public BaseReport(Report report, Service service, FilterEngine filterEngine)
	{
		initBaseReport(report, service, filterEngine);
	}

	private void initBaseReport(Report report, Service service, FilterEngine filterEngine)
	{
		Objects.requireNonNull(report, "Report must set");
		Objects.requireNonNull(service);

		this.report = report;
		this.service = service;
		if (filterEngine != null) {
			this.filterEngine = filterEngine;
		} else {
			this.filterEngine = FilterEngine.getInstance(getReportUserContext());
			this.filterEngine.setForceFiltering(true);
		}

		if (this.report.getReportOption() == null) {
			this.report.setReportOption(new ReportOption());
		}
	}

	public static BaseReport getReport(Report report)
	{
		BaseReport baseReport = null;
		if (null != report.getReportType()) {
			switch (report.getReportType()) {
				case ReportType.ENTRY_REPORT:
					baseReport = new ComponentReport(report);
					break;
				case ReportType.USAGE_REPORT:
					baseReport = new UsageReport(report);
					break;
				case ReportType.USER_ORG_REPORT:
					baseReport = new OrganizationReport(report);
					break;
				case ReportType.ENTRIES_BY_ORG_REPORT:
					baseReport = new ComponentOrganizationReport(report);
					break;
				case ReportType.USER_REPORT:
					baseReport = new UserReport(report);
					break;
				case ReportType.LINK_VALIDATION_REPORT:
					baseReport = new ExternalLinkValidationReport(report);
					break;
				case ReportType.SUBMISSIONS_REPORT:
					baseReport = new SubmissionsReport(report);
					break;
				case ReportType.ENTRIES_BY_CAT_REPORT:
					baseReport = new CategoryComponentReport(report);
					break;
				case ReportType.ENTRY_DETAIL_REPORT:
					baseReport = new ComponentDetailReport(report);
					break;
				case ReportType.EVAL_STATUS_REPORT:
					baseReport = new EvaluationStatusReport(report);
					break;
				case ReportType.ACTION_REPORT:
					baseReport = new AdminActionReport(report);
					break;
				case ReportType.ENTRY_LISTING_REPORT:
					baseReport = new EntryListingReport(report);
					break;
				case ReportType.ENTRY_STATUS_REPORT:
					baseReport = new EntryStatusReport(report);
					break;
				case ReportType.WORKPLAN_STATUS:
					baseReport = new WorkPlanStatusReport(report);
					break;

				default:
					throw new OpenStorefrontRuntimeException("Report Type not supported", "Check type and/or add support. Type: " + report.getReportType(), ErrorTypeCode.REPORT);
			}
		} else {
			Objects.requireNonNull(report.getReportType(), "Report Type required");
		}
		return baseReport;
	}

	protected Branding getBranding()
	{
		if (branding == null) {
			branding = service.getBrandingService().getCurrentBrandingView();
		}
		return branding;
	}

	/**
	 * Generates the report; Controls the flow
	 *
	 */
	public void generateReport()
	{
		try {
			BaseReportModel baseReportModel = gatherDataOnly();
			handleOutputs(baseReportModel);
		} catch (Exception e) {
			throw new OpenStorefrontRuntimeException("Report failed to generate.", e, ErrorTypeCode.REPORT);
		}
	}

	public BaseReportModel gatherDataOnly()
	{
		BaseReportModel baseReportModel = gatherData();
		return baseReportModel;
	}

	protected abstract T gatherData();

	protected void handleOutputs(BaseReportModel reportModel)
	{
		if (report.getReportOutputs() == null || report.getReportOutputs().isEmpty()) {
			throw new OpenStorefrontRuntimeException("No report output defined", "Report should have at least one.");
		} else {
			for (ReportOutput reportOutput : report.getReportOutputs()) {
				BaseOutput outputHandler = BaseOutput.getOutput(reportOutput, report, this, getReportUserContext());
				doOutput(outputHandler, reportModel);
			}
		}
	}

	protected void doOutput(BaseOutput outputHandler, BaseReportModel reportModel)
	{
		Map<String, ReportWriter> writerMap = getWriterMap();
		outputHandler.outputReport(reportModel, writerMap);
	}

	protected abstract Map<String, ReportWriter> getWriterMap();

	public abstract List<ReportTransmissionType> getSupportedOutputs();

	public abstract List<ReportFormat> getSupportedFormats(String reportTransmissionType);

	protected String outputKey(String transmissionType, String reportFormat)
	{
		String key = transmissionType + "-" + reportFormat;
		return key;
	}

	/**
	 * @param reportModel
	 * @return html/text with a summary of the report
	 */
	public final String reportSummmaryDefault(T reportModel)
	{
		StringBuilder summary = new StringBuilder();
		summary.append("<h2>Report: ")
				.append(reportModel.getTitle())
				.append("</h2>");
		summary.append("Generated on ");
		summary.append(sdf.format(reportModel.getCreateTime()));
		summary.append(" is ready to be viewed. To view your report, log in then go to the reports section under <i>History</i><br>");

		return summary.toString();
	}

	/**
	 * Override to create summary; some outputs may use this. Example: EMAIL
	 *
	 * @param reportModel
	 * @return html/text with a summary of the report
	 */
	public String reportSummmary(T reportModel)
	{
		StringBuilder summary = new StringBuilder();
		summary.append("<h2>Report: ")
				.append(reportModel.getTitle())
				.append("</h2>");
		summary.append("Generated on ");
		summary.append(sdf.format(reportModel.getCreateTime()));
		summary.append(" is ready to be viewed. To view your report, log in then go to the reports section under <i>History</i><br>");

		return summary.toString();
	}

	/**
	 * Get user who created the report or scheduled report
	 *
	 * @return if user is not known or groups is unknown than only default group
	 * will return (Assumes the report belongs to a registered user)
	 */
	protected UserContext getReportUserContext()
	{
		UserContext userContext = service.getSecurityService().getUserContext(report.getCreateUser());
		if (userContext == null) {
			userContext = new UserContext();

			SecurityRole defaultRole = new SecurityRole();
			defaultRole.setRoleName(SecurityRole.DEFAULT_GROUP);
			defaultRole = defaultRole.find();
			if (defaultRole != null) {
				userContext.getRoles().add(defaultRole);
			}

		}

		return userContext;
	}

	protected void updateReportTimeRange()
	{
		if (report.getReportOption().getPreviousDays() != null) {
			Instant instantEnd = Instant.now();
			Instant instantStart = instantEnd.minus(report.getReportOption().getPreviousDays(), ChronoUnit.DAYS);
			report.getReportOption().setStartDts(TimeUtil.beginningOfDay(new Date(instantStart.toEpochMilli())));
			report.getReportOption().setEndDts(TimeUtil.endOfDay(new Date(instantEnd.toEpochMilli())));
		}

		if (report.getReportOption().getStartDts() == null) {
			report.getReportOption().setStartDts(TimeUtil.beginningOfDay(TimeUtil.currentDate()));
		} else {
			report.getReportOption().setStartDts(TimeUtil.beginningOfDay(report.getReportOption().getStartDts()));
		}

		if (report.getReportOption().getEndDts() == null) {
			report.getReportOption().setEndDts(TimeUtil.endOfDay(TimeUtil.currentDate()));
		} else {
			report.getReportOption().setEndDts(TimeUtil.endOfDay(report.getReportOption().getEndDts()));
		}
	}

}
