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
import java.util.ArrayList;
import java.util.List;

/**
 * Convert remove in 2.6 This will convert the old scheduled reports to using
 * the new output modes
 *
 * @author dshurtleff
 */
public class ScheduledReportMigration
		extends ApplyOnceInit
{

	public ScheduledReportMigration()
	{
		super("SREPORT-Migration-Init");
	}

	@Override
	protected String internalApply()
	{
		//find all
		ScheduledReport scheduledReport = new ScheduledReport();

		List<ScheduledReport> reports = scheduledReport.findByExample();
		for (ScheduledReport report : reports) {
			List<ReportOutput> reportOutputs = new ArrayList<>();

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
			service.getReportService().saveScheduledReport(scheduledReport);
		}

		return "Converted: " + reports.size();
	}

	@Override
	public int getPriority()
	{
		return 30;
	}

}
