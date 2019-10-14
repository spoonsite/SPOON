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

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.util.MediaFileType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author kbair
 */
public class MediaFile
		extends BaseEntity<MediaFile>
{

	private static final long serialVersionUID = 1L;

	@PK(generated = true)
	@NotNull
	private String mediaFileId;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@APIDescription("Stored filename")
	private String fileName;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@APIDescription("Name of the file uploaded")
	private String originalName;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@APIDescription("Type of the file uploaded")
	private String mimeType;

	private MediaFileType fileType;

	/**
	 * Get the path to the file on disk.
	 *
	 * @return Path or null if this doesn't represent a disk resource
	 */
	public Path path()
	{
		//Note: this may be ran from a proxy so don't use fields directly
		Path path = null;
		if (StringUtils.isNotBlank(getFileName())) {
			path = Paths.get(this.getFileType().getPath() + "/" + getFileName());
		}
		return path;
	}

	/**
	 * This copy the file and create a new record
	 *
	 * @return a new Media file (Note it's not persisted to the db)
	 */
	public MediaFile copy()
	{
		MediaFile newMedia = new MediaFile();

		newMedia.setMediaFileId(StringProcessor.uniqueId());
		newMedia.setMimeType(this.getMimeType());
		newMedia.setFileName(StringProcessor.uniqueId() + OpenStorefrontConstant.getFileExtensionForMime(this.getMimeType()));
		newMedia.setOriginalName(this.getOriginalName());

		newMedia.setFileType(MediaFileType.MEDIA);

		Path newPath = Paths.get(MediaFileType.MEDIA.getPath(), newMedia.getFileName());
		try {
			Files.copy(this.path(), newPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException(
					"Unable to copy media.",
					"Old Path: " + this.path()
					+ " New Path: " + newPath,
					ex
			);
		}

		return newMedia;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 13 * hash + (this.fileName != null ? this.fileName.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		return this.getFileName().equals(((MediaFile) obj).getFileName());
	}

	public String getMediaFileId()
	{
		return mediaFileId;
	}

	public void setMediaFileId(String mediaFileId)
	{
		this.mediaFileId = mediaFileId;
	}

	/**
	 *
	 * @return name of the file on the file system
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 *
	 * @param fileName name of the file on the file system
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 *
	 * @return filename used by the original source
	 */
	public String getOriginalName()
	{
		return originalName;
	}

	/**
	 *
	 * @param originalName filename used by the original source
	 */
	public void setOriginalName(String originalName)
	{
		this.originalName = originalName;
	}

	/**
	 *
	 * @return the mime type encoding of the file
	 */
	public String getMimeType()
	{
		return mimeType;
	}

	/**
	 *
	 * @param mimeType the mime type encoding of the file
	 */
	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}

	public MediaFileType getFileType()
	{
		return fileType;
	}

	public void setFileType(MediaFileType fileType)
	{
		this.fileType = fileType;
	}
}
