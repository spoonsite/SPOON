/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.repo.api;

import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.view.UserFilterParams;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author dshurtleff
 */
public interface UserRepo
{

	/**
	 * Searches User profiles
	 *
	 * @param usernames
	 * @param queryParams
	 * @return profiles found
	 */
	public List<UserProfile> getUserProfilesBaseOnSearch(Set<String> usernames, UserFilterParams queryParams);

	/**
	 * Find profiles matching username or email address
	 *
	 * @param userNameOrEmails (Username or email address)
	 * @return
	 */
	public List<UserProfile> findUsersFromEmails(List<String> userNameOrEmails);

	/**
	 * Pulls the last login from the tracking record Warning tracking record are
	 * only kept for limit type period
	 *
	 * @param userProfiles
	 * @return
	 */
	public Map<String, Date> getLastLoginFromTracking(List<UserProfile> userProfiles);

}
