/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
public class CacheView
{

	private String name;
	private long hitCount;
	private double hitRatio;
	private long missCount;
	private long roughCount;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getHitCount()
	{
		return hitCount;
	}

	public void setHitCount(long hitCount)
	{
		this.hitCount = hitCount;
	}

	public double getHitRatio()
	{
		return hitRatio;
	}

	public void setHitRatio(double hitRatio)
	{
		this.hitRatio = hitRatio;
	}

	public long getMissCount()
	{
		return missCount;
	}

	public void setMissCount(long missCount)
	{
		this.missCount = missCount;
	}

	public long getRoughCount()
	{
		return roughCount;
	}

	public void setRoughCount(long roughCount)
	{
		this.roughCount = roughCount;
	}

}
