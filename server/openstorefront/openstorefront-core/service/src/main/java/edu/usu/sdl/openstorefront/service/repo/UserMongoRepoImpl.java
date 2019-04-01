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

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import edu.usu.sdl.openstorefront.core.entity.OrganizationModel;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.core.entity.UserSecurity;
import edu.usu.sdl.openstorefront.core.view.UserFilterParams;
import edu.usu.sdl.openstorefront.service.repo.api.UserRepo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
//			query += " and " + queryParams.getSearchField() + ".toLowerCase() like :searchValue";
//			parameterMap.put("searchValue", queryParams.getSearchValue().toLowerCase() + "%");
		}

		FindIterable<UserProfile> findIterable = collection.find(Filters.eq(OrganizationModel.FIELD_ORGANIZATION, null));

		return findIterable.into(new ArrayList<>());
	}

	@Override
	public List<UserProfile> findUsersFromEmails(List<String> userNameOrEmails)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Map<String, Date> getLastLoginFromTracking(List<UserProfile> userProfiles)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
