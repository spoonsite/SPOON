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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.EmailAddress;
import edu.usu.sdl.openstorefront.core.entity.ErrorTypeCode;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportType;
import edu.usu.sdl.openstorefront.core.entity.RunStatus;
import edu.usu.sdl.openstorefront.core.entity.ScheduledReport;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.service.manager.MailManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import org.codemonkey.simplejavamail.email.Email;
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

	private static final Logger log = Logger.getLogger(ScheduledReportJob.class.getName());

	@Override
	protected void executeInternaljob(JobExecutionContext context)
	{
		ScheduledReport scheduleReportExample = new ScheduledReport();
		scheduleReportExample.setActiveStatus(ScheduledReport.ACTIVE_STATUS);

		List<ScheduledReport> reports = service.getPersistenceService().queryByExample(scheduleReportExample);
		for (ScheduledReport report : reports) {
			boolean run = true;

			if (report.getLastRanDts() != null) {
				Instant instant = Instant.ofEpochMilli(report.getLastRanDts().getTime());
				instant = instant.plus(report.getScheduleIntervalDays(), ChronoUnit.DAYS);
				if (Instant.now().isBefore(instant)) {
					run = false;
					if (log.isLoggable(Level.FINEST)) {
						log.log(Level.FINEST, MessageFormat.format("Not time to generate: {0} schedule report id: {1}", new Object[]{TranslateUtil.translate(ReportType.class, report.getReportType()), report.getScheduleReportId()}));
					}
				}
			}

			if (run) {

				Report reportHistory = new Report();
				reportHistory.setScheduled(true);
				reportHistory.setReportFormat(report.getReportFormat());
				reportHistory.setReportType(report.getReportType());
				reportHistory.setReportOption(report.getReportOption());
				reportHistory.setCreateUser(report.getCreateUser());
				reportHistory.setIds(report.getComponentIds());
				reportHistory.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);

				Report reportProcessed = service.getReportService().generateReport(reportHistory);

				report.setLastRanDts(TimeUtil.currentDate());
				report.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
				service.getReportService().saveScheduledReport(report);

				if (RunStatus.COMPLETE.equals(reportProcessed.getRunStatus())) {
					//send email
					String replyAddress = PropertiesManager.getValue(PropertiesManager.KEY_MAIL_REPLY_ADDRESS);

					StringBuilder message = new StringBuilder();
					message.append("Report is ready to be viewed. Please login then go to the reports section under History to view your report.<br><br><br>");
					message.append("To stop receiving this message, please contact an administrator at ").append(replyAddress);

					String applicationTitle = PropertiesManager.getValue(PropertiesManager.KEY_APPLICATION_TITLE, "Openstorefront");

					byte[] reportData = null;
					boolean attachFile = Convert.toBoolean(PropertiesManager.getValue(PropertiesManager.KEY_MAIL_ATTACH_FILE));
					if (attachFile) {
						Path path = reportProcessed.pathToReport();
						try {
							reportData = Files.readAllBytes(path);
						} catch (IOException ex) {
							throw new OpenStorefrontRuntimeException("Unable to read the report.", "Check disk permissions and disk space. ", ex, ErrorTypeCode.REPORT);
						}
					}

					if (report.getEmailAddresses() == null) {
						report.setEmailAddresses(new ArrayList<>());
					}
					for (EmailAddress emailAddress : report.getEmailAddresses()) {
						Email email = MailManager.newEmail();
						email.setSubject(applicationTitle + " - " + TranslateUtil.translate(ReportType.class, report.getReportType()) + " Report");
						email.setTextHTML(message.toString());

						if (attachFile && reportData != null) {
							String extension = OpenStorefrontConstant.getFileExtensionForMime(ReportFormat.mimeType(report.getReportFormat()));
							email.addAttachment(TranslateUtil.translate(ReportType.class, report.getReportType()) + extension, reportData, ReportFormat.mimeType(report.getReportFormat()));
						}
						
						email.addRecipient("", emailAddress.getEmail(), Message.RecipientType.TO);
						MailManager.send(email);
					}
				} else {
					log.log(Level.SEVERE, MessageFormat.format("A scheduled report failed to generate: {0} schedule report id: {1}", new Object[]{TranslateUtil.translate(ReportType.class, report.getReportType()), report.getScheduleReportId()}));
				}
			}
		}
	}

}
