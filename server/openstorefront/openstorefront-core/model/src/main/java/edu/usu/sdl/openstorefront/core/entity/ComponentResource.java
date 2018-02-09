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
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.LinkSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jlaw
 * @author kbair
 */
@APIDescription("Resource for a component")
public class ComponentResource
		extends BaseComponent<ComponentResource>
		implements LoggableModel<ComponentResource>, MediaModel
{

	@PK(generated = true)
	@NotNull
	private String resourceId;

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

	/**
	 * @deprecated As of release 2.5, replaced by {@link #file}
	 */
	@Deprecated
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@APIDescription("Deprecated as of release 2.5, replaced by MediaFile")
	private String mimeType;

	@APIDescription("A local resource file")
	private MediaFile file;

	@NotNull
	@ConsumeField
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = ResourceType.class)
	@FK(ResourceType.class)
	private String resourceType;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_URL)
	@Sanitize(LinkSanitizer.class)
	@APIDescription("For an external resource")
	private String link;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_16K)
	@Sanitize(HTMLSanitizer.class)
	private String description;

	@ConsumeField
	@APIDescription("This is used to indentify if a resource requires a login or CAC")
	private Boolean restricted;

	public ComponentResource()
	{
	}

	@Override
	public String uniqueKey()
	{
		String key = getResourceType()
				+ OpenStorefrontConstant.GENERAL_KEY_SEPARATOR
				+ getDescription()
				+ OpenStorefrontConstant.GENERAL_KEY_SEPARATOR
				+ ((getFile() != null) ? getFile().getMimeType() + OpenStorefrontConstant.GENERAL_KEY_SEPARATOR : "")
				+ getRestricted()
				+ OpenStorefrontConstant.GENERAL_KEY_SEPARATOR
				+ (StringUtils.isNotBlank(getLink()) ? getLink() : (getFile() != null) ? getFile().getOriginalName() : "");
		return key;
	}

	@Override
	protected void customKeyClear()
	{
		setResourceId(null);
	}

	@Override
	public void updateFields(StandardEntity entity)
	{
		ComponentResource resource = (ComponentResource) entity;
		ServiceProxyFactory.getServiceProxy().getChangeLogService().findUpdateChanges(this, resource);
		super.updateFields(entity);

		if (StringUtils.isNotBlank(resource.getLink())) {
			this.setFileName(null);
			this.setOriginalName(null);
			this.setMimeType(null);
			this.setFile(null);
		} else {
			this.setFileName(resource.getFileName());
			this.setOriginalName(resource.getOriginalName());
			this.setMimeType(resource.getMimeType());
			this.setFile(resource.getFile());
		}
		this.setDescription(resource.getDescription());
		this.setLink(resource.getLink());
		this.setResourceType(resource.getResourceType());
		this.setRestricted(resource.getRestricted());

	}

	@Override
	public int customCompareTo(ComponentResource o)
	{
		int value = ReflectionUtil.compareObjects(getFile(), o.getFile());
		return value;
	}

	/**
	 * Get the path to the resource on disk.
	 *
	 * @return Resource or null if this doesn't represent a disk resource
	 */
	public Path pathToResource()
	{
		//Note: this may be ran from a proxy so don't use variable directly
		return (this.getFile() == null) ? null : this.getFile().path();
	}

	@Override
	public List<FieldChangeModel> findChanges(ComponentResource updated)
	{
		Set<String> excludeFields = excludedChangeFields();
		excludeFields.add("resourceId");
		excludeFields.add("fileName");
		List<FieldChangeModel> changes = FieldChangeModel.allChangedFields(excludeFields, this, updated);
		return changes;
	}

	@Override
	public String addRemoveComment()
	{
		return TranslateUtil.translate(ResourceType.class, getResourceType()) + " - " + (getLink() != null ? getLink() : (getFile() != null ? getFile().getOriginalName() : ""));
	}

	public String getResourceId()
	{
		return resourceId;
	}

	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getResourceType()
	{
		return resourceType;
	}

	public void setResourceType(String resourceType)
	{
		this.resourceType = resourceType;
	}

	public Boolean getRestricted()
	{
		return restricted;
	}

	public void setRestricted(Boolean restricted)
	{
		this.restricted = restricted;
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

	public MediaFile getFile()
	{
		return file;
	}

	public void setFile(MediaFile file)
	{
		this.file = file;
	}
}
