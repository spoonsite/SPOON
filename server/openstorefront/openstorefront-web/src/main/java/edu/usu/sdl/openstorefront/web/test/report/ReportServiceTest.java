/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.test.report;

import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportType;
import static edu.usu.sdl.openstorefront.core.entity.RunStatus.PENDING;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ccummings
 */
public class ReportServiceTest extends BaseTestCase
{
	@Override
	protected void runInternalTest()
	{
		List<ReportType> types = service.getLookupService().findLookup(ReportType.class);
		List<String> reportTypes = new ArrayList();
		for (ReportType reportType : types) {
			reportTypes.add(reportType.getCode());
		}

		List<ReportFormat> formatList = service.getLookupService().findLookup(ReportFormat.class);
		List<String> reportFormats = new ArrayList();
		for (ReportFormat format : formatList) {
			reportFormats.add(format.getCode());
		}

		Map<String, List<String>> formats = service.getReportService().getSupportedFormats();

		for (int i = 0; i < reportTypes.size(); i++) {
			Report report = new Report();
			report.setReportType(reportTypes.get(i));
			report.setReportFormat(reportFormats.get(0));
			report.setRunStatus(PENDING);
			report = service.getReportService().generateReport(report);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

	}

	@Override
	public String getDescription()
	{
		return "Report Test";
	}

}
