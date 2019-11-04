/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.model.FieldChangeModel;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import edu.usu.sdl.openstorefront.validation.BasicHTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.LinkSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jlaw
 * @author dshurtleff
 * @author kbair
 */
@APIDescription("Holds the media information for a component")
public class ComponentMedia
		extends BaseComponent<ComponentMedia>
		implements LoggableModel<ComponentMedia>, MediaModel
{

	@PK(generated = true)
	@NotNull
	private String componentMediaId;

	@ManyToOne(cascade = {CascadeType.ALL})
	@APIDescription("A local media file")
	private MediaFile file;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = MediaType.class)
	@ConsumeField
	@FK(MediaType.class)
	private String mediaTypeCode;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_URL)
	@ConsumeField
	@Sanitize(LinkSanitizer.class)
	@APIDescription("External Link")
	private String link;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@ConsumeField
	@Sanitize(BasicHTMLSanitizer.class)
	private String caption;

	@ConsumeField
	@APIDescription("Should this media be hidden in a display in the user interface?")
	@DefaultValue("false")
	private Boolean hideInDisplay;

	@ConsumeField
	@APIDescription("Is this media referred to inline in a description/etc?")
	@DefaultValue("false")
	private Boolean usedInline;

	@ConsumeField
	@APIDescription("Specific the media to be used as an icon; Only one active icon should exist.")
	@DefaultValue("false")
	private Boolean iconFlag;

	@APIDescription("For mapping to third party information")
	private String externalId;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public ComponentMedia()
	{
	}

	@Override
	public String uniqueKey()
	{
		String key = getMediaTypeCode()
				+ OpenStorefrontConstant.GENERAL_KEY_SEPARATOR
				+ getCaption()
				+ OpenStorefrontConstant.GENERAL_KEY_SEPARATOR
				+ ((getFile() != null) ? getFile().getMimeType() + OpenStorefrontConstant.GENERAL_KEY_SEPARATOR : "")
				+ getHideInDisplay()
				+ OpenStorefrontConstant.GENERAL_KEY_SEPARATOR
				+ getUsedInline()
				+ OpenStorefrontConstant.GENERAL_KEY_SEPARATOR
				+ getIconFlag()
				+ OpenStorefrontConstant.GENERAL_KEY_SEPARATOR
				+ (StringUtils.isNotBlank(getLink()) ? getLink() : (getFile() != null) ? getFile().getOriginalName() : "");
		return key;
	}

	@Override
	protected void customKeyClear()
	{
		setComponentMediaId(null);
	}

	@Override
	public void updateFields(StandardEntity entity)
	{
		ComponentMedia media = (ComponentMedia) entity;
		ServiceProxyFactory.getServiceProxy().getChangeLogService().findUpdateChanges(this, media);

		super.updateFields(entity);

		if (StringUtils.isNotBlank(media.getLink())) {
			this.setFile(null);
		} else {

			/**
			 * This change was added to prevent duplicate files being created
			 * from proxying the database.
			 */
			if (this.getFile() != null) {
				this.getFile().setFileName(media.getFile().getFileName());
				this.getFile().setOriginalName(media.getFile().getOriginalName());
				this.getFile().setMimeType(media.getFile().getMimeType());
				this.getFile().setFileType(media.getFile().getFileType());
			} else {
				this.setFile(media.getFile());
			}
		}
		this.setCaption(media.getCaption());
		this.setLink(media.getLink());
		this.setMediaTypeCode(media.getMediaTypeCode());
		this.setHideInDisplay(media.getHideInDisplay());
		this.setUsedInline(media.getUsedInline());
		this.setIconFlag(media.getIconFlag());
	}

	@Override
	public int customCompareTo(ComponentMedia o)
	{
		int value = ReflectionUtil.compareObjects(getFile(), o.getFile());
		return value;
	}

	/**
	 * Get the path to the media on disk.
	 *
	 * @return Path or null if this doesn't represent a disk resource
	 */
	public Path pathToMedia()
	{
		//Note: this may be ran from a proxy so don't use fields directly
		return (this.getFile() == null) ? null : this.getFile().path();
	}

	@Override
	public List<FieldChangeModel> findChanges(ComponentMedia updated)
	{
		Set<String> excludeFields = excludedChangeFields();
		excludeFields.add("componentMediaId");
		excludeFields.add("fileName");
		List<FieldChangeModel> changes = FieldChangeModel.allChangedFields(excludeFields, this, updated);
		return changes;
	}

	@Override
	public String addRemoveComment()
	{
		return TranslateUtil.translate(MediaType.class, getMediaTypeCode()) + " - " + (getLink() != null ? getLink() : (getFile() != null ? getFile().getOriginalName() : ""));
	}

	public String getComponentMediaId()
	{
		return componentMediaId;
	}

	public void setComponentMediaId(String componentMediaId)
	{
		this.componentMediaId = componentMediaId;
	}

	public String getMediaTypeCode()
	{
		return mediaTypeCode;
	}

	public void setMediaTypeCode(String mediaTypeCode)
	{
		this.mediaTypeCode = mediaTypeCode;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getCaption()
	{
		return caption;
	}

	public void setCaption(String caption)
	{
		this.caption = caption;
	}

	public Boolean getHideInDisplay()
	{
		return hideInDisplay;
	}

	public void setHideInDisplay(Boolean hideInDisplay)
	{
		this.hideInDisplay = hideInDisplay;
	}

	public Boolean getUsedInline()
	{
		return usedInline;
	}

	public void setUsedInline(Boolean usedInline)
	{
		this.usedInline = usedInline;
	}

	public Boolean getIconFlag()
	{
		return iconFlag;
	}

	public void setIconFlag(Boolean iconFlag)
	{
		this.iconFlag = iconFlag;
	}

	@Override
	public MediaFile getFile()
	{
		return file;
	}

	@Override
	public void setFile(MediaFile file)
	{
		this.file = file;
	}

	public String getExternalId()
	{
		return externalId;
	}

	public void setExternalId(String externalId)
	{
		this.externalId = externalId;
	}
}
