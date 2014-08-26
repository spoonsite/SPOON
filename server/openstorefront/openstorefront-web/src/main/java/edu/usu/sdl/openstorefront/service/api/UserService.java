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

import edu.usu.sdl.openstorefront.storage.model.Component;
import edu.usu.sdl.openstorefront.storage.model.UserProfile;
import edu.usu.sdl.openstorefront.storage.model.UserWatch;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public interface UserService
{

	/**
	 * Return the list of watches tied to a userID
	 *
	 * @param userId
	 * @return
	 */
	public List<UserWatch> getWatches(String userId);

	/**
	 *
	 * @param watch
	 * @return
	 */
	public UserWatch addWatch(UserWatch watch);

	/**
	 * Update a watch in the user's watch list
	 *
	 * @param watch
	 * @return
	 */
	public UserWatch updateWatch(UserWatch watch);

	/**
	 * Delete a watch from the user's watch list
	 *
	 * @param watchId
	 * @return
	 */
	public Boolean deleteWatch(String watchId);

	/**
	 * Get the user profile based on the userID
	 *
	 * @param userId
	 * @return
	 */
	public UserProfile getUserProfile(String userId);

	/**
	 * Save any changes to the user profile
	 *
	 * @param user
	 * @return
	 */
	public UserProfile saveUserProfile(UserProfile user);

	/**
	 * Get the most recently viewed components list for a user
	 *
	 * @param userId
	 * @return
	 */
	public List<Component> getRecentlyViewed(String userId);

}
