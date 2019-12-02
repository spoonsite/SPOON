/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service;

import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.api.FeedbackService;
import edu.usu.sdl.openstorefront.core.entity.Branding;
import edu.usu.sdl.openstorefront.core.entity.FeedbackHandleType;
import edu.usu.sdl.openstorefront.core.entity.FeedbackTicket;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.security.UserContext;
import edu.usu.sdl.openstorefront.service.manager.JiraManager;
import edu.usu.sdl.openstorefront.service.manager.MailManager;
import edu.usu.sdl.openstorefront.service.manager.resource.JiraClient;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import org.apache.commons.lang3.StringUtils;
import org.codemonkey.simplejavamail.email.Email;

/**
 *
 * @author dshurtleff
 */
public class FeedbackServiceImpl
		extends ServiceProxy
		implements FeedbackService
{

	private static final Logger LOG = Logger.getLogger(FeedbackServiceImpl.class.getName());

	@Override
	public FeedbackTicket submitFeedback(FeedbackTicket ticket)
	{
		Objects.requireNonNull(ticket);

		UserContext userContext = SecurityUtil.getUserContext();
		if (userContext != null) {
			UserProfile userProfile = userContext.getUserProfile();
			if (userProfile.getUsername().equals(OpenStorefrontConstant.ANONYMOUS_USER)) {
				// Expecting firstname to be Guest, lastname to be empty string, & username to be ANONYMOUS from userProfile
				ticket.setFirstname(userProfile.getFirstName());
				ticket.setLastname(userProfile.getLastName());
				ticket.setOrganization(ticket.getOrganization());
				ticket.setEmail(ticket.getEmail());
				ticket.setPhone(ticket.getPhone());
				ticket.setUsername(userProfile.getUsername());
				ticket.setFullname(ticket.getFullname());
			} else {

				ticket.setFirstname(userProfile.getFirstName());
				ticket.setLastname(userProfile.getLastName());
				ticket.setOrganization(userProfile.getOrganization());
				ticket.setEmail(userProfile.getEmail());
				ticket.setPhone(userProfile.getPhone());
				ticket.setUsername(userProfile.getUsername());
				ticket.setFullname(userProfile.getFirstName() + " " + userProfile.getLastName());
			}
		}
		ticket.setFeedbackId(getPersistenceService().generateId());
		ticket.populateBaseCreateFields();
		ticket = getPersistenceService().persist(ticket);

		Branding branding = getBrandingService().getCurrentBrandingView();
		switch (branding.getFeedbackHandler()) {
			case FeedbackHandleType.EMAIL:

				String emailAddress = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_FEEDBACK_EMAIL);
				if (StringUtils.isNotBlank(emailAddress)) {
					Email email = MailManager.newEmail();
					email.setSubject(ticket.fullSubject());
					email.setText(ticket.fullDescription());
					email.addRecipient("Admin", emailAddress, Message.RecipientType.TO);
					MailManager.send(email);
				} else {
					LOG.log(Level.WARNING, "Email is setup as the feedback handler however the configure properties doesn't have a email added defined for property: " + PropertiesManager.KEY_FEEDBACK_EMAIL);
				}
				break;
		}
		return ticket;
	}

	@Override
	public FeedbackTicket markAsComplete(String ticketId)
	{
		return updateStatus(ticketId, FeedbackTicket.INACTIVE_STATUS);
	}

	@Override
	public FeedbackTicket markAsOutstanding(String ticketId)
	{
		return updateStatus(ticketId, FeedbackTicket.ACTIVE_STATUS);
	}

	private FeedbackTicket updateStatus(String ticketId, String status)
	{
		FeedbackTicket feedbackTicket = getPersistenceService().findById(FeedbackTicket.class, ticketId);
		if (feedbackTicket != null) {
			feedbackTicket.setActiveStatus(status);
			feedbackTicket.populateBaseUpdateFields();
			feedbackTicket = getPersistenceService().persist(feedbackTicket);
		}
		return feedbackTicket;
	}

	@Override
	public void deleteFeedback(String ticketId)
	{
		FeedbackTicket feedbackTicket = getPersistenceService().findById(FeedbackTicket.class, ticketId);
		if (feedbackTicket != null) {
			getPersistenceService().delete(feedbackTicket);
		}
	}

}
