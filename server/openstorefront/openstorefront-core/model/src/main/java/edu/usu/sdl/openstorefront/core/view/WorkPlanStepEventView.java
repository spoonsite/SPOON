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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.EntityEventType;
import edu.usu.sdl.openstorefront.core.entity.WorkPlanStepEvent;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class WorkPlanStepEventView
		extends WorkPlanStepEvent
{

	private static final long serialVersionUID = 1L;

	private String eventTypeDescription;

	public WorkPlanStepEventView()
	{
	}

	public static WorkPlanStepEventView toView(WorkPlanStepEvent event)
	{
		WorkPlanStepEventView view = new WorkPlanStepEventView();

		try {
			BeanUtils.copyProperties(view, event);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		view.setEventTypeDescription(TranslateUtil.translate(EntityEventType.class, view.getEntityEventType()));

		return view;
	}

	public static List<WorkPlanStepEventView> toView(List<WorkPlanStepEvent> events)
	{
		List<WorkPlanStepEventView> views = new ArrayList<>();

		events.forEach(event -> {
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

}
