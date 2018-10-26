/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.spoon.aerospace.importor.model;

/**
 *
 * @author dshurtleff
 */
public abstract class BaseFeature
{

	protected String name;
	protected String description;
	protected String valueDescription;
	protected String type;
	protected String datayType;

	public BaseFeature()
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

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getValueDescription()
	{
		return valueDescription;
	}

	public void setValueDescription(String valueDescription)
	{
		this.valueDescription = valueDescription;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getDatayType()
	{
		return datayType;
	}

	public void setDatayType(String datayType)
	{
		this.datayType = datayType;
	}

}
