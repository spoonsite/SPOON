/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.model;

/**
 *
 * @author dshurtleff
 */
public class PageModel
{

	private String page;
	private String roles[];

	public PageModel()
	{
	}

	public PageModel(String page)
	{
		this.page = page;
	}

	public PageModel(String page, String[] roles)
	{
		this.page = page;
		this.roles = roles;
	}

	public String getPage()
	{
		return page;
	}

	public void setPage(String page)
	{
		this.page = page;
	}

	public String[] getRoles()
	{
		return roles;
	}

	public void setRoles(String[] roles)
	{
		this.roles = roles;
	}

}
