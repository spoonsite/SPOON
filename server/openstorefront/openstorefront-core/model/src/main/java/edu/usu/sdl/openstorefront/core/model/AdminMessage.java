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
package edu.usu.sdl.openstorefront.core.model;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.ValidValueType;
import edu.usu.sdl.openstorefront.core.entity.UserTypeCode;
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;
import edu.usu.sdl.openstorefront.validation.TextSanitizer;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Holds admin message information
 *
 * @author dshurtleff
 */
public class AdminMessage
{

	@APIDescription("List of usernames or email addresses (To Field)")
	@ConsumeField
	private List<String> usersToEmail = new ArrayList<>();

	@APIDescription("email addresses to carbon-copy")
	@ConsumeField
	private List<String> ccEmails = new ArrayList<>();

	@APIDescription("email addresses to blind-carbon-copy")
	@ConsumeField
	private List<String> bccEmails = new ArrayList<>();

	@APIDescription("Sends messages to only user of a specific type. Defaults: to all users  (Setting this overrides any specfic users specified.")
	@ValidValueType(value = {}, lookupClass = UserTypeCode.class)
	@ConsumeField
	private String userTypeCode;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	@Sanitize(TextSanitizer.class)
	@ConsumeField
	private String subject;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_ADMIN_MESSAGE)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String message;

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getUserTypeCode()
	{
		return userTypeCode;
	}

	public void setUserTypeCode(String userTypeCode)
	{
		this.userTypeCode = userTypeCode;
	}

	public List<String> getUsersToEmail()
	{
		return usersToEmail;
	}

	public void setUsersToEmail(List<String> usersToEmail)
	{
		this.usersToEmail = usersToEmail;
	}

	public List<String> getCcEmails()
	{
		return ccEmails;
	}

	public void setCcEmails(List<String> ccEmails)
	{
		this.ccEmails = ccEmails;
	}

	public List<String> getBccEmails()
	{
		return bccEmails;
	}

	public void setBccEmails(List<String> bccEmails)
	{
		this.bccEmails = bccEmails;
	}

}
