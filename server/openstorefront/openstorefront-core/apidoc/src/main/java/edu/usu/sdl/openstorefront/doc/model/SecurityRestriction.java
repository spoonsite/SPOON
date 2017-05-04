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
package edu.usu.sdl.openstorefront.doc.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class SecurityRestriction
{
	private List<String> permissions = new ArrayList<>();
	private List<String> roles = new ArrayList<>();
	private String logicOperation;
	private String specialCheck;

	public SecurityRestriction()
	{
	}

	public List<String> getPermissions()
	{
		return permissions;
	}

	public void setPermissions(List<String> permissions)
	{
		this.permissions = permissions;
	}

	public List<String> getRoles()
	{
		return roles;
	}

	public void setRoles(List<String> roles)
	{
		this.roles = roles;
	}

	public String getLogicOperation()
	{
		return logicOperation;
	}

	public void setLogicOperation(String logicOperation)
	{
		this.logicOperation = logicOperation;
	}

	public String getSpecialCheck()
	{
		return specialCheck;
	}

	public void setSpecialCheck(String specialCheck)
	{
		this.specialCheck = specialCheck;
	}
	
}
