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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.UserMessage;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserSavedSearch;
import edu.usu.sdl.openstorefront.core.entity.UserTracking;
import edu.usu.sdl.openstorefront.core.entity.UserWatch;
import edu.usu.sdl.openstorefront.core.model.AdminMessage;
import edu.usu.sdl.openstorefront.core.model.Dashboard;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.core.view.UserTrackingResult;
import edu.usu.sdl.openstorefront.security.UserContext;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dshurtleff
 */
public interface UserService
		extends AsyncService
{

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
	 * Queries user tracking records
	 *
	 * @param filter
	 * @param userId
	 * @return
	 */
	public UserTrackingResult getUserTracking(FilterQueryParams filter, String userId);

	/**
	 * Save a user watch
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
	 * This will get cache set of lookup(code, description) values This pulls
	 * active only
	 *
	 * @return
	 */
	public List<LookupModel> getProfilesForLookup();

	/**
	 * Get the last login for a group of users.
	 *
	 * @param userProfiles
	 * @return username, last know login (if user is missing they don't have a
	 * last login)
	 */
	public Map<String, Date> getLastLogin(List<UserProfile> userProfiles);

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
	 * Saves user profile and optionally refreshes the user session
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
	 * @param allowSync allow Sync if external management policy is activated
	 * @return
	 */
	public UserContext handleLogin(UserProfile userprofile, HttpServletRequest request, Boolean allowSync);

	/**
	 * This will send a test email to the address on the user profile. or to
	 * override email if set.
	 *
	 * @param username (required)
	 * @param overrideEmail (optional)
	 */
	public void sendTestEmail(String username, String overrideEmail);

	/**
	 * This will return email address on user profile.
	 *
	 * @param username
	 */
	public String getEmailFromUserProfile(String username);

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

	/**
	 * Performs the sync task with the user profiles with an external user
	 * manager
	 *
	 * @param userManager
	 */
	public void syncUserProfilesWithUserManagement(ExternalUserManager userManager);

	/**
	 * Saves a user Search
	 *
	 * @param userSavedSearch
	 * @return the saved search record
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public UserSavedSearch saveUserSearch(UserSavedSearch userSavedSearch);

	/**
	 * Removes saved search
	 *
	 * @param userSearchId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteUserSearch(String userSearchId);

	/**
	 * Get a dashboard for a user. If the user doesn't have a dashboard it
	 * creates one.
	 *
	 * @param username
	 * @return
	 */
	public Dashboard getDashboard(String username);

	/**
	 * Saves a dashboard and all it of widgets. Note: this clear the old widgets
	 * replaces with the new widgets.
	 *
	 * @param dashboard
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public Dashboard saveDashboard(Dashboard dashboard);

}
