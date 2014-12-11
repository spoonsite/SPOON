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
import edu.usu.sdl.openstorefront.service.transfermodel.AdminMessage;
import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.UserMessage;
import edu.usu.sdl.openstorefront.storage.model.UserProfile;
import edu.usu.sdl.openstorefront.storage.model.UserTracking;
import edu.usu.sdl.openstorefront.storage.model.UserWatch;
import edu.usu.sdl.openstorefront.web.rest.model.FilterQueryParams;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dshurtleff
 */
public interface UserService
		extends AsyncService
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
	@ServiceInterceptor(TransactionInterceptor.class)
	public UserWatch saveWatch(UserWatch watch);

	/**
	 * Delete a watch from the user's watch list
	 *
	 * @param watchId
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
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
	 * Save any changes to the user profile (This will refresh the session) This
	 * is the prefered call
	 *
	 * @param user
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public UserProfile saveUserProfile(UserProfile user);

	/**
	 *
	 * @param user
	 * @param refreshSession
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public UserProfile saveUserProfile(UserProfile user, boolean refreshSession);

	/**
	 * Deletes the user profile (Inactive)
	 *
	 * @param username
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteProfile(String username);

	/**
	 * Reactivate a profile and restore user data
	 *
	 * @param username
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void reactiveProfile(String username);

	/**
	 *
	 * @param tracking
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
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

	/**
	 * This will send a test email to the address on the user profile.
	 *
	 * @param username
	 */
	public void sendTestEmail(String username);

	/**
	 * Pulls active watches for the component and create messages for 'notfiy'
	 * watches.
	 *
	 * @param component
	 */
	public void checkComponentWatches(Component component);

	/**
	 * Queue messaged will be delayed thus allowing for duplicate handling and
	 * reduce spam.
	 *
	 * @param userMessage
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void queueUserMessage(UserMessage userMessage);

	/**
	 * Finds the messages based on filter
	 *
	 * @param filter
	 * @return
	 */
	public List<UserMessage> findUserMessages(FilterQueryParams filter);

	/**
	 * This will inactivate a user message
	 *
	 * @param userMessageId
	 */
	public void removeUserMessage(String userMessageId);

	/**
	 * This will remove old user messages
	 */
	public void cleanupOldUserMessages();

	/**
	 * Sends AdminMessages to all uses of the storefront with emails
	 *
	 * @param adminMessage
	 */
	public void sendAdminMessage(AdminMessage adminMessage);

	/**
	 * This handles processing all user messages. Cleanup old message, sending
	 * out queued messages.
	 *
	 * @param sendNow set to true to force send the messages immediately
	 */
	public void processAllUserMessages(boolean sendNow);

	/**
	 * Sends an email to all user bases on what has changed since the date.
	 * Components, Articles, Hightlights
	 *
	 * @param lastRunDts
	 */
	public void sendRecentChangeEmail(Date lastRunDts);

	/**
	 * Sends an email to all user bases on what has changed since the date.
	 * Components, Articles, Hightlights
	 *
	 * @param lastRunDts
	 * @param emailAddress (Only sends to this email if set)
	 */
	public void sendRecentChangeEmail(Date lastRunDts, String emailAddress);

//  This will be fleshed out more later
//	/**
//	 * Get the most recently viewed components list for a user
//	 *
//	 * @param userId
//	 * @return
//	 */
//	public List<Component> getRecentlyViewed(String userId);
}
