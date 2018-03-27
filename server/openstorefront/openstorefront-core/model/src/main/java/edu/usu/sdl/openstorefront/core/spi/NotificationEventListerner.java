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
package edu.usu.sdl.openstorefront.core.spi;

import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;

/**
 *
 * @author dshurtleff
 */
@FunctionalInterface
public interface NotificationEventListerner
{

	/**
	 * It is called when a new event get trigger in the application
	 *
	 * @param notificationEvent
	 * @return success of the processing
	 */
	public boolean processEvent(NotificationEvent notificationEvent);

}
