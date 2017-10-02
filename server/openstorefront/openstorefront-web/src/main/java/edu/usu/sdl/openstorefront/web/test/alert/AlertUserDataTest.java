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
import edu.usu.sdl.openstorefront.core.entity.UserMessageType;
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
	private List<Alert> existingAlerts = null;
	private ExperienceTimeType experienceTimeType = null;
	private String messageId = null;
	private String contactId = null;

	@Override
	protected void initializeTest()
	{
		// Get all alerts and set to inactive
		existingAlerts = getActiveAlerts();
		if (!existingAlerts.isEmpty()) {
			inactivateAlerts(existingAlerts);
		}
	}

	@Override
	protected void runInternalTest()
	{
		ComponentAll componentAll = getTestComponent();

		results.append("Test 1 - One User Data Option Selected<br><br>");
		alertUserDataTag = new Alert();
		alertUserDataTag.setName("New User Data Alert Test Tag");
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
		Component componentAlertTag = componentAll.getComponent();
		ComponentTag compTag = new ComponentTag();
		compTag.setText("New Test Tag");
		compTag.setComponentId(componentAlertTag.getComponentId());
		compTag.setAdminModified(false);
		componentAll.getTags().add(compTag);
		service.getComponentService().saveComponentTag(compTag);

		UserMessage userMessage = new UserMessage();
		List<UserMessage> userMessages = userMessage.findByExample();
		boolean alertIdsEqual = false;
		for (UserMessage message : userMessages) {
			if (UserMessageType.USER_DATA_ALERT.equals(message.getUserMessageType())
					&& message.getAlertId().equals(alertUserDataTag.getAlertId())) {

				alertIdsEqual = true;
				messageId = message.getUserMessageId();
			}
		}

		if (alertIdsEqual) {
			service.getUserService().processAllUserMessages(true);
			service.getUserService().removeUserMessage(messageId);
			results.append("Test Passed - Tag user data message found<br><br>");
		} else {
			failureReason.append("Test Failed - Tag user data message not found (Note: Your Login permission may have prevent triggering alert.)<br><br>");
		}

		results.append("Check complete<br><br>");

		results.append("Test 2 - Multiple User Data Option Selected<br><br>");

		alertUserDataRQ = new Alert();
		alertUserDataRQ.setName("New User Data Alert Test RQ");
		alertUserDataRQ.setActiveStatus(ACTIVE_STATUS);
		alertUserDataRQ.setAlertType(AlertType.USER_DATA);
		alertUserDataRQ.setEmailAddresses(emails);
		userDataOption = new UserDataAlertOption();
		userDataOption.setAlertOnQuestions(true);
		userDataOption.setAlertOnReviews(true);
		alertUserDataRQ.setUserDataAlertOption(userDataOption);
		alertUserDataRQ = service.getAlertService().saveAlert(alertUserDataRQ);

		results.append("Checking alert user data question message...<br>");

		QuestionAll question = new QuestionAll();
		ComponentQuestion componentQuestion = new ComponentQuestion();
		componentQuestion.setComponentId(componentAll.getComponent().getComponentId());
		componentQuestion.setUserTypeCode(END_USER);
		componentQuestion.setQuestion("What is the largest basin on the moon?");
		componentQuestion.setOrganization(TEST_ORGANIZATION);
		componentQuestion.setAdminModified(false);
		question.setQuestion(componentQuestion);
		componentAll.getQuestions().add(question);
		service.getComponentService().saveComponentQuestion(componentQuestion);

		userMessage = new UserMessage();
		userMessages = userMessage.findByExample();
		alertIdsEqual = false;
		messageId = "";
		for (UserMessage message : userMessages) {
			if (UserMessageType.USER_DATA_ALERT.equals(message.getUserMessageType())
					&& message.getAlertId().equals(alertUserDataRQ.getAlertId())) {

				alertIdsEqual = true;
				messageId = message.getUserMessageId();
			}
		}

		if (alertIdsEqual) {
			service.getUserService().processAllUserMessages(true);
			service.getUserService().removeUserMessage(messageId);
			results.append("Test Passed - Question user data message found<br><br>");
		} else {
			failureReason.append("Test Failed - Question user data message not found. (Note: Your Login permission may have prevent triggering alert.)<br><br>");
		}
		results.append("Check complete<br><br>");

		results.append("Checking alert user data review message...<br>");
		ReviewAll review = new ReviewAll();
		ComponentReview componentReview = new ComponentReview();
		componentReview.setUserTypeCode(END_USER);
		componentReview.setComponentId(componentAll.getComponent().getComponentId());
		componentReview.setComment("User Data Review Test Comment");
		componentReview.setRating(4);
		componentReview.setTitle("User Data Review Test Title");
		componentReview.setRecommend(true);
		componentReview.setOrganization(TEST_ORGANIZATION);
		componentReview.setAdminModified(false);
		componentReview.setUserTimeCode("TIME_TEST");
		componentReview.setLastUsed(TimeUtil.currentDate());

		experienceTimeType = new ExperienceTimeType();
		experienceTimeType.setCode("TIME_TEST");
		experienceTimeType.setDescription("The Time Experience Test");
		service.getLookupService().saveLookupValue(experienceTimeType);

		review.setComponentReview(componentReview);
		componentAll.getReviews().add(review);
		service.getComponentService().saveComponentReview(componentReview);

		userMessage = new UserMessage();
		userMessages = userMessage.findByExample();
		alertIdsEqual = false;
		messageId = "";
		for (UserMessage message : userMessages) {
			if (UserMessageType.USER_DATA_ALERT.equals(message.getUserMessageType())
					&& message.getAlertId().equals(alertUserDataRQ.getAlertId())) {

				alertIdsEqual = true;
				messageId = message.getUserMessageId();
			}
		}

		if (alertIdsEqual) {
			service.getUserService().processAllUserMessages(true);
			service.getUserService().removeUserMessage(messageId);
			results.append("Test Passed - Review user data message found<br><br>");
		} else {
			failureReason.append("Test Failed - Review user data message not found (Note: Your Login permission may have prevent triggering alert.)<br><br>");
		}
		results.append("Check complete<br><br>");

		results.append("Test 3 - User Data Option Not Selected<br><br>");

		alertUserDataContact = new Alert();
		alertUserDataContact.setName("New User Data Alert Test Contact");
		alertUserDataContact.setActiveStatus(ACTIVE_STATUS);
		alertUserDataContact.setAlertType(AlertType.USER_DATA);
		alertUserDataContact.setEmailAddresses(emails);
		userDataOption = new UserDataAlertOption();
		userDataOption.setAlertOnContactUpdate(false);
		alertUserDataContact.setUserDataAlertOption(userDataOption);
		alertUserDataContact = service.getAlertService().saveAlert(alertUserDataContact);

		results.append("Checking alert contact message...<br>");

		ComponentContact compAlertContact = new ComponentContact();
		Contact contact = new Contact();
		compAlertContact.setContactId(contact.getContactId());
		compAlertContact.setComponentId(componentAll.getComponent().getComponentId());
		compAlertContact.setContactType(SUBMITTER);
		compAlertContact.setFirstName("UserDataFirstName1");
		compAlertContact.setLastName("UserDataLastName1");
		compAlertContact.setEmail("myUserDataTest@alertuserdata.com");
		compAlertContact.setOrganization(TEST_ORGANIZATION);
		compAlertContact.setAdminModified(false);
		componentAll.getContacts().add(compAlertContact);
		service.getComponentService().saveComponentContact(compAlertContact);

		ComponentContact updatedCompContact = new ComponentContact();
		updatedCompContact.setFirstName("UserDataFirstName1");
		updatedCompContact.setEmail("myUserDataTest@alertuserdata.com");
		updatedCompContact.setComponentId(componentAll.getComponent().getComponentId());
		updatedCompContact = updatedCompContact.find();
		contactId = updatedCompContact.getContactId();
		updatedCompContact.setFirstName("Updated UserData Contact FirstName");
		service.getComponentService().saveComponentContact(updatedCompContact);

		userMessage = new UserMessage();
		userMessages = userMessage.findByExample();
		alertIdsEqual = false;
		messageId = "";
		for (UserMessage message : userMessages) {
			if (UserMessageType.USER_DATA_ALERT.equals(message.getUserMessageType())
					&& message.getAlertId().equals(alertUserDataContact.getAlertId())) {

				alertIdsEqual = true;
				messageId = message.getUserMessageId();
			}
		}

		if (!alertIdsEqual) {
			results.append("Test Passed - User data message not found<br><br>");
		} else {
			service.getUserService().removeUserMessage(messageId);
			failureReason.append("Test Failed - User data message found (Note: Your Login permission may have prevent triggering alert.)<br><br>");
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
		if (experienceTimeType != null) {
			service.getLookupService().removeValue(ExperienceTimeType.class, experienceTimeType.getCode());
		}
		if (contactId != null) {
			service.getContactService().deleteContact(contactId);
		}
		if (messageId != null) {
			service.getUserService().removeUserMessage(messageId);
		}
		if (!existingAlerts.isEmpty()) {
			activateAlerts(existingAlerts);
		}
	}

	@Override
	public String getDescription()
	{
		return "User Data Test";
	}

}
