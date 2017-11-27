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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.entity.ScheduledReport;
import java.util.List;

/**
 * Handles generation of report and tracking them
 *
 * @author dshurtleff
 */
public interface ReportService
		extends AsyncService
{

	/**
	 * Adds a report to the report queue Note: The report still needs to be
	 * generated as a separate step. This allow for getting a handling on the
	 * report. While the generation may occur in the background.
	 *
	 * @param report
	 * @return Managed report
	 */
	public Report queueReport(Report report);

	/**
	 * Generates a report; The report should be queued prior to this call.
	 * However, calling this directly will still work as it queue the report if
	 * the report is new.
	 *
	 * @param report
	 * @return updated report entity
	 */
	public Report generateReport(Report report);

	/**
	 * WARNING: This is a hard delete of the report
	 *
	 * @param reportId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteReport(String reportId);

	/**
	 * Gets the supported formats for a report
	 *
	 * @param reportType
	 * @param reportTranmissionType
	 * @return Report Type, Value list reportFormats keys
	 */
	public List<ReportFormat> getSupportedFormats(String reportType, String reportTranmissionType);

	/**
	 * Gets the report output for a given type
	 *
	 * @param reportType
	 * @return
	 */
	public List<ReportTransmissionType> getSupportedOutputs(String reportType);

	/**
	 * Saves a scheduled report
	 *
	 * @param scheduledReport
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public ScheduledReport saveScheduledReport(ScheduledReport scheduledReport);

	/**
	 * This is hard delete of the record
	 *
	 * @param scheduledReportId
	 */
	public void deleteScheduledReport(String scheduledReportId);

	/**
	 * In-activates all scheduled report for a user
	 *
	 * @param username
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void disableAllScheduledReportsForUser(String username);

	/**
	 * Removes all expired report
	 */
	public void deleteExpiredReports();

	/**
	 * Typically this is called from a job
	 *
	 * @param scheduledReport
	 */
	public void runScheduledReportNow(ScheduledReport scheduledReport);

	/**
	 * This will set status and add/or remove scheduled report if cron-based;
	 *
	 * @param scheduledReportId
	 * @param activeStatus
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void updateStatusOnScheduledReport(String scheduledReportId, String activeStatus);

}
