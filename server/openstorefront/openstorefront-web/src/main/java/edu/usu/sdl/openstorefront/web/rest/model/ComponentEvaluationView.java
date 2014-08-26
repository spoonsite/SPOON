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
import java.util.ArrayList;
import java.util.Date;
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
	
	@DataType(ComponentEvaluationScheduleView.class)
	private List<ComponentEvaluationScheduleView> evaluationSchedule = new ArrayList<>();
	
	@DataType(ComponentEvaluationSectionView.class)
	private List<ComponentEvaluationSectionView> evaluationSections = new ArrayList<>();

	public ComponentEvaluationView()
	{
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
	
}
