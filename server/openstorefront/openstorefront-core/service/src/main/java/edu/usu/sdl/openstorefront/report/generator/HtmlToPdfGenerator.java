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

import com.lowagie.text.DocumentException;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 *
 * @author cyearsley
 */
public class HtmlToPdfGenerator
		extends BaseGenerator
{

	private static final Logger LOG = Logger.getLogger(HtmlToPdfGenerator.class.getName());

	private OutputStream writer;

	public HtmlToPdfGenerator(GeneratorOptions generatorOptions)
	{
		super(generatorOptions);
	}

	@Override
	public void init()
	{
		if (generatorOptions.getOutputStream() != null) {
			writer = generatorOptions.getOutputStream();
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
				writer = new FileOutputStream(reportFile);
			} catch (FileNotFoundException ex) {
				throw new OpenStorefrontRuntimeException("Unable to open file to write report.", "Check file system permissions", ex);
			}
		}
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

	/**
	 * Saves to output can not be written to after this point
	 *
	 * @param htmlContent
	 */
	public void savePdfDocument(String htmlContent)
	{
		savePdfDocument(htmlContent, null);
	}

	public void savePdfDocument(String htmlContent, PDFRenderHandler renderHandler)
	{
		//	Convert HTML to XHTML
		ITextRenderer renderer = new ITextRenderer();
		Tidy tidy = new Tidy();
		tidy.setXHTML(true);
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			tidy.parseDOM(inputStream, outputStream);
			htmlContent = outputStream.toString("UTF-8");
		} catch (UnsupportedEncodingException ex) {
			throw new OpenStorefrontRuntimeException("Encoding not supported on the output.", ex);
		}

		if (renderHandler != null) {
			renderHandler.configRender(renderer);
		}
		renderer.setDocumentFromString(htmlContent);
		renderer.layout();

		try {
			//	Create and save the PDF
			renderer.createPDF(writer, true);
		} catch (DocumentException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
	}
}
