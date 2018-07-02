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
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.FK;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw, dshurtleff
 */
@APIDescription("Holds user information and preferences")
public class UserProfile
		extends StandardEntity<UserProfile>
		implements OrganizationModel
{

	@PK
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	private String username;

	@ConsumeField
	@Sanitize(TextSanitizer.class)
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_FIRSTNAME)
	private String firstName;

	@ConsumeField
	@Sanitize(TextSanitizer.class)
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_LASTNAME)
	private String lastName;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_URL)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String email;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_PHONE)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String phone;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String positionTitle;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = UserTypeCode.class)
	@ConsumeField
	@FK(UserTypeCode.class)
	private String userTypeCode;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_ORGANIZATION)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	@FK(value = Organization.class, softReference = true, referencedField = "name")
	private String organization;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GUID)
	@ConsumeField
	@APIDescription("External system id used for tracking")
	private String externalGuid;

	//If there no external guid use our guid.
	@NotNull
	@APIDescription("System generated id used for tracking")
	private String internalGuid;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	@ConsumeField
	@APIDescription("Used to map user from external systems")
	private String externalUserId;

	@ConsumeField
	@APIDescription("Send user a message about recent changes")
	private Boolean notifyOfNew;

	private Date lastLoginDts;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_255)
	private String landingPage;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	private String searchResultView;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public UserProfile()
	{
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getUserTypeCode()
	{
		return userTypeCode;
	}

	public void setUserTypeCode(String userTypeCode)
	{
		this.userTypeCode = userTypeCode;
	}

	@Override
	public String getOrganization()
	{
		return organization;
	}

	@Override
	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public String getExternalGuid()
	{
		return externalGuid;
	}

	public void setExternalGuid(String externalGuid)
	{
		this.externalGuid = externalGuid;
	}

	public String getExternalUserId()
	{
		return externalUserId;
	}

	public void setExternalUserId(String externalUserId)
	{
		this.externalUserId = externalUserId;
	}

	public String getInternalGuid()
	{
		return internalGuid;
	}

	public void setInternalGuid(String internalGuid)
	{
		this.internalGuid = internalGuid;
	}

	public Boolean getNotifyOfNew()
	{
		return notifyOfNew;
	}

	public void setNotifyOfNew(Boolean notifyOfNew)
	{
		this.notifyOfNew = notifyOfNew;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public Date getLastLoginDts()
	{
		return lastLoginDts;
	}

	public void setLastLoginDts(Date lastLoginDts)
	{
		this.lastLoginDts = lastLoginDts;
	}

	public String getLandingPage()
	{
		return landingPage;
	}

	public void setLandingPage(String landingPage)
	{
		this.landingPage = landingPage;
	}

	public String getSearchResultView()
	{
		return searchResultView;
	}

	public void setSearchResultView(String searchResultView)
	{
		this.searchResultView = searchResultView;
	}

	public String getPositionTitle()
	{
		return positionTitle;
	}

	public void setPositionTitle(String positionTitle)
	{
		this.positionTitle = positionTitle;
	}

}
