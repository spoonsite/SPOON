/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.workplan;

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentType;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserRole;
import edu.usu.sdl.openstorefront.core.entity.WorkPlan;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanLink;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStep;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStepAction;
import edu.usu.sdl.openstorefront.service.manager.MailManager;
import java.util.ArrayList;
import java.util.List;
import javax.mail.Message;
import org.apache.commons.lang3.StringUtils;
import org.codemonkey.simplejavamail.email.Email;

/**
 *
 * @author dshurtleff
 */
public class EmailStepAction
		extends BaseWorkPlanStepAction
{

	public EmailStepAction(WorkPlanLink workPlanLink, WorkPlan workPlan, WorkPlanStepAction currentStepAction, WorkPlanStep workPlanStep)
	{
		super(workPlanLink, workPlan, currentStepAction, workPlanStep);
	}

	@Override
	public void performAction()
	{
		if (currentStepAction.getActionOption() != null
				&& StringUtils.isNotBlank(currentStepAction.getActionOption().getEmailMessage())) {

			if (Convert.toBoolean(currentStepAction.getActionOption().getEmailAssignee())) {
				sendEmailToUser(workPlanLink.getCurrentUserAssigned());
			} else if (Convert.toBoolean(currentStepAction.getActionOption().getEmailOwner())) {

				if (StringUtils.isNotBlank(workPlanLink.getComponentId())) {
					Component component = new Component();
					component.setComponentId(workPlanLink.getComponentId());
					component = component.find();
					if (component != null) {
						sendEmailToUser(component.entityOwner());
					}
				}

			} else if (StringUtils.isNotBlank(currentStepAction.getActionOption().getEmailGroup())) {

				UserRole userRoleExample = new UserRole();
				userRoleExample.setRole(currentStepAction.getActionOption().getEmailGroup());
				List<UserRole> userRoles = new ArrayList<>();
				for (UserRole userRole : userRoles) {
					sendEmailToUser(userRole.getUsername());
				}

			} else if (Convert.toBoolean(currentStepAction.getActionOption().getEmailEntryTypeGroup())) {

				if (StringUtils.isNotBlank(workPlanLink.getComponentId())) {
					String componentType = service.getComponentService().getComponentTypeForComponent(workPlanLink.getComponentId());

					if (StringUtils.isNotBlank(componentType)) {
						ComponentType componentTypeFull = new ComponentType();
						componentTypeFull.setComponentType(componentType);
						componentTypeFull = componentTypeFull.find();
						if (componentTypeFull != null) {
							UserRole userRoleExample = new UserRole();
							userRoleExample.setRole(componentTypeFull.getComponentType());
							List<UserRole> userRoles = new ArrayList<>();
							for (UserRole userRole : userRoles) {
								sendEmailToUser(userRole.getUsername());
							}
						}
					}
				}

			} else if (currentStepAction.getActionOption().getFixedEmails() != null
					&& !currentStepAction.getActionOption().getFixedEmails().isEmpty()) {

				//Sending one at time is the safest way to ensure privacy
				currentStepAction.getActionOption().getFixedEmails().forEach((emailAddress) -> {

					Email email = setUpEmail();
					email.addRecipient("", emailAddress.getEmail(), Message.RecipientType.TO);
					MailManager.send(email);
				});
			}
		}
	}

	private void sendEmailToUser(String userName)
	{
		UserProfile userProfile = new UserProfile();
		userProfile.setUsername(userName);
		userProfile = userProfile.find();

		if (userProfile != null && StringUtils.isNotBlank(userProfile.getEmail())) {
			Email email = setUpEmail();
			email.addRecipient(userProfile.fullName(), userProfile.getEmail(), Message.RecipientType.TO);
			MailManager.send(email);
		}
	}

	private Email setUpEmail()
	{
		Email email = MailManager.newEmail();
		email.setTextHTML(currentStepAction.getActionOption().getEmailMessage());

		String subject = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_APPLICATION_TITLE) + " Notification";
		if (StringUtils.isNotBlank(currentStepAction.getActionOption().getEmailSubject())) {
			subject = subject + " - " + currentStepAction.getActionOption().getEmailSubject();
		}
		email.setSubject(subject);

		return email;
	}

}
