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
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.nio.file.Path;
import javax.persistence.CascadeType;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Use for training video/help videos")
public class SupportMedia
		extends StandardEntity<SupportMedia>
{

	@PK(generated = true)
	@NotNull
	private String supportMediaId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = MediaType.class)
	@ConsumeField
	@FK(MediaType.class)
	private String mediaType;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	private String title;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String description;

	@NotNull
	@ConsumeField
	@Min(0)
	@Max(Integer.MAX_VALUE)
	@APIDescription("Used to force order")
	private Integer orderNumber;

	@OneToOne(cascade = {CascadeType.ALL}, optional = false, orphanRemoval = true)
	@APIDescription("A local media file")
	private MediaFile file;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public SupportMedia()
	{
	}

	@Override
	public <T extends StandardEntity> void updateFields(T entity)
	{
		super.updateFields(entity);

		SupportMedia supportMedia = (SupportMedia) entity;

		this.setTitle(supportMedia.getTitle());
		this.setDescription(supportMedia.getDescription());
		this.setMediaType(supportMedia.getMediaType());
		this.setOrderNumber(supportMedia.getOrderNumber());

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

	public String getMediaType()
	{
		return mediaType;
	}

	public void setMediaType(String mediaType)
	{
		this.mediaType = mediaType;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Integer getOrderNumber()
	{
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber)
	{
		this.orderNumber = orderNumber;
	}

	public String getSupportMediaId()
	{
		return supportMediaId;
	}

	public void setSupportMediaId(String supportMediaId)
	{
		this.supportMediaId = supportMediaId;
	}

	public MediaFile getFile()
	{
		return file;
	}

	public void setFile(MediaFile file)
	{
		this.file = file;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

}
