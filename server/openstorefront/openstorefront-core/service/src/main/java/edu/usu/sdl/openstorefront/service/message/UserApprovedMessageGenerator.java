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
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import org.codemonkey.simplejavamail.email.Email;

/**
 *
 * @author dshurtleff
 */
public class UserApprovedMessageGenerator
	extends BaseMessageGenerator
{
	
	public UserApprovedMessageGenerator(MessageContext messageContext)
	{
		super(messageContext);
	}	

	@Override
	protected String getSubject()
	{
		return "User Account Approved";
	}

	@Override
	protected String generateMessageInternal(Email email)
	{
		UserProfile userProfile = messageContext.getUserProfile();
		
		StringBuilder message = new StringBuilder();
		message.append("Your user account request for " + userProfile.getUsername() + " has been approved.<br><br>");
		
		String externalHostUrl = PropertiesManager.getValueDefinedDefault(PropertiesManager.KEY_EXTERNAL_HOST_URL);
		message.append("Login to use the system. <a href='"+ externalHostUrl+"'>" + externalHostUrl + "</a><br>");
		
	
		return message.toString();
	}

	@Override
	protected String getUnsubscribe()
	{
		return "If you are not the intended user of this message, please delete this message. ";
	}
	
}
