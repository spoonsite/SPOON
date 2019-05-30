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
import edu.usu.sdl.openstorefront.validation.HTMLSanitizer;
import edu.usu.sdl.openstorefront.validation.Sanitize;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Holds information for users contacting vendors.
 *
 * @author gfowler
 */
public class ContactVendorMessage
{

	@APIDescription("Email addresses to send message to (To Field)")
	@ConsumeField
    private String userToEmail = new String();
    
    @APIDescription("Email addresses that the message is coming from (From Field)")
	@ConsumeField
	private String userFromEmail = new String();

	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_ADMIN_MESSAGE)
	@Sanitize(HTMLSanitizer.class)
	@ConsumeField
	private String message;

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getUserToEmail()
	{
		return userToEmail;
	}

	public void setUserToEmail(String userToEmail)
	{
		this.userToEmail = userToEmail;
    }
    
    public String getUserFromEmail()
	{
		return userFromEmail;
	}

	public void setUserFromEmail(String userFromEmail)
	{
		this.userFromEmail = userFromEmail;
	}
}
