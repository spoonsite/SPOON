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
package edu.usu.sdl.openstorefront.security;

import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * This hold the Header info used in the auth
 *
 * @author dshurtleff
 */
public class HeaderAuthToken
		implements AuthenticationToken
{

	private String username;
	private String firstname;
	private String lastname;
	private String email;
	private String phone;
	private String group;
	private String guid;
	private String organization;
	private String adminGroupName;
	private HttpServletRequest request;
	private UserContext userContext;
	private Set<String> groups = new HashSet<>();

	public HeaderAuthToken()
	{
	}

	@Override
	public Object getPrincipal()
	{
		return username;
	}

	@Override
	public Object getCredentials()
	{
		//There really isn't any
		return username;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getFirstname()
	{
		return firstname;
	}

	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

	public String getLastname()
	{
		return lastname;
	}

	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getGroup()
	{
		return group;
	}

	public void setGroup(String group)
	{
		this.group = group;
	}

	public String getGuid()
	{
		return guid;
	}

	public void setGuid(String guid)
	{
		this.guid = guid;
	}

	public String getOrganization()
	{
		return organization;
	}

	public void setOrganization(String organization)
	{
		this.organization = organization;
	}

	public String getAdminGroupName()
	{
		return adminGroupName;
	}

	public void setAdminGroupName(String adminGroupName)
	{
		this.adminGroupName = adminGroupName;
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public UserContext getUserContext()
	{
		return userContext;
	}

	public void setUserContext(UserContext userContext)
	{
		this.userContext = userContext;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public Set<String> getGroups()
	{
		return groups;
	}

	public void setGroups(Set<String> groups)
	{
		this.groups = groups;
	}

}
