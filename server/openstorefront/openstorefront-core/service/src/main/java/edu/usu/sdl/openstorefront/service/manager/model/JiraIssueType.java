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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class JiraIssueType
{

	private String self;
	private String id;
	private String name;
	private boolean subtask;
	private List<JiraStatus> statuses = new ArrayList<>();

	public JiraIssueType()
	{
	}

	public String getSelf()
	{
		return self;
	}

	public void setSelf(String self)
	{
		this.self = self;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isSubtask()
	{
		return subtask;
	}

	public void setSubtask(boolean subtask)
	{
		this.subtask = subtask;
	}

	public List<JiraStatus> getStatuses()
	{
		return statuses;
	}

	public void setStatuses(List<JiraStatus> statuses)
	{
		this.statuses = statuses;
	}

}
