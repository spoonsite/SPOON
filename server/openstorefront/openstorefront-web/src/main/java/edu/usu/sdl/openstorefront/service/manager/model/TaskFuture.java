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

import edu.usu.sdl.openstorefront.service.manager.resource.AsyncTaskCallback;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant.TaskStatus;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * Wraps a future
 *
 * @author dshurtleff
 */
public class TaskFuture
{

	public static int EXPIRED_TIME = 60000 * 5;

	private transient Future future;
	private Date submitedDts;
	private Date completedDts;
	private String taskName;
	private String taskId;
	private boolean allowMultiple;
	private String error;
	private TaskStatus status = TaskStatus.QUEUED;
	private AsyncTaskCallback callback;

	public TaskFuture()
	{
	}

	public TaskFuture(Future future, Date submittedDts, boolean allowMultiple)
	{
		this.future = future;
		this.submitedDts = submittedDts;
		this.allowMultiple = allowMultiple;
		this.taskId = UUID.randomUUID().toString();
	}

	public boolean isExpired()
	{
		boolean expired = false;
		if (completedDts != null) {
			if (System.currentTimeMillis() > (completedDts.getTime() + EXPIRED_TIME)) {
				expired = true;
			}
		}
		return expired;
	}

	public Date getExpireDts()
	{
		Date expireDts = null;
		if (completedDts != null) {
			expireDts = new Date(completedDts.getTime() + EXPIRED_TIME);
		}
		return expireDts;
	}

	public boolean isDone()
	{
		return future.isDone();
	}

	public boolean isCanceled()
	{
		return future.isCancelled();
	}

	public boolean cancel(boolean interrupt)
	{
		boolean canceled = false;
		if (future.isDone() == false
				&& future.isCancelled() == false) {
			canceled = future.cancel(interrupt);
			status = TaskStatus.CANCELLED;
		}
		if (future.isCancelled()) {
			canceled = true;
		}
		return canceled;
	}

	public Future getFuture()
	{
		return future;
	}

	public void setFuture(Future future)
	{
		this.future = future;
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

	public boolean isAllowMultiple()
	{
		return allowMultiple;
	}

	public void setAllowMultiple(boolean allowMultiple)
	{
		this.allowMultiple = allowMultiple;
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

	public AsyncTaskCallback getCallback()
	{
		return callback;
	}

	public void setCallback(AsyncTaskCallback callback)
	{
		this.callback = callback;
	}

}
