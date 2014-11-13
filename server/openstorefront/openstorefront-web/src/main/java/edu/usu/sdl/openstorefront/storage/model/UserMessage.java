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
import edu.usu.sdl.openstorefront.util.PK;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Holds Information about a notification message")
public class UserMessage
		extends BaseEntity
{

	@PK
	@NotNull
	private String userMessageId;

	@NotNull
	private String username;
	private String componentId;
	private String bodyOfMessage;

	public UserMessage()
	{
	}

	public String getUserMessageId()
	{
		return userMessageId;
	}

	public void setUserMessageId(String userMessageId)
	{
		this.userMessageId = userMessageId;
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

}
