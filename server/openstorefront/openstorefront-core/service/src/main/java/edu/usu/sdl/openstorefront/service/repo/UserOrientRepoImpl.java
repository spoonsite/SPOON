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
package edu.usu.sdl.openstorefront.service.repo;

import com.orientechnologies.orient.core.record.impl.ODocument;
import edu.usu.sdl.openstorefront.core.api.repo.UserRepo;
import edu.usu.sdl.openstorefront.core.entity.TrackEventCode;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserSecurity;
import edu.usu.sdl.openstorefront.core.entity.UserTracking;
import edu.usu.sdl.openstorefront.core.view.UserFilterParams;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class UserOrientRepoImpl
		extends BaseOrientRepo
		implements UserRepo
{

	@Override
	public List<UserProfile> getUserProfilesBaseOnSearch(Set<String> usernames, UserFilterParams queryParams)
	{
		String query = "select from " + UserProfile.class.getSimpleName()
				+ " where username in :usernameList ";

		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("usernameList", usernames);

		if (StringUtils.isNotBlank(queryParams.getSearchField())
				&& StringUtils.isNotBlank(queryParams.getSearchValue())
				&& !UserSecurity.FIELD_USERNAME.equals(queryParams.getSearchField())) {
			query += " and " + queryParams.getSearchField() + ".toLowerCase() like :searchValue";
			parameterMap.put("searchValue", queryParams.getSearchValue().toLowerCase() + "%");
		}

		return service.getPersistenceService().query(query, parameterMap);
	}

	@Override
	public List<UserProfile> findUsersFromEmails(List<String> userNameOrEmails)
	{
		StringBuilder query = new StringBuilder();
		query.append("select from ").append(UserProfile.class.getSimpleName()).append(" where email IS NOT NULL AND username IN :userList OR email IN :userList2");
		Map<String, Object> params = new HashMap<>();
		params.put("userList", userNameOrEmails);
		params.put("userList2", userNameOrEmails);
		List<UserProfile> usersToSend = service.getPersistenceService().query(query.toString(), params);
		return usersToSend;
	}

	@Override
	public Map<String, Date> getLastLoginFromTracking(List<UserProfile> userProfiles)
	{
		Map<String, Date> userLoginMap = new HashMap<>();

		if (userProfiles.isEmpty() == false) {
			StringBuilder query = new StringBuilder();
			query.append("select MAX(eventDts), createUser from ").append(UserTracking.class.getSimpleName());
			query.append(" where activeStatus = :userTrackingActiveStatusParam  and  trackEventTypeCode = :trackEventCodeParam and createUser IN :userListParam");

			List<String> usernames = new ArrayList<>();
			userProfiles.stream().forEach((userProfile) -> {
				usernames.add(userProfile.getUsername());
			});
			query.append(" group by createUser");

			if (usernames.isEmpty() == false) {
				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("userTrackingActiveStatusParam", UserTracking.ACTIVE_STATUS);
				paramMap.put("trackEventCodeParam", TrackEventCode.LOGIN);
				paramMap.put("userListParam", usernames);
				List<ODocument> documents = service.getPersistenceService().query(query.toString(), paramMap);
				documents.stream().forEach((document) -> {
					userLoginMap.put(document.field("createUser"), document.field("MAX"));
				});
			}
		}
		return userLoginMap;
	}

}
