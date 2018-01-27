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
import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.api.model.TaskFuture;
import edu.usu.sdl.openstorefront.core.api.model.TaskRequest;
import edu.usu.sdl.openstorefront.service.manager.model.TaskManagerStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles running async tasks.
 *
 * @author dshurtleff
 */
public class AsyncTaskManager
		implements Initializable
{	
	private static final Logger log = Logger.getLogger(AsyncTaskManager.class.getName());
	
	private static AtomicBoolean started = new AtomicBoolean(false);
	private static TaskThreadExecutor taskPool;

	public static void init()
	{
		String maxPoolSize = PropertiesManager.getInstance().getValue(PropertiesManager.KEY_MAX_TASK_POOL_SIZE, "20");
		int poolSize = Convert.toInteger(maxPoolSize);
		taskPool = new TaskThreadExecutor(5, poolSize, 30L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(200));
		taskPool.allowCoreThreadTimeOut(true);		
	}

	public static void cleanup()
	{
		if (taskPool != null) {
			taskPool.shutdownNow();
			try {
				taskPool.awaitTermination(5L, TimeUnit.SECONDS);
			} catch (InterruptedException ex) {
				log.log(Level.WARNING, "Task pool was interrupted durning shutdown. It may not have finsihed shutting down.");
			}
		}
	}

	public static TaskFuture submitTask(TaskRequest taskRequest)
	{
		return taskPool.submitTask(taskRequest);
	}

	public static TaskManagerStatus managerStatus()
	{
		TaskManagerStatus status = new TaskManagerStatus();
		status.setActiveCount(taskPool.getActiveCount());
		status.setCompletedCount(taskPool.getCompletedTaskCount());
		status.setLargestPoolSize(taskPool.getLargestPoolSize());
		status.setMaxPoolSize(taskPool.getMaximumPoolSize());
		status.setQueuedCount(taskPool.getQueue().size());
		status.setTotalTaskCount(taskPool.getTaskCount());
		status.setThreadPoolSize(taskPool.getPoolSize());
		status.getTasks().addAll(taskPool.getTasks());
		return status;
	}

	/**
	 * Gets the first task with the name...note there could be more than one. If
	 * the exact task is needed than make sure to use a unique name.
	 *
	 * @param name
	 * @return taskfuture or null
	 */
	public static TaskFuture getTaskByName(String name)
	{
		TaskFuture taskFuture = null;

		for (TaskFuture taskFutureLocal : taskPool.getTasks()) {
			if (taskFutureLocal.getTaskName().equals(name)) {
				taskFuture = taskFutureLocal;
				break;
			}
		}
		return taskFuture;
	}

	public static List<TaskFuture> getTasksByName(String name)
	{
		List<TaskFuture> taskFutures = new ArrayList<>();

		for (TaskFuture taskFutureLocal : taskPool.getTasks()) {
			if (taskFutureLocal.getTaskName().equals(name)) {
				taskFutures.add(taskFutureLocal);
			}
		}
		return taskFutures;
	}

	/**
	 * Attempts to cancel job
	 *
	 * @param taskId
	 * @param interrupt (set to true, not wait for completion)
	 * @return true if the task was canceled
	 */
	public static boolean cancelTask(String taskId, boolean interrupt)
	{
		return taskPool.cancelTask(taskId, interrupt);
	}

	/**
	 * Remove Completed Task
	 *
	 * @param taskId
	 * @throws OpenStorefrontRuntimeException failure on removing incomplete
	 * task
	 */
	public static void deleteTask(String taskId)
	{
		taskPool.removeTask(taskId);
	}

	public static TaskFuture getTaskById(String taskId)
	{
		TaskFuture taskFuture = null;

		for (TaskFuture taskFutureLocal : taskPool.getTasks()) {
			if (taskFutureLocal.getTaskId().equals(taskId)) {
				taskFuture = taskFutureLocal;
				break;
			}
		}
		return taskFuture;
	}

	@Override
	public void initialize()
	{
		AsyncTaskManager.init();
		started.set(true);		
	}

	@Override
	public void shutdown()
	{
		AsyncTaskManager.cleanup();
		started.set(false);
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

}
