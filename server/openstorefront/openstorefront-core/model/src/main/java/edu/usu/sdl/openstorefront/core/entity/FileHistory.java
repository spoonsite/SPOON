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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import javax.persistence.Embedded;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
@APIDescription("History of a data import")
public class FileHistory
		extends StandardEntity<FileHistory>
{

	@PK(generated = true)
	@NotNull
	private String fileHistoryId;
	
	@FK(FileFormat.class)
	@NotNull
	@APIDescription("May be a built in format or a format from a plugin which may not be loaded.")
	private String fileFormat;
	
	@ConsumeField
	@FK(FileDataMap.class)
	private String fileDataMapId;	

	private Integer numberRecords;
	private Integer recordsProcessed;
	private Integer recordsStored;
	private Date startDts;
	private Date completeDts;

	@NotNull
	private String filename;

	@NotNull
	private String originalFilename;
	private String mimeType;

	@ValidValueType(value = "", lookupClass = DataSource.class)
	@FK(DataSource.class)
	private String dataSource;

	@ConsumeField
	@Embedded
	@OneToOne(orphanRemoval = true)
	private FileHistoryOption fileHistoryOption;

	public FileHistory()
	{
	}

	@Override
	public void updateFields(StandardEntity entity)
	{
		super.updateFields(entity);

		FileHistory fileHistory = (FileHistory) entity;

		this.setCompleteDts(fileHistory.getCompleteDts());
		this.setDataSource(fileHistory.getDataSource());
		this.setFileFormat(fileHistory.getFileFormat());
		this.setFilename(fileHistory.getFilename());
		this.setMimeType(fileHistory.getMimeType());
		this.setNumberRecords(fileHistory.getNumberRecords());
		this.setOriginalFilename(fileHistory.getOriginalFilename());
		this.setRecordsStored(fileHistory.getRecordsStored());
		this.setRecordsProcessed(fileHistory.getRecordsProcessed());
		this.setStartDts(fileHistory.getStartDts());
		this.setFileHistoryOption(fileHistory.getFileHistoryOption());
	}

	public Path pathToFileName()
	{
		Path path = null;
		if (StringUtils.isNotBlank(getFilename())) {
			File resourceDir = FileSystemManager.getDir(FileSystemManager.IMPORT_HISTORY_DIR);
			path = Paths.get(resourceDir.getPath() + "/" + getFilename());
		}
		return path;
	}

	public String getFileHistoryId()
	{
		return fileHistoryId;
	}

	public void setFileHistoryId(String fileHistoryId)
	{
		this.fileHistoryId = fileHistoryId;
	}

	public Integer getNumberRecords()
	{
		return numberRecords;
	}

	public void setNumberRecords(Integer numberRecords)
	{
		this.numberRecords = numberRecords;
	}

	public Integer getRecordsStored()
	{
		return recordsStored;
	}

	public void setRecordsStored(Integer recordsStored)
	{
		this.recordsStored = recordsStored;
	}

	public Date getStartDts()
	{
		return startDts;
	}

	public void setStartDts(Date startDts)
	{
		this.startDts = startDts;
	}

	public Date getCompleteDts()
	{
		return completeDts;
	}

	public void setCompleteDts(Date completeDts)
	{
		this.completeDts = completeDts;
	}

	public String getFileFormat()
	{
		return fileFormat;
	}

	public void setFileFormat(String fileFormat)
	{
		this.fileFormat = fileFormat;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public String getOriginalFilename()
	{
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename)
	{
		this.originalFilename = originalFilename;
	}

	public String getMimeType()
	{
		return mimeType;
	}

	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}

	public String getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(String dataSource)
	{
		this.dataSource = dataSource;
	}

	public FileHistoryOption getFileHistoryOption()
	{
		return fileHistoryOption;
	}

	public void setFileHistoryOption(FileHistoryOption fileHistoryOption)
	{
		this.fileHistoryOption = fileHistoryOption;
	}

	public Integer getRecordsProcessed()
	{
		return recordsProcessed;
	}

	public void setRecordsProcessed(Integer recordsProcessed)
	{
		this.recordsProcessed = recordsProcessed;
	}

	public String getFileDataMapId()
	{
		return fileDataMapId;
	}

	public void setFileDataMapId(String fileDataMapId)
	{
		this.fileDataMapId = fileDataMapId;
	}

}
