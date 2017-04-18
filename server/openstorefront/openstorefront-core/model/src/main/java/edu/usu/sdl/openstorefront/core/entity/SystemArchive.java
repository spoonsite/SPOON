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

import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;

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
	@Sanitize(TextSanitizer.class)
	private String name;

	private String archiveFilename;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	private String originalArchiveFilename;

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

	private String statusDetails;
	private Integer recordsProcessed;
	private Integer totalRecords;
	private Date startDts;
	private Date completedDts;

	@NotNull
	@ValidValueType(value = {}, lookupClass = IODirectionType.class)
	@FK(IODirectionType.class)
	private String ioDirectionType;

	@ValidValueType(value = {}, lookupClass = ImportModeType.class)
	@FK(ImportModeType.class)
	private String importModeType;

	public SystemArchive()
	{
	}

	/**
	 * Get the path to the archive on disk. Note: this may be ran from a proxy
	 * so don't use fields directly
	 *
	 * @return Path or null if archive file doesn't exist.
	 */
	public Path pathToArchive()
	{
		Path path = null;
		if (StringUtils.isNotBlank(getArchiveFilename())) {
			File archiveDir = FileSystemManager.getDir(FileSystemManager.ARCHIVE_DIR);
			path = Paths.get(archiveDir.getPath() + "/" + getArchiveFilename());
		}
		return path;
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

	public String getIoDirectionType()
	{
		return ioDirectionType;
	}

	public void setIoDirectionType(String ioDirectionType)
	{
		this.ioDirectionType = ioDirectionType;
	}

	public String getImportModeType()
	{
		return importModeType;
	}

	public void setImportModeType(String importModeType)
	{
		this.importModeType = importModeType;
	}

	public String getOriginalArchiveFilename()
	{
		return originalArchiveFilename;
	}

	public void setOriginalArchiveFilename(String originalArchiveFilename)
	{
		this.originalArchiveFilename = originalArchiveFilename;
	}

	public String getStatusDetails()
	{
		return statusDetails;
	}

	public void setStatusDetails(String statusDetails)
	{
		this.statusDetails = statusDetails;
	}

	public Integer getRecordsProcessed()
	{
		return recordsProcessed;
	}

	public void setRecordsProcessed(Integer recordsProcessed)
	{
		this.recordsProcessed = recordsProcessed;
	}

	public Integer getTotalRecords()
	{
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords)
	{
		this.totalRecords = totalRecords;
	}

	public Date getStartDts()
	{
		return startDts;
	}

	public void setStartDts(Date startDts)
	{
		this.startDts = startDts;
	}

	public Date getCompletedDts()
	{
		return completedDts;
	}

	public void setCompletedDts(Date completedDts)
	{
		this.completedDts = completedDts;
	}

}
