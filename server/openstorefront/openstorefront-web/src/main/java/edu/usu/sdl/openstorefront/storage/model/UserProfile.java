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

import edu.usu.sdl.openstorefront.doc.ConsumeField;
import edu.usu.sdl.openstorefront.doc.ValidValueType;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.PK;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author jlaw
 */
public class UserProfile
		extends BaseEntity
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

	@Pattern(regexp = OpenStorefrontConstant.EMAIL_PATTERN)
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_URL)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String email;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = UserTypeCode.class)
	@ConsumeField
	private String userTypeCode;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_ORGANIZATION)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String organization;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_GUID)
	@ConsumeField
	private String externalGuid;

	//If there no external guid use our guid.
	@NotNull
	private String internalGuid;

	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	@ConsumeField
	private String externalUserId;

	@ConsumeField
	private Boolean notifyOfNew;

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

	public String getOrganization()
	{
		return organization;
	}

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

}
