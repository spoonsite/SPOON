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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.api.NotificationService;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;
import edu.usu.sdl.openstorefront.core.entity.NotificationEventReadStatus;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserRole;
import edu.usu.sdl.openstorefront.core.spi.NotificationEventListener;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.NotificationEventView;
import edu.usu.sdl.openstorefront.core.view.NotificationEventWrapper;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.api.NotificationServicePrivate;
import edu.usu.sdl.openstorefront.service.manager.MailManager;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.service.model.EmailCommentModel;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.mail.Message;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.codemonkey.simplejavamail.email.Email;

/**
 * Handles Notification Events
 *
 * @author dshurtleff
 */
public class NotificationServiceImpl
		extends ServiceProxy
		implements NotificationService, NotificationServicePrivate
{

	private static final String LISTENER_KEY = "NOTIFICATION_LISTENERS";
	private static final String SUBMISSION_COMMENT_SUBJECT = "Submission Comment was made.";
	private static final String NO_EMAIL = "User is missing email address.";
	private static final String NO_EMAIL_SOL = "Add a valid email address.";
	private static final String NO_USER = "NOTIFICATION_LISTENERS";
	private static final String NO_USER_SOL = "NOTIFICATION_LISTENERS";

	@Override
	public NotificationEventWrapper getAllEventsForUser(String username, FilterQueryParams queryParams)
	{
		NotificationEventWrapper notificationEventWrapper = new NotificationEventWrapper();

		long totalCount = getRepoFactory().getNotificationRepo().getTotalNotificationsForUser(username);
		notificationEventWrapper.setTotalNumber(totalCount);

		List<NotificationEvent> notificationEvents = getRepoFactory().getNotificationRepo().getNotificationsForUser(username, queryParams);

		notificationEventWrapper.setResults(notificationEvents.size());
		notificationEventWrapper.setData(NotificationEventView.toView(notificationEvents));

		//mark read flag
		Set<String> eventIds = new HashSet<>();
		for (NotificationEventView view : notificationEventWrapper.getData()) {
			eventIds.add(view.getEventId());
		}

		if (eventIds.isEmpty() == false) {
			List<NotificationEventReadStatus> readStatuses = getRepoFactory().getNotificationRepo().getReadNoficationsForUser(username, eventIds);

			Set<String> readSet = new HashSet<>();
			for (NotificationEventReadStatus readStatus : readStatuses) {
				readSet.add(readStatus.getEventId());
			}

			for (NotificationEventView view : notificationEventWrapper.getData()) {
				if (readSet.contains(view.getEventId())) {
					view.setReadMessage(true);
				}
			}

		}

		//apply read filter
		if (Convert.toBoolean(queryParams.getAll()) == false) {
			notificationEventWrapper.setData(notificationEventWrapper.getData().stream().filter(r -> r.getReadMessage() == false).collect(Collectors.toList()));
		}

		// Return Response
		return notificationEventWrapper;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void registerNotificationListerner(NotificationEventListener notificationEventListerner)
	{
		Element element = OSFCacheManager.getApplicationCache().get(LISTENER_KEY);
		if (element == null) {
			List<NotificationEventListener> listeners = new ArrayList<>();
			element = new Element(LISTENER_KEY, listeners);
			OSFCacheManager.getApplicationCache().put(element);
		}
		((List<NotificationEventListener>) element.getObjectValue()).add(notificationEventListerner);
	}

	@Override
	public NotificationEvent postEvent(NotificationEvent notificationEvent)
	{
		notificationEvent.setEventId(getPersistenceService().generateId());
		notificationEvent.populateBaseCreateFields();
		notificationEvent = getPersistenceService().persist(notificationEvent);

		Element element = OSFCacheManager.getApplicationCache().get(LISTENER_KEY);
		if (element != null) {
			@SuppressWarnings("unchecked")
			List<NotificationEventListener> listerners = (List<NotificationEventListener>) element.getObjectValue();
			for (NotificationEventListener listerner : listerners) {
				listerner.processEvent(getPersistenceService().deattachAll(notificationEvent));
			}
		}
		return notificationEvent;
	}

	@Override
	public void deleteEvent(String eventId)
	{
		NotificationEvent notificationEvent = getPersistenceService().findById(NotificationEvent.class, eventId);
		if (notificationEvent != null) {

			NotificationEventReadStatus notificationEventReadStatus = new NotificationEventReadStatus();
			notificationEventReadStatus.setEventId(eventId);
			getPersistenceService().deleteByExample(notificationEventReadStatus);

			getPersistenceService().delete(notificationEvent);
		}
	}

	@Override
	public void cleanupOldEvents()
	{
		int maxDays = Convert.toInteger(PropertiesManager.getInstance().getValueDefinedDefault(PropertiesManager.KEY_NOTIFICATION_MAX_DAYS));

		LocalDateTime archiveTime = LocalDateTime.now();
		archiveTime = archiveTime.minusDays(maxDays);
		archiveTime = archiveTime.truncatedTo(ChronoUnit.DAYS);

		ZonedDateTime zdt = archiveTime.atZone(ZoneId.systemDefault());
		Date archiveDts = Date.from(zdt.toInstant());

		getRepoFactory().getNotificationRepo().deleteNotificationBeforeDate(archiveDts);
	}

	@Override
	public void markEventAsRead(String eventId, String username)
	{
		Objects.requireNonNull(eventId);
		Objects.requireNonNull(username);

		NotificationEventReadStatus notificationEventReadStatus = new NotificationEventReadStatus();
		notificationEventReadStatus.setReadStatusId(getPersistenceService().generateId());
		notificationEventReadStatus.setEventId(eventId);
		notificationEventReadStatus.setUsername(username);

		getPersistenceService().persist(notificationEventReadStatus);
	}

	@Override
	public void markEventAsUnRead(String eventId, String username)
	{
		Objects.requireNonNull(eventId);
		Objects.requireNonNull(username);

		NotificationEventReadStatus notificationEventReadStatus = new NotificationEventReadStatus();
		notificationEventReadStatus.setEventId(eventId);
		notificationEventReadStatus.setUsername(username);

		NotificationEventReadStatus temp = notificationEventReadStatus.findProxy();

		getPersistenceService().delete(temp);
	}

	@Override
	public void deleteEventsForUser(String username)
	{
		if (StringUtils.isNotBlank(username)) {
			//mark all event global events and read
			NotificationEvent notificationEventExample = new NotificationEvent();
			notificationEventExample.setActiveStatus(NotificationEvent.ACTIVE_STATUS);
			QueryByExample<NotificationEvent> queryByExample = new QueryByExample<>(notificationEventExample);

			NotificationEvent notificationNotExample = new NotificationEvent();
			notificationNotExample.setUsername(QueryByExample.STRING_FLAG);
			notificationNotExample.setRoleGroup(QueryByExample.STRING_FLAG);
			SpecialOperatorModel<NotificationEvent> specialOperatorModel = new SpecialOperatorModel<>(notificationNotExample);
			specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_NULL);
			queryByExample.getExtraWhereCauses().add(specialOperatorModel);

			List<NotificationEvent> notificationEvents = getPersistenceService().queryByExample(queryByExample);
			for (NotificationEvent notificationEvent : notificationEvents) {
				markEventAsRead(notificationEvent.getEventId(), username);
			}

			//delete user events
			NotificationEvent notificationEvent = new NotificationEvent();
			notificationEvent.setUsername(username);
			getPersistenceService().deleteByExample(notificationEvent);
		} else {
			throw new OpenStorefrontRuntimeException("Username is required.", "Check data passed in.");
		}
	}

	private void sendEmailToProfile(UserProfile userProfile, EmailCommentModel emailCommentModel)
	{
		emailCommentModel.updateConfigs();
		if (userProfile != null) {
			if (StringUtils.isNotBlank(userProfile.getEmail())) {
				Email email = MailManager.newTemplateEmail(MailManager.Templates.EMAIL_COMMENT.toString(), emailCommentModel, false);
				email.setSubject(SUBMISSION_COMMENT_SUBJECT);
				email.addRecipient("", userProfile.getEmail(), Message.RecipientType.TO);
				MailManager.send(email, true);
			} else {
				throw new OpenStorefrontRuntimeException(NO_EMAIL, NO_EMAIL_SOL);
			}
		} else {
			throw new OpenStorefrontRuntimeException(NO_USER, NO_USER_SOL);
		}

	}

	@Override
	public void emailCommentMessage(EmailCommentModel emailCommentModel)
	{
		emailCommentModel.updateConfigs();
		List<UserRole> userRoles = new ArrayList<>();
		if (StringUtils.isNotBlank(emailCommentModel.getAssignedGroup()) && StringUtils.isNotEmpty(emailCommentModel.getAssignedGroup())) {
			UserRole userRole = new UserRole();
			userRole.setRole(emailCommentModel.getAssignedGroup());
			userRole.setActiveStatus(UserRole.ACTIVE_STATUS);
			userRoles = userRole.findByExample();

			userRoles.removeIf((uRole) -> {
				return SecurityUtil.getCurrentUserName().equals(uRole.getUsername());
			});
		}

		boolean canEmailAssignee = StringUtils.isNotBlank(emailCommentModel.getAssignedUser()) && !SecurityUtil.getCurrentUserName().equals(emailCommentModel.getAssignedUser());
		boolean canEmailGroup = !userRoles.isEmpty();
		boolean canEmailOwner = StringUtils.isNotBlank(emailCommentModel.getEntryOwner()) && !SecurityUtil.getCurrentUserName().equals(emailCommentModel.getEntryOwner());

		if (!emailCommentModel.isAdminComment()) {
			// THIS IS AN OWNER COMMENT.
			if (canEmailAssignee) {
				// EMAIL THE ASSIGNEE FROM THE WORKLINK
				sendEmailToProfile(getUserService().getUserProfile(emailCommentModel.getAssignedUser()), emailCommentModel);
			} else if (canEmailGroup) {
				// EMAIL THE GROUP BUT DON'T EMAIL THE PERSON WHO MADE THE COMMENT
				for (UserRole uRole : userRoles) {
					sendEmailToProfile(getUserService().getUserProfile(uRole.getUsername()), emailCommentModel);
				}
			} else {
				// EMAIL SUPPORT SO THAT THE OWNER ALWAYS HAS A CONTACT. support@spoonsite.com
				Email email = MailManager.newTemplateEmail(MailManager.Templates.EMAIL_COMMENT.toString(), emailCommentModel, false);
				email.setSubject("Owner Comment to Support.");
				email.addRecipient("", "support@spoonsite.com", Message.RecipientType.TO);
				MailManager.send(email, true);

			}
		} else {
			// THIS IS AN ADMIN COMMENT
			if (emailCommentModel.isPrivateComment()) {
				// THIS IS AN ADMIN PRIVATE COMMENT DO NOT EMAIL THE OWNER.
				if (canEmailAssignee) {
					// EMAIL THE ASSIGNEE FROM THE WORKLINK
					sendEmailToProfile(getUserService().getUserProfile(emailCommentModel.getAssignedUser()), emailCommentModel);
				} else if (canEmailGroup) {
					// EMAIL THE GROUP BUT DON'T EMAIL THE PERSON WHO MADE THE COMMENT.
					for (UserRole uRole : userRoles) {
						sendEmailToProfile(getUserService().getUserProfile(uRole.getUsername()), emailCommentModel);
					}
				}
			} else {
				// THIS IS AN ADMIN PUBLIC COMMENT. SEND AN EMAIL TO THE OWNER, GROUP, AND, ASSIGNEE BUT DON'T SEND AN EMAIL TO THE PERSON WHO MADE THE COMMENT.
				if (canEmailAssignee) {
					// EMAIL THE ASSIGNEE FROM THE WORKLINK
					sendEmailToProfile(getUserService().getUserProfile(emailCommentModel.getAssignedUser()), emailCommentModel);
				} else if (canEmailGroup) {
					// EMAIL THE GROUP BUT DON'T EMAIL THE PERSON WHO MADE THE COMMENT
					for (UserRole uRole : userRoles) {
						sendEmailToProfile(getUserService().getUserProfile(uRole.getUsername()), emailCommentModel);
					}
				}
				if (canEmailOwner) {
					// EMAIL THE OWNER
					sendEmailToProfile(getUserService().getUserProfile(emailCommentModel.getEntryOwner()), emailCommentModel);
				}
			}
		}
	}
}
