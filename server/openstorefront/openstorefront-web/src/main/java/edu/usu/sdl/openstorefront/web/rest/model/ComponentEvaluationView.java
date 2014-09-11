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

import edu.usu.sdl.openstorefront.doc.DataType;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSchedule;
import edu.usu.sdl.openstorefront.storage.model.ComponentEvaluationSection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ComponentEvaluationView
{
	private Date startDate;
	private Date endDate;
	private String currentLevelCode = "N/A";
	private String reviewedVersion;
	private Date updateDts;
	
	@DataType(ComponentEvaluationScheduleView.class)
	private List<ComponentEvaluationScheduleView> evaluationSchedule = new ArrayList<>();
	
	@DataType(ComponentEvaluationSectionView.class)
	private List<ComponentEvaluationSectionView> evaluationSections = new ArrayList<>();

	public ComponentEvaluationView()
	{
	}
	
	public static ComponentEvaluationView toViewFromStorage(List<ComponentEvaluationSchedule> schedules, List<ComponentEvaluationSection> sections)
	{
		List<ComponentEvaluationScheduleView> newSchedules = ComponentEvaluationScheduleView.toViewList(schedules);
		List<ComponentEvaluationSectionView> newSections = ComponentEvaluationSectionView.toViewList(sections);
		return ComponentEvaluationView.toView(newSchedules, newSections);
	}
	
	public static ComponentEvaluationView toView(List<ComponentEvaluationScheduleView> schedules, List<ComponentEvaluationSectionView> sections)
	{
		ComponentEvaluationView view = new ComponentEvaluationView();
		view.setEvaluationSchedule(schedules);
		view.setEvaluationSections(sections);
		
		//FIXME: TODO: Change this to the correct start date. (where do we get it from?)
		view.setStartDate(new Date());
		view.setEndDate(new Date());
		
		//FIND THE CORRECT LEVEL CODE SOMEHOW
		view.setCurrentLevelCode("");
		
		// where do we get this from?
		view.setReviewedVersion(null);
		Date max;
		max = (schedules.size() > 0 && schedules.get(0) != null)? schedules.get(0).getUpdateDts(): null;
		for (ComponentEvaluationScheduleView schedule : schedules) {
			if (schedule.getUpdateDts().compareTo(max) > 0)
			{
				max = schedule.getUpdateDts();
			}
		}
		for (ComponentEvaluationSectionView section : sections) {
			if (section.getUpdateDts().compareTo(max) > 0)
			{
				max = section.getUpdateDts();
			}
		}
		view.setUpdateDts(max);
		return view;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public String getCurrentLevelCode()
	{
		return currentLevelCode;
	}

	public void setCurrentLevelCode(String currentLevelCode)
	{
		this.currentLevelCode = currentLevelCode;
	}

	public List<ComponentEvaluationScheduleView> getEvaluationSchedule()
	{
		return evaluationSchedule;
	}

	public void setEvaluationSchedule(List<ComponentEvaluationScheduleView> evaluationSchedule)
	{
		this.evaluationSchedule = evaluationSchedule;
	}

	public String getReviewedVersion()
	{
		return reviewedVersion;
	}

	public void setReviewedVersion(String reviewedVersion)
	{
		this.reviewedVersion = reviewedVersion;
	}

	public List<ComponentEvaluationSectionView> getEvaluationSections()
	{
		return evaluationSections;
	}

	public void setEvaluationSections(List<ComponentEvaluationSectionView> evaluationSections)
	{
		this.evaluationSections = evaluationSections;
	}

	/**
	 * @return the updateDts
	 */
	public Date getUpdateDts()
	{
		return updateDts;
	}

	/**
	 * @param updateDts the updateDts to set
	 */
	public void setUpdateDts(Date updateDts)
	{
		this.updateDts = updateDts;
	}
	
}
