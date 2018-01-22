/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.api.ReportService;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.ErrorTypeCode;
import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;
import edu.usu.sdl.openstorefront.core.entity.NotificationEventType;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.entity.ReportType;
import edu.usu.sdl.openstorefront.core.entity.RunStatus;
import edu.usu.sdl.openstorefront.core.entity.ScheduledReport;
import edu.usu.sdl.openstorefront.core.model.ErrorInfo;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.report.BaseReport;
import edu.usu.sdl.openstorefront.service.manager.JobManager;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;

/**
 * Handles report work-flow
 *
 * @author dshurtleff
 */
public class ReportServiceImpl
		extends ServiceProxy
		implements ReportService
{

	private static final Logger LOG = Logger.getLogger(OrientPersistenceService.class.getName());

	private static final int MAX_RETRIES = 3;

	@Override
	public Report queueReport(Report report)
	{
		Objects.requireNonNull(report, "Report is required");

		report.setReportId(persistenceService.generateId());
		report.setRunStatus(RunStatus.PENDING);
		report.populateBaseCreateFields();
		report = persistenceService.persist(report);
		report = persistenceService.deattachAll(report);
		return report;
	}

	@Override
	public Report generateReport(Report report)
	{
		Objects.requireNonNull(report, "Report is required");

		//save Report
		Report managedReport;
		if (StringUtils.isBlank(report.getReportId())) {
			queueReport(report);
			managedReport = persistenceService.findById(Report.class, report.getReportId());
		} else {
			managedReport = persistenceService.findById(Report.class, report.getReportId());
		}
		if (managedReport == null) {
			throw new OpenStorefrontRuntimeException("Unable to find report.", "Check report id: " + report.getReportId());
		}

		managedReport.setRunStatus(RunStatus.WORKING);
		managedReport.setUpdateDts(TimeUtil.currentDate());
		managedReport.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
		persistenceService.persist(managedReport);

		//run report
		try {
			BaseReport reportGenerator = BaseReport.getReport(managedReport);
			reportGenerator.generateReport();

			//retry if out of date (for some reason DB may not be in sync at this point...find pulls an old record; cache delay?)
			for (int i = 0; i < MAX_RETRIES; i++) {
				try {
					managedReport = persistenceService.findById(Report.class, managedReport.getReportId());
					managedReport.setRunStatus(RunStatus.COMPLETE);
					managedReport.setUpdateDts(TimeUtil.currentDate());
					managedReport.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
					persistenceService.persist(managedReport);
					break;
				} catch (Exception e) {
					if (i == (MAX_RETRIES - 1)) {
						throw e;
					}
				}
			}

			if (OpenStorefrontConstant.ANONYMOUS_USER.equals(managedReport.getCreateUser()) == false) {
				NotificationEvent notificationEvent = new NotificationEvent();
				notificationEvent.setEventType(NotificationEventType.REPORT);
				notificationEvent.setUsername(managedReport.getCreateUser());
				notificationEvent.setMessage("Report: " + TranslateUtil.translate(ReportType.class, managedReport.getReportType()) + " has finished generating.");
				notificationEvent.setEntityName(Report.class.getSimpleName());
				notificationEvent.setEntityId(managedReport.getReportId());
				getNotificationService().postEvent(notificationEvent);
			}

		} catch (Exception e) {
			managedReport = persistenceService.findById(Report.class, managedReport.getReportId());
			managedReport.setRunStatus(RunStatus.ERROR);
			managedReport.setUpdateDts(TimeUtil.currentDate());
			managedReport.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
			persistenceService.persist(managedReport);

			ErrorInfo errorInfo = new ErrorInfo(e, null);
			errorInfo.setErrorTypeCode(ErrorTypeCode.REPORT);
			errorInfo.setInputData("Report: " + managedReport.getReportType() + " Format: " + managedReport.getReportFormat() + " Report Id: " + managedReport.getReportId() + " Create Date: " + managedReport.getCreateDts());
			getSystemService().generateErrorTicket(errorInfo);

			//Note: we don't need to rethrow as we want to track report error separately.
		}

		return managedReport;
	}

	@Override
	public void deleteReport(String reportId)
	{
		Report existingReport = persistenceService.findById(Report.class, reportId);
		if (existingReport != null) {

			Path path = existingReport.pathToReport();
			if (path != null) {
				if (path.toFile().exists()) {
					boolean success = path.toFile().delete();
					if (success == false) {
						LOG.log(Level.WARNING, MessageFormat.format("Unable to remove old report: {0}", path.toString()));
					}
				}
			}
			persistenceService.delete(existingReport);
		}
	}

	@Override
	public List<ReportFormat> getSupportedFormats(String reportType, String reportTranmissionType)
	{
		Report report = new Report();
		report.setReportType(reportType);
		BaseReport baseReport = BaseReport.getReport(report);
		List<ReportFormat> reportFormat = baseReport.getSupportedFormats(reportTranmissionType);
		return reportFormat;
	}

	@Override
	public List<ReportTransmissionType> getSupportedOutputs(String reportType)
	{
		Report report = new Report();
		report.setReportType(reportType);
		BaseReport baseReport = BaseReport.getReport(report);
		List<ReportTransmissionType> transmissionTypes = baseReport.getSupportedOutputs();
		return transmissionTypes;
	}

	@Override
	public ScheduledReport saveScheduledReport(ScheduledReport scheduledReport)
	{
		Objects.requireNonNull(scheduledReport, "Schedule report is required");

		ScheduledReport existing = persistenceService.findById(ScheduledReport.class, scheduledReport.getScheduleReportId());
		if (existing != null) {
			existing.updateFields(scheduledReport);
			persistenceService.persist(existing);
		} else {
			scheduledReport.setScheduleReportId(persistenceService.generateId());
			scheduledReport.populateBaseCreateFields();
			persistenceService.persist(scheduledReport);
		}

		if (StringUtils.isNotBlank(scheduledReport.getScheduleIntervalCron())) {
			try {
				JobManager.addReportJob(scheduledReport.getScheduleReportId(), scheduledReport.getReportType(), scheduledReport.getScheduleIntervalCron());
			} catch (SchedulerException ex) {
				throw new OpenStorefrontRuntimeException("Unable to create the cron job for report.", "See error ticket/log for issue", ex);
			}
		}

		return scheduledReport;
	}

	@Override
	public void deleteScheduledReport(String scheduledReportId)
	{
		ScheduledReport scheduledReport = persistenceService.findById(ScheduledReport.class, scheduledReportId);
		if (scheduledReport != null) {
			persistenceService.delete(scheduledReport);
		}
	}

	@Override
	public void deleteExpiredReports()
	{
		//	go through all reports and delete those which are
		//		"expired" - is older than configured report lifetime.
		LocalDate expirationLocalDate;
		LocalDate currentDate = LocalDate.now();

		expirationLocalDate = currentDate.minusDays(Integer.parseInt(PropertiesManager.getValueDefinedDefault(PropertiesManager.KEY_REPORT_LIFETIME)) - 1);
		Date expirationDate = Date.from(expirationLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		Report reportExample = new Report();
		Report filteredReportExample = new Report();
		filteredReportExample.setCreateDts(expirationDate);

		//	Query reports that are older than the configured expiration value
		QueryByExample<Report> queryByExample = new QueryByExample<>(reportExample);
		SpecialOperatorModel<Report> specialOperatorModel = new SpecialOperatorModel<>();
		specialOperatorModel.setExample(filteredReportExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		List<Report> results = getPersistenceService().queryByExample(queryByExample);

		//	Remove expired reports
		for (Report report : results) {
			deleteReport(report.getReportId());
		}
	}

	@Override
	public void disableAllScheduledReportsForUser(String username)
	{
		ScheduledReport scheduledReportExample = new ScheduledReport();
		scheduledReportExample.setCreateUser(username);

		ScheduledReport scheduledReportSetExample = new ScheduledReport();
		scheduledReportSetExample.setActiveStatus(ScheduledReport.INACTIVE_STATUS);
		scheduledReportSetExample.populateBaseUpdateFields();
		persistenceService.updateByExample(ScheduledReport.class, scheduledReportSetExample, scheduledReportExample);
	}

	@Override
	public void runScheduledReportNow(ScheduledReport scheduledReport)
	{
		Objects.requireNonNull(scheduledReport);

		Report reportHistory = new Report();
		reportHistory.setScheduled(true);
		reportHistory.setScheduledId(scheduledReport.getScheduleReportId());
		reportHistory.setReportFormat(scheduledReport.getReportFormat());
		reportHistory.setReportType(scheduledReport.getReportType());
		reportHistory.setReportOption(scheduledReport.getReportOption());
		reportHistory.setCreateUser(scheduledReport.getCreateUser());
		reportHistory.setIds(scheduledReport.getIds());
		reportHistory.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
		reportHistory.setReportOutputs(scheduledReport.getReportOutputs());

		Report reportProcessed = generateReport(reportHistory);

		//repull as the report may be inactive or delete while this was running
		ScheduledReport existingReport = persistenceService.findById(ScheduledReport.class, scheduledReport.getScheduleReportId());

		if (existingReport != null) {
			scheduledReport.setLastRanDts(TimeUtil.currentDate());
			scheduledReport.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
			saveScheduledReport(scheduledReport);
		} else {
			LOG.log(Level.FINE, "Scheduled report was removed.  Old Id: " + scheduledReport.getScheduleReportId());
		}

		if (RunStatus.ERROR.equals(reportProcessed.getRunStatus())) {
			LOG.log(Level.SEVERE, MessageFormat.format("A scheduled report failed to generate: {0} schedule report id: {1}", new Object[]{TranslateUtil.translate(ReportType.class, scheduledReport.getReportType()), scheduledReport.getScheduleReportId()}));
		}
	}

	@Override
	public void updateStatusOnScheduledReport(String scheduledReportId, String activeStatus)
	{
		Objects.requireNonNull(scheduledReportId);
		Objects.requireNonNull(activeStatus);

		ScheduledReport report = persistenceService.findById(ScheduledReport.class, scheduledReportId);
		if (report != null) {

			boolean updateRecord = true;
			boolean addJob = false;
			switch (activeStatus) {
				case ScheduledReport.ACTIVE_STATUS:
					addJob = true;
					break;
				case ScheduledReport.INACTIVE_STATUS:
					addJob = false;
					break;
				default:
					LOG.log(Level.FINE, MessageFormat.format("Active Status not supported: {0}", activeStatus));
					updateRecord = false;
					break;
			}

			if (updateRecord) {
				report.setActiveStatus(activeStatus);
				report.populateBaseUpdateFields();
				persistenceService.persist(report);
			}

			if (addJob
					&& StringUtils.isNotBlank(report.getScheduleIntervalCron())) {
				try {
					JobManager.addReportJob(scheduledReportId, report.getReportType(), report.getScheduleIntervalCron());
				} catch (SchedulerException ex) {
					throw new OpenStorefrontRuntimeException("Unable to schedule report.", ex);
				}
			} else {
				JobManager.removeReportJob(scheduledReportId);
			}

		} else {
			throw new OpenStorefrontRuntimeException("Unable to find scheduled report", "Check id");
		}
	}

}
