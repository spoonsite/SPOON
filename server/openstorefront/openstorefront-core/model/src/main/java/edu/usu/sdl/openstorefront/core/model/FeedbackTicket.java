/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
public class FeedbackTicket
{

	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String summary;

	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	private String description;

	private String username;
	private String firstname;
	private String lastname;
	private String email;
	private String phone;
	private String organization;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_60)
	private String ticketType;

	@ConsumeField
	private WebInformation webInformation = new WebInformation();

	public FeedbackTicket()
	{
	}

	public WebInformation getWebInformation()
	{
		return webInformation;
	}

	public void setWebInformation(WebInformation webInformation)
	{
		this.webInformation = webInformation;
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getFirstname()
	{
		return firstname;
	}

	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

	public String getLastname()
	{
		return lastname;
	}

	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public String getTicketType()
	{
		return ticketType;
	}

	public void setTicketType(String ticketType)
	{
		this.ticketType = ticketType;
	}

}
