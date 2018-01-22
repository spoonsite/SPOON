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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author dshurtleff
 */
public class EntryStatusReportModel
		extends BaseReportModel
{
	private List<EntryStatusDetailModel> createdEntries = new ArrayList<>();
	private List<EntryStatusDetailModel> evaluations = new ArrayList<>();
	private List<EntryStatusDetailModel> evaluationsInprogress = new ArrayList<>();
	private List<EntryStatusDetailModel> evaluationsPublished = new ArrayList<>();
	

	public EntryStatusReportModel()
	{
	}

	@Override
	public <T> List<T> getData()
	{
		return new ArrayList<>();
	}

	public int entryCreated()
	{
		return createdEntries.size();
	}
	
	public int evaluationsPublished()
	{
		return evaluationDetail(true).size();
	}
	
	public int evaluationsStarted()
	{
		return evaluationDetail(false).size();
	}
	
	public List<EntryStatusDetailModel> evaluationDetail(boolean published)
	{
		List<EntryStatusDetailModel> evals = new ArrayList<>();
		
		evals = evaluations.stream()
						.filter(e->e.getPublished() == published)
						.collect(Collectors.toList());
		return evals;
	}
	
	public int userSubmissions()
	{
		return userSubmissionsDetails().size();
	}
	
	public List<EntryStatusDetailModel> userSubmissionsDetails()
	{
		List<EntryStatusDetailModel> submissions = new ArrayList<>();
		
		submissions = evaluations.stream()
						.filter(e->e.getUserSubmitted())
						.collect(Collectors.toList());
		
		return submissions;
	}	

	
	public List<EntryStatusDetailModel> getCreatedEntries()
	{
		return createdEntries;
	}

	public void setCreatedEntries(List<EntryStatusDetailModel> createdEntries)
	{
		this.createdEntries = createdEntries;
	}

	public List<EntryStatusDetailModel> getEvaluations()
	{
		return evaluations;
	}

	public void setEvaluations(List<EntryStatusDetailModel> evaluations)
	{
		this.evaluations = evaluations;
	}

	public List<EntryStatusDetailModel> getEvaluationsInprogress()
	{
		return evaluationsInprogress;
	}

	public void setEvaluationsInprogress(List<EntryStatusDetailModel> evaluationsInprogress)
	{
		this.evaluationsInprogress = evaluationsInprogress;
	}

	public List<EntryStatusDetailModel> getEvaluationsPublished()
	{
		return evaluationsPublished;
	}

	public void setEvaluationsPublished(List<EntryStatusDetailModel> evaluationsPublished)
	{
		this.evaluationsPublished = evaluationsPublished;
	}

}
