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

import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.manager.model.TaskFuture;
import edu.usu.sdl.openstorefront.service.manager.model.TaskRequest;
import edu.usu.sdl.openstorefront.service.transfermodel.ErrorInfo;
import edu.usu.sdl.openstorefront.storage.model.ErrorTypeCode;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.TimeUtil;
import edu.usu.sdl.openstorefront.web.viewmodel.SystemErrorModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
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

	private static final Logger log = Logger.getLogger(TaskThreadExecutor.class.getName());

	private static List<TaskFuture> tasks = Collections.synchronizedList(new ArrayList<>());

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
			for (TaskFuture taskFuture : getTasks()) {
				if (taskFuture.getFuture().equals(future)) {
					taskFuture.setCompletedDts(TimeUtil.currentDate());
					taskFuture.setStatus(OpenStorefrontConstant.TaskStatus.DONE);
					if (t != null) {
						taskFuture.setStatus(OpenStorefrontConstant.TaskStatus.FAILED);

						ServiceProxy serviceProxy = new ServiceProxy();
						ErrorInfo errorInfo = new ErrorInfo(t, null);
						errorInfo.setErrorTypeCode(ErrorTypeCode.SYSTEM);
						errorInfo.setInputData("Background Task Error");
						SystemErrorModel systemErrorModel = serviceProxy.getSystemService().generateErrorTicket(errorInfo);
						taskFuture.setError("Task failed.  " + systemErrorModel.toString());
					}
				}
			}
		}
		if (t != null) {
			log.log(Level.SEVERE, "Failure in background task", t);
		}
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r)
	{
		super.beforeExecute(t, r);

		if (r instanceof Future<?>) {
			Future future = ((Future<?>) r);
			for (TaskFuture taskFuture : getTasks()) {
				if (taskFuture.getFuture().equals(future)) {
					taskFuture.setStatus(OpenStorefrontConstant.TaskStatus.WORKING);
				}
			}
		}
	}

	public List<TaskFuture> getTasks()
	{
		for (int i = tasks.size() - 1; i >= 0; i--) {
			TaskFuture taskFuture = tasks.get(i);
			if (taskFuture.isExpired()) {
				tasks.remove(i);
			}
		}
		return tasks;
	}

	/**
	 * Submits a new task
	 *
	 * @param taskRequest
	 * @return taskfuture or null if unable to be queued.
	 */
	public TaskFuture submitTask(TaskRequest taskRequest)
	{
		boolean runJob = true;
		if (taskRequest.isAllowMultiple() == false) {
			List<TaskFuture> currentTasks = getTasks();
			for (TaskFuture taskFuture : currentTasks) {
				if (taskFuture.getTaskName().equals(taskRequest.getName())
						&& OpenStorefrontConstant.TaskStatus.WORKING.equals(taskFuture.getStatus())) {
					runJob = false;
				}
			}
		}

		TaskFuture taskFuture = null;
		if (runJob) {
			Future future = submit(taskRequest.getTask());
			taskFuture = new TaskFuture(future, TimeUtil.currentDate(), taskRequest.isAllowMultiple());
			tasks.add(taskFuture);
		}
		return taskFuture;
	}

	public boolean cancelTask(String taskId, boolean interrupt)
	{
		boolean cancelled = false;

		for (TaskFuture taskFuture : getTasks()) {
			if (taskFuture.getTaskId().equals(taskId)) {
				cancelled = taskFuture.cancel(interrupt);
			}
		}

		return cancelled;
	}

}
