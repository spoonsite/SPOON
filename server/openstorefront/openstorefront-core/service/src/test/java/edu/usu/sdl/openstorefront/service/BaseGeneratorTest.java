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
import static edu.usu.sdl.openstorefront.core.entity.StandardEntity.LOG;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import edu.usu.sdl.openstorefront.report.generator.HtmlGenerator;
import edu.usu.sdl.openstorefront.report.generator.HtmlToPdfGenerator;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author cyearsley
 */
public class BaseGeneratorTest
{
	String tempReportId = "TEMP-REPORT-ID";
	private static String baseDir;
	/**
	 *	Test of getGenerator method, of class BaseGenerator.
	 */
	
	@BeforeClass
	public static void setup ()
	{
//		init dir
		baseDir = System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID().toString();
		File file = new File(baseDir);
		file.mkdirs();
		System.setProperty("application.datadir", baseDir);
	}
	
	@AfterClass
	public static void tearDown ()
	{
		File file = new File(baseDir);
			if (file.exists()) {
				try {
					FileUtils.deleteDirectory(file);
				} catch (IOException ex) {
					LOG.warning("Unable to delete tomcat directory");
				}
			}
	}
	
	@Test
	public void testGetGenerator ()
	{
		//	Setup example reports with CSV, HTML, and PDF formats
		Report csvReportExample = new Report();
		csvReportExample.setReportFormat(ReportFormat.CSV);
		
		Report htmlRportExample = new Report();
		htmlRportExample.setReportFormat(ReportFormat.HTML);
		
		Report pdfRportExample = new Report();
		pdfRportExample.setReportFormat(ReportFormat.PDF);
		
		//	Generator classes should be the same
		Assert.assertEquals(BaseGenerator.getGenerator(csvReportExample).getClass().toString(), CSVGenerator.class.toString());
		Assert.assertEquals(BaseGenerator.getGenerator(htmlRportExample).getClass().toString(), HtmlGenerator.class.toString());
		Assert.assertEquals(BaseGenerator.getGenerator(pdfRportExample).getClass().toString(), HtmlToPdfGenerator.class.toString());
	}
	
	/**
	 *	Failing test of getGenerator method, of class BaseGenerator.
	 */
	@Test
	public void failTestGetGenerator ()
	{
		try {
			//	Setup example report
			Report reportExample = new Report();
			reportExample.setReportFormat("This is a bad ReportFormat value!");
			
			BaseGenerator.getGenerator(reportExample);
			Assert.fail("Expected UnsupportedOperationException");
		}
		catch (UnsupportedOperationException e) {
			Assert.assertEquals("Unsupported report format: This is a bad ReportFormat value!", e.getMessage());
		}
	}
	
	/**
	 *	Test of finish method (if failed flag == true), of class BaseGenerator.
	 */
	@Test
	public void testFinish ()
	{
		//	Setup example report
		Report reportExample = new Report();
		reportExample.setReportId(tempReportId);
		
		BaseGenerator generatorExample = new BaseGenerator(reportExample) {
			@Override
			public void init(){}
			@Override
			protected void internalFinish(){this.setFailed(Boolean.TRUE);}
		};
		
		//	Create temp file
		Path pathToReport = reportExample.pathToReport();
		try {
			Files.deleteIfExists(pathToReport);
			Files.write(pathToReport, new ArrayList<>(), Charset.forName("UTF-8"));
		}
		catch (IOException ex) {
			Logger.getLogger(BaseGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
		}
			
		//	Check both states of the file, before and after finish method is called.
		//	Check the "Failed" state of the report is toggled to true
		//		as it was set to "false" in the "internalFinish" method.
		try {
			Assert.assertEquals(Boolean.TRUE, (Boolean) new File(pathToReport.toString()).exists());
			generatorExample.finish();
			Assert.fail();
		}
		catch (Exception e) {
			Assert.assertEquals(Boolean.TRUE, (Boolean) generatorExample.isFailed());
			Assert.assertEquals(Boolean.FALSE, (Boolean) new File(pathToReport.toString()).exists());
		}
	}
}
