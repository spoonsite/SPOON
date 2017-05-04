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

import edu.usu.sdl.openstorefront.core.entity.UserApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.UserSecurity;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codemonkey.simplejavamail.email.Email;

/**
 *
 * @author dshurtleff
 */
public class UserApprovalGenerator
	extends BaseMessageGenerator
{
	private static final Logger LOG = Logger.getLogger(UserApprovalGenerator.class.getName());
	
	public UserApprovalGenerator(MessageContext messageContext)
	{
		super(messageContext);
	}

	@Override
	protected String getSubject()
	{
		return "User(s) Waiting Approval";
	}

	@Override
	protected String generateMessageInternal(Email email)
	{
		StringBuilder message = new StringBuilder();
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
				
		//Send list of all user wait for approval to remind of an exising
		UserSecurity userSecurity = new UserSecurity();
		userSecurity.setApprovalStatus(UserApprovalStatus.PENDING);
		List<UserSecurity> users = userSecurity.findByExample();
		
		if (!users.isEmpty()) {
			message.append("Users waiting for approval: <br><br>");
			message.append("<ul>");
			for (UserSecurity user : users) {
				message.append("<li>")
						.append(user.getUsername())
						.append(" registered on ")
						.append(sdf.format(user.getCreateDts()))						
						.append("</li>");				
				
			}
			message.append("</ul>");			
			
			return message.toString();
		} else  {
			LOG.log(Level.FINE, "No users currently need approval. (No message generated)");			
			return null;
		}
	}

	@Override
	protected String getUnsubscribe()
	{
		return "To stop receiving this message, please contact an administrator.";
	}
	
}
