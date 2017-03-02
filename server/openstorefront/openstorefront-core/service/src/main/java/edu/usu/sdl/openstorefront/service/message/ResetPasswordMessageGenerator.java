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
package edu.usu.sdl.openstorefront.service.message;

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import org.codemonkey.simplejavamail.email.Email;

/**
 *
 * @author dshurtleff
 */
public class ResetPasswordMessageGenerator
	extends BaseMessageGenerator
{
	
	public ResetPasswordMessageGenerator(MessageContext messageContext)
	{
		super(messageContext);
	}

	@Override
	protected String getSubject()
	{
		return "Reset Password Request";
	}

	@Override
	protected String generateMessageInternal(Email email)
	{
		StringBuilder message = new StringBuilder();
		message.append("A password reset was requested for your user.<br>");
		message.append("Click or Copy/Paste the link into your web browser to Approve the request.<br><br>");
		
		String externalHostUrl = PropertiesManager.getValueDefinedDefault(PropertiesManager.KEY_EXTERNAL_HOST_URL);
		
		String urlToApprove = externalHostUrl +"/approveChange.jsp?approvalCode=" +  messageContext.getUserPasswordResetCode();
		
		message.append("<a href='")
				.append(urlToApprove)		
				.append("'>" + urlToApprove + "</a><br><br>");
		
		message.append("If you didn't request this change you can ignore this message and notify an Admin.<br>Your password will not be changed until it's approved.");
		
		return message.toString();
	}

	@Override
	protected String getUnsubscribe()
	{
		return "If you are not the intended user of this message, please delete this message. ";
	}

}
