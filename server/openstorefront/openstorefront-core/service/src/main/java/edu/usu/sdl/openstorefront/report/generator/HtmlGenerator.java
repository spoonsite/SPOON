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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class HtmlGenerator
		extends BaseGenerator
{

	private static final Logger LOG = Logger.getLogger(HtmlGenerator.class.getName());

	private BufferedWriter writer;

	public HtmlGenerator(GeneratorOptions generatorOption)
	{
		super(generatorOption);
	}

	@Override
	public void init()
	{
		if (generatorOptions.getOutputStream() != null) {
			writer = new BufferedWriter(new OutputStreamWriter(generatorOptions.getOutputStream()));
		} else {
			File reportFile = null;
			if (StringUtils.isNotBlank(generatorOptions.getOverrideOutputPath())) {
				reportFile = new File(generatorOptions.getOverrideOutputPath());
			} else {
				Objects.requireNonNull(report, "The generator requires the report to exist.");
				Objects.requireNonNull(report.getReportId(), "The report id is required.");
				reportFile = report.pathToReport().toFile();
			}

			try {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reportFile)));
			} catch (FileNotFoundException ex) {
				throw new OpenStorefrontRuntimeException("Unable to open file to write report.", "Check file system permissions", ex);
			}
		}
	}

	public void addDefaultStartBlock()
	{
		try {
			writer.append("<html><body style='padding: 20px'>");
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable write to report.", "System error or bad outputstream", ex);
		}
	}

	public void addDefaultEndBlock()
	{
		try {
			writer.append("</body></html>");
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable write to report.", "System error or bad outputstream", ex);
		}
	}

	public void addStyleBlock(String style)
	{
		try {
			writer.append("<style>");
			writer.newLine();
			writer.append(style);
			writer.newLine();
			writer.append("</style>");
			writer.newLine();

		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
	}

	public void addMainHeader(String data)
	{
		try {
			writer.append("<h1>");
			writer.append(data);
			writer.append("</h1>");
			writer.newLine();
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
	}

	public void addRuleLine()
	{
		addLine("<hr>");
	}

	public void addBreak()
	{
		addLine("<br>");
	}

	public void addLine(String line)
	{
		if (StringUtils.isNotBlank(line)) {
			try {
				writer.append(line);
				writer.newLine();
			} catch (IOException ex) {
				throw new OpenStorefrontRuntimeException(ex);
			}
		}
	}

	public String convertImageToDataUrl(String pathToMedia, String mimetype)
	{
		Path path = Paths.get(pathToMedia);
		byte[] data;
		try {
			data = Files.readAllBytes(path);
			byte[] encoded = Base64.getEncoder().encode(data);
			String imgDataAsBase64 = new String(encoded);
			String imgAsBase64 = "data:" + mimetype + ";base64," + imgDataAsBase64;
			return imgAsBase64;
		} catch (IOException ex) {
			LOG.log(Level.WARNING, MessageFormat.format("Unable to find or read media. Path: {0}", pathToMedia));
			LOG.log(Level.FINER, null, ex);
		}
		return "";
	}

	@Override
	protected void internalFinish()
	{
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException ex) {
				throw new OpenStorefrontRuntimeException("Failed to close report output.", ex);
			}
		}
	}

}
