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
package edu.usu.sdl.openstorefront.service.manager.model;

import edu.usu.sdl.openstorefront.common.manager.PropertiesManager;
import edu.usu.sdl.openstorefront.security.UserRecord;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * Holds LDAP Record information
 *
 * @author dshurtleff
 */
public class LdapRecord
		extends UserRecord
{

	private String groupName;
	private String name;
	private Map<String, String> attributeMap = new HashMap<>();

	public LdapRecord()
	{
	}

	public void populateFormAttributes()
	{
		setUsername(attributeMap.get(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_LDAP_MANAGER_ATTRIB_USERNAME, "sAMAccountName")));
		setEmail(attributeMap.get(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_LDAP_MANAGER_ATTRIB_EMAIL, "mail")));
		setPhone(attributeMap.get(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_LDAP_MANAGER_ATTRIB_PHONE, "telephonenumber")));
		setOrganization(attributeMap.get(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_LDAP_MANAGER_ATTRIB_ORGANIZATION, "company")));
		setGuid(attributeMap.get(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_LDAP_MANAGER_ATTRIB_GUID, "objectGUID")));
		String fullName = attributeMap.get(PropertiesManager.getInstance().getValue(PropertiesManager.KEY_LDAP_MANAGER_ATTRIB_FULLNAME, "name"));
		if (StringUtils.isNotBlank(fullName)) {
			String nameSplit[] = fullName.split(" ");
			if (nameSplit.length > 1) {
				setFirstName(nameSplit[0]);
				setLastName(nameSplit[1]);
			} else {
				setFirstName(fullName);
			}
		}
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Map<String, String> getAttributeMap()
	{
		return attributeMap;
	}

	public void setAttributeMap(Map<String, String> attributeMap)
	{
		this.attributeMap = attributeMap;
	}

}
