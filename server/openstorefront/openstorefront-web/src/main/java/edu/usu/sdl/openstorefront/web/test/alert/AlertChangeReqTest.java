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
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.EmailAddress;
import static edu.usu.sdl.openstorefront.core.entity.StandardEntity.ACTIVE_STATUS;
import edu.usu.sdl.openstorefront.core.entity.UserMessage;
import edu.usu.sdl.openstorefront.core.model.AlertContext;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
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
public class AlertChangeReqTest extends BaseTestCase
{

	private Alert alertChangeReq = null;
	String messageId = null;
	private List<Alert> alerts = null;


	public AlertChangeReqTest()
	{
	}

	@Override
	protected void runInternalTest()
	{

		// Get all alerts and set to inactive
		alerts = getActiveAlerts();
		if (!alerts.isEmpty()) {
			inactivateAlerts(alerts);
		}

		alertChangeReq = new Alert();
		alertChangeReq.setName("New Change Request Alert");
		alertChangeReq.setActiveStatus(ACTIVE_STATUS);
		alertChangeReq.setAlertType(AlertType.CHANGE_REQUEST);
		EmailAddress emailAddress = new EmailAddress();
		emailAddress.setEmail(getSystemEmail());
		List<EmailAddress> emails = new ArrayList();
		emails.add(emailAddress);
		alertChangeReq.setEmailAddresses(emails);

		results.append("Saving change request alert...<br>");
		alertChangeReq = service.getAlertService().saveAlert(alertChangeReq);
		results.append("Alert saved<br><br>");

		AlertContext alertContxt = new AlertContext();
		alertContxt.setAlertType(alertChangeReq.getAlertType());

		ComponentAll componentAll = getTestComponent();
		Component compChangeReq = componentAll.getComponent();
		compChangeReq.setName("Change Request");
		compChangeReq.setActiveStatus(Component.PENDING_STATUS);
		compChangeReq.setApprovalState(ApprovalStatus.PENDING);
		compChangeReq.setSubmittedDts(TimeUtil.currentDate());
		service.getComponentService().saveFullComponent(componentAll);
		alertContxt.setDataTrigger(compChangeReq);

		results.append("Checking alert...<br>");
		service.getAlertService().checkAlert(alertContxt);
		results.append("Check complete<br><br>");

		UserMessage userMessage = new UserMessage();
		userMessage.setActiveStatus(ACTIVE_STATUS);
		List<UserMessage> userMessages = userMessage.findByExample();
		boolean alertIdsEqual = false;
		for (UserMessage message : userMessages) {
			if (message.getAlertId().equals(alertChangeReq.getAlertId())) {
				alertIdsEqual = true;
				messageId = message.getUserMessageId();
			}
		}

		if (alertIdsEqual) {
			service.getUserService().processAllUserMessages(true);
			results.append("Test Passed - Change request message found<br><br>");
		} else {
			failureReason.append("Test Failed - Unable to find change request message<br><br>");
		}
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();
		if (alertChangeReq != null) {
			service.getAlertService().deleteAlert(alertChangeReq.getAlertId());
		}
		service.getUserService().removeUserMessage(messageId);
		if (!alerts.isEmpty()) {
			activateAlerts(alerts);
		}
	}

	@Override
	public String getDescription()
	{
		return "Change Request Test";
	}
}
