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
package edu.usu.sdl.openstorefront.service.manager;

import edu.usu.sdl.openstorefront.common.manager.Initializable;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.service.manager.resource.LdapClient;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * Manages LDAP Clients
 *
 * @author dshurtleff
 */
public class LDAPManager
		implements Initializable
{

	private static final Logger log = Logger.getLogger(LDAPManager.class.getName());

	private static AtomicBoolean started = new AtomicBoolean(false);
	
	public static void init()
	{
		//Nothing to do at the moment			
	}

	public static void cleanup()
	{
		//Nothing to do at the moment		
	}

	public static LdapClient getLdapClient()
	{
		LdapClient ldapClient = new LdapClient();

		ldapClient.setUrl(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_LDAP_MANAGER_URL));
		ldapClient.setDnTemplate(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_LDAP_MANAGER_USER_DN_TEMPLATE));
		ldapClient.setSaslRealm(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_LDAP_MANAGER_SASL_REALM));
		ldapClient.setAuthMechansism(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_LDAP_MANAGER_AUTHM, "SIMPLE"));
		ldapClient.setBindDN(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_LDAP_MANAGER_BIND_DN));
		ldapClient.setCredentials(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_LDAP_MANAGER_CREDENTIALS));
		ldapClient.setContextRoot(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_LDAP_MANAGER_CONTEXT_ROOT));

		return ldapClient;
	}

	@Override
	public void initialize()
	{
		LDAPManager.init();
		started.set(true);	
	}

	@Override
	public void shutdown()
	{
		LDAPManager.cleanup();
		started.set(false);
	}
	
	@Override
	public boolean isStarted()
	{
		return started.get();
	}	

}
