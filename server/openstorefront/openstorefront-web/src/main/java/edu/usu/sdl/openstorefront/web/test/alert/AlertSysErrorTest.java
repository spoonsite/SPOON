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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.Alert;
import edu.usu.sdl.openstorefront.core.entity.AlertType;
import edu.usu.sdl.openstorefront.core.entity.EmailAddress;
import static edu.usu.sdl.openstorefront.core.entity.StandardEntity.ACTIVE_STATUS;
import edu.usu.sdl.openstorefront.core.entity.SystemErrorAlertOption;
import edu.usu.sdl.openstorefront.core.entity.UserMessage;
import edu.usu.sdl.openstorefront.core.model.ErrorInfo;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class AlertSysErrorTest extends BaseTestCase
{

	private Alert alertSysError = null;

	@Override
	protected void runInternalTest()
	{
		alertSysError = new Alert();
		alertSysError.setName("New System Alert");
		alertSysError.setActiveStatus(ACTIVE_STATUS);
		alertSysError.setAlertType(AlertType.SYSTEM_ERROR);
		SystemErrorAlertOption alertOption = new SystemErrorAlertOption();
		alertOption.setAlertOnSystem(true);
		alertOption.setAlertOnIntegration(true);
		alertSysError.setSystemErrorAlertOption(alertOption);
		EmailAddress emailAddress = new EmailAddress();
		emailAddress.setEmail(getSystemEmail());
		List<EmailAddress> emails = new ArrayList();
		emails.add(emailAddress);
		alertSysError.setEmailAddresses(emails);

		results.append("Saving system alert...<br>");
		alertSysError = service.getAlertService().saveAlert(alertSysError);
		results.append("Alert saved<br><br>");

		// Check alert method is called in generateErrorTicket
		results.append("Checking alert...<br><br>");
		ErrorInfo errorInfo = new ErrorInfo(new OpenStorefrontRuntimeException("System Error"), null);
		service.getSystemService().generateErrorTicket(errorInfo);
				
		UserMessage userMessage = new UserMessage();
		userMessage.setActiveStatus(ACTIVE_STATUS);
		List<UserMessage> userMessages = userMessage.findByExample();
		boolean alertIdsEqual = false;
		String messageId = "";
		for (UserMessage message : userMessages) {
			if (message.getAlertId().equals(alertSysError.getAlertId())) {
				alertIdsEqual = true;
				messageId = message.getUserMessageId();
			}
		}

		if (alertIdsEqual) {
			service.getUserService().processAllUserMessages(true);
			service.getUserService().removeUserMessage(messageId);
			results.append("Test Passed:  System error message found<br><br>");
		} else {
			failureReason.append("Test Failed:  Unable to find system error message<br><br>");
		}
	}

	@Override
	protected void cleanupTest()
	{
		super.cleanupTest();
		if (alertSysError != null) {
			service.getAlertService().deleteAlert(alertSysError.getAlertId());
		}
	}

	@Override
	public String getDescription()
	{
		return "System Error Test";
	}
}
