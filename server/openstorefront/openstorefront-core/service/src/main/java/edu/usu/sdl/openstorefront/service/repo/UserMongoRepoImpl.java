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

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import edu.usu.sdl.openstorefront.core.entity.TrackEventCode;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserSecurity;
import edu.usu.sdl.openstorefront.core.entity.UserTracking;
import edu.usu.sdl.openstorefront.core.view.UserFilterParams;
import edu.usu.sdl.openstorefront.service.repo.api.UserRepo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.bson.conversions.Bson;

/**
 *
 * @author dshurtleff
 */
public class UserMongoRepoImpl
		extends BaseMongoRepo
		implements UserRepo
{

	@Override
	public List<UserProfile> getUserProfilesBaseOnSearch(Set<String> usernames, UserFilterParams queryParams)
	{
		MongoCollection<UserProfile> collection = getQueryUtil().getCollectionForEntity(UserProfile.class);

		Bson filter = Filters.in(UserProfile.FIELD_USERNAME, usernames);

		if (StringUtils.isNotBlank(queryParams.getSearchField())
				&& StringUtils.isNotBlank(queryParams.getSearchValue())
				&& !UserSecurity.FIELD_USERNAME.equals(queryParams.getSearchField())) {

			String likeValue = getQueryUtil().convertSQLLikeCharacterToRegex(queryParams.getSearchValue().toLowerCase());
			filter = Filters.and(filter, Filters.regex(queryParams.getSearchField(), Pattern.compile(likeValue, Pattern.CASE_INSENSITIVE)));
		}

		FindIterable<UserProfile> findIterable = collection.find(filter);

		return findIterable.into(new ArrayList<>());
	}

	@Override
	public List<UserProfile> findUsersFromEmails(List<String> userNameOrEmails)
	{
		MongoCollection<UserProfile> collection = getQueryUtil().getCollectionForEntity(UserProfile.class);

		Bson filter = Filters.ne(UserProfile.FIELD_EMAIL, null);

		filter = Filters.and(
				filter,
				Filters.or(
						Filters.in(UserProfile.FIELD_USERNAME, userNameOrEmails),
						Filters.in(UserProfile.FIELD_EMAIL, userNameOrEmails)
				)
		);

		FindIterable<UserProfile> findIterable = collection.find(filter);
		return findIterable.into(new ArrayList<>());
	}

	@Override
	public Map<String, Date> getLastLoginFromTracking(List<UserProfile> userProfiles)
	{
		Map<String, Date> userLoginMap = new HashMap<>();

		MongoCollection<UserTracking> collection = getQueryUtil().getCollectionForEntity(UserTracking.class);

		Set<String> userNames = userProfiles.stream()
				.map(UserProfile::getUsername)
				.collect(Collectors.toSet());

		BasicDBObject groupQuery = new BasicDBObject();

		BasicDBObject groupFields = new BasicDBObject();
		groupFields = new BasicDBObject("_id", "$" + UserTracking.FIELD_CREATE_USER);

		BasicDBObject maxProjection = new BasicDBObject();
		maxProjection.put("$max", "$" + UserTracking.FIELD_EVENTDTS);
		groupFields.put(UserTracking.FIELD_EVENTDTS, maxProjection);

		BasicDBObject fieldProjection = new BasicDBObject();
		fieldProjection.put("$first", "$" + UserTracking.FIELD_CREATE_USER);
		groupFields.put(UserTracking.FIELD_CREATE_USER, fieldProjection);
		groupQuery.put("$group", groupFields);

		List<Bson> pipeline = Arrays.asList(
				Aggregates.match(
						Filters.and(
								Filters.eq(UserTracking.FIELD_ACTIVE_STATUS, UserTracking.ACTIVE_STATUS),
								Filters.eq(UserTracking.FIELD_TRACK_EVENT_TYPE_CODE, TrackEventCode.LOGIN),
								Filters.in(UserTracking.FIELD_CREATE_USER, userNames)
						)
				),
				groupQuery
		);
		List<UserTracking> trackingFound = collection.aggregate(pipeline).into(new ArrayList<>());
		for (UserTracking tracking : trackingFound) {
			userLoginMap.put(tracking.getCreateUser(), tracking.getEventDts());
		}

		return userLoginMap;
	}

}
