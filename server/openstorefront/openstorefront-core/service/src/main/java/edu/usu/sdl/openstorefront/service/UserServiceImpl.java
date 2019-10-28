/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.common.util.NetworkUtil;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.api.ExternalUserManager;
import edu.usu.sdl.openstorefront.core.api.UserService;
import edu.usu.sdl.openstorefront.core.api.query.GenerateStatementOption;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.api.query.QueryType;
import edu.usu.sdl.openstorefront.core.api.query.SpecialOperatorModel;
import edu.usu.sdl.openstorefront.core.entity.Alert;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.Branding;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.DashboardWidget;
import edu.usu.sdl.openstorefront.core.entity.Highlight;
import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;
import edu.usu.sdl.openstorefront.core.entity.NotificationEventType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPolicy;
import edu.usu.sdl.openstorefront.core.entity.TrackEventCode;
import edu.usu.sdl.openstorefront.core.entity.UserDashboard;
import edu.usu.sdl.openstorefront.core.entity.UserMessage;
import edu.usu.sdl.openstorefront.core.entity.UserMessageType;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserSavedSearch;
import edu.usu.sdl.openstorefront.core.entity.UserTracking;
import edu.usu.sdl.openstorefront.core.entity.UserTypeCode;
import edu.usu.sdl.openstorefront.core.entity.UserWatch;
import edu.usu.sdl.openstorefront.core.filter.FilterEngine;
import edu.usu.sdl.openstorefront.core.model.AdminMessage;
import edu.usu.sdl.openstorefront.core.model.Dashboard;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.util.EntityUtil;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.core.view.UserTrackingResult;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.security.UserContext;
import edu.usu.sdl.openstorefront.security.UserRecord;
import edu.usu.sdl.openstorefront.service.api.UserServicePrivate;
import edu.usu.sdl.openstorefront.service.manager.MailManager;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.service.manager.UserAgentManager;
import edu.usu.sdl.openstorefront.service.message.ApprovalMessageGenerator;
import edu.usu.sdl.openstorefront.service.message.BaseMessageGenerator;
import edu.usu.sdl.openstorefront.service.message.ChangeRequestApprovedMessageGenerator;
import edu.usu.sdl.openstorefront.service.message.ChangeRequestMessageGenerator;
import edu.usu.sdl.openstorefront.service.message.ComponentSubmissionMessageGenerator;
import edu.usu.sdl.openstorefront.service.message.ComponentWatchMessageGenerator;
import edu.usu.sdl.openstorefront.service.message.MessageContext;
import edu.usu.sdl.openstorefront.service.message.RecentChangeMessage;
import edu.usu.sdl.openstorefront.service.message.RecentChangeMessageGenerator;
import edu.usu.sdl.openstorefront.service.message.SystemErrorAlertMessageGenerator;
import edu.usu.sdl.openstorefront.service.message.TestMessageGenerator;
import edu.usu.sdl.openstorefront.service.message.UserDataAlertMessageGenerator;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.servlet.http.HttpServletRequest;
import org.ehcache.Element;
import net.sf.uadetector.ReadableUserAgent;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.codemonkey.simplejavamail.MailException;
import org.codemonkey.simplejavamail.email.Email;

/**
 * Handles all user business logic
 *
 * @author dshurtleff
 */
