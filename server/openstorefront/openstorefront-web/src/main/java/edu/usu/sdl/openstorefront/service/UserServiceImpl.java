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

import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.security.UserContext;
import edu.usu.sdl.openstorefront.service.api.UserService;
import edu.usu.sdl.openstorefront.service.manager.UserAgentManager;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import edu.usu.sdl.openstorefront.storage.model.TrackEventCode;
import edu.usu.sdl.openstorefront.storage.model.UserProfile;
import edu.usu.sdl.openstorefront.storage.model.UserTracking;
import edu.usu.sdl.openstorefront.storage.model.UserTypeCode;
import edu.usu.sdl.openstorefront.storage.model.UserWatch;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import net.sf.uadetector.ReadableUserAgent;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;

/**
 * Handles all user business logic
 *
 * @author dshurtleff
 */
public class UserServiceImpl
		extends ServiceProxy
		implements UserService
{

	private static final Logger log = Logger.getLogger(UserServiceImpl.class.getName());

	private static final int MAX_NAME_CHECK = 100;

	@Override
	public <T extends BaseEntity> List<T> getBaseEntity(Class<T> subComponentClass, String userId)
	{
		return getBaseEntity(subComponentClass, userId, false);
	}

	@Override
	public <T extends BaseEntity> List<T> getBaseEntity(Class<T> subComponentClass, String userId, boolean all)
	{
		try {
			T baseComponentExample = subComponentClass.newInstance();
			baseComponentExample.setUpdateUser(userId);
			if (all == false) {
				baseComponentExample.setActiveStatus(BaseEntity.ACTIVE_STATUS);
			}
			return persistenceService.queryByExample(subComponentClass, new QueryByExample(baseComponentExample));
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
	}

	@Override
	public <T extends BaseEntity> List<T> getBaseEntityByCreateUser(Class<T> subComponentClass, String userId)
	{
		return getBaseEntity(subComponentClass, userId, false);
	}

	@Override
	public <T extends BaseEntity> List<T> getBaseEntityByCreateUser(Class<T> subComponentClass, String userId, boolean all)
	{
		try {
			T baseComponentExample = subComponentClass.newInstance();
//			baseComponentExample.setCreateUser(userId);
			if (all == false) {
				baseComponentExample.setActiveStatus(BaseEntity.ACTIVE_STATUS);
			}
			return persistenceService.queryByExample(subComponentClass, new QueryByExample(baseComponentExample));
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
	}

	@Override
	public <T extends BaseEntity> T deactivateBaseEntity(Class<T> subComponentClass, Object pk)
	{
		return deactivateBaseEntity(subComponentClass, pk, false);
	}

	@Override
	public <T extends BaseEntity> T deactivateBaseEntity(Class<T> subComponentClass, Object pk, Boolean all)
	{
		T found = persistenceService.findById(subComponentClass, pk);
		if (found != null) {
			found.setActiveStatus(T.INACTIVE_STATUS);
			persistenceService.persist(found);
		}
		return found;
	}

	@Override
	public List<UserWatch> getWatches(String userId)
	{
		UserWatch temp = new UserWatch();
		temp.setUsername(userId);
		temp.setActiveStatus(UserWatch.ACTIVE_STATUS);
		return persistenceService.queryByExample(UserWatch.class, new QueryByExample(temp));
	}

	@Override
	public UserWatch getWatch(String watchId)
	{
		UserWatch temp = new UserWatch();
		temp.setUserWatchId(watchId);
		return persistenceService.queryOneByExample(UserWatch.class, new QueryByExample(temp));
	}

	/**
	 *
	 * @param watch
	 * @return
	 */
	@Override
	public UserWatch saveWatch(UserWatch watch)
	{
		UserWatch oldWatch = persistenceService.findById(UserWatch.class, watch.getUserWatchId());
		if (oldWatch != null) {
			oldWatch.setActiveStatus(watch.getActiveStatus());
			oldWatch.setLastViewDts(watch.getLastViewDts());
			oldWatch.setNotifyFlg(watch.getNotifyFlg());
			oldWatch.setUpdateUser(watch.getUpdateUser());
			return persistenceService.persist(oldWatch);
		}
		watch.setUserWatchId(persistenceService.generateId());
		watch.setCreateDts(TimeUtil.currentDate());
		watch.setUpdateDts(TimeUtil.currentDate());
		watch.setLastViewDts(TimeUtil.currentDate());
		return persistenceService.persist(watch);
	}

	@Override
	public Boolean deleteWatch(String watchId)
	{
		UserWatch temp = persistenceService.findById(UserWatch.class, watchId);
		persistenceService.delete(temp);
		return Boolean.TRUE;
	}

	@Override
	public UserProfile getUserProfile(String userId)
	{
		UserProfile profile = persistenceService.findById(UserProfile.class, userId);
		return profile;
	}

	@Override
	public List<UserProfile> getAllProfiles()
	{
		UserProfile example = new UserProfile();
		example.setActiveStatus(UserProfile.ACTIVE_STATUS);
		return persistenceService.queryByExample(UserProfile.class, new QueryByExample(example));
	}

	@Override
	public UserProfile saveUserProfile(UserProfile user)
	{
		return saveUserProfile(user, true);
	}

	public UserProfile saveUserProfile(UserProfile user, boolean refreshSession)
	{
		UserProfile userProfile = persistenceService.findById(UserProfile.class, user.getUsername());
		if (userProfile != null) {
			userProfile.setActiveStatus(UserProfile.ACTIVE_STATUS);
			userProfile.setEmail(user.getEmail());
			userProfile.setFirstName(user.getFirstName());
			userProfile.setLastName(user.getLastName());
			userProfile.setOrganization(user.getOrganization());
			userProfile.setUserTypeCode(user.getUserTypeCode());
			userProfile.setUpdateUser(SecurityUtil.getCurrentUserName());
			if (StringUtils.isNotBlank(userProfile.getInternalGuid())) {
				userProfile.setInternalGuid(persistenceService.generateId());
			}
			persistenceService.persist(userProfile);
		} else {
			user.setActiveStatus(UserProfile.ACTIVE_STATUS);
			user.setInternalGuid(persistenceService.generateId());
			user.setCreateDts(TimeUtil.currentDate());
			user.setUpdateDts(TimeUtil.currentDate());
			user.setCreateUser(SecurityUtil.getCurrentUserName());
			user.setUpdateUser(SecurityUtil.getCurrentUserName());
			userProfile = persistenceService.persist(user);
		}

		userProfile = persistenceService.deattachAll(userProfile);
		if (refreshSession) {
			UserContext userContext = SecurityUtil.getUserContext();
			if (userContext != null) {
				if (userContext.getUserProfile().getUsername().equals(userProfile.getUsername())) {
					userContext.setUserProfile(userProfile);
					SecurityUtils.getSubject().getSession().setAttribute(SecurityUtil.USER_CONTEXT_KEY, userContext);
				}
			}
		}
		return userProfile;
	}

	@Override
	public Boolean deleteProfile(String userId)
	{
		UserProfile profile = persistenceService.findById(UserProfile.class, userId);
		if (profile != null) {
			profile.setActiveStatus(UserProfile.INACTIVE_STATUS);
			if (persistenceService.persist(profile) != null) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	public UserTracking saveUserTracking(UserTracking tracking)
	{
		UserTracking oldTracking = persistenceService.findById(UserTracking.class, tracking.getTrackingId());
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
			oldTracking.setUserAgent(tracking.getUserAgent());
			return persistenceService.persist(oldTracking);
		}
		tracking.setActiveStatus(UserTracking.ACTIVE_STATUS);
		tracking.setCreateDts(TimeUtil.currentDate());
		tracking.setUpdateDts(TimeUtil.currentDate());
		tracking.setTrackingId(persistenceService.generateId());
		return persistenceService.persist(tracking);
	}

//  This will be fleshed out more later
//	@Override
//	public List<Component> getRecentlyViewed(String userId)
//	{
//
//		//CONTINUE HERE... left off after work on friday.
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//	}
//
	@Override
	public UserContext handleLogin(UserProfile userprofile, HttpServletRequest request, Boolean admin)
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

			//check for an existing profile
			UserProfile profile = persistenceService.findById(UserProfile.class, userprofile.getUsername());
			if (profile == null) {
				profile = userprofile;
				saveUserProfile(profile, false);
			} else {
				//Check user id
				boolean conflictUsername = false;
				if (StringUtils.isNotBlank(profile.getExternalGuid())
						|| StringUtils.isNotBlank(userprofile.getExternalGuid())) {
					if (profile.getExternalGuid() != null
							&& profile.getExternalGuid().equals(userprofile.getExternalGuid()) == false) {
						conflictUsername = true;
					} else if (userprofile.getExternalGuid() != null
							&& userprofile.getExternalGuid().equals(profile.getExternalGuid()) == false) {
						conflictUsername = true;
					}
				}

				if (conflictUsername) {
					//create a new user name and then save the profile  (We may get multiple "john.doe" so this handles that situation)
					boolean unique = false;
					int idIndex = 1;
					do {
						String userName = userprofile.getUsername() + "_" + (idIndex++);
						UserProfile profileCheck = persistenceService.findById(UserProfile.class, userName);
						if (profileCheck == null) {
							profile = userprofile;
							profile.setExternalUserId(userprofile.getUsername());
							profile.setUsername(userName);
							saveUserProfile(profile, false);
							unique = true;
						}
					} while (!unique && idIndex < MAX_NAME_CHECK);

					if (profile == null) {
						throw new OpenStorefrontRuntimeException("Failed to create a unique username.", "Check username and make sure it's unique.");
					}
				}
			}

			profile = persistenceService.deattachAll(profile);
			userContext.setUserProfile(profile);
			if (admin != null) {
				userContext.setAdmin(admin);
			} else {
				userContext.setAdmin(SecurityUtil.isAdminUser());
			}
			SecurityUtils.getSubject().getSession().setAttribute(SecurityUtil.USER_CONTEXT_KEY, userContext);

			//Add tracking if it's a client request
			if (request != null) {
				UserTracking userTracking = new UserTracking();
				userTracking.setTrackEventTypeCode(TrackEventCode.LOGIN);
				userTracking.setEventDts(TimeUtil.currentDate());
				userTracking.setUpdateUser(profile.getUsername());
				userTracking.setCreateUser(profile.getUsername());

				String userAgent = request.getHeader(OpenStorefrontConstant.HEADER_USER_AGENT);
				ReadableUserAgent readableUserAgent = UserAgentManager.parse(userAgent);
				if (readableUserAgent != null) {
					userTracking.setBrowser(readableUserAgent.getName());
					userTracking.setBrowserVersion(readableUserAgent.getVersionNumber().toVersionString());
					userTracking.setDeviceType(readableUserAgent.getTypeName());
					userTracking.setOsPlatform(readableUserAgent.getOperatingSystem().getName() + "  version: " + readableUserAgent.getOperatingSystem().getVersionNumber().toVersionString());
				}
				userTracking.setClientIp(request.getRemoteAddr());
				userTracking.setUserAgent(userAgent);

				saveUserTracking(userTracking);
			}

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
		persistenceService.deleteByExample(userWatchExample);
	}

}
