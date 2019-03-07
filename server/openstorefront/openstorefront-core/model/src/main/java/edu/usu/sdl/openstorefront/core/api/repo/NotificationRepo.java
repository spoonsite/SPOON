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
package edu.usu.sdl.openstorefront.core.api.repo;

import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;
import edu.usu.sdl.openstorefront.core.entity.NotificationEventReadStatus;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dshurtleff
 */
public interface NotificationRepo
{

	/**
	 * Counts notifications for a User including global messages
	 *
	 * @param username
	 * @return
	 */
	long getTotalNotificationsForUser(String username);

	/**
	 * Pulls all active notifications for a user
	 *
	 * @param username
	 * @param queryParams
	 * @return
	 */
	List<NotificationEvent> getNotificationsForUser(String username, FilterQueryParams queryParams);

	/**
	 * Get the read notification
	 *
	 * @param username
	 * @param eventIds
	 * @return
	 */
	List<NotificationEventReadStatus> getReadNoficationsForUser(String username, Set<String> eventIds);

	/**
	 * Delete old notification before date
	 *
	 * @param archiveDts
	 */
	void deleteNotificationBeforeDate(Date archiveDts);

}
