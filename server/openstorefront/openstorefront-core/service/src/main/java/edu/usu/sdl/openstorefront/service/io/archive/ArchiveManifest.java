/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.io.archive;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ArchiveManifest
{

	private long totalRecords;
	private String systemArchiveType;
	private String applicationVersion;
	private List<EntityManifestRecord> entityRecords = new ArrayList<>();

	public ArchiveManifest()
	{
	}

	public long getTotalRecords()
	{
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords)
	{
		this.totalRecords = totalRecords;
	}

	public List<EntityManifestRecord> getEntityRecords()
	{
		return entityRecords;
	}

	public void setEntityRecords(List<EntityManifestRecord> entityRecords)
	{
		this.entityRecords = entityRecords;
	}

	public String getSystemArchiveType()
	{
		return systemArchiveType;
	}

	public void setSystemArchiveType(String systemArchiveType)
	{
		this.systemArchiveType = systemArchiveType;
	}

	public String getApplicationVersion()
	{
		return applicationVersion;
	}

	public void setApplicationVersion(String applicationVersion)
	{
		this.applicationVersion = applicationVersion;
	}

}
