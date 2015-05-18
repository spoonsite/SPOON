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

import javax.naming.directory.SearchControls;

/**
 *
 * @author dshurtleff
 */
public class LdapSearch
{

	private String searchBase;
	private String filter = "(objectClass=*)";
	private SearchControls searchControls;

	public LdapSearch()
	{
		searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		searchControls.setReturningObjFlag(true);
		searchControls.setDerefLinkFlag(true);
		searchControls.setCountLimit(1000);
	}

	public String getSearchBase()
	{
		return searchBase;
	}

	public void setSearchBase(String searchBase)
	{
		this.searchBase = searchBase;
	}

	public String getFilter()
	{
		return filter;
	}

	public void setFilter(String filter)
	{
		this.filter = filter;
	}

	public SearchControls getSearchControls()
	{
		return searchControls;
	}

	public void setSearchControls(SearchControls searchControls)
	{
		this.searchControls = searchControls;
	}

}
