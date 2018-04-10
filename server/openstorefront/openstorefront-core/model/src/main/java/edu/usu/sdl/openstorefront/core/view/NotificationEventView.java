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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;
import edu.usu.sdl.openstorefront.core.entity.NotificationEventType;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class NotificationEventView
		extends NotificationEvent
{

	private String eventTypeDescription;
	private boolean readMessage;

	public static NotificationEventView toView(NotificationEvent notificationEvent)
	{
		NotificationEventView view = new NotificationEventView();
		try {
			BeanUtils.copyProperties(view, notificationEvent);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		view.setEventTypeDescription(TranslateUtil.translate(NotificationEventType.class, notificationEvent.getEventType()));

		return view;
	}

	public static List<NotificationEventView> toView(List<NotificationEvent> notificationEvents)
	{
		List<NotificationEventView> views = new ArrayList<>();

		notificationEvents.forEach(event -> {
			views.add(toView(event));
		});

		return views;
	}

	public String getEventTypeDescription()
	{
		return eventTypeDescription;
	}

	public void setEventTypeDescription(String eventTypeDescription)
	{
		this.eventTypeDescription = eventTypeDescription;
	}

	public boolean getReadMessage()
	{
		return readMessage;
	}

	public void setReadMessage(boolean readMessage)
	{
		this.readMessage = readMessage;
	}

}
