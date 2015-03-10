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
import edu.usu.sdl.openstorefront.doc.ValidValueType;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.PK;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Holds Information about a notification message")
public class UserMessage
		extends BaseEntity
{

	public static final String SORT_FIELD_USERNAME = "username";

	@PK(generated = true)
	@NotNull
	private String messageId;

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_CODE)
	@ValidValueType(value = {}, lookupClass = UserMessageType.class)
	private String userMessageType;

	private String username;
	private String emailAddress;
	private String componentId;
	private String alertId;
	private String bodyOfMessage;
	private String subject;
	private String sentEmailAddress;

	@NotNull
	private Integer retryCount;

	public UserMessage()
	{
	}

	public String getUserMessageId()
	{
		return messageId;
	}

	public void setUserMessageId(String userMessageId)
	{
		this.messageId = userMessageId;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public void setComponentId(String componentId)
	{
		this.componentId = componentId;
	}

	public String getBodyOfMessage()
	{
		return bodyOfMessage;
	}

	public void setBodyOfMessage(String bodyOfMessage)
	{
		this.bodyOfMessage = bodyOfMessage;
	}

	public String getSentEmailAddress()
	{
		return sentEmailAddress;
	}

	public void setSentEmailAddress(String sentEmailAddress)
	{
		this.sentEmailAddress = sentEmailAddress;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public Integer getRetryCount()
	{
		return retryCount;
	}

	public void setRetryCount(Integer retryCount)
	{
		this.retryCount = retryCount;
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}

	public String getUserMessageType()
	{
		return userMessageType;
	}

	public void setUserMessageType(String userMessageType)
	{
		this.userMessageType = userMessageType;
	}

	public String getAlertId()
	{
		return alertId;
	}

	public void setAlertId(String alertId)
	{
		this.alertId = alertId;
	}

}
