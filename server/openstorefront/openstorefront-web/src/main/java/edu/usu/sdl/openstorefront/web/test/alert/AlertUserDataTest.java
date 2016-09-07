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
package edu.usu.sdl.openstorefront.web.test.alert;

import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.Alert;
import edu.usu.sdl.openstorefront.core.entity.AlertType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentContact;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.Contact;
import static edu.usu.sdl.openstorefront.core.entity.ContactType.SUBMITTER;
import edu.usu.sdl.openstorefront.core.entity.EmailAddress;
import edu.usu.sdl.openstorefront.core.entity.ExperienceTimeType;
import static edu.usu.sdl.openstorefront.core.entity.StandardEntity.ACTIVE_STATUS;
import edu.usu.sdl.openstorefront.core.entity.UserDataAlertOption;
import edu.usu.sdl.openstorefront.core.entity.UserMessage;
import static edu.usu.sdl.openstorefront.core.entity.UserTypeCode.END_USER;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.model.QuestionAll;
import edu.usu.sdl.openstorefront.core.model.ReviewAll;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import static edu.usu.sdl.openstorefront.web.test.alert.AlertTestUtil.activateAlerts;
import static edu.usu.sdl.openstorefront.web.test.alert.AlertTestUtil.getActiveAlerts;
import static edu.usu.sdl.openstorefront.web.test.alert.AlertTestUtil.inactivateAlerts;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class AlertUserDataTest extends BaseTestCase
{

	private Alert alertUserDataTag = null;
	private Alert alertUserDataRQ = null;
	private Alert alertUserDataContact = null;
	private List<Alert> alerts = null;
	private ExperienceTimeType experience = null;

	@Override
	protected void initializeTest()
	{
		super.initializeTest(); //To change body of generated methods, choose Tools | Templates.
	}

	
	
	@Override
	protected void runInternalTest()
	{
		// Get all alerts and set to inactive
		alerts = getActiveAlerts();
		if (!alerts.isEmpty()) {
			inactivateAlerts(alerts);
		}

		results.append("Test 1 - One User Data Option Selected<br><br>");
		alertUserDataTag = new Alert();
		alertUserDataTag.setName("New User Data Alert Test1");
		alertUserDataTag.setActiveStatus(ACTIVE_STATUS);
		alertUserDataTag.setAlertType(AlertType.USER_DATA);
		EmailAddress emailAddress = new EmailAddress();
		emailAddress.setEmail(getSystemEmail());
		List<EmailAddress> emails = new ArrayList();
		emails.add(emailAddress);
		alertUserDataTag.setEmailAddresses(emails);
		UserDataAlertOption userDataOption = new UserDataAlertOption();
		userDataOption.setAlertOnTags(true);
		alertUserDataTag.setUserDataAlertOption(userDataOption);

		results.append("Saving user data alert...<br>");
		alertUserDataTag = service.getAlertService().saveAlert(alertUserDataTag);
		results.append("Alert saved<br><br>");

		results.append("Checking alert user data tag message...<br>");
		ComponentAll componentAllTag = getTestComponent();
		Component componentAlertTag = componentAllTag.getComponent();
		ComponentTag compTag = new ComponentTag();
		compTag.setText("New Test Tag");
		compTag.setComponentId(componentAlertTag.getComponentId());
		compTag.setAdminModified(false);
		componentAllTag.getTags().add(compTag);
		service.getComponentService().saveComponentTag(compTag);

		UserMessage userMessage = new UserMessage();
		List<UserMessage> userMessages = userMessage.findByExample();
		boolean alertIdsEqual = false;
		String messageId = "";
		for (UserMessage message : userMessages) {
			if (message.getAlertId().equals(alertUserDataTag.getAlertId())) {
				alertIdsEqual = true;
				messageId = message.getUserMessageId();
			}
		}

		if (alertIdsEqual) {
			service.getUserService().processAllUserMessages(true);
			service.getUserService().removeUserMessage(messageId);
			results.append("Test Passed (Checking Tag) - User data message found<br><br>");
		} else {
			failureReason.append("Test Failed (Checking Tag) - User data message not found<br><br>");
		}

		results.append("Check complete<br><br>");

		results.append("Test 2 - Multiple User Data Option Selected<br><br>");

		alertUserDataRQ = new Alert();
		alertUserDataRQ.setName("New User Data Alert Test2");
		alertUserDataRQ.setActiveStatus(ACTIVE_STATUS);
		alertUserDataRQ.setAlertType(AlertType.USER_DATA);
		emails.add(emailAddress);
		alertUserDataRQ.setEmailAddresses(emails);
		userDataOption = new UserDataAlertOption();
		userDataOption.setAlertOnQuestions(true);
		userDataOption.setAlertOnReviews(true);
		alertUserDataRQ.setUserDataAlertOption(userDataOption);
		alertUserDataRQ = service.getAlertService().saveAlert(alertUserDataRQ);

		results.append("Checking alert user data question message...<br>");
		ComponentAll componentAllRQ = getTestComponent();
		Component compAlertQuestion = componentAllRQ.getComponent();
		QuestionAll question = new QuestionAll();
		ComponentQuestion componentQuestion = new ComponentQuestion();
		componentQuestion.setComponentId(compAlertQuestion.getComponentId());
		componentQuestion.setUserTypeCode(END_USER);
		componentQuestion.setQuestion("Did man really land on the moon?");
		componentQuestion.setOrganization("User Data Organization");
		componentQuestion.setAdminModified(false);
		question.setQuestion(componentQuestion);
		componentAllRQ.getQuestions().add(question);
		service.getComponentService().saveComponentQuestion(componentQuestion);

		userMessage = new UserMessage();
		userMessages = userMessage.findByExample();
		alertIdsEqual = false;
		messageId = "";
		for (UserMessage message : userMessages) {
			if (message.getAlertId().equals(alertUserDataRQ.getAlertId())) {
				alertIdsEqual = true;
				messageId = message.getUserMessageId();
			}
		}

		if (alertIdsEqual) {
			service.getUserService().processAllUserMessages(true);
			service.getUserService().removeUserMessage(messageId);
			results.append("Test Passed (Component Question) - User data message found<br><br>");
		} else {
			failureReason.append("Test Failed (Component Question) - User data message not found<br><br>");
		}
		results.append("Check complete<br><br>");

		results.append("Checking alert user data review message...<br>");
		ReviewAll review = new ReviewAll();
		ComponentReview componentReview = new ComponentReview();
		componentReview.setUserTypeCode(END_USER);
		componentReview.setComponentId(compAlertQuestion.getComponentId());
		componentReview.setComment("User Data Test Comment");
		componentReview.setRating(4);
		componentReview.setTitle("User Data Review Test");
		componentReview.setRecommend(true);
		componentReview.setOrganization("User data test organization");
		componentReview.setAdminModified(false);
		componentReview.setUserTimeCode("TIME_TEST");
		componentReview.setLastUsed(TimeUtil.currentDate());
		experience = new ExperienceTimeType();
		experience.setCode("TIME_TEST");
		experience.setDescription("The Time Experience Test");
		service.getLookupService().saveLookupValue(experience);
		review.setComponentReview(componentReview);
		componentAllRQ.getReviews().add(review);
		service.getComponentService().saveComponentReview(componentReview);

		userMessage = new UserMessage();
		userMessages = userMessage.findByExample();
		alertIdsEqual = false;
		messageId = "";
		for (UserMessage message : userMessages) {
			if (message.getAlertId().equals(alertUserDataRQ.getAlertId())) {
				alertIdsEqual = true;
				messageId = message.getUserMessageId();
			}
		}

		if (alertIdsEqual) {
			service.getUserService().processAllUserMessages(true);
			service.getUserService().removeUserMessage(messageId);
			results.append("Test Passed (Component Review) - User data message found<br><br>");
		} else {
			failureReason.append("Test Failed (Component Review) - User data message not found<br><br>");
		}
		results.append("Check complete<br><br>");

		results.append("Test 3 - User Data Option Not Selected<br><br>");

		alertUserDataContact = new Alert();
		alertUserDataContact.setName("New User Data Alert Test2");
		alertUserDataContact.setActiveStatus(ACTIVE_STATUS);
		alertUserDataContact.setAlertType(AlertType.USER_DATA);
		emails.add(emailAddress);
		alertUserDataContact.setEmailAddresses(emails);
		userDataOption = new UserDataAlertOption();
		userDataOption.setAlertOnContactUpdate(false);
		alertUserDataContact.setUserDataAlertOption(userDataOption);
		alertUserDataContact = service.getAlertService().saveAlert(alertUserDataContact);

		results.append("Checking alert contact message...<br>");
		ComponentAll componentAllContact = getTestComponent();
		Component compContact = componentAllContact.getComponent();
		ComponentContact compAlertContact = new ComponentContact();
		Contact contact = new Contact();
		compAlertContact.setContactId(contact.getContactId());
		compAlertContact.setComponentId(compContact.getComponentId());
		compAlertContact.setContactType(SUBMITTER);
		compAlertContact.setFirstName(TEST_USER);
		compAlertContact.setOrganization("User Data Contact Organization");
		compAlertContact.setAdminModified(false);
		componentAllContact.getContacts().add(compAlertContact);
		service.getComponentService().saveComponentContact(compAlertContact);

		ComponentContact updateCompContact = new ComponentContact();
		updateCompContact.setFirstName(TEST_USER);
		updateCompContact.setComponentId(compContact.getComponentId());
		updateCompContact = (ComponentContact) updateCompContact.find();
		updateCompContact.setFirstName("Updated Test_User");
		service.getComponentService().saveComponentContact(updateCompContact);

		userMessage = new UserMessage();
		userMessages = userMessage.findByExample();
		alertIdsEqual = false;
		messageId = "";
		for (UserMessage message : userMessages) {
			if (message.getAlertId().equals(alertUserDataContact.getAlertId())) {
				alertIdsEqual = true;
				messageId = message.getUserMessageId();
			}
		}

		if (!alertIdsEqual) {
			results.append("Test Passed (ComponentContact) - User data message found<br><br>");
		} else {
			service.getUserService().processAllUserMessages(true);
			service.getUserService().removeUserMessage(messageId);
			failureReason.append("Test Failed (ComponentContact) - User data message found<br><br>");
		}

		results.append("Check complete<br><br>");

	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();
		if (alertUserDataTag != null) {
			service.getAlertService().deleteAlert(alertUserDataTag.getAlertId());
		}
		if (alertUserDataRQ != null) {
			service.getAlertService().deleteAlert(alertUserDataRQ.getAlertId());
		}
		if (alertUserDataContact != null) {
			service.getAlertService().deleteAlert(alertUserDataContact.getAlertId());
		}
		if (!alerts.isEmpty()) {
			activateAlerts(alerts);
		}
		if (experience != null) {
			service.getLookupService().removeValue(ExperienceTimeType.class, experience.getCode());
		}
	}

	@Override
	public String getDescription()
	{
		return "User Data Test";
	}

}
