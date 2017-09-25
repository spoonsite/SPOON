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
import edu.usu.sdl.openstorefront.core.entity.ReportType;
import edu.usu.sdl.openstorefront.core.entity.RunStatus;
import edu.usu.sdl.openstorefront.core.entity.ScheduledReport;
import edu.usu.sdl.openstorefront.core.model.ErrorInfo;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.report.BaseReport;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * Handles report work-flow
 *
 * @author dshurtleff
 */
public class ReportServiceImpl
		extends ServiceProxy
		implements ReportService
{

	private static final Logger log = Logger.getLogger(OrientPersistenceService.class.getName());

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
			managedReport = queueReport(report);
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
						log.log(Level.WARNING, MessageFormat.format("Unable to remove old report: {0}", path.toString()));
					}
				}
			}
			persistenceService.delete(existingReport);
		}
	}

	@Override
	public Map<String, List<String>> getSupportedFormats()
	{
		Map<String, List<String>> formatMap = new HashMap<>();

		//formats
		List<ReportType> reportTypes = getLookupService().findLookup(ReportType.class);
		reportTypes.stream().forEach((reportType) -> {
			formatMap.put(reportType.getCode(), reportType.getSupportedFormats());
		});

		return formatMap;
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
		
		try {
			expirationLocalDate = currentDate.minusDays(Integer.parseInt(PropertiesManager.getValue(PropertiesManager.KEY_REPORT_LIFETIME)));
		}
		catch (NumberFormatException e) {
			//	If the configured report lifetime is invalid, fallback to the default value for the report lifetime
			expirationLocalDate = currentDate.minusDays(Integer.parseInt(PropertiesManager.getValueDefinedDefault(PropertiesManager.KEY_REPORT_LIFETIME)));
		}
		Date expirationDate = Date.from(expirationLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		Report reportExample = new Report();
		Report filteredReportExample = new Report();
		filteredReportExample.setCreateDts(expirationDate);
		
		//	Query reports that are older than the configured expiration value
		QueryByExample queryByExample = new QueryByExample(reportExample);
		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.setExample(filteredReportExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);
		
		List<Report> results = getPersistenceService().queryByExample(queryByExample);
		
		//	Remove expired reports
		for (Report report : results) {
			deleteReport(report.getReportId());
		}
	}

}
