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
package edu.usu.sdl.openstorefront.service.message;

import org.codemonkey.simplejavamail.Email;

/**
 *
 * @author dshurtleff
 */
public class TestMessageGenerator
		extends BaseMessageGenerator
{

	public TestMessageGenerator(MessageContext messageContext)
	{
		super(messageContext);
	}

	@Override
	protected String getSubject()
	{
		return "Email Check";
	}

	@Override
	protected String generateMessageInternal(Email email)
	{
		StringBuilder message = new StringBuilder();
		message.append("This is a test message used to confirm that emails can be sent to you as intended.");
		return message.toString();
	}

	@Override
	protected String getUnsubscribe()
	{
		return "If you are not the intended user of this message, please delete this message. ";
	}

}
