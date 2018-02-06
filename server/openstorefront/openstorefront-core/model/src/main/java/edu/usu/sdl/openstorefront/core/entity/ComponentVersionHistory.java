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
import edu.usu.sdl.openstorefront.core.annotation.PK;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Record of a component version history")
public class ComponentVersionHistory
		extends BaseComponent<ComponentVersionHistory>
{

	@PK(generated = true)
	@NotNull
	private String versionHistoryId;

	@NotNull
	@APIDescription("Based on the record version, so it is not necessarily sequential.")
	private Integer version;

	@APIDescription("If this history was based on file history, this field will hold filehistoryId")
	private String fileHistoryId;

	public ComponentVersionHistory()
	{
	}

	@Override
	public String uniqueKey()
	{
		return Integer.toString(version);
	}

	@Override
	protected void customKeyClear()
	{
		setVersionHistoryId(null);
	}

	/**
	 * Get the path to the media on disk. Note: this may be ran from a proxy so
	 * don't use variable directly
	 *
	 * @return Path or null if this doesn't represent a disk resource
	 */
	public Path pathToFile()
	{
		Path path = null;
		if (StringUtils.isNotBlank(getVersionHistoryId())) {
			File pathDir = FileSystemManager.getInstance().getDir(FileSystemManager.COMPONENT_VERSION_DIR);
			path = Paths.get(pathDir.getPath() + "/" + getVersionHistoryId() + ".zip");
		}
		return path;
	}

	public String getVersionHistoryId()
	{
		return versionHistoryId;
	}

	public void setVersionHistoryId(String versionHistoryId)
	{
		this.versionHistoryId = versionHistoryId;
	}

	public Integer getVersion()
	{
		return version;
	}

	public void setVersion(Integer version)
	{
		this.version = version;
	}

	public String getFileHistoryId()
	{
		return fileHistoryId;
	}

	public void setFileHistoryId(String fileHistoryId)
	{
		this.fileHistoryId = fileHistoryId;
	}

}
