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
package edu.usu.sdl.openstorefront.service.job;

import edu.usu.sdl.openstorefront.core.entity.ScheduledReport;
import edu.usu.sdl.openstorefront.service.manager.JobManager;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * This expected to run one schedule report at a time
 *
 * @author dshurtleff
 */
@DisallowConcurrentExecution
public class ScheduledReportCronJob
		extends BaseJob
{

	private static final Logger LOG = Logger.getLogger(ScheduledReportCronJob.class.getName());

	public static final String SCHEDULED_REPORT_ID = "SCHEDULED_REPORT_ID";

	@Override
	protected void executeInternaljob(JobExecutionContext context)
	{

		String reportId = (String) context.get(SCHEDULED_REPORT_ID);
		if (StringUtils.isNotBlank(reportId)) {
			ScheduledReport scheduleReportExample = new ScheduledReport();
			scheduleReportExample.setActiveStatus(ScheduledReport.ACTIVE_STATUS);
			scheduleReportExample.setScheduleReportId(reportId);

			ScheduledReport report = scheduleReportExample.find();
			if (report != null) {
				service.getReportService().runScheduledReportNow(report);
			} else {
				LOG.log(Level.FINE, MessageFormat.format("Scheduled report can not be found OR is not active (removing job).  Report Id: {0}", reportId));
				JobManager.removeReportJob(context.getJobDetail().getKey().getName());
			}
		} else {
			LOG.log(Level.INFO, "Job is missing SCHEDULED_REPORT_ID in job context");
		}
	}

}
