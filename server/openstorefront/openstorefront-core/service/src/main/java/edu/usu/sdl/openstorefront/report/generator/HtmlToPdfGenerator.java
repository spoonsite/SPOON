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
import edu.usu.sdl.openstorefront.core.entity.Report;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 *
 * @author cyearsley
 */
public class HtmlToPdfGenerator
		extends BaseGenerator
{
	OutputStream writer;
	
	public HtmlToPdfGenerator(Report report)
	{
		super(report);
	}
	
	@Override
	public void init()
	{
		Objects.requireNonNull(report, "The generator requires the report to exist.");
		Objects.requireNonNull(report.getReportId(), "The report id is required.");
		try {
			writer = new FileOutputStream(report.pathToReport().toFile());
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to open file to write report.", "Check file system permissions", ex);
		}
	}
	
	@Override
	protected void internalFinish()
	{
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException ex) {
				throw new OpenStorefrontRuntimeException("Failed to close report file. Report: " + report.pathToReport(), ex);
			}
		}
	}
	
	public void savePdfDocument(String htmlContent)
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
			Logger.getLogger(HtmlToPdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		//	Set XHTML as the document string
		renderer.setDocumentFromString(htmlContent);
		renderer.layout();

		try {
			//	Create and save the PDF
			renderer.createPDF(writer, true);
		} catch (DocumentException ex) {
			Logger.getLogger(HtmlToPdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
