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

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.service.manager.LDAPManager;
import edu.usu.sdl.openstorefront.service.manager.model.LdapRecord;
import edu.usu.sdl.openstorefront.service.manager.resource.LdapClient;
import edu.usu.sdl.openstorefront.web.test.BaseTestCase;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class LDAPTest
		extends BaseTestCase
{

	@Override
	protected void runInternalTest()
	{
		LdapClient ldapClient = LDAPManager.getLdapClient();

		//We mainly checking connection
		String ldapUser = PropertiesManager.getValue(PropertiesManager.KEY_TOOLS_USER);
		if (StringUtils.isNotBlank(ldapUser)) {
			LdapRecord ldapRecord = ldapClient.findUser(ldapUser);
			if (ldapRecord != null) {
				results.append(StringProcessor.printObject(ldapRecord)).append("<br>");
			}
		} else {
			addResultsLines("LDAP user is not setup in this enviroment. See System properties.");
		}
	}

	@Override
	public String getDescription()
	{
		return "LDAP_Test";
	}

}
