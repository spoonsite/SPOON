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
package edu.usu.sdl.openstorefront.service.manager.resource;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.security.UserRecord;
import edu.usu.sdl.openstorefront.service.manager.model.LdapRecord;
import edu.usu.sdl.openstorefront.service.manager.model.LdapSearch;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 *
 * @author dshurtleff
 */
public class LdapClient
{

	private static final Logger log = Logger.getLogger(LdapClient.class.getName());

	private String url;
	private String dnTemplate;
	private String authMechansism;
	private String saslRealm;
	private String bindDN;
	private String credentials;
	private String timeout = "15000";
	private String contextRoot;

	private DirContext ctx;

	public LdapClient()
	{
	}

	public List<LdapRecord> getAllOURecords()
	{
		List<LdapRecord> ldapRecords = new ArrayList<>();
		DirContext context = getConnection();
		try {
			SearchControls controls = new SearchControls();
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			controls.setReturningObjFlag(true);
			controls.setDerefLinkFlag(true);

			NamingEnumeration<SearchResult> results = context.search(contextRoot, "(&(objectClass=*)(ou=users))", controls);
			while (results.hasMore()) {
				SearchResult result = results.next();

				//NamingEnumeration<SearchResult> groupResults = context.search(result.getName() + "," + contextRoot, "(objectClass=*)", controls);
				LdapRecord ldapRecord = new LdapRecord();
				ldapRecord.setName(result.getName());
				ldapRecord.setGroupName(contextRoot);
				ldapRecords.add(ldapRecord);
			}

		} catch (NamingException ex) {
			System.out.println(ex);
			throw new OpenStorefrontRuntimeException("Unable to load records from root: " + contextRoot, "Check Root.", ex);
		} finally {
			disconnect();
		}
		return ldapRecords;
	}

	public LdapRecord findUser(String username)
	{
		LdapRecord record = null;

		List<String> usernames = new ArrayList<>();
		usernames.add(username);
		List<? extends UserRecord> records = findUsers(usernames);
		if (records.size() > 0) {
			record = (LdapRecord) records.get(0);
		}
		return record;
	}

	public List<? extends UserRecord> findUsers(List<String> usernames)
	{
		if (usernames.isEmpty()) {
			return new ArrayList<>();
		}
		StringBuilder filter = new StringBuilder();
		filter.append("(& "
				+ "    (objectClass=*)"
				+ "    (| ");
		for (String user : usernames) {
			filter.append("(")
					.append(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_LDAP_MANAGER_ATTRIB_USERNAME, "sAMAccountName"))
					.append("=")
					.append(user)
					.append(") ");
		}
		filter.append("    )")
				.append("   (! (userAccountControl:1.2.840.113556.1.4.803:=2)) ")
				.append(")");

		LdapSearch ldapSearch = new LdapSearch();
		ldapSearch.setSearchBase(contextRoot);
		ldapSearch.setFilter(filter.toString());

		return performSearch(ldapSearch);
	}

	public List<? extends UserRecord> performSearch(LdapSearch ldapSearch)
	{
		List<LdapRecord> ldapRecords = new ArrayList<>();
		DirContext context = getConnection();
		try {
			log.log(Level.FINEST, MessageFormat.format("Searching:  {0} \n Filter: {1}", new Object[]{ldapSearch.getSearchBase(), ldapSearch.getFilter()}));
			NamingEnumeration<SearchResult> groupResults = context.search(ldapSearch.getSearchBase(), ldapSearch.getFilter(), ldapSearch.getSearchControls());
			while (groupResults.hasMore()) {
				SearchResult groupResult = groupResults.next();
				LdapRecord record = new LdapRecord();
				record.setGroupName(ldapSearch.getSearchBase());
				record.setName(groupResult.getName());

				NamingEnumeration<Attribute> attributes = (NamingEnumeration<Attribute>) groupResult.getAttributes().getAll();
				while (attributes.hasMore()) {
					Attribute attribute = attributes.next();
					NamingEnumeration<Object> values = (NamingEnumeration<Object>) attribute.getAll();
					StringBuilder value = new StringBuilder();
					while (values.hasMore()) {
						Object valueObject = values.next();
						if (valueObject != null) {
							if (value.length() > 0) {
								value.append(" | ");
							}
							value.append(valueObject);
						}
					}
					record.getAttributeMap().put(attribute.getID(), value.toString());
				}
				record.populateFormAttributes();
				ldapRecords.add(record);
			}
		} catch (NamingException ex) {
			throw new OpenStorefrontRuntimeException("Unable to load records from root: " + contextRoot, "Check Root and connection.", ex);
		} finally {
			disconnect();
		}
		return ldapRecords;
	}

	private DirContext getConnection()
	{
		if (ctx == null) {
			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put("com.sun.jndi.ldap.connect.pool", "true");
			env.put("com.sun.jndi.ldap.connect.timeout", timeout);
			env.put(Context.PROVIDER_URL, url);
			env.put(Context.SECURITY_AUTHENTICATION, authMechansism);
			env.put(Context.SECURITY_PRINCIPAL, bindDN);
			env.put(Context.SECURITY_CREDENTIALS, credentials);
			env.put(Context.REFERRAL, "follow");

			try {
				log.log(Level.FINEST, MessageFormat.format("Connecting to: {0}", url));
				ctx = new InitialDirContext(env);
				log.log(Level.FINEST, MessageFormat.format("Success connecting to: {0}", url));
			} catch (NamingException ex) {
				throw new OpenStorefrontRuntimeException("Unable to connect to: " + url, "Check connection parameters.", ex);
			}
		}
		return ctx;
	}

	private void disconnect()
	{
		if (ctx != null) {
			try {
				ctx.close();
			} catch (NamingException ex) {
				log.log(Level.WARNING, "Error occured while trying to disconnect from LDAP: " + url, ex);
			}
			ctx = null;
		}
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getDnTemplate()
	{
		return dnTemplate;
	}

	public void setDnTemplate(String dnTemplate)
	{
		this.dnTemplate = dnTemplate;
	}

	public String getAuthMechansism()
	{
		return authMechansism;
	}

	public void setAuthMechansism(String authMechansism)
	{
		this.authMechansism = authMechansism;
	}

	public String getSaslRealm()
	{
		return saslRealm;
	}

	public void setSaslRealm(String saslRealm)
	{
		this.saslRealm = saslRealm;
	}

	public String getBindDN()
	{
		return bindDN;
	}

	public void setBindDN(String bindDN)
	{
		this.bindDN = bindDN;
	}

	public String getCredentials()
	{
		return credentials;
	}

	public void setCredentials(String credentials)
	{
		this.credentials = credentials;
	}

	public String getContextRoot()
	{
		return contextRoot;
	}

	public void setContextRoot(String contextRoot)
	{
		this.contextRoot = contextRoot;
	}

	public String getTimeout()
	{
		return timeout;
	}

	public void setTimeout(String timeout)
	{
		this.timeout = timeout;
	}

}
