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
package edu.usu.sdl.openstorefront.core.view;

/**
 *
 * @author dshurtleff
 */
public class MemoryPoolStatus
{

	private String name;
	private String details;
	private long initKb;
	private long usedKb;
	private long commitedKb;
	private long maxKb;

	public long getInitKb()
	{
		return initKb;
	}

	public void setInitKb(long initKb)
	{
		this.initKb = initKb;
	}

	public long getUsedKb()
	{
		return usedKb;
	}

	public void setUsedKb(long usedKb)
	{
		this.usedKb = usedKb;
	}

	public long getCommitedKb()
	{
		return commitedKb;
	}

	public void setCommitedKb(long commitedKb)
	{
		this.commitedKb = commitedKb;
	}

	public long getMaxKb()
	{
		return maxKb;
	}

	public void setMaxKb(long MaxKb)
	{
		this.maxKb = MaxKb;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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
