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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.ConsumeField;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Captures user feedback")
public class FeedbackTicket
		extends StandardEntity<FeedbackTicket>
{

	@PK(generated = true)
	private String feedbackId;

	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_GENERAL_TEXT)
	private String summary;

	@ConsumeField
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_4K)
	private String description;
	
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_FULLNAME)
	private String fullname;
	
	@ConsumeField
	private String email;
	
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_ORGANIZATION)
	private String organization;
	
	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_PHONE)
	private String phone;

	private String username;
	private String firstname;
	private String lastname;

	@ConsumeField
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_60)
	private String ticketType;

	private String externalId;

	@ConsumeField
	@Embedded
	private WebInformation webInformation;

	public FeedbackTicket()
	{
	}

	public String fullSubject()
	{
		return this.getTicketType() + " - " + this.getSummary();
	}

	public String fullDescription()
	{
		if (getWebInformation() == null) {
			setWebInformation(new WebInformation());
		}

		/**
		 * Tab characters were added before each newline character to prevent
		 * Windows Outlook from removing extra line breaks in email message
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("*Reported Issue Type*:  ").append(this.getTicketType()).append("\t\n");
		sb.append("*Reporter Username*:  ").append(this.getUsername()).append("\t\n");
		sb.append("*Reporter Firstname*:  ").append(StringProcessor.blankIfNull(this.getFirstname())).append("\t\n");
		sb.append("*Reporter Lastname*:  ").append(StringProcessor.blankIfNull(this.getLastname())).append("\t\n");
		sb.append("*Reporter Fullname*:  ").append(StringProcessor.blankIfNull(this.getFullname())).append("\t\n");
		sb.append("*Reporter Organization*:  ").append(StringProcessor.blankIfNull(this.getOrganization())).append("\t\n");
		sb.append("*Reporter Email*:  ").append(StringProcessor.blankIfNull(this.getEmail())).append("\t\n");
		sb.append("*Reporter Phone*:  ").append(StringProcessor.blankIfNull(this.getPhone())).append("\t\n\n");
		sb.append("*Web Location*:  ").append(StringProcessor.blankIfNull(this.getWebInformation().getLocation())).append("\t\n");
		sb.append("*Web User-agent*:  ").append(StringProcessor.blankIfNull(this.getWebInformation().getUserAgent())).append("\t\n");
		sb.append("*Web Referrer*:  ").append(StringProcessor.blankIfNull(this.getWebInformation().getReferrer())).append("\t\n");
		sb.append("*Web Screen Resolution*:  ").append(StringProcessor.blankIfNull(this.getWebInformation().getScreenResolution())).append("\n");
		sb.append("*Application Version*:  ").append(PropertiesManager.getApplicationVersion()).append("\t\n");
		sb.append("\t\n");
		sb.append(this.getDescription());
		return sb.toString();
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
	
	public String getFullname()
	{
		return fullname;
	}
	
	public void setFullname(String fullname)
	{
		this.fullname = fullname;
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

	public String getFeedbackId()
	{
		return feedbackId;
	}

	public void setFeedbackId(String feedbackId)
	{
		this.feedbackId = feedbackId;
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
