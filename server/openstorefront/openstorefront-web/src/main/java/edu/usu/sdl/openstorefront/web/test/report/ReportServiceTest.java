/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.test.report;

import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportOutput;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionOption;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.entity.ReportType;
import static edu.usu.sdl.openstorefront.core.entity.ReportType.COMPONENT;
import static edu.usu.sdl.openstorefront.core.entity.RunStatus.PENDING;
import edu.usu.sdl.openstorefront.core.entity.ScheduledReport;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This runs all report for view only and for one format only Still this is
 * expensive test especially if there is a lot of data.
 *
 * @author ccummings
 */
public class ReportServiceTest extends BaseTestCase
{

	private List<Report> reports = null;
	private ScheduledReport schedule = null;

	@Override
	protected void runInternalTest()
	{

		results.append("Generating reports...<br>");
		List<ReportType> types = service.getLookupService().findLookup(ReportType.class);

		reports = new ArrayList();

		for (ReportType type : types) {

			//only try one format
			List<ReportFormat> formats = service.getReportService().getSupportedFormats(type.getCode(), ReportTransmissionType.VIEW);

			if (!formats.isEmpty()) {

				Report report = new Report();
				report.setReportType(type.getCode());

				ReportOutput reportOutput = new ReportOutput();
				reportOutput.setReportTransmissionType(ReportTransmissionType.VIEW);
				ReportTransmissionOption options = new ReportTransmissionOption();
				options.setReportFormat(formats.get(0).getCode());
				reportOutput.setReportTransmissionOption(options);
				report.setReportOutputs(new ArrayList<>());
				report.getReportOutputs().add(reportOutput);

				reports.add(service.getReportService().generateReport(report));
			} else {
				addResultsLines("Unable to run " + type.getDescription() + " no View formats.");
			}
		}

		boolean isPending = true;
		int maxTimeCheck = 0;
		Report queryReport = new Report();
		queryReport.setRunStatus(PENDING);
		Set<String> allPendingReports = new HashSet();

		while (isPending || maxTimeCheck == 100) {
			isPending = false;
			List<Report> pendingReports = queryReport.findByExample();
			for (Report pending : pendingReports) {
				allPendingReports.add(pending.getReportId());
			}
			for (Report report : reports) {
				if (allPendingReports.contains(report.getReportId())) {
					isPending = true;
					break;
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {

			}
			maxTimeCheck++;
		}
		results.append("Reports complete<br><br>");

		results.append("Scheduling a report...<br>");

		schedule = new ScheduledReport();
		schedule.setReportType(COMPONENT);

		List<ReportFormat> formats = service.getReportService().getSupportedFormats(COMPONENT, ReportTransmissionType.VIEW);
		ReportOutput reportOutput = new ReportOutput();
		reportOutput.setReportTransmissionType(ReportTransmissionType.VIEW);
		ReportTransmissionOption options = new ReportTransmissionOption();
		options.setReportFormat(formats.get(0).getCode());
		reportOutput.setReportTransmissionOption(options);
		schedule.setReportOutputs(new ArrayList<>());
		schedule.getReportOutputs().add(reportOutput);

		schedule.setCreateUser(SecurityUtil.getCurrentUserName());
		schedule.setScheduleIntervalDays(1);
		schedule = service.getReportService().saveScheduledReport(schedule);

		ScheduledReport scheduledReportCheck = new ScheduledReport();
		scheduledReportCheck.setReportType(COMPONENT);
		scheduledReportCheck.setScheduleIntervalDays(1);
		scheduledReportCheck.setCreateUser(SecurityUtil.getCurrentUserName());
		scheduledReportCheck = scheduledReportCheck.find();
		if (scheduledReportCheck.getScheduleReportId().equals(schedule.getScheduleReportId())) {
			results.append("Report Scheduled<br><br>");
		} else {
			failureReason.append("Failed to schedule report<br><br>");
		}

		service.getReportService().deleteScheduledReport(schedule.getScheduleReportId());
		schedule = schedule.find();
		if (schedule == null) {
			results.append("Scheduled Report Deleted<br>");
		} else {
			failureReason.append("Unable to delete report<br>");
		}
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();
		if (!reports.isEmpty()) {
			for (Report deleteReport : reports) {
				service.getReportService().deleteReport(deleteReport.getReportId());
			}
		}
		if (schedule != null) {
			service.getReportService().deleteScheduledReport(schedule.getScheduleReportId());
		}
	}

	@Override
	public String getDescription()
	{
		return "Report Test";
	}

}
