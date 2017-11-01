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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.model.FieldChangeModel;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Section Media")
public class ContentSectionMedia
		extends StandardEntity<ContentSectionMedia>
		implements LoggableModel<ContentSectionMedia>
{

	@PK(generated = true)
	@NotNull
	private String contentSectionMediaId;

	@NotNull
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@FK(ContentSection.class)
	private String contentSectionId;

	/**
	 * @deprecated As of release 2.5, replaced by {@link #file}
	 */
	@Deprecated
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@APIDescription("Deprecated as of release 2.5, replaced by MediaFile")
	private String fileName;

	/**
	 * @deprecated As of release 2.5, replaced by {@link #file}
	 */
	@Deprecated
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@APIDescription("Deprecated as of release 2.5, replaced by MediaFile")
	private String originalName;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = MediaType.class)
	@ConsumeField
	@FK(MediaType.class)
	private String mediaTypeCode;

	/**
	 * @deprecated As of release 2.5, replaced by {@link #file}
	 */
	@Deprecated
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@APIDescription("Deprecated as of release 2.5, replaced by MediaFile")
	private String mimeType;

	@APIDescription("A local media file")
	private MediaFile file;
	
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String caption;

	@NotNull
	@ConsumeField
	private Boolean privateMedia;

	public ContentSectionMedia()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		ContentSectionMedia contentSectionMedia = (ContentSectionMedia) entity;
		ServiceProxyFactory.getServiceProxy().getChangeLogService().findUpdateChanges(this, contentSectionMedia);
		super.updateFields(entity);

		setContentSectionMediaId(contentSectionMedia.contentSectionMediaId);
		setMediaTypeCode(contentSectionMedia.getMediaTypeCode());
		setFileName(contentSectionMedia.getFileName());
		setMimeType(contentSectionMedia.getMimeType());
		setOriginalName(contentSectionMedia.getOriginalName());
		setPrivateMedia(contentSectionMedia.getPrivateMedia());
		setCaption(contentSectionMedia.getCaption());
		setFile(contentSectionMedia.getFile());
	}

	/**
	 * Get the path to the media on disk.
	 *
	 * @return Path or null if this doesn't represent a disk resource
	 */
	public Path pathToMedia()
	{ 
		//Note: this may be ran from a proxy so don't use fields directly
		return getFile().path();
	}

	@Override
	public List<FieldChangeModel> findChanges(ContentSectionMedia updated)
	{
		Set<String> excludeFields = excludedChangeFields();
		excludeFields.add("contentSectionMediaId");
		excludeFields.add("contentSectionId");
		excludeFields.add("fileName");
		List<FieldChangeModel> changes = FieldChangeModel.allChangedFields(excludeFields, this, updated);
		return changes;
	}

	@Override
	public void setChangeParent(ChangeLog changeLog)
	{
		changeLog.setParentEntity(ContentSection.class.getSimpleName());
		changeLog.setParentEntityId(getContentSectionId());
	}

	@Override
	public String addRemoveComment()
	{
		return TranslateUtil.translate(MediaType.class, getMediaTypeCode()) + " - " + (getFile() == null ? "" : getFile().getOriginalName());
	}

	public String getContentSectionMediaId()
	{
		return contentSectionMediaId;
	}

	public void setContentSectionMediaId(String contentSectionMediaId)
	{
		this.contentSectionMediaId = contentSectionMediaId;
	}

	public String getContentSectionId()
	{
		return contentSectionId;
	}

	public void setContentSectionId(String contentSectionId)
	{
		this.contentSectionId = contentSectionId;
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
	public String getOriginalName()
	{
		return originalName;
	}
	
	/**
	 * @param originalName filename used by the original source
	 * @deprecated As of release 2.5, replaced by
	 * {@link #getFile().setOriginalName(String originalName)}
	 */
	@Deprecated
	public void setOriginalName(String originalName)
	{
		this.originalName = originalName;
	}

	public String getMediaTypeCode()
	{
		return mediaTypeCode;
	}

	public void setMediaTypeCode(String mediaTypeCode)
	{
		this.mediaTypeCode = mediaTypeCode;
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

	public Boolean getPrivateMedia()
	{
		return privateMedia;
	}

	public void setPrivateMedia(Boolean privateMedia)
	{
		this.privateMedia = privateMedia;
	}

	public String getCaption()
	{
		return caption;
	}

	public void setCaption(String caption)
	{
		this.caption = caption;
	}

	public MediaFile getFile()
	{
		return file;
	}

	public void setFile(MediaFile file)
	{
		this.file = file;
	}

}
