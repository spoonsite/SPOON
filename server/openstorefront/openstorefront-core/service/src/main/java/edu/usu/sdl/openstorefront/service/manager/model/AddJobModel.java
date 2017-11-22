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
package edu.usu.sdl.openstorefront.service.manager.model;

/**
 * Used to Add a Job
 *
 * @author dshurtleff
 */
public class AddJobModel
{

	public static final String JOB_GROUP_SYSTEM = "SYSTEM";
	public static final String JOB_GROUP_REPORT = "REPORT";

	private Class jobClass;
	private String jobName;
	private String description;
	private String jobGroup = JOB_GROUP_SYSTEM;
	private Integer hours;
	private Integer minutes;
	private Integer seconds;
	private Long milliseconds;
	private boolean repeatForever = true;
	private int repeatCount = 0;
	private boolean pause;

	public AddJobModel()
	{
	}

	public Class getJobClass()
	{
		return jobClass;
	}

	public void setJobClass(Class jobClass)
	{
		this.jobClass = jobClass;
	}

	public String getJobName()
	{
		return jobName;
	}

	public void setJobName(String jobName)
	{
		this.jobName = jobName;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getJobGroup()
	{
		return jobGroup;
	}

	public void setJobGroup(String jobGroup)
	{
		this.jobGroup = jobGroup;
	}

	public Integer getHours()
	{
		return hours;
	}

	public void setHours(Integer hours)
	{
		this.hours = hours;
	}

	public Integer getMinutes()
	{
		return minutes;
	}

	public void setMinutes(Integer minutes)
	{
		this.minutes = minutes;
	}

	public Integer getSeconds()
	{
		return seconds;
	}

	public void setSeconds(Integer seconds)
	{
		this.seconds = seconds;
	}

	public boolean isRepeatForever()
	{
		return repeatForever;
	}

	public void setRepeatForever(boolean repeatForever)
	{
		this.repeatForever = repeatForever;
	}

	public int getRepeatCount()
	{
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount)
	{
		this.repeatCount = repeatCount;
	}

	public boolean isPause()
	{
		return pause;
	}

	public void setPause(boolean pause)
	{
		this.pause = pause;
	}

	public Long getMilliseconds()
	{
		return milliseconds;
	}

	public void setMilliseconds(Long milliseconds)
	{
		this.milliseconds = milliseconds;
	}

}
