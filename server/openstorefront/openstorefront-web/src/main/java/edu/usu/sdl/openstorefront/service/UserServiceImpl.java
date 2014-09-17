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
import edu.usu.sdl.openstorefront.service.api.UserService;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import edu.usu.sdl.openstorefront.storage.model.TestEntity;
import edu.usu.sdl.openstorefront.storage.model.UserProfile;
import edu.usu.sdl.openstorefront.storage.model.UserTracking;
import edu.usu.sdl.openstorefront.storage.model.UserTypeCode;
import edu.usu.sdl.openstorefront.storage.model.UserWatch;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

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
		return persistenceService.queryByOneExample(UserWatch.class, new QueryByExample(temp));
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
		try {
			UserProfile profile = persistenceService.findById(UserProfile.class, userId);
			if (profile == null) {
				/*TODO: Here we need to create a new profile if it doesn't exist...*/
				profile = new UserProfile();
				profile.setActiveStatus(TestEntity.ACTIVE_STATUS);
				profile.setCreateDts(new Date());
				profile.setCreateUser(userId);
				profile.setUpdateDts(new Date());
				profile.setUpdateUser(userId);
				profile.setUserTypeCode(UserTypeCode.END_USER);
				profile.setUsername(userId);
				return persistenceService.persist(profile);
			}
			return profile;
		} catch (OpenStorefrontRuntimeException ex) {
			throw new OpenStorefrontRuntimeException("There was an error getting the user profile");
		}
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
		UserProfile temp = persistenceService.findById(UserProfile.class, user.getUsername());
		if (temp != null) {
			temp.setActiveStatus(user.getActiveStatus());
			temp.setEmail(user.getEmail());
			temp.setFirstName(user.getFirstName());
			temp.setLastName(user.getLastName());
			temp.setOrganization(user.getOrganization());
			temp.setUserTypeCode(user.getUserTypeCode());
			temp.setUpdateUser(SecurityUtil.getCurrentUserName());
			return persistenceService.persist(temp);
		} else {
			user.setCreateDts(TimeUtil.currentDate());
			user.setUpdateDts(TimeUtil.currentDate());
			user.setCreateUser(SecurityUtil.getCurrentUserName());
			user.setUpdateUser(SecurityUtil.getCurrentUserName());
			return persistenceService.persist(user);
		}
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
			oldTracking.setMobileDevice(tracking.getMobileDevice());
			oldTracking.setOsPlatform(tracking.getOsPlatform());
			oldTracking.setScreenHeight(tracking.getScreenHeight());
			oldTracking.setScreenWidth(tracking.getScreenWidth());
			oldTracking.setTrackEventTypeCode(tracking.getTrackEventTypeCode());
			oldTracking.setUserAgent(tracking.getUserAgent());
			return persistenceService.persist(oldTracking);
		}
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
}
