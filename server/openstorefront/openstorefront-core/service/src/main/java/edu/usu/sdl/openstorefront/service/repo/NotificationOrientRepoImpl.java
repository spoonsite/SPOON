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
import edu.usu.sdl.openstorefront.core.api.repo.NotificationRepo;
import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;
import edu.usu.sdl.openstorefront.core.entity.NotificationEventReadStatus;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author dshurtleff
 */
public class NotificationOrientRepoImpl
		extends BaseOrientRepo
		implements NotificationRepo
{

	@Override
	public long getTotalNotificationsForUser(String username)
	{
		long totalCount = 0;

		String eventQuery = "SELECT COUNT(*) FROM " + NotificationEvent.class.getSimpleName() + " WHERE activeStatus = '" + NotificationEvent.ACTIVE_STATUS + "'";

		if (username != null) {
			eventQuery += " AND (username = '" + username + "' OR (username IS NULL AND roleGroup IS NULL))";
		}
		List<ODocument> countDocuments = service.getPersistenceService().query(eventQuery, null);
		if (!countDocuments.isEmpty()) {
			totalCount = countDocuments.get(0).field("COUNT");
		}

		return totalCount;
	}

	@Override
	public List<NotificationEvent> getNotificationsForUser(String username, FilterQueryParams queryParams)
	{
		String eventQuery = "SELECT FROM " + NotificationEvent.class.getSimpleName() + " WHERE activeStatus = '" + NotificationEvent.ACTIVE_STATUS + "'";

		if (username != null) {
			eventQuery += " AND (username = '" + username + "' OR (username IS NULL AND roleGroup IS NULL))";
		}
		eventQuery += " ORDER BY " + queryParams.getSortField() + " " + queryParams.getSortOrder();
		eventQuery += " SKIP " + queryParams.getOffset();
		eventQuery += " LIMIT " + queryParams.getMax();
		List<NotificationEvent> notificationEvents = service.getPersistenceService().query(eventQuery, null);
		return notificationEvents;
	}

	@Override
	public List<NotificationEventReadStatus> getReadNoficationsForUser(String username, Set<String> eventIds)
	{
		String query = "select from " + NotificationEventReadStatus.class.getSimpleName() + " where username = :usernameParam and eventId in :eventIdSetParam ";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("usernameParam", username);
		paramMap.put("eventIdSetParam", eventIds);

		List<NotificationEventReadStatus> readStatuses = service.getPersistenceService().query(query, paramMap);
		return readStatuses;
	}

	@Override
	public void deleteNotificationBeforeDate(Date archiveDts)
	{
		String deleteQuery = "updateDts < :maxUpdateDts";

		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("maxUpdateDts", archiveDts);

		service.getPersistenceService().deleteByQuery(NotificationEvent.class, deleteQuery, queryParams);
	}

}
