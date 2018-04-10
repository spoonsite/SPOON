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
package edu.usu.sdl.openstorefront.service.manager;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant.TaskStatus;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.api.model.TaskFuture;
import edu.usu.sdl.openstorefront.core.api.model.TaskRequest;
import edu.usu.sdl.openstorefront.core.entity.AsyncTask;
import edu.usu.sdl.openstorefront.core.entity.ErrorTypeCode;
import edu.usu.sdl.openstorefront.core.entity.NotificationEvent;
import edu.usu.sdl.openstorefront.core.entity.NotificationEventType;
import edu.usu.sdl.openstorefront.core.model.ErrorInfo;
import edu.usu.sdl.openstorefront.core.view.SystemErrorModel;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles running background jobs
 *
 * @author dshurtleff
 */
public class TaskThreadExecutor
		extends ThreadPoolExecutor
{

	private static final Logger LOG = Logger.getLogger(TaskThreadExecutor.class.getName());

	private static final int MAX_DELAY_CHECK = 5;
	private static final long MAX_DELAY_MILLIS = 10;
	private static final int MAX_ORPHAN_QUEUE_TIME = 60000;

	private static List<TaskFuture> tasks = Collections.synchronizedList(new ArrayList<>());
	private static Queue<TaskRequest> queue = new ConcurrentLinkedQueue<>();

	public TaskThreadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t)
	{
		super.afterExecute(r, t);
		if (r instanceof Future<?>) {
			Future future = ((Future<?>) r);
			for (TaskFuture taskFuture : getTasks(true)) {
				if (taskFuture.getFuture().equals(future)) {
					taskFuture.setCompletedDts(TimeUtil.currentDate());
					taskFuture.setStatus(OpenStorefrontConstant.TaskStatus.DONE);

					if (taskFuture.isQueueable()) {
						TaskRequest nextRequest = queue.poll();
						if (nextRequest != null) {
							this.submitTask(nextRequest);
						}
					}

					try {
						future.get();
					} catch (CancellationException ce) {
						taskFuture.setStatus(OpenStorefrontConstant.TaskStatus.CANCELLED);
					} catch (ExecutionException ee) {
						Throwable cause = ee.getCause();
						taskFuture.setStatus(OpenStorefrontConstant.TaskStatus.FAILED);

						ServiceProxy serviceProxy = new ServiceProxy();
						ErrorInfo errorInfo = new ErrorInfo(cause, null);
						errorInfo.setErrorTypeCode(ErrorTypeCode.SYSTEM);
						errorInfo.setInputData("Background Task Error");
						SystemErrorModel systemErrorModel = serviceProxy.getSystemService().generateErrorTicket(errorInfo);
						taskFuture.setError("Task failed.  " + systemErrorModel.toString());
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt(); // ignore/reset
					}

					if (taskFuture.getCallback() != null) {
						taskFuture.getCallback().afterExecute(taskFuture);
					}

					if (TaskStatus.CANCELLED.equals(taskFuture.getStatus()) == false) {
						ServiceProxy serviceProxy = ServiceProxy.getProxy();
						serviceProxy.getSystemService().saveAsyncTask(taskFuture);
					}

					if (OpenStorefrontConstant.ANONYMOUS_USER.equals(taskFuture.getCreateUser()) == false) {
						if (TaskRequest.TASKNAME_REPORT.equals(taskFuture.getTaskName()) == false) {
							NotificationEvent notificationEvent = new NotificationEvent();
							notificationEvent.setEventType(NotificationEventType.TASK);
							notificationEvent.setUsername(taskFuture.getCreateUser());
							notificationEvent.setMessage("Task: " + taskFuture.getTaskName() + " has finished processing with status: " + taskFuture.getStatus());
							notificationEvent.setEntityMetaDataStatus(taskFuture.getStatus().name());
							notificationEvent.setEntityName(AsyncTask.class.getSimpleName());
							notificationEvent.setEntityId(taskFuture.getTaskId());
							ServiceProxy.getProxy().getNotificationService().postEvent(notificationEvent);
						}
					}

				}
			}
		}
		if (t != null) {
			LOG.log(Level.SEVERE, "Failure in background task", t);
		}
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r)
	{
		t.setName("Async-Pool-Task-" + t.getId());
		if (r instanceof Future<?>) {
			Future future = ((Future<?>) r);

			/**
			 * There is a race condition on the task being added and when it
			 * actually runs. So this is here to correct that by waiting for it
			 * to be added.
			 */
			boolean foundTask = false;
			int checks = 0;
			do {
				checks++;
				for (TaskFuture taskFuture : getTasks(true)) {
					if (taskFuture.getFuture().equals(future)) {
						taskFuture.setStatus(OpenStorefrontConstant.TaskStatus.WORKING);

						if (taskFuture.getCallback() != null) {
							taskFuture.getCallback().beforeExecute(taskFuture);
						}

						//Mark thread as a system user
						if (taskFuture.isSystemUser()) {
							SecurityUtil.initSystemUser();
						}

						foundTask = true;
					}
				}
				checkDelay();

			} while (!foundTask && checks <= MAX_DELAY_CHECK);
		}
		super.beforeExecute(t, r);
	}

	private void checkDelay()
	{
		try {
			Thread.sleep(MAX_DELAY_MILLIS);
		} catch (InterruptedException ex) {
			if (LOG.isLoggable(Level.FINEST)) {
				LOG.log(Level.FINEST, null, ex);
			}
			Thread.currentThread().interrupt();
		}
	}

	public List<TaskFuture> getTasks()
	{
		return getTasks(false);
	}

	public List<TaskFuture> getTasks(boolean liveonly)
	{
		Map<String, TaskFuture> taskMap = new HashMap<>();
		for (int i = tasks.size() - 1; i >= 0; i--) {
			TaskFuture taskFuture = tasks.get(i);
			if (taskFuture.isExpired()) {
				tasks.remove(i);
			} else if (this.getQueue().isEmpty()
					&& OpenStorefrontConstant.TaskStatus.QUEUED.equals(taskFuture.getStatus())
					&& System.currentTimeMillis() > (taskFuture.getSubmitedDts().getTime() + MAX_ORPHAN_QUEUE_TIME)) {
				//Remove rejected queued tasks or orphan tasks
				tasks.remove(i);
			} else {
				taskMap.put(taskFuture.getTaskId(), taskFuture);
			}
		}

		if (liveonly == false) {
			ServiceProxy serviceProxy = ServiceProxy.getProxy();

			AsyncTask asyncTaskExample = new AsyncTask();
			asyncTaskExample.setActiveStatus(AsyncTask.ACTIVE_STATUS);

			List<AsyncTask> asyncTasks = serviceProxy.getPersistenceService().queryByExample(asyncTaskExample);
			for (AsyncTask asyncTask : asyncTasks) {
				TaskFuture taskFuture = asyncTask.toTaskFuture();
				if (taskFuture.isExpired()) {
					serviceProxy.getSystemService().removeAsyncTask(taskFuture.getTaskId());
				} else if (taskMap.containsKey(taskFuture.getTaskId()) == false) {
					taskMap.put(taskFuture.getTaskId(), taskFuture);
				}
			}
		}

		return new ArrayList<>(taskMap.values());
	}

	/**
	 * Submits a new task
	 *
	 * @param taskRequest
	 * @return TaskFuture or null if unable to be queued.
	 */
	public synchronized TaskFuture submitTask(TaskRequest taskRequest)
	{
		boolean runJob = true;
		if (taskRequest.isAllowMultiple() == false) {
			List<TaskFuture> currentTasks = getTasks(true);
			for (TaskFuture taskFuture : currentTasks) {
				if (taskFuture.getTaskName().equals(taskRequest.getName())
						&& OpenStorefrontConstant.TaskStatus.WORKING.equals(taskFuture.getStatus())) {

					if (taskRequest.isQueueable()) {
						queue.add(taskRequest);
					}
					runJob = false;
					break;
				}
			}
		}

		TaskFuture taskFuture = null;
		if (runJob) {
			Future future = submit(SecurityUtil.associateSecurity(taskRequest.getTask()));
			taskFuture = new TaskFuture(future, TimeUtil.currentDate(), taskRequest.isAllowMultiple());
			taskFuture.setQueueable(taskRequest.isQueueable());
			taskFuture.setCreateUser(SecurityUtil.getCurrentUserName());
			taskFuture.setDetails(taskRequest.getDetails());
			taskFuture.setTaskData(taskRequest.getTaskData());
			taskFuture.setTaskName(taskRequest.getName());
			taskFuture.setCallback(taskRequest.getCallback());
			taskFuture.setSystemUser(SecurityUtil.isSystemUser());
			tasks.add(taskFuture);

			if (OpenStorefrontConstant.ANONYMOUS_USER.equals(taskFuture.getCreateUser()) == false) {
				if (TaskRequest.TASKNAME_REPORT.equals(taskRequest.getName()) == false) {
					NotificationEvent notificationEvent = new NotificationEvent();
					notificationEvent.setEventType(NotificationEventType.TASK);
					notificationEvent.setUsername(taskFuture.getCreateUser());
					notificationEvent.setMessage("Task: " + taskFuture.getTaskName() + " has been queued for processing. ");
					notificationEvent.setEntityName(AsyncTask.class.getSimpleName());
					notificationEvent.setEntityId(taskFuture.getTaskId());
					notificationEvent.setEntityMetaDataStatus(OpenStorefrontConstant.TaskStatus.QUEUED.name());
					ServiceProxy.getProxy().getNotificationService().postEvent(notificationEvent);
				}
			}

		}
		return taskFuture;
	}

	public boolean cancelTask(String taskId, boolean interrupt)
	{
		boolean cancelled = false;

		for (TaskFuture taskFuture : getTasks(true)) {
			if (taskFuture.getTaskId().equals(taskId)) {
				cancelled = taskFuture.cancel(interrupt);
			}
		}

		return cancelled;
	}

	public void removeTask(String taskId)
	{

		TaskFuture taskFuture = AsyncTaskManager.getTaskById(taskId);

		if (taskFuture != null) {
			if (taskFuture.getCompletedDts() != null) {
				for (int i = tasks.size() - 1; i >= 0; i--) {
					TaskFuture taskFutureInMemory = tasks.get(i);
					if (taskFutureInMemory.getTaskId().equals(taskId)) {
						tasks.remove(i);
					}
				}
				ServiceProxy service = ServiceProxy.getProxy();
				service.getSystemService().removeAsyncTask(taskId);
			} else {
				throw new OpenStorefrontRuntimeException("Unable to remove task.", "Wait for task to complete or cancel it first");
			}
		}
	}

}