public class UserServiceImpl
		extends ServiceProxy
		implements UserService, UserServicePrivate
{

	private static final Logger LOG = Logger.getLogger(UserServiceImpl.class.getName());
	private static final String ALL_ACTIVE_USERS_CACHE_KEY = "ALL_ACTIVE_USERS";

	@Override
	public List<UserWatch> getWatches(String userId)
	{
		UserWatch temp = new UserWatch();
		temp.setUsername(userId);
		temp.setActiveStatus(UserWatch.ACTIVE_STATUS);
		return getPersistenceService().queryByExample(new QueryByExample<>(temp));
	}

	@Override
	public UserWatch getWatch(String watchId)
	{
		UserWatch temp = new UserWatch();
		temp.setUserWatchId(watchId);
		return getPersistenceService().queryOneByExample(new QueryByExample<>(temp));
	}

	/**
	 *
	 * @param watch
	 * @return
	 */
	@Override
	public UserWatch saveWatch(UserWatch watch)
	{
		UserWatch oldWatch = getPersistenceService().findById(UserWatch.class, watch.getUserWatchId());
		if (oldWatch != null) {
			oldWatch.setActiveStatus(watch.getActiveStatus());
			oldWatch.setLastViewDts(watch.getLastViewDts());
			oldWatch.setNotifyFlg(watch.getNotifyFlg());
			oldWatch.setUpdateUser(watch.getUpdateUser());
			return getPersistenceService().persist(oldWatch);
		}
		watch.setUserWatchId(getPersistenceService().generateId());
		watch.setCreateDts(TimeUtil.currentDate());
		watch.setUpdateDts(TimeUtil.currentDate());
		watch.setLastViewDts(TimeUtil.currentDate());
		return getPersistenceService().persist(watch);
	}

	@Override
	public Boolean deleteWatch(String watchId)
	{
		UserWatch temp = getPersistenceService().findById(UserWatch.class, watchId);
		getPersistenceService().delete(temp);
		return Boolean.TRUE;
	}

	@Override
	public UserProfile getUserProfile(String userId)
	{
		UserProfile profile = getPersistenceService().findById(UserProfile.class, userId);
		return profile;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LookupModel> getProfilesForLookup()
	{
		List<LookupModel> profiles = new ArrayList<>();

		Element element = OSFCacheManager.getApplicationCache().get(ALL_ACTIVE_USERS_CACHE_KEY);
		if (element == null) {
			// get all active user profiles
			UserProfile example = new UserProfile();
			example.setActiveStatus(UserProfile.ACTIVE_STATUS);
			List<UserProfile> userProfiles = example.findByExample();

			for (UserProfile userProfile : userProfiles) {
				LookupModel lookupModel = new LookupModel();
				lookupModel.setCode(userProfile.getUsername());
				String name = userProfile.getUsername();
				if (StringUtils.isNotBlank(userProfile.getFirstName())) {
					name = userProfile.getFirstName() + ", " + userProfile.getLastName();
				}
				String email = StringUtils.isNotBlank(userProfile.getEmail()) ? " (" + userProfile.getEmail() + ")" : "";
				lookupModel.setDescription(name + email);
				profiles.add(lookupModel);
			}

			element = new Element(ALL_ACTIVE_USERS_CACHE_KEY, profiles);
			OSFCacheManager.getApplicationCache().put(element);
		} else {
			profiles = (List<LookupModel>) element.getObjectValue();
		}
		//return copy (so the list can be modified; however the elements are still shared...just FYI)
		return new ArrayList<>(profiles);
	}

	private void clearCache()
	{
		OSFCacheManager.getApplicationCache().remove(ALL_ACTIVE_USERS_CACHE_KEY);
	}

	@Override
	public UserProfile saveUserProfile(UserProfile user)
	{
		return saveUserProfile(user, true);
	}

	@Override
	public UserProfile saveUserProfile(UserProfile user, boolean refreshSession)
	{
		UserProfile userProfile = getPersistenceService().findById(UserProfile.class, user.getUsername());
		if (userProfile != null) {
			if (StringUtils.isBlank(user.getActiveStatus())) {
				userProfile.setActiveStatus(UserProfile.ACTIVE_STATUS);
			} else {
				userProfile.setActiveStatus(user.getActiveStatus());
			}
			userProfile.setEmail(user.getEmail());
			userProfile.setPhone(user.getPhone());
			userProfile.setPositionTitle(user.getPositionTitle());
			userProfile.setFirstName(user.getFirstName());
			userProfile.setLastName(user.getLastName());
			userProfile.setNotifyOfNew(user.getNotifyOfNew());
			userProfile.setOrganization(user.getOrganization());
			userProfile.setUserTypeCode(user.getUserTypeCode());
			if (StringUtils.isNotBlank(userProfile.getInternalGuid())) {
				userProfile.setInternalGuid(getPersistenceService().generateId());
			}
			userProfile.populateBaseUpdateFields();
			getPersistenceService().persist(userProfile);
		} else {
			user.setInternalGuid(getPersistenceService().generateId());
			user.populateBaseCreateFields();
			userProfile = getPersistenceService().persist(user);
		}

		userProfile = getPersistenceService().deattachAll(userProfile);
		getOrganizationService().addOrganization(userProfile.getOrganization());
		if (refreshSession) {
			UserContext userContext = SecurityUtil.getUserContext();
			if (userContext != null) {
				if (userContext.getUserProfile().getUsername().equals(userProfile.getUsername())) {
					userContext.setUserProfile(userProfile);
					SecurityUtils.getSubject().getSession().setAttribute(SecurityUtil.USER_CONTEXT_KEY, userContext);
				}
			}
		}
		clearCache();

		return userProfile;
	}

	@Override
	public void deleteProfile(String username)
	{
		UserProfile profile = getPersistenceService().findById(UserProfile.class, username);
		if (profile != null) {

			//Check for duplicate (Should be rare)
			//if their is dups; delete all but the last updated
			UserProfile dupCheck = new UserProfile();
			dupCheck.setUsername(username);
			List<UserProfile> dupUsers = dupCheck.findByExampleProxy();
			if (dupUsers.size() > 1) {
				Date maxUpdateDate = null;
				for (UserProfile userProfile : dupUsers) {
					if (maxUpdateDate == null
							|| maxUpdateDate.before(userProfile.getUpdateDts())) {
						maxUpdateDate = userProfile.getUpdateDts();
					}
				}
				for (UserProfile userProfile : dupUsers) {
					if (userProfile.getUpdateDts().before(maxUpdateDate)) {
						//Because of the duplicate username we can only delete the user safely
						getPersistenceService().delete(userProfile);
					} else {
						profile = userProfile;
					}
				}
			}

			profile.setActiveStatus(UserProfile.INACTIVE_STATUS);
			profile.setUpdateDts(TimeUtil.currentDate());
			profile.setUpdateUser(SecurityUtil.getCurrentUserName());
			if (StringUtils.isBlank(profile.getInternalGuid())) {
				//old profiles add missing info
				profile.setInternalGuid((getPersistenceService().generateId()));
			}
			getPersistenceService().persist(profile);

			UserWatch userwatchExample = new UserWatch();
			userwatchExample.setUsername(username);

			UserWatch userwatchSetExample = new UserWatch();
			userwatchSetExample.setActiveStatus(UserWatch.INACTIVE_STATUS);
			userwatchSetExample.setUpdateDts(TimeUtil.currentDate());
			userwatchSetExample.setUpdateUser(SecurityUtil.getCurrentUserName());

			getPersistenceService().updateByExample(UserWatch.class, userwatchSetExample, userwatchExample);

			UserMessage userMessageExample = new UserMessage();
			userMessageExample.setUsername(username);

			UserMessage userMessageSetExample = new UserMessage();
			userMessageSetExample.setActiveStatus(UserWatch.INACTIVE_STATUS);
			userMessageSetExample.setUpdateDts(TimeUtil.currentDate());
			userMessageSetExample.setUpdateUser(SecurityUtil.getCurrentUserName());
			getPersistenceService().updateByExample(UserMessage.class, userMessageSetExample, userMessageExample);

			getReportService().disableAllScheduledReportsForUser(username);
			getComponentServicePrivate().removeUserFromComponentType(username);

			clearCache();
		}
	}

	@Override
	public void reactiveProfile(String username)
	{
		UserProfile profile = getPersistenceService().findById(UserProfile.class, username);
		if (profile != null) {
			profile.setActiveStatus(UserProfile.ACTIVE_STATUS);
			profile.setUpdateDts(TimeUtil.currentDate());
			profile.setUpdateUser(SecurityUtil.getCurrentUserName());
			getPersistenceService().persist(profile);

			UserWatch userwatchExample = new UserWatch();
			userwatchExample.setUsername(username);

			UserWatch userwatchSetExample = new UserWatch();
			userwatchSetExample.setActiveStatus(UserWatch.ACTIVE_STATUS);
			userwatchSetExample.setUpdateDts(TimeUtil.currentDate());
			userwatchSetExample.setUpdateUser(SecurityUtil.getCurrentUserName());

			//Inactive Schedules reports for the user will stay inactive; user should activate the ones they want.
			getPersistenceService().updateByExample(UserWatch.class, userwatchSetExample, userwatchExample);

			clearCache();
		} else {
			throw new OpenStorefrontRuntimeException("Unable to reactivate profile.  Userprofile not found: " + username, "Check userprofiles and username");
		}
	}

	@Override
	public UserTracking saveUserTracking(UserTracking tracking)
	{
		UserTracking oldTracking = getPersistenceService().findById(UserTracking.class, tracking.getTrackingId());
		if (oldTracking != null) {
			oldTracking.setActiveStatus(tracking.getActiveStatus());
			oldTracking.setBrowser(tracking.getBrowser());
			oldTracking.setBrowserVersion(tracking.getBrowserVersion());
			oldTracking.setClientIp(tracking.getClientIp());
			oldTracking.setUpdateDts(TimeUtil.currentDate());
			oldTracking.setUpdateUser(tracking.getUpdateUser());
			oldTracking.setEventDts(tracking.getEventDts());
			oldTracking.setDeviceType(tracking.getDeviceType());
			oldTracking.setOsPlatform(tracking.getOsPlatform());
			oldTracking.setScreenHeight(tracking.getScreenHeight());
			oldTracking.setScreenWidth(tracking.getScreenWidth());
			oldTracking.setTrackEventTypeCode(tracking.getTrackEventTypeCode());
			oldTracking.setOrganization(tracking.getOrganization());
			oldTracking.setUserTypeCode(tracking.getUserTypeCode());
			oldTracking.setUserAgent(tracking.getUserAgent());
			return getPersistenceService().persist(oldTracking);
		}
		tracking.setActiveStatus(UserTracking.ACTIVE_STATUS);
		tracking.setCreateDts(TimeUtil.currentDate());
		tracking.setUpdateDts(TimeUtil.currentDate());
		tracking.setTrackingId(getPersistenceService().generateId());
		return getPersistenceService().persist(tracking);
	}

	@Override
	public UserContext handleLogin(UserProfile userprofile, HttpServletRequest request, Boolean allowSync)
	{
		Objects.requireNonNull(userprofile, "User Profile is required");
		Objects.requireNonNull(userprofile.getUsername(), "User Profile -> username is required");

		UserContext userContext = new UserContext();

		if (StringUtils.isBlank(userprofile.getUserTypeCode())) {
			userprofile.setUserTypeCode(UserTypeCode.END_USER);
		}

		//validate
		ValidationModel validationModelCode = new ValidationModel(userprofile);
		validationModelCode.setConsumeFieldsOnly(true);
		ValidationResult validationResult = ValidationUtil.validate(validationModelCode);
		if (validationResult.valid()) {

			//check for an existing profile (username must be unique whether internally or externally)
			UserProfile profile = getPersistenceService().findById(UserProfile.class, userprofile.getUsername());
			if (profile == null) {
				//new user
				profile = userprofile;
				saveUserProfile(profile, false);
			}

			//Activative profile on login
			if (UserProfile.INACTIVE_STATUS.equals(profile.getActiveStatus())) {
				LOG.log(Level.INFO, MessageFormat.format("User: {0} profile was INACTIVE reactivating it on login.", profile.getUsername()));
				getUserService().reactiveProfile(userprofile.getUsername());
			}

			// Modify the 'last login date' if it's a client request
			if (request != null) {
				profile.setLastLoginDts(TimeUtil.currentDate());
			}

			if (Convert.toBoolean(allowSync)) {
				SecurityPolicy securityPolicy = getSecurityService().getSecurityPolicy();
				if (Convert.toBoolean(securityPolicy.getDisableUserInfoEdit())) {

					profile.setFirstName(userprofile.getFirstName());
					profile.setLastName(userprofile.getLastName());

					//External may or may not provide this; don't want wipe out existing
					//Currently the Header Realm has support to send it but Open AM is not configured for it
					//Daily sync can pull it so we don't want to wipe that out
					if (StringUtils.isNotBlank(userprofile.getOrganization())) {
						profile.setOrganization(userprofile.getOrganization());
					}
					profile.setEmail(userprofile.getEmail());
					profile.setPhone(userprofile.getPhone());

					//In the same situation as Organization
					if (StringUtils.isNotBlank(userprofile.getExternalGuid())) {
						profile.setExternalGuid(userprofile.getExternalGuid());
					}
					saveUserProfile(profile, false);
				}
			}

			profile = getPersistenceService().deattachAll(profile);
			userContext = getSecurityService().getUserContext(profile.getUsername());
			SecurityUtils.getSubject().getSession().setAttribute(SecurityUtil.USER_CONTEXT_KEY, userContext);

			//Add tracking if it's a client request
			if (request != null) {
				UserTracking userTracking = new UserTracking();
				userTracking.setTrackEventTypeCode(TrackEventCode.LOGIN);
				userTracking.setEventDts(TimeUtil.currentDate());
				userTracking.setUpdateUser(profile.getUsername());
				userTracking.setCreateUser(profile.getUsername());
				userTracking.setOrganization(profile.getOrganization());
				userTracking.setUserTypeCode(profile.getUserTypeCode());

				String userAgent = request.getHeader(OpenStorefrontConstant.HEADER_USER_AGENT);
				ReadableUserAgent readableUserAgent = UserAgentManager.parse(userAgent);
				if (readableUserAgent != null) {
					userTracking.setBrowser(readableUserAgent.getName());
					userTracking.setBrowserVersion(readableUserAgent.getVersionNumber().toVersionString());
					userTracking.setDeviceType(readableUserAgent.getTypeName());
					userTracking.setOsPlatform(readableUserAgent.getOperatingSystem().getName() + "  version: " + readableUserAgent.getOperatingSystem().getVersionNumber().toVersionString());
				}
				userTracking.setClientIp(NetworkUtil.getClientIp(request));
				userTracking.setUserAgent(userAgent);

				saveUserTracking(userTracking);
			} else {
				LOG.log(Level.INFO, MessageFormat.format("Login handled for user: {0} (Not an external client request...not tracking", profile.getUsername()));
			}
			String adminLog = "";
			if (userContext.isAdmin()) {
				adminLog = "(Admin)";
			}

			LOG.log(Level.INFO, MessageFormat.format("User {0} sucessfully logged in. {1}", profile.getUsername(), adminLog));

		} else {
			throw new OpenStorefrontRuntimeException("Failed to validate the userprofile. Validation Message: " + validationResult.toString(), "Check data");
		}

		return userContext;
	}

	@Override
	public void removeAllWatchesForComponent(String componentId)
	{
		UserWatch userWatchExample = new UserWatch();
		userWatchExample.setComponentId(componentId);
		getPersistenceService().deleteByExample(userWatchExample);
	}

	@Override
	public void sendTestEmail(String username, String overrideEmail)
	{
		UserProfile userProfile = getUserProfile(username);
		if (userProfile != null) {
			if (StringUtils.isNotBlank(overrideEmail) || StringUtils.isNotBlank(userProfile.getEmail())) {
				if (StringUtils.isNotBlank(overrideEmail)) {
					userProfile.setEmail(overrideEmail);
				}
				TestMessageGenerator testMessageGenerator = new TestMessageGenerator(new MessageContext(userProfile));
				Email email = testMessageGenerator.generateMessage();
				MailManager.send(email, true);
				LOG.log(Level.INFO, MessageFormat.format("Sent test email to: {0}", userProfile.getEmail()));
			} else {
				throw new OpenStorefrontRuntimeException("User is missing email address.", "Add a valid email address.");
			}
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find user.", "Check username.");
		}
	}

	@Override
	public String getEmailFromUserProfile(String username)
	{
		UserProfile userProfile = getUserProfile(username);
		if(userProfile != null){
			return userProfile.getEmail();
		}
		return null;
	}

	@Override
	public void checkComponentWatches(Component component)
	{
		UserWatch userWatchExample = new UserWatch();
		userWatchExample.setActiveStatus(UserMessage.ACTIVE_STATUS);
		userWatchExample.setComponentId(component.getComponentId());

		List<UserWatch> userWatches = getPersistenceService().queryByExample(userWatchExample);
		for (UserWatch userWatch : userWatches) {
			if (component.getLastActivityDts().after(userWatch.getLastViewDts())) {

				//make sure the user can still access the component
				Component accessComponent = FilterEngine.getInstance().filter(component);
				if (accessComponent != null) {
					if (Convert.toBoolean(userWatch.getNotifyFlg())) {
						UserMessage userMessage = new UserMessage();
						userMessage.setUsername(userWatch.getUsername());
						userMessage.setComponentId(component.getComponentId());
						userMessage.setUserMessageType(UserMessageType.COMPONENT_WATCH);
						userMessage.setCreateUser(OpenStorefrontConstant.SYSTEM_USER);
						userMessage.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
						getUserService().queueUserMessage(userMessage);
					}

					NotificationEvent notificationEvent = new NotificationEvent();
					notificationEvent.setEventType(NotificationEventType.WATCH);
					notificationEvent.setUsername(userWatch.getUsername());
					notificationEvent.setMessage("Component: " + component.getName() + " has been updated.");
					notificationEvent.setEntityName(Component.class.getSimpleName());
					notificationEvent.setEntityId(component.getComponentId());
					getNotificationService().postEvent(notificationEvent);
				} else {
					LOG.log(Level.FINE, MessageFormat.format("User can not access component. (User Watch) No message sent.  Component: {0}", component.getName()));
				}
			}
		}
	}

	@Override
	public void queueUserMessage(UserMessage userMessage)
	{
		UserMessage userMessageExample = new UserMessage();
		userMessageExample.setActiveStatus(UserMessage.ACTIVE_STATUS);
		userMessageExample.setUsername(userMessage.getUsername());
		userMessageExample.setComponentId(userMessage.getComponentId());
		userMessageExample.setUserMessageType(userMessage.getUserMessageType());
		userMessageExample.setAlertId(userMessage.getAlertId());
		userMessageExample.setEmailAddress(userMessage.getEmailAddress());

		//Duplicate check;
		UserMessage userMessageExisting = getPersistenceService().queryOneByExample(userMessageExample);
		if (userMessageExisting == null) {
			userMessage.setUserMessageId(getPersistenceService().generateId());
			userMessage.setRetryCount(0);
			userMessage.populateBaseCreateFields();
			getPersistenceService().persist(userMessage);
		}
	}

	@Override
	public void cleanupOldUserMessages()
	{
		int maxDays = Convert.toInteger(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MESSAGE_KEEP_DAYS, "30"));

		LocalDateTime archiveTime = LocalDateTime.now();
		archiveTime = archiveTime.minusDays(maxDays);
		archiveTime = archiveTime.truncatedTo(ChronoUnit.DAYS);

		ZonedDateTime zdt = archiveTime.atZone(ZoneId.systemDefault());
		Date archiveDts = Date.from(zdt.toInstant());

		UserMessage userMessageExample = new UserMessage();
		userMessageExample.setActiveStatus(UserMessage.INACTIVE_STATUS);

		UserMessage userMessageMaxExample = new UserMessage();
		userMessageMaxExample.setUpdateDts(archiveDts);

		QueryByExample<UserMessage> query = new QueryByExample<>(userMessageExample);

		SpecialOperatorModel<UserMessage> specialOperatorModel = new SpecialOperatorModel<>();
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN);
		specialOperatorModel.setExample(userMessageMaxExample);
		query.getExtraWhereCauses().add(specialOperatorModel);

		getPersistenceService().deleteByExample(query);

	}

	@Override
	public void sendAdminMessage(AdminMessage adminMessage)
	{
		Branding branding = getBrandingService().getCurrentBrandingView();
		String appTitle = branding.getApplicationName();

		UserProfile userProfileExample = new UserProfile();
		userProfileExample.setActiveStatus(UserProfile.ACTIVE_STATUS);

		List<UserProfile> usersToSend = new ArrayList<>();

		if (StringUtils.isNotBlank(adminMessage.getUserTypeCode())) {
			LOG.log(Level.INFO, MessageFormat.format("(Admin Message) Sending email to users of type: {0}", adminMessage.getUserTypeCode()));
			userProfileExample.setUserTypeCode(adminMessage.getUserTypeCode());
			List<UserProfile> userProfiles = getPersistenceService().queryByExample(userProfileExample);
			for (UserProfile userProfile : userProfiles) {
				if (StringUtils.isNotBlank(userProfile.getEmail())) {
					usersToSend.add(userProfile);
				}
			}
		} else if (adminMessage.getUsersToEmail().isEmpty() == false) {
			LOG.log(Level.INFO, "(Admin Message) Sending email to specfic users");
			List<String> emailList = new ArrayList<>();
			for (String email : adminMessage.getUsersToEmail()) {
				if (StringUtils.isNotBlank(email)) {
					emailList.add(email.trim());
				}
			}

			usersToSend = getRepoFactory().getUserRepo().findUsersFromEmails(emailList);
			for (String email : emailList) {
				Boolean found = false;
				for (UserProfile user : usersToSend) {
					if (StringUtils.equalsIgnoreCase(user.getEmail(), email)
							|| StringUtils.equalsIgnoreCase(user.getUsername(), email)) {
						found = true;
					}
				}
				if (!found && StringProcessor.isEmail(email)) {
					UserProfile temp = new UserProfile();
					temp.setEmail(email);
					temp.setFirstName("");
					temp.setLastName("");
					usersToSend.add(temp);
				}
			}
		}

		//filter out duplicate email addresses
		Set<String> emailAddressSet = new HashSet<>();
		for (int i = usersToSend.size() - 1; i >= 0; i--) {
			UserProfile userProfile = usersToSend.get(i);
			if (emailAddressSet.contains(userProfile.getEmail())) {
				usersToSend.remove(i);
			} else {
				emailAddressSet.add(userProfile.getEmail());
			}
		}

		int emailCount = 0;
		Email email = MailManager.newEmail();
		email.setSubject(appTitle + " - " + adminMessage.getSubject());
		email.setTextHTML(adminMessage.getMessage());

		for (UserProfile userProfile : usersToSend) {
			String name = userProfile.getFirstName() + " " + userProfile.getLastName();
			email.addRecipient(name, userProfile.getEmail(), Message.RecipientType.TO);
			emailCount++;
		}
		for (String emailAddress : adminMessage.getCcEmails()) {
			email.addRecipient("", emailAddress, Message.RecipientType.CC);
			emailCount++;
		}
		for (String emailAddress : adminMessage.getBccEmails()) {
			email.addRecipient("", emailAddress, Message.RecipientType.BCC);
			emailCount++;
		}
		MailManager.send(email);
		LOG.log(Level.INFO, MessageFormat.format("(Admin Message) {0} email(s) sent (in one message)", emailCount));
	}

	@Override
	public void processAllUserMessages(boolean sendNow)
	{
		cleanupOldUserMessages();

		UserMessage userMessageExample = new UserMessage();
		userMessageExample.setActiveStatus(UserMessage.ACTIVE_STATUS);

		List<UserMessage> userMessages = getPersistenceService().queryByExample(userMessageExample);
		int minQueueMinutes = Convert.toInteger(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MESSAGE_MIN_QUEUE_MINUTES, "10"));
		int maxRetries = Convert.toInteger(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MESSAGE_MAX_RETRIES, "5"));
		if (minQueueMinutes < 0) {
			minQueueMinutes = 0;
		}
		long queueMills = System.currentTimeMillis() - TimeUtil.minutesToMillis(minQueueMinutes);

		//remove dups
		Map<String, UserMessage> messageMap = new HashMap<>();
		for (UserMessage userMessage : userMessages) {
			if (messageMap.containsKey(userMessage.uniqueKey())) {
				LOG.log(Level.FINE, MessageFormat.format("Removing duplicate user message: ", userMessage.uniqueKey()));
			} else {
				messageMap.put(userMessage.uniqueKey(), userMessage);
			}
		}

		for (UserMessage userMessage : messageMap.values()) {

			if (sendNow || userMessage.getCreateDts().getTime() <= queueMills) {

				boolean updateUserMessage = false;
				UserMessage userMessageExisting = getPersistenceService().findById(UserMessage.class, userMessage.getUserMessageId());
				if (userMessage.getRetryCount() < maxRetries) {
					try {
						getUserServicePrivate().sendUserMessage(userMessage);
					} catch (MailException mailException) {
						LOG.log(Level.FINE, "Unable to send message.", mailException);
						userMessageExisting.setBodyOfMessage("Unable to send message to user.  Mail Server down? " + mailException.getMessage());
						userMessageExisting.setRetryCount(userMessage.getRetryCount() + 1);
						updateUserMessage = true;
					} catch (Exception e) {
						LOG.log(Level.SEVERE, "Unexpected error.  Failed sending message. (Halt sending of " + userMessage.getUserMessageId() + " message.)", e);
						userMessageExisting.setActiveStatus(UserMessage.INACTIVE_STATUS);
						userMessageExisting.setBodyOfMessage("System exception occured while sending message. See logs for details");
						updateUserMessage = true;
					}
				} else {
					userMessageExisting.setActiveStatus(UserMessage.INACTIVE_STATUS);
					userMessageExisting.setBodyOfMessage("Exceed max reties.  Inactivated  message.");
					updateUserMessage = true;
				}

				if (updateUserMessage) {

					userMessageExisting.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
					userMessageExisting.setUpdateDts(TimeUtil.currentDate());
					getPersistenceService().persist(userMessageExisting);
				}

			} else {
				LOG.log(Level.FINEST, MessageFormat.format("Not time yet to send email to user: {0}", userMessage.getUsername()));
			}

		}

	}

	@Override
	public void sendUserMessage(UserMessage userMessage)
	{
		UserProfile userProfile = getUserProfile(userMessage.getUsername());
		UserMessage userMessageExisting = getPersistenceService().findById(UserMessage.class, userMessage.getUserMessageId());

		String emailAddress = userMessage.getEmailAddress();
		if (StringUtils.isBlank(emailAddress)) {
			if (userProfile != null) {
				emailAddress = userProfile.getEmail();
			}
		}

		if (StringUtils.isNotBlank(emailAddress)) {
			MessageContext messageContext = new MessageContext(userProfile);
			messageContext.setUserMessage(userMessage);
			if (StringUtils.isNotBlank(userMessage.getAlertId())) {
				messageContext.setAlert(getPersistenceService().findById(Alert.class, userMessage.getAlertId()));
			}

			BaseMessageGenerator generator = null;
			if (null != userMessage.getUserMessageType()) {
				switch (userMessage.getUserMessageType()) {
					case UserMessageType.COMPONENT_WATCH:
						generator = new ComponentWatchMessageGenerator(messageContext);
						break;
					case UserMessageType.USER_DATA_ALERT:
						generator = new UserDataAlertMessageGenerator(messageContext);
						break;
					case UserMessageType.SYSTEM_ERROR_ALERT:
						generator = new SystemErrorAlertMessageGenerator(messageContext);
						break;
					case UserMessageType.COMPONENT_SUBMISSION_ALERT:
						generator = new ComponentSubmissionMessageGenerator(messageContext);
						break;
					case UserMessageType.APPROVAL_NOTIFICATION:
						generator = new ApprovalMessageGenerator(messageContext);
						break;
					case UserMessageType.CHANGE_REQUEST_APPROVAL_NOTIFICATION:
						generator = new ChangeRequestApprovedMessageGenerator(messageContext);
						break;
					case UserMessageType.CHANGE_REQUEST_ALERT:
						generator = new ChangeRequestMessageGenerator(messageContext);
						break;

				}
			}

			if (generator == null) {
				throw new UnsupportedOperationException("Message type not supported.  Type: " + userMessage.getUserMessageType());
			}

			Email email = generator.generateMessage();
			if (email != null) {
				MailManager.send(email);
				userMessageExisting.setSubject(email.getSubject());
				userMessageExisting.setBodyOfMessage(email.getTextHTML());
				userMessageExisting.setSentEmailAddress(emailAddress);
			} else {
				userMessageExisting.setBodyOfMessage("Message was empty no email sent.");
			}
		} else {
			userMessageExisting.setBodyOfMessage("No email address set for user");
		}

		userMessageExisting.setActiveStatus(UserMessage.INACTIVE_STATUS);
		userMessageExisting.setUpdateDts(TimeUtil.currentDate());
		userMessageExisting.setUpdateUser(OpenStorefrontConstant.SYSTEM_USER);
		getPersistenceService().persist(userMessageExisting);
	}

	@Override
	public void sendRecentChangeEmail(Date lastRunDts)
	{
		sendRecentChangeEmail(lastRunDts, null);
	}

	@Override
	public void sendRecentChangeEmail(Date lastRunDts, String emailAddress)
	{
		Objects.requireNonNull(lastRunDts, "Last Run Dts is required");

		UserProfile userProfileExample = new UserProfile();
		userProfileExample.setActiveStatus(UserProfile.ACTIVE_STATUS);
		userProfileExample.setNotifyOfNew(Boolean.TRUE);

		List<UserProfile> userProfiles = getPersistenceService().queryByExample(userProfileExample);
		RecentChangeMessage recentChangeMessage = new RecentChangeMessage();
		recentChangeMessage.setLastRunDts(lastRunDts);

		Component componentExample = new Component();
		componentExample.setActiveStatus(Component.ACTIVE_STATUS);
		QueryByExample<Component> componentQuery = new QueryByExample<>(componentExample);

		Component componentLastActivityExample = new Component();
		componentLastActivityExample.setLastActivityDts(lastRunDts);

		SpecialOperatorModel<Component> specialOperatorModel = new SpecialOperatorModel<>();
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		specialOperatorModel.setExample(componentLastActivityExample);
		componentQuery.getExtraWhereCauses().add(specialOperatorModel);

		List<Component> components = getPersistenceService().queryByExample(componentExample);
		for (Component component : components) {
			if (ApprovalStatus.APPROVED.equals(component.getApprovalState())) {
				if (component.getApprovedDts() != null
						&& component.getApprovedDts().after(lastRunDts)) {
					recentChangeMessage.getComponentsAdded().add(component);
				} else {
					recentChangeMessage.getComponentsUpdated().add(component);
				}
			}
		}

		Highlight highlightExample = new Highlight();
		highlightExample.setActiveStatus(Highlight.ACTIVE_STATUS);
		QueryByExample<Highlight> highlightQuery = new QueryByExample<>(highlightExample);

		Highlight highlightUpdateExample = new Highlight();
		highlightUpdateExample.setUpdateDts(lastRunDts);

		SpecialOperatorModel<Highlight> specialOperatorModelHighlight = new SpecialOperatorModel<>();
		specialOperatorModelHighlight.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		specialOperatorModelHighlight.setExample(highlightUpdateExample);
		highlightQuery.getExtraWhereCauses().add(specialOperatorModelHighlight);

		List<Highlight> highLights = getPersistenceService().queryByExample(highlightQuery);
		for (Highlight highLight : highLights) {
			if (highLight.getCreateDts().after(lastRunDts)) {
				recentChangeMessage.getHighlightsAdded().add(highLight);
			} else {
				recentChangeMessage.getHighlightsUpdated().add(highLight);
			}
		}

		if (StringUtils.isNotBlank(emailAddress)) {
			MessageContext messageContext = new MessageContext(null);
			messageContext.setRecentChangeMessage(recentChangeMessage);

			RecentChangeMessageGenerator messageGenerator = new RecentChangeMessageGenerator(messageContext);
			Email email = messageGenerator.generateMessage();
			if (email != null) {
				email.addRecipient("", emailAddress, Message.RecipientType.TO);
				MailManager.send(email);
			}

		} else {
			for (UserProfile userProfile : userProfiles) {
				if (StringUtils.isNotBlank(userProfile.getEmail())) {
					MessageContext messageContext = new MessageContext(userProfile);
					messageContext.setRecentChangeMessage(recentChangeMessage);

					RecentChangeMessageGenerator messageGenerator = new RecentChangeMessageGenerator(messageContext);
					Email email = messageGenerator.generateMessage();
					if (email != null) {
						MailManager.send(email);
					}
				}
			}
		}

	}

	@Override
	public void removeUserMessage(String userMessageId)
	{
		UserMessage userMessage = getPersistenceService().findById(UserMessage.class, userMessageId);
		if (userMessage != null) {
			getPersistenceService().delete(userMessage);
		}
	}

	@Override
	public Map<String, Date> getLastLogin(List<UserProfile> userProfiles)
	{
		Map<String, Date> userLoginMap = getRepoFactory().getUserRepo().getLastLoginFromTracking(userProfiles);
		return userLoginMap;
	}

	@Override
	public UserTrackingResult getUserTracking(FilterQueryParams filter, String userId)
	{
		UserTrackingResult result = new UserTrackingResult();

		UserTracking userTrackingExample = new UserTracking();
		userTrackingExample.setActiveStatus(filter.getStatus());
		userTrackingExample.setCreateUser(userId);

		UserTracking userTrackingStartExample = new UserTracking();
		userTrackingStartExample.setEventDts(filter.getStart());

		UserTracking userTrackingEndExample = new UserTracking();
		userTrackingEndExample.setEventDts(filter.getEnd());

		UserTracking userTrackingNameExample = new UserTracking();
		userTrackingNameExample.setUpdateUser("%" + filter.getName().toLowerCase().trim() + "%");    // Force SQL Wildcards Into Parameter

		QueryByExample<UserTracking> queryByExample = new QueryByExample<>(userTrackingExample);

		SpecialOperatorModel<UserTracking> specialOperatorModel = new SpecialOperatorModel<>();
		specialOperatorModel.setExample(userTrackingStartExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_GREATER_THAN);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel<>();
		specialOperatorModel.setExample(userTrackingEndExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LESS_THAN_EQUAL);
		specialOperatorModel.getGenerateStatementOption().setParameterSuffix(GenerateStatementOption.PARAMETER_SUFFIX_END_RANGE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		specialOperatorModel = new SpecialOperatorModel<>();
		specialOperatorModel.setExample(userTrackingNameExample);
		specialOperatorModel.getGenerateStatementOption().setOperation(GenerateStatementOption.OPERATION_LIKE);
		specialOperatorModel.getGenerateStatementOption().setMethod(GenerateStatementOption.METHOD_LOWER_CASE);
		queryByExample.getExtraWhereCauses().add(specialOperatorModel);

		queryByExample.setMaxResults(filter.getMax());
		queryByExample.setFirstResult(filter.getOffset());
		queryByExample.setSortDirection(filter.getSortOrder());

		UserTracking userTrackingOrderExample = new UserTracking();
		Field sortField = ReflectionUtil.getField(userTrackingOrderExample, filter.getSortField());
		if (sortField != null) {
			try {
				BeanUtils.setProperty(userTrackingOrderExample, sortField.getName(), QueryByExample.getFlagForType(sortField.getType()));
			} catch (IllegalAccessException | InvocationTargetException ex) {
				LOG.log(Level.WARNING, "Unable set sort field", ex);
			}
			queryByExample.setOrderBy(userTrackingOrderExample);
		}

		result.setResult(getPersistenceService().queryByExample(queryByExample));
		queryByExample.setQueryType(QueryType.COUNT);
		result.setCount(getPersistenceService().countByExample(queryByExample));

		return result;
	}

	@Override
	public void syncUserProfilesWithUserManagement(ExternalUserManager userManager)
	{
		UserProfile userProfileExample = new UserProfile();
		userProfileExample.setActiveStatus(UserProfile.ACTIVE_STATUS);

		//page through users
		long pageSize = 200;
		long maxRecords = getPersistenceService().countByExample(userProfileExample);
		for (long i = 0; i < maxRecords; i = i + pageSize) {
			QueryByExample<UserProfile> queryByExample = new QueryByExample<>(userProfileExample);
			queryByExample.setFirstResult((int) i);
			queryByExample.setMaxResults((int) pageSize);
			queryByExample.setReturnNonProxied(false);

			List<UserProfile> userProfiles = getPersistenceService().queryByExample(queryByExample);
			List<String> usernames = new ArrayList<>();
			for (UserProfile userProfile : userProfiles) {
				usernames.add(userProfile.getUsername());
			}
			List<UserRecord> userRecords = userManager.findUsers(usernames);
			Map<String, UserRecord> activeUserMap = new HashMap<>();
			for (UserRecord userRecord : userRecords) {
				activeUserMap.put(userRecord.getUsername(), userRecord);
			}
			for (UserProfile userProfile : userProfiles) {
				if (activeUserMap.containsKey(userProfile.getUsername()) == false) {
					LOG.log(Level.INFO, "User not found in external user management, Inactivating user. (Sync Service)");
					deleteProfile(userProfile.getUsername());
				} else {
					//check for syncing; if the user can't edit they should be syncing
					SecurityPolicy securityPolicy = getSecurityService().getSecurityPolicy();
					if (Convert.toBoolean(securityPolicy.getDisableUserInfoEdit())) {
						UserRecord userRecord = activeUserMap.get(userProfile.getUsername());

						//check to see if needs syncing
						boolean sync = false;

						UserRecord currentRecord = new UserRecord();
						currentRecord.setFirstName(userProfile.getFirstName());
						currentRecord.setLastName(userProfile.getLastName());
						currentRecord.setOrganization(userProfile.getOrganization());
						currentRecord.setEmail(userProfile.getEmail());
						currentRecord.setPhone(userProfile.getPhone());
						currentRecord.setGuid(userProfile.getExternalGuid());
						if (EntityUtil.isObjectsDifferent(userRecord, currentRecord, false)) {
							sync = true;
						}

						if (sync) {
							userProfile.setFirstName(userRecord.getFirstName());
							userProfile.setLastName(userRecord.getLastName());
							userProfile.setOrganization(userRecord.getOrganization());
							userProfile.setEmail(userRecord.getEmail());
							userProfile.setPhone(userRecord.getPhone());
							userProfile.setExternalGuid(userRecord.getGuid());
							saveUserProfile(userProfile, false);

							LOG.log(Level.FINEST, MessageFormat.format("Sync user profile for user: {0}", userProfile.getUsername()));
						}
					}
				}
			}
		}
	}

	@Override
	public UserSavedSearch saveUserSearch(UserSavedSearch userSavedSearch)
	{
		UserSavedSearch existing = getPersistenceService().findById(UserSavedSearch.class, userSavedSearch.getUserSearchId());
		if (existing != null) {
			existing.updateFields(userSavedSearch);
			existing = getPersistenceService().persist(existing);
		} else {
			userSavedSearch.setUserSearchId(getPersistenceService().generateId());
			userSavedSearch.populateBaseCreateFields();
			existing = getPersistenceService().persist(userSavedSearch);
		}

		return existing;
	}

	@Override
	public void deleteUserSearch(String userSearchId)
	{
		UserSavedSearch existing = getPersistenceService().findById(UserSavedSearch.class, userSearchId);
		if (existing != null) {
			getPersistenceService().delete(existing);
		}
	}

	@Override
	public Dashboard getDashboard(String username)
	{
		Dashboard dashboard = new Dashboard();

		UserDashboard userDashboard = new UserDashboard();
		userDashboard.setUsername(username);
		userDashboard = userDashboard.find();

		if (userDashboard == null) {
			userDashboard = new UserDashboard();
			userDashboard.setDashboardId(getPersistenceService().generateId());
			userDashboard.setUsername(username);
			userDashboard.setName(UserDashboard.DEFAULT_NAME);
			userDashboard.populateBaseCreateFields();
			userDashboard = getPersistenceService().persist(userDashboard);
		} else {
			DashboardWidget widget = new DashboardWidget();
			widget.setDashboardId(userDashboard.getDashboardId());
			dashboard.setWidgets(widget.findByExample());
			dashboard.getWidgets().sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, DashboardWidget.FIELD_WIDGET_ORDER));
		}
		dashboard.setDashboard(userDashboard);

		return dashboard;
	}

	@Override
	public Dashboard saveDashboard(Dashboard dashboard)
	{
		Objects.requireNonNull(dashboard);
		Objects.requireNonNull(dashboard.getDashboard());

		UserDashboard userDashboard = getPersistenceService().findById(UserDashboard.class, dashboard.getDashboard().getDashboardId());
		if (userDashboard != null) {
			userDashboard.updateFields(dashboard.getDashboard());
			userDashboard = getPersistenceService().persist(userDashboard);
		} else {
			dashboard.getDashboard().setDashboardId(getPersistenceService().generateId());
			dashboard.getDashboard().populateBaseCreateFields();
			userDashboard = getPersistenceService().persist(dashboard.getDashboard());
		}

		//clear old widgets and replace
		DashboardWidget widgetExample = new DashboardWidget();
		widgetExample.setDashboardId(userDashboard.getDashboardId());
		getPersistenceService().deleteByExample(widgetExample);

		for (DashboardWidget widget : dashboard.getWidgets()) {
			widget.setWidgetId(getPersistenceService().generateId());
			widget.populateBaseCreateFields();
			widget.setDashboardId(userDashboard.getDashboardId());
			getPersistenceService().persist(widget);
		}

		dashboard.setDashboard(userDashboard);
		return dashboard;
	}

}
