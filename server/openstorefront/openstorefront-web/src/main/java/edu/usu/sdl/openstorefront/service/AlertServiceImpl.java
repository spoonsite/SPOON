/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.service.api.AlertService;
import edu.usu.sdl.openstorefront.service.transfermodel.AlertContext;
import edu.usu.sdl.openstorefront.storage.model.Alert;
import edu.usu.sdl.openstorefront.storage.model.AlertType;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestion;
import edu.usu.sdl.openstorefront.storage.model.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.storage.model.ComponentReview;
import edu.usu.sdl.openstorefront.storage.model.ComponentTag;
import edu.usu.sdl.openstorefront.storage.model.ErrorTicket;
import edu.usu.sdl.openstorefront.storage.model.ErrorTypeCode;
import edu.usu.sdl.openstorefront.storage.model.UserMessage;
import edu.usu.sdl.openstorefront.storage.model.UserMessageType;
import edu.usu.sdl.openstorefront.util.Convert;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class AlertServiceImpl
		extends ServiceProxy
		implements AlertService
{

	private static final Logger log = Logger.getLogger(AlertServiceImpl.class.getName());

	@Override
	public Alert saveAlert(Alert alert)
	{
		Alert oldAlert = persistenceService.findById(Alert.class, alert.getAlertId());
		if (oldAlert != null) {
			oldAlert.setAlertType(alert.getAlertType());
			oldAlert.setEmailAddresses(alert.getEmailAddresses());
			oldAlert.setName(alert.getName());
			oldAlert.setSystemErrorAlertOption(alert.getSystemErrorAlertOption());
			if (oldAlert.getSystemErrorAlertOption() != null) {
				oldAlert.getSystemErrorAlertOption().populateBaseUpdateFields();
			}
			oldAlert.setUserDataAlertOption(alert.getUserDataAlertOption());
			if (oldAlert.getUserDataAlertOption() != null) {
				oldAlert.getUserDataAlertOption().populateBaseUpdateFields();
			}
			oldAlert.populateBaseUpdateFields();
			persistenceService.persist(oldAlert);
			alert = oldAlert;
		} else {
			alert.setAlertId(persistenceService.generateId());
			alert.populateBaseCreateFields();
			if (alert.getSystemErrorAlertOption() != null) {
				alert.populateBaseCreateFields();
			}
			if (alert.getUserDataAlertOption() != null) {
				alert.populateBaseCreateFields();
			}
			persistenceService.persist(alert);
		}
		return alert;
	}

	@Override
	public void deleteAlert(String alertId)
	{
		Alert existingAlert = persistenceService.findById(Alert.class, alertId);
		if (existingAlert != null) {
			persistenceService.delete(existingAlert);
		}
	}

	@Override
	public void checkAlert(AlertContext alertContext)
	{
		Objects.requireNonNull(alertContext, "Alert context required");
		Objects.requireNonNull(alertContext.getAlertType(), "Alert type required");

		Alert alertExample = new Alert();
		alertExample.setAlertType(alertContext.getAlertType());
		alertExample.setActiveStatus(Alert.ACTIVE_STATUS);
		List<Alert> alerts = persistenceService.queryByExample(Alert.class, alertExample);

		for (Alert alert : alerts) {
			boolean createUserMessage = false;
			String userMessageType = null;
			switch (alertContext.getAlertType()) {
				case AlertType.USER_DATA:
					userMessageType = UserMessageType.USER_DATA_ALERT;
					if (alert.getUserDataAlertOption() != null) {
						//Don't trigger on admin changes
						if (SecurityUtil.isAdminUser() == false) {
							if (alertContext.getDataTrigger() instanceof ComponentTag) {
								if (Convert.toBoolean(alert.getUserDataAlertOption().getAlertOnTags())) {
									createUserMessage = true;
								}
							} else if (alertContext.getDataTrigger() instanceof ComponentReview) {
								if (Convert.toBoolean(alert.getUserDataAlertOption().getAlertOnReviews())) {
									createUserMessage = true;
								}
							} else if (alertContext.getDataTrigger() instanceof ComponentQuestion
									|| alertContext.getDataTrigger() instanceof ComponentQuestionResponse) {
								if (Convert.toBoolean(alert.getUserDataAlertOption().getAlertOnQuestions())) {
									createUserMessage = true;
								}
							}
						}
					}
					break;
				case AlertType.SYSTEM_ERROR:
					userMessageType = UserMessageType.SYSTEM_ERROR_ALERT;
					if (alert.getSystemErrorAlertOption() != null) {
						if (alertContext.getDataTrigger() instanceof ErrorTicket) {
							ErrorTicket errorTicket = (ErrorTicket) alertContext.getDataTrigger();

							if (ErrorTypeCode.INTEGRATION.equals(errorTicket.getErrorTypeCode())) {
								if (Convert.toBoolean(alert.getSystemErrorAlertOption().getAlertOnIntegration())) {
									createUserMessage = true;
								}
							} else if (ErrorTypeCode.SYSTEM.equals(errorTicket.getErrorTypeCode())) {
								if (Convert.toBoolean(alert.getSystemErrorAlertOption().getAlertOnIntegration())) {
									createUserMessage = true;
								}
							} else if (ErrorTypeCode.REST_API.equals(errorTicket.getErrorTypeCode())) {
								if (Convert.toBoolean(alert.getSystemErrorAlertOption().getAlertOnIntegration())) {
									createUserMessage = true;
								}
							} else if (ErrorTypeCode.REPORT.equals(errorTicket.getErrorTypeCode())) {
								if (Convert.toBoolean(alert.getSystemErrorAlertOption().getAlertOnReport())) {
									createUserMessage = true;
								}
							} else {
								log.log(Level.FINE, MessageFormat.format("Missing option for error type: {0}", errorTicket.getErrorTypeCode()));
							}
						} else {
							log.log(Level.FINE, "System error without a ticket...check alert.");
						}
					}
					break;
			}

			if (createUserMessage) {
				for (String email : alert.getEmailAddresses()) {
					UserMessage userMessage = new UserMessage();
					userMessage.setEmailAddress(email);
					userMessage.setAlertId(alert.getAlertId());
					userMessage.setUserMessageType(userMessageType);
					getUserService().queueUserMessage(userMessage);
				}
			}
		}
	}

}
