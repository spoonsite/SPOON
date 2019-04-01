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

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import edu.usu.sdl.openstorefront.core.entity.StandardEntity;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.service.repo.api.StandardEntityRepo;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.bson.conversions.Bson;

/**
 *
 * @author dshurtleff
 */
public class StandardEntityMongoRepoImpl
		extends BaseMongoRepo
		implements StandardEntityRepo
{

	@Override
	public long getRecordCountsByUsers(Class recordClass, List<UserProfile> userProfiles, String trackCodeType)
	{
		long count = 0;

		List<String> userIds = new ArrayList<>();
		for (UserProfile userProfile : userProfiles) {
			userIds.add(userProfile.getUsername());
		}

		if (userIds.isEmpty() == false) {

			MongoCollection collection = getQueryUtil().getCollectionForEntity(recordClass);

			Bson filter = Filters.in(StandardEntity.FIELD_CREATE_USER, userIds);

			if (StringUtils.isNotBlank(trackCodeType)) {
				filter = Filters.and(filter, Filters.eq(trackCodeType));
			}
			count = collection.countDocuments(filter);
		}

		return count;

	}

}
