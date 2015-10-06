/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;
import edu.usu.sdl.openstorefront.core.spi.NotificationEventListerner;
import edu.usu.sdl.openstorefront.core.view.FilterQueryParams;
import edu.usu.sdl.openstorefront.core.view.NotificationEventWrapper;

/**
 * Handles Notification Events.
 *
 * @author dshurtleff
 */
public interface NotificationService
		extends AsyncService
{

	/**
	 * Finds all notification for a user.
	 *
	 * @param username
	 * @param queryParams
	 * @return
	 */
	public NotificationEventWrapper getAllEventsForUser(String username, FilterQueryParams queryParams);

	/**
	 * Adds a new Listerner to be able to handle events
	 *
	 * @param notificationEventListerner
	 */
	public void registerNotificationListerner(NotificationEventListerner notificationEventListerner);

	/**
	 * Save a new Notification event and notify Listerner
	 *
	 * @param notificationEvent
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void postEvent(NotificationEvent notificationEvent);

	/**
	 * Remove an event from the system
	 *
	 * @param eventId
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteEvent(String eventId);

	/**
	 * Removes records older than the clean up property is set to
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void cleanupOldEvents();

}
