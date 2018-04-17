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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
public class ApplicationStatus
{

	private String applicationVersion;
	private int processorCount;
	private double systemLoad;
	private Map<String, String> systemProperties = new HashMap<>();
	private Date startTime;
	private String upTime;
	private long totalThreadCount;
	private long liveThreadCount;
	private MemoryPoolStatus heapMemoryStatus = new MemoryPoolStatus();
	private MemoryPoolStatus nonHeapMemoryStatus = new MemoryPoolStatus();
	private List<MemoryPoolStatus> memoryPools = new ArrayList<>();
	private List<String> garbageCollectionInfos = new ArrayList<>();
	private long freeDiskSpace;
	private long usedDiskSpace;
	private long totalDiskSpace;
	private String rootStoragePath;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ApplicationStatus()
	{
	}

	public String getApplicationVersion()
	{
		return applicationVersion;
	}

	public void setApplicationVersion(String applicationVersion)
	{
		this.applicationVersion = applicationVersion;
	}

	public int getProcessorCount()
	{
		return processorCount;
	}

	public void setProcessorCount(int processorCount)
	{
		this.processorCount = processorCount;
	}

	public double getSystemLoad()
	{
		return systemLoad;
	}

	public void setSystemLoad(double systemLoad)
	{
		this.systemLoad = systemLoad;
	}

	public Map<String, String> getSystemProperties()
	{
		return systemProperties;
	}

	public void setSystemProperties(Map<String, String> systemProperties)
	{
		this.systemProperties = systemProperties;
	}

	public Date getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	public String getUpTime()
	{
		return upTime;
	}

	public void setUpTime(String upTime)
	{
		this.upTime = upTime;
	}

	public long getTotalThreadCount()
	{
		return totalThreadCount;
	}

	public void setTotalThreadCount(long totalThreadCount)
	{
		this.totalThreadCount = totalThreadCount;
	}

	public long getLiveThreadCount()
	{
		return liveThreadCount;
	}

	public void setLiveThreadCount(long liveThreadCount)
	{
		this.liveThreadCount = liveThreadCount;
	}

	public List<String> getGarbageCollectionInfos()
	{
		return garbageCollectionInfos;
	}

	public void setGarbageCollectionInfos(List<String> garbageCollectionInfos)
	{
		this.garbageCollectionInfos = garbageCollectionInfos;
	}

	public MemoryPoolStatus getHeapMemoryStatus()
	{
		return heapMemoryStatus;
	}

	public void setHeapMemoryStatus(MemoryPoolStatus heapMemoryStatus)
	{
		this.heapMemoryStatus = heapMemoryStatus;
	}

	public MemoryPoolStatus getNonHeapMemoryStatus()
	{
		return nonHeapMemoryStatus;
	}

	public void setNonHeapMemoryStatus(MemoryPoolStatus nonHeapMemoryStatus)
	{
		this.nonHeapMemoryStatus = nonHeapMemoryStatus;
	}

	public List<MemoryPoolStatus> getMemoryPools()
	{
		return memoryPools;
	}

	public void setMemoryPools(List<MemoryPoolStatus> memoryPools)
	{
		this.memoryPools = memoryPools;
	}

	public long getTotalDiskSpace()
	{
		return totalDiskSpace;
	}

	public void setTotalDiskSpace(long totalDiskSpace)
	{
		this.totalDiskSpace = totalDiskSpace;
	}

	public long getFreeDiskSpace()
	{
		return freeDiskSpace;
	}

	public void setFreeDiskSpace(long freeDiskSpace)
	{
		this.freeDiskSpace = freeDiskSpace;
	}

	public long getUsedDiskSpace()
	{
		return usedDiskSpace;
	}

	public void setUsedDiskSpace(long usedDiskSpace)
	{
		this.usedDiskSpace = usedDiskSpace;
	}

	public String getRootStoragePath()
	{
		return rootStoragePath;
	}

	public void setRootStoragePath(String rootStoragePath)
	{
		this.rootStoragePath = rootStoragePath;
	}

}
