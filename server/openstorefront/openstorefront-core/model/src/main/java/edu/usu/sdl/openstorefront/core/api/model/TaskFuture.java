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

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant.TaskStatus;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * Wraps a future
 *
 * @author dshurtleff
 */
public class TaskFuture
{

	private transient Future future;
	private Date submitedDts;
	private Date completedDts;
	private String taskName;
	private String details;
	private String taskId;
	private boolean allowMultiple;
	private boolean queueable;
	private String error;
	private String createUser;
	private TaskStatus status = TaskStatus.QUEUED;
	private AsyncTaskCallback callback;
	private Map<String, Object> taskData = new HashMap<>();
	private Date expireDts;
	private boolean systemUser;

	public TaskFuture()
	{
		//This generally system created.
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
			String expireTimeMinutes;
			if (TaskStatus.DONE.equals(status) || TaskStatus.CANCELLED.equals(status)) {
				expireTimeMinutes = PropertiesManager.getValue(PropertiesManager.KEY_MAX_TASK_COMPLETE_EXPIRE, "5");
			} else {
				expireTimeMinutes = PropertiesManager.getValue(PropertiesManager.KEY_MAX_TASK_ERROR_EXPIRE, "4320");
			}
			long expireTime = Convert.toLong(expireTimeMinutes) * 60000;

			if (System.currentTimeMillis() > (completedDts.getTime() + Convert.toLong(expireTime))) {
				expired = true;
			}
		}
		return expired;
	}

	public Date getExpireDts()
	{
		if (completedDts != null) {
			String expireTimeMinutes;
			if (TaskStatus.DONE.equals(status) || TaskStatus.CANCELLED.equals(status)) {
				expireTimeMinutes = PropertiesManager.getValue(PropertiesManager.KEY_MAX_TASK_COMPLETE_EXPIRE, "5");
			} else {
				expireTimeMinutes = PropertiesManager.getValue(PropertiesManager.KEY_MAX_TASK_ERROR_EXPIRE, "4320");
			}
			long expireTime = Convert.toLong(expireTimeMinutes) * 60000;

			expireDts = new Date(completedDts.getTime() + expireTime);
		}
		return expireDts;
	}

	public void setExpireDts(Date expiredDts)
	{
		this.expireDts = expiredDts;
	}

	public boolean isDone()
	{
		if (future != null) {
			return future.isDone();
		}
		return false;
	}

	public boolean isCanceled()
	{
		if (future != null) {
			return future.isCancelled();
		}
		return false;
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

	public boolean isQueueable()
	{

		return this.queueable;
	}

	public void setQueueable(boolean queueable)
	{

		this.queueable = queueable;
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

	public String getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
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

	public boolean isSystemUser()
	{
		return systemUser;
	}

	public void setSystemUser(boolean systemUser)
	{
		this.systemUser = systemUser;
	}

}
