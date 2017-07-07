/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.manager;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.codemonkey.simplejavamail.MailException;
import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;
import org.codemonkey.simplejavamail.email.Email;
import org.codemonkey.simplejavamail.email.Recipient;
import org.hazlewood.connor.bottema.emailaddress.EmailAddressValidator;

/**
 * Used for Handling Email
 *
 * @author dshurtleff
 */
public class MailManager
		implements Initializable
{
    public enum Templates { 
        EMAIL_VERIFICATION("emailVerification.ftl");
		
        private String filename; 
        private Templates(String filename) { 
            this.filename = filename; 
        } 
        
        @Override 
        public String toString(){ 
            return filename; 
        } 
    } 

	private static final Logger log = Logger.getLogger(MailManager.class.getName());

	private static AtomicBoolean started = new AtomicBoolean(false);
	private static Mailer mailer;

	// configuration for mail templating engine
	private static Configuration templateConfig = null;

	public static void init()
	{
		// Initialize templating engine configurations
		try {
			templateConfig = new Configuration(Configuration.VERSION_2_3_26);
			templateConfig.setClassForTemplateLoading(ReportManager.class, "/templates/email");
			templateConfig.setDefaultEncoding("UTF-8");
			templateConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			templateConfig.setLogTemplateExceptions(false);
		} catch (Exception e) {
			log.log(Level.WARNING, "While configuring the email template, the follow error was caught: {0}", e);
		}
		//pull properties
		String server = PropertiesManager.getValue(PropertiesManager.KEY_MAIL_SERVER);
		String serverPort = StringProcessor.nullIfBlank(PropertiesManager.getValue(PropertiesManager.KEY_MAIL_SERVER_PORT));
		String serverUser = StringProcessor.nullIfBlank(PropertiesManager.getValue(PropertiesManager.KEY_MAIL_SERVER_USER));
		String serverPW = StringProcessor.nullIfBlank(PropertiesManager.getValue(PropertiesManager.KEY_MAIL_SERVER_PW));
		String useSSL = PropertiesManager.getValue(PropertiesManager.KEY_MAIL_USE_SSL);
		String useTLS = PropertiesManager.getValue(PropertiesManager.KEY_MAIL_USE_TLS);

		if (StringUtils.isNotBlank(server)) {
			TransportStrategy transportStrategy = TransportStrategy.SMTP_PLAIN;
			if (Convert.toBoolean(useSSL)) {
				transportStrategy = TransportStrategy.SMTP_SSL;
			} else if (Convert.toBoolean(useTLS)) {
				transportStrategy = TransportStrategy.SMTP_TLS;
			}

			mailer = new Mailer(server, Convert.toInteger(serverPort), serverUser, serverPW, transportStrategy);
		} else {
			log.log(Level.WARNING, "No mail server is set up.  See application properties file to configure.");
		}
	}

	public static void cleanup()
	{
		//nothing needed for now
	}

	public static Email newEmail()
	{
		String fromName = PropertiesManager.getValue(PropertiesManager.KEY_MAIL_FROM_NAME, OpenStorefrontConstant.NOT_AVAILABLE);
		String fromAddress = PropertiesManager.getValue(PropertiesManager.KEY_MAIL_FROM_ADDRESS, OpenStorefrontConstant.DEFAULT_FROM_ADDRESS);
		String fromReply = PropertiesManager.getValue(PropertiesManager.KEY_MAIL_REPLY_NAME);
		String fromReplyAddress = PropertiesManager.getValue(PropertiesManager.KEY_MAIL_REPLY_ADDRESS);

		Email email = new Email();
		email.setFromAddress(fromName, fromAddress);
		if (StringUtils.isNotBlank(fromReplyAddress)) {
			email.setReplyToAddress(fromReply, fromReplyAddress);
		}
		return email;
	}
	
	/**
	 * Creates a new email with the body of the email set to the email template
	 * @param templateName template to use in setting the contents of the email
	 * @param data data values to use for populating the template variables
	 * @return new Email with populated text field
	 */
	public static Email newTemplateEmail(String templateName, Map data)
	{
		return newTemplateEmail(templateName, data, false);
	}
	
	/**
	 * Creates a new email with the body of the email set to the email template
	 * @param templateName template to use in setting the contents of the email
	 * @param data data values to use for populating the template variables
	 * @param throwException When true, throws an exception when loading the 
	 * template or processing data map fails. When false, will still log, 
	 * but will not throw an exception
	 * @return new Email with populated text field
	 */
	public static Email newTemplateEmail(String templateName, Map data, boolean throwException)
	{
		Email email = newEmail();
		try {
			Template template = templateConfig.getTemplate(templateName);
			Writer writer = new StringWriter();
			template.process(data, writer);
			email.setTextHTML(writer.toString());
		} catch (TemplateNotFoundException ex) {
			log.log(Level.SEVERE, MessageFormat.format("Unable to load template {0}, error: {1}",templateName, ex.getMessage()), ex);
			// Check If Exception Should Be Thrown
			if (throwException) {

				// Throw Runtime Exception
				throw new OpenStorefrontRuntimeException("Unable to create email", ex);
			}
		} catch (MalformedTemplateNameException | ParseException | TemplateException ex) {
			log.log(Level.SEVERE, MessageFormat.format("Unable to process template {0}, error: {1}",templateName, ex.getMessage()), ex);
			// Check If Exception Should Be Thrown
			if (throwException) {

				// Throw Runtime Exception
				throw new OpenStorefrontRuntimeException("Unable to create email", ex);
			}
		} catch (IOException ex) {
			log.log(Level.SEVERE, MessageFormat.format("error reading template {0}, error: {1}",templateName, ex.getMessage()), ex);
			// Check If Exception Should Be Thrown
			if (throwException) {

				// Throw Runtime Exception
				throw new OpenStorefrontRuntimeException("Unable to create email", ex);
			}
		}
		return email;
	}
	
	/**
	 * Sends an email
	 * <p>
	 * Performs validation on recipient email addresses prior to sending.
	 * Catches and logs any exceptions thrown during the sending of the email.
	 * Will not throw or re-throw exceptions when validation or sending fails.
	 *
	 * @param email An email object which is pre-configured and ready to be sent
	 */
	public static void send(Email email)
	{
		// Send Email
		send(email, false);
	}

	/**
	 * Sends an email
	 * <p>
	 * Performs validation on recipient email addresses prior to sending.
	 * Catches and logs any exceptions thrown during the sending of the email.
	 * Throws or re-throws exceptions when instructed to do so.
	 *
	 * @param email An email object which is pre-configured and ready to be sent
	 * @param throwException When true, throws an exception when validation or
	 * sending fails. When false, will still log, but will not throw an
	 * exception
	 */
	public static void send(Email email, boolean throwException)
	{
		// Check For Null Email Object
		if (email != null) {

			// Check For Null Mailer Service
			if (mailer != null) {

				// Store Recipients
				List<Recipient> recipients = email.getRecipients();

				// Validate Recipients
				recipients = validateRecipients(recipients);

				// Check For Recipients
				if (!recipients.isEmpty()) {

					// Attempt To Send Email
					try {

						// Send Email
						mailer.sendMail(email);
					} // Catch Mail Error
					catch (MailException e) {

						// Log Error
						log.log(Level.SEVERE, MessageFormat.format("An error occurred while sending email. The error message follows: {0}", e.getMessage()));

						// Check If Exception Should Be Thrown
						if (throwException) {

							// Throw Runtime Exception
							throw new OpenStorefrontRuntimeException("Unable to send email", e);
						}
					}
				} else {
					// Check If Exception Should Be Thrown
					if (throwException) {

						// Throw Runtime Exception
						throw new OpenStorefrontRuntimeException("Email validation failed", "Check recipient email address(es)");
					}
				}
			} else {
				// Initialize Recipients String
				StringBuilder sb = new StringBuilder();

				// Check Recipients
				email.getRecipients().forEach(recipient -> {

					// Store Recipient Type & Colon
					sb.append(recipient.getType()).append(": ");

					// Store Recipient Address & Separator (Comma)
					sb.append(recipient.getAddress()).append(", ");
				});

				// Log Recipients
				log.log(Level.FINE, MessageFormat.format("(Mock Email Handler) Sending Message Subject: {0} To {1}", new Object[]{email.getSubject(), sb.toString()}));
			}
		} else {

			// Log Error
			log.log(Level.FINE, "Unable to send NULL email message. No message to send.");
		}
	}

	/**
	 * Validate email addresses
	 * <p>
	 * Receives a list of recipients and validates each of their email
	 * addresses. Returns a list of recipients whose email addresses passed
	 * validation and logs those whose did not.
	 *
	 * @param recipients A list of recipients who are intended to receive an
	 * email
	 * @return A list of recipients which passed email address validation. An
	 * empty list will be returned in the event that no recipient passed
	 * validation.
	 */
	public static List<Recipient> validateRecipients(List<Recipient> recipients)
	{

		// Initialize Failed Recipient Flag
		boolean recipientsFailed = false;

		// Create New Recipient List
		List<Recipient> validRecipients = new ArrayList<>();

		// Initialize Failed Recipients String
		StringBuilder failedRecipients = new StringBuilder();

		// Loop Through Recipients
		for (Recipient recipient : recipients) {

			// Validate Email
			if (EmailAddressValidator.isValid(recipient.getAddress())) {

				// Add Recipient
				validRecipients.add(recipient);
			} else {

				// Indicate Recipient Failed
				recipientsFailed = true;

				// Check For Existing Failed Recipient
				if (failedRecipients.length() != 0) {

					// Append Comma
					failedRecipients.append(", ");
				}

				// Append Failed Recipient Name
				failedRecipients.append(recipient.getName());

				// Append Colon
				failedRecipients.append(": ");

				// Append Failed Recipient's Invalid Email Address
				failedRecipients.append(recipient.getAddress());
			}
		}

		// Check For Failed Recipients
		if (recipientsFailed) {

			// Log Failed Recipients
			log.log(Level.WARNING, MessageFormat.format("Some recipient email addresses failed validation. The following are invalid: {0}", failedRecipients.toString()));
		}

		// Return New Recipients
		return validRecipients;
	}

	@Override
	public void initialize()
	{
		MailManager.init();
		started.set(true);
	}

	@Override
	public void shutdown()
	{
		MailManager.cleanup();
		started.set(false);
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

}
