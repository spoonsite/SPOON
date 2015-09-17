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
package edu.usu.sdl.openstorefront.web.test.system;

import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.service.manager.LDAPManager;
import edu.usu.sdl.openstorefront.service.manager.model.LdapRecord;
import edu.usu.sdl.openstorefront.service.manager.resource.LdapClient;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;

/**
 *
 * @author dshurtleff
 */
public class LDAPTest
		extends BaseTestCase
{

	public LDAPTest()
	{
		this.description = "LDAP_Test";
	}

	@Override
	protected void runInternalTest()
	{
		LdapClient ldapClient = LDAPManager.getLdapClient();

		//We mainly checking connection
		LdapRecord ldapRecord = ldapClient.findUser("zStorefront");
		if (ldapRecord != null) {
			results.append(StringProcessor.printObject(ldapRecord)).append("<br>");
		}
	}

}
