/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.test.user;

import edu.usu.sdl.openstorefront.core.model.AdminMessage;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ccummings
 */
public class AdminMessageTest extends BaseTestCase
{

	@Override
	protected void runInternalTest()
	{
		List<String> emails = new ArrayList();
		emails.add(getSystemEmail());

		results.append("Creating admin message...<br>");
		AdminMessage adminMessage = new AdminMessage();
		adminMessage.setUsersToEmail(emails);
		adminMessage.setCcEmails(emails);
		adminMessage.setBccEmails(emails);
		adminMessage.setSubject("Admin Message Test");
		adminMessage.setMessage("**You have received this message due to automated admin message testing.**");

		service.getUserService().sendAdminMessage(adminMessage);
		results.append("Admin message successfully created and sent<br><br>");
	}

	@Override
	public String getDescription()
	{
		return "Admin Message Test";
	}
}
