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

import static edu.usu.sdl.openstorefront.core.entity.StandardEntity.LOG;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.GeneratorOptions;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
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
	 * Test of getGenerator method, of class BaseGenerator.
	 */
	@BeforeClass
	public static void setup()
	{
//		init dir
		baseDir = System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID().toString();
		File file = new File(baseDir);
		file.mkdirs();
		System.setProperty("application.datadir", baseDir);
	}

	@AfterClass
	public static void tearDown()
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

	/**
	 * Failing test of getGenerator method, of class BaseGenerator.
	 */
	@Test
	public void failTestGetGenerator()
	{
		try {

			GeneratorOptions generatorOptions = new GeneratorOptions();

			BaseGenerator.getGenerator("This is a bad ReportFormat value!", generatorOptions);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
			Assert.assertEquals("Unsupported report format: This is a bad ReportFormat value!", e.getMessage());
		}
	}

	/**
	 * Test of finish method (if failed flag == true), of class BaseGenerator.
	 */
	@Test
	public void testFinish()
	{
		//	Setup example report  (This test was changed on 2.6)

		//This will create merge conflict....just use 2.6
//		Report reportExample = new Report();
//		reportExample.setReportId(tempReportId);
//
//		GeneratorOptions generatorOptions = new GeneratorOptions();
//		generatorOptions.setReport(reportExample);
//
//		BaseGenerator generatorExample = new BaseGenerator(generatorOptions)
//		{
//			@Override
//			public void init()
//			{
//			}
//
//			@Override
//			protected void internalFinish()
//			{
//				this.setFailed(Boolean.TRUE);
//			}
//		};
//
//		//	Create temp file
//		Path pathToReport = reportExample.pathToReport();
//		try {
//			Files.deleteIfExists(pathToReport);
//			Files.write(pathToReport, new ArrayList<>(), Charset.forName("UTF-8"));
//		} catch (IOException ex) {
//			Logger.getLogger(BaseGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
//		}
//
//		//	Check both states of the file, before and after finish method is called.
//		//	Check the "Failed" state of the report is toggled to true
//		//		as it was set to "false" in the "internalFinish" method.
//		Assert.assertEquals(Boolean.TRUE, (Boolean) new File(pathToReport.toString()).exists());
//		generatorExample.finish();
//		Assert.assertEquals(Boolean.TRUE, (Boolean) generatorExample.isFailed());
//		Assert.assertEquals(Boolean.FALSE, (Boolean) new File(pathToReport.toString()).exists());
	}
}
