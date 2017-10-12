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
package edu.usu.sdl.openstorefront.report.generator;

import edu.usu.sdl.openstorefront.core.entity.Report;
import java.io.OutputStream;

/**
 *
 * @author dshurtleff
 */
public class GeneratorOptions
{

	private Report report;
	private String overrideOutputPath;
	private OutputStream outputStream;

	public GeneratorOptions()
	{
	}

	public GeneratorOptions(Report report)
	{
		this.report = report;
	}

	public Report getReport()
	{
		return report;
	}

	public void setReport(Report report)
	{
		this.report = report;
	}

	public String getOverrideOutputPath()
	{
		return overrideOutputPath;
	}

	public void setOverrideOutputPath(String overrideOutputPath)
	{
		this.overrideOutputPath = overrideOutputPath;
	}

	public OutputStream getOutputStream()
	{
		return outputStream;
	}

	public void setOutputStream(OutputStream outputStream)
	{
		this.outputStream = outputStream;
	}

}
