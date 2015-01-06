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
package edu.usu.sdl.openstorefront.storage.model;

import edu.usu.sdl.openstorefront.doc.APIDescription;
import edu.usu.sdl.openstorefront.doc.ConsumeField;
import edu.usu.sdl.openstorefront.doc.ValidValueType;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.PK;
import edu.usu.sdl.openstorefront.util.ServiceUtil;
import edu.usu.sdl.openstorefront.validation.BasicHTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
public class Component
		extends BaseEntity<Component>
{

	@PK(generated = true)
	@NotNull
	private String componentId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_COMPONENT_NAME)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String name;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_COMPONENT_DESCRIPTION)
	@Sanitize(BasicHTMLSanitizer.class)
	@ConsumeField
	private String description;

	@ConsumeField
	@APIDescription("Id to a internal component that parent to this component")
	private String parentComponentId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GUID)
	@ConsumeField
	@APIDescription("External system id")
	private String guid;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_ORGANIZATION)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	@APIDescription("Component organization")
	private String organization;

	@ConsumeField
	@APIDescription("The component's release date")
	private Date releaseDate;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	@APIDescription("Version of the component")
	private String version;

	@NotNull
	@ValidValueType(
			{
				Component.APPROVAL_STATE_APPROVED, Component.APPROVAL_STATE_PENDING
			})
	@ConsumeField
	@APIDescription("Status of an approval")
	private String approvalState;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	@APIDescription("Who approved the component")
	private String approvedUser;

	@APIDescription("When the component was approved for the site")
	private Date approvedDts;

	@NotNull
	@APIDescription("Updated when any of the component's related data has changed.  Used for  watches.")
	private Date lastActivityDts;

	public static final String APPROVAL_STATE_APPROVED = "A";
	public static final String APPROVAL_STATE_PENDING = "P";

	public Component()
	{
	}

	@Override
	public int compareTo(Component o)
	{
		int value = super.compareTo(o);

		if (value == 0) {
			value = ServiceUtil.compareConsumeFields(this, o);
		}
		if (value == 0) {
			value = ServiceUtil.compareObjects(this.getApprovedUser(), o.getApprovedUser());
		}
		if (value == 0) {
			value = ServiceUtil.compareObjects(this.getApprovedDts(), o.getApprovedDts());
		}

		return value;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getParentComponentId()
	{
		return parentComponentId;
	}

	public void setParentComponentId(String parentComponentId)
	{
		this.parentComponentId = parentComponentId;
	}

	public String getGuid()
	{
		return guid;
	}

	public void setGuid(String guid)
	{
		this.guid = guid;
	}

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getApprovalState()
	{
		return approvalState;
	}

	public void setApprovalState(String approvalState)
	{
		this.approvalState = approvalState;
	}

	public String getApprovedUser()
	{
		return approvedUser;
	}

	public void setApprovedUser(String approvedUser)
	{
		this.approvedUser = approvedUser;
	}

	public Date getApprovedDts()
	{
		return approvedDts;
	}

	public void setApprovedDts(Date approvedDts)
	{
		this.approvedDts = approvedDts;
	}

	public Date getLastActivityDts()
	{
		return lastActivityDts;
	}

	public void setLastActivityDts(Date lastActivityDts)
	{
		this.lastActivityDts = lastActivityDts;
	}

	public Date getReleaseDate()
	{
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate)
	{
		this.releaseDate = releaseDate;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

}
