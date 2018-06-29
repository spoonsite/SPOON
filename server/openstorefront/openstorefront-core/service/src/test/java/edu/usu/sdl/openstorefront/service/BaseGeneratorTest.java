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
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.CSVGenerator;
import edu.usu.sdl.openstorefront.report.generator.GeneratorOptions;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author cyearsley
 */
public class BaseGeneratorTest
{

	private static final Logger LOG = Logger.getLogger(BaseGeneratorTest.class.getName());

	private static String baseDir;

	/**
	 * Test of getGenerator method, of class BaseGenerator.
	 */
	@BeforeClass
	public static void setup()
	{
//		init dir (Not Used Currently)

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
				LOG.warning("Unable to delete directory");
			}
		}
	}

	/**
	 * Failing test of getGenerator method, of class BaseGenerator.
	 */
	@Test
	public void testGetGenerator()
	{
		LOG.info("Scenario 1: Good Generator");
		Report report = Mockito.mock(Report.class);

		Mockito.when(report.getReportId()).thenReturn("1");
		Mockito.when(report.pathToReport()).thenReturn(Paths.get(baseDir, "report1"));

		GeneratorOptions generatorOptions = new GeneratorOptions();
		generatorOptions.setReport(report);
		BaseGenerator generator = BaseGenerator.getGenerator(ReportFormat.CSV, generatorOptions);
		generator.finish();
		if (!(generator instanceof CSVGenerator)) {
			Assert.fail("Expected to get a CSV Generator");
		}

		LOG.info("Scenario 2: Bad Generator");
		try {
			generatorOptions = new GeneratorOptions();

			BaseGenerator.getGenerator("This is a bad ReportFormat value!", generatorOptions);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
			Assert.assertEquals("Unsupported report format: This is a bad ReportFormat value!", e.getMessage());
		}
	}

}
