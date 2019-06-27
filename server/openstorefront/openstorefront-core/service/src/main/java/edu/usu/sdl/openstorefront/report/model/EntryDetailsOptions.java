/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.report.model;

import edu.usu.sdl.openstorefront.core.entity.ReportOption;

/**
 *
 * @author dshurtleff
 */
public class EntryDetailsOptions
{

	private boolean displayContacts;
	private boolean displayDependencies;
	private boolean displayDescription;
	private boolean displayEvalDetails;
	private boolean displayEvalSummary;
	private boolean displayEvalVersions;
	private boolean displayOrgData;
	private boolean displayQA;
	private boolean displayDates;
	private boolean displayRelationships;
	private boolean displayReportReviews;
	private boolean displayResources;
	private boolean displayTags;
	private boolean displayVitals;

	public EntryDetailsOptions(ReportOption reportOption)
	{
		displayContacts = reportOption.getDisplayContacts();
		displayDependencies = reportOption.getDisplayDependencies();
		displayDescription = reportOption.getDisplayDescription();
		displayEvalDetails = reportOption.getDisplayEvalDetails();
		displayEvalSummary = reportOption.getDisplayEvalSummary();
		displayEvalVersions = reportOption.getDisplayEvalVersions();
		displayOrgData = reportOption.getDisplayOrgData();
		displayQA = reportOption.getDisplayQA();
		displayDates = reportOption.getDisplayDates();
		displayRelationships = reportOption.getDisplayRelationships();
		displayReportReviews = reportOption.getDisplayReportReviews();
		displayResources = reportOption.getDisplayResources();
		displayTags = reportOption.getDisplayTags();
		displayVitals = reportOption.getDisplayVitals();
	}

	public boolean isDisplayContacts()
	{
		return displayContacts;
	}

	public void setDisplayContacts(boolean displayContacts)
	{
		this.displayContacts = displayContacts;
	}

	public boolean isDisplayDependencies()
	{
		return displayDependencies;
	}

	public void setDisplayDependencies(boolean displayDependencies)
	{
		this.displayDependencies = displayDependencies;
	}

	public boolean isDisplayDescription()
	{
		return displayDescription;
	}

	public void setDisplayDescription(boolean displayDescription)
	{
		this.displayDescription = displayDescription;
	}

	public boolean isDisplayEvalDetails()
	{
		return displayEvalDetails;
	}

	public void setDisplayEvalDetails(boolean displayEvalDetails)
	{
		this.displayEvalDetails = displayEvalDetails;
	}

	public boolean isDisplayEvalSummary()
	{
		return displayEvalSummary;
	}

	public void setDisplayEvalSummary(boolean displayEvalSummary)
	{
		this.displayEvalSummary = displayEvalSummary;
	}

	public boolean isDisplayEvalVersions()
	{
		return displayEvalVersions;
	}

	public void setDisplayEvalVersions(boolean displayEvalVersions)
	{
		this.displayEvalVersions = displayEvalVersions;
	}

	public boolean isDisplayOrgData()
	{
		return displayOrgData;
	}

	public void setDisplayOrgData(boolean displayOrgData)
	{
		this.displayOrgData = displayOrgData;
	}

	public boolean isDisplayQA()
	{
		return displayQA;
	}

	public boolean isDisplayDates() {
		return this.displayDates;
	}

	public void setDisplayDates(boolean displayDates) {
		this.displayDates = displayDates;
	}


	public void setDisplayQA(boolean displayQA)
	{
		this.displayQA = displayQA;
	}

	public boolean isDisplayRelationships()
	{
		return displayRelationships;
	}

	public void setDisplayRelationships(boolean displayRelationships)
	{
		this.displayRelationships = displayRelationships;
	}

	public boolean isDisplayReportReviews()
	{
		return displayReportReviews;
	}

	public void setDisplayReportReviews(boolean displayReportReviews)
	{
		this.displayReportReviews = displayReportReviews;
	}

	public boolean isDisplayResources()
	{
		return displayResources;
	}

	public void setDisplayResources(boolean displayResources)
	{
		this.displayResources = displayResources;
	}

	public boolean isDisplayTags()
	{
		return displayTags;
	}

	public void setDisplayTags(boolean displayTags)
	{
		this.displayTags = displayTags;
	}

	public boolean isDisplayVitals()
	{
		return displayVitals;
	}

	public void setDisplayVitals(boolean displayVitals)
	{
		this.displayVitals = displayVitals;
	}

}
