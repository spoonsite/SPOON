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
package edu.usu.sdl.openstorefront.core.api.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Sets options for the task
 *
 * @author dshurtleff
 */
public class TaskRequest
{

	public static final String TASKNAME_REPORT = "Generating Report";

	public static final String DATAKEY_REPORT_ID = "REPORTID";

	private String name;
	private String details;
	private boolean allowMultiple;
	private boolean queueable = false;
	private Callable task;
	private AsyncTaskCallback callback;
	private Map<String, Object> taskData = new HashMap<>();

	public TaskRequest()
	{
	}

	public String getName()
	{
		return name;
	}

	/**
	 * The name should be concrete (not Dynamic) in order for the Allow multiple
	 * to be enforced. Put Dynamic content in the details
	 *
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isAllowMultiple()
	{
		return allowMultiple;
	}

	public void setAllowMultiple(boolean allowMultiple)
	{
		this.allowMultiple = allowMultiple;
	}
	
	public boolean isQueueable() {
		
		return this.queueable;
	}
	
	public void setQueueable(boolean queueable) {
		
		this.queueable = queueable;
	}

	public Callable getTask()
	{
		return task;
	}

	public void setTask(Callable task)
	{
		this.task = task;
	}

	public AsyncTaskCallback getCallback()
	{
		return callback;
	}

	public void setCallback(AsyncTaskCallback callback)
	{
		this.callback = callback;
	}

	public Map<String, Object> getTaskData()
	{
		return taskData;
	}

	public void setTaskData(Map<String, Object> taskData)
	{
		this.taskData = taskData;
	}

	public String getDetails()
	{
		return details;
	}

	public void setDetails(String details)
	{
		this.details = details;
	}

}
