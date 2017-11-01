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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.Unique;
import edu.usu.sdl.openstorefront.validation.GeneralMediaUniqueHandler;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.nio.file.Path;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 * @author kbair
 */
@APIDescription("General media used for articles, bagdes, etc.")
public class GeneralMedia
		extends StandardEntity<GeneralMedia>
{

	@PK
	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@Unique(GeneralMediaUniqueHandler.class)
	private String name;

	@NotNull
	@APIDescription("Stored file information")
	private MediaFile file;

	/**
	 * @deprecated As of release 2.5, replaced by {@link #file}
	 */
	@Deprecated
	@APIDescription("Deprecated as of release 2.5, replaced by MediaFile")
	private String fileName;

	/**
	 * @deprecated As of release 2.5, replaced by {@link #file}
	 */
	@Deprecated
	@APIDescription("Deprecated as of release 2.5, replaced by MediaFile")
	private String originalFileName;

	/**
	 * @deprecated As of release 2.5, replaced by {@link #file}
	 */
	@Deprecated
	@APIDescription("Deprecated as of release 2.5, replaced by MediaFile")
	private String mimeType;

	public GeneralMedia()
	{
	}

	/**
	 * Get the path to the media on disk.
	 *
	 * @return Path or null if this doesn't represent a disk resource
	 */
	public Path pathToMedia()
	{
		//Note: this may be ran from a proxy so don't use variable directly
		return (this.getFile() == null) ? null : this.getFile().path();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public MediaFile getFile()
	{
		return file;
	}

	public void setFile(MediaFile file)
	{
		this.file = file;
	}

	/**
	 * @return name of the file on the file system
	 * @deprecated As of release 2.5, replaced by
	 * {@link #getFile().getFileName()}
	 */
	@Deprecated
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName name of the file on the file system
	 * @deprecated As of release 2.5, replaced by
	 * {@link #getFile().setFileName(String fileName)}
	 */
	@Deprecated
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return filename used by the original source
	 * @deprecated As of release 2.5, replaced by
	 * {@link #getFile().getOriginalName()}
	 */
	@Deprecated
	public String getOriginalFileName()
	{
		return originalFileName;
	}

	/**
	 * @param originalFileName filename used by the original source
	 * @deprecated As of release 2.5, replaced by
	 * {@link #getFile().setOriginalName(String originalName)}
	 */
	@Deprecated
	public void setOriginalFileName(String originalFileName)
	{
		this.originalFileName = originalFileName;
	}

	/**
	 * @return the mime type encoding of the file
	 * @deprecated As of release 2.5, replaced by
	 * {@link #getFile().getMimeType()}
	 */
	@Deprecated
	public String getMimeType()
	{
		return mimeType;
	}

	/**
	 * @param mimeType the mime type encoding of the file
	 * @deprecated As of release 2.5, replaced by
	 * {@link #getFile().setMimeType(String mimeType)}
	 */
	@Deprecated
	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}

}
