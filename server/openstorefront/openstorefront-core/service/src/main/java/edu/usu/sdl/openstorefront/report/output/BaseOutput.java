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
import edu.usu.sdl.openstorefront.report.generator.GeneratorOptions;
import edu.usu.sdl.openstorefront.report.model.BaseReportModel;
import edu.usu.sdl.openstorefront.security.UserContext;
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
	protected UserContext userContext;

	public BaseOutput(ReportOutput reportOutput, Report report, BaseReport reportGenerator, UserContext userContext)
	{
		this.reportOutput = reportOutput;
		this.report = report;
		this.reportGenerator = reportGenerator;
		this.userContext = userContext;
	}

	public static BaseOutput getOutput(ReportOutput reportOutput, Report report, BaseReport reportGenerator, UserContext userContext)
	{
		BaseOutput output = null;
		switch (reportOutput.getReportTransmissionType()) {
			case ReportTransmissionType.VIEW:
				output = new ViewOutput(reportOutput, report, reportGenerator, userContext);
				break;
			case ReportTransmissionType.EMAIL:
				output = new EmailOutput(reportOutput, report, reportGenerator, userContext);
				break;
			case ReportTransmissionType.CONFLUENCE:
				output = new ConfluenceOutput(reportOutput, report, reportGenerator, userContext);
				break;
			default:
				throw new OpenStorefrontRuntimeException("Output Type not supported", "Add Support to BaseOutput");
		}
		return output;
	}

	public void outputReport(BaseReportModel reportModel, Map<String, ReportWriter> writerMap)
	{
		BaseGenerator generator = init();
		if (generator != null) {
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
				throw new OpenStorefrontRuntimeException("Output failed.", "See trace.", e);
			} finally {
				generator.finish();
				finishOutput(reportModel);
			}
		} else {
			LOG.log(Level.FINER, MessageFormat.format("No generator for output...not writing report. Output: {0}", this.getClass().getSimpleName()));

			finishOutput(reportModel);
		}
	}

	protected abstract BaseGenerator init();

	protected abstract void finishOutput(BaseReportModel reportModel);

	protected BaseGenerator getBaseGenerator()
	{
		GeneratorOptions generatorOptions = new GeneratorOptions(report);
		BaseGenerator generator = BaseGenerator.getGenerator(reportOutput.getReportTransmissionOption().getReportFormat(), generatorOptions);
		return generator;
	}

	/**
	 * Keep in mind: Since the user is not login in...this can't be verified via
	 * Security That means we have to rely on the database for external
	 * management it will use the last know state of the user. Since the user is
	 * tied to roles not permissions the role can dynamically change which will
	 * effect scheduled reports. Which is a desired effect otherwise the user
	 * would have 'baked' in permissions.
	 *
	 * @param permission
	 * @return
	 */
	protected boolean hasPermission(String permission)
	{
		return userContext.permissions().contains(permission);
	}

}
