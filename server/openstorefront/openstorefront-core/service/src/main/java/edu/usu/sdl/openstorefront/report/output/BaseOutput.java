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
package edu.usu.sdl.openstorefront.report.output;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportOutput;
import edu.usu.sdl.openstorefront.core.entity.ReportTransmissionType;
import edu.usu.sdl.openstorefront.report.BaseReport;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.model.BaseReportModel;
import java.text.MessageFormat;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public abstract class BaseOutput
{

	private static final Logger LOG = Logger.getLogger(BaseOutput.class.getName());

	protected ReportOutput reportOutput;
	protected Report report;
	protected BaseReport reportGenerator;

	public BaseOutput(ReportOutput reportOutput, Report report, BaseReport reportGenerator)
	{
		this.reportOutput = reportOutput;
		this.report = report;
		this.reportGenerator = reportGenerator;
	}

	public static BaseOutput getOutput(ReportOutput reportOutput, Report report, BaseReport reportGenerator)
	{
		BaseOutput output = null;
		switch (reportOutput.getReportTransmissionType()) {
			case ReportTransmissionType.VIEW:
				output = new ViewOutput(reportOutput, report, reportGenerator);
				break;
			case ReportTransmissionType.EMAIL:
				output = new EmailOutput(reportOutput, report, reportGenerator);
				break;
			case ReportTransmissionType.CONFLUENCE:
				output = new ConfluenceOutput(reportOutput, report, reportGenerator);
				break;
			default:
				throw new OpenStorefrontRuntimeException("Output Type not supported", "Add Support to BaseOutput");
		}
		return output;
	}

	public void outputReport(BaseReportModel reportModel, Map<String, ReportWriter> writerMap)
	{
		BaseGenerator generator = init();
		try {
			String key = reportOutput.toFormatKey();
			ReportWriter writer = writerMap.get(key);
			if (writer != null) {
				writer.writeReport(generator, reportModel);
			} else {
				LOG.log(Level.WARNING, MessageFormat.format("No writer support for {0}", key));
			}
		} catch (Exception e) {
			generator.setFailed(true);
		} finally {
			generator.finish();
			finishOutput();
		}

	}

	protected abstract BaseGenerator init();

	protected abstract void finishOutput();

	protected BaseGenerator getBaseGenerator()
	{
		BaseGenerator generator = BaseGenerator.getGenerator(report, reportOutput.getReportTransmissionOption().getReportFormat());

		return generator;
	}

}
