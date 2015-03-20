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
package edu.usu.sdl.openstorefront.storage.model;

import edu.usu.sdl.openstorefront.service.manager.model.TaskFuture;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant.TaskStatus;
import edu.usu.sdl.openstorefront.util.PK;
import java.util.Date;

/**
 * Holds an Error / Completed Task Information so it can be persists over a
 * longer period across bounces This is a read-only system model
 *
 * @author dshurtleff
 */
public class AsyncTask
		extends BaseEntity<AsyncTask>
{

	@PK()
	private String taskId;
	private Date submitedDts;
	private Date completedDts;
	private String taskName;
	private String details;
	private Boolean allowMultiple;
	private String error;
	private TaskStatus status;

	public AsyncTask()
	{
	}

	public TaskFuture toTaskFuture()
	{
		TaskFuture taskFuture = new TaskFuture();
		taskFuture.setAllowMultiple(getAllowMultiple());
		taskFuture.setCompletedDts(getCompletedDts());
		taskFuture.setCreateUser(getCreateUser());
		taskFuture.setError(getError());
		taskFuture.setStatus(getStatus());
		taskFuture.setSubmitedDts(getSubmitedDts());
		taskFuture.setTaskId(getTaskId());
		taskFuture.setTaskName(getTaskName());
		taskFuture.setDetails(getDetails());
		return taskFuture;
	}

	public Date getSubmitedDts()
	{
		return submitedDts;
	}

	public void setSubmitedDts(Date submitedDts)
	{
		this.submitedDts = submitedDts;
	}

	public Date getCompletedDts()
	{
		return completedDts;
	}

	public void setCompletedDts(Date completedDts)
	{
		this.completedDts = completedDts;
	}

	public String getTaskName()
	{
		return taskName;
	}

	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public String getError()
	{
		return error;
	}

	public void setError(String error)
	{
		this.error = error;
	}

	public TaskStatus getStatus()
	{
		return status;
	}

	public void setStatus(TaskStatus status)
	{
		this.status = status;
	}

	public Boolean getAllowMultiple()
	{
		return allowMultiple;
	}

	public void setAllowMultiple(Boolean allowMultiple)
	{
		this.allowMultiple = allowMultiple;
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
