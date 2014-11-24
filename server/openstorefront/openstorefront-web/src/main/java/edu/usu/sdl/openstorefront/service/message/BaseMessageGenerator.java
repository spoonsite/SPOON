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

import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.manager.MailManager;
import edu.usu.sdl.openstorefront.service.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.storage.model.UserProfile;
import javax.mail.Message.RecipientType;
import org.codemonkey.simplejavamail.Email;
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

	public Email generateMessage()
	{
		Email email = MailManager.newEmail();
		email.setSubject(getApplcationTitle() + " - " + getSubject());
		addUserToEmail(email);

		StringBuilder message = new StringBuilder();
		message.append(generateMessageInternal(email));
		message.append("<br><br>");
		message.append(getUnsubscribe());

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
		String applicationTitle = PropertiesManager.getValue(PropertiesManager.KEY_APPLICATION_TITLE, "Openstorefront");
		return applicationTitle;
	}

	/**
	 * Adds User if the user profile exists.
	 *
	 * @param email
	 */
	protected void addUserToEmail(Email email)
	{
		UserProfile userProfile = messageContext.getUserProfile();
		if (userProfile != null) {
			String name = userProfile.getFirstName() + " " + userProfile.getLastName();
			email.addRecipient(name, userProfile.getEmail(), RecipientType.TO);
		}
	}

}
