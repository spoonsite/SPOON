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
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeAlertOption;
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
public class AlertCompSubmissionTest extends BaseTestCase
{

	private Alert alertCompSubmission = null;
	String messageId = null;
	private List<Alert> alerts = null;

	@Override
	protected void runInternalTest()
	{
		// Get all alerts and set to inactive
		alerts = getActiveAlerts();
		if (!alerts.isEmpty()) {
			inactivateAlerts(alerts);
		}

		ComponentAll componentAll = getTestComponent();

		alertCompSubmission = new Alert();
		alertCompSubmission.setName("New Component Submission Alert");
		alertCompSubmission.setActiveStatus(ACTIVE_STATUS);
		alertCompSubmission.setAlertType(AlertType.COMPONENT_SUBMISSION);
		List<ComponentTypeAlertOption> componentTypeOptions = new ArrayList<>();
		ComponentTypeAlertOption option = new ComponentTypeAlertOption();
		option.setComponentType(componentAll.getComponent().getComponentType());
		componentTypeOptions.add(option);
		alertCompSubmission.setComponentTypeAlertOptions(componentTypeOptions);

		EmailAddress emailAddress = new EmailAddress();
		emailAddress.setEmail(getSystemEmail());
		List<EmailAddress> emails = new ArrayList<>();
		emails.add(emailAddress);
		alertCompSubmission.setEmailAddresses(emails);

		results.append("Saving component submission alert...<br>");
		alertCompSubmission = service.getAlertService().saveAlert(alertCompSubmission);
		results.append("Alert saved<br><br>");

		AlertContext alertCont = new AlertContext();
		alertCont.setAlertType(alertCompSubmission.getAlertType());

		Component componentSub = componentAll.getComponent();
		componentSub.setName("Component Submission");
		componentSub.setActiveStatus(Component.ACTIVE_STATUS);
		componentSub.setApprovalState(ApprovalStatus.PENDING);
		componentSub.setSubmittedDts(TimeUtil.currentDate());
		service.getComponentService().saveFullComponent(componentAll);
		alertCont.setDataTrigger(componentSub);

		results.append("Checking alert...<br>");
		service.getAlertService().checkAlert(alertCont);
		results.append("Check complete<br><br>");

		UserMessage userMessage = new UserMessage();
		userMessage.setActiveStatus(ACTIVE_STATUS);
		List<UserMessage> userMessages = userMessage.findByExample();
		boolean alertIdsEqual = false;
		for (UserMessage message : userMessages) {
			if (message.getAlertId().equals(alertCompSubmission.getAlertId())) {
				alertIdsEqual = true;
				messageId = message.getUserMessageId();
			}
		}

		if (alertIdsEqual) {
			service.getUserService().processAllUserMessages(true);
			results.append("Test Passed:  Component submission message found<br><br>");
		} else {
			failureReason.append("Test Failed:  Unable to find component submission message<br><br>");
		}
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();
		if (alertCompSubmission != null) {
			service.getAlertService().deleteAlert(alertCompSubmission.getAlertId());
		}
		service.getUserService().removeUserMessage(messageId);
		if (!alerts.isEmpty()) {
			activateAlerts(alerts);
		}
	}

	@Override
	public String getDescription()
	{
		return "CompSubmission Test";
	}
}
