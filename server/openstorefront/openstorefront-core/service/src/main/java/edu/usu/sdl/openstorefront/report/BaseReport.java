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
import edu.usu.sdl.openstorefront.core.entity.Branding;
import edu.usu.sdl.openstorefront.core.entity.ErrorTypeCode;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportOption;
import edu.usu.sdl.openstorefront.core.entity.ReportType;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 *
 * @author dshurtleff
 */
public abstract class BaseReport
{

	protected Report report;
	protected BaseGenerator generator;
	protected ServiceProxy service = ServiceProxy.getProxy();

	//Note: not thread-safe
	protected SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
	
	private Branding branding;

	public BaseReport(Report report)
	{
		Objects.requireNonNull(report, "Report must set");

		this.report = report;
		if (this.report.getReportOption() == null) {
			this.report.setReportOption(new ReportOption());
		}
		generator = BaseGenerator.getGenerator(report);
	}

	public static BaseReport getReport(Report report)
	{
		BaseReport baseReport = null;
		if (null != report.getReportType()) {
			switch (report.getReportType()) {
				case ReportType.COMPONENT:
					baseReport = new ComponentReport(report);
					break;
				case ReportType.USAGE:
					baseReport = new UsageReport(report);
					break;
				case ReportType.ORGANIZATION:
					baseReport = new OrganizationReport(report);
					break;
				case ReportType.COMPONENT_ORGANIZATION:
					baseReport = new ComponentOrganizationReport(report);
					break;
				case ReportType.USER:
					baseReport = new UserReport(report);
					break;
				case ReportType.LINK_VALIDATION:
					baseReport = new ExternalLinkValidationReport(report);
					break;
				case ReportType.SUBMISSION:
					baseReport = new SubmissionsReport(report);
					break;
				case ReportType.CATEGORY_COMPONENT:
					baseReport = new CategoryComponentReport(report);
					break;
				case ReportType.COMPONENT_DETAIL:
					baseReport = new ComponentDetailReport(report);
					break;
				case ReportType.EVALUATION_STATUS:
					baseReport = new EvaluationStatusReport(report);
					break;
				case ReportType.ACTION_REPORT:
					baseReport = new AdminActionReport(report);
					break;
				default:
					throw new OpenStorefrontRuntimeException("Report Type not supported", "Check type and/or add support. Type: " + report.getReportType(), ErrorTypeCode.REPORT);
			}
		} else {
			Objects.requireNonNull(report.getReportType(), "Report Type required");
		}
		return baseReport;
	}

	public Branding getBranding() {
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
		generator.init();
		try {
			gatherData();
			writeReport();
		} catch (Exception e) {
			generator.setFailed(true);
			throw new OpenStorefrontRuntimeException("Report failed to generate.", e, ErrorTypeCode.REPORT);
		} finally {
			generator.finish();
		}
	}

	protected abstract void gatherData();

	protected abstract void writeReport();

}
