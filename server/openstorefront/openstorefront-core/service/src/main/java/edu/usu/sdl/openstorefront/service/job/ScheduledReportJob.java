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
package edu.usu.sdl.openstorefront.service.job;

import edu.usu.sdl.openstorefront.core.entity.ReportType;
import edu.usu.sdl.openstorefront.core.entity.ScheduledReport;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 *
 * @author dshurtleff
 */
@DisallowConcurrentExecution
public class ScheduledReportJob
		extends BaseJob
{

	private static final Logger LOG = Logger.getLogger(ScheduledReportJob.class.getName());

	@Override
	protected void executeInternaljob(JobExecutionContext context)
	{
		ScheduledReport scheduleReportExample = new ScheduledReport();
		scheduleReportExample.setActiveStatus(ScheduledReport.ACTIVE_STATUS);

		List<ScheduledReport> reports = service.getPersistenceService().queryByExample(scheduleReportExample);
		for (ScheduledReport report : reports) {
			boolean run = true;

			if (report.getScheduleIntervalDays() != null) {
				run = checkReportRunTime(scheduleReportExample, ChronoUnit.DAYS);
			} else if (report.getScheduleIntervalMinutes() != null) {
				run = checkReportRunTime(scheduleReportExample, ChronoUnit.MINUTES);
			} else {
				//Filter out cron base reports
				run = false;
			}

			if (run) {
				service.getReportService().runScheduledReportNow(report);
			}
		}
	}

	private boolean checkReportRunTime(ScheduledReport scheduledReport, ChronoUnit chronoUnit)
	{
		boolean run = true;
		if (scheduledReport.getLastRanDts() != null) {
			Instant instant = Instant.ofEpochMilli(scheduledReport.getLastRanDts().getTime());
			instant = instant.plus(scheduledReport.getScheduleIntervalMinutes(), chronoUnit);
			if (Instant.now().isBefore(instant)) {
				run = false;
				if (LOG.isLoggable(Level.FINEST)) {
					LOG.log(Level.FINEST, MessageFormat.format("Not time to generate: {0} schedule report id: {1}", new Object[]{TranslateUtil.translate(ReportType.class, scheduledReport.getReportType()), scheduledReport.getScheduleReportId()}));
				}
			}
		}
		return run;
	}

}
