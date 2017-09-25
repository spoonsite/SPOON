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
package edu.usu.sdl.openstorefront.report.generator;

import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public abstract class BaseGenerator
{

	private static final Logger log = Logger.getLogger(BaseGenerator.class.getName());

	protected Report report;
	private boolean failed;

	public BaseGenerator(Report report)
	{
		this.report = report;
	}

	public static BaseGenerator getGenerator(Report report)
	{
		BaseGenerator generator = null;
		switch (report.getReportFormat()) {
			case ReportFormat.CSV:
				generator = new CSVGenerator(report);
				break;
			case ReportFormat.HTML:
				generator = new HtmlGenerator(report);
				break;
			case ReportFormat.PDF:
				generator = new HtmlToPdfGenerator(report);
				break;
			default:
				throw new UnsupportedOperationException("Unsupported report format: " + report.getReportFormat());
		}
		return generator;
	}

	public abstract void init();

	public void finish()
	{
		internalFinish();
		if (failed) {
			Path path = report.pathToReport();
			if (path != null) {
				try {
					Files.deleteIfExists(path);
				} catch (IOException ex) {
					log.log(Level.WARNING, "Unable to cleanup failed report.", ex);
				}
			}
		}
	}

	protected abstract void internalFinish();

	public boolean isFailed()
	{
		return failed;
	}

	public void setFailed(boolean failed)
	{
		this.failed = failed;
	}

}
