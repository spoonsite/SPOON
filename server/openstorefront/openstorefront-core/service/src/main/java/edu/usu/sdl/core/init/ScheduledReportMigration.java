/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.core.init;

import edu.usu.sdl.openstorefront.core.entity.ReportOutput;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionOption;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.entity.ScheduledReport;
import edu.usu.sdl.openstorefront.service.manager.JobManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Convert remove in 2.6 This will convert the old scheduled reports to using
 * the new output modes
 *
 * @author dshurtleff
 */
public class ScheduledReportMigration
		extends ApplyOnceInit
{

	private static final Logger LOG = Logger.getLogger(ScheduledReportMigration.class.getName());

	public ScheduledReportMigration()
	{
		super("SREPORT-Migration-Init");
	}

	@Override
	protected String internalApply()
	{
		JobManager.pauseScheduler();
		StringBuilder result = new StringBuilder();
		try {

			//find all
			ScheduledReport scheduledReport = new ScheduledReport();

			List<ScheduledReport> reports = scheduledReport.findByExample();
			for (ScheduledReport report : reports) {
				List<ReportOutput> reportOutputs = new ArrayList<>();

				if (report.getReportOutputs() == null
						|| report.getReportOutputs().isEmpty()) {

					//move formats
					ReportOutput viewOutput = new ReportOutput();
					viewOutput.setReportTransmissionType(ReportTransmissionType.VIEW);
					ReportTransmissionOption option = new ReportTransmissionOption();
					option.setReportFormat(report.getReportFormat());
					viewOutput.setReportTransmissionOption(option);
					reportOutputs.add(viewOutput);

					//emails
					if (report.getEmailAddresses() != null) {
						ReportOutput emailOutput = new ReportOutput();
						emailOutput.setReportTransmissionType(ReportTransmissionType.EMAIL);
						option = new ReportTransmissionOption();
						option.setReportFormat(report.getReportFormat());
						option.setEmailAddresses(report.getEmailAddresses());
						emailOutput.setReportTransmissionOption(option);
					}

					report.setReportOutputs(reportOutputs);
					LOG.log(Level.INFO, "Converting Scheduled Report: " + report.getReportType() + " For " + report.getCreateUser());
					service.getReportService().saveScheduledReport(report);
				} else {
					LOG.log(Level.INFO, "Scheduled Report already Converted: " + report.getReportType() + " For " + report.getCreateUser());
				}
			}
			result.append("Converted: " + reports.size());
		} finally {
			JobManager.resumeScheduler();
		}

		return result.toString();
	}

	@Override
	public int getPriority()
	{
		return 30;
	}

}
