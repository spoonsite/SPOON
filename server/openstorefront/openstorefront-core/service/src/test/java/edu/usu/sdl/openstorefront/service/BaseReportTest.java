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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportType;
import edu.usu.sdl.openstorefront.report.AdminActionReport;
import edu.usu.sdl.openstorefront.report.BaseReport;
import edu.usu.sdl.openstorefront.report.CategoryComponentReport;
import edu.usu.sdl.openstorefront.report.ComponentDetailReport;
import edu.usu.sdl.openstorefront.report.ComponentOrganizationReport;
import edu.usu.sdl.openstorefront.report.ComponentReport;
import edu.usu.sdl.openstorefront.report.EvaluationStatusReport;
import edu.usu.sdl.openstorefront.report.ExternalLinkValidationReport;
import edu.usu.sdl.openstorefront.report.OrganizationReport;
import edu.usu.sdl.openstorefront.report.SubmissionsReport;
import edu.usu.sdl.openstorefront.report.UsageReport;
import edu.usu.sdl.openstorefront.report.UserReport;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Test of getReport method, of class BaseReport.
 * @author cyearsley
 */
@RunWith(Parameterized.class)
public class BaseReportTest
{
	
	private final String type;
	private final String expectedOutput;
	
	public BaseReportTest (String type, String expectedOutput)
	{
		this.type = type;
		this.expectedOutput = expectedOutput;
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> generateData()
	{
		//	The following arguments are: type and the expected class.
		//	Given the report type, the BaseReport factory should generate
		//		the appropriate class.
		return Arrays.asList(new Object[][]{
			{ReportType.COMPONENT, ComponentReport.class.toString()},
			{ReportType.USAGE, UsageReport.class.toString()},
			{ReportType.ORGANIZATION, OrganizationReport.class.toString()},
			{ReportType.COMPONENT_ORGANIZATION, ComponentOrganizationReport.class.toString()},
			{ReportType.USER, UserReport.class.toString()},
			{ReportType.LINK_VALIDATION, ExternalLinkValidationReport.class.toString()},
			{ReportType.SUBMISSION, SubmissionsReport.class.toString()},
			{ReportType.CATEGORY_COMPONENT, CategoryComponentReport.class.toString()},
			{ReportType.COMPONENT_DETAIL, ComponentDetailReport.class.toString()},
			{ReportType.EVALUATION_STATUS, EvaluationStatusReport.class.toString()},
			{ReportType.ACTION_REPORT, AdminActionReport.class.toString()}
		});
	}
	
	@Test
	public void testGetReport()
	{
		// Setup
		String reportFormat = ReportFormat.CSV;
		Report reportExample = new Report();
		reportExample.setReportFormat(reportFormat);
		reportExample.setReportType(type);
		
		//	Act + assert
		Assert.assertEquals(BaseReport.getReport(reportExample).getClass().toString(), expectedOutput);
	}
}
