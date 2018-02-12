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
package edu.usu.sdl.openstorefront.service.message;

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.manager.MailManager;
import javax.mail.Message.RecipientType;
import org.apache.commons.lang.StringUtils;
import org.codemonkey.simplejavamail.email.Email;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;

/**
 * A new generator should be created for each email
 *
 * @author dshurtleff
 */
public abstract class BaseMessageGenerator
{

	protected MessageContext messageContext;
	protected ServiceProxy serviceProxy = new ServiceProxy();

	public BaseMessageGenerator(MessageContext messageContext)
	{
		this.messageContext = messageContext;
	}

	/**
	 * Generates message to send
	 *
	 * @return email message to send or null if nothing to send.
	 */
	public Email generateMessage()
	{
		Email email = MailManager.newEmail();
		email.setSubject(getApplcationTitle() + " - " + getSubject());
		addUserToEmail(email);

		StringBuilder message = new StringBuilder();

		String body = generateMessageInternal(email);
		if (StringUtils.isNotBlank(body)) {
			message.append(body);
		} else {
			return null;
		}
		message.append("<br><br>");
		message.append(getUnsubscribe());
		message.append("<br>");
		message.append(getContactLine());

		email.setTextHTML(message.toString());
		String textBased = message.toString().replace("<br>", "\n");
		textBased = new HtmlToPlainText().getPlainText(Jsoup.parse(textBased));
		email.setText(textBased);
		return email;
	}

	protected abstract String getSubject();

	/**
	 * Generate email content
	 *
	 * @param email (Use to set attachment or embed images)
	 * @return Email body
	 */
	protected abstract String generateMessageInternal(Email email);

	protected abstract String getUnsubscribe();

	protected String getApplcationTitle()
	{
		String applicationTitle = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_APPLICATION_TITLE, "Openstorefront");
		return applicationTitle;
	}

	protected String getContactLine()
	{
		StringBuilder contactLine = new StringBuilder();
		String replyName = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MAIL_REPLY_NAME);
		String replyAddress = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MAIL_REPLY_ADDRESS);
		if (StringUtils.isNotBlank(replyName) && StringUtils.isNotBlank(replyAddress)) {
			contactLine.append("Contact ").append(replyName).append(" at ").append(replyAddress).append(" for questions.");
		} else if (StringUtils.isNotBlank(replyAddress)) {
			contactLine.append("Contact ").append(replyAddress).append(" for questions.");
		}

		return contactLine.toString();
	}

	/**
	 * Adds Recipient (Fail
	 *
	 * @param email
	 */
	protected void addUserToEmail(Email email)
	{
		String name = "";
		UserProfile userProfile = messageContext.getUserProfile();
		if (userProfile != null) {
			name = userProfile.getFirstName() + " " + userProfile.getLastName();
		}

		if (messageContext.getUserMessage() != null
				&& StringUtils.isNotBlank(messageContext.getUserMessage().getEmailAddress())) {
			email.addRecipient(name, messageContext.getUserMessage().getEmailAddress(), RecipientType.TO);
		} else {
			if (userProfile != null) {
				email.addRecipient(name, userProfile.getEmail(), RecipientType.TO);
			}
		}
	}

}
