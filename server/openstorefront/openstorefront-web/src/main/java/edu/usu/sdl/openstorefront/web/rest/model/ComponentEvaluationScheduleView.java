/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.rest.model;

import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSchedule;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentEvaluationScheduleView
{

	private String evaluationLevelCode;
	private Date completionDate;
	private String levelStatus;

	public ComponentEvaluationScheduleView()
	{
	}
	
	public static ComponentEvaluationScheduleView toView(ComponentEvaluationSchedule schedule)
	{
		ComponentEvaluationScheduleView view = new ComponentEvaluationScheduleView();
		view.setCompletionDate(schedule.getCompletionDate());
		view.setEvaluationLevelCode(schedule.getComponentEvaluationSchedulePk().getEvaluationLevelCode());
		view.setLevelStatus(schedule.getLevelStatus());
		return view;
	}
	
	public static List<ComponentEvaluationScheduleView> toViewList(List<ComponentEvaluationSchedule> schedules)
	{
		List<ComponentEvaluationScheduleView> viewList = new ArrayList();
		schedules.forEach(schedule->{
			viewList.add(ComponentEvaluationScheduleView.toView(schedule));
		});
		return viewList;
	}

	public String getEvaluationLevelCode()
	{
		return evaluationLevelCode;
	}

	public void setEvaluationLevelCode(String evaluationLevelCode)
	{
		this.evaluationLevelCode = evaluationLevelCode;
	}

	public String getLevelStatus()
	{
		return levelStatus;
	}

	public void setLevelStatus(String levelStatus)
	{
		this.levelStatus = levelStatus;
	}

	public Date getCompletionDate()
	{
		return completionDate;
	}

	public void setCompletionDate(Date completionDate)
	{
		this.completionDate = completionDate;
	}

}
