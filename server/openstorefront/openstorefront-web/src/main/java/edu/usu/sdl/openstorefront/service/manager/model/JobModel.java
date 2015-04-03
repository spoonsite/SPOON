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
package edu.usu.sdl.openstorefront.service.manager.model;

import java.util.Date;

/**
 * Holds information on a job
 *
 * @author dshurtleff
 */
public class JobModel
{

	private String jobName;
	private String groupName;
	private String description;
	private String primaryTrigger;
	private String status;
	private boolean durable;
	private boolean persistJobDataAfterExecution;
	private boolean concurrentExectionDisallowed;
	private boolean requestsRecovery;
	private Date perviousFiredTime;
	private Date nextFiredTime;
	private String jobData;
	private String jobClass;

	public JobModel()
	{
	}

	public String getJobName()
	{
		return jobName;
	}

	public void setJobName(String jobName)
	{
		this.jobName = jobName;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public String getPrimaryTrigger()
	{
		return primaryTrigger;
	}

	public void setPrimaryTrigger(String primaryTrigger)
	{
		this.primaryTrigger = primaryTrigger;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public boolean isDurable()
	{
		return durable;
	}

	public void setDurable(boolean durable)
	{
		this.durable = durable;
	}

	public boolean isPersistJobDataAfterExecution()
	{
		return persistJobDataAfterExecution;
	}

	public void setPersistJobDataAfterExecution(boolean persistJobDataAfterExecution)
	{
		this.persistJobDataAfterExecution = persistJobDataAfterExecution;
	}

	public boolean isConcurrentExectionDisallowed()
	{
		return concurrentExectionDisallowed;
	}

	public void setConcurrentExectionDisallowed(boolean concurrentExectionDisallowed)
	{
		this.concurrentExectionDisallowed = concurrentExectionDisallowed;
	}

	public boolean isRequestsRecovery()
	{
		return requestsRecovery;
	}

	public void setRequestsRecovery(boolean requestsRecovery)
	{
		this.requestsRecovery = requestsRecovery;
	}

	public Date getPerviousFiredTime()
	{
		return perviousFiredTime;
	}

	public void setPerviousFiredTime(Date perviousFiredTime)
	{
		this.perviousFiredTime = perviousFiredTime;
	}

	public Date getNextFiredTime()
	{
		return nextFiredTime;
	}

	public void setNextFiredTime(Date nextFiredTime)
	{
		this.nextFiredTime = nextFiredTime;
	}

	public String getJobData()
	{
		return jobData;
	}

	public void setJobData(String jobData)
	{
		this.jobData = jobData;
	}

	public String getJobClass()
	{
		return jobClass;
	}

	public void setJobClass(String jobClass)
	{
		this.jobClass = jobClass;
	}

}
