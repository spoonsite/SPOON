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
package edu.usu.sdl.openstorefront.web.atmosphere;

import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;
import edu.usu.sdl.openstorefront.core.model.NotificationMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.atmosphere.cpr.AtmosphereFramework;
import org.atmosphere.cpr.Broadcaster;
import edu.usu.sdl.openstorefront.core.spi.NotificationEventListener;

/**
 *
 * @author dshurtleff
 */
public class AtmosphereNotificationListerner
		implements NotificationEventListener
{

	private static final Logger log = Logger.getLogger(AtmosphereNotificationListerner.class.getName());

	public static final String BROADCASTER_ROLEGROUP = "ROLE_GROUP_";

	private AtmosphereFramework atmosphereFramework;

	public AtmosphereNotificationListerner(AtmosphereFramework atmosphereFramework)
	{
		this.atmosphereFramework = atmosphereFramework;
	}

	@Override
	public boolean processEvent(NotificationEvent notificationEvent)
	{
		boolean success = false;

		Broadcaster broadcaster = null;
		boolean sendToAll = false;
		if (StringUtils.isNotBlank(notificationEvent.getUsername())) {
			broadcaster = atmosphereFramework.getBroadcasterFactory().lookup(notificationEvent.getUsername());
		} else if (StringUtils.isNotBlank(notificationEvent.getRoleGroup())) {
			broadcaster = atmosphereFramework.getBroadcasterFactory().lookup(BROADCASTER_ROLEGROUP + notificationEvent.getRoleGroup());
		} else {
			sendToAll = true;
		}

		NotificationMessage notificationMessage = NotificationMessage.toMessage(notificationEvent);

		if (sendToAll) {
			for (Broadcaster broadcasterInstance : atmosphereFramework.getBroadcasterFactory().lookupAll()) {
				broadcasterInstance.broadcast(notificationMessage.messageToJson());
			}
		} else {
			if (broadcaster != null) {
				broadcaster.broadcast(notificationMessage.messageToJson());
			} else {
				log.log(Level.FINEST, "Unable to find broadcaster");
			}
		}

		return success;
	}

}
