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

import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.api.AlertService;
import edu.usu.sdl.openstorefront.core.entity.Alert;
import edu.usu.sdl.openstorefront.core.entity.AlertType;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestion;
import edu.usu.sdl.openstorefront.core.entity.ComponentQuestionResponse;
import edu.usu.sdl.openstorefront.core.entity.ComponentReview;
import edu.usu.sdl.openstorefront.core.entity.ComponentTag;
import edu.usu.sdl.openstorefront.core.entity.Contact;
import edu.usu.sdl.openstorefront.core.entity.EmailAddress;
import edu.usu.sdl.openstorefront.core.entity.ErrorTicket;
import edu.usu.sdl.openstorefront.core.entity.ErrorTypeCode;
import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;
import edu.usu.sdl.openstorefront.core.entity.NotificationEventType;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.entity.UserMessage;
import edu.usu.sdl.openstorefront.core.entity.UserMessageType;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserRegistration;
import edu.usu.sdl.openstorefront.core.entity.UserSecurity;
import edu.usu.sdl.openstorefront.core.model.AlertContext;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles Alerts triggers
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
			oldAlert.updateFields(alert);
			persistenceService.persist(oldAlert);
			alert = oldAlert;
		} else {
			alert.setAlertId(persistenceService.generateId());
			alert.populateBaseCreateFields();
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
		List<Alert> alerts = persistenceService.queryByExample(alertExample);

		for (Alert alert : alerts) {
			boolean createUserMessage = false;
			String userMessageType = null;
			switch (alertContext.getAlertType()) {
				case AlertType.USER_DATA:
					userMessageType = UserMessageType.USER_DATA_ALERT;
					if (alert.getUserDataAlertOption() != null) {
						//Don't trigger on admin changes
						if (alertContext.getDataTrigger() != null) {
							boolean adminModified = false;
							if (alertContext.getDataTrigger() instanceof StandardEntity) {
								adminModified = Convert.toBoolean(((StandardEntity) alertContext.getDataTrigger()).getAdminModified());
								if (adminModified == false) {
									adminModified = SecurityUtil.isEvaluatorUser();
								}
							}

							if (adminModified == false) {
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
								} else if (alertContext.getDataTrigger() instanceof Contact) {
									if (Convert.toBoolean(alert.getUserDataAlertOption().getAlertOnContactUpdate())) {
										createUserMessage = true;
									}
								} else if (alertContext.getDataTrigger() instanceof AttributeCode) {
									if (Convert.toBoolean(alert.getUserDataAlertOption().getAlertOnUserAttributeCodes())) {
										createUserMessage = true;
									}
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
				case AlertType.COMPONENT_SUBMISSION:
					userMessageType = UserMessageType.COMPONENT_SUBMISSION_ALERT;
					createUserMessage = true;
					break;
				case AlertType.CHANGE_REQUEST:
					userMessageType = UserMessageType.CHANGE_REQUEST_ALERT;
					createUserMessage = true;
					break;
				case AlertType.USER_MANAGEMENT:
					if (alert.getUserManagementAlertOption() != null) {
						if (alertContext.getDataTrigger() instanceof UserRegistration) {
							if (alert.getUserManagementAlertOption().getAlertOnUserRegistration()) {
								userMessageType = UserMessageType.USER_REGISTRATION;
								createUserMessage = true;
							}
						} else if (alertContext.getDataTrigger() instanceof UserSecurity) {
							if (alert.getUserManagementAlertOption().getAlertOnUserNeedsApproval()) {
								userMessageType = UserMessageType.USER_NEED_APPROVAL;
								createUserMessage = true;
							}
						}
					}
					break;
			}

			if (createUserMessage) {
				if (alert.getEmailAddresses() == null) {
					alert.setEmailAddresses(new ArrayList<>());
				}
				for (EmailAddress email : alert.getEmailAddresses()) {
					UserMessage userMessage = new UserMessage();
					userMessage.setEmailAddress(email.getEmail());
					userMessage.setAlertId(alert.getAlertId());
					userMessage.setUserMessageType(userMessageType);
					getUserService().queueUserMessage(userMessage);

					UserProfile userProfile = new UserProfile();
					userProfile.setActiveStatus(UserProfile.ACTIVE_STATUS);
					userProfile.setEmail(email.getEmail());
					userProfile = (UserProfile) userProfile.find();
					if (userProfile != null) {
						if (AlertType.CHANGE_REQUEST.equals(alert.getAlertType())) {

							String message = "";
							String componentId = null;
							if (alertContext.getDataTrigger() instanceof Component) {
								Component component = (Component) alertContext.getDataTrigger();
								componentId = component.getComponentId();
								if (ApprovalStatus.NOT_SUBMITTED.equals(component.getApprovalState())) {
									message = "Change request for entry: " + component.getName() + " has been un-submitted for approval.";
								} else {
									message = "Change request for entry: " + component.getName() + " has been submitted for approval.";
								}
							}
							NotificationEvent notificationEvent = new NotificationEvent();
							notificationEvent.setEventType(NotificationEventType.CHANGE_REQUEST);
							notificationEvent.setUsername(userProfile.getUsername());
							notificationEvent.setMessage(message);
							notificationEvent.setEntityName(Component.class.getSimpleName());
							notificationEvent.setEntityId(componentId);
							getNotificationService().postEvent(notificationEvent);
						}

						if (AlertType.COMPONENT_SUBMISSION.equals(alert.getAlertType())) {

							String message = "";
							String componentId = null;
							if (alertContext.getDataTrigger() instanceof Component) {
								Component component = (Component) alertContext.getDataTrigger();
								componentId = component.getComponentId();
								if (ApprovalStatus.NOT_SUBMITTED.equals(component.getApprovalState())) {
									message = "Entry: " + component.getName() + " has been un-submitted for approval.";
								} else {
									message = "Entry: " + component.getName() + " has been submitted for approval.";
								}
							}
							NotificationEvent notificationEvent = new NotificationEvent();
							notificationEvent.setEventType(NotificationEventType.SUBMISSION);
							notificationEvent.setUsername(userProfile.getUsername());
							notificationEvent.setMessage(message);
							notificationEvent.setEntityName(Component.class.getSimpleName());
							notificationEvent.setEntityId(componentId);
							getNotificationService().postEvent(notificationEvent);
						}
					}
				}

			}
		}
	}

}
