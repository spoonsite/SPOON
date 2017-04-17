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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
public class SystemArchive
		extends StandardEntity<SystemArchive>
{

	@PK(generated = true)
	@NotNull
	private String archiveId;

	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_255)
	private String name;

	private String archiveFilename;

	@NotNull
	@ValidValueType(value = {}, lookupClass = SystemArchiveType.class)
	@FK(SystemArchiveType.class)
	private String systemArchiveType;

	@ConsumeField
	@Embedded
	@OneToMany(orphanRemoval = true)
	private List<SystemArchiveOption> archiveOptions;

	@NotNull
	@ValidValueType(value = {}, lookupClass = RunStatus.class)
	@FK(RunStatus.class)
	private String runStatus;

	@NotNull
	@ValidValueType(value = {}, lookupClass = IODirectionType.class)
	@FK(IODirectionType.class)
	private String iodirectionType;

	public SystemArchive()
	{
	}

	public String getArchiveId()
	{
		return archiveId;
	}

	public void setArchiveId(String archiveId)
	{
		this.archiveId = archiveId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getArchiveFilename()
	{
		return archiveFilename;
	}

	public void setArchiveFilename(String archiveFilename)
	{
		this.archiveFilename = archiveFilename;
	}

	public String getSystemArchiveType()
	{
		return systemArchiveType;
	}

	public void setSystemArchiveType(String systemArchiveType)
	{
		this.systemArchiveType = systemArchiveType;
	}

	public List<SystemArchiveOption> getArchiveOptions()
	{
		return archiveOptions;
	}

	public void setArchiveOptions(List<SystemArchiveOption> archiveOptions)
	{
		this.archiveOptions = archiveOptions;
	}

	public String getRunStatus()
	{
		return runStatus;
	}

	public void setRunStatus(String runStatus)
	{
		this.runStatus = runStatus;
	}

	public String getIodirectionType()
	{
		return iodirectionType;
	}

	public void setIodirectionType(String iodirectionType)
	{
		this.iodirectionType = iodirectionType;
	}

}
