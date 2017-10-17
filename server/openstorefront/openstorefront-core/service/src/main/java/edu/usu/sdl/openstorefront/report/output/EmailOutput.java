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

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.entity.EmailAddress;
import edu.usu.sdl.openstorefront.core.entity.Report;
import edu.usu.sdl.openstorefront.core.entity.ReportFormat;
import edu.usu.sdl.openstorefront.core.entity.ReportOutput;
import edu.usu.sdl.openstorefront.core.entity.ReportType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.report.BaseReport;
import edu.usu.sdl.openstorefront.report.generator.BaseGenerator;
import edu.usu.sdl.openstorefront.report.generator.GeneratorOptions;
import edu.usu.sdl.openstorefront.report.model.BaseReportModel;
import edu.usu.sdl.openstorefront.security.UserContext;
import edu.usu.sdl.openstorefront.service.manager.MailManager;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import javax.mail.Message;
import org.codemonkey.simplejavamail.email.Email;

/**
 *
 * @author dshurtleff
 */
public class EmailOutput
		extends BaseOutput
{

	private ByteArrayOutputStream attachedReport;

	public EmailOutput(ReportOutput reportOutput, Report report, BaseReport reportGenerator, UserContext userContext)
	{
		super(reportOutput, report, reportGenerator, userContext);
	}

	@Override
	protected BaseGenerator init()
	{
		BaseGenerator generator = null;
		//write to a temp file if attaching
		if (Convert.toBoolean(reportOutput.getReportTransmissionOption().getAttachReport())) {
			GeneratorOptions generatorOptions = new GeneratorOptions(report);
			attachedReport = new ByteArrayOutputStream();
			generatorOptions.setOutputStream(attachedReport);
			generator = BaseGenerator.getGenerator(reportOutput.getReportTransmissionOption().getReportFormat(), generatorOptions);
		}
		return generator;
	}

	@Override
	protected void finishOutput(BaseReportModel reportModel)
	{
		StringBuilder message = new StringBuilder();

		if (Convert.toBoolean(reportOutput.getReportTransmissionOption().getPostToEmailBody())) {
			message.append(reportGenerator.reportSummmary(reportModel));
		} else {
			String replyAddress = PropertiesManager.getValue(PropertiesManager.KEY_MAIL_REPLY_ADDRESS);
			message.append("Report is ready to be viewed. To view your report, log in then go to the reports section under <i>History</i>")
					.append(attachedReport != null ? " or see the attached file" : "")
					.append(".<br><br><br>");
			message.append("To stop receiving this message, please contact an administrator at ").append(replyAddress);

		}

		String applicationTitle = PropertiesManager.getValue(PropertiesManager.KEY_APPLICATION_TITLE, "Openstorefront");
		if (reportOutput.getReportTransmissionOption().getEmailAddresses() == null) {
			reportOutput.getReportTransmissionOption().setEmailAddresses(new ArrayList<>());
		}
		for (EmailAddress emailAddress : reportOutput.getReportTransmissionOption().getEmailAddresses()) {
			Email email = MailManager.newEmail();
			email.setSubject(applicationTitle + " - " + TranslateUtil.translate(ReportType.class, report.getReportType()) + " Report");
			email.setTextHTML(message.toString());

			if (attachedReport != null) {
				String extension = OpenStorefrontConstant.getFileExtensionForMime(ReportFormat.mimeType(reportOutput.getReportTransmissionOption().getReportFormat()));
				email.addAttachment(TranslateUtil.translate(ReportType.class,
						report.getReportType()) + extension,
						attachedReport.toByteArray(),
						ReportFormat.mimeType(reportOutput.getReportTransmissionOption().getReportFormat()));
			}

			email.addRecipient("", emailAddress.getEmail(), Message.RecipientType.TO);
			MailManager.send(email);
		}

	}

}
