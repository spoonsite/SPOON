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

import java.util.concurrent.Callable;

/**
 *
 * @author dshurtleff
 */
public class TaskRequest
{

	private String name;
	private boolean allowMultiple;
	private Callable task;

	public TaskRequest()
	{
	}

	public String getName()
	{
		return name;
	}

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

	public Callable getTask()
	{
		return task;
	}

	public void setTask(Callable task)
	{
		this.task = task;
	}

}
