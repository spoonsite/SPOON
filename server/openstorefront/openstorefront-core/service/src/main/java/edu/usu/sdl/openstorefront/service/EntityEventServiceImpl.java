/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.core.api.EntityEventService;
import edu.usu.sdl.openstorefront.core.model.EntityEventModel;
import edu.usu.sdl.openstorefront.core.model.EntityEventRegistrationModel;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class EntityEventServiceImpl
		extends ServiceProxy
		implements EntityEventService
{

	private static final Logger LOG = Logger.getLogger(EntityEventServiceImpl.class.getName());

	private static final String EVENT_LISTENER_KEY = "EVENT_LISTENERS";

	@Override
	@SuppressWarnings("unchecked")
	public void registerEventListener(EntityEventRegistrationModel registrationModel)
	{
		List<EntityEventRegistrationModel> listeners = (List<EntityEventRegistrationModel>) OSFCacheManager.getApplicationCache().get(EVENT_LISTENER_KEY);
		if (listeners == null) {
			listeners = new ArrayList<>();
			OSFCacheManager.getApplicationCache().put(EVENT_LISTENER_KEY, listeners);
		}
		listeners.add(registrationModel);
		OSFCacheManager.getApplicationCache().replace(EVENT_LISTENER_KEY, listeners);
		LOG.log(Level.INFO, () -> "Registered Event Listener: " + registrationModel.getName() + " Id: " + registrationModel.getRegistrationId());
	}

	@Override
	public void unregisterEventListener(String registrationId)
	{
		Object element = OSFCacheManager.getApplicationCache().get(EVENT_LISTENER_KEY);
		if (element != null) {

			@SuppressWarnings("unchecked")
			List<EntityEventRegistrationModel> listeners = (List<EntityEventRegistrationModel>) element;
			listeners.removeIf((listenerModel) -> {
				return listenerModel.getRegistrationId().equals(registrationId);
			});

			OSFCacheManager.getApplicationCache().replace(EVENT_LISTENER_KEY, listeners);
			LOG.log(Level.INFO, () -> "Un Register Event Listener: " + registrationId);
		}
	}

	@Override
	public void processEvent(EntityEventModel entityEventModel)
	{
		//NO-OP if null
		if (entityEventModel != null) {
			Object element = OSFCacheManager.getApplicationCache().get(EVENT_LISTENER_KEY);
			if (element != null) {

				@SuppressWarnings("unchecked")
				List<EntityEventRegistrationModel> listeners = (List<EntityEventRegistrationModel>) element;
				listeners.forEach((listenerModel) -> {
					try {
						if (LOG.isLoggable(Level.FINEST)) {
							LOG.log(Level.FINEST, () -> "Process Event: " + entityEventModel + " Handler: " + listenerModel.getName());
						}
						boolean processed = listenerModel.getListener().processEvent(entityEventModel);
						LOG.log(Level.FINEST, "Finished Processing Event");

					} catch (Exception e) {
						LOG.log(Level.WARNING, "Failed during processing of an event.", e);
					}
				});
			}
		}
	}

}
