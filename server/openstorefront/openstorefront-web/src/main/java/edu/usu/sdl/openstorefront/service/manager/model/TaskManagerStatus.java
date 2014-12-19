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

import edu.usu.sdl.openstorefront.doc.DataType;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds task manager status
 *
 * @author dshurtleff
 */
public class TaskManagerStatus
{

	private int activeCount;
	private int maxPoolSize;
	private long completedCount;
	private long totalTaskCount;
	private int largestPoolSize;
	private int queuedCount;
	private int threadPoolSize;

	@DataType(TaskFuture.class)
	private List<TaskFuture> tasks = new ArrayList<>();

	public TaskManagerStatus()
	{
	}

	public int getActiveCount()
	{
		return activeCount;
	}

	public void setActiveCount(int activeCount)
	{
		this.activeCount = activeCount;
	}

	public int getMaxPoolSize()
	{
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize)
	{
		this.maxPoolSize = maxPoolSize;
	}

	public long getCompletedCount()
	{
		return completedCount;
	}

	public void setCompletedCount(long completedCount)
	{
		this.completedCount = completedCount;
	}

	public long getTotalTaskCount()
	{
		return totalTaskCount;
	}

	public void setTotalTaskCount(long totalTaskCount)
	{
		this.totalTaskCount = totalTaskCount;
	}

	public int getLargestPoolSize()
	{
		return largestPoolSize;
	}

	public void setLargestPoolSize(int largestPoolSize)
	{
		this.largestPoolSize = largestPoolSize;
	}

	public int getQueuedCount()
	{
		return queuedCount;
	}

	public void setQueuedCount(int queuedCount)
	{
		this.queuedCount = queuedCount;
	}

	public List<TaskFuture> getTasks()
	{
		return tasks;
	}

	public void setTasks(List<TaskFuture> tasks)
	{
		this.tasks = tasks;
	}

	public int getThreadPoolSize()
	{
		return threadPoolSize;
	}

	public void setThreadPoolSize(int threadPoolSize)
	{
		this.threadPoolSize = threadPoolSize;
	}

}
