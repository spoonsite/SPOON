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
package edu.usu.sdl.openstorefront.web.test.feedback;

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import static edu.usu.sdl.openstorefront.common.manager.PropertiesManager.KEY_FEEDBACK_EMAIL;
import edu.usu.sdl.openstorefront.core.entity.Branding;
import static edu.usu.sdl.openstorefront.core.entity.FeedbackHandleType.EMAIL;
import static edu.usu.sdl.openstorefront.core.entity.FeedbackHandleType.INTERNAL;
import edu.usu.sdl.openstorefront.core.entity.FeedbackTicket;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;

/**
 *
 * @author ccummings
 */
public class FeedbackServiceTest extends BaseTestCase
{

	private Branding branding_internal = null;
	private Branding branding_email = null;
	private Branding defaultBranding = null;
	private String defaultPropertyValue = null;
	private FeedbackTicket ticket_InternalTest = null;
	private FeedbackTicket ticket_EmailTest = null;

	@Override
	public String getDescription()
	{

		return "Feedback Test";
	}

	@Override
	protected void initializeTest()
	{
		super.initializeTest();
		defaultBranding = service.getBrandingService().getCurrentBrandingView();

	}

	@Override
	protected void runInternalTest()
	{
		branding_internal = new Branding();
		branding_internal.setName("Branding for Feedback Internal test");
		branding_internal.setFeedbackHandler(INTERNAL);
		branding_internal = service.getBrandingService().saveBranding(branding_internal);
		service.getBrandingService().setBrandingAsCurrent(branding_internal.getBrandingId());

		ticket_InternalTest = new FeedbackTicket();
		ticket_InternalTest.setSummary("Internal feedback test");
		ticket_InternalTest.setDescription("Test uses INTERNAL for feedback handle type");

		// Submit feedback marked for internal message
		results.append("Submitting feedback (internal)...").append("<br>");
		service.getFeedbackService().submitFeedback(ticket_InternalTest);
		results.append("Feedback submitted successfully").append("<br><br>");

		// Change the branding to send feedback via email
		branding_email = new Branding();
		branding_email.setName("Branding for Feedback Email test");
		branding_email.setFeedbackHandler(EMAIL);
		branding_email = service.getBrandingService().saveBranding(branding_email);
		service.getBrandingService().setBrandingAsCurrent(branding_email.getBrandingId());

		ticket_EmailTest = new FeedbackTicket();
		ticket_EmailTest.setTicketType("Feedback Test");
		ticket_EmailTest.setUsername(TEST_USER);
		ticket_EmailTest.setFirstname("Test FirstName");
		ticket_EmailTest.setLastname("Test LastName");
		ticket_EmailTest.setOrganization(TEST_ORGANIZATION);
		ticket_EmailTest.setPhone("(XXX) XXX-XXXX");
		ticket_EmailTest.setSummary("Email feedback test");
		ticket_EmailTest.setDescription("Test uses EMAIL for feedback handle type");
		String email = getSystemEmail();
		ticket_EmailTest.setEmail(email);

		defaultPropertyValue = PropertiesManager.getInstance().getValue(KEY_FEEDBACK_EMAIL);
		PropertiesManager.getInstance().setProperty(KEY_FEEDBACK_EMAIL, email);

		FeedbackTicket submittedTicket = new FeedbackTicket();
		submittedTicket.setTicketType(ticket_EmailTest.getTicketType());
		submittedTicket.setSummary(ticket_EmailTest.getSummary());

		// Submit feedback marked for email message
		results.append("Submitting feedback (email)...<br>");
		ticket_EmailTest = service.getFeedbackService().submitFeedback(ticket_EmailTest);
		results.append("Feedback submitted successfully<br><br>");

		// Mark ticket as complete
		submittedTicket = submittedTicket.find();
		ticket_EmailTest = service.getFeedbackService().markAsComplete(submittedTicket.getFeedbackId());

		submittedTicket = new FeedbackTicket();
		submittedTicket.setTicketType(ticket_EmailTest.getTicketType());
		submittedTicket.setSummary(ticket_EmailTest.getSummary());
		submittedTicket = submittedTicket.find();

		if (FeedbackTicket.INACTIVE_STATUS.equals(submittedTicket.getActiveStatus())) {
			results.append("Mark Feedback As Complete:  Passed<br><br>");
		} else {
			failureReason.append("Mark Feedback As Complete:  Failed - Feedback does not exist or remains outstanding<br><br>");
		}

		// Mark ticket as outstanding
		ticket_EmailTest = service.getFeedbackService().markAsOutstanding(submittedTicket.getFeedbackId());
		submittedTicket = new FeedbackTicket();
		submittedTicket.setTicketType(ticket_EmailTest.getTicketType());
		submittedTicket.setSummary(ticket_EmailTest.getSummary());
		submittedTicket = submittedTicket.find();

		if (FeedbackTicket.ACTIVE_STATUS.equals(submittedTicket.getActiveStatus())) {
			results.append("Mark Feedback As Outstanding:  Passed<br><br>");
		} else {
			failureReason.append("Mark Feedback As Outstanding:  Failed - Feedback does not exist or is inactive<br><br>");
		}
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();

		if (branding_internal != null) {
			service.getBrandingService().deleteBranding(branding_internal.getBrandingId());
		}
		if (branding_email != null) {
			service.getBrandingService().deleteBranding(branding_email.getBrandingId());
		}
		if (ticket_InternalTest != null) {
			service.getFeedbackService().deleteFeedback(ticket_InternalTest.getFeedbackId());
		}
		if (ticket_EmailTest != null) {
			service.getFeedbackService().deleteFeedback(ticket_EmailTest.getFeedbackId());
		}

		if (defaultPropertyValue != null) {
			PropertiesManager.getInstance().setProperty(KEY_FEEDBACK_EMAIL, defaultPropertyValue);
		} else {
			PropertiesManager.getInstance().removeProperty(KEY_FEEDBACK_EMAIL);
		}
		if (defaultBranding != null) {
			service.getBrandingService().setBrandingAsCurrent(defaultBranding.getBrandingId());
		}
	}
}
