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
package edu.usu.sdl.describe.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author dshurtleff
 */
@Root(strict = false)
public class Person
{
	@Element(required = false)
	private String name;
	
	@Element(required = false)
	private String surname;
	
	@Element(required = false)
	private String phone;
	
	@Element(required = false)
	private String email;
	
	@Element(required = false)
	private String affiliation;

	public Person()
	{
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getSurname()
	{
		return surname;
	}

	public void setSurname(String surname)
	{
		this.surname = surname;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getAffiliation()
	{
		return affiliation;
	}

	public void setAffiliation(String affiliation)
	{
		this.affiliation = affiliation;
	}

}
