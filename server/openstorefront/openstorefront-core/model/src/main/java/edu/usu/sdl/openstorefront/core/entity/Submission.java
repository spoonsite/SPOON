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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.DefaultFieldValue;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.Unique;
import edu.usu.sdl.openstorefront.validation.ComponentUniqueHandler;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author gfowler
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@APIDescription("This is the item that represents a submission object that has not yet been turned into a component.")
public class Submission
		extends StandardEntity<Submission>
		implements OrganizationModel
{

	private static final long serialVersionUID = 1L;

	public static final String FIELD_NAME = "name";
	public static final String FIELD_APPROVAL_STATE = "approvalState";
	public static final String FIELD_APPROVED_DTS = "approvedDts";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_LAST_ACTIVITY_DTS = "lastActivityDts";
	public static final String FIELD_RELEASE_DATE = "releaseDate";
	public static final String FIELD_DATA_SOURCE = "dataSource";
	public static final String FIELD_COMPONENT_ID = "componentId";

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@ConsumeField
	@APIDescription("External system guid")
	private String guid;

	@PK(generated = true)
	@NotNull
	private String componentId;

	@APIDescription("Updated when any of the component's related data has changed.  Used for watches.")
	private Date lastActivityDts;

	@APIDescription("Indicates a user submission and when it was submitted")
	private Date submittedDts;

	@APIDescription("Current owner/managing user")
	private String ownerUser;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_COMPONENT_NAME)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	@Unique(ComponentUniqueHandler.class)
	private String name;

	@ConsumeField
	@DefaultFieldValue(ComponentType.COMPONENT)
	@APIDescription("Type of listing")
	@FK(value = ComponentType.class, enforce = true)
	private String componentType;

	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_ORGANIZATION)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	@APIDescription("Component organization")
	@FK(value = Organization.class, referencedField = "name", softReference = true)
	private String organization;

	@ConsumeField
	@APIDescription("List of submitted images")
	private List<GeneralMedia> images;

	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_64K)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String description;

	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private List<AttributeCodePk> requiredAttributes;

	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private List<AttributeCodePk> suggestedAttributes;

	@ConsumeField
	@APIDescription("List of submitted images")
	private List<GeneralMedia> localFiles;

	@ConsumeField
	@APIDescription("List of submitted images")
	private List<GeneralMedia> url;

	@ConsumeField
	private List<String> tags;

	@ConsumeField
	private List<Contact> contacts;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public Submission()
	{
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	public static String getFieldName()
	{
		return FIELD_NAME;
	}

	public static String getFieldApprovalState()
	{
		return FIELD_APPROVAL_STATE;
	}

	public static String getFieldApprovedDts()
	{
		return FIELD_APPROVED_DTS;
	}

	public static String getFieldDescription()
	{
		return FIELD_DESCRIPTION;
	}

	public static String getFieldLastActivityDts()
	{
		return FIELD_LAST_ACTIVITY_DTS;
	}

	public static String getFieldReleaseDate()
	{
		return FIELD_RELEASE_DATE;
	}

	public static String getFieldDataSource()
	{
		return FIELD_DATA_SOURCE;
	}

	public static String getFieldComponentId()
	{
		return FIELD_COMPONENT_ID;
	}

	public String getGuid()
	{
		return guid;
	}

	public void setGuid(String guid)
	{
		this.guid = guid;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public Date getLastActivityDts()
	{
		return lastActivityDts;
	}

	public void setLastActivityDts(Date lastActivityDts)
	{
		this.lastActivityDts = lastActivityDts;
	}

	public Date getSubmittedDts()
	{
		return submittedDts;
	}

	public void setSubmittedDts(Date submittedDts)
	{
		this.submittedDts = submittedDts;
	}

	public String getOwnerUser()
	{
		return ownerUser;
	}

	public void setOwnerUser(String ownerUser)
	{
		this.ownerUser = ownerUser;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getComponentType()
	{
		return componentType;
	}

	public void setComponentType(String componentType)
	{
		this.componentType = componentType;
	}

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public List<GeneralMedia> getImages()
	{
		return images;
	}

	public void setImages(List<GeneralMedia> images)
	{
		this.images = images;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public List<AttributeCodePk> getRequiredAttributes()
	{
		return requiredAttributes;
	}

	public void setRequiredAttributes(List<AttributeCodePk> requiredAttributes)
	{
		this.requiredAttributes = requiredAttributes;
	}

	public List<AttributeCodePk> getSuggestedAttributes()
	{
		return suggestedAttributes;
	}

	public void setSuggestedAttributes(List<AttributeCodePk> suggestedAttributes)
	{
		this.suggestedAttributes = suggestedAttributes;
	}

	public List<GeneralMedia> getLocalFiles()
	{
		return localFiles;
	}

	public void setLocalFiles(List<GeneralMedia> localFiles)
	{
		this.localFiles = localFiles;
	}

	public List<GeneralMedia> getUrl()
	{
		return url;
	}

	public void setUrl(List<GeneralMedia> url)
	{
		this.url = url;
	}

	public List<String> getTags()
	{
		return tags;
	}

	public void setTags(List<String> tags)
	{
		this.tags = tags;
	}

	public List<Contact> getContacts()
	{
		return contacts;
	}

	public void setContacts(List<Contact> contacts)
	{
		this.contacts = contacts;
	}
}
