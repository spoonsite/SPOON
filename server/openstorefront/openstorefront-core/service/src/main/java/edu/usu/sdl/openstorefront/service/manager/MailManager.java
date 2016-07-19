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

import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;
import org.codemonkey.simplejavamail.email.Email;
import org.codemonkey.simplejavamail.email.Recipient;

/**
 * Used for Handling Email
 *
 * @author dshurtleff
 */
public class MailManager
		implements Initializable
{

	private static final Logger log = Logger.getLogger(MailManager.class.getName());

	private static AtomicBoolean started = new AtomicBoolean(false);
	private static Mailer mailer;

	public static void init()
	{
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

	public static void send(Email email)
	{
		if (email != null) {
			if (mailer != null) {
				mailer.sendMail(email);
			} else {
				StringBuilder sb = new StringBuilder();
				for (Recipient recipient : email.getRecipients()) {
					sb.append(recipient.getType()).append(": ");
					sb.append(recipient.getAddress()).append(", ");
				}
				log.log(Level.FINE, MessageFormat.format("(Mock Email Handler) Sending Message Subject: {0} To {1}", new Object[]{email.getSubject(), sb.toString()}));
			}
		} else {
			log.log(Level.FINE, "Unable to send NULL email message. No message to send.");
		}
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
