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

import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.UserRegistration;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codemonkey.simplejavamail.email.Email;

/**
 *
 * @author dshurtleff
 */
public class UserRegistrationGenerator
	extends BaseMessageGenerator		
{
	private static final Logger LOG = Logger.getLogger(UserRegistrationGenerator.class.getName());

	public UserRegistrationGenerator(MessageContext messageContext)
	{
		super(messageContext);
	}

	@Override
	protected String getSubject()
	{
		return "New User Registration(s)";
	}

	@Override
	protected String generateMessageInternal(Email email)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
				
		Instant instant = Instant.ofEpochMilli(messageContext.getUserMessage().getCreateDts().getTime());
		instant = instant.minusSeconds(10);
		Date checkDate = new Date(instant.toEpochMilli());
		
		UserRegistration userRegistration = new UserRegistration();
		userRegistration.setActiveStatus(UserRegistration.ACTIVE_STATUS);
		
		UserRegistration userRegistrationExample = new UserRegistration();
		userRegistrationExample.setCreateDts(checkDate);		
		
		QueryByExample queryByExample = new QueryByExample(userRegistration);
		SpecialOperatorModel specialOperatorModel = new SpecialOperatorModel();
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN_EQUAL);
		specialOperatorModel.setExample(userRegistrationExample);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);		
		
		List<UserRegistration> users = serviceProxy.getPersistenceService().queryByExample(queryByExample);
		if (users.isEmpty() == false) {
			StringBuilder message = new StringBuilder();
			
			message.append("New User Registrations<br><br>");
			message.append("<ul>");
			for (UserRegistration registration : users) {
				message.append("<li>")
						.append(registration.getUsername())
						.append(" - ")
						.append(userRegistration.getFirstName())
						.append(", ")
						.append(userRegistration.getLastName())
						.append(" registered at ")
						.append(sdf.format(userRegistration.getCreateDts()))
						.append("</li>");
			}
			message.append("</ul>");
			return message.toString();
		} else {
			LOG.log(Level.WARNING, "Expected new registrations. Unable to find an active registrations.");			
			return null;
		}	
	}

	@Override
	protected String getUnsubscribe()
	{
		return "To stop receiving this message, please contact an administrator.";
	}
	
}
