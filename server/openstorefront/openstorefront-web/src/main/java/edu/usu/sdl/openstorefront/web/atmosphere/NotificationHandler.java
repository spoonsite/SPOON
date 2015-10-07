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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.atmosphere.cache.SessionBroadcasterCache;
import org.atmosphere.config.service.AtmosphereHandlerService;
import org.atmosphere.cpr.AtmosphereHandler;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResponse;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;
import org.atmosphere.util.ExcludeSessionBroadcaster;

/**
 * Handles sending notification events. Note: the path must match the web.xml
 * path for the servlet
 *
 * @author dshurtleff
 */
@AtmosphereHandlerService(path = "/event", interceptors = {AtmosphereResourceLifecycleInterceptor.class
///*, BroadcastOnPostAtmosphereInterceptor.class*/
}, broadcasterCache = SessionBroadcasterCache.class, broadcaster = ExcludeSessionBroadcaster.class)
public class NotificationHandler
		implements AtmosphereHandler
{

	private final static Logger log = Logger.getLogger(NotificationHandler.class.getName());

	@Inject
	private BroadcasterFactory factory;

	@Override
	public void onRequest(AtmosphereResource resource) throws IOException
	{
		log.log(Level.FINEST, MessageFormat.format("On Request Browser {0} connected.", resource.uuid()));
		String username = resource.getRequest().getParameter("id");
		if (StringUtils.isNotBlank(username)) {
			Broadcaster broadcaster = factory.lookup(username);
			if (broadcaster == null) {
				broadcaster = factory.get(username);
				broadcaster.addAtmosphereResource(resource);
			}
			broadcaster.addAtmosphereResource(resource);
		}
	}

	@Override
	public void onStateChange(AtmosphereResourceEvent event) throws IOException
	{
		AtmosphereResource resource = event.getResource();
		if (resource.isCancelled() == false) {
			AtmosphereResponse response = resource.getResponse();

			if (event.isSuspended() && event.getMessage() != null) {
				String message = event.getMessage().toString();
				//String username = resource.getRequest().getParameter("id");

				response.getWriter().write(message);
			}
		} else {
			log.log(Level.FINEST, MessageFormat.format("State Browser {0} unexpectedly disconnected", event.getResource().uuid()));
		}

		if (event.isClosedByClient()) {
			log.log(Level.FINEST, MessageFormat.format("State Browser {0} closed the connection", event.getResource().uuid()));
		}

	}

	@Override
	public void destroy()
	{
		log.log(Level.FINEST, "Destroy");
	}

	public void broadcastEvent(String username, String event)
	{
		Broadcaster broadcaster = factory.lookup(username);
		if (broadcaster != null) {
			broadcaster.broadcast(event);
		} else {
			log.log(Level.FINEST, MessageFormat.format("Unable to find broadcaster for: {0}", username));
		}
	}

}
