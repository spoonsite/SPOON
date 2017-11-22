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
package edu.usu.sdl.openstorefront.report;

import edu.usu.sdl.openstorefront.core.api.SecurityService;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportOutput;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionOption;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.report.generator.HtmlGenerator;
import edu.usu.sdl.openstorefront.report.model.EntryListingReportModel;
import edu.usu.sdl.openstorefront.report.output.ConfluenceOutput;
import edu.usu.sdl.openstorefront.report.output.ReportWriter;
import edu.usu.sdl.openstorefront.security.UserContext;
import edu.usu.sdl.openstorefront.service.manager.ConfluenceManager;
import edu.usu.sdl.openstorefront.service.manager.model.confluence.Content;
import edu.usu.sdl.openstorefront.service.manager.resource.ConfluenceClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests the report output to confluence
 *
 * @author dshurtleff
 */
public class ConfluenceOutputTest
{

	/**
	 * The goal of the test is to test the code just before going to confluence.
	 * This is not has useful as the integration test that actually test
	 * confluence api which the bulk of issues would come form. However, this
	 * may useful as an example for testing outputs.
	 *
	 */
	@Test
	public void testConfluenceOutput()
	{

		ReportOutput reportOutput = new ReportOutput();
		ReportTransmissionOption option = new ReportTransmissionOption();
		option.setReportFormat(ReportFormat.HTML);
		reportOutput.setReportTransmissionOption(option);
		reportOutput.setReportTransmissionType(ReportTransmissionType.VIEW);

		Report report = new Report();
		report.setReportOutputs(new ArrayList<>());
		report.getReportOutputs().add(reportOutput);

		//need to mock ServiceProxy and report user;
		Service serviceMock = Mockito.mock(Service.class);
		UserContext userContext = new UserContext();

		SecurityService securityServiceMock = Mockito.mock(SecurityService.class);
		Mockito.when(serviceMock.getSecurityService()).thenReturn(securityServiceMock);
		Mockito.when(securityServiceMock.getUserContext(Mockito.any()))
				.thenReturn(userContext);

		FilterEngine filterEngineMock = Mockito.mock(FilterEngine.class);

		EntryListingReport entryListingReport = new EntryListingReport(report, serviceMock, filterEngineMock);

		ConfluenceOutput output = new ConfluenceOutput(reportOutput, report, entryListingReport, null);

		EntryListingReportModel reportModel = new EntryListingReportModel();
		Map<String, ReportWriter> reportMap = new HashMap<>();

		String key = ReportTransmissionType.VIEW + "-" + ReportFormat.HTML;
		reportMap.put(key, (generator, model) -> {
			HtmlGenerator html = (HtmlGenerator) generator;
			html.addLine("<b>This is a TEST</b>");
		});

		ConfluenceManager confluenceManagerMock = Mockito.mock(ConfluenceManager.class);
		ConfluenceClient confluenceClientMock = Mockito.mock(ConfluenceClient.class);
		Mockito.when(confluenceManagerMock.getClient()).thenReturn(confluenceClientMock);

		Mockito.when(confluenceClientMock.getPage(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
		Content content = new Content();
		Mockito.when(confluenceClientMock.createPage(Mockito.any())).thenReturn(content);

		output.setConfluenceManager(confluenceManagerMock);

		//This will throw error on fail
		output.outputReport(reportModel, reportMap);
	}

}
