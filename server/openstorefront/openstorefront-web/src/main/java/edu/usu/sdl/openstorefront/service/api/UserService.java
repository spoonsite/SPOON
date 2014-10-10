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
package edu.usu.sdl.openstorefront.service.api;

import edu.usu.sdl.openstorefront.security.UserContext;
import edu.usu.sdl.openstorefront.service.ServiceInterceptor;
import edu.usu.sdl.openstorefront.service.TransactionInterceptor;
import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import edu.usu.sdl.openstorefront.storage.model.UserProfile;
import edu.usu.sdl.openstorefront.storage.model.UserTracking;
import edu.usu.sdl.openstorefront.storage.model.UserWatch;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dshurtleff
 */
public interface UserService
{

	/**
	 *
	 * @param <T>
	 * @param subComponentClass
	 * @param userId
	 * @return
	 */
	public <T extends BaseEntity> List<T> getBaseEntity(Class<T> subComponentClass, String userId);

	/**
	 *
	 * @param <T>
	 * @param subComponentClass
	 * @param userId
	 * @param all
	 * @return
	 */
	public <T extends BaseEntity> List<T> getBaseEntity(Class<T> subComponentClass, String userId, boolean all);

	/**
	 *
	 * @param <T>
	 * @param subComponentClass
	 * @param userId
	 * @return
	 */
	public <T extends BaseEntity> List<T> getBaseEntityByCreateUser(Class<T> subComponentClass, String userId);

	/**
	 *
	 * @param <T>
	 * @param subComponentClass
	 * @param userId
	 * @param all
	 * @return
	 */
	public <T extends BaseEntity> List<T> getBaseEntityByCreateUser(Class<T> subComponentClass, String userId, boolean all);

	/**
	 *
	 * @param <T>
	 * @param subComponentClass
	 * @param pk
	 * @return
	 */
	public <T extends BaseEntity> T deactivateBaseEntity(Class<T> subComponentClass, Object pk);

	/**
	 *
	 * @param <T>
	 * @param subComponentClass
	 * @param pk
	 * @param all
	 * @return
	 */
	public <T extends BaseEntity> T deactivateBaseEntity(Class<T> subComponentClass, Object pk, Boolean all);

	/**
	 * Return the list of watches tied to a userID
	 *
	 * @param userId
	 * @return
	 */
	public List<UserWatch> getWatches(String userId);

	/**
	 * Return the list of watches tied to a userID
	 *
	 * @param watchId
	 * @return
	 */
	public UserWatch getWatch(String watchId);

	/**
	 *
	 * @param watch
	 * @return
	 */
	public UserWatch saveWatch(UserWatch watch);

	/**
	 * Delete a watch from the user's watch list
	 *
	 * @param watchId
	 * @return
	 */
	public Boolean deleteWatch(String watchId);

	/**
	 * Removes all watches for a component
	 *
	 * @param componentId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void removeAllWatchesForComponent(String componentId);

	/**
	 * Get the user profile based on the userID
	 *
	 * @param userId
	 * @return profile or null if not found
	 */
	public UserProfile getUserProfile(String userId);

	/**
	 * Get the user profile based on the userID
	 *
	 * @return
	 */
	public List<UserProfile> getAllProfiles();

	/**
	 * Save any changes to the user profile
	 *
	 * @param user
	 * @return
	 */
	public UserProfile saveUserProfile(UserProfile user);

	/**
	 * Save any changes to the user profile
	 *
	 * @param userId
	 * @return
	 */
	public Boolean deleteProfile(String userId);

	/**
	 *
	 * @param tracking
	 * @return
	 */
	public UserTracking saveUserTracking(UserTracking tracking);

	/**
	 * This is called on login to pull the user info It will check for an
	 * existing user based on the user name. If the user doesn't exist it will
	 * create a profile for them based on the information in the user profile
	 * ...filling in gaps as needed. This will also add the user context to the
	 * session.
	 *
	 * @param userprofile
	 * @param request
	 * @param admin (pass null if login in to shiro already
	 * @return
	 */
	public UserContext handleLogin(UserProfile userprofile, HttpServletRequest request, Boolean admin);

//  This will be fleshed out more later
//	/**
//	 * Get the most recently viewed components list for a user
//	 *
//	 * @param userId
//	 * @return
//	 */
//	public List<Component> getRecentlyViewed(String userId);
}
