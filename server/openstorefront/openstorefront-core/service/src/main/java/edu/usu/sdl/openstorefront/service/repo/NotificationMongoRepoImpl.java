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
import com.mongodb.client.model.Filters;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;
import edu.usu.sdl.openstorefront.core.entity.NotificationEventReadStatus;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import static edu.usu.sdl.openstorefront.service.repo.MongoQueryUtil.MONGO_SORT_ASCENDING;
import static edu.usu.sdl.openstorefront.service.repo.MongoQueryUtil.MONGO_SORT_DESCENDING;
import edu.usu.sdl.openstorefront.service.repo.api.NotificationRepo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.bson.conversions.Bson;

/**
 *
 * @author dshurtleff
 */
public class NotificationMongoRepoImpl
		extends BaseMongoRepo
		implements NotificationRepo
{

	@Override
	public long getTotalNotificationsForUser(String username)
	{
		long totalCount;

		MongoCollection<NotificationEvent> collection = getQueryUtil().getCollectionForEntity(NotificationEvent.class);

		Bson filter = Filters.eq(NotificationEvent.FIELD_ACTIVE_STATUS, NotificationEvent.ACTIVE_STATUS);
		if (username != null) {
			filter = Filters.and(
					filter,
					Filters.or(
							Filters.eq(NotificationEvent.FIELD_USERNAME, username),
							Filters.and(
									Filters.eq(NotificationEvent.FIELD_USERNAME, null),
									Filters.eq(NotificationEvent.FIELD_ROLEGROUP, null)
							)
					)
			);
		}
		totalCount = collection.countDocuments(filter);

		return totalCount;
	}

	@Override
	public List<NotificationEvent> getNotificationsForUser(String username, FilterQueryParams queryParams)
	{
		MongoCollection<NotificationEvent> collection = getQueryUtil().getCollectionForEntity(NotificationEvent.class);

		Bson filter = Filters.eq(NotificationEvent.FIELD_ACTIVE_STATUS, NotificationEvent.ACTIVE_STATUS);
		if (username != null) {
			filter = Filters.and(
					filter,
					Filters.or(
							Filters.eq(NotificationEvent.FIELD_USERNAME, username),
							Filters.and(
									Filters.eq(NotificationEvent.FIELD_USERNAME, null),
									Filters.eq(NotificationEvent.FIELD_ROLEGROUP, null)
							)
					)
			);
		}

		BasicDBObject sort = new BasicDBObject();
		int sortDirection = MONGO_SORT_ASCENDING;
		if (OpenStorefrontConstant.SORT_DESCENDING.equals(queryParams.getSortOrder())) {
			sortDirection = MONGO_SORT_DESCENDING;
		}
		sort.append(queryParams.getSortField(), sortDirection);

		FindIterable<NotificationEvent> findIterable = collection.find(filter)
				.sort(sort)
				.skip(queryParams.getOffset())
				.limit(queryParams.getMax());

		return findIterable.into(new ArrayList<>());
	}

	@Override
	public List<NotificationEventReadStatus> getReadNoficationsForUser(String username, Set<String> eventIds)
	{
		MongoCollection<NotificationEventReadStatus> collection = getQueryUtil().getCollectionForEntity(NotificationEventReadStatus.class);

		Bson filter = Filters.and(
				Filters.eq(NotificationEventReadStatus.FIELD_USERNAME, username),
				Filters.in(NotificationEventReadStatus.FIELD_EVENTID, eventIds)
		);
		FindIterable<NotificationEventReadStatus> findIterable = collection.find(filter);
		return findIterable.into(new ArrayList<>());
	}

	@Override
	public void deleteNotificationBeforeDate(Date archiveDts)
	{
		MongoCollection<NotificationEvent> collection = getQueryUtil().getCollectionForEntity(NotificationEvent.class);
		collection.deleteMany(Filters.lt(NotificationEvent.FIELD_UPDATE_DTS, archiveDts));
	}

}
